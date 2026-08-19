package spring.security.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;
import spring.security.config.properties.CorsProperties;

@Configuration
public class CorsConfig {
    private final CorsProperties corsProperties;

    public CorsConfig(CorsProperties corsProperties) {
        this.corsProperties = corsProperties;
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();

        // 🔹 Cho phép các Origin (Domain của Frontend) nào được phép gọi API
        // Trong môi trường dev, thường là localhost của React (3000) hoặc Vite (5173)
        configuration.setAllowedOrigins(corsProperties.allowedOrigins());

        // 🔹 Cho phép các phương thức HTTP nào
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));

        // 🔹 Cho phép gửi các Header nào lên (Bắt buộc phải có Authorization để gửi JWT)
        configuration.setAllowedHeaders(List.of(
                "Authorization", "Content-Type", "Cache-Control", "X-XSRF-TOKEN", "X-Request-ID"));
        configuration.setExposedHeaders(List.of("X-Request-ID"));
        configuration.setMaxAge(3600L);

        // 🔹 Cho phép gửi kèm Credentials (ví dụ: Cookie, thông tin xác thực) nếu Frontend cần
        configuration.setAllowCredentials(true);

        // Áp dụng cấu hình này cho tất cả các endpoint (/**) của hệ thống
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);

        return source;
    }

}
