## crm-framework-archetype
该文件夹下面是Archetype的源码，
创建新的应用请使用下面的命令：

```
mvn archetype:generate  -DgroupId=com.alibaba.crm -DartifactId=demo -Dversion=1.0-SNAPSHOT -Dpackage=com.alibaba.crm.demo -DarchetypeArtifactId=crm-framework-archetype -DarchetypeGroupId=com.alibaba.crm -DarchetypeVersion=1.0-SNAPSHOT

```
只需要将demo替换成你自己的Artifact名字就可以了

## crm-framework
该文件夹下面是framework的源码，可以拉下来直接启动Spring容器，或者PandoraBoot跑通HSF和Diamond的。