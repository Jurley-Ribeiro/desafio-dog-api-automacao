package com.dogapi.util;

import com.dogapi.config.ConfigProvider;
import io.restassured.response.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Centraliza as asserções comuns da Dog API, evitando duplicação de código
 * e tornando os testes mais legíveis.
 *
 * Princípio SOLID aplicado:
 * - SRP: única responsabilidade é realizar asserções.
 * - DRY: elimina repetição de validações nos testes.
 */
public final class ApiAssertions {

    private static final Logger log = LoggerFactory.getLogger(ApiAssertions.class);
    private static final String SUCCESS_STATUS = ConfigProvider.get().successStatus();

    private ApiAssertions() {
        // utility class
    }

    /**
     * Valida que a resposta possui status HTTP 200 e status de payload "success".
     */
    public static void assertSuccessResponse(Response response) {
        assertHttpStatus(response, 200);
        assertSuccessPayloadStatus(response);
    }

    /**
     * Valida o código HTTP da resposta.
     */
    public static void assertHttpStatus(Response response, int expectedStatus) {
        log.debug("Validando HTTP status: esperado={}, recebido={}", expectedStatus, response.statusCode());
        assertThat(response.statusCode())
                .as("HTTP status code")
                .isEqualTo(expectedStatus);
    }

    /**
     * Valida que o campo 'status' do payload é "success".
     */
    public static void assertSuccessPayloadStatus(Response response) {
        String actualStatus = response.jsonPath().getString("status");
        log.debug("Validando payload status: esperado={}, recebido={}", SUCCESS_STATUS, actualStatus);
        assertThat(actualStatus)
                .as("Campo 'status' do payload")
                .isEqualToIgnoringCase(SUCCESS_STATUS);
    }

    /**
     * Valida que o campo 'message' não é nulo e não está vazio.
     */
    public static void assertMessageNotEmpty(Response response) {
        Object message = response.jsonPath().get("message");
        assertThat(message)
                .as("Campo 'message' não deve ser nulo")
                .isNotNull();
    }

    /**
     * Valida que uma URL de imagem possui o formato esperado da Dog API.
     */
    public static void assertValidImageUrl(String url) {
        assertThat(url)
                .as("URL de imagem inválida: %s", url)
                .isNotBlank()
                .startsWith("https://images.dog.ceo/");

        assertThat(url.toLowerCase())
                .as("URL deve terminar com extensão de imagem válida")
                .matches(".*\\.(jpg|jpeg|png|gif)$");
    }

    /**
     * Valida que o Content-Type é application/json.
     */
    public static void assertJsonContentType(Response response) {
        assertThat(response.contentType())
                .as("Content-Type")
                .containsIgnoringCase("application/json");
    }

    /**
     * Valida que a resposta de erro contém o status HTTP e campo status "error".
     */
    public static void assertErrorResponse(Response response, int expectedHttpStatus) {
        assertHttpStatus(response, expectedHttpStatus);
        String status = response.jsonPath().getString("status");
        assertThat(status)
                .as("Campo 'status' para cenário de erro")
                .isEqualToIgnoringCase("error");
    }
}
