package org.example.cloudstorageapi.service.auth;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;

public interface AuthenticationService <T> {
    Authentication authenticate (T t, HttpServletRequest request, HttpServletResponse response);
}
