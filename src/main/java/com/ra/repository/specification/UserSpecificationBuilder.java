package com.ra.repository.specification;

import com.ra.model.User;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

/**
 * Class để xây dựng các điều kiện tìm kiếm (specifications) cho entity User
 * Sử dụng Builder pattern để tạo ra các điều kiện tìm kiếm
 */
public class UserSpecificationBuilder {
    // Danh sách các tiêu chí tìm kiếm
    public final List<SpecSearchCriteria> params;

    /**
     * Constructor nhận vào danh sách các tiêu chí tìm kiếm
     */
    public UserSpecificationBuilder() {
        this.params = new ArrayList<>();
    }

    /**
     * Phương thức tiện ích để thêm điều kiện tìm kiếm không có orPredicate
     */
    public UserSpecificationBuilder with(String key, String operation, Object value, String prefix, String suffix) {
        return with(null, key, operation, value, prefix, suffix);
    }

    /**
     * Thêm một điều kiện tìm kiếm mới vào builder
     *
     * @param orPredicate Nếu true thì sẽ dùng OR, false thì dùng AND để kết hợp với
     *                    điều kiện trước
     * @param key         Tên trường cần tìm kiếm
     * @param operation   Phép toán so sánh (=, >, <, ...)
     * @param value       Giá trị cần so sánh
     * @param prefix      Tiền tố cho điều kiện LIKE
     * @param suffix      Hậu tố cho điều kiện LIKE
     */
    public UserSpecificationBuilder with(String orPredicate, String key, String operation, Object value, String prefix,
                                         String suffix) {
        // Chuyển đổi operation string sang enum SearchOperation
        SearchOperation oper = SearchOperation.getSimpleOperation(operation.charAt(0));

        if (oper != null) {
            // Xử lý đặc biệt cho trường hợp tìm kiếm bằng LIKE
            if (oper == SearchOperation.EQUALITY) {
                // Kiểm tra xem có dấu * ở đầu hoặc cuối không
                boolean startWithAsterisk = prefix != null && prefix.contains(SearchOperation.ZERO_OR_MORE_REGEX);
                boolean endWithAsterisk = suffix != null && suffix.contains(SearchOperation.ZERO_OR_MORE_REGEX);

                // Xác định loại operation dựa vào vị trí của dấu *
                if (startWithAsterisk && endWithAsterisk) {
                    oper = SearchOperation.CONTAINS; // %value%
                } else if (startWithAsterisk) {
                    oper = SearchOperation.ENDS_WITH; // %value
                } else if (endWithAsterisk) {
                    oper = SearchOperation.STARTS_WITH; // value%
                } else {
                    oper = SearchOperation.EQUALITY; // value
                }
            }
        }
        // Thêm điều kiện mới vào danh sách
        params.add(new SpecSearchCriteria(orPredicate, key, oper, value));
        return this;
    }

    /**
     * Xây dựng Specification từ tất cả các điều kiện đã thêm vào
     *
     * @return Specification<User> kết hợp tất cả các điều kiện
     */
    public Specification<User> build() {
        if (params.isEmpty()) {
            return null;
        }

        // Khởi tạo specification với điều kiện đầu tiên
        Specification<User> result = new UserSpecification(params.get(0));

        // Kết hợp các điều kiện còn lại
        for (int i = 1; i < params.size(); i++) {
            // Nếu là OR thì dùng or(), nếu không thì dùng and() để kết hợp
            result = params.get(i)
                    .getOrPredicate()
                    ? Specification.where(result).or(new UserSpecification(params.get(i)))
                    : Specification.where(result).and(new UserSpecification(params.get(i)));
        }

        return result;
    }
}
