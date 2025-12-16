package net.asiedlecki.mcp.McpDemo.utils;

import net.asiedlecki.mcp.McpDemo.model.Employee;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LeaveService {


    public List<Employee> employeesWithoutLeave(List<Employee> employees) {
        return employees.stream()
                .filter(e -> !e.isLeaveLeft())
                .toList();
    }


    public String leaveStatus(List<Employee> employees, String name) {
        return employees.stream()
                .filter(e -> e.employeeName().equalsIgnoreCase(name))
                .findFirst()
                .map(e -> e.isLeaveLeft()
                        ? e.employeeName() + " ma jeszcze " + e.getRemainingLeaveDays() + " dni urlopu."
                        : e.employeeName() + " nie ma już urlopu."
                )
                .orElse("Nie znaleziono pracownika");
    }
}