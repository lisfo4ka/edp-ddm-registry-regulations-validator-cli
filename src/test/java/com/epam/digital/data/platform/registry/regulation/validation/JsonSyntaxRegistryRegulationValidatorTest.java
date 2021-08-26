package com.epam.digital.data.platform.registry.regulation.validation;

import static org.assertj.core.api.Assertions.assertThat;

import com.fasterxml.jackson.databind.json.JsonMapper;
import java.io.File;
import org.junit.Before;
import org.junit.Test;

public class JsonSyntaxRegistryRegulationValidatorTest {

  private JsonSyntaxRegulationFileValidator validator;

  @Before
  public void setUp() {
    this.validator = new JsonSyntaxRegulationFileValidator(new JsonMapper());
  }

  @Test
  public void shouldPassJsonFormValidation() {
    var processFile = getFileFromClasspath("registry-regulation/correct/ui-form.json");

    var errors = this.validator.validate(processFile);

    assertThat(errors.isEmpty()).isTrue();
  }

  @Test
  public void shouldFailJsonFormValidationDueToMissingComma() {
    var processFile = getFileFromClasspath("registry-regulation/broken/ui-form-broken.json");

    var errors = this.validator.validate(processFile);

    assertThat(errors.isEmpty()).isFalse();
  }

  private File getFileFromClasspath(String filePath) {
    var classLoader = getClass().getClassLoader();
    return new File(classLoader.getResource(filePath).getFile());
  }
}