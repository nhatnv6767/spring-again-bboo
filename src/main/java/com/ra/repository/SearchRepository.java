package com.ra.repository;

import com.ra.dto.response.PageResponse;
import com.ra.model.User;
import com.ra.repository.criteria.SearchCriteria;
import com.ra.repository.criteria.UserSearchCriteriaQueryConsumer;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Repository
public class SearchRepository {

    @PersistenceContext
    private EntityManager entityManager;

    public PageResponse<?> searchUsers(int pageNo, int pageSize, String search,
                                       String sortBy) {
        // query list user
        StringBuilder sqlQuery = new StringBuilder(
                "Select new com.ra.dto.response.UserDetailResponse(u.id, u.firstName, u.lastName, u.email, u.phone, u.dateOfBirth, u.gender, u.username) from User u where 1=1");
        if (StringUtils.hasLength(search)) {
            sqlQuery.append(" and lower(u.firstName) like lower(:firstName)");
            sqlQuery.append(" or lower(u.lastName) like lower(:lastName)");
            sqlQuery.append(" or lower(u.email) like lower(:email)");
        }

        if (StringUtils.hasLength(sortBy)) {

            Pattern pattern = Pattern.compile("(\\w+?)(:)(.*)");
            Matcher matcher = pattern.matcher(sortBy);
            if (matcher.find()) {
                String direction = matcher.group(3).toLowerCase();
                if (direction.equals("asc") || direction.equals("desc")) {
                    sqlQuery.append(String.format(" order by u.%s %s", matcher.group(1), direction));
                }
            }

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

        Page<?> page = new PageImpl<Object>(users, PageRequest.of(pageNo, pageSize), totalElements);

        return PageResponse.builder()
                .pageNo(pageNo)
                .pageSize(pageSize)
                .totalPages(page.getTotalPages())
                .items(page.stream().toList())
                .build();
    }

    public PageResponse advanceSearchUser(int pageNo, int pageSize, String sortBy, String... search) {

        List<SearchCriteria> criteriaList = new ArrayList<>();

        // 1. get list of users
        // firstName:T, lastName:T, ...
        if (search != null) {
            for (String s : search) {
                // firstName:value
                // group(1) = firstName
                // group(2) = :
                // group(3) = value
                Pattern pattern = Pattern.compile("(\\w+?)([:><])(.*)"); // : or > or <
                Matcher matcher = pattern.matcher(s);
                if (matcher.find()) {
                    criteriaList.add(new SearchCriteria(matcher.group(1), matcher.group(2), matcher.group(3)));
                }
            }
        }

        // 2. get total number of records - paging
        List<User> users = getUsers(pageNo, pageSize, criteriaList, sortBy);
        long totalElements = getTotalElements(criteriaList);
        return PageResponse.builder()
                .pageNo(pageNo)
                .pageSize(pageSize)
                .totalPages((int) totalElements)
                .items(users)
                .build();
    }

    /**
     * Hàm này dùng để tìm kiếm User theo nhiều tiêu chí (criteria) khác nhau
     * <p>
     * Ví dụ: Tìm kiếm user theo các điều kiện sau:
     * - firstName:John -> tìm user có firstName = "John"
     * - age>20 -> tìm user có tuổi lớn hơn 20
     * - city:HaNoi -> tìm user ở thành phố Hà Nội
     * <p>
     * Giải thích chi tiết:
     * 1. CriteriaBuilder: Là interface dùng để tạo các câu query, predicate,
     * expression
     * 2. CriteriaQuery: Đại diện cho câu query SELECT
     * 3. Root: Đại diện cho entity gốc trong câu query (FROM User)
     * 4. Predicate: Biểu thức điều kiện (WHERE)
     * <p>
     * Ví dụ cụ thể:
     * Input: criteriaList = [
     * {key: "firstName", operation: ":", value: "John"},
     * {key: "age", operation: ">", value: "20"}
     * ]
     * <p>
     * Sẽ tạo ra câu query tương đương:
     * SELECT u FROM User u WHERE u.firstName = 'John' AND u.age > 20
     *
     * @param pageNo       số trang hiện tại
     * @param pageSize     số lượng record mỗi trang
     * @param criteriaList danh sách các tiêu chí tìm kiếm
     * @param sortBy       trường để sắp xếp
     * @return danh sách User thỏa mãn điều kiện
     */
    private List<User> getUsers(int pageNo, int pageSize, List<SearchCriteria> criteriaList, String sortBy) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<User> query = criteriaBuilder.createQuery(User.class);
        Root<User> root = query.from(User.class);

        // process search criteria
        Predicate predicate = criteriaBuilder.conjunction();
        UserSearchCriteriaQueryConsumer queryConsumer = new UserSearchCriteriaQueryConsumer(criteriaBuilder, predicate,
                root);
        criteriaList.forEach(queryConsumer);
        predicate = queryConsumer.getPredicate();
        query.where(predicate);

        // sort
        if (StringUtils.hasLength(sortBy)) {
            Pattern pattern = Pattern.compile("(\\w+?)(:)(asc|desc)");
            Matcher matcher = pattern.matcher(sortBy);
            if (matcher.find()) {
                if (matcher.group(3).equalsIgnoreCase("asc")) {
                    query.orderBy(criteriaBuilder.asc(root.get(matcher.group(1))));
                } else if (matcher.group(3).equalsIgnoreCase("desc")) {
                    query.orderBy(criteriaBuilder.desc(root.get(matcher.group(1))));
                }
            }
        }

        return entityManager.createQuery(query).setFirstResult(pageNo).setMaxResults(pageSize).getResultList();
    }

    private long getTotalElements(List<SearchCriteria> criteriaList) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Long> countQuery = criteriaBuilder.createQuery(Long.class);
        Root<User> root = countQuery.from(User.class);

        Predicate predicate = criteriaBuilder.conjunction();
        UserSearchCriteriaQueryConsumer queryConsumer = new UserSearchCriteriaQueryConsumer(criteriaBuilder, predicate, root);
        criteriaList.forEach(queryConsumer);
        predicate = queryConsumer.getPredicate();
        countQuery.select(criteriaBuilder.count(root)).where(predicate);

        return entityManager.createQuery(countQuery).getSingleResult();
    }
}
