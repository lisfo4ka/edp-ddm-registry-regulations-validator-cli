package com.epam.digital.data.platform.registry.regulation.validation.cli.validator.typed;

import com.epam.digital.data.platform.registry.regulation.validation.cli.model.BpTrembitaConfiguration;
import com.epam.digital.data.platform.registry.regulation.validation.cli.model.Identifiable;
import java.util.List;

public class BpTrembitaProcessUniquenessValidator extends AbstractProcessUniquenessValidator<BpTrembitaConfiguration> {

  @Override
  protected List<? extends Identifiable> getProcessDefinitions(BpTrembitaConfiguration regulation) {
    return regulation.getTrembita().getProcessDefinitions();
  }
}
