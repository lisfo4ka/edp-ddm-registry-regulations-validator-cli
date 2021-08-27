package com.epam.digital.data.platform.registry.regulation.validation;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.File;
import org.camunda.bpm.model.bpmn.Bpmn;
import org.camunda.bpm.model.xml.ModelValidationException;
import org.junit.Before;
import org.junit.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

public class BpmnFileValidatorTest {

  private BpmnFileValidator validator;

  @Before
  public void setUp() {
    this.validator = new BpmnFileValidator();
  }

  @Test
  public void shouldPassProcessValidation() {
    var processFile = getFileFromClasspath("registry-regulation/correct/process.bpmn");

    var errors = this.validator.validate(processFile);

    assertThat(errors.isEmpty()).isTrue();
  }

  @Test
  public void shouldFailProcessParsingDueToMissingProcessId() {
    var brokenProcessFile = getFileFromClasspath("registry-regulation/broken/process-broken.bpmn");

    var errors = this.validator.validate(brokenProcessFile);

    assertThat(errors.isEmpty()).isFalse();
  }

  @Test
  public void shouldFailDueToProcessModelValidationIssue() {
    var processFile = getFileFromClasspath("registry-regulation/correct/process.bpmn");
    var bpmnModel = Bpmn.readModelFromFile(processFile);

    try (MockedStatic<Bpmn> bpmn = Mockito.mockStatic(Bpmn.class)) {
      bpmn.when(() -> Bpmn.readModelFromFile(processFile))
          .thenReturn(bpmnModel);
      bpmn.when(() -> Bpmn.validateModel(bpmnModel))
          .thenThrow(new ModelValidationException());

      var errors = this.validator.validate(processFile);

      assertThat(errors.isEmpty()).isFalse();
    }
  }

  @Test
  public void shouldFailDueToUnsupportedFileExtension() {
    File missingFile = new File("missing-file.bpmn");

    var errors = this.validator.validate(missingFile);

    assertThat(errors.isEmpty()).isFalse();
  }

  @Test
  public void shouldFailDueToMissingFile() {
    File unsupportedFile = new File("unsupported-file.xml");

    var errors = this.validator.validate(unsupportedFile);

    assertThat(errors.isEmpty()).isFalse();
  }

  private File getFileFromClasspath(String filePath) {
    var classLoader = getClass().getClassLoader();
    return new File(classLoader.getResource(filePath).getFile());
  }
}