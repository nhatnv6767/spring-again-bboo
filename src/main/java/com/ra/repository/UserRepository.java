package com.ra.repository;

import com.ra.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

/*
 * JpaSpecificationExecutor được sử dụng để thực hiện các truy vấn động (dynamic queries) phức tạp.
 * 
 * Lý do cần JpaSpecificationExecutor:
 * 1. Cho phép tạo các câu truy vấn động dựa trên nhiều điều kiện khác nhau:
 *    - Tìm kiếm theo nhiều tiêu chí (criteria)
 *    - Lọc dữ liệu linh hoạt
 *    - Kết hợp nhiều điều kiện AND/OR
 *
 * 2. Hỗ trợ tạo câu truy vấn phức tạp mà không cần viết JPQL/SQL:
 *    - Sử dụng Specification để định nghĩa điều kiện
 *    - Dễ dàng thêm/sửa/xóa điều kiện
 *    - Code sạch và dễ bảo trì hơn
 *
 * 3. Tối ưu hiệu năng:
 *    - Tạo câu truy vấn tối ưu
 *    - Tránh N+1 query problem
 *    - Có thể join với các bảng khác
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long>, JpaSpecificationExecutor<User> {

    Optional<User> findByEmail(String email);

    Optional<User> findByUsername(String username);

}
