package com.dogapi.tests;

import com.dogapi.util.ApiAssertions;
import io.qameta.allure.*;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Testes para o endpoint: GET /breed/{breed}/images
 *
 * Cobre os seguintes cenários:
 * - Resposta com status 200 para raça válida
 * - Lista de imagens não vazia
 * - URLs de imagens no formato correto
 * - Múltiplas raças válidas (parametrizado)
 * - Raça inválida retorna 404
 * - Raça com sub-raça retorna imagens
 */
@Feature("Imagens por Raça")
@Tag("breed-images")
@DisplayName("GET /breed/{breed}/images")
class BreedImagesTest extends BaseTest {

    @Test
    @Story("Buscar imagens de uma raça")
    @Severity(SeverityLevel.BLOCKER)
    @Description("Valida que a API retorna HTTP 200 e status 'success' para a raça 'hound'.")
    @DisplayName("Deve retornar HTTP 200 para raça válida (hound)")
    void shouldReturn200ForValidBreed() {
        Response response = dogApiClient.getBreedImages("hound");

        ApiAssertions.assertSuccessResponse(response);
        log.info("Raça 'hound' retornou status 200 com sucesso");
    }

    @Test
    @Story("Buscar imagens de uma raça")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Valida que a lista de imagens retornada para a raça 'hound' não está vazia.")
    @DisplayName("Deve retornar lista de imagens não vazia para raça válida")
    void shouldReturnNonEmptyImageListForValidBreed() {
        Response response = dogApiClient.getBreedImages("hound");

        List<String> images = response.jsonPath().getList("message", String.class);

        assertThat(images)
                .as("Lista de imagens não deve estar vazia")
                .isNotNull()
                .isNotEmpty();

        log.info("Total de imagens retornadas para 'hound': {}", images.size());
    }

    @Test
    @Story("Buscar imagens de uma raça")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Valida que todas as URLs de imagem estão no formato esperado da Dog API.")
    @DisplayName("Todas as URLs de imagem devem ser válidas")
    void allImageUrlsShouldBeValid() {
        Response response = dogApiClient.getBreedImages("bulldog");

        List<String> images = response.jsonPath().getList("message", String.class);

        assertThat(images)
                .as("Lista de imagens não deve estar vazia")
                .isNotEmpty();

        images.forEach(ApiAssertions::assertValidImageUrl);

        log.info("Todas as {} URLs de 'bulldog' são válidas.", images.size());
    }

    @ParameterizedTest(name = "Raça: {0}")
    @ValueSource(strings = {"hound", "bulldog", "labrador", "poodle", "beagle", "boxer", "husky", "retriever"})
    @Story("Buscar imagens de múltiplas raças")
    @Severity(SeverityLevel.NORMAL)
    @Description("Valida que a API retorna imagens para diversas raças conhecidas.")
    @DisplayName("Deve retornar imagens para múltiplas raças válidas")
    void shouldReturnImagesForMultipleValidBreeds(String breed) {
        Response response = dogApiClient.getBreedImages(breed);

        ApiAssertions.assertSuccessResponse(response);

        List<String> images = response.jsonPath().getList("message", String.class);
        assertThat(images)
                .as("Imagens da raça '%s' não devem estar vazias", breed)
                .isNotEmpty();

        log.info("Raça '{}': {} imagens retornadas.", breed, images.size());
    }

    @Test
    @Story("Raça inválida")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Valida que a API retorna HTTP 404 e status 'error' para uma raça inexistente.")
    @DisplayName("Deve retornar 404 para raça inexistente")
    void shouldReturn404ForInvalidBreed() {
        Response response = dogApiClient.getBreedImages("cachorroInexistente123");

        // A Dog API retorna 404 com body em texto, não JSON
        assertThat(response.statusCode())
                .as("HTTP status code")
                .isEqualTo(404);

        String message = response.getBody().asString();
        assertThat(message)
                .as("Mensagem de erro deve estar presente")
                .isNotBlank()
                .contains("Breed not found");

        log.info("Mensagem de erro recebida: {}", message);
    }

    @Test
    @Story("Buscar imagens com sub-raça")
    @Severity(SeverityLevel.NORMAL)
    @Description("Valida que a API retorna imagens consistentes para raças que possuem sub-raças.")
    @DisplayName("Deve retornar imagens para raça que possui sub-raças")
    void shouldReturnImagesForValidSubBreed() {
        // Testa a raça "hound" que sabemos ter sub-raças
        Response response = dogApiClient.getBreedImages("hound");

        ApiAssertions.assertSuccessResponse(response);

        List<String> images = response.jsonPath().getList("message", String.class);
        assertThat(images)
                .as("Raça 'hound' deve ter imagens")
                .isNotNull()
                .isNotEmpty();

        log.info("Raça 'hound' (que possui sub-raças) retornou {} imagens", images.size());

        // Verifica se a raça tem sub-raças no endpoint de listagem
        Response breedsResponse = dogApiClient.getAllBreeds();
        List<String> subBreeds = breedsResponse.jsonPath().getList("message.hound");

        assertThat(subBreeds)
                .as("Raça 'hound' deve ter sub-raças definidas")
                .isNotNull()
                .isNotEmpty();

        log.info("Raça 'hound' possui sub-raças: {}", subBreeds);
    }

    @Test
    @Story("Buscar imagens de uma raça")
    @Severity(SeverityLevel.MINOR)
    @Description("Valida que o Content-Type da resposta é application/json.")
    @DisplayName("Deve retornar Content-Type application/json")
    void shouldReturnJsonContentType() {
        Response response = dogApiClient.getBreedImages("poodle");

        ApiAssertions.assertJsonContentType(response);
    }

    @Test
    @Story("Buscar imagens de uma raça")
    @Severity(SeverityLevel.NORMAL)
    @Description("Valida que todas as imagens retornadas pertencem à raça solicitada (URL contém o nome da raça).")
    @DisplayName("URLs das imagens devem conter o nome da raça")
    void imageUrlsShouldContainBreedName() {
        String breed = "poodle";
        Response response = dogApiClient.getBreedImages(breed);

        List<String> images = response.jsonPath().getList("message", String.class);

        assertThat(images)
                .as("Lista de imagens não deve estar vazia")
                .isNotEmpty();

        images.forEach(url ->
                assertThat(url.toLowerCase())
                        .as("URL '%s' deve conter o nome da raça '%s'", url, breed)
                        .contains(breed)
        );

        log.info("Todas as {} imagens contêm o nome da raça '{}'", images.size(), breed);
    }

    @Test
    @Story("Buscar imagens de uma raça")
    @Severity(SeverityLevel.NORMAL)
    @Description("Valida o formato da URL da imagem com extensão válida.")
    @DisplayName("URLs das imagens devem ter extensão válida")
    void imageUrlsShouldHaveValidImageExtension() {
        Response response = dogApiClient.getBreedImages("beagle");

        List<String> images = response.jsonPath().getList("message", String.class);

        assertThat(images)
                .as("Lista de imagens não deve estar vazia")
                .isNotEmpty();

        images.forEach(url -> {
            assertThat(url.toLowerCase())
                    .as("URL deve terminar com extensão de imagem: %s", url)
                    .matches(".*\\.(jpg|jpeg|png|gif)$");
        });

        log.info("Todas as {} imagens têm extensão válida", images.size());
    }
}