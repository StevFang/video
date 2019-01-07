package com.qs.enums.base;

/**
 * @author FBin
 * @time 2019/1/7 18:04
 */
public enum OrgRangeTypeEnum {

    All("全部部门", "all"),
    CurrentAndBelow("本部门及以下", "currentAndBelow"),
    Specify("指定部门", "specify");

    private String label;
    private String value;

    OrgRangeTypeEnum(String label, String value){
        this.label = label;
        this.value = value;
    }

    public String getLabel() {
        return label;
    }

    public String getValue() {
        return value;
    }
}
