package com.mergebine.jopenai.mappers;

import com.mergebine.jopenai.api.v1.models.ModelResponse;
import com.mergebine.jopenai.model.Model;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface ModelMapper {

    ModelMapper INSTANCE = Mappers.getMapper(ModelMapper.class);

    Model fromApi(ModelResponse model);

    List<Model> fromApiList(List<ModelResponse> users);
}
