package com.qs.vo.resp;

import lombok.Builder;
import lombok.Data;

/**
 * 通用调用返参
 *
 * Created by fbin on 2018/5/30.
 *
 * @author FBin
 */
@Data
@Builder
public class VideoRespVO {

    public String code;

    public String msg;

    public Object data;

}
