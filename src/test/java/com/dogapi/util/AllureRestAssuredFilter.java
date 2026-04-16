package com.dogapi.util;

import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.filter.Filter;

/**
 * Provê o filtro do Allure para REST Assured de forma singleton.
 * Garante que todos os detalhes de request/response sejam anexados ao relatório.
 *
 * Princípio SOLID aplicado: SRP — responsabilidade única de fornecer o filtro.
 */
public final class AllureRestAssuredFilter {

    private static final Filter INSTANCE = new AllureRestAssured();

    private AllureRestAssuredFilter() {
        // utility class
    }

    public static Filter get() {
        return INSTANCE;
    }
}
