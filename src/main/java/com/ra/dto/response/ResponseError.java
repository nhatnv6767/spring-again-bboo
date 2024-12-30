package com.ra.dto.response;

import lombok.*;

//@Builder
public class ResponseError extends ResponseData {
    public ResponseError(int status, String message, Object data) {
        super(status, message, data);
    }

    public ResponseError(int status, String message) {
        super(status, message);
    }
}
