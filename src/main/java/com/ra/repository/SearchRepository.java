package com.ra.repository;

import com.ra.dto.response.PageResponse;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Repository
public class SearchRepository {

    @PersistenceContext
    private EntityManager entityManager;

    public PageResponse<?> getAllUsersWithSortByColumnAndSearch(int pageNo, int pageSize, String search,
                                                                String sortBy) {
        // query list user
        StringBuilder sqlQuery = new StringBuilder(
                "Select new com.ra.dto.response.UserDetailResponse(u.id, u.firstName, u.lastName, u.email, u.phone, u.dateOfBirth, u.gender, u.username) from User u where 1=1");
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
        StringBuilder sqlCountQuery = new StringBuilder("Select count(*) from User u where 1=1");
        if (StringUtils.hasLength(search)) {
            sqlCountQuery.append(" and lower(u.firstName) like lower(?1)");
            sqlCountQuery.append(" or lower(u.lastName) like lower(?2)");
            sqlCountQuery.append(" or lower(u.email) like lower(?3)");
        }
        Query selectCountQuery = entityManager.createQuery(sqlCountQuery.toString());

        if (StringUtils.hasLength(search)) {
            selectCountQuery.setParameter(1, String.format("%%%s%%", search));
            selectCountQuery.setParameter(2, String.format("%%%s%%", search));
            selectCountQuery.setParameter(3, String.format("%%%s%%", search));
        }
        Long totalElements = (Long) selectCountQuery.getSingleResult();
        System.out.println(totalElements);
        Pageable pageable = PageRequest.of(pageNo, pageSize);

        if (StringUtils.hasLength(sortBy)) {
            // firstName:asc|desc
            // Pattern pattern = Pattern.compile("^[a-zA-Z]+:(asc|desc)$");
            Pattern pattern = Pattern.compile("(\\w+?)(:)(.*)");
            Matcher matcher = pattern.matcher(sortBy);
            if (matcher.find()) {
                if (matcher.group(3).equalsIgnoreCase("asc")) {
                    pageable = PageRequest.of(pageNo, pageSize, Sort.by(new Sort.Order(Sort.Direction.ASC, matcher.group(1))));
                } else if (matcher.group(3).equalsIgnoreCase("desc")) {
                    pageable = PageRequest.of(pageNo, pageSize, Sort.by(new Sort.Order(Sort.Direction.DESC, matcher.group(1))));
                }
            }
        }


        Page<?> page = new PageImpl<Object>(users, pageable, totalElements);

        return PageResponse.builder()
                .pageNo(pageNo)
                .pageSize(pageSize)
                .totalPages(page.getTotalPages())
                .items(page.stream().toList())
                .build();
    }
}
