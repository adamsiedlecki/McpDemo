package net.asiedlecki.mcp.McpDemo.model;

public record Employee(String employeeName, int yearsOfService, int usedLeaveDays) {
    public int getTotalLeaveDays() {
        return yearsOfService < 10 ? 20 : 26;
    }

    public int getRemainingLeaveDays() {
        return getTotalLeaveDays() - usedLeaveDays;
    }

    public boolean isLeaveLeft() {
        return getRemainingLeaveDays() > 0;
    }
}