package com.epam.digital.data.platform.registry.regulation.validation.cli;

import com.epam.digital.data.platform.registry.regulation.validation.RegistryRegulationValidatorFactory;
import com.epam.digital.data.platform.registry.regulation.validation.model.RegulationFiles;
import com.epam.digital.data.platform.registry.regulation.validation.model.ValidationError;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.cli.ParseException;
import org.springframework.boot.CommandLineRunner;

@Slf4j
public class RegulationValidationCommandLineRunner implements CommandLineRunner {

  private final CommandLineArgsParser commandLineArgsParser;
  private final CommandLineOptionsConverter commandLineOptionsConverter;

  private final RegistryRegulationValidatorFactory registryRegulationValidatorFactory;

  private final SystemExit systemExit;

  public RegulationValidationCommandLineRunner(RegistryRegulationValidatorFactory registryRegulationValidatorFactory,
      CommandLineArgsParser commandLineArgsParser, CommandLineOptionsConverter commandLineOptionsConverter,
      SystemExit systemExit) {
    this.commandLineArgsParser = commandLineArgsParser;
    this.commandLineOptionsConverter = commandLineOptionsConverter;
    this.registryRegulationValidatorFactory = registryRegulationValidatorFactory;
    this.systemExit = systemExit;
  }

  @Override
  public void run(String... args) {
    try {
      parseAndValidate(args);
    } catch (ParseException e) {
      handleCommandLineParseException(e);
    }
  }

  private void parseAndValidate(String[] args) throws ParseException {
    var options = this.commandLineArgsParser.parse(args);

    if (options.hasOption("help")) {
      this.commandLineArgsParser.printHelp();
      this.systemExit.complete();
      return;
    }

    var regulationFiles = commandLineOptionsConverter.convert(options);
    if (regulationFiles.isEmpty()) {
      log.info("No registry regulation files to validate.");
      this.systemExit.complete();
      return;
    }

    validate(regulationFiles);
  }

  private void validate(RegulationFiles registryRegulationFiles) {
    var registryRegulationValidator = registryRegulationValidatorFactory.newRegistryRegulationFilesValidator();

    var errors = registryRegulationValidator.validate(registryRegulationFiles);

    if (errors.isEmpty()) {
      log.info("Registry regulation validation passed successfully.");
      this.systemExit.complete();
      return;
    }

    log.error("Registry regulation validation failure! See details below:");
    for (ValidationError error : errors) {
      log.error(error.toString());
    }
    systemExit.validationFailure();
  }

  private void handleCommandLineParseException(ParseException e) {
    log.error("Parsing failure: {}. Find help below:", e.getMessage());

    this.commandLineArgsParser.printHelp();
    this.systemExit.systemError();
  }
}