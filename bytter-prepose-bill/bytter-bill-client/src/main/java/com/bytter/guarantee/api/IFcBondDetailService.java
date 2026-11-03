package com.bytter.guarantee.api;

import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.core.bpm.persistence.WorkFlowPersistence;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.guarantee.vo.FcBondDetailVO;
import com.bytter.guarantee.vo.FcBondVO;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

public interface IFcBondDetailService {

    DataTablesOutput<FcBondDetailVO> getDataTablesUnion(DataTablesInput dataTablesInput, ICurrentUser user) ;
    /**
     * 新增保证金
     *
     * @param fcBond
     * @param user
     */
    void saveFcBondDetail(FcBondDetailVO fcBond, ICurrentUser user);

    /**
     * fusb 2018-09-13
     * 修改存出保证金
     *
     * @param fcBond
     * @param user
     */
    public void updateFcBondDetail(FcBondDetailVO fcBond, ICurrentUser user) ;
    /**
     * 获取审批列表
     *
     * @param dataTablesInput
     * @return
     */
    public DataTablesOutput<WorkFlowPersistence> dataTableApprove(DataTablesInput dataTablesInput) ;
    /**
     * fusb,2018-9-12添加
     * 根据保证金id,得到保证金信息
     *
     * @param id
     * @return
     */
    public FcBondDetailVO getFcBondById(String id) ;

    /**
     * 查询保证金账户是否已存出
     */
    public boolean queryBondAcc(String bankAccId);
    /**
     * 改变保证金的状态为删除
     */
    public void deleteBond(String id, ICurrentUser user);


    /**
     * 保存or更新
     *
     * @param entity
     */
    public void saveOrUpdate(FcBondVO entity);

    /**
     * 保证金额度进行汇率折算
     *
     * @param balList
     * @param preDate
     * @param corpId  当前用户所属单位ID
     * @return
     */
    public BigDecimal dealBondBalance(List<FcBondDetailVO> balList, Date preDate, String corpId) ;
    /**
     * <br>
     * <b>功能：</b> 删除头寸调拨单打回保证金 <br>
     * <b>作者：</b>liyh<br>
     * <b>日期：</b> 2020/8/18 0018 <br>
     */
     void tempBond(String id, ICurrentUser user) ;

    /**
     * 新增保证金-不过审批，系统直接新增
     *
     * @param fcBond
     * @param user
     */
    FcBondDetailVO addFcBondDetail(FcBondDetailVO fcBond, ICurrentUser user);

    /**
     * 同批导入的保证金汇总到一起
     *
     * @param fcBond
     * @param bailMoney
     * @param user
     */
    void updateExistFcBondDetail(FcBondDetailVO fcBond,BigDecimal bailMoney, ICurrentUser user);

    /**
     * 根据帐号查询保证金信息
     *
     * @param bankAcc
     */
    FcBondDetailVO getFcBondByBankAcc(String bankAcc);

    /**
     * 追加保证金-不过审批，系统直接新增
     *
     * @param fcBond
     * @param bailMoney
     * @param user
     */
    void addFcBondAddData(FcBondDetailVO fcBond,BigDecimal bailMoney, ICurrentUser user);

    /**
     * 保证金账户查询批量编辑利率
     *
     * @param bondIds      多条id记录
     * @param interestRate 利率
     * @param user 用户对象
     */
    void batchEditInterestRate(String bondIds, BigDecimal interestRate, ICurrentUser user);
}
