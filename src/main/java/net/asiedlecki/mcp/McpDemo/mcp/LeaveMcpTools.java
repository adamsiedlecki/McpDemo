package net.asiedlecki.mcp.McpDemo.mcp;

import net.asiedlecki.mcp.McpDemo.model.Employee;
import net.asiedlecki.mcp.McpDemo.utils.LeaveService;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class LeaveMcpTools {


    private final LeaveService leaveService;


    public LeaveMcpTools(LeaveService leaveService) {
        this.leaveService = leaveService;
    }


    @Tool(
            name = "employeesWithoutLeave",
            description = "Zwraca listę pracowników, którzy nie mają już urlopu"
    )
    public List<String> employeesWithoutLeave(List<Employee> employees) {
        return leaveService.employeesWithoutLeave(employees)
                .stream()
                .map(Employee::name)
                .toList();
    }


    @Tool(
            name = "employeeLeaveStatus",
            description = "Sprawdza status urlopu pracownika"
    )
    public String employeeLeaveStatus(String name, List<Employee> employees) {
        return leaveService.leaveStatus(employees, name);
    }
}