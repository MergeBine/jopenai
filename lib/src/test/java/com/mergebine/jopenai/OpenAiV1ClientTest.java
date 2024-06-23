package com.mergebine.jopenai;

import com.mergebine.jopenai.model.Error;
import com.mergebine.jopenai.model.Model;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockserver.integration.ClientAndServer;
import org.mockserver.model.Header;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockserver.model.HttpRequest.request;
import static org.mockserver.model.HttpResponse.response;

class OpenAiV1ClientTest {

    public static final String URL = "http://localhost:1080";
    public static final String BEARER_TOKEN = "3BpmhSCa8g56RWD9Twq4cuGpK7yJrjCWiBpQvt0x4zwB8XeEib4bmYt35Qj";
    public static final String ORGANIZATION_TOKEN = "org-XrQ7sDo3LcOIzQClx8j4l8Fi";
    public static final String PROJECT_TOKEN = "proj_XrQ7sDo3LcOIzQClx8j4l8Fi";

    static ClientAndServer mockServer;
    static OpenAiApiConfig config;
    static OpenAiClientFactory factory;
    static OpenAiClient openAiClient;

    @BeforeAll
    static void beforeAll() {
        config = new OpenAiApiConfig(URL, BEARER_TOKEN,
                ORGANIZATION_TOKEN, PROJECT_TOKEN);

        factory = new OpenAiClientFactory();

        openAiClient = factory.createV1Client(config);

        mockServer = ClientAndServer.startClientAndServer(1080);
    }

    @BeforeEach
    public void startServer() {
        mockServer.reset();
    }

    @AfterAll
    static void afterAll() throws Exception {
        mockServer.stop();
        openAiClient.close();
    }

    @Test
    void given200_whenGetModels_thenSuccess() throws Exception {
        //Given
        mockServer.when(
                        request()
                                .withMethod("GET")
                                .withPath("/v1/models")
                                .withHeader("Authorization", "Bearer " + BEARER_TOKEN)
                                .withHeader("OpenAI-Project", PROJECT_TOKEN)
                                .withHeader("OpenAI-Organization", ORGANIZATION_TOKEN))
                .respond(
                        response()
                                .withStatusCode(200)
                                .withHeaders(
                                        new Header("Content-Type", "application/json"))
                                .withBody("""
                                        {
                                            "object": "list",
                                            "data": [
                                                {
                                                    "id": "whisper-1",
                                                    "object": "model",
                                                    "created": 1677532384,
                                                    "owned_by": "openai-internal"
                                                }]
                                        }""")
                );

        //When
        List<Model> models = openAiClient.getModels();

        //Then
        assertEquals(1, models.size());

        Model model = models.getFirst();
        assertEquals("whisper-1", model.getId());
        assertEquals("model", model.getObject());
        assertEquals(1677532384000L, model.getCreated().toEpochMilli());
        assertEquals("openai-internal", model.getOwner());
    }

    @Test
    void given401_whenGetModels_thenSuccess() throws Exception {
        //Given
        mockServer.when(
                        request()
                                .withMethod("GET")
                                .withPath("/v1/models")
                                .withHeader("Authorization", "Bearer " + BEARER_TOKEN)
                                .withHeader("OpenAI-Project", PROJECT_TOKEN)
                                .withHeader("OpenAI-Organization", ORGANIZATION_TOKEN))
                .respond(
                        response()
                                .withStatusCode(401)
                                .withHeaders(
                                        new Header("Content-Type", "application/json"))
                                .withBody("""
                                        {
                                            "error": {
                                                "message": "OpenAI-Organization header should match organization for API key",
                                                "type": "invalid_request_error",
                                                "param": null,
                                                "code": "mismatched_organization"
                                            }
                                        }""")
                );

        //When
        OpenApiException openApiException = assertThrows(OpenApiException.class, () -> {
            openAiClient.getModels();
        });

        assertEquals("Request to GET /v1/models returned http code 401", openApiException.getMessage());
        Error error = openApiException.getError();
        assertEquals("OpenAI-Organization header should match organization for API key", error.getMessage());
        assertEquals("invalid_request_error", error.getType());
        assertNull(error.getParam());
        assertEquals("mismatched_organization", error.getCode());
    }

}