package com.qs.vo.req;

import com.qs.dto.common.QueryParamDTO;
import lombok.*;

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
public class VideoReqVO extends QueryParamDTO {

    private String videoId;

    private String videoName;

    /**
     * 简介
     */
    private String videoIntro;

}
