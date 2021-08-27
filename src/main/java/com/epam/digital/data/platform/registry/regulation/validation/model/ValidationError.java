package com.epam.digital.data.platform.registry.regulation.validation.model;

import java.io.File;
import lombok.Builder;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

@Data
@Builder
public class ValidationError {

  private String regulationFileName;
  private String errorMessage;
  private String originalCauseMessage;

  public static ValidationError of(File regulationFile, String errorMessage) {
    return ValidationError.builder()
        .regulationFileName(regulationFile.getName())
        .errorMessage(errorMessage)
        .build();
  }

  public static ValidationError of(File regulationFile, String errorMessage, Exception ex) {
    return ValidationError.builder()
        .regulationFileName(regulationFile.getName())
        .errorMessage(errorMessage)
        .originalCauseMessage(ex.getMessage())
        .build();
  }

  @Override
  public String toString() {
    if (StringUtils.isBlank(originalCauseMessage)) {
      return String.format("[%s] %s", regulationFileName, errorMessage);
    }
    return String.format("[%s] %s: %s", regulationFileName, errorMessage, originalCauseMessage);
  }
}