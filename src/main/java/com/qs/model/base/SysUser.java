package com.qs.model.base;

import com.qs.enums.base.ActiveEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 系统用户
 *
 * @author FBin
 * @time 2019/1/4 1:10
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SysUser implements Serializable {

    private Long oid;

    private String userName;

    private ActiveEnum isActive;

}
