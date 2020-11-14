## 作用
制定了Exception的相关规范，一是，为了复用；二是，使得应用层面的Logging和异常处理AOP成为可能。

实际上，对于应用系统而言，只有三种类型的异常：
1. BizException：业务异常，有明确的业务语义，不需要记录Error日志，不需要Retry
2. SysException：已知的系统异常，需要记录Error日志，可以Retry
3. Exception：未知的其它异常，需要完整的Error Stack日志，可以Retry


