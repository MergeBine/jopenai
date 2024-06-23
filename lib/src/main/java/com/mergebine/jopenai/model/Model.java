package com.mergebine.jopenai.model;

import lombok.Value;

import java.time.Instant;

@Value
public class Model {

    String id;
    String object;
    Instant created;
    String owner;
}
