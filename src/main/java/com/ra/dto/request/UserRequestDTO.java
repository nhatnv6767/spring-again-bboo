package com.ra.dto.request;

import lombok.*;

import java.io.Serializable;

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
    private String firstName;
    private String lastName;
    private String email;
    private String phone;

}
