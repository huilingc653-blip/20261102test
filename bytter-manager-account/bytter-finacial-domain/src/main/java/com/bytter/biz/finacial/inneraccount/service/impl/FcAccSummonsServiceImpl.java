package com.bytter.biz.finacial.inneraccount.service.impl;
import com.bytter.biz.finacial.api.service.IFcAccSummonsService;
import com.bytter.biz.finacial.api.vo.VoucherInfoVO;
import com.bytter.biz.finacial.inneraccount.service.FcAccSummonsService;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
@DubboService
@Service("fcAccSummonsServiceImpl")
public class FcAccSummonsServiceImpl implements IFcAccSummonsService {
    @Autowired
    private FcAccSummonsService fcAccSummonsService;
    /**
     * 查询内传票凭证列表
     * @param dataTablesInput
     * @return
     */
    public DataTablesOutput getDataTablesUnion(DataTablesInput dataTablesInput){
      return   fcAccSummonsService.getDataTablesUnion(dataTablesInput);
    }
    /**
     *查询凭证详细信息
     * @param vo
     * @return
     */
    public List<VoucherInfoVO> getVoucherInfo(VoucherInfoVO vo){
       return fcAccSummonsService.getVoucherInfo(vo);
    }
}
