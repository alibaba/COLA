## 介绍
这是COLA规则引擎

## 使用
hello world 案例：
```java
        RuleEngine ruleEngine = new DefaultRuleEngine();
        Rule rule = new RuleBuilder()
                .name("hello world rule")
                .description("always say hello world")
                .priority(1)
                .when(facts -> true)
                .then(facts -> System.out.println("hello world"))
                .build();
        Rules rules = new Rules();
        rules.register(rule);

        ruleEngine.fire(rules, null);
```

