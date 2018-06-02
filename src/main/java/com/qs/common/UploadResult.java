package com.qs.common;

import com.qs.utils.ConvertUtil;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * 上传结果信息
 *
 * Created by fbin on 2018/5/30.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UploadResult {

    private String url;

    private String startTime;

    private String endTime;

    private String totalTime;

    public void getStart(){
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(DateConstants.PATTERN_COMMON_MILLISECOND);
        String localDateTimeStr = LocalDateTime.now().format(dateTimeFormatter);
        this.setStartTime(localDateTimeStr);
    }

    public void getEnd(String url){
        this.url = url;
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(DateConstants.PATTERN_COMMON_MILLISECOND);
        String localDateTimeStr = LocalDateTime.now().format(dateTimeFormatter);
        this.setEndTime(localDateTimeStr);

        long interval = ConvertUtil.getInterval(LocalDateTime.parse(startTime, dateTimeFormatter),
                LocalDateTime.parse(endTime, dateTimeFormatter), "s");
        this.setTotalTime(String.valueOf(interval));
    }
}
