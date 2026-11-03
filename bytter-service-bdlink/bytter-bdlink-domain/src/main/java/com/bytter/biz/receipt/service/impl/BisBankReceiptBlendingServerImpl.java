package com.bytter.biz.receipt.service.impl;

import com.bytter.biz.bdlink.api.dto.BisAccDtlDTO;
import com.bytter.biz.bdlink.api.dto.BisReceiptDtlDTO;
import com.bytter.biz.bdlink.api.service.IBisBankReceiptBlendingServer;
import com.bytter.biz.bdlink.vo.BisBankReceiptDtlAutoVO;
import com.bytter.biz.bdlink.vo.BisBankReceiptDtlVO;
import com.bytter.biz.receipt.dao.BisBankReceiptBlendingDao;
import com.bytter.biz.receipt.service.BisBankReceiptBlendingServer;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Set;

@Service("bisBankReceiptBlendingServerImpl")
@DubboService
public class BisBankReceiptBlendingServerImpl implements IBisBankReceiptBlendingServer {
    @Autowired
    BisBankReceiptBlendingServer bisBankReceiptBlendingServer;
    @Autowired
    private BisBankReceiptBlendingDao bisBankReceiptBlendingDao;
    public DataTablesOutput<BisBankReceiptDtlVO> getDataTablesUnion(DataTablesInput input, ICurrentUser user,Set<String> accNos) {
        return this.bisBankReceiptBlendingServer.getDataTablesUnion(input, user,accNos);
    }
    public List<BisBankReceiptDtlVO> listReceiptByids(String ids) {
        return bisBankReceiptBlendingDao.listReceiptByids(ids);
    }
    /**
     * 自动勾兑
     *
     * @param ids
     * @param bankAccTypeStr
     */
    public String aotoBlending(String ids, String bankAccTypeStr) {
        return this.bisBankReceiptBlendingServer.aotoBlending(ids, bankAccTypeStr);
    }
    public DataTablesOutput getDtlRules(DataTablesInput input) {
        return bisBankReceiptBlendingDao.getDtlRules(input);
    }
    public List<BisBankReceiptDtlAutoVO> getForReceipt(String bankAccTypeStr, Map<String, String> map, String cdSingnStr) {
        return bisBankReceiptBlendingDao.getForReceipt(bankAccTypeStr, map, cdSingnStr);
    }
    /*
    唯一标识勾兑
     */
    public void JustOne(BisBankReceiptDtlAutoVO bisBankReceiptDtlAutoVO) {
        this.bisBankReceiptBlendingServer.JustOne(bisBankReceiptDtlAutoVO);
    }
    public List<BisAccDtlDTO> getJust(String eqs, String check) {
        return bisBankReceiptBlendingDao.getJust(eqs, check);
    }
    public List<String> getDtlId(String receiptId) {
        return bisBankReceiptBlendingDao.getDtlId(receiptId);
    }

    /**
     * 根据银行电子回单id获取中间关联表的流水id
     *
     * @param receiptId
     * @return
     */
    public List<BisReceiptDtlDTO> getBisReceiptDtl(String receiptId) {
        return bisBankReceiptBlendingDao.getBisReceiptDtl(receiptId);
    }
    /**
     * 更新电子回单勾兑标志
     *
     * @param id
     * @param blendSign
     */
    public int toUpdateBlendSign(String id, String blendSign) {
        return this.bisBankReceiptBlendingServer.toUpdateBlendSign(id, blendSign);
    }
    /**
     * 更新电子回单勾兑标志
     *
     * @param id
     * @param blendSign
     */
    public int toUpdateBlendSign(String id, String blendSign, String oldBlendSign) {
        return this.bisBankReceiptBlendingServer.toUpdateBlendSign(id, blendSign, oldBlendSign);
    }
    /**
     * 插入银行电子回单和银行流水的关系
     *
     * @param dtlId
     * @param receiptId
     */
    public int saveBisReceiptDtl(String dtlId, String receiptId, String rulesName) {
        return this.bisBankReceiptBlendingServer.saveBisReceiptDtl(dtlId, receiptId, rulesName);
    }
    /**
     * 手工勾兑保存
     *
     * @param dtlId
     * @param receiptId
     */
    public void manualBlending(String dtlId, String receiptId) {
        bisBankReceiptBlendingServer.manualBlending(dtlId, receiptId);
    }
    /**
     * 取消勾兑
     *
     * @param receiptId
     */
    public void cancelBlending(String receiptId) {
        bisBankReceiptBlendingServer.cancelBlending(receiptId);
    }
}
