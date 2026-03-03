package org.example.cloudstorageapi.security;

import lombok.RequiredArgsConstructor;
import org.example.cloudstorageapi.mapper.UserMapper;
import org.example.cloudstorageapi.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import static org.example.cloudstorageapi.constant.SecurityErrorMessages.USERNAME_NOT_FOUND;

@Component
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username)
                .map(userMapper::toDetails)
                .orElseThrow(() -> new UsernameNotFoundException(USERNAME_NOT_FOUND));
    }
}
