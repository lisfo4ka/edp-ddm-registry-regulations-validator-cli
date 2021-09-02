package com.epam.digital.data.platform.registry.regulation.validation.cli.validator.json;

import com.epam.digital.data.platform.registry.regulation.validation.cli.model.RegulationFileType;
import com.epam.digital.data.platform.registry.regulation.validation.cli.validator.RegulationValidator;
import com.epam.digital.data.platform.registry.regulation.validation.cli.validator.ValidationContext;
import com.epam.digital.data.platform.registry.regulation.validation.cli.validator.ValidationError;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.networknt.schema.JsonSchema;
import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;

public class JsonSchemaFileValidator implements RegulationValidator<File> {

  private final JsonSchema schema;
  private final ObjectMapper fileObjectMapper;

  public JsonSchemaFileValidator(JsonSchema schema, ObjectMapper fileObjectMapper) {
    this.schema = schema;
    this.fileObjectMapper = fileObjectMapper;
  }

  @Override
  public Set<ValidationError> validate(File regulationFile, ValidationContext validationContext) {
    try {
      var jsonNode = fileObjectMapper.readTree(regulationFile);
      return validateSchema(regulationFile, jsonNode, validationContext.getRegulationFileType());
    } catch (IOException ex) {
      return singleError("File processing failure", ex, regulationFile, validationContext);
    }
  }

  private Set<ValidationError> validateSchema(File regulationFile, JsonNode jsonNode, RegulationFileType regulationFileType) {
    var validationMessages = schema.validate(jsonNode);
    var errors = new LinkedHashSet<ValidationError>();
    validationMessages.forEach(
        validationMessage -> errors.add(ValidationError.of(regulationFileType, regulationFile, validationMessage.getMessage()))
    );
    return errors;
  }

  private Set<ValidationError> singleError(String errorMessage, Exception ex, File regulationFile, ValidationContext validationContext) {
    return Collections.singleton(
        ValidationError.of(validationContext.getRegulationFileType(), regulationFile, errorMessage, ex)
    );
  }
}
