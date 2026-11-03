package com.bytter.biz.receipt.service.impl;
import com.bytter.biz.bdlink.api.service.IBisBankReceiptDtlHisService;
import com.bytter.biz.bdlink.vo.BisBankReceiptDtlHisVO;
import com.bytter.biz.bdlink.vo.BisBankReceiptDtlVO;
import com.bytter.biz.receipt.entity.BisBankReceiptDtlHis;
import com.bytter.biz.receipt.service.BisBankReceiptDtlHisService;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.utils.BeanUtils;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service("bisBankReceiptDtlHisServiceImpl")
@DubboService
public class BisBankReceiptDtlHisServiceImpl implements IBisBankReceiptDtlHisService {
    @Autowired
    private BisBankReceiptDtlHisService bisBankReceiptDtlHisService;
    @Override
    public void saveEntity(BisBankReceiptDtlVO vo){
        BisBankReceiptDtlHis his=new BisBankReceiptDtlHis();
        BeanUtils.copyNotNullProperties(vo, his);
        bisBankReceiptDtlHisService.saveEntity(his);
    }
    @Override
    public DataTablesOutput<BisBankReceiptDtlHisVO> getDataTablesUnion(DataTablesInput input, ICurrentUser user) {
        return bisBankReceiptDtlHisService.getDataTablesUnion(input,user);
    }


    @Override
    public DataTablesOutput<BisBankReceiptDtlHisVO> getDataTablesUnion(DataTablesInput input,String commonSqlStr, ICurrentUser user) {
        return bisBankReceiptDtlHisService.getDataTablesUnion(input,commonSqlStr,user);
    }
    /**
     * 根据付款ID获取电子回单信息
     *
     * @param id
     * @return
     */
    @Override
    public BisBankReceiptDtlHisVO getByIdUnion(String id) {
        return this.bisBankReceiptDtlHisService.getByIdUnion(id);
    }
    /**
     * 更新打印次数
     *
     * @param id
     */
    @Override
    public void updatePrint(String id) {
        this.bisBankReceiptDtlHisService.updatePrint(id);
    }
    @Override
    public List<BisBankReceiptDtlHisVO> listByIds(String ids) {
        return bisBankReceiptDtlHisService.listByIds(ids);
    }
}
