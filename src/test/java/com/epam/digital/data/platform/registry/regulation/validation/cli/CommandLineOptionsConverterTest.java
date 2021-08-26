package com.epam.digital.data.platform.registry.regulation.validation.cli;

import static org.junit.Assert.assertFalse;

import com.google.common.base.Joiner;
import lombok.SneakyThrows;
import org.apache.commons.cli.Options;
import org.junit.Before;
import org.junit.Test;

public class CommandLineOptionsConverterTest {

  private CommandLineOptionsConverter commandLineOptionsConverter;

  @Before
  public void setUp() {
    this.commandLineOptionsConverter = new CommandLineOptionsConverter();
  }

  @Test
  public void shouldConvertAllOptionsToFiles() {
    var regulationFiles = commandLineOptionsConverter.convert(allOptionsPrefilled());

    assertFalse(regulationFiles.getBpTrembitaFiles().isEmpty());
    assertFalse(regulationFiles.getGlobalVarsFiles().isEmpty());
    assertFalse(regulationFiles.getBpAuthFiles().isEmpty());
    assertFalse(regulationFiles.getRolesFiles().isEmpty());
    assertFalse(regulationFiles.getBpmnFiles().isEmpty());
    assertFalse(regulationFiles.getDmnFiles().isEmpty());
    assertFalse(regulationFiles.getFormFiles().isEmpty());
  }

  @SneakyThrows
  private Options allOptionsPrefilled() {
    return new CommandLineArgsParser().parse(regulationFiles());
  }

  private String[] regulationFiles() {
    return new String[]{
        argOf(CommandLineArg.GLOBAL_VARS, "global-vars.yml"),
        argOf(CommandLineArg.BP_AUTH, "bp-auth.yml"),
        argOf(CommandLineArg.BP_TREMBITA, "bp-trembita.yml"),
        argOf(CommandLineArg.ROLES, "roles.yml"),
        argOf(CommandLineArg.BPMN, "process.bpmn"),
        argOf(CommandLineArg.DMN, "rule.dmn"),
        argOf(CommandLineArg.FORMS, "ui-form.json")
    };
  }

  private String argOf(CommandLineArg arg, String... resourcePath) {
    return String.format("--%s=%s", arg.getArgOptionName(), Joiner.on(',').join(resourcePath));
  }
}