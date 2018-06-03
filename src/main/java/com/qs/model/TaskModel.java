package com.qs.model;

/**
 * 用于存放任务id,任务主进程，任务输出线程
 *
 * @author fbin
 */
public class TaskModel {

    private final String id;//任务id

    private final Process process;//任务主进程

    private final Thread thread;//任务输出线程

    public TaskModel(String id, Process process, Thread thread) {
        this.id = id;
        this.process = process;
        this.thread = thread;
    }

    public String getId() {
        return id;
    }

    public Process getProcess() {
        return process;
    }

    public Thread getThread() {
        return thread;
    }

    @Override
    public String toString() {
        return "TaskModel [id=" + id + ", process=" + process + ", thread=" + thread + "]";
    }

}
