package net.asiedlecki.mcp.McpDemo.mcp;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.asiedlecki.mcp.McpDemo.model.Employee;
import net.asiedlecki.mcp.McpDemo.security.model.UserInfo;
import net.asiedlecki.mcp.McpDemo.security.services.UserInfoClient;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.core.io.ResourceLoader;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Component
@Slf4j
@RequiredArgsConstructor
public class LeaveMcpTools {

    private final ResourceLoader resourceLoader;
    private final UserInfoClient userInfoClient;

    @Tool(
            name = "employeeInfo",
            description = """
                    Zwraca dane o użytkowniku który jest pracownikiem, wraz informacją o tym ile zostało mu urlopu wypoczynkowego do wykorzystania
                     oraz ile tego urlopu pracownikowi przysługiwało w sumie.
                    """
    )
    public Map<String, Object> employees() {
        log.info("Invoked employees");
        try {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            JwtAuthenticationToken jwtAuth = (JwtAuthenticationToken) auth;
            String sub = jwtAuth.getToken().getSubject();
            Optional<Employee> employee = loadEmployeesFromFile().stream().filter(e -> e.employeeName().equals(sub)).findFirst();
            if (employee.isPresent()) {
                return Map.of("employee data", employee);
            } else {
                return Map.of("message", "Nie znaleziono danych użytkownika: " + sub);
            }
        } catch (Exception e) {
            log.error("Error during fetching employees");
            return Map.of("message", "Error during fetching employees");
        }
    }

    @Tool(
            name = "employeeLeaveTerms",
            description = """
                    Zwraca tekst zasad, na jakich pracownikom przyszługują urlopy.
                    """
    )
    public String employeeLeaveTerms() {
        log.info("Invoked employeeLeaveTerms");
        return """
                Pracownikom przysługuje urlop wypoczynkowy. Wynosi on 26 dni w roku,
                gdy ktoś przepracował 10 lat lub więcej, oraz 20 dni w roku, jeśli ktoś przepracował krócej niż 10 lat.
               """;
    }

    @Tool(name = "getCurrentUser", description = """
            Pobiera informacje na temat aktualnie zalogowanego użytkownika, między innymi nazwę (name)
            """)
    public Map<String, Object> getCurrentUser() {
        try {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            JwtAuthenticationToken jwtAuth = (JwtAuthenticationToken) auth;
            String sub = jwtAuth.getToken().getSubject();
            String accessToken = jwtAuth.getToken().getTokenValue();

            UserInfo userInfo = userInfoClient.getUserInfo(sub, accessToken);

            return Map.of("name", userInfo.getSub(),
                    "surname", userInfo.getFamilyName(),
                    "email", userInfo.getEmail());
        } catch (RuntimeException e) {
            log.error("Error during current user mcp operation", e);
            return Map.of("message to user", "wystąpił  błąd podczas pobierania informacji: " + e.getMessage());
        }
    }

    private List<Employee> loadEmployeesFromFile() throws IOException {
        String content = resourceLoader
                .getResource("classpath:urlopy test.csv")
                .getContentAsString(StandardCharsets.UTF_8);

        return content.lines()
                .skip(1) // pomijamy nagłówek
                .map(line -> line.split(";"))
                .map(parts -> new Employee(
                        parts[0],
                        Integer.parseInt(parts[1]),
                        Integer.parseInt(parts[2])
                ))
                .toList();
    }
}