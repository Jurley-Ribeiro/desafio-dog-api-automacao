package com.dogapi.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;
import java.util.Map;

/**
 * Representa a resposta do endpoint GET /breeds/list/all.
 *
 * Exemplo de payload:
 * {
 *   "message": { "affenpinscher": [], "african": [], ... },
 *   "status": "success"
 * }
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class BreedsListResponse {

    private Map<String, List<String>> message;
    private String status;

    public Map<String, List<String>> getMessage() {
        return message;
    }

    public void setMessage(Map<String, List<String>> message) {
        this.message = message;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public boolean hasBreeds() {
        return message != null && !message.isEmpty();
    }

    public boolean containsBreed(String breed) {
        return message != null && message.containsKey(breed);
    }
}
