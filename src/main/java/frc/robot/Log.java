package frc.robot;

import java.util.HashMap;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.net.Socket;
import java.time.Instant;

import edu.wpi.first.util.datalog.*;
import edu.wpi.first.wpilibj.DataLogManager;
import edu.wpi.first.wpilibj.DriverStation;

public class Log {
    // private final String kLogHost = "10.15.59.5";
    private final String kLogHost = "localhost";
    private final int kLogPort = 1234;
    private final int kBufferSize = 1000;

    private HashMap<String, DataLogEntry> mVariables = new HashMap<String, DataLogEntry>();
    private DataLog mDataLog;
    private Socket mSocket;
    private DataOutputStream mSocketStream;
    private Thread mSenderThread;

    private static Log mInstance;

    public static void InitLog() {
        // Start recording to data log
        DataLogManager.start();

        // Record both DS control and joystick data
        DriverStation.startDataLog(DataLogManager.getLog());

        mInstance = new Log();
    }

    public Log() {
        mDataLog = DataLogManager.getLog();

        try {
            mSocket = new Socket(kLogHost, kLogPort);
            mSocketStream = new DataOutputStream(mSocket.getOutputStream());
            System.out.println("Opened socket to " + kLogHost + ":" + kLogPort);
        } catch (Exception e) {
            System.out.println(e);
        }
        mSenderThread = new Thread(this::run);
        mSenderThread.start();
    }

    public void run() {
        String filename = "log-" + Instant.now().toEpochMilli();
        mDataLog.setFilename(filename);
        File logFile = new File(filename);
        FileInputStream logStream;

        try {
            logStream = new FileInputStream(logFile);
        } catch (Exception e) {
            System.out.println(e);
            return;
        }
        byte[] buffer = new byte[kBufferSize];

        while (true) {
            try {
                int numBytes = logStream.read(buffer);
                mSocketStream.write(buffer, 0, numBytes);
                System.out.printf("sent %d bytes\n", numBytes);
            } catch (Exception e) {
                System.out.println(e);
            }
        }
    }

    public void LogDouble(String key, double value) {
        DataLogEntry entry = mVariables.get(key);
        if (entry == null) {
            entry = new DoubleLogEntry(mDataLog, key);
            mVariables.put(key, entry);
        } else if (entry instanceof DoubleLogEntry) {
            DoubleLogEntry doubleEntry = DoubleLogEntry.class.cast(entry);
            doubleEntry.append(value);
        } else {
            System.out.println(key + " is already defined and is not double");
        }
    }

    public void LogInt(String key, long value) {
        DataLogEntry entry = mVariables.get(key);
        if (entry == null) {
            entry = new IntegerLogEntry(mDataLog, key);
            mVariables.put(key, entry);
        } else if (entry instanceof IntegerLogEntry) {
            IntegerLogEntry intEntry = IntegerLogEntry.class.cast(entry);
            intEntry.append(value);
        } else {
            System.out.println(key + " is already defined and is not int");
        }
    }

    public void LogBoolean(String key, boolean value) {
        DataLogEntry entry = mVariables.get(key);
        if (entry == null) {
            entry = new BooleanLogEntry(mDataLog, key);
            mVariables.put(key, entry);
        } else if (entry instanceof BooleanLogEntry) {
            BooleanLogEntry boolEntry = BooleanLogEntry.class.cast(entry);
            boolEntry.append(value);
        } else {
            System.out.println(key + " is already defined and is not boolean");
        }
    }

    public void LogString(String key, String value) {
        DataLogEntry entry = mVariables.get(key);
        if (entry == null) {
            entry = new StringLogEntry(mDataLog, key);
            mVariables.put(key, entry);
        } else if (entry instanceof StringLogEntry) {
            StringLogEntry stringEntry = StringLogEntry.class.cast(entry);
            stringEntry.append(value);
        } else {
            System.out.println(key + " is already defined and is not string");
        }
    }

    public static void Double(String key, double value) {
        mInstance.LogDouble(key, value);
    }

    public static void Int(String key, long value) {
        mInstance.LogInt(key, value);
    }

    public static void Boolean(String key, boolean value) {
        mInstance.LogBoolean(key, value);
    }

    public static void String(String key, String value) {
        mInstance.LogString(key, value);
    }
}
