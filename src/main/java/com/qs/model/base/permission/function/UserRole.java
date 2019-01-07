package com.qs.model.base.permission.function;

import com.qs.model.AbstractModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author FBin
 * @time 2019/1/7 18:20
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserRole extends AbstractModel implements Serializable {

    private Long userId;

    private Long roleId;

}
