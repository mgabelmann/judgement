package ca.mikegabelmann.judgement.controller.rest;

import ca.mikegabelmann.judgement.persistence.model.AccountStatusCode;
import ca.mikegabelmann.judgement.persistence.model.ProjectRoleCode;
import ca.mikegabelmann.judgement.persistence.model.RoleCode;
import ca.mikegabelmann.judgement.persistence.service.CodesService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController
public class CodesRestController {
    private static final Logger LOGGER = LoggerFactory.getLogger(CodesRestController.class);

    public static final String PATH_ROLE_CODES = "/api/codes/rolecodes";
    public static final String PATH_ACCOUNT_STATUS_CODES = "/api/codes/accountstatuscodes";
    public static final String PATH_PROJECT_ROLE_CODES = "/api/codes/projectrolecodes";

    private final CodesService codesService;


    @Autowired
    public CodesRestController(final CodesService codesService) {
        this.codesService = codesService;
    }

    @GetMapping(path = PATH_ROLE_CODES)
    public ResponseEntity<List<RoleCode>> getActiveRoleCodes() {
        List<RoleCode> codes = codesService.findAllRoleByActiveIs(true);
        return ResponseEntity.ok(codes);
    }

    @GetMapping(path = PATH_ACCOUNT_STATUS_CODES)
    public ResponseEntity<List<AccountStatusCode>> getActiveAccountStatusCodes() {
        List<AccountStatusCode> codes = codesService.findAllAccountStatusByActiveIs(true);
        return ResponseEntity.ok(codes);
    }

    @GetMapping(path = PATH_PROJECT_ROLE_CODES)
    public ResponseEntity<List<ProjectRoleCode>> getActiveProjectRoleCodes() {
        List<ProjectRoleCode> codes = codesService.findAllProjectRoleByActiveIs(true);
        return ResponseEntity.ok(codes);
    }

}
