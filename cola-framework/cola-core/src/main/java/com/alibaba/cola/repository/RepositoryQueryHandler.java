package com.alibaba.cola.repository;

/**
 * @author lorne
 * @date 2020/1/27
 * @description
 */
public interface RepositoryQueryHandler<R extends CmdResponseI,C extends CommandI> extends RepositoryHandlerI {

    R query(C command);

}
