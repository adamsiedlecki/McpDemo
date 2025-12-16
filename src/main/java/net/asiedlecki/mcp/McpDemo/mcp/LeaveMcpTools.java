package net.asiedlecki.mcp.McpDemo.mcp;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.asiedlecki.mcp.McpDemo.model.Employee;
import net.asiedlecki.mcp.McpDemo.utils.LeaveService;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Component
@Slf4j
@RequiredArgsConstructor
public class LeaveMcpTools {


    private final LeaveService leaveService;
    private final ResourceLoader resourceLoader;

    @Tool(
            name = "employees",
            description = """
                    Zwraca listę nazw pracowników, wraz informacją o tym ile każdemu z nich zostało urlopu do wykorzystania
                     oraz ile tego urlopu pracownikowi przysługiwało w sumie.
                    """
    )
    public List<Employee> employees() {
        log.info("Invoked employees");
        try {
            return loadEmployeesFromFile();
        } catch (Exception e) {
            log.error("Error during fetching employees");
            throw new RuntimeException(e);
        }
    }

    @Tool(
            name = "employeesWithoutLeave",
            description = """
                    Zwraca listę nazw pracowników, którzy nie mają już dni urlopu wypoczynkowego do wykorzystania.
                            Wejście: lista obiektów Employee z polami: name (string) i leaveDays (number).
                            Użyj tego narzędzia, gdy pytają o 'kto nie ma już urlopu', 'komu skończył się urlop'.
                    """
    )
    public List<String> employeesWithoutLeave(
            @ToolParam(description = "lista pracowników") List<Employee> employees) {
        log.info("Invoked employeesWithoutLeave");
        return leaveService.employeesWithoutLeave(employees)
                .stream()
                .map(Employee::employeeName)
                .toList();
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