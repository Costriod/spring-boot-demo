package com.example.demo;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;

@FeignClient(value = "feign-server", fallback = FailCallback.class)
public interface ServerProvider {
    @RequestMapping("/hello")
    String hello();
}
