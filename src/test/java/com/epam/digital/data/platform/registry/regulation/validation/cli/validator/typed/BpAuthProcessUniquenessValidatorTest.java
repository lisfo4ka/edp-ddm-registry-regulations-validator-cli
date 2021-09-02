package com.epam.digital.data.platform.registry.regulation.validation.cli.validator.typed;

import static com.google.common.collect.Lists.newArrayList;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNot.not;

import com.epam.digital.data.platform.registry.regulation.validation.cli.model.BpAuthConfiguration;
import com.epam.digital.data.platform.registry.regulation.validation.cli.model.BpAuthConfiguration.Authorization;
import com.epam.digital.data.platform.registry.regulation.validation.cli.model.BpAuthConfiguration.ProcessDefinition;
import com.epam.digital.data.platform.registry.regulation.validation.cli.validator.ValidationContext;
import org.junit.Test;

public class BpAuthProcessUniquenessValidatorTest {

  @Test
  public void shouldPassBpAuthDueToNoDuplicates() {
    var validator = new BpAuthProcessUniquenessValidator();

    var config = config(
        processDefinition("id1"),
        processDefinition("id2"),
        processDefinition("id3")
    );

    var errors = validator.validate(config, ValidationContext.empty());

    assertThat(errors, is(empty()));
  }

  @Test
  public void shouldFailBpAuthDueToDuplicates() {
    var validator = new BpAuthProcessUniquenessValidator();

    var config = config(
        processDefinition("id1"),
        processDefinition("id2"),
        processDefinition("id1")
    );

    var errors = validator.validate(config, ValidationContext.empty());

    assertThat(errors, is(not(empty())));
  }

  private BpAuthConfiguration.ProcessDefinition processDefinition(String processDefinitionId) {
    var processDefinition = new ProcessDefinition();
    processDefinition.setProcessDefinitionId(processDefinitionId);
    return processDefinition;
  }

  private BpAuthConfiguration config(BpAuthConfiguration.ProcessDefinition... processDefinitions) {
    var config = new BpAuthConfiguration();
    var authorization = new Authorization();
    authorization.setProcessDefinitions(newArrayList(processDefinitions));
    config.setAuthorization(authorization);
    return config;
  }
}