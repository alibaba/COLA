## 原理
通过注解AOP，提供service级别的logging和exception处理。

通过Spring Boot的autoConfig机制进行加载，无需手动配置，只需要添加如下依赖即可：
```xml
        <dependency>
            <groupId>com.alibaba.lst.tech.shared</groupId>
            <artifactId>catch-log-starter</artifactId>
        </dependency>
```

兼容普通的HSF service和Mtop service，具体做法可以查看代码`ResponseHandler`
```java
public class ResponseHandler {

    public static Object handle(Class returnType, String errCode, String errMsg){
        if (isColaResponse(returnType)){
            return handleColaResponse(returnType, errCode, errMsg);
        }
        if(isMtopResponse(returnType)){
            return handleMtopResponse(returnType, errCode, errMsg);
        }
        return null;
    }
    ...
  }

```


## 使用介绍
1、在需要处理的Service类上面加上@CatchAndLog注解
```java
@CatchAndLog
public class GrouponServiceImpl implements GrouponService 
```

2、logback-test.xml为组件开启DEGUG level的日志输出
```xml
   <!--这个是统一异常处理，日志记录组件的日志-->
    <logger name="com.alibaba.lst.tech.shared" level="DEBUG"/>
```

3、如果在控制台看到如下的日志输出，说明CatchAndLog已经在做AOP拦截
```xml
DEBUG c.a.l.t.s.catchlog.CatchLogAspect - Start processing: GrouponServiceImpl.queryGrouponItemDetail(..)
DEBUG c.a.l.t.s.catchlog.CatchLogAspect - REQUEST : 257

DEBUG c.a.l.t.s.catchlog.CatchLogAspect - RESPONSE : {"errCode":"UNKNOWN_ERROR"...}
DEBUG c.a.l.t.s.catchlog.CatchLogAspect - COST : 1329ms
```

