package com.banking.threeom.config;

import java.util.concurrent.Executor;
import javax.sql.DataSource;
import liquibase.integration.spring.SpringLiquibase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.autoconfigure.liquibase.DataSourceClosingSpringLiquibase;
import org.springframework.boot.autoconfigure.liquibase.LiquibaseDataSource;
import org.springframework.boot.autoconfigure.liquibase.LiquibaseProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.core.env.Profiles;
import liquibase.exception.LiquibaseException;
import org.springframework.util.StopWatch;

import java.sql.Connection;
import java.sql.SQLException;

import static com.banking.threeom.utils.SpringDefaultProfileUtil.SPRING_PROFILE_DEVELOPMENT;
import static com.banking.threeom.utils.SpringDefaultProfileUtil.SPRING_PROFILE_NO_LIQUIBASE;

@Configuration
public class LiquibaseConfiguration {

    private final Logger log = LoggerFactory.getLogger(LiquibaseConfiguration.class);

    private final Environment env;

    public LiquibaseConfiguration(Environment env) {
        this.env = env;
    }

    @Bean
    public SpringLiquibase liquibase(
        @Qualifier("taskExecutor") Executor executor,
        @LiquibaseDataSource ObjectProvider<DataSource> liquibaseDataSource,
        LiquibaseProperties liquibaseProperties,
        ObjectProvider<DataSource> dataSource,
        DataSourceProperties dataSourceProperties
    ) {
        // If you don't want Liquibase to start asynchronously, substitute by this:
        // SpringLiquibase liquibase = SpringLiquibaseUtil.createSpringLiquibase(liquibaseDataSource.getIfAvailable(), liquibaseProperties, dataSource.getIfUnique(), dataSourceProperties);
        SpringLiquibase liquibase = createAsyncSpringLiquibase(
            this.env,
            executor,
            liquibaseDataSource.getIfAvailable(),
            liquibaseProperties,
            dataSource.getIfUnique(),
            dataSourceProperties
        );
        liquibase.setChangeLog("classpath:config/liquibase/master.xml");
        liquibase.setContexts(liquibaseProperties.getContexts());
        liquibase.setDefaultSchema(liquibaseProperties.getDefaultSchema());
        liquibase.setLiquibaseSchema(liquibaseProperties.getLiquibaseSchema());
        liquibase.setLiquibaseTablespace(liquibaseProperties.getLiquibaseTablespace());
        liquibase.setDatabaseChangeLogLockTable(liquibaseProperties.getDatabaseChangeLogLockTable());
        liquibase.setDatabaseChangeLogTable(liquibaseProperties.getDatabaseChangeLogTable());
        liquibase.setDropFirst(liquibaseProperties.isDropFirst());
        liquibase.setLabels(liquibaseProperties.getLabels());
        liquibase.setChangeLogParameters(liquibaseProperties.getParameters());
        liquibase.setRollbackFile(liquibaseProperties.getRollbackFile());
        liquibase.setTestRollbackOnUpdate(liquibaseProperties.isTestRollbackOnUpdate());
        if (env.acceptsProfiles(Profiles.of(SPRING_PROFILE_NO_LIQUIBASE))) {
            liquibase.setShouldRun(false);
        } else {
            liquibase.setShouldRun(liquibaseProperties.isEnabled());
            log.debug("Configuring Liquibase");
        }
        return liquibase;
    }

    public static AsyncSpringLiquibase createAsyncSpringLiquibase(Environment env, Executor executor, DataSource liquibaseDatasource, LiquibaseProperties liquibaseProperties, DataSource dataSource, DataSourceProperties dataSourceProperties) {
        AsyncSpringLiquibase liquibase = new AsyncSpringLiquibase(executor, env);
        DataSource liquibaseDataSource = getDataSource(liquibaseDatasource, liquibaseProperties, dataSource);
        if (liquibaseDataSource != null) {
            liquibase.setCloseDataSourceOnceMigrated(false);
            liquibase.setDataSource(liquibaseDataSource);
        }
        return liquibase;
    }

    private static DataSource getDataSource(DataSource liquibaseDataSource, LiquibaseProperties liquibaseProperties, DataSource dataSource) {
        if (liquibaseDataSource != null) {
            return liquibaseDataSource;
        }
        if (liquibaseProperties.getUrl() == null && liquibaseProperties.getUser() == null) {
            return dataSource;
        }
        return null;
    }

    public static SpringLiquibase createSpringLiquibase(DataSource liquibaseDatasource, LiquibaseProperties liquibaseProperties, DataSource dataSource, DataSourceProperties dataSourceProperties) {
        SpringLiquibase liquibase;
        DataSource liquibaseDataSource = getDataSource(liquibaseDatasource, liquibaseProperties, dataSource);
        if (liquibaseDataSource != null) {
            liquibase = new SpringLiquibase();
            liquibase.setDataSource(liquibaseDataSource);
            return liquibase;
        }
        liquibase = new DataSourceClosingSpringLiquibase();
        //liquibase.setDataSource(createNewDataSource(liquibaseProperties, dataSourceProperties));
        return liquibase;
    }

    static class AsyncSpringLiquibase extends DataSourceClosingSpringLiquibase {
        /** Constant <code>DISABLED_MESSAGE="Liquibase is disabled"</code> */
        public static final String DISABLED_MESSAGE = "Liquibase is disabled";
        /** Constant <code>STARTING_ASYNC_MESSAGE="Starting Liquibase asynchronously, your"{trunked}</code> */
        public static final String STARTING_ASYNC_MESSAGE =
                "Starting Liquibase asynchronously, your database might not be ready at startup!";
        /** Constant <code>STARTING_SYNC_MESSAGE="Starting Liquibase synchronously"</code> */
        public static final String STARTING_SYNC_MESSAGE = "Starting Liquibase synchronously";
        /** Constant <code>STARTED_MESSAGE="Liquibase has updated your database in "{trunked}</code> */
        public static final String STARTED_MESSAGE = "Liquibase has updated your database in {} ms";
        /** Constant <code>EXCEPTION_MESSAGE="Liquibase could not start correctly, yo"{trunked}</code> */
        public static final String EXCEPTION_MESSAGE = "Liquibase could not start correctly, your database is NOT ready: " +
                "{}";

        /** Constant <code>SLOWNESS_THRESHOLD=5</code> */
        public static final long SLOWNESS_THRESHOLD = 5; // seconds
        /** Constant <code>SLOWNESS_MESSAGE="Warning, Liquibase took more than {} se"{trunked}</code> */
        public static final String SLOWNESS_MESSAGE = "Warning, Liquibase took more than {} seconds to start up!";

        // named "logger" because there is already a field called "log" in "SpringLiquibase"
        private final Logger logger = LoggerFactory.getLogger(AsyncSpringLiquibase.class);

        private final Executor executor;

        private final Environment env;

        /**
         * <p>Constructor for AsyncSpringLiquibase.</p>
         *
         * @param executor a {@link java.util.concurrent.Executor} object.
         * @param env a {@link org.springframework.core.env.Environment} object.
         */
        public AsyncSpringLiquibase(Executor executor, Environment env) {
            this.executor = executor;
            this.env = env;
        }

        /** {@inheritDoc} */
        @Override
        public void afterPropertiesSet() throws LiquibaseException {
            if (!env.acceptsProfiles(Profiles.of(SPRING_PROFILE_NO_LIQUIBASE))) {
                if (env.acceptsProfiles(Profiles.of(SPRING_PROFILE_DEVELOPMENT))) {
                    // Prevent Thread Lock with spring-cloud-context GenericScope
                    // https://github.com/spring-cloud/spring-cloud-commons/commit/aaa7288bae3bb4d6fdbef1041691223238d77b7b#diff-afa0715eafc2b0154475fe672dab70e4R328
                    try (Connection connection = getDataSource().getConnection()) {
                        executor.execute(() -> {
                            try {
                                logger.warn(STARTING_ASYNC_MESSAGE);
                                initDb();
                            } catch (LiquibaseException e) {
                                logger.error(EXCEPTION_MESSAGE, e.getMessage(), e);
                            }
                        });
                    } catch (SQLException e) {
                        logger.error(EXCEPTION_MESSAGE, e.getMessage(), e);
                    }
                } else {
                    logger.debug(STARTING_SYNC_MESSAGE);
                    initDb();
                }
            } else {
                logger.debug(DISABLED_MESSAGE);
            }
        }

        /**
         * <p>initDb.</p>
         *
         * @throws liquibase.exception.LiquibaseException if any.
         */
        protected void initDb() throws LiquibaseException {
            StopWatch watch = new StopWatch();
            watch.start();
            super.afterPropertiesSet();
            watch.stop();
            logger.debug(STARTED_MESSAGE, watch.getTotalTimeMillis());
            if (watch.getTotalTimeMillis() > SLOWNESS_THRESHOLD * 1000L) {
                logger.warn(SLOWNESS_MESSAGE, SLOWNESS_THRESHOLD);
            }
        }
    }
}
