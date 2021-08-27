package com.epam.digital.data.platform.registry.regulation.validation;

import com.epam.digital.data.platform.registry.regulation.validation.model.ValidationError;
import java.io.File;
import java.util.Collections;
import java.util.Set;

public class FileExistenceValidator implements RegulationValidator<File> {

  @Override
  public Set<ValidationError> validate(File regulationFile) {
    if (!regulationFile.exists()) {
      return Collections.singleton(ValidationError.of(regulationFile, "Regulation file not found"));
    }
    return Collections.emptySet();
  }
}
