package com.clouify.notificationservice;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class NotificationListener {

    private static final Logger log = LoggerFactory.getLogger(NotificationListener.class);

    @KafkaListener(topics = {"stock-notification", "order-notification"})
    public void listen(ConsumerRecord<String, String> record) {
        log.info("Received message: {} from topic: {}", record.value(), record.topic());
    }
}
