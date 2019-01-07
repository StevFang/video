package com.qs.enums.base;

/**
 * @author FBin
 * @time 2019/1/7 17:19
 */
public enum RegionTypeEnum {

    Area("大区", "area"),
    Province("省份", "province"),
    City("城市", "city"),
    District("地区", "district");

    private String label;
    private String value;

    RegionTypeEnum(String label, String value){
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
