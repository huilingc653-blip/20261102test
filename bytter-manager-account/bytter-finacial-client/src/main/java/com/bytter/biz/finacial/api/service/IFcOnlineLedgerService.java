package com.bytter.biz.finacial.api.service;

import com.bytter.biz.finacial.api.dto.FcOnlineLedgerDTO;
import com.bytter.biz.finacial.api.vo.FcAccVO;
import com.bytter.biz.finacial.api.vo.FcOnlineLedgerVO;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * 账户联机余额查询对外接口
 *
 * @author liyh
 */
public interface IFcOnlineLedgerService {
    /**
     * 根据账号查询账户联机余额
     *
     * @param accList 账号集合
     * @return 账号联机余额集合
     */
    List<FcOnlineLedgerDTO> getLedgerDTOsByAccSet(List<String> accList);
    /**
     * 根据账号查询账户联机余额。--资金池
     *
     * @param accList 账号集合
     * @return 账号联机余额集合
     */
    List<FcOnlineLedgerDTO> getPoolLedgerDTOsByAccSet(List<String> accList);
    /**
     * 根据账号查询账户余额
     *
     * @param accList 账号集合
     * @return 账号联机余额集合
     */
    List<FcOnlineLedgerDTO> getFcOnlineLedgerByAccList(List<String> accList);
    /**
     * 更新账户记账余额信息
     *
     * @param ledgerDTOs 账号联机余额集合
     */
    @Deprecated
    void updateOnlineBalanceList(List<FcOnlineLedgerDTO> ledgerDTOs);
    /**
     * 更新账户记账余额信息
     *
     * @param ledgerDTOs     账号联机余额集合(需要更新的联机余额)
     * @param origLedgerDTOs （需要更新之前的联机余额）
     */
    void updateOnlineBalanceList(List<FcOnlineLedgerDTO> ledgerDTOs, List<FcOnlineLedgerDTO> origLedgerDTOs);
    /**
     * 新账户复核余额信息
     *
     * @param ledgerDTOs 账号联机余额集合
     */
    void updateCheckBalanceList(List<FcOnlineLedgerDTO> ledgerDTOs);
    /**
     * 账户初始化更新账户余额
     *
     * @param account
     * @param balance
     */
    void updateOnlineBalanceByInitAcc(String account, BigDecimal balance);
    /**
     * 根据账号查询账户联机余额
     *
     * @param blnctrAcc 内部账户
     * @return 账号联机余额实体
     */
    FcOnlineLedgerDTO getByBlnctrAcc(String blnctrAcc);

    List<FcOnlineLedgerDTO> getByBlnctrAccByList(List<String> blnctrAcc);
    /**
     * 根据账号效验金额
     *
     * @param blnctrAcc 内部账户
     * @return true 不通过，false 通过
     */
    boolean validAmt(String blnctrAcc);
    FcOnlineLedgerVO getEntityByAccount(String account);
    void insertFclOnlineLedger(FcAccVO acc, String loginName);
    void updateFreezeMoney(FcOnlineLedgerVO onlineLedger, BigDecimal money, boolean isFreeze, String loginName);
    void updateFreezeMoney(String account, BigDecimal money, boolean isFreeze);
    DataTablesOutput getDataTables(DataTablesInput dataTablesInput);
    FcOnlineLedgerVO getFcOnlineLedgerById(String id);
    void save(FcOnlineLedgerVO fcAccuBalance);
    void saveOrUpdate(FcOnlineLedgerVO fcAccuBalance);
    void deleteEntity(FcOnlineLedgerVO fcAccuBalance);
    /**
     * 获取联机余额
     *
     * @param accList 账号集合
     * @return 联机余额
     */
    List<FcOnlineLedgerDTO> getBalanceByAcc(List<String> accList);

    /**
     * 批量获取联机余额数据
     * @param accList 帐号列表
     */
    Map<String,FcOnlineLedgerDTO> getBalanceByAccMap(List<String> accList);

    FcOnlineLedgerDTO getByBlnctrAcc(String payeeCounterAcc, String poolSign);

    void updateBalance(List<FcOnlineLedgerDTO> balanceList);
}
