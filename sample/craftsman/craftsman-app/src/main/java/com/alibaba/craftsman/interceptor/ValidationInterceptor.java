package com.alibaba.craftsman.interceptor;

import com.alibaba.cola.command.CommandInterceptorI;
import com.alibaba.cola.command.PreInterceptor;
import com.alibaba.cola.dto.Command;
import com.alibaba.cola.exception.BizException;
import com.alibaba.cola.logger.Logger;
import com.alibaba.cola.logger.LoggerFactory;
import com.alibaba.cola.validator.AbstractValidationInterceptor;
import com.alibaba.cola.validator.ColaMessageInterpolator;
import org.hibernate.validator.HibernateValidator;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.lang.reflect.Field;
import java.util.Set;

/**
 * ValidationInterceptor
 *
 * @author Frank Zhang 2018-01-06 8:27 PM
 */
@PreInterceptor
public class ValidationInterceptor extends AbstractValidationInterceptor implements CommandInterceptorI {

    private static Logger logger = LoggerFactory.getLogger(ValidationInterceptor.class);

    //Enable fail fast, which will improve performance
    private ValidatorFactory factory = Validation.byProvider(HibernateValidator.class).configure().failFast(true)
            .messageInterpolator(new ColaMessageInterpolator()).buildValidatorFactory();

    @Override
    public void preIntercept(Command command) {
        super.validate(command);
    }

    public void doValidation(Object target){
        Validator validator = factory.getValidator();
        Set<ConstraintViolation<Object>> constraintViolations = validator.validate(target);
        constraintViolations.forEach(violation -> {
            logger.debug("Field: "+violation.getPropertyPath() + " Message: " + violation.getMessage());
            throw new BizException(violation.getPropertyPath() + " " + violation.getMessage());
        });
    }
}
