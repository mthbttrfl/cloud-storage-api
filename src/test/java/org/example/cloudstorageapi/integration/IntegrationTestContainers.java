package org.example.cloudstorageapi.integration;

import jakarta.transaction.Transactional;
import org.example.cloudstorageapi.CloudStorageApiApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.MinIOContainer;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

@SpringBootTest(classes = CloudStorageApiApplication.class)
@Testcontainers
@Transactional
public abstract class IntegrationTestContainers {

    @Container
    private static final PostgreSQLContainer<?> POSTGRE_SQL_CONTAINER = new PostgreSQLContainer<>("postgres:latest");

    @Container
    private static final MinIOContainer MIN_IO_CONTAINER = new MinIOContainer("minio/minio:latest");

    @Container
    private static final GenericContainer<?> REDIS_CONTAINER = new GenericContainer<>(DockerImageName.parse("redis:latest")).withExposedPorts(6379);

    @DynamicPropertySource
    public static void configureProperties(DynamicPropertyRegistry registry) {
        configurePostgreSqlProperties(registry);
        configureMinIoProperties(registry);
        configureRedisProperties(registry);
    }

    private static void configurePostgreSqlProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.username", POSTGRE_SQL_CONTAINER::getUsername);
        registry.add("spring.datasource.password", POSTGRE_SQL_CONTAINER::getPassword);
        registry.add("spring.datasource.driver-class-name", POSTGRE_SQL_CONTAINER::getDriverClassName);
        registry.add("spring.datasource.url", POSTGRE_SQL_CONTAINER::getJdbcUrl);
    }

    private static void configureMinIoProperties(DynamicPropertyRegistry registry) {
        registry.add("minio.client.url", MIN_IO_CONTAINER::getS3URL);
        registry.add("minio.client.access-key", MIN_IO_CONTAINER::getUserName);
        registry.add("minio.client.secret-key", MIN_IO_CONTAINER::getPassword);
    }

    private static void configureRedisProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.data.redis.host", REDIS_CONTAINER::getHost);
        registry.add("spring.data.redis.port", () -> REDIS_CONTAINER.getMappedPort(6379));
    }
}