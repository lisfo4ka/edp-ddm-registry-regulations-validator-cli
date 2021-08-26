package com.epam.digital.data.platform.registry.regulation.validation.model;

import java.util.Arrays;

public enum RegulationFileType {

  JSON("json"),
  YAML("yml"),
  BPMN("bpmn"),
  DMN("dmn");

  private String fileExtension;

  RegulationFileType(String fileExtension) {
    this.fileExtension = fileExtension;
  }

  public String getFileExtension() {
    return fileExtension;
  }

  public static RegulationFileType of(String fileExtension) {
    return Arrays.stream(values())
        .filter(regulationFileType -> regulationFileType.getFileExtension().equals(fileExtension))
        .findFirst()
        .orElse(null);
  }
}
