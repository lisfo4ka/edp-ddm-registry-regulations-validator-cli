package com.epam.digital.data.platform.registry.regulation.validation.model;

import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class ValidationErrors implements Iterable<ValidationError> {

  private final Set<ValidationError> validationErrors = new HashSet<>();

  public void add(ValidationError validationError) {
    this.validationErrors.add(validationError);
  }

  public boolean isEmpty() {
    return this.validationErrors.isEmpty();
  }

  public Set<ValidationError> getErrors() {
    return Collections.unmodifiableSet(validationErrors);
  }

  @Override
  public Iterator<ValidationError> iterator() {
    return validationErrors.iterator();
  }
}
