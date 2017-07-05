package Tests;

import io.appium.java_client.remote.AndroidMobileCapabilityType;
import io.appium.java_client.remote.MobileBrowserType;
import org.openqa.selenium.By;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * Created by amit.licht on 05/24/2017.
 */
public class WebTest extends BaseTest {

    public WebTest(String deviceEntry, DesiredCapabilities generalDC, String url, int i) {
        super("WebTest", deviceEntry, url,i);
        DesiredCapabilities dc = createCapabilities(generalDC);
        if (init(dc)) {
            executeTest();
        }
    }

    public DesiredCapabilities createCapabilities(DesiredCapabilities dc) {
        DesiredCapabilities tempDC = dc;
        if (deviceOS.contains("android")) tempDC.setBrowserName(MobileBrowserType.CHROMIUM);
        else tempDC.setBrowserName(MobileBrowserType.SAFARI);
        return tempDC;
    }

    @Override
    protected void androidTest() throws Exception {
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
        driver.get("http://google.com");
        System.out.println("page title: " + driver.getTitle());
        driver.findElement(By.xpath("//*[@id='lst-ib']")).sendKeys("appium tutorial");
        driver.findElementByXPath("//*[@name='btnG']").click();
        Map<String, String> sites = getSites();
        for (Map.Entry site : sites.entrySet()) {
            driver.get("http://" + site.getKey());
            new WebDriverWait(driver, 20).until(ExpectedConditions.presenceOfElementLocated(By.xpath((String) site.getValue())));
        }

    }

    @Override
    protected void iosTest() throws Exception {
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
        driver.get("http://google.com");
        System.out.println("page title: " + driver.getTitle());
        driver.findElement(By.xpath("//*[@id='lst-ib']")).sendKeys("appium tutorial");
        driver.findElementByXPath("//*[@name='btnG']").click();

        Map<String, String> sites = getSites();
        for (Map.Entry site : sites.entrySet()) {
            driver.get("http://" + site.getKey());
            new WebDriverWait(driver, 20).until(ExpectedConditions.presenceOfElementLocated(By.xpath((String) site.getValue())));
        }
    }

    private Map<String, String> getSites() {
        Map<String, String> sitesMap = new HashMap<>();
        sitesMap.put("www.bbc.com", "//*[@alt='BBC']");
        sitesMap.put("www.google.com", "//*[@id='hplogo']");
        sitesMap.put("www.amazon.com", "//*[@class='nav-logo-base nav-sprite']");
        sitesMap.put("www.apple.com", "//*[@id='ac-gn-firstfocus-small' or @id='ac-gn-firstfocus']");
        sitesMap.put("www.facebook.com", "xpath=//*[@id='header' or @class='clearfix loggedout_menubar']");
        sitesMap.put("www.wikipedia.org", "xpath=//*[@alt='WikipediA']");
        sitesMap.put("www.instagram.com", "xpath=//*[@class='_du7bh _soakw coreSpriteLoggedOutWordmark']");
        sitesMap.put("www.reddit.com", "xpath=//*[@class='TopNav-text-vcentering']");
        sitesMap.put("www.linkedin.com", "xpath=//*[@alt='LinkedIn' and @class='lazy-loaded']");
        sitesMap.put("www.netflix.com", "xpath=//*[@nodeName='svg']");
        sitesMap.put("www.stackoverflow.com", "xpath=//*[@class='topbar-icon js-site-switcher-button icon-site-switcher-bubble' or @text='Stack Overflow']");
        sitesMap.put("www.imdb.com", "xpath=//*[@class='navbar-link' or @text='IMDb']");
        sitesMap.put("www.paypal.com", "xpath=//*[@text='PayPal' and @class='paypal-img-logo']");
        sitesMap.put("www.dropbox.com", "xpath=//*[@class='dropbox-logo__type' or @alt='Dropbox']");
//        sitesMap.put("www.yahoo.com", "xpath=//*[@id='yucs-logo-img']");
//        sitesMap.put("www.cnn.com", "//*[@id='logo']");
//        sitesMap.put("www.youtube.com", "xpath=//*[@class='_moec _mvgc']");
//        sitesMap.put("www.baidu.com", "xpath=//*[@id='logo' or @alt='logo'] ");
//        sitesMap.put("www.twitter.com", "xpath=//*[@class='AppBar-icon Icon Icon--twitter' or @text='Welcome to Twitter']");
//        sitesMap.put("www.aliexpress.com", "xpath=//*[@class='downloadbar-logo'or @text='AliExpress']");
//        sitesMap.put("www.ask.com", "xpath=//*[@class='sb-logo posA' or @class='sb-logo']");
//        sitesMap.put("www.espn.com", "xpath=//*[@class='container']");
        return sitesMap;
    }
}
