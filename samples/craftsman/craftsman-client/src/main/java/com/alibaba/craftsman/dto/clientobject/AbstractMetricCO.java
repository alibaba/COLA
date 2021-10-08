package com.alibaba.craftsman.dto.clientobject;

import com.alibaba.cola.dto.ClientObject;

// import org.springframework.validation.annotation.Validated;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotBlank;

/**
 * AbstractMetricCO
 *
 * @author Frank Zhang
 * @date 2019-03-04 11:32 AM
 */

@Data
public abstract class AbstractMetricCO extends ClientObject{
    /**
     * The ownerId of this Metric Item
     */
    @NotEmpty
    @NotBlank
    private String ownerId;
}
