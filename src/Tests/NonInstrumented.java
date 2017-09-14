package Tests;

import io.appium.java_client.remote.AndroidMobileCapabilityType;
import io.appium.java_client.android.AndroidElement;
import io.appium.java_client.remote.IOSMobileCapabilityType;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created by khaled.abbas on 5/23/2017.
 */
public class NonInstrumented extends BaseTest {


    public NonInstrumented(String deviceID, DesiredCapabilities generalDC, String url,int iteration) {

        super("NonInstrumented", deviceID, url, iteration);
        DesiredCapabilities dc = createCapabilities(generalDC);
        try {
            CreateDriver(dc);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        executeTest();

    }

    public DesiredCapabilities createCapabilities(DesiredCapabilities dc) {
        DesiredCapabilities tempDC = dc;
        if (deviceOS.contains("ios")) {
            tempDC.setCapability(IOSMobileCapabilityType.APP_NAME, "noApp");
        } else {

            tempDC.setCapability(AndroidMobileCapabilityType.APP_PACKAGE, "com.android.vending");
            tempDC.setCapability(AndroidMobileCapabilityType.APP_ACTIVITY, ".AssetBrowserActivity");
            tempDC.setCapability("noReset", false);

        }

        return tempDC;
    }

    @Override
    protected void androidTest() throws Exception {
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);

//        try {
//            driver.findElement(By.xpath("//*[@text='Retry']")).click();
//            driver.findElement(By.xpath("//*[@text='Retry']")).click();
//        } catch (Exception e) {
//            System.out.println("No Retry at start");
//        }

//        try {
//            new WebDriverWait(driver, 5).until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@text='ACCEPT' or @text='Accept']")));
//            driver.findElement(By.xpath("//*[@text='ACCEPT' or @text='Accept']")).click();
//        } catch (Exception e) {
//            System.out.println("No Accept at start");
//        }

        try {
            new WebDriverWait(driver, 5).until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@text='TOP CHARTS']")));
            driver.findElement(By.xpath("//*[@text='TOP CHARTS']")).click();

        } catch (Exception ex) {
            System.out.println("No Top Charts");
        }

        driver.findElement(By.xpath("//*[contains(@text,'TOP FREE')]")).click();
        int countOfOnScreenApps;

        List<AndroidElement> freeAppsElements = driver.findElements(By.xpath("//*[@resource-id='com.android.vending:id/li_title']"));
        ArrayList<String> freeApps = new ArrayList<String>();

        countOfOnScreenApps = freeAppsElements.size();

        int iter = 0;
        while (freeApps.size() < 10) {
            iter++;
            for (int i = 0; i < countOfOnScreenApps; i++) {
                String temp = freeAppsElements.get(i).getText();
                freeApps.add(temp);
            }
            if (iter > 10) {
                throw new Exception("Failed after 10 iterations");
            }
            driver.swipe(100, 100, 100, 300, 500);
        }
        System.out.println("------------------- 10's free apps today ------------------");
        for (int i = 0; i < 10; i++) {
            System.out.println(freeApps.get(i));
        }
        System.out.println("-----------------------------------------------------------");

        driver.closeApp();
    }

    @Override
    protected void iosTest() throws Exception {
        String countriesString = "//*[(@knownSuperClass='UILabel' or @knownSuperClass='UICollectionViewCell' or @knownSuperClass='UIAccessibilityElement') and (not(contains(@text,'Today')) and not(contains(@text,':')) and not(contains(@text,'Add')))][1]";
        driver.executeScript("client:client.sendText(\"{HOME}\")");
        driver.findElement(By.xpath("//*[@accessibilityLabel='Clock']")).click();
        driver.findElement(By.xpath("//*[@text='World Clock' and (@knownSuperClass='UITabBarButton' or @class='UIAButton')]")).click();
        try {
            WebElement element = driver.findElement(By.xpath("//*[@text='Add' and @x>0 and @onScreen='true']"));
        } catch (Exception e) {
            List<WebElement> list = driver.findElements(By.xpath(countriesString));
            deleteCountry(list.get(list.size() - 1).getText());
        }
        driver.findElement(By.xpath("//*[@text='Add' and @x>0 and @onScreen='true']")).click();
        driver.findElement(By.xpath("  //*[@knownSuperClass='UISearchBarTextField' or @class='UIASearchBar']")).click();
        driver.getKeyboard().sendKeys("LONDON");
        driver.findElement(By.xpath("//*[@text='London, England']")).click();
        deleteCountry("London");
        driver.executeScript("client:client.sendText(\"{HOME}\")");

    }

    private void deleteCountry(String countryStr) {
        driver.executeScript("client:client.getVisualDump(\"native\")");
        String deleteElement = "//*[contains(@text,'Delete " + countryStr + "')]  | //*[contains(@text,'London')]/*/*[@text='Remove clock']";

        driver.findElement(By.xpath("//*[@text='Edit']")).click();
        driver.findElement(By.xpath(deleteElement)).click();

        try {
            WebElement element = driver.findElement(By.xpath("//*[@text='Delete']"));
            driver.findElement(By.xpath("//*[@text='Delete']")).click();
            driver.findElement(By.xpath("//*[@accessibilityLabel='World Clock']")).click();
        } catch (Exception e) {

        }
    }
}