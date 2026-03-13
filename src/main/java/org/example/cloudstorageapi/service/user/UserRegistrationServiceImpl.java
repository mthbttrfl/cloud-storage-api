package org.example.cloudstorageapi.service.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.cloudstorageapi.dto.req.ReqUserDTO;
import org.example.cloudstorageapi.exception.UsernameAlreadyExists;
import org.example.cloudstorageapi.mapper.UserMapper;
import org.example.cloudstorageapi.repository.UserRepository;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static org.example.cloudstorageapi.constant.DataBaseErrorMessages.USERNAME_ALREADY_EXISTS;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class UserRegistrationServiceImpl implements UserRegistrationService<ReqUserDTO, Long> {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;

    @Override
    public Long registration(ReqUserDTO userDTO) {
        try {
            Long id = userRepository.save(userMapper.toEntity(userDTO, passwordEncoder.encode(userDTO.getPassword()))).getId();

            log.debug("Saving {} in database with id ({})", userDTO.getUsername(), id);

            return id;
        }catch (DataIntegrityViolationException ex){
            throw new UsernameAlreadyExists(USERNAME_ALREADY_EXISTS, ex);
        }
    }
}
