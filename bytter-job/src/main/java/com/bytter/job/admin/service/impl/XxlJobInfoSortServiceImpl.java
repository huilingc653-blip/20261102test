package com.bytter.job.admin.service.impl;

import com.bytter.job.admin.core.model.XxlJobInfo;
import com.bytter.job.admin.core.model.XxlJobUser;
import com.bytter.job.admin.dao.XxlJobInfoSortDao;
import com.bytter.job.admin.service.LoginService;
import com.bytter.job.admin.service.XxlJobInfoSortService;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 任务信息分页查询 Service 实现(支持动态排序)
 * 与 XxlJobServiceImpl 相互独立,仅用于内管端租户定时任务分配列表查询
 *
 * @author zhukr
 */
@Service
public class XxlJobInfoSortServiceImpl implements XxlJobInfoSortService {

	@Resource
	private XxlJobInfoSortDao xxlJobInfoSortDao;

	@Override
	public Map<String, Object> pageList(int start, int length, int jobGroup, int triggerStatus, int jobId,
										String jobDesc, String executorHandler, String author,
										String tenantId,
										String orderBy, String orderDir) {
		int userId = getUserId();
		// page list
		List<XxlJobInfo> list = xxlJobInfoSortDao.pageList(start, length, jobGroup, triggerStatus, jobId, jobDesc, executorHandler, author, tenantId, userId, orderBy, orderDir);
		int list_count = xxlJobInfoSortDao.pageListCount(start, length, jobGroup, triggerStatus, jobId, jobDesc, executorHandler, author, tenantId, userId);

		// package result
		Map<String, Object> maps = new HashMap<String, Object>();
		maps.put("recordsTotal", list_count);		// 总记录数
		maps.put("recordsFiltered", list_count);	// 过滤后的总记录数
		maps.put("data", list);  					// 分页列表
		return maps;
	}

	private int getUserId() {
		XxlJobUser user = getUser();
		return user != null ? user.getId() : 0;
	}

	private XxlJobUser getUser() {
		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
		return (XxlJobUser) request.getAttribute(LoginService.LOGIN_IDENTITY_KEY);
	}
}
