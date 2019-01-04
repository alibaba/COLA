#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )

package ${package}.validator.extension;

import ${package}.common.BizCode;
import ${package}.dto.CustomerAddCmd;
import ${package}.dto.clientobject.CustomerType;
import ${package}.validator.CustomerAddValidator;
import ${package}.validator.extensionpoint.CustomerAddValidatorExtPt;

import com.alibaba.cola.exception.BizException;
import com.alibaba.cola.extension.Extension;
import com.alibaba.cola.logger.Logger;
import com.alibaba.cola.logger.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;

/**
 * CustomerAddBizOneValidatorExt
 * 
 * @author fulan.zjf 2017-11-04
 */
@Extension(bizCode = BizCode.BIZ_ONE)
public class CustomerAddBizOneValidatorExt implements CustomerAddValidatorExtPt {

	private Logger logger = LoggerFactory.getLogger(CustomerAddBizOneValidatorExt.class);

    @Autowired
    private CustomerAddValidator customerAddValidator;

    @Override
    public void validate(Object candidate) {
        customerAddValidator.validate(candidate);
    	logger.debug("Biz One validation");
    }

}
