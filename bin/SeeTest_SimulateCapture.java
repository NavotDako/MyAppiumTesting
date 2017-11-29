package SeetestTests;

import SeeTestFramework.BaseSeeTest;
import Tests.BaseAppiumTest;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

public class SeeTest_SimulateCapture extends BaseSeeTest {

    public SeeTest_SimulateCapture(String deviceOS, String deviceSN, String testName) {

        super(deviceOS, deviceSN, testName);
    }

    @Override
    protected void androidRunTest() {
        //checkAppData();
        client.setProperty("android.instrumentation.camera", "true");
        boolean found = false;
        client.install("http://192.168.2.72:8181/AndroidApps/cameraFlash-%20simulateCapture/com.CameraFlash-.MainActivity_ver_11.0.apk", true, false);
        client.launch("com.CameraFlash/.MainActivity", true, true);
        File file = new File("lib/SimulateCapture/pwr.png");
        client.simulateCapture(file.getAbsolutePath());
        client.textFilter("0xA8C02E", 80);
        client.getText("TEXT");
        client.verifyElementFound("Text", "by", 0);
        client.uninstall("com.CameraFlash/.MainActivity");
        client.setProperty("android.instrumentation.camera", "false");

    }

    @Override
    protected void iOSRunTest() {
        client.setProperty("android.instrumentation.camera", "true");

        boolean found = false;
        client.install("http://192.168.2.72:8181/iOSApps/PhotoPicker.ipa", true, false);
        client.launch("com.example.apple-samplecode.PhotoPicker", true, true);
        client.sleep(2000);
        File file = new File("lib\\SimulateCapture\\pwr.png");
        client.simulateCapture(file.getAbsolutePath());
        client.click("NATIVE", "xpath=//*[@class='_UIToolbarNavigationButton' and ./*[@class='UIImageView']]", 0, 1);
        client.sync(1500, 0, 6000);
        client.getText("TEXT");
        client.verifyElementFound("Text", "by", 0);
        client.uninstall("com.example.apple-samplecode.PhotoPicker");
        client.setProperty("android.instrumentation.camera", "false");

    }


    private static void checkAppData() {
        String startPath = System.getenv("APPDATA");
        System.out.println("Path from the system is: " + startPath);
        String path = startPath + "\\seetest\\app.properties";
        System.out.println("path is: " + path);
        File file = new File(path);
        boolean found = false;
        try {
            BufferedReader br = new BufferedReader(new FileReader(file));
            found = false;
            String line;
            while ((line = br.readLine()) != null) {
                if (line.replace(" ", "").equalsIgnoreCase("android.instrumentation.camera=true")) {
                    found = true;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (!found)
            throw new RuntimeException("App properties doesn't contain the 'android.instrumentation.camera = true' sentence");

    }
}
