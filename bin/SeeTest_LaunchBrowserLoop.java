package SeetestTests;

import SeeTestFramework.BaseSeeTest;
import Tests.BaseAppiumTest;


public class SeeTest_LaunchBrowserLoop extends BaseSeeTest {

    public SeeTest_LaunchBrowserLoop(String deviceOS, String deviceSN, String testName) {
        super(deviceSN, testName);
    }


    @Override
    protected void androidRunTest() {
        String google = "www.google.com";
        String ebay = "m.ebay.com";
        String springboardIdentifier = "xpath=//*[contains(@contentDescription,'App') or contains(@contentDescription,'apps')  or @contentDescription='Xperia™ Home' or @id='workspace']";
        String xpathToVerify = "";
        if (client.capture() == null) client.report("Can't Capture!!!", false);

        for (int i = 0; i < 5; i++) {
            String site = (i % 2 == 0) ? ebay : google;
            client.launch("chrome:" + site, true, true);
            client.hybridWaitForPageLoad(30000);
           /* client.syncElements(2000,10000);
            client.sync(2000,1,10000);*/
            xpathToVerify = (i % 2 == 0) ? "//*[@alt='eBay Home page']" : "//*[@id='hplogo']";
            client.verifyElementFound("WEB", "xpath=" + xpathToVerify, 0);
            client.sendText("{HOME}");
            client.verifyElementFound("NATIVE", springboardIdentifier, 0);
        }
        if (client.capture() == null) client.report("Can't Get Capture!!!", false);
    }

    @Override
    protected void iOSRunTest() {
        String google = "www.google.com";
        String ebay = "http://m.ebay.com";
        String xpathToVerify = "";
        for (int i = 0; i < 5; i++) {
            String site = (i % 2 == 0) ? ebay : google;
            client.launch("safari:" + site, true, true);
            client.hybridWaitForPageLoad(30000);
            /* client.syncElements(2000,10000);
            client.sync(2000,1,10000);*/
            xpathToVerify = (i % 2 == 0) ? "//*[@alt='eBay Home page']" : "//*[@id='hplogo']";
            client.verifyElementFound("WEB", "xpath=" + xpathToVerify, 0);
            client.sendText("{HOME}");
            client.verifyElementFound("NATIVE", "xpath=//*[@accessibilityLabel='Settings']", 0);
        }

    }
}


