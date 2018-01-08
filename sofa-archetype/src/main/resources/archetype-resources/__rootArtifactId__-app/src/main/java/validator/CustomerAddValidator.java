#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}.validator;

import org.springframework.stereotype.Component;

import com.alibaba.sofa.logger.Logger;
import com.alibaba.sofa.logger.LoggerFactory;
import ${package}.dto.CustomerAddCmd;
import ${package}.validator.extensionpoint.CustomerAddValidatorExtPt;

@Component
public class CustomerAddValidator implements CustomerAddValidatorExtPt {

	private Logger logger = LoggerFactory.getLogger(CustomerAddValidator.class);
	
	@Override
	public void validate(Object candidate) {
		logger.debug("Do common validation");
		CustomerAddCmd addCmd = (CustomerAddCmd)candidate;
	}
}
