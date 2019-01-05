package com.qs.vo.resp;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 通用调用返参
 *
 * Created by fbin on 2018/5/30.
 *
 * @author FBin
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CommonRespVO implements Serializable {

    public String code;

    public String msg;

    public Object data;

}
