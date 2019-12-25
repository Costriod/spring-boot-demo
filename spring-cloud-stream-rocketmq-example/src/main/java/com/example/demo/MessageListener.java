package com.example.demo;

import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

@Component
public class MessageListener {
    @StreamListener("input1")
    public void input1(Message<String> message) {
        System.out.println("input1 header: " + message.getHeaders().get("rocketmq_QUEUE_ID"));
        System.out.println("input1 payload: " + message.getPayload());
    }

    @StreamListener("input2")
    public void input2(Message<String> message) {
        System.out.println("input2 header: " + message.getHeaders().get("rocketmq_QUEUE_ID"));
        System.out.println("input2 payload: " + message.getPayload());
    }
}
