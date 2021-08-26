package com.epam.digital.data.platform.registry.regulation.validation;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.File;
import org.junit.Before;
import org.junit.Test;

public class BpmnRegistryRegulationValidatorTest {

  private BpmnRegulationFileValidator validator;

  @Before
  public void setUp() {
    this.validator = new BpmnRegulationFileValidator();
  }

  @Test
  public void shouldPassProcessValidation() {
    var processFile = getFileFromClasspath("registry-regulation/correct/process.bpmn");

    var errors = this.validator.validate(processFile);

    assertThat(errors.isEmpty()).isTrue();
  }

  @Test
  public void shouldFailProcessValidationDueToMissingProcessId() {
    var brokenProcessFile = getFileFromClasspath("registry-regulation/broken/process-broken.bpmn");

    var errors = this.validator.validate(brokenProcessFile);

    assertThat(errors.isEmpty()).isFalse();
  }

  private File getFileFromClasspath(String filePath) {
    var classLoader = getClass().getClassLoader();
    return new File(classLoader.getResource(filePath).getFile());
  }
}