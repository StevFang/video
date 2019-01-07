package com.qs.model.base.user;

import com.qs.enums.base.ActiveEnum;
import com.qs.model.AbstractModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author FBin
 * @time 2019/1/7 17:48
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Employee extends AbstractModel implements Serializable {

    private String code;

    private String name;

    private ActiveEnum isActive;

}
