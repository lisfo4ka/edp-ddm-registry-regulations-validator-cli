package com.epam.digital.data.platform.registry.regulation.validation.cli.validator.file;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.epam.digital.data.platform.registry.regulation.validation.cli.validator.RegulationValidator;
import com.epam.digital.data.platform.registry.regulation.validation.cli.validator.ValidationContext;
import java.io.File;
import org.junit.Test;

public class FileValidatorLoggingDecoratorTest {

  @Test
  @SuppressWarnings("unchecked")
  public void shouldDelegateValidationToDecorated() {
    var fileToValidate = new File("");
    var context = ValidationContext.empty();
    var decoratedValidator = mock(RegulationValidator.class);

    FileValidatorLoggingDecorator.wrap(decoratedValidator).validate(fileToValidate, context);

    verify(decoratedValidator, times(1)).validate(fileToValidate, context);
  }
}