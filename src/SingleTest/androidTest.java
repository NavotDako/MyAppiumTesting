package SingleTest;//package <set your test package>;

import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.net.URL;
import java.net.MalformedURLException;
import java.util.Date;

public class AndroidTest implements Runnable {
    private String reportDirectory = "reports";
    private String reportFormat = "xml";
    private String testName = "AndroidTest";
    protected AndroidDriver driver = null;
    String build;
    DesiredCapabilities dc = new DesiredCapabilities();
    private boolean setupFlag = false;

    public AndroidTest(String iterationIndex) {
        build = "build#" + iterationIndex;
    }

    public void run() {
        try {
            setUp();
        } catch (Exception e) {
            System.out.println("setUp Failed for - " + dc.toString());
            e.printStackTrace();
            setupFlag = true;
        }
        if (setupFlag) {
            try {
                executeTest();
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                tearDown();
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }


    public void setUp() throws MalformedURLException {

        dc.setCapability("user", "zekra");
        dc.setCapability("password", "Zekra123");
        dc.setCapability("testName", testName);
        dc.setCapability("build", build);
        driver = new AndroidDriver(new URL(getURL()), dc);
        System.out.println(SingleTestRunner.indexUp() + " - NEW Driver - " + (driver.getCapabilities().toString()));
    }


    private static String getURL() {

        if (SingleTestRunner.GRID) {
            return SingleTestRunner.cloudServer.gridURL;
        } else {
            return "http://localhost:4723/wd/hub/";
        }

    }

    private void executeTest() throws InterruptedException {
        for (int i = 0; i < SingleTestRunner.repNum; i++) {
            driver.get("https://www.google.co.il/search?q=" + i + "&aqs=chrome..69i57j69i60l4j69i65.1376j0j4&sourceid=chrome&ie=UTF-8");
            Thread.sleep(1000);
            System.out.println(String.format("%-10s%-30s%-50s%-15s%-5s", SingleTestRunner.sdf.format(new Date(System.currentTimeMillis())), driver.getCapabilities().getCapability("device.name"), driver.getCapabilities().getCapability("device.serialNumber"), "Launch number - ", (i + 1)));

        }
    }


    public void tearDown() {
        driver.quit();
    }


}