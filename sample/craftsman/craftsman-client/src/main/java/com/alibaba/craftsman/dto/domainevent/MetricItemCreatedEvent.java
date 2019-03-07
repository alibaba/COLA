package com.alibaba.craftsman.dto.domainevent;

import com.alibaba.cola.dto.event.DomainEvent;
import lombok.Data;

@Data
public class MetricItemCreatedEvent extends DomainEvent {

    private String id;

    private String userId;

    private String mainMetricType;
}
