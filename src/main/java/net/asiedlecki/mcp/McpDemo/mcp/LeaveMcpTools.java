package net.asiedlecki.mcp.McpDemo.mcp;

import lombok.extern.slf4j.Slf4j;
import net.asiedlecki.mcp.McpDemo.model.Employee;
import net.asiedlecki.mcp.McpDemo.utils.LeaveService;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Slf4j
public class LeaveMcpTools {


    private final LeaveService leaveService;


    public LeaveMcpTools(LeaveService leaveService) {
        this.leaveService = leaveService;
    }


    @Tool(
            name = "employeesWithoutLeave",
            description = """
                    Zwraca listę nazw pracowników, którzy nie mają już dni urlopu wypoczynkowego do wykorzystania.
                            Wejście: lista obiektów Employee z polami: name (string) i leaveDays (number).
                            Użyj tego narzędzia, gdy pytają o 'kto nie ma już urlopu', 'komu skończył się urlop'.
                    """
    )
    public List<String> employeesWithoutLeave(List<Employee> employees) {
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
    public String employeeLeaveStatus(String name, List<Employee> employees) {
        log.info("Invoked employeeLeaveStatus");
        return leaveService.leaveStatus(employees, name);
    }
}