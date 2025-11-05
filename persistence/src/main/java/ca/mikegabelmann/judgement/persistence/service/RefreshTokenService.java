package ca.mikegabelmann.judgement.persistence.service;

import java.time.Instant;

public interface RefreshTokenService {

    /**
     * Delete all refresh tokens older than the date supplied.
     * @param expiry delete records older than this
     * @return number of records deleted
     */
    int deleteAllExpiredTokens(Instant expiry);

}
