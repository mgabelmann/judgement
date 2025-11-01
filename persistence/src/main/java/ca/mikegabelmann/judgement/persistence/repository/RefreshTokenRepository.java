package ca.mikegabelmann.judgement.persistence.repository;

import ca.mikegabelmann.judgement.persistence.model.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;


@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, UUID> {
    /**
     *
     * @param token
     * @return
     */
    Optional<RefreshToken> findByToken(String token);

    /**
     *
     * @param username
     * @return
     */
    Optional<RefreshToken> findByUsername(String username);

    /**
     *
     * @param username
     */
    void deleteByUsername(String username);

    /**
     * Find all expired tokens, used to periodically clean up expired tokens in the database.
     * @param expiry
     * @return all expired tokens
     */
    List<RefreshToken> findAllByExpiryBefore(Instant expiry);

}
