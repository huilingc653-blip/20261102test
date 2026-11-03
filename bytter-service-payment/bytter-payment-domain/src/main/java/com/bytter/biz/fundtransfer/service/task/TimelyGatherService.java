package com.bytter.biz.fundtransfer.service.task;

import com.bytter.biz.allocation.vo.BisAccGatherPolicyVO;
import com.bytter.biz.bdlinks.entity.BisGatherCmd;
import com.bytter.biz.bdlinks.service.BisGatherCmdService;
import com.bytter.biz.fundtransfer.bean.BisAccGatherPolicySearchBean;
import com.bytter.biz.fundtransfer.dao.FundTransferDao;
import com.bytter.biz.fundtransfer.enums.BankConstants;
import com.bytter.biz.fundtransfer.enums.GatherModeEnum;
import com.bytter.biz.fundtransfer.enums.VoucherStatEnum;
import com.bytter.biz.fundtransfer.service.FundTransferBase;
import com.bytter.biz.fundtransfer.util.DateUtil;
import com.bytter.biz.inneraccount.dao.BisGatherPolicyDao;
import com.bytter.biz.inneraccount.entity.BisGatherPolicy;
import com.bytter.biz.payment.api.constant.AllocationConstant;
import com.bytter.biz.payment.api.constant.BdlinkConstant;
import com.bytter.biz.payment.api.dto.BisAccDtlDTO;
import com.bytter.biz.payment.api.dto.BtBankAccDTO;
import com.bytter.core.common.context.UserContextHelper;
import com.bytter.core.sequence.SequenceSingleton;
import com.bytter.core.sequence.enums.SequenceEnum;
import com.bytter.core.utils.DateUtils;
import com.bytter.system.api.ITaskService;
import com.bytter.system.api.dto.BisAccBalDTO;
import com.google.common.collect.Lists;
import com.xxl.job.core.handler.annotation.XxlJob;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.*;

/**
 * 资金调拨-实时归集定时任务实现
 *
 * @author fangcf
 * @date 2026-04-13
 */
@Service
public class TimelyGatherService extends FundTransferBase implements ITaskService {
    @Autowired
    FundTransferDao fundTransferDao;
    @Autowired
    BisGatherPolicyDao bisGatherPolicyDao;
    @Autowired
    private BisGatherCmdService bisGatherCmdService;

    @Override
    @XxlJob("TimelyGatherService")
    public void run(String arguments) {
        //初始化查询bean
        BisAccGatherPolicySearchBean bagpSearchBean = new BisAccGatherPolicySearchBean();
        // gather_type: 0 定时归集 1  实时归集 2 定时下拨
        bagpSearchBean.setGatherType(new String[]{AllocationConstant.GATHERTYPE_1});
        //实时归集参数设置时必须设定为全额
        bagpSearchBean.setGatherMode(new String[]{
                GatherModeEnum.GATHER_ALL.value
        });
        //当前租户
        String tenantId = UserContextHelper.helper().getTenantId();
        this.logger.info("开始执行资金调拨-实时归集任务，租户：{}，时间：{}", tenantId, DateUtils.format(DateUtils.getDate(), DateUtils.PATTERN_CLASSICAL));
        //获取全量实时归集数据 bis_acc_gather_policy
        List<BisAccGatherPolicyVO> bisAccGatherPolicy = fundTransferDao.getBisAccGatherPolicyList(bagpSearchBean);
        if (bisAccGatherPolicy.isEmpty()) {
            this.logger.info("---当前租户：{}，没有实时归集的任务需要执行---", tenantId);
            return;
        }
        //执行资金实时归集
        this.genTimelyGather(bisAccGatherPolicy);
    }

    private void genTimelyGather(List<BisAccGatherPolicyVO> bisAccGatherPolicy) {
        this.logger.info("资金调拨-实时归集任务，定时扫描服务执行共：" + bisAccGatherPolicy.size() + "条归集信息");
        // 一次性提取所有相关账号
        Map<String, BtBankAccDTO> bankAccInfoMap = getAllBankAccInfo(bisAccGatherPolicy);
        // 需要过滤的交易信息（例外规则管理菜单配置）
        HashMap<String, String> filterMap = fundTransferDao.gatherPolicyFilterMap();
        //循环处理
        for (BisAccGatherPolicyVO accGatherPolicy : bisAccGatherPolicy) {
            BisGatherPolicy bisGatherPolicy = bisGatherPolicyDao.getUniqueBy("id", accGatherPolicy.getGatherPolicyId());
            accGatherPolicy.setBisGatherPolicy(bisGatherPolicy);
            try {
                //获取未归集的收款明细(并剔除需要过滤的数据)
                List<BisAccDtlDTO> accDtls = getGatherDetails(accGatherPolicy, filterMap);
                if (null == accDtls || accDtls.isEmpty()) {
                    logger.error("当前不存在有效的实时归集明细。");
                    continue;
                }
                logger.info("有" + accDtls.size() + "条明细可以进行归集");
                genBisGatherCmd(accGatherPolicy, accDtls, bankAccInfoMap);
            } catch (Exception e) {
                String exceptionMessage = e.getMessage();
                logger.error("租户：" + accGatherPolicy.getTenantId()
                        + "，子账号：" + accGatherPolicy.getBankAcc()
                        + "，父账号：" + accGatherPolicy.getParentBankAcc()
                        + "，策略：" + accGatherPolicy.getBisGatherPolicy().getGatherPolicyCode()
                        + "，归集指令生成异常:" + exceptionMessage
                );
            }
        }
    }

    /**
     * 获取未归集的收款明细 ，收多少归多少
     *
     * @param filterMap       需过滤的收款明细
     * @param accGatherPolicy BisAccGatherPolicyVO
     * @return java.util.List<com.bytter.facade.netbank.dto.BisAccDtlDTO>
     */
    private List<BisAccDtlDTO> getGatherDetails(BisAccGatherPolicyVO accGatherPolicy, HashMap filterMap) {
        Date lastGatherDate = accGatherPolicy.getFirstTime();
        int backDays = 0; // 配置可回查天数（当前默认只归集当天的收款明细）
        //保证回查时间不会超过第一次归集时间
        if (DateUtil.diffDays(accGatherPolicy.getFirstTime(), new Date()) > backDays) {
            lastGatherDate = DateUtil.addDay(new Date(), (-1) * backDays);
        }
        lastGatherDate = DateUtil.parseDate(DateUtil.formatDate(lastGatherDate, "yyyy-MM-dd"), "yyyy-MM-dd");
        //获取据指定上次归集时间没有完成该归集的且为收入的余额明细及历史明细（收款明细）
        List<BisAccDtlDTO> dtls = fundTransferDao.getBisAccDtl4NotGather(accGatherPolicy.getBankAcc(), lastGatherDate);
        List<BisAccDtlDTO> realDtls = new ArrayList();
        for (BisAccDtlDTO dtl : dtls) {
            //剔除需要过滤的交易明细
            if (filterMap.get(accGatherPolicy.getBankAcc() + dtl.getOppAccNo()) != null) {
                continue;
            }
            realDtls.add(dtl);
        }
        return realDtls;
    }

    /**
     * 批量生成归集指令。
     *
     * @param accGatherPolicy 当前归集账号信息
     * @param accDtls         当前被归集的账号收入明细
     * @param bankAccInfoMap  相关账号信息（用于判断当前处理账号是否有效）
     * @throws Exception
     */
    private void genBisGatherCmd(BisAccGatherPolicyVO accGatherPolicy, List<BisAccDtlDTO> accDtls, Map<String, BtBankAccDTO> bankAccInfoMap) throws Exception {
        BtBankAccDTO payeeBankAccInfo = bankAccInfoMap.get(accGatherPolicy.getParentBankAcc() + accGatherPolicy.getCurId());
        if (payeeBankAccInfo == null) {
            throw new Exception("租户：" + accGatherPolicy.getTenantId() + "，收款父账号：" + accGatherPolicy.getParentBankAcc() + "信息未维护。");
        }
        BtBankAccDTO payerBankAccInfo = bankAccInfoMap.get(accGatherPolicy.getBankAcc() + accGatherPolicy.getCurId());
        if (payerBankAccInfo == null) {
            throw new Exception("租户：" + accGatherPolicy.getTenantId() + "，付款子账号：" + accGatherPolicy.getBankAcc() + "信息未维护。");
        }

        List<BisAccBalDTO> accBalList = fundTransferDao.listByUnion(accGatherPolicy.getBankAcc(), accGatherPolicy.getCurId(), DateUtils.getDate());
        if (CollectionUtils.isEmpty(accBalList)) {
            throw new Exception("没有从数据库中查到租户：" + accGatherPolicy.getTenantId() + "，付款子账号" + accGatherPolicy.getBankAcc() + "的余额值。");
        }

        List<BisGatherCmd> entityList = Lists.newArrayList();
        BisAccDtlDTO accDtl;
        for (int i = 0; i < accDtls.size(); i++) {
            accDtl = accDtls.get(i);

            BisGatherCmd gc = new BisGatherCmd();
            //收款单ID
            gc.setCbsBillId("");
            //单位ID
            gc.setCorpId(payerBankAccInfo.getCorpId());
            //银行编码ID
            gc.setBifCode(payerBankAccInfo.getBifCode());
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
            gc.setGatherAmt(accDtl.getAmt());
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
            //收付款来源
            gc.setVoucherFrom(BdlinkConstant.FROM_ALLOCATION_TYPE_AUTO_UP);
            //业务类型
            gc.setBusOriType(10);
            //指令类型
            gc.setCmdType(BdlinkConstant.CMDTYPE_AUTO);
            gc.setDtlId(accDtl.getId());
            // 3准备发送
            gc.setCmdStat(VoucherStatEnum.READY_SENDING.value);
            //产生bis_gather_cmd凭证号voucherNo
            gc.setVoucherNo(SequenceSingleton.getSequence(SequenceEnum.BIS_GATHER_CMD));

            entityList.add(gc);
        }
        if (!CollectionUtils.isEmpty(entityList)) {
            // 批量新增付款指令
            bisGatherCmdService.batchSave(entityList);
        }
        logger.info("实时归集指令插入成功，租户：" + payerBankAccInfo.getTenantId() + "，付款账号：" + payerBankAccInfo.getBankAcc() + "，收款账号：" + payeeBankAccInfo.getBankAcc());
    }

}
