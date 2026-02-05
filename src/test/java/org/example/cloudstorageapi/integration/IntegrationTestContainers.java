package org.example.cloudstorageapi.integration;

import jakarta.transaction.Transactional;
import org.example.cloudstorageapi.CloudStorageApiApplication;
import org.junit.jupiter.api.BeforeAll;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.testcontainers.containers.PostgreSQLContainer;

@SpringBootTest(classes = CloudStorageApiApplication.class)
@Sql(scripts = "classpath:sql/init-data.sql")
@Transactional
public abstract class IntegrationTestContainers {

    private static final PostgreSQLContainer<?> POSTGRE_SQL_CONTAINER = new PostgreSQLContainer<>("postgres:latest");

    @BeforeAll
    public static void start(){
        POSTGRE_SQL_CONTAINER.start();
    }

    @DynamicPropertySource
    public static void configureProperty(DynamicPropertyRegistry registry){
        configurePropertyPostgreSQL(registry);
    }

    private static void configurePropertyPostgreSQL(DynamicPropertyRegistry registry){
        registry.add("spring.datasource.username", POSTGRE_SQL_CONTAINER::getUsername);
        registry.add("spring.datasource.password", POSTGRE_SQL_CONTAINER::getPassword);
        registry.add("spring.datasource.driver-class-name", POSTGRE_SQL_CONTAINER::getDriverClassName);
        registry.add("spring.datasource.url", POSTGRE_SQL_CONTAINER::getJdbcUrl);
    }
}
