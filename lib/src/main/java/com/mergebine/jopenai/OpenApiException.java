package com.mergebine.jopenai;

import com.mergebine.jopenai.model.Error;
import org.jetbrains.annotations.Nullable;

import java.io.Serial;

public class OpenApiException extends Exception {

    @Serial
    private static final long serialVersionUID = -3387516993124229948L;

    @Nullable
    private Error error;

    public OpenApiException(String message) {
        super(message);
    }

    public OpenApiException(String message, Error error) {
        super(message);
        this.error = error;
    }

    public OpenApiException(String message, Throwable cause) {
        super(message, cause);
    }

    @Nullable
    public Error getError() {
        return error;
    }
}
