/*
 * Copyright 2021 EPAM Systems.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.epam.digital.data.platform.registry.regulation.validation.cli.validator.file;

import com.epam.digital.data.platform.registry.regulation.validation.cli.validator.RegulationValidator;
import com.epam.digital.data.platform.registry.regulation.validation.cli.validator.ValidationContext;
import com.epam.digital.data.platform.registry.regulation.validation.cli.validator.ValidationError;
import java.io.File;
import java.util.Collections;
import java.util.Set;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.Assert;

@Slf4j
public class FileValidatorLoggingDecorator implements RegulationValidator<File> {

  private final RegulationValidator<File> validator;

  private FileValidatorLoggingDecorator(RegulationValidator<File> validator) {
    this.validator = validator;
  }

  public static RegulationValidator<File> wrap(RegulationValidator<File> validator) {
    Assert.notNull(validator, "Validator must be not NULL");

    return new FileValidatorLoggingDecorator(validator);
  }

  @Override
  public Set<ValidationError> validate(File regulationFile, ValidationContext validationContext) {
    log.info("[{}] Starting validation of regulation file.", regulationFile.getName());
    var errors = this.validator.validate(regulationFile, validationContext);

    if (errors.isEmpty()) {
      log.info("[{}] Regulation file passed validation successfully.", regulationFile.getName());
      return Collections.emptySet();
    }

    log.error("[{}] Regulation file FAILED validation.", regulationFile.getName());
    return errors;
  }
}
