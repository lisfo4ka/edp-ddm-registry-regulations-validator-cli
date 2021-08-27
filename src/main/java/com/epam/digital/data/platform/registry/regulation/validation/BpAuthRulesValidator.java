package com.epam.digital.data.platform.registry.regulation.validation;

import com.deliveredtechnologies.rulebook.Fact;
import com.deliveredtechnologies.rulebook.FactMap;
import com.deliveredtechnologies.rulebook.lang.RuleBookBuilder;
import com.epam.digital.data.platform.registry.regulation.validation.model.BpAuthConfiguration;
import com.epam.digital.data.platform.registry.regulation.validation.model.ValidationError;
import com.epam.digital.data.platform.registry.regulation.validation.model.ValidationErrors;
import com.epam.digital.data.platform.registry.regulation.validation.rules.BpAuthValidationRules;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.util.Set;
import lombok.SneakyThrows;

public class BpAuthRulesValidator implements RegulationValidator<File> {

  private final ObjectMapper objectMapper;

  public BpAuthRulesValidator(ObjectMapper objectMapper) {
    this.objectMapper = objectMapper;
  }

  @Override
  public Set<ValidationError> validate(File regulationFile) {
    var bpAuthConfiguration = readBpAuthConfiguration(regulationFile);
    var bpAuthConfigurationFacts = new FactMap<>(new Fact<>(bpAuthConfiguration));

    var bpAuthRuleBook =
        RuleBookBuilder.create(BpAuthValidationRules.class)
            .withResultType(ValidationErrors.class)
            .withDefaultResult(ValidationErrors::new)
            .build();

    bpAuthRuleBook.run(bpAuthConfigurationFacts);

    return bpAuthRuleBook.getResult().get().getValue().getErrors();
  }

  @SneakyThrows
  private BpAuthConfiguration readBpAuthConfiguration(File regulationFile) {
    BpAuthConfiguration bpAuthConfiguration = objectMapper.readValue(regulationFile, BpAuthConfiguration.class);
    bpAuthConfiguration.setRegulationFileName(regulationFile.getName());
    return bpAuthConfiguration;
  }
}
