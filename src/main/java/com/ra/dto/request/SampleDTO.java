package com.ra.dto.request;

import lombok.*;

import java.io.Serializable;

@Getter
// best practice in DTO is to use only @Getter, in response DTO we can use @Builder
public class SampleDTO implements Serializable {
    private Integer id;
    private String name;
}
