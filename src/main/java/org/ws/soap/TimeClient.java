package org.ws.soap;

import org.ws.soap.client.TimeService;
import org.ws.soap.client.TimeServiceImplService;

import java.net.MalformedURLException;

public class TimeClient {
    public static void main(String[] args) throws MalformedURLException {
        TimeServiceImplService timeServiceImplService = new TimeServiceImplService();
        TimeService timeServiceImplPort = timeServiceImplService.getTimeServiceImplPort();
        System.out.println(timeServiceImplPort.getServerTime());
//        URL url = new URL("http://127.0.0.1:8080/ts?wsdl");
//
//        QName qName = new QName("http://soap.ws.org/", "TimeServiceImplService");
//        Service service = Service.create(url, qName);
//        TimeService timeService = service.getPort(TimeService.class);
//        String serverTime = timeService.getServerTime();
//        System.out.println("Time on server: " + serverTime);
    }
}
