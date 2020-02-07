package com.alibaba.cola.repository;

import com.alibaba.cola.exception.framework.ColaException;
import lombok.Getter;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * @author lorne
 * @date 2020/1/27
 */
@Component
public class RepositoryHub {

    @Getter
    private Map<Class<? extends CommandI>, RepositoryHandlerI> presentationRepository = new HashMap<>();

    public RepositoryHandlerI getPresentationRepository(Class<? extends CommandI> presentationClass) {
        RepositoryHandlerI presentationHandler = presentationRepository.get(presentationClass);
        if (presentationHandler == null ) {
            throw new ColaException(presentationClass + "is not registered in presentationHub, please register first");
        }
        return presentationHandler;
    }

}
