//package com.bytter.biz.inneraccount.service;
//
//import java.util.Date;
//import java.util.List;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//import com.bytter.core.base.BaseDao;
//import com.bytter.core.base.BaseService;
//import com.bytter.core.common.datatable.DataTablesInput;
//import com.bytter.core.common.datatable.DataTablesOutput;
//import com.bytter.core.constant.CommonConstant;
//import com.bytter.core.utils.DateUtils;
//import com.bytter.core.utils.EmptyUtils;
//import com.bytter.system.api.IAccountGrantService;
//import com.bytter.system.api.IBisAccDtlService;
//import com.bytter.system.api.IConfigService;
//import com.bytter.system.api.INotificationService;
//import com.bytter.system.api.ITaskService;
//import com.bytter.system.api.IUserService;
//import com.bytter.system.api.dto.BisAccDtlDTO;
//import com.bytter.system.api.dto.VNisPayBillDTO;
//import com.bytter.system.constants.AllocationConstant;
//import com.bytter.system.constants.BtBankAccConstant;
//import com.bytter.biz.inneraccount.dao.PaymentBlendingDao;
//import com.bytter.biz.inneraccount.entity.BisAccDtl;
//import com.bytter.system.vo.BisPaymentBlendingVO;
//import com.bytter.system.vo.VNisPayBillheadVO;
//TODO 放到资金核对
//@Service("BisPaymentBlendingService")
//@Scope(proxyMode = ScopedProxyMode.TARGET_CLASS)
//@Transactional
//public class BisPaymentBlendingService extends BaseService<BisAccDtl, String> implements ITaskService {
//    @Autowired
//    private PaymentBlendingDao paymentBlendingDao;
//    @Autowired
//    private IAccountGrantService accountGrantService;
//    private INisBillheadService nisBillheadService;
//    @Autowired
//    private INotificationService notificationService;
//    @Autowired
//    private IBisAccDtlService bisAccDtlService;
//    @Autowired
//    private IUserService userService;
//    @Autowired
//    private PaymentBlendingService paymentBlendingService;
//    @Autowired
//    private IConfigService configService;
//
//    @Override
//    public BaseDao<BisAccDtl, String> getEntityDao() {
//        return paymentBlendingDao;
//    }
//
//
//    /**
//     * 定时器调用自动付款勾兑
//     */
//    @Override
//@XxlJob("BisPaymentBlendingService")
//    public void run(String arguments) {
//        try {
//            Date curDate = DateUtils.getDate();
//            this.logger.info(curDate + "---启用付款明细勾对，开始执行定时任务---");
//            DataTablesInput input1 = new DataTablesInput();
//            //一次只查300条
//            input1.setLength(300);
//            //手动调拨单是否要与付款明细勾对
//            String allocationAllowBlending = configService.getConfig(AllocationConstant.ALLOT_CONFIG_TYPE, AllocationConstant.ALLOT_CONFIG_ALLOCATIONALLOWBLENDING).getValue();
//            String table = "v_nis_pay_bill";
//            if("1".equals(allocationAllowBlending)){
//                table = "v_nis_pay_bill_allocation";
//            }
//            DataTablesOutput out = this.paymentBlendingDao.getDtlRules(input1);
//            List<BisPaymentBlendingVO> list = out.getData();
//            for (BisPaymentBlendingVO bisPaymentBlendingVO : list) {
//                //更新是否自动执行标志
//                paymentBlendingDao.toUpdateAutoSign(bisPaymentBlendingVO.getId(),BtBankAccConstant.BLENDSIGN_1);
//                if (bisPaymentBlendingVO.getRules() != null) {
//                    DataTablesInput input = new DataTablesInput();
//                    DataTablesInput bisinput = new DataTablesInput();
//                    if (bisPaymentBlendingVO.getIsExistId().equals(CommonConstant.VALID_SIGN_NO)) {  //不存在唯一标识
//                        String[] bankAccTypeStrs = bisPaymentBlendingVO.getRules().split(CommonConstant.COMMA_SPLIT);
//                        String bisPaymenRules = "";
//                        for (String s : bankAccTypeStrs) {
//                            bisPaymenRules = bisPaymenRules + "," + s;
//                        }
//                        if (bisPaymenRules.contains("amt")) {
//                            bisinput.getFormdata().put("amt", bisPaymentBlendingVO.getAmt());
//                            input.getFormdata().put("eqg_payMoney", bisPaymentBlendingVO.getAmt());
//                        }
//                        if (bisPaymenRules.contains("bankAcc")) {
//                            bisinput.getFormdata().put("bankAcc", bisPaymentBlendingVO.getBankAcc());
//                            input.getFormdata().put("eqs_payerAcc", bisPaymentBlendingVO.getBankAcc());
//                        }
//                        if (bisPaymenRules.contains("transTime")) {
//                            Date date = bisPaymentBlendingVO.getTransTime();
//                            String dateStr = DateUtils.formatDate(date, "yyyy-MM-dd");
//                            bisinput.getFormdata().put("transTime", dateStr);
//                            input.getFormdata().put("wishPayday", dateStr);
//                        }
//                        if (bisPaymenRules.contains("corpId")) {
//                            bisinput.getFormdata().put("corpId", bisPaymentBlendingVO.getCorpId());
//                            input.getFormdata().put("eqs_corpId", bisPaymentBlendingVO.getCorpId());
//                        }
//                        if (bisPaymenRules.contains("accName")) {
//                            bisinput.getFormdata().put("accName", bisPaymentBlendingVO.getAccName());
//                            input.getFormdata().put("eqs_payerAccName", bisPaymentBlendingVO.getAccName());
//                        }
//                        //如果流水某些字段为空，则不匹配
//                            if(bisPaymentBlendingVO.getCdSign().equals(CommonConstant.YES)){   //收支属性为付款
//                                bisinput.getFormdata().put("cdSign", bisPaymentBlendingVO.getCdSign());
//                            }else {
//                                continue;
//                            }
//
//                        if (bisPaymenRules.contains("curId")) {
//                            if (EmptyUtils.isEmpty(bisPaymentBlendingVO.getCurId())) {
//                                continue;
//                            } else {
//                                bisinput.getFormdata().put("curId", bisPaymentBlendingVO.getCurId());
//                                input.getFormdata().put("eqs_curId", bisPaymentBlendingVO.getCurId());
//                            }
//                        }
//                        if (bisPaymenRules.contains("oppAccNo")) {
//                            if (EmptyUtils.isEmpty(bisPaymentBlendingVO.getOppAccNo())) {
//                                continue;
//                            } else {
//                                bisinput.getFormdata().put("oppAccNo", bisPaymentBlendingVO.getOppAccNo());
//                                input.getFormdata().put("eqs_payeeAcc", bisPaymentBlendingVO.getOppAccNo());
//                            }
//                        }
//                        if (bisPaymenRules.contains("uses")) {
//                            if (EmptyUtils.isEmpty(bisPaymentBlendingVO.getUses())) {
//                                continue;
//                            } else {
//                                bisinput.getFormdata().put("uses", bisPaymentBlendingVO.getUses());
//                                input.getFormdata().put("eqs_purpose", bisPaymentBlendingVO.getUses());
//                            }
//                        }
//                        if (bisPaymenRules.contains("oppAccName")) {
//                            if (EmptyUtils.isEmpty(bisPaymentBlendingVO.getOppAccName())) {
//                                continue;
//                            } else {
//                                bisinput.getFormdata().put("oppAccName", bisPaymentBlendingVO.getOppAccName());
//                                input.getFormdata().put("eqs_payeeAccName", bisPaymentBlendingVO.getOppAccName());
//                            }
//                        }
//                        DataTablesOutput output = paymentBlendingDao.getDataTablesUnionForDtl(input,table);
//                        boolean flag = paymentBlendingDao.getBisAccDtlDataTable(bisinput);
//                        //只能一对一勾兑
//                        if (output.getData().size() == 1 && flag) {
//                            List<VNisPayBillheadVO> li = output.getData();
//                            //如果付款单字段为空，则不匹配
//                            if (bisPaymenRules.contains("curId") && EmptyUtils.isEmpty(li.get(0).getCurId())) {
//                                continue;
//                            }
//                            if (bisPaymenRules.contains("oppAccName") && EmptyUtils.isEmpty(li.get(0).getPayeeAccName())) {
//                                continue;
//                            }
//                            if (bisPaymenRules.contains("uses") && EmptyUtils.isEmpty(li.get(0).getPurpose())) {
//                                continue;
//                            }
//                            if (bisPaymenRules.contains("oppAccNo") && EmptyUtils.isEmpty(li.get(0).getPayeeAcc())) {
//                                continue;
//                            }
//                            this.toUpdateBlendSign(bisPaymentBlendingVO.getId(), BtBankAccConstant.BLENDSIGN_1);
//                            this.toUpdateBisExc(li.get(0).getId(), bisPaymentBlendingVO.getId(), li.get(0).getIsFrom(), bisPaymentBlendingVO.getId());
//                        }
//                    }
//                }
//                if (bisPaymentBlendingVO.getIsExistId().equals(CommonConstant.VALID_SIGN_YES)) {   //存在唯一标识
//                    String billOnlyId = bisPaymentBlendingVO.getBillOnlyId();   //付款单
//                    String dtlOnlyId = bisPaymentBlendingVO.getDtlOnlyId();   //流水
//                    String dtlOnlyValue = "";
//                    if (dtlOnlyId.equals("BANK_SERIAL_NO")) {
//                        if (EmptyUtils.isEmpty(bisPaymentBlendingVO.getBankSerialNo())) {
//                            continue;
//                        } else {
//                            dtlOnlyValue = bisPaymentBlendingVO.getBankSerialNo();
//                        }
//                    }
//                    if (dtlOnlyId.equals("ACC_NO")) {
//                        if (EmptyUtils.isEmpty(bisPaymentBlendingVO.getAccNo())) {
//                            continue;
//                        } else {
//                            dtlOnlyValue = bisPaymentBlendingVO.getAccNo();
//                        }
//                    }
//                    //只能一对一勾兑
//                    if (dtlOnlyValue != null) {
//                        List<VNisPayBillDTO> li = paymentBlendingDao.getBillhead(billOnlyId, dtlOnlyValue,table);
//                        List<BisAccDtlDTO> bisAcc = paymentBlendingDao.getBisacc(billOnlyId, dtlOnlyValue);
//                        if (li.size() == 1 && bisAcc.size() == 1) {
//                            //如果付款单特定字段为空，则不匹配
//                            if (dtlOnlyId.equals("BANK_SERIAL_NO") && EmptyUtils.isEmpty(li.get(0).getBankSerialNo())) {
//                                continue;
//                            }
//                            if (dtlOnlyId.equals("ACC_NO") && EmptyUtils.isEmpty(li.get(0).getBillNo())) {
//                                continue;
//                            }
//                            this.toUpdateBlendSign(bisPaymentBlendingVO.getId(), BtBankAccConstant.BLENDSIGN_1);
//                            this.toUpdateBisExc(li.get(0).getId(), bisPaymentBlendingVO.getId(), li.get(0).getIsFrom(), bisPaymentBlendingVO.getId());
//                        }
//                    }
//
//                }
//            }
//            this.logger.info("---启用付款明细勾对，结束执行定时任务---");
//        } catch (Exception e) {
//
//            e.printStackTrace();
//            throw new RuntimeException(e);
//        }
//    }
//
//    /**
//     * 更新流水勾兑状态
//     *
//     * @param id
//     * @param blendSign
//     * @return
//     */
//    public int toUpdateBlendSign(String id, String blendSign) {
//        return paymentBlendingDao.toUpdateBlendSign(id, blendSign);
//    }
//
//    /**
//     * 更新流水的流水号
//     *
//     * @param id
//     * @param bankSerialNo
//     * @return
//     */
//    public int toUpdateBankSerialNo(String id, String bankSerialNo) {
//        return paymentBlendingDao.toUpdateBankSerialNo(id, bankSerialNo);
//    }
//
//    /**
//     * 更新指令与付款单的流水号
//     *
//     * @param id
//     * @param bankSerialNo
//     * @return
//     */
//    public void toUpdateBisExc(String id, String bankSerialNo, String isFrom, String dtlId) {
//        //多笔明细可以勾兑一笔付款单，插入中间表
//        this.paymentBlendingDao.insetBillBankSerialNo(id, isFrom, bankSerialNo, dtlId);
//    }
//}
