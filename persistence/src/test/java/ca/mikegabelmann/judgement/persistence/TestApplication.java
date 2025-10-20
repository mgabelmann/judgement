package ca.mikegabelmann.judgement.persistence;

import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.test.context.TestPropertySource;


/**
 * Required for @DataJpaTest. Alternatively you could use @SpringBootApplication.
 */
@SpringBootConfiguration
@EnableAutoConfiguration
//@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
//@TestPropertySource(properties = {
//        "spring.datasource.url=jdbc:sqlite:target/memory.db:judgement?cache=shared",
//        "spring.jpa.hibernate.ddl-auto=create-drop",
//})
public class TestApplication {

}
