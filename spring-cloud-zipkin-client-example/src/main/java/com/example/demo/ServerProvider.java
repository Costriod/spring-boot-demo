package com.example.demo;

import feign.Headers;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;

@FeignClient(value = "zipkin-server", fallback = FailCallback.class)
public interface ServerProvider {
    @RequestMapping("/hello")
    String hello();
}
