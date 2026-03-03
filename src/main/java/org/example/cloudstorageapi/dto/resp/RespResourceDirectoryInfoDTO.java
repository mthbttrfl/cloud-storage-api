package org.example.cloudstorageapi.dto.resp;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import org.example.cloudstorageapi.model.ResourceType;

@Schema(description = "Директория")
@Getter
public final class RespResourceDirectoryInfoDTO extends RespResourceInfoDTO {

    @Schema(description = "Тип", example = "DIRECTORY")
    private final ResourceType type;

    public RespResourceDirectoryInfoDTO(String path, String name) {
        super(path, name);
        this.type = ResourceType.DIRECTORY;
    }
}
