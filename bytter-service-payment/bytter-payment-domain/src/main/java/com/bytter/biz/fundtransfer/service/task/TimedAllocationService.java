package com.bytter.biz.fundtransfer.service.task;

import com.bytter.biz.allocation.vo.BisAccGatherPolicyVO;
import com.bytter.biz.bdlinks.entity.BisExc;
import com.bytter.biz.bdlinks.service.BisExcService;
import com.bytter.biz.fundtransfer.bean.BisAccGatherPolicySearchBean;
import com.bytter.biz.fundtransfer.dao.FundTransferDao;
import com.bytter.biz.fundtransfer.enums.BankConstants;
import com.bytter.biz.fundtransfer.enums.GatherModeEnum;
import com.bytter.biz.fundtransfer.enums.VoucherStatEnum;
import com.bytter.biz.fundtransfer.service.FundTransferBase;
import com.bytter.biz.fundtransfer.util.Arith;
import com.bytter.biz.fundtransfer.util.DateUtil;
import com.bytter.biz.fundtransfer.util.TransferUtil;
import com.bytter.biz.inneraccount.dao.BisGatherPolicyDao;
import com.bytter.biz.inneraccount.entity.BisAccGatherPolicy;
import com.bytter.biz.inneraccount.entity.BisGatherPolicy;
import com.bytter.biz.payment.api.constant.AllocationConstant;
import com.bytter.biz.payment.api.constant.BdlinkConstant;
import com.bytter.biz.payment.api.dto.BtBankAccDTO;
import com.bytter.core.base.BaseDao;
import com.bytter.core.common.context.UserContextHelper;
import com.bytter.core.sequence.SequenceSingleton;
import com.bytter.core.sequence.enums.SequenceEnum;
import com.bytter.core.utils.DateUtils;
import com.bytter.system.api.ITaskService;
import com.bytter.system.api.dto.BisAccBalDTO;
import com.xxl.job.core.handler.annotation.XxlJob;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.util.*;

/**
 * 资金调拨-定时下拨定时任务实现
 *
 * @author fangcf
 * @date 2026-04-13
 */
@Service
@Transactional
public class TimedAllocationService extends FundTransferBase implements ITaskService {

    @Autowired
    private FundTransferDao fundTransferDao;
    @Autowired
    private BisGatherPolicyDao bisGatherPolicyDao;
    @Autowired
    private BisExcService bisExcService;

    @Override
    public BaseDao<BisAccGatherPolicy, String> getEntityDao() {
        return null;
    }

    @Override
    @XxlJob("TimedAllocationService")
    public void run(String arguments) {
        //初始化查询bean
        BisAccGatherPolicySearchBean bagpSearchBean = new BisAccGatherPolicySearchBean();
        // gather_type: 0 定时归集 1  实时归集 2 定时下拨
        bagpSearchBean.setGatherType(new String[]{AllocationConstant.GATHERTYPE_2});
        bagpSearchBean.setGatherMode(new String[]{
                GatherModeEnum.APPROPRIATE_CONSTANT.value,
                GatherModeEnum.APPROPRIATE_MARGIN.value,
                GatherModeEnum.APPROPRIATE_SUPPLY.value,
                GatherModeEnum.APPROPRIATE_LACK_OF_BALANCE.value,
                GatherModeEnum.APPROPRIATE_PERCENT.value,
                GatherModeEnum.APPROPRIATE_TO_GATHER.value
        });
        //当前时间
        Date date = new Date();
        bagpSearchBean.setNextTime(date);
        //当前租户
        String tenantId = UserContextHelper.helper().getTenantId();
        this.logger.info("开始执行资金调拨-定时下拨任务，租户：{}，时间：{}", tenantId, DateUtils.format(date, DateUtils.PATTERN_CLASSICAL));
        //获取全量下拨数据 bis_acc_gather_policy
        List<BisAccGatherPolicyVO> bisAccGatherPolicy = fundTransferDao.getBisAccGatherPolicyList(bagpSearchBean);
        if (bisAccGatherPolicy.isEmpty()) {
            this.logger.info("---当前租户：{}，没有下拨任务需要执行---", tenantId);
            return;
        }
        //执行资金下拨
        this.genFundAllocation(bisAccGatherPolicy);
    }

    /**
     * 资金下拨
     *
     * @param bisAccGatherPolicy 策略
     * @return void
     */
    private void genFundAllocation(List<BisAccGatherPolicyVO> bisAccGatherPolicy) {
        this.logger.info("资金调拨-定时下拨任务，定时扫描服务执行共：" + bisAccGatherPolicy.size() + "条下拨信息");
        //一次性提取所有相关账号
        Map<String, BtBankAccDTO> bankAccInfoMap = super.getAllBankAccInfo(bisAccGatherPolicy);
        //当前时间是否是工作日
        boolean isWorkSign = fundTransferDao.isWorkDaySign(DateUtil.getFormattingDateTime(new Date(), 7));
        for (BisAccGatherPolicyVO accGatherPolicy : bisAccGatherPolicy) {
            //获取策略对象
            BisGatherPolicy bisGatherPolicy = bisGatherPolicyDao.getUniqueBy("id", accGatherPolicy.getGatherPolicyId());
            accGatherPolicy.setBisGatherPolicy(bisGatherPolicy);
            //计算下次下拨时间(未到执行时间或当前是节假日且策略是不执行时返回空，本条策略账号不执行)
            Date expectedNextTime = tryGetNextRunTime(accGatherPolicy);
            if (null == expectedNextTime) {
                //当前时间不是本条策略的运行时间，跳过本次执行
                continue;
            }
            accGatherPolicy.setExpectedNextTime(expectedNextTime);
            logger.info("开始处理下拨策略：" + accGatherPolicy.getBisGatherPolicy().getGatherPolicyCode() + "，租户：" + accGatherPolicy.getTenantId() + "，子账号为：" + accGatherPolicy.getBankAcc() + " 父账号为：" + accGatherPolicy.getParentBankAcc());
            try {
                //进行节假日规则校验判断是否执行
                if (checkFilterHolidays(accGatherPolicy, isWorkSign)) {
                    //生成付款指令和更新下次下拨执行时间
                    genBisExc(accGatherPolicy, bankAccInfoMap);
                } else {
                    continue;
                }
            } catch (Exception e) {
                //如果失败策略是'执行一次'则更新下次执行时间
                if ("1".equals(accGatherPolicy.getBisGatherPolicy().getFailHandleMethod())) {
                    fundTransferDao.updateBisAccGatherPolicyNextTime(accGatherPolicy);
                }
                String exceptionMessage = e.getMessage();
                logger.error("租户：" + accGatherPolicy.getTenantId()
                        + "，子账号：" + accGatherPolicy.getBankAcc()
                        + "，父账号：" + accGatherPolicy.getParentBankAcc()
                        + "，策略：" + accGatherPolicy.getBisGatherPolicy().getGatherPolicyCode()
                        + "，下拨指令生成异常:" + exceptionMessage
                );
                continue;
            }
        }
        return;
    }


    /**
     * 生成付款指令和更新下次下拨执行时间
     *
     * @param accGatherPolicy 账号策略
     * @param bankAccInfoMap  银行账号信息key账号，value银行账号对象
     * @throws Exception
     */
    private void genBisExc(BisAccGatherPolicyVO accGatherPolicy, Map<String, BtBankAccDTO> bankAccInfoMap) throws Exception {
        //校验父子账号信息与余额
        BtBankAccDTO payerBankAccInfo = bankAccInfoMap.get(accGatherPolicy.getParentBankAcc() + accGatherPolicy.getCurId());
        if (payerBankAccInfo == null) {
            throw new Exception("租户：" + accGatherPolicy.getTenantId() + "，付款父账号：" + accGatherPolicy.getParentBankAcc() + "信息未维护。");
        }
        BtBankAccDTO payeeBankAccInfo = bankAccInfoMap.get(accGatherPolicy.getBankAcc() + accGatherPolicy.getCurId());
        if (payeeBankAccInfo == null) {
            throw new Exception("租户：" + accGatherPolicy.getTenantId() + "，收款子账号：" + accGatherPolicy.getBankAcc() + "信息未维护。");
        }
        List<BisAccBalDTO> parentAccBalList = fundTransferDao.listByUnion(accGatherPolicy.getParentBankAcc(), accGatherPolicy.getCurId(), DateUtils.getDate());
        if (CollectionUtils.isEmpty(parentAccBalList)) {
            throw new Exception("没有从数据库中查到租户：" + accGatherPolicy.getTenantId() + "，付款父账号：" + accGatherPolicy.getParentBankAcc() + "的余额值。");
        }
        if (parentAccBalList.size() > 1) {
            throw new Exception("数据库中查到租户：" + accGatherPolicy.getTenantId() + "，付款父账号：" + accGatherPolicy.getParentBankAcc() + "余额数据重复，重复日期为：" + DateUtils.formatDate(DateUtils.getDate()));
        }
        BisAccBalDTO parentAccBal = parentAccBalList.stream().findFirst().get();

        List<BisAccBalDTO> accBalList = fundTransferDao.listByUnion(accGatherPolicy.getBankAcc(), accGatherPolicy.getCurId(), DateUtils.getDate());
        if (CollectionUtils.isEmpty(accBalList)) {
            throw new Exception("没有从数据库中查到租户：" + accGatherPolicy.getTenantId() + "，收款子账号" + accGatherPolicy.getBankAcc() + "的余额值。");
        }
        if (accBalList.size() > 1) {
            throw new Exception("数据库中查到租户：" + accGatherPolicy.getTenantId() + "，收款子账号：" + accGatherPolicy.getBankAcc() + "余额数据重复，重复日期为：" + DateUtils.formatDate(DateUtils.getDate()));
        }
        BisAccBalDTO accBal = accBalList.stream().findFirst().get();

        //计算并校验下拨的金额
        BigDecimal amount = getAllocationAmount(accGatherPolicy, accBal, parentAccBal);
        if (amount == null || amount.doubleValue() <= 0.0d) {
            throw new Exception("下拨金额无效（下拨金额为空或为负数），无法下拨。");
        }

        BisExc be = new BisExc();
        //付款单ID
        be.setCbsBillId("");
        //单位ID
        be.setCorpId(payerBankAccInfo.getCorpId());
        //申请日期
        be.setReqDate(DateUtils.getDate());
        //付款账号
        be.setPayerAcc(payerBankAccInfo.getBankAcc());
        //付款人账户名
        be.setPayerAccName(payerBankAccInfo.getAccName());
        //付款人开户行名称
        be.setPayerBank(payerBankAccInfo.getBankName());
        //付款人省份
        be.setPayerProv(payerBankAccInfo.getProv());
        //付款人城市
        be.setPayerCity(payerBankAccInfo.getCity());
        //币种
        be.setCurId(accGatherPolicy.getCurId());
        //下拨金额
        be.setPayMoney(amount);
        //付款方式
        be.setVoucherTypeId(fundTransferDao.getSysVoucherCode("10", payerBankAccInfo.getTenantId()));
        //收款人账号
        be.setPayeeAcc(payeeBankAccInfo.getBankAcc());
        //收款人名称payeeBankAccInfo = {BtBankAccDTO@33069}
        be.setPayeeAccName(payeeBankAccInfo.getAccName());
        //收款人开户行名称
        be.setPayeeBank(payeeBankAccInfo.getBankName());
        //收款人省份
        be.setPayeeProv(payeeBankAccInfo.getProv());
        //收款人城市
        be.setPayeeCity(payeeBankAccInfo.getCity());
        //收款人联行号
        be.setPayeeBankCode(payeeBankAccInfo.getBankCode());
        //加急标志
        be.setIsQuick(accGatherPolicy.getBisGatherPolicy().getIsQuick());
        //交易模式
        be.setTransMode("3");
        //接口代码
        be.setBifCode(payerBankAccInfo.getBifCode());
        //跨行标志
        be.setDifBank(payerBankAccInfo.getBifCode().equals(payeeBankAccInfo.getBifCode()) ? BankConstants.SAME_BANK : BankConstants.DIF_BANK);
        //同城异地标志
        be.setAreaSign(payerBankAccInfo.getCity().endsWith(payeeBankAccInfo.getCity()) ? BankConstants.SAME_AREA : BankConstants.DIF_AREA);
        GatherModeEnum gatherMode = GatherModeEnum.toEnum(accGatherPolicy.getBisGatherPolicy().getGatherMode()); // impossible null
        //摘要
        if (accGatherPolicy.getBisGatherPolicy().getPurpose() != null) {
            be.setPurpose(accGatherPolicy.getBisGatherPolicy().getPurpose());
        } else {
            be.setPurpose(gatherMode.description);
        }
        //附言
        if (accGatherPolicy.getBisGatherPolicy().getAddWord() != null) {
            be.setAddWord(accGatherPolicy.getBisGatherPolicy().getAddWord());
        } else {
            be.setAddWord(gatherMode.description);
        }
        //策略ID
        be.setPolicyId(accGatherPolicy.getGatherPolicyId());
        //付款来源
        be.setVoucherFrom(BdlinkConstant.FROM_ALLOCATION_TYPE_AUTO_DOWN);
        //业务类型BUS_ORI_TYPE
        be.setBusOriType(new Integer("33"));
        //指令类型CMD_TYPE
        be.setCmdType(BdlinkConstant.CMDTYPE_AUTO);
        // 其它信息
        be.setCounterAcc(payerBankAccInfo.getBankAcc());// 中心账号－记帐
        //准备发送状态3
        be.setVoucherStat(VoucherStatEnum.READY_SENDING.value);
        //指令编号
        be.setVoucherNo(SequenceSingleton.getSequence(SequenceEnum.BIS_EXC));
        //付款指令新增
        bisExcService.save(be);
        //更新下次执行时间
        fundTransferDao.updateBisAccGatherPolicyNextTime(accGatherPolicy);

        logger.info("下拨指令插入成功，指令编号：" + be.getVoucherNo() + "，租户：" + payerBankAccInfo.getTenantId() + "，付款账号：" + payerBankAccInfo.getBankAcc() + "，收款账号：" + payeeBankAccInfo.getBankAcc() + "，金额：" + amount);
    }


    /**
     * 计算并校验调拨的金额
     *
     * @param accGatherPolicy 账号调拨策略
     * @param accBal          账号对应余额信息
     * @return BigDecimal计算出来的调拨金额
     * @throws Exception
     */
    protected BigDecimal getAllocationAmount(BisAccGatherPolicyVO accGatherPolicy, BisAccBalDTO accBal, BisAccBalDTO parentAccBal) throws Exception {
        BisGatherPolicy gatherPolicy = accGatherPolicy.getBisGatherPolicy();
        boolean isParentCur = DateUtil.getFormattingDateTime(new Date(), 0).equals(DateUtil.getFormattingDateTime(parentAccBal.getGetDate(), 0));
        if (!isParentCur) {
            throw new Exception("付款父账号：" + accGatherPolicy.getParentBankAcc() + "在数据库中的余额不为当天余额。");
        }
        boolean isCur = DateUtil.getFormattingDateTime(new Date(), 0).equals(DateUtil.getFormattingDateTime(accBal.getGetDate(), 0));
        if (!isCur) {
            throw new Exception("收款子账号：" + accGatherPolicy.getBankAcc() + "在数据库中的余额不为当天余额。");
        }
        double amount = 0.0d;
        GatherModeEnum gatherMode = GatherModeEnum.toEnum(gatherPolicy.getGatherMode());
        // 当前父账号可用余额
        double parentAccAvailBal = parentAccBal.getAvailBal().doubleValue();
        // 当前子账号可用余额
        double availBal = accBal.getAvailBal().doubleValue();
        switch (gatherMode) {
            case APPROPRIATE_CONSTANT:
                //调拨策略3
                //获取固定额度
                amount = gatherPolicy.getDownFixedAmt().doubleValue();
                if (parentAccAvailBal < amount) {
                    throw new Exception("父账号:" + accGatherPolicy.getParentBankAcc() + " 余额不足,无法下拨!,父账号余额为:" + new BigDecimal(parentAccAvailBal) + ",需下拨余额为:" + new BigDecimal(amount));
                }
                if (amount <= 0.0) {
                    throw new Exception("无效策略金额设置：GatherAmt:" + amount);
                }
                break;
            case APPROPRIATE_MARGIN://子账号余额最少保持一定额度，不够从父账号下拨差额（不够部分）。
                //调拨策略4
                //获取子账户差额
                amount = Arith.sub(gatherPolicy.getGatherAmt().doubleValue(), availBal);// 高精度运算
                if (amount <= 0.0) {
                    throw new Exception("余额已足，不用下拨。");
                }
                if (parentAccAvailBal < amount) {
                    throw new Exception("父账号:" + accGatherPolicy.getParentBankAcc() + " 余额不足,无法下拨!,父账号余额为:" + new BigDecimal(parentAccAvailBal) + ",需下拨余额为:" + new BigDecimal(amount));
                }
                break;
            case APPROPRIATE_SUPPLY://子账号余额最少保持一定额度，不够从父账号下拨差额（不够部分），并且差额必须大于设定最小金额。
                //调拨策略7
                //获取子账户差额
                amount = Arith.sub(gatherPolicy.getGatherAmt().doubleValue(), availBal);// 高精度运算
                if (amount <= 0.0) {
                    throw new Exception("余额已足，不用下拨。");
                }
                if (parentAccAvailBal < amount) {
                    throw new Exception("父账号:" + accGatherPolicy.getParentBankAcc() + " 余额不足,无法下拨!,父账号余额为:" + new BigDecimal(parentAccAvailBal) + ",需下拨余额为:" + new BigDecimal(amount));
                }
                if (amount < gatherPolicy.getMinAmt().doubleValue()) {
                    throw new Exception("下拨金额小于最小差额比例金额，不能下拨。");
                }
                break;
            case APPROPRIATE_LACK_OF_BALANCE://余额不足时固定额度下拨
                //获取账户固定余额
                amount = gatherPolicy.getGatherAmt().doubleValue();
                //获取下拔固定余额
                double appAmt = gatherPolicy.getDownFixedAmt().doubleValue();
                //获取账户可用余额   如果账户没有可用余额,就直接取相应的账户余额
                double bal = 0.0;
                if (availBal > 0) {
                    bal = availBal;
                } else {
                    bal = accBal.getBal().doubleValue();
                }
                //下拨时，查找设置了策略的子账号的可用余额，如可用余额<账户固定余额的值时，按“下拨固定额度”的值下拨
                if (bal <= amount) {
                    amount = appAmt;
                } else {
                    throw new Exception("可用余额大于下拔固定余额无需生成下拔指令,可用余额为:" + new BigDecimal(bal) + ",下拔固定余额为:" + new BigDecimal(appAmt));
                }
                if (parentAccAvailBal < amount) {
                    throw new Exception("父账号:" + accGatherPolicy.getParentBankAcc() + " 余额不足,无法下拨!,父账号余额为:" + new BigDecimal(parentAccAvailBal) + ",需下拨余额为:" + new BigDecimal(amount));
                }
                break;
            case APPROPRIATE_PERCENT://比例下拨
                amount = availBal * gatherPolicy.getDifferScale().doubleValue();
                if (amount <= 0) {
                    throw new Exception("计算后的下拨金额为零!不能下拨。");
                }
                if (parentAccAvailBal < amount) {
                    throw new Exception("父账号:" + accGatherPolicy.getParentBankAcc() + " 余额不足,无法下拨!,父账号余额为:" + new BigDecimal(parentAccAvailBal) + ",需下拨余额为:" + new BigDecimal(amount));
                }
                if (Double.compare(availBal, amount) < 0) {
                    throw new Exception("当前余额不足，不能下拨。");
                }
                break;
            //TODO:根据前一日归集金额下拨
//		case APPROPRIATE_TO_GATHER://根据前一日归集金额下拨
//			//将昨天的归集上去的金额下拨   带需求确认
//
//			//amount = gatherPolicy.getGatherAmt()-availBal;
//			if (Double.compare(amount, 0.0) > 0){
//				throw new BpBizException("可用余额不足，不能下拨。需要下拨金额为:" + decimalFormat.format(gatherPolicy.getGatherAmt()) + ",可用金额为:" + decimalFormat.format(availBal));
//			}
            //amount = gatherPolicy.getGatherAmt();
//		    break;
            default:
                throw new Exception("无效的归集类型！" + gatherPolicy.getGatherMode());
        }
        //TODO:联机余额时需要判断联机余额
//        if (!BpParamHelper.getBooleanParamValue(BpParamConstants.PARAM_KEY_IS_VERIFY_ONLINE_BAL)) { //非联机余额
//            return new BigDecimal(amount);
//        }
//        String bankAcc = accGatherPolicy.getBankAcc();
//        // 获取下拨内部账号联机分户信息
//        FcOnlineLedgerInfo oli = locateFcOnlineLedgerInfo(bankAcc,null);
//        if (oli == null) {// 存在内部账号就一定存在联机分户信息？
//            throw new Exception("获取不到内部账号或内部账号对应的联机分户信息,对应的银行账号为：" + bankAcc);
//        }
//        double innerBal = oli.getCheckBalance().doubleValue();
//        double fc_online_ledger = oli.getFreezeMoney(); // 内部账号的冻结金额
//        double onlineBal = Arith.sub(innerBal, fc_online_ledger); // 内部余额-内部账号的冻结金额 =
//        // 内部账号的可用余额
//        if ("N".equals(oli.getOverdraftSign())) { // 不允许透支
//            if (onlineBal < amount) {//子账号对应内部账号可用余额必须大于下拨金额
//                throw new Exception("联机余额不足，不能下拨,账号为：" + bankAcc + "，内部账号为:" + oli.getBlnctrAcc() + ",透支标示为:" + oli.getOverdraftSign() + ",内部余额:" + innerBal
//                        + ",冻结金额:" + fc_online_ledger + ",下拨金额:" + amount);
//            }
//        } else if ("D".equals(oli.getOverdraftSign())) { // 不受限透支
//            return new BigDecimal(amount);
//        } else if ("R".equals(oli.getOverdraftSign())) { // 受限透支
//            double overdraft_money = oli.getOverdraftMoney().doubleValue();
//            if (Arith.add(overdraft_money, onlineBal) < amount) {
//                throw new Exception("下拨金额为:" + amount + ",联机余额为:" + onlineBal + "透支金额为:" + overdraft_money
//                        + "联机余额加透支余额小于下拨金额，不能下拨。");
//            } else {
//                return new BigDecimal(amount);
//            }
//        } else {// impossbile?
//            throw new Exception("不存在的内部账号透支标识：" + oli.getOverdraftSign());
//        }
        return new BigDecimal(amount);

    }

    /**
     * 获取所有子账号归集中的付款方账号
     **/
    private String[] getBankAcc(List<BisAccGatherPolicyVO> accGatherPolicys) {
        Set<String> bas = new HashSet<String>();//所有子账号
        for (BisAccGatherPolicyVO agp : accGatherPolicys) {
            bas.add(agp.getBankAcc());
        }
        return bas.toArray(new String[bas.size()]);
    }


}
