/*
 * Copyright 2023 EPAM Systems.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.epam.digital.data.platform.registry.regulation.validation.cli.validator.form;

import com.epam.digital.data.platform.liquibase.extension.change.core.DdmCreateSearchConditionChange;
import com.epam.digital.data.platform.liquibase.extension.change.core.DdmDropSearchConditionChange;
import com.epam.digital.data.platform.registry.regulation.validation.cli.model.RegulationFiles;
import com.epam.digital.data.platform.registry.regulation.validation.cli.utils.ChangelogParser;
import com.epam.digital.data.platform.registry.regulation.validation.cli.validator.RegulationValidator;
import com.epam.digital.data.platform.registry.regulation.validation.cli.validator.ValidationContext;
import com.epam.digital.data.platform.registry.regulation.validation.cli.validator.ValidationError;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Sets;
import com.google.common.collect.Streams;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import liquibase.change.Change;
import liquibase.changelog.DatabaseChangeLog;
import liquibase.exception.LiquibaseException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

@Slf4j
@RequiredArgsConstructor
public class FormToSearchConditionExistenceValidator implements
    RegulationValidator<RegulationFiles> {

  private final ObjectMapper fileObjectMapper;

  @Override
  public Set<ValidationError> validate(RegulationFiles regulation, ValidationContext context) {
    Set<ValidationError> errors = Sets.newHashSet();
    List<Change> changes = new ArrayList<>();

    var liquibaseFiles = regulation.getLiquibaseFiles();
    if (!liquibaseFiles.isEmpty()) {
      var mainLiquibase = liquibaseFiles.iterator().next();
      try {
        changes = getAllChanges(mainLiquibase);
      } catch (LiquibaseException e) {
        return Collections.singleton(
            ValidationError.of(context.getRegulationFileType(), mainLiquibase,
                "File processing failure", e)
        );
      }
    }

    var searchConditions = getSearchConditions(changes);

    for (File regulationFile : regulation.getFormFiles()) {
      try {
        validateDataUrl(regulationFile, context, searchConditions, errors);
      } catch (IOException ex) {
        return Collections.singleton(
            ValidationError.of(context.getRegulationFileType(), regulationFile,
                "File processing failure", ex)
        );
      }
    }
    return errors;
  }

  private void validateDataUrl(File regulationFile, ValidationContext context,
      Map<String, Change> searchConditions, Set<ValidationError> errors) throws IOException {
    var form = fileObjectMapper.readTree(regulationFile);
    var urls = getDataUrlsFromForm(form);
    for (var url : urls) {
      if (!url.startsWith("/api/data-factory")) {
        log.warn("Unable to validate not data-factory url: {}, ", url);
        continue;
      }
      var searchConditionName = getSearchConditionNameFromURL(url);
      var change = searchConditions.get(searchConditionName);
      if (Objects.isNull(change) || change instanceof DdmDropSearchConditionChange) {
        errors.add(ValidationError.of(context.getRegulationFileType(),
            regulationFile,
            "Next search condition doesn't exist under data-model files: "
                + searchConditionName));
      }
    }
  }

  private List<String> getDataUrlsFromForm(JsonNode form) {
    return Streams.stream(form.findValue("components").elements())
        .filter(component -> "selectLatest".equals(component.get("type").asText()))
        .map(component -> component.findValue("url"))
        .filter(url -> Objects.nonNull(url) && !url.asText().isBlank())
        .map(JsonNode::asText)
        .collect(Collectors.toList());
  }

  private Map<String, Change> getSearchConditions(List<Change> changes) {
    return changes.stream()
        .filter(c -> DdmCreateSearchConditionChange.class.isAssignableFrom(c.getClass())
            || DdmDropSearchConditionChange.class.isAssignableFrom(c.getClass()))
        .collect(Collectors.toMap(
            this::getChangeName,
            c -> c,
            (first, second) -> second
        ));
  }

  private DatabaseChangeLog getDatabaseChangeLog(File regulationFile) throws LiquibaseException {
    return ChangelogParser.parseChangeLog(regulationFile);
  }

  private List<Change> getAllChanges(File mainLiquibase) throws LiquibaseException {

    return getDatabaseChangeLog(mainLiquibase).getChangeSets()
        .stream()
        .flatMap(x -> x.getChanges().stream())
        .collect(Collectors.toList());
  }

  private String getChangeName(Change change) {
    if (change instanceof DdmCreateSearchConditionChange) {
      return ((DdmCreateSearchConditionChange) change).getName();
    } else if (change instanceof DdmDropSearchConditionChange) {
      return ((DdmDropSearchConditionChange) change).getName();
    }
    return "";
  }

  private String getSearchConditionNameFromURL(String url) {
    return StringUtils.substringAfterLast(url, "/").replace("-", "_");
  }
}
