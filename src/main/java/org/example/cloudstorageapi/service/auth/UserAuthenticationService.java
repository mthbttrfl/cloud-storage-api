package org.example.cloudstorageapi.service.auth;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.cloudstorageapi.dto.req.ReqUserDTO;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserAuthenticationService implements AuthenticationService<ReqUserDTO> {

    private final AuthenticationManager authenticationManager;
    private final SecurityContextRepository securityContextRepository;

    @Override
    public Authentication authenticate(ReqUserDTO userDTO, HttpServletRequest request, HttpServletResponse response) {

        log.debug("{} authentication start", userDTO.getUsername());

        Authentication authenticated = authenticationManager.authenticate(
                UsernamePasswordAuthenticationToken.unauthenticated(
                        userDTO.getUsername(),
                        userDTO.getPassword()
                ));
        SecurityContext context = SecurityContextHolder.getContext();
        context.setAuthentication(authenticated);
        securityContextRepository.saveContext(context, request, response);

        log.debug("{} is authenticated", userDTO.getUsername());
        return authenticated;
    }
}
