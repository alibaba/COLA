#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )

package ${package}.validator.extensionpoint;

import com.alibaba.cola.extension.ExtensionPointI;
import com.alibaba.cola.validator.ValidatorI;

/**
 * CustomerAdd Validation Point
 * @author fulan.zjf 2017-11-05
 */
public interface CustomerAddValidatorExtPt extends ValidatorI, ExtensionPointI{

}
