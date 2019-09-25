package com.xiayiye.openwechatapp;

/**
 * @author DELL
 * 存放常量的类
 */
public class AppConstant {
    /**
     * 生产环境获取token的url
     */
    static String getTokenProdUrl = "http://api.reapal.com/token/create";
    /**
     * 测试环境获取token的url
     */
    static String getTokenTestUrl = "http://10.168.17.51:8080/token/create";
    /**
     * 生产环境小程序的appId
     */
    static String smallAppIdProd = "wx97f86a39010edf03";
    /**
     * 测试环境小程序的appId
     */
    static String smallAppIdTest = "wx515a9a1287395c48";
    /**
     * 生产环境的商户号
     */
    static String merchantIdProd = "100000000071531";
    /**
     * 测试环境的商户号
     */
    static String merchantIdTest = "100000000000147";
    /**
     * 生产环境的分账参数
     */
    static String legerParamProd = "1,100000000071531,2,3";
    /**
     * 测试环境的分账参数
     */
    static String legerParamTest = "1,100000000000147,2,3";
    /**
     * 生产环境小程序的原始appId
     */
    static String originalAppIdProd = "gh_e5300fcdd6ca";
    /**
     * 测试环境小程序的原始appId
     */
    static String originalAppIdTest = "gh_99fa76dc00e6";
    /**
     * 移动应用的APPId
     */
    public static String appId = "wx82759842723273d4";
}
