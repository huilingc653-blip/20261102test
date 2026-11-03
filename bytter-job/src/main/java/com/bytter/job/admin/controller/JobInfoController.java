package com.bytter.job.admin.controller;

import com.bytter.job.admin.controller.annotation.OperationLog;
import com.bytter.job.admin.controller.annotation.PermissionLimit;
import com.bytter.job.admin.core.constants.Constants;
import com.bytter.job.admin.core.exception.XxlJobException;
import com.bytter.job.admin.core.model.XxlJobGroup;
import com.bytter.job.admin.core.model.XxlJobInfo;
import com.bytter.job.admin.core.model.XxlJobUser;
import com.bytter.job.admin.core.route.ExecutorRouteStrategyEnum;
import com.bytter.job.admin.core.scheduler.MisfireStrategyEnum;
import com.bytter.job.admin.core.scheduler.ScheduleTypeEnum;
import com.bytter.job.admin.core.thread.JobScheduleHelper;
import com.bytter.job.admin.core.thread.JobTriggerPoolHelper;
import com.bytter.job.admin.core.trigger.TriggerTypeEnum;
import com.bytter.job.admin.core.util.I18nUtil;
import com.bytter.job.admin.dao.XxlJobGroupDao;
import com.bytter.job.admin.dao.XxlJobInfoDao;
import com.bytter.job.admin.service.LoginService;
import com.bytter.job.admin.service.XxlJobInfoSortService;
import com.bytter.job.admin.service.XxlJobService;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.enums.ExecutorBlockStrategyEnum;
import com.xxl.job.core.glue.GlueTypeEnum;
import com.xxl.job.core.util.DateUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.*;
/**
 * index controller
 * @author xuxueli 2015-12-19 16:13:16
 */
@Controller
@RequestMapping("/jobinfo")
public class JobInfoController {
	private static Logger logger = LoggerFactory.getLogger(JobInfoController.class);
	@Resource
	private XxlJobGroupDao xxlJobGroupDao;
	@Resource
	private XxlJobService xxlJobService;
	@Resource
	private XxlJobInfoSortService xxlJobInfoSortService;
	@Resource
	private XxlJobInfoDao xxlJobInfoDao;

	@RequestMapping
	public String index(HttpServletRequest request, Model model, @RequestParam(required = false, defaultValue = "-1") int jobGroup) {
		// 枚举-字典
		model.addAttribute("ExecutorRouteStrategyEnum", ExecutorRouteStrategyEnum.values());	    // 路由策略-列表
		model.addAttribute("GlueTypeEnum", GlueTypeEnum.values());								// Glue类型-字典
		model.addAttribute("ExecutorBlockStrategyEnum", ExecutorBlockStrategyEnum.values());	    // 阻塞处理策略-字典
		model.addAttribute("ScheduleTypeEnum", ScheduleTypeEnum.values());	    				// 调度类型
		model.addAttribute("MisfireStrategyEnum", MisfireStrategyEnum.values());	    			// 调度过期策略
		// 执行器列表
		List<XxlJobGroup> jobGroupList_all =  xxlJobGroupDao.findAll();
		// filter group
		List<XxlJobGroup> jobGroupList = filterJobGroupByRole(request, jobGroupList_all);
		if (jobGroupList==null || jobGroupList.size()==0) {
			throw new XxlJobException(I18nUtil.getString("jobgroup_empty"));
		}
		// 获取登陆用户角色
		XxlJobUser loginUser = (XxlJobUser) request.getAttribute(LoginService.LOGIN_IDENTITY_KEY);
		model.addAttribute("JobGroupList", jobGroupList);
		model.addAttribute("jobGroup", jobGroup);
		model.addAttribute("userRole", (loginUser != null ? loginUser.getRole() : 0));
		return "jobinfo/jobinfo.index";
	}
	public static List<XxlJobGroup> filterJobGroupByRole(HttpServletRequest request, List<XxlJobGroup> jobGroupList_all){
		List<XxlJobGroup> jobGroupList = new ArrayList<>();
		if (jobGroupList_all!=null && jobGroupList_all.size()>0) {
			XxlJobUser loginUser = (XxlJobUser) request.getAttribute(LoginService.LOGIN_IDENTITY_KEY);
			if (loginUser.getRole() == 1) {
				jobGroupList = jobGroupList_all;
			} else {
				List<String> groupIdStrs = new ArrayList<>();
				if (loginUser.getPermission()!=null && loginUser.getPermission().trim().length()>0) {
					groupIdStrs = Arrays.asList(loginUser.getPermission().trim().split(","));
				}
				for (XxlJobGroup groupItem:jobGroupList_all) {
					if (groupIdStrs.contains(String.valueOf(groupItem.getId()))) {
						jobGroupList.add(groupItem);
					}
				}
			}
		}
		return jobGroupList;
	}
	public static void validPermission(HttpServletRequest request, int jobGroup) {
		XxlJobUser loginUser = (XxlJobUser) request.getAttribute(LoginService.LOGIN_IDENTITY_KEY);
		if (!loginUser.validPermission(jobGroup)) {
			throw new RuntimeException(I18nUtil.getString("system_permission_limit") + "[username="+ loginUser.getUsername() +"]");
		}
	}

	@RequestMapping("/pageList")
	@ResponseBody
	public Map<String, Object> pageList(@RequestParam(required = false, defaultValue = "0") int start,
			@RequestParam(required = false, defaultValue = "10") int length,
			int jobGroup, int triggerStatus,@RequestParam(required = false, defaultValue = "0") int jobId, String jobDesc, String executorHandler, String author) {

		return xxlJobService.pageList(start, length, jobGroup, triggerStatus, jobId, jobDesc, executorHandler, author);
	}

	/**
	 * 分页查询(支持动态排序),供内管端租户定时任务分配列表使用
	 * 独立于原 pageList,不改动原 XXL-Job 管理端查询
	 *
	 * @param tenantId 当前租户ID，仅查询默认、未归属及当前租户的任务
	 * @param orderBy  排序字段(白名单字段,为空默认 id)
	 * @param orderDir 排序方向 asc/desc,为空默认 desc
	 */
	@RequestMapping("/pageListSort")
	@ResponseBody
	public Map<String, Object> pageListSort(@RequestParam(required = false, defaultValue = "0") int start,
			@RequestParam(required = false, defaultValue = "10") int length,
			int jobGroup, int triggerStatus, int jobId, String jobDesc, String executorHandler, String author,
			@RequestParam(required = false) String tenantId,
			@RequestParam(required = false) String orderBy, @RequestParam(required = false) String orderDir) {

		return xxlJobInfoSortService.pageList(start, length, jobGroup, triggerStatus, jobId, jobDesc, executorHandler, author, tenantId, orderBy, orderDir);
	}

	@RequestMapping("/loadByHandler")
	@ResponseBody
	@PermissionLimit(limit = false)
	public ReturnT<XxlJobInfo> loadByName(int jobGroup, String executorHandler) {
		// 检查任务是否已存在
		XxlJobInfo existJob = xxlJobService.findByJobGroupAndExecutorHandler(jobGroup, executorHandler).getContent();
		return existJob != null ? new ReturnT<>(existJob) : new ReturnT<>(ReturnT.FAIL_CODE, null);
	}

	@PostMapping("/save")
	@ResponseBody
	@PermissionLimit(limit = false)
	public ReturnT<String> save(@RequestBody XxlJobInfo jobInfo) {
		// 检查任务是否已存在
		XxlJobInfo existJob = xxlJobService.findByJobGroupAndExecutorHandler(
				jobInfo.getJobGroup(), jobInfo.getExecutorHandler()).getContent();

		if (existJob != null) {
			// 更新已有任务
			existJob.setScheduleConf(jobInfo.getScheduleConf());
			existJob.setAuthor(jobInfo.getAuthor());
			existJob.setAlarmEmail(jobInfo.getAlarmEmail());
			existJob.setExecutorRouteStrategy(jobInfo.getExecutorRouteStrategy());
			existJob.setMisfireStrategy(jobInfo.getMisfireStrategy());
			existJob.setExecutorBlockStrategy(jobInfo.getExecutorBlockStrategy());
			existJob.setExecutorTimeout(jobInfo.getExecutorTimeout());
			existJob.setExecutorFailRetryCount(jobInfo.getExecutorFailRetryCount());
			xxlJobService.update(existJob);
			return ReturnT.SUCCESS;
		}

		// 保存新任务
		xxlJobService.add(jobInfo);
		return ReturnT.SUCCESS;
	}

	@RequestMapping("/add")
	@ResponseBody
	@OperationLog(value = Constants.JOB_OPERATE_ADD)
	@PermissionLimit(adminuser = true, jobAccess = true)
	public ReturnT<String> add(XxlJobInfo jobInfo) {
		return xxlJobService.add(jobInfo);
	}

	@RequestMapping("/update")
	@ResponseBody
	@OperationLog(value = Constants.JOB_OPERATE_UPDATE)
	@PermissionLimit(jobAccess = true)
	public ReturnT<String> update(XxlJobInfo jobInfo) {
		return xxlJobService.update(jobInfo);
	}

	@RequestMapping("/remove")
	@ResponseBody
	@OperationLog(value = Constants.JOB_OPERATE_DELETE)
	@PermissionLimit(jobAccess = true)
	public ReturnT<String> remove(int id) {
		return xxlJobService.remove(id);
	}

	@RequestMapping("/stop")
	@ResponseBody
	@OperationLog(value = Constants.JOB_OPERATE_STOP)
	@PermissionLimit(jobAccess = true)
	public ReturnT<String> pause(int id) {
		return xxlJobService.stop(id);
	}

	@RequestMapping("/start")
	@ResponseBody
	@OperationLog(value = Constants.JOB_OPERATE_START)
	@PermissionLimit(jobAccess = true)
	public ReturnT<String> start(int id) {
		return xxlJobService.start(id);
	}

	@RequestMapping("/trigger")
	@ResponseBody
	@OperationLog(value = Constants.JOB_OPERATE_TRIGGER)
	@PermissionLimit(jobAccess = true)
	public ReturnT<String> triggerJob(int id, String executorParam, String addressList) {
		// force cover job param
		if (executorParam == null) {
			executorParam = "";
		}
		JobTriggerPoolHelper.trigger(id, TriggerTypeEnum.MANUAL, -1, null, executorParam, addressList);
		return ReturnT.SUCCESS;
	}
	@RequestMapping("/nextTriggerTime")
	@ResponseBody
	public ReturnT<List<String>> nextTriggerTime(String scheduleType, String scheduleConf) {
		XxlJobInfo paramXxlJobInfo = new XxlJobInfo();
		paramXxlJobInfo.setScheduleType(scheduleType);
		paramXxlJobInfo.setScheduleConf(scheduleConf);
		List<String> result = new ArrayList<>();
		try {
			Date lastTime = new Date();
			for (int i = 0; i < 5; i++) {
				lastTime = JobScheduleHelper.generateNextValidTime(paramXxlJobInfo, lastTime);
				if (lastTime != null) {
					result.add(DateUtil.formatDateTime(lastTime));
				} else {
					break;
				}
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return new ReturnT<List<String>>(ReturnT.FAIL_CODE, (I18nUtil.getString("schedule_type")+I18nUtil.getString("system_unvalid")) + e.getMessage());
		}
		return new ReturnT<List<String>>(result);
	}

	@RequestMapping("/loadById")
	@ResponseBody
	public XxlJobInfo loadById(int id){
		XxlJobInfo xxlJobInfo = xxlJobInfoDao.loadById(id);
		return xxlJobInfo;
	}

}
