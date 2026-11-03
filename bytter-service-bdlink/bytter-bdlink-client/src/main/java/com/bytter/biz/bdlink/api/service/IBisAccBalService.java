package com.bytter.biz.bdlink.api.service;

import com.bytter.biz.bdlink.api.dto.BisAccBalDTO;
import com.bytter.biz.bdlink.api.dto.BtBankAccDTO;
import com.bytter.biz.bdlink.api.dto.DepositReportCommonDTO;
import com.bytter.biz.bdlink.vo.BisAccDtlBillVO;
import com.bytter.biz.bdlink.vo.BtBankAccBalVO;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 银行账户余额接口类
 *
 * @author zuoy
 * @since 2017-08-25
 */
public interface IBisAccBalService {
    /**
     * 根据id得到实体
     *
     * @param id 余额ID
     * @return BisAccBalDTO 银行账号余额
     */
    BisAccBalDTO getById(String id);
    /**
     * 根据条件查询银行账户余额(可能存在同一账户多个币别的余额信息)
     *
     * @param banAcc 账号
     * @return 银行账号余额集合
     */
    List<BisAccBalDTO> listBisAccBalBy(String banAcc);
    /**
     * 根据条件查询银行账户最近一次的余额信息
     *
     * @param bankAcc 账户
     * @param curId   币别ID
     * @return BisAccBalDTO 银行账号余额
     */
    BisAccBalDTO getLastBisAccBalBy(String bankAcc, String curId);
    
//    List<BisAccBalDTO> getLastBisAccBalByList(List<String> bankAcc, List<String> curId);
    /**
     * 更新银行账户冻结余额与可用余额
     *
     * @param bankAcc    账户
     * @param curId      币别ID
     * @param updateDate 更新日期
     * @param frzBal     冻结余额
     * @param isFrozen   true/false
     */
    void updateFrzBalAndAvailBal(String bankAcc, String curId, Date updateDate, BigDecimal frzBal, boolean isFrozen, ICurrentUser user);

    void updateFrzBalAndAvailBal(String bankAcc, String curId, Date updateDate,String planFlag, BigDecimal frzBal, boolean isFrozen, ICurrentUser user);

    /**
     * 获取最近一次的余额
     *
     * @param bankAcc 账户
     * @param curId   币别ID
     * @return BigDecimal 余额
     */
    BigDecimal getLastByBalDate(String bankAcc, String curId);
    
    
    public BigDecimal getLastByBalDate(String bankAcc, String curId, boolean status);
    /**
     * 获取指定日期的余额
     *
     * @param bankAcc 账户
     * @param curId   币别ID
     * @param balDate 日期
     * @return BigDecimal 余额
     */
    BigDecimal getBalByBalDate(String bankAcc, String curId, Date balDate);
    /**
     * 获取指定日期的余额，若指定日期不存在余额，则查询最近一次
     *
     * @param bankAcc 账户
     * @param curId   币别ID
     * @param balDate 日期
     * @return BigDecimal 余额
     */
    BigDecimal getCurBalOrLastBal(String bankAcc, String curId, Date balDate);
    /**
     * 获取指定日期的余额，若指定日期不存在余额，则查询最近一次
     *
     * @param bankAcc 账户
     * @param curId   币别ID
     * @param balDate 日期
     * @return BigDecimal 余额
     */
    BigDecimal getCurBalOrLastRealBal(String bankAcc, String curId, Date balDate);
    /**
     * 根据比较日期组合查询余额
     *
     * @param bankAcc  账户
     * @param curId    币别ID
     * @param balDate  比较日期，为null则查询最近一次余额
     * @param operator 跟balDate组合使用，日期比较运算符，参照MatchTypeEnum
     * @return BigDecimal 余额
     */
    BigDecimal getBalByBalDate(String bankAcc, String curId, Date balDate, String operator);
    /**
     * 外接口保存余额
     *
     * @param dto
     */
    void saveBalByDto(BisAccBalDTO dto);
    /**
     * 外接口更新余额
     *
     * @param dto
     */
    void updateBalByDto(BisAccBalDTO dto);
    /**
     * 外接口判断余额是否存在
     *
     * @param bankAcc
     * @param curId
     * @return
     */
    BisAccBalDTO getBisAccBalBy(String bankAcc, String curId, Date balDate);
    /**
     * 获取所有账户类型的存款
     *
     * @param user     用户
     * @param resource 资源
     * @return
     */
    List<DepositReportCommonDTO> getBankAccBalByUser(ICurrentUser user, String resource, String corpId);
    List<BtBankAccBalVO> queryBankAccBalanceById(List<String> bankAccs, List<String> curIds);
    /**
     * 外汇资金池账户余额查询
     * @param balDate
     * @param bankAcc
     * @param curId
     * @return
     */
    BtBankAccBalVO getFexCorpPoolBankAccBalance(Date balDate, String bankAcc, String curId);
    DataTablesOutput getDataTablesUnion(DataTablesInput dataTablesInput, ICurrentUser user);
    void initBalSave(BisAccBalDTO bisAccBalDTO, ICurrentUser user);
    void batchDelete(String ids);
    void batchSubmit(String ids, ICurrentUser user);
    void delete(String id);
    BisAccBalDTO getByUnion(String id);
    void saveImport(List<BisAccBalDTO> list, ICurrentUser user);
    List listExport(DataTablesInput dataTablesInput, ICurrentUser user);
    void saveComplete(BisAccBalDTO bisAccBalDTO, ICurrentUser user);
    void saveBisAccBal(BisAccBalDTO bisAccBalDTO, ICurrentUser user);
    void updateBisAccBal(BisAccBalDTO bisAccBalDTO, ICurrentUser user);
    BisAccBalDTO getInitBisAccBal(String bankAcc, String curId);
    void saveAccBalByDtl(BisAccBalDTO bisAccBalDTO, ICurrentUser user);
    DataTablesOutput getDataTablesQuery(DataTablesInput dataTablesInput, ICurrentUser user);
    DataTablesOutput getDataTablesApprove(DataTablesInput dataTablesInput);
    BisAccBalDTO getBisAccBalBy(String bankAcc, String curId, Date balDate, boolean status);
    List<BisAccBalDTO> getBankBal(String bankAcc, String balanceDate);
    /**
     * 票据认领待确认或已确认银行流水列表
     *
     * @param dataTablesInput 查询参数
     * @return 前端列表数据
     **/
     DataTablesOutput<BisAccDtlBillVO> listAbClaimConfirmedInfoAcc(DataTablesInput dataTablesInput, List<String> claimIdList);

    /**
     * 通过sql语句查询账号，可以是关联查询语句
     * @param sql
     * @param params
     * @return
     */
    List<BisAccBalDTO> listBisAccBalBySql(String sql, Map<String, Object> params );

    /**
     * 根据日期查询境外账号余额
     * @return 银行余额列表
     */
    public List<BisAccBalDTO> selectBalance(String balDate, String curId);

    /**
     * 银行账户创建时初始化余额信息
     *
     * @param bankAcc 银行账户实体对象
     * @return boolean true/false
     */
    boolean initBisAccBal(BtBankAccDTO bankAcc, ICurrentUser user);
    /**
     * 银行账户创建时初始化余额信息
     *
     * @param bankAcc 银行账户实体对象
     * @return boolean true/false
     */
    boolean initBisAccBal(List<BtBankAccDTO> bankAcc, ICurrentUser user);

    void saveBisAccBalByDtl(BtBankAccDTO bankAcc,Date beginDate,Date endDate, ICurrentUser user);

    /**
     * 更新银行账户系统冻结余额
     *
     * @param accBalId 余额表id
     * @param sysFrzBal 系统冻结金额
     * @param freezeType 冻结状态：freeze-冻结，unfreeze-解冻
     */
    void updateSysFrzBal(String accBalId, BigDecimal sysFrzBal, String freezeType);

    /**
     * 账户余额
     * @param corpIds
     * @param balDate
     * @param bankTypeId
     * @param bankCode
     * @return
     */
    public BigDecimal getBankAccBalForReportDate(List<String> corpIds, Date balDate, String bankTypeId, String bankCode);
}
