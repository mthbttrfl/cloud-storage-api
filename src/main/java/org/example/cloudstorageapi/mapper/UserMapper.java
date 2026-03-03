package org.example.cloudstorageapi.mapper;

import org.example.cloudstorageapi.dto.req.ReqUserDTO;
import org.example.cloudstorageapi.model.UserEntity;
import org.example.cloudstorageapi.security.CustomUserDetails;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(target = "username", source = "reqUserDTO.username")
    @Mapping(target = "password", source = "hashPassword")
    UserEntity toEntity(ReqUserDTO reqUserDTO, String hashPassword);

    CustomUserDetails toDetails(UserEntity user);
}