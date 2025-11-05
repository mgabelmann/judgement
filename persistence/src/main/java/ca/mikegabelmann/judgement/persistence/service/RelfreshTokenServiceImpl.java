package ca.mikegabelmann.judgement.persistence.service;

import ca.mikegabelmann.judgement.persistence.model.RefreshToken;
import ca.mikegabelmann.judgement.persistence.repository.RefreshTokenRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;


@Service
public class RelfreshTokenServiceImpl implements RefreshTokenService {
    private static final Logger LOGGER = LoggerFactory.getLogger(RelfreshTokenServiceImpl.class);

    private final RefreshTokenRepository refreshTokenRepository;


    @Autowired
    public RelfreshTokenServiceImpl(RefreshTokenRepository refreshTokenRepository) {
        this.refreshTokenRepository = refreshTokenRepository;
    }

    @Override
    public int deleteAllExpiredTokens(final Instant expiry) {
        if (expiry == null) {
            throw new IllegalArgumentException("expiry can't be null");
        }

        List<RefreshToken> records = refreshTokenRepository.findAllByExpiryBefore(expiry);
        int count = records.size();

        refreshTokenRepository.deleteAll(records);

        if (count > 0) {
            LOGGER.info("deleted {} refresh tokens", count);
        } else {
            LOGGER.debug("no refresh tokens to delete");
        }

        return count;
    }

}
