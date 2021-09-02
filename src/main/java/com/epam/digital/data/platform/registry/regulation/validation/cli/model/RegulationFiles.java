package com.epam.digital.data.platform.registry.regulation.validation.cli.model;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import lombok.Builder;
import lombok.Builder.Default;
import lombok.Getter;

@Getter
@Builder
public class RegulationFiles {

  @Default
  private Collection<File> bpTrembitaFiles = new ArrayList<>();
  @Default
  private Collection<File> globalVarsFiles = new ArrayList<>();
  @Default
  private Collection<File> bpAuthFiles = new ArrayList<>();
  @Default
  private Collection<File> rolesFiles = new ArrayList<>();
  @Default
  private Collection<File> bpmnFiles = new ArrayList<>();
  @Default
  private Collection<File> dmnFiles = new ArrayList<>();
  @Default
  private Collection<File> formFiles = new ArrayList<>();

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
