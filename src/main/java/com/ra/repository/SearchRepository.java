package com.ra.repository;

import com.ra.dto.response.PageResponse;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import java.util.List;

@Repository
public class SearchRepository {

    @PersistenceContext
    private EntityManager entityManager;

    public PageResponse<?> getAllUsersWithSortByColumnAndSearch(int pageNo, int pageSize, String search,
            String sortBy) {
        // query list user
        StringBuilder sqlQuery = new StringBuilder("Select u from User u where 1=1");
        if (StringUtils.hasLength(search)) {
            sqlQuery.append(" and lower(u.firstName) like lower(:firstName)");
            sqlQuery.append(" or lower(u.lastName) like lower(:lastName)");
            sqlQuery.append(" or lower(u.email) like lower(:email)");
        }
        Query selectQuery = entityManager.createQuery(sqlQuery.toString());
        selectQuery.setFirstResult(pageNo);
        selectQuery.setMaxResults(pageSize);

        if (StringUtils.hasLength(search)) {
            // String.format("%%%s%%", search) được sử dụng để tạo pattern cho câu lệnh LIKE
            // trong SQL
            // Trong đó:
            // %% - Đại diện cho ký tự % trong chuỗi kết quả vì % là ký tự đặc biệt trong
            // String.format()
            // %s - Placeholder sẽ được thay thế bằng giá trị của tham số search
            // Ví dụ: Nếu search = "john"
            // Kết quả: String.format("%%%s%%", "john") = "%john%"
            // Điều này cho phép tìm kiếm các bản ghi có chứa "john" ở bất kỳ vị trí nào
            // VD: "john doe", "johnny", "mr john" đều sẽ được tìm thấy
            selectQuery.setParameter("firstName", String.format("%%%s%%", search));
            selectQuery.setParameter("lastName", String.format("%%%s%%", search));
            selectQuery.setParameter("email", String.format("%%%s%%", search));
        }

        List users = selectQuery.getResultList();
        System.out.println("users = " + users);

        // query num of record
        return null;
    }
}
