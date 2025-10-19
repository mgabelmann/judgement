package ca.mikegabelmann.judgement.controller.config;

import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

@Component
public class JudgementConfiguration {
    private static final Logger LOGGER = LoggerFactory.getLogger(JudgementConfiguration.class);

    private final Environment environment;

    @Value("${judgement.security.pepper}")
    private String pepper;


    /**
     * Constructor.
     * @param environment environment
     */
    public JudgementConfiguration(final Environment environment) {
        this.environment = environment;
    }

    @PostConstruct
    public void postConstruct() {
        if (pepper == null || pepper.isEmpty()) {
            /* if pepper is null or empty then securing passwords hashes in the database will be compromised. It should
             * not be set as a spring property. It should be securely stored and set as an environment variable, system
             * property, external config file not stored in repository with limited access, etc.
             */
            throw new NullPointerException("Pepper can not be null or empty. You MUST set this value, preferably as an environment variable.");

        } else if (isProfileActive("local")) {
            LOGGER.info("security pepper=" + pepper);

        } else {
            LOGGER.info("pepper has been successfully set");
        }

    }

    public String getPepper() {
        return pepper;
    }

    public boolean isProfileActive(final String profile) {
        for (String activeProfile : environment.getActiveProfiles()) {
            if (activeProfile.equalsIgnoreCase(profile)) {
                return true;
            }
        }

        return false;
    }

}
