package org.example.cloudstorageapi.dto.resp;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;
@Schema(description = "Сообщения об ошибках")
public record RespMessagesDTO(
        @Schema(
                description = "Сообщения",
                example = "[Same messages...]"
        )
        List<String> message) {}
