package com.dogapi.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

/**
 * Representa a resposta dos endpoints:
 * - GET /breed/{breed}/images
 * - GET /breeds/image/random
 *
 * Exemplos de payload:
 * { "message": ["https://images.dog.ceo/...jpg", ...], "status": "success" }
 * { "message": "https://images.dog.ceo/...jpg", "status": "success" }
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class ImageResponse {

    /** Usado quando message é uma lista (endpoint /breed/{breed}/images) */
    private List<String> message;

    /** Usado quando message é uma string única (endpoint /random) */
    private String singleMessage;

    private String status;

    // --- Getters & Setters ---

    public List<String> getMessage() {
        return message;
    }

    public void setMessage(List<String> message) {
        this.message = message;
    }

    public String getSingleMessage() {
        return singleMessage;
    }

    public void setSingleMessage(String singleMessage) {
        this.singleMessage = singleMessage;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    // --- Helper methods ---

    public boolean hasImages() {
        return message != null && !message.isEmpty();
    }

    public boolean hasSingleImage() {
        return singleMessage != null && !singleMessage.isBlank();
    }
}
