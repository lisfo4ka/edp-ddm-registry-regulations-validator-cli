package com.epam.digital.data.platform.registry.regulation.validation;

import com.epam.digital.data.platform.registry.regulation.validation.model.ValidationError;
import com.google.common.collect.Lists;
import java.io.File;
import java.util.Collection;
import java.util.Collections;
import java.util.Set;

public class CompositeFileValidator implements RegulationValidator<File> {

  private final Collection<RegulationValidator<File>> validators;

  public static CompositeFileValidator of(RegulationValidator<File>... validators) {
    return new CompositeFileValidator(validators);
  }

  private CompositeFileValidator(RegulationValidator<File>... validators) {
    this.validators = Lists.newArrayList(validators);
  }

  @Override
  public Set<ValidationError> validate(File regulationFile) {
    for (var validator : validators) {
      var errors = validator.validate(regulationFile);
      if (errors.isEmpty()) {
        continue;
      }
      return errors;
    }
    return Collections.emptySet();
  }
}
