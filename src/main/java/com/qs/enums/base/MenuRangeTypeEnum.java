package com.qs.enums.base;

/**
 * @author FBin
 * @time 2019/1/7 18:16
 */
public enum MenuRangeTypeEnum {

    All("全部菜单", "all"),
    CurrentAndBelow("当前菜单及以下", "currentAndBelow"),
    Specify("指定菜单", "specify");

    private String label;
    private String value;

    MenuRangeTypeEnum(String label, String value){
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
