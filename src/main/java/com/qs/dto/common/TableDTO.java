package com.qs.dto.common;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

/**
 * @author FBin
 * @time 2019/1/7 20:44
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TableDTO {

    private String tableName;

    private Map<String, String> propertyColumnMap;

    private Map<String, String> columnTypeMap;

    private Map<String, Class<?>> propertyTypeMap;

}
