package com.alibaba.cola.extension;

/**
 * 扩展点初始化或者查找失败时，使用次异常
 * <p>
 * 扩展点初始化或者查找失败时，使用次异常
 * <p>
 *
 * @author ***flying@126.com
 * @version 1.0.0
 * @since 1.0.0 2022/9/26
 */
public class ExtensionException extends RuntimeException {

    private String errCode;

    public ExtensionException(String errMessage) {
        super(errMessage);
    }

    public ExtensionException(String errCode, String errMessage) {
        super(errMessage);
        this.errCode = errCode;
    }

    public ExtensionException(String errMessage, Throwable e) {
        super(errMessage, e);
    }

    public ExtensionException(String errCode, String errMessage, Throwable e) {
        super(errMessage, e);
        this.errCode = errCode;
    }

    public String getErrCode() {
        return errCode;
    }

    public void setErrCode(String errCode) {
        this.errCode = errCode;
    }

}
