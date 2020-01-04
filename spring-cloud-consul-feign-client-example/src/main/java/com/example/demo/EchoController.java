package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
public class EchoController {
    @Autowired
    private ServerProvider provider;

    private static final Map<String, Object> map = new HashMap<>();
    static {
        map.put("status","up");
        map.put("server","ok");
    }

    @RequestMapping("/health")
    public Map<String, Object> health() {
        return map;
    }

    @RequestMapping("/hello")
    public String hello() {
        return provider.hello();
    }
}
