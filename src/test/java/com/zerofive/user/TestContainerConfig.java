package com.zerofive.user;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MySQLContainer;

@SpringBootTest
@ActiveProfiles("test")
public abstract class TestContainerConfig {

    protected static MySQLContainer<?> mysql;

    static {
        mysql = new MySQLContainer<>("mysql:8.4.5")
                .withDatabaseName("testdb")
                .withUsername("root")
                .withPassword("test");

        mysql.start();
    }

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", () -> mysql.getJdbcUrl());
        registry.add("spring.datasource.username", () -> mysql.getUsername());
        registry.add("spring.datasource.password", () -> mysql.getPassword());
        registry.add("spring.jpa.hibernate.ddl-auto", () -> "create-drop");
        registry.add("spring.jpa.show-sql", () -> "true");
        registry.add("spring.jpa.properties.hibernate.format_sql", () -> "true");
    }
}
