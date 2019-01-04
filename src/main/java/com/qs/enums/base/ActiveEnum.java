package com.qs.enums.base;

/**
 * 是否有效
 *
 * @author FBin
 * @time 2019/1/4 17:11
 */
public enum ActiveEnum {

    Y("Y", "有效"),
    N("N", "无效");

    private String code;
    private String label;

    ActiveEnum(String code, String label){
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
