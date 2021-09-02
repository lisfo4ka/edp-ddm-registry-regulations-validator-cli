package com.epam.digital.data.platform.registry.regulation.validation.cli.validator.file;

import com.epam.digital.data.platform.registry.regulation.validation.cli.validator.RegulationValidator;
import com.epam.digital.data.platform.registry.regulation.validation.cli.validator.ValidationContext;
import com.epam.digital.data.platform.registry.regulation.validation.cli.validator.ValidationError;
import java.io.File;
import java.util.Collections;
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
  public Set<ValidationError> validate(File regulationFile, ValidationContext validationContext) {
    var errors = this.validator.validate(regulationFile, validationContext);

    if (errors.isEmpty()) {
      log.info("[{}] Regulation file passed validation successfully.", regulationFile.getName());
      return Collections.emptySet();
    }

    errors.forEach(error -> log.error(error.toString()));
    return errors;
  }
}
