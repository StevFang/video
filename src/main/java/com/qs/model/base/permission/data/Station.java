package com.qs.model.base.permission.data;

import com.qs.model.AbstractModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author FBin
 * @time 2019/1/7 18:03
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Station extends AbstractModel implements Serializable {

    private String code;

    private String name;

}
