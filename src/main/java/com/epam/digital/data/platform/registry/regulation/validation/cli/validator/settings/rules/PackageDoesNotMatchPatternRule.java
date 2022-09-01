/*
 * Copyright 2022 EPAM Systems.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.epam.digital.data.platform.registry.regulation.validation.cli.validator.settings.rules;

import com.deliveredtechnologies.rulebook.RuleState;
import com.deliveredtechnologies.rulebook.annotation.Rule;
import com.deliveredtechnologies.rulebook.annotation.Then;
import com.deliveredtechnologies.rulebook.annotation.When;
import com.deliveredtechnologies.rulebook.spring.RuleBean;
import com.epam.digital.data.platform.registry.regulation.validation.cli.validator.ValidationError;
import com.epam.digital.data.platform.registry.regulation.validation.cli.validator.settings.RulesOrder;

@RuleBean
@Rule(order = RulesOrder.PACKAGE_DOES_NOT_MATCH_PATTERN_RULE)
public class PackageDoesNotMatchPatternRule extends AbstractSettingsValidationRule {

    private static final String PACKAGE_PATTERN = "^[a-z_][a-z0-9_]*(\\.[a-z_][a-z0-9_]*){0,100}$";

    @When
    public boolean isPackageIncorrect() {
        return !settingsYaml
                .getSettings()
                .getGeneral()
                .getBasePackageName()
                .matches(PACKAGE_PATTERN);
    }

    @Then
    public RuleState then() {
        errors.add(ValidationError.of(regulationFileType, regulationFile,
                "Package name should have next format: abc.de_1.ghi._123jkl. " +
                        "It can include lowercase Latin letters, numbers, and underscores. " +
                        "The subpacket name must not start with a number."));
        return RuleState.NEXT;
    }
}
