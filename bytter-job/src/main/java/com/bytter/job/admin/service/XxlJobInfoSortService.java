package com.bytter.job.admin.service;

import java.util.Map;

/**
 * 任务信息分页查询 Service(支持动态排序)
 * 与 XxlJobService 相互独立,仅用于内管端租户定时任务分配列表查询,不影响原 XXL-Job 管理端
 *
 * @author zhukr
 */
public interface XxlJobInfoSortService {

	/**
	 * page list
	 *
	 * @param start       起始偏移量
	 * @param length      每页条数
	 * @param jobGroup    执行器ID,<=0 不过滤
	 * @param triggerStatus 调度状态,-1 全部
	 * @param jobId       任务ID精确查询
	 * @param jobDesc     任务描述模糊查询
	 * @param executorHandler 任务 Handler 模糊查询
	 * @param author      负责人模糊查询
	 * @param tenantId    当前租户ID
	 * @param orderBy     排序字段(白名单字段,为空默认 id)
	 * @param orderDir    排序方向 asc/desc,为空默认 desc
	 * @return 分页结果
	 */
	Map<String, Object> pageList(int start, int length, int jobGroup, int triggerStatus, int jobId,
								 String jobDesc, String executorHandler, String author,
								 String tenantId,
								 String orderBy, String orderDir);
}
