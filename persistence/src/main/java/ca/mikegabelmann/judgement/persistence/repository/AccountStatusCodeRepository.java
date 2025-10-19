package ca.mikegabelmann.judgement.persistence.repository;

import ca.mikegabelmann.judgement.persistence.model.AccountStatusCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface AccountStatusCodeRepository extends JpaRepository<AccountStatusCode, String> {

    /**
     * Find all active codes.
     * @return active codes
     */
    List<AccountStatusCode> findAllByActiveTrue();

}
