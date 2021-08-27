package com.epam.digital.data.platform.registry.regulation.validation;

import com.epam.digital.data.platform.registry.regulation.validation.model.RegulationFileType;
import com.epam.digital.data.platform.registry.regulation.validation.model.ValidationError;
import com.google.common.base.Joiner;
import com.google.common.io.Files;
import java.io.File;
import java.util.Collections;
import java.util.Set;

public class FileExtensionValidator implements RegulationValidator<File> {

  private final RegulationFileType regulationFileType;

  public FileExtensionValidator(RegulationFileType regulationFileType) {
    this.regulationFileType = regulationFileType;
  }

  @Override
  public Set<ValidationError> validate(File regulationFile) {
    var fileExtension = Files.getFileExtension(regulationFile.getName());

    if (regulationFileType.isExtensionSupported(fileExtension)) {
      return Collections.emptySet();
    }

    String errorMessage = String.format("Regulation file must have '%s' extension", Joiner.on(",").join(regulationFileType.getFileExtensions()));
    return Collections.singleton(ValidationError.of(regulationFile, errorMessage));
  }
}
