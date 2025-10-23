package ca.mikegabelmann.judgement.controller.rest;

import ca.mikegabelmann.judgement.persistence.model.AccountStatusCode;
import ca.mikegabelmann.judgement.persistence.model.RoleCode;
import ca.mikegabelmann.judgement.persistence.service.AccountStatusCodeService;
import ca.mikegabelmann.judgement.persistence.service.RoleCodeService;
//import ca.mikegabelmann.judgement.security.preauthorize.role.RoleAdministrator;
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

    public static final String PATH_ROLE_CODES = "/codes/rolecodes";
    public static final String PATH_ACCOUNT_STATUS_CODES = "/codes/accountstatuscodes";

    private final RoleCodeService roleCodeService;
    private final AccountStatusCodeService accountStatusCodeService;


    @Autowired
    public CodesRestController(final AccountStatusCodeService accountStatusCodeService, final RoleCodeService roleCodeService) {
        this.accountStatusCodeService = accountStatusCodeService;
        this.roleCodeService = roleCodeService;
    }

    @GetMapping(path = PATH_ROLE_CODES)
    public ResponseEntity<List<RoleCode>> getActiveRoleCodes() {
        List<RoleCode> codes = roleCodeService.findActive();
        return ResponseEntity.ok(codes);
    }

    //@RoleAdministrator
    @GetMapping(path = PATH_ACCOUNT_STATUS_CODES)
    public ResponseEntity<List<AccountStatusCode>> getActiveAccountStatusCodes() {
        List<AccountStatusCode> codes = accountStatusCodeService.findActive();
        return ResponseEntity.ok(codes);
    }

}
