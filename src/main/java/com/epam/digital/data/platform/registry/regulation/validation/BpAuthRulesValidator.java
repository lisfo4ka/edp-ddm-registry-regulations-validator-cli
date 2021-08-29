package com.epam.digital.data.platform.registry.regulation.validation;

import com.deliveredtechnologies.rulebook.Fact;
import com.deliveredtechnologies.rulebook.FactMap;
import com.deliveredtechnologies.rulebook.lang.RuleBookBuilder;
import com.epam.digital.data.platform.registry.regulation.validation.model.BpAuthConfiguration;
import com.epam.digital.data.platform.registry.regulation.validation.model.ValidationError;
import com.epam.digital.data.platform.registry.regulation.validation.model.ValidationErrors;
import com.epam.digital.data.platform.registry.regulation.validation.rules.BpAuthValidationRules;
import java.io.File;
import java.util.Set;

public class BpAuthRulesValidator implements RegulationValidator<File> {

  private final RegulationConfigurationLoader configurationLoader;

  public BpAuthRulesValidator(RegulationConfigurationLoader configurationLoader) {
    this.configurationLoader = configurationLoader;
  }

  @Override
  public Set<ValidationError> validate(File regulationFile) {
    var bpAuthConfiguration = configurationLoader.load(regulationFile, BpAuthConfiguration.class);
    var bpAuthConfigurationFacts = new FactMap<>(new Fact<>(bpAuthConfiguration));

    var bpAuthRuleBook =
        RuleBookBuilder.create(BpAuthValidationRules.class)
            .withResultType(ValidationErrors.class)
            .withDefaultResult(ValidationErrors::new)
            .build();

    bpAuthRuleBook.run(bpAuthConfigurationFacts);

    return bpAuthRuleBook.getResult().get().getValue().getErrors();
  }
}
