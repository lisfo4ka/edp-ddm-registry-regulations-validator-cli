package com.epam.digital.data.platform.registry.regulation.validation.cli.support;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.apache.commons.cli.MissingArgumentException;
import org.apache.commons.cli.ParseException;
import org.junit.Before;
import org.junit.Test;

public class CommandLineArgsParserTest {

  private CommandLineArgsParser commandLineArgsParser;

  @Before
  public void setUp() {
    this.commandLineArgsParser = new CommandLineArgsParser();
  }

  @Test
  public void shouldSupportHelpOption() throws ParseException {
    var options = commandLineArgsParser.parse("--help");

    assertTrue(options.hasOption("help"));
  }

  @Test
  public void shouldSupportGlobalVarsOption() throws ParseException {
    var options = commandLineArgsParser.parse("--global-vars-files=test");

    assertTrue(options.hasOption("global-vars-files"));
  }

  @Test
  public void shouldRequireArgumentForGlobalVarsOption() {
    assertThrows(MissingArgumentException.class, () -> commandLineArgsParser.parse("--global-vars-file"));
  }

  @Test
  public void shouldSupportBpAuthOption() throws ParseException {
    var options = commandLineArgsParser.parse("--bp-auth-files=test");

    assertTrue(options.hasOption("bp-auth-files"));
  }

  @Test
  public void shouldSupportMultipleArgumentsForBpAuthOption() throws ParseException {
    var options = commandLineArgsParser.parse("--bp-auth-files=test1,test2");
    String[] values = options.getOption("bp-auth-files").getValues();

    assertArrayEquals(new String[]{"test1", "test2"}, values);
  }

  @Test
  public void shouldSupportBpTrembitaOption() throws ParseException {
    var options = commandLineArgsParser.parse("--bp-trembita-files=test");

    assertTrue(options.hasOption("bp-trembita-files"));
  }

  @Test
  public void shouldSupportRolesOption() throws ParseException {
    var options = commandLineArgsParser.parse("--roles-files=test");

    assertTrue(options.hasOption("roles-files"));
  }

  @Test
  public void shouldSupportBpmnOption() throws ParseException {
    var options = commandLineArgsParser.parse("--bpmn-files=test");

    assertTrue(options.hasOption("bpmn-files"));
  }

  @Test
  public void shouldSupportDmnOption() throws ParseException {
    var options = commandLineArgsParser.parse("--dmn-files=test");

    assertTrue(options.hasOption("dmn-files"));
  }

  @Test
  public void shouldSupportFormsOption() throws ParseException {
    var options = commandLineArgsParser.parse("--form-files=test");

    assertTrue(options.hasOption("form-files"));
  }

  @Test
  public void shouldFailWithUnrecognizedOption() {
    assertThrows(ParseException.class, () -> commandLineArgsParser.parse("--unrecognized"));
  }
}