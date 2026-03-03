package org.example.cloudstorageapi.dto.resp;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Сообщение об ошибке")
public record RespMessageDTO(
        @Schema(
                description = "Сообщение",
                example = "Same message"
        )
        String message) {}
