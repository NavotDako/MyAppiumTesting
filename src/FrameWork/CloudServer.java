package FrameWork;

import org.apache.commons.codec.binary.Base64;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class CloudServer {
    private static String webPage;
    private static String authStringEnc;
    private static String DEVICES_URL = "/devices";
    public String HOST;
    public String PORT;
    public String USER;
    public String PASS;
    public String PROJECT;
    public boolean SECURED;
    public String gridURL;
    CloudServerName cloudName;
    private String authString;
    String result;

    public static void main(String[] args) throws IOException {
        System.out.println("Initiating The Cloud Object");

        String HOST = "192.168.2.13";
        String PORT = "80";
        String USER = "admin";
        String PASS = "Experitest2012";

        String prefix = "http://";
        String authString = USER + ":" + PASS;
        String webPage = prefix + HOST + ":" + PORT + "/api/v1";
        byte[] authEncBytes = Base64.encodeBase64(authString.getBytes());
        String authStringEnc = new String(authEncBytes);
        String DEVICES_URL = "/devices";
        URL url = new URL(webPage + DEVICES_URL);
        for (int i = 0; i < 10000; i++) {

            URLConnection urlConnection = url.openConnection();
            urlConnection.setRequestProperty("Authorization", "Basic " + authStringEnc);
            InputStream is = urlConnection.getInputStream();
            InputStreamReader isr = new InputStreamReader(is);
            int numCharsRead;
            char[] charArray = new char[1024];
            StringBuffer sb = new StringBuffer();
            while ((numCharsRead = isr.read(charArray)) > 0) {
                sb.append(charArray, 0, numCharsRead);
            }
            String result = sb.toString();
            if (((HttpURLConnection) urlConnection).getResponseCode() < 300) {
                System.out.println(result);
            } else {
                throw new RuntimeException(result);
            }

            ((HttpURLConnection) urlConnection).disconnect();
        }
    }

    public CloudServer(CloudServerName cloudName) {
        System.out.println("Initiating The Cloud Object");
        this.cloudName = cloudName;
        updateCloudDetails();
        String prefix = "http://";
        if (HOST.contains("https")) prefix = "";
        gridURL = prefix + this.HOST + ":" + this.PORT + "/wd/hub/";
        authString = this.USER + ":" + this.PASS;
        webPage = prefix + this.HOST + ":" + this.PORT + "/api/v1";
        byte[] authEncBytes = Base64.encodeBase64(authString.getBytes());
        authStringEnc = new String(authEncBytes);
        System.out.println("Done Initiating The Cloud Object");
        System.out.println("Cloud Details:\n" + this.toString());
        try {
            result = doGet(DEVICES_URL);
            System.out.println(result);
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("Connection to Cloud IP - "+ HOST+ " - Is OK");
    }

    public String toString() {
        return (String.format("%-20s\n%-10s\n%-20s\n", "HOST - " + HOST, "PORT - " + PORT, "USER - " + USER));
    }

    public User getRandomUser() {
        Random r = new Random();
        int selection = r.nextInt(3);
        switch (selection) {
            case 0:
                return new User("admin", "Experitest2012");
            case 1:
                return new User("projectadmin", "Experitest2012");
            case 2:
                return new User("user", "Experitest2012");
        }
        return null;
    }

    public enum CloudServerName {
        MY, QA, MIRRON, PUBLIC, EYAl, QA_Not_Secured, MY_N_S, YEHUDA, DIKLA, ARIEL, ATT
    }

    public void updateCloudDetails() {
        switch (cloudName) {
            case MY:
                HOST = "192.168.2.13";
                PORT = "80";
                USER = "admin";
                PASS = "Experitest2012";
                PROJECT = "default";
                SECURED = false;
                break;
            case ARIEL:
                HOST = "192.168.2.108";
                PORT = "80";
                USER = "admin";
                PASS = "Experitest2012";
                break;
            case DIKLA:
                HOST = "192.168.1.59";
                PORT = "8090";
                USER = "admin";
                PASS = "Experitest2012";
                break;
            case MY_N_S:
                HOST = "192.168.2.13";
                PORT = "8090";
                USER = "admin";
                PASS = "Experitest2012";
                break;
            case YEHUDA:
                HOST = "192.168.2.31";
                PORT = "1111";
                USER = "yehuda";
                PASS = "Experitest2012";
                break;
            case QA:
                HOST = "qacloud.experitest.com";
                PORT = "443";
                USER = "zekra";
                PASS = "Zekra1234";
                break;
            case QA_Not_Secured:
                HOST = "192.168.2.135";
                PORT = "80";
                USER = "zekra";
                PASS = "Zekra1234";
                break;
            case MIRRON:
                HOST = "192.168.2.71";
                PORT = "8080";
                USER = "user1";
                PASS = "Welc0me!";
                break;
            case PUBLIC:
                HOST = "https://cloud.experitest.com";
                PORT = "443";
                USER = "dikla";
                PASS = "Experitest2012";
                break;
            case EYAl:
                HOST = "eyalneumann.experitest.local";
                PORT = "8091";
                USER = "admin";
                PASS = "Experitest2012";
                break;

            default:
                HOST = "192.168.2.13";
                PORT = "80";
                USER = "admin";
                PASS = "Experitest2012";
                break;
        }
    }

    public List<String> getAllAvailableDevices(String os) throws IOException {

        List<String> devicesList = getAvailableDevicesList(result, os);
        return devicesList;
    }

    private List<String> getAvailableDevicesList(String result, String os) {
        List<String> tempDevicesList = new ArrayList<>();
        JSONObject jsonObject = new JSONObject(result);
        Map obj = jsonObject.toMap();
        List<Object> data = (List<Object>) obj.get("data");
        Object[] devicesArray = GetFilteredDevices(data, os);

        for (int i = 0; i < devicesArray.length; i++) {
            String[] devicePropertiesArray = devicesArray[i].toString().replace("{", "").replace("]", "").split(",");
            int j = 0;

            boolean udidFlag = false;
            String udid = null;

            while (j < devicePropertiesArray.length && !udidFlag) {
                if (devicePropertiesArray[j].contains("udid")) {
                    udid = devicePropertiesArray[j].replace("udid=", "").trim();
                    udidFlag = true;
                }
                j++;
            }
            tempDevicesList.add(udid);
        }

        System.out.println("DevicesList size - " + tempDevicesList.size() + " - " + tempDevicesList.toString());
        return tempDevicesList;
    }

    private Object[] GetFilteredDevices(List<Object> data, String os) {
        Object[] devicesArray = new Object[0];
        switch (os) {
            case "android": {
                devicesArray = data
                        .stream()
                        .filter(device -> ((Map) device).get("displayStatus").equals("Available") && ((Map) device).get("deviceOs").equals("Android"))
                        .toArray();
                break;
            }
            case "ios": {
                devicesArray = data
                        .stream()
                        .filter(device -> ((Map) device).get("displayStatus").equals("Available") && ((Map) device).get("deviceOs").equals("iOS"))
                        .toArray();
                break;
            }
            case "all": {
                devicesArray = data
                        .stream()
                        .filter(device -> ((Map) device).get("displayStatus").equals("Available"))
                        .toArray();
                break;
            }
        }
        return devicesArray;
    }

    private String doGet(String entity) throws IOException {
        URL url = new URL(webPage + entity);
        URLConnection urlConnection = url.openConnection();
        urlConnection.setRequestProperty("Authorization", "Basic " + authStringEnc);
        InputStream is = urlConnection.getInputStream();
        InputStreamReader isr = new InputStreamReader(is);
        int numCharsRead;
        char[] charArray = new char[1024];
        StringBuffer sb = new StringBuffer();
        while ((numCharsRead = isr.read(charArray)) > 0) {
            sb.append(charArray, 0, numCharsRead);
        }
        String result = sb.toString();
        if (((HttpURLConnection) urlConnection).getResponseCode() < 300) {
            return result;
        } else {
            throw new RuntimeException(result);
        }
    }

    public String getDeviceOSByUDID(String UDID){
        String deviceOS = getDeviceOS(result, UDID);
        return deviceOS;
    }

    private String getDeviceOS(String result, String udid) {
        JSONObject jsonObject = new JSONObject(result);
        Map obj = jsonObject.toMap();
        List<Object> data = (List<Object>) obj.get("data");
        Object[] devicesArray = data
                .stream()
                .filter(student -> ((Map) student).get("udid").equals(udid))
                .toArray();

        String[] devicePropertiesArray = devicesArray[0].toString().replace("{", "").replace("]", "").split(",");
        int j = 0;

        boolean udidFlag = false;
        boolean osFlag = false;
        String deviceOs = null;
        while (j < devicePropertiesArray.length && (!udidFlag || !osFlag)) {

            if (devicePropertiesArray[j].contains("deviceOs")) {
                deviceOs = devicePropertiesArray[j].replace("deviceOs=", "").trim().toLowerCase();
                osFlag = true;
            }
            j++;
        }
        return deviceOs;
    }

    private String getDeviceName(String deviceID) {
        JSONObject jsonObject = new JSONObject(result);
        Map obj = jsonObject.toMap();
        List<Object> data = (List<Object>) obj.get("data");
        Object[] devicesArray = data
                .stream()
                .filter(student -> ((Map) student).get("udid").equals(deviceID))
                .toArray();

        String[] devicePropertiesArray = devicesArray[0].toString().replace("{", "").replace("]", "").split(",");
        int j = 0;

        boolean udidFlag = false;
        boolean osFlag = false;
        String deviceOs = null;
        while (j < devicePropertiesArray.length && !osFlag) {

            if (devicePropertiesArray[j].contains("deviceName")) {
                deviceOs = devicePropertiesArray[j].substring(devicePropertiesArray[j].indexOf("=") + 1).trim().toLowerCase();
                osFlag = true;
            }
            j++;
        }
        return deviceOs;
    }

    public String getDeviceNameByUDID(String deviceID) {
        String deviceOS = getDeviceName(deviceID);
        return deviceOS;
    }

}
