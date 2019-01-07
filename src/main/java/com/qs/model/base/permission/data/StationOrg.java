package com.qs.model.base.permission.data;

import com.qs.enums.base.OrgRangeTypeEnum;
import com.qs.model.AbstractModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author FBin
 * @time 2019/1/7 18:09
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class StationOrg extends AbstractModel implements Serializable {

    private Long stationId;

    private OrgRangeTypeEnum rangeType;

    private Long orgId;

}
