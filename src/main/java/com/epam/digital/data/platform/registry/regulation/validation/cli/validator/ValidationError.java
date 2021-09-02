package com.epam.digital.data.platform.registry.regulation.validation.cli.validator;

import com.epam.digital.data.platform.registry.regulation.validation.cli.model.RegulationFileType;
import java.io.File;
import lombok.Builder;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

@Data
@Builder
public class ValidationError {

  private RegulationFileType regulationFileType;
  private File regulationFile;
  private String errorMessage;
  private String originalCauseMessage;

  public static ValidationError of(RegulationFileType regulationFileType, File regulationFile, String errorMessage) {
    return ValidationError.builder()
        .regulationFileType(regulationFileType)
        .regulationFile(regulationFile)
        .errorMessage(errorMessage)
        .build();
  }

  public static ValidationError of(RegulationFileType regulationFileType, File regulationFile, String errorMessage, Exception ex) {
    return ValidationError.builder()
        .regulationFileType(regulationFileType)
        .regulationFile(regulationFile)
        .errorMessage(errorMessage)
        .originalCauseMessage(ex.getMessage())
        .build();
  }

  @Override
  public String toString() {
    var regulationParentDirectory = getParentDirectoryIfAny(regulationFile);
    var regulationFileName = regulationFile.getName();

    var messageFormatBuilder = new StringBuilder(String.format("[%s]", regulationFileType));
    messageFormatBuilder.append(StringUtils.isBlank(regulationParentDirectory)
        ? String.format("[%s] ", regulationFileName)
        : String.format("[%s/%s] ", regulationParentDirectory, regulationFileName));
    messageFormatBuilder.append(StringUtils.isBlank(originalCauseMessage)
        ? String.format("%s", errorMessage)
        : String.format("%s: %s", errorMessage, originalCauseMessage));
    return messageFormatBuilder.toString();
  }

  private String getParentDirectoryIfAny(File regulationFile) {
    return regulationFile.getParentFile() != null ? regulationFile.getParentFile().getName() : null;
  }
}