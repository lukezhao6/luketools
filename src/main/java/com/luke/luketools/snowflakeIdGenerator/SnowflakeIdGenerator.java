package com.luke.luketools.snowflakeIdGenerator;

public class SnowflakeIdGenerator {
    // 2021-01-01 00:00:00
    private final long epoch = 1609459200000L; // 设置起始时间戳，例如2021-01-01 00:00:00的毫秒数
    // 机器ID所占的位数
    private final long machineIdBits = 10L;
    // 机器ID的最大值
    private final long maxMachineId = -1L ^ (-1L << machineIdBits);
    // 毫秒内自增位
    private final long sequenceBits = 12L;
    // 机器ID向左移12位
    private final long machineIdShift = sequenceBits;
    // 时间戳向左移22位(10+12)
    private final long timestampLeftShift = sequenceBits + machineIdBits;
    // 生成序列的掩码，这里为4095 (0b111111111111=0xfff=4095)
    private final long sequenceMask = -1L ^ (-1L << sequenceBits);
    // 机器ID(0~1023)
    private long machineId;
    // 毫秒内序列(0~4095)
    private long sequence = 0L;
    // 上次生成ID的时间戳
    private long lastTimestamp = -1L;
    // 构造器
    public SnowflakeIdGenerator(long machineId) {
        if (machineId < 0 || machineId > maxMachineId) {
            throw new IllegalArgumentException("Machine ID must be between 0 and " + maxMachineId);
        }
        this.machineId = machineId;
    }
    // 线程安全的id生成方法
    public synchronized long generateId() {
        long timestamp = System.currentTimeMillis();

        if (timestamp < lastTimestamp) {
            // 如果当前时间小于上一次ID生成的时间戳，说明系统时钟回退过，出现问题返回-1
            throw new RuntimeException("Clock moved backwards. Refusing to generate ID for " + (lastTimestamp - timestamp) + " milliseconds.");
        }
        // 如果是同一时间生成的，则进行毫秒内序列
        if (lastTimestamp == timestamp) {
            // sequence自增，因为sequence只有12bit，所以和sequenceMask相与一下，去掉高位
            sequence = (sequence + 1) & sequenceMask;
            // 判断是否溢出，也就是每毫秒内超过4095，当为4096时，与sequenceMask相与，sequence就等于0
            if (sequence == 0) {
                // 自旋等待到下一毫秒
                timestamp = tilNextMillis(lastTimestamp);
            }
        } else {
            // 如果不是同一时间生成的，则重置sequence
            sequence = 0L;
        }
        // 上次生成ID的时间戳
        lastTimestamp = timestamp;
        // 移位并通过或运算拼到一起组成64位的ID
        return ((timestamp - epoch) << timestampLeftShift) |
                (machineId << machineIdShift) |
                sequence;
    }
    // 自旋等待到下一毫秒
    private long tilNextMillis(long lastTimestamp) {
        // 获取当前时间戳
        long timestamp = System.currentTimeMillis();
        // 如果当前时间小于上一次ID生成的时间戳，说明系统时钟回退过，出现问题返回-1
        while (timestamp <= lastTimestamp) {
            // 获取当前时间戳
            timestamp = System.currentTimeMillis();
        }
        return timestamp;
    }

    public static void main(String[] args) {
        // 使用机器ID为1的实例
        SnowflakeIdGenerator idGenerator = new SnowflakeIdGenerator(1);

        // 生成10个ID并打印
        for (int i = 0; i < 10; i++) {
            long id = idGenerator.generateId();
            System.out.println("Generated ID: " + id);
        }
    }
}