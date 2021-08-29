package com.epam.digital.data.platform.registry.regulation.validation;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.io.File;
import org.junit.Test;

public class FileValidatorLoggingDecoratorTest {

  @Test
  public void shouldDelegateValidationToDecorated() {
    File fileToValidate = new File("");
    RegulationValidator<File> decoratedValidator = mock(RegulationValidator.class);

    FileValidatorLoggingDecorator.wrap(decoratedValidator).validate(fileToValidate);

    verify(decoratedValidator, times(1)).validate(fileToValidate);
  }
}