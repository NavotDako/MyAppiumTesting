package AppiumSuite;

import FrameWork.Runner;
import FrameWork.User;
import Tests.*;
import io.appium.java_client.remote.MobileCapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Random;

public class AppiumSuite implements Runnable {

    DesiredCapabilities dc = new DesiredCapabilities();
    String deviceID;
    public String url;
    SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");

    public AppiumSuite(String deviceID, String url) throws IOException {
        this.url = url;
        this.deviceID = deviceID;
        dc.setCapability("reportDirectory", Runner.reportFolderString);
        dc.setCapability("reportFormat", "xml");
        dc.setCapability("build", Runner.BUILD_NUM);
        dc.setCapability("deviceName", deviceID);
        dc.setCapability(MobileCapabilityType.UDID, deviceID);
        if (Runner.GRID) {
            dc.setCapability("deviceName", Runner.cloudServer.getDeviceNameByUDID(this.deviceID));
            User user = Runner.cloudServer.getRandomUser();
            System.out.println("\nDevice: " + deviceID + " & User: " + user.USERNAME+"\n");
            dc.setCapability("user", user.USERNAME);
            dc.setCapability("password", user.PASSWORD);
        }
    }

    public void run() {
//        System.out.println("Starting Suite For - " + deviceID);
//        new Reboot(deviceID, new DesiredCapabilities(this.dc), url,1);

        for (int i = 0; i < Runner.REP_NUM; i++) {
            if (Runner.ERIBANK) new EriBankAppiumTest(deviceID, new DesiredCapabilities(this.dc), url, i + 1);
            if (Runner.WEBTEST) new WebAppiumTest(deviceID, new DesiredCapabilities(this.dc), url, i + 1);
            if (Runner.WIKI) new wiki(deviceID, new DesiredCapabilities(this.dc), url, i + 1);
            if (Runner.REBOOT) new Reboot(deviceID, new DesiredCapabilities(this.dc), url, i + 1);
//            if (Runner.SLEEP) sleep();
//            new NonInstrumented(deviceID, new DesiredCapabilities(this.dc),url,i+1);
//            if (Runner.SLEEP) sleep();
        }
    }

    private void sleep() {
        try {
            int timeToSleep = ((new Random()).nextInt(10000)) + 20000;
            System.out.println("device - " + deviceID + " is going to sleep for - " + timeToSleep + " milliseconds");
            Thread.sleep(timeToSleep);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
