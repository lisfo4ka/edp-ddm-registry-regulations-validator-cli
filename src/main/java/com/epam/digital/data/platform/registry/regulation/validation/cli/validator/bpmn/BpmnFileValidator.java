package com.epam.digital.data.platform.registry.regulation.validation.cli.validator.bpmn;

import com.epam.digital.data.platform.registry.regulation.validation.cli.validator.RegulationValidator;
import com.epam.digital.data.platform.registry.regulation.validation.cli.validator.ValidationContext;
import com.epam.digital.data.platform.registry.regulation.validation.cli.validator.ValidationError;
import java.io.File;
import java.util.Collections;
import java.util.Set;
import org.camunda.bpm.model.bpmn.Bpmn;
import org.camunda.bpm.model.bpmn.BpmnModelException;
import org.camunda.bpm.model.xml.ModelParseException;
import org.camunda.bpm.model.xml.ModelValidationException;

public class BpmnFileValidator implements RegulationValidator<File> {

  @Override
  public Set<ValidationError> validate(File regulationFile, ValidationContext validationContext) {
    try {
      var bpmnModel = Bpmn.readModelFromFile(regulationFile);
      Bpmn.validateModel(bpmnModel);
      return Collections.emptySet();
    } catch (BpmnModelException | ModelParseException ex) {
      return singleError("BPMN file parsing failure", ex, regulationFile, validationContext);
    } catch (ModelValidationException ex) {
      return singleError("BPMN file validation against the schema failure", ex, regulationFile, validationContext);
    }
  }

  private Set<ValidationError> singleError(String errorMessage, Exception ex, File regulationFile, ValidationContext validationContext) {
    return Collections.singleton(
        ValidationError.of(validationContext.getRegulationFileType(), regulationFile, errorMessage, ex)
    );
  }
}
