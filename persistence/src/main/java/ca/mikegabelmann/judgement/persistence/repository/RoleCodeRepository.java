package ca.mikegabelmann.judgement.persistence.repository;

import ca.mikegabelmann.judgement.persistence.model.RoleCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface RoleCodeRepository extends JpaRepository<RoleCode, String> {

    /**
     * Find all active or inactive codes.
     * @param active active or inactive
     * @return records
     */
    List<RoleCode> findAllByActiveIs(Boolean active);

}
