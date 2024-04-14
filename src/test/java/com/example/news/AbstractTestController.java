package com.example.news;

import com.example.news.mapper.v2.NewsMapperV2;
import com.example.news.mapper.v2.UserMapperV2;
import com.example.news.repository.DatabaseUserRepository;
import com.example.news.service.NewsService;
import com.example.news.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import java.time.Instant;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@Transactional
@Sql("classpath:db/init.sql")
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Testcontainers
public abstract class AbstractTestController {

    protected static Long USER_ID_WITH_USER_ROLE = 2L;

    protected static Long ADMIN_ID_WITH_ADMIN_ROLE = 1L;

    protected static Instant NEWS_CREATED = Instant.parse("2024-01-03T10:15:30.00Z");

    protected static Instant NEWS_UPDATED = Instant.parse("2024-01-05T10:15:30.00Z");

    protected static Instant COMMENT_CREATED = Instant.parse("2024-01-04T10:15:30.00Z");

    protected static Instant COMMENT_UPDATED = Instant.parse("2024-01-06T10:15:30.00Z");

    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    protected ObjectMapper objectMapper;

    @Autowired
    protected UserService userService;

    @Autowired
    protected NewsService newsService;

    @Autowired
    protected UserMapperV2 userMapper;

    @Autowired
    protected NewsMapperV2 newsMapper;

    @Autowired
    protected DatabaseUserRepository userRepository;

    @Autowired
    protected PasswordEncoder passwordEncoder;

    protected static PostgreSQLContainer postgreSQLContainer;

    static {
        DockerImageName postgres = DockerImageName.parse("postgres:12:3");

        postgreSQLContainer = (PostgreSQLContainer) new PostgreSQLContainer(postgres).withReuse(true);

        postgreSQLContainer.start();
    }

    @DynamicPropertySource
    public static void registerProperties(DynamicPropertyRegistry registry) {
        String jdbcUrl = postgreSQLContainer.getJdbcUrl();

        registry.add("spring.datasource.username", postgreSQLContainer::getUsername);
        registry.add("spring.datasource.password", postgreSQLContainer::getPassword);
        registry.add("spring.datasource.url", () -> jdbcUrl);
    }


}
