package com.mergebine.jopenai;

import org.apache.hc.client5.http.impl.io.PoolingHttpClientConnectionManager;

public class OpenAiClientFactory {

    public static final int DEFAULT_MAX_TOTAL_CONNECTIONS = 100;
    public static final int DEFAULT_MAX_CONNECTIONS_PER_ROUTE = 10;

    public OpenAiClient createV1Client(OpenAiApiConfig config) {

        return new OpenAiV1Client(config, createHttpClientPool(DEFAULT_MAX_TOTAL_CONNECTIONS, DEFAULT_MAX_CONNECTIONS_PER_ROUTE));
    }

    public OpenAiClient createV1Client(OpenAiApiConfig config, PoolingHttpClientConnectionManager connectionManager) {

        return new OpenAiV1Client(config, connectionManager);
    }

    private PoolingHttpClientConnectionManager createHttpClientPool(int maxTotalConnections, int maxConnectionsPerRoute) {
        PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager();
        connectionManager.setMaxTotal(maxTotalConnections);
        connectionManager.setDefaultMaxPerRoute(maxConnectionsPerRoute);

        return connectionManager;
    }
}
