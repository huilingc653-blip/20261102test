package com.bytter.increment.domain.vo.v1;

import lombok.Data;

@Data
public class ChoBankFinanceAddVO {

    private Object choBankFinancePage;
    /**
     * 业务类型
     */
    private Object busType;
    /**
     * 系统时间
     */
    private String sysDate;
    /**
     * 银行理财参数配置：regularOnset-定期存款起存额度
     */
    private String regularOnset;
    /**
     * 银行理财参数配置：regularKeep-定期存款最低留存额度
     */
    private String regularKeep;
    /**
     * 银行理财参数配置：noticeKeep-通知存款最低留存额度
     */
    private String noticeKeep;
    /**
     * 银行理财参数配置：regularNum-定期存款取款次数
     */
    private String regularNum;
    /**
     * 银行理财参数配置：noticeMinLimit-通知存款最低取款额度
     */
    private String noticeMinLimit;
    /**
     * 是否启用限额管理  1-是 0-否
     */
    private String isOpenLimit;
    /**
     * 银行理财参数配置：noticeOnset-通知存款起存额度
     */
    private String noticeOnset;
    /**
     * 跳转页面地址
     */
    private String forwardUrl;
    private String flowStatus;
    private String cycleTime;
    private String   taskId;

}
