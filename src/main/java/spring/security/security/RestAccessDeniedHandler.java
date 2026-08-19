package spring.security.security;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;
import spring.security.config.RequestCorrelationFilter;
import spring.security.dto.response.ErrorResponse;
import tools.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.time.Instant;

@Component
public class RestAccessDeniedHandler implements AccessDeniedHandler {
    private final ObjectMapper objectMapper;

    public RestAccessDeniedHandler(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public void handle(HttpServletRequest request,
                       HttpServletResponse response,
                       AccessDeniedException exception) throws IOException {
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("UTF-8");
        Object requestId = request.getAttribute(RequestCorrelationFilter.ATTRIBUTE_NAME);
        objectMapper.writeValue(response.getOutputStream(), ErrorResponse.builder()
                .code("AUTH_003")
                .message("Không có quyền thực hiện hành động này")
                .path(request.getRequestURI())
                .requestId(requestId == null ? null : requestId.toString())
                .timestamp(Instant.now())
                .build());
    }
}
