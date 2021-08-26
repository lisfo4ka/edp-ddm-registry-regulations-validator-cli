package com.epam.digital.data.platform.registry.regulation.validation;

import com.epam.digital.data.platform.registry.regulation.validation.model.RegulationFileType;
import com.epam.digital.data.platform.registry.regulation.validation.model.RegulationFiles;
import com.epam.digital.data.platform.registry.regulation.validation.model.ValidationError;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.networknt.schema.JsonSchema;
import com.networknt.schema.JsonSchemaFactory;
import com.networknt.schema.SpecVersion.VersionFlag;
import java.io.File;
import java.util.LinkedHashSet;
import lombok.SneakyThrows;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;

public class RegistryRegulationValidatorFactory {

  private static final VersionFlag JSON_SCHEMA_VERSION = VersionFlag.V4;

  private static final String BP_AUTH_JSON_SCHEMA = "classpath:schema/bp-auth-schema.json";
  private static final String BP_TREMBITA_JSON_SCHEMA = "classpath:schema/bp-trembita-schema.json";
  private static final String ROLES_JSON_SCHEMA = "classpath:schema/roles-schema.json";
  private static final String GLOBAL_VARS_JSON_SCHEMA = "classpath:schema/global-vars-schema.json";

  private final ResourceLoader resourceLoader;

  private final ObjectMapper yamlObjectMapper;
  private final ObjectMapper jsonObjectMapper;

  public RegistryRegulationValidatorFactory(ResourceLoader resourceLoader, ObjectMapper yamlObjectMapper, ObjectMapper jsonObjectMapper) {
    this.resourceLoader = resourceLoader;
    this.yamlObjectMapper = yamlObjectMapper;
    this.jsonObjectMapper = jsonObjectMapper;
  }

  public RegistryRegulationValidator<RegulationFiles> newRegistryRegulationFilesValidator() {
    var bpAuthJsonSchemaValidator = newBpAuthJsonSchemaValidator();
    var rolesJsonSchemaValidator = newRolesJsonSchemaValidator();
    var bpTrembitaJsonSchemaValidator = newBpTrembitaJsonSchemaValidator();
    var globalVarsTrembitaJsonSchemaValidator = newGlobalVarsTrembitaJsonSchemaValidator();
    var bpmnValidator = newBpmnValidator();
    var dmnValidator = newDmnValidator();
    var formValidator = newJsonSyntaxValidator();

    return (regulationFiles) -> {
      var errors = new LinkedHashSet<ValidationError>();

      regulationFiles.getBpAuthFiles().forEach(file -> errors.addAll(bpAuthJsonSchemaValidator.validate(file)));

      regulationFiles.getRolesFiles().forEach(file -> errors.addAll(rolesJsonSchemaValidator.validate(file)));

      regulationFiles.getBpmnFiles().forEach(file -> errors.addAll(bpmnValidator.validate(file)));

      regulationFiles.getDmnFiles().forEach(file -> errors.addAll(dmnValidator.validate(file)));

      regulationFiles.getFormFiles().forEach(file -> errors.addAll(formValidator.validate(file)));

      regulationFiles.getBpTrembitaFiles().forEach(file -> errors.addAll(bpTrembitaJsonSchemaValidator.validate(file)));

      regulationFiles.getGlobalVarsFiles().forEach(file -> errors.addAll(globalVarsTrembitaJsonSchemaValidator.validate(file)));

      return errors;
    };
  }

  public RegistryRegulationValidator<File> newBpAuthJsonSchemaValidator() {
    var resource = resourceLoader.getResource(BP_AUTH_JSON_SCHEMA);
    return JsonSchemaRegulationFileValidator.emptyFileTolerant(jsonSchemaOf(resource), RegulationFileType.YAML, yamlObjectMapper);
  }

  public RegistryRegulationValidator<File> newBpTrembitaJsonSchemaValidator() {
    var resource = resourceLoader.getResource(BP_TREMBITA_JSON_SCHEMA);
    return JsonSchemaRegulationFileValidator.emptyFileTolerant(jsonSchemaOf(resource), RegulationFileType.YAML, yamlObjectMapper);
  }

  public RegistryRegulationValidator<File> newRolesJsonSchemaValidator() {
    var resource = resourceLoader.getResource(ROLES_JSON_SCHEMA);
    return JsonSchemaRegulationFileValidator.emptyFileTolerant(jsonSchemaOf(resource), RegulationFileType.YAML, yamlObjectMapper);
  }

  public RegistryRegulationValidator<File> newGlobalVarsTrembitaJsonSchemaValidator() {
    var resource = resourceLoader.getResource(GLOBAL_VARS_JSON_SCHEMA);
    return JsonSchemaRegulationFileValidator.emptyFileIntolerant(jsonSchemaOf(resource), RegulationFileType.YAML, yamlObjectMapper);
  }

  public RegistryRegulationValidator<File> newBpmnValidator() {
    return new BpmnRegulationFileValidator();
  }

  public RegistryRegulationValidator<File> newDmnValidator() {
    return new DmnRegulationFileValidator();
  }

  public RegistryRegulationValidator<File> newJsonSyntaxValidator() {
    return new JsonSyntaxRegulationFileValidator(jsonObjectMapper);
  }

  @SneakyThrows
  private JsonSchema jsonSchemaOf(Resource schemaResource) {
    var factory = JsonSchemaFactory
        .builder(JsonSchemaFactory.getInstance(JSON_SCHEMA_VERSION))
        .objectMapper(new JsonMapper())
        .build();

    return factory.getSchema(schemaResource.getInputStream());
  }
}
