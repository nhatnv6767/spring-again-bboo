package com.ra.repository.specification;

import com.ra.model.User;
import com.ra.util.Gender;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.AllArgsConstructor;
import org.springframework.data.jpa.domain.Specification;

@AllArgsConstructor
public class UserSpecification implements Specification<User> {

    private SpecSearchCriteria criteria;

    @Override
    public Predicate toPredicate(Root<User> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
        // Từ khóa yield được sử dụng trong switch expression để trả về giá trị từ một
        // khối code phức tạp
        // Khi một case trong switch cần thực hiện nhiều câu lệnh và trả về kết quả
        // yield giúp chỉ định giá trị trả về một cách rõ ràng
        // Ví dụ trong case GREATER_THAN và LESS_THAN, ta cần khai báo biến value trước
        // nên phải dùng khối code {} và yield để trả về kết quả
        return switch (criteria.getOperation()) {
            case EQUALITY -> criteriaBuilder.equal(criteriaBuilder.upper(root.get(criteria.getKey())),
                    criteria.getValue().toString().toUpperCase());
            case NEGATION -> criteriaBuilder.notEqual(criteriaBuilder.upper(root.get(criteria.getKey())),
                    criteria.getValue().toString().toUpperCase());
            case GREATER_THAN -> {
                String value = criteria.getValue().toString().toUpperCase();
                yield criteriaBuilder.greaterThan(criteriaBuilder.upper(root.get(criteria.getKey())), value);
            }
            case LESS_THAN -> {
                String value = criteria.getValue().toString().toUpperCase();
                yield criteriaBuilder.lessThan(criteriaBuilder.upper(root.get(criteria.getKey())), value);
            }
            case LIKE -> criteriaBuilder.like(criteriaBuilder.upper(root.get(criteria.getKey())),
                    criteria.getValue().toString().toUpperCase());
            case STARTS_WITH -> criteriaBuilder.like(criteriaBuilder.upper(root.get(criteria.getKey())),
                    criteria.getValue().toString().toUpperCase() + "%");
            case ENDS_WITH -> criteriaBuilder.like(criteriaBuilder.upper(root.get(criteria.getKey())),
                    "%" + criteria.getValue().toString().toUpperCase());
            case CONTAINS -> criteriaBuilder.like(criteriaBuilder.upper(root.get(criteria.getKey())),
                    "%" + criteria.getValue().toString().toUpperCase() + "%");
            default -> throw new IllegalArgumentException("Invalid operation: " + criteria.getOperation());
        };
    }
}
