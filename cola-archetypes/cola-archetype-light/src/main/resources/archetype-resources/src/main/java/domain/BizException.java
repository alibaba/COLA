#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}.domain;

public class BizException extends RuntimeException{

    public BizException(String errMessage) {
        super(errMessage);
    }

    public static BizException of(String errMessage){
        return new BizException(errMessage);
    }
}
