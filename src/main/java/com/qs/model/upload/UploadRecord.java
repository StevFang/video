package com.qs.model.upload;

import com.qs.model.AbstractModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 上传记录
 *
 * @author FBin
 * @time 2019/1/7 17:32
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UploadRecord extends AbstractModel implements Serializable {

    private String code;

    private String originName;

    private String saveName;

    private String extName;

}
