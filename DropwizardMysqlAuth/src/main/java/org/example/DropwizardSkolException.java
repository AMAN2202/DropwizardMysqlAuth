package org.example;

import com.fasterxml.jackson.annotation.JsonProperty;

public class DropwizardSkolException extends Throwable {

    private int code;

    public DropwizardSkolException() {

        this(500);

    }

    public DropwizardSkolException(int code) {
        this(code, "Error while processing the request", null);
    }

    public DropwizardSkolException(int code, String message) {
        this(code, message, null);
    }

    public DropwizardSkolException(int code, String message, Throwable throwable) {

        super(message, throwable);

        this.code = code;

    }

    @JsonProperty
    public int getCode() {

        return code;

    }

    @JsonProperty
    @Override
    public String getMessage() {
        return super.getMessage();
    }
}