#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}.assembler;

import java.util.HashMap;
import java.util.Map;

import com.alibaba.sofa.assembler.AssemblerI;
import ${package}.dto.clientobject.CustomerCO;

public class CustomerAssembler implements AssemblerI{

	public Map<String, String> assembleQueryParam(CustomerCO customerCO) {
		
		return new HashMap<>();
	}
}
