# COLA发布操作说明

COLA发布到`Maven`中央库操作过程/CheckList。

## 0. 前置准备与配置

在Maven的`setting.xml`中配置`oss.sonatype.org`账号：

```xml

<servers>
    <server>
        <id>ossrh</id>
        <username>__YOUR_USERNAME__</username>
        <password>__YOUR_PASSWORD__</password>
    </server>
</servers>
```

更多发布操作说明（如用于`GPG`签名的`GPG`安装与配置），参见：

- OSSRH Guide  
  https://central.sonatype.org/pages/ossrh-guide.html
- Deploying to OSSRH with Apache Maven - Introduction  
  https://central.sonatype.org/pages/apache-maven.html

发布过程与发布文件的查看地址：

- sonatype的发布控制台  
  https://oss.sonatype.org/index.html
- Maven中央库的文件查看  
  https://repo1.maven.org/maven2/com/alibaba/cola/

发布使用`JDK 11`，为了生成`Javadoc`更现代。  
TODO：这个约束应该要去掉。使用`JDK 8`能发布挺好 :")

## 1. 发布 COLA Components

先确认版本号，去掉`SNAPSHOT`，如`4.x.y`。  
更新版本操作可以通过脚本[`bump_cola_version`](bump_cola_version)来统一完成。

在[COLA Components的根目录](../cola-components)，执行发布

```bash
./mvnw clean && ./mvnw deploy -DperformRelease
```

## 2. 发布 COLA Archetype

先确认版本号，去掉`SNAPSHOT`，如`4.x.y`：

- 更新 Archetype工程的POM文件的工程版本号：
    - [`cola-archetypes/pom.xml`](../cola-archetypes/pom.xml)
    - [`cola-archetype-service/pom.xml`](../cola-archetypes/cola-archetype-service/pom.xml)
    - [`cola-archetype-web/pom.xml`](../cola-archetypes/cola-archetype-web/pom.xml)
- 更新 Archetype模板中的POM文件的`cola.components.version`：
    - [`cola-archetypes/cola-archetype-service/src/main/resources/archetype-resources/pom.xml`](../cola-archetypes/cola-archetype-service/src/main/resources/archetype-resources/pom.xml)
    - [`cola-archetypes/cola-archetype-web/src/main/resources/archetype-resources/pom.xml`](../cola-archetypes/cola-archetype-web/src/main/resources/archetype-resources/pom.xml)

更新版本操作可以通过脚本[`bump_cola_version`](bump_cola_version)来统一完成。

在[COLA Archetype的根目录](../cola-archetypes)，执行发布

```bash
./mvnw clean && ./mvnw deploy -DperformRelease
```

## 3. 使用发布版本的COLA Archetype重新生成Sample

在[Samples目录](../samples)执行：

```bash
rm -rf craftsman

./mvnw archetype:generate \
    -DgroupId=com.alibaba.craftsman \
    -DartifactId=craftsman \
    -Dversion=1.0.0-SNAPSHOT \
    -Dpackage=com.alibaba.craftsman \
    -DarchetypeGroupId=com.alibaba.cola \
    -DarchetypeArtifactId=cola-framework-archetype-web \
    -DarchetypeVersion=4.x.y \
    -DinteractiveMode=false
```

然后`git`提交Sample。
