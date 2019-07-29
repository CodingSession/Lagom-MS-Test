package de.ewks.funcdemo.repository;


import de.ewks.funcdemo.service.API_Service;
import de.ewks.funcdemo.service.REST_API_Service;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;

@Service
public class API_Collection {
    final List<API_Service> apis = new LinkedList<>();

    public API_Collection() {

        for (int i = 1; i < 11; i++) {
            for (int j = 0; j < 3; j++) {
                if (i == 10) {
                    apis.add(new REST_API_Service("http://localhost:8090"));

                } else {
                    apis.add(new REST_API_Service("http://localhost:808" + i));
                }

            }
        }
    }

    public List<API_Service> getAllServices() {
        return apis;
    }

    public void addApi(String adress) {
        apis.add(new REST_API_Service(adress));

    }

}
