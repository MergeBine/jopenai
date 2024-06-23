package com.mergebine.jopenai.api.v1;

import lombok.Data;

@Data
public class ResponseWrapper<T> {

    private String object;
    private T data;
}
