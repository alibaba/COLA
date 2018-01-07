package com.alibaba.sofa.command;

import com.alibaba.sofa.dto.Command;
import com.alibaba.sofa.dto.Response;

public interface QueryExecutorI<R extends Response, C extends Command> extends CommandExecutorI<R,C>{

}
