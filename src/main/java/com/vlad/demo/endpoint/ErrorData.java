package com.vlad.demo.endpoint;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@EqualsAndHashCode
@ToString
public class ErrorData {

    @JsonProperty
    private final String apiErrorMessage;
    @JsonProperty
    private final Integer statusCode;

    public ErrorData(String apiErrorMessage, Integer statusCode){
        this.apiErrorMessage = apiErrorMessage;
        this.statusCode = statusCode;
    }
}
