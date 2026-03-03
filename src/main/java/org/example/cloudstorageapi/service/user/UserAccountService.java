package org.example.cloudstorageapi.service.user;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;

public interface UserAccountService<ID, T> {

    void registerAndInitRootFolder(T t);
    ID registration(T t);
    Authentication authenticate(T t, HttpServletRequest request, HttpServletResponse response);
    void initRootFolder(ID id);
}
