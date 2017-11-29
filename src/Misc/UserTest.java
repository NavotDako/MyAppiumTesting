package Misc;

import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import static java.lang.Thread.sleep;

class UserTest implements Runnable {
    String username;
    String password;
    int userID;
    WebDriver driver;

    UserTest(String username, String password, int userID) {
        this.username = username;
        this.password = password;
        this.userID = userID;
    }

    public void run() {
        DesiredCapabilities dc = new DesiredCapabilities().chrome();
        dc.setCapability("user", SE.ADMIN_USERNAME);
        dc.setCapability("password", SE.ADMIN_PASSWORD);
        try {
            this.driver = new RemoteWebDriver(new URL(SE.url), dc);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        try {
            driver.get(SE.CloudUrl);
            SE.login(driver, username, password);
            password = firstTime(driver, password);

            sortDevicesList();
            openSTAW();
            switchToSTAW();
            endSession();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            driver.close();
            driver.quit();
        }

        System.out.println(username + " closing.");
    }

    private void switchToSTAW() throws InterruptedException {
        Thread.sleep(500);
        ArrayList<String> windowHandles;
        windowHandles = new ArrayList<>(driver.getWindowHandles());
        driver.switchTo().window(windowHandles.get(1));
        Thread.sleep(500);
    }

    private void sortDevicesList() throws InterruptedException {
        String address = driver.getCurrentUrl();
        if (address.equals(SE.CloudUrl + "/index.html#/launchpad")) {
            driver.findElement(By.xpath("//*[@id=\"content\"]/md-content/md-toolbar/div/div/a/button")).click(); // Switch to list View
        }
        Thread.sleep(1500);
        driver.findElement(By.xpath("//*[@id=\"content-after-toolbar\"]/div/md-content[2]/div/div/div[3]/md-menu/md-input-container/div[1]")).click(); //status box
        SE.clickByText(driver, "button", "CLEAR", 0);
        SE.clickByText(driver, "md-checkbox", "Available", 0);
        driver.navigate().refresh();
        Thread.sleep(1500);
    }

    private boolean openSTAW() throws InterruptedException {
        boolean success = false;
        String element = "//*[@id=\"content-after-toolbar\"]/div/md-virtual-repeat-container/div/div[2]/div/md-content/table/*/*/*/div[contains(text(),\"Available\")]";
        int deviceIndex = 0;
        while (!success) {
            try {
                // Stage 0 - Find a device
                List<WebElement> automationBtn = driver.findElements(By.xpath(element));
                if (automationBtn.size() > 0) {

                    System.out.println(username + " have found a device, trying to open an automation window");

                    boolean goodMessage = false;

                    while (!goodMessage) {
                        SE.clickByText(driver, "table/tbody/tr/td", "Available", deviceIndex);
                        driver.findElement(By.xpath("//*[@id=\"full-page-container\"]/div[1]/div/div/div/button[5]")).click(); // Click on Automation button
                        Thread.sleep(5000);
                        List<WebElement> list = driver.findElements(By.xpath("//div"));
                        for (WebElement divElement : list) {
                            goodMessage = true;
                            //  System.out.println(element.getText());
                            if (divElement.getText().contains("Cannot reserve device")) {
                                goodMessage = false;
                                list = driver.findElements(By.xpath("//button"));
                                for (WebElement ele : list) {
                                    //  System.out.println(element.getText());
                                    goodMessage = true;
                                    if (ele.getText().contains("Ok")) {
                                        ele.click();
                                        deviceIndex++;
                                        goodMessage = false;
                                        break;
                                    }
                                }
                                break;
                            }

                        }
                    }
                    // Stage 1 - "Setting up connection to device. Operation may take up to 1 minute."
                    success = waitForWindow();
                } else {
                    System.out.println(username + " No Available Devices. waiting for a device...");
                    Thread.sleep(5000);
                }
            } catch (NoSuchElementException e) {
                e.printStackTrace();
                System.out.println(username + " couldn't open an automation window, next try");
                Thread.sleep(3000);
                return false;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }

        return true;
    }

    private boolean waitForWindow() throws InterruptedException {
        System.out.println(username + " - Waiting for Page to Open");
        boolean success = false;
        ArrayList<String> windowHandles;
        long startTime = System.currentTimeMillis();
        do {
            windowHandles = new ArrayList<>(driver.getWindowHandles());
            if (windowHandles.size() > 1) { // Check if new tab was opened
                success = true;
                break;
            }
            Thread.sleep(1000);
        }
        while (((System.currentTimeMillis() - startTime) < 60000));   // Check if "Device Connection" window still appear
        if (!success && !driver.findElements(By.xpath("/html/body/div[1]/div//*[contains(text(),\"Device Connection\")]")).isEmpty()) {
            System.out.println(username + " - Problem Opening a device..");
        }
        return success;
    }

    private void endSession() throws InterruptedException {
        Thread.sleep(5000);
        System.out.println(username + ": CLOSE AUTOMATION");
        boolean flag = false;
        while (!flag) {
            try {
                driver.findElement(By.xpath("//*[contains(@aria-label,'End Session')]")).click();
                flag = true;
            } catch (Exception e) {
                Thread.sleep(1000);
            }

        }


        driver.findElement(By.xpath("/html/body/div[1]/div/div/before-exit-dialog/div/div[3]/button[1]/span")).click(); // release

    }

    private static String firstTime(WebDriver driver, String password) throws InterruptedException {
        WebDriverWait wait = new WebDriverWait(driver, 15);
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("/html/body/div[2]/div/div/form/div[1]/input")));  //Wait for more

        driver.findElement(By.xpath("/html/body/div[2]/div/div/form/div[1]/input")).sendKeys(password); //  old password
        driver.findElement(By.xpath("/html/body/div[2]/div/div/form/div[2]/input")).sendKeys(password + 1); // new password
        driver.findElement(By.xpath("/html/body/div[2]/div/div/form/div[3]/input")).sendKeys(password + 1); // new password

        Actions action = new Actions(driver);
        WebElement element = driver.findElement(By.xpath("/html/body/div[2]/div/div/form/div[4]/button[2]"));
        // action.click(element).perform();
        action.clickAndHold(element).perform();
        Thread.sleep(500);
        action.release().perform();
        Thread.sleep(500);
        String address = driver.getCurrentUrl();
        if (address.equals(SE.CloudUrl + "/index.html#/expired")) {
            System.out.println("Change Password Page Error - refreshing");
            driver.get(SE.CloudUrl + "/index.html#/expired");

        }
        Thread.sleep(500);
        return password + 1;
    }

}