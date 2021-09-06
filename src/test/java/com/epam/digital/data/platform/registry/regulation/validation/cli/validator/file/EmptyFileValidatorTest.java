package com.epam.digital.data.platform.registry.regulation.validation.cli.validator.file;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNot.not;
import static org.mockito.Mockito.times;

import com.epam.digital.data.platform.registry.regulation.validation.cli.model.RegulationFileType;
import com.epam.digital.data.platform.registry.regulation.validation.cli.validator.ValidationContext;
import com.google.common.io.Files;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import org.junit.Before;
import org.junit.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

public class EmptyFileValidatorTest {

  private EmptyFileValidator validator;

  @Before
  public void setUp() {
    this.validator = new EmptyFileValidator();
  }

  @Test
  public void shouldPassIfNonEmptyFile() {
    var nonEmptyFile = getFileFromClasspath("registry-regulation/correct/bp-auth.yml");

    var errors = validator.validate(nonEmptyFile, ValidationContext.of(RegulationFileType.BP_AUTH));

    assertThat(errors, is(empty()));
  }

  @Test
  public void shouldFailIfEmptyFile() {
    var emptyFile = getFileFromClasspath("registry-regulation/empty/empty-file.json");

    var errors = validator.validate(emptyFile, ValidationContext.of(RegulationFileType.BP_AUTH));

    assertThat(errors, is(not(empty())));
  }

  @Test
  public void shouldFailIfUnableToReadFile() {
    var someFile = new File("some-file");
    try (MockedStatic<Files> filesUtilMock = Mockito.mockStatic(Files.class)) {
      filesUtilMock.when(() -> Files.readLines(someFile, StandardCharsets.UTF_8)).thenThrow(IOException.class);

      var errors = validator.validate(someFile, ValidationContext.empty());

      filesUtilMock.verify(times(1), () -> Files.readLines(someFile, StandardCharsets.UTF_8));
      assertThat(errors, is(not(empty())));
    }
  }

  private File getFileFromClasspath(String filePath) {
    var classLoader = getClass().getClassLoader();
    return new File(classLoader.getResource(filePath).getFile());
  }
}