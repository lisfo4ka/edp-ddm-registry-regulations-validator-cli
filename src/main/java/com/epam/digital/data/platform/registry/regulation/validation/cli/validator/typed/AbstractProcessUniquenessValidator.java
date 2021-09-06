package com.epam.digital.data.platform.registry.regulation.validation.cli.validator.typed;

import com.epam.digital.data.platform.registry.regulation.validation.cli.model.Identifiable;
import com.epam.digital.data.platform.registry.regulation.validation.cli.model.RegulationConfiguration;
import com.epam.digital.data.platform.registry.regulation.validation.cli.validator.RegulationValidator;
import com.epam.digital.data.platform.registry.regulation.validation.cli.validator.ValidationContext;
import com.epam.digital.data.platform.registry.regulation.validation.cli.validator.ValidationError;
import com.google.common.collect.Sets;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public abstract class AbstractProcessUniquenessValidator<T extends RegulationConfiguration> implements RegulationValidator<T> {

  private static final String DUPLICATES_ERROR_MSG_FORMAT = "Duplicated process definitions found: %s";

  @Override
  public Set<ValidationError> validate(T regulation, ValidationContext context) {
    var regulationFileType = context.getRegulationFileType();
    var regulationFile = regulation.getRegulationFile();
    var processDefinitions = getProcessDefinitions(regulation);
    var duplicateItems = findDuplicates(processDefinitions);

    if (duplicateItems.isEmpty()) {
      return Collections.emptySet();
    }

    var joinedDuplicatedIds = duplicateItems.stream()
        .map(item -> String.format("'%s'", item.getId()))
        .collect(Collectors.joining(","));

    var validationError = ValidationError.builder()
        .regulationFileType(regulationFileType)
        .regulationFile(regulationFile)
        .errorMessage(String.format(DUPLICATES_ERROR_MSG_FORMAT, joinedDuplicatedIds))
        .build();

    return Collections.singleton(validationError);
  }

  protected abstract List<? extends Identifiable> getProcessDefinitions(T regulation);

  private <T extends Identifiable> Set<Identifiable> findDuplicates(List<T> identifiableItems) {
    var duplicates = Sets.newHashSet();
    return identifiableItems.stream()
        .filter(n -> !duplicates.add(n.getId()))
        .collect(Collectors.toSet());
  }
}
