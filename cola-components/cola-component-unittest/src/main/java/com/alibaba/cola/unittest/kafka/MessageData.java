package com.alibaba.cola.unittest.kafka;

import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.List;

@Data
@EqualsAndHashCode
@ToString
public class MessageData {
    private String topic;
    private List<ObjectNode> messages;
}
