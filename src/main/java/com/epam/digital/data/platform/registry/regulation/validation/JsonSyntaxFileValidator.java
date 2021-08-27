package com.epam.digital.data.platform.registry.regulation.validation;

import com.epam.digital.data.platform.registry.regulation.validation.model.ValidationError;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.Set;

public class JsonSyntaxFileValidator implements RegulationValidator<File> {

  private final ObjectMapper objectMapper;

  public JsonSyntaxFileValidator(ObjectMapper objectMapper) {
    this.objectMapper = objectMapper;
  }

  @Override
  public Set<ValidationError> validate(File regulationFile) {
    try {
      this.objectMapper.readTree(regulationFile);
      return Collections.emptySet();
    } catch (IOException ex) {
      return Collections.singleton(ValidationError.of(regulationFile, "File processing failure", ex));
    }
  }
}
