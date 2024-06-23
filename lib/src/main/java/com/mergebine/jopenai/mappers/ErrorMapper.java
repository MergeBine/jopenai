package com.mergebine.jopenai.mappers;

import com.mergebine.jopenai.api.v1.ErrorResponse;
import com.mergebine.jopenai.model.Error;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface ErrorMapper {

    ErrorMapper INSTANCE = Mappers.getMapper(ErrorMapper.class);

    Error fromApi(ErrorResponse model);
}
