package com.alibaba.cola.dto;

import java.io.Serializable;

/**
 * Order Description
 *
 * @author Danny.Lee 2017/12/19
 */
public class OrderDesc implements Serializable{

    private String col;
    private boolean asc = true;

    public String getCol() {
        return col;
    }

    public void setCol(String col) {
        this.col = col;
    }

    public boolean isAsc() {
        return asc;
    }

    public void setAsc(boolean asc) {
        this.asc = asc;
    }
}
