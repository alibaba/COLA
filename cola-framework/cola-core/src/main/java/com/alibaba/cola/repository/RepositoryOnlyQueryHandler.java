package com.alibaba.cola.repository;

/**
 * @author lorne
 * @date 2020/1/27
 * @description
 */
public interface RepositoryOnlyQueryHandler<Res> extends RepositoryHandlerI {

    Res query();

}
