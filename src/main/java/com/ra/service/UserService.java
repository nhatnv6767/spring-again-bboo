package com.ra.service;

import com.ra.dto.request.UserRequestDTO;
import com.ra.dto.response.PageResponse;
import com.ra.dto.response.UserDetailResponse;
import com.ra.util.UserStatus;
import jakarta.mail.MessagingException;
import org.springframework.data.domain.Pageable;

import java.io.UnsupportedEncodingException;

public interface UserService {
    long saveUser(UserRequestDTO requestDTO) throws MessagingException, UnsupportedEncodingException;

    void updateUser(long userId, UserRequestDTO requestDTO);

    void changeStatus(long userId, UserStatus status);

    void deleteUser(long userId);

    UserDetailResponse getUser(long userId);

    PageResponse<?> getAllUsersWithSortBy(int pageNo, int pageSize, String sortBy);

    PageResponse<?> getAllUsersWithSortByMultipleColumns(int pageNo, int pageSize, String... sorts); // List<String>

    PageResponse<?> getAllUsersWithSortByColumnAndSearch(int pageNo, int pageSize, String search, String sortBy);

    PageResponse<?> advanceSearchByCriteria(int pageNo, int pageSize, String sortBy, String address, String... search);

    PageResponse<?> advanceSearchBySpecification(Pageable pageable, String[] user, String[] address);

    void confirmUser(long userId, String secretCode);
}
