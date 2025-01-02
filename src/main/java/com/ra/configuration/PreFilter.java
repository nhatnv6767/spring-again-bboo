package com.ra.configuration;

import com.ra.service.JwtService;
import com.ra.service.UserService;
import com.ra.util.TokenType;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
@Component
@RequiredArgsConstructor
public class PreFilter extends OncePerRequestFilter {
    private final JwtService jwtService;
    private final UserService userService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        // Bước 1: Log thông tin request để debug
        log.info("PreFilter: " + request.getMethod() + " request to " + request.getRequestURI());

        // Bước 2: Lấy JWT token từ header Authorization
        final String authorizationHeader = request.getHeader("Authorization");

        // Bước 3: Kiểm tra token có tồn tại và đúng format không
        // Nếu không có token hoặc không đúng format -> cho qua filter
        if (!StringUtils.hasLength(authorizationHeader) || !authorizationHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        // Bước 4: Tách lấy JWT token (bỏ "Bearer " ở đầu)
        final String jwt = authorizationHeader.substring(7);

        // Bước 5: Lấy username từ JWT token
        final String userName = jwtService.extractUsername(jwt, TokenType.ACCESS_TOKEN);

        // Bước 6: Xác thực token nếu:
        // - Có username hợp lệ
        // - Chưa được xác thực trước đó
        if (StringUtils.hasLength(userName) && SecurityContextHolder.getContext().getAuthentication() == null) {
            // Bước 7: Load thông tin user từ database
            UserDetails userDetails = userService.userDetailsService().loadUserByUsername(userName);

            // Bước 8: Kiểm tra token có hợp lệ không
            if (jwtService.isValid(jwt, TokenType.ACCESS_TOKEN, userDetails)) {
                // Bước 9: Tạo context xác thực mới
                SecurityContext context = SecurityContextHolder.createEmptyContext();

                // Bước 10: Tạo token xác thực với thông tin user
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        userDetails,
                        null, // credentials
                        userDetails.getAuthorities() // quyền của user
                );

                // Bước 11: Set thông tin chi tiết cho token
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                // Bước 12: Lưu context xác thực vào SecurityContextHolder
                context.setAuthentication(authToken);
                SecurityContextHolder.setContext(context);
            }
        }

        // Bước 13: Chuyển request đến filter tiếp theo
        filterChain.doFilter(request, response);
    }
}
