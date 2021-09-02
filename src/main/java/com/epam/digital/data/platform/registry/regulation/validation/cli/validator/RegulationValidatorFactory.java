package com.epam.digital.data.platform.registry.regulation.validation.cli.validator;

import com.epam.digital.data.platform.registry.regulation.validation.cli.model.BpAuthConfiguration;
import com.epam.digital.data.platform.registry.regulation.validation.cli.model.BpTrembitaConfiguration;
import com.epam.digital.data.platform.registry.regulation.validation.cli.model.RegulationFileType;
import com.epam.digital.data.platform.registry.regulation.validation.cli.model.RegulationFiles;
import com.epam.digital.data.platform.registry.regulation.validation.cli.support.RegulationConfigurationLoader;
import com.epam.digital.data.platform.registry.regulation.validation.cli.validator.bpmn.BpmnFileValidator;
import com.epam.digital.data.platform.registry.regulation.validation.cli.validator.dmn.DmnFileValidator;
import com.epam.digital.data.platform.registry.regulation.validation.cli.validator.file.EmptyFileValidator;
import com.epam.digital.data.platform.registry.regulation.validation.cli.validator.file.FileExistenceValidator;
import com.epam.digital.data.platform.registry.regulation.validation.cli.validator.file.FileExtensionValidator;
import com.epam.digital.data.platform.registry.regulation.validation.cli.validator.file.FileValidatorLoggingDecorator;
import com.epam.digital.data.platform.registry.regulation.validation.cli.validator.json.JsonSchemaFileValidator;
import com.epam.digital.data.platform.registry.regulation.validation.cli.validator.json.JsonSyntaxFileValidator;
import com.epam.digital.data.platform.registry.regulation.validation.cli.validator.typed.BpAuthProcessUniquenessValidator;
import com.epam.digital.data.platform.registry.regulation.validation.cli.validator.typed.BpTrembitaProcessUniquenessValidator;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.networknt.schema.JsonSchema;
import com.networknt.schema.JsonSchemaFactory;
import com.networknt.schema.SpecVersion.VersionFlag;
import java.io.File;
import java.util.Map;
import lombok.SneakyThrows;
import org.springframework.core.io.ResourceLoader;

public class RegulationValidatorFactory {

  private static final VersionFlag JSON_SCHEMA_VERSION = VersionFlag.V4;

  private static final String BP_AUTH_JSON_SCHEMA = "classpath:schema/bp-auth-schema.json";
  private static final String BP_TREMBITA_JSON_SCHEMA = "classpath:schema/bp-trembita-schema.json";
  private static final String ROLES_JSON_SCHEMA = "classpath:schema/roles-schema.json";
  private static final String GLOBAL_VARS_JSON_SCHEMA = "classpath:schema/global-vars-schema.json";

  private final Map<RegulationFileType, RegulationValidator<File>> regulationTypeValidators;

  private final ResourceLoader resourceLoader;

  public RegulationValidatorFactory(ResourceLoader resourceLoader, ObjectMapper yamlObjectMapper, ObjectMapper jsonObjectMapper) {
    this.resourceLoader = resourceLoader;
    this.regulationTypeValidators = regulationTypeValidators(yamlObjectMapper, jsonObjectMapper);
  }

  public RegulationValidator<RegulationFiles> newRegulationFilesValidator() {
    return new RegulationFilesValidator(regulationTypeValidators);
  }

  private Map<RegulationFileType, RegulationValidator<File>> regulationTypeValidators(ObjectMapper yamlObjectMapper, ObjectMapper jsonObjectMapper) {
    return Map.of(
        RegulationFileType.BP_AUTH, newBpAuthFileValidator(yamlObjectMapper),
        RegulationFileType.BP_TREMBITA, newBpTrembitaFileValidator(yamlObjectMapper),
        RegulationFileType.ROLES, newRolesFileValidator(yamlObjectMapper),
        RegulationFileType.GLOBAL_VARS, newGlobalVarsFileValidator(yamlObjectMapper),
        RegulationFileType.FORMS, newFormsFileValidator(jsonObjectMapper),
        RegulationFileType.BPMN, newBpmnFileValidator(),
        RegulationFileType.DMN, newDmnFileValidator()
    );
  }

  private RegulationValidator<File> newBpAuthFileValidator(ObjectMapper yamlObjectMapper) {
    return decorate(
        CompositeFileValidator.builder()
            .validator(new FileExistenceValidator())
            .validator(new FileExtensionValidator())
            .validator(new EmptyFileValidator())
            .validator(new JsonSchemaFileValidator(jsonSchemaOf(BP_AUTH_JSON_SCHEMA), yamlObjectMapper))
            .validator(
                TypedConfigurationValidator.<BpAuthConfiguration>builder()
                    .configurationClass(BpAuthConfiguration.class)
                    .configurationLoader(new RegulationConfigurationLoader(yamlObjectMapper))
                    .validator(new BpAuthProcessUniquenessValidator())
                    .build())
            .build()
    );
  }

  private RegulationValidator<File> newBpTrembitaFileValidator(ObjectMapper yamlObjectMapper) {
    return decorate(
        CompositeFileValidator.builder()
            .validator(new FileExistenceValidator())
            .validator(new FileExtensionValidator())
            .validator(new EmptyFileValidator())
            .validator(new JsonSchemaFileValidator(jsonSchemaOf(BP_TREMBITA_JSON_SCHEMA), yamlObjectMapper))
            .validator(
                TypedConfigurationValidator.<BpTrembitaConfiguration>builder()
                    .configurationClass(BpTrembitaConfiguration.class)
                    .configurationLoader(new RegulationConfigurationLoader(yamlObjectMapper))
                    .validator(new BpTrembitaProcessUniquenessValidator())
                    .build())
            .build()
    );
  }

  private RegulationValidator<File> newRolesFileValidator(ObjectMapper yamlObjectMapper) {
    return decorate(
        CompositeFileValidator.builder()
            .validator(new FileExistenceValidator())
            .validator(new FileExtensionValidator())
            .validator(new EmptyFileValidator())
            .validator(new JsonSchemaFileValidator(jsonSchemaOf(ROLES_JSON_SCHEMA), yamlObjectMapper))
            .build()
    );
  }

  private RegulationValidator<File> newGlobalVarsFileValidator(ObjectMapper yamlObjectMapper) {
    return decorate(
        CompositeFileValidator.builder()
            .validator(new FileExistenceValidator())
            .validator(new FileExtensionValidator())
            .validator(new EmptyFileValidator())
            .validator(new JsonSchemaFileValidator(jsonSchemaOf(GLOBAL_VARS_JSON_SCHEMA), yamlObjectMapper))
            .build()
    );
  }

  private RegulationValidator<File> newFormsFileValidator(ObjectMapper jsonObjectMapper) {
    return decorate(
        CompositeFileValidator.builder()
            .validator(new FileExistenceValidator())
            .validator(new FileExtensionValidator())
            .validator(new EmptyFileValidator())
            .validator(new JsonSyntaxFileValidator(jsonObjectMapper))
            .build()
    );
  }

  private RegulationValidator<File> newBpmnFileValidator() {
    return decorate(
        CompositeFileValidator.builder()
            .validator(new FileExistenceValidator())
            .validator(new FileExtensionValidator())
            .validator(new BpmnFileValidator())
            .build()
    );
  }

  private RegulationValidator<File> newDmnFileValidator() {
    return decorate(
        CompositeFileValidator.builder()
            .validator(new FileExistenceValidator())
            .validator(new FileExtensionValidator())
            .validator(new DmnFileValidator())
            .build()
    );
  }

  private RegulationValidator<File> decorate(RegulationValidator<File> validator) {
    return FileValidatorLoggingDecorator.wrap(validator);
  }

  @SneakyThrows
  private JsonSchema jsonSchemaOf(String jsonSchemaLocation) {
    var resource = resourceLoader.getResource(jsonSchemaLocation);
    var factory = JsonSchemaFactory
        .builder(JsonSchemaFactory.getInstance(JSON_SCHEMA_VERSION))
        .objectMapper(new JsonMapper())
        .build();
    return factory.getSchema(resource.getInputStream());
  }
}
