package com.alibaba.cola.repository;

import com.alibaba.cola.common.ApplicationContextHelper;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * @author lorne
 * @date 2020/1/27
 */
@Component
@AllArgsConstructor
public class RepositoryBus {

    private RepositoryHub repositoryHub;

    public void command(CommandI presentation){
        RepositoryCommandHandler presentationHandler =  repositoryHub.getCommandPresentationHandler(presentation.getClass());
        presentationHandler.command(presentation);
    }

    public Object query(CommandI presentation){
        RepositoryQueryHandler presentationHandler =  repositoryHub.getQueryPresentationHandler(presentation.getClass());
        return presentationHandler.query(presentation);
    }

    public Object onlyQuery(Class<? extends RepositoryOnlyQueryHandler> clazz){
        RepositoryOnlyQueryHandler presentationHandler = ApplicationContextHelper.getBean(clazz);
        return presentationHandler.query();
    }

    public Object commandResponse(CommandI presentation){
        RepositoryCommandResponseHandler presentationHandler =  repositoryHub.getCommandResponsePresentationHandler(presentation.getClass());
        return presentationHandler.command(presentation);
    }
}
