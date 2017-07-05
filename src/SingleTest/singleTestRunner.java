package SingleTest;


import FrameWork.CloudServer;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class SingleTestRunner {
    public static CloudServer cloudServer;
    public static boolean GRID = true;
    private static int index;
    public static SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
    public static int repNum = 5;
    private static String build = (""+System.currentTimeMillis()).substring(8);

    public static void main(String[] args) throws IOException {
        System.out.println("Build - "+build);
        System.out.println("Initiating connection to the cloud");
        cloudServer = new CloudServer(CloudServer.CloudServerName.MY);
        System.out.println("Cloud - \n" + cloudServer.toString());
        ExecutorService executorService = Executors.newFixedThreadPool(50);

        for (int repetitionIndex = 1; repetitionIndex < 100; repetitionIndex++) {
            System.out.println("Getting devices data from the cloud...");
            List<String> androidDevicesList = cloudServer.getAllAvailableDevices("android");
            List<String> iOSDevicesList = cloudServer.getAllAvailableDevices("ios");
            System.out.println("Finished getting devices data from the cloud");
            index = 1;
            System.out.println("------------------------------------------------------------------");
            System.out.println("STARTING ITERATION - " + repetitionIndex);
            System.out.println("------------------------------------------------------------------");
            List<Future> res = new LinkedList<>();
            for (int i = 0; i < iOSDevicesList.size(); i++) {
                iOSTest ios = new iOSTest(build);
                res.add(executorService.submit(ios));
                System.out.println("ios - submitted - " + (i + 1));
            }
            for (int i = 0; i < androidDevicesList.size(); i++) {
                AndroidTest android = new AndroidTest(build);
                res.add(executorService.submit(android));
                System.out.println("Android - submitted - " + (i + 1));
            }
            for (Future re : res) {
                try {
                    re.get();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
            }
            System.out.println("------------------------------------------------------------------");
            System.out.println("DONE ITERATION - " + repetitionIndex);
            System.out.println("------------------------------------------------------------------");
        }

    }


    public static synchronized int indexUp() {
        index++;
        return index - 1;
    }
}
