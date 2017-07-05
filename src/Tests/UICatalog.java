package Tests;

import org.openqa.selenium.By;
import org.openqa.selenium.Point;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.DesiredCapabilities;
import io.appium.java_client.TouchAction;

public class UICatalog extends BaseTest {

    UICatalog(String testName, String deviceID, String url,int iteration) {
        super(testName, deviceID, url, iteration);
    }

    @Override
    public DesiredCapabilities createCapabilities(DesiredCapabilities dc) {
        DesiredCapabilities cap = new DesiredCapabilities();
        if (deviceOS.contains("ios")) {
            cap.setCapability("bundleId", "com.experitest.UICatalog");

        } else {
            cap.setCapability("app", "com.experitest.uicatalog");
            cap.setCapability("appActivity", ".MainActivity");

        }
        return cap;
    }

    @Override
    protected void androidTest() throws Exception {
        String app = "com.experitest.uicatalog";
        long t = System.currentTimeMillis();
        Thread.sleep(1000);
        WebElement ele = driver.findElementByXPath(
                "/hierarchy/android.widget.FrameLayout/android.widget.LinearLayout/android.widget.FrameLayout/android.view.ViewGroup/android.widget.FrameLayout[2]/android.widget.RelativeLayout/android.widget.ListView");

        ele.findElement(By.xpath("//android.widget.TextView[1]")).click();//

        driver.findElementById(app + ":id/rb2").click();
        driver.findElementById(app + ":id/rb1").click();
        driver.findElementById(app + ":id/rb3").click();
        driver.findElementById(app + ":id/changeBt").click();
        driver.findElementById(app + ":id/changeBt").click();
        driver.findElementById(app + ":id/changeBt").click();
        driver.findElementById(app + ":id/changeBt").click();
        driver.findElementById(app + ":id/changeBt").click();
        driver.findElementById(app + ":id/checkBox").click();
        Thread.sleep(500);
        driver.navigate().back();
        Thread.sleep(500);
        ele.findElement(By.xpath("//android.widget.TextView[2]")).click();

        Point sb = driver.findElementById(app + ":id/seekBar").getLocation();
        Point rb = driver.findElementById(app + ":id/ratingBar").getLocation();
        driver.swipe(sb.x, sb.y, sb.x + 300, sb.y, 10);
        driver.swipe(rb.x, rb.y, rb.x + 500, rb.y, 10);
        driver.swipe(rb.x, rb.y, rb.x + 250, rb.y, 10);
        driver.navigate().back();
        Thread.sleep(500);
        ele.findElement(By.xpath("//android.widget.TextView[3]")).click();
        Thread.sleep(500);
        driver.findElementById(app + ":id/editText").sendKeys("do");
        driver.findElementById(app + ":id/editText2").sendKeys("you");
        driver.findElementById(app + ":id/editText3").sendKeys("see");
        driver.findElementById(app + ":id/editText4").sendKeys("486");
        driver.findElementById(app + ":id/editText5").sendKeys("?");
        driver.swipe(150, 400, 150, 150, 1000);
        driver.findElementById(app + ":id/editText6")
                .sendKeys("1215616123164561431215616123164561431215616123164561431215616123");
        driver.findElementById(app + ":id/editText7").sendKeys("do");
        driver.swipe(150, 400, 150, 150, 1000);
        driver.findElementById(app + ":id/editText8").sendKeys("you");
        driver.findElementById(app + ":id/editText9").sendKeys("0000");
        driver.findElementById(app + ":id/editText10").sendKeys("08/64/31");
        driver.hideKeyboard();
        driver.navigate().back();
        Thread.sleep(500);
        ele.findElement(By.xpath("//android.widget.TextView[4]")).click();

        driver.findElementById("com.experitest.uicatalog:id/dateButton").click();
        driver.findElementById("android:id/date_picker_header_year").click();
        Point year = driver.findElementById("android:id/text1").getLocation();
        driver.swipe(year.x, year.y + 300, year.x, year.y, 100);
        driver.swipe(year.x, year.y + 300, year.x, year.y, 100);
        driver.findElementById("android:id/text1").click();
        driver.findElementById("Next month").click();
        driver.findElementById("Next month").click();
        driver.findElementById("Next month").click();

        driver.findElementById("android:id/button1").click();
        driver.navigate().back();
        Thread.sleep(500);
        ele.findElement(By.xpath("//android.widget.TextView[5]")).click();
        Thread.sleep(500);
        driver.findElementById(app + ":id/button3").click();
        driver.findElementById(app + ":id/button2").click();
        Thread.sleep(500);
        System.out.println(driver.findElementById("android:id/message").getText());
        driver.findElementById("android:id/button3").click();
        driver.findElementById(app + ":id/button3").click();

        Thread.sleep(500);
        driver.navigate().back();
        Thread.sleep(500);
        ele.findElement(By.xpath("//android.widget.TextView[9]")).click();
        WebElement flam1 = driver.findElementById("com.experitest.uicatalog:id/myimage1");
        WebElement flam2 = driver.findElementById("com.experitest.uicatalog:id/myimage2");
        WebElement flam3 = driver.findElementById("com.experitest.uicatalog:id/myimage3");
        WebElement flam4 = driver.findElementById("com.experitest.uicatalog:id/myimage4");
        WebElement flamBase = driver.findElementById("com.experitest.uicatalog:id/txtForPinch");
        TouchAction touchAction = new TouchAction(driver);
        touchAction.press(flam1).moveTo(flamBase).release().perform();
        Thread.sleep(1000);
        touchAction = new TouchAction(driver);
        touchAction.press(flam2).moveTo(flamBase).release().perform();
        Thread.sleep(1000);
        touchAction = new TouchAction(driver);
        touchAction.press(flam3).moveTo(flamBase).release().perform();
        Thread.sleep(1000);
        touchAction = new TouchAction(driver);
        touchAction.press(flam4).moveTo(flamBase).release().perform();
        Thread.sleep(1000);

        driver.navigate().back();
        Thread.sleep(500);
        System.out.println("time is : " + (System.currentTimeMillis() - t));

    }

    @Override
    protected void iosTest() throws Exception {

        driver.findElementByAccessibilityId("TextFields").click();
        Thread.sleep(1000);
        driver.findElementByName("Normal").clear();
        driver.findElementByName("Normal").sendKeys("you");
        driver.findElementByName("Rounded").clear();
        driver.findElementByName("Rounded").sendKeys("to");
        driver.findElementByName("Secure").clear();
        driver.findElementByName("Secure").sendKeys("relax");
        driver.hideKeyboard();
        driver.findElementByName("Check").clear();
        driver.findElementByName("Check").sendKeys("!!");
        driver.findElementByName("Back").click();

        Thread.sleep(1000);
        driver.findElementByName("Buttons").click();
        Thread.sleep(1000);

        driver.findElementByName("Gray").click();
        driver.findElementByName("UIButtonIdentifier").click();
        driver.findElementByName("RoundedUIButtonIdentifier").click();
        driver.findElementByName("DetailedUIButtonIdentifier").click();
        driver.swipe(150, 600, 150, 150, 10);
        driver.swipe(150, 600, 150, 150, 10);
        driver.findElementByName("InfoLightUIButtonIdentifier").click();
        driver.findElementByName("InfoUIButtonIdentifier").click();
        driver.findElementByName("ContactUIButtonIdentifier").click();
        driver.findElementByName("Back").click();

        Thread.sleep(1000);
        driver.findElementByName("Map").click();
        Thread.sleep(1000);

        int i = 0;
        while (i++ < 3) {
            int yS = (int) (Math.random() * 700) + 300;
            int xS = (int) (Math.random() * 500) + 300;
            int yE = (int) (Math.random() * 700) + 300;
            int xE = (int) (Math.random() * 500) + 300;
            driver.swipe(xS, xE, yS, yE, 10);
        }

        driver.findElementByName("Back").click();
        Thread.sleep(500);
        driver.findElementByName("Alerts").click();
        Thread.sleep(500);
        driver.findElementByName("Show Simple").click();
        Thread.sleep(1500);
        driver.findElementByName("OK").click();
        Thread.sleep(500);
        driver.findElementByName("Show OK-Cancel").click();
        Thread.sleep(500);
        driver.findElementByName("Cancel").click();
        Thread.sleep(500);
        driver.findElementByName("Show Customized").click();
        Thread.sleep(500);
        driver.findElementByName("Button1").click();
        Thread.sleep(500);
        driver.findElementByName("Back").click();

        Thread.sleep(500);
        driver.findElementByName("Toolbar").click();
        Thread.sleep(500);

        driver.findElementByName("Translucent").click();
        driver.findElementByName("Image").click();
        driver.swipe(45, 185, 45, 220, 10);
        // will
        // WebElement ele =
        // client.findElementByXPath("//XCUIElementTypeApplication[@name=\"UICatalog\"]/XCUIElementTypeWindow[1]/XCUIElementTypeOther/XCUIElementTypeOther/XCUIElementTypeOther/XCUIElementTypeOther/XCUIElementTypePicker/XCUIElementTypePickerWheel/XCUIElementTypeOther[17]");
        driver.swipe(100, 250, 100, 200, 10);
        Thread.sleep(1000);
        driver.swipe(100, 250, 100, 200, 10);
        Thread.sleep(500);
        driver.findElementByName("Back").click();
        Thread.sleep(500);
        driver.swipe(150, 300, 150, 200, 10);
        driver.findElementByName("Authentication").click();
        driver.findElementByName("Request Touch ID Authentication").click();
        System.out.println(
                driver.findElementByName("Error Code: -7. No fingers are enrolled with Touch ID.").getText());
        driver.findElementByName("Ok, Got it!").click();
        Thread.sleep(1000);
        driver.findElementByName("Back").click();
        Thread.sleep(1000);
        driver.closeApp();


    }
}
