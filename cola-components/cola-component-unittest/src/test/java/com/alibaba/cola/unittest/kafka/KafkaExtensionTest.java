package com.alibaba.cola.unittest.kafka;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.test.context.EmbeddedKafka;

import java.util.concurrent.TimeUnit;

import static org.awaitility.Awaitility.await;

@Slf4j
@SpringBootTest
@EmbeddedKafka(partitions = 1, brokerProperties = { "listeners=PLAINTEXT://localhost:9092", "port=9092" })
@ExtendWith(KafkaExtension.class)
public class KafkaExtensionTest {

    @Autowired
    private KafkaConsumer consumer;

    @Test
    @ProduceMessage("/fixture/kafka/produce-message.json")
    public void testProduceMessage(){
        log.info("test produce message");

        // 等待消息业务处理，每100毫秒poll一下，最长等待10秒
        await().atMost(10, TimeUnit.SECONDS).pollInterval(100, TimeUnit.MILLISECONDS)
                .until(() -> consumer.isFinished);

        log.info("consume message finished");
    }
}

