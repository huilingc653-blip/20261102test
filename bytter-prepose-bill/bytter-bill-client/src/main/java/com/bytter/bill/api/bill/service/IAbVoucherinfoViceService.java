package com.bytter.bill.api.bill.service;

public interface IAbVoucherinfoViceService {
    /**
     * 是否挂接预算系统
     * @param briefId 业务流水 001开票，6001库存登记，010背书
     * @return
     */
    public boolean isBudget(String briefId);

    /**
     * 根据业务获取预算科目
     * @param briefId 业务流水 001开票，6001库存登记，010背书
     * @return
     */
    public String getBudgetItem(String briefId);

    /**
    * @Description 根据从表id获取票据号
    * @author zhous
    * @date 2024/8/20 16:05
    */
    String getOrigNoByVid(String billId);
}
