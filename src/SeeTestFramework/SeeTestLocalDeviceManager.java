package SeeTestFramework;

import com.experitest.client.Client;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SeeTestLocalDeviceManager {

    NodeList deviceNodeList;

    public SeeTestLocalDeviceManager(String host, int port) {
        Client client = new Client(host, port, true);
        String devicesXml = client.getDevicesInformation();
        client.releaseClient();
        try {
            deviceNodeList = parseDocument(devicesXml);
        } catch (Exception e) {
            System.out.println("Couldn't parse connected devices list");
            System.exit(0);
        }
    }

    private NodeList parseDocument(String devicesXml) throws ParserConfigurationException, IOException, SAXException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        StringBuilder xmlStringBuilder = new StringBuilder();
        xmlStringBuilder.append(devicesXml);
        ByteArrayInputStream input = new ByteArrayInputStream(xmlStringBuilder.toString().getBytes("UTF-8"));
        Document doc = builder.parse(input);
        doc.getDocumentElement().normalize();
        System.out.println("Root element :"
                + doc.getDocumentElement().getNodeName());
        return doc.getElementsByTagName("device");
    }

    private List<String> getDeviceList(String requestedOS) throws ParserConfigurationException, IOException, SAXException {
        List<String> devicesList = new ArrayList<>();
        for (int temp = 0; temp < deviceNodeList.getLength(); temp++) {
            Node nNode = deviceNodeList.item(temp);
            if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                Element eElement = (Element) nNode;
                if (eElement.getAttribute("remote").contains("false") || eElement.getAttribute("status").contains("unreserved online")) {
                    if (eElement.getAttribute("os").contains(requestedOS)) {
                        String serialnumber = eElement.getAttribute("serialnumber");
                        devicesList.add(eElement.getAttribute("serialnumber"));
                    }
                }

            }
        }
        return devicesList;
    }

    public String getDeviceOSByUDID(String deviceID) {
        for (int temp = 0; temp < deviceNodeList.getLength(); temp++) {
            Node nNode = deviceNodeList.item(temp);
            if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                Element eElement = (Element) nNode;
                if (eElement.getAttribute("serialnumber").contains(deviceID)) {
                    return eElement.getAttribute("os");
                }
            }
        }
        return null;
    }
    public String getDeviceNameByUDID(String deviceID) {
        for (int temp = 0; temp < deviceNodeList.getLength(); temp++) {
            Node nNode = deviceNodeList.item(temp);
            if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                Element eElement = (Element) nNode;
                if (eElement.getAttribute("serialnumber").contains(deviceID)) {
                    return eElement.getAttribute("name");
                }
            }
        }
        return null;
    }
}