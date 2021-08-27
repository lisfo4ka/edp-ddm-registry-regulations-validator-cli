package com.epam.digital.data.platform.registry.regulation.validation;

import com.epam.digital.data.platform.registry.regulation.validation.model.ValidationError;
import java.io.File;
import java.util.Collections;
import java.util.Set;
import org.camunda.bpm.model.dmn.Dmn;
import org.camunda.bpm.model.dmn.DmnModelException;
import org.camunda.bpm.model.xml.ModelValidationException;

public class DmnFileValidator implements RegulationValidator<File> {

  @Override
  public Set<ValidationError> validate(File regulationFile) {
    try {
      var dmnModelInstance = Dmn.readModelFromFile(regulationFile);
      Dmn.validateModel(dmnModelInstance);
      return Collections.emptySet();
    } catch (DmnModelException ex) {
      return Collections.singleton(ValidationError.of(regulationFile, "DMN file parsing failure", ex));
    } catch (ModelValidationException ex) {
      return Collections.singleton(ValidationError.of(regulationFile, "DMN file validation against the schema failure", ex));
    }
  }
}
