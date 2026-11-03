package com.bytter.system.service.impl;

import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpStatus;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.bytter.core.common.context.UserContextHelper;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.system.api.IXxlJobTemplate;
import com.bytter.system.api.vo.XxlJobGroup;
import com.bytter.system.api.vo.XxlJobInfo;
import com.bytter.system.api.vo.XxlJobPathEnum;
import com.bytter.system.dao.SysTenantManagerDao;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.net.HttpCookie;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * XXL接口工具实现类。
 *
 * @author zhaohc
 * @date 2023/10/25 17:46
 */
@Service("xxlJobTemplate")
@DubboService
@Slf4j
public class XxlJobTemplateImpl implements IXxlJobTemplate {

    @Value("${xxl.job.admin.addresses}")
    private String addresses;

    @Value("${xxl.job.admin.username}")
    private String username;

    @Value("${xxl.job.admin.password}")
    private String password;

    private String cookie;

    @Resource
    private SysTenantManagerDao sysTenantManagerDao;

    @Override
    public List<XxlJobGroup> listGroup(String appName, String title) {
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("appname", appName);
        paramMap.put("title", title);
        JSONObject pageGroup = doRequest(XxlJobPathEnum.PAGE_GROUP, paramMap);
        return JSONUtil.toList(pageGroup.getJSONArray("data"), XxlJobGroup.class);
    }

    @Override
    public List<XxlJobGroup> listGroupByExcMachine(String appName) {
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("appname", appName);
        JSONObject pageGroup = doRequest(XxlJobPathEnum.PAGE_GROUP, paramMap);
        return JSONUtil.toList(pageGroup.getJSONArray("data"), XxlJobGroup.class);
    }

    @Override
    public List<XxlJobInfo> listJob(int jobGroup, int triggerStatus, String jobDesc, String executorHandler, String author) {
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("jobGroup", jobGroup);
        paramMap.put("triggerStatus", triggerStatus);
        paramMap.put("jobDesc", jobDesc);
        paramMap.put("executorHandler", executorHandler);
        paramMap.put("author", author);
        JSONObject pageJob = doRequest(XxlJobPathEnum.PAGE_JOB, paramMap);
        return JSONUtil.toList(pageJob.getJSONArray("data"), XxlJobInfo.class);
    }

    @Override
    public XxlJobInfo getJobByExecutorHandler(String appName, String id) {
        List<XxlJobGroup> list = listGroupByExcMachine(appName);
        if (CollectionUtils.isEmpty(list) || list.size() > 1) {
            throw new RuntimeException(String.format("执行器不存在或xxl-job-admin任务查询结果不唯一:%s", appName));
        }
        XxlJobInfo xxlJobIn = getJobByExecutorHandler(id);
        if (ObjectUtils.isEmpty(xxlJobIn)) {
            throw new RuntimeException(String.format("xxl-job-admin任务查询结果不唯一:%s", xxlJobIn.toString()));
        }
        return xxlJobIn;
    }

    @Override
    public XxlJobInfo getJobByExecutorHandler(String id) {
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("id", Integer.valueOf(id));
        JSONObject pageJob = doRequest(XxlJobPathEnum.GET_JOB, paramMap);
        return JSONUtil.toBean(pageJob, XxlJobInfo.class);
    }

    @Override
    public XxlJobInfo getOneJob(int jobGroup, int triggerStatus, String jobDesc, String executorHandler, String author) {
        List<XxlJobInfo> xxlJobInfos = listJob(jobGroup, triggerStatus, jobDesc, executorHandler, author);
        if (xxlJobInfos.size() > 1) {
            throw new RuntimeException(String.format("xxl-job-admin任务查询结果不唯一:%s", xxlJobInfos));
        }
        return xxlJobInfos.isEmpty() ? null : xxlJobInfos.get(0);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Integer addJob(XxlJobInfo addJob, String appName) {
        List<XxlJobGroup> list = listGroupByExcMachine(appName);
        if(CollectionUtils.isEmpty(list) || list.size()>1) {
            throw new RuntimeException(String.format("执行器不存在或xxl-job-admin任务查询结果不唯一:%s", appName));
        }
        int jobgroup = list.get(0).getId();
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("jobGroup", jobgroup);
        paramMap.put("jobDesc", addJob.getJobDesc());
        paramMap.put("author", addJob.getAuthor());
        paramMap.put("scheduleType", addJob.getScheduleType());
        paramMap.put("scheduleConf", addJob.getScheduleConf());
        paramMap.put("misfireStrategy", "DO_NOTHING");
        paramMap.put("executorRouteStrategy", "ROUND");
        paramMap.put("executorHandler", addJob.getExecutorHandler());
        paramMap.put("executorParam", addJob.getExecutorParam());
        paramMap.put("executorBlockStrategy", "SERIAL_EXECUTION");
        paramMap.put("executorTimeout", 0);
        paramMap.put("executorFailRetryCount", 0);
        paramMap.put("glueType", "BEAN");
        paramMap.put("glueSource", "");
        paramMap.put("glueRemark", "GLUE代码初始化");
        paramMap.put("glueUpdatetime", null);
        paramMap.put("childJobId", "");
        paramMap.put("triggerStatus", addJob.getTriggerStatus());
        paramMap.put("triggerLastTime", 0);
        paramMap.put("triggerNextTime", 0);
        String tenantId = UserContextHelper.helper().getTenantId();
        paramMap.put("tenantId", tenantId);
        JSONObject result = doRequest(XxlJobPathEnum.ADD, paramMap);
        Integer jobId = result.getInt("content");
        saveDefaultTenantAssignment(tenantId, jobId);
        return jobId;
    }

    private void saveDefaultTenantAssignment(String tenantId, Integer jobId) {
        if (StrUtil.isBlank(tenantId)) {
            log.warn("新增XXL任务[{}]未获取到租户上下文，跳过默认租户分配", jobId);
            return;
        }
        if (jobId == null || jobId <= 0) {
            throw new IllegalStateException("新增XXL任务未返回有效任务ID，无法创建租户任务分配");
        }
        ICurrentUser currentUser = UserContextHelper.helper().getCurrentUser();
        Date now = new Date();
        sysTenantManagerDao.saveScheduledTaskAssignment(tenantId, jobId,
                currentUser == null ? null : currentUser.getId(), now);
    }

    @Override
    public void updateJob(XxlJobInfo updateXxlJob) {
        JSONObject paramMap = JSONUtil.parseObj(updateXxlJob);
        JSONObject result = doRequest(XxlJobPathEnum.UPDATE, paramMap);
        if (HttpStatus.HTTP_OK != result.getInt("code")) {
            throw new RuntimeException(String.format("xxl-job-admin更新Job失败:%s", result.getStr("msg")));
        }
    }

    @Override
    public void removeJob(int jobId) {
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("id", jobId);
        doRequest(XxlJobPathEnum.REMOVE, paramMap);
    }

    /**
     * 获取登录cookie
     *
     * @return
     */
    private String getCookie() {
        Map<String, Object> paramsMap = new HashMap<>();
        paramsMap.put("userName", username);
        paramsMap.put("password", password);
        // 避免配置路径多个/
        if (StrUtil.endWith(addresses, "/")) {
            addresses = addresses.substring(0, addresses.length() - 1);
        }
        String url = String.format("%s%s", addresses, XxlJobPathEnum.LOGIN.getPath());
        log.info("远程调用xxl-job-admin方法，请示地址：【{}】，请求参数：【{}】。", url, paramsMap);
        HttpResponse response = HttpRequest.post(url).form(paramsMap).execute();
        if (HttpStatus.HTTP_OK != response.getStatus()) {
            throw new RuntimeException(String.format("xxl-job-admin登录失败:statusCode=%s", response.getStatus()));
        }
        List<HttpCookie> cookies = response.getCookies();

        if (cookies.isEmpty()) {
            throw new RuntimeException(String.format("xxl-job-admin登录失败:[userName=%s,password=%s]", username, password));
        }
        return cookies.stream().map(HttpCookie::toString).collect(Collectors.joining());
    }

    /**
     * 远程调用xxl-job-admin方法。
     *
     * @param xxlJobPathEnum XXL请求地址。
     * @param paramMap       请示参数。
     * @return 返回结果。
     */
    private JSONObject doRequest(XxlJobPathEnum xxlJobPathEnum, Map<String, Object> paramMap) {
        if (StrUtil.isBlank(cookie)) {
            cookie = getCookie();
        }
        // 避免配置路径多个/
        if (StrUtil.endWith(addresses, "/")) {
            addresses = addresses.substring(0, addresses.length() - 1);
        }
        String url = String.format("%s%s", addresses, xxlJobPathEnum.getPath());
        log.info("远程调用xxl-job-admin方法，请示地址：【{}】，请求参数：【{}】。", url, paramMap);
        HttpResponse response = HttpRequest.post(url).cookie(cookie).form(paramMap).execute();
        log.info("xxl-job-admin响应结果：【{}】", response.getStatus());
        if (HttpStatus.HTTP_OK != response.getStatus()) {
            throw new RuntimeException(String.format("xxl-job-admin%s请求失败:statusCode=%s",
                    xxlJobPathEnum.getDesc(), response.getStatus()));
        }
        JSONObject result = JSONUtil.parseObj(response.body());

        Integer code = result.getInt("code");
        if (code != null && HttpStatus.HTTP_OK != code) {
            throw new RuntimeException(String.format("xxl-job-admin%s失败:msg=%s", xxlJobPathEnum.getDesc(), result.getStr("msg")));
        }
        return result;
    }
}
