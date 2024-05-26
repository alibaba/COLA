package com.alibaba.cola.unittest.kafka;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class KafkaConsumer {


    private String payload;
    boolean isFinished;

    @KafkaListener(topics = "${test.topic}", groupId = "testGroup")
    public void receive(ConsumerRecord<?, ?> consumerRecord) {
        log.info("received payload='{}'", consumerRecord.toString());
        payload = consumerRecord.toString();

        processBiz();

        isFinished = true;
    }

    private void processBiz() {
        try {
            Thread.sleep(300);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }


    public String getPayload() {
        return payload;
    }

}

