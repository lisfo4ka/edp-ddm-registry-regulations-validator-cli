package com.epam.digital.data.platform.registry.regulation.validation.cli.validator.json;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNot.not;

import com.epam.digital.data.platform.registry.regulation.validation.cli.model.RegulationFileType;
import com.epam.digital.data.platform.registry.regulation.validation.cli.validator.ValidationContext;
import com.fasterxml.jackson.databind.json.JsonMapper;
import java.io.File;
import org.junit.Before;
import org.junit.Test;

public class JsonSyntaxFileValidatorTest {

  private JsonSyntaxFileValidator validator;

  @Before
  public void setUp() {
    this.validator = new JsonSyntaxFileValidator(new JsonMapper());
  }

  @Test
  public void shouldPassJsonFormValidation() {
    var processFile = getFileFromClasspath("registry-regulation/correct/ui-form.json");

    var errors = this.validator.validate(processFile, ValidationContext.of(RegulationFileType.FORMS));

    assertThat(errors, is(empty()));
  }

  @Test
  public void shouldFailJsonFormValidationDueToMissingComma() {
    var processFile = getFileFromClasspath("registry-regulation/broken/ui-form-broken.json");

    var errors = this.validator.validate(processFile, ValidationContext.of(RegulationFileType.FORMS));

    assertThat(errors, is(not(empty())));
  }

  private File getFileFromClasspath(String filePath) {
    var classLoader = getClass().getClassLoader();
    return new File(classLoader.getResource(filePath).getFile());
  }
}