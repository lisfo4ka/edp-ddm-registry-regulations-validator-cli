package com.epam.digital.data.platform.registry.regulation.validation;

import com.epam.digital.data.platform.registry.regulation.validation.model.RegulationFileType;
import com.epam.digital.data.platform.registry.regulation.validation.model.ValidationError;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.Set;

public class JsonSyntaxRegulationFileValidator extends AbstractRegulationFileValidator {

  private final ObjectMapper objectMapper;

  public JsonSyntaxRegulationFileValidator(ObjectMapper objectMapper) {
    super(RegulationFileType.JSON);
    this.objectMapper = objectMapper;
  }

  @Override
  protected Set<ValidationError> validateInternal(File regulationFile) {
    try {
      this.objectMapper.readTree(regulationFile);
      return Collections.emptySet();
    } catch (IOException ex) {
      return Collections.singleton(buildValidationError(regulationFile, "File processing failure", ex));
    }
  }
}
