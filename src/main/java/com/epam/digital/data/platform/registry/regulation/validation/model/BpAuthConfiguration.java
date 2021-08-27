package com.epam.digital.data.platform.registry.regulation.validation.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.ArrayList;
import java.util.List;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
public class BpAuthConfiguration {

  @JsonIgnore
  private String regulationFileName;

  private Authorization authorization;

  @Data
  public static class Authorization {
    private String realm;

    @JsonProperty(value = "process_definitions")
    private List<ProcessDefinition> processDefinitions = new ArrayList<>();
  }

  @Data
  @EqualsAndHashCode(of = {"processDefinitionId"})
  public static class ProcessDefinition {
    @JsonProperty("process_definition_id")
    private String processDefinitionId;

    @JsonProperty("process_name")
    private String processName;

    @JsonProperty("process_description")
    private String processDescription;

    private List<String> roles = new ArrayList<>();
  }
}