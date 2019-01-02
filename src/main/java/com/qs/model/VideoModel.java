package com.qs.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 视频信息
 *
 * @author FBin
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VideoModel {

    /**
     * 视频Id
     */
    private String videoId;

    /**
     * 视频访问路径
     */
    private String videoPath;

    /**
     * 视频名称
     */
    private String videoName;

    /**
     * 视频类型
     */
    private String videoType;

}
