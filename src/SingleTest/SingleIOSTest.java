package SingleTest;//package <set your test package>;

import FrameWork.CloudServer;
import AppiumSuite.NewIOSDriver;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.ios.IOSDriver;
import io.appium.java_client.remote.MobileCapabilityType;
import io.appium.java_client.remote.MobilePlatform;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

public class SingleIOSTest {

    protected AppiumDriver driver = null;
    CloudServer cloudServer;
    private boolean GRID = true;

    @Before
    public void setUp() throws IOException {
        driver = CreateDriver(getDesiredCapabilities("30ea1939099b89ebf97cbd3422770af6dc5ee295"));
    }

    private DesiredCapabilities getDesiredCapabilities(String udid) throws IOException {
        DesiredCapabilities dc = new DesiredCapabilities();
        if (GRID) {
            cloudServer = new CloudServer(CloudServer.CloudServerName.MY);
            dc.setCapability("user", cloudServer.USER);
            dc.setCapability("password", cloudServer.PASS);
        }
        dc.setCapability(MobileCapabilityType.PLATFORM_NAME, MobilePlatform.IOS);
        dc.setCapability(MobileCapabilityType.UDID, udid);
//        dc.setCapability(MobileCapabilityType.APP, "http://192.168.2.72:8181/iOSApps/Eri22222Bank.ipa");
//        dc.setCapability(IOSMobileCapabilityType.BUNDLE_ID, "com.experitest.ExperiBank");
        return dc;
    }

    private IOSDriver CreateDriver(DesiredCapabilities dc) throws MalformedURLException {
        IOSDriver driver = (IOSDriver) getDriver(dc);
        return driver;
    }

    private AppiumDriver getDriver(DesiredCapabilities dc) throws MalformedURLException {
        AppiumDriver driver;
        if (GRID) {
            driver = new NewIOSDriver(new URL(cloudServer.gridURL), dc);
//            driver.executeScript("client:client.setLocation('-3.083852476163502','37.330864026562494');");
            // driver.setLocation(new Location(0, 0, 0));

        } else {
            driver = new NewIOSDriver(new URL("http://localhost:4723/wd/hub"), dc);
        }
        return driver;
    }

    @Test
    public void testUntitled() {
        driver.get("http://");
        driver.get("http://www.bbc.com");
        String str = (String) driver.executeScript("experitest:client.elementGetText(\"WEB\", \"//*[@id='gh-mlogo']\", \"0\")");
        String b = driver.findElement(By.xpath("//*[@id='gh-mlogo']")).getText();
        System.out.println(str + "                - " + b);
    }

    @After
    public void tearDown() {
        driver.quit();
    }
}