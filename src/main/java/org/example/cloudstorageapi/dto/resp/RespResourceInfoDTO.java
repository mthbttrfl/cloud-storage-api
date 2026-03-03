package org.example.cloudstorageapi.dto.resp;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
@Schema(description = "Базовый класс для ресурсов", oneOf = {RespResourceDirectoryInfoDTO.class, RespResourceFileInfoDTO.class})
public abstract class RespResourceInfoDTO {

    @Schema(description = "Путь к ресурсу", example = "SamePath/")
    private final String path;

    @Schema(description = "Имя ресурса", example = "SameName")
    private final String name;
}
