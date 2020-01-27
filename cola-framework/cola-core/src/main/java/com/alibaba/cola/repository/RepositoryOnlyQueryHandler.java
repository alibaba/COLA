package com.alibaba.cola.repository;

/**
 * @author lorne
 * @date 2020/1/27
 * @description
 */
public interface RepositoryOnlyQueryHandler<Res extends CmdResponseI> extends RepositoryHandlerI {

    Res query();

}
