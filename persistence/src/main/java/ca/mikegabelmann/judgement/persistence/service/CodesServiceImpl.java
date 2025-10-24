package ca.mikegabelmann.judgement.persistence.service;

import ca.mikegabelmann.judgement.persistence.model.AccountStatusCode;
import ca.mikegabelmann.judgement.persistence.model.ProjectRoleCode;
import ca.mikegabelmann.judgement.persistence.model.RoleCode;
import ca.mikegabelmann.judgement.persistence.repository.AccountStatusCodeRepository;
import ca.mikegabelmann.judgement.persistence.repository.ProjectRoleCodeRepository;
import ca.mikegabelmann.judgement.persistence.repository.RoleCodeRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;


@Service
public class CodesServiceImpl implements CodesService {
    private static final Logger LOGGER = LoggerFactory.getLogger(CodesServiceImpl.class);

    private final RoleCodeRepository roleCodeRepository;
    private final ProjectRoleCodeRepository projectRoleCodeRepository;
    private final AccountStatusCodeRepository accountStatusCodeRepository;


    @Autowired
    public CodesServiceImpl(AccountStatusCodeRepository accountStatusCodeRepository, ProjectRoleCodeRepository projectRoleCodeRepository, RoleCodeRepository roleCodeRepository) {
        this.accountStatusCodeRepository = accountStatusCodeRepository;
        this.projectRoleCodeRepository = projectRoleCodeRepository;
        this.roleCodeRepository = roleCodeRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<RoleCode> findRoleById(final String id) {
        return roleCodeRepository.findById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<RoleCode> findAllRoleByActiveIs(final Boolean active) {
        return roleCodeRepository.findAllByActiveIs(active);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<AccountStatusCode> findAccountStatusById(final String id) {
        return accountStatusCodeRepository.findById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<AccountStatusCode> findAllAccountStatusByActiveIs(final Boolean active) {
        return accountStatusCodeRepository.findAllByActiveIs(active);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ProjectRoleCode> findProjectRoleById(final String id) {
        return projectRoleCodeRepository.findById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProjectRoleCode> findAllProjectRoleByActiveIs(final Boolean active) {
        return projectRoleCodeRepository.findAllByActiveIs(active);
    }

}
