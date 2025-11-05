package ca.mikegabelmann.judgement.batch;

import ca.mikegabelmann.judgement.persistence.service.RefreshTokenService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.Instant;


/**
 *
 */
@EnableScheduling
@Service
public class JudgementBatch extends AbstractBatchJob {
    private static final Logger LOGGER = LoggerFactory.getLogger(JudgementBatch.class);

    private final RefreshTokenService refreshTokenService;


    @Autowired
    public JudgementBatch(final RefreshTokenService refreshTokenService) {
        this.refreshTokenService = refreshTokenService;
    }

    //TODO: send emails to users who registered a new account

    //TODO: send emails to users who have reset their password


    //@Scheduled(cron = "${judgement.batch.refresh}:0 */5 * * * *")
    @Scheduled(cron = "0 */5 * * * *")
    public void deleteRefresh()  throws Exception {
        if (isDisable()) {
            LOGGER.info("batch job - refresh delete is disabled");
        }

        int count = refreshTokenService.deleteAllExpiredTokens(Instant.now());

        LOGGER.info("batch job - refresh delete completed with {} records deleted", count);
    }

}
