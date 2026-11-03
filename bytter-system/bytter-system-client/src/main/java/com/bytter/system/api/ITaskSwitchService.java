package com.bytter.system.api;
import com.bytter.system.api.dto.SysTaskDTO;
/**
 * Created by zhouj on 2017/10/24.
 */
public interface ITaskSwitchService {
    void startTask();//定时任务总启动
    void standTask();//定时任务总停止
    void addTask(SysTaskDTO dto);//新增任务
    void updateTask(SysTaskDTO dto);//修改任务
    void delTask(String id);//删除任务
    /**
     * @param id     定时任务ID
     * @param active 1启动0暂停
     */
    void operationTask(String id, String active);//启动\暂停 任务
    /**
     * 根据传递的月数来删除几个月以前的正常数据
     *
     * @param month 删除的月数
     */
    void deleteBeforeDate(int month);
}
