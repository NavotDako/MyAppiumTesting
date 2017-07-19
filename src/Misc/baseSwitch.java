package Misc;

import java.util.regex.Pattern;

/**
 * Created by navot.dako on 7/4/2017.
 */
public class baseSwitch {

    public static void main(String[] args) {

        //  System.out.println("\nResult: " + decSwitch("1c.1", 16));
        System.out.println("\nResult: " + noDecSwitch(123.1, 3));
    }

    private static String noDecSwitch(double decNum, int newBase) {
        int decNumInt = ((int) decNum);
        float afterDot = (float) (decNum - decNumInt);

        int i;
        for (i = 0; Math.pow(newBase, i) < decNumInt; i++) {
        }
        double tempNum = decNumInt;
        String result = "";
        while (tempNum > 0 || i > 0) {
            int c = (int) (tempNum / Math.pow(newBase, i - 1));
            result += "" + c;
            tempNum -= Math.pow(newBase, i - 1) * c;
            i--;
        }


        return result;
    }

    private static double decSwitch(String num, int base) {

        int dotSpot = -1;
        try {
            dotSpot = num.indexOf(".");
        } catch (Exception e) {
        }

        String tempNum = num.replace(".", "");
        int startPow = num.length();

        if (dotSpot > 0) {
            String[] parts = num.split(Pattern.quote("."));
            startPow = parts[0].length();
        }

        double sum = 0;
        for (int i = 0; i < tempNum.length(); i++) {
            sum += getInt(tempNum.charAt(i)) * Math.pow(base, startPow - i - 1);
            System.out.println(tempNum.charAt(i) + " * " + base + "^" + (startPow - i - 1));
        }
        return sum;
    }

    private static int getInt(char c) {
        int result = 0;
        try {
            result = Integer.parseInt(String.valueOf(c));
        } catch (Exception e) {
            System.out.println("char " + c + " - " + (int) c);
            System.out.println("Will use - " + c + " = " + ((int) c - 87));
            result = ((int) c) - 87;
        }
        return result;
    }


}
