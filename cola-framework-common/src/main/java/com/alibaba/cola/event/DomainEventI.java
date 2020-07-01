package com.alibaba.cola.event;

/**
 * Domain Event (领域事件）
 *
 * 命名规则：实体名+动词的过去时态+Event
 *
 * 比如CustomerCreated 表示创建完客户发送出来的领域事件
 * 比如ContactAdded 表示添加完联系人发送出来的领域事件
 * 比如OpportunityTransferred 表示机会转移完发送出来的领域事件
 *
 * @author Frank Zhang
 * @date 2018-07-30 11:15 AM
 */
public interface DomainEventI extends EventI {
}
