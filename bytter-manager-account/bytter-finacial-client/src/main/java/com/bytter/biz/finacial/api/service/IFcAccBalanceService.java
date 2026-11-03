package com.bytter.biz.finacial.api.service;

import com.bytter.biz.finacial.api.dto.FcAccBalanceDTO;
import com.bytter.biz.finacial.api.vo.FcAccBalanceVO;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

public interface IFcAccBalanceService {
    /**
     * 判断月账单是否可以核对
     *
     * @param date 月账单
     * @return true 存在，false 不存在
     */
    public boolean existBalance(String date) ;
    public FcAccBalanceDTO getBalanceByAccountAndDate(String account, Date balanceDate);
    /**
     * 查找
     * @return
     */
    public FcAccBalanceVO getFcAccBalanceById(String id);
    public DataTablesOutput getDataTables(DataTablesInput input);
    /**
     * 删除对象
     * @param entity
     */
    public void deleteEntity(FcAccBalanceVO entity);
    /**
     * 根据id删除
     * @param id
     */
    public void delete(String id);
    /**
     * 保存
     * @param entity
     */
    public void save(FcAccBalanceVO entity);
    /**
     * 更新
     * @param entity
     */
    public void update(FcAccBalanceVO entity);
    /**
     * 保存or更新
     * @param entity
     */
    public void saveOrUpdate(FcAccBalanceVO entity);
    /**
     * 查找某一天的账号对象
     * @param date
     * @param account
     * @return
     */
    public FcAccBalanceVO getEntityByAccountAndDate(Date date, String account);
    /**
     * 初始化余额
     * @param balanceDate 余额日期
     * @param blnctrAcc 账号
     * @param accName 户名
     */
    public void initAccountBalance(Date balanceDate, String blnctrAcc, String accName,String curId,String loginName);
    public void updateAccBalanceByInitAcc(Date date, String account, BigDecimal balance) ;
    /**
     * 根据内部账号分组查询笔数
     * @param account
     * @return
     */
    FcAccBalanceVO getGroupByAccount(String account) ;
    /**
     * 按日期分组余额
     * @param dates
     * @return
     */
    List<FcAccBalanceVO> getGroupByDates(Date[] dates) ;

    /**
     * 根据内部账号列表分组查询笔数
     * @param accountList
     * @return
     */
    Map<String,FcAccBalanceVO> getGroupByAccount(List<String> accountList) ;

    /***
     * 自动同步多级单位内部账号余额
     * @param balanceList
     * @return TODO
     */
    void saveOrUpdateSpecial(List<FcAccBalanceDTO> balanceList);
}
