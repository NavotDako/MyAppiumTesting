package Tests;

import io.appium.java_client.remote.AndroidMobileCapabilityType;
import io.appium.java_client.remote.IOSMobileCapabilityType;
import io.appium.java_client.remote.MobileCapabilityType;
import org.openqa.selenium.By;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.ScreenOrientation;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.concurrent.TimeUnit;

public class EriBankAppiumTest extends BaseAppiumTest {

    public EriBankAppiumTest(String deviceID, DesiredCapabilities generalDC, String url, int iteration) {
        super("EriBank", deviceID, url, iteration);
        DesiredCapabilities dc = createCapabilities(generalDC);
        if (init(dc)) {
            executeTest();
        }
    }


    public DesiredCapabilities createCapabilities(DesiredCapabilities dc) {
        if (deviceOS.contains("ios")) {
            dc.setCapability(MobileCapabilityType.APP, "http://192.168.2.72:8181/iOSApps/EriBank.ipa");
            dc.setCapability(IOSMobileCapabilityType.BUNDLE_ID, "com.experitest.ExperiBank");
            dc.setCapability("instrumentApp", true);
        } else {
            dc.setCapability(MobileCapabilityType.APP, "http://192.168.2.72:8181/AndroidApps/eribank.apk");
            dc.setCapability(AndroidMobileCapabilityType.APP_PACKAGE, "com.experitest.ExperiBank");
            dc.setCapability(AndroidMobileCapabilityType.APP_ACTIVITY, ".LoginActivity");
        }
        DesiredCapabilities tempDC = dc;
        tempDC.setCapability(MobileCapabilityType.NO_RESET, false);


        return tempDC;
    }

    @Override
    protected void androidTest() throws Exception {
        Capabilities aaa = driver.getCapabilities();
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
        driver.findElementByXPath("//*[@text='Username']").sendKeys("company");

        WebElement passwordField = driver.findElement(By.xpath("//*[@resource-id='com.experitest.ExperiBank:id/passwordTextField']"));
        passwordField.sendKeys("company");

        WebElement loginElement = driver.findElement(By.xpath("//*[@resource-id='com.experitest.ExperiBank:id/loginButton']"));
        loginElement.click();
        Thread.sleep(500);
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

    @Override
    protected void iosTest() throws Exception {
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
        driver.findElement(By.xpath("//*[@placeholder='Username']")).sendKeys("company");
        driver.findElement(By.xpath("//*[@placeholder='Password']")).sendKeys("company");
        driver.findElement(By.xpath("//*[@accessibilityLabel='loginButton']")).click();
        driver.findElement(By.xpath("//*[@accessibilityLabel='makePaymentButton']")).click();
        driver.findElement(By.xpath("//*[@placeholder='Phone']")).sendKeys("097856765");
        driver.findElement(By.xpath("//*[@placeholder='Name']")).sendKeys("Eyal");
        driver.findElement(By.xpath("//*[@placeholder='Amount']")).sendKeys("-100");
        driver.findElement(By.xpath("//*[@accessibilityLabel='countryButton']")).click();
        driver.findElement(By.xpath("//*[@accessibilityLabel='New Zealand']")).click();
        driver.findElement(By.xpath("//*[@accessibilityLabel='sendPaymentButton']")).click();
        driver.findElement(By.xpath("//*[@text='Yes']")).click();
        driver.findElement(By.xpath("//*[@accessibilityLabel='Expense Report']")).click();
        driver.findElement(By.xpath("//*[@text='Detail goes here' and ./preceding-sibling::*[@text='Expense 1']]")).click();
        driver.findElement(By.xpath("//*[@text='Detail goes here' and ./preceding-sibling::*[@text='Expense 0']]")).click();
        driver.findElement(By.xpath("//*[@text='Add']")).click();
        driver.findElement(By.xpath("//*[@text='Add']")).click();
        new WebDriverWait(driver, 10).until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@text='Back']")));
        driver.findElement(By.xpath("//*[@text='Back']")).click();
        driver.findElement(By.xpath("//*[@accessibilityLabel='logoutButton']")).click();
        driver.findElement(By.xpath("//*[@placeholder='Username']")).click();
        driver.findElement(By.xpath("//*[@class='UIButton']")).click();
        driver.findElement(By.xpath("//*[@placeholder='Password']")).click();
        driver.findElement(By.xpath("//*[@class='UIButton']")).click();
    }


}
