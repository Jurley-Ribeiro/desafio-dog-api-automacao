package com.dogapi.config;

import org.aeonbits.owner.ConfigFactory;

/**
 * Fábrica responsável por fornecer instâncias de configuração.
 *
 * Princípio SOLID aplicado: Single Responsibility (SRP) —
 * única responsabilidade: criar e fornecer a configuração da API.
 */
public final class ConfigProvider {

    private static final ApiConfig INSTANCE = ConfigFactory.create(ApiConfig.class);

    private ConfigProvider() {
        // utility class — não deve ser instanciada
    }

    public static ApiConfig get() {
        return INSTANCE;
    }
}
