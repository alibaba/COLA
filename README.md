# COLA 3.0架构
<strong>COLA是Clean Object-Oriented and Layered Architecture的缩写，表示“整洁面向对象分层架构”，目前COLA已经发展到[COLA 3.0](https://blog.csdn.net/significantfrank/article/details/106976804)。</strong>  需要访问老版本，请使用[COLA 2.0 TAG](https://github.com/alibaba/COLA/tree/COLA2.0), [COLA 1.0 TAG](https://github.com/alibaba/COLA/tree/COLA1.0)

关于COLA的更多信息，请关注微信公众号：

![qrcode_60.jpg](https://img-blog.csdnimg.cn/2020110314110321.png#pic_center)

如果你有技术热情，对阿里有兴趣，可以email：fulan.zjf@alibaba-inc.com

# 阿里云COLA应用生成器
https://start.aliyun.com/bootstrap.html
![image.png](https://imgconvert.csdnimg.cn/aHR0cHM6Ly9hdGEyLWltZy5vc3MtY24temhhbmdqaWFrb3UuYWxpeXVuY3MuY29tLzQ4Zjg3MGI5OGQ2Y2VhN2YwNjg2NGUxNzM4N2UwNzE3LnBuZw?x-oss-process=image/format,png)

# 项目说明
**COLA既是框架，也是架构。创建COLA的主要目的是为应用架构提供一套简单的可以复制、可以理解、可以落地、可以控制复杂性的”指导和约束"。**
- 架构部分主要是提供了创建符合COLA要求的应用Archetype。
- 框架部分主要是以二方库的形式被应用依赖和使用。

## COLA架构
COLA首先是作为架构的存在，是一种应用架构思想，主要是制定了一套指导和约束，并将这套规范沉淀成Archetype。以便通过Archetype可以快速的生成符合COLA规范的应用。满足COLA的应用是一个有清晰的依赖关系的分层架构，如下图所示：

![image.png](https://img-blog.csdnimg.cn/20201103141447661.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3NpZ25pZmljYW50ZnJhbms=,size_16,color_FFFFFF,t_70#pic_center)

我们提供了两个Archetype，分别是cola-archetype-service和cola-archetype-web

### cola-archetype-service
用来生成纯后端应用（没有Adapter），生成应用的命令为：
```
mvn archetype:generate  -DgroupId=com.alibaba.demo -DartifactId=demoService -Dversion=1.0.0-SNAPSHOT -Dpackage=com.alibaba.demo -DarchetypeArtifactId=cola-framework-archetype-service -DarchetypeGroupId=com.aliyun -DarchetypeVersion=3.1.0
```

### cola-archetype-web
用来生成Web后端应用（有Adapter），生成应用的命令为：
```
mvn archetype:generate  -DgroupId=com.alibaba.demo -DartifactId=demoWeb -Dversion=1.0.0-SNAPSHOT -Dpackage=com.alibaba.demo -DarchetypeArtifactId=cola-framework-archetype-web -DarchetypeGroupId=com.aliyun -DarchetypeVersion=3.1.0
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
        ├── exception \\提供Exception标准
        ├── extension  \\负责扩展机制中的重要概念-扩展(Extension)的定义和执行
        ├── logger  \\提供DIP的日志接口
```
### cola-framework-common
该Module提供了应用中通用DTO, Exception的定义，二方库会依赖该Module。


# 如何使用COLA

## 第一步：生成COLA应用
**1、使用Archetype生成应用：**

直接运行上面提供的Archetype命令就可以生成应用，默认，你可以在中央仓库获取到[cola-archetype-service](https://oss.sonatype.org/#nexus-search;quick~cola-framework-archetype-service) 和 [cola-archetype-web](https://oss.sonatype.org/#nexus-search;quick~cola-framework-archetype-web)

如果downloading有问题，也可以自己下载Archetype源码到本地，然后本地运行mvn install安装。

**2、 检查应用里的模块和组件：**

如果命令执行成功的话，我们可以看到如下的代码结构：
![image.png](https://img-blog.csdnimg.cn/20201103141618978.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3NpZ25pZmljYW50ZnJhbms=,size_16,color_FFFFFF,t_70#pic_center)

## 第二步：运行Demo
**1、进入在第一步中生成的应用目录。**

**2、启动SpringBoot：**

首先在demoWeb目录下运行mvn install（如果不想运行测试，可以加上-DskipTests参数）。然后进入start目录，执行mvn spring-boot:run。
运行成功的话，可以看到SpringBoot启动成功的界面。

**3、 执行测试：**

生成的应用中，已经实现了一个简单的Rest请求，可以在浏览器中输入 http://localhost:8080/helloworld 进行测试。

**4、查看运行日志：**

请求执行成功的话，可以在浏览器中的返回值中看到："Hello, welcome to cola world"。


# 版本迭代
## 1.0.0 版本
https://blog.csdn.net/significantfrank/article/details/85785565

## 2.0.0 版本
https://blog.csdn.net/significantfrank/article/details/100074716

## 3.0.0 版本
https://blog.csdn.net/significantfrank/article/details/106976804

## 3.0.1 版本
之前的扩展点在locate扩展实现的时候，没有寻找默认实现的能力。增强之后，可以去寻找默认实现了。
比如，"tmall.placeOrder.88vip"这个场景，其寻找扩展点的路径是：
1. 尝试寻找"tmall.placeOrder.88vip"实现
2. 如果没有，继续寻找"tmall.placeOrder"实现
3. 如果没有，继续寻找"tmall"实现

## 3.1.0 版本
1. 进一步简化了cola-core，只保留了扩展能力。
2. 将exception从cola-core移入到cola-common。
3. 对archetype中的分包逻辑进行重构，改成按照domain做划分。
4. 将cola-archetype-web中的controller改名为adaptor，为了呼应六边形架构的命名。
