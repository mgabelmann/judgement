package ca.mikegabelmann.judgement.persistence.service;

import ca.mikegabelmann.judgement.persistence.model.RoleCode;

import java.util.List;
import java.util.Optional;

public interface RoleCodeService {
    /**
     * Get all RoleCodes.
     * @return all records
     */
    List<RoleCode> findAll();

    /**
     * Get record by its id.
     * @param id primary key
     * @return record
     */
    Optional<RoleCode> findById(String id);

}
