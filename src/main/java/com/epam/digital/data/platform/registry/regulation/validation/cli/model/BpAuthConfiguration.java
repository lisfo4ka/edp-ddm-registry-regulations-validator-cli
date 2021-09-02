package com.epam.digital.data.platform.registry.regulation.validation.cli.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
public class BpAuthConfiguration implements RegulationConfiguration {

  @JsonIgnore
  private File regulationFile;

  private Authorization authorization;

  @Data
  public static class Authorization {

    private String realm;

    @JsonProperty(value = "process_definitions")
    private List<ProcessDefinition> processDefinitions = new ArrayList<>();
  }

  @Data
  @EqualsAndHashCode(of = {"processDefinitionId"})
  public static class ProcessDefinition implements Identifiable<String> {

    @JsonProperty("process_definition_id")
    private String processDefinitionId;

    @JsonProperty("process_name")
    private String processName;

    @JsonProperty("process_description")
    private String processDescription;

    private List<String> roles = new ArrayList<>();

    @Override
    @JsonIgnore
    public String getId() {
      return processDefinitionId;
    }
  }
}