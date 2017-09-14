package SingleTest;//package <set your test package>;
import FrameWork.NewAndroidDriver;
import io.appium.java_client.remote.AndroidMobileCapabilityType;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.AndroidElement;
import io.appium.java_client.remote.MobileCapabilityType;
import org.openqa.selenium.ScreenOrientation;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.By;
import org.junit.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.net.URL;
import java.net.MalformedURLException;
import java.util.concurrent.TimeUnit;

public class Untitled {
    private String reportDirectory = "reports";
    private String reportFormat = "xml";
    private String testName = "Untitled";
    protected NewAndroidDriver driver = null;

    DesiredCapabilities dc = new DesiredCapabilities();

    @Before
    public void setUp() throws MalformedURLException {
        dc.setCapability("reportDirectory", reportDirectory);
        dc.setCapability("reportFormat", reportFormat);
        dc.setCapability("testName", testName);
        dc.setCapability(MobileCapabilityType.UDID, "daeb0cf1");
        dc.setCapability(MobileCapabilityType.APP, "http://192.168.2.72:8181/AndroidApps/eribank.apk");
        dc.setCapability(AndroidMobileCapabilityType.APP_PACKAGE, "com.experitest.ExperiBank");
        dc.setCapability(AndroidMobileCapabilityType.APP_ACTIVITY, ".LoginActivity");
        dc.setCapability("instrumentApp", true);

        driver = new NewAndroidDriver(new URL("http://localhost:4724/wd/hub"), dc);
    }

    @Test
    public void testUntitled() {
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
        driver.findElementByXPath("//*[@text='Username']").sendKeys("company");

        WebElement passwordField = driver.findElement(By.xpath("//*[@resource-id='com.experitest.ExperiBank:id/passwordTextField']"));
        passwordField.sendKeys("company");

        WebElement loginElement = driver.findElement(By.xpath("//*[@resource-id='com.experitest.ExperiBank:id/loginButton']"));
        loginElement.click();
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        new WebDriverWait(driver, 10).until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@resource-id='com.experitest.ExperiBank:id/makePaymentButton']")));
        driver.findElement(By.id("com.experitest.ExperiBank:id/makePaymentButton")).click();

        new WebDriverWait(driver, 10).until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@resource-id='com.experitest.ExperiBank:id/phoneTextField']")));
        driver.findElement(By.xpath("//*[@resource-id='com.experitest.ExperiBank:id/phoneTextField']")).sendKeys("55151");

        driver.rotate(ScreenOrientation.LANDSCAPE);
        driver.findElement(By.xpath("//*[@text='Name']")).sendKeys("app manager");
        driver.hideKeyboard();
        driver.findElement(By.xpath("//*[@text='Amount']")).sendKeys("100");
        driver.hideKeyboard();
        WebElement selectCountry = driver.findElement(By.xpath("//*[@resource-id='com.experitest.ExperiBank:id/countryButton']"));
        selectCountry.click();
        driver.findElement(By.xpath("//*[@text='New Zealand']")).click();


        boolean flag = false;
        long loopStartTime = System.currentTimeMillis();
        while (!flag) {
            try {
                driver.findElement(By.xpath("xpath=//*[@text='Send Payment' and @onScreen='true']"));
                flag = true;
            } catch (Exception e) {
                driver.swipe(500, 600, 500, 200, 1000);
                if (System.currentTimeMillis() > (loopStartTime + 30000)) {
                    flag = true;
                }

            }
        }
        driver.findElement(By.xpath("//*[@text='Send Payment' and @onScreen='true']")).click();
        new WebDriverWait(driver, 10).until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@text='Yes']")));
        driver.findElement(By.xpath("//*[@text='Yes']")).click();
        driver.rotate(ScreenOrientation.PORTRAIT);
        driver.findElement(By.xpath("//*[@resource-id='com.experitest.ExperiBank:id/logoutButton']")).click();
    }

    @After
    public void tearDown() {
        driver.quit();
    }
}