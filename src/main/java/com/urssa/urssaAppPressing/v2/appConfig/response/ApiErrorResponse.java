package com.urssa.urssaAppPressing.v2.appConfig.response;


import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class ApiErrorResponse {

    private LocalDateTime timeStamp;
    private int status;
    private String error;
    private String message;
    private String path;


}
