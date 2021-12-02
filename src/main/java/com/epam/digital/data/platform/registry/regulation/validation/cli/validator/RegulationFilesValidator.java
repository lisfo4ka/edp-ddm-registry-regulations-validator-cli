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

package com.epam.digital.data.platform.registry.regulation.validation.cli.validator;

import com.epam.digital.data.platform.registry.regulation.validation.cli.model.RegulationFileType;
import com.epam.digital.data.platform.registry.regulation.validation.cli.model.RegulationFiles;
import java.io.File;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

public class RegulationFilesValidator implements RegulationValidator<RegulationFiles> {

  private final Map<RegulationFileType, RegulationValidator<File>> regulationTypeValidators;

  public RegulationFilesValidator(Map<RegulationFileType, RegulationValidator<File>> regulationTypeValidators) {
    this.regulationTypeValidators = regulationTypeValidators;
  }

  @Override
  public Set<ValidationError> validate(RegulationFiles regulationFiles, ValidationContext context) {
    var errors = new LinkedHashSet<ValidationError>();

    regulationFiles.getBpAuthFiles().forEach(file -> errors.addAll(validate(file, RegulationFileType.BP_AUTH)));

    regulationFiles.getBpTrembitaFiles().forEach(file -> errors.addAll(validate(file, RegulationFileType.BP_TREMBITA)));

    regulationFiles.getBpTrembitaConfig().forEach(file -> errors.addAll(validate(file, RegulationFileType.BP_TREMBITA_CONFIG)));

    regulationFiles.getGlobalVarsFiles().forEach(file -> errors.addAll(validate(file, RegulationFileType.GLOBAL_VARS)));

    regulationFiles.getRolesFiles().forEach(file -> errors.addAll(validate(file, RegulationFileType.ROLES)));

    regulationFiles.getBpmnFiles().forEach(file -> errors.addAll(validate(file, RegulationFileType.BPMN)));

    regulationFiles.getDmnFiles().forEach(file -> errors.addAll(validate(file, RegulationFileType.DMN)));

    regulationFiles.getFormFiles().forEach(file -> errors.addAll(validate(file, RegulationFileType.FORMS)));

    return errors;
  }

  private Set<ValidationError> validate(File file, RegulationFileType regulationFileType) {
    var validator = this.regulationTypeValidators.get(regulationFileType);
    return validator.validate(file, ValidationContext.of(regulationFileType));
  }
}
