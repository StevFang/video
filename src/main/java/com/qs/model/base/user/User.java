package com.qs.model.base.user;

import com.qs.enums.base.ActiveEnum;
import com.qs.enums.base.YesOrNoEnum;
import com.qs.model.AbstractModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 用户
 *
 * @author FBin
 * @time 2019/1/4 1:10
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class User extends AbstractModel implements Serializable {

    private String code;

    private String name;

    private ActiveEnum isActive;

    private Long employeeId;

    private YesOrNoEnum openAccount;

    private String account;

    private String password;

}
