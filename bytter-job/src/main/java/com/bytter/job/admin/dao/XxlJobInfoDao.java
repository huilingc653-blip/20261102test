package com.bytter.job.admin.dao;
import com.bytter.job.admin.core.model.XxlJobInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;
/**
 * job info
 * @author xuxueli 2016-1-12 18:03:45
 */
@Mapper
public interface XxlJobInfoDao {
	public List<XxlJobInfo> pageList(@Param("offset") int offset,
									 @Param("pagesize") int pagesize,
									 @Param("jobGroup") int jobGroup,
									 @Param("triggerStatus") int triggerStatus,
									 @Param("jobId") Integer jobId,
									 @Param("jobDesc") String jobDesc,
									 @Param("executorHandler") String executorHandler,
									 @Param("author") String author,
									 @Param("userId") Integer userId);

	public List<XxlJobInfo> pageAllList(@Param("offset") int offset,
										@Param("pagesize") int pagesize,
										@Param("jobGroup") int jobGroup,
										@Param("jobDesc") String jobDesc);

	public int pageListCount(@Param("offset") int offset,
							 @Param("pagesize") int pagesize,
							 @Param("jobGroup") int jobGroup,
							 @Param("triggerStatus") int triggerStatus,
							 @Param("jobId") Integer jobId,
							 @Param("jobDesc") String jobDesc,
							 @Param("executorHandler") String executorHandler,
							 @Param("author") String author,
							 @Param("userId") Integer userId);

	public int pageAllListCount(@Param("jobGroup") int jobGroup,
								@Param("jobDesc") String jobDesc);
	
	public int save(XxlJobInfo info);
	public XxlJobInfo loadById(@Param("id") int id);

	public XxlJobInfo findByJobGroupAndExecutorHandler(@Param("jobGroup") int jobGroup,
													   @Param("executorHandler") String executorHandler);
	
	public int update(XxlJobInfo xxlJobInfo);
	
	public int delete(@Param("id") long id);
	public List<XxlJobInfo> getJobsByGroup(@Param("jobGroup") int jobGroup);
	public int findAllCount();
	public List<XxlJobInfo> scheduleJobQuery(@Param("maxNextTime") long maxNextTime, @Param("pagesize") int pagesize );
	public int scheduleUpdate(XxlJobInfo xxlJobInfo);
}
