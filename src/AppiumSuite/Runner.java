package AppiumSuite;

import FrameWork.CloudServer;
import FrameWork.Utils;

import java.io.*;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class Runner {
    static int REP_NUM = 100;
    public static boolean GRID = true;
    private static boolean ALL_DEVICES = true;
    public static String USED_OS = "all"; //android//ios//all

    static String reportFolderString = "c:\\temp\\AppiumReports";
    public static int index = 1;
    public static boolean SCAN_LOG = false;
    public static boolean PRINT_ERROR = false;

    public static CloudServer cloudServer;
    public static String BUILD_NUM;
    public static boolean SLEEP = true;

    public static void main(String[] args) throws IOException {
        PrepareReportsFolders();
        if (GRID) {
            BUILD_NUM = "build#" + getBuildNum();
        }
        cloudServer = new CloudServer(CloudServer.CloudServerName.MY);

        List<String> devicesList = getDevicesList();

        ExecutorService executorService = Executors.newFixedThreadPool(devicesList.size());

        List<Future> res = new LinkedList<>();
        for (int i = 0; i < devicesList.size(); i++) {
            Suite suite = new Suite(devicesList.get(i), getURL());
            res.add(executorService.submit(suite));
            System.out.println(String.format("%-50s%-15s%-3s", devicesList.get(i), "- submitted -", (i + 1)));
        }

        for (Future re : res) {
            try {
                re.get();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        executorService.shutdown();
    }

    private static String getBuildNum() throws IOException {
        File file = new File("lib/build.txt");
        String buildString = null;
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                buildString = line;
            }
        }
        int buildInt = 998;
        try {
            buildInt = Integer.parseInt(buildString);
        } catch (Exception e) {
            System.out.println("--- NO BUILD NUMBER FOUND ---");
            System.out.println("--- Will Use #999 ---");

        }
        buildInt++;
        PrintWriter writer = null;
        try {
            writer = new PrintWriter(new BufferedWriter(new FileWriter(file, false)));
            writer.write("" + buildInt);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("BUILD_NUM - " + buildString);
        return buildString;
    }

    private static List<String> getDevicesList() throws IOException {
        List<String> devicesList = new ArrayList<>();
        if (GRID) {
            if (ALL_DEVICES) {
                devicesList = cloudServer.getAllAvailableDevices(USED_OS.toLowerCase());
            } else {
                devicesList.add("1115fbd4746c2f05");
//                devicesList.add("458f9f58d515f647b4cb3ea7cedc68ce300f2128");
            }
        } else {
            devicesList.add("daeb0cf1");
        }
        System.out.println("Device List Size - " + devicesList.size());
        return devicesList;
    }

    private static String getURL() {
        if (GRID) {
            return cloudServer.gridURL;
        } else {
            return "http://localhost:4723/wd/hub/";
        }
    }

    private static void PrepareReportsFolders() {
        System.out.println("Preparing the reports folder");
        try {
            File reportFolder = new File(reportFolderString);
            if (!reportFolder.exists()) reportFolder.mkdir();
            for (File file : reportFolder.listFiles()) file.delete();
            System.out.println("Finished cleaning the reports folder");

            File logsFolder = new File("reports");
            if (!logsFolder.exists()) logsFolder.mkdir();
            for (File file : logsFolder.listFiles()) Utils.DeleteRecursive(file);
            System.out.println("Finished cleaning the Logs folder");

            File screenShots = new File("screenShots");
            if (!screenShots.exists()) screenShots.mkdir();
            for (File file : (screenShots.listFiles())) file.delete();
            System.out.println("Finished cleaning the ScreenShots folder");

            System.out.println("Finished preparing the reports folder");
            System.out.println("Reports will be created at - " + reportFolder.getAbsolutePath());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
