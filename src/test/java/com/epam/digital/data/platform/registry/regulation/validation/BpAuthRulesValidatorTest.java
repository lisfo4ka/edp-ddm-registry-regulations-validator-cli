package com.epam.digital.data.platform.registry.regulation.validation;

import static org.assertj.core.api.Assertions.assertThat;

import com.fasterxml.jackson.dataformat.yaml.YAMLMapper;
import java.io.File;
import org.junit.Before;
import org.junit.Test;

public class BpAuthRulesValidatorTest {

  private RegulationValidator<File> validator;

  @Before
  public void setUp() {
    this.validator = new BpAuthRulesValidator(new YAMLMapper());
  }

  @Test
  public void shouldFailDueToDuplicatedProcessDefinitionIds() {
    var bpAuthFile = getFileFromClasspath("registry-regulation/broken/bp-auth-duplicates.yml");

    var errors = validator.validate(bpAuthFile);

    assertThat(errors.isEmpty()).isFalse();
  }

  @Test
  public void shouldPassDueToNoDuplicatedProcessDefinitionIds() {
    var bpAuthFile = getFileFromClasspath("registry-regulation/correct/bp-auth.yml");

    var errors = validator.validate(bpAuthFile);

    assertThat(errors.isEmpty()).isTrue();
  }

  private File getFileFromClasspath(String filePath) {
    var classLoader = getClass().getClassLoader();
    return new File(classLoader.getResource(filePath).getFile());
  }
}