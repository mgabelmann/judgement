package ca.mikegabelmann.judgement.persistence.service;

import ca.mikegabelmann.judgement.persistence.model.AccountStatusCode;
import ca.mikegabelmann.judgement.persistence.model.ProjectRoleCode;
import ca.mikegabelmann.judgement.persistence.model.RoleCode;

import java.util.List;
import java.util.Optional;

public interface CodesService {
    /**
     * Get record by its id.
     * @param id primary key
     * @return record
     */
    Optional<RoleCode> findRoleById(String id);

    /**
     * Find all active.
     * @return active records
     */
    List<RoleCode> findAllRoleByActiveIs(Boolean active);

    /**
     * Get record by its id.
     * @param id primary key
     * @return record
     */
    Optional<AccountStatusCode> findAccountStatusById(String id);

    /**
     * Find all active.
     * @return active records
     */
    List<AccountStatusCode> findAllAccountStatusByActiveIs(Boolean active);

    /**
     * Get record by its id.
     * @param id primary key
     * @return record
     */
    Optional<ProjectRoleCode> findProjectRoleById(String id);

    /**
     * Find all active.
     * @return active records
     */
    List<ProjectRoleCode> findAllProjectRoleByActiveIs(Boolean active);

}
