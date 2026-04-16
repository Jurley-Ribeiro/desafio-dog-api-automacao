package com.dogapi.config;

import org.aeonbits.owner.Config;
import org.aeonbits.owner.Config.Sources;

/**
 * Centraliza todas as configurações da API.
 * Utiliza a biblioteca Owner para carregar propriedades de forma tipada.
 *
 * Princípio SOLID aplicado: Interface Segregation (ISP) —
 * cada interface expõe apenas o contrato que lhe compete.
 */
@Sources({
        "classpath:api.properties",
        "system:properties",
        "system:env"
})
public interface ApiConfig extends Config {

    @Key("api.base.url")
    @DefaultValue("https://dog.ceo/api")
    String baseUrl();

    @Key("api.timeout.connect")
    @DefaultValue("10000")
    int connectTimeout();

    @Key("api.timeout.read")
    @DefaultValue("15000")
    int readTimeout();

    @Key("api.status.success")
    @DefaultValue("success")
    String successStatus();
}
