package com.qs.model.base.permission.function;

import com.qs.enums.base.MenuRangeTypeEnum;
import com.qs.model.AbstractModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author FBin
 * @time 2019/1/7 18:13
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RoleMenu extends AbstractModel implements Serializable {

    private Long roleId;

    private MenuRangeTypeEnum rangeType;

    private Long menuId;

}
