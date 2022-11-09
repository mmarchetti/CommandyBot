package frc.robot;

import java.time.Instant;

import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableInstance;

public class Log {
    private static final String kTableName = "Data";
    private static NetworkTable table;

    public static void InitLog() {
        NetworkTableInstance inst = NetworkTableInstance.getDefault();
        table = inst.getTable(kTableName);
    }

    public static void periodic() {
        Int("time", Instant.now().toEpochMilli());
    }

    public static void Double(String key, double value) {
        table.getEntry(key).setDouble(value);
    }

    public static void Int(String key, long value) {
        table.getEntry(key).setNumber(value);
    }

    public static void Boolean(String key, boolean value) {
        table.getEntry(key).setBoolean(value);
    }

    public static void String(String key, String value) {
        table.getEntry(key).setString(value);
    }
}
