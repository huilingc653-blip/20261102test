package com.bytter.job.admin.dao;

import com.bytter.job.admin.core.model.XxlJobLoginLog;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

/**
 * 登录日志。
 *
 * @author zhaohc
 * @date 2025/6/24 16:24
 */
@Mapper
public interface XxlJobLoginLogDao {

    int save(XxlJobLoginLog xxlJobLoginLog);

    List<XxlJobLoginLog> pageList(@Param("offset") int offset, @Param("pagesize") int pagesize,
                                  @Param("username") String username, @Param("status") int status,
                                  @Param("loginTimeStart") Date loginTimeStart,
                                  @Param("loginTimeEnd") Date loginTimeEnd);

    int pageListCount(@Param("username") String username, @Param("status") int status,
                     @Param("loginTimeStart") Date loginTimeStart,
                     @Param("loginTimeEnd") Date loginTimeEnd);
}
