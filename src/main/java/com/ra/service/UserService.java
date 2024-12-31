package com.ra.service;

import com.ra.dto.request.UserRequestDTO;
import com.ra.dto.response.PageResponse;
import com.ra.dto.response.UserDetailResponse;
import com.ra.util.UserStatus;

public interface UserService {
    long saveUser(UserRequestDTO requestDTO);

    void updateUser(long userId, UserRequestDTO requestDTO);

    void changeStatus(long userId, UserStatus status);

    void deleteUser(long userId);

    UserDetailResponse getUser(long userId);

    PageResponse<?> getAllUsersWithSortBy(int pageNo, int pageSize, String sortBy);

    PageResponse<?> getAllUsersWithSortByMultipleColumn(int pageNo, int pageSize, String... sorts); // List<String>
}
