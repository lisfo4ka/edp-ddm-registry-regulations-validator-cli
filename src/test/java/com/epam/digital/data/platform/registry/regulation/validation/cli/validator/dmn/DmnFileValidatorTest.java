package com.epam.digital.data.platform.registry.regulation.validation.cli.validator.dmn;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNot.not;

import com.epam.digital.data.platform.registry.regulation.validation.cli.model.RegulationFileType;
import com.epam.digital.data.platform.registry.regulation.validation.cli.validator.ValidationContext;
import java.io.File;
import org.camunda.bpm.model.dmn.Dmn;
import org.camunda.bpm.model.xml.ModelValidationException;
import org.junit.Before;
import org.junit.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

public class DmnFileValidatorTest {

  private DmnFileValidator validator;

  @Before
  public void setUp() {
    this.validator = new DmnFileValidator();
  }

  @Test
  public void shouldPassRuleValidation() {
    var ruleFile = getFileFromClasspath("registry-regulation/correct/rule.dmn");

    var errors = this.validator.validate(ruleFile, ValidationContext.of(RegulationFileType.DMN));

    assertThat(errors, is(empty()));
  }

  @Test
  public void shouldFailDueToRuleParsingIssue() {
    var ruleFile = getFileFromClasspath("registry-regulation/broken/rule-broken.dmn");

    var errors = this.validator.validate(ruleFile, ValidationContext.of(RegulationFileType.DMN));

    assertThat(errors, is(not(empty())));
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

      var errors = this.validator.validate(ruleFile, ValidationContext.of(RegulationFileType.DMN));

      assertThat(errors, is(not(empty())));
    }
  }

  private File getFileFromClasspath(String filePath) {
    var classLoader = getClass().getClassLoader();
    return new File(classLoader.getResource(filePath).getFile());
  }
}