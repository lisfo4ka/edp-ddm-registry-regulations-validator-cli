package com.epam.digital.data.platform.registry.regulation.validation;

import static org.hamcrest.CoreMatchers.hasItems;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;
import static org.hamcrest.MatcherAssert.assertThat;

import com.epam.digital.data.platform.registry.regulation.validation.model.ValidationError;
import java.io.File;
import java.util.Collections;
import java.util.Set;
import org.junit.Test;

public class CompositeFileValidatorTest {

  @Test
  public void shouldIterateOverAllValidatorsIfNoErrorsEncountered() {
    RegulationValidator<File> validator1 = mock(RegulationValidator.class);
    when(validator1.validate(any())).thenReturn(Collections.emptySet());
    RegulationValidator<File> validator2 = mock(RegulationValidator.class);
    when(validator2.validate(any())).thenReturn(Collections.emptySet());

    CompositeFileValidator compositeValidator = CompositeFileValidator.builder()
        .validator(validator1)
        .validator(validator2)
        .build();

    Set<ValidationError> errors = compositeValidator.validate(any());

    assertThat(errors, is(empty()));

    verify(validator1, times(1)).validate(any());
    verify(validator2, times(1)).validate(any());
  }

  @Test
  public void shouldStopIteratingIfErrorsEncountered() {
    ValidationError validationError = someValidationError();

    RegulationValidator<File> validator1 = mock(RegulationValidator.class);
    when(validator1.validate(any())).thenReturn(Collections.singleton(validationError));
    RegulationValidator<File> validator2 = mock(RegulationValidator.class);

    CompositeFileValidator compositeValidator = CompositeFileValidator.builder()
        .validator(validator1)
        .validator(validator2)
        .build();

    Set<ValidationError> errors = compositeValidator.validate(any());

    assertThat(errors, hasSize(1));
    assertThat(errors, hasItems(validationError));

    verify(validator1, times(1)).validate(any());
    verifyNoInteractions(validator2);
  }

  private ValidationError someValidationError() {
    return ValidationError.of(new File(""), "any error");
  }
}