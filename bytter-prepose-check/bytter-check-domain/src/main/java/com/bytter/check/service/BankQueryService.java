package com.bytter.check.service;
import com.bytter.check.dao.BankQueryDao;
import com.bytter.check.entity.FcBankBill;
import com.bytter.check.vo.FcBankBillVo;
import com.bytter.check.vo.FcCompareAccTempVo;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.system.api.IBtBankAccService;
import com.bytter.system.api.IBtCurrencyService;
import com.bytter.system.api.ICorpService;
import com.bytter.system.api.dto.BtBankAccDTO;
import com.bytter.system.api.dto.BtCurrencyDTO;
import com.bytter.system.api.dto.FcAccSubjectDTO;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
/**
 * @author zhengxn
 * @version 1.0
 * @Title: 银行对账单
 * @Description: FC_BANK_BILL
 * @date 2017-06-07 11:30:25
 */
@Service("bankQueryService")
@Transactional
public class BankQueryService extends BaseService<FcBankBill, String> {
    @Autowired
    private BankQueryDao fcBankBillDao;
    @DubboReference(check = false)
    private ICorpService corpService;
    @DubboReference(check = false)
    private IBtBankAccService btBankAccService;
    @DubboReference(check = false)
    private IBtCurrencyService iBtCurrencyService;
    public List<FcBankBill> listQuery(String eqs_compareSign, String eqs_bankAcc, String eqs_corpId, Date ged_tallyDate, Date led_tallyDate) {
        return fcBankBillDao.listQuery(eqs_compareSign, eqs_bankAcc, ged_tallyDate, led_tallyDate);
    }
    /**
     * 查询table表
     **/
    @Transactional(readOnly = true)
    public DataTablesOutput listTable(DataTablesInput input, String resource, String userId) {
        List<String> corpAuth = corpService.getUserCorpIds(resource, userId);
        //查询账户
        List<String> accS = new ArrayList<>();
        //单位查询条件
        String corpId = input.getFormdata().get("eqs_corpId").toString();
        if(EmptyUtils.isNotEmpty(corpId)){
            corpAuth = new ArrayList<>();
            corpAuth.add(corpId);
            //清除掉单位查询条件
            input.getFormdata().remove("eqs_corpId");
        }
        List<BtBankAccDTO> accList = btBankAccService.getListByCorpIds(corpAuth);
        for (BtBankAccDTO acc : accList) {
            accS.add(acc.getBankAcc());
        }
        DataTablesOutput output = fcBankBillDao.listTable(input, accS);
        List<FcBankBillVo> voList = output.getData();
        if (voList.size() > 0) {
            Map<String, BtCurrencyDTO> curMap = iBtCurrencyService.getCurrency();
            for (FcBankBillVo vo : voList) {
                vo.setCurName(curMap.get(vo.getCurId()).getCurName());
            }
        }
        return output;
    }
    @Override
    public BaseDao<FcBankBill, String> getEntityDao() {
        return fcBankBillDao;
    }
}
