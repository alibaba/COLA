# COLA 2.0架构
<strong>COLA是Clean Object-Oriented and Layered Architecture的缩写，代表“整洁面向对象分层架构”，也叫“可乐”架构，目前COLA已经发展到[COLA 2.0](https://blog.csdn.net/significantfrank/article/details/100074716)。</strong>  需要访问老版本，请使用[COLA 1.0 TAG](https://github.com/alibaba/COLA/tree/COLA1.0)

关于COLA 2.0的更多信息，请关注微信公众号：

![qrcode_60.jpg](https://ata2-img.cn-hangzhou.oss-pub.aliyun-inc.com/9434d30a2db4c6036e1ba37be55b2c6e.jpg)

也可以购买我的新书[《代码精进之路：从码农到工匠》](https://detail.tmall.com/item.htm?id=610042416451)，里面有关于COLA比较详细的描述和使用。

# 调整部分
1. 修改Command为Executor。
2. 定义对Repository的CQRS标准。
3. 添加Domain的构造工厂,Domain可通过RepositoryBus、EventBus访问数据资源。

思路：
https://github.com/alibaba/COLA/issues/61

参考Domain
```java 
//Domain 需要继承DomainObject对象
class XXXDomain extends DomainObject{
    //初始化数据
    private final XXX xxx;
    
    //资源对象，有DomainFactory构建传入，对象在DomainObject中，子类可直接使用。
    protected static EventBus eventBus;
    protected static RepositoryBus repositoryBus;
   
    
    //构造函数 初始化数据
    public XXXDomain(XXX xxx){
        this.xxx = xxx;
    }
    
     //构造函数 初始化数据
    public XXXDomain(String xxxId){
        this.xxx = repositoryBus.execute(new XXXHandler.GetById(xxxId));
    }
    
    //临时对象，用于业务处理过程中
    private Object temp;
    
    //domain的main函数，也可以执行其他的public方法。
    public void execute(){
        //...
    }
    
    
    public void test(){
        //...
    }
    
    
    private void temp(){
        //temp doing...
    }

}
```
### 安装脚本

```

mvn clean install -Dtest.skip=true 

```

# 项目说明
**COLA既是框架，也是架构。创建COLA的主要目的是为应用架构提供一套简单的可以复制、可以理解、可以落地、可以控制复杂性的”指导和约束"。**
- 框架部分主要是以二方库的形式被应用依赖和使用。
- 架构部分主要是提供了创建符合COLA要求的应用Archetype。

## COLA框架
COLA作为框架主要是提供应用和架构需要的通用组件支撑，比如对CQRS和扩展点功能的支持。COLA框架主要由cola-framework这个项目来实现。
在这个项目里面包含3个Module：cola-core, cola-common和cola-test。

### cola-core
该Module是整个框架的核心，里面的主要功能和Package如下：
```
com
└── alibaba
    └── cola
        ├── assembler  \\提供Assembler标准
        ├── boot \\这是框架的核心启动包，负责框架组件的注册、发现
        ├── executor  \\提供executor标准
        ├── common
        ├── context  \\提供框架执行所需要的上下文
        ├── domain  \\提供Domain Entity标准
        ├── event
        ├── exception \\提供Exception标准
        ├── extension  \\负责扩展机制中的重要概念-扩展(Extension)的定义和执行
        ├── logger  \\提供DIP的日志接口
        ├── repository  \\提供仓储（Repository）的标准
```
### cola-common
该Module提供了框架中Client Object, Entity Object和Data Object的定义，二方库会依赖该Module。

### cola-test  
该Module主要是提供一些开发测试的工具，可以使用TDD来进行开发。

## COLA架构
COLA作为架构，组要是制定了一套指导和约束，并将这套规范沉淀成Archetype。以便通过Archetype可以快速的生成符合COLA规范的应用。满足COLA的应用是一个有清晰的依赖关系的分层架构，如下图所示：

![image.png](https://ata2-img.cn-hangzhou.oss-pub.aliyun-inc.com/a33b80bcac5ec73d0d1358d6b49a119c.png)

我们提供了两个Archetype，分别是cola-archetype-service和cola-archetype-web

### cola-archetype-service
用来生成纯后端应用（没有Controller），生成应用的命令为：
```
mvn archetype:generate  -DgroupId=com.alibaba.demo -DartifactId=demo -Dversion=1.0.0-SNAPSHOT -Dpackage=com.alibaba.demo -DarchetypeArtifactId=cola-framework-archetype-service -DarchetypeGroupId=com.alibaba.cola -DarchetypeVersion=2.0.0
```

### cola-archetype-web
用来生成Web后端应用（有Controller），生成应用的命令为：
```
mvn archetype:generate  -DgroupId=com.alibaba.demo -DartifactId=demo -Dversion=1.0.0-SNAPSHOT -Dpackage=com.alibaba.demo -DarchetypeArtifactId=cola-framework-archetype-web -DarchetypeGroupId=com.alibaba.cola -DarchetypeVersion=2.0.0
```

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

首先在demo目录下运行mvn install（如果不想运行测试，可以加上-DskipTests参数）。然后进入start目录，执行mvn spring-boot:run。
运行成功的话，可以看到SpringBoot启动成功的界面。

**3、 执行测试：**

生成的应用中，已经实现了一个简单的Rest请求，可以在浏览器中输入 http://localhost:8080/customer?name=World 进行测试。

**4、查看运行日志：**

请求执行成功的话，可以在浏览器中的返回值中看到："customerName":"Hello, World"。同时观察启动SpringBoot的控制台，可以看到LoggerInterceptor打印出来的日志。
