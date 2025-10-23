package ca.mikegabelmann.judgement.persistence.repository;

import ca.mikegabelmann.judgement.persistence.model.ProjectRoleCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProjectRoleCodeRepository extends JpaRepository<ProjectRoleCode, String> {

    /**
     * Find all active codes.
     * @return active codes
     */
    List<ProjectRoleCode> findAllByActiveTrue();

}
