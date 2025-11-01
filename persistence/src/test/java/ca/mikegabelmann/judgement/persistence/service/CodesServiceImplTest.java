package ca.mikegabelmann.judgement.persistence.service;

import ca.mikegabelmann.judgement.persistence.model.AccountStatusCode;
import ca.mikegabelmann.judgement.persistence.model.ProjectRoleCode;
import ca.mikegabelmann.judgement.persistence.model.RoleCode;
import ca.mikegabelmann.judgement.persistence.repository.AccountStatusCodeRepository;
import ca.mikegabelmann.judgement.persistence.repository.ProjectRoleCodeRepository;
import ca.mikegabelmann.judgement.persistence.repository.RoleCodeRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class CodesServiceImplTest {
    @Mock
    private RoleCodeRepository roleCodeRepository;

    @Mock
    private ProjectRoleCodeRepository projectRoleCodeRepository;

    @Mock
    private AccountStatusCodeRepository accountStatusCodeRepository;

    @InjectMocks
    private CodesServiceImpl codesService;

    private RoleCode roleCode1;
    private RoleCode roleCode2;
    private ProjectRoleCode projectRoleCode1;
    private ProjectRoleCode projectRoleCode2;
    private AccountStatusCode accountStatusCode1;
    private AccountStatusCode accountStatusCode2;


    @BeforeEach
    void beforeEach() {
        this.roleCode1 = new RoleCode(true, "description", "ROLE_NEW");
        this.roleCode2 = new RoleCode(false, "description", "ROLE_OLD");

        this.projectRoleCode1 = new ProjectRoleCode(true, "description", "PROJECT_NEW");
        this.projectRoleCode2 = new ProjectRoleCode(false, "description", "PROJECT_OLD");

        this.accountStatusCode1 = new AccountStatusCode(true, "description", "ACCOUNT_NEW");
        this.accountStatusCode2 = new AccountStatusCode(false, "description", "ACCOUNT_OLD");
    }

    @Test
    @DisplayName("findRoleById - happy path")
    void test1_findRoleById() {
        Mockito.when(roleCodeRepository.findById("ROLE_NEW")).thenReturn(Optional.of(roleCode1));
        Optional<RoleCode> code = codesService.findRoleById("ROLE_NEW");

        Assertions.assertTrue(code.isPresent());
    }

    @ParameterizedTest
    @ValueSource(booleans = {true, false})
    @DisplayName("findAllRoleByActive - happy path")
    void test1_findAllRoleByActive(Boolean active) {
        Mockito.lenient().when(roleCodeRepository.findAllByActiveIs(Boolean.TRUE)).thenReturn(List.of(roleCode1));
        Mockito.lenient().when(roleCodeRepository.findAllByActiveIs(Boolean.FALSE)).thenReturn(List.of(roleCode2));

        List<RoleCode> codes = codesService.findAllRoleByActiveIs(active);
        Assertions.assertFalse(codes.isEmpty());
        Assertions.assertEquals(1, codes.size());

        Assertions.assertEquals(active ? roleCode1.getCode() : roleCode2.getCode(), codes.get(0).getCode());
    }

    @Test
    @DisplayName("findAccountStatusById - happy path")
    void test1_findAccountStatusById() {
        Mockito.when(accountStatusCodeRepository.findById("ACCOUNT_NEW")).thenReturn(Optional.of(accountStatusCode1));
        Optional<AccountStatusCode> code = codesService.findAccountStatusById("ACCOUNT_NEW");

        Assertions.assertTrue(code.isPresent());
    }

    @ParameterizedTest
    @ValueSource(booleans = {true, false})
    @DisplayName("findAllAccountStatusByActiveIs - happy path")
    void test1_findAllAccountStatusByActiveIs(Boolean active) {
        Mockito.lenient().when(accountStatusCodeRepository.findAllByActiveIs(Boolean.TRUE)).thenReturn(List.of(accountStatusCode1));
        Mockito.lenient().when(accountStatusCodeRepository.findAllByActiveIs(Boolean.FALSE)).thenReturn(List.of(accountStatusCode2));

        List<AccountStatusCode> codes = codesService.findAllAccountStatusByActiveIs(active);
        Assertions.assertFalse(codes.isEmpty());
        Assertions.assertEquals(1, codes.size());

        Assertions.assertEquals(active ? accountStatusCode1.getCode() : accountStatusCode2.getCode(), codes.get(0).getCode());
    }

    @Test
    @DisplayName("findProjectRoleById - happy path")
    void test1_findProjectRoleById() {
        Mockito.when(projectRoleCodeRepository.findById("PROJECT_NEW")).thenReturn(Optional.of(projectRoleCode1));
        Optional<ProjectRoleCode> code = codesService.findProjectRoleById("PROJECT_NEW");

        Assertions.assertTrue(code.isPresent());
    }

    @ParameterizedTest
    @ValueSource(booleans = {true, false})
    @DisplayName("findAllProjectRoleByActiveIs - happy path")
    void test1_findAllProjectRoleByActiveIs(Boolean active) {
        Mockito.lenient().when(projectRoleCodeRepository.findAllByActiveIs(Boolean.TRUE)).thenReturn(List.of(projectRoleCode1));
        Mockito.lenient().when(projectRoleCodeRepository.findAllByActiveIs(Boolean.FALSE)).thenReturn(List.of(projectRoleCode2));

        List<ProjectRoleCode> codes = codesService.findAllProjectRoleByActiveIs(active);
        Assertions.assertFalse(codes.isEmpty());
        Assertions.assertEquals(1, codes.size());

        Assertions.assertEquals(active ? projectRoleCode1.getCode() : projectRoleCode2.getCode(), codes.get(0).getCode());
    }

}
