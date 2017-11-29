package AppiumSuite;

import io.appium.java_client.ios.IOSDriver;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.remote.SessionId;

import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;

public class NewIOSDriver extends IOSDriver {
    private String deviceID = null;
    private String deviceName = null;
    private String lastCommand = "";
    SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
    private boolean INSIDE = false;
    String lastWhen = "";
    public NewIOSDriver(URL remoteAddress, Capabilities desiredCapabilities) {
        super(remoteAddress, desiredCapabilities);
//        if (Runner.GRID) System.out.println(this.getCapabilities().getCapability("cloudViewLink"));
        try {
            this.deviceID = (String) desiredCapabilities.getCapability("udid");
            this.deviceName = ((String) desiredCapabilities.getCapability("deviceName")).replace(" ", "_").replace("'", "-").trim();

        } catch (Exception e) {
            System.out.println("No Id or Name");
        }
        lastCommand = "";
    }

    @Override
    protected void log(SessionId sessionId, String commandName, Object toLog, When when) {
        if (commandName.equals("newSession")) sdf = new SimpleDateFormat("HH:mm:ss");
        System.out.println(sdf.format(new Date(System.currentTimeMillis())) + ": " + deviceID + " - " + when + ": " + commandName + " toLog:" + toLog);
        super.log(sessionId, commandName, toLog, when);
//        if (deviceName != null) {
//            Utils.writeToDeviceLog(deviceName, sdf.format(new Date(System.currentTimeMillis())) + when + ": " + commandName + " toLog:" + toLog);
//        }
    }


}
