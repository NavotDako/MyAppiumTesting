package SingleTest;//package <set your test package>;

import FrameWork.CloudServer;
import FrameWork.NewAndroidDriver;
import FrameWork.NewIOSDriver;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.ios.IOSDriver;
import io.appium.java_client.remote.MobileCapabilityType;
import io.appium.java_client.remote.MobilePlatform;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.Platform;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Date;

public class SingleIOSTest {

    protected AppiumDriver driver = null;
    CloudServer cloudServer;
    private boolean GRID = true;

    @Before
    public void setUp() throws IOException {

        driver = CreateDriver(getDesiredCapabilities("1cbc20b1088c65f4a27dca70b158843f69f249a64"));
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
//            driver = new NewIOSDriver(new URL("https://192.168.2.135:443/wd/hub"), dc);
//            driver = new NewIOSDriver(new URL("https://qacloud.experitest.com:443/wd/hub"), dc);

        } else {
            driver = new NewIOSDriver(new URL("http://localhost:4723/wd/hub"), dc);
        }
        return driver;
    }

    @Test
    public void testUntitled() {
        for (int i = 0; i < 5; i++) {
            driver.get("https://www.google.co.il/search?q=" + i + "&aqs=chrome..69i57j69i60l4j69i65.1376j0j4&sourceid=chrome&ie=UTF-8");
        }
    }

    @After
    public void tearDown() {
        driver.quit();
    }
}