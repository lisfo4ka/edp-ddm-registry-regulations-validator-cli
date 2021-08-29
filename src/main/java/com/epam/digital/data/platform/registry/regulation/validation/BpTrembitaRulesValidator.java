package com.epam.digital.data.platform.registry.regulation.validation;

import com.epam.digital.data.platform.registry.regulation.validation.model.BpTrembitaConfiguration;
import com.epam.digital.data.platform.registry.regulation.validation.model.ValidationError;
import com.google.common.collect.Sets;
import java.io.File;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class BpTrembitaRulesValidator implements RegulationValidator<File> {

  private static final String DUPLICATES_ERROR_MSG_FORMAT = "Duplicated process definitions found: %s";

  private final RegulationConfigurationLoader configurationLoader;

  public BpTrembitaRulesValidator(RegulationConfigurationLoader configurationLoader) {
    this.configurationLoader = configurationLoader;
  }

  @Override
  public Set<ValidationError> validate(File regulationFile) {
    var bpTrembitaConfiguration = this.configurationLoader.load(regulationFile, BpTrembitaConfiguration.class);
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
        .errorMessage(String.format(DUPLICATES_ERROR_MSG_FORMAT, joinedDuplicatedIds))
        .build();

    return Collections.singleton(validationError);
  }

  private Set<BpTrembitaConfiguration.ProcessDefinition> findDuplicates(List<BpTrembitaConfiguration.ProcessDefinition> processDefinitions) {
    var duplicates = Sets.newHashSet();
    return processDefinitions.stream()
        .filter(n -> !duplicates.add(n))
        .collect(Collectors.toSet());
  }
}
