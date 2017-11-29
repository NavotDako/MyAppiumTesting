package Misc;

import org.junit.runner.JUnitCore;
import org.junit.runner.Result;

/**
 * Created by navot.dako on 10/25/2017.
 */
public class main {
    public static void main(String[] args) {
        runThreads rt = null;
        for (int i = 0; i < 10; i++) {
            rt = new runThreads();
            Thread t = new Thread(rt);
            t.start();
        }

    }
}

class runThreads extends Thread {
    public runThreads() {

    }

    @Override
    public void run() {
        JUnitCore junit = new JUnitCore();
        Result result = junit.run();
    }
}