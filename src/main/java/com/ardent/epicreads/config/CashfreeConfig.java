package com.ardent.epicreads.config;

import com.cashfree.Cashfree;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CashfreeConfig {

    @Value("${cashfree.clientId}")
    private String clientId;

    @Value("${cashfree.clientSecret}")
    private String clientSecret;

    @Value("${cashfree.environment}")
    private String environment;

    @PostConstruct
    public void init() {
        Cashfree.XClientId = clientId;
        Cashfree.XClientSecret = clientSecret;
        Cashfree.XEnvironment = environment.equals("PROD") ? Cashfree.PRODUCTION : Cashfree.SANDBOX;
    }
}
