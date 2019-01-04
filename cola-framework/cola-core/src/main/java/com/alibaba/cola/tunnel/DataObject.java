package com.alibaba.cola.tunnel;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;

/**
 * This is the parent of all Data Objects.
 * Data object only has fields and according getters and setters, it's used to do CRUD operations
 *
 * @author fulan.zjf 2017年10月27日 上午10:21:01
 */
@Data
public abstract class DataObject {

    private String id;
    private String creator;
    private String modifier;
    private Date gmtCreate;
    private Date gmtModified;

}
