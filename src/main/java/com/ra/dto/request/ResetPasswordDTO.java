package com.ra.dto.request;

import lombok.Getter;

import java.io.Serializable;

@Getter
public class ResetPasswordDTO implements Serializable {
    private String secretKey;
    private String newPassword;
    private String confirmPassword;
}
