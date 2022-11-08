package frc.robot;

import java.io.DataOutputStream;
import java.net.Socket;
import java.time.Instant;
import java.util.concurrent.ArrayBlockingQueue;

public class Log {
    // private final String kLogHost = "10.15.59.5";
    private final String kLogHost = "localhost";
    private final int kLogPort = 4242;
    private final int kMaxQueueSize = 200;

    private Socket mSocket;
    private DataOutputStream mSocketStream;
    private long mCurrentTime;
    private ArrayBlockingQueue<String> mQueue;
    private Thread mThread;

    private static Log mInstance;

    public Log() {
        // call this from robotInit if logging is enabled
        mQueue = new ArrayBlockingQueue<String>(kMaxQueueSize);
        mThread = new Thread(this::backgroundThread);
        mThread.start();
        Init();
        mInstance = this;
    }

    public void Init() {
        try {
            if (mSocket != null) {
                mSocket.close();
            }
        } catch (Exception e) {
            System.out.println(e);
        }
        try {
            mSocket = new Socket(kLogHost, kLogPort);
            mSocketStream = new DataOutputStream(mSocket.getOutputStream());
            System.out.println("Opened socket to " + kLogHost + ":" + kLogPort);
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    private void backgroundThread() {
        while (true) {
            try {
                String data = mQueue.take();
                mSocketStream.writeBytes(data);
                System.out.print(data);
            } catch (Exception e) {
                System.out.println(e);
            }
        }
    }

    public void periodic() {
        // Call this from robotPeriodic if logging is enabled
        mCurrentTime = Instant.now().toEpochMilli();
    }

    private void QueueData(String data) {
        if (mSocketStream != null) {
            while (!mQueue.offer(data)) {
                // Drop oldest if queue is full
                mQueue.remove();
            }
        }
    }

    public void LogNumber(String key, double value, String tagKey, String tagValue) {
        String data = String.format("put %s %d %.3f %s=%s\n", key, mCurrentTime, value,
                        tagKey, tagValue);
        QueueData(data);
    }

    public void LogBoolean(String key, boolean value, String tagKey, String tagValue) {
        if (value) {
            LogNumber(key, 1, tagKey, tagValue);
        } else {
            LogNumber(key, 0, tagKey, tagValue);
        }
    }

    public static void Number(String key, double value, String tagKey,
                    String tagValue) {
        mInstance.LogNumber(key, value, tagKey, tagValue);
    }

    public static void Boolean(String key, boolean value, String tagKey,
                    String tagValue) {
        mInstance.LogBoolean(key, value, tagKey, tagValue);
    }
}
