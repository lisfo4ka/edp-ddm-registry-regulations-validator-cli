package com.epam.digital.data.platform.registry.regulation.validation;

import com.epam.digital.data.platform.registry.regulation.validation.model.BpTrembitaConfiguration;
import com.epam.digital.data.platform.registry.regulation.validation.model.ValidationError;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Sets;
import java.io.File;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.SneakyThrows;

public class BpTrembitaRulesValidator implements RegulationValidator<File> {

  private final ObjectMapper objectMapper;

  public BpTrembitaRulesValidator(ObjectMapper objectMapper) {
    this.objectMapper = objectMapper;
  }

  @Override
  public Set<ValidationError> validate(File regulationFile) {
    var bpTrembitaConfiguration = readBpTrembitaConfiguration(regulationFile);
    var processDefinitions = bpTrembitaConfiguration.getTrembita().getProcessDefinitions();
    var duplicates = findDuplicates(processDefinitions);

    if (duplicates.isEmpty()) {
      return Collections.emptySet();
    }

    var joinedDuplicatedIds = duplicates.stream()
        .map(d -> String.format("'%s'", d.getProcessDefinitionId()))
        .collect(Collectors.joining(","));

    var validationError = ValidationError.builder()
        .regulationFileName(bpTrembitaConfiguration.getRegulationFileName())
        .errorMessage(String.format("Duplicated process definitions found: %s", joinedDuplicatedIds))
        .build();

    return Collections.singleton(validationError);
  }

  private Set<BpTrembitaConfiguration.ProcessDefinition> findDuplicates(List<BpTrembitaConfiguration.ProcessDefinition> processDefinitions) {
    var duplicates = Sets.newHashSet();
    return processDefinitions.stream()
        .filter(n -> !duplicates.add(n))
        .collect(Collectors.toSet());
  }

  @SneakyThrows
  private BpTrembitaConfiguration readBpTrembitaConfiguration(File regulationFile) {
    var bpTrembitaConfiguration = objectMapper.readValue(regulationFile, BpTrembitaConfiguration.class);
    bpTrembitaConfiguration.setRegulationFileName(regulationFile.getName());
    return bpTrembitaConfiguration;
  }
}
