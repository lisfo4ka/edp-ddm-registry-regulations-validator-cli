package com.epam.digital.data.platform.registry.regulation.validation.cli;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;

import com.epam.digital.data.platform.registry.regulation.validation.RegistryRegulationValidatorFactory;
import com.epam.digital.data.platform.registry.regulation.validation.model.RegulationFiles;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLMapper;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Collections;
import java.util.stream.Collectors;
import lombok.SneakyThrows;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ResourceLoader;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
public class RegulationValidationCommandLineRunnerTest {

  @Autowired
  private ResourceLoader resourceLoader;

  private SystemExit systemExit;
  private CommandLineArgsParser commandLineArgsParser;
  private CommandLineOptionsConverter commandLineOptionsConverter;
  private RegulationValidationCommandLineRunner validationRunner;

  @Before
  public void setUp() {
    this.systemExit = mock(SystemExit.class);
    this.commandLineArgsParser = mock(CommandLineArgsParser.class);
    this.commandLineOptionsConverter = mock(CommandLineOptionsConverter.class);
  }

  @Test
  public void shouldDisplayHelpIfSpecified() throws ParseException {
    var options = new Options();
    options.addOption(Option.builder().longOpt("help").build());
    when(commandLineArgsParser.parse("--help")).thenReturn(options);

    validationRunner = newValidationRunner(resourceLoader, commandLineArgsParser, commandLineOptionsConverter, systemExit);

    validationRunner.run("--help");

    Mockito.verify(commandLineArgsParser, times(1)).printHelp();
    Mockito.verify(systemExit, times(1)).complete();
  }

  @Test
  public void shouldPassIfNoRegulationFilesSpecified() {
    validationRunner = newValidationRunner(resourceLoader, new CommandLineArgsParser(), new CommandLineOptionsConverter(), systemExit);

    validationRunner.run("--bp-auth=");

    Mockito.verify(systemExit, times(1)).complete();
  }

  @Test
  public void shouldCompleteIfNoFilesPassed() throws ParseException {
    var emptyOptions = new Options();
    when(commandLineArgsParser.parse()).thenReturn(emptyOptions);
    when(commandLineOptionsConverter.convert(emptyOptions)).thenReturn(emptyRegulationFiles());

    validationRunner = newValidationRunner(resourceLoader, commandLineArgsParser, commandLineOptionsConverter, systemExit);

    validationRunner.run();

    Mockito.verify(systemExit, times(1)).complete();
  }

  @Test
  public void shouldDisplayHelpIfUnrecognizedOptionPassed() throws ParseException {
    when(commandLineArgsParser.parse("-unrecognized")).thenThrow(ParseException.class);

    validationRunner = newValidationRunner(resourceLoader, commandLineArgsParser, commandLineOptionsConverter, systemExit);

    validationRunner.run("-unrecognized");

    Mockito.verify(commandLineArgsParser, times(1)).printHelp();
    Mockito.verify(systemExit, times(1)).systemError();
  }

  @Test
  public void shouldPassWithoutValidationErrorsLogged() {
    validationRunner = newValidationRunner(resourceLoader, new CommandLineArgsParser(), new CommandLineOptionsConverter(), systemExit);

    validationRunner.run(correctRegistryRegulations());

    Mockito.verify(systemExit, times(1)).complete();
  }

  @Test
  public void shouldFailWithValidationErrorsLogged() {
    validationRunner = newValidationRunner(resourceLoader, new CommandLineArgsParser(), new CommandLineOptionsConverter(), systemExit);

    validationRunner.run(brokenRegistryRegulations());

    Mockito.verify(systemExit, times(1)).validationFailure();
  }

  @Test
  public void shouldPassEmptyRegulationsWithoutValidationErrorsLogged() {
    validationRunner = newValidationRunner(resourceLoader, new CommandLineArgsParser(), new CommandLineOptionsConverter(), systemExit);

    validationRunner.run(emptyRegistryRegulations());

    Mockito.verify(systemExit, times(1)).complete();
  }

  private RegulationValidationCommandLineRunner newValidationRunner(ResourceLoader resourceLoader,
      CommandLineArgsParser commandLineArgsParser,
      CommandLineOptionsConverter commandLineOptionsConverter,
      SystemExit systemExit) {
    return new RegulationValidationCommandLineRunner(
        new RegistryRegulationValidatorFactory(resourceLoader, new YAMLMapper(), new JsonMapper()),
        commandLineArgsParser, commandLineOptionsConverter, systemExit
    );
  }

  private String[] correctRegistryRegulations() {
    return new String[]{
        argOf(CommandLineArg.GLOBAL_VARS, testResourcePathOf("registry-regulation/correct/global-vars.yml")),
        argOf(CommandLineArg.BP_AUTH, testResourcePathOf("registry-regulation/correct/bp-auth.yml")),
        argOf(CommandLineArg.BP_TREMBITA, testResourcePathOf("registry-regulation/correct/bp-trembita.yml")),
        argOf(CommandLineArg.ROLES, testResourcePathOf("registry-regulation/correct/roles.yml")),
        argOf(CommandLineArg.BPMN, testResourcePathOf("registry-regulation/correct/process.bpmn")),
        argOf(CommandLineArg.DMN, testResourcePathOf("registry-regulation/correct/rule.dmn")),
        argOf(CommandLineArg.FORMS, testResourcePathOf("registry-regulation/correct/ui-form.json"))
    };
  }

  private String[] brokenRegistryRegulations() {
    return new String[]{
        argOf(CommandLineArg.GLOBAL_VARS, testResourcePathOf("registry-regulation/broken/global-vars-broken.yml")),
        argOf(CommandLineArg.BP_AUTH,
            testResourcePathOf("registry-regulation/broken/bp-auth-broken.yml"),
            testResourcePathOf("registry-regulation/empty/bp-auth-empty.yml")),
        argOf(CommandLineArg.BP_TREMBITA, testResourcePathOf("registry-regulation/broken/bp-trembita-broken.yml")),
        argOf(CommandLineArg.ROLES, testResourcePathOf("registry-regulation/broken/roles-broken.yml")),
        argOf(CommandLineArg.BPMN, testResourcePathOf("registry-regulation/broken/process-broken.bpmn")),
        argOf(CommandLineArg.DMN, testResourcePathOf("registry-regulation/broken/rule-broken.dmn")),
        argOf(CommandLineArg.FORMS, testResourcePathOf("registry-regulation/broken/ui-form-broken.json"))
    };
  }

  private String[] emptyRegistryRegulations() {
    return new String[]{
        argOf(CommandLineArg.BP_AUTH, testResourcePathOf("registry-regulation/empty/bp-auth-empty.yml")),
        argOf(CommandLineArg.BP_TREMBITA, testResourcePathOf("registry-regulation/empty/bp-trembita-empty.yml")),
        argOf(CommandLineArg.ROLES, testResourcePathOf("registry-regulation/empty/roles-empty.yml"))
    };
  }

  private String argOf(CommandLineArg arg, Path... resourcePath) {
    return String.format("--%s=%s",
        arg.getArgOptionName(),
        Arrays.stream(resourcePath).map(Path::toString).collect(Collectors.joining(",")));
  }

  @SneakyThrows
  private Path testResourcePathOf(String resource) {
    var classLoader = getClass().getClassLoader();
    return Paths.get(classLoader.getResource(resource).toURI());
  }

  private RegulationFiles emptyRegulationFiles() {
    return RegulationFiles.builder()
        .bpTrembitaFiles(Collections.emptyList())
        .globalVarsFiles(Collections.emptyList())
        .bpAuthFiles(Collections.emptyList())
        .rolesFiles(Collections.emptyList())
        .formFiles(Collections.emptyList())
        .bpmnFiles(Collections.emptyList())
        .dmnFiles(Collections.emptyList())
        .build();
  }
}