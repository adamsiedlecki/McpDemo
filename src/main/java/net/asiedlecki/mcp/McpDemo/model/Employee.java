package net.asiedlecki.mcp.McpDemo.model;

public record Employee(String employeeName, int yearsOfService, int usedLeaveDays) {
    public int totalLeaveDays() {
        return yearsOfService < 10 ? 20 : 26;
    }

    public int remainingLeaveDays() {
        return totalLeaveDays() - usedLeaveDays;
    }

    public boolean hasLeaveLeft() {
        return remainingLeaveDays() > 0;
    }
}