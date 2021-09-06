package com.epam.digital.data.platform.registry.regulation.validation.cli.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
public class BpTrembitaConfiguration implements RegulationConfiguration {

  @JsonIgnore
  private File regulationFile;

  private Trembita trembita;

  @Data
  public static class Trembita {

    @JsonProperty(value = "process_definitions")
    private List<ProcessDefinition> processDefinitions = new ArrayList<>();
  }

  @Data
  @EqualsAndHashCode(of = {"processDefinitionId"})
  public static class ProcessDefinition implements Identifiable<String> {

    @JsonProperty("process_definition_id")
    private String processDefinitionId;

    @JsonProperty("start_vars")
    private List<String> startVars = new ArrayList<>();

    @JsonProperty("return_vars")
    private List<String> returnVars = new ArrayList<>();

    @JsonProperty("requires_signature")
    private boolean requiresSignature;

    @Override
    @JsonIgnore
    public String getId() {
      return processDefinitionId;
    }
  }
}