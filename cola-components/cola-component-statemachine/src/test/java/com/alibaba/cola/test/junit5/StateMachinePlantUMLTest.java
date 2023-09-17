package com.alibaba.cola.test.junit5;

import com.alibaba.cola.statemachine.Action;
import com.alibaba.cola.statemachine.Condition;
import com.alibaba.cola.statemachine.StateMachine;
import com.alibaba.cola.statemachine.builder.StateMachineBuilder;
import com.alibaba.cola.statemachine.builder.StateMachineBuilderFactory;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.stream.Stream;

import static com.alibaba.cola.test.junit5.StateMachinePlantUMLTest.PriceAdjustmentTaskEventEnum.*;
import static com.alibaba.cola.test.junit5.StateMachinePlantUMLTest.PriceAdjustmentTaskStatusEnum.*;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * StateMachinePlantUMLTest
 *
 * @author cookie lin
 * @date 2023-03-08 7:53 PM
 */
public class StateMachinePlantUMLTest {

    enum PriceAdjustmentTaskStatusEnum {
        /**
         * 开始状态
         */
        None,
        /**
         * 待商家处理
         */
        Supplier_Processing,
        /**
         * 待控商小二处理
         */
        Supplier_Manager_Processing,
        /**
         * 待价格管控小二处理
         */
        Price_Manager_Processing,
        /**
         * 退出
         */
        Closed
    }

    enum PriceAdjustmentTaskEventEnum {

        // 系统事件
        Create,
        Normal_Update,
        /**
         * 合理价变更
         */
        P0_Changed,
        /**
         * 页面价变合理
         */
        Page_Price_changed,

        // 商家事件
        Supplier_Reject,
        Supplier_Agree,
        Supplier_Timeout,

        // 控商小二事件
        Apply_Over_P0_Sell,

        // 价格小二事件
        Agree_Over_P0_Sell,
        Reject_Over_P0_Sell;

    }

    @Test
    @DisplayName("test generated content by plantuml")
    public void testPlantUML(){
        StateMachineBuilder<PriceAdjustmentTaskStatusEnum, PriceAdjustmentTaskEventEnum, StateMachineTest.Context> builder = StateMachineBuilderFactory.create();

        builder.externalTransition()
                .from(None)
                .to(Supplier_Processing)
                .on(Create)
                .when(checkCondition())
                .perform(doAction());

        // 商家调价
        Stream.of(Supplier_Processing, Supplier_Manager_Processing, Price_Manager_Processing)
                .forEach(status ->
                        builder.externalTransition()
                                .from(status)
                                .to(Closed)
                                .on(Supplier_Agree)
                                .when(checkCondition())
                                .perform(doAction())
                );

        // 商家 -上升至-> 控商小二
        builder.externalTransition()
                .from(Supplier_Processing)
                .to(Supplier_Manager_Processing)
                .on(Supplier_Reject)
                .when(checkCondition())
                .perform(doAction());

        builder.externalTransition()
                .from(Supplier_Processing)
                .to(Supplier_Manager_Processing)
                .on(Supplier_Timeout)
                .when(checkCondition())
                .perform(doAction());

        // 申请申请高于P0售卖
        builder.externalTransition()
                .from(Supplier_Manager_Processing)
                .to(Price_Manager_Processing)
                .on(Apply_Over_P0_Sell)
                .when(checkCondition())
                .perform(doAction());

        // 同意高于P0价售卖
        builder.externalTransition()
                .from(Price_Manager_Processing)
                .to(Closed)
                .on(Agree_Over_P0_Sell)
                .when(checkCondition())
                .perform(doAction());

        // 拒绝高于P0价售卖
        builder.externalTransition()
                .from(Price_Manager_Processing)
                .to(Supplier_Manager_Processing)
                .on(Reject_Over_P0_Sell)
                .when(checkCondition())
                .perform(doAction());

        // 普通字段更新事件
        Stream.of(Supplier_Processing, Supplier_Manager_Processing, Price_Manager_Processing)
                .forEach(status -> builder
                        .internalTransition()
                        .within(status)
                        .on(Normal_Update)
                        .when(checkCondition())
                        .perform(doAction())
                );

        // P0价变更事件、页面价高于合理价事件
        Stream.of(P0_Changed, Page_Price_changed)
                .forEach(event -> builder.externalTransitions()
                        .fromAmong(Supplier_Processing, Supplier_Manager_Processing, Price_Manager_Processing)
                        .to(Closed)
                        .on(event)
                        .when(checkCondition())
                        .perform(doAction()));

        StateMachine<PriceAdjustmentTaskStatusEnum, PriceAdjustmentTaskEventEnum, StateMachineTest.Context> stateMachine = builder.build("AdjustPriceTask");
        String plantUML = stateMachine.generatePlantUML();

        String[] expected = new String[] {
                "@startuml",
                "Supplier_Processing --> Closed : Supplier_Agree",
                "Supplier_Processing --> Closed : P0_Changed",
                "Supplier_Processing --> Supplier_Manager_Processing : Supplier_Timeout",
                "Supplier_Processing --> Closed : Page_Price_changed",
                "Supplier_Processing --> Supplier_Processing : Normal_Update",
                "Supplier_Processing --> Supplier_Manager_Processing : Supplier_Reject",
                "Price_Manager_Processing --> Closed : Supplier_Agree",
                "Price_Manager_Processing --> Closed : P0_Changed",
                "Price_Manager_Processing --> Supplier_Manager_Processing : Reject_Over_P0_Sell",
                "Price_Manager_Processing --> Closed : Page_Price_changed",
                "Price_Manager_Processing --> Closed : Agree_Over_P0_Sell",
                "Price_Manager_Processing --> Price_Manager_Processing : Normal_Update",
                "None --> Supplier_Processing : Create",
                "Supplier_Manager_Processing --> Supplier_Manager_Processing : Normal_Update",
                "Supplier_Manager_Processing --> Closed : Page_Price_changed",
                "Supplier_Manager_Processing --> Closed : Supplier_Agree",
                "Supplier_Manager_Processing --> Price_Manager_Processing : Apply_Over_P0_Sell",
                "Supplier_Manager_Processing --> Closed : P0_Changed",
                "@enduml"
        };

        for (String s : expected) {
            assertTrue(plantUML.contains(s));
        }

    }

    private Condition<StateMachineTest.Context> checkCondition() {
        return (ctx) -> true;
    }

    private Action<PriceAdjustmentTaskStatusEnum, PriceAdjustmentTaskEventEnum, StateMachineTest.Context> doAction() {
        return (from, to, event, ctx)-> System.out.println(ctx.operator+" is operating "+ctx.entityId+" from:"+from+" to:"+to+" on:"+event);
    }
}
