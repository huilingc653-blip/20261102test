package com.bytter.biz.fundtransfer.service.task;

import com.bytter.biz.allocation.vo.BisAccGatherPolicyVO;
import com.bytter.biz.bdlinks.entity.BisExc;
import com.bytter.biz.bdlinks.entity.BisGatherCmd;
import com.bytter.biz.bdlinks.service.BisExcService;
import com.bytter.biz.bdlinks.service.BisGatherCmdService;
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
import com.bytter.biz.inneraccount.entity.BisGatherPolicy;
import com.bytter.biz.payment.api.constant.AllocationConstant;
import com.bytter.biz.payment.api.constant.BdlinkConstant;
import com.bytter.biz.payment.api.dto.BtBankAccDTO;
import com.bytter.core.common.context.UserContextHelper;
import com.bytter.core.sequence.SequenceSingleton;
import com.bytter.core.sequence.enums.SequenceEnum;
import com.bytter.core.utils.DateUtils;
import com.bytter.system.api.ITaskService;
import com.bytter.system.api.dto.BisAccBalDTO;
import com.xxl.job.core.handler.annotation.XxlJob;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 资金调拨-定时归集定时任务实现
 *
 * @author fangcf
 * @date 2026-04-13
 */
@Service
public class TimedGatherService extends FundTransferBase implements ITaskService {
    @Autowired
    private FundTransferDao fundTransferDao;
    @Autowired
    private BisGatherPolicyDao bisGatherPolicyDao;
    @Autowired
    private BisGatherCmdService bisGatherCmdService;
    @Autowired
    private BisExcService bisExcService;

    @Override
    @XxlJob("TimedGatherService")
    public void run(String arguments) {
        //初始化查询bean
        BisAccGatherPolicySearchBean bagpSearchBean = new BisAccGatherPolicySearchBean();
        // gather_type: 0 定时归集 1  实时归集 2 定时下拨
        bagpSearchBean.setGatherType(new String[]{AllocationConstant.GATHERTYPE_0});
        bagpSearchBean.setGatherMode(new String[]{
                GatherModeEnum.GATHER_HOLD.value,
                GatherModeEnum.GATHER_HOLD_INTEGER.value,
                GatherModeEnum.GATHER_INTEGER.value,
                GatherModeEnum.GATHER_ALL.value,
                GatherModeEnum.GATHER_CONSTANT.value,
                GatherModeEnum.GATHER_PERCENT.value
        });
        //当前时间
        Date date = new Date();
        bagpSearchBean.setNextTime(date);
        //当前租户
        String tenantId = UserContextHelper.helper().getTenantId();
        this.logger.info("开始执行资金调拨-定时归集任务，租户：{}，时间：{}", tenantId, DateUtils.format(date, DateUtils.PATTERN_CLASSICAL));
        //获取全量调拨数据 bis_acc_gather_policy
        List<BisAccGatherPolicyVO> bisAccGatherPolicy = fundTransferDao.getBisAccGatherPolicyList(bagpSearchBean);
        if (bisAccGatherPolicy.isEmpty()) {
            this.logger.info("---当前租户：{}，没有归集任务需要执行---", tenantId);
            return;
        }
        //执行资金归集
        this.genFundGather(bisAccGatherPolicy);
    }

    /**
     * 资金归集
     *
     * @param bisAccGatherPolicy
     * @return void
     */
    private void genFundGather(List<BisAccGatherPolicyVO> bisAccGatherPolicy) {
        this.logger.info("资金调拨-定时归集任务，定时扫描服务执行共:" + bisAccGatherPolicy.size() + "条归集信息");
        // 一次性提取所有相关账号
        Map<String, BtBankAccDTO> bankAccInfoMap = super.getAllBankAccInfo(bisAccGatherPolicy);
        //当前时间是否是工作日
        boolean isWorkSign = fundTransferDao.isWorkDaySign(DateUtil.getFormattingDateTime(new Date(), 7));
        for (BisAccGatherPolicyVO accGatherPolicy : bisAccGatherPolicy) {
            //获取策略对象
            BisGatherPolicy bisGatherPolicy = bisGatherPolicyDao.getUniqueBy("id", accGatherPolicy.getGatherPolicyId());
            accGatherPolicy.setBisGatherPolicy(bisGatherPolicy);
            //计算下次归集时间(未到执行时间或当前是节假日且策略是节假日不执行时返回空，本条策略账号不执行)
            Date expectedNextTime = tryGetNextRunTime(accGatherPolicy);
            if (null == expectedNextTime) {
                //当前时间不是本条策略的运行时间，跳过本次执行
                continue;
            }
            accGatherPolicy.setExpectedNextTime(expectedNextTime);
            logger.info("开始处理归集策略：" + accGatherPolicy.getBisGatherPolicy().getGatherPolicyCode() + "，租户：" + accGatherPolicy.getTenantId() + " 子账号为：" + accGatherPolicy.getBankAcc() + " 父账号为：" + accGatherPolicy.getParentBankAcc());
            try {
                //进行节假日规则校验判断是否执行
                if (checkFilterHolidays(accGatherPolicy, isWorkSign)) {
                    //生成归集付款指令并更新下次下拨执行时间
                    genBisGatherCmd(accGatherPolicy, bankAccInfoMap);
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
                        + "，归集指令生成异常:" + exceptionMessage
                );
                continue;
            }
        }
        return;
    }

    /**
     * 归集指令生成
     *
     * @param accGatherPolicy
     * @param bankAccInfoMap
     * @return void
     */
    private void genBisGatherCmd(BisAccGatherPolicyVO accGatherPolicy, Map<String, BtBankAccDTO> bankAccInfoMap) throws Exception {
        //校验父子账号信息与余额
        BtBankAccDTO payeeBankAccInfo = bankAccInfoMap.get(accGatherPolicy.getParentBankAcc() + accGatherPolicy.getCurId());
        if (payeeBankAccInfo == null) {
            throw new Exception("租户：" + accGatherPolicy.getTenantId() + "，收款父账号：" + accGatherPolicy.getParentBankAcc() + "信息未维护。");
        }
        BtBankAccDTO payerBankAccInfo = bankAccInfoMap.get(accGatherPolicy.getBankAcc() + accGatherPolicy.getCurId());
        if (payerBankAccInfo == null) {
            throw new Exception("租户：" + accGatherPolicy.getTenantId() + "，付款子账号：" + accGatherPolicy.getBankAcc() + "信息未维护。");
        }

        List<BisAccBalDTO> parentAccBalList = fundTransferDao.listByUnion(accGatherPolicy.getParentBankAcc(), accGatherPolicy.getCurId(), DateUtils.getDate());
        if (CollectionUtils.isEmpty(parentAccBalList)) {
            throw new Exception("没有从数据库中查到租户：" + accGatherPolicy.getTenantId() + "，收款父账号：" + accGatherPolicy.getParentBankAcc() + "的余额值。");
        }
        if (parentAccBalList.size() > 1) {
            throw new Exception("数据库中查到租户：" + accGatherPolicy.getTenantId() + "，收款父账号：" + accGatherPolicy.getParentBankAcc() + "余额数据重复，重复日期为：" + DateUtils.formatDate(DateUtils.getDate()));
        }

        List<BisAccBalDTO> accBalList = fundTransferDao.listByUnion(accGatherPolicy.getBankAcc(), accGatherPolicy.getCurId(), DateUtils.getDate());
        if (CollectionUtils.isEmpty(accBalList)) {
            throw new Exception("没有从数据库中查到租户：" + accGatherPolicy.getTenantId() + "，付款子账号" + accGatherPolicy.getBankAcc() + "的余额值。");
        }
        if (accBalList.size() > 1) {
            throw new Exception("数据库中查到租户：" + accGatherPolicy.getTenantId() + "，付款子账号：" + accGatherPolicy.getBankAcc() + "余额数据重复，重复日期为：" + DateUtils.formatDate(DateUtils.getDate()));
        }
        BisAccBalDTO accBal = accBalList.stream().findFirst().get();

        //计算并校验归集的金额
        BigDecimal amount = getRealGatherAmount(accGatherPolicy, accBal);

        if (payerBankAccInfo.getBifCode().equals(payeeBankAccInfo.getBifCode())) {
            //同行
            genBisGatherCmd4SameBank(accGatherPolicy, payerBankAccInfo, payeeBankAccInfo, amount);
        } else {
            //跨行
            genBisExc4DiffBank(accGatherPolicy, payerBankAccInfo, payeeBankAccInfo, amount);
        }
    }

    /**
     * 计算并校验归集的金额
     *
     * @param accGatherPolicy 账号归集策略
     * @param accBal          账号对应余额信息
     * @return Double计算出来的归集金额
     * @throws Exception
     */
    private BigDecimal getRealGatherAmount(BisAccGatherPolicyVO accGatherPolicy, BisAccBalDTO accBal) throws Exception {

        BisGatherPolicy gatherPolicy = accGatherPolicy.getBisGatherPolicy();
        DecimalFormat format = new DecimalFormat("###0.00");
        boolean isCur = DateUtil.getFormattingDateTime(new Date(), 0).equals(DateUtil.getFormattingDateTime(accBal.getGetDate(), 0));
        if (!isCur) {
            throw new Exception("付款子账号:" + accGatherPolicy.getBankAcc() + "在数据库中的余额不为当天余额。");
        }

        double amount = 0.0d;
        GatherModeEnum gatherMode = GatherModeEnum.toEnum(gatherPolicy.getGatherMode());
        double availBal = accBal.getAvailBal().doubleValue(); //当前可用余额
        switch (gatherMode) {
            //全额归集
            case GATHER_ALL:
                if (availBal <= 0.0d) {
                    throw new Exception("当前余额小于或等于零。");
                }
                if (gatherPolicy.getMinAmt() != null && availBal < gatherPolicy.getMinAmt().doubleValue()) {
                    throw new Exception("归集金额：" + new BigDecimal(availBal) + "小于最小金额:" + gatherPolicy.getMinAmt());
                }
                amount = availBal;
                logger.info("全额归集类型为：" + gatherMode + ",可用余额为:" + availBal + ",需归集金额为：" + amount);
                break;

            //固定金额归集
            case GATHER_CONSTANT:
                if (availBal < gatherPolicy.getGatherAmt().doubleValue()) {
                    throw new Exception("当前余额小于保留金额。当前余额：" + new BigDecimal(availBal) + ",保留金额：" + gatherPolicy.getMinAmt());
                }
                amount = gatherPolicy.getGatherAmt().doubleValue();
                logger.info("定额归集(固定金额归集)类型为：" + gatherMode + ",可用余额为:" + availBal + ",需归集金额为：" + amount);
                break;

            //留底资金归集
            case GATHER_HOLD://如果当前账号的余额小于策略上定义的保留金额不需要在归集
                if (availBal <= gatherPolicy.getGatherAmt().doubleValue()) {
                    throw new Exception("当前余额小于保留金额。当前余额：" + new BigDecimal(availBal) + ",保留金额：" + gatherPolicy.getMinAmt());
                }
                //需要归集的钱=当前的账号余额-需要保留的余额,如果需要保留的金额为0，就是0余额归集，把整个账号的钱全部归集上来
                amount = Arith.sub(availBal, gatherPolicy.getGatherAmt().doubleValue());//高精度运算
                if (gatherPolicy.getMinAmt() != null && amount < gatherPolicy.getMinAmt().doubleValue()) {
                    throw new Exception("归集金额：" + new BigDecimal(amount) + "小于最小金额:" + gatherPolicy.getMinAmt());
                }
                logger.info("留底资金归集类型为：" + gatherMode + ",可用余额为:" + availBal + ",保留金额（留底金额）为:" + gatherPolicy.getGatherAmt() + ",需归集金额为：" + amount + ",最小执行额度为:" + gatherPolicy.getMinAmt());
                break;

            //取整归集
            case GATHER_INTEGER:
                if (availBal <= gatherPolicy.getGatherAmt().doubleValue()) {
                    throw new Exception("当前余额小于归集基数。当前余额：" + new BigDecimal(availBal) + ",归集基数：" + gatherPolicy.getMinAmt());
                }
                amount = Arith.sub(availBal, availBal % gatherPolicy.getGatherAmt().doubleValue());
                if (gatherPolicy.getMinAmt() != null && amount < gatherPolicy.getMinAmt().doubleValue()) {
                    throw new Exception("归集金额：" + new BigDecimal(amount) + "小于最小金额:" + gatherPolicy.getMinAmt());
                }
                logger.info("取整归集类型为：" + gatherMode + ",可用余额为:" + new BigDecimal(availBal) + ",需归集金额为：" + new BigDecimal(amount));
                break;

            //留底取整归集
            case GATHER_HOLD_INTEGER:
                if (availBal <= gatherPolicy.getGatherAmt().doubleValue()) {
                    throw new Exception("当前余额小于保留金额不能归集。当前余额：" + new BigDecimal(availBal) + ",保留金额：" + gatherPolicy.getMinAmt());
                }
                //需要归集的钱=当前的账号余额-需要保留的余额,如果需要保留的金额为0，就是0余额归集，把整个账号的钱全部归集上来
                amount = Arith.sub(availBal, gatherPolicy.getGatherAmt().doubleValue());//高精度运算
                if (amount <= 0) {
                    throw new Exception("计算后的归集金额为零!，不能归集。");
                }
                logger.info("留底取整计算前归集金额为:" + format.format(new BigDecimal(amount)));
                amount = Arith.sub(amount, amount % gatherPolicy.getMinAmt().doubleValue());
                //当取整后有小数，再次取整
                amount = Arith.sub(amount, amount % gatherPolicy.getMinAmt().doubleValue());
                logger.info("留底取整计算后归集金额为:" + new BigDecimal(amount));
                logger.info("留底取整归集类型为：" + gatherMode + ",可用余额为:" + format.format(new BigDecimal(availBal)) + ",保留金额（留底金额）为:" + format.format(gatherPolicy.getMinAmt()) + ",需归集金额为：" + new BigDecimal(amount) + ",取整单位为:" + gatherPolicy.getMinAmt());
                break;

            //比例归集
            case GATHER_PERCENT:
                amount = availBal * gatherPolicy.getDifferScale().doubleValue();
                if (amount <= 0) {
                    throw new Exception("计算后的归集金额为零!，不能归集。");
                }
                if (Double.compare(availBal, amount) < 0) {
                    throw new Exception("当前余额不足，不能归集。");
                }
                logger.info("比例归集类型为：" + gatherMode + ",可用余额为:" + availBal + ",需归集金额为：" + amount + ",比例为：" + gatherPolicy.getDifferScale());
                break;

            default:
                throw new Exception("无效的归集类型！" + gatherPolicy.getGatherMode());
        }
        if (amount <= 0.0d) {
            throw new Exception("账号：" + accGatherPolicy.getParentBankAcc() + "下拨金额无效（下拨金额为负数），无法归集。");
        }
        return new BigDecimal(amount);
    }


    /**
     * 生成归集指令及更新下次归集执行时间(同行)
     *
     * @param accGatherPolicy  账号策略
     * @param amount           归集金额
     * @param payerBankAccInfo 付方账号
     * @param payeeBankAccInfo 收方账号
     * @return void
     */
    private void genBisGatherCmd4SameBank(BisAccGatherPolicyVO accGatherPolicy, BtBankAccDTO payerBankAccInfo, BtBankAccDTO payeeBankAccInfo, BigDecimal amount) throws Exception {
        BisGatherCmd gc = new BisGatherCmd();
        //收款单ID
        gc.setCbsBillId("");
        //单位ID
        gc.setCorpId(payerBankAccInfo.getCorpId());
        //银行编码ID
        gc.setBifCode(accGatherPolicy.getBifCode());
        //子账号为付款账号
        gc.setChildAccId(accGatherPolicy.getBankAccId());
        gc.setChildAcc(payerBankAccInfo.getBankAcc());
        gc.setChildAccName(payerBankAccInfo.getAccName());
        gc.setChildBank(payerBankAccInfo.getBankName());
        gc.setChildProv(payerBankAccInfo.getProv());
        gc.setChildCity(payerBankAccInfo.getCity());
        //父账号为收方账号
        gc.setParentAccId(accGatherPolicy.getParentAccId());
        gc.setParentAcc(payeeBankAccInfo.getBankAcc());
        gc.setParentAccName(payeeBankAccInfo.getAccName());
        gc.setParentBank(payeeBankAccInfo.getBankName());
        gc.setParentProv(payeeBankAccInfo.getProv());
        gc.setParentCity(payeeBankAccInfo.getCity());
        //归集金额
        gc.setGatherAmt(amount);
        //归集策略ID
        gc.setGatherPolicyId(accGatherPolicy.getGatherPolicyId());
        //币别
        gc.setCurId(accGatherPolicy.getCurId());
        //运行日期
        gc.setRunTime(new Date());
        //加急标志
        gc.setIsQuick(accGatherPolicy.getBisGatherPolicy().getIsQuick());
        //跨行标志
        gc.setDifBank(payerBankAccInfo.getBifCode().equals(payeeBankAccInfo.getBifCode()) ? BankConstants.SAME_BANK : BankConstants.DIF_BANK);
        //同城异地标志
        gc.setAreaSign(payerBankAccInfo.getCity().endsWith(payeeBankAccInfo.getCity()) ? BankConstants.SAME_AREA : BankConstants.DIF_AREA);
        GatherModeEnum gatherMode = GatherModeEnum.toEnum(accGatherPolicy.getBisGatherPolicy().getGatherMode()); // impossible null
        //摘要
        if (accGatherPolicy.getBisGatherPolicy().getPurpose() != null) {
            gc.setPurpose(accGatherPolicy.getBisGatherPolicy().getPurpose());
        } else {
            gc.setPurpose(gatherMode.description);
        }
        //附言
        if (accGatherPolicy.getBisGatherPolicy().getAddWord() != null) {
            gc.setAddWord(accGatherPolicy.getBisGatherPolicy().getAddWord());
        } else {
            gc.setAddWord(gc.getPurpose());
        }
        //收付款来源 8 定时同行归集
        gc.setVoucherFrom(BdlinkConstant.FROM_ALLOCATION_TYPE_AUTO_UP);
        //业务类型
        gc.setBusOriType(10);
        //指令类型
        gc.setCmdType(BdlinkConstant.CMDTYPE_AUTO);
        gc.setDtlId("");
        gc.setCmdStat(VoucherStatEnum.READY_SENDING.value);// 3准备发送
        //产生bis_gather_cmd 凭证号voucherNo
        gc.setVoucherNo(SequenceSingleton.getSequence(SequenceEnum.BIS_GATHER_CMD));

        //增加付款指令
        bisGatherCmdService.save(gc);
        //更新下次执行时间
        fundTransferDao.updateBisAccGatherPolicyNextTime(accGatherPolicy);

        logger.info("同行归集指令（付款指令）插入成功，指令编号：" + gc.getVoucherNo() + "，租户：" + payerBankAccInfo.getTenantId() + "，付款账号：" + payerBankAccInfo.getBankAcc() + "，收款账号：" + payeeBankAccInfo.getBankAcc() + "，金额：" + amount);
    }

    /**
     * 生成付款指令和更新下次归集执行时间（跨行）
     *
     * @param accGatherPolicy  账号策略
     * @param payerBankAccInfo 支付账号信息
     * @param payeeBankAccInfo 收款账号信息
     * @param amount           余额
     * @throws Exception
     */
    private void genBisExc4DiffBank(BisAccGatherPolicyVO accGatherPolicy, BtBankAccDTO payerBankAccInfo, BtBankAccDTO payeeBankAccInfo, BigDecimal amount) throws Exception {
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
        //修改人：谢金花，修改时间：20210824，修改理由：前端业务系统付款方式可设置为无效会引起该字段值是空，而付款指令表中付款方式是必填项，造成插入跨行归集指令入库失败
        String vourcherTypeId = fundTransferDao.getSysVoucherCode("10", payerBankAccInfo.getTenantId());
        //判断付款方式是否是空，为空则不入库
        if (vourcherTypeId == null || vourcherTypeId.trim().length() == 0) {
            throw new Exception("生成跨行定时归集指令发生错误，未找到有效的拨付付款方式。账号：" + accGatherPolicy.getBankAcc());
        }
        be.setVoucherTypeId(vourcherTypeId);
        //收款人账号
        be.setPayeeAcc(payeeBankAccInfo.getBankAcc());
        //收款人名称
        be.setPayeeAccName(payeeBankAccInfo.getAccName());
        //收款人开户行名称
        be.setPayeeBank(payeeBankAccInfo.getBankName());
        //收款人省份
        be.setPayeeProv(payeeBankAccInfo.getProv());
        //收款人城市
        be.setPayeeCity(payeeBankAccInfo.getCity());
        //收款人联行号
        be.setPayeeBankCode(payeeBankAccInfo.getBankCode());
//        //收款人联行号
//        be.setPayeeBankCode(payeeBankAccInfo.getBankSourcecode());
        //加急标志
        be.setIsQuick(accGatherPolicy.getBisGatherPolicy().getIsQuick());
        //交易模式
        be.setTransMode("3");
        //接口代码
        be.setBifCode(accGatherPolicy.getBifCode());
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
            be.setAddWord(be.getPurpose());
        }
        be.setPolicyId(accGatherPolicy.getGatherPolicyId());
        //付款来源 10 定时跨行归集
        be.setVoucherFrom(BdlinkConstant.FROM_ALLOCATION_TYPE_AUTO_UP);
        //业务类型
        be.setBusOriType(10);
        //指令类型
        be.setCmdType(BdlinkConstant.CMDTYPE_AUTO);
        // 其它信息
        be.setCounterAcc(payerBankAccInfo.getCounterAccId());// 中心账号－记帐
        //准备发送状态3
        be.setVoucherStat(VoucherStatEnum.READY_SENDING.value);
        // 执行生成付款指令和更新下次执行时间操作(对于跨行来说归集只是一笔普通付款)
        //指令编号
        be.setVoucherNo(SequenceSingleton.getSequence(SequenceEnum.BIS_EXC));
        //付款指令新增
        bisExcService.save(be);
        //更新下次执行时间
        fundTransferDao.updateBisAccGatherPolicyNextTime(accGatherPolicy);
        logger.info("跨行归集指令（付款指令）插入成功，指令编号:" + be.getVoucherNo() + "，租户：" + payerBankAccInfo.getTenantId() + "，付款账号：" + payerBankAccInfo.getBankAcc() + "，收款账号：" + payeeBankAccInfo.getBankAcc() + "，金额：" + amount);
    }
}
