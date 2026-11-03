package com.bytter.third.service;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.date.*;
import cn.hutool.core.io.IORuntimeException;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.bytter.core.cache.redis.core.RedisCacheKey;
import com.bytter.core.utils.RedisUtils;
import com.bytter.system.api.ICorpService;
import com.bytter.system.api.IUserService;
import com.bytter.system.api.dto.CorpDTO;
import com.bytter.system.api.dto.UserDTO;
import com.bytter.third.dto.DslResponse;
import com.bytter.third.dto.DslToken;
import com.bytter.third.dto.DslUserInfo;
import com.xxl.job.core.context.XxlJobHelper;
import com.xxl.job.core.handler.annotation.XxlJob;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 大参林SSO用户信息同步业务。
 *
 * @author zhaohc
 * @since 2024/5/27 19:53
 */
@Slf4j
@Component
public class DslSsoSyncUserService {

    @Resource
    private RedisUtils redisUtils;

    private static final String SUCCESS_CODE = "000000";
    private static final String JOB_PARAMS_START = "startDate";
    private static final String JOB_PARAMS_END = "endDate";
    private static final String JOB_PARAMS_CORP_ID = "corpId";
    /**
     * 单线程最多处理员工信息数目
     */
    private static final Integer THREAD_COUNT = 1000;
    private static final Integer REPEAT_MAX = 5;

    @Value("${sso.service.dsl.clientId}")
    private String clientId;
    @Value("${sso.service.dsl.clientSecret}")
    private String clientSecret;
    @Value("${sso.service.dsl.tokenUrl}")
    private String tokenUrl;
    @Value("${sso.service.dsl.listEmpUrl}")
    private String listEmpUrl;

    @Autowired(required = false)
    @Qualifier(value = "asyncThreadPoolExecutor")
    private ThreadPoolTaskExecutor asyncThreadPoolExecutor;
    @Autowired(required = false)
    @Qualifier(value = "asyncDslPoolExecutor")
    private ThreadPoolTaskExecutor asyncDslPoolExecutor;
    @DubboReference(check = false)
    private IUserService userService;
    @DubboReference(check = false)
    private ICorpService corpService;

    @XxlJob(value = "dslSsoSyncUserJob")
    public void dslSsoSyncUserJob() {
        String jobParam = XxlJobHelper.getJobParam();
        log.info("同步SSO员工信息接口开始，参数：【{}】", jobParam);
        // 获取接口请求Token
        String tokenData = getApiToken();
        XxlJobHelper.log("获取Token信息：{}", tokenData);
        if (ObjectUtil.isEmpty(tokenData)) {
            log.error("获取API接口鉴权token异常!");
            return;
        }
        // 组装接口鉴权Token信息
        DslToken token = JSONUtil.toBean(tokenData, DslToken.class);
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        headers.put("Authorization", token.getTokenType() + " " + token.getAccessToken());
        // 分页获取用户信息--根据变更时间获取
        // 如果传参则以传参日期开始同步，不传参则取当天日期
        if (ObjectUtil.isNotEmpty(jobParam)) {
            JSONObject jobData = JSONUtil.parseObj(jobParam);
            String startDate = jobData.getStr(JOB_PARAMS_START);
            String endDate = jobData.getStr(JOB_PARAMS_END);
            String corpId = jobData.getStr(JOB_PARAMS_CORP_ID);
            // 校验CorpId是否合法
            CorpDTO defaultCorp;
            if (ObjectUtil.isNotEmpty(corpId)) {
                defaultCorp = corpService.get(corpId);
                XxlJobHelper.log("当前同步默认单位信息：【{}】", defaultCorp);
                if (ObjectUtil.isEmpty(defaultCorp)) {
                    XxlJobHelper.log("输入单位编号【{}】不存在，默认设置所属单位为空！", corpId);
                }
            } else {
                defaultCorp = null;
            }
            if (validateDate(startDate)) {
                // 如果设置了结束日期，则以结束日期为准，未输入则以当日为止
                Date endTime = DateUtil.date();
                if (validateDate(endDate)) {
                    endTime = DateUtil.parse(endDate);
                }
                XxlJobHelper.log("同步【{}】-【{}】的员工信息。。。。。。", startDate, DateUtil.formatDate(endTime));
                // 多日的信息，以线程池来执行
                long between = DateUtil.between(DateUtil.parse(startDate), endTime, DateUnit.DAY);
                // 同步指定日期的员工信息
                if (between > 0) {
                    // 多线程异步同步员工信息
                    List<CompletableFuture<Void>> futures = new ArrayList<>();
                    for (int i = 0; i <= between; i++) {
                        // 逐日同步
                        DateTime syncDate = DateUtil.parse(startDate).offset(DateField.DAY_OF_MONTH, i);
                        XxlJobHelper.log("开始同步【{}】日员工信息。。。。。。", DateUtil.formatDate(syncDate));
                        CompletableFuture<Void> future = CompletableFuture.runAsync(() ->
                                        syncEmployeeByDate(headers, syncDate, defaultCorp, 1, 0, 0),
                                asyncThreadPoolExecutor);
                        futures.add(future);
                    }
                    // 使用CompletableFuture.allOf等待所有任务完成
                    CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join();
                    XxlJobHelper.log("所有员工信息同步完成。。。。。。");
                } else {
                    syncEmployeeByDate(headers, DateUtil.parse(startDate), defaultCorp, 1, 0, 0);
                }
            } else {
                XxlJobHelper.log("startDate【{}】参数异常，取当日日期开始。", startDate);
                // 同步当日的员工信息
                syncEmployeeByDate(headers, DateUtil.date(), defaultCorp, 1, 0, 0);
            }
        } else {
            // 同步当日的员工信息
            syncEmployeeByDate(headers, DateUtil.date(), null, 1, 0, 0);
        }
        log.info("同步SSO员工信息接口结束。");
    }

    /**
     * 辅助方法：同步指定日期的员工信息。
     *
     * @param headers  接口鉴权Token信息。
     * @param syncDate 指定日期。
     * @param pageNum  当前页码。
     * @param maxNum   最大页码。
     */
    private void syncEmployeeByDate(Map<String, String> headers, Date syncDate, CorpDTO defaultCorp, int pageNum, int maxNum, int index) {
        Map<String, Object> params = new HashMap<>();
        params.put("pageNum", pageNum);
        params.put("pageSize", 100);
        // 一天天同步员工信息
        params.put("updateTimeStart", DateUtil.beginOfDay(syncDate).toString());
        params.put("updateTimeEnd", DateUtil.endOfDay(syncDate).toString());
        // 获取员工信息
        String result = "";
        try {
            result = HttpUtil.createPost(listEmpUrl).addHeaders(headers).body(JSONUtil.toJsonStr(params)).execute().body();
        } catch (IORuntimeException e) {
            index++;
            XxlJobHelper.log("【{}】【页码：{}】查询请求异常【{}】，重新发起请求【{}】。", DateUtil.formatDate(syncDate), pageNum, e.getMessage(), index);
            log.error("【{}】【页码：{}】查询请求异常【{}】，重新发起请求【{}】。", DateUtil.formatDate(syncDate), pageNum, e.getMessage(), index);
            if (index <= REPEAT_MAX) {
                // 连接失败重新连接
                syncEmployeeByDate(headers, syncDate, defaultCorp, pageNum, maxNum, index);
            } else {
                XxlJobHelper.log("【{}】【页码：{}】查询请求异常【{}】，重试次数【{}】达到上限，不再重试。", DateUtil.formatDate(syncDate), pageNum, e.getMessage(), index);
                log.error("【{}】【页码：{}】查询请求异常【{}】，重试次数【{}】达到上限，不再重试。", DateUtil.formatDate(syncDate), pageNum, e.getMessage(), index);
            }
            return;
        }
        // 解析返回值
        DslResponse tokenResponse = JSONUtil.toBean(result, DslResponse.class);
        if (ObjectUtil.equal(tokenResponse.getCode(), SUCCESS_CODE)) {
            // 清除当前线程的重试次数
            index = 0;
            // 解析用户信息
            List<UserDTO> userList = null;
            List<UserDTO> saveUserList = null;
            List<UserDTO> updateUserList = null;
            List<String> employeeIdList = null;
            List<Map<String, Object>> userIdMap = null;
            CorpDTO corp = null;
            if (tokenResponse.getTotal() > 0) {
                saveUserList = new ArrayList<>();
                updateUserList = new ArrayList<>();
                // 将员工信息转换为用户信息
                userList = DslUserInfo.toUserList(JSONUtil.parseArray(tokenResponse.getData()).toList(DslUserInfo.class), defaultCorp);
                // 统计每个employeeId出现的次数
                Map<String, Long> duplicateCounts = userList.stream()
                        .collect(Collectors.groupingBy(UserDTO::getEmployeeId, Collectors.counting()));
                // 找出出现次数大于1的employeeId，即重复的数据
                userList.stream()
                        .filter(user -> duplicateCounts.getOrDefault(user.getEmployeeId(), 0L) > 1)
                        .forEach(item -> {
                            XxlJobHelper.log("【{}】用户【{}】关联员工编号【{}】重复，已忽略。", DateUtil.formatDate(syncDate), item.getUsername(), item.getEmployeeId());
                            log.info("【{}】用户【{}】关联员工编号【{}】重复，已忽略。", DateUtil.formatDate(syncDate), item.getUsername(), item.getEmployeeId());
                        });
                // 获取不重复的数据，以后出现的数据覆盖先出现的数据
                Map<String, UserDTO> uniqueUsers = userList.stream().collect(Collectors.toMap(UserDTO::getEmployeeId,
                        Function.identity(), (existing, replacement) -> replacement));
                // 收集过滤后的非重复用户列表
                userList = new ArrayList<>(uniqueUsers.values());
                // 获取去重后的员工编号
                employeeIdList = userList.stream().map(UserDTO::getEmployeeId).collect(Collectors.toList());
                userIdMap = userService.getUserIdMapByEmployeeIdList(employeeIdList);
                for (UserDTO userDTO : userList) {
                    Optional<Map<String, Object>> mapData = userIdMap.stream().filter(map -> ObjectUtil.equal(map.get("employeeId"), userDTO.getEmployeeId())).distinct().findFirst();
                    if (mapData.isPresent()) {
                        String userId = Convert.toStr(mapData.get().get("id"));
                        String corpId = Convert.toStr(mapData.get().get("corpId"));
                        Integer status = Convert.toInt(mapData.get().get("status"));
                        userDTO.setId(userId);
                        // 如果用户已分配单位，则以分配后的单位为准
                        if (ObjectUtil.isAllNotEmpty(corpId, defaultCorp) && ObjectUtil.notEqual(corpId, defaultCorp.getId())) {
                            corp = new CorpDTO();
                            corp.setId(corpId);
                            userDTO.setCorp(corp);
                        }
                        // 只有系统用户状态为1-正常，且SSO端状态为2-禁用，才同步状态，避免同步大量可用用户
                        if (ObjectUtil.equal(status, 1) && ObjectUtil.equal(userDTO.getStatus(), 2)) {
                            XxlJobHelper.log("【{}】用户【{}】状态为【{}】，已同步状态为【{}】。", DateUtil.formatDate(syncDate), userDTO.getUsername(), status, 2);
                            log.info("【{}】用户【{}】状态为【{}】，已同步状态为【{}】。", DateUtil.formatDate(syncDate), userDTO.getUsername(), status, 2);
                            userDTO.setStatus(2);
                        }
                        updateUserList.add(userDTO);
                    } else {
                        // 新增用户统一为禁用状态，避免同步大量可用用户
                        userDTO.setStatus(2);
                        saveUserList.add(userDTO);
                    }
                }
                // 添加员工信息
                if (ObjectUtil.isNotEmpty(saveUserList)) {
                    XxlJobHelper.log("新增【{}】【页码：{}】【{}】个员工信息。", DateUtil.formatDate(syncDate), pageNum, saveUserList.size());
                    log.info("新增【{}】【页码：{}】【{}】个员工信息。", DateUtil.formatDate(syncDate), pageNum, saveUserList.size());
                    userService.saveUserNoCorp(saveUserList, "DSL");
                }
                // 变更员工信息
                if (ObjectUtil.isNotEmpty(updateUserList)) {
                    XxlJobHelper.log("修改【{}】【页码：{}】【{}】个员工信息。", DateUtil.formatDate(syncDate), pageNum, updateUserList.size());
                    log.info("修改【{}】【页码：{}】【{}】个员工信息。", DateUtil.formatDate(syncDate), pageNum, updateUserList.size());
                    userService.updateUserNoCorp(updateUserList, "DSL");
                }
                // 第一次请求时，根据总行数，拆分线程池
                if (pageNum == 1 && tokenResponse.getNext()) {
                    queryByThreadPool(headers, syncDate, defaultCorp, tokenResponse);
                }
                // 如果有下一页，则继续同步
                if (tokenResponse.getNext() && pageNum <= maxNum) {
                    // 遍历员工数据
                    syncEmployeeByDate(headers, syncDate, defaultCorp, pageNum + 1, maxNum, index);
                }
                if (!tokenResponse.getNext()) {
                    XxlJobHelper.log("【{}】日【{}】员工信息同步完成。", DateUtil.formatDate(syncDate), tokenResponse.getTotal());
                    log.info("【{}】日【{}】员工信息同步完成。", DateUtil.formatDate(syncDate), tokenResponse.getTotal());
                }
            } else {
                XxlJobHelper.log("获取【{}】员工列表【页码：{}】信息为空。", DateUtil.formatDate(syncDate), pageNum);
            }
        } else {
            index++;
            XxlJobHelper.log("获取【{}】员工列表【页码：{}】信息异常：{}，重新发起请求【{}】。", DateUtil.formatDate(syncDate), pageNum, tokenResponse.getMsg(), index);
            log.error("获取【{}】员工列表【页码：{}】信息异常：{}，重新发起请求【{}】。", DateUtil.formatDate(syncDate), pageNum, tokenResponse.getMsg(), index);
            if (index <= REPEAT_MAX) {
                // 连接失败重新连接
                syncEmployeeByDate(headers, syncDate, defaultCorp, pageNum, maxNum, index);
            } else {
                XxlJobHelper.log("获取【{}】员工列表【页码：{}】信息异常：{}，重试次数【{}】达到上限，不再重试。", DateUtil.formatDate(syncDate), pageNum, tokenResponse.getMsg(), index);
                log.error("获取【{}】员工列表【页码：{}】信息异常：{}，重试次数【{}】达到上限，不再重试。", DateUtil.formatDate(syncDate), pageNum, tokenResponse.getMsg(), index);
            }
        }
    }

    private void queryByThreadPool(Map<String, String> headers, Date syncDate, CorpDTO defaultCorp, DslResponse tokenResponse) {
        Integer total = tokenResponse.getTotal();
        Integer pageSize = tokenResponse.getPageSize();
        // 总页码数
        int pageTotal = total % pageSize == 0 ? (total / pageSize) : (total / pageSize) + 1;
        // 总线程数
        int threadNum = total % THREAD_COUNT == 0 ? (total / THREAD_COUNT) : (total / THREAD_COUNT) + 1;
        // 单线程处理页数
        int limit = THREAD_COUNT / pageSize;
        // 多线程异步同步员工信息
        List<CompletableFuture<Void>> futures = new ArrayList<>();
        for (int i = 0; i < threadNum; i++) {
            int maxNum = Math.min(pageTotal, (i + 1) * limit);
            int minNum = (i == 0 ? 2 : i * limit + 1);
            XxlJobHelper.log("线程池【{}】同步【{}】员工信息：开始页码【{}】，结束页码【{}】。。。。。。", i, DateUtil.formatDate(syncDate), minNum, maxNum);
            log.info("线程池【{}】同步【{}】员工信息：开始页码【{}】，结束页码【{}】。。。。。。", i, DateUtil.formatDate(syncDate), minNum, maxNum);
            CompletableFuture<Void> future = CompletableFuture.runAsync(() ->
                            syncEmployeeByDate(headers, syncDate, defaultCorp, minNum, maxNum, 0),
                    asyncDslPoolExecutor);
            futures.add(future);
        }
        // 使用CompletableFuture.allOf等待所有任务完成
        XxlJobHelper.log("线程池同步请求【发送】完成。。。。。。");
        CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join();
        XxlJobHelper.log("线程池同步请求【执行】完成。。。。。。");
        // return maxNum;
    }

    /**
     * 辅助方法：获取接口的鉴权Token。
     *
     * @return 鉴权Token。
     */
    private String getApiToken() {
        String tokenData = "";
        String redisKey = RedisCacheKey.makeRedisKey(RedisCacheKey.THIRD_DSL_API_TOKEN);
        Object token = redisUtils.get(redisKey);
        if (ObjectUtil.isEmpty(token)) {
            Map<String, Object> params = new HashMap<>();
            params.put("client_id", clientId);
            params.put("grant_type", "client_credentials");
            params.put("client_secret", clientSecret);
            String tokenResult = HttpUtil.get(tokenUrl, params);
            XxlJobHelper.log("获取API接口鉴权token结果：{}", tokenResult);
            DslResponse tokenResponse = JSONUtil.toBean(tokenResult, DslResponse.class);
            if (ObjectUtil.equal(tokenResponse.getCode(), SUCCESS_CODE)) {
                tokenData = JSONUtil.toJsonStr(tokenResponse.getData());
                DslToken dslToken = JSONUtil.toBean(tokenData, DslToken.class);
                redisUtils.put(redisKey, tokenData, dslToken.getExpiresIn());
            } else {
                XxlJobHelper.log("获取API接口鉴权token异常：{}", tokenResponse.getMsg());
            }
        } else {
            tokenData = token.toString();
        }
        return tokenData;
    }

    private static boolean validateDate(String dateStr) {
        try {
            // 尝试使用默认的日期时间格式解析字符串
            Date date = DateUtil.parse(dateStr);
            return date != null;
        } catch (DateException e) {
            // 如果解析失败，说明字符串不是一个有效的日期时间
            XxlJobHelper.log("startDate【{}】不是一个有效的日期时间字符串。", dateStr);
        }
        return false;
    }


}
