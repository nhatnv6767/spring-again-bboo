package com.ra.repository.specification;

import lombok.Getter;

/**
 * Class SpecSearchCriteria dùng để định nghĩa các tiêu chí tìm kiếm (search
 * criteria)
 * cho việc xây dựng các đặc tả (specifications) trong Spring Data JPA
 *
 * Các thành phần chính:
 * - key: tên trường cần tìm kiếm (firstName, lastName, email,...)
 * - operation: phép toán so sánh (=, >, <, LIKE,...)
 * - value: giá trị cần so sánh
 * - orPredicate: cờ đánh dấu điều kiện OR (true) hay AND (false)
 */
@Getter
public class SpecSearchCriteria {
    private String key;
    private SearchOperation operation;
    private Object value;
    private Boolean orPredicate;

    public SpecSearchCriteria(String key, SearchOperation operation, Object value) {
        super();
        this.key = key;
        this.operation = operation;
        this.value = value;
    }

    /**
     * Constructor mở rộng hỗ trợ điều kiện OR
     * Nếu orPredicate = "'" thì sẽ dùng OR, ngược lại dùng AND
     */
    public SpecSearchCriteria(String orPredicate, String key, SearchOperation operation, Object value) {
        super();
        this.orPredicate = orPredicate != null && orPredicate.equals(SearchOperation.OR_PREDICATE_FLAG);
        this.key = key;
        this.operation = operation;
        this.value = value;
    }

    /**
     * Constructor xử lý các trường hợp tìm kiếm với LIKE
     * Hỗ trợ các pattern:
     * - *abc*: CONTAINS (chứa)
     * - *abc: ENDS_WITH (kết thúc bằng)
     * - abc*: STARTS_WITH (bắt đầu bằng)
     * - abc: EQUALITY (bằng)
     */
    public SpecSearchCriteria(String key, String operation, Object value, String prefix, String suffix) {
        SearchOperation oper = SearchOperation.getSimpleOperation(operation.charAt(0));
        if (oper != null) {
            if (oper == SearchOperation.EQUALITY) {
                boolean startWithAsterisk = prefix != null && prefix.contains(SearchOperation.ZERO_OR_MORE_REGEX);
                boolean endWithAsterisk = suffix != null && suffix.contains(SearchOperation.ZERO_OR_MORE_REGEX);
                if (startWithAsterisk && endWithAsterisk) {
                    this.operation = SearchOperation.CONTAINS;
                } else if (startWithAsterisk) {
                    this.operation = SearchOperation.ENDS_WITH;
                } else if (endWithAsterisk) {
                    this.operation = SearchOperation.STARTS_WITH;
                } else {
                    this.operation = SearchOperation.EQUALITY;
                }
            } else {
                this.operation = oper;
            }
        }
        this.key = key;
        this.value = value;
    }
}
