package com.banking.threeom.utils;

import org.springframework.boot.SpringApplication;
import org.springframework.core.env.Environment;

import java.util.HashMap;
import java.util.Map;

/**
 * Utility class to load a Spring profile to be used as default
 * when there is no <code>spring.profiles.active</code> set in the environment or as command line argument.
 * If the value is not available in <code>application.yml</code> then <code>dev</code> profile will be used as default.
 */
public class SpringDefaultProfileUtil {

    private static final String SPRING_PROFILE_DEFAULT = "spring.profiles.default";
    /** Constant <code>SPRING_PROFILE_TEST="test"</code> */
    public static final String SPRING_PROFILE_DEVELOPMENT = "dev";
    /** Constant <code>SPRING_PROFILE_TEST="test"</code> */
    public static final String SPRING_PROFILE_TEST = "test";
    /** Constant <code>SPRING_PROFILE_PRODUCTION="prod"</code> */
    public static final String SPRING_PROFILE_PRODUCTION = "prod";
    /** Spring profile used when deploying with Spring Cloud (used when deploying to CloudFoundry)
     Constant <code>SPRING_PROFILE_CLOUD="cloud"</code> */
    public static final String SPRING_PROFILE_CLOUD = "cloud";/** Spring profile used when deploying with Spring Cloud (used when deploying to CloudFoundry)
    /** Spring profile used to disable running liquibase
     Constant <code>SPRING_PROFILE_NO_LIQUIBASE="no-liquibase"</code> */
    public static final String SPRING_PROFILE_NO_LIQUIBASE = "no-liquibase";

    private SpringDefaultProfileUtil() {
    }

    /**
     * Set a default to use when no profile is configured.
     *
     * @param app the Spring application
     */
    public static void addDefaultProfile(SpringApplication app) {
        Map<String, Object> defProperties = new HashMap<>();
        /*
         * The default profile to use when no other profiles are defined
         * This cannot be set in the <code>application.yml</code> file.
         * See https://github.com/spring-projects/spring-boot/issues/1219
         */
        defProperties.put(SPRING_PROFILE_DEFAULT, SPRING_PROFILE_DEVELOPMENT);
        app.setDefaultProperties(defProperties);
    }

    /**
     * Get the profiles that are applied else get default profiles.
     *
     * @param env spring environment
     * @return profiles
     */
    public static String[] getActiveProfiles(Environment env) {
        String[] profiles = env.getActiveProfiles();
        if (profiles.length == 0) {
            return env.getDefaultProfiles();
        }
        return profiles;
    }
}
