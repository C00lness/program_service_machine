package com.example.programservice.repository;

import com.example.programservice.model.Program;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@Testcontainers
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class ProgramRepositoryTest {

    // Поднимаем реальный PostgreSQL в Docker на время теста
    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:16-alpine");

    // Говорим Spring, чтобы он подключался к этому контейнеру
    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
    }

    @Autowired
    private ProgramRepository programRepository;

    @Test
    void shouldSaveAndFindProgram() {
        // Создаем программу
        Program program = new Program();
        program.setName("Test Program");
        program.setStatus("DRAFT");

        // Сохраняем в реальную БД (в Docker)
        Program saved = programRepository.save(program);

        // Проверяем, что ID присвоился (значит, БД реально отработала)
        assertNotNull(saved.getId());

        // Ищем обратно
        Program found = programRepository.findById(saved.getId()).orElse(null);
        assertNotNull(found);
        assertEquals("Test Program", found.getName());
    }
}
