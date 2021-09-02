package com.epam.digital.data.platform.registry.regulation.validation.cli.validator.typed;

import com.epam.digital.data.platform.registry.regulation.validation.cli.model.BpAuthConfiguration;
import com.epam.digital.data.platform.registry.regulation.validation.cli.model.Identifiable;
import java.util.List;

public class BpAuthProcessUniquenessValidator extends AbstractProcessUniquenessValidator<BpAuthConfiguration> {

  @Override
  protected List<? extends Identifiable> getProcessDefinitions(BpAuthConfiguration regulation) {
    return regulation.getAuthorization().getProcessDefinitions();
  }
}
