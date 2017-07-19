package SingleTest;//package <set your test package>;

import FrameWork.CloudServer;
import FrameWork.NewAndroidDriver;
import FrameWork.NewIOSDriver;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.remote.AndroidMobileCapabilityType;
import io.appium.java_client.remote.IOSMobileCapabilityType;
import io.appium.java_client.ios.IOSDriver;
import io.appium.java_client.remote.MobileCapabilityType;
import org.openqa.selenium.*;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.junit.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.IOException;
import java.net.URL;
import java.net.MalformedURLException;
import java.util.concurrent.TimeUnit;

public class SingleAndroidTest {

    protected AppiumDriver driver = null;
    CloudServer cloudServer;
    private boolean GRID = false;

    @Before
    public void setUp() throws IOException {

        driver = CreateDriver(getDesiredCapabilities("ce051605686c683b03"));
    }

    private DesiredCapabilities getDesiredCapabilities(String udid) throws IOException {
        DesiredCapabilities dc = new DesiredCapabilities();
        if (GRID) {
            cloudServer = new CloudServer(CloudServer.CloudServerName.MY);
            dc.setCapability("user", cloudServer.USER);
            dc.setCapability("password", cloudServer.PASS);
        }
        dc.setCapability(MobileCapabilityType.PLATFORM_NAME, Platform.ANDROID);
        dc.setCapability(MobileCapabilityType.UDID, udid);

        return dc;
    }

    private AndroidDriver CreateDriver(DesiredCapabilities dc) throws MalformedURLException {
        AndroidDriver driver = getAndroidDriver(dc);
        return driver;
    }

    private AndroidDriver getAndroidDriver(DesiredCapabilities dc) throws MalformedURLException {
        AndroidDriver driver;
        if (GRID) {
            driver = new NewAndroidDriver(new URL(cloudServer.gridURL), dc);
        } else {
            driver = new NewAndroidDriver(new URL("http://localhost:4723/wd/hub"), dc);
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