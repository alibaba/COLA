package com.alibaba.cola.repository;


/**
 * @author lorne
 * @date 2020/1/27
 * @description
 */
public interface RepositoryCommandHandler<C extends CommandI> extends RepositoryHandlerI {

    void command(C command);

}
