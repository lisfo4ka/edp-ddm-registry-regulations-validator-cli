package com.epam.digital.data.platform.registry.regulation.validation.model;

import java.util.Arrays;

public enum RegulationFileType {

  GLOBAL_VARS("yml", "yaml"),
  BP_AUTH("yml", "yaml"),
  BP_TREMBITA("yml", "yaml"),
  ROLES("yml", "yaml"),
  BPMN("bpmn"),
  DMN("dmn"),
  FORMS("json");

  private final String[] fileExtensions;

  RegulationFileType(String... fileExtensions) {
    this.fileExtensions = fileExtensions;
  }

  public String[] getFileExtensions() {
    return fileExtensions;
  }

  public boolean isExtensionSupported(String fileExtension) {
    return Arrays.asList(this.fileExtensions).contains(fileExtension);
  }
}
