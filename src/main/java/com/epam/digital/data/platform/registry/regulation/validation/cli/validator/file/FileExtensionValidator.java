package com.epam.digital.data.platform.registry.regulation.validation.cli.validator.file;

import com.epam.digital.data.platform.registry.regulation.validation.cli.validator.RegulationValidator;
import com.epam.digital.data.platform.registry.regulation.validation.cli.validator.ValidationContext;
import com.epam.digital.data.platform.registry.regulation.validation.cli.validator.ValidationError;
import com.google.common.base.Joiner;
import com.google.common.io.Files;
import java.io.File;
import java.util.Collections;
import java.util.Set;

public class FileExtensionValidator implements RegulationValidator<File> {

  private static final String EXT_ERROR_MESSAGE_FORMAT = "Regulation file must have '%s' extension";

  @Override
  public Set<ValidationError> validate(File regulationFile, ValidationContext validationContext) {
    var fileExtension = Files.getFileExtension(regulationFile.getName());
    var regulationFileType = validationContext.getRegulationFileType();

    if (regulationFileType.isExtensionSupported(fileExtension)) {
      return Collections.emptySet();
    }

    var error = ValidationError.builder()
        .regulationFileType(regulationFileType)
        .regulationFile(regulationFile)
        .errorMessage(String.format(EXT_ERROR_MESSAGE_FORMAT, Joiner.on(",").join(regulationFileType.getFileExtensions())))
        .build();

    return Collections.singleton(error);
  }
}
