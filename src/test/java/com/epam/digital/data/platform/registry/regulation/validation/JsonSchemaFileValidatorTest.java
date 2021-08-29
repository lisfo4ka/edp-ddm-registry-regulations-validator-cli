package com.epam.digital.data.platform.registry.regulation.validation;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNot.not;

import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLMapper;
import com.networknt.schema.JsonSchema;
import com.networknt.schema.JsonSchemaFactory;
import com.networknt.schema.SpecVersion.VersionFlag;
import java.io.File;
import lombok.SneakyThrows;
import org.junit.Before;
import org.junit.Test;
import org.springframework.core.io.ClassRelativeResourceLoader;
import org.springframework.core.io.ResourceLoader;

public class JsonSchemaFileValidatorTest {

  private final ResourceLoader resourceLoader = new ClassRelativeResourceLoader(getClass());

  private RegulationValidator<File> validator;

  @Before
  public void setUp() {
    this.validator = new JsonSchemaFileValidator(jsonSchemaOf("classpath:schema/bp-auth-schema.json"), new YAMLMapper());
  }

  @Test
  public void shouldPassSchemaValidation() {
    var processFile = getFileFromClasspath("registry-regulation/correct/bp-auth.yml");

    var errors = validator.validate(processFile);

    assertThat(errors, is(empty()));
  }

  @Test
  public void shouldFailSchemaValidation() {
    var processFile = getFileFromClasspath("registry-regulation/broken/bp-auth-broken.yml");

    var errors = validator.validate(processFile);

    assertThat(errors, is(not(empty())));
  }

  private File getFileFromClasspath(String filePath) {
    var classLoader = getClass().getClassLoader();
    return new File(classLoader.getResource(filePath).getFile());
  }

  @SneakyThrows
  private JsonSchema jsonSchemaOf(String jsonSchemaLocation) {
    var resource = resourceLoader.getResource(jsonSchemaLocation);
    var factory = JsonSchemaFactory
        .builder(JsonSchemaFactory.getInstance(VersionFlag.V4))
        .objectMapper(new JsonMapper())
        .build();
    return factory.getSchema(resource.getInputStream());
  }
}