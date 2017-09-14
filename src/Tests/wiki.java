package Tests;

import io.appium.java_client.remote.AndroidMobileCapabilityType;
import io.appium.java_client.remote.IOSMobileCapabilityType;
import io.appium.java_client.remote.MobileBrowserType;
import io.appium.java_client.remote.MobileCapabilityType;
import org.openqa.selenium.By;
import org.openqa.selenium.ScreenOrientation;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

/**
 * Created by navot.dako on 9/12/2017.
 */
public class wiki extends BaseTest {
    public wiki(String deviceID, DesiredCapabilities generalDC, String url, int iteration) {
        super("wiki", deviceID, url, iteration);
        DesiredCapabilities dc = createCapabilities(generalDC);
        if (init(dc)) {
            executeTest();
        }
    }


    public DesiredCapabilities createCapabilities(DesiredCapabilities dc) {
        DesiredCapabilities tempDC = dc;
        if (deviceOS.contains("android")) tempDC.setBrowserName(MobileBrowserType.CHROMIUM);
        else tempDC.setBrowserName(MobileBrowserType.SAFARI);
        tempDC.setCapability(MobileCapabilityType.NO_RESET, false);

        return tempDC;
    }

    @Override
    protected void androidTest() throws Exception {
        final String searchBox = "//*[@id='kw' or @id='gh-ac']";
        final String searchButton = "//*[@value='Search']";
        driver.rotate(ScreenOrientation.PORTRAIT);
        driver.get("m.ebay.com"); //launch eaby on chrome
        new WebDriverWait(driver, 10).until(ExpectedConditions.presenceOfElementLocated(By.xpath(searchBox)));
        driver.findElement(By.xpath(searchBox)).sendKeys("Hello");
        driver.findElement(By.xpath(searchButton)).click();
        new WebDriverWait(driver, 10).until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id='gh-mlogo' or @id='gh-la']")));
        try {
            Thread.sleep(2000);
            String strArray = (String) driver.executeScript("experitest:client.getAllValues(\"WEB\", \"xpath=//*[@nodeName='H3']\", \"text\")");
            splitStringAndroid(strArray);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("@@@@@@@@@@@@@@@@@@@@@@");
            String strArray = (String) driver.executeScript("experitest:client.getAllValues(\"WEB\", \"xpath=//*[@css='H3.s-item__title']\", \"text\")");
            splitStringAndroid(strArray);
        }
        driver.findElement(By.xpath(searchBox)).sendKeys("Hello Again");
        driver.findElement(By.xpath(searchButton)).click();
        try {
            new WebDriverWait(driver, 10).until(ExpectedConditions.presenceOfElementLocated(By.id("gh-mlogo")));
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        driver.findElement(By.xpath(searchBox)).sendKeys("3rd Time... Enough Already!");
        String searchText =driver.findElement(By.xpath("//*[@name='_nkw']")).getAttribute("value");
        System.out.println(searchText);
        driver.findElement(By.xpath(searchButton)).click();
        new WebDriverWait(driver, 10).until(ExpectedConditions.presenceOfElementLocated(By.id("gh-mlogo")));
    }

    public void splitStringAndroid(String str) {
        String arr[] = str.split("\"text\":\"");
        String s[] = arr[1].split("\\\\n");

        for (int i = 0; i < s.length; i++) {
            System.out.println(s[i]);
            System.out.println();
        }

    }

    @Override
    protected void iosTest() throws Exception {
        driver.rotate(ScreenOrientation.PORTRAIT);

        try {
            driver.get("http://www.apple.com");
            new WebDriverWait(driver, 10).until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id='ac-gn-firstfocus-small' or @id='ac-gn-firstfocus']")));
        } catch (Exception e) {
            driver.findElement(By.xpath("//*[@text='OK']")).click();
            driver.get("m.ebay.com");
        }
        try {
            Thread.sleep(20000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        driver.get("http://wikipedia.org");
        try {
            driver.findElement(By.id("searchInput"));
        } catch (Exception e) {

            System.out.println("Failed on first launch - Will try again");
            driver.get("http://wikipedia.org");
            try {
                driver.findElement(By.xpath("//*[@text='OK']")).click();
            } catch (Exception e2) {
            }
        }
        try {
            driver.findElement(By.id("searchInput")).sendKeys("Long Run");
        } catch (Exception e) {
            System.err.println("No Internet Connection!");
        }

        driver.findElement(By.xpath("//*[@nodeName='BUTTON']")).click();
        new WebDriverWait(driver, 10).until(ExpectedConditions.presenceOfElementLocated(By.id("mw-mf-main-menu-button")));

        driver.findElement(By.id("mw-mf-main-menu-button")).click();
        new WebDriverWait(driver, 10).until(ExpectedConditions.presenceOfElementLocated(By.id("mw-mf-main-menu-button")));
        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        driver.findElement(By.xpath("//*[@class='transparent-shield cloaked-element']")).click();
        driver.findElement(By.xpath("//*[@text='About Wikipedia' and @top='true' or @id='searchInput' or @value='Search' or @id='searchIcon']")).click();
        try {
            driver.getKeyboard().sendKeys("LONG RUN{ENTER}");

        } catch (Exception e) {
            System.out.println("**********FALIED TO SEND TEXT**************");
            e.printStackTrace();
            driver.getKeyboard().sendKeys("LONG RUN{ENTER}");
        }
        new WebDriverWait(driver, 10).until(ExpectedConditions.presenceOfElementLocated(By.id("ca-edit")));
        try {
            Thread.sleep(2000);
        } catch (Exception e) {
        }

        String str0 = (String) driver.executeScript("experitest:client.getAllValues(\"WEB\", \"xpath=//*[@id='ca-edit']\", \"hidden\")");
        String[] srt0Arr = splitStringIOS(str0);
        if (srt0Arr[0].equals("false")) {
            driver.findElement(By.id("ca-edit")).click();
            driver.executeScript("client:client.swipeWhileNotFound(\"DOWN\", 250,1000, 'WEB', \"xpath=//*[@text='Sign up']\", 0, 0, 2, true)");
            driver.rotate(ScreenOrientation.LANDSCAPE);
            driver.findElement(By.id("section_0")).click();
            driver.findElement(By.xpath("//*[contains(@id,'wpName')]")).sendKeys("LONG");
            driver.findElement(By.id("wpPassword2")).sendKeys("RUN");
            driver.findElement(By.id("wpRetype")).sendKeys("123456");
            driver.executeScript("client:client.swipeWhileNotFound(\"DOWN\", 250,1000, 'WEB', \"id=wpEmail\", 0, 0, 2, true)");
            driver.findElement(By.id("wpEmail")).sendKeys("LONG@RUN.COM");
            driver.rotate(ScreenOrientation.PORTRAIT);
            driver.rotate(ScreenOrientation.LANDSCAPE);
            driver.executeScript("client:client.swipeWhileNotFound(\"DOWN\", 150,1000, 'WEB', \"xpath=//*[@id='wpCreateaccount' and @onScreen='true']\", 0, 0, 2, true)");
            driver.context("NATIVE_APP_NON_INSTRUMENTED");
            try {
                driver.findElement(By.xpath("//*[@name='Not Now']")).click();
            } catch (Exception e) {

            }
            driver.context("WEBVIEW_1");

            try {
                driver.findElement(By.xpath("//*[@class='error']"));
            } catch (Exception e) {
                System.out.println("No error - trying to insert text and click again");
                try {
                    driver.getKeyboard().sendKeys("SOME TEXT");
                    driver.executeScript("client:client.swipeWhileNotFound(\"DOWN\", 150,1000, 'WEB', \"xpath=//*[@id='wpCreateaccount' and @onScreen='true']\", 0, 0, 2, true)");
                    driver.findElement(By.xpath("//*[@class='error']"));

                } catch (Exception ee) {
                    ee.printStackTrace();
                }
            }
            driver.findElement(By.xpath("//*[@id='mw-mf-main-menu-button']")).click();
            driver.rotate(ScreenOrientation.PORTRAIT);
            driver.findElement(By.xpath("//*[@text='Home']")).click();
        } else {
            try {
                String str1 = driver.findElement(By.xpath("//*[@id='mw-mf-last-modified']")).getText();
                if (str1.contains("Last modified")) {
                    System.out.println("Text was identified!");
                }
                driver.findElement(By.id("mw-mf-main-menu-button")).click();
                String b = driver.findElement(By.xpath("//*[@id='countryTextField']")).getText();
                System.out.println("b - "+b);
                String str2 = driver.getTitle();
                if (str2.contains("HOME")) {
                    driver.findElement(By.xpath("xpath=//*[@text='Home' and @nodeName='A']")).click();
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e1) {
                        e1.printStackTrace();
                    }
                }
            } catch (Exception e) {
                System.out.println("else failed");
            }
        }
        driver.get("http://www.google.com");
    }


    public String[] splitStringIOS(String str) {
        String arr[] = str.replace('"', ' ').split("text :")[1].split(",")[0].split("\\\\n");
        String[] s = new String[arr.length];
        int j = 0;
        for (int i = 0; i < s.length; i++) {
            if (arr[i].equals("true") || arr[i].equals("false")) {
                s[j] = arr[i];
                j++;
            }
        }
        for (int i = 0; i < s.length; i++) {
            System.out.println(s[i]);
        }
        return s;
    }


}
