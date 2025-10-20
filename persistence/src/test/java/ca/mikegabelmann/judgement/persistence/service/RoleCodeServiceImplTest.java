package ca.mikegabelmann.judgement.persistence.service;

import ca.mikegabelmann.judgement.persistence.model.RoleCode;
import ca.mikegabelmann.judgement.persistence.repository.RoleCodeRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class RoleCodeServiceImplTest {
    @Mock
    private RoleCodeRepository roleCodeRepository;

    @InjectMocks
    private RoleCodeServiceImpl roleCodeService;

    private RoleCode roleCode1;
    private RoleCode roleCode2;


    @BeforeEach
    void beforeEach() {
        this.roleCode1 = new RoleCode(true, "description", "ADMIN");
        this.roleCode2 = new RoleCode(false, "description", "USER_OLD");
    }

    @Test
    void findAll() {
        Mockito.when(roleCodeRepository.findAll()).thenReturn(Arrays.asList(roleCode1, roleCode2));
        List<RoleCode> roleCodes = roleCodeService.findAll();

        Assertions.assertNotNull(roleCodes);
        Assertions.assertEquals(2, roleCodes.size());
    }

    @Test
    void findById() {
        Mockito.when(roleCodeRepository.findById(roleCode1.getCode())).thenReturn(Optional.of(roleCode1));
        Optional<RoleCode> roleCode = roleCodeService.findById(roleCode1.getCode());

        Assertions.assertNotNull(roleCode);
        Assertions.assertTrue(roleCode.isPresent());
    }

    @Test
    void findActiveRoleCodes() {
        Mockito.when(roleCodeRepository.findAllByActiveTrue()).thenReturn(Arrays.asList(roleCode1));
        List<RoleCode> roleCodes = roleCodeService.findActive();

        Assertions.assertNotNull(roleCodes);
        Assertions.assertEquals(1, roleCodes.size());
    }

}
