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

  public RegulationValidator<RegulationFiles> newRegistryRegulationFilesValidator() {
    return (regulationFiles) -> {
      var errors = new LinkedHashSet<ValidationError>();

      regulationFiles.getBpAuthFiles().forEach(file -> errors.addAll(validatorFor(RegulationFileType.BP_AUTH).validate(file)));

      regulationFiles.getBpTrembitaFiles().forEach(file -> errors.addAll(validatorFor(RegulationFileType.BP_TREMBITA).validate(file)));

      regulationFiles.getGlobalVarsFiles().forEach(file -> errors.addAll(validatorFor(RegulationFileType.GLOBAL_VARS).validate(file)));

      regulationFiles.getRolesFiles().forEach(file -> errors.addAll(validatorFor(RegulationFileType.ROLES).validate(file)));

      regulationFiles.getBpmnFiles().forEach(file -> errors.addAll(validatorFor(RegulationFileType.BPMN).validate(file)));

      regulationFiles.getDmnFiles().forEach(file -> errors.addAll(validatorFor(RegulationFileType.DMN).validate(file)));

      regulationFiles.getFormFiles().forEach(file -> errors.addAll(validatorFor(RegulationFileType.FORMS).validate(file)));

      return errors;
    };
  }

  private Map<RegulationFileType, RegulationValidator<File>> regulationTypeValidators(ObjectMapper yamlObjectMapper, ObjectMapper jsonObjectMapper) {
    return Map.of(
        RegulationFileType.BP_AUTH, FileValidatorLoggingDecorator.wrap(
            CompositeFileValidator.builder()
                .validator(new FileExistenceValidator())
                .validator(new FileExtensionValidator(RegulationFileType.BP_AUTH))
                .validator(new EmptyFileValidator())
                .validator(new JsonSchemaFileValidator(jsonSchemaOf(BP_AUTH_JSON_SCHEMA), yamlObjectMapper))
                .validator(new BpAuthRulesValidator(new RegulationConfigurationLoader(yamlObjectMapper)))
                .build()
        ),
        RegulationFileType.BP_TREMBITA, FileValidatorLoggingDecorator.wrap(
            CompositeFileValidator.builder()
                .validator(new FileExistenceValidator())
                .validator(new FileExtensionValidator(RegulationFileType.BP_TREMBITA))
                .validator(new EmptyFileValidator())
                .validator(new JsonSchemaFileValidator(jsonSchemaOf(BP_TREMBITA_JSON_SCHEMA), yamlObjectMapper))
                .validator(new BpTrembitaRulesValidator(new RegulationConfigurationLoader(yamlObjectMapper)))
                .build()
        ),
        RegulationFileType.ROLES, FileValidatorLoggingDecorator.wrap(
            CompositeFileValidator.builder()
                .validator(new FileExistenceValidator())
                .validator(new FileExtensionValidator(RegulationFileType.ROLES))
                .validator(new EmptyFileValidator())
                .validator(new JsonSchemaFileValidator(jsonSchemaOf(ROLES_JSON_SCHEMA), yamlObjectMapper))
                .build()
        ),
        RegulationFileType.GLOBAL_VARS, FileValidatorLoggingDecorator.wrap(
            CompositeFileValidator.builder()
                .validator(new FileExistenceValidator())
                .validator(new FileExtensionValidator(RegulationFileType.GLOBAL_VARS))
                .validator(new EmptyFileValidator())
                .validator(new JsonSchemaFileValidator(jsonSchemaOf(GLOBAL_VARS_JSON_SCHEMA), yamlObjectMapper))
                .build()
        ),
        RegulationFileType.FORMS, FileValidatorLoggingDecorator.wrap(
            CompositeFileValidator.builder()
                .validator(new FileExistenceValidator())
                .validator(new FileExtensionValidator(RegulationFileType.FORMS))
                .validator(new EmptyFileValidator())
                .validator(new JsonSyntaxFileValidator(jsonObjectMapper))
                .build()
        ),
        RegulationFileType.BPMN, FileValidatorLoggingDecorator.wrap(
            CompositeFileValidator.builder()
                .validator(new FileExistenceValidator())
                .validator(new FileExtensionValidator(RegulationFileType.BPMN))
                .validator(new BpmnFileValidator())
                .build()
        ),
        RegulationFileType.DMN, FileValidatorLoggingDecorator.wrap(
            CompositeFileValidator.builder()
                .validator(new FileExistenceValidator())
                .validator(new FileExtensionValidator(RegulationFileType.DMN))
                .validator(new DmnFileValidator())
                .build()
        )
    );
  }

  private RegulationValidator<File> validatorFor(RegulationFileType regulationFileType) {
    return this.regulationTypeValidators.get(regulationFileType);
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
