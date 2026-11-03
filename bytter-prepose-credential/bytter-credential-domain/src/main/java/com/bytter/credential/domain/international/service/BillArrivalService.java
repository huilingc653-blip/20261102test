package com.bytter.credential.domain.international.service;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.workflow.dto.IWorkFlowResult;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.credential.api.constant.InternationalResourceConstant;
import com.bytter.credential.domain.international.dao.BillArrivalDao;
import com.bytter.credential.domain.international.entity.LcBillArrival;
import com.bytter.credential.domain.international.utils.CreditUtil;
import com.bytter.system.api.IBtCurrencyService;
import com.bytter.system.api.ICorpService;
import com.bytter.workflow.bpm.IWorkFlowListener;
import com.bytter.system.api.dto.BtCurrencyDTO;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Map;
/**
 * @Title
 * @Description
 * @Author liusy
 * @Date 2018-12-17 19:00
 * @Version 1.0
 */
@Transactional
@Service("billArrivalService")
public class BillArrivalService extends BaseService<LcBillArrival, String> implements IWorkFlowListener {
    @Autowired
    private BillArrivalDao billArrivalDao;
    @DubboReference(check = false)
	private ICorpService corpService;
    @DubboReference(check = false)
    private IBtCurrencyService currencyService;
    /**
     * 查询所有的已入库的信用证
     * @param dataTablesInput
     * @return
     */
    public DataTablesOutput listStorageCredit(DataTablesInput dataTablesInput, String userId){
        List<String> corpIds = corpService.getUserCorpIds(InternationalResourceConstant.INTERNATIONAL_CREDIT_BILLARRIVAL_QUERY, userId);
        return billArrivalDao.listStorageCredit(dataTablesInput, corpIds);
    }
    /**
     * 到单登记修改
     * @param dataTablesInput
     * @param userId
     * @return
     */
    public DataTablesOutput listArrivalUpdate(DataTablesInput dataTablesInput, String userId){
        List<String> corpIds = corpService.getUserCorpIds(InternationalResourceConstant.INTERNATIONAL_CREDIT_BILLARRIVAL_UPDATE, userId);
        return billArrivalDao.listArrivalUpdate(dataTablesInput, corpIds);
    }
    /**
     * 入库提交或暂存
     * @param lcBillArrival
     * @param user
     */
    public void saveOrTmpBillArrival(LcBillArrival lcBillArrival, ICurrentUser user) {

        if (null == lcBillArrival.getId()) {
            lcBillArrival.setCreateDate(new Date());
            lcBillArrival.setCreateBy(user.getLoginName());
            lcBillArrival.setArrivalCode(CreditUtil.getArrivalNo());
            BtCurrencyDTO btCurrencyDTO = currencyService.getObjectById(lcBillArrival.getCurId());
            lcBillArrival.setCurName(btCurrencyDTO.getCurName());
            /*0:进口(开证);1:出口(收到)*/
            lcBillArrival.setArrivalType(0);
            saveEntity(lcBillArrival);
        } else {
            LcBillArrival lcBillArrivalEntity = get(lcBillArrival.getId());
            entityPropertiesSet(lcBillArrival, lcBillArrivalEntity);
            lcBillArrivalEntity.setUpdateBy(user.getLoginName());
            lcBillArrivalEntity.setUpdateDate(new Date());
            updateEntity(lcBillArrivalEntity);
        }
    }
    /**
     * 到单报表查询
     * @param dataTablesInput
     * @param user
     * @return
     */
    public DataTablesOutput listReportBillArrival(DataTablesInput dataTablesInput, ICurrentUser user) {
        List<String> corpIds = corpService.getUserCorpIds(InternationalResourceConstant.INTERNATIONAL_CREDIT_BILLARRIVAL_REPORT_QUERY, user.getId());
        return billArrivalDao.listReportBillArrival(dataTablesInput, corpIds);
    }
    /**
     *
     * @param params
     * @return
     */
    public List getArrivalCountAndSumbillMoney(Map<String, Object> params) {
        return billArrivalDao.getArrivalCountAndSumbillMoney(params);
    }
    @Override
    public BaseDao<LcBillArrival, String> getEntityDao() {
        return billArrivalDao;
    }
    @Override
    public void flowCompleted(IWorkFlowResult workFlowResult) {
    }
    @Override
    public void flowStarted(IWorkFlowResult workFlowResult) {
    }
    private void entityPropertiesSet(LcBillArrival lcBillArrival, LcBillArrival lcBillArrivalEntity){
        /*取回日期*/
        lcBillArrivalEntity.setGetDate(lcBillArrival.getGetDate());
        /*手续费*/
        lcBillArrivalEntity.setHandlingCharge(lcBillArrival.getHandlingCharge());
        /*到单银行*/
        lcBillArrivalEntity.setHandDocBank(lcBillArrival.getHandDocBank());
        /*到单行编号*/
        lcBillArrivalEntity.setDocBankNo(lcBillArrival.getDocBankNo());
        /*到单日期*/
        lcBillArrivalEntity.setPaymentDate(lcBillArrival.getPaymentDate());
        /*付汇金额*/
        lcBillArrivalEntity.setPayMoney(lcBillArrival.getPayMoney());
        /*来单金额*/
        lcBillArrivalEntity.setBillMoney(lcBillArrival.getBillMoney());
        /*币别*/
        lcBillArrival.setCurId(lcBillArrival.getCorpId());
        /*付汇日期*/
        lcBillArrivalEntity.setPayDate(lcBillArrival.getPayDate());
        /*缴税日期*/
        lcBillArrivalEntity.setTaxDate(lcBillArrival.getTaxDate());
        /*增值税金额*/
        lcBillArrivalEntity.setAddTaxMoney(lcBillArrival.getAddTaxMoney());
        /*关税金额*/
        lcBillArrivalEntity.setTaxMoney(lcBillArrival.getTaxMoney());
        /*备注*/
        lcBillArrivalEntity.setRmk(lcBillArrival.getRmk());
        /*期限*/
        lcBillArrivalEntity.setPeriod(lcBillArrival.getPeriod());
        /*远期天数*/
        lcBillArrivalEntity.setDelayDates(lcBillArrival.getDelayDates());
        /*关单号*/
        lcBillArrivalEntity.setCloseLcNo(lcBillArrival.getCloseLcNo());
        /*状态copy*/
        lcBillArrivalEntity.setStatus(lcBillArrival.getStatus());
    }
}
