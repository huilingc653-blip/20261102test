package com.bytter.bill.domain.bill.service;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import com.bytter.bill.api.bill.constant.AccebillConstant;
import com.bytter.bill.domain.bill.bean.DSLAbEmitBillPayBean;
import com.bytter.bill.domain.bill.bean.DSLAbEmitBillPayResponseBean;
import com.bytter.bill.domain.bill.dao.ErpAbEmitBillPayDao;
import com.bytter.bill.domain.bill.entity.AbAccrecords;
import com.bytter.bill.domain.bill.entity.AbVoucherinfo;
import com.bytter.bill.domain.bill.entity.AbVoucherinfoVice;
import com.bytter.bill.domain.bill.entity.DSLInterfaceLogBill;
import com.bytter.bill.domain.bill.vo.AbBillPayAndReceVO;
import com.bytter.bill.domain.bill.vo.AbEmitBillPayVo;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.utils.DateUtils;
import com.bytter.core.utils.StringUtil;
import com.bytter.core.utils.StringUtils;
import com.bytter.guarantee.api.IFcBondHookService;
import com.bytter.guarantee.dto.FcBondApplyDTO;
import com.bytter.system.api.*;
import com.bytter.system.api.dto.*;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author lijinquan
 * @description:
 * @date 2024-09-03 18:05
 */
@Service("erpAbEmitBillPayService")
@Transactional
public class ErpAbEmitBillPayService {

    @DubboReference(check = false)
    private IUserService userService;
    @DubboReference(check = false)
    private IDictService dictService;
    @Autowired
    private ErpAbEmitBillPayDao erpAbEmitBillPayDao;
    @Autowired
    private AbEmitBillPayService abEmitBillPayService;
    @Autowired
    private AbVoucherinfoViceService abVoucherinfoViceService;
    @Autowired
    private AbConfigService abConfigService;
    @DubboReference(check = false)
    private IExternalCorpService externalCorpService;
    @DubboReference(check = false)
    private IBtBankAccService iBtBankAccService;
    @DubboReference(check = false)
    private IInputBankInfoService inputBankInfoService;
    @Autowired
    private AbAccrecordsService abAccrecordsService;
    @Autowired
    private AbVoucherinfoService abVoucherinfoService;
    @Autowired
    private AbFcBondHookService abFcBondHookService;
    @Resource(name = "fcBondHookServiceImp")
    private IFcBondHookService IFcBondHookService;
    /**
     * 加载应付票据台账已结清数据
     * @return 返回列表数据信息
     */
    @SuppressWarnings("rawtypes")
    @Transactional(readOnly = true)
    public List<String> billPayDataTable(List<String> origNos) {
        List<String> origNoList = new ArrayList<>();
        DataTablesInput input = new DataTablesInput();
        input.setLength(30000);
        DataTablesOutput<AbBillPayAndReceVO> output = erpAbEmitBillPayDao.billPayDataTable(input,origNos);
        List<AbBillPayAndReceVO> data = output.getData();
        for (AbBillPayAndReceVO vo : data) {
            origNoList.add(vo.getOrigNo());
        }
        return origNoList;
    }


    public Map<String,AbEmitBillPayVo> getBillPayMap(List<String> origNos){
        Map<String,AbEmitBillPayVo> map = new HashMap<>();
        DataTablesInput input = new DataTablesInput();
        input.setLength(30000);
        DataTablesOutput output = this.listAbEmitBillPay(input,origNos);
        List<AbEmitBillPayVo> data = output.getData();
        for (AbEmitBillPayVo vo : data) {
            map.put(vo.getOrigNo(),vo);
        }
        return map;
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = Exception.class)
    public DSLAbEmitBillPayResponseBean handleErpBillPay(DSLAbEmitBillPayResponseBean responseBean,DSLAbEmitBillPayBean billPayBean,
                                                         List<String> origNoList, Map<String, AbEmitBillPayVo> billPayMap, DSLInterfaceLogBill dslLog) throws Exception {

        responseBean.setReceiptNum(billPayBean.getReceiptNum());
        responseBean.setOrgId(billPayBean.getOrgId());
        responseBean.setCompanyCode(billPayBean.getCompanyCode());
        String message = "";
        String status = "1"; // 1接收失败 2接收成功
        UserDTO userDTO = this.userService.getByLoginName("admin");

        if (billPayMap.containsKey(billPayBean.getReceiptNum())) { //在可兑付列表
            String notNullMsg = validateNotNullData(billPayBean);
            if (StringUtils.isBlank(notNullMsg)) {
                String validMsg = validateFieldFormat(billPayBean);
                if (StringUtils.isBlank(validMsg)) {
                    AbEmitBillPayVo billPayVo = billPayMap.get(billPayBean.getReceiptNum());
                    AbAccrecords abAccrecords = abAccrecordsService.get(billPayVo.getId(), true);

                    if (StringUtils.isNotBlank(billPayBean.getCashDepositScale()) || billPayBean.getCashDepositCost() != null) {
                        //更新票据从表
                        AbVoucherinfoVice abVice = abVoucherinfoViceService.get(abAccrecords.getVvid(), true);

                        BigDecimal cashDepositScale = null;
                        if (!StringUtil.isBlank_new(billPayBean.getCashDepositScale())) {
                            cashDepositScale = new BigDecimal(billPayBean.getCashDepositScale().replace("%", ""));
                            cashDepositScale = cashDepositScale.divide(new BigDecimal(100), 6, BigDecimal.ROUND_HALF_UP);
                            abVice.setBailRate(cashDepositScale);
                        }
                        if (billPayBean.getCashDepositCost() != null) {
                            abVice.setBailMoney(billPayBean.getCashDepositCost());
                        }
                        abVoucherinfoViceService.merge(abVice);

                        //判断是否有已挂保证金数据
                        FcBondApplyDTO fcBondApplyDTO = abFcBondHookService.getFcBondApplyByItemIds(abVice.getId(), abVice.getAppNo());
                        int i = 0;
                        if (fcBondApplyDTO != null) {
                            fcBondApplyDTO.setBondPercent(cashDepositScale); //保证金比例
                            fcBondApplyDTO.setApplyMoney(billPayBean.getCashDepositCost()); //保证金比例
                            IFcBondHookService.updateFcBondApply(fcBondApplyDTO);
                        }
                        dslLog.info("FC_BOND_APPLY一共修改" + i + "条数据", true);
                    }

                    // 更新票据主表
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    AbVoucherinfo ab = abVoucherinfoService.get(abAccrecords.getVid(), true);
                    ab.setBillMoney(billPayBean.getReceiptCost());
                    ab.setStartDate(sdf.parse(billPayBean.getCreateReceiptDate()));
                    if (!StringUtil.isBlank_new(billPayBean.getReceiptDueDate())) {
                        ab.setEndDate(sdf.parse(billPayBean.getReceiptDueDate()));
                    }
                    abVoucherinfoService.merge(ab);

                    AbEmitBillPayVo pay = new AbEmitBillPayVo();
                    pay.setDate(DateUtils.str2Date(billPayBean.getPayDate(), DateUtils.date_sdf()));
                    pay.setEmitSttlmmk("0"); //线下兑付
                    pay.setIds(billPayVo.getId());
                    pay.setVids(billPayVo.getVid());
                    pay.setVvids(billPayVo.getVvid());
//                    AbEmitBillPayOnlineVo vo = new AbEmitBillPayOnlineVo();
                    pay.setId(billPayVo.getId());
                    pay.setVid(billPayVo.getVid());
                    pay.setVvid(billPayVo.getVvid());
                    pay.setPayCorpId(billPayVo.getCorpId());
                    pay.setPayPayeeacc(billPayVo.getPayPayeeacc());
                    pay.setPayPayeeName(billPayVo.getPayPayeeName());
                    pay.setPayPayeeBankNo(billPayVo.getPayPayeeBankNo());
                    pay.setPayPayeeBankName(billPayVo.getPayPayeeBankName());
                    pay.setProvName(billPayVo.getProvName());
                    pay.setCityName(billPayVo.getCityName());
                    pay.setPayMoney(ab.getBillMoney());
                    pay.setPayUse(billPayVo.getPayUse());
                    pay.setPayAbs(billPayVo.getPayAbs());

//                    List<AbEmitBillPayOnlineVo> abEmitBillPayOnlineVos = new ArrayList<AbEmitBillPayOnlineVo>();
//                    abEmitBillPayOnlineVos.add(vo);
//                    pay.setAbEmitBillPayOnlineVos(abEmitBillPayOnlineVos);
                    //引用票据兑付接口(线下兑付)
                    abEmitBillPayService.cashingConfirmation(pay, userDTO);
                    status = "2";
                    billPayMap.remove(billPayBean.getReceiptNum());
                    origNoList.add(billPayBean.getReceiptNum());
                } else {
                    message = validMsg;
                }
            } else {
                message = notNullMsg;
            }
        } else { //不在可对付列表
            if (origNoList.contains(billPayBean.getReceiptNum())) {
                message = "该票据已完成解付！";
                status = "2";
            } else {
                message = "此票据不在兑付列表，不可解付！";
            }
        }

        responseBean.setStatus(status);
        responseBean.setMessage(message);
        return responseBean;
    }



    /**
     * @Title: validateNotNullData
     * @Description: 非空校验
     * @param erpPay
     *            void
     */
    private String validateNotNullData(DSLAbEmitBillPayBean erpPay) throws Exception {
        String message = "";
        if (StringUtil.isBlank_new(erpPay.getReceiptNum())) {
            message+="唯一ID不能为空;";
        }

        if (erpPay.getReceiptCost()==null) {
            message+="票面金额(RECEIPT_COST)不能为空;";
        }

        if (StringUtil.isBlank_new(erpPay.getReceiptDueDate())) {
            message+="开票日期(CREATE_RECEIPT_DATE)不能为空;";
        }


        if (StringUtil.isBlank_new(erpPay.getPayDate())) {
            message+="兑付日期(PAY_DATE)不能为空;";
        }

        return message;
    }


    /**
     * @Title: validateFieldFormat
     * @Description: 校验数据格式
     * @param dslAbEmitBillPayBean
     *            void
     */
    private String validateFieldFormat(DSLAbEmitBillPayBean dslAbEmitBillPayBean) {
        String message = "";

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            sdf.parse(dslAbEmitBillPayBean.getCreateReceiptDate());
        } catch (ParseException e) {
            e.printStackTrace();
            message+=String.format("开票日期【%s】格式错误;", dslAbEmitBillPayBean.getCreateReceiptDate());
        }

        if(!StringUtil.isBlank_new(dslAbEmitBillPayBean.getReceiptDueDate())) {
            try {
                sdf.parse(dslAbEmitBillPayBean.getReceiptDueDate());
            } catch (ParseException e) {
                e.printStackTrace();
                message += String.format("到期日期【%s】格式错误;", dslAbEmitBillPayBean.getReceiptDueDate());
            }
        }

        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
        if(!StringUtil.isBlank_new(dslAbEmitBillPayBean.getPayDate())) {
            try {
                sdf1.parse(dslAbEmitBillPayBean.getPayDate());
            } catch (ParseException e) {
                e.printStackTrace();
                message += String.format("兑付日期【%s】格式错误;", dslAbEmitBillPayBean.getPayDate());
            }
        }

        if(!StringUtil.isBlank_new(dslAbEmitBillPayBean.getCashDepositScale())) {
            try {
                new BigDecimal(dslAbEmitBillPayBean.getCashDepositScale().replace("%",""));
            } catch (Exception e) {
                e.printStackTrace();
                message += String.format("保证金比例【%s】格式错误;", dslAbEmitBillPayBean.getCashDepositScale());
            }
        }

        return message;
    }

    /**
     * 加载对付确认数据
     *
     * @param dataTablesInput
     * @return
     */
    @SuppressWarnings("rawtypes")
    @Transactional(readOnly = true)
    public DataTablesOutput listAbEmitBillPay(DataTablesInput dataTablesInput,List<String> origNos) {
        dataTablesInput.getFormdata().put(AccebillConstant.AB_CONFIG_ISEMITMAKE, abVoucherinfoViceService.isEmitMake());
        DataTablesOutput<AbEmitBillPayVo> dataTablesOutput = erpAbEmitBillPayDao.listAbEmitBillPay(dataTablesInput,origNos);
        // 根据兑付申请线上兑付收款人设置情况查询收款人信息
        String isCashingOnlinePayee = abConfigService.getBillConfig(AccebillConstant.AB_CONFIG_ISCASHINGONLINEPAYEE).toFormatString();
        // 银票：默认承兑人
        boolean isBankToAcc = isCashingOnlinePayee.indexOf("1") > -1;
        boolean isBusinessToAcc = isCashingOnlinePayee.indexOf("2") > -1;
        // 商票：默认收票人
        List<AbEmitBillPayVo> data = dataTablesOutput.getData();
        // 账号集合
        Set<String> accSet = new HashSet<>();
        // 联行号集合
        Set<String> bankCodeSet = new HashSet<>();
        if (data != null && !data.isEmpty()) {
            Map<String, Map<String, DictDTO>> listDictMap = dictService.listDictMap("AB_BILL_TYPE", "AB_BILL_FLAG" , "AB_BILL_OPENWAY" , "AB_BILL_AbEmitBillBack_STATUS"
                    , "AB_BILL_AbEmitBillPay_BRIEF_ID", "AB_BILL_ABACCRECORDS_EMITSTTLMMK","AB_BILL_NEW_STM_ISSUOFPPR_IND","AB_BILL_AbEmitBillPay_STATUS");
            for (AbEmitBillPayVo vo : data) {
                // 冗余数据
                injectValues(vo, listDictMap);
                if (StringUtils.isNotEmpty(vo.getPayPayeeacc())) {
                    bankCodeSet.add(vo.getPayPayeeBankNo());
                } else {
                    // 银票银行账号、开户行号保存
                    if (StringUtils.isNotEmpty(vo.getAcceptoracc()) && isBankToAcc && AccebillConstant.AB_BILL_TYPE_BANK.equals(String.valueOf(vo.getBillType()))) {
                        vo.setPayPayeeacc(vo.getAcceptoracc());
                        if ("0".equals(vo.getAcceptoracc())) {
                            bankCodeSet.add(vo.getAcceptorbankno());
                        } else {
                            accSet.add(vo.getAcceptoracc());
                        }
                    }
                    // 商票银行账号、开户行号保存
                    if (StringUtils.isNotEmpty(vo.getPayeeacc()) && isBusinessToAcc && AccebillConstant.AB_BILL_TYPE_MERCH.equals(String.valueOf(vo.getBillType()))) {
                        vo.setPayPayeeacc(vo.getPayeeacc());
                        accSet.add(vo.getPayeeacc());
                        bankCodeSet.add(vo.getPayeebankno());
                    }
                }
            }
        }
        // 根据账号集合获得账号信息
        Map<String, ExternalCorpBankAccDTO> externalCorpBankAccVOMap = new HashMap<>();
        Map<String, BtBankAccDTO> btBankAccDTOMap = new HashMap<>();
        if (StringUtils.isNotEmpty(accSet)) {
            // 根据账号查询客商账户信息
            List<String> acclist = new ArrayList<>(accSet);
            externalCorpBankAccVOMap = externalCorpService.getExternalCorpBankAccByAcc(acclist);
            // 根据账号查询账户信息
            btBankAccDTOMap = iBtBankAccService.getBankAccByInfoAccs(acclist);
            if(CollUtil.isEmpty(btBankAccDTOMap)){
                btBankAccDTOMap = new HashMap<>();
            }
        }
        // 根据联行号集合获得联行号信息
        Map<String, InputBankInfoDTO> bankInfoMap = new HashMap<>();
        if (StringUtils.isNotEmpty(bankCodeSet)) {
            List<String> bankCodelist = new ArrayList<>(bankCodeSet);
            bankInfoMap = inputBankInfoService.getBankCodeInfoByBankCodes(bankCodelist);
        }
        // 添加收款信息
        if (data != null && !data.isEmpty()) {
            for (AbEmitBillPayVo vo : data) {
                // 如果存在审核或打回信息，补充省市信息
                if (StringUtils.isNotBlank(vo.getPayPayeeacc()) && StringUtils.isNotEmpty(bankInfoMap) && (bankInfoMap.containsKey(vo.getPayPayeeBankNo())|| bankInfoMap.containsKey(vo.getPayeebankno())) ) {
                    InputBankInfoDTO bankInfo = new InputBankInfoDTO();
                    if(StringUtils.isNotBlank(vo.getPayPayeeBankNo())){
                        bankInfo = bankInfoMap.get(vo.getPayPayeeBankNo());
                    }else {
                        bankInfo = bankInfoMap.get(vo.getPayeebankno());
                    }

                    vo.setProvName(bankInfo.getProvName());// 所在省
                    vo.setCityName(bankInfo.getCityName());// 所在市
                    vo.setPayPayeeBankNo(bankInfo.getBankCode());//行号
                    vo.setPayPayeeBankName(bankInfo.getBankName());
                    BtBankAccDTO bankAccDTO = btBankAccDTOMap.get(vo.getPayPayeeacc());
                    if (ObjectUtil.isNotEmpty(bankAccDTO)){
                        vo.setPayPayeeName(bankAccDTO.getAccName());
                    }
                } else {
                    // 银票：默认承兑人
                    if (isBankToAcc && AccebillConstant.AB_BILL_TYPE_BANK.equals(String.valueOf(vo.getBillType()))) {
                        // 如果账号为0，则承兑人信息为银行信息，根据联行号获取信息
                        if ("0".equals(vo.getPayPayeeacc()) && StringUtils.isNotEmpty(bankInfoMap) && bankInfoMap.containsKey(vo.getAcceptorbankno())) {
                            InputBankInfoDTO bankInfo = bankInfoMap.get(vo.getAcceptorbankno());
                            vo.setPayPayeeacc(vo.getPayPayeeacc());// 收票人账号
                            vo.setPayPayeeName(bankInfo.getBankName());// 收票人名称
                            vo.setPayPayeeBankNo(bankInfo.getBankCode());// 收票人开户行行号
                            vo.setPayPayeeBankName(bankInfo.getBankName());// 收票人开户行行名
                            vo.setProv(bankInfo.getProv());// 所在省
                            vo.setProvName(bankInfo.getProvName());// 所在省
                            vo.setCity(bankInfo.getCity());// 所在市
                            vo.setCityName(bankInfo.getCityName());// 所在市
                        } else {
                            getBankAcc(vo, externalCorpBankAccVOMap, btBankAccDTOMap);
                        }
                    }
                    // 商票：默认收票人
                    if (isBusinessToAcc && AccebillConstant.AB_BILL_TYPE_MERCH.equals(String.valueOf(vo.getBillType()))) {
                        getBankAcc(vo, externalCorpBankAccVOMap, btBankAccDTOMap);
                    }
                }
            }
        }
        dataTablesOutput.setData(data);
        return dataTablesOutput;
    }

    private void injectValues(AbEmitBillPayVo bill, Map<String, Map<String, DictDTO>> listDictMap) {
        // 票据类型：10 银票、11 商票
        Optional.ofNullable(bill.getBillType()).ifPresent(billType -> {
            Map<String, DictDTO> dictDtoMap = listDictMap.get("AB_BILL_TYPE");
            Optional.ofNullable(dictDtoMap).flatMap(dictMap -> Optional.ofNullable(dictMap.get(billType + "")))
                    .ifPresent(dict -> bill.setBillTypeName(dict.getName()));
        });
        // 流通介质：10 纸票、11 电票
        Optional.ofNullable(bill.getBillFlag()).ifPresent(billFlag -> {
            Map<String, DictDTO> dictDtoMap = listDictMap.get("AB_BILL_FLAG");
            Optional.ofNullable(dictDtoMap).flatMap(dictMap -> Optional.ofNullable(dictMap.get(billFlag + "")))
                    .ifPresent(dict -> bill.setBillFlagName(dict.getName()));
        });
        // 开出方式：0  对外开出；1 对内自开
        Optional.ofNullable(bill.getInsideSing()).ifPresent(insideSing -> {
            Map<String, DictDTO> dictDtoMap = listDictMap.get("AB_BILL_OPENWAY");
            Optional.ofNullable(dictDtoMap).flatMap(dictMap -> Optional.ofNullable(dictMap.get(insideSing + "")))
                    .ifPresent(dict -> bill.setInsideSingName(dict.getName()));
        });
        // 业务处理状态：-2已删除；-1打回；0 暂存；1导入;11-94审批中；95 已完成
        Optional.ofNullable(bill.getStatus()).ifPresent(status -> {
            Map<String, DictDTO> dictDtoMap = listDictMap.get("AB_BILL_AbEmitBillPay_STATUS");
            Optional.ofNullable(dictDtoMap).flatMap(dictMap -> Optional.ofNullable(dictMap.get(status + "")))
                    .ifPresent(dict -> bill.setStatusName(dict.getName()));
        });
        // 业务状态
        Optional.ofNullable(bill.getBriefId()).ifPresent(briefId -> {
            Map<String, DictDTO> dictDtoMap = listDictMap.get("AB_BILL_AbEmitBillPay_BRIEF_ID");
            Optional.ofNullable(dictDtoMap).flatMap(dictMap -> Optional.ofNullable(dictMap.get(briefId)))
                    .ifPresent(dict -> bill.setBriefIdName(dict.getName()));
        });
        // 兑付方式:0或空线下兑付,1线上兑付'
        Optional.ofNullable(bill.getEmitSttlmmk()).ifPresent(emitSttlmmk -> {
            Map<String, DictDTO> dictDtoMap = listDictMap.get("AB_BILL_ABACCRECORDS_EMITSTTLMMK");
            Optional.ofNullable(dictDtoMap).flatMap(dictMap -> Optional.ofNullable(dictMap.get(emitSttlmmk)))
                    .ifPresent(dict -> bill.setEmitSttlmmkName(dict.getName()));
        });
        // 付款状态
        Optional.ofNullable(bill.getPayStatus()).ifPresent(payStatus -> {
            Map<String, DictDTO> dictDtoMap = listDictMap.get("nisBillhead.status");
            Optional.ofNullable(dictDtoMap).flatMap(dictMap -> Optional.ofNullable(dictMap.get(payStatus+"")))
                    .ifPresent(dict -> bill.setPayStatusName(dict.getName()));
        });
        // 交易状态
        Optional.ofNullable(bill.getVoucherStat()).ifPresent(voucherStat -> {
            Map<String, DictDTO> dictDtoMap = listDictMap.get("bisExc.voucherStat");
            Optional.ofNullable(dictDtoMap).flatMap(dictMap -> Optional.ofNullable(dictMap.get(voucherStat)))
                    .ifPresent(dict -> bill.setVoucherStatName(dict.getName()));
        });
        //出票标志: 1 新系统出票 、0 老系统出票
        Optional.ofNullable(bill.getNewStmIssuofpprInd()).ifPresent(newStmIssuofpprInd ->{
            Map<String, DictDTO> dictDtoMap = listDictMap.get("AB_BILL_NEW_STM_ISSUOFPPR_IND");
            Optional.ofNullable(dictDtoMap).flatMap(dictMap -> Optional.ofNullable(dictMap.get(newStmIssuofpprInd)))
                    .ifPresent(dict -> bill.setNewStmIssuofpprIndName(dict.getName()));
        });
    }

    /**
     * 通过账号补全收票人信息
     *
     * @param vo                       兑付信息数据
     * @param externalCorpBankAccVOMap 客商账户信息
     * @param btBankAccDTOMap          银行账户信息
     */
    private void getBankAcc(AbEmitBillPayVo vo, Map<String, ExternalCorpBankAccDTO> externalCorpBankAccVOMap, Map<String, BtBankAccDTO> btBankAccDTOMap) {
        if (StringUtils.isNotEmpty(externalCorpBankAccVOMap) && externalCorpBankAccVOMap.containsKey(vo.getPayPayeeacc())) {
            // 根据账号查询客商账户信息
            ExternalCorpBankAccDTO externalCorpBankAccVO = externalCorpBankAccVOMap.get(vo.getPayPayeeacc());
            if (StringUtils.isNotEmpty(externalCorpBankAccVO)) {
                vo.setPayPayeeacc(externalCorpBankAccVO.getExternalAcc());// 收票人账号
                vo.setPayPayeeName(externalCorpBankAccVO.getExternalAcc());// 收票人名称
                vo.setPayPayeeBankNo(externalCorpBankAccVO.getBankSourceCode());// 收票人开户行行号
                vo.setPayPayeeBankName(externalCorpBankAccVO.getBankTypeName());// 收票人开户行行名
                vo.setProv(externalCorpBankAccVO.getAccProvince());// 所在省
                vo.setProvName(externalCorpBankAccVO.getAccProvince());// 所在省
                vo.setCity(externalCorpBankAccVO.getAccCity());// 所在市
                vo.setCityName(externalCorpBankAccVO.getAccCity());// 所在市
            }
        }
        if (StringUtils.isNotEmpty(btBankAccDTOMap) && btBankAccDTOMap.containsKey(vo.getPayPayeeacc())) {
            // 根据账号查询账户信息
            BtBankAccDTO btBankAccDTO = btBankAccDTOMap.get(vo.getPayPayeeacc());
            if (StringUtils.isNotEmpty(btBankAccDTO)) {
                vo.setPayPayeeacc(btBankAccDTO.getBankAcc());// 收票人账号
                vo.setPayPayeeName(btBankAccDTO.getAccName());// 收票人名称
                vo.setPayPayeeBankNo(btBankAccDTO.getBankCode());// 收票人开户行行号
                vo.setPayPayeeBankName(btBankAccDTO.getBankName());// 收票人开户行行名
                vo.setProv(btBankAccDTO.getProv());// 所在省
                vo.setProvName(btBankAccDTO.getProvName());// 所在省
                vo.setCity(btBankAccDTO.getCity());// 所在市
                vo.setCityName(btBankAccDTO.getCityName());// 所在市
            }
        }
    }

}
