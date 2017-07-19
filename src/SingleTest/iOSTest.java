package SingleTest;//package <set your test package>;

import io.appium.java_client.ios.IOSDriver;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Date;

public class iOSTest implements Runnable {
    private String reportDirectory = "reports";
    private String reportFormat = "xml";
    private String testName = "AndroidTest";
    protected IOSDriver driver = null;
    String build;

    public iOSTest(String iterationIndex) {
        build = "build#" + iterationIndex;
    }


    @Override
    public void run() {
        try {
            setUp();
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

    private void executeTest() throws InterruptedException {
        for (int i = 0; i < SingleTestRunner.repNum; i++) {
            driver.get("https://www.google.co.il/search?q=" + i + "&aqs=chrome..69i57j69i60l4j69i65.1376j0j4&sourceid=chrome&ie=UTF-8");
            Thread.sleep(1000);
            System.out.println(String.format("%-10s%-30s%-50s%-15s%-5s", SingleTestRunner.sdf.format(new Date(System.currentTimeMillis())), driver.getCapabilities().getCapability("device.name"), driver.getCapabilities().getCapability("device.serialNumber"), "Launch number - ", (i + 1)));
        }
    }

    public void setUp() throws MalformedURLException {
        DesiredCapabilities dc = new DesiredCapabilities();
        dc.setCapability("user", "waseem.suleiman");
        dc.setCapability("password", "Experitest2013");
        dc.setCapability("testName", testName);
        dc.setCapability("build", build);
        try {
            driver = new IOSDriver(new URL(getURL()), dc);
            System.out.println(SingleTestRunner.indexUp() + " - NEW Driver - " + (driver.getCapabilities().toString()));

        } catch (Exception e) {

        }
    }

    public void tearDown() {
        driver.quit();
    }

    private static String getURL() {

        if (SingleTestRunner.GRID) {
            return "https://cloud.experitest.com";//SingleTestRunner.cloudServer.gridURL;
        } else {
            return "http://localhost:4723/wd/hub/";
        }

    }


}