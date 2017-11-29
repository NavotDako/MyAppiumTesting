package SeeTestFramework;

import FrameWork.Runner;
import SeetestTests.SeeTest_EriBank;


public class SeeTestSuite implements Runnable {

    public String deviceID = "";

    public SeeTestSuite(String deviceID) {

        this.deviceID = deviceID;

    }

    @Override
    public void run() {

        for (int i = 0; i < Runner.REP_NUM; i++) {
            new SeeTest_EriBank(deviceID, "EriBank", i + 1);
            //new SeeTest_NonInstrumented(deviceID, "Non-Instrumented",i);
        }

        System.out.println("----------------------------------------- DONE WITH " + deviceID + "-----------------------------------------");
    }
}
