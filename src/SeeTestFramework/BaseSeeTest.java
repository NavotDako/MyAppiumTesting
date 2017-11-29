package SeeTestFramework;

import FrameWork.Runner;
import FrameWork.Utils;
import com.experitest.client.Client;
import com.experitest.client.GridClient;

import java.io.*;


public abstract class BaseSeeTest {
    protected int success = 0;
    public String deviceOSVersion = null;
    public String deviceName = null;
    protected String reportFolder;
    protected String deviceOS;
    protected String testName;
    protected String deviceID = "";
    protected Client client = null;
    public String deviceShortName = null;
    int iteration = 0;
    private boolean STOP_FLAG = false;
    long startTime = 0;

    public BaseSeeTest(String deviceID, String testName, int iteration) {
        this.iteration = iteration;
        this.reportFolder = Runner.reportFolderString;
        if (Runner.GRID) {
            this.deviceOS = Runner.cloudServer.getDeviceOSByUDID(deviceID);
            this.deviceName = Runner.cloudServer.getDeviceNameByUDID(deviceID);
        } else {
            this.deviceOS = Runner.LDM.getDeviceOSByUDID(deviceID);
            this.deviceName = Runner.LDM.getDeviceNameByUDID(deviceID);
        }
        this.deviceID = deviceID;
        this.testName = testName;

    }

    public boolean init() {
        startTime = System.currentTimeMillis();
        try {
            CreateClient();
            return true;
        } catch (Exception e) {
            reportFailure(e);
            finish();
            e.printStackTrace();
            return false;
        }
    }

    protected void CreateClient() throws Exception {

        if (Runner.GRID) {
            getGridClient(deviceID);
        } else {
            getClient(deviceID);
        }
        if (client == null) {
            System.out.println("Client SetUp Failed for - " + deviceID);
        } else {
            System.out.println("Client Was SetUp For - " + deviceID);
        }
        prepareReporter(iteration);
        prepareDevice();

    }

    public void getGridClient(String deviceID) {
        GridClient grid = new GridClient(Runner.cloudServer.USER, Runner.cloudServer.PASS, Runner.cloudServer.PROJECT, Runner.cloudServer.HOST, Integer.parseInt(Runner.cloudServer.PORT), Runner.cloudServer.SECURED);
        client = grid.lockDeviceForExecution(testName, "@serialnumber='" + deviceID + "'", 5, 30000);
    }

    public void getClient(String deviceID) {
        client = new Client("localhost", 8889, true);
        System.out.println("Boaz Hadad Is The King Of Client - Not Gridy");
        client.waitForDevice("@serialnumber='" + deviceID + "'", 300000);
    }

    public void executeTest() {
        System.out.println("--------------------------------------------------------------------------");
        System.out.println("Starting test - " + testName + " For Device - " + deviceID);
        System.out.println("--------------------------------------------------------------------------");

        try {
            if (deviceOS.contains("ios")) {
                iosTest();
            } else {
                androidTest();
            }
            reportSuccess();
        } catch (Exception e) {
            try {
                client.getVisualDump("native");
                client.getVisualDump("web");

            } catch (Exception ex) {
                System.out.println(deviceID + "- Failed to get PAGE SOURCE after failure in test - " + testName);
            }
            reportFailure(e);
        }
        finish();
    }

    protected abstract void androidTest() throws Exception;

    protected abstract void iosTest() throws Exception;

    private void reportSuccess() {
        long time = System.currentTimeMillis() - startTime;
        String reportURL;
        try {
            reportURL = client.generateReport(false);
        } catch (Exception e2) {
            reportURL = "generateReport failed!!! NOT reportURL in the driver capabilities";
        }
        Utils.writeToOverall(true, deviceName.replace(" ", "_").trim(), testName, null, time, reportURL, iteration);
        System.out.println("--------------------------------------------------------------------------");
        System.out.println("THE TEST HAD PASSED - " + testName + " For Device - " + deviceID);
        System.out.println("--------------------------------------------------------------------------");
    }

    private void reportFailure(Exception e) {

        long time = 0;
        String report = "";
        try {
            report = client.generateReport(false);
        } catch (Exception e2) {
            System.out.println("Can't get reportURL for test - " + testName + " - device - " + deviceID);
        }
        time = System.currentTimeMillis() - startTime;

        //tryToCollectSupportData();

        String deviceString = deviceID;
        try {
            deviceString = deviceName.replace(" ", "_").trim();
        } catch (Exception ex) {
            System.out.println("Can't get name for - " + deviceID);
        }
        Utils.writeToOverall(false, deviceString, testName, e, time, report, iteration);
        System.out.println("--------------------------------------------------------------------------");
        System.out.println("THE TEST HAD FAILED *** - " + testName + " For Device - " + deviceName + "_" + deviceID);
        System.out.println("--------------------------------------------------------------------------");
        e.printStackTrace();

    }

    private void finish() {
        try {
            client.releaseClient();
        } catch (Exception e) {
            System.out.println("Failed to releaseClient()!!!! - " + deviceID);
            e.printStackTrace();
        }
        System.out.println("--------------------------------------------------------------------------");
        System.out.println("Ending test - " + testName + " For Device - " + deviceID);
        System.out.println("--------------------------------------------------------------------------");
    }

    protected void prepareReporter(int i) {
        deviceName = client.getDeviceProperty("device.name");
        deviceShortName = deviceName.substring(deviceName.indexOf(":") + 1);
        System.out.println("----------------@---------------- " + Thread.currentThread().getName() + "  STARTING - " + deviceName + " - " + testName + ": Iteration - " + (i + 1));
        System.out.println("----------------@---------------- " + Thread.currentThread().getName() + "  Set Reporter - " + client.setReporter("xml", reportFolder, deviceShortName + " - " + deviceOS + " - " + testName + "_" + (i + 1)));

    }

    public void prepareDevice() throws Exception {
        client.capture();
        deviceOSVersion = client.getDeviceProperty("device.version");
//        client.setShowPassImageInReport(false);
        client.setSpeed("FAST");
        client.deviceAction("unlock");
        client.deviceAction("portrait");
        client.deviceAction("home");
        client.capture();
        if (deviceOS.contains("ios")) client.setProperty("ios.non-instrumented.dump.parameters", "20,1000,50");
    }

    public boolean tryToCollectSupportData(StringWriter errors, String generatedReport) {
        String supportData = null;
        if (generatedReport.contains("http")) generatedReport = Runner.reportFolderString;
        try {
            client.collectSupportData(generatedReport + "\\SupportDataFor_" + deviceID + "_test_" + testName + "_" + System.currentTimeMillis() + ".zip", "", deviceName, errors.toString(), "", "", true, true);
            supportData = generatedReport + "\\SupportDataFor_" + deviceID + "_test_" + testName + "_" + System.currentTimeMillis() + ".zip";
        } catch (Exception e) {
            System.out.println(Thread.currentThread().getName() + " - Failed to Collect Support Data - " + Thread.currentThread().getName() + " - Device - " + deviceShortName + " - test - " + testName);
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public void addSupportDataToReporter(String supportData) {
        boolean collected = false;
        long startTime = System.currentTimeMillis();
        do {
            try {
                // if (Runner.reporter) PManager.getInstance().addReportZipFile(supportData);
                collected = true;
            } catch (Exception e) {
                System.out.println("Failed to COLLECT SUPPORT DATA");
                System.out.println("Going to sleep for 5 seconds");
                client.sleep(2000);
            }
        } while (!collected && (System.currentTimeMillis() - startTime) > 30000);
    }

    private StringWriter getErrors(Exception e) {
        StringWriter errors = null;
        try {
            errors = new StringWriter();
            e.printStackTrace(new PrintWriter(errors));
            System.err.println(Thread.currentThread().getName() + " " + deviceName + " - StackTrace: " + errors.toString());
        } catch (Exception e1) {

        }
        return errors;
    }

}
