package com.epam.digital.data.platform.registry.regulation.validation;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.File;
import org.camunda.bpm.model.dmn.Dmn;
import org.camunda.bpm.model.xml.ModelValidationException;
import org.junit.Before;
import org.junit.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

public class DmnRegistryRegulationValidatorTest {

  private DmnRegulationFileValidator validator;

  @Before
  public void setUp() {
    this.validator = new DmnRegulationFileValidator();
  }

  @Test
  public void shouldPassRuleValidation() {
    var ruleFile = getFileFromClasspath("registry-regulation/correct/rule.dmn");

    var errors = this.validator.validate(ruleFile);

    assertThat(errors.isEmpty()).isTrue();
  }

  @Test
  public void shouldFailDueToRuleParsingIssue() {
    var ruleFile = getFileFromClasspath("registry-regulation/broken/rule-broken.dmn");

    var errors = this.validator.validate(ruleFile);

    assertThat(errors.isEmpty()).isFalse();
  }

  @Test
  public void shouldFailDueToRuleModelValidationIssue() {
    var ruleFile = getFileFromClasspath("registry-regulation/correct/rule.dmn");
    var dmnModel = Dmn.readModelFromFile(ruleFile);

    try (MockedStatic<Dmn> bpmn = Mockito.mockStatic(Dmn.class)) {
      bpmn.when(() -> Dmn.readModelFromFile(ruleFile))
          .thenReturn(dmnModel);
      bpmn.when(() -> Dmn.validateModel(dmnModel))
          .thenThrow(new ModelValidationException());

      var errors = this.validator.validate(ruleFile);

      assertThat(errors.isEmpty()).isFalse();
    }
  }

  private File getFileFromClasspath(String filePath) {
    var classLoader = getClass().getClassLoader();
    return new File(classLoader.getResource(filePath).getFile());
  }
}