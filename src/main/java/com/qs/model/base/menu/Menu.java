package com.qs.model.base.menu;

import com.qs.enums.base.YesOrNoEnum;
import com.qs.model.AbstractModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author FBin
 * @time 2019/1/7 17:52
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Menu extends AbstractModel implements Serializable {

    private String code;

    private String name;

    private String hierarchyPath;

    private String menuType;

    private YesOrNoEnum needLink;

    private String linkUrl;

    private Long rootMenuId;

}
