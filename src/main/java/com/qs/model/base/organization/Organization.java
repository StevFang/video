package com.qs.model.base.organization;

import com.qs.model.AbstractModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author FBin
 * @time 2019/1/7 18:02
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Organization extends AbstractModel implements Serializable {

    private String code;

    private String name;

    private String hierarchyPath;

}
