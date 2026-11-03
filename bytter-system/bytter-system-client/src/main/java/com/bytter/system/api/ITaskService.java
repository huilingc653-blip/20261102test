package com.bytter.system.api;
/**
 * 定时任务接口
 * Created by zhouj on 2017/9/28.
 */
public interface ITaskService {
    void run(String arguments);
}
