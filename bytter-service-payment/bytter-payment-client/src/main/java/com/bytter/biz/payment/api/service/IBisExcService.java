package com.bytter.biz.payment.api.service;
import com.bytter.biz.payment.api.dto.BisExcDTO;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import java.util.List;
import java.util.Map;
/**
 * 付款指令接口类
 * @author huangx
 * @since 2017-6-30 09:49:44
 */
public interface IBisExcService {
    /**
     * 新增付款指令
     *
     * @param bisExc 付款指令DTO
     * @param user   用户
     */
    void addBisExc(BisExcDTO bisExc, ICurrentUser user);
    /**
     * 新增付款指令
     *
     * @param bisExc 付款指令DTO
     * @param user   用户
     */
    BisExcDTO addBisExcReturn(BisExcDTO bisExc, ICurrentUser user);
    /**
     * 修改付款指令
     *
     * @param bisExc 付款指令DTO
     * @param user   用户
     */
    void updateBisExc(BisExcDTO bisExc, ICurrentUser user);
    /**
     * 根据指令单号获取指令信息
     *
     * @param voucherNo 指令单号
     * @return 付款指令
     */
    BisExcDTO getBisExcByVoucherNo(String voucherNo);
    /**
     * 根据业务单号获取指令信息
     *
     * @param cbsBillNo   业务单号
     * @param voucherFrom 指令来源
     * @return 付款指令
     */
    BisExcDTO getBisExcByCbsBillNo(String cbsBillNo, String voucherFrom);
    /**
     * 根据业务单号获取指令信息
     *
     * @param cbsBillId   业务单号
     * @return 付款指令
     */
    BisExcDTO getBisExcBycbsBillId(String cbsBillId);
    /**
     * 指令查询
     *
     * @param dataTablesInput 表格数据包装对象
     * @param user            用户
     * @return 表格数据包装对象
     */
    DataTablesOutput getBisExcDataTables(DataTablesInput dataTablesInput, ICurrentUser user);
    /**
     * 新增付款指令（代发工资）
     *
     * @param bisExc 付款指令
     * @param user   用户
     * @return 指令单号
     */
    String addBisExcForNisWage(BisExcDTO bisExc, ICurrentUser user);
    /**
     * 根据业务单号获取指令信息（代发工资）
     *
     * @param cbsBillNo   业务单号
     * @param voucherFrom 指令来源
     * @return 付款指令集合
     */
    List<BisExcDTO> getBisExcByCbsBillNoForNisWage(String cbsBillNo, String voucherFrom);
    /**
     * 付款指令发送
     *
     * @param bisExc 付款指令
     * @param user   用户
     */
    void autoSendBisExc(BisExcDTO bisExc, ICurrentUser user);

    /**
     * 付款指令发送 不记账 付款办理，资金调拨，代发工资自动指令使用，业务自己产生记账凭证
     *
     * @param bisExc 付款指令
     * @param user   用户
     */
    void autoSendBisExcNoAccounting(BisExcDTO bisExc, ICurrentUser user);
    /**
     * 更新付款指令银行流水号（付款明细勾兑返写银行流水号）
     *
     * @param cbsBillId    业务单ID
     * @param bankSerialNo 银行流水号
     */
    void updateBisExcForBankSerialNo(String cbsBillId, String bankSerialNo);
    /**
     * 查询待记账数据
     */
    List<BisExcDTO> queryTallyBisEx();
    /**
     * 退款勾对打回原单 更新指令为无效
     *
     * @param id
     * @param user
     */
    void updateBisExc(String id, ICurrentUser user);
    /**
     * 根据支付单据id获取指令信息
     *
     * @param billIds 单据id集合
     * @return 指令信息集合
     */
    List<BisExcDTO> getBisExcByCbsId(List<String> billIds);

    List<BisExcDTO> getAllBisExcBankSerialNo();
    /**
     * 根据业务单号ID集合和指令来源，获取指令Map
     * @param billIds
     * @param voucherFrom
     * @return
     */
    Map<String, BisExcDTO> getBisExcMapByCbsBillIds(List<String> billIds, String voucherFrom);
    Map<String, BisExcDTO> getBisExcMapByCbsBillIds(List<String> billIds, String voucherFrom,List<String> voucherStat);

    /**
     * 根据业务单号集合和指令来源，获取指令Map
     * @param billNos
     * @param voucherFrom
     * @return
     */
    Map<String, BisExcDTO> getBisExcMapByCbsBillNos(List<String> billNos, String voucherFrom);
    
   List<String> getBisExcCbsBillIdsByVoucherstat();
   
   List<BisExcDTO> getBisExcByCbsIdsWithCondition(List<String> billIds);

    List<BisExcDTO> listBisExc(BisExcDTO dto);

    BisExcDTO getByVoucherNo(String voucherNo);

}
