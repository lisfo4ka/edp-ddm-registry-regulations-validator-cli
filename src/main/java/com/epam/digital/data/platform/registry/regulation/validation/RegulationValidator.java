package com.epam.digital.data.platform.registry.regulation.validation;

import com.epam.digital.data.platform.registry.regulation.validation.model.ValidationError;
import java.util.Set;

public interface RegulationValidator<T> {

  Set<ValidationError> validate(T regulationFile);
}
