package com.qs.common;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 上传结果信息
 *
 * Created by fbin on 2018/5/30.
 *
 * @author FBin
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UploadResult {

    private String url;

}
