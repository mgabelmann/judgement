package ca.mikegabelmann.judgement.persistence.repository;

import ca.mikegabelmann.judgement.persistence.model.AccountActivityLog;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;


@Repository
public interface AccountActivityLogRepository extends JpaRepository<AccountActivityLog, UUID> {

    /**
     * Get all AccountActivityLog records by account id.
     * @param username
     * @param pageable
     * @return
     */
    List<AccountActivityLog> findByUsernameOrderByActivityOnDesc(String username, Pageable pageable);

}
