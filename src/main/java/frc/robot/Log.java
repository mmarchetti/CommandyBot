package frc.robot;

import java.time.Instant;

import com.influxdb.client.InfluxDBClient;
import com.influxdb.client.InfluxDBClientFactory;
import com.influxdb.client.WriteApi;
import com.influxdb.client.domain.WritePrecision;
import com.influxdb.client.write.Point;
import com.influxdb.client.write.events.*;

public class Log {
    private static String serverURL = "http://localhost:8086";
    private static String token = "6qzIGRwnMC-dn3xaG1QTF3d_oZTv4pC3ozPc_CnhA9cd3FRPVQs2NmHzCV-533mY1GecretwdwFmtEUsLvu5cg==";
    private static char[] tokenChars = token.toCharArray();
    private static String db = "team1559";
    private static String bucket = "candybot";

    private InfluxDBClient client;
    private WriteApi clientAPI;
    private long currentTime;

    private static Log mInstance;

    public Log() {
        // call this from robotInit if logging is enabled
        mInstance = this;

        client = InfluxDBClientFactory.create(serverURL, tokenChars, db, bucket);
        clientAPI = client.makeWriteApi();
        clientAPI.listenEvents(BackpressureEvent.class, value -> {
            System.out.println("Received InfluxDB backpressure event");
        });
        clientAPI.listenEvents(WriteErrorEvent.class, event -> {
            Throwable exception = event.getThrowable();
            System.out.println("Received InfluxDB error: " + exception);
        });
        // clientAPI.listenEvents(WriteSuccessEvent.class, event -> {
        // String data = event.getLineProtocol();
        // System.out.println("Logged data: " + data);
        // });
    }

    public void periodic() {
        // Call this from robotPeriodic if logging is enabled
        currentTime = Instant.now().toEpochMilli();
    }

    public void LogNumber(String key, double value, String tagKey, String tagValue) {
        // @formatter:off
        Point point = Point
            .measurement(key)
            .addField("value", value)
            .time(currentTime, WritePrecision.MS);
        // @formatter:on

        if (tagKey != "") {
            point.addTag(tagKey, tagValue);
        }
        clientAPI.writePoint(point);
    }

    public void LogNumber(String key, double value) {
        LogNumber(key, value, "", "");
    }

    public void LogBoolean(String key, boolean value, String tagKey, String tagValue) {
        if (value) {
            LogNumber(key, 1, tagKey, tagValue);
        } else {
            LogNumber(key, 0, tagKey, tagValue);
        }
    }

    public void LogBoolean(String key, boolean value) {
        LogBoolean(key, value, "", "");
    }

    public static void Number(String key, double value, String tagKey,
                    String tagValue) {
        mInstance.LogNumber(key, value, tagKey, tagValue);
    }

    public static void Number(String key, double value) {
        mInstance.LogNumber(key, value);
    }

    public static void Boolean(String key, boolean value, String tagKey,
                    String tagValue) {
        mInstance.LogBoolean(key, value, tagKey, tagValue);
    }

    public static void Boolean(String key, boolean value) {
        mInstance.LogBoolean(key, value);
    }
}
