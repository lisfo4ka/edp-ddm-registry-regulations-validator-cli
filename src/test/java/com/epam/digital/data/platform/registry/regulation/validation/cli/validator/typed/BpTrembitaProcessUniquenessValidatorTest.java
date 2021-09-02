package com.epam.digital.data.platform.registry.regulation.validation.cli.validator.typed;

import static com.google.common.collect.Lists.newArrayList;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNot.not;

import com.epam.digital.data.platform.registry.regulation.validation.cli.model.BpTrembitaConfiguration;
import com.epam.digital.data.platform.registry.regulation.validation.cli.model.BpTrembitaConfiguration.Trembita;
import com.epam.digital.data.platform.registry.regulation.validation.cli.validator.ValidationContext;
import org.junit.Test;

public class BpTrembitaProcessUniquenessValidatorTest {

  @Test
  public void shouldPassBpTrembitaDueToNoDuplicates() {
    var validator = new BpTrembitaProcessUniquenessValidator();

    var config = config(
        processDefinition("id1"),
        processDefinition("id2"),
        processDefinition("id3")
    );

    var errors = validator.validate(config, ValidationContext.empty());

    assertThat(errors, is(empty()));
  }

  @Test
  public void shouldFailBpTrembitaDueToDuplicates() {
    var validator = new BpTrembitaProcessUniquenessValidator();

    var config = config(
        processDefinition("id1"),
        processDefinition("id2"),
        processDefinition("id1")
    );

    var errors = validator.validate(config, ValidationContext.empty());

    assertThat(errors, is(not(empty())));
  }

  private BpTrembitaConfiguration.ProcessDefinition processDefinition(String processDefinitionId) {
    var processDefinition = new BpTrembitaConfiguration.ProcessDefinition();
    processDefinition.setProcessDefinitionId(processDefinitionId);
    return processDefinition;
  }

  private BpTrembitaConfiguration config(BpTrembitaConfiguration.ProcessDefinition... processDefinitions) {
    var config = new BpTrembitaConfiguration();
    var trembita = new Trembita();
    trembita.setProcessDefinitions(newArrayList(processDefinitions));
    config.setTrembita(trembita);
    return config;
  }
}