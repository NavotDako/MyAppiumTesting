package Tests;

import AppiumSuite.Runner;
import FrameWork.NewAndroidDriver;
import FrameWork.NewIOSDriver;
import FrameWork.Utils;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.logging.LogEntry;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static Misc.ExceptionExtractor.ExtractExceptions;

public abstract class BaseTest {
    private final int iteration;
    private String deviceName;
    String deviceID;
    String deviceOS;
    AppiumDriver driver;
    String testName;
    String url;
    private long startTime;

    BaseTest(String testName, String deviceID, String url, int i) {
        this.testName = testName;
        this.deviceID = deviceID;
        this.url = url;
        iteration = i;
        try {
            this.deviceOS = Runner.cloudServer.getDeviceOSByUDID(deviceID);
            this.deviceName = Runner.cloudServer.getDeviceNameByUDID(this.deviceID);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public abstract DesiredCapabilities createCapabilities(DesiredCapabilities dc);

    public boolean init(DesiredCapabilities dc) {
        startTime = System.currentTimeMillis();
        try {
            CreateDriver(dc);
            return true;
        } catch (Exception e) {
            reportFailure(e);
            finish();
            e.printStackTrace();
            return false;
        }
    }

    protected void CreateDriver(DesiredCapabilities dc) throws MalformedURLException {
        dc.setCapability("testName", testName + "_" + deviceName);
        dc.setCapability("startTime", String.valueOf(System.currentTimeMillis()));

        if (deviceOS.contains("ios")) {
            driver = new NewIOSDriver(new URL(url), dc);
            System.out.println("A Good iOS Driver Was Created For - " + deviceID);

        } else {
            driver = new NewAndroidDriver(new URL(url), dc);
            System.out.println("A Good Android Driver Was Created For - " + deviceID);
            if (((AndroidDriver) driver).isLocked())
                ((AndroidDriver) driver).unlockDevice();

        }
    }

    public void executeTest() {
        System.out.println("--------------------------------------------------------------------------");
        System.out.println("Starting test - " + testName + " For Device - " + deviceID);
        System.out.println("--------------------------------------------------------------------------");

        try {
            if (deviceOS.contains("ios")) {
                iosTest();
            } else {
                androidTest();
            }
            reportSuccess();
        } catch (Exception e) {
            reportFailure(e);
        }
        finish();
    }

    private void reportFailure(Exception e) {

        long time = 0;
        String report = "";
        try {
            report = (String) driver.getCapabilities().getCapability("reportUrl");
        } catch (Exception e2) {
            System.out.println("Can't get reportURL for test - " + testName + " - device - " + deviceID);
            try{
                report = e.getMessage().substring( e.getMessage().indexOf("reportUrl"),e.getMessage().indexOf("))"));
            }catch (Exception ex){
                e.printStackTrace();
                report = e.getMessage();
            }
        }
        try {
            time = System.currentTimeMillis() - startTime;
        } catch (Exception e1) {
            System.out.println("Can't Get startTime");
        }
        String deviceString = deviceID;
        try {
            deviceString = deviceName.replace(" ", "_").trim();
        } catch (Exception ex) {
            System.out.println("Can't get name for - " + deviceID);
        }
        Utils.writeToOverall(false, deviceString, testName, e, time, report, iteration);
        System.out.println("--------------------------------------------------------------------------");
        System.out.println("THE TEST HAD FAILED *** - " + testName + " For Device - " + deviceName + "_" + deviceID);
        System.out.println("--------------------------------------------------------------------------");
        e.printStackTrace();

    }

    private void reportSuccess() {
        long time = System.currentTimeMillis() - Long.parseLong((String) driver.getCapabilities().getCapability("startTime"));
        String reportURL;
        try {
            reportURL = (String) driver.getCapabilities().getCapability("reportUrl");
        } catch (Exception e2) {
            reportURL = "NOT reportURL in the driver capabilities";
        }
        Utils.writeToOverall(true, deviceName.replace(" ", "_").trim(), testName, null, time, reportURL, iteration);
        System.out.println("--------------------------------------------------------------------------");
        System.out.println("THE TEST HAD PASSED - " + testName + " For Device - " + deviceID);
        System.out.println("--------------------------------------------------------------------------");
    }

    private void finish() {
        try {
            driver.quit();
        } catch (Exception e) {
            System.out.println("Failed to quit()!!!! - " + deviceID);
            e.printStackTrace();
        }
        if (Runner.GRID && Runner.SCAN_LOG) {
            Utils.getAndWriteExceptions("");
        }
        System.out.println("--------------------------------------------------------------------------");
        System.out.println("Ending test - " + testName + " For Device - " + deviceID);
        System.out.println("--------------------------------------------------------------------------");
    }

    protected abstract void androidTest() throws Exception;

    protected abstract void iosTest() throws Exception;

}
