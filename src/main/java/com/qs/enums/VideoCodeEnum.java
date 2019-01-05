package com.qs.enums;

/**
 * Video Code 码一览
 *
 * @author FBin
 * @time 2018/12/3 20:36
 */
public enum VideoCodeEnum {

    UPLOAD_SUCCESS("10000", "上传成功"),

    QUERY_SUCCESS("20000", "操作成功"),
    QUERY_ERROR("20010", "查询异常"),

    LIVE_SUCCESS("30000", "直播推流成功"),
    LIVE_ERROR("30010", "直播推流异常"),
    PARAM_CHECK_ERROR("30020", "参数%s校验不正确, 说明:%s"),
    CONFIG_ERROR("30040", "配置加载异常"),

    VIDEO_DECODE_SUCCESS("40000", "视频解码转码成功"),
    VIDEO_DECODE_ERROR("40010", "视频转码解码异常");

    private String code;
    private String label;

    VideoCodeEnum(String code, String label){
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
