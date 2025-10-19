package ca.mikegabelmann.judgement.persistence.service;

import ca.mikegabelmann.judgement.persistence.model.AccountStatusCode;

import java.util.List;
import java.util.Optional;

public interface AccountStatusCodeService {
    /**
     * Get all AccountStatusCode.
     * @return all records
     */
    List<AccountStatusCode> findAll();

    /**
     * Get record by its id.
     * @param id primary key
     * @return record
     */
    Optional<AccountStatusCode> findById(String id);

    /**
     * Find all active.
     * @return active records
     */
    List<AccountStatusCode> findActive();

}
