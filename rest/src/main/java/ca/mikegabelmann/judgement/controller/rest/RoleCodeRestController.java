package ca.mikegabelmann.judgement.controller.rest;

import ca.mikegabelmann.judgement.persistence.model.RoleCode;
import ca.mikegabelmann.judgement.persistence.service.RoleCodeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class RoleCodeRestController {
    private static final Logger LOGGER = LoggerFactory.getLogger(RoleCodeRestController.class);

    public static final String PATH_ROLE_CODES = "/rolecodes";

    private final RoleCodeService roleCodeService;


    @Autowired
    public RoleCodeRestController(RoleCodeService roleCodeService) {
        this.roleCodeService = roleCodeService;
    }

    @GetMapping(path = PATH_ROLE_CODES)
    public ResponseEntity<List<RoleCode>> getActiveRoleCodes() {
        List<RoleCode> roleCodes = roleCodeService.findActiveRoleCodes();
        return ResponseEntity.ok(roleCodes);
    }

}
