package com.ra.configuration;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.i18n.AcceptHeaderLocaleResolver;

import java.util.List;
import java.util.Locale;

@Configuration
public class LocalResolver extends AcceptHeaderLocaleResolver implements WebMvcConfigurer {
    @Override
    public Locale resolveLocale(HttpServletRequest request) {
        String languageHeader = request.getHeader("Accept-Language");
        // Kiểm tra xem header Accept-Language có giá trị hay không
        // Nếu có giá trị thì xử lý theo các bước sau:
        // 1. Locale.LanguageRange.parse(languageHeader): Parse chuỗi Accept-Language
        // thành danh sách các language range
        // 2. List.of(new Locale("en"), new Locale("fr")): Tạo danh sách các locale được
        // hỗ trợ (en và fr)
        // 3. Locale.lookup(): So khớp language range với danh sách locale để tìm locale
        // phù hợp nhất
        // Nếu không có giá trị header thì trả về locale mặc định của hệ thống
        // return StringUtils.hasText(languageHeader) ?
        // Locale.lookup(Locale.LanguageRange.parse(languageHeader), List.of(new
        // Locale("en"), new Locale("fr"))) : Locale.getDefault();
        return StringUtils.hasText(languageHeader)
                ? Locale.lookup(Locale.LanguageRange.parse(languageHeader),
                        List.of(new Locale("en"), new Locale("jp")))
                : Locale.getDefault();

    }

    /*
     * @Bean: Đánh dấu method này tạo ra một bean để Spring quản lý
     * 
     * ResourceBundleMessageSource được sử dụng để:
     * 1. Đọc các file properties chứa các message đa ngôn ngữ
     * 2. Cung cấp cơ chế load và cache các message
     * 
     * Cấu hình:
     * - setBasename("messages"): Định nghĩa tên cơ sở của file properties
     * VD: messages_en.properties, messages_jp.properties
     * - setDefaultEncoding("UTF-8"): Set encoding mặc định là UTF-8 để hỗ trợ
     * Unicode
     * - setCacheSeconds(3600): Cache message trong 3600 giây (1 giờ) để tối ưu hiệu
     * năng
     */
    @Bean
    public ResourceBundleMessageSource bundleMessageSource() {
        ResourceBundleMessageSource source = new ResourceBundleMessageSource();
        source.setBasename("messages");
        source.setDefaultEncoding("UTF-8");
        source.setCacheSeconds(3600);
        return source;
    }
}
