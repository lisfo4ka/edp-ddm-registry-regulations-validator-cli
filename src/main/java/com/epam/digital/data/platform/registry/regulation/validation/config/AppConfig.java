package com.epam.digital.data.platform.registry.regulation.validation.config;

import com.epam.digital.data.platform.registry.regulation.validation.RegulationValidatorFactory;
import com.epam.digital.data.platform.registry.regulation.validation.cli.CommandLineArgsParser;
import com.epam.digital.data.platform.registry.regulation.validation.cli.CommandLineOptionsConverter;
import com.epam.digital.data.platform.registry.regulation.validation.cli.RegulationValidationCommandLineRunner;
import com.epam.digital.data.platform.registry.regulation.validation.cli.SystemExit;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ResourceLoader;

@Configuration
public class AppConfig {

  @Bean
  @Autowired
  public CommandLineRunner commandLineRunner(RegulationValidatorFactory regulationValidatorFactory, SystemExit systemExit) {
    return new RegulationValidationCommandLineRunner(regulationValidatorFactory, new CommandLineArgsParser(), new CommandLineOptionsConverter(), systemExit);
  }

  @Bean
  @Autowired
  public RegulationValidatorFactory registryRegulationValidatorFactory(ResourceLoader resourceLoader) {
    return new RegulationValidatorFactory(resourceLoader, yamlObjectMapper(), jsonObjectMapper());
  }

  @Bean
  @Autowired
  public SystemExit systemExit(ApplicationContext context) {
    return new SystemExit(context);
  }

  private YAMLMapper yamlObjectMapper() {
    return new YAMLMapper();
  }

  private JsonMapper jsonObjectMapper() {
    return new JsonMapper();
  }
}
