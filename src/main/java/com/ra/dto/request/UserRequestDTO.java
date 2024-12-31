package com.ra.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ra.dto.validator.EnumPattern;
import com.ra.dto.validator.EnumValue;
import com.ra.dto.validator.GenderSubset;
import com.ra.dto.validator.PhoneNumber;
import com.ra.util.*;
import jakarta.validation.constraints.*;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Set;

import static com.ra.util.Gender.*;

/*
 * Serializable là một interface đánh dấu (marker interface) trong Java, được sử dụng để cho phép một đối tượng có thể chuyển đổi thành một chuỗi byte
 * và ngược lại (deserialization).
 *
 * Lý do UserRequestDTO implements Serializable:
 * 1. Cho phép đối tượng UserRequestDTO có thể được lưu trữ (persist):
 *    - Có thể lưu xuống file
 *    - Có thể lưu vào database
 *    - Có thể cache trong bộ nhớ
 *
 * 2. Cho phép truyền đối tượng qua mạng:
 *    - Khi gọi API, object cần được chuyển thành JSON/byte để truyền qua HTTP
 *    - Trong phân tán hệ thống (distributed system), các object cần được serialize để truyền giữa các service
 *
 * 3. Đây là best practice khi làm việc với DTO (Data Transfer Object):
 *    - DTO thường được dùng để truyền data giữa các layer
 *    - Implement Serializable giúp DTO linh hoạt hơn trong việc truyền nhận data
 *
 * 4. Đảm bảo backward compatibility:
 *    - Khi thêm/sửa/xóa field trong tương lai
 *    - Giúp version control tốt hơn
 */
@Getter

public class UserRequestDTO implements Serializable {
    @NotBlank(message = "firstName must be not blank")
    private String firstName;

    @NotNull(message = "lastName must be not null")
    private String lastName;

    @Email(message = "email invalid format")
    private String email;

    @PhoneNumber(message = "phone invalid format")
    private String phone;

    @NotNull(message = "dateOfBirth must be not null")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    @JsonFormat(pattern = "MM/dd/yyyy")
    private Date dateOfBirth;

    @GenderSubset(anyOf = { MALE, FEMALE, OTHER })
    private Gender gender;

    @NotNull(message = "username must be not null")
    private String username;

    @NotNull(message = "password must be not null")
    private String password;

    @NotNull(message = "type must be not null")
    @EnumValue(name = "type", enumClass = UserType.class)
    private String type;

    @NotNull(message = "status must be not null")
    @EnumValue(name = "status", enumClass = UserStatus.class)
    private UserStatus status;

    @NotEmpty(message = "addresses can not empty")
    private Set<AddressDTO> addresses;

    public UserRequestDTO(String firstName, String lastName, String email, String phone) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phone = phone;
    }

}
