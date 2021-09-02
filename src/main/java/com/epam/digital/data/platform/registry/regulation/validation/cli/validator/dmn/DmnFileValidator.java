package com.epam.digital.data.platform.registry.regulation.validation.cli.validator.dmn;

import com.epam.digital.data.platform.registry.regulation.validation.cli.validator.RegulationValidator;
import com.epam.digital.data.platform.registry.regulation.validation.cli.validator.ValidationContext;
import com.epam.digital.data.platform.registry.regulation.validation.cli.validator.ValidationError;
import java.io.File;
import java.util.Collections;
import java.util.Set;
import org.camunda.bpm.model.dmn.Dmn;
import org.camunda.bpm.model.dmn.DmnModelException;
import org.camunda.bpm.model.xml.ModelValidationException;

public class DmnFileValidator implements RegulationValidator<File> {

  @Override
  public Set<ValidationError> validate(File regulationFile, ValidationContext validationContext) {
    try {
      var dmnModelInstance = Dmn.readModelFromFile(regulationFile);
      Dmn.validateModel(dmnModelInstance);
      return Collections.emptySet();
    } catch (DmnModelException ex) {
      return singleError("DMN file parsing failure", ex, regulationFile, validationContext);
    } catch (ModelValidationException ex) {
      return singleError("DMN file validation against the schema failure", ex, regulationFile, validationContext);
    }
  }

  private Set<ValidationError> singleError(String errorMessage, Exception ex, File regulationFile, ValidationContext validationContext) {
    return Collections.singleton(
        ValidationError.of(validationContext.getRegulationFileType(), regulationFile, errorMessage, ex)
    );
  }
}
