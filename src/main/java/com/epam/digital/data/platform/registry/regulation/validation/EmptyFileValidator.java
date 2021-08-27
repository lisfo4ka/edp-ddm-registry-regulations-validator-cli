package com.epam.digital.data.platform.registry.regulation.validation;

import static org.apache.commons.lang3.StringUtils.isBlank;

import com.epam.digital.data.platform.registry.regulation.validation.model.ValidationError;
import com.google.common.io.Files;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class EmptyFileValidator implements RegulationValidator<File> {

  @Override
  public Set<ValidationError> validate(File regulationFile ) {
    try {
      List<String> strLines = Files.readLines(regulationFile, StandardCharsets.UTF_8);
      if (isBlank(strLines.stream().collect(Collectors.joining()))) {
        return Collections.singleton(ValidationError.of(regulationFile, "File must not be empty"));
      }
      return Collections.emptySet();
    } catch (IOException ex) {
      return Collections.singleton(ValidationError.of(regulationFile, "File processing failure", ex));
    }
  }
}
