package com.epam.digital.data.platform.registry.regulation.validation.cli.validator;

import java.io.File;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import lombok.Builder;
import lombok.Getter;
import lombok.Singular;

@Builder
@Getter
public class CompositeFileValidator implements RegulationValidator<File> {

  @Singular
  private List<RegulationValidator<File>> validators;

  @Override
  public Set<ValidationError> validate(File regulationFile, ValidationContext validationContext) {
    for (var validator : validators) {
      var errors = validator.validate(regulationFile, validationContext);
      if (errors.isEmpty()) {
        continue;
      }
      return errors;
    }
    return Collections.emptySet();
  }
}
