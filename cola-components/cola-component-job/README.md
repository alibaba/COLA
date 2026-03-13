# 主要功能
可持久化、分布式任务管理组件：
1. 可同步，可异步
2. 可持久化（redis，数据库）
3. Job可重复执行，Step失败可回滚，Step可断点继续
4. 支持批量任务

# 核心领域概念
6. JobLauncher：是任务启动器，通过它来启动任务，可以看做是程序的入口。
7. BatchJobLauncher: 批量任务的处理入口
8. BatchJob：批量任务，包含了多个JobInstance，一个JobInstance包含一个Job单例和一个ExecutionContext对象
9. ExecutionContext: 任务执行的上下文，主要是用来承载用户传递的参数，以及不同Job、step之间传递参数
10. Job: 代表着一个具体的任务。
11. JobExecution: 代表一次具体的任务执行，可持久化，会管理任务执行状态。
12. Step: 代表着一个具体的步骤，一个Job可以包含多个Step。在实际业务场景中，可能一个任务很复杂，这个时候可以将任务 拆分成多个step，分别对这些step 进行管理（将一个复杂任务简单化）。
13. StepExecution： 代表一个具体的执行步骤，可持久化，会管理步骤的执行状态。
14. JobRepository：批处理框架执行过程中的上下文（元数据）,有三种实现，1）通过内存来管理，2）通过Redis持久化，3）通过数据库持久化。

# 详细设计解析
https://blog.csdn.net/significantfrank/article/details/145314072

# 如何使用
1. 引入依赖cola-component-job依赖。注意：为了减少冲突，依赖的存储redis和database均为provided，需要使用方自己提供，SpringBoot版本最好在3.2.0以上
2. 在SpringBoot的启动类上添加@EnableColaJob注解，该注解会扫描注册需要的bean，以及负责初始化JobRepository
3. 默认的数据库初始化脚本是schema-mysql.sql。如果使用其他数据库，可通过application.yml配置文件中的cola.job.database.ddl-location属性来指定脚本路径
