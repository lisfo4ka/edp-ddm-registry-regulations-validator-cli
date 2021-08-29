package com.epam.digital.data.platform.registry.regulation.validation;

import com.epam.digital.data.platform.registry.regulation.validation.model.ValidationError;
import java.io.File;
import java.util.Set;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.Assert;

@Slf4j
public class FileValidatorLoggingDecorator implements RegulationValidator<File> {

  private final RegulationValidator<File> validator;

  private FileValidatorLoggingDecorator(RegulationValidator<File> validator) {
    this.validator = validator;
  }

  public static RegulationValidator<File> wrap(RegulationValidator<File> validator) {
    Assert.notNull(validator, "Validator must be not NULL");

    return new FileValidatorLoggingDecorator(validator);
  }

  @Override
  public Set<ValidationError> validate(File regulationFile) {
    Set<ValidationError> errors = this.validator.validate(regulationFile);
    if (errors.isEmpty()) {
      log.info("[{}] Regulation file passed validation successfully.", regulationFile.getName());
    } else {
      errors.forEach(error -> log.error(error.toString()));
    }
    return errors;
  }
}
