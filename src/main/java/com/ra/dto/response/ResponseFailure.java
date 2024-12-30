package com.ra.dto.response;

import org.springframework.http.HttpStatusCode;

public class ResponseFailure extends ResponseSuccess {
    public ResponseFailure(HttpStatusCode status, String message) {
        super(status, message);
    }

    public ResponseFailure(HttpStatusCode status, String message, Object data) {
        super(status, message, data);
    }
}
