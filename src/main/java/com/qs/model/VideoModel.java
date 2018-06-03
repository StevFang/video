package com.qs.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 视频信息
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VideoModel {

    private String videoId;

    private String videoPath;

    private String videoName;

    private String videoType;

}
