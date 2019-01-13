#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )

package ${package}.validator.extension;

import ${package}.common.BizCode;
import ${package}.dto.CustomerAddCmd;
import ${package}.validator.CustomerAddValidator;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.cola.extension.Extension;
import com.alibaba.cola.logger.Logger;
import com.alibaba.cola.logger.LoggerFactory;
import ${package}.validator.extensionpoint.CustomerAddValidatorExtPt;
import com.alibaba.cola.validator.ValidatorCompoiste;

/**
 * CustomerAddDDValidator
 *
 * @author fulan.zjf 2017-11-04
 */
@Extension(bizCode = BizCode.BIZ_TWO)
public class CustomerAddBizTwoValidatorExt implements CustomerAddValidatorExtPt{

    private Logger logger = LoggerFactory.getLogger(CustomerAddBizTwoValidatorExt.class);

    @Autowired
    private CustomerAddValidator customerAddValidator;

    @Override
    public void validate(Object candidate) {
        customerAddValidator.validate(candidate);
        logger.debug("Biz Two validation");
    }

}
