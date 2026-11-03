package com.bytter.biz.compareBill.service;
import com.bytter.biz.compareBill.dao.CorpCompareBillDao;
import com.bytter.biz.compareBill.entity.CorpCompareBill;
import com.bytter.biz.compareBill.vo.CorpCompareBillInputVO;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.workflow.bpm.annotation.WorkFlow;
import com.bytter.workflow.dto.IWorkFlowResult;
import com.bytter.workflow.dto.WorkFlowBusiness;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.constant.CommonConstant;
import com.bytter.core.utils.BeanUtils;
import com.bytter.core.utils.SqlUtil;
import com.bytter.system.api.*;
import com.bytter.system.api.constant.AccountGrantConstant;
import com.bytter.system.api.constant.SysConfigConstant;
import com.bytter.system.api.dto.BtCurrencyDTO;
import com.bytter.system.api.dto.FcAccDTO;
import com.bytter.workflow.bpm.IWorkFlowListener;
import com.bytter.workflow.api.IWorkFlowService;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import java.util.Date;
import java.util.List;
/**
 * 单位对账单service
 *
 * @author zhoul 2020-11-03
 * @since 3.4
 */
@WorkFlow("corpCompareBill")
@DubboService(interfaceClass = IWorkFlowListener.class,group = "corpCompareBill")
@Service
@Transactional
public class CorpCompareBillService extends BaseService<CorpCompareBill, String> implements IWorkFlowListener {
    @Autowired
    private CorpCompareBillDao corpCompareBillDao;
    @Autowired
	private IWorkFlowService workFlowService;
    @DubboReference(check = false)
	private ICorpService corpService;
    @DubboReference(check = false)
	private IFcAccService fcAccService;
    @DubboReference(check = false)
	private IBtCurrencyService currencyService;
    @DubboReference(check = false)
	private IConfigService configService;
    @DubboReference(check = false)
	private IAccountGrantService accountGrantService;
    @Override
    public BaseDao<CorpCompareBill, String> getEntityDao() {
        return corpCompareBillDao;
    }
    public DataTablesOutput getCorpCompareBillInputVO(DataTablesInput dataTablesInput, String userId) {
        StringBuilder sql = new StringBuilder();
        String type = (String) dataTablesInput.getFormdata().get("type");
        //不为审批列表
        if (!"approve".equals(type)) {
            //权限控制类型  1-单位级 2-账号级
            String controlType = configService.getConfig(SysConfigConstant.Module.SYS, SysConfigConstant.CONTROLTYPE).getValue();
            if (CommonConstant.YES.equals(controlType)) {
                String corpAuthSql = SqlUtil.getInSql(
                        corpService.getUserCorpIds("fc:corpCompareBill", userId), "fa.corp_id",
                        true);
                sql.append(" and ").append(corpAuthSql);
            } else {
                List<String> accList = accountGrantService.listByMultiVal(userId, "fc:corpCompareBill", null, AccountGrantConstant.INNER_BANK_TYPE, AccountGrantConstant.QUERY_BALANCE);
                String accAuthSql = SqlUtil.getInSql(accList, "fa.id", true);
                sql.append(" and ").append(accAuthSql);
            }
        } else {
            WorkFlowBusiness workFlowBusiness = workFlowService.getWorkFlowBusiness("corpCompareBill", dataTablesInput);
            List<String> idList = workFlowBusiness.getBusinessIdList();
        }
        return corpCompareBillDao.getCorpCompareBillInputVO(dataTablesInput, sql.toString(), userId);
    }
    public void saveOrUpdate(CorpCompareBill corpCompareBill, String billId, String userId) {
        if (StringUtils.isEmpty(billId)) {
            corpCompareBill.setPrints(0);
            corpCompareBill.setOpDate(new Date());
            corpCompareBillDao.save(corpCompareBill);
        } else {
            CorpCompareBill corpCompareBillDB = corpCompareBillDao.get(billId);
            BeanUtils.copyNotNullProperties(corpCompareBill, corpCompareBillDB);
            corpCompareBill = corpCompareBillDB;
            corpCompareBillDao.update(corpCompareBillDB);
        }
        if ("1".equals(corpCompareBill.getIsCheck())) {
            corpCompareBill.setErpBalance(corpCompareBill.getAccountBalance());
        }
        //若为待审批启动流程
        if (11 == corpCompareBill.getStatus()) {
             getStartHandler().handler("corpCompareBill", corpCompareBill.getId(), corpCompareBill, corpCompareBill.getCorpId(), userId);
        }
    }
    public CorpCompareBillInputVO getCorpCompareBillByBillIdOrAccountAndDate(String billId, String account, String date) {
        CorpCompareBillInputVO corpCompareBillInputVO = new CorpCompareBillInputVO();
        if (StringUtils.hasText(billId) && billId.length() == 32) {
            CorpCompareBill corpCompareBill = corpCompareBillDao.get(billId,true);
            BeanUtils.copyProperties(corpCompareBill, corpCompareBillInputVO);
            corpCompareBillInputVO.setBalance(corpCompareBill.getAccountBalance() == null ? "0" : corpCompareBill.getAccountBalance().toString());
            corpCompareBillInputVO.setErpBalance(corpCompareBill.getErpBalance() == null ? "0" : corpCompareBill.getErpBalance().toString());
            corpCompareBillInputVO.setSame(corpCompareBill.getIsCheck());
            corpCompareBillInputVO.setStatus(corpCompareBill.getStatus().toString());
            account = corpCompareBill.getAccount();
        } else {
            String balance = corpCompareBillDao.getBalanceByAccountAndDate(account, date);
            corpCompareBillInputVO.setBalance(balance);
            corpCompareBillInputVO.setSame("1");
            corpCompareBillInputVO.setStatus("-2");
        }
        FcAccDTO accDTOByAccount = fcAccService.getAccDTOByAccount(account);
        BtCurrencyDTO currencyDTO = currencyService.getObjectById(accDTOByAccount.getCurId());
        corpCompareBillInputVO.setBillId(billId);
        corpCompareBillInputVO.setAccount(account);
        corpCompareBillInputVO.setCorpId(accDTOByAccount.getCorpId());
        corpCompareBillInputVO.setAccountType(accDTOByAccount.getAccType());
        corpCompareBillInputVO.setAccountName(accDTOByAccount.getAccName());
        corpCompareBillInputVO.setCurrencyName(currencyDTO.getCurName());
        corpCompareBillInputVO.setCorpName(accDTOByAccount.getCorpName());
        corpCompareBillInputVO.setNetName(accDTOByAccount.getNetName());
        return corpCompareBillInputVO;
    }
    public boolean existBalance(String date) {
        return corpCompareBillDao.existBalance(date);
    }
    public void updateBillPrintCount(String billId) {
        corpCompareBillDao.updateBillPrintCount(billId);
    }
    @Override
    public void flowCompleted(IWorkFlowResult workFlowResult) {
        String businessKey = workFlowResult.getBusinessKey();
        CorpCompareBill corpCompareBill = corpCompareBillDao.get(businessKey);
        corpCompareBill.setCheckDate(new Date());
        if (workFlowResult.isPass()) {
            corpCompareBill.setStatus(95);
        } else {
            corpCompareBill.setStatus(-1);
        }
        corpCompareBillDao.update(corpCompareBill);
    }
    @Override
    public void flowStarted(IWorkFlowResult workFlowResult) {
    }
}
