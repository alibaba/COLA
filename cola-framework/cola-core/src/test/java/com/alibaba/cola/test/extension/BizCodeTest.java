package com.alibaba.cola.test.extension;

/**
 * BizCodeTest
 *
 * @author Frank Zhang
 * @date 2019-01-02 12:09 PM
 */
public class BizCodeTest {

    public static void main(String[] args) {
        String bizCode = "ali.tmall.supermarket";
        bizCode.lastIndexOf(".");
        //System.out.println(bizCode.substring(0, bizCode.lastIndexOf(".")));

        parseBizCode(bizCode);
        System.out.println(BizCodeTest.class.getSimpleName()+" "+BizCodeTest.class.getName());
    }

    public static void parseBizCode(String bizCode){
        System.out.println(bizCode);
        while(bizCode.lastIndexOf(".") != -1){
            bizCode = bizCode.substring(0, bizCode.lastIndexOf("."));
            System.out.println(bizCode);
        }
    }
}
