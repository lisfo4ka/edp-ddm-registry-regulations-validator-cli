package com.epam.digital.data.platform.registry.regulation.validation;

import com.epam.digital.data.platform.registry.regulation.validation.model.ValidationError;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.networknt.schema.JsonSchema;
import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;

public class JsonSchemaFileValidator implements  RegulationValidator<File> {

  private final JsonSchema schema;
  private final ObjectMapper fileObjectMapper;

  public JsonSchemaFileValidator(JsonSchema schema, ObjectMapper fileObjectMapper) {
    this.schema = schema;
    this.fileObjectMapper = fileObjectMapper;
  }

  @Override
  public Set<ValidationError> validate(File regulationFile) {
    try {
      var jsonNode = fileObjectMapper.readTree(regulationFile);
      return validateSchema(regulationFile, jsonNode);
    } catch (IOException ex) {
      return Collections.singleton(ValidationError.of(regulationFile, "File processing failure", ex));
    }
  }

  private Set<ValidationError> validateSchema(File regulationFile, JsonNode jsonNode) {
    var validationMessages = schema.validate(jsonNode);
    var errors = new LinkedHashSet<ValidationError>();
    validationMessages.forEach(
        validationMessage -> errors.add(ValidationError.of(regulationFile, validationMessage.getMessage()))
    );
    return errors;
  }
}
