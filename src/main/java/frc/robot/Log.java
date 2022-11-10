package frc.robot;

import java.time.Instant;

import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableInstance;

public class Log {
    private static final String kTableName = "Data";
    private static NetworkTable table;

    public static void init() {
        NetworkTableInstance inst = NetworkTableInstance.getDefault();
        table = inst.getTable(kTableName);
    }

    public static void periodic() {
        putNumber("time", Instant.now().toEpochMilli());
    }

    public static void putNumber(String key, double value) {
        table.getEntry(key).setDouble(value);
    }

    public static void putBoolean(String key, boolean value) {
        table.getEntry(key).setBoolean(value);
    }

    public static void putString(String key, String value) {
        table.getEntry(key).setString(value);
    }
}
