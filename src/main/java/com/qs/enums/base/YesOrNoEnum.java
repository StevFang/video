package com.qs.enums.base;

/**
 * 是否有效
 *
 * @author FBin
 * @time 2019/1/4 17:11
 */
public enum YesOrNoEnum {

    Y("Y", "是"),
    N("N", "否");

    private String code;
    private String label;

    YesOrNoEnum(String code, String label){
        this.code = code;
        this.label = label;
    }

    public String getCode() {
        return code;
    }

    public String getLabel() {
        return label;
    }
}
