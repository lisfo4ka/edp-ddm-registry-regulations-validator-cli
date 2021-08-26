package com.epam.digital.data.platform.registry.regulation.validation;

import com.epam.digital.data.platform.registry.regulation.validation.model.RegulationFileType;
import com.epam.digital.data.platform.registry.regulation.validation.model.ValidationError;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.networknt.schema.JsonSchema;
import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;

public class JsonSchemaRegulationFileValidator extends AbstractRegulationFileValidator {

  private final JsonSchema schema;
  private final ObjectMapper fileObjectMapper;

  private final boolean emptyFileTolerant;

  public static RegistryRegulationValidator<File> emptyFileTolerant(JsonSchema jsonSchema, RegulationFileType regulationFileType, ObjectMapper fileObjectMapper) {
    return new JsonSchemaRegulationFileValidator(jsonSchema, regulationFileType, fileObjectMapper, true);
  }

  public static RegistryRegulationValidator<File> emptyFileIntolerant(JsonSchema jsonSchema, RegulationFileType regulationFileType, ObjectMapper fileObjectMapper) {
    return new JsonSchemaRegulationFileValidator(jsonSchema, regulationFileType, fileObjectMapper, false);
  }

  private JsonSchemaRegulationFileValidator(JsonSchema jsonSchema, RegulationFileType regulationFileType, ObjectMapper regulationFileObjectMapper, boolean emptyFileTolerant) {
    super(regulationFileType);

    this.schema = jsonSchema;
    this.fileObjectMapper = regulationFileObjectMapper;
    this.emptyFileTolerant = emptyFileTolerant;
  }

  @Override
  protected Set<ValidationError> validateInternal(File regulationFile) {
    try {
      var jsonNode = fileObjectMapper.readTree(regulationFile);
      if (jsonNode.isEmpty() && emptyFileTolerant) {
        return Collections.emptySet();
      }
      return validateSchema(regulationFile, jsonNode);
    } catch (IOException ex) {
      return Collections.singleton(buildValidationError(regulationFile, "File processing failure", ex));
    }
  }

  private Set<ValidationError> validateSchema(File regulationFile, JsonNode jsonNode) {
    var validationMessages = schema.validate(jsonNode);
    var errors = new LinkedHashSet<ValidationError>();
    validationMessages.forEach(
        validationMessage -> errors.add(buildValidationError(regulationFile, validationMessage.getMessage()))
    );
    return errors;
  }
}
