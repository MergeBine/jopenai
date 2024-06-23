package com.mergebine.jopenai.api.v1.models;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.time.Instant;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class ModelResponse {

    private String id;
    private String object;
    private Instant created;

    @JsonAlias("owned_by")
    private String owner;

}
