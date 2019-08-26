package com.alibaba.craftsman.dto.domainevent;

import com.alibaba.cola.event.DomainEventI;
import lombok.Data;

@Data
public class MetricItemCreatedEvent implements DomainEventI {

    private String id;

    private String userId;

    private String mainMetricType;
}
