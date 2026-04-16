package com.dogapi.tests;

import com.dogapi.util.ApiAssertions;
import io.qameta.allure.*;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Testes para o endpoint: GET /breeds/image/random
 *
 * Cobre os seguintes cenários:
 * - Resposta com status 200
 * - URL de imagem válida no campo message
 * - Randomicidade: múltiplas chamadas retornam URLs diferentes
 * - Content-Type correto
 * - Tempo de resposta aceitável
 */
@Feature("Imagem Aleatória")
@Tag("random-image")
@Tag("smoke")
@DisplayName("GET /breeds/image/random")
class RandomImageTest extends BaseTest {

    private static final long MAX_RESPONSE_TIME_MS = 3000;

    @Test
    @Story("Buscar imagem aleatória")
    @Severity(SeverityLevel.BLOCKER)
    @Description("Valida que a API retorna HTTP 200 e status 'success' para imagem aleatória.")
    @DisplayName("Deve retornar HTTP 200 e status success")
    void shouldReturn200AndSuccessStatus() {
        Response response = dogApiClient.getRandomImage();

        ApiAssertions.assertSuccessResponse(response);
    }

    @Test
    @Story("Buscar imagem aleatória")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Valida que o campo 'message' contém uma URL de imagem válida.")
    @DisplayName("Deve retornar URL de imagem válida no campo message")
    void shouldReturnValidImageUrl() {
        Response response = dogApiClient.getRandomImage();

        String imageUrl = response.jsonPath().getString("message");

        assertThat(imageUrl)
                .as("Campo 'message' deve conter URL de imagem")
                .isNotBlank();

        ApiAssertions.assertValidImageUrl(imageUrl);

        log.info("URL de imagem aleatória retornada: {}", imageUrl);
    }

    @Test
    @Story("Buscar imagem aleatória")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Valida que o Content-Type da resposta é application/json.")
    @DisplayName("Deve retornar Content-Type application/json")
    void shouldReturnJsonContentType() {
        Response response = dogApiClient.getRandomImage();

        ApiAssertions.assertJsonContentType(response);
    }

    @Test
    @Story("Buscar imagem aleatória")
    @Severity(SeverityLevel.NORMAL)
    @Description("Valida que múltiplas chamadas retornam URLs diferentes, confirmando a randomicidade.")
    @DisplayName("Deve retornar URLs diferentes em chamadas sucessivas (randomicidade)")
    void shouldReturnDifferentUrlsOnSuccessiveCalls() {
        int totalCalls = 5;
        Set<String> urls = new HashSet<>();

        for (int i = 0; i < totalCalls; i++) {
            Response response = dogApiClient.getRandomImage();
            ApiAssertions.assertSuccessResponse(response);
            urls.add(response.jsonPath().getString("message"));
        }

        log.info("URLs únicas obtidas em {} chamadas: {}", totalCalls, urls.size());

        assertThat(urls)
                .as("Ao menos 2 URLs distintas devem ser retornadas em %d chamadas", totalCalls)
                .hasSizeGreaterThan(1);
    }

    @Test
    @Story("Buscar imagem aleatória")
    @Severity(SeverityLevel.MINOR)
    @Description("Valida que o tempo de resposta é inferior a 3 segundos.")
    @DisplayName("Deve responder em menos de 3 segundos")
    void shouldRespondWithinAcceptableTime() {
        Response response = dogApiClient.getRandomImage();

        long responseTime = response.getTime();
        log.info("Tempo de resposta: {} ms", responseTime);

        assertThat(responseTime)
                .as("Tempo de resposta deve ser inferior a %d ms", MAX_RESPONSE_TIME_MS)
                .isLessThan(MAX_RESPONSE_TIME_MS);
    }

    @RepeatedTest(3)
    @Story("Buscar imagem aleatória")
    @Severity(SeverityLevel.MINOR)
    @Description("Executa 3 vezes consecutivas para validar consistência da resposta.")
    @DisplayName("Deve retornar resposta consistente em execuções repetidas")
    void shouldConsistentlyReturnValidResponseOnRepeatedCalls() {
        Response response = dogApiClient.getRandomImage();

        ApiAssertions.assertSuccessResponse(response);

        String imageUrl = response.jsonPath().getString("message");
        ApiAssertions.assertValidImageUrl(imageUrl);
    }

    @Test
    @Story("Buscar imagem aleatória")
    @Severity(SeverityLevel.MINOR)
    @Description("Valida que a URL retornada aponta para o domínio oficial de imagens da Dog API.")
    @DisplayName("URL da imagem deve apontar para o domínio oficial images.dog.ceo")
    void imageShouldPointToOfficialDomain() {
        Response response = dogApiClient.getRandomImage();

        String imageUrl = response.jsonPath().getString("message");

        assertThat(imageUrl)
                .as("URL deve apontar para o domínio oficial images.dog.ceo")
                .startsWith("https://images.dog.ceo/");
    }
}
