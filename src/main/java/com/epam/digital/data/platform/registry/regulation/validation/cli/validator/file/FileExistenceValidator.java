package com.epam.digital.data.platform.registry.regulation.validation.cli.validator.file;

import com.epam.digital.data.platform.registry.regulation.validation.cli.validator.RegulationValidator;
import com.epam.digital.data.platform.registry.regulation.validation.cli.validator.ValidationContext;
import com.epam.digital.data.platform.registry.regulation.validation.cli.validator.ValidationError;
import java.io.File;
import java.util.Collections;
import java.util.Set;

public class FileExistenceValidator implements RegulationValidator<File> {

  @Override
  public Set<ValidationError> validate(File regulationFile, ValidationContext validationContext) {
    if (regulationFile.exists()) {
      return Collections.emptySet();
    }

    var error = ValidationError.builder()
        .regulationFileType(validationContext.getRegulationFileType())
        .regulationFile(regulationFile)
        .errorMessage("Regulation file not found")
        .build();

    return Collections.singleton(error);
  }
}
