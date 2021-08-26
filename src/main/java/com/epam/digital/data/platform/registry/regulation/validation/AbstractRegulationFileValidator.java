package com.epam.digital.data.platform.registry.regulation.validation;

import com.epam.digital.data.platform.registry.regulation.validation.model.RegulationFileType;
import com.epam.digital.data.platform.registry.regulation.validation.model.ValidationError;
import com.google.common.io.Files;
import java.io.File;
import java.util.Collections;
import java.util.Set;
import org.springframework.util.Assert;

public abstract class AbstractRegulationFileValidator implements RegistryRegulationValidator<File> {

  protected final RegulationFileType regulationFileType;

  AbstractRegulationFileValidator(RegulationFileType regulationFileType) {
    this.regulationFileType = regulationFileType;
  }

  @Override
  public Set<ValidationError> validate(File regulationFile ) {
    Assert.notNull(regulationFile, "Regulation file must not be NULL");

    var fileName = regulationFile.getName();
    var fileExtension = Files.getFileExtension(fileName);

    if (!regulationFileType.equals(RegulationFileType.of(fileExtension))) {
      return Collections.singleton(buildValidationError(regulationFile, String.format("Regulation file must have '%s' extension", regulationFileType.getFileExtension())));
    }

    if (!regulationFile.exists()) {
      return Collections.singleton(buildValidationError(regulationFile, "Regulation file not found"));
    }

    return validateInternal(regulationFile);
  }

  protected abstract Set<ValidationError> validateInternal(File regulationFile);

  protected ValidationError buildValidationError(File regulationFile, String errorMessage, Exception ex) {
    return ValidationError.builder()
        .regulationFileType(regulationFileType)
        .regulationFileName(regulationFile.getName())
        .errorMessage(errorMessage)
        .originalCauseMessage(ex.getMessage())
        .build();
  }

  protected ValidationError buildValidationError(File regulationFile, String errorMessage) {
    return ValidationError.builder()
        .regulationFileType(regulationFileType)
        .regulationFileName(regulationFile.getName())
        .errorMessage(errorMessage)
        .build();
  }
}
