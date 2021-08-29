package com.epam.digital.data.platform.registry.regulation.validation.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.ArrayList;
import java.util.List;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
public class BpTrembitaConfiguration implements RegulationConfiguration {

  @JsonIgnore
  private String regulationFileName;

  private Trembita trembita;

  @Data
  public static class Trembita {
    @JsonProperty(value = "process_definitions")
    private List<ProcessDefinition> processDefinitions = new ArrayList<>();
  }

  @Data
  @EqualsAndHashCode(of = {"processDefinitionId"})
  public static class ProcessDefinition {
    @JsonProperty("process_definition_id")
    private String processDefinitionId;

    @JsonProperty("start_vars")
    private List<String> startVars = new ArrayList<>();

    @JsonProperty("return_vars")
    private List<String> returnVars = new ArrayList<>();
  }
}