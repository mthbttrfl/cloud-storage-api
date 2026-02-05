package org.example.cloudstorageapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository<T, ID> extends JpaRepository<T, ID> {

    Optional<T> findByUsername(String username);
}
