package com.qs.dto.video;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 用于存放任务id,任务主进程，任务输出线程
 *
 * @author fbin
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TaskDTO implements Serializable {

    /**
     * 任务id
     */
    private String id;

    /**
     * 任务主进程
     */
    private Process process;

    /**
     * 任务输出线程
     */
    private Thread thread;

}
