package com.dogapi.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Representa a resposta de erro da Dog API.
 *
 * Exemplo de payload:
 * { "status": "error", "message": "Breed not found (master breed does not exist)", "code": 404 }
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class ErrorResponse {

    private String status;
    private String message;
    private int code;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public boolean isError() {
        return "error".equalsIgnoreCase(status);
    }
}
