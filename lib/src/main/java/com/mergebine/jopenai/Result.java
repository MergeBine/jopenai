package com.mergebine.jopenai;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

import static java.util.Objects.nonNull;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class Result<T> {

    private final T result;
    private final OpenApiException exception;

    public static <T> Result<T> of(T result) {
        return new Result<T>(result, null);
    }

    public static <T> Result<T> ofException(OpenApiException exception) {
        return new Result<T>(null, exception);
    }

    public T get() throws OpenApiException {
        if (nonNull(exception)) {
            throw exception;
        }
        return result;
    }
}
