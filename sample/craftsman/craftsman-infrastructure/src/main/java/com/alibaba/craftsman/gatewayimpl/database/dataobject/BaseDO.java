package com.alibaba.craftsman.gatewayimpl.database.dataobject;

/**
 * BaseDO
 *
 * @author Frank Zhang
 * @date 2020-07-02 10:03 AM
 */
public class BaseDO {
    private String creator;
    private String modifier;

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public String getModifier() {
        return modifier;
    }

    public void setModifier(String modifier) {
        this.modifier = modifier;
    }
}
