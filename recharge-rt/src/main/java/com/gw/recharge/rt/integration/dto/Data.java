/**
 * 
 *大智慧股份有限公司
 * Copyright (c) 2006-2015 DZH,Inc.All Rights Reserved.
 */
package com.gw.recharge.rt.integration.dto;


/**
 * 
 * @author log.yin
 * @version $Id: Data.java, v 0.1 2015年5月11日 下午4:23:10 log.yin Exp $
 */
public class Data {
    private String operator;
    private String area;
    private String area_operator;

    //    private Set<String> support_price;

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getArea_operator() {
        return area_operator;
    }

    public void setArea_operator(String area_operator) {
        this.area_operator = area_operator;
    }

    /*    public Set<String> getSupport_price() {
            return support_price;
        }

        public void setSupport_price(Set<String> support_price) {
            this.support_price = support_price;
        }*/

}
