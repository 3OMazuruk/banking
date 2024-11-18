package com.banking.threeom.config;

import com.banking.threeom.aop.logging.LoggingAspect;
import org.springframework.context.annotation.*;
import org.springframework.core.env.Environment;

import static com.banking.threeom.utils.SpringDefaultProfileUtil.SPRING_PROFILE_DEVELOPMENT;

@Configuration
@EnableAspectJAutoProxy
public class LoggingAspectConfiguration {

    @Bean
    @Profile(SPRING_PROFILE_DEVELOPMENT)
    public LoggingAspect loggingAspect(Environment env) {
        return new LoggingAspect(env);
    }
}
