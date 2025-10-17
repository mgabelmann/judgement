package ca.mikegabelmann.judgement.persistence.repository;

import ca.mikegabelmann.judgement.persistence.model.RoleCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface RoleCodeRepository extends JpaRepository<RoleCode, String> {

    /**
     * Find all active codes.
     * @return active codes
     */
    List<RoleCode> findAllByActiveTrue();

}
