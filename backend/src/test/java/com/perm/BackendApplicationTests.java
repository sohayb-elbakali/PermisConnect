package com.perm;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest(classes = com.autoecole.AutoEcoleApplication.class)
@ActiveProfiles("test")
@TestPropertySource(properties = {
    "spring.jpa.hibernate.ddl-auto=create-drop",
    "spring.datasource.url=jdbc:h2:mem:testdb",
    "spring.datasource.driver-class-name=org.h2.Driver",
    "spring.datasource.username=sa",
    "spring.datasource.password=password"
})
class BackendApplicationTests {

    @Test
    void contextLoads() {
        // This test verifies that the Spring application context loads successfully
    }
}
