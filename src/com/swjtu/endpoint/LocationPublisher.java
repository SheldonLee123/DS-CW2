package com.swjtu.endpoint;

import javax.xml.ws.Endpoint;
import com.swjtu.ws.LocationImpl;

//Endpoint publisher
public class LocationPublisher {

    public static void main(String[] args) {
        Endpoint.publish("http://localhost:8888/ws/location", new LocationImpl());
    }

}
