package org.example.cloudstorageapi.dto.resp;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import org.example.cloudstorageapi.model.ResourceType;

@Schema(description = "Файл")
@Getter
public final class RespResourceFileInfoDTO extends RespResourceInfoDTO {

    @Schema(description = "Размер файла в байтах", example = "123")
    private final Long size;

    @Schema(description = "Тип", example = "FILE")
    private final ResourceType type;

    public RespResourceFileInfoDTO(String path, String name, Long size) {
        super(path, name);
        this.type = ResourceType.FILE;
        this.size = size;
    }
}
