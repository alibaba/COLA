package com.alibaba.cola.unittest.kafka;

import com.alibaba.cola.unittest.FixtureLoader;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.serialization.Serializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.junit.jupiter.api.extension.*;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.serializer.JsonSerializer;
import org.springframework.kafka.test.EmbeddedKafkaBroker;
import org.springframework.kafka.test.utils.KafkaTestUtils;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
import java.util.Map;
import java.util.Objects;

@Slf4j
public class KafkaExtension implements BeforeAllCallback, BeforeEachCallback {

    private EmbeddedKafkaBroker embeddedKafkaBroker;
    private ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void beforeAll(ExtensionContext context) throws Exception {
        try {
            EmbeddedKafkaBroker embeddedKafkaBroker = (EmbeddedKafkaBroker) SpringExtension.getApplicationContext(context).getBean(EmbeddedKafkaBroker.class);
            log.debug("embeddedKafkaBroker:" + embeddedKafkaBroker);
            this.embeddedKafkaBroker = embeddedKafkaBroker;
        } catch (NoSuchBeanDefinitionException e) {
            log.error("Please add @EmbeddedKafka for your test", e);
            throw e;
        }
    }

    @Override
    public void beforeEach(ExtensionContext context) throws Exception {
        ProduceMessage produceMessage = context.getElement().get().getAnnotation(ProduceMessage.class);
        if (Objects.nonNull(produceMessage)) {
            log.info("begin produce message for kafka");
            String location = produceMessage.value();
            MessageData messageData = objectMapper.readValue(FixtureLoader.loadResource(location), MessageData.class);
            log.debug("messageData: " + messageData);

            //get producer
            KafkaTemplate<String, JsonNode> producer = this.createProducer();

            List<ObjectNode> messages = messageData.getMessages();
            int count = 0;
            for (ObjectNode message : messages) {
                //  TODO：add key support later
                //  Optional<String> recordKey = Optional.ofNullable(record.remove("$KEY$")).map(JsonNode::asText);
                producer.send(messageData.getTopic(), message);

                log.info("produce message[{}:{}]: {}", new Object[]{messageData.getTopic(), ++count, message});
            }
        }
    }

    public <K, V> KafkaTemplate<K, V> createProducer(Serializer<K> keySerializer, Serializer<V> valueSerializer) {
        Map<String, Object> props = KafkaTestUtils.producerProps(this.embeddedKafkaBroker);
        return new KafkaTemplate(new DefaultKafkaProducerFactory(props, keySerializer, valueSerializer));
    }

    public <V> KafkaTemplate<String, V> createProducer() {
        return this.createProducer(new StringSerializer(), new JsonSerializer());
    }
}
