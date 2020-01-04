package com.example.demo;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

@RestController
public class EchoController {
    @Value("${spring.cloud.consul.discovery.instance-id}")
    private String instance;

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
    public String hello(HttpServletRequest request) throws InterruptedException {
        Enumeration<String> header = request.getHeaderNames();
        while (header.hasMoreElements()) {
            String key = header.nextElement();
            System.out.println(key + ": " + request.getHeader(key));
        }
        Thread.sleep(1000);
        return "Hello, I'm " + instance;
    }
}
