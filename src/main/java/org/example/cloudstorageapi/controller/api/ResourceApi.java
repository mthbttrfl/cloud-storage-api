package org.example.cloudstorageapi.controller.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import org.example.cloudstorageapi.dto.resp.RespMessageDTO;
import org.example.cloudstorageapi.dto.resp.RespResourceDirectoryInfoDTO;
import org.example.cloudstorageapi.dto.resp.RespResourceFileInfoDTO;
import org.example.cloudstorageapi.dto.resp.RespResourceInfoDTO;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Tag(
        name = "Resources",
        description = "Файлы и операции над ними"
)
public interface ResourceApi<U extends UserDetails> {

    @Operation(
            summary = "Получить информацию о ресурсе",
            description = "Можно получить информацию о директории или файлу"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Информация о ресурсе", content = @Content(schema = @Schema(anyOf = {RespResourceFileInfoDTO.class, RespResourceDirectoryInfoDTO.class}))),
            @ApiResponse(responseCode = "400", description = "Ошибка валидации", content = @Content(schema = @Schema(implementation = RespMessageDTO.class))),
            @ApiResponse(responseCode = "401", description = "Пользователь не авторизован", content = @Content(schema = @Schema(implementation = RespMessageDTO.class))),
            @ApiResponse(responseCode = "404", description = "Ресурс не найден", content = @Content(schema = @Schema(implementation = RespMessageDTO.class))),
            @ApiResponse(responseCode = "500", description = "Неизвестная ошибка", content = @Content(schema = @Schema(implementation = RespMessageDTO.class)))
    })
    ResponseEntity<RespResourceInfoDTO> getInfo(
            @Parameter(
                    description = "Путь к ресурсу",
                    example = "SameSegment/SameResourceName"
            )
            String path,

            @Parameter(hidden = true)
            U userDetails
    );

    @Operation(
            summary = "Удалить ресурс",
            description = "Удаляет файл или директорию, при удалении директории удаляются вложенные ресурсы рекурсивно"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Удален ресурс"),
            @ApiResponse(responseCode = "400", description = "Ошибка валидации", content = @Content(schema = @Schema(implementation = RespMessageDTO.class))),
            @ApiResponse(responseCode = "401", description = "Пользователь не авторизован", content = @Content(schema = @Schema(implementation = RespMessageDTO.class))),
            @ApiResponse(responseCode = "404", description = "Ресурс не найден", content = @Content(schema = @Schema(implementation = RespMessageDTO.class))),
            @ApiResponse(responseCode = "500", description = "Неизвестная ошибка", content = @Content(schema = @Schema(implementation = RespMessageDTO.class)))
    })
    void delete(
            @Parameter(
                    description = "Путь к ресурсу",
                    example = "SameSegment/SameResourceName"
            )
            String path,

            @Parameter(hidden = true)
            U userDetails
    );

    @Operation(
            summary = "Переместить или переименовать ресурс",
            description = "При перемещении меняется только путь к файлу, а при переименовании меняется только имя файла"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Ресурс перемещен или переименован"),
            @ApiResponse(responseCode = "400", description = "Ошибка валидации", content = @Content(schema = @Schema(implementation = RespMessageDTO.class))),
            @ApiResponse(responseCode = "401", description = "Пользователь не авторизован", content = @Content(schema = @Schema(implementation = RespMessageDTO.class))),
            @ApiResponse(responseCode = "404", description = "Ресурс не найден", content = @Content(schema = @Schema(implementation = RespMessageDTO.class))),
            @ApiResponse(responseCode = "409", description = "Ресурс уже существует", content = @Content(schema = @Schema(implementation = RespMessageDTO.class))),
            @ApiResponse(responseCode = "500", description = "Неизвестная ошибка", content = @Content(schema = @Schema(implementation = RespMessageDTO.class)))
    })
    ResponseEntity<RespResourceInfoDTO> moveOrRename(
            @Parameter(
                    description = "Путь к ресурсу откуда",
                    example = "SameSegment/SameResourceNameFrom"
            )
            String pathFrom,

            @Parameter(
                    description = "Путь к ресурсу куда",
                    example = "SameSegment/SameResourceNameTo"
            )
            String pathTo,

            @Parameter(hidden = true)
            U userDetails
    );

    @Operation(summary = "Поиск ресурсов")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Ресурсы найдены",
                    content = @Content(
                            array = @ArraySchema(
                                    schema = @Schema(
                                            anyOf = {RespResourceFileInfoDTO.class, RespResourceDirectoryInfoDTO.class})))),
            @ApiResponse(responseCode = "400", description = "Ошибка валидации", content = @Content(schema = @Schema(implementation = RespMessageDTO.class))),
            @ApiResponse(responseCode = "401", description = "Пользователь не авторизован", content = @Content(schema = @Schema(implementation = RespMessageDTO.class))),
            @ApiResponse(responseCode = "500", description = "Неизвестная ошибка", content = @Content(schema = @Schema(implementation = RespMessageDTO.class)))
    })
    ResponseEntity<List<RespResourceInfoDTO>> search(
            @Parameter(
                    description = "Поисковая строка",
                    example = "ResourceName"
            )
            String searchPath,

            @Parameter(hidden = true)
            U userDetails
    );

    @Operation(
            summary = "Загрузка файлов",
            description = "Загружает один или несколько файлов в указанную директорию"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Файлы загружены",
                    content = @Content(
                            array = @ArraySchema(
                                    schema = @Schema(implementation = RespResourceFileInfoDTO.class)))),
            @ApiResponse(responseCode = "400", description = "Ошибка валидации", content = @Content(schema = @Schema(implementation = RespMessageDTO.class))),
            @ApiResponse(responseCode = "401", description = "Пользователь не авторизован", content = @Content(schema = @Schema(implementation = RespMessageDTO.class))),
            @ApiResponse(responseCode = "409", description = "Файл c таким именем уже существует", content = @Content(schema = @Schema(implementation = RespMessageDTO.class))),
            @ApiResponse(responseCode = "413", description = "Превышен максимальный размер", content = @Content(schema = @Schema(implementation = RespMessageDTO.class))),
            @ApiResponse(responseCode = "500", description = "Неизвестная ошибка", content = @Content(schema = @Schema(implementation = RespMessageDTO.class)))
    })
    ResponseEntity<List<RespResourceInfoDTO>> upload(
            @Parameter(
                    description = "Путь загрузки",
                    example = "SameSegment/"
            )
            String path,

            @Parameter(
                    description = "Файлы для загрузки",
                    required = true
            )
            List<MultipartFile> files,

            @Parameter(hidden = true)
            U userDetails
    ) throws IOException;

    @Operation(
            summary = "Скачать файл",
            description = "Скачивает файл или директорию по указанному пути"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Файлы скачены", content = @Content(mediaType = MediaType.APPLICATION_OCTET_STREAM_VALUE)),
            @ApiResponse(responseCode = "400", description = "Ошибка валидации", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = RespMessageDTO.class))),
            @ApiResponse(responseCode = "401", description = "Пользователь не авторизован", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = RespMessageDTO.class))),
            @ApiResponse(responseCode = "404", description = "Ресурс не найден", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = RespMessageDTO.class))),
            @ApiResponse(responseCode = "500", description = "Неизвестная ошибка", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = RespMessageDTO.class)))
    })
    void download(
            @Parameter(example = "SameSegment/SameResourceName")
            String path,

            @Parameter(hidden = true)
            HttpServletResponse response,

            @Parameter(hidden = true)
            U userDetails
    ) throws IOException;
}
