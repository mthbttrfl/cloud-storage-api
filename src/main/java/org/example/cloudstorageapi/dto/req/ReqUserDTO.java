package org.example.cloudstorageapi.dto.req;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Schema(description = "Данные для регистрации и входа пользователя")
public class ReqUserDTO {

    @Schema(
            description = "Имя пользователя",
            example = "username",
            minLength = 4,
            maxLength = 16
    )
    @NotBlank(message = "Username cannot be empty")
    @Size(min = 4, max = 16, message = "Username must be a minimum of 4 characters and a maximum of 16")
    @Pattern(regexp = "^[a-zA-Z]+$", message = "The username must contain only Latin letters")
    private String username;

    @Schema(
            description = "Пароль пользователя",
            example = "Password@123",
            minLength = 8,
            maxLength = 20
    )
    @NotBlank(message = "Password cannot be empty")
    @Size(min = 8, max = 20, message = "The password cannot be less than 8 characters and more than 20")
    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*[@$!%*?&]).*$", message = "The password must consist of Latin characters, contain at least 1 number and 1 special character")
    private String password;
}
