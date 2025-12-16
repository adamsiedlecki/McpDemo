package net.asiedlecki.mcp.McpDemo.mcp;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.asiedlecki.mcp.McpDemo.model.Employee;
import net.asiedlecki.mcp.McpDemo.utils.LeaveService;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;

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
            name = "employeeLeaveStatus",
            description = """
                    Sprawdza stan urlopu wypoczynkowego konkretnego pracownika po jego name.

                    DZIAŁANIE:
                    1. Wyszukuje pracownika po nazwie (niezależnie od wielkości liter)
                    2. Sprawdza czy ma pozostałe dni urlopu
                    3. Zwraca czytelny status

                    WYMAGANE DANE WEJŚCIOWE:
                    - name: dokładna nazwa pracownika do wyszukania (np. "Jan Kowalski", albo "Marta")
                    - employees: lista obiektów Employee z polami:
                      * employeeName (string): imię i nazwisko
                      * remainingLeaveDays (int): pozostałe dni urlopu
                      * hasLeaveLeft (boolean): czy ma jeszcze urlop

                    PRZYKŁAD WYWOŁANIA:
                    Wejście: name="Anna Nowak", employees=[lista pracowników]
                    Wyjście: "Anna Nowak ma jeszcze 5 dni urlopu." LUB "Anna Nowak nie ma już urlopu."

                    UWAGA: Jeśli pracownik nie zostanie znaleziony, zwraca komunikat "Nie znaleziono pracownika"
                    """
    )
    public String employeeLeaveStatus(@ToolParam(description = "nazwa pracownika")String employeeName,
                                      @ToolParam(description = "lista pracowników") List<Employee> employees) {
        log.info("Invoked employeeLeaveStatus");
        return leaveService.leaveStatus(employees, employeeName);
    }
}