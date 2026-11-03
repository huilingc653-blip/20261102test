package com.bytter.job.admin.dao;

import com.bytter.job.admin.core.model.XxlJobOperateLog;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

/**
 * 操作日志持久层。
 *
 * @author zhaohc
 * @date 2025/6/24 15:59
 */
@Mapper
public interface XxlJobOperateLogDao {

    int save(XxlJobOperateLog xxlJobOperateLog);

    List<XxlJobOperateLog> pageList(@Param("offset") int offset, @Param("pagesize") int pagesize,
                                    @Param("username") String username, @Param("operateType") String operateType,
                                    @Param("jobDesc") String jobDesc,
                                    @Param("operateTimeStart") Date operateTimeStart,
                                    @Param("operateTimeEnd") Date operateTimeEnd);

    int pageListCount(@Param("username") String username, @Param("operateType") String operateType,
                      @Param("jobDesc") String jobDesc,
                      @Param("operateTimeStart") Date operateTimeStart,
                      @Param("operateTimeEnd") Date operateTimeEnd);
}
