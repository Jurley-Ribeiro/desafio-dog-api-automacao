package com.dogapi.tests;

import com.dogapi.model.BreedsListResponse;
import com.dogapi.util.ApiAssertions;
import io.qameta.allure.*;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Testes para o endpoint: GET /breeds/list/all
 *
 * Cobre os seguintes cenários:
 * - Resposta com status HTTP 200
 * - Payload com status "success"
 * - Lista de raças não vazia
 * - Raças conhecidas presentes na resposta
 * - Sub-raças corretamente vinculadas
 * - Formato de Content-Type correto
 */
@Feature("Listagem de Raças")
@Tag("breeds")
@Tag("smoke")
@DisplayName("GET /breeds/list/all")
class BreedsListTest extends BaseTest {

    @Test
    @Story("Listar todas as raças")
    @Severity(SeverityLevel.BLOCKER)
    @Description("Valida que a API retorna HTTP 200 e status 'success' ao listar todas as raças.")
    @DisplayName("Deve retornar HTTP 200 e status success")
    void shouldReturn200AndSuccessStatus() {
        Response response = dogApiClient.getAllBreeds();

        ApiAssertions.assertSuccessResponse(response);
        log.info("Status HTTP e payload validados com sucesso.");
    }

    @Test
    @Story("Listar todas as raças")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Valida que o Content-Type da resposta é application/json.")
    @DisplayName("Deve retornar Content-Type application/json")
    void shouldReturnJsonContentType() {
        Response response = dogApiClient.getAllBreeds();

        ApiAssertions.assertJsonContentType(response);
    }

    @Test
    @Story("Listar todas as raças")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Valida que a lista de raças não está vazia e contém pelo menos uma raça.")
    @DisplayName("Deve retornar lista de raças não vazia")
    void shouldReturnNonEmptyBreedList() {
        Response response = dogApiClient.getAllBreeds();

        BreedsListResponse breedsResponse = response.as(BreedsListResponse.class);

        assertThat(breedsResponse.getMessage())
                .as("Mapa de raças não deve ser nulo")
                .isNotNull();

        assertThat(breedsResponse.getMessage())
                .as("Deve existir ao menos uma raça")
                .isNotEmpty();

        log.info("Total de raças retornadas: {}", breedsResponse.getMessage().size());
    }

    @Test
    @Story("Listar todas as raças")
    @Severity(SeverityLevel.NORMAL)
    @Description("Valida que raças conhecidas estão presentes na resposta.")
    @DisplayName("Deve conter raças conhecidas na resposta")
    void shouldContainKnownBreeds() {
        Response response = dogApiClient.getAllBreeds();

        BreedsListResponse breedsResponse = response.as(BreedsListResponse.class);
        Map<String, List<String>> breeds = breedsResponse.getMessage();

        assertThat(breeds)
                .as("Raças conhecidas devem estar presentes")
                .containsKeys("hound", "bulldog", "labrador", "poodle", "retriever");
    }

    @Test
    @Story("Listar todas as raças")
    @Severity(SeverityLevel.NORMAL)
    @Description("Valida que raças com sub-raças possuem a lista de sub-raças corretamente preenchida.")
    @DisplayName("Deve retornar sub-raças para raças que as possuem")
    void shouldReturnSubBreedsForBreedsWithSubBreeds() {
        Response response = dogApiClient.getAllBreeds();

        BreedsListResponse breedsResponse = response.as(BreedsListResponse.class);
        Map<String, List<String>> breeds = breedsResponse.getMessage();

        // "hound" tem várias sub-raças conhecidas
        assertThat(breeds)
                .containsKey("hound");

        List<String> houndSubBreeds = breeds.get("hound");
        assertThat(houndSubBreeds)
                .as("Raça 'hound' deve ter sub-raças")
                .isNotEmpty()
                .contains("afghan", "basset", "blood", "english");

        log.info("Sub-raças de 'hound': {}", houndSubBreeds);
    }

    @Test
    @Story("Listar todas as raças")
    @Severity(SeverityLevel.MINOR)
    @Description("Valida que raças sem sub-raças possuem lista vazia (não nula) como valor.")
    @DisplayName("Deve retornar lista vazia para raças sem sub-raças")
    void shouldReturnEmptyListForBreedsWithoutSubBreeds() {
        Response response = dogApiClient.getAllBreeds();

        BreedsListResponse breedsResponse = response.as(BreedsListResponse.class);
        Map<String, List<String>> breeds = breedsResponse.getMessage();

        // "labrador" não possui sub-raças
        assertThat(breeds)
                .containsKey("labrador");

        List<String> labradorSubBreeds = breeds.get("labrador");
        assertThat(labradorSubBreeds)
                .as("Raça 'labrador' deve ter lista de sub-raças vazia")
                .isEmpty();
    }

    @Test
    @Story("Listar todas as raças")
    @Severity(SeverityLevel.MINOR)
    @Description("Valida que todas as chaves (nomes de raças) contêm apenas letras minúsculas.")
    @DisplayName("Todos os nomes de raças devem ser minúsculos")
    void allBreedNamesShouldBeLowerCase() {
        Response response = dogApiClient.getAllBreeds();

        BreedsListResponse breedsResponse = response.as(BreedsListResponse.class);
        Map<String, List<String>> breeds = breedsResponse.getMessage();

        breeds.keySet().forEach(breed ->
                assertThat(breed)
                        .as("Nome da raça '%s' deve ser minúsculo", breed)
                        .isEqualTo(breed.toLowerCase())
        );
    }
}
