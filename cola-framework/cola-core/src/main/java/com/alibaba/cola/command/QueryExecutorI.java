package com.alibaba.cola.command;

import com.alibaba.cola.dto.Command;
import com.alibaba.cola.dto.Response;

public interface QueryExecutorI<R extends Response, C extends Command> extends CommandExecutorI<R,C>{

}
