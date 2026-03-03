package org.example.cloudstorageapi.controller.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.example.cloudstorageapi.dto.resp.RespMessageDTO;
import org.example.cloudstorageapi.dto.resp.RespResourceDirectoryInfoDTO;
import org.example.cloudstorageapi.dto.resp.RespResourceFileInfoDTO;
import org.example.cloudstorageapi.dto.resp.RespResourceInfoDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;

@Tag(
        name = "Directories",
        description = "Работа с директориями пользователя"
)
public interface DirectoryApi<U extends UserDetails> {

    @Operation(
            summary = "Получить содержимое директории",
            description = "Возвращает список файлов и папок по указанному пути"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Данные из директории найдены",
                    content = @Content(
                            array = @ArraySchema(
                                    schema = @Schema(
                                            anyOf = {RespResourceFileInfoDTO.class, RespResourceDirectoryInfoDTO.class})))),
            @ApiResponse(responseCode = "400", description = "Ошибка валидации", content = @Content(schema = @Schema(implementation = RespMessageDTO.class))),
            @ApiResponse(responseCode = "401", description = "Пользователь не авторизован", content = @Content(schema = @Schema(implementation = RespMessageDTO.class))),
            @ApiResponse(responseCode = "404", description = "Папки не существует", content = @Content(schema = @Schema(implementation = RespMessageDTO.class))),
            @ApiResponse(responseCode = "500", description = "Неизвестная ошибка", content = @Content(schema = @Schema(implementation = RespMessageDTO.class)))
    })
    ResponseEntity<List<RespResourceInfoDTO>> getObjectsInfo(
            @Parameter(
                    description = "Путь к директории",
                    example = "SameSegment/SameSegment/",
                    required = true
            )
            String path,

            @Parameter(hidden = true)
            U userDetails
    );

    @Operation(
            summary = "Создать директорию",
            description = "Создаёт новую директорию по указанному пути"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Директория создана", content = @Content(schema = @Schema(implementation = RespResourceDirectoryInfoDTO.class))),
            @ApiResponse(responseCode = "400", description = "Ошибка валидации", content = @Content(schema = @Schema(implementation = RespMessageDTO.class))),
            @ApiResponse(responseCode = "401", description = "Пользователь не авторизован", content = @Content(schema = @Schema(implementation = RespMessageDTO.class))),
            @ApiResponse(responseCode = "404", description = "Родительской папки не существует", content = @Content(schema = @Schema(implementation = RespMessageDTO.class))),
            @ApiResponse(responseCode = "409", description = "Папка уже существует", content = @Content(schema = @Schema(implementation = RespMessageDTO.class))),
            @ApiResponse(responseCode = "500", description = "Неизвестная ошибка", content = @Content(schema = @Schema(implementation = RespMessageDTO.class))),
    })
    ResponseEntity<RespResourceInfoDTO> create(
            @Parameter(
                    description = "Путь для новой директории",
                    example = "SameSegment/SameNewFolderName/",
                    required = true
            )
            String path,

            @Parameter(hidden = true)
            U userDetails
    );
}