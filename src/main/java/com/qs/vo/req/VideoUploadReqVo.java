package com.qs.vo.req;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 视频上传ViewObject
 *
 * @author FBin
 * @time 2019/1/28 16:36
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class VideoUploadReqVo implements Serializable {

    private String originName;

    private String saveName;

    private String extName;

}
