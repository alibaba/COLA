package com.alibaba.sofa.validator;

import org.hibernate.validator.messageinterpolation.ResourceBundleMessageInterpolator;

import java.util.Locale;

/**
 * @author fulan.zjf
 * @date 2017/12/25
 */
public class SofaMessageInterpolator extends ResourceBundleMessageInterpolator{

    @Override
    public String interpolate(String message, Context context) {
        //Use English Locale
        String resolvedMessage = super.interpolate(message, context, Locale.ENGLISH);
        return resolvedMessage;
    }
}
