package com.epam.digital.data.platform.registry.regulation.validation;

import static org.assertj.core.api.Assertions.assertThat;

import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLMapper;
import java.io.File;
import org.junit.Test;
import org.springframework.core.io.ClassRelativeResourceLoader;
import org.springframework.core.io.ResourceLoader;

public class JsonSchemaRegistryRegulationValidatorTest {

  private RegistryRegulationValidator<File> validator;

  private ResourceLoader resourceLoader = new ClassRelativeResourceLoader(getClass());

  @Test
  public void shouldPassSchemaValidation() {
    var processFile = getFileFromClasspath("registry-regulation/correct/bp-auth.yml");

    var registryRegulationValidatorFactory = new RegistryRegulationValidatorFactory(resourceLoader, new YAMLMapper(), new JsonMapper());

    validator = registryRegulationValidatorFactory.newBpAuthJsonSchemaValidator();

    var errors = validator.validate(processFile);

    assertThat(errors.isEmpty()).isTrue();
  }

  @Test
  public void shouldFailSchemaValidation() {
    var processFile = getFileFromClasspath("registry-regulation/broken/bp-auth-broken.yml");

    var registryRegulationValidatorFactory = new RegistryRegulationValidatorFactory(resourceLoader, new YAMLMapper(), new JsonMapper());

    validator = registryRegulationValidatorFactory.newBpAuthJsonSchemaValidator();

    var errors = validator.validate(processFile);

    assertThat(errors.isEmpty()).isFalse();
  }

  private File getFileFromClasspath(String filePath) {
    var classLoader = getClass().getClassLoader();
    return new File(classLoader.getResource(filePath).getFile());
  }
}