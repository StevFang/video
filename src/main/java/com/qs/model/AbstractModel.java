package com.qs.model;

import lombok.Data;

import java.util.Date;

/**
 * @author FBin
 * @time 2019/1/7 17:34
 */
@Data
public abstract class AbstractModel {

    protected Long oid;

    protected Long parentId;

    protected Long createdBy;

    protected Long updatedBy;

    protected Date createdOn;

    protected Date updatedOn;

}
