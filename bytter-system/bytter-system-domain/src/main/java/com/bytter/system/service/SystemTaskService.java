package com.bytter.system.service;

import cn.hutool.core.util.StrUtil;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.system.dao.UserDao;
import com.bytter.system.entity.User;
import com.xxl.job.core.context.XxlJobHelper;
import com.xxl.job.core.handler.annotation.XxlJob;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * 自定义系统定时任务入口，用于XXL执行器调用。
 *
 * @author zhaohc
 * @date 2024/9/12 10:59
 */
@Service("systemTaskService")
public class SystemTaskService extends BaseService<User, String> {

    @Resource
    private UserDao userDao;

    @Override
    public BaseDao<User, String> getEntityDao() {
        return userDao;
    }

    public static void main(String[] args) {
        String arguments = "select * from xxx del ete TRUncATE";
        boolean delete = StrUtil.containsAnyIgnoreCase(arguments, "delete","truncate");
        System.out.println("delete = " + delete);
    }

    @XxlJob("AutomaticExecutionSqlJob")
    public void run() {
        String jobParam = XxlJobHelper.getJobParam();
        XxlJobHelper.log("定时任务接收参数：【{}】", jobParam);
        try {
            XxlJobHelper.log("---启用【自动执行SQL脚本】定时任务，开始执行定时任务---");
            // 校验SQL中不允许有删除语句
            boolean checkFlag = StrUtil.containsAnyIgnoreCase(jobParam, "delete","truncate");
            if (checkFlag){
                XxlJobHelper.log("执行SQL脚本【{}】不合法，不允许有删除语句，停止执行。", jobParam);
                return;
            }
            // 执行SQL，并返回响 应条数
            int count = userDao.batchSQLExecute(jobParam);
            XxlJobHelper.log("执行SQL脚本【{}】，响应【{}】条。", jobParam, count);
            XxlJobHelper.log("---启用【自动执行SQL脚本】定时任务，结束执行定时任务---");
        } catch (Exception e) {
            XxlJobHelper.log(e.getMessage(), e);
            throw new RuntimeException(e);
        }
    }

}
