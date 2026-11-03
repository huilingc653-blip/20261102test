package com.bytter.job.admin.controller;

import com.bytter.job.admin.core.model.XxlJobLoginLog;
import com.bytter.job.admin.dao.XxlJobLoginLogDao;
import com.xxl.job.core.util.DateUtil;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 登录日志操作类。
 *
 * @author zhaohc
 * @date 2025/6/27 14:53
 */
@Controller
@RequestMapping("/loginlog")
public class LoginLogController {
    @Resource
    private XxlJobLoginLogDao xxlJobLoginLogDao;

    @RequestMapping
    public String index(Model model) {
        return "loginlog/loginlog.index";
    }

    @PostMapping("/pageList")
    @ResponseBody
    public Map<String, Object> pageList(
            @RequestParam(required = false, defaultValue = "0") int start,
            @RequestParam(required = false, defaultValue = "10") int length,
            @RequestParam(required = false, defaultValue = "-1") Integer status,
            @RequestParam(required = false) String username,
            @RequestParam(required = false) String filterTime) {

        // 转换日期
        Date startTime = null;
        Date endTime = null;
        if (filterTime!=null && filterTime.trim().length()>0) {
            String[] temp = filterTime.split(" - ");
            if (temp.length == 2) {
                startTime = DateUtil.parseDateTime(temp[0]);
                endTime = DateUtil.parseDateTime(temp[1]);
            }
        }

        // 查询日志数量
        int count = xxlJobLoginLogDao.pageListCount(username, status, startTime, endTime);

        // 分页查询日志列表
        List<XxlJobLoginLog> list = xxlJobLoginLogDao.pageList(
                start, length, username, status, startTime, endTime);

        Map<String, Object> result = new HashMap<>();
        result.put("recordsTotal", count);
        result.put("recordsFiltered", count);
        result.put("data", list);

        return result;
    }
}
