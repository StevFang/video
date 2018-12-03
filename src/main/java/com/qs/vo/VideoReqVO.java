package com.qs.vo;

import com.qs.dto.QueryParamDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 上传视频信息交互 View Object
 *
 * Created by fbin on 2018/6/2.
 *
 * @author FBin
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class VideoReqVO extends QueryParamDto {

    private String videoId;

    private String videoName;

    /**
     * 简介
     */
    private String videoIntro;

}
