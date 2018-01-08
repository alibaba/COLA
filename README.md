# SOFA
> SOFA是Simple Object-Oriented and Flexible Architecture的缩写，是轻量级的企业应用框架，可以有效的降低业务系统复杂度。

关于架构和设计的详细内容，请查看：https://www.atatech.org/articles/96063

# Module说明
## sofa-framework module

## sofa-archetype module

## sofa-demo module

## crm-framework-archetype
该文件夹下面是Archetype的源码，
创建新的应用请使用下面的命令：

```
mvn archetype:generate  -DgroupId=com.alibaba.crm -DartifactId=demo -Dversion=1.0-SNAPSHOT -Dpackage=com.alibaba.crm.demo -DarchetypeArtifactId=crm-framework-archetype -DarchetypeGroupId=com.alibaba.crm -DarchetypeVersion=1.0-SNAPSHOT

```
只需要将demo替换成你自己的Artifact名字就可以了

## crm-framework
该文件夹下面是framework的源码，可以拉下来直接启动Spring容器，或者PandoraBoot跑通HSF和Diamond的。
