package ca.mikegabelmann.judgement.persistence.repository;

import ca.mikegabelmann.judgement.persistence.model.ProjectAccount;
import ca.mikegabelmann.judgement.persistence.model.ProjectAccountId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProjectAccountRepository extends JpaRepository<ProjectAccount, ProjectAccountId> {

}
