package Misc; /**
 * Created by david.zagury on 7/18/2017.
 */

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import static java.lang.Thread.activeCount;
import static java.lang.Thread.sleep;

public class SE {

    private static final String CHROME_DRIVER = "lib\\chromedriver.exe";
    public static final String CloudUrl = "http://192.168.2.13";
    public static final String ADMIN_USERNAME = "admin";
    public static final String ADMIN_PASSWORD = "Experitest2012";
    static WebDriver driver = null;
    static WebDriverWait wait = null;
    static String url = null;
    private static boolean cloudHub = false;

    public static void main(String[] args) throws InterruptedException, MalformedURLException {
        if (cloudHub) {
            url = CloudUrl + "/wd/hub";
        } else {
            url = "http://localhost:4444/wd/hub";
        }

        DesiredCapabilities dc = new DesiredCapabilities().chrome();
        dc.setCapability("user", ADMIN_USERNAME);
        dc.setCapability("password", ADMIN_PASSWORD);
        driver = new RemoteWebDriver(new URL(url), dc);
        driver.manage().window().maximize();
        wait = new WebDriverWait(driver, 15);

        int numberOfUsers = 3;

        LoginToUsersPage();
        Thread.sleep(1000);
        DeleteUsers(numberOfUsers);

        final String[] passwords;

        try {

            passwords = createUsers(driver, numberOfUsers);

            List<Thread> testsPool = new ArrayList<>();
            for (int i = 0; i < numberOfUsers; i++) {
                testsPool.add(new Thread(new UserTest("user_" + i, passwords[i], i)));
            }

            for (Thread t : testsPool) {
                System.out.println("\nStarting user ");
                t.start();
            }

            System.out.println("\nAll user has been started, waiting for finish\n");
            for (Thread t : testsPool) {
                t.join();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DeleteUsers(numberOfUsers);
            driver.close();
            driver.quit();
        }


    }

    private static void DeleteUsers(int numberOfUsers) {
        System.out.println("Deleting Users:");
        for (int i = 0; i < numberOfUsers; i++) {
            System.out.println("Searching for user_" + i);
            try {
                driver.findElement(By.xpath("//*[@id=\"content-after-toolbar\"]/div/div/div[1]/md-content/md-virtual-repeat-container/div/div[2]/div/table/tbody/*/*[text()='user_" + i + "']/../*[1]/*[1]")).click();  // Select user_#
            } catch (Exception e) {
                System.out.println("No user named - user_" + i);
            }
        }
        try {
            driver.findElement(By.xpath("//*[@id=\"full-page-container\"]/div[1]/div/div/div/button[3]")).click();  // Click delete
            driver.findElement(By.xpath("/html/body/div[1]/div/div/div/div[3]/button[2]")).click(); // Click to confirm delete

        } catch (Exception e) {
            System.out.println("No Users To Delete");
        }


    }

    public static void login(WebDriver driver, String username, String password) {

        driver.findElement(By.xpath("/html/body/div[2]/div[2]/div/form/div[1]/div[1]/input")).sendKeys(username); //username
        driver.findElement(By.xpath("/html/body/div[2]/div[2]/div/form/div[2]/div[1]/input")).sendKeys(password);    //password
        driver.findElement(By.xpath("/html/body/div[2]/div[2]/div/form/button")).click();   //login

    }

    private static String[] createUsers(WebDriver driver, int numberOfUsers) {


        String[] passwords = new String[numberOfUsers];
        // LOGIN:
        // LoginToUsersPage();

        for (int i = 0; i < numberOfUsers; i++) {
            try {
                sleep(500);
            } catch (InterruptedException e1) {
            }
            passwords[i] = addNewUser(driver, "user_" + i, "firstName_" + i, "lastName_" + i, "email_" + i + "@email.com", "User", "MY_PROJECT");
            if (passwords[i].contains("-1")) {

            } else {
                System.out.println("New user: user_" + i + " Password: " + passwords[i]);
            }
        }
        return passwords;
    }

    private static void LoginToUsersPage() {
        driver.get(CloudUrl);
        driver.findElement(By.xpath("/html/body/div[2]/div[2]/div/form/div[1]/div[1]/input")).sendKeys(ADMIN_USERNAME); //username
        driver.findElement(By.xpath("/html/body/div[2]/div[2]/div/form/div[2]/div[1]/input")).sendKeys(ADMIN_PASSWORD);    //password
        driver.findElement(By.xpath("/html/body/div[2]/div[2]/div/form/button")).click();   //login

        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id=\"side-menu\"]/li[8]/a")));  //Wait for more
        driver.findElement(By.xpath("//*[@id=\"side-menu\"]/li[8]/a")).click(); // more
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id=\"side-menu\"]/li[8]/a")));  //Wait for users
        driver.findElement(By.xpath("//*[@id=\"side-menu\"]/li[8]/ul/li[2]/a")).click();    // Users
    }

    private static String addNewUser(WebDriver driver, String user, String firstName, String lastName, String email, String role, String project) {
        WebDriverWait wait = new WebDriverWait(driver, 15);

        // Add user:
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        driver.findElement(By.xpath("//*[@id=\"full-page-container\"]/div[1]/div/div/div/button[1]")).click();


        driver.findElement(By.xpath("/html/body/div[1]/div/div/div/form/div[2]/div[1]/input")).sendKeys(user);  //Username
        driver.findElement(By.xpath("/html/body/div[1]/div/div/div/form/div[2]/div[2]/input")).sendKeys(firstName); //First Name
        driver.findElement(By.xpath("/html/body/div[1]/div/div/div/form/div[2]/div[3]/input")).sendKeys(lastName);  //Last Name
        driver.findElement(By.xpath("/html/body/div[1]/div/div/div/form/div[2]/div[4]/input")).sendKeys(email);     //Email
        new Select(driver.findElement(By.xpath("/html/body/div[1]/div/div/div/form/div[2]/fieldset/div[1]/select"))).selectByVisibleText(role); // Role


        //Select Project:
        driver.findElement(By.xpath("/html/body/div[1]/div/div/div/form/div[2]/fieldset/div[2]/md-autocomplete/md-autocomplete-wrap/*[1]")).clear();
        driver.findElement(By.xpath("/html/body/md-virtual-repeat-container/div/*[2]//*[1]/*[1]")).click();


        driver.findElement(By.xpath("/html/body/div[1]/div/div/div/form/div[3]/button[2]")).click();    // Create


        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("/html/body/div[1]/div/div/div/div[2]/p[1]/b")));
        String pass = driver.findElement(By.xpath("/html/body/div[1]/div/div/div/div[2]/p[1]/b")).getText();
        try {
            sleep(500);
        } catch (InterruptedException e1) {
        }

        wait.until(ExpectedConditions.elementToBeClickable(By.xpath("/html/body/div[1]/div/div/div/div[3]/button")));
        driver.findElement(By.xpath("/html/body/div[1]/div/div/div/div[3]/button")).click();
        try {
            sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return pass;
    }

    public static void clickByText(WebDriver driver, String tag, String text, int i) {
        int count = 0;
        List<WebElement> list = driver.findElements(By.xpath("//" + tag));
        for (WebElement element : list) {
            //  System.out.println(element.getText());
            if (element.getText().contains(text)) {
                count++;
                if (count > i) {
                    element.click();
                    break;
                }
            }
        }
    }

}
