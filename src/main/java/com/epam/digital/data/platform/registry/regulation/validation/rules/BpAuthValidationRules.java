package com.epam.digital.data.platform.registry.regulation.validation.rules;

import com.deliveredtechnologies.rulebook.NameValueReferableTypeConvertibleMap;
import com.deliveredtechnologies.rulebook.lang.RuleBuilder;
import com.deliveredtechnologies.rulebook.model.Rule;
import com.deliveredtechnologies.rulebook.model.rulechain.cor.CoRRuleBook;
import com.epam.digital.data.platform.registry.regulation.validation.model.BpAuthConfiguration;
import com.epam.digital.data.platform.registry.regulation.validation.model.BpAuthConfiguration.ProcessDefinition;
import com.epam.digital.data.platform.registry.regulation.validation.model.ValidationError;
import com.epam.digital.data.platform.registry.regulation.validation.model.ValidationErrors;
import com.google.common.collect.Sets;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class BpAuthValidationRules extends CoRRuleBook<ValidationErrors> {

  @Override
  public void defineRules() {
    addRule(uniqueProcessDefinitionIdsRule());
  }

  private Rule<BpAuthConfiguration, ValidationErrors> uniqueProcessDefinitionIdsRule() {
    return RuleBuilder.create()
        .withFactType(BpAuthConfiguration.class)
        .withResultType(ValidationErrors.class)
        .when(value -> duplicatedProcessDefinitionIdsExist(value))
        .then((value, result) -> result.getValue().add(duplicatedProcessDefinitionIdsFoundError(value)))
        .build();
  }

  private ValidationError duplicatedProcessDefinitionIdsFoundError(NameValueReferableTypeConvertibleMap<BpAuthConfiguration> value) {
    var bpAuthConfiguration = value.getOne();
    var processDefinitions = bpAuthConfiguration.getAuthorization().getProcessDefinitions();
    var duplicates = findDuplicates(processDefinitions);

    var joinedDuplicatedIds = duplicates.stream()
        .map(d -> String.format("'%s'", d.getProcessDefinitionId()))
        .collect(Collectors.joining(","));

    var validationError = ValidationError.builder()
        .regulationFileName(bpAuthConfiguration.getRegulationFileName())
        .errorMessage(String.format("Duplicated process definitions found: %s", joinedDuplicatedIds))
        .build();

    return validationError;
  }

  private boolean duplicatedProcessDefinitionIdsExist(NameValueReferableTypeConvertibleMap<BpAuthConfiguration> value) {
    var bpAuthConfiguration = value.getOne();
    var processDefinitions = bpAuthConfiguration.getAuthorization().getProcessDefinitions();
    var duplicates = findDuplicates(processDefinitions);
    return !duplicates.isEmpty();
  }

  private Set<ProcessDefinition> findDuplicates(List<ProcessDefinition> processDefinitions) {
    var duplicates = Sets.newHashSet();
    return processDefinitions.stream()
        .filter(n -> !duplicates.add(n))
        .collect(Collectors.toSet());
  }
}