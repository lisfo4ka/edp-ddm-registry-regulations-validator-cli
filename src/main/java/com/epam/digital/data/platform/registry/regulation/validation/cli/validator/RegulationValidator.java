package com.epam.digital.data.platform.registry.regulation.validation.cli.validator;

import java.util.Set;

public interface RegulationValidator<T> {

  Set<ValidationError> validate(T regulation, ValidationContext context);

}
