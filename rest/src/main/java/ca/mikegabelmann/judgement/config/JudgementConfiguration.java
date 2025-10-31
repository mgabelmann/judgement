package ca.mikegabelmann.judgement.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;


@Component
public class JudgementConfiguration {
    private static final Logger LOGGER = LoggerFactory.getLogger(JudgementConfiguration.class);

    private final Environment environment;


    /**
     * Constructor.
     * @param environment environment
     */
    public JudgementConfiguration(final Environment environment) {
        this.environment = environment;
    }

    /**
     * Determine if given profile is active.
     * @param profile profile to check
     * @return true if active, false otherwise
     */
    public boolean isProfileActive(final String profile) {
        for (String activeProfile : environment.getActiveProfiles()) {
            if (activeProfile.equalsIgnoreCase(profile)) {
                return true;
            }
        }

        return false;
    }

}
