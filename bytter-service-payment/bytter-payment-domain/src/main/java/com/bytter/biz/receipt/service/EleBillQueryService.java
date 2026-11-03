package com.bytter.biz.receipt.service;

import com.bytter.biz.finacial.api.dto.FcAccbookDTO;
import com.bytter.biz.finacial.api.service.IFcAccbookBlnedService;
import com.bytter.biz.bdlink.api.constant.ResourceConstant;
import com.bytter.biz.receipt.util.ReceiptUtils;
import com.bytter.biz.receipt.vo.EleAllBillVO;
import com.bytter.core.base.AbstractService;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.utils.StringUtils;
import com.bytter.system.api.IAccountGrantService;
import com.bytter.system.api.IConfigService;
import com.bytter.system.api.IInnerAccountService;
import com.bytter.system.api.constant.AccountGrantConstant;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
/**
* @Title: Service
* @Description: 电子回单查询及打印
* @author onlineGenerator
* @date 2017-06-05 10:52:28
* @version 1.0
*
*/
@Service("eleBillQueryService")
@Transactional
public class EleBillQueryService extends AbstractService {
    @Autowired(required = false)
    private com.bytter.biz.receipt.dao.EleBillQueryDao EleBillQueryDao;
    @DubboReference(check = false)
	private IInnerAccountService innerAccountService;
    @DubboReference(check = false)
	private IConfigService configService;
    @DubboReference(check = false)
	private IAccountGrantService accountGrantService;
    @DubboReference(check = false)
    private IFcAccbookBlnedService iFcAccbookBlnedService;
    public DataTablesOutput listForUnion(DataTablesInput input, ICurrentUser user){
        StringBuilder commonSql=new StringBuilder();
        if(input.getFormdata().containsKey("printSign")) {
            String printSign = (String)input.getFormdata().get("printSign");
            commonSql.append(ReceiptUtils.getSearchSqlCommon(printSign));
        }
        List<String> accountGrantList = accountGrantService.listByMultiValAccs(user.getId(), ResourceConstant.NETBANK_RECEIPT_ELEBILL_QUERY, null,
                AccountGrantConstant.INNER_BANK_TYPE, AccountGrantConstant.QUERY_DETAIL);
        DataTablesOutput output = EleBillQueryDao.listForUnion(input,commonSql.toString(), accountGrantList);
        List<EleAllBillVO> voList=output.getData();
        for(EleAllBillVO vo:voList){
            if (StringUtils.isNotEmpty(vo.getVoucherNo())){
                FcAccbookDTO fcAccBook = new FcAccbookDTO();
                fcAccBook.setVoucherNo(vo.getVoucherNo());
                FcAccbookDTO fcAccbookDTO = iFcAccbookBlnedService.queryCompareAccBy(fcAccBook);
                if (null != fcAccbookDTO) {
                    vo.setGenerateMode(fcAccbookDTO.getGenerateMode());
                }
            }
        }
        return output;
    }
}
