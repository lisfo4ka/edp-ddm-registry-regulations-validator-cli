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
import com.epam.digital.data.platform.registry.regulation.validation.cli.validator.typed.BpAuthProcessUniquenessValidator;
import com.epam.digital.data.platform.registry.regulation.validation.cli.validator.typed.BpTrembitaProcessUniquenessValidator;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.util.Map;
import org.springframework.core.io.ResourceLoader;

public class RegulationValidatorFactory {

  private static final String BP_AUTH_JSON_SCHEMA = "classpath:schema/bp-auth-schema.json";
  private static final String BP_TREMBITA_JSON_SCHEMA = "classpath:schema/bp-trembita-schema.json";
  private static final String ROLES_JSON_SCHEMA = "classpath:schema/roles-schema.json";
  private static final String GLOBAL_VARS_JSON_SCHEMA = "classpath:schema/global-vars-schema.json";
  private static final String FORMS_JSON_SCHEMA = "classpath:schema/forms-schema.json";

  private final Map<RegulationFileType, RegulationValidator<File>> regulationTypeValidators;

  public RegulationValidatorFactory(ResourceLoader resourceLoader, ObjectMapper yamlObjectMapper, ObjectMapper jsonObjectMapper) {
    this.regulationTypeValidators = regulationTypeValidators(resourceLoader, yamlObjectMapper, jsonObjectMapper);
  }

  public RegulationValidator<RegulationFiles> newRegulationFilesValidator() {
    return new RegulationFilesValidator(regulationTypeValidators);
  }

  private Map<RegulationFileType, RegulationValidator<File>> regulationTypeValidators(ResourceLoader resourceLoader, ObjectMapper yamlObjectMapper, ObjectMapper jsonObjectMapper) {
    return Map.of(
        RegulationFileType.BP_AUTH, newBpAuthFileValidator(resourceLoader, yamlObjectMapper),
        RegulationFileType.BP_TREMBITA, newBpTrembitaFileValidator(resourceLoader, yamlObjectMapper),
        RegulationFileType.ROLES, newRolesFileValidator(resourceLoader, yamlObjectMapper),
        RegulationFileType.GLOBAL_VARS, newGlobalVarsFileValidator(resourceLoader, yamlObjectMapper),
        RegulationFileType.FORMS, newFormsFileValidator(resourceLoader, jsonObjectMapper),
        RegulationFileType.BPMN, newBpmnFileValidator(),
        RegulationFileType.DMN, newDmnFileValidator()
    );
  }

  private RegulationValidator<File> newBpAuthFileValidator(ResourceLoader resourceLoader, ObjectMapper yamlObjectMapper) {
    return decorate(
        CompositeFileValidator.builder()
            .validator(new FileExistenceValidator())
            .validator(new FileExtensionValidator())
            .validator(new EmptyFileValidator())
            .validator(new JsonSchemaFileValidator(BP_AUTH_JSON_SCHEMA, resourceLoader, yamlObjectMapper))
            .validator(
                TypedConfigurationValidator.<BpAuthConfiguration>builder()
                    .configurationClass(BpAuthConfiguration.class)
                    .configurationLoader(new RegulationConfigurationLoader(yamlObjectMapper))
                    .validator(new BpAuthProcessUniquenessValidator())
                    .build())
            .build()
    );
  }

  private RegulationValidator<File> newBpTrembitaFileValidator(ResourceLoader resourceLoader, ObjectMapper yamlObjectMapper) {
    return decorate(
        CompositeFileValidator.builder()
            .validator(new FileExistenceValidator())
            .validator(new FileExtensionValidator())
            .validator(new EmptyFileValidator())
            .validator(new JsonSchemaFileValidator(BP_TREMBITA_JSON_SCHEMA, resourceLoader, yamlObjectMapper))
            .validator(
                TypedConfigurationValidator.<BpTrembitaConfiguration>builder()
                    .configurationClass(BpTrembitaConfiguration.class)
                    .configurationLoader(new RegulationConfigurationLoader(yamlObjectMapper))
                    .validator(new BpTrembitaProcessUniquenessValidator())
                    .build())
            .build()
    );
  }

  private RegulationValidator<File> newRolesFileValidator(ResourceLoader resourceLoader, ObjectMapper yamlObjectMapper) {
    return decorate(
        CompositeFileValidator.builder()
            .validator(new FileExistenceValidator())
            .validator(new FileExtensionValidator())
            .validator(new EmptyFileValidator())
            .validator(new JsonSchemaFileValidator(ROLES_JSON_SCHEMA, resourceLoader, yamlObjectMapper))
            .build()
    );
  }

  private RegulationValidator<File> newGlobalVarsFileValidator(ResourceLoader resourceLoader, ObjectMapper yamlObjectMapper) {
    return decorate(
        CompositeFileValidator.builder()
            .validator(new FileExistenceValidator())
            .validator(new FileExtensionValidator())
            .validator(new EmptyFileValidator())
            .validator(new JsonSchemaFileValidator(GLOBAL_VARS_JSON_SCHEMA, resourceLoader, yamlObjectMapper))
            .build()
    );
  }

  private RegulationValidator<File> newFormsFileValidator(ResourceLoader resourceLoader, ObjectMapper jsonObjectMapper) {
    return decorate(
        CompositeFileValidator.builder()
            .validator(new FileExistenceValidator())
            .validator(new FileExtensionValidator())
            .validator(new EmptyFileValidator())
            .validator(new JsonSchemaFileValidator(FORMS_JSON_SCHEMA, resourceLoader, jsonObjectMapper))
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

}
