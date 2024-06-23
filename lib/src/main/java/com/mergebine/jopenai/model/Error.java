package com.mergebine.jopenai.model;

import lombok.Value;
import org.jetbrains.annotations.Nullable;

@Value
public class Error {

    String message;
    String type;

    @Nullable
    String param;

    String code;
}
