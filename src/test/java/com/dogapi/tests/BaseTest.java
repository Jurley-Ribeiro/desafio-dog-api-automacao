package com.dogapi.tests;

import com.dogapi.client.DogApiClient;
import com.dogapi.config.ConfigProvider;
import io.qameta.allure.Epic;
import org.junit.jupiter.api.BeforeEach;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Classe base para todos os testes.
 * Centraliza a inicialização do cliente e configurações compartilhadas.
 *
 * Princípio SOLID aplicado:
 * - SRP: gerencia exclusivamente o setup/teardown comum dos testes.
 * - DRY: elimina repetição de inicialização em cada classe de teste.
 */
@Epic("Dog API - Testes de Integração")
public abstract class BaseTest {

    protected final Logger log = LoggerFactory.getLogger(getClass());

    protected DogApiClient dogApiClient;

    @BeforeEach
    void setUp() {
        dogApiClient = new DogApiClient();
        log.info("=== Iniciando teste | Base URL: {} ===", ConfigProvider.get().baseUrl());
    }
}
