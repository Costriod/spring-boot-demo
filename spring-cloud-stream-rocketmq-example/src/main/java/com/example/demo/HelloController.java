package com.example.demo;

import org.apache.rocketmq.client.exception.MQBrokerException;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.common.message.MessageConst;
import org.apache.rocketmq.remoting.exception.RemotingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
public class HelloController {
    @Autowired
    private Source source;
    @Autowired
    private DefaultMQProducer producer;

    @RequestMapping("/send")
    public Object send(String value, String tag) {
        Map<String, Object> headers = new HashMap<>();
        headers.put(MessageConst.PROPERTY_TAGS, tag);
        headers.put(MessageConst.PROPERTY_REAL_QUEUE_ID, 1);
        MessageHeaders messageHeaders = new MessageHeaders(headers);
        Message<String> message = MessageBuilder.createMessage(value, messageHeaders);
        source.output().send(message);
        return value;
    }

    @RequestMapping("/sendDefault")
    public Object sendDefault(String value, String tag, String topic) throws InterruptedException, RemotingException, MQClientException, MQBrokerException {
        org.apache.rocketmq.common.message.Message message = new org.apache.rocketmq.common.message.Message(topic, tag, value.getBytes());
        producer.send(message);
        return value;
    }
}
