package com.qs.enums.base;

/**
 * @author FBin
 * @time 2019/1/30 17:46
 */
public enum VideoTypeEnum {
    AVI("avi", "avi"),
    MPG("mpg", "mpg"),
    WMV("wmv", "wmv"),
    THREE_GP("3gp", "3gp"),
    MOV("mov", "mov"),
    MP_FOUR("mp4", "mp4"),
    ASF("asf", "asf"),
    ASX("asx", "asx"),
    FLV("flv", "flv"),
    WMV_NINE("wmv9", "wmv9"),
    RM("rm", "rm"),
    RMVB("rmvb", "rmvb");


    private String value;
    private String label;

    VideoTypeEnum(String value, String label){
        this.value = value;
        this.label = label;
    }

    public String getValue() {
        return value;
    }

    public String getLabel() {
        return label;
    }

}
