package org.example.cloudstorageapi.config;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EntityScan(basePackages = "org.example.cloudstorageapi.entity")
@EnableJpaRepositories(basePackages = "org.example.cloudstorageapi.repository")
@EnableTransactionManagement
public class DataJpaConfig {
}
