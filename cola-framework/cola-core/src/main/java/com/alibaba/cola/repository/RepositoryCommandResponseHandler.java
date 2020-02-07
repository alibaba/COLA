package com.alibaba.cola.repository;


/**
 * @author lorne
 * @date 2020/1/27
 * @description
 */
public interface RepositoryCommandResponseHandler<R,C extends CommandI>
        extends RepositoryHandlerI {

    R command(C command);

}
