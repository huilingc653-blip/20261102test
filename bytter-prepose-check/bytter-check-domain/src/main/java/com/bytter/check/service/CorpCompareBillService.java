package com.bytter.check.service;

import cn.hutool.core.collection.CollectionUtil;
import com.bytter.biz.finacial.api.service.IFcAccBalanceService;
import com.bytter.check.dao.CorpCompareBillDao;
import com.bytter.check.entity.CorpCompareBill;
import com.bytter.check.vo.CorpCompareBillInputVO;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.workflow.bpm.IWorkFlowListener;
import com.bytter.workflow.bpm.annotation.WorkFlow;
import com.bytter.workflow.dto.IWorkFlowResult;
import com.bytter.workflow.dto.WorkFlowBusiness;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.constant.CommonConstant;
import com.bytter.core.utils.BeanUtils;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.core.utils.SqlUtil;
import com.bytter.system.api.*;
import com.bytter.system.api.constant.AccountGrantConstant;
import com.bytter.system.api.constant.SysConfigConstant;
import com.bytter.system.api.dto.BtCurrencyDTO;
import com.bytter.system.api.dto.CorpDTO;
import com.bytter.system.api.dto.FcAccDTO;
import com.bytter.system.api.dto.UserDTO;
import com.bytter.workflow.api.IWorkFlowService;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 单位对账单service
 *
 * @author zhoul 2020-11-03
 * @since 3.4
 */
@WorkFlow("corpCompareBill")
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
    private IFcAccBalanceService fcAccBalanceService;
    @DubboReference(check = false)
    private IBtCurrencyService btCurrencyService;
    @DubboReference(check = false)
    private IConfigService configService;
    @DubboReference(check = false)
    private IAccountGrantService accountGrantService;
    @DubboReference(check = false)
    private IUserService userService;
    @Override
    public BaseDao<CorpCompareBill, String> getEntityDao() {
        return corpCompareBillDao;
    }
    public DataTablesOutput getCorpCompareBillInputVO(DataTablesInput dataTablesInput, String userId) {
        StringBuilder sql = new StringBuilder();
        String type = (String) dataTablesInput.getFormdata().get("type");
        List<String> idList = new ArrayList<>();
        WorkFlowBusiness workFlowBusiness = null;
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
        }else {
            workFlowBusiness = workFlowService.getWorkFlowBusiness("corpCompareBill", dataTablesInput);
            idList = workFlowBusiness.getBusinessIdList();
        }
        DataTablesOutput dataTablesOutput = corpCompareBillDao.getCorpCompareBillInputVO(dataTablesInput, sql.toString(),idList);
        List<CorpCompareBillInputVO> list = dataTablesOutput.getData();
        if(EmptyUtils.isNotEmpty(list)){
            Map<String, CorpDTO> corpMap = corpService.getCorpMap();
            if(null != corpMap){
                for(CorpCompareBillInputVO vo : list){
                    CorpDTO corpDTO = corpMap.get(vo.getCorpId());
                    if(null != corpDTO){
                        vo.setCorpName(corpDTO.getName());
                    }
                    if(com.bytter.core.utils.StringUtils.isNotEmpty(vo.getCurrencyId())){
                        BtCurrencyDTO btCurrencyDTO=btCurrencyService.getObjectById(vo.getCurrencyId());
                        vo.setCurrencyName(btCurrencyDTO.getCurName());
                    }
                    if(com.bytter.core.utils.StringUtils.isNotEmpty(vo.getOpCode())){
                        UserDTO userDTO=userService.getByUserId(vo.getOpCode());
                        vo.setOpUserName(userDTO.getUsername());
                    }
                }
            }
        }
        if (workFlowBusiness == null || CollectionUtil.isEmpty(workFlowBusiness.getData())) {
            return dataTablesOutput;
        } else {
            if(dataTablesOutput.getData() != null && dataTablesOutput.getData().size()>0){
                return workFlowBusiness.merge(dataTablesOutput);
            }
            return dataTablesOutput;
        }
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
        BtCurrencyDTO currencyDTO = btCurrencyService.getObjectById(accDTOByAccount.getCurId());
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
        return fcAccBalanceService.existBalance(date);
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
