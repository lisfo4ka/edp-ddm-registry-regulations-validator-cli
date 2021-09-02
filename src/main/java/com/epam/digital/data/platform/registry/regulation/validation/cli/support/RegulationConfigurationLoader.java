package com.epam.digital.data.platform.registry.regulation.validation.cli.support;

import com.epam.digital.data.platform.registry.regulation.validation.cli.model.RegulationConfiguration;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import lombok.SneakyThrows;

public class RegulationConfigurationLoader {

  private final ObjectMapper objectMapper;

  public RegulationConfigurationLoader(ObjectMapper objectMapper) {
    this.objectMapper = objectMapper;
  }

  @SneakyThrows
  public <T extends RegulationConfiguration> T load(File regulationFile, Class<T> configurationClass) {
    var config = objectMapper.readValue(regulationFile, configurationClass);
    config.setRegulationFile(regulationFile);
    return config;
  }
}
