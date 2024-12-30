package com.ra.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
public class ResponseData<T> {
    private final int status;
    private final String message;
    private T data;

    public ResponseData(int status, String message) {
        this.status = status;
        this.message = message;
    }
}
