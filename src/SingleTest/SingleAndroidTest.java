package SingleTest;//package <set your test package>;

import FrameWork.CloudServer;
import AppiumSuite.NewAndroidDriver;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.remote.MobileCapabilityType;
import org.openqa.selenium.html5.Location;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.junit.*;

import java.io.IOException;
import java.net.URL;
import java.net.MalformedURLException;

public class SingleAndroidTest {

    protected AppiumDriver driver = null;
    CloudServer cloudServer;
    private boolean GRID = true;
    private String reportDirectory = "reports";
    private String reportFormat = "xml";
    private String testName = "Untitled";

    @Before
    public void setUp() throws IOException {

        driver = CreateDriver(getDesiredCapabilities("1115fb3c0e5f3c03"));
    }

    private DesiredCapabilities getDesiredCapabilities(String udid) throws IOException {
        DesiredCapabilities dc = new DesiredCapabilities();
        if (GRID) {
            cloudServer = new CloudServer(CloudServer.CloudServerName.MY);
            dc.setCapability("username", cloudServer.USER);
            dc.setCapability("password", cloudServer.PASS);
        }
//        dc.setCapability(MobileCapabilityType.PLATFORM_NAME, Platform.ANDROID);
        dc.setCapability("reportDirectory", reportDirectory);
        dc.setCapability("reportFormat", reportFormat);
        dc.setCapability("testName", testName);

//        dc.setCapability(MobileCapabilityType.APP, "http://192.168.2.72:8181/AndroidApps/eribank.apk");
//        dc.setCapability(AndroidMobileCapabilityType.APP_PACKAGE, "com.experitest.ExperiBank");
//        dc.setCapability(AndroidMobileCapabilityType.APP_ACTIVITY, ".LoginActivity");
        dc.setCapability(MobileCapabilityType.UDID, udid);
//        dc.setBrowserName(MobileBrowserType.CHROMIUM);

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
//            driver.executeScript("client:client.setLocation('-3.083852476163502','37.330864026562494');");
//            driver.executeScript("client:client.")
//            Location location = driver.location();
//            System.out.println(location);
            driver.setLocation(new Location(0, 0, 0));
//            location = driver.location();
//            System.out.println(location);
        } else {
            driver = new NewAndroidDriver(new URL("http://localhost:4723/wd/hub"), dc);
        }
        return driver;
    }

    @Test
    public void testUntitled() {
        System.out.println(driver.getPageSource());

    }

    @After
    public void tearDown() {
        driver.quit();
    }
}