package com.mergebine.jopenai;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.mergebine.jopenai.api.v1.ErrorWrapper;
import com.mergebine.jopenai.api.v1.ResponseWrapper;
import com.mergebine.jopenai.api.v1.models.ModelResponse;
import com.mergebine.jopenai.mappers.ErrorMapper;
import com.mergebine.jopenai.mappers.ModelMapper;
import com.mergebine.jopenai.model.Error;
import com.mergebine.jopenai.model.Model;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.client5.http.impl.io.PoolingHttpClientConnectionManager;
import org.apache.hc.core5.http.ClassicHttpRequest;
import org.apache.hc.core5.http.HttpHeaders;
import org.apache.hc.core5.http.ParseException;
import org.apache.hc.core5.http.io.entity.EntityUtils;
import org.apache.hc.core5.http.io.support.ClassicRequestBuilder;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.List;

public class OpenAiV1Client implements OpenAiClient {

    private final OpenAiApiConfig openAiApiConfig;
    private final PoolingHttpClientConnectionManager connectionManager;
    private final CloseableHttpClient httpClient;

    private final ObjectMapper objectMapper;

    protected OpenAiV1Client(OpenAiApiConfig openAiApiConfig, PoolingHttpClientConnectionManager connectionManager) {
        this.connectionManager = connectionManager;
        this.openAiApiConfig = openAiApiConfig;
        httpClient = HttpClients.custom()
                .setConnectionManager(connectionManager)
                .build();

        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
    }

    @Override
    public List<Model> getModels() throws IOException, OpenApiException {

        String endpoint = "/v1/models";

        ClassicRequestBuilder requestBuilder = ClassicRequestBuilder.get();
        setConfigHeaders(requestBuilder, endpoint);
        ClassicHttpRequest request = requestBuilder.build();

        try (CloseableHttpResponse response = httpClient.execute(request)) {

            String responseBody = EntityUtils.toString(response.getEntity());
            if (response.getCode() / 100 != 2) {
                ErrorWrapper apiError = objectMapper.readValue(responseBody, ErrorWrapper.class);
                Error error = ErrorMapper.INSTANCE.fromApi(apiError.getError());
                throw new OpenApiException(createNotSuccessfulExceptionMessage(requestBuilder, response), error);
            }

            TypeReference<ResponseWrapper<List<ModelResponse>>> typeRef = new TypeReference<>() {
            };
            ResponseWrapper<List<ModelResponse>> apiResponse = objectMapper.readValue(responseBody, typeRef);
            return ModelMapper.INSTANCE.fromApiList(apiResponse.getData());
        } catch (ParseException | JsonProcessingException e) {
            throw new OpenApiException("Could not parse response from " + requestBuilder.getMethod() + " " + requestBuilder.getPath(), e);
        }
    }

    private void setConfigHeaders(ClassicRequestBuilder classicRequestBuilder, String endpoint) {
        classicRequestBuilder
                .setUri(openAiApiConfig.getUrl() + endpoint)
                .setHeader("OpenAI-Organization", openAiApiConfig.getOrganization())
                .setHeader("OpenAI-Project", openAiApiConfig.getProject())
                .addHeader(HttpHeaders.AUTHORIZATION, "Bearer " + openAiApiConfig.getToken());
    }

    private static @NotNull String createNotSuccessfulExceptionMessage(ClassicRequestBuilder requestBuilder, CloseableHttpResponse response) {
        return "Request to " + requestBuilder.getMethod() + " " + requestBuilder.getPath() + " returned http code " + response.getCode();
    }

    @Override
    public void close() throws Exception {
        try {
            httpClient.close();
        } finally {
            connectionManager.close();
        }
    }
}
