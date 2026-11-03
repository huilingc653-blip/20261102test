package com.bytter.check.service;


import com.bytter.biz.budget.spi.dto.FbsBudgetProcessDTO;
import com.bytter.biz.budget.spi.service.IFbsBudgetProcessService;
import com.bytter.biz.bdlink.api.dto.BisAccDtlBudgetRuleDTO;
import com.bytter.biz.bdlink.api.dto.BisAccDtlDTO;
import com.bytter.biz.payment.api.dto.NisBusTypeDTO;
import com.bytter.biz.bdlink.api.service.IBisAccDtlService;
import com.bytter.biz.payment.api.service.INisBusTypeService;
import com.bytter.biz.bdlink.vo.BisAccDtlBudgetRuleVO;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.exception.ParameterException;
import com.bytter.core.constant.BudgetConstant;
import com.bytter.core.constant.CommonConstant;
import com.bytter.core.constant.NetbankConstant;
import com.bytter.core.utils.*;
import com.bytter.system.api.IBtBankAccService;
import com.bytter.system.api.IBtCurrencyService;
import com.bytter.system.api.ICorpService;
import com.bytter.system.api.dto.BtBankAccDTO;
import com.bytter.system.api.dto.BtCurrencyDTO;
import com.bytter.system.api.dto.CorpDTO;
import com.bytter.system.api.enums.NetbankMessageEnum;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;

/**
 * @author onlineGenerator
 * @version 1.0
 * @Title: Service
 * @Description: 银行流水明细查询
 * @date 2017-07-05 11:20:29
 */
@Service("bisAccDtlBudgetRuleService")
@Transactional
public class BisAccDtlBudgetRuleService extends BaseService<BisAccDtlBudgetRuleVO, String> {
    @DubboReference(check = false)
    private IBisAccDtlService accDtlService;
    @DubboReference(check = false)
    private IFbsBudgetProcessService fbsBudgetProcessService;
    @DubboReference(check = false)
    private IBtBankAccService btBankAccService;
    @DubboReference(check = false)
    private ICorpService corpService;
    @DubboReference(check = false)
    private INisBusTypeService nisBusTypeService;
    @DubboReference(check = false)
    private IBtCurrencyService btCurrencyService;
    @Override
    public BaseDao<BisAccDtlBudgetRuleVO, String> getEntityDao() {
        return null;
    }
    public DataTablesOutput getDataTablesUnion(DataTablesInput dataTablesInput, String joinSql) {
        DataTablesOutput dataTablesOutput = accDtlService.getDataTablesUnion(dataTablesInput, joinSql);
        List<BisAccDtlBudgetRuleVO> list = dataTablesOutput.getData();
        List<BisAccDtlBudgetRuleDTO> bisAccDtlBudgetRuleVOList = BeanUtils.copyListProperties(list, BisAccDtlBudgetRuleDTO.class);
        for(BisAccDtlBudgetRuleDTO bisAccDtlBudgetRuleVO:bisAccDtlBudgetRuleVOList){
            this.injectValue(bisAccDtlBudgetRuleVO);
        }
        dataTablesOutput.setData(bisAccDtlBudgetRuleVOList);
        return dataTablesOutput;
    }
    public BisAccDtlBudgetRuleDTO getBisAccDtlByIdUnion(String id) {
        BisAccDtlBudgetRuleDTO bisAccDtlBudgetRuleVO = accDtlService.getBisAccDtlByIdUnion(id);
        this.injectValue(bisAccDtlBudgetRuleVO);
        return bisAccDtlBudgetRuleVO;
    }
    public List<BisAccDtlBudgetRuleDTO> getBisAccDtlByMergeId(String mergeId) {
        List<BisAccDtlBudgetRuleDTO> bisAccDtlBudgetRuleVOList = accDtlService.getBisAccDtlByMergeId(mergeId);
        for(BisAccDtlBudgetRuleDTO bisAccDtlBudgetRuleVO:bisAccDtlBudgetRuleVOList){
            this.injectValue(bisAccDtlBudgetRuleVO);
        }
        return bisAccDtlBudgetRuleVOList;
    }
    /**
     * 撤销拆分
     * @param id
     */
    public void splitCancel(String id, ICurrentUser user) {
        BisAccDtlDTO bisAccDtl = accDtlService.getDTOById(id);
        //根据拆分单号获取拆分前单据
        BisAccDtlDTO old = accDtlService.getUniqueBy("id", bisAccDtl.getSplitId());
        //获取拆分明细
        List<BisAccDtlDTO> list = accDtlService.listBy("splitId", old.getId());
        //校验拆分明细单状态必须都是未确认的
        if (!EmptyUtils.isEmptyList(list)) {
            String billNos = "";
            for (BisAccDtlDTO entity : list) {
                if (!EmptyUtils.isEmpty(entity.getFbsStatus()) && "1".equals(entity.getFbsStatus())) {
                    if (StringUtils.isBlank(billNos)) {
                        billNos = entity.getId();
                    } else {
                        billNos = billNos + CommonConstant.COMMA_SPLIT + entity.getId();
                    }
                }
            }
            if (StringUtils.isNotBlank(billNos)) {
                throw new ParameterException(NetbankMessageEnum.NIS_PAY_MESSAGE_SPLITCANCEL_VALID,"");
            }
        }
        //还原拆分前单据
        old.setStatus(null);
        old.setUpdateBy(user.getLoginName());
        old.setUpdateDate(DateUtils.getDate());
        accDtlService.updateDtlByDto(old);
        // 还原拆分前单据的计划
        fbsBudgetProcessService.updateBudgetProcessForBill(BudgetConstant.BUSINESS_NAME_BISACCDTL, BudgetConstant.PROCESS_SYSNAME_NIS, old.getId(), NetbankConstant.STATUE_TEMP, user);
        //更新拆分明细单状态
        for (BisAccDtlDTO t : list) {
            t.setStatus(NetbankConstant.STATUE_DEL);
            t.setSplitId(null);
            t.setUpdateBy(user.getLoginName());
            t.setUpdateDate(DateUtils.getDate());
            accDtlService.updateDtlByDto(t);
        }
    }
    /**
     * 付款单合并
     *
     * @param ids
     */
    public void merge(String ids, ICurrentUser user) {
        List<BisAccDtlDTO> list = new ArrayList<>();
        String[] idAtr = ids.split(CommonConstant.COMMA_SPLIT);
        //获取第一笔合并单据
        BisAccDtlDTO t1 = accDtlService.getDTOById(idAtr[0]);
        //获取第一笔合并单据的资金计划
        List<FbsBudgetProcessDTO> t1ListBudget = fbsBudgetProcessService.getBudgetProcessByBillNoForBill(BudgetConstant.BUSINESS_NAME_BISACCDTL, BudgetConstant.PROCESS_SYSNAME_NIS, t1.getId());
        //数据校验
        for (String id : idAtr) {
            BisAccDtlDTO t = accDtlService.getDTOById(id);
            list.add(t);
            //非原始单拆分单据不可进行合并
            if (!StringUtils.isEmpty(t.getSplitId())) {
                throw new ParameterException(NetbankMessageEnum.NIS_PAY_MESSAGE_SPLIT_NOT_MERGE);
            }
            //合并单据不可进行合并
            if (this.isMergeBill(t.getId())) {
                throw new ParameterException(NetbankMessageEnum.NIS_PAY_MESSAGE_MERGE_NOT_MERGE);
            }
            if (t1.getId().equals(t.getId())) {
                continue;
            }
            //付款账号一致
            if (!StringUtils.isEmpty(t1.getOppAccNo()) && !t1.getOppAccNo().equals(t.getOppAccNo())) {
                throw new ParameterException(NetbankMessageEnum.NIS_PAY_MESSAGE_PAYEEACC_SAME);
            }
            //付款币别一致
            if (!t1.getCurId().equals(t.getCurId())) {
                throw new ParameterException(NetbankMessageEnum.NIS_PAY_MESSAGE_CUR_SAME);
            }
            //收款人一致
            if (!t1.getAccName().equals(t.getAccName())) {
                throw new ParameterException(NetbankMessageEnum.NIS_PAY_MESSAGE_PAYEEACCNAME_SAME);
            }
            //收款账号一致
            if (!t1.getBankAcc().equals(t.getBankAcc())) {
                throw new ParameterException(NetbankMessageEnum.NIS_PAY_MESSAGE_PAYEEACC_SAME);
            }
            //收款开户行一致
            if (!StringUtils.isEmpty(t1.getBankName()) && !t1.getBankName().equals(t.getBankName())) {
                throw new ParameterException(NetbankMessageEnum.NIS_PAY_MESSAGE_PAYEEBANK_SAME);
            }
            List<FbsBudgetProcessDTO> tListBudget = fbsBudgetProcessService.getBudgetProcessByBillNoForBill(BudgetConstant.BUSINESS_NAME_BISACCDTL, BudgetConstant.PROCESS_SYSNAME_NIS, t.getId());
            if ((!EmptyUtils.isEmptyList(tListBudget) && EmptyUtils.isEmptyList(t1ListBudget)) ||
                    (!EmptyUtils.isEmptyList(t1ListBudget) && EmptyUtils.isEmptyList(tListBudget))) {
                throw new ParameterException(NetbankMessageEnum.NIS_PAY_MESSAGE_IS_FBSBUDGET);
            }
        }
        BigDecimal amt = BigDecimal.ZERO;
        //合并计划
        List<FbsBudgetProcessDTO> listBudget = new ArrayList<FbsBudgetProcessDTO>();
        BisAccDtlDTO bisAccDtl = new BisAccDtlDTO();
        String remake = "";
        for (int i =0;i<list.size();i++) {
            BisAccDtlDTO t = list.get(i);
            BeanUtils.copyNotNullProperties(t, bisAccDtl);
            //合计金额
            amt = amt.add(t.getAmt());
            if(EmptyUtils.isNotEmpty(t.getRemark())) {
                if (i == 0) {
                    remake = t.getRemark();
                } else {
                    remake += ";" + t.getRemark();
                }
            }
            List<FbsBudgetProcessDTO> listProcess = fbsBudgetProcessService.getBudgetProcessByBillNoForBill(BudgetConstant.BUSINESS_NAME_PAY, BudgetConstant.PROCESS_SYSNAME_NIS, t.getId());
            if (!EmptyUtils.isEmptyList(listProcess)) {
                listBudget.addAll(listProcess);
                //释放合并单据的资金计划
                fbsBudgetProcessService.releaseBudgetProcessForBill(BudgetConstant.BUSINESS_NAME_PAY, BudgetConstant.PROCESS_SYSNAME_NIS, t.getId());
            }
        }
        String newId = UUID.randomUUID().toString().replace("-", "");
        bisAccDtl.setId(newId);
        bisAccDtl.setSplitId(null);
        //汇总金额
        bisAccDtl.setAmt(amt);
        bisAccDtl.setCreateBy(user.getLoginName());
        bisAccDtl.setCreateDate(DateUtils.getDate());
        bisAccDtl.setRemark(remake);
        //生成合并单据
        accDtlService.saveDtlByDto(bisAccDtl);
        //合并资金计划
        if (!EmptyUtils.isEmptyList(listBudget)) {
            HashMap<String, FbsBudgetProcessDTO> mapBudget = new HashMap<String, FbsBudgetProcessDTO>();
            for (FbsBudgetProcessDTO fbsBudgetProcessDTO : listBudget) {
                if (!mapBudget.containsKey(fbsBudgetProcessDTO.getItemCode())) {
                    mapBudget.put(fbsBudgetProcessDTO.getItemCode(), fbsBudgetProcessDTO);
                } else {
                    //相同科目合计金额
                    FbsBudgetProcessDTO dto = mapBudget.get(fbsBudgetProcessDTO.getItemCode());
                    dto.setProcessMoney(dto.getProcessMoney().add(fbsBudgetProcessDTO.getProcessMoney()));
                    dto.setProcessCurMoney(dto.getProcessCurMoney().add(fbsBudgetProcessDTO.getProcessCurMoney()));
                    mapBudget.put(dto.getItemCode(), dto);
                }
            }
            listBudget = new ArrayList<FbsBudgetProcessDTO>();
            for (Map.Entry<String, FbsBudgetProcessDTO> entry : mapBudget.entrySet()) {
                listBudget.add(entry.getValue());
            }
            for (FbsBudgetProcessDTO fbsBudgetProcessDTO : listBudget) {
                fbsBudgetProcessDTO.setId(null);
                fbsBudgetProcessDTO.setBillId(bisAccDtl.getId());
                fbsBudgetProcessDTO.setBillNo(bisAccDtl.getAccNo());
                fbsBudgetProcessDTO.setCreateBy(bisAccDtl.getCreateBy());
                fbsBudgetProcessDTO.setCreateDate(bisAccDtl.getUpdateDate());
                fbsBudgetProcessDTO.setUpdateBy(user.getLoginName());
                fbsBudgetProcessDTO.setUpdateDate(DateUtils.getDate());
                fbsBudgetProcessDTO.setStatus(BudgetConstant.STATUE_TEMP);
            }
            fbsBudgetProcessService.writeBudgetProcess(listBudget, user);
        }
        //更新合并明细单状态
        for (BisAccDtlDTO t : list) {
            t.setStatus(NetbankConstant.STATUE_DEL);
            //保存合并单号
            t.setMergeId(newId);
            t.setUpdateBy(user.getLoginName());
            t.setUpdateDate(DateUtils.getDate());
            accDtlService.updateDtlByDto(t);
        }
    }
    /**
     * 撤销合并
     *
     * @param ids
     */
    public void mergeCancel(String ids, ICurrentUser user) {
        for (String id : ids.split(CommonConstant.COMMA_SPLIT)) {
            //获取合并明细
            List<BisAccDtlDTO> list = accDtlService.listBy("mergeId", id);
            if (list == null || list.size() <= 0) {
                //非撤销合并单据，不可进行撤销合并操作
                throw new ParameterException(NetbankMessageEnum.NIS_PAY_MESSAGE_MERGECANCEL_VALID);
            } else {
                BisAccDtlDTO t = accDtlService.getDTOById(id);
                for (BisAccDtlDTO n : list) {
                    n.setStatus(null);
                    n.setMergeId(null);
                    n.setUpdateBy(user.getLoginName());
                    n.setUpdateDate(DateUtils.getDate());
                    accDtlService.updateDtlByDto(n);
                    //撤销合并还原预算执行
                    fbsBudgetProcessService.updateBudgetProcessForBill(BudgetConstant.BUSINESS_NAME_BISACCDTL, BudgetConstant.PROCESS_SYSNAME_NIS, n.getId(), BudgetConstant.STATUE_TEMP, user);
                }
                //删除合并单据
                t.setStatus(NetbankConstant.STATUE_DEL);
                t.setUpdateBy(user.getLoginName());
                t.setUpdateDate(DateUtils.getDate());
                accDtlService.updateDtlByDto(t);
                //删除合并预算
                fbsBudgetProcessService.releaseBudgetProcess(BudgetConstant.BUSINESS_NAME_BISACCDTL, BudgetConstant.PROCESS_SYSNAME_NIS, t.getId());
            }
        }
    }
    /**
     * 判断是否是合并单据
     *
     * @param id
     * @return
     */
    public boolean isMergeBill(String id) {
        return  this.accDtlService.isMergeBill(id);
    }
    public void injectValue(BisAccDtlBudgetRuleDTO bisAccDtlBudgetRuleVO){
        BtBankAccDTO btBankAccDTO = btBankAccService.getBankAccInfoByBankAcc(bisAccDtlBudgetRuleVO.getBankAcc());
        if(!EmptyUtils.isEmpty(btBankAccDTO)){
            bisAccDtlBudgetRuleVO.setCorpId(btBankAccDTO.getCorpId());
            CorpDTO corpDTO = corpService.get(bisAccDtlBudgetRuleVO.getCorpId());
            if(!EmptyUtils.isEmpty(corpDTO)){
                bisAccDtlBudgetRuleVO.setCorpName(corpDTO.getName());
            }
        }
        List<FbsBudgetProcessDTO> fbsBudgetProcessDTOList = fbsBudgetProcessService.getBudgetProcessByBillNo(BudgetConstant.BUSINESS_NAME_BISACCDTL, BudgetConstant.PROCESS_SYSNAME_NIS, bisAccDtlBudgetRuleVO.getBillId());
        if(!EmptyUtils.isEmpty(fbsBudgetProcessDTOList)){
            bisAccDtlBudgetRuleVO.setProcessDateTime(fbsBudgetProcessDTOList.get(0).getProcessDate());
        }
        NisBusTypeDTO nisBusTypeDTO = nisBusTypeService.getById(bisAccDtlBudgetRuleVO.getBusType());
        if(!EmptyUtils.isEmpty(nisBusTypeDTO)){
            bisAccDtlBudgetRuleVO.setBusTypeName(nisBusTypeDTO.getBusTypeName());
        }
        BtCurrencyDTO btCurrencyDTO = btCurrencyService.getObjectById(bisAccDtlBudgetRuleVO.getCurId());
        if(!EmptyUtils.isEmpty(btCurrencyDTO)){
            bisAccDtlBudgetRuleVO.setCurName(btCurrencyDTO.getCurName());
        }
    }
    public void injectValueBifName(BisAccDtlBudgetRuleVO bisAccDtlBudgetRuleVO){
    }
}
