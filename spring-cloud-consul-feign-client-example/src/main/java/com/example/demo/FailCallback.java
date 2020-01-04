package com.example.demo;

import org.springframework.stereotype.Component;

@Component
public class FailCallback implements ServerProvider{
    @Override
    public String hello() {
        return "oops, fail";
    }
}
