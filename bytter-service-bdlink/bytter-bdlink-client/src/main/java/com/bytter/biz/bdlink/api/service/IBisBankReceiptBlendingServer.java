package com.bytter.biz.bdlink.api.service;

import com.bytter.biz.bdlink.api.dto.BisAccDtlDTO;
import com.bytter.biz.bdlink.api.dto.BisReceiptDtlDTO;
import com.bytter.biz.bdlink.vo.BisBankReceiptDtlAutoVO;
import com.bytter.biz.bdlink.vo.BisBankReceiptDtlVO;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface IBisBankReceiptBlendingServer {
    DataTablesOutput<BisBankReceiptDtlVO> getDataTablesUnion(DataTablesInput input, ICurrentUser user, Set<String> accNos);
    /**
     * 自动勾兑
     *
     * @param ids
     * @param bankAccTypeStr
     */
    public String aotoBlending(String ids, String bankAccTypeStr);
    DataTablesOutput getDtlRules(DataTablesInput input);
    List<BisBankReceiptDtlAutoVO> getForReceipt(String bankAccTypeStr, Map<String, String> map, String cdSingnStr);
    List<BisBankReceiptDtlVO> listReceiptByids(String ids);
    /*
    唯一标识勾兑
     */
    void JustOne(BisBankReceiptDtlAutoVO bisBankReceiptDtlAutoVO);
    List<BisAccDtlDTO> getJust(String eqs, String check);
    List<String> getDtlId(String receiptId);
    List<BisReceiptDtlDTO> getBisReceiptDtl(String receiptId);
    /**
     * 更新电子回单勾兑标志
     *
     * @param id
     * @param blendSign
     */
    int toUpdateBlendSign(String id, String blendSign);
    /**
     * 更新电子回单勾兑标志
     *
     * @param id
     * @param blendSign
     */
    int toUpdateBlendSign(String id, String blendSign, String oldBlendSign);
    /**
     * 插入银行电子回单和银行流水的关系
     *
     * @param dtlId
     * @param receiptId
     */
    int saveBisReceiptDtl(String dtlId, String receiptId, String rulesName);
    /**
     * 手工勾兑保存
     *
     * @param dtlId
     * @param receiptId
     */
    void manualBlending(String dtlId, String receiptId);
    /**
     * 取消勾兑
     *
     * @param receiptId
     */
    void cancelBlending(String receiptId);
}
