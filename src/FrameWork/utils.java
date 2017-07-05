package FrameWork;

import AppiumSuite.Runner;
import io.appium.java_client.AppiumDriver;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.WebDriver;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import static Misc.ExceptionExtractor.ExtractExceptions;

/**
 * Created by navot.dako on 6/5/2017.
 */
public class Utils {
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

    public static synchronized void writeToOverall(boolean status, String deviceID, String testName, Exception e, long time, String reportUrl, int iteration) {
        PrintWriter writer = null;

        try {
            writer = new PrintWriter(new BufferedWriter(new FileWriter("reports\\overallReport.txt", true)));
            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
            if (status) {
                writer.write(String.format("%-5s%-10s%-50s%-15s%-20s%-20s\n",Runner.index+".", sdf.format(new Date(System.currentTimeMillis())), deviceID, "PASS - "+iteration, testName, (time / 1000) + "s"));
            } else {
                writer.write(String.format("%-5s%-10s%-50s%-15s%-20s%-20s%-100s\n",Runner.index+".", sdf.format(new Date(System.currentTimeMillis())), deviceID, "FAIL - "+iteration, testName, (time / 1000) + "s",reportUrl));
              //  if (Runner.GRID) writer.write(String.format("%-30s%-40s%-20s%-100s\n\n","Report URL - ",deviceID, testName, reportUrl));

                if (Runner.PRINT_ERROR) {
                    e.printStackTrace(writer);
                    writer.write("---------------------------------------------------------------------------------------------------------------------\n");
                }
            }
            Runner.index++;
            writer.close();
        } catch (IOException ex) {
            e.printStackTrace();
        }
    }

    public static void getAndWriteExceptions(String generatedReportFolder) {
        ArrayList<String> exceptionArray = null;
        exceptionArray = tryToCheckTheLogForExceptions(generatedReportFolder);
        boolean flag = false;
        try {
            if (exceptionArray.size() > 0) {
                for (int j = 0; j < exceptionArray.size(); j++) {
                    if (j > 5) break;
                    if (!exceptionArray.get(j).contains("start ui automationCould")) {
                        if (!exceptionArray.get(j).contains("illegal node name")) {
                            if (!exceptionArray.get(j).contains("Failed to scroll the element into view")) {
                                String exceptionFinalString;
                                try {
                                    exceptionFinalString = exceptionArray.get(j).substring(0, exceptionArray.get(j).indexOf(" at ", 500));
                                } catch (Exception e) {
                                    exceptionFinalString = exceptionArray.get(j);
                                }
                                // writeToSummaryReport("\t" + deviceName + " -\n" + "\t" + exceptionFinalString);
                                flag = true;
                            }
                        }
                    }
                }
                if (flag) {
                }
                //       writeToSummaryReport(Thread.currentThread().getName() + "  " + deviceName + " - " + "REPORT - " + generatedReportFolder + " - file:///" + generatedReportFolder.replace('\\', '/') + "/index.html\n");

            }
        } catch (Exception e) {

        }
    }

    public static ArrayList<String> tryToCheckTheLogForExceptions(String generatedReportFolder) {
        ArrayList<String> exceptionArray = null;

        try {
            exceptionArray = ExtractExceptions(generatedReportFolder);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return exceptionArray;
    }
    public static void screenshot(AppiumDriver driver, String path) throws IOException {
        File srcFile = driver.getScreenshotAs(OutputType.FILE);
        String filename = driver.getCapabilities().getCapability("device.name") + "#" + System.currentTimeMillis();
        File targetFile = new File(path + "\\" + filename + ".jpg");
        FileUtils.copyFile(srcFile, targetFile);
    }
}
