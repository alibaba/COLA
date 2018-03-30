# COPA
> COPA是Collaborative Open Platform Architecture的缩写，是轻量级的企业应用框架，可以有效的降低业务系统复杂度。

关于架构和设计的详细内容，请查看
- COPA架构：http://blog.csdn.net/significantfrank/article/details/79286947
- 领域建模：http://blog.csdn.net/significantfrank/article/details/79614915
- 需要进一步交流的，也可以加我的微信 25216348

# 项目说明
COPA框架包括两个Project，一个是sofa-framework里面是框架的核心代码，另一个是sofa-archetype是用来生成新应用的Maven Archetype源码。
## sofa-framework Project
该Project包含3个Module，sofa-core, sofa-common, sofa-test
### sofa-core
该Module是整个框架的核心，里面的主要功能和Package如下：
```
com
└── alibaba
    └── sofa
        ├── assembler  \\提供Assembler标准
        ├── boot \\这是框架的核心启动包，负责框架组件的注册、发现
        ├── command  \\提供Command标准
        ├── common
        ├── context  \\提供框架执行所需要的上下文
        ├── convertor  \\提供Convertor标准
        ├── domain  \\提供Domain Entity标准
        ├── event
        ├── exception \\提供Exception标准
        ├── extension  \\负责扩展机制中的重要概念-扩展(Extension)的定义和执行
        ├── extensionpoint  \\负责扩展机制中的重要概念-扩展点(Extension Point)的定义
        ├── logger  \\提供DIP的日志接口
        ├── repository  \\提供仓储（Repository）的标准
        ├── rule  \\提供业务规则或者叫策略（Rule）的标准和执行
        │   └── ruleengine
        └── validator  \\提供Validator标准和执行
```
### sofa-common
该Module提供了框架中Client Object, Entity Object和Data Object的定义，二方库会依赖该Module。
### sofa-test  
该Module主要是提供一些开发测试的工具，可以使用TDD来进行开发。

## sofa-archetype Project
该Project下面是Archetype的源码，先执行`mvn install`，然后就可以用下面的命令来创建新应用：
```
mvn archetype:generate  -DgroupId=com.alibaba.crm -DartifactId=demo -Dversion=1.0.0-SNAPSHOT -Dpackage=com.alibaba.crm.demo -DarchetypeArtifactId=sofa-framework-archetype -DarchetypeGroupId=com.alibaba.sofa -DarchetypeVersion=1.0.0-SNAPSHOT
```
生成的应用主要包括demo-app, demo-domain, demo-infrastructure, demo-client和Start五个Module，分别代表不同层次（Tier）和用途。
```
├── demo-app  \\这个是Application层
│   └── src
│       └── main
│           └── java
│               └── com
│                   └── alibaba
│                       └── crm
│                           └── demo
│                               ├── assembler
│                               ├── command
│                               │   ├── extension
│                               │   ├── extensionpoint
│                               │   └── query
│                               ├── event
│                               │   └── handler
│                               ├── interceptor
│                               ├── service
│                               └── validator
│                                   ├── extension
│                                   └── extensionpoint
├── demo-domain \\这个是Domain层，所有的业务逻辑都应该在这个Module里面
│   └── src
│       └── main
│           └── java
│               └── com
│                   └── alibaba
│                       └── crm
│                           └── demo
│                               └── domain
│                                   └── customer
│                                       ├── convertor
│                                       │   ├── extension
│                                       │   └── extensionpoint
│                                       ├── entity
│                                       ├── factory
│                                       ├── repository
│                                       ├── rule
│                                       │   ├── extension
│                                       │   └── extensionpoint
│                                       └── valueobject
├── demo-infrastructure \\\\这个是infrastructure层，也就是仓储(tunnul), 配置（config）和通用（common）
│   └── src
│       ├── main
│           ├── java
│           │   └── com
│           │       └── alibaba
│           │           └── crm
│           │               └── demo
│           │                   ├── common
│           │                   ├── config
│           │                   └── tunnel
│           │                       ├── dataobject
│           │                       └── datatunnel
│           │                           └── impl
│           └── resources
├── demo-client \\这个是二方库，提供给Consumer做RPC调用用的
│   └── src
│       └── main
│           └── java
│               └── com
│                   └── alibaba
│                       └── crm
│                           └── demo
│                               ├── api
│                               └── dto
│                                   └── clientobject
└── start \\这个是应用的启动Module，通常是用SpringBoot，如果是阿里系的话，通常是PandoraBoot
    └── src
        └── main
            └── java
                └── com
                    └── alibaba
                        └── crm
                            └── demo
```
