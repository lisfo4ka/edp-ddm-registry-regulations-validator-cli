package com.epam.digital.data.platform.registry.regulation.validation;

import com.epam.digital.data.platform.registry.regulation.validation.model.ValidationError;
import java.io.File;
import java.util.Collections;
import java.util.Set;
import org.camunda.bpm.model.bpmn.Bpmn;
import org.camunda.bpm.model.bpmn.BpmnModelException;
import org.camunda.bpm.model.xml.ModelParseException;
import org.camunda.bpm.model.xml.ModelValidationException;

public class BpmnFileValidator implements RegulationValidator<File> {

  @Override
  public Set<ValidationError> validate(File regulationFile) {
    try {
      var bpmnModel = Bpmn.readModelFromFile(regulationFile);
      Bpmn.validateModel(bpmnModel);
      return Collections.emptySet();
    } catch (BpmnModelException | ModelParseException ex) {
      return Collections.singleton(ValidationError.of(regulationFile, "BPMN file parsing failure", ex));
    } catch (ModelValidationException ex) {
      return Collections.singleton(ValidationError.of(regulationFile, "BPMN file validation against the schema failure", ex));
    }
  }
}
