package com.ra.dto.request;

import lombok.*;
import lombok.extern.slf4j.Slf4j;

import java.io.Serializable;

@AllArgsConstructor(access = AccessLevel.NONE)
// best practice in DTO is to use only @Getter, in response DTO we can use @Builder...
public class SampleDTO implements Serializable {
    private Integer id;
    private String name;
}
