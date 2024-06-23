package com.mergebine.jopenai;

import lombok.Value;

@Value
public class OpenAiApiConfig {

    String url;
    String token;
    String organization;
    String project;

    public OpenAiApiConfig(String url, String token, String organization, String project) {
        this.url = url.charAt(url.length() - 1) == '/'
                ? url.substring(0, url.length() - 1)
                : url;
        this.token = token;
        this.organization = organization;
        this.project = project;
    }


}
