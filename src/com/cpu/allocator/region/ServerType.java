package com.cpu.allocator.region;

/**
 * Available Server types enum.
 */
public enum ServerType {
    large("large", 1),
    large1x("xlarge", 2),
    large2x("2xlarge", 4),
    large4x("4xlarge", 8),
    large8x("8xlarge", 16),
    large10x("10xlarge", 32);

    private final String typeName;
    private final int cpuCount;

    ServerType(String typeName, int cpuCount) {
        this.typeName = typeName;
        this.cpuCount = cpuCount;
    }

    public int getCpuCount() {
        return cpuCount;
    }

    public String getTypeName() {
        return typeName;
    }
}