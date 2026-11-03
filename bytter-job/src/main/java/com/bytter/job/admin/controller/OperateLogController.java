package com.bytter.job.admin.controller;

import com.bytter.job.admin.core.model.XxlJobOperateLog;
import com.bytter.job.admin.dao.XxlJobOperateLogDao;
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
 * 操作日志控制器。
 *
 * @author zhaohc
 * @date 2025/6/27 14:56
 */
@Controller
@RequestMapping("/operatelog")
public class OperateLogController {
    @Resource
    private XxlJobOperateLogDao xxlJobOperateLogDao;

    @RequestMapping
    public String index(Model model) {
        return "operatelog/operatelog.index";
    }

    @PostMapping("/pageList")
    @ResponseBody
    public Map<String, Object> pageList(
            @RequestParam(required = false, defaultValue = "0") int start,
            @RequestParam(required = false, defaultValue = "10") int length,
            @RequestParam(required = false) String operateType,
            @RequestParam(required = false) String username,
            @RequestParam(required = false) String jobDesc,
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
        int count = xxlJobOperateLogDao.pageListCount(username, operateType, jobDesc, startTime, endTime);

        // 分页查询日志列表
        List<XxlJobOperateLog> list = xxlJobOperateLogDao.pageList(
                start, length, username, operateType, jobDesc, startTime, endTime);

        Map<String, Object> result = new HashMap<>();
        result.put("recordsTotal", count);
        result.put("recordsFiltered", count);
        result.put("data", list);

        return result;
    }
}
