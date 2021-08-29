package com.epam.digital.data.platform.registry.regulation.validation;

import com.epam.digital.data.platform.registry.regulation.validation.model.ValidationError;
import java.io.File;
import java.util.Collection;
import java.util.Collections;
import java.util.Set;
import lombok.Builder;
import lombok.Getter;
import lombok.Singular;

@Builder
@Getter
public class CompositeFileValidator implements RegulationValidator<File> {

  @Singular private Collection<RegulationValidator<File>> validators;

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
