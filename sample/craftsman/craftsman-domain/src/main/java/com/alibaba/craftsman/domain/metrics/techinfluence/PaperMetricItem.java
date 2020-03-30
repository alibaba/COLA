package com.alibaba.craftsman.domain.metrics.techinfluence;

import com.alibaba.craftsman.domain.metrics.MetricItem;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;

/**
 * PaperMetricItem
 *
 * @author Frank Zhang
 * @date 2018-09-20 3:26 PM
 */
@Data
public class PaperMetricItem extends MetricItem {

	private String paperName;
	private String paperDesc;
	private String magazine;
	private String paperLink;

	private static final double PAPER_SCORE = 10;

	public PaperMetricItem(){

	}

	public PaperMetricItem(String paperName, String paperDesc, String magazine, String paperLink) {
		this.paperName = paperName;
		this.paperDesc = paperDesc;
		this.magazine = magazine;
		this.paperLink = paperLink;
	}

	public static PaperMetricItem valueOf(String json){
		return JSON.parseObject(json, PaperMetricItem.class);
	}

	@Override
	public double calculateScore() {
		return PAPER_SCORE;
	}
}
