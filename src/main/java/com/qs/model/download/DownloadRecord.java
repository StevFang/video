package com.qs.model.download;

import com.qs.enums.base.YesOrNoEnum;
import com.qs.model.AbstractModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author FBin
 * @time 2019/1/7 17:43
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DownloadRecord extends AbstractModel implements Serializable {

    private String code;

    private Long uploadRecordId;

    private YesOrNoEnum downloadFlag;

    private String downloadMsg;

}
