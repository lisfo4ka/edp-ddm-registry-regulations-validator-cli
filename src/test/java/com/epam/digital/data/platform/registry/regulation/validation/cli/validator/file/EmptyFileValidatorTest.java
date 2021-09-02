package com.epam.digital.data.platform.registry.regulation.validation.cli.validator.file;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNot.not;

import com.epam.digital.data.platform.registry.regulation.validation.cli.model.RegulationFileType;
import com.epam.digital.data.platform.registry.regulation.validation.cli.validator.ValidationContext;
import java.io.File;
import org.junit.Test;

public class EmptyFileValidatorTest {

  @Test
  public void shouldPassIfNonEmptyFile() {
    var validator = new EmptyFileValidator();
    var nonEmptyFile = getFileFromClasspath("registry-regulation/correct/bp-auth.yml");

    var errors = validator.validate(nonEmptyFile, ValidationContext.of(RegulationFileType.BP_AUTH));

    assertThat(errors, is(empty()));
  }

  @Test
  public void shouldFailIfEmptyFile() {
    var validator = new EmptyFileValidator();
    var emptyFile = getFileFromClasspath("registry-regulation/empty/empty-file.json");

    var errors = validator.validate(emptyFile, ValidationContext.of(RegulationFileType.BP_AUTH));

    assertThat(errors, is(not(empty())));
  }

  private File getFileFromClasspath(String filePath) {
    var classLoader = getClass().getClassLoader();
    return new File(classLoader.getResource(filePath).getFile());
  }
}