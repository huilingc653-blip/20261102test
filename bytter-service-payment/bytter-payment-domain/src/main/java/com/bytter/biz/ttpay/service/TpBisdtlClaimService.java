package com.bytter.biz.ttpay.service;
import com.bytter.biz.budget.spi.dto.FbsBudgetProcessDTO;
import com.bytter.biz.budget.spi.service.IFbsBudgetProcessService;
import com.bytter.biz.budget.spi.service.IFbsCorpBudgetService;
import com.bytter.biz.bdlink.api.constant.BisAccBalConstant;
import com.bytter.biz.payment.api.constant.TtPayConstant;
import com.bytter.biz.ttpay.dao.TpBisdtlClaimDao;
import com.bytter.biz.ttpay.vo.TPpBisdtlclaimVO;
import com.bytter.biz.ttpay.vo.TpMerchantVO;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.constant.BudgetConstant;
import com.bytter.core.constant.CommonConstant;
import com.bytter.core.utils.DateUtils;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.core.utils.StringUtils;
import com.bytter.system.api.*;
import com.bytter.system.api.dto.BtCurrencyDTO;
import com.bytter.system.api.dto.CorpDTO;
import com.bytter.system.api.dto.DictDTO;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.poi.ss.formula.functions.T;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
@Service("tpBisdtlClaimService")
@Transactional
public class TpBisdtlClaimService extends BaseService<T, String> {
    @DubboReference(check = false)
	private ICorpService corpService;
    @Autowired(required = false)
    private TpBisdtlClaimDao tpBisdtlClaimDao;
    @DubboReference(check = false)
	private IBtCurrencyService btCurrencyService;
    @DubboReference(check = false)
	private IConfigService configService;
    @DubboReference(check = false)
	private IFbsBudgetProcessService fbsBudgetProcessService;
    @DubboReference(check = false)
	private IFcExcrateAdjustService iFcExcrateAdjustService;
    @DubboReference(check = false)
	private IFbsCorpBudgetService fbsCorpBudgetService;
    @DubboReference(check = false)
    private IDictService dictService;
    @Transactional(readOnly = true)
    public DataTablesOutput getDataTablesUnion(DataTablesInput input) {
        List<String> corpAuthList = corpService.getUserCorpIds(TtPayConstant.TTPAY_RESOURCE_PAYDTL_OP, input.getCurrentUser().getId());
        DataTablesOutput data = tpBisdtlClaimDao.getDataTablesUnion(input, corpAuthList);
        this.setItem(data);
        return this.setJoinField(data);
    }
    /**
     * 设置关联字段
     * @param output
     * @return
     */
    public DataTablesOutput setJoinField(DataTablesOutput output) {
        if(!EmptyUtils.isEmpty(output.getData())) {
            List<TPpBisdtlclaimVO> list = output.getData();
            BtCurrencyDTO rmbCurDTO = btCurrencyService.getRMBCurrency();
            Map<String, CorpDTO> corpMap = corpService.getCorpMap();
            Map<String, Map<String, DictDTO>> listDictMap = dictService.listDictMap(TtPayConstant.TTPAY_PAYTYPE_DICT,TtPayConstant.TTP_REFUND_APPROVE_STATUS_DICT);
            for (TPpBisdtlclaimVO vo:list) {
                if (corpMap.containsKey(vo.getCorpId())) {
                    vo.setCorpName(corpMap.get(vo.getCorpId()).getName());
                }
                //默认为人民币
                if (rmbCurDTO != null) {
                    vo.setCurId(rmbCurDTO.getId());
                }
                //字典注入
                this.injectValues(vo, listDictMap);
            }
        }
        return output;
    }
    /**
     * 字典注入
     *
     * @param vo
     * @param listDictMap
     */
    private void injectValues(TPpBisdtlclaimVO vo, Map<String, Map<String, DictDTO>> listDictMap) {
        // 支付方式
        Optional.ofNullable(vo.getPayType()).ifPresent(payType -> {
            Map<String, DictDTO> dictDtoMap = listDictMap.get(TtPayConstant.TTPAY_PAYTYPE_DICT);
            Optional.ofNullable(dictDtoMap).flatMap(dictMap -> Optional.ofNullable(dictMap.get(payType)))
                    .ifPresent(dict -> vo.setPayTypeName(dict.getName()));
        });
        // 退款审批状态
        Optional.ofNullable(vo.getAppStatus()).ifPresent(appStatus -> {
            Map<String, DictDTO> dictDtoMap = listDictMap.get(TtPayConstant.TTP_REFUND_APPROVE_STATUS_DICT);
            Optional.ofNullable(dictDtoMap).flatMap(dictMap -> Optional.ofNullable(dictMap.get(appStatus)))
                    .ifPresent(dict -> vo.setAppStatusName(dict.getName()));
        });
    }
    public List<TpMerchantVO> getMchId(String cdSign, String corpId) {
        return tpBisdtlClaimDao.getMchId(corpId, cdSign);
    }
    @Override
    public BaseDao<T, String> getEntityDao() {
        return tpBisdtlClaimDao;
    }
    /**
     * 确认计划
     * @param tPpBisdtlclaimVO
     * @param user
     */
    public void sure(TPpBisdtlclaimVO tPpBisdtlclaimVO, ICurrentUser user) {
        //是否挂资金计划
        String budgetSel = configService.getConfig(TtPayConstant.TP_CONFIG_TYPE, TtPayConstant.TP_CONFIG_BUDGETSEL).getValue();
        if (tPpBisdtlclaimVO != null) {
            if (CommonConstant.YES.equals(budgetSel) && EmptyUtils.isNotEmpty(tPpBisdtlclaimVO.getBudget())) {
                tPpBisdtlclaimVO.setFbsStatus(BisAccBalConstant.FBS_STATUS_Y);
            } else if (CommonConstant.NO.equals(budgetSel)) {
                tPpBisdtlclaimVO.setFbsStatus(BisAccBalConstant.FBS_STATUS_Y);
            }
            if (!EmptyUtils.isBlank(tPpBisdtlclaimVO.getBusType())) {
                tPpBisdtlclaimVO.setBusType(tPpBisdtlclaimVO.getBusType());
            }
            if("002".equals(tPpBisdtlclaimVO.getPayType())){
                tpBisdtlClaimDao.updateAliById(tPpBisdtlclaimVO.getId(),tPpBisdtlclaimVO.getFbsStatus(),tPpBisdtlclaimVO.getBusType());
            }else if("003".equals(tPpBisdtlclaimVO.getPayType())){
                tpBisdtlClaimDao.updateWeiXinById(tPpBisdtlclaimVO.getId(),tPpBisdtlclaimVO.getFbsStatus(),tPpBisdtlclaimVO.getBusType());
            }
            this.saveBisAccDtlBudgetProcess(tPpBisdtlclaimVO, user);
        }
    }
    /**
     * 预算信息占用
     *
     * @param vo
     * @param user
     */
    public void saveBisAccDtlBudgetProcess(TPpBisdtlclaimVO vo, ICurrentUser user) {
        if (EmptyUtils.isNotEmpty(vo.getProcessDate())) {
            List<String[]> itemCodeList = new ArrayList();
            List<String[]> itemNameList = new ArrayList();
            List<BigDecimal[]> processMoneyList = new ArrayList();
            if (StringUtils.isNotBlank(vo.getBudget())) {
                String[] budgets = String.valueOf(vo.getBudget()).split(",");
                if (!EmptyUtils.isEmptyArray(budgets)) {
                    String[] itemCode = new String[budgets.length];
                    String[] itemName = new String[budgets.length];
                    BigDecimal[] processMoney = new BigDecimal[budgets.length];
                    for (int i = 0; i < budgets.length; i++) {
                        String[] budget = budgets[i].split(BudgetConstant.BUDGET_PROCESS_SPLIT);
                        itemCode[i] = budget[0];
                        itemName[i] = budget[1];
                        processMoney[i] = new BigDecimal(budget[2]);
                    }
                    itemCodeList.add(itemCode);
                    itemNameList.add(itemName);
                    processMoneyList.add(processMoney);
                }
            }
            for (int i = 0; i < itemCodeList.size(); i++) {
                this.batchProcess(vo, itemCodeList.get(i), itemNameList.get(i), processMoneyList.get(i), user);
            }
        }
    }
    /**
     * 预算信息写入
     *
     * @param entity
     * @param itemCode
     * @param itemName
     * @param processMoney
     * @param user
     */
    public void batchProcess(TPpBisdtlclaimVO entity, String[] itemCode, String[] itemName, BigDecimal[] processMoney, ICurrentUser user) {
        if (!EmptyUtils.isEmptyArray(itemCode)) {
            List<FbsBudgetProcessDTO> list = new ArrayList<FbsBudgetProcessDTO>();
            for (int i = 0; i < itemCode.length; i++) {
                FbsBudgetProcessDTO fbsBudgetProcessDTO = new FbsBudgetProcessDTO();
                fbsBudgetProcessDTO.setStatus(BudgetConstant.STATUE_CHECK);//状态:11在途，95占用
                fbsBudgetProcessDTO.setBillNo(entity.getTrandNo());//业务单据号
                fbsBudgetProcessDTO.setBillId(entity.getId());//保存流水ID
                fbsBudgetProcessDTO.setItemCode(itemCode[i]);//科目代码
                fbsBudgetProcessDTO.setItemName(itemName[i]);//科目名称
                fbsBudgetProcessDTO.setProcessMoney(new BigDecimal(Math.abs(processMoney[i].doubleValue())));//执行金额
                fbsBudgetProcessDTO.setProcessDate(DateUtils.str2Date(entity.getProcessDate(), new SimpleDateFormat("yyyy-MM-dd")));//执行日期
                fbsBudgetProcessDTO.setCorpId(entity.getCorpId());//单位id
                fbsBudgetProcessDTO.setCurId(entity.getCurId());//币别
                fbsBudgetProcessDTO.setBusinessName(BudgetConstant.BUSINESS_BISACCDTL_CLAIM);//执行业务名称  内网收款" 外部系统付款";
                fbsBudgetProcessDTO.setProcessSysname(BudgetConstant.PROCESS_SYSNAME_TP);//执行系统名称
                fbsBudgetProcessDTO.setCreateBy(user.getLoginName());
                fbsBudgetProcessDTO.setCreateDate(DateUtils.getDate());
                fbsBudgetProcessDTO.setPayeeAcc(entity.getPayAcc());
                fbsBudgetProcessDTO.setInnerSign(BudgetConstant.INNER_SIGN_0);
                list.add(fbsBudgetProcessDTO);
            }
            // 修改时，先删除之前的计划执行数据再保存
            this.fbsBudgetProcessService.deleteBudgetProcessForBill(BudgetConstant.BUSINESS_BISACCDTL_CLAIM, BudgetConstant.PROCESS_SYSNAME_TP, entity.getId());
            this.fbsBudgetProcessService.writeBudgetProcess(list, user);
        }
    }
    /**
     * 设置计划科目
     *
     * @param output
     */
    public void setItem(DataTablesOutput output) {
        if (null != output.getData()) {
            for (int i = 0; i < output.getData().size(); i++) {
                TPpBisdtlclaimVO vo = (TPpBisdtlclaimVO) output.getData().get(i);
                List<FbsBudgetProcessDTO> fbsBudgetProcessDTOList = fbsBudgetProcessService.getBudgetProcessByBillNoForBill(BudgetConstant.BUSINESS_BISACCDTL_CLAIM, BudgetConstant.PROCESS_SYSNAME_TP, vo.getId());
                String item = this.setItemInfo(fbsBudgetProcessDTOList);
                if (!EmptyUtils.isEmptyList(fbsBudgetProcessDTOList)) {
                    vo.setProcessDate(DateUtils.date2Str(fbsBudgetProcessDTOList.get(0).getProcessDate(), new SimpleDateFormat("yyyy-MM-dd")));
                }
                vo.setItemInfo(item);
                BigDecimal curExrate = iFcExcrateAdjustService.getExchangeRateBy(vo.getCurId(), null);
                vo.setCurExrate(curExrate);
                //单位是否受计划控制
                Boolean control = fbsCorpBudgetService.controlBudgetByCorpId(vo.getCorpId());
                vo.setControl(control);
                //设置状态
                if(StringUtils.isBlank(vo.getFbsStatus())){
                    vo.setFbsStatus(CommonConstant.NO);
                    // //inject(vo);
                }
            }
        }
    }
    /**
     * 设置计划科目详情
     *
     * @param fbsBudgetProcessDTOList
     */
    public String setItemInfo(List<FbsBudgetProcessDTO> fbsBudgetProcessDTOList) {
        String item = "";
        if (!EmptyUtils.isEmptyList(fbsBudgetProcessDTOList)) {
            for (int j = 0; j < fbsBudgetProcessDTOList.size(); j++) {
                if (j == 0) {
                    item = fbsBudgetProcessDTOList.get(j).getItemCode() + BudgetConstant.BUDGET_PROCESS_SPLIT
                            + fbsBudgetProcessDTOList.get(j).getItemName() + BudgetConstant.BUDGET_PROCESS_SPLIT
                            + fbsBudgetProcessDTOList.get(j).getProcessMoney().toString();
                } else {
                    item = item + "," + fbsBudgetProcessDTOList.get(j).getItemCode() + BudgetConstant.BUDGET_PROCESS_SPLIT
                            + fbsBudgetProcessDTOList.get(j).getItemName() + BudgetConstant.BUDGET_PROCESS_SPLIT
                            + fbsBudgetProcessDTOList.get(j).getProcessMoney().toString();
                }
            }
        }
        return item;
    }
    /**
     * 逻辑业务
     *
     * @param id
     * @param user
     * @param type 0 恢复 1还原 2删除
     */
    public void business(String id, ICurrentUser user, String type) {
        if (StringUtils.isNotBlank(type)) {
            TPpBisdtlclaimVO tPpBisdtlclaimVO = tpBisdtlClaimDao.getByUnion(id);
            if (BisAccBalConstant.TYPE_H.equals(type)) {
                //恢复
                if("002".equals(tPpBisdtlclaimVO.getPayType())){
                    tpBisdtlClaimDao.updateAliById(tPpBisdtlclaimVO.getId(),BisAccBalConstant.FBS_STATUS_N,"");
                }else if("003".equals(tPpBisdtlclaimVO.getPayType())){
                    tpBisdtlClaimDao.updateWeiXinById(tPpBisdtlclaimVO.getId(),BisAccBalConstant.FBS_STATUS_N,"");
                }
            } else if (BisAccBalConstant.TYPE_R.equals(type)) {
                //还原
                if("002".equals(tPpBisdtlclaimVO.getPayType())){
                    tpBisdtlClaimDao.updateAliById(tPpBisdtlclaimVO.getId(),BisAccBalConstant.FBS_STATUS_N,"");
                }else if("003".equals(tPpBisdtlclaimVO.getPayType())){
                    tpBisdtlClaimDao.updateWeiXinById(tPpBisdtlclaimVO.getId(),BisAccBalConstant.FBS_STATUS_N,"");
                }
            } else {
                //删除
                if("002".equals(tPpBisdtlclaimVO.getPayType())){
                    tpBisdtlClaimDao.updateAliById(tPpBisdtlclaimVO.getId(),BisAccBalConstant.FBS_STATUS_D,"");
                }else if("003".equals(tPpBisdtlclaimVO.getPayType())){
                    tpBisdtlClaimDao.updateWeiXinById(tPpBisdtlclaimVO.getId(),BisAccBalConstant.FBS_STATUS_D,"");
                }
            }
            List<FbsBudgetProcessDTO> fbsBudgetProcessDTOList = fbsBudgetProcessService.getBudgetProcessByBillNoForBill(BudgetConstant.BUSINESS_BISACCDTL_CLAIM, BudgetConstant.PROCESS_SYSNAME_TP, tPpBisdtlclaimVO.getId());
            if (!EmptyUtils.isEmptyList(fbsBudgetProcessDTOList)) {
                this.fbsBudgetProcessService.deleteBudgetProcessForBill(BudgetConstant.BUSINESS_BISACCDTL_CLAIM, BudgetConstant.PROCESS_SYSNAME_TP, tPpBisdtlclaimVO.getId());
            }
        }
    }
    /**
     * 根据id查找银行账户流水信息
     *
     * @param id
     * @return BtBankAccDtlVO
     */
    public TPpBisdtlclaimVO getByUnion(String id) {
        TPpBisdtlclaimVO vo = this.tpBisdtlClaimDao.getByUnion(id);
        List<FbsBudgetProcessDTO> fbsBudgetProcessDTOList = fbsBudgetProcessService.getBudgetProcessByBillNoForBill(BudgetConstant.BUSINESS_BISACCDTL_CLAIM, BudgetConstant.PROCESS_SYSNAME_TP, vo.getId());
        if (!EmptyUtils.isEmptyList(fbsBudgetProcessDTOList)) {
            vo.setProcessDate(DateUtils.date2Str(fbsBudgetProcessDTOList.get(0).getProcessDate(), new SimpleDateFormat("yyyy-MM-dd")));
        }
        return vo;
    }
    /**
     * 批量确认计划
     *
     * @param ids
     * @param type        Y 确认 D 删除
     * @param itemCode    科目代码
     * @param itemName    科目名称
     * @param processDate 执行时间
     * @return
     */
    public void batchBusiness(String ids, String itemCode, String itemName, String processDate, ICurrentUser user, String type, String batchBusType) {
        if (StringUtils.isNotBlank(type) && BisAccBalConstant.TYPE_D.equals(type)) {
            for (String id : ids.split(CommonConstant.COMMA_SPLIT)) {
                this.business(id, user, BisAccBalConstant.TYPE_D);
            }
        } else {
            String[] idStr = ids.split(CommonConstant.COMMA_SPLIT);
            for (int i=0;i<idStr.length;i++) {
                String id = idStr[i];
                TPpBisdtlclaimVO tPpBisdtlclaimVO = this.getByUnion(id);
                tPpBisdtlclaimVO.setFbsStatus(BisAccBalConstant.FBS_STATUS_Y);
                if(!EmptyUtils.isBlank(batchBusType)){
                    tPpBisdtlclaimVO.setBusType(batchBusType);
                }
                if("002".equals(tPpBisdtlclaimVO.getPayType())){
                    this.tpBisdtlClaimDao.updateAliById(tPpBisdtlclaimVO.getId(),tPpBisdtlclaimVO.getFbsStatus(),tPpBisdtlclaimVO.getBusType());
                }else{
                    this.tpBisdtlClaimDao.updateWeiXinById(tPpBisdtlclaimVO.getId(),tPpBisdtlclaimVO.getFbsStatus(),tPpBisdtlclaimVO.getBusType());
                }
                tPpBisdtlclaimVO.setBudget(itemCode + BudgetConstant.BUDGET_PROCESS_SPLIT + itemName
                        + BudgetConstant.BUDGET_PROCESS_SPLIT + tPpBisdtlclaimVO.getTrandMoney());
                tPpBisdtlclaimVO.setProcessDate(processDate);
                this.saveBisAccDtlBudgetProcess(tPpBisdtlclaimVO, user);
            }
        }
    }
}
