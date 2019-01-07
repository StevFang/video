package com.qs.model.base.region;

import com.qs.enums.base.RegionTypeEnum;
import com.qs.model.AbstractModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author FBin
 * @time 2019/1/7 17:50
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Region extends AbstractModel implements Serializable {

    private String code;

    private String name;

    private String hierarchyPath;

    private RegionTypeEnum regionType;

    private Long rootRegionId;

}
