package Tests;

import io.appium.java_client.remote.MobileCapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;


public class Reboot extends BaseAppiumTest {

    public Reboot(String deviceID, DesiredCapabilities generalDC, String url, int iteration) {
        super("Reboot", deviceID, url, iteration);
        DesiredCapabilities dc = createCapabilities(generalDC);
        if (init(dc)) {
            executeTest();
        }
    }


    public DesiredCapabilities createCapabilities(DesiredCapabilities dc) {
        DesiredCapabilities tempDC = dc;

        tempDC.setCapability(MobileCapabilityType.NO_RESET, false);

        return tempDC;
    }

    @Override
    protected void androidTest() throws Exception {
        driver.executeScript("client:client.reboot(200000)");
    }

    @Override
    protected void iosTest() throws Exception {
        driver.executeScript("client:client.reboot(200000)");
    }


}
