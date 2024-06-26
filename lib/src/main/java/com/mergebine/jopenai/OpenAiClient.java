package com.mergebine.jopenai;

import com.mergebine.jopenai.model.Model;

import java.io.IOException;
import java.util.List;

public interface OpenAiClient extends AutoCloseable {

    Result<List<Model>> getModels() throws IOException;
}
