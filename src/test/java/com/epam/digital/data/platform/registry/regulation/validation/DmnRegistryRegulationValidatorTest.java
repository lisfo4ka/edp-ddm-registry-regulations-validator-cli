package com.epam.digital.data.platform.registry.regulation.validation;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.File;
import org.junit.Before;
import org.junit.Test;

public class DmnRegistryRegulationValidatorTest {

  private DmnRegulationFileValidator validator;

  @Before
  public void setUp() {
    this.validator = new DmnRegulationFileValidator();
  }

  @Test
  public void shouldPassRuleValidation() {
    var processFile = getFileFromClasspath("registry-regulation/correct/rule.dmn");

    var errors = this.validator.validate(processFile);

    assertThat(errors.isEmpty()).isTrue();
  }

  @Test
  public void shouldFailRuleValidation() {
    var processFile = getFileFromClasspath("registry-regulation/broken/rule-broken.dmn");

    var errors = this.validator.validate(processFile);

    assertThat(errors.isEmpty()).isFalse();
  }

  private File getFileFromClasspath(String filePath) {
    var classLoader = getClass().getClassLoader();
    return new File(classLoader.getResource(filePath).getFile());
  }
}