## 作用
主要提供了@Entity注解，该注解将Spring的Bean的scope定义为prototype，因为Domain Entity是有状态的，不能
进行多线程共享，所以必须是多实例的。

另外提供了DomainFactory辅助类，帮助应用创建Domain Entity。


