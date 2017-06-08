package FrameWork;

import com.sun.javafx.binding.StringFormatter;
import org.openqa.selenium.Capabilities;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

/**
 * Created by navot.dako on 6/5/2017.
 */
public class utils {
    public static void writeToDeviceLog(String deviceID, String stringToWrite) {
        PrintWriter writer = null;
        try {
            writer = new PrintWriter(new BufferedWriter(new FileWriter("reports\\" + deviceID + ".txt", true)));
            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
            writer.write(String.format("%-10s %-100s\n", sdf.format(new Date(System.currentTimeMillis())), stringToWrite));
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void DeleteRecursive(File fileOrDirectory) {

        if (fileOrDirectory.isDirectory())
            for (File child : fileOrDirectory.listFiles())
                DeleteRecursive(child);

        fileOrDirectory.delete();

    }

    public static void writeToOverall(boolean status, String deviceID, String testName, Exception e) {
        PrintWriter writer = null;

        try {
            writer = new PrintWriter(new BufferedWriter(new FileWriter("reports\\overallReport.txt", true)));
            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
            if (status) {
                writer.write(String.format("%-10s%-10s%-40s%-20s\n", sdf.format(new Date(System.currentTimeMillis())), "PASS", deviceID, testName));
            } else {
                writer.write(String.format("%-10s%-10s%-40s%-20s\n", sdf.format(new Date(System.currentTimeMillis())), "FAIL", deviceID, testName));
                e.printStackTrace(writer);
            }
            writer.close();
        } catch (IOException ex) {
            e.printStackTrace();
        }
    }
}