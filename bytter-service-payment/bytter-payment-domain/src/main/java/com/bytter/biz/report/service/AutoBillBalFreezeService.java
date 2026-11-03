package com.bytter.biz.report.service;

import com.bytter.biz.report.dao.AutoBillBalFreezeDao;
import com.bytter.biz.report.vo.AutoBillBalFreezeVO;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 定时任务-自动新增票据计划冻结申请
 * 取值逻辑：获取下周一至周日到期应付票据，挂接保证金非100%。按照票据账号和到期日提前两个工作日进行汇总
 *
 * @author fangcf
 * @date 2025-10-22
 */
@Service("autoBillBalFreezeService")
@Transactional
public class AutoBillBalFreezeService extends BaseService<AutoBillBalFreezeVO, String> {

    @Autowired
    private AutoBillBalFreezeDao autoBillBalFreezeDao;

    @Override
    public BaseDao<AutoBillBalFreezeVO, String> getEntityDao() {
        return autoBillBalFreezeDao;
    }

    /**
     * 获取下周一至周日到期应付票据，挂接保证金非100%
     *
     * @param startDateStr 开始日期
     * @param endDateStr   结束日期
     */
    public List<AutoBillBalFreezeVO> listBill(String startDateStr, String endDateStr) {
        return autoBillBalFreezeDao.listBill(startDateStr, endDateStr);
    }
}