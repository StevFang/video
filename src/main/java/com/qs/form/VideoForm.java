package com.qs.form;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 上传视频信息交互表单
 *
 * Created by fbin on 2018/6/2.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class VideoForm extends QueryInfo{

    private String videoId;

    private String videoName;

    //简介
    private String videoIntro;

}
