package com.epam.digital.data.platform.registry.regulation.validation.cli.validator;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.epam.digital.data.platform.registry.regulation.validation.cli.model.BpAuthConfiguration;
import com.epam.digital.data.platform.registry.regulation.validation.cli.support.RegulationConfigurationLoader;
import java.io.File;
import org.junit.Test;

public class TypedConfigurationValidatorTest {

  @Test
  @SuppressWarnings("unchecked")
  public void shouldLoadTypedConfigAndValidate() {
    var someFile = new File("");
    var bpAuthConfiguration = new BpAuthConfiguration();
    var validationContext = ValidationContext.empty();

    var bpAuthFileValidator = mock(RegulationValidator.class);
    var configurationLoader = mock(RegulationConfigurationLoader.class);

    when(configurationLoader.load(someFile, BpAuthConfiguration.class)).thenReturn(bpAuthConfiguration);

    var validator = TypedConfigurationValidator.<BpAuthConfiguration>builder()
        .configurationClass(BpAuthConfiguration.class)
        .configurationLoader(configurationLoader)
        .validator(bpAuthFileValidator)
        .build();

    validator.validate(someFile, validationContext);

    verify(configurationLoader, times(1)).load(someFile, BpAuthConfiguration.class);
    verify(bpAuthFileValidator, times(1)).validate(bpAuthConfiguration, validationContext);
  }
}