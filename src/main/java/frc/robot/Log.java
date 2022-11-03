package frc.robot;

import java.util.HashMap;

import edu.wpi.first.util.datalog.*;
import edu.wpi.first.wpilibj.DataLogManager;
import edu.wpi.first.wpilibj.DriverStation;

public class Log {
    private HashMap<String, DataLogEntry> variables = new HashMap<String, DataLogEntry>();
    private DataLog dataLog = DataLogManager.getLog();
    private static Log instance;

    public static void InitLog() {
        // Start recording to data log
        DataLogManager.start();

        // Record both DS control and joystick data
        DriverStation.startDataLog(DataLogManager.getLog());

        instance = new Log();
    }

    public void LogDouble(String key, double value) {
        DataLogEntry entry = variables.get(key);
        if (entry == null) {
            entry = new DoubleLogEntry(dataLog, key);
            variables.put(key, entry);
        } else if (entry instanceof DoubleLogEntry) {
            DoubleLogEntry doubleEntry = DoubleLogEntry.class.cast(entry);
            doubleEntry.append(value);
        } else {
            System.out.println(key + " is already defined and is not double");
        }
    }

    public void LogInt(String key, long value) {
        DataLogEntry entry = variables.get(key);
        if (entry == null) {
            entry = new IntegerLogEntry(dataLog, key);
            variables.put(key, entry);
        } else if (entry instanceof IntegerLogEntry) {
            IntegerLogEntry intEntry = IntegerLogEntry.class.cast(entry);
            intEntry.append(value);
        } else {
            System.out.println(key + " is already defined and is not int");
        }
    }

    public void LogBoolean(String key, boolean value) {
        DataLogEntry entry = variables.get(key);
        if (entry == null) {
            entry = new BooleanLogEntry(dataLog, key);
            variables.put(key, entry);
        } else if (entry instanceof BooleanLogEntry) {
            BooleanLogEntry boolEntry = BooleanLogEntry.class.cast(entry);
            boolEntry.append(value);
        } else {
            System.out.println(key + " is already defined and is not boolean");
        }
    }

    public void LogString(String key, String value) {
        DataLogEntry entry = variables.get(key);
        if (entry == null) {
            entry = new StringLogEntry(dataLog, key);
            variables.put(key, entry);
        } else if (entry instanceof StringLogEntry) {
            StringLogEntry stringEntry = StringLogEntry.class.cast(entry);
            stringEntry.append(value);
        } else {
            System.out.println(key + " is already defined and is not string");
        }
    }

    public static void Double(String key, double value) {
        instance.LogDouble(key, value);
    }

    public static void Int(String key, long value) {
        instance.LogInt(key, value);
    }

    public static void Boolean(String key, boolean value) {
        instance.LogBoolean(key, value);
    }

    public static void String(String key, String value) {
        instance.LogString(key, value);
    }
}
