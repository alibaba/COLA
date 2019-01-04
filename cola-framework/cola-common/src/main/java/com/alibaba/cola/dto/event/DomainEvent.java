package com.alibaba.cola.dto.event;

import com.alibaba.cola.dto.event.Event;

/**
 * Domain Event (领域事件）
 *
 * 命名规则：实体名+动词的过去时态+Event
 *
 * 比如CustomerCreatedEvent 表示创建完客户发送出来的领域事件
 * 比如ContactAddedEvent 表示添加完联系人发送出来的领域事件
 * 比如OpportunityTransferredEvent 表示机会转移完发送出来的领域事件
 *
 * @author Frank Zhang
 * @date 2019-01-03 12:24 PM
 */
public abstract class DomainEvent extends Event{
}
