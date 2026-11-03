package com.bytter.system.api;

import com.bytter.system.api.vo.XxlJobGroup;
import com.bytter.system.api.vo.XxlJobInfo;

import java.util.List;

/**
 * XXL接口工具类。
 *
 * @author zhaohc
 * @date 2023/10/25 17:19
 */
public interface IXxlJobTemplate {

    /**
     * 查询执行器列表。
     *
     * @param appName 执行器服务名称。
     * @param title   执行器名称。
     * @return 执行器列表。
     */
    List<XxlJobGroup> listGroup(String appName, String title);

    /**
     * 根据服务名获取执行器（理论上一个服务只有一个执行器，可以注册多个集群服务）。
     *
     * @param appName 服务名。
     * @return 执行器。
     */
    List<XxlJobGroup> listGroupByExcMachine(String appName);

    /**
     * 根据查询条件获取对应的定时任务明细。
     *
     * @param jobGroup        执行器ID。
     * @param triggerStatus   调度状态。
     * @param jobDesc         job描述。
     * @param executorHandler 任务Handler名称。
     * @param author          负责人。
     * @return 定时任务明细。
     */
    List<XxlJobInfo> listJob(int jobGroup, int triggerStatus, String jobDesc, String executorHandler, String author);

    /**
     * 根据执行方法获取定时任务明细。
     *
     * @param appName       服务名。
     * @param id            任务ID。
     * @return 任务明细。
     */
    XxlJobInfo getJobByExecutorHandler(String appName, String id);

    /**
     * 根据任务ID获取定时任务明细。
     *
     * @param id 任务ID。
     * @return 任务明细。
     */
    XxlJobInfo getJobByExecutorHandler(String id);

    /**
     * 根据查询条件获取定时任务明细。
     *
     * @param jobGroup        执行器ID。
     * @param triggerStatus   调度状态。
     * @param jobDesc         job描述。
     * @param executorHandler 任务Handler名称。
     * @param author          负责人。
     * @return 定时任务明细。
     */
    XxlJobInfo getOneJob(int jobGroup, int triggerStatus, String jobDesc, String executorHandler, String author);

    /**
     * 添加定时任务。
     *
     * @param addJob  定时任务明细。
     * @param appName 执行器服务名。
     * @return 执行结果。
     */
    Integer addJob(XxlJobInfo addJob, String appName);

    /**
     * 变更定时任务明细。
     *
     * @param updateXxlJob 定时任务明细。
     */
    void updateJob(XxlJobInfo updateXxlJob);

    /**
     * 删除定时任务。
     *
     * @param jobId 定时任务ID。
     */
    void removeJob(int jobId);


}
