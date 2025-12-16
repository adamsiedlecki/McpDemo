package net.asiedlecki.mcp.McpDemo.utils;

import net.asiedlecki.mcp.McpDemo.model.Employee;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LeaveService {


    public List<Employee> employeesWithoutLeave(List<Employee> employees) {
        return employees.stream()
                .filter(e -> !e.hasLeaveLeft())
                .toList();
    }


    public String leaveStatus(List<Employee> employees, String name) {
        return employees.stream()
                .filter(e -> e.name().equalsIgnoreCase(name))
                .findFirst()
                .map(e -> e.hasLeaveLeft()
                        ? e.name() + " ma jeszcze " + e.remainingLeaveDays() + " dni urlopu."
                        : e.name() + " nie ma już urlopu."
                )
                .orElse("Nie znaleziono pracownika");
    }
}