package ca.mikegabelmann.judgement.persistence.repository;

import ca.mikegabelmann.judgement.persistence.model.RoleCode;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.TestPropertySource;

@DataJpaTest
//@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
//@TestPropertySource(properties = {
//        "spring.datasource.url=jdbc:sqlite:target/memory.db:judgement?cache=shared",
//        "spring.jpa.hibernate.ddl-auto=create-drop",
//})
public class RoleCodeRepositoryTest {
    private final RoleCodeRepository roleCodeRepository;


    @Autowired
    public RoleCodeRepositoryTest(final RoleCodeRepository roleCodeRepository) {
        this.roleCodeRepository = roleCodeRepository;
    }

    @BeforeEach
    public void beforeEach() {
        RoleCode tmp1Code = new RoleCode(true, "system administrator", "ADMINISTRATOR");
        this.roleCodeRepository.save(tmp1Code);
    }

    @Test
    @DisplayName("get by id - happy path")
    public void test1_getById() {
        RoleCode roleCode1 = this.roleCodeRepository.getReferenceById("ADMINISTRATOR");
        Assertions.assertNotNull(roleCode1);
    }

    @Test
    @DisplayName("get by id - not found")
    public void test2_getById() {
        Assertions.assertThrows(EntityNotFoundException.class, () -> {
            RoleCode rc = this.roleCodeRepository.getReferenceById("NOT FOUND");
            rc.toString();
        });
    }
}
