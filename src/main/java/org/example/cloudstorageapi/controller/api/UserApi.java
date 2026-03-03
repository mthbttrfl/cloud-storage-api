package org.example.cloudstorageapi.controller.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.example.cloudstorageapi.dto.resp.RespMessageDTO;
import org.example.cloudstorageapi.dto.resp.RespUsernameDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;

@Tag(
        name = "User",
        description = "Информация о текущем пользователе"
)
public interface UserApi<U extends UserDetails> {

    @Operation(
            summary = "Текущий пользователь",
            description = "Возвращает информацию об аутентифицированном пользователе"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Информация о юзере", content = @Content(schema = @Schema(implementation = RespUsernameDTO.class))),
            @ApiResponse(responseCode = "401", description = "Пользователь не авторизован", content = @Content(schema = @Schema(implementation = RespMessageDTO.class))),
            @ApiResponse(responseCode = "500", description = "Неизвестная ошибка", content = @Content(schema = @Schema(implementation = RespMessageDTO.class)))
    })
    ResponseEntity<RespUsernameDTO> me(
            @Parameter(hidden = true)
            U userDetails
    );
}
