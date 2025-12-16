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
}