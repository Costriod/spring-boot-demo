package com.example.demo;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;

@RestController
public class EchoController {
    @GetMapping("/hello")
    public String hello(HttpServletRequest request, String name) {
        Enumeration<String> headerNames = request.getHeaderNames();
        System.out.println();
        while (headerNames.hasMoreElements()) {
            String headerName = headerNames.nextElement();
            String value = request.getHeader(headerName);
            System.out.println(headerName + ": " + value);
        }
        System.out.println();
        return "hello " + name;
    }
}
