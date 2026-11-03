package com.bytter.bill.domain.bill.service;
import com.bytter.bill.api.bill.constant.AccebillConstant;
import com.bytter.bill.domain.bill.dao.AbBillCorpBalDao;
import com.bytter.bill.domain.bill.entity.AbBillAccBal;
import com.bytter.bill.domain.bill.vo.AbBillCorpBalVO;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.system.api.IBtCurrencyService;
import com.bytter.system.api.dto.BtCurrencyDTO;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
/**
 * @author zhengg
 * @version 1.0
 * @Title: 票据单位余额Service
 * @Description: AB_BILL_ACC_BAL
 * @date 2017-09-28 19:20:01
 */
@Service("abBillCorpBalService")
@Transactional
public class AbBillCorpBalService extends BaseService<AbBillAccBal, String> {
    @Autowired
    private AbBillCorpBalDao abBillCorpBalDao;
    @Autowired
    private AbCorpService abCorpService;
    @Autowired
    private AbAccountGrantService accountGrantService;
    @DubboReference(check = false)
    private IBtCurrencyService iBtCurrencyService;
    @Override
    public BaseDao<AbBillAccBal, String> getEntityDao() {
        return abBillCorpBalDao;
    }
    @SuppressWarnings("rawtypes")
    @Transactional(readOnly = true)
    public DataTablesOutput listAbBillCorpBal(DataTablesInput dataTablesInput) {
        List<String> corpAuthList = abCorpService.getUserCorpIds(AccebillConstant.AB_BILL_CORPBAL_QUERY_CORPOP, dataTablesInput.getCurrentUserId());
        String corpIds = dataTablesInput.getFormdata().get("ins_corpId") == null ? null : String.valueOf(dataTablesInput.getFormdata().get("ins_corpId"));
        //设置是否过滤票据账号
        accountGrantService.setElectricBillAccount(dataTablesInput, AccebillConstant.AB_BILL_CORPBAL_QUERY_CORPOP, corpIds);
        DataTablesOutput dataTablesOutput = abBillCorpBalDao.listAbBillCorpBal(dataTablesInput, corpAuthList);
        List<AbBillCorpBalVO> list = dataTablesOutput.getData();
        List<String> curIdList = list.stream().map(AbBillCorpBalVO::getCurId).collect(Collectors.toList());
        Map<String, BtCurrencyDTO> curMap = iBtCurrencyService.getCurrencyByIds(curIdList);
        for (AbBillCorpBalVO vo : list) {
            BtCurrencyDTO dto = curMap.get(vo.getCurId());
            vo.setCurName(dto.getCurName());
        }
        return dataTablesOutput;
    }
}
