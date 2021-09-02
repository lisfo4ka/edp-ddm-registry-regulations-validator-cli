package com.epam.digital.data.platform.registry.regulation.validation.cli.model;

import java.io.File;

public interface RegulationConfiguration {

  void setRegulationFile(File regulationFile);

  File getRegulationFile();
}
