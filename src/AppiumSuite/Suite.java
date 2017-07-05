package AppiumSuite;

import Tests.EriBankTest;
import Tests.NonInstrumented;
import Tests.Reboot;
import Tests.WebTest;
import io.appium.java_client.remote.MobileCapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.io.IOException;
import java.text.SimpleDateFormat;

public class Suite implements Runnable {

    DesiredCapabilities dc = new DesiredCapabilities();
    String deviceID;
    public String url;
    SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");

    public Suite(String deviceID, String url) throws IOException {
        this.url = url;
        this.deviceID = deviceID;
        dc.setCapability("reportDirectory", Runner.reportFolderString);
        dc.setCapability("reportFormat", "xml");
        dc.setCapability("build", Runner.BUILD_NUM);
        dc.setCapability("deviceName", deviceID);
        dc.setCapability(MobileCapabilityType.UDID, deviceID);
        if (Runner.GRID) {
            dc.setCapability("deviceName", Runner.cloudServer.getDeviceNameByUDID(this.deviceID));
            dc.setCapability("user", Runner.cloudServer.USER);
            dc.setCapability("password", Runner.cloudServer.PASS);
        }
    }

    public void run() {
        System.out.println("Starting Suite For - " + deviceID);
        //new Reboot(deviceID, new DesiredCapabilities(this.dc), url);
        for (int i = 0; i < Runner.REP_NUM; i++) {
            new EriBankTest(deviceID, new DesiredCapabilities(this.dc), url,i+1);
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            //   new WebTest(deviceID, new DesiredCapabilities(this.dc), url,i+1);

//            new NonInstrumented(deviceID, new DesiredCapabilities(this.dc),url);
        }
    }
}
