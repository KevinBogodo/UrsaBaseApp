package com.urssa.urssaAppPressing.v2.appConfig.response;


import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Optional;

@Data
@AllArgsConstructor
public class ApiErrorResponse {

    private LocalDateTime timeStamp;
    private int status;
    private String error;
    private String message;
    private Object errors;
    private String path;


}
