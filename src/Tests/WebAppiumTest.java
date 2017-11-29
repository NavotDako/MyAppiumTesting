package Tests;

import io.appium.java_client.remote.MobileBrowserType;
import org.openqa.selenium.By;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.*;

/**
 * Created by amit.licht on 05/24/2017.
 */
public class WebAppiumTest extends BaseAppiumTest {

    public WebAppiumTest(String deviceEntry, DesiredCapabilities generalDC, String url, int i) {
        super("WebTest", deviceEntry, url, i);
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

        Map<String, String> sites = getSites();
        for (Map.Entry site : sites.entrySet()) {
            boolean failed = false;
            try {
                driver.get("http://" + site.getKey());
                new WebDriverWait(driver, 20).until(ExpectedConditions.presenceOfElementLocated(By.xpath((String) site.getValue())));

            } catch (Exception e) {
                driver.navigate().refresh();
                failed = true;
            }
            if (failed) {
                driver.get("http://" + site.getKey());
                new WebDriverWait(driver, 20).until(ExpectedConditions.presenceOfElementLocated(By.xpath((String) site.getValue())));
            }
        }

    }

    @Override
    protected void iosTest() throws Exception {

        Map<String, String> sites = getSites();
        for (Map.Entry site : sites.entrySet()) {
            boolean failed = false;
            try {
                driver.get("http://" + site.getKey());
                new WebDriverWait(driver, 20).until(ExpectedConditions.presenceOfElementLocated(By.xpath((String) site.getValue())));

            } catch (Exception e) {
                driver.navigate().refresh();
                failed = true;
            }
            if (failed) {
                driver.get("http://" + site.getKey());
                new WebDriverWait(driver, 20).until(ExpectedConditions.presenceOfElementLocated(By.xpath((String) site.getValue())));
            }
        }
    }
    //*[@class='nav-sprite nav-logo-base']
    private Map<String, String> getSites() {
        Map<String, String> sitesMap = new HashMap<>();
        sitesMap.put("www.bbc.com", "//*[@alt='BBC']");
        sitesMap.put("www.amazon.com", "//*[@class='nav-logo-base nav-sprite' or @class='nav-sprite nav-logo-base']");
        sitesMap.put("www.facebook.com", "//*[@id='header' or @class='clearfix loggedout_menubar']");
        sitesMap.put("www.wikipedia.org", "//*[@alt='WikipediA']");
        sitesMap.put("www.reddit.com", "//*[@class='TopNav-text-vcentering']");
        sitesMap.put("www.linkedin.com", "//*[@alt='LinkedIn' and @class='lazy-loaded']");
        sitesMap.put("www.stackoverflow.com", "//*[@class='topbar-icon js-site-switcher-button icon-site-switcher-bubble' or @text='Stack Overflow']");
        sitesMap.put("www.imdb.com", "//*[@class='navbar-link' or @text='IMDb']");
        sitesMap.put("www.paypal.com", "//*[@text='PayPal' and @class='paypal-img-logo']");



//        sitesMap.put("www.apple.com", "//*[@id='ac-gn-firstfocus-small' or @id='ac-gn-firstfocus']");
//        sitesMap.put("www.instagram.com", "//*[@class='_du7bh _soakw coreSpriteLoggedOutWordmark']");
//        sitesMap.put("www.google.com", "//*[@id='hplogo']");
//        sitesMap.put("www.netflix.com", "//*[@nodeName='svg']");
//        sitesMap.put("www.dropbox.com", "//*[@class='dropbox-logo__type' or @alt='Dropbox']");
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
