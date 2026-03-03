package org.example.cloudstorageapi.controller.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.cloudstorageapi.dto.req.ReqUserDTO;
import org.example.cloudstorageapi.dto.resp.RespMessageDTO;
import org.example.cloudstorageapi.dto.resp.RespMessagesDTO;
import org.example.cloudstorageapi.dto.resp.RespUsernameDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseStatus;

@Tag(
        name = "Authentication",
        description = "Регистрация и аутентификация пользователей"
)
public interface AuthApi {

    @Operation(
            summary = "Регистрация пользователя",
            description = """
            Создаёт нового пользователя и автоматически аутентифицирует его.
            В случае успеха создаётся HTTP-сессия.
            """
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Успешная регистрация", content = @Content(schema = @Schema(implementation = RespUsernameDTO.class))),
            @ApiResponse(responseCode = "400", description = "Ошибка валидации", content = @Content(schema = @Schema(implementation = RespMessagesDTO.class))),
            @ApiResponse(responseCode = "409", description = "Конфликт имен", content = @Content(schema = @Schema(implementation = RespMessageDTO.class))),
            @ApiResponse(responseCode = "500", description = "Неизвестная ошибка", content = @Content(schema = @Schema(implementation = RespMessageDTO.class)))
    })
    ResponseEntity<RespUsernameDTO> signUp(
            @RequestBody(
                    description = "Данные пользователя",
                    required = true,
                    content = @Content(schema = @Schema(implementation = ReqUserDTO.class))
            )
            ReqUserDTO req,

            @Parameter(hidden = true)
            HttpServletRequest request,

            @Parameter(hidden = true)
            HttpServletResponse response
    );

    @Operation(
            summary = "Вход пользователя",
            description = "Аутентифицирует пользователя и создаёт HTTP-сессию"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Успешный вход", content = @Content(schema = @Schema(implementation = RespUsernameDTO.class))),
            @ApiResponse(responseCode = "400", description = "Ошибка валидации", content = @Content(schema = @Schema(implementation = RespMessagesDTO.class))),
            @ApiResponse(responseCode = "401", description = "Неверные данные пользователя", content = @Content(schema = @Schema(implementation = RespMessageDTO.class))),
            @ApiResponse(responseCode = "500", description = "Неизвестная ошибка", content = @Content(schema = @Schema(implementation = RespMessageDTO.class)))
    })
    ResponseEntity<RespUsernameDTO> signIn(
            @RequestBody(
                    description = "Логин и пароль пользователя",
                    required = true,
                    content = @Content(schema = @Schema(implementation = ReqUserDTO.class))
            )
            ReqUserDTO req,

            @Parameter(hidden = true)
            HttpServletRequest request,

            @Parameter(hidden = true)
            HttpServletResponse response
    );

    @Operation(
            summary = "Выход пользователя",
            description = "При выходе удаляется HTTP-сессия пользователя"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Успешный вход"),
            @ApiResponse(responseCode = "401", description = "Пользователь не авторизован", content = @Content(schema = @Schema(implementation = RespMessageDTO.class))),
            @ApiResponse(responseCode = "500", description = "Неизвестная ошибка", content = @Content(schema = @Schema(implementation = RespMessageDTO.class)))
    })
    @PostMapping("/sign-out")
    default void signOut(){}
}
