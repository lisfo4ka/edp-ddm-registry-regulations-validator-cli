package com.epam.digital.data.platform.registry.regulation.validation.model;

import java.io.File;
import java.util.Collection;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RegulationFiles {

  private Collection<File> bpTrembitaFiles;
  private Collection<File> globalVarsFiles;
  private Collection<File> bpAuthFiles;
  private Collection<File> rolesFiles;
  private Collection<File> bpmnFiles;
  private Collection<File> dmnFiles;
  private Collection<File> formFiles;

  public boolean isEmpty() {
    return bpTrembitaFiles.isEmpty()
        && globalVarsFiles.isEmpty()
        && bpAuthFiles.isEmpty()
        && rolesFiles.isEmpty()
        && bpmnFiles.isEmpty()
        && dmnFiles.isEmpty()
        && formFiles.isEmpty();
  }
}
