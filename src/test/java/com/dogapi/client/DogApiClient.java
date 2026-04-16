package com.dogapi.client;

import com.dogapi.config.ConfigProvider;
import com.dogapi.util.AllureRestAssuredFilter;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static io.restassured.RestAssured.given;

/**
 * Cliente HTTP responsável por todas as chamadas à Dog API.
 *
 * Princípios SOLID aplicados:
 * - SRP: única responsabilidade é realizar as requisições HTTP.
 * - OCP: novos endpoints podem ser adicionados sem alterar os existentes.
 * - DIP: depende da abstração {@link RequestSpecification}, não de implementações concretas.
 */
public class DogApiClient {

    private static final Logger log = LoggerFactory.getLogger(DogApiClient.class);

    private final RequestSpecification requestSpec;

    public DogApiClient() {
        this.requestSpec = buildRequestSpec();
    }

    // -------------------------------------------------------------------------
    // Endpoints
    // -------------------------------------------------------------------------

    /**
     * GET /breeds/list/all
     * Retorna todas as raças e sub-raças disponíveis.
     */
    public Response getAllBreeds() {
        log.info("GET /breeds/list/all");
        return given()
                .spec(requestSpec)
                .when()
                .get("/breeds/list/all");
    }

    /**
     * GET /breed/{breed}/images
     * Retorna todas as imagens de uma raça específica.
     *
     * @param breed nome da raça (ex: "hound", "bulldog")
     */
    public Response getBreedImages(String breed) {
        log.info("GET /breed/{}/images", breed);
        return given()
                .spec(requestSpec)
                .pathParam("breed", breed)
                .when()
                .get("/breed/{breed}/images");
    }

    /**
     * GET /breeds/image/random
     * Retorna uma imagem aleatória de qualquer raça.
     */
    public Response getRandomImage() {
        log.info("GET /breeds/image/random");
        return given()
                .spec(requestSpec)
                .when()
                .get("/breeds/image/random");
    }

    /**
     * GET /breed/{breed}/images/random/{count}
     * Retorna N imagens aleatórias de uma raça específica.
     *
     * @param breed nome da raça
     * @param count quantidade de imagens
     */
    public Response getBreedRandomImages(String breed, int count) {
        log.info("GET /breed/{}/images/random/{}", breed, count);
        return given()
                .spec(requestSpec)
                .pathParam("breed", breed)
                .pathParam("count", count)
                .when()
                .get("/breed/{breed}/images/random/{count}");
    }

    // -------------------------------------------------------------------------
    // Builder
    // -------------------------------------------------------------------------

    private RequestSpecification buildRequestSpec() {
        return given()
                .baseUri(ConfigProvider.get().baseUrl())
                .contentType("application/json")
                .accept("application/json")
                .filter(AllureRestAssuredFilter.get());
    }
}
