package com.bytter.biz.bdlink.api.service;

import com.bytter.biz.bdlink.api.dto.BisAccDtlBudgetRuleDTO;
import com.bytter.biz.bdlink.api.dto.BisAccDtlDTO;
import com.bytter.biz.bdlink.api.dto.BisAccDtlExportDTO;
import com.bytter.biz.bdlink.api.dto.NisFlowRulePurposeDTO;
import com.bytter.biz.bdlink.vo.BisAccDtlVO;
import com.bytter.biz.bdlink.vo.BtBankAccDtl;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.page.IPageResult;
import com.bytter.core.common.page.IParameterInput;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 银行账户流水接口类
 *
 * @author wangzx
 * @since 2018-03-21
 */
public interface IBisAccDtlService {

    List<BisAccDtlDTO> getByTransTime(String gedTransTime,String ledTransTime);

    com.bytter.biz.bdlink.vo.BisAccDtlRulesVO getRules(String id);

    DataTablesOutput setDtlReceipt(DataTablesOutput out);

    /**
     * 根据id得到实体
     *
     * @param id 流水ID
     * @return BisAccDtlDTO 银行账号流水
     */
    BisAccDtlDTO getById(String id);
    /**
     * 更新流水计划匹配状态
     *
     * @param id     流水ID
     * @param status 状态
     */
    void updateDtlFbsStatus(String id, String status);
    /**
     * 更新流水计划
     *
     * @param id     流水ID
     * @param status 状态
     */
    void updateDtlFbsStatus(String id, String status, String voucherTypeId);
    /**
     * 银行流水号为空。更新为id
     *
     * @param id
     */
    void updateDtlBankSerialNo(String id);
    /**
     * 根新流水
     *
     * @param dto
     */
    void updateDtlByDto(BisAccDtlDTO dto);
    /**
     * 根据id数组修改票据认领中间表id
     *
     * @param ids     流水明细id数组
     * @param claimId 票据认领中间表id
     */
    void updateClaimIdByIds(String[] ids, String claimId);
    /**
     * 外接口保存流水
     *
     * @param dto
     */
    void saveDtlByDto(BisAccDtlDTO dto);
    /**
     * 判断是否是合并单据
     *
     * @param id
     * @return
     */
    Boolean isMergeBill(String id);
    /**
     * <br>
     * <b>功能：</b>  <br>
     * <b>作者：</b>liyh<br>
     * <b>日期：</b> 2020/10/15 0015 <br>
     */
    BisAccDtlDTO getBisAccDtlByAccCurTime(String bankAcc, String curId, Date transTime);
    /**
     * 获取一个账号期间发生额
     *
     * @param curId     币种
     * @param bankAcc   账号
     * @param beginDate 开始日期
     * @param endDate   结束日期
     * @return 期间发生额
     */
    BigDecimal getDuringMoney(String curId, String bankAcc, Date beginDate, Date endDate);
    /**
     * 电子回单勾兑根据条件获取银行流水
     *
     * @param dataTablesInput
     * @return
     */
    DataTablesOutput<BisAccDtlVO> getDataTablesUnionForReceiptBlending(DataTablesInput dataTablesInput);
    /**
     * 根据票据认领id数组修改
     *
     * @param claimIdArr 票据认领id数组
     */
    void updateByClaimArr(String[] claimIdArr);
    /**
     * 根据id得到实体
     *
     * @param ids 流水ID 集合
     * @return List<BisAccDtlDTO> 银行流水集合
     */
    List<BisAccDtlDTO> getByIds(List<String> ids);
    List<BisAccDtlVO> getListForFlush();
    /**
     * 根据流水余额
     *
     * @param dtl 流水
     */
    void updateBal(BtBankAccDtl dtl);
    List<BisAccDtlVO> listAfterBtBankAccDtl(String bankAcc, String curId, Date transTime, String id, Integer orderNo);
    void saveOrUpdate(BisAccDtlVO entity);
    /**
     * 余额刷新
     */
    void flushBalance();
    /**
     * 根据账号id和币别id查找账号明细最小时间
     *
     * @param bankAcc
     * @param curId
     * @return
     */
    Date getMinTransTime(String bankAcc, String curId);
    /**
     * 获取当天流水的结余
     *
     * @param bankAcc
     * @param curId
     * @param transTime
     * @return
     */
    BigDecimal getLastBal(String bankAcc, String curId, Date transTime);
    BisAccDtlVO getBisAccDtlByAccNo(String accNo);
    void save(BisAccDtlVO dtl);
    /**
     * 根据查询参数，获取票据认领处理的银行流水数据。
     *
     * @param params      查询参数。
     * @param corpAuthSql 单位权限语句。
     * @return 银行流水数据。
     */
    List<BisAccDtlDTO> getClaimBillAccInfo(Map<String, Object> params, String corpAuthSql);
    DataTablesOutput getDataTablesUnion(DataTablesInput dataTablesInput, ICurrentUser user);
    BisAccDtlDTO getByUnion(String id);
    DataTablesOutput getDataTablesMakeup(DataTablesInput dataTablesInput, ICurrentUser user);
    void saveOrUpdate(BisAccDtlDTO bisAccDtl);
    List<BisAccDtlExportDTO> listExport(DataTablesInput dataTablesInput, ICurrentUser user);
    /**
     * 通过sql语句查询明细列表
     * @param params
     * @param sql
     * @return
     */
    List<BisAccDtlVO> getBisAccDtlListBySql(Map<String, Object> params, String sql);
    /**
     * 根据条件获取分页数据
     *
     * @param input    查询条件
     * @param menuType 查询类型
     * @param dtlIds   明细id集合
     * @return 列表数据
     */
    DataTablesOutput<BisAccDtlDTO> queryFcBankAccDtl(DataTablesInput input, String menuType, List<String> dtlIds);
    DataTablesOutput getInterfaceFcBankBill(DataTablesInput dataTablesInput, String jionSql);

    List<BisAccDtlDTO> queryBisAccDtlDTO(DataTablesInput dataTablesInput, String menuType, List<String> accList);

    /**
     * 通过bankids的还原银行明细导出标志
     */
    void recoverBankBillImportSign(String bankIds);

    /**
     * 通过bankids的修改银行明细导出标志
     */
    int updateBankBillImportSign(String bankIds);

    /**
     * 获取银行明细列表
     *
     * @param dataTablesInput
     * @param joinSql
     * @return
     */
    DataTablesOutput getDataTablesUnionPaymentBlending(DataTablesInput dataTablesInput, String joinSql,String allocationAllowBlending);


    /**
     * 获取银行明细列表
     * @param dataTablesInput
     * @param joinSql
     * @return
     */
     DataTablesOutput getDataTablesUnionRefundBlending(DataTablesInput dataTablesInput, String joinSql) ;

    DataTablesOutput getDataTablesUnion(DataTablesInput dataTablesInput, String joinSql);

    BisAccDtlBudgetRuleDTO getBisAccDtlByIdUnion(String id);

    /**
     * 根据合并id查到流水明细
     * @param mergeId
     * @return
     */
    List<BisAccDtlBudgetRuleDTO> getBisAccDtlByMergeId(String mergeId);

    /**
     * 获取流水实体转的dto
     * @param id
     * @return
     */
    BisAccDtlDTO getDTOById(String id);

    BisAccDtlDTO getUniqueBy(String propertyName,Object value);

    List<BisAccDtlDTO> listBy(String propertyName,Object value);

    /**
     * 银行流水利息自动认领。
     *
     * @param bankTypeId 银行类型编号。
     * @param remark     用途/摘要。
     * @param transTime  交易日期。
     * @param busType    业务类型ID。
     * @return 认领行数。
     */
    int interestDtlAutoClaim(String bankTypeId, String remark, Date transTime, String busType);

    /**
     * 更新流水勾兑状态
     *
     * @param id
     * @param blendSign
     * @return
     */
    int toUpdateBlendSign(String id, String blendSign, String userName);
    /**
     * 更新流水银行流水号
     *
     * @param id
     * @param bankSerialNo
     * @return
     */
    int toUpdateBankSerialNo(String id, String bankSerialNo);

    IPageResult<BisAccDtlVO> getPageListByBankAcc(IParameterInput parameterInput, String bankAcc, String curId, String remark, String beginDate, String endDate);

    BigDecimal getListByBankAccAmt(String bankAcc, String curId, String remark,String cdSign);
    BigDecimal getListWithTransTime(String bankAcc, String curId, String remark,String cdSign,String beginDate, String endDate);

    List<BisAccDtlVO> getListByBankAcc(String bankAcc, String curId, String remark, String beginDate, String endDate);

    /**
     * 资金调拨流水自动认领
     * @param busType 业务类型
     * @param list  规则集合
     * @param transferMode 调拨方式
     * @return
     */
    int fundTansferFlowAutoClaim(String busType, List<NisFlowRulePurposeDTO> list, String transferMode);

    /**
     * 根据查询日期，业务类型获取明细数据
     * @param queryDate
     * @param busTypes
     * @return
     */
    List<BisAccDtlDTO> listBisAccDtlByBusType(Date queryDate, List<String> busTypes);
    /**
     * 根据查询日期，业务类型获取明细数据
     * @param bankAccs
     * @param queryDate
     * @param busTypes
     * @return
     */
    List<BisAccDtlDTO> listBisAccDtlByBankAccQueryDateBusTypes(List<String> bankAccs,Date queryDate, List<String> busTypes);
    /**
     * 根据单位ID、查询日期、业务类型
     * @param corpIds
     * @param queryDate
     * @param busTypes
     * @return
     */
    List<BisAccDtlDTO> listBisAccDtlByCorpIdsQueryDateBusTypes(List<String> corpIds,Date queryDate, List<String> busTypes);
    List<BisAccDtlDTO> listBisAccDtlByCorpIdsQueryDateBusTypes(List<String> corpIds,List<String> busTypes,Date queryDateBegin,Date queryDateEnd,String accountGroup);

    /**
     * 根据查询条件获取银行账号。
     * @param dataTablesInput
     * @param corpList
     * @param accList
     * @return
     */
    List<String> getAcclistByCondition(DataTablesInput dataTablesInput, List<String> corpList, List<String> accList);

    void updateBisAccDtlDTOByAutoAndAgainSign(List<BisAccDtlDTO> bisAccDtlDTOs);

    public BigDecimal getBnakAccDtlForReportDate(List<String> corpIds, Date startDate, Date endDate,String bankTypeId, String bankCode);
    public BigDecimal getBalFromDetail(String bankAcc, String curId, Date transTime);
}
