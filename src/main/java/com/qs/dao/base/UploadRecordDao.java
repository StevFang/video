package com.qs.dao.base;

import com.qs.model.upload.UploadRecord;
import org.springframework.stereotype.Repository;

/**
 * @author FBin
 * @time 2019/1/7 17:32
 */
@Repository
public interface UploadRecordDao {

    Integer save(UploadRecord uploadRecord);

}
