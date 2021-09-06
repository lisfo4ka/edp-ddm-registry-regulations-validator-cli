package com.epam.digital.data.platform.registry.regulation.validation.cli.support;

import com.epam.digital.data.platform.registry.regulation.validation.cli.model.RegulationConfiguration;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.io.IOException;

public class RegulationConfigurationLoader {

  private final ObjectMapper objectMapper;

  public RegulationConfigurationLoader(ObjectMapper objectMapper) {
    this.objectMapper = objectMapper;
  }

  public <T extends RegulationConfiguration> T load(File regulationFile, Class<T> configurationClass) throws IOException {
    var config = objectMapper.readValue(regulationFile, configurationClass);
    config.setRegulationFile(regulationFile);
    return config;
  }
}
