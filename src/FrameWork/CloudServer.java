package FrameWork;

import org.apache.commons.codec.binary.Base64;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CloudServer {
    private static String webPage;
    private static String authStringEnc;
    private static String DEVICES_URL = "/devices";
    private String HOST;
    private String PORT;
    public String USER;
    public String PASS;
    public String gridURL;
    CloudServerName cloudName;
    private String authString;
    String result;

    public CloudServer(CloudServerName cloudName) {
        this.cloudName = cloudName;
        updateCloudDetails();
        gridURL = "http://" + HOST + ":" + PORT + "/wd/hub/";
        authString = this.USER + ":" + this.PASS;
        webPage = "http://" + this.HOST + ":" + this.PORT + "/api/v1";
        byte[] authEncBytes = Base64.encodeBase64(authString.getBytes());
        authStringEnc = new String(authEncBytes);
        try {
            init();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("Cloud Details:\n" + this.toString());
    }

    public String toString() {
        return (String.format("%-20s\n%-10s\n%-20s\n", "HOST - " + HOST, "PORT - " + PORT, "USER - " + USER));
    }

    public void init() throws IOException {
        System.out.println("Initiating The Cloud Object");
        result = doGet(DEVICES_URL);
        System.out.println("Done Initiating The Cloud Object");

    }

    public String getDeviceNameByUDID(String deviceID) throws IOException {
        String deviceOS = getDeviceName(result, deviceID);
        return deviceOS;
    }

    private String getDeviceName(String result, String deviceID) {
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

    public enum CloudServerName {
        MY, QA, MIRRON, ATT
    }

    public void updateCloudDetails() {
        switch (cloudName) {
            case MY:
                HOST = "192.168.2.13";
                PORT = "80";
                USER = "admin";
                PASS = "Experitest2012";
                break;
            case QA:
                HOST = "qacloud.experitest.com";
                PORT = "443";
                USER = "zekra";
                PASS = "Zekra123";
                break;
            case MIRRON:
                HOST = "192.168.2.71";
                PORT = "8080";
                USER = "user1";
                PASS = "Welc0me!";
                break;
            default:
                HOST = "192.168.2.13";
                PORT = "80";
                USER = "admin";
                PASS = "Experitest2012";
                break;
        }
    }

    public String getDeviceOSByUDID(String UDID) throws IOException {
        String deviceOS = getDeviceOS(result, UDID);
        return deviceOS;
    }

    public List<String> getAllAvailableDevices(String os) throws IOException {
        List<String> devicesList = getAvailableDevicesList(result, os);
        return devicesList;
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

        System.out.println("DevicesList size - "+tempDevicesList.size()+" - "+tempDevicesList.toString());
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
}
