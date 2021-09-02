package com.epam.digital.data.platform.registry.regulation.validation.cli.validator.file;

import com.epam.digital.data.platform.registry.regulation.validation.cli.validator.RegulationValidator;
import com.epam.digital.data.platform.registry.regulation.validation.cli.validator.ValidationContext;
import com.epam.digital.data.platform.registry.regulation.validation.cli.validator.ValidationError;
import com.google.common.io.Files;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.Set;
import org.apache.commons.lang3.StringUtils;

public class EmptyFileValidator implements RegulationValidator<File> {

  @Override
  public Set<ValidationError> validate(File regulationFile, ValidationContext validationContext) {
    try {
      var content = readFileContent(regulationFile);
      if (StringUtils.isBlank(content)) {
        return singleError("File must not be empty", regulationFile, validationContext);
      }
      return Collections.emptySet();
    } catch (IOException ex) {
      return singleError("File processing failure", ex, regulationFile, validationContext);
    }
  }

  private String readFileContent(File regulationFile) throws IOException {
    var strLines = Files.readLines(regulationFile, StandardCharsets.UTF_8);
    return String.join("", strLines);
  }

  private Set<ValidationError> singleError(String errorMessage, Exception ex, File regulationFile, ValidationContext validationContext) {
    return Collections.singleton(
        ValidationError.of(validationContext.getRegulationFileType(), regulationFile, errorMessage, ex)
    );
  }

  private Set<ValidationError> singleError(String errorMessage, File regulationFile, ValidationContext validationContext) {
    return Collections.singleton(
        ValidationError.of(validationContext.getRegulationFileType(), regulationFile, errorMessage)
    );
  }
}
