package com.bytter.system.api;

import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.system.api.dto.BtBankAccDTO;
import com.bytter.system.api.dto.BtBankAccFreezeDTO;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

public interface IBtBankAccFreezeService {
    public DataTablesOutput getDataTablesUnion(DataTablesInput dataTablesInput, String joinSql);
    /**
     * 根据id和币别得到银行账号
     *
     * @param id
     * @param curId
     * @return
     */
    public BtBankAccDTO getAccVOById(String id, String curId);

    public BigDecimal getFreezeMoneyByBankAcc(String bankAcc, String curId, Date balDate);

    public DataTablesOutput getDataTablesQuery(DataTablesInput dataTablesInput, String joinSql);

    /**
     * 账户全部冻结解冻
     *
     * @param accId
     * @param curId
     * @return
     */
    public List<BtBankAccFreezeDTO> getBtBankAccFreeze(String accId, String curId);

    /**
     * 账户冻结暂存
     *
     * @param accId
     * @param curId
     * @return
     */
    public List<BtBankAccFreezeDTO> getBtBankAccFreezeTemp(String accId, String curId);

    /**
     * 得到账户冻结金额
     *
     * @param accId
     * @param curId
     * @return
     */
    public BigDecimal getFreezeMoney(String accId, String curId);

    /**
     * 得到账户解冻金额
     *
     * @param accId
     * @param curId
     * @return
     */
    public BigDecimal getUfFreezeMoney(String accId, String curId);

    /**
     * 查询审核列表信息
     *
     * @param dataTablesInput
     * @param businessSql
     * @return
     */
    public DataTablesOutput dataTableApprove(DataTablesInput dataTablesInput, String businessSql);

    /**
     * 账户冻结待审批
     *
     * @param accId
     * @param curId
     * @return
     */
    public List<BtBankAccFreezeDTO> getBtBankAccFreezeWait(String accId, String curId);

    /**
     * 根据单位统计银行账户(离线)
     * @param corpId
     * @return
     */
    public int getBankAccFreezeCountByCorpId(String corpId);

    /**
     * 获取冻结金额
     * @param bankAcc
     * @return
     */
    public BigDecimal getFreezeMoneyByBankAcc(String bankAcc, String curId, String ufFlag,Date balDate);

    /**
     * 删除对象
     * @param dto
     */
    public void deleteEntity(BtBankAccFreezeDTO dto);
    /**
     * 根据id删除
     * @param id
     */
    public void delete(String id);
    /**
     * 保存
     * @param dto
     */
    public void save(BtBankAccFreezeDTO dto);
    /**
     * 更新
     * @param dto
     */
    public void update(BtBankAccFreezeDTO dto);
    /**
     * 保存or更新
     * @param dto
     */
    public void saveOrUpdate(BtBankAccFreezeDTO dto);

    public BtBankAccFreezeDTO getBtBankAccFreezeDTO(String accId);

    public BtBankAccFreezeDTO getBtBankAccFreezeDTO(String id,boolean eager);

    BtBankAccFreezeDTO getBtBankAccFreezeByFreezeNo(String freezeNo);

    BigDecimal getUnFreezedMoneyById(String freezeId);
}
