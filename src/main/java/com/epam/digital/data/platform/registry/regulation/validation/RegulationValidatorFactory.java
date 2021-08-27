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
        RegulationFileType.BP_AUTH, loggingDecorator(
            compositeValidator(
                new FileExistenceValidator(),
                new FileExtensionValidator(RegulationFileType.BP_AUTH),
                new EmptyFileValidator(),
                new JsonSchemaFileValidator(jsonSchemaOf(BP_AUTH_JSON_SCHEMA), yamlObjectMapper),
                new BpAuthRulesValidator(yamlObjectMapper)
            )
        ),
        RegulationFileType.BP_TREMBITA, loggingDecorator(
            compositeValidator(
                new FileExistenceValidator(),
                new FileExtensionValidator(RegulationFileType.BP_TREMBITA),
                new EmptyFileValidator(),
                new JsonSchemaFileValidator(jsonSchemaOf(BP_TREMBITA_JSON_SCHEMA), yamlObjectMapper),
                new BpTrembitaRulesValidator(yamlObjectMapper)
            )
        ),
        RegulationFileType.ROLES, loggingDecorator(
            compositeValidator(
                new FileExistenceValidator(),
                new FileExtensionValidator(RegulationFileType.ROLES),
                new EmptyFileValidator(),
                new JsonSchemaFileValidator(jsonSchemaOf(ROLES_JSON_SCHEMA), yamlObjectMapper)
            )
        ),
        RegulationFileType.GLOBAL_VARS, loggingDecorator(
            compositeValidator(
                new FileExistenceValidator(),
                new FileExtensionValidator(RegulationFileType.GLOBAL_VARS),
                new EmptyFileValidator(),
                new JsonSchemaFileValidator(jsonSchemaOf(GLOBAL_VARS_JSON_SCHEMA), yamlObjectMapper)
            )
        ),
        RegulationFileType.FORMS, loggingDecorator(
            compositeValidator(
                new FileExistenceValidator(),
                new FileExtensionValidator(RegulationFileType.FORMS),
                new EmptyFileValidator(),
                new JsonSyntaxFileValidator(jsonObjectMapper)
            )
        ),
        RegulationFileType.BPMN, loggingDecorator(
            compositeValidator(
                new FileExistenceValidator(),
                new FileExtensionValidator(RegulationFileType.BPMN),
                new BpmnFileValidator()
            )
        ),
        RegulationFileType.DMN, loggingDecorator(
            compositeValidator(
                new FileExistenceValidator(),
                new FileExtensionValidator(RegulationFileType.DMN),
                new DmnFileValidator()
            )
        )
    );
  }

  private RegulationValidator<File> validatorFor(RegulationFileType regulationFileType) {
    return this.regulationTypeValidators.get(regulationFileType);
  }

  private RegulationValidator<File> loggingDecorator(RegulationValidator<File> validator) {
    return new FileValidatorLoggingDecorator(validator);
  }

  private RegulationValidator<File> compositeValidator(RegulationValidator<File>... validators) {
    return CompositeFileValidator.of(validators);
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
