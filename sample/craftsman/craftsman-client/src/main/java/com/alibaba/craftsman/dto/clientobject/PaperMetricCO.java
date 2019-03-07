package com.alibaba.craftsman.dto.clientobject;

import lombok.Data;

import javax.validation.constraints.NotEmpty;

/**
 * PaperMetricCO
 *
 * @author Frank Zhang
 * @date 2019-03-03 11:16 AM
 */
@Data
public class PaperMetricCO extends AbstractMetricCO{
    @NotEmpty
    private String paperName;
    private String paperDesc;
    private String magazine;
    private String paperLink;
}
