package com.epam.digital.data.platform.registry.regulation.validation.cli.validator;

import com.epam.digital.data.platform.registry.regulation.validation.cli.model.RegulationConfiguration;
import com.epam.digital.data.platform.registry.regulation.validation.cli.support.RegulationConfigurationLoader;
import java.io.File;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import lombok.Builder;
import lombok.Getter;
import lombok.Singular;

@Getter
@Builder
public class TypedConfigurationValidator<T extends RegulationConfiguration> implements RegulationValidator<File> {

  private final RegulationConfigurationLoader configurationLoader;
  private final Class<T> configurationClass;

  @Singular
  private List<RegulationValidator<T>> validators;

  @Override
  public Set<ValidationError> validate(File regulationFile, ValidationContext validationContext) {
    var configuration = this.configurationLoader.load(regulationFile, this.configurationClass);
    var errors = new LinkedHashSet<ValidationError>();
    for (var validator : validators) {
      errors.addAll(validator.validate(configuration, validationContext));
    }
    return errors;
  }
}

