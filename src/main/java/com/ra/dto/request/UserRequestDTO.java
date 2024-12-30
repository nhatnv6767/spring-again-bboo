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
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class UserRequestDTO implements Serializable {
    @NotBlank(message = "First name is required")
    private String firstName;

    @NotNull(message = "Last name is required")
    private String lastName;

    @Email(message = "Email should be valid")
    private String email;

    //    @Pattern(regexp = "^\\d{10}$", message = "Phone number should be 10 digits")
    @PhoneNumber
    private String phone;

    @NotNull(message = "Date of birth is required")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date dateOfBirth;

    @NotEmpty(message = "Permissions are required")
    private List<String> permissions;

    //    @Pattern(regexp = "^ACTIVE|INACTIVE|NONE$", message = "status must be one in {ACTIVE, INACTIVE, NONE}")
    @EnumPattern(name = "status", regexp = "ACTIVE|INACTIVE|NONE", message = "status must be one in {ACTIVE, INACTIVE, NONE}")
    private UserStatus status;

    @GenderSubset(anyOf = {MALE, FEMALE, OTHER})
    private Gender gender;

    @NotNull(message = "type must be not null")
    @EnumValue(name = "type", enumClass = UserType.class)
    private String type;

}
