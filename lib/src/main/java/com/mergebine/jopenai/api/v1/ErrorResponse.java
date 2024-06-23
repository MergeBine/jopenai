package com.mergebine.jopenai.api.v1;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import org.jetbrains.annotations.Nullable;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class ErrorResponse {

    private String message;
    private String type;

    @Nullable
    private String param;

    private String code;

}
