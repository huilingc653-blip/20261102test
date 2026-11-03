package com.bytter.job.admin.controller;

import com.bytter.job.admin.core.model.XxlJobGroup;
import com.bytter.job.admin.core.model.XxlJobInfo;
import com.bytter.job.admin.core.model.XxlJobPermission;
import com.bytter.job.admin.core.model.XxlJobUser;
import com.bytter.job.admin.core.vo.JobPermissionVO;
import com.bytter.job.admin.dao.XxlJobGroupDao;
import com.bytter.job.admin.dao.XxlJobInfoDao;
import com.bytter.job.admin.dao.XxlJobPermissionDao;
import com.bytter.job.admin.service.LoginService;
import com.xxl.job.core.biz.model.ReturnT;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 任务权限配置。
 *
 * @author zhaohc
 * @date 2025/6/25 9:32
 */
@Slf4j
@RestController
@RequestMapping("/jobpermission")
public class JobPermissionController {
    @Resource
    private XxlJobPermissionDao xxlJobPermissionDao;
    @Resource
    private XxlJobInfoDao xxlJobInfoDao;
    @Resource
    private XxlJobGroupDao xxlJobGroupDao;


    @PostMapping("/pageList")
    public Map<String, Object> pageList(
            @RequestParam("userId") int userId,
            @RequestParam(value = "start", defaultValue = "0") int start,
            @RequestParam(value = "length", defaultValue = "10") int length,
            @RequestParam(value = "jobGroup", required = false) Integer jobGroup,
            @RequestParam(value = "jobDesc", required = false) String jobDesc) {

        // 查询任务总数
        int total = xxlJobInfoDao.pageAllListCount(jobGroup, jobDesc);

        // 分页查询任务列表
        List<XxlJobInfo> jobList = xxlJobInfoDao.pageAllList(start, length, jobGroup, jobDesc);

        // 查询用户已有权限
        Map<Integer, XxlJobPermission> userPermissions = xxlJobPermissionDao.findByUserId(userId)
                .stream()
                .collect(Collectors.toMap(XxlJobPermission::getJobId, Function.identity()));
        // 获取任务组名称
        List<XxlJobGroup> jobGroupList = xxlJobGroupDao.findAll();
        Map<Integer, String> gropMap = jobGroupList.stream().collect(Collectors.toMap(XxlJobGroup::getId, XxlJobGroup::getTitle));

        // 构建返回数据
        List<JobPermissionVO> data = new ArrayList<>();
        for (XxlJobInfo job : jobList) {
            JobPermissionVO vo = new JobPermissionVO();
            vo.setId(job.getId());
            vo.setJobDesc(job.getJobDesc());
            vo.setJobGroupTitle(gropMap.getOrDefault(job.getJobGroup(), ""));

            XxlJobPermission permission = userPermissions.get(job.getId());
            vo.setPermissionType(permission != null ? permission.getPermissionType() : 0);

            data.add(vo);
        }

        Map<String, Object> result = new HashMap<>();
        result.put("recordsTotal", total);
        result.put("recordsFiltered", total);
        result.put("data", data);

        return result;
    }

    @PostMapping("/save")
    public ReturnT<String> save(HttpServletRequest request, @RequestBody List<XxlJobPermission> permissions) {
        if (permissions == null || permissions.isEmpty()) {
            return ReturnT.FAIL;
        }
        // 获取登陆用户角色
        XxlJobUser loginUser = (XxlJobUser) request.getAttribute(LoginService.LOGIN_IDENTITY_KEY);
        if (loginUser == null) {
            return new ReturnT<>(ReturnT.FAIL_CODE, "没有权限");
        }
        for (XxlJobPermission permission : permissions) {
            if (permission.getPermissionType() > 0) {
                // 保存或更新
                XxlJobPermission exist = xxlJobPermissionDao.findByUserAndJob(
                        permission.getUserId(), permission.getJobId());
                if (exist != null) {
                    exist.setPermissionType(permission.getPermissionType());
                    exist.setUpdateBy(loginUser.getUsername());
                    exist.setUpdateTime(new Date());
                    xxlJobPermissionDao.update(exist);
                } else {
                    permission.setCreateBy(loginUser.getUsername());
                    permission.setCreateTime(new Date());
                    xxlJobPermissionDao.save(permission);
                }
            } else {
                // 删除权限
                xxlJobPermissionDao.deleteByUserAndJob(permission.getUserId(), permission.getJobId());
            }
        }

        return ReturnT.SUCCESS;
    }

    @PostMapping("/batchSave")
    public ReturnT<String> saveAllPermissions(HttpServletRequest request, Integer userId, Integer permissionType,
                                              @RequestParam(value = "jobGroup", required = false) Integer jobGroup,
                                              @RequestParam(value = "jobDesc", required = false) String jobDesc) {
        if (ObjectUtils.isEmpty(userId) || ObjectUtils.isEmpty(permissionType)) {
            return ReturnT.FAIL;
        }
        // 获取登陆用户角色
        XxlJobUser loginUser = (XxlJobUser) request.getAttribute(LoginService.LOGIN_IDENTITY_KEY);
        if (loginUser == null) {
            return new ReturnT<>(ReturnT.FAIL_CODE, "没有权限");
        }
        // 批量变更所有任务权限，删除所有任务权限，重新保存
        int deleteCount = xxlJobPermissionDao.deleteByUserId(userId);
        // 批量添加用户任务权限，permissionType：1-只读，2-读写，0-不可见
        int batchCount = 0;
        // 不可见时不需要添加记录
        if (permissionType > 0) {
            batchCount = xxlJobPermissionDao.batchSavePermissions(userId, permissionType, loginUser.getUsername(), jobGroup, jobDesc);
        }
        log.info("批量保存用户任务权限，删除{}条，添加{}条", deleteCount, batchCount);
        return ReturnT.SUCCESS;
    }
}
