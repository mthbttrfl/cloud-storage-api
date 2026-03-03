package org.example.cloudstorageapi.dto.resp;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Имя пользователя")
public record RespUsernameDTO(
        @Schema(
                description = "Имя",
                example = "username"
        )
        String username) {
}
