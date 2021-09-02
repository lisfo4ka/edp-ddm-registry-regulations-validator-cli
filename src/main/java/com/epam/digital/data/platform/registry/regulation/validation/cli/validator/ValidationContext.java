package com.epam.digital.data.platform.registry.regulation.validation.cli.validator;

import com.epam.digital.data.platform.registry.regulation.validation.cli.model.RegulationFileType;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ValidationContext {

  private RegulationFileType regulationFileType;

  public static ValidationContext of(RegulationFileType regulationFileType) {
    return new ValidationContext(regulationFileType);
  }

  public static ValidationContext empty() {
    return new ValidationContext();
  }
}
