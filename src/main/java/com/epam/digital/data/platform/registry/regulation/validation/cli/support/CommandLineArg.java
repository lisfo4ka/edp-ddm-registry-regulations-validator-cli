package com.epam.digital.data.platform.registry.regulation.validation.cli.support;

public enum CommandLineArg {

  GLOBAL_VARS("global-vars-files"),
  BP_AUTH("bp-auth-files"),
  BP_TREMBITA("bp-trembita-files"),
  ROLES("roles-files"),
  BPMN("bpmn-files"),
  DMN("dmn-files"),
  FORMS("form-files");

  private final String argOptionName;

  CommandLineArg(String argOptionName) {
    this.argOptionName = argOptionName;
  }

  public String getArgOptionName() {
    return argOptionName;
  }
}
