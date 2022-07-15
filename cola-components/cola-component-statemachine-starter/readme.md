## COLA-COMPONENT-STATEMACHINE-STARTER

[]: # Language: markdown
[]: # Path: cola-components/cola-component-statemachine-starter/readme.md

### 使用方式

1. 在项目中引入cola-component-statemachine-starter
2. 在启动类上添加@EnableStateMachine 注解
3. 新建actionBean，实现Action接口和Condition接口
4. bean 上添加注解
      ```java
        @StateMachine(
            from =  "STARTED",
            to =  "FINISHED",
            on = "DEMO",
            machineId = "DEMO")
      ```
      from,to,on 三个参数的参数类型值全部取值于Action接口的泛型参数类型值，from，to取值于第一个参数的类型 on 取值于第二个参数的类型。
      为了防止书写等问题导致无法启动，from，to, on 的值都是从枚举中获取,强制取值为枚举的name()方法,

5. 调用过程
    ```java
      StateMachineFactory.<STATUS, EVENT, MODEL>get(DEMO)
                        .fireEvent(typeEnums, taskTypeEnum, model);   
    ```
    先根据machineId常量获取对应的状态机实例,然后执行fireEvent方法,第一个参数表示from变化前状态,第二个参数on表示在什么场景下触发,第三个参数传入model实体
    > 详细可见测试类
