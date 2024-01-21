package account.config;

import account.model.SecurityAction;
import account.service.LogService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.access.AccessDeniedHandler;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.LinkedHashMap;
import java.util.Map;

@Configuration
public class CustomAccessDeniedHandler
        implements AccessDeniedHandler {
    @Autowired
    private LogService logService;

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
        logService.addEvent(SecurityAction.ACCESS_DENIED, SecurityContextHolder.getContext().getAuthentication().getName().toLowerCase(),
                request.getRequestURI(), request.getRequestURI());
        response.setStatus(HttpStatus.FORBIDDEN.value());
        response.setContentType("application/json");

        Map<String, Object> errorDetails = new LinkedHashMap<>();
        errorDetails.put("timestamp", DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS").format(LocalDateTime.now()));
        errorDetails.put("status", 403);
        errorDetails.put("error", "Forbidden");
        errorDetails.put("message", "Access Denied!");
        errorDetails.put("path", request.getRequestURI());

        response.getOutputStream().println(new ObjectMapper().writeValueAsString(errorDetails));
    }

}
