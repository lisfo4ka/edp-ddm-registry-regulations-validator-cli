package com.epam.digital.data.platform.registry.regulation.validation.model;

import lombok.Builder;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

@Data
@Builder
public class ValidationError {

  private RegulationFileType regulationFileType;
  private String regulationFileName;
  private String errorMessage;
  private String originalCauseMessage;

  @Override
  public String toString() {
    if (StringUtils.isBlank(originalCauseMessage)) {
      return String.format("[%s] '%s': %s", regulationFileType, regulationFileName, errorMessage);
    }
    return String.format("[%s] '%s': %s -> %s", regulationFileType, regulationFileName, errorMessage, originalCauseMessage);
  }
}