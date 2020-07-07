# COLA 3.0架构
<strong>COLA是Clean Object-Oriented and Layered Architecture的缩写，代表“整洁面向对象分层架构”，也叫“可乐”架构，目前COLA已经发展到[COLA 3.0](https://blog.csdn.net/significantfrank/article/details/106976804)。</strong>  需要访问老版本，请使用[COLA 2.0 TAG](https://github.com/alibaba/COLA/tree/COLA2.0), [COLA 1.0 TAG](https://github.com/alibaba/COLA/tree/COLA1.0)

关于COLA的更多信息，请关注微信公众号：

![qrcode_60.jpg](https://ata2-img.cn-hangzhou.oss-pub.aliyun-inc.com/9434d30a2db4c6036e1ba37be55b2c6e.jpg)

如果你有技术热情，对阿里有兴趣，可以email：fulan.zjf@alibaba-inc.com

# 项目说明
**COLA既是框架，也是架构。创建COLA的主要目的是为应用架构提供一套简单的可以复制、可以理解、可以落地、可以控制复杂性的”指导和约束"。**
- 架构部分主要是提供了创建符合COLA要求的应用Archetype。
- 框架部分主要是以二方库的形式被应用依赖和使用。

## COLA架构
COLA首先是作为架构的存在，是一种应用架构思想，主要是制定了一套指导和约束，并将这套规范沉淀成Archetype。以便通过Archetype可以快速的生成符合COLA规范的应用。满足COLA的应用是一个有清晰的依赖关系的分层架构，如下图所示：

![image.png](https://ata2-img.cn-hangzhou.oss-pub.aliyun-inc.com/a33b80bcac5ec73d0d1358d6b49a119c.png)

我们提供了两个Archetype，分别是cola-archetype-service和cola-archetype-web

### cola-archetype-service
用来生成纯后端应用（没有Controller），生成应用的命令为：
```
mvn archetype:generate  -DgroupId=com.alibaba.demo -DartifactId=demoService -Dversion=1.0.0-SNAPSHOT -Dpackage=com.alibaba.demo -DarchetypeArtifactId=cola-framework-archetype-service -DarchetypeGroupId=com.aliyun -DarchetypeVersion=3.0.0
```

### cola-archetype-web
用来生成Web后端应用（有Controller），生成应用的命令为：
```
mvn archetype:generate  -DgroupId=com.alibaba.demo -DartifactId=demoWeb -Dversion=1.0.0-SNAPSHOT -Dpackage=com.alibaba.demo -DarchetypeArtifactId=cola-framework-archetype-web -DarchetypeGroupId=com.aliyun -DarchetypeVersion=3.0.0
```

## COLA框架
其次，COLA也是一个框架，提供了扩展点功能组件和一般API定义规范组件。如果你打算使用COLA提供的扩展点功能。
你需要在项目里面依赖两个组件：[cola-core](https://oss.sonatype.org/#nexus-search;quick~cola-core), [cola-common](https://oss.sonatype.org/#nexus-search;quick~cola-common)。

关于组件引用方式，你可以下载源码在本地生成，也可以从nexus的中央仓库获取。下面简单介绍一下这两个组件：

### cola-framework-core
该Module是整个框架的核心，里面的主要功能和Package如下：
```
com
└── alibaba
    └── cola
        ├── boot \\这是框架的核心启动包，负责框架组件的注册、发现
        ├── common
        ├── domain  \\提供Domain Entity标准
        ├── event
        ├── exception \\提供Exception标准
        ├── extension  \\负责扩展机制中的重要概念-扩展(Extension)的定义和执行
        ├── logger  \\提供DIP的日志接口
```
### cola-framework-common
该Module提供了框架中Client Object, Entity Object和Data Object的定义，二方库会依赖该Module。



# 如何使用COLA

## 第一步：生成COLA应用
**1、使用Archetype生成应用：**

直接运行上面提供的Archetype命令就可以生成应用，如果你的Remote Maven Repository里面没有Archetype的Jar包，也可以自己下载Archetype到本地，然后本地运行 mvn install安装。

**2、 检查应用里的模块和组件：**

如果命令执行成功的话，我们可以看到如下的代码结构，**它们就是COLA应用架构。**
![image.png](https://ata2-img.cn-hangzhou.oss-pub.aliyun-inc.com/27569bf9d656f89a32e18d9ef15c85c6.png)

## 第二步：运行Demo
**1、进入在第一步中生成的应用目录。**

**2、启动SpringBoot：**

首先在demoWeb目录下运行mvn install（如果不想运行测试，可以加上-DskipTests参数）。然后进入start目录，执行mvn spring-boot:run。
运行成功的话，可以看到SpringBoot启动成功的界面。

**3、 执行测试：**

生成的应用中，已经实现了一个简单的Rest请求，可以在浏览器中输入 http://localhost:8080/customer?name=World 进行测试。

**4、查看运行日志：**

请求执行成功的话，可以在浏览器中的返回值中看到："customerName":"Hello, World"。同时观察启动SpringBoot的控制台，可以看到LoggerInterceptor打印出来的日志。
