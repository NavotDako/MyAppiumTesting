package SingleTest;//package <set your test package>;
import AppiumSuite.NewIOSDriver;
import io.appium.java_client.ios.IOSDriver;
import io.appium.java_client.ios.IOSElement;
import io.appium.java_client.remote.MobileCapabilityType;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.junit.*;
import java.net.URL;
import java.net.MalformedURLException;

public class simple {
    private String reportDirectory = "reports";
    private String reportFormat = "xml";
    private String testName = "Untitled";
    protected IOSDriver<IOSElement> driver = null;

    DesiredCapabilities dc = new DesiredCapabilities();

    @Before
    public void setUp() throws MalformedURLException {
        dc.setCapability("reportDirectory", "c:/temp/1");
        dc.setCapability("reportFormat", reportFormat);
        dc.setCapability("testName", testName);
        dc.setCapability("user", "admin");
        dc.setCapability("password", "Experitest2012");
        dc.setCapability(MobileCapabilityType.UDID, "c16d59063d1a2ec89695667be3e1518d99ae8eb7");
        driver = new NewIOSDriver(new URL("http://192.168.2.13:80/wd/hub/"), dc);
    }

    @Test
    public void testUntitled() {
        driver.get("http://google.com");
    }

    @After
    public void tearDown() {
        Capabilities sss = driver.getCapabilities();
        System.out.println(sss.toString());
        driver.quit();
    }
}