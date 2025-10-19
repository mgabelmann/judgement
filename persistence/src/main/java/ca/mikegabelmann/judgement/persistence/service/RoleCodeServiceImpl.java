package ca.mikegabelmann.judgement.persistence.service;

import ca.mikegabelmann.judgement.persistence.model.RoleCode;
import ca.mikegabelmann.judgement.persistence.repository.RoleCodeRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RoleCodeServiceImpl implements RoleCodeService {
    /** Logger. */
    private static final Logger LOG = LoggerFactory.getLogger(RoleCodeServiceImpl.class);

    private RoleCodeRepository roleCodeRepository;


    @Autowired
    public RoleCodeServiceImpl(RoleCodeRepository roleCodeRepository) {
        this.roleCodeRepository = roleCodeRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<RoleCode> findAll() {
        return roleCodeRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<RoleCode> findById(String id) {
        return roleCodeRepository.findById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<RoleCode> findActive() {
        return roleCodeRepository.findAllByActiveTrue();
    }

}
