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

public class SingleIOSTest {

    protected AppiumDriver driver = null;
    CloudServer cloudServer;
    private boolean GRID = false;

    @Before
    public void setUp() throws IOException {

        driver = CreateDriver(getDesiredCapabilities("636cb7a36d429661e6be6d70e1447a66268f73ff"));
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
        String URL = null;
        if (GRID) URL = cloudServer.gridURL;
        IOSDriver driver = (IOSDriver) getDriver(dc, URL);
        return driver;
    }

    private AppiumDriver getDriver(DesiredCapabilities dc, String cloudURL) throws MalformedURLException {
        AppiumDriver driver;
        if (!GRID) {
            driver = new NewIOSDriver(new URL("http://localhost:4723/wd/hub"), dc);

        } else {
            driver = new NewIOSDriver(new URL(cloudURL), dc);
        }
        return driver;
    }

    @Test
    public void testUntitled() {
        Object result1 = driver.executeScript("client:client.launch(\"com.apple.calculator\", \"true\", \"true\")");
        WebElement el = driver.findElement(By.xpath("//*[@text='8']"));

        Object result3 = driver.executeScript("client:client.click(\"NATIVE\", \"xpath=//*[@text='8']\", \"0\", \"3\")");
        System.out.println("");
    }

    @After
    public void tearDown() {
        driver.quit();
    }
}