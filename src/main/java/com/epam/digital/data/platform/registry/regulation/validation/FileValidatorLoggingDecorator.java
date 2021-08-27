package com.epam.digital.data.platform.registry.regulation.validation;

import com.epam.digital.data.platform.registry.regulation.validation.model.ValidationError;
import java.io.File;
import java.util.Set;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class FileValidatorLoggingDecorator implements RegulationValidator<File> {

  private final RegulationValidator<File> validator;

  public FileValidatorLoggingDecorator(RegulationValidator<File> validator) {
    this.validator = validator;
  }

  @Override
  public Set<ValidationError> validate(File regulationFile) {
    Set<ValidationError> errors = this.validator.validate(regulationFile);
    if (errors.isEmpty()) {
      log.info("[{}] Regulation file passed validation successfully.", regulationFile.getName());
    } else {
      for (ValidationError error : errors) {
        log.error(error.toString());
      }
    }
    return errors;
  }
}
