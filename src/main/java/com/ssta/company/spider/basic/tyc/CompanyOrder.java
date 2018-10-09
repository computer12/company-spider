package com.ssta.company.spider.basic.tyc;

/**
 * @Author Xavier
 * @Description 天眼查的五种排序
 * @Date 2018/8/24 15:59
 **/
public enum CompanyOrder {
    /** 默认
     */
    DEFAULT("","默认")
    /**
     * 注册资本降序
     */
    ,REGISTCAPITAL_DESC("/ola1","注册资本降序")
    /**
     * 注册资本降序
     */
    ,REGISTCAPITAL("/ola2","注册资本降序")
    /**
     * 注册时间降序
     */
    ,REGISTTIME_DESC("/ola3","注册时间降序")
    /**
     * 注册时间升序
     */
    ,REGISTTIME("/ola4","注册时间升序");

    CompanyOrder(String orderType,String msg){
        this.orderType = orderType;
        this.msg = msg;
    }

    private String orderType;

    private String msg;

    public String getOrderType() {
        return orderType;
    }

    public String getMsg() {
        return msg;
    }
}
