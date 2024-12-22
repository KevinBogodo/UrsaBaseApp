package com.urssa.urssaAppPressing.v2.appConfig.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ApiResponseOk {

    @JsonProperty("message")
    private String message;

    @JsonProperty("data")
    private Object data;
}
