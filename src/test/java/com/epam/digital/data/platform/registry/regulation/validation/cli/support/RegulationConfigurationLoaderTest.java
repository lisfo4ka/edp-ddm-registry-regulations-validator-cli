package com.epam.digital.data.platform.registry.regulation.validation.cli.support;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNot.not;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.epam.digital.data.platform.registry.regulation.validation.cli.model.BpAuthConfiguration;
import com.epam.digital.data.platform.registry.regulation.validation.cli.model.BpTrembitaConfiguration;
import com.fasterxml.jackson.dataformat.yaml.YAMLMapper;
import java.io.File;
import java.io.IOException;
import org.junit.Test;

public class RegulationConfigurationLoaderTest {

  @Test
  public void shouldPassForBpAuthFile() {
    var configurationLoader = new RegulationConfigurationLoader(new YAMLMapper());
    var bpAuthFile = getFileFromClasspath("registry-regulation/correct/bp-auth.yml");

    var bpAuthConfiguration = configurationLoader.load(bpAuthFile, BpAuthConfiguration.class);

    assertThat(bpAuthConfiguration.getRegulationFile().getName(), is("bp-auth.yml"));
    assertThat(bpAuthConfiguration.getAuthorization(), is(notNullValue()));
    assertThat(bpAuthConfiguration.getAuthorization().getProcessDefinitions(), is(not(empty())));
  }

  @Test
  public void shouldPassForBpTrembitaFile() {
    var configurationLoader = new RegulationConfigurationLoader(new YAMLMapper());
    var bpTrembitaFile = getFileFromClasspath("registry-regulation/correct/bp-trembita.yml");

    var bpTrembitaConfiguration = configurationLoader.load(bpTrembitaFile, BpTrembitaConfiguration.class);

    assertThat(bpTrembitaConfiguration.getRegulationFile().getName(), is("bp-trembita.yml"));
    assertThat(bpTrembitaConfiguration.getTrembita(), is(notNullValue()));
    assertThat(bpTrembitaConfiguration.getTrembita().getProcessDefinitions(), is(not(empty())));
  }

  @Test
  public void shouldFailForUnknownConfiguration() {
    var configurationLoader = new RegulationConfigurationLoader(new YAMLMapper());
    var unknownFile = getFileFromClasspath("registry-regulation/correct/roles.yml");

    assertThrows(IOException.class, () -> configurationLoader.load(unknownFile, BpTrembitaConfiguration.class));
  }

  private File getFileFromClasspath(String filePath) {
    var classLoader = getClass().getClassLoader();
    return new File(classLoader.getResource(filePath).getFile());
  }
}