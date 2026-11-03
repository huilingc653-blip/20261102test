package com.bytter.job.admin.dao;

import com.bytter.job.admin.core.model.XxlJobInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 任务信息分页查询 DAO(支持动态排序)
 * 与 XxlJobInfoDao 相互独立,仅用于内管端租户定时任务分配列表查询,不影响原 XXL-Job 管理端
 * 排序字段在白名单内由 SQL 侧 <choose> 二次校验,防止 ORDER BY 注入
 *
 * @author zhukr
 */
@Mapper
public interface XxlJobInfoSortDao {

	List<XxlJobInfo> pageList(@Param("offset") int offset,
							  @Param("pagesize") int pagesize,
							  @Param("jobGroup") int jobGroup,
							  @Param("triggerStatus") int triggerStatus,
							  @Param("jobId") Integer jobId,
							  @Param("jobDesc") String jobDesc,
							  @Param("executorHandler") String executorHandler,
							  @Param("author") String author,
							  @Param("tenantId") String tenantId,
							  @Param("userId") Integer userId,
							  @Param("orderBy") String orderBy,
							  @Param("orderDir") String orderDir);

	int pageListCount(@Param("offset") int offset,
					  @Param("pagesize") int pagesize,
					  @Param("jobGroup") int jobGroup,
					  @Param("triggerStatus") int triggerStatus,
					  @Param("jobId") Integer jobId,
					  @Param("jobDesc") String jobDesc,
					  @Param("executorHandler") String executorHandler,
					  @Param("author") String author,
					  @Param("tenantId") String tenantId,
					  @Param("userId") Integer userId);
}
