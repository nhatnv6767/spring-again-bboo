package com.ra.service;

import com.ra.dto.request.UserRequestDTO;
import com.ra.dto.response.UserDetailResponse;
import com.ra.util.UserStatus;

import java.util.List;

public interface UserService {
    long saveUser(UserRequestDTO requestDTO);

    void updateUser(long userId, UserRequestDTO requestDTO);

    void changeStatus(long userId, UserStatus status);

    void deleteUser(long userId);

    UserDetailResponse getUser(long userId);

    List<UserDetailResponse> getAllUsers(int pageNo, int pageSize, String sortBy);
}
