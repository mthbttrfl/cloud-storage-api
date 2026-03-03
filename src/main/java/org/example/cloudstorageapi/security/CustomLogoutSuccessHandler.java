package org.example.cloudstorageapi.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.cloudstorageapi.dto.resp.RespMessageDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

import static org.example.cloudstorageapi.constant.SecurityErrorMessages.UNAUTHORIZED;

@Slf4j
@Component
@RequiredArgsConstructor
public class CustomLogoutSuccessHandler implements LogoutSuccessHandler {

    private final ObjectMapper objectMapper;

    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {

        if(authentication == null || !authentication.isAuthenticated()){ //logout выполняется для всех пользователей(анонимных)

            log.debug("An unauthorized user logout successfully");

            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            objectMapper.writeValue(response.getWriter(), new RespMessageDTO(UNAUTHORIZED)); // сообщение имеет информационный характер
            return;
        }

        log.debug("Logout successfully");

        response.setStatus(HttpStatus.NO_CONTENT.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
    }
}
