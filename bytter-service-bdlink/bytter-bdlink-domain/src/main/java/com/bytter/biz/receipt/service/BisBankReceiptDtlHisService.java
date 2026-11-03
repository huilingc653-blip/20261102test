package com.bytter.biz.receipt.service;
import com.bytter.biz.bdlink.vo.BisBankDtlBillVO;
import com.bytter.biz.bdlink.vo.BisBankReceiptDtlHisVO;
import com.bytter.biz.receipt.dao.BisBankReceiptDtlDao;
import com.bytter.biz.receipt.dao.BisBankReceiptDtlHisDao;
import com.bytter.biz.receipt.entity.BisBankReceiptDtlHis;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.constant.CommonConstant;
import com.bytter.core.constant.ResourceConstant;
import com.bytter.system.api.IAccountGrantService;
import com.bytter.system.api.ICorpService;
import com.bytter.system.api.constant.AccountGrantConstant;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
/**
 * 银行电子回单
 *
 * @author zuoy 2018-04-09
 */
@Service("bisBankReceiptDtlHisService")
@Transactional
public class BisBankReceiptDtlHisService extends BaseService<BisBankReceiptDtlHis, String> {
    @Autowired
    private BisBankReceiptDtlHisDao bisBankReceiptDtlHisDao;
    @Autowired
    private BisBankReceiptDtlDao bisBankReceiptDtlDao;
    @DubboReference(check = false)
    private ICorpService corpService;
    @DubboReference(check = false)
    private IAccountGrantService accountGrantService;
    @Override
    public BaseDao<BisBankReceiptDtlHis, String> getEntityDao() {
        return bisBankReceiptDtlHisDao;
    }
    public DataTablesOutput<BisBankReceiptDtlHisVO> getDataTablesUnion(DataTablesInput input, ICurrentUser user) {
        StringBuilder commonSql = new StringBuilder();
        //设置账号授权控制
        List<String> bankAccGrantList = accountGrantService.listByMultiValAccs(input.getCurrentUserId(), ResourceConstant.NETBANK_RECEIPT_BANKBILL_QUERY, null,
                AccountGrantConstant.ONLINE_BANK_TYPE + CommonConstant.COMMA_SPLIT + AccountGrantConstant.OFFLINE_BANK_TYPE,
                AccountGrantConstant.QUERY_DETAIL);
        input.getFormdata().put("bankAccGrantList", bankAccGrantList);
        DataTablesOutput<BisBankReceiptDtlHisVO> dataTablesOutput = this.bisBankReceiptDtlHisDao.getDataTablesUnion(input, commonSql);
        for (int i = 0; i < dataTablesOutput.getData().size(); i++) {
            BisBankReceiptDtlHisVO bisBankReceiptDtlHisVO = (BisBankReceiptDtlHisVO) dataTablesOutput.getData().get(i);
            List<BisBankDtlBillVO> bisBankDtlBillVOS = bisBankReceiptDtlDao.getDtls(bisBankReceiptDtlHisVO.getId());
            bisBankReceiptDtlHisVO.setBillFrom("no_one");
            bisBankReceiptDtlHisVO.setBisBankDtlBillVOList(bisBankDtlBillVOS);
        }
        return dataTablesOutput;
    }

    public DataTablesOutput<BisBankReceiptDtlHisVO> getDataTablesUnion(DataTablesInput input,String commonSqlStr, ICurrentUser user) {
        StringBuilder commonSql = new StringBuilder(commonSqlStr);
        //设置账号授权控制
        List<String> bankAccGrantList = accountGrantService.listByMultiValAccs(input.getCurrentUserId(), ResourceConstant.NETBANK_RECEIPT_BANKBILL_QUERY, null,
                AccountGrantConstant.ONLINE_BANK_TYPE + CommonConstant.COMMA_SPLIT + AccountGrantConstant.OFFLINE_BANK_TYPE,
                AccountGrantConstant.QUERY_DETAIL);
        input.getFormdata().put("bankAccGrantList", bankAccGrantList);
        DataTablesOutput<BisBankReceiptDtlHisVO> dataTablesOutput = this.bisBankReceiptDtlHisDao.getDataTablesUnion(input, commonSql);
        for (int i = 0; i < dataTablesOutput.getData().size(); i++) {
            BisBankReceiptDtlHisVO bisBankReceiptDtlHisVO = (BisBankReceiptDtlHisVO) dataTablesOutput.getData().get(i);
            List<BisBankDtlBillVO> bisBankDtlBillVOS = bisBankReceiptDtlDao.getDtls(bisBankReceiptDtlHisVO.getId());
            bisBankReceiptDtlHisVO.setBillFrom("no_one");
            bisBankReceiptDtlHisVO.setBisBankDtlBillVOList(bisBankDtlBillVOS);
        }
        return dataTablesOutput;
    }
    /**
     * 根据付款ID获取电子回单信息
     *
     * @param id
     * @return
     */
    public BisBankReceiptDtlHisVO getByIdUnion(String id) {
        return this.bisBankReceiptDtlHisDao.getByIdUnion(id);
    }
    /**
     * 更新打印次数
     *
     * @param id
     */
    public void updatePrint(String id) {
        this.bisBankReceiptDtlHisDao.updatePrint(id);
    }
    public List<BisBankReceiptDtlHisVO> listByIds(String ids) {
        return bisBankReceiptDtlHisDao.listByIds(ids);
    }
}
