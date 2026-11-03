package com.bytter.bill.domain.bill.service;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import com.bytter.bill.api.bill.constant.AccebillConstant;
import com.bytter.bill.domain.bill.dao.AbBillAccBalDao;
import com.bytter.bill.domain.bill.dao.AbBillAccDtlDao;
import com.bytter.bill.domain.bill.entity.*;
import com.bytter.bill.domain.bill.vo.AbAccrecordsVO;
import com.bytter.bill.domain.bill.vo.AbBillAccBalTypeVO;
import com.bytter.bill.domain.bill.vo.AbBillAccBalVO;
import com.bytter.bill.domain.bill.vo.AbBillAccDtlBalVO;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.utils.*;
import com.bytter.system.api.ICorpService;
import com.bytter.system.api.ITaskService;
import com.bytter.system.api.dto.BtBankAccDTO;
import com.bytter.system.api.dto.CorpDTO;
import com.bytter.system.api.dto.DictDTO;
import com.google.common.collect.Maps;
import com.xxl.job.core.context.XxlJobHelper;
import com.xxl.job.core.handler.annotation.XxlJob;

import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;
/**
 * @author zhengg
 * @version 1.0
 * @Title: 票据账户余额Service
 * @Description: AB_BILL_ACC_BAL
 * @date 2017-09-28 19:20:01
 */
@Service("abBillAccBalService")
@Transactional
public class AbBillAccBalService extends BaseService<AbBillAccBal, String> implements ITaskService {
    @Autowired
    private AbBillAccBalDao abBillAccBalDao;
    @Autowired
    private AbBillAccDtlDao abBillAccDtlDao;
    @Autowired
    private AbAccrecordsService abAccrecordsService;
    @Autowired
    private AbVoucherinfoViceService abVoucherinfoViceService;
    @Autowired
    private AbVoucherinfoService abVoucherinfoService;
    @Autowired
    private AbBillTransferDownManageService abBillTransferDownManageService;
    @Autowired
    private AbCorpService abCorpService;
    @Autowired
    private AbUserService abUserService;
    @Autowired
    private AbBtBankAccService abBtBankAccService;
    @Autowired
    private AbBillTransferDtlService abBillTransferDtlService;
    @Autowired
    private AbAccountGrantService accountGrantService;
    @DubboReference(check = false)
    private ICorpService iCorpService;
    @Override
    public BaseDao<AbBillAccBal, String> getEntityDao() {
        return abBillAccBalDao;
    }
    @Autowired
    private AbDictService abDictService;
    /**
     * 票据余额透视图数据缓存，同一个用户相同的条件，一分钟内不查数据库
     */
    private static Map<String,List<AbBillAccDtlBalVO>> billBalanceDataMap = new ConcurrentHashMap<>();
    private static Set<String> lockSet = ConcurrentHashMap.newKeySet();
    /**
     * 缓存失效时间
     */
    private static long expireTime = 180L;
    @SuppressWarnings("rawtypes")
    @Transactional(readOnly = true)
    public DataTablesOutput listAbBillAccBalDao(DataTablesInput dataTablesInput) {
        List<String> corpAuthList = abCorpService.getUserCorpIds(AccebillConstant.AB_BILL_ACCBAL_QUERY_CORPOP, dataTablesInput.getCurrentUserId());
        String corpIds = dataTablesInput.getFormdata().get("ins_corpId") == null ? null : String.valueOf(dataTablesInput.getFormdata().get("ins_corpId"));
        //设置是否过滤票据账号
        accountGrantService.setElectricBillAccount(dataTablesInput, AccebillConstant.AB_BILL_ACCBAL_QUERY_CORPOP, corpIds);
        DataTablesOutput dataTablesOutput = abBillAccBalDao.listAbBillAccBal(dataTablesInput, corpAuthList);
        //处理c.code as corpCode,c.name as corpName SYS_CORP
        List<AbBillAccBalVO> data = dataTablesOutput.getData();
        List<String> corpIdList = data.stream().map(vo -> vo.getCorpId()).collect(Collectors.toList());
        corpAuthList.addAll(corpIdList);
        Map<String, CorpDTO> corpDTOMap = abCorpService.listCorpDtoByCorpIds(corpAuthList);
        List<AbBillAccBalVO> result = data.stream().map(da -> {
            if (corpDTOMap.containsKey(da.getCorpId())) {
                da.setCorpName(corpDTOMap.get(da.getCorpId()).getName());
                da.setCorpCode(corpDTOMap.get(da.getCorpId()).getCode());
                return da;
            } else {
                return da;
            }
        }).collect(Collectors.toList());
        dataTablesOutput.setData(result);
        return dataTablesOutput;
    }
    /**
     * 票据业务余额操作
     * 设置票据账户余额
     *
     * @param accrecordsId 业务流水ID
     */
    public void setBankAccBillBal(String accrecordsId, ICurrentUser userVo) {
        abBillAccBalDao.flush();
//		abBillAccBalDao.clear();
        setBankAccBillBal(accrecordsId, AccebillConstant.BILLBAL_OP_BUSTYPE_DEFAULT, userVo);
    }
    /**
     * 票据业务余额操作
     * 设置票据下拨申请业务账户余额
     *
     * @param transferDownId 下拨申请流水ID
     */
    public void setTransferDownBillBal(String transferDownId, ICurrentUser userVo) {
        abBillAccBalDao.flush();
//		abBillAccBalDao.clear();
        if (StringUtils.isEmpty(transferDownId)) {
            return;
        }
        AbBillTransfer transferInfo = abBillTransferDownManageService.get(transferDownId, true);
        if (transferInfo == null) {
            return;
        }
        if (transferInfo != null) {
            abBillTransferDownManageService.refresh(transferInfo);
        }
        AbAccrecords trAccrecords = abAccrecordsService.getTransferAccrecordsInfoByTrid(transferInfo.getId());
        if (trAccrecords == null) {
            return;
        }
//		List<AbAccrecords> accrecordsList=abAccrecordsService.getAccrecordsInfoByIds(accrecordsIds);
        List<AbBillTransferDtl> accrecordsIds = abBillTransferDtlService.getBillAidListByTransferId(transferInfo.getId());
        if (accrecordsIds != null && !accrecordsIds.isEmpty()) {
            String busType = null;
            if (AccebillConstant.AB_BILL_BUS_STATUS11.equals(trAccrecords.getStatus())) {
                busType = AccebillConstant.BILLBAL_OP_BUSTYPE_TRANSFERDOWN11;
            } else if (AccebillConstant.AB_BILL_BUS_STATUS_1.equals(trAccrecords.getStatus())) {
                busType = AccebillConstant.BILLBAL_OP_BUSTYPE_TRANSFERDOWN_1;
            }
            for (AbBillTransferDtl accrecordsId : accrecordsIds) {
                setBankAccBillBal(accrecordsId.getBillAid(), busType, trAccrecords.getBusinessDate(), userVo);
            }
        }
    }
    /**
     * 票据业务操作
     * 设置票据账户余额
     *
     * @param accrecordsId 业务流水ID
     * @param busType      票据业务类型
     */
    public void setBankAccBillBal(String accrecordsId, String busType, ICurrentUser userVo) {
        setBankAccBillBal(accrecordsId, busType, null, userVo);
    }
    /**
     * 票据业务操作
     * 设置票据账户余额
     *
     * @param accrecordsId 业务流水ID
     * @param busType      票据业务类型
     * @param businessDate 业务处理日期
     */
    public void setBankAccBillBal(String accrecordsId, String busType, Date businessDate, ICurrentUser userVo) {
        if (StringUtils.isEmpty(accrecordsId)) {
            return;
        }
        AbAccrecords accrecords = abAccrecordsService.get(accrecordsId, true);
        abAccrecordsService.refresh(accrecords);
        if (accrecords != null
                && StringUtils.isNotEmpty(accrecords.getVid())
                && StringUtils.isNotEmpty(accrecords.getVvid())) {
            AbVoucherinfo vouInfo = accrecords.getVouInfo();
            if (vouInfo == null) {
                vouInfo = abVoucherinfoService.get(accrecords.getVid(), true);
                accrecords.setVouInfo(vouInfo);
            }
            if (vouInfo != null) {
                abVoucherinfoService.refresh(vouInfo);
            } else {
                return;
            }
            AbVoucherinfoVice viceInfo = accrecords.getVouViceInfo();
            if (viceInfo == null) {
                viceInfo = abVoucherinfoViceService.get(accrecords.getVvid(), true);
                accrecords.setVouViceInfo(viceInfo);
            }
            if (viceInfo != null) {
                abVoucherinfoViceService.refresh(viceInfo);
            } else {
                return;
            }
            AbBillAccBalVO billAccBal = new AbBillAccBalVO();
            billAccBal.setBillMoney(vouInfo.getBillMoney());
            Date balDate = businessDate;
            if (balDate == null) {
                balDate = accrecords.getBusinessDate();
            }
            if (balDate != null) {
                balDate = DateUtils.getDateYMD(balDate);
            } else {
                balDate = DateUtils.getDateYMD();
            }
            billAccBal.setBillType(vouInfo.getBillType());
            //进行解析票据业务和状态判断余额是否生成 以及余额计算模式
            boolean isOpBal = billBusBalOp(accrecords, billAccBal, busType, balDate);
            if (isOpBal) {
                billAccBal.setRecordId(accrecords.getId());
                billAccBal.setBankAcc(viceInfo.getBankAcc());
                billAccBal.setCorpId(viceInfo.getCorpId());
                if (balDate != null) {
                    if (billAccBal.getBalDate() == null
                            || balDate.getTime() > billAccBal.getBalDate().getTime()) {
                        billAccBal.setBalDate(balDate);
                    }
                }
//                opBillAccDtl(billAccBal, userVo); 不生成明细无意义涉及修改太麻烦了。
                opBankAccBillBal(billAccBal, userVo);
            }
        }
    }
    /**
     * 进行解析票据业务和状态判断余额是否生成 以及余额计算模式
     *
     * @param accrecords 票据流水信息
     * @param billAccBal 余额计算对象
     * @param busType    票据业务类型
     * @param balDate    余额归属日期
     * @return
     */
    @Transactional(readOnly = true)
    public boolean billBusBalOp(AbAccrecords accrecords, AbBillAccBalVO billAccBal, String busType, Date balDate) {
        boolean reBoo = false;
        if (AccebillConstant.BILLBAL_OP_BUSTYPE_DEFAULT.equals(busType)) {
            String briefId = accrecords.getBriefId();
            Integer status = accrecords.getStatus();
            String optStatus = accrecords.getOptStatus();
            if (AccebillConstant.BRIEF_STATUS_6001.equals(briefId)
                    || AccebillConstant.BRIEF_STATUS_6019.equals(briefId)
                    || AccebillConstant.BRIEF_STATUS_6212.equals(briefId)
                    || (AccebillConstant.AB_SGNUPMK_Y.equals(accrecords.getSignUpFlag()) &&
                    (AccebillConstant.BRIEF_STATUS_9003.equals(briefId)
                            || AccebillConstant.BRIEF_STATUS_9010.equals(briefId)
                            || AccebillConstant.BRIEF_STATUS_9012.equals(briefId)
                            || AccebillConstant.BRIEF_STATUS_9018.equals(briefId)
                            || AccebillConstant.BRIEF_STATUS_9019.equals(briefId))
            )) {
                //		1	9003	提示收票签收
                //			线上电票，由定时器自动触发
                //			业务状态：审批通过95 指令状态：操作完成19 且同意签收
                //			可用余额、余额 增加，占用金额 不变
                //		2	6001	应收票据登记
                //			纸票、线下电票 非质押收入 ，由业务处理
                //			业务状态：审批通过95 指令状态：操作完成19
                //			可用余额、余额 增加，占用金额 不变
                //		7	9010	背书申请签收
                //			业务状态：审批通过95 指令状态：操作完成19
                //			可用余额、余额 增加，占用金额 不变
                //		10	9012	贴现赎回签收
                //			业务状态：审批通过95 指令状态：操作完成19
                //			可用余额、余额 增加，占用金额 不变
                //		20	6212	贴现赎回收回
                //			业务状态：审批通过95 指令状态：操作完成19
                //			可用余额、余额 增加，占用金额 不变
                if (AccebillConstant.AB_BILL_BUS_STATUS95.equals(status)
                        && AccebillConstant.AB_BILL_OPT_STATUS19.equals(optStatus)) {
                    reBoo = true;
                    setbillAccBalInfo(billAccBal,
                            billAccBal.getBillMoney(),
                            billAccBal.getBillMoney(),
                            new BigDecimal(0),
                            AccebillConstant.AB_BILL_CD_SIGN_0,
                            AccebillConstant.AB_BILL_VALID_SIGN_1);
                }
            } else if (AccebillConstant.BRIEF_STATUS_6201.equals(briefId)
                    || AccebillConstant.BRIEF_STATUS_6202.equals(briefId)
                    || AccebillConstant.BRIEF_STATUS_6204.equals(briefId)) {
                //		3	6201	票据上划
                //			1>业务状态：审批中 11  ，由业务处理
                //			可用余额 减少，余额 不变，占用金额 增加
                //			2>业务状态：打回 -2  ，由业务处理
                //			可用余额 增加，余额 不变，占用金额 减少
                //			一：纸票、线下电票
                //			1>业务状态：审批通过95 指令状态：操作完成19 （不做改变）
                //			2>自动生成背书业务（自动生成背书业务中体现）
                //			背书申请人  审批通过95 指令状态：操作完成19
                //			可用余额 不变，余额 减少，占用金额 减少
                //			3>被背书人收入库存
                //			业务状态：审批通过95 指令状态：操作完成19
                //			可用余额、余额 增加，占用金额不变
                //			二：线上电票
                //			1>自动生成背书业务 按线上电票背书业务处理
                //			2>被背书人收入库存 按背书签收处理
                //		4	6202	票据归集
                //			业务状态：审批中 11  ，由业务处理
                //			可用余额 减少，余额 不变，占用金额 增加
                //			业务状态：打回 -2  ，由业务处理
                //			可用余额 增加，余额 不变，占用金额 减少
                //			一：纸票、线下电票
                //			1>业务状态：审批通过95 指令状态：操作完成19  （不做改变）
                //			2>自动生成背书业务 （自动生成背书业务中体现）
                //			背书申请人  审批通过95 指令状态：操作完成19
                //			可用余额 不变，余额 减少，占用金额 减少
                //			3>被背书人收入库存
                //			业务状态：审批通过95 指令状态：操作完成19
                //			可用余额、余额 增加，占用金额不变
                //			二：线上电票
                //			自动生成背书业务 按线上电票背书业务处理
                //			被背书人收入库存 按背书签收处理
                //		5	6204	票据下划
                //			业务状态：审批中 11  ，由业务处理
                //			可用余额 减少，余额 不变，占用金额 增加
                //			业务状态：打回 -2  ，由业务处理
                //			可用余额 增加，余额 不变，占用金额 减少
                //			一：纸票、线下电票
                //			1>业务状态：审批通过95 指令状态：操作完成19（不做改变）
                //			2>自动生成背书业务（自动生成背书业务中体现）
                //			背书申请人 审批通过95 指令状态：操作完成19
                //			可用余额 不变，余额 减少，占用金额 减少
                //			3>被背书人收入库存
                //			业务状态：审批通过95 指令状态：操作完成19
                //			可用余额、余额 增加，占用金额不变
                //			二：线上电票
                //			自动生成背书业务 按线上电票背书业务处理
                //			被背书人收入库存 按背书签收处理
                if (AccebillConstant.AB_BILL_BUS_STATUS11.equals(status)) {
                    reBoo = true;
                    setbillAccBalInfo(billAccBal,
                            new BigDecimal(0),
                            billAccBal.getBillMoney().negate(),
                            billAccBal.getBillMoney(),
                            AccebillConstant.AB_BILL_CD_SIGN_2,
                            AccebillConstant.AB_BILL_VALID_SIGN_1);
                } else if (AccebillConstant.AB_BILL_BUS_STATUS_2.equals(status)) {
                    reBoo = true;
                    setbillAccBalInfo(billAccBal,
                            new BigDecimal(0),
                            billAccBal.getBillMoney(),
                            billAccBal.getBillMoney().negate(),
                            null,
                            AccebillConstant.AB_BILL_VALID_SIGN_0);
                }
            } else if (AccebillConstant.BRIEF_STATUS_010.equals(briefId)
                    || AccebillConstant.BRIEF_STATUS_018.equals(briefId)//修改质押申请和质押解除申请和背书业务余额变化一致
                    || AccebillConstant.BRIEF_STATUS_019.equals(briefId)
            ) {
                //		6	010	背书申请
                //			业务状态：审批中 11 ，由业务处理
                //			可用余额 减少，余额 不变，占用金额 增加
                //			业务状态：打回 -2   （审批打回、指令发送打回） ，由业务处理
                //			可用余额 增加，余额 不变，占用金额 减少
                //			一：纸票、线下电票 ，由业务处理
                //			业务状态：审批通过95 指令状态：操作完成19
                //			可用余额 不变，余额 减少，占用金额 减少
                //			二：线上电票
                //			业务状态：审批通过95 指令状态： -3 指令撤回\-4 指令拒签 ，由定时器业务处理
                //			可用余额 增加，余额 不变，占用金额 减少
                //			业务状态：审批通过95 指令状态：对方同意签收 19 ，由定时器业务处理
                //			可用余额 不变，余额 减少，占用金额 减少
                if (AccebillConstant.AB_BILL_BUS_STATUS11.equals(status)
                        || AccebillConstant.AB_BILL_OPT_STATUS15.equals(optStatus)
                        || AccebillConstant.AB_BILL_OPT_STATUS16.equals(optStatus)) {
                    reBoo = true;
                    setbillAccBalInfo(billAccBal,
                            new BigDecimal(0),
                            billAccBal.getBillMoney().negate(),
                            billAccBal.getBillMoney(),
                            AccebillConstant.AB_BILL_CD_SIGN_2,
                            AccebillConstant.AB_BILL_VALID_SIGN_1);
                } else if (AccebillConstant.AB_BILL_BUS_STATUS_2.equals(status)
                        || (AccebillConstant.AB_BILL_BUS_STATUS95.equals(status)
                        && (AccebillConstant.AB_BILL_OPT_STATUS_3.equals(optStatus)
                        || AccebillConstant.AB_BILL_OPT_STATUS_4.equals(optStatus)))) {
                    reBoo = true;
                    setbillAccBalInfo(billAccBal,
                            new BigDecimal(0),
                            billAccBal.getBillMoney(),
                            billAccBal.getBillMoney().negate(),
                            null,
                            AccebillConstant.AB_BILL_VALID_SIGN_0);
                } else if (AccebillConstant.AB_BILL_BUS_STATUS95.equals(status)
                        && AccebillConstant.AB_BILL_OPT_STATUS19.equals(optStatus)) {
                    reBoo = true;
                    if (StringUtils.isEmpty(accrecords.getCreateBy())
                            || (AccebillConstant.AB_BILL_SEND_SIGN_N.equals(accrecords.getSendSign())
                            && StringUtils.isEmpty(accrecords.getUpdateBy()))
                            || StringUtil.isNotEmpty(accrecords.getPdId())) {//添加银行持有回来系统自动完成的业务设置余额 或者 付款联动线下票据直接减少余额和可用余额 或者 票据池入池质押直接减少余额和可用余额
                        setbillAccBalInfo(billAccBal,
                                billAccBal.getBillMoney().negate(),
                                billAccBal.getBillMoney().negate(),
                                new BigDecimal(0),
                                AccebillConstant.AB_BILL_CD_SIGN_1,
                                AccebillConstant.AB_BILL_VALID_SIGN_1);
                    } else {
                        setbillAccBalInfo(billAccBal,
                                billAccBal.getBillMoney().negate(),
                                new BigDecimal(0),
                                billAccBal.getBillMoney().negate(),
                                AccebillConstant.AB_BILL_CD_SIGN_1,
                                AccebillConstant.AB_BILL_VALID_SIGN_1);
                    }
                }
            } else if (AccebillConstant.BRIEF_STATUS_6011.equals(briefId)) {
                //		8	6011	应收贴现申请
                //			业务状态：审批中 11 ，由业务处理
                //			可用余额 减少，余额 不变，占用金额 增加
                //			业务状态：打回 -2   （审批打回、办理打回） ，由业务处理
                //			可用余额 增加，余额 不变，占用金额 减少
                if (AccebillConstant.AB_BILL_BUS_STATUS11.equals(status)) {
                    reBoo = true;
                    setbillAccBalInfo(billAccBal,
                            new BigDecimal(0),
                            billAccBal.getBillMoney().negate(),
                            billAccBal.getBillMoney(),
                            AccebillConstant.AB_BILL_CD_SIGN_2,
                            AccebillConstant.AB_BILL_VALID_SIGN_1);
                } else if (AccebillConstant.AB_BILL_BUS_STATUS_2.equals(status)) {
                    reBoo = true;
                    setbillAccBalInfo(billAccBal,
                            new BigDecimal(0),
                            billAccBal.getBillMoney(),
                            billAccBal.getBillMoney().negate(),
                            null,
                            AccebillConstant.AB_BILL_VALID_SIGN_0);
                }
            } else if (AccebillConstant.BRIEF_STATUS_011.equals(briefId)) {
                //		9	011	应收贴现办理
                //			一：纸票、线下电票 ，由业务处理
                //			业务状态：审批通过95 指令状态：操作完成19
                //			可用余额 不变，余额 减少，占用金额 减少
                //			二：线上电票
                //			业务状态：审批通过95 指令状态：对方同意签收 19 ，由定时器业务处理
                //			可用余额 不变，余额 减少，占用金额 减少
                if (AccebillConstant.AB_BILL_BUS_STATUS95.equals(status)
                        && AccebillConstant.AB_BILL_OPT_STATUS19.equals(optStatus)) {
                    reBoo = true;
                    if (StringUtils.isEmpty(accrecords.getCreateBy())) {//添加银行持有回来系统自动完成的业务设置余额
                        setbillAccBalInfo(billAccBal,
                                billAccBal.getBillMoney().negate(),
                                billAccBal.getBillMoney().negate(),
                                new BigDecimal(0),
                                AccebillConstant.AB_BILL_CD_SIGN_1,
                                AccebillConstant.AB_BILL_VALID_SIGN_1);
                    } else {
                        setbillAccBalInfo(billAccBal,
                                billAccBal.getBillMoney().negate(),
                                new BigDecimal(0),
                                billAccBal.getBillMoney().negate(),
                                AccebillConstant.AB_BILL_CD_SIGN_1,
                                AccebillConstant.AB_BILL_VALID_SIGN_1);
                    }
                }
            }
            //质押申请和质押解除和背书业务保持一致、质押申请签收和质押解除签收和收票签收保持一致、质押收回和收票登记保持一致
//            else if (AccebillConstant.BRIEF_STATUS_018.equals(briefId)) {
//                //		11	018	应收质押申请
//                //			业务状态：审批中 11 ，由业务处理
//                //			可用余额 减少，余额 不变，占用金额 增加
//                //			业务状态：打回 -2（审批打回、指令打回） \-3 指令撤回\-4 指令拒签 ，由业务处理
//                //			可用余额 增加，余额 不变，占用金额 减少
//                //			票据质押至票据到期日 还未解除 ，定时器处理
//                //			--出质人处理
//                //			业务状态：审批通过95 指令状态：操作完成19
//                //			可用余额 不变，余额 减少，占用金额 减少
//                if (AccebillConstant.AB_BILL_BUS_STATUS11.equals(status)) {
//                    reBoo = true;
//                    setbillAccBalInfo(billAccBal,
//                            new BigDecimal(0),
//                            billAccBal.getBillMoney().negate(),
//                            billAccBal.getBillMoney(),
//                            AccebillConstant.AB_BILL_CD_SIGN_2,
//                            AccebillConstant.AB_BILL_VALID_SIGN_1);
//                } else if (AccebillConstant.AB_BILL_BUS_STATUS_2.equals(status)
//                        || (AccebillConstant.AB_BILL_BUS_STATUS95.equals(status)
//                        && (AccebillConstant.AB_BILL_OPT_STATUS_3.equals(optStatus)
//                        || AccebillConstant.AB_BILL_OPT_STATUS_4.equals(optStatus)))) {
//                    reBoo = true;
//                    setbillAccBalInfo(billAccBal,
//                            new BigDecimal(0),
//                            billAccBal.getBillMoney(),
//                            billAccBal.getBillMoney().negate(),
//                            null,
//                            AccebillConstant.AB_BILL_VALID_SIGN_0);
//                } else if (AccebillConstant.AB_BILL_BUS_STATUS95.equals(status)
//                        && AccebillConstant.AB_BILL_OPT_STATUS19.equals(optStatus)
////                        && accrecords.getVouInfo().getEndDate().getTime() < DateUtils.getDateYMD().getTime()
//                        ) {
//                    reBoo = true;
//                    //设置质押申请完成票据结束以后未赎回设置余额信息
//                    //billAccBal.setBalDate(DateUtils.addDay(accrecords.getVouInfo().getEndDate(), 1));
//                    billAccBal.setBriefId(AccebillConstant.BRIEF_STATUS_018);
//                    setbillAccBalInfo(billAccBal,
//                            billAccBal.getBillMoney().negate(),
//                            new BigDecimal(0),
//                            billAccBal.getBillMoney().negate(),
//                            AccebillConstant.AB_BILL_CD_SIGN_1,
//                            AccebillConstant.AB_BILL_VALID_SIGN_1);
//                }
//            } else if (AccebillConstant.BRIEF_STATUS_9018.equals(briefId)) {
//                //		12	9018	质押申请签收
//                //			票据质押至票据到期日 还未解除 ，定时器处理
//                //			--质权人处理
//                //			业务状态：审批通过95 指令状态：操作完成19
//                //			可用余额、余额 增加，占用金额 不变
//                if (AccebillConstant.AB_SGNUPMK_Y.equals(accrecords.getSignUpFlag())
//                        && AccebillConstant.AB_BILL_BUS_STATUS95.equals(status)
//                        && AccebillConstant.AB_BILL_OPT_STATUS19.equals(optStatus)
////                        && accrecords.getVouInfo().getEndDate().getTime() < DateUtils.getDateYMD().getTime()
//                        ) {
//                    reBoo = true;
//                    //billAccBal.setBalDate(DateUtils.addDay(accrecords.getVouInfo().getEndDate(), 1));
//                    setbillAccBalInfo(billAccBal,
//                            billAccBal.getBillMoney(),
//                            billAccBal.getBillMoney(),
//                            new BigDecimal(0),
//                            AccebillConstant.AB_BILL_CD_SIGN_0,
//                            AccebillConstant.AB_BILL_VALID_SIGN_1);
//                }
//            } else if (AccebillConstant.BRIEF_STATUS_6019.equals(briefId)
//                    || (AccebillConstant.AB_SGNUPMK_Y.equals(accrecords.getSignUpFlag()) &&
//                    AccebillConstant.BRIEF_STATUS_9019.equals(briefId)
//            )) {
//                //		13	9019	质押解除签收
//                //			业务状态：审批通过95 指令状态：操作完成19
//                //			可用余额 增加，余额 不变，占用金额 减少
//                //		14	6019	应收质押收回
//                //			业务状态：审批通过95 指令状态：操作完成19
//                //			可用余额 增加，余额 不变，占用金额 减少
//                if (AccebillConstant.AB_BILL_BUS_STATUS95.equals(status)
//                        && AccebillConstant.AB_BILL_OPT_STATUS19.equals(optStatus)) {
//                    reBoo = true;
//                    billAccBal.setBriefId(briefId);
////                    if (accrecords.getVouInfo().getEndDate().getTime() < DateUtils.getDateYMD().getTime()) {
//                        setbillAccBalInfo(billAccBal,
//                                billAccBal.getBillMoney(),
//                                billAccBal.getBillMoney(),
//                                new BigDecimal(0),
//                                AccebillConstant.AB_BILL_CD_SIGN_0,
//                                AccebillConstant.AB_BILL_VALID_SIGN_1);
////                    } else {
////                        setbillAccBalInfo(billAccBal,
////                                new BigDecimal(0),
////                                billAccBal.getBillMoney(),
////                                billAccBal.getBillMoney().negate(),
////                                null,
////                                null);
////                    }
//                }
//            }
            else if (AccebillConstant.BRIEF_STATUS_020.equals(briefId)
                    || AccebillConstant.BRIEF_STATUS_021.equals(briefId)) {
                //		15	020	应收托收申请
                //			业务状态：审批中 11 ，由业务处理
                //			可用余额 减少，余额 不变，占用金额 增加
                //			业务状态：打回 -2   （审批打回、指令发送打回） ，由业务处理
                //			可用余额 增加，余额 不变，占用金额 减少
                //			一：纸票、线下电票 ，由业务处理
                //			业务状态：审批通过95 指令状态：操作完成19
                //			可用余额 不变，余额 减少，占用金额 减少
                //			二：线上电票
                //			业务状态：审批通过95 指令状态：\-3 指令撤回\-4 指令拒签 ，由定时器业务处理
                //			可用余额 增加，余额 不变，占用金额 减少
                //			业务状态：审批通过95 指令状态：对方同意签收 19 ，由定时器业务处理
                //			可用余额 不变，余额 减少，占用金额 减少
                //		16	021	应收逾期托收申请
                //			业务状态：审批中 11 ，由业务处理
                //			可用余额 减少，余额 不变，占用金额 增加
                //			业务状态：打回 -2   （审批打回、指令发送打回） ，由业务处理
                //			可用余额 增加，余额 不变，占用金额 减少
                //			一：纸票、线下电票 ，由业务处理
                //			业务状态：审批通过95 指令状态：操作完成19
                //			可用余额 不变，余额 减少，占用金额 减少
                //			二：线上电票
                //			业务状态：审批通过95 指令状态：\-3 指令撤回\-4 指令拒签，由定时器业务处理
                //			可用余额 增加，余额 不变，占用金额 减少
                //			业务状态：审批通过95 指令状态：对方同意签收 19 ，由定时器业务处理
                //			可用余额 不变，余额 减少，占用金额 减少
                if (AccebillConstant.AB_BILL_BUS_STATUS11.equals(status)) {
                    reBoo = true;
                    setbillAccBalInfo(billAccBal,
                            new BigDecimal(0),
                            billAccBal.getBillMoney().negate(),
                            billAccBal.getBillMoney(),
                            AccebillConstant.AB_BILL_CD_SIGN_2,
                            AccebillConstant.AB_BILL_VALID_SIGN_1);
                } else if (AccebillConstant.AB_BILL_BUS_STATUS_2.equals(status)
                        || (AccebillConstant.AB_BILL_BUS_STATUS95.equals(status)
                        && (AccebillConstant.AB_BILL_OPT_STATUS_3.equals(optStatus)
                        || AccebillConstant.AB_BILL_OPT_STATUS_4.equals(optStatus)))) {
                    reBoo = true;
                    setbillAccBalInfo(billAccBal,
                            new BigDecimal(0),
                            billAccBal.getBillMoney(),
                            billAccBal.getBillMoney().negate(),
                            null,
                            AccebillConstant.AB_BILL_VALID_SIGN_0);
                } else if (AccebillConstant.AB_BILL_BUS_STATUS95.equals(status)
                        && AccebillConstant.AB_BILL_OPT_STATUS19.equals(optStatus)) {
                    reBoo = true;
                    if (StringUtils.isEmpty(accrecords.getCreateBy())) {//添加银行持有回来系统自动完成的业务设置余额
                        setbillAccBalInfo(billAccBal,
                                billAccBal.getBillMoney().negate(),
                                billAccBal.getBillMoney().negate(),
                                new BigDecimal(0),
                                AccebillConstant.AB_BILL_CD_SIGN_1,
                                AccebillConstant.AB_BILL_VALID_SIGN_1);
                    } else {
                        setbillAccBalInfo(billAccBal,
                                billAccBal.getBillMoney().negate(),
                                new BigDecimal(0),
                                billAccBal.getBillMoney().negate(),
                                AccebillConstant.AB_BILL_CD_SIGN_1,
                                AccebillConstant.AB_BILL_VALID_SIGN_1);
                    }
                }
            } else if (AccebillConstant.BRIEF_STATUS_9023.equals(briefId)) {
                //		17	9023	追索同意清偿签收
                //			线上电票，由定时器自动触发
                //			业务状态：审批通过95 指令状态：操作完成19 且同意签收
                //			可用余额、余额 减少，占用金额 不变
                if (AccebillConstant.AB_SGNUPMK_Y.equals(accrecords.getSignUpFlag())
                        && AccebillConstant.AB_BILL_BUS_STATUS95.equals(status)
                        && AccebillConstant.AB_BILL_OPT_STATUS19.equals(optStatus)) {
                    reBoo = true;
                    setbillAccBalInfo(billAccBal,
                            billAccBal.getBillMoney().negate(),
                            billAccBal.getBillMoney().negate(),
                            new BigDecimal(0),
                            AccebillConstant.AB_BILL_CD_SIGN_1,
                            AccebillConstant.AB_BILL_VALID_SIGN_1);
                }
            } else if (AccebillConstant.BRIEF_STATUS_6004.equals(briefId)) {
                //		18	6004	应收退票
                //			业务状态：审批中 11 ，由业务处理
                //			可用余额 减少，余额 不变，占用金额 增加
                //			业务状态：打回 -2
                //			可用余额 增加，余额 不变，占用金额 减少
                //			业务状态：审批通过95 指令状态：操作完成19
                //			可用余额 不变，余额 减少，占用金额 减少
                if (AccebillConstant.AB_BILL_BUS_STATUS11.equals(status)) {
                    reBoo = true;
                    setbillAccBalInfo(billAccBal,
                            new BigDecimal(0),
                            billAccBal.getBillMoney().negate(),
                            billAccBal.getBillMoney(),
                            AccebillConstant.AB_BILL_CD_SIGN_2,
                            AccebillConstant.AB_BILL_VALID_SIGN_1);
                } else if (AccebillConstant.AB_BILL_BUS_STATUS_2.equals(status)) {
                    reBoo = true;
                    setbillAccBalInfo(billAccBal,
                            new BigDecimal(0),
                            billAccBal.getBillMoney(),
                            billAccBal.getBillMoney().negate(),
                            null,
                            AccebillConstant.AB_BILL_VALID_SIGN_0);
                } else if (AccebillConstant.AB_BILL_BUS_STATUS95.equals(status)
                        && AccebillConstant.AB_BILL_OPT_STATUS19.equals(optStatus)) {
                    reBoo = true;
                    setbillAccBalInfo(billAccBal,
                            billAccBal.getBillMoney().negate(),
                            new BigDecimal(0),
                            billAccBal.getBillMoney().negate(),
                            AccebillConstant.AB_BILL_CD_SIGN_1,
                            AccebillConstant.AB_BILL_VALID_SIGN_1);
                }
            } else if (AccebillConstant.BRIEF_STATUS_012.equals(briefId)) {
                //		19	012	贴现赎回申请
                //			业务状态：审批中 11
                //			可用余额 减少，余额 不变，占用金额 增加
                //			业务状态：打回 -2
                //			可用余额 增加，余额 不变，占用金额 减少
                //			业务状态：审批通过95 指令状态：操作完成19
                //			可用余额 不变，余额 减少，占用金额 减少
                //			内部业务自动收回 （体现在贴现收回中）
                //			业务状态：审批通过95 指令状态：操作完成19
                //			可用余额、余额 增加，占用金额 不变
                if (AccebillConstant.AB_BILL_BUS_STATUS11.equals(status)) {
                    reBoo = true;
                    setbillAccBalInfo(billAccBal,
                            new BigDecimal(0),
                            billAccBal.getBillMoney().negate(),
                            billAccBal.getBillMoney(),
                            AccebillConstant.AB_BILL_CD_SIGN_2,
                            AccebillConstant.AB_BILL_VALID_SIGN_1);
                } else if (AccebillConstant.AB_BILL_BUS_STATUS_2.equals(status)) {
                    reBoo = true;
                    setbillAccBalInfo(billAccBal,
                            new BigDecimal(0),
                            billAccBal.getBillMoney(),
                            billAccBal.getBillMoney().negate(),
                            null,
                            AccebillConstant.AB_BILL_VALID_SIGN_0);
                } else if (AccebillConstant.AB_BILL_BUS_STATUS95.equals(status)
                        && AccebillConstant.AB_BILL_OPT_STATUS19.equals(optStatus)) {
                    reBoo = true;
                    setbillAccBalInfo(billAccBal,
                            billAccBal.getBillMoney().negate(),
                            new BigDecimal(0),
                            billAccBal.getBillMoney().negate(),
                            AccebillConstant.AB_BILL_CD_SIGN_1,
                            AccebillConstant.AB_BILL_VALID_SIGN_1);
                }
            }
        } else if (AccebillConstant.BILLBAL_OP_BUSTYPE_TRANSFERDOWN11.equals(busType)) {
            reBoo = true;
            setbillAccBalInfo(billAccBal,
                    new BigDecimal(0),
                    billAccBal.getBillMoney().negate(),
                    billAccBal.getBillMoney(),
                    AccebillConstant.AB_BILL_CD_SIGN_2,
                    AccebillConstant.AB_BILL_VALID_SIGN_1);
        } else if (AccebillConstant.BILLBAL_OP_BUSTYPE_TRANSFERDOWN_1.equals(busType)) {
            reBoo = true;
            billAccBal.setMoney(new BigDecimal(0));
            billAccBal.setAvailMoney(billAccBal.getBillMoney());
            billAccBal.setTiedupMoney(billAccBal.getBillMoney().negate());
            setbillAccBalInfo(billAccBal,
                    new BigDecimal(0),
                    billAccBal.getBillMoney(),
                    billAccBal.getBillMoney().negate(),
                    null,
                    AccebillConstant.AB_BILL_VALID_SIGN_0);
        }
        return reBoo;
    }
    /**
     * 设置余额的基本要素 可用余额、实际余额、冻结余额、明细借贷标记、可用标记
     *
     * @param billAccBal  余额对象vo
     * @param money       全部余额
     * @param availMoney  可用余额
     * @param tiedupMoney 占用金额
     * @param cdSign      流水标志0借1贷2冻结
     * @param validSign   有效标记0无效1有效
     */
    private void setbillAccBalInfo(AbBillAccBalVO billAccBal, BigDecimal money, BigDecimal availMoney, BigDecimal tiedupMoney, String cdSign, String validSign) {
        billAccBal.setMoney(money);
        billAccBal.setAvailMoney(availMoney);
        billAccBal.setTiedupMoney(tiedupMoney);
        billAccBal.setCdSign(cdSign);
        billAccBal.setValidSign(validSign);
        //对银票余额和商票余额进行设置
        if (AccebillConstant.AB_BILL_TYPE_BANK.equals(String.valueOf(billAccBal.getBillType()))) {
            billAccBal.setYpMoney(billAccBal.getMoney());
            billAccBal.setYpAvailMoney(billAccBal.getAvailMoney());
            billAccBal.setYpTiedupMoney(billAccBal.getTiedupMoney());
        } else {
            billAccBal.setSpMoney(billAccBal.getMoney());
            billAccBal.setSpAvailMoney(billAccBal.getAvailMoney());
            billAccBal.setSpTiedupMoney(billAccBal.getTiedupMoney());
        }
    }
    public void setBillAccBalIsNullTo0(AbBillAccBalVO billAccBal) {
        if (billAccBal.getMoney() == null) {
            billAccBal.setMoney(new BigDecimal(0));
        }
        if (billAccBal.getAvailMoney() == null) {
            billAccBal.setAvailMoney(new BigDecimal(0));
        }
        if (billAccBal.getTiedupMoney() == null) {
            billAccBal.setTiedupMoney(new BigDecimal(0));
        }
        if (billAccBal.getSpMoney() == null) {
            billAccBal.setSpMoney(new BigDecimal(0));
        }
        if (billAccBal.getSpAvailMoney() == null) {
            billAccBal.setSpAvailMoney(new BigDecimal(0));
        }
        if (billAccBal.getSpTiedupMoney() == null) {
            billAccBal.setSpTiedupMoney(new BigDecimal(0));
        }
        if (billAccBal.getYpMoney() == null) {
            billAccBal.setYpMoney(new BigDecimal(0));
        }
        if (billAccBal.getYpAvailMoney() == null) {
            billAccBal.setYpAvailMoney(new BigDecimal(0));
        }
        if (billAccBal.getYpTiedupMoney() == null) {
            billAccBal.setYpTiedupMoney(new BigDecimal(0));
        }
    }
    /**
     * 执行余额设置功能
     *
     * @param billAccBal 余额信息
     */
    private void opBankAccBillBal(AbBillAccBalVO billAccBal, ICurrentUser userVo) {
        //初始一般数据
        abBillAccBalVOInit(billAccBal, null, userVo);
        //创建左边缺失的余额设置
        createBankAccBillBalByLeft(billAccBal, userVo);
        //创建右边缺失的余额设置
        createBankAccBillBalByRight(billAccBal, userVo);
//		Date tadyDate=DateUtils.getDateYMD();
        billAccBal.setBeginDate(billAccBal.getBalDate());
//		if(tadyDate.getTime()<billAccBal.getBalDate().getTime()){
//			billAccBal.setEndDate(billAccBal.getBalDate());
//		}else{
//			billAccBal.setEndDate(tadyDate);
//		}
        updateBankAccBillBal(billAccBal, userVo);
    }
    /**
     * 创建左边空余的余额
     *
     * @param billAccBal 余额信息
     * @return 返回新增条数
     */
    private int createBankAccBillBalByLeft(AbBillAccBalVO billAccBal, ICurrentUser userVo) {
        int size = 0;
        //获取业务日期往前最近一天的余额
        AbBillAccBal leftAccBal = abBillAccBalDao.getAbBillAccBalLeft1ByBalDate(billAccBal.getBankId(), billAccBal.getCorpId(), billAccBal.getBalDate());
        if (leftAccBal == null) {//如果没有数据表示只需要设置余额日期的余额就OK
            billAccBal.setBeginDate(billAccBal.getBalDate());
        } else {
            //如果有数据且日期和余额日期不一样要设置此数据的日期后一天到余额日期的余额
            billAccBal.setBeginDate(DateUtils.addDay(leftAccBal.getBalDate(), 1));
            billAccBal.setBal(leftAccBal.getBal());
            billAccBal.setAvailBal(leftAccBal.getAvailBal());
            billAccBal.setTiedupBal(leftAccBal.getTiedupBal());
            billAccBal.setYpBal(leftAccBal.getYpBal());
            billAccBal.setYpAvailBal(leftAccBal.getYpAvailBal());
            billAccBal.setYpTiedupBal(leftAccBal.getYpTiedupBal());
            billAccBal.setSpBal(leftAccBal.getSpBal());
            billAccBal.setSpAvailBal(leftAccBal.getSpAvailBal());
            billAccBal.setSpTiedupBal(leftAccBal.getSpTiedupBal());
        }
        billAccBal.setEndDate(billAccBal.getBalDate());
        //判断结束日期只需要满足大于等于开始日期就可以创建余额
        if (billAccBal.getBeginDate().getTime() <= billAccBal.getEndDate().getTime()) {
            size = createBankAccBillBal(billAccBal, userVo);
        }
        return size;
    }
    /**
     * 创建右边空余的余额
     *
     * @param billAccBal 余额信息
     * @return 返回新增条数
     */
    private int createBankAccBillBalByRight(AbBillAccBalVO billAccBal, ICurrentUser userVo) {
        int size = 0;
        Date tadyDate = DateUtils.getDateYMD();
        if (billAccBal.getBalDate().getTime() < tadyDate.getTime()) {
            //获取业务日期往后最近一天的余额
            AbBillAccBal rightAccBal = abBillAccBalDao.getAbBillAccBalRight1ByBalDate(billAccBal.getBankId(), billAccBal.getCorpId(), billAccBal.getBalDate());
            if (rightAccBal == null) {
                billAccBal.setEndDate(tadyDate);
            } else {
                billAccBal.setEndDate(DateUtils.addDay(rightAccBal.getBalDate(), -1));
            }
            billAccBal.setBeginDate(DateUtils.addDay(billAccBal.getBalDate(), 1));
            //判断结束日期只需要满足大于等于开始日期就可以创建余额
            if (billAccBal.getBeginDate().getTime() <= billAccBal.getEndDate().getTime()) {
                size = createBankAccBillBal(billAccBal, userVo);
            }
        }
        return size;
    }
    /**
     * 批量创建单个账户的账户余额
     *
     * @param billAccBal
     * @return
     */
    private int createBankAccBillBal(AbBillAccBalVO billAccBal, ICurrentUser userVo) {
        //冗余字段新增
        CorpDTO dto=iCorpService.get(billAccBal.getCorpId());
        billAccBal.setCorpCode(dto.getCode());
        billAccBal.setCorpName(dto.getName());
        int size = 0;
        if (billAccBal != null
                && billAccBal.getBeginDate() != null
                && billAccBal.getEndDate() != null) {
            abBillAccBalVOInit(billAccBal, AccebillConstant.BILL_OPERATE_ADD, userVo);
            Date balDate = billAccBal.getBeginDate();
            List<AbBillAccBal> accBalList = new ArrayList<>();
            while (balDate.getTime() <= billAccBal.getEndDate().getTime()) {
                AbBillAccBal accBalInfo = new AbBillAccBal();
                BeanUtils.copyNotNullProperties(billAccBal, accBalInfo);
                accBalInfo.setBalDate(DateUtils.getDateYMD(balDate));
                accBalInfo.setCurId(billAccBal.getCurId());
                accBalList.add(accBalInfo);
                balDate = DateUtils.addDay(balDate, 1);
            }
            if (!accBalList.isEmpty()) {
                size = abBillAccBalDao.batchSave(accBalList);
                abBillAccBalDao.flush();
            }
        }
        return size;
    }
    /**
     * 修改时间区间的余额
     *
     * @param billAccBal
     * @return
     */
    private int updateBankAccBillBal(AbBillAccBalVO billAccBal, ICurrentUser userVo) {
        int size = 0;
        if (billAccBal != null
                && billAccBal.getBeginDate() != null
                && billAccBal.getEndDate() != null) {
            abBillAccBalVOInit(billAccBal, AccebillConstant.BILL_OPERATE_UPDATE, userVo);
            Date beginDate = DateUtils.getDateYMD(billAccBal.getBeginDate());
//			if(beginDate.getTime()<=billAccBal.getEndDate().getTime()
            if (beginDate != null
                    && (billAccBal.getMoney().doubleValue() != 0
                    || billAccBal.getAvailMoney().doubleValue() != 0
                    || billAccBal.getTiedupMoney().doubleValue() != 0)) {
                size = abBillAccBalDao.updateBankAccBillBal(billAccBal);
                abBillAccBalDao.flush();
            }
        }
        return size;
    }
    /**
     * 初始化余额基础信息
     *
     * @param billAccBal
     * @param operate    初始化类型AccebillConstant.BILL_OPERATE_ADD,AccebillConstant.BILL_OPERATE_UPDATE
     */
    @Transactional(readOnly = true)
    public void abBillAccBalVOInit(AbBillAccBalVO billAccBal, String operate, ICurrentUser userVo) {
        if (billAccBal.getBankAccDTO() == null) {
            if (StringUtils.isNotEmpty(billAccBal.getBankAcc())) {
                billAccBal.setBankAccDTO(abAccrecordsService.getBtBankAccDTO(billAccBal.getBankAcc()));
            } else {
                String curId = abVoucherinfoViceService.getCurIdByCNY();
                BtBankAccDTO bankAccDTO = new BtBankAccDTO();
                bankAccDTO.setBankAcc(AccebillConstant.AB_BILL_ACCNO_DEFAULT);
                bankAccDTO.setCorpId(billAccBal.getCorpId());
                bankAccDTO.setCurId(curId);
                billAccBal.setBankAccDTO(bankAccDTO);
            }
        }
        if (billAccBal.getBankAccDTO() != null) {//基本信息根据账户信息初始化
            billAccBal.setBankId(billAccBal.getBankAccDTO().getId());
            billAccBal.setBankAcc(billAccBal.getBankAccDTO().getBankAcc());
            if (EmptyUtils.isNotEmpty(billAccBal.getBankAccDTO().getBtBankAccCurDTOList())) {
                billAccBal.setCurId(billAccBal.getBankAccDTO().getBtBankAccCurDTOList().get(0).getCurId());
            } else {
                billAccBal.setCurId(billAccBal.getBankAccDTO().getCurId());
            }
            billAccBal.setCorpId(billAccBal.getBankAccDTO().getCorpId());
        }
        if (AccebillConstant.BILL_OPERATE_ADD.equals(operate)) {//创建初始化
            if (StringUtils.isEmpty(billAccBal.getCreateBy())
                    && userVo != null) {
                billAccBal.setCreateBy(userVo.getLoginName());
            }
            if (billAccBal.getCreateDate() == null) {
                billAccBal.setCreateDate(new Date());
            }
        } else if (AccebillConstant.BILL_OPERATE_UPDATE.equals(operate)) {//修改初始化
            if (StringUtils.isEmpty(billAccBal.getUpdateBy())
                    && userVo != null) {
                billAccBal.setUpdateBy(userVo.getLoginName());
            }
            if (billAccBal.getUpdateDate() == null) {
                billAccBal.setUpdateDate(new Date());
            }
            setBillAccBalIsNullTo0(billAccBal);
        }
        //余额为空初始化
        if (billAccBal.getBal() == null) {
            billAccBal.setBal(new BigDecimal(0));
        }
        if (billAccBal.getAvailBal() == null) {
            billAccBal.setAvailBal(new BigDecimal(0));
        }
        if (billAccBal.getTiedupBal() == null) {
            billAccBal.setTiedupBal(new BigDecimal(0));
        }
        if (billAccBal.getYpBal() == null) {
            billAccBal.setYpBal(new BigDecimal(0));
        }
        if (billAccBal.getYpAvailBal() == null) {
            billAccBal.setYpAvailBal(new BigDecimal(0));
        }
        if (billAccBal.getYpTiedupBal() == null) {
            billAccBal.setYpTiedupBal(new BigDecimal(0));
        }
        if (billAccBal.getSpBal() == null) {
            billAccBal.setSpBal(new BigDecimal(0));
        }
        if (billAccBal.getSpAvailBal() == null) {
            billAccBal.setSpAvailBal(new BigDecimal(0));
        }
        if (billAccBal.getSpTiedupBal() == null) {
            billAccBal.setSpTiedupBal(new BigDecimal(0));
        }
    }
    /**
     * 自动提示收票定时任务触发执行
     */
    @Override
    @XxlJob("AbBillAccBalService")
    public void run(String arguments) {
        arguments = XxlJobHelper.getJobParam();
        logger.info("定时任务接收参数：【{}】", arguments);
        try {
            ICurrentUser userVo = abUserService.getScheduler();
            if (AccebillConstant.ACCEBILL_TASK_AUTOTODAYBILLACCBAL.equals(arguments)) {
                this.logger.info("---自动设置当天票据账户余额，开始执行定时任务---");
                autoTodayBillAccBal(userVo);
                this.logger.info("---自动设置当天票据账户余额，结束执行定时任务---");
            } else if (AccebillConstant.ACCEBILL_TASK_ELECTBUSBILLACCBAL.equals(arguments)) {
                this.logger.info("---设置电票已完成的业务进行票据账户余额处理，开始执行定时任务---");
                setElectBusBillAccBal(userVo);
                this.logger.info("---设置电票已完成的业务进行票据账户余额处理，结束执行定时任务---");
            } else if (AccebillConstant.ACCEBILL_TASK_PLEDGEBUSBILLACCBAL.equals(arguments)) {
                //修改质押余额设置 质押申请完成余额就扣减 质押签收余额就叠加（修改之前质押余额要票据到期才k算设计）
//                this.logger.info("---设置质押未解除已到期截止票据账户余额处理，开始执行定时任务---");
//                setPledgeBusBillAccBal(null);
//                this.logger.info("---设置质押未解除已到期截止票据账户余额处理，结束执行定时任务---");
            }
        } catch (Exception e) {
            this.logger.error(e.getMessage(), e);
            throw new RuntimeException(e);
        }
    }
    /**
     * 设置当天票据账户余额处理
     */
    private void autoTodayBillAccBal(ICurrentUser userVo) {
        Date today = new Date();
        String curId = abVoucherinfoViceService.getCurIdByCNY();
        BtBankAccDTO dto = new BtBankAccDTO();
        dto.setCurId(curId);
        //查询余额有账户数据
        List<String> accList = abBillAccBalDao.getBankAccListHasAcc();
        if (accList != null && !accList.isEmpty()) {
            this.logger.info("---自动设置当天票据账户余额，账户总数为【" + accList.size() + "】---");
            for (int i = 0; i < accList.size(); i++) {
                dto.setBankAcc(accList.get(i));
                if (abBtBankAccService.getBankAccList(dto) == null || abBtBankAccService.getBankAccList(dto).isEmpty()) {
                    continue;
                }
                BtBankAccDTO accInfo = abBtBankAccService.getBankAccList(dto).get(0);
                //设置账户余额信息
                AbBillAccBalVO billAccBal = new AbBillAccBalVO();
                billAccBal.setBankAcc(accInfo.getBankAcc());
                billAccBal.setBalDate(today);
                billAccBal.setBankAccDTO(accInfo);
                //初始一般数据
                abBillAccBalVOInit(billAccBal, null, userVo);
                //创建左边缺失的余额设置
                createBankAccBillBalByLeft(billAccBal, userVo);
            }
        }
        //查询余额无账户数据
        List<String> corpIdList = abBillAccBalDao.getCorpIdListNotAcc();
        if (corpIdList != null && !corpIdList.isEmpty()) {
            this.logger.info("---自动设置当天票据账户余额，无账户单位总数为【" + corpIdList.size() + "】---");
            for (int i = 0; i < corpIdList.size(); i++) {
                BtBankAccDTO accInfo = new BtBankAccDTO();
                accInfo.setCorpId(corpIdList.get(i));
                accInfo.setCurId(curId);
                accInfo.setBankAcc(AccebillConstant.AB_BILL_ACCNO_DEFAULT);
                //设置账户余额信息
                AbBillAccBalVO billAccBal = new AbBillAccBalVO();
                billAccBal.setBankAcc(accInfo.getBankAcc());
                billAccBal.setBalDate(today);
                billAccBal.setBankAccDTO(accInfo);
                //初始一般数据
                abBillAccBalVOInit(billAccBal, null, userVo);
                //创建左边缺失的余额设置
                createBankAccBillBalByLeft(billAccBal, userVo);
            }
        }
    }
    /**
     * 设置电票已完成的业务进行票据账户余额处理
     */
    private void setElectBusBillAccBal(ICurrentUser userVo) {
        List<AbAccrecordsVO> accrecordsList = abAccrecordsService.getElectBusBillInfoByBal();
        if (accrecordsList != null && !accrecordsList.isEmpty()) {
            this.logger.info("---设置电票已完成的业务进行票据账户余额处理，总数为【" + accrecordsList.size() + "】---");
            setBusBillAccBal(accrecordsList, userVo);
        } else {
            this.logger.info("---设置电票已完成的业务进行票据账户余额处理，总数为【0】不处理---");
        }
    }
    /**
     * 设置质押未解除已到期截止票据账户余额处理
     */
    private void setPledgeBusBillAccBal(ICurrentUser userVo) {
        List<AbAccrecordsVO> accrecordsList = abAccrecordsService.getPledgeBusBillInfoByBal();
        if (accrecordsList != null && !accrecordsList.isEmpty()) {
            this.logger.info("---设置质押未解除已到期截止票据账户余额处理，总数*为【" + accrecordsList.size() + "】---");
            setBusBillAccBal(accrecordsList, userVo);
        } else {
            this.logger.info("---设置质押未解除已到期截止票据账户余额处理，总数为【0】不处理---");
        }
    }
    /**
     * 进行票据业务处理
     *
     * @param accrecordsList
     */
    private void setBusBillAccBal(List<AbAccrecordsVO> accrecordsList, ICurrentUser userVo) {
        String[] idArray = new String[accrecordsList.size()];
        for (int i = 0; i < accrecordsList.size(); i++) {
            AbAccrecordsVO accrecordsInfo = accrecordsList.get(i);
            String accrecordsId = accrecordsInfo.getId();
            setBankAccBillBal(accrecordsId, userVo);
            idArray[i] = accrecordsId;
        }
        AbAccrecordsVO accRecordsVO = new AbAccrecordsVO();
        accRecordsVO.setIdArray(idArray);
        accRecordsVO.setCdtRatgs("1");//借用此字段用来标记已设置过余额
        abAccrecordsService.batchUpdate(accRecordsVO, userVo);
    }
    /**
     * 根据票据余额VO生成票据余额明细表数据
     *
     * @param billAccBal
     */
    private void opBillAccDtl(AbBillAccBalVO billAccBal, ICurrentUser user) {
        if (StringUtils.isNotEmpty(billAccBal.getRecordId())) {
            AbBillAccDtl accDtl = abBillAccDtlDao.getAbBillAccDtlByRecordIdAndValid(billAccBal.getRecordId());
            if (accDtl == null) {
                saveBillAccDtl(billAccBal, user);
            } else {
                updateBillAccDtl(accDtl, billAccBal, user);
            }
//            if (StringUtils.isEmpty(billAccBal.getBriefId())) {
//                AbBillAccDtl accDtl = abBillAccDtlDao.getAbBillAccDtlByRecordIdAndValid(billAccBal.getRecordId());
//                if (accDtl == null) {
//                    saveBillAccDtl(billAccBal, user);
//                } else {
//                    updateBillAccDtl(accDtl, billAccBal, user);
//                }
//            } else if (AccebillConstant.BRIEF_STATUS_018.equals(billAccBal.getBriefId())) {
//                updateBillAccDtlSetThawTime(billAccBal, billAccBal.getRecordId(), user);
//                saveBillAccDtl(billAccBal, user);
//            } else if (AccebillConstant.BRIEF_STATUS_6019.equals(billAccBal.getBriefId())
//                    || AccebillConstant.BRIEF_STATUS_9019.equals(billAccBal.getBriefId())) {
//                if (StringUtils.isNotEmpty(billAccBal.getCdSign())
//                        && StringUtils.isNotEmpty(billAccBal.getValidSign())) {
//                    //质押收回、解除质押签收业务票据已到期的需要维护收入明细即可
//                    saveBillAccDtl(billAccBal, user);
//                } else {
//                    //质押收回、解除质押签收业务票据未到期的需要设置之前锁定的明细设置冻结解除日期即可
//                    String upRecordId = abAccrecordsService.getUpBusinessByRecordId(billAccBal.getRecordId());
//                    updateBillAccDtlSetThawTime(billAccBal, upRecordId, user);
//                }
//            }
        }
    }
    /**
     * 设置余额明细为冻结明细时修改明细数据
     *
     * @param billAccBal 余额操作VO
     * @param recordId   余额对应操作票据流水
     * @param user       操作用户信息
     */
    private void updateBillAccDtlSetThawTime(AbBillAccBalVO billAccBal, String recordId, ICurrentUser user) {
        AbBillAccDtl accDtl = abBillAccDtlDao.getAbBillAccDtlByRecordIdAndValid(recordId);
        if (accDtl != null && AccebillConstant.AB_BILL_CD_SIGN_2.equals(accDtl.getCdSign())) {
            //设置冻结解除时间
            accDtl.setThawTime(billAccBal.getBalDate());
            if (accDtl.getTransTime().getTime() == accDtl.getThawTime().getTime()) {
                //如果冻结交易日期和解冻日期是一天这余额流水无意义标志失效
                accDtl.setValidSign(AccebillConstant.AB_BILL_VALID_SIGN_0);
            } else {
                //质押申请业务完成、质押收回、质押解除签收要保留冻结记录 其他业务不需要处理
                accDtl.setValidSign(null);
            }
            updateBillAccDtl(accDtl, billAccBal, user);
        }
    }
    /**
     * 新增余额明细信息
     *
     * @param billAccBal 余额信息VO
     * @param user       操作用户
     */
    private void saveBillAccDtl(AbBillAccBalVO billAccBal, ICurrentUser user) {
        AbBillAccDtl accDtl = new AbBillAccDtl();
        accDtl.setRecordId(billAccBal.getRecordId());
        accDtl.setTransTime(billAccBal.getBalDate());
        accDtl.setMoney(billAccBal.getBillMoney());
        if (user != null && StringUtils.isNotEmpty(user.getId())) {
            accDtl.setCreateBy(user.getId());
        }
        accDtl.setCreateDate(new Date());
        accDtl.setValidSign(billAccBal.getValidSign());
        if (StringUtils.isNotEmpty(billAccBal.getCdSign())) {
            accDtl.setCdSign(billAccBal.getCdSign());
        }
        abBillAccDtlDao.save(accDtl);
    }
    /**
     * 新增余额明细信息
     *
     * @param accDtl     明细信息
     * @param billAccBal 余额信息VO
     * @param user       操作用户
     */
    private void updateBillAccDtl(AbBillAccDtl accDtl, AbBillAccBalVO billAccBal, ICurrentUser user) {
        if (user != null && StringUtils.isNotEmpty(user.getId())) {
            accDtl.setUpdateBy(user.getId());
        }
        accDtl.setUpdateDate(new Date());
        if (StringUtils.isNotEmpty(billAccBal.getValidSign())) {
            accDtl.setValidSign(billAccBal.getValidSign());
        }
        if (StringUtils.isNotEmpty(billAccBal.getCdSign())) {
            accDtl.setCdSign(billAccBal.getCdSign());
        }
        abBillAccDtlDao.update(accDtl);
    }
    /**
     * 票据账户余额重新生成操作
     *
     * @param corpIds      需要重新生成的单位
     * @param beginDateStr 重新生成余额的开始日期
     * @param user         操作用户
     * @param isBalDate    是否是指定日期重置
     */
    public void resetAbBillAccBal(String corpIds, String beginDateStr, ICurrentUser user, boolean isBalDate) {
        Map<String, Object> params = new HashMap<>();
        List<String> corpAuthList = abCorpService.getUserCorpIds(AccebillConstant.AB_BILL_ACCBAL_RESET_CORPOP, user.getId());
        List<String> corpList = null;
        if (StringUtils.isNotEmpty(corpIds)) {
            corpList = new ArrayList<>();
            //设置查询单位和有权限单位取并集
            String[] corpIdArray = corpIds.split(",");
            for (String corpId : corpIdArray) {
                if (StringUtils.isNotEmpty(corpId)
                        && corpAuthList.contains(corpId)) {
                    corpList.add(corpId);
                }
            }
        } else {
            corpList = corpAuthList;
        }
        //没有授权单位不需要做余额初始化功能
        if (ListUtils.isNullOrEmpty(corpList)) {
            return;
        }
        Date beginDate = null;
        if (StringUtils.isNotEmpty(beginDateStr)) {
            beginDate = DateUtils.str2Date(beginDateStr, DateUtils.date_sdf());
            params.put("beginDate", beginDate);
        }
        //1先删除旧的余额数据
        int size = 0;
        if (isBalDate) {
            size = abBillAccBalDao.deleteAbBillAccBalByBalDate(params, corpList);
        } else {
            size = abBillAccBalDao.deleteAbBillAccBal(params, corpList);
        }
        List<AbBillAccDtlBalVO> accBalDtlVoList = abBillAccBalDao.listAbBillAccDtlBal(params, corpList);
        if (accBalDtlVoList.size() > 0) {
            //处理SQL中的 从 bt_bank_acc ba  取 ba.id as bankAccId
            List<String> bankAccList = accBalDtlVoList.stream().map(vo -> vo.getBankAcc()).collect(Collectors.toList());
            List<Map<String, Object>> bankModelByBankAcc = abBtBankAccService.getBankModelByBankAcc(bankAccList);
            Map<String, String> bankAccAndIdMap = Maps.newHashMap();
            if (ObjectUtil.isNotEmpty(bankModelByBankAcc)) {
                for (Map<String, Object> objectMap : bankModelByBankAcc) {
                    if (ObjectUtil.isNotNull(objectMap.get("bankAcc"))) {
                        bankAccAndIdMap.put(String.valueOf(objectMap.get("bankAcc")), String.valueOf(objectMap.get("id")));
                    }
                }
            }
            if (!bankAccAndIdMap.isEmpty()) {
                for (AbBillAccDtlBalVO vo : accBalDtlVoList) {
                    if (bankAccAndIdMap.containsKey(vo.getBankAcc())) {
                        vo.setBankAccId(bankAccAndIdMap.get(vo.getBankAcc()));
                    }
                }
            }
            Map<String, List<AbBillAccDtlBalVO>> corpMapAccBalDtlVoList = getCorpMapAccBalDtlVoList(accBalDtlVoList);
            if (ListUtils.isNotNullOrEmpty(accBalDtlVoList)) {
                if (beginDate.getTime() < DateUtils.getDateYMD(accBalDtlVoList.get(0).getBeginDate()).getTime()) {
                    beginDate = DateUtils.getDateYMD(accBalDtlVoList.get(0).getBeginDate());
                }
                //beginDate = DateUtils.getDateYMD(accBalDtlVoList.get(0).getBeginDate());
                opAbBillAccBal(beginDate, corpList, corpMapAccBalDtlVoList, user, isBalDate);
            }
        }
    }

    /**
     * 通过缓存或者数据库获取数据
     * @param dataTablesInput
     * @param params
     * @param corpList
     * @return
     */
    private List<AbBillAccDtlBalVO> achieveDataByCacheOrDb(DataTablesInput dataTablesInput, Map<String, Object> params, List<String> corpList) {
        if(dataTablesInput == null || dataTablesInput.getFormdata() == null || !"1".equals(dataTablesInput.getFormdata().get("lockFlag"))){
            return abBillAccBalDao.listAbBillAccDtlBal(params, corpList);
        }
        String cacheKeyStr = CollectionUtil.join(corpList,",") + JsonUtils.toJSONString(params);
        String cacheKey = MD5Util.MD5Encode(cacheKeyStr);
        //System.out.println("cacheKey===="+cacheKey);
        if(billBalanceDataMap.containsKey(cacheKey)){
            return billBalanceDataMap.get(cacheKey);
        }
        boolean isLock = lockSet.add(cacheKey);
        if(isLock && !billBalanceDataMap.containsKey(cacheKey)){
            List<AbBillAccDtlBalVO> accBalDtlLists = abBillAccBalDao.listAbBillAccDtlBal(params, corpList);
            billBalanceDataMap.put(cacheKey,accBalDtlLists);
        }else if(isLock){
            lockSet.remove(cacheKey);
            return billBalanceDataMap.get(cacheKey);
        }else if(!billBalanceDataMap.containsKey(cacheKey)){
            try {
                //50毫秒后没有获取到锁且集合没有值时递归获取
                Thread.sleep(50);
                return achieveDataByCacheOrDb(dataTablesInput, params, corpList);
            } catch (InterruptedException e) {
            }
        }else {
            return billBalanceDataMap.get(cacheKey);
        }
        new Thread(() -> {
            try {
                //默认expireTime秒移除缓存数据
                Thread.sleep(expireTime);
            } catch (InterruptedException e) {
            } finally {
                lockSet.remove(cacheKey);
                billBalanceDataMap.remove(cacheKey);
            }
        }).start();
        return billBalanceDataMap.get(cacheKey);
    }

    /**
     * 对每个单位的余额进行初始化操作
     * @param corpId 单位ID
     * @param bankAcc 持票账户
     * @param bankAccId 持票账户ID
     * @param balDate 余额日期
     * @param balList 需要设置的余额集合
     * @param balMap 需要设置的余额map
     * @param curId 币别ID
     * @param user 用户信息
     * @return 返回操作余额对象
     */
    public AbBillAccBalVO initAbBillAccBalVO(String corpId,String bankAcc,String bankAccId,Date balDate,List<AbBillAccBalVO> balList,Map<String,AbBillAccBalVO> balMap, String curId, ICurrentUser user,String bankName){
        String key= new StringBuilder(100).append(corpId).append("_").append(bankAcc).append("_").append(balDate.getTime()).toString();
        AbBillAccBalVO accBal=null;
        if(balMap.containsKey(key)){
            accBal=balMap.get(key);
        }else{
            accBal=new AbBillAccBalVO(corpId, bankAccId, bankAcc, balDate, curId);
            accBal.setTypeName(bankName);
            if(user!=null){
                accBal.setCreateBy(user.getLoginName());
                accBal.setCreateDate(new Date());
            }
            balMap.put(key, accBal);
            balList.add(accBal);
        }
        return accBal;
    }

    public void initBillTypeBalVO(AbBillAccBalVO accBalInfo,String billType){
        if(accBalInfo.getBillTypeBalVO().isEmpty() || !accBalInfo.getBillTypeBalVO().containsKey(billType)){
            AbBillAccBalTypeVO abBillAccBalTypeVO=new AbBillAccBalTypeVO(billType);
            accBalInfo.getBillTypeBalVO().put(billType,abBillAccBalTypeVO);
        }
    }

    /**
     * 对余额进行计算操作
     * @param dtlBillInfo
     * @param accBalInfo
     * @return 返回是否移除流水信息：true移除false不移除
     */
    public boolean setAccBalInfoVO(AbBillAccDtlBalVO dtlBillInfo,AbBillAccBalVO accBalInfo){
        Date beginDate=DateUtils.getDateYMD(dtlBillInfo.getBeginDate());
        Date endDate=DateUtils.getDateYMD(dtlBillInfo.getEndDate());
        boolean reBoo=false;
        initBillTypeBalVO(accBalInfo,String.valueOf(dtlBillInfo.getBillType()));
        AbBillAccBalTypeVO abBillAccBalTypeVO=accBalInfo.getBillTypeBalVO().get(String.valueOf(dtlBillInfo.getBillType()));
        if(accBalInfo.getBalDate().getTime()>=beginDate.getTime()
                &&accBalInfo.getBalDate().getTime()<=endDate.getTime()){
            if(accBalInfo.getBalDate().getTime()==endDate.getTime()){
                return true;
            }
            if(AccebillConstant.BRIEF_STATUS_6011.equals(dtlBillInfo.getBriefIdOut())){
                //持有未出去的算可用余额
                if(dtlBillInfo.getOutDate()==null
                        ||accBalInfo.getBalDate().getTime()<dtlBillInfo.getOutDate().getTime()){
                    accBalInfo.setAvailBal(accBalInfo.getAvailBal().add(dtlBillInfo.getBillMoney()));
                    abBillAccBalTypeVO.setAvailBal(abBillAccBalTypeVO.getAvailBal().add(dtlBillInfo.getBillMoney()));
                    if(AccebillConstant.AB_BILL_TYPE_BANK.equals(String.valueOf(dtlBillInfo.getBillType()))){
                        accBalInfo.setYpAvailBal(accBalInfo.getYpAvailBal().add(dtlBillInfo.getBillMoney()));
                    }else{
                        accBalInfo.setSpAvailBal(accBalInfo.getSpAvailBal().add(dtlBillInfo.getBillMoney()));
                    }
                    accBalInfo.setBal(accBalInfo.getBal().add(dtlBillInfo.getBillMoney()));
                    abBillAccBalTypeVO.setBal(abBillAccBalTypeVO.getBal().add(dtlBillInfo.getBillMoney()));
                    if(AccebillConstant.AB_BILL_TYPE_BANK.equals(String.valueOf(dtlBillInfo.getBillType()))){
                        accBalInfo.setYpBal(accBalInfo.getYpBal().add(dtlBillInfo.getBillMoney()));
                    }else{
                        accBalInfo.setSpBal(accBalInfo.getSpBal().add(dtlBillInfo.getBillMoney()));
                    }
                }else
                    //持有出去未完成的都算锁定占用余额（以及贴现申请完成贴现办理完成之间的日期都算锁定占用余额）
                    if(dtlBillInfo.getOutDate()!=null
                            &&((accBalInfo.getBalDate().getTime()>=dtlBillInfo.getOutDate().getTime()
                            &&(!AccebillConstant.AB_BILL_BUS_STATUS95.equals(dtlBillInfo.getOutStatus())
                            ||!AccebillConstant.AB_BILL_OPT_STATUS19.equals(dtlBillInfo.getOutOptStatus())
                            ||!AccebillConstant.AB_BILL_BUS_STATUS95.equals(dtlBillInfo.getOutDiscountStatus())
                            ||!AccebillConstant.AB_BILL_OPT_STATUS19.equals(dtlBillInfo.getOutDiscountOptStatus())))
                            ||(dtlBillInfo.getOutDiscountDate()!=null
                            &&accBalInfo.getBalDate().getTime()>=dtlBillInfo.getOutDate().getTime()
                            &&accBalInfo.getBalDate().getTime()<dtlBillInfo.getOutDiscountDate().getTime()
                            &&AccebillConstant.AB_BILL_BUS_STATUS95.equals(dtlBillInfo.getOutDiscountStatus())
                            &&AccebillConstant.AB_BILL_OPT_STATUS19.equals(dtlBillInfo.getOutDiscountOptStatus()))
                    )){
                        accBalInfo.setTiedupBal(accBalInfo.getTiedupBal().add(dtlBillInfo.getBillMoney()));
                        abBillAccBalTypeVO.setTiedupBal(abBillAccBalTypeVO.getTiedupBal().add(dtlBillInfo.getBillMoney()));
                        if(AccebillConstant.AB_BILL_TYPE_BANK.equals(String.valueOf(dtlBillInfo.getBillType()))){
                            accBalInfo.setYpTiedupBal(accBalInfo.getYpTiedupBal().add(dtlBillInfo.getBillMoney()));
                        }else{
                            accBalInfo.setSpTiedupBal(accBalInfo.getSpTiedupBal().add(dtlBillInfo.getBillMoney()));
                        }
                        accBalInfo.setBal(accBalInfo.getBal().add(dtlBillInfo.getBillMoney()));
                        abBillAccBalTypeVO.setBal(abBillAccBalTypeVO.getBal().add(dtlBillInfo.getBillMoney()));
                        if(AccebillConstant.AB_BILL_TYPE_BANK.equals(String.valueOf(dtlBillInfo.getBillType()))){
                            accBalInfo.setYpBal(accBalInfo.getYpBal().add(dtlBillInfo.getBillMoney()));
                        }else{
                            accBalInfo.setSpBal(accBalInfo.getSpBal().add(dtlBillInfo.getBillMoney()));
                        }
                    }
            }else{
                //持有未出去的算可用余额
                if(dtlBillInfo.getOutDate()==null
                        ||accBalInfo.getBalDate().getTime()<dtlBillInfo.getOutDate().getTime()){
                    accBalInfo.setAvailBal(accBalInfo.getAvailBal().add(dtlBillInfo.getBillMoney()));
                    abBillAccBalTypeVO.setAvailBal(abBillAccBalTypeVO.getAvailBal().add(dtlBillInfo.getBillMoney()));
                    if(AccebillConstant.AB_BILL_TYPE_BANK.equals(String.valueOf(dtlBillInfo.getBillType()))){
                        accBalInfo.setYpAvailBal(accBalInfo.getYpAvailBal().add(dtlBillInfo.getBillMoney()));
                    }else{
                        accBalInfo.setSpAvailBal(accBalInfo.getSpAvailBal().add(dtlBillInfo.getBillMoney()));
                    }

                    accBalInfo.setBal(accBalInfo.getBal().add(dtlBillInfo.getBillMoney()));
                    abBillAccBalTypeVO.setBal(abBillAccBalTypeVO.getBal().add(dtlBillInfo.getBillMoney()));
                    if(AccebillConstant.AB_BILL_TYPE_BANK.equals(String.valueOf(dtlBillInfo.getBillType()))){
                        accBalInfo.setYpBal(accBalInfo.getYpBal().add(dtlBillInfo.getBillMoney()));
                    }else{
                        accBalInfo.setSpBal(accBalInfo.getSpBal().add(dtlBillInfo.getBillMoney()));
                    }
                }else
                    //持有出去未完成的都算锁定占用余额
                    if(dtlBillInfo.getOutDate()!=null
                            &&accBalInfo.getBalDate().getTime()>=dtlBillInfo.getOutDate().getTime()
                            &&(!AccebillConstant.AB_BILL_BUS_STATUS95.equals(dtlBillInfo.getOutStatus())
                            ||!AccebillConstant.AB_BILL_OPT_STATUS19.equals(dtlBillInfo.getOutOptStatus()))){
                        accBalInfo.setTiedupBal(accBalInfo.getTiedupBal().add(dtlBillInfo.getBillMoney()));
                        abBillAccBalTypeVO.setTiedupBal(abBillAccBalTypeVO.getTiedupBal().add(dtlBillInfo.getBillMoney()));
                        if(AccebillConstant.AB_BILL_TYPE_BANK.equals(String.valueOf(dtlBillInfo.getBillType()))){
                            accBalInfo.setYpTiedupBal(accBalInfo.getYpTiedupBal().add(dtlBillInfo.getBillMoney()));
                        }else{
                            accBalInfo.setSpTiedupBal(accBalInfo.getSpTiedupBal().add(dtlBillInfo.getBillMoney()));
                        }

                        accBalInfo.setBal(accBalInfo.getBal().add(dtlBillInfo.getBillMoney()));
                        abBillAccBalTypeVO.setBal(abBillAccBalTypeVO.getBal().add(dtlBillInfo.getBillMoney()));
                        if(AccebillConstant.AB_BILL_TYPE_BANK.equals(String.valueOf(dtlBillInfo.getBillType()))){
                            accBalInfo.setYpBal(accBalInfo.getYpBal().add(dtlBillInfo.getBillMoney()));
                        }else{
                            accBalInfo.setSpBal(accBalInfo.getSpBal().add(dtlBillInfo.getBillMoney()));
                        }
                    }
            }
        }
        return reBoo;
    }

    /**
     * 根据票据余额明细生成生成新的余额数据
     * @param beginDate 余额开始日期
     * @param corpList 需要设置的单位
     * @param corpList 需要设置的单位
     */
    private List<AbBillAccBalVO> opAbBillAccBalVO(Date beginDate,Date enDate,List<String> corpList,Map<String,List<AbBillAccDtlBalVO>> corpMapAccBalDtlVoList,ICurrentUser user, String resource){
        Date balDate = beginDate;
        if(balDate==null){
            balDate=DateUtils.getDateYMD();
        }
        if(enDate==null){
            enDate= DateUtils.getDateYMD();
        }
        List<AbBillAccBalVO> balList=new ArrayList<>();
        Map<String,AbBillAccBalVO> balMap=new HashMap<>();
        String curId = abVoucherinfoViceService.getCurIdByCNY();
        //初始化日期和和单位的余额信息
        while (balDate.getTime()<=enDate.getTime()) {
            for(Map.Entry<String, List<AbBillAccDtlBalVO>> entry : corpMapAccBalDtlVoList.entrySet()){
                String corpId = entry.getKey();
                List<AbBillAccDtlBalVO> accBalDtlVoList=corpMapAccBalDtlVoList.get(corpId);
                if(ListUtils.isNotNullOrEmpty(accBalDtlVoList)){
                    for(int i=0;i<accBalDtlVoList.size();i++){
                        AbBillAccDtlBalVO info=accBalDtlVoList.get(i);
//                        resource 通过查询资源判断是单位维度还是账号维度。进行余额汇总
                        AbBillAccBalVO accBalInfo=initAbBillAccBalVO(corpId, AccebillConstant.AB_BILL_CORPBAL_QUERY_CORPOP.equals(resource)?"_":info.getBankAcc(), info.getBankAccId(), balDate, balList, balMap, curId, user, info.getTypeName());
                        //对余额进行计算
                        boolean isRemove=setAccBalInfoVO(info, accBalInfo);
                        if(isRemove){
                            accBalDtlVoList.remove(i);
                            i--;
                        }
                    }
                }else{
                    initAbBillAccBalVO(corpId, AccebillConstant.AB_BILL_CORPBAL_QUERY_CORPOP.equals(resource)?"_":null, null, balDate, balList, balMap, curId, user,null);
                }
            }
            balDate=DateUtils.addDay(balDate, 1);
        }
        return balList;
    }

    /**
     * 排序
     * @param outList
     * @param dataTablesInput
     */
    private void sortAbBillAccBalVO(List<AbBillAccBalVO> outList,DataTablesInput dataTablesInput){
        String orderField="corpName";
        int orderDir=1;
        if(ListUtils.isNotNullOrEmpty(dataTablesInput.getOrder())){
            dataTablesInput.getOrder().get(0).getColumn();
            orderField = dataTablesInput.getColumns().get(dataTablesInput.getOrder().get(0).getColumn()).getName();
            if("desc".equals(dataTablesInput.getOrder().get(0).getDir())){
                orderDir=-1;
            }
        }
        String finalOrderField = orderField;
        int finalOrderDir = orderDir;
        Collections.sort(outList,new Comparator<AbBillAccBalVO>() {
            @Override
            public int compare(AbBillAccBalVO o1, AbBillAccBalVO o2) {
                int compareTo=0;
                if("corpName".equals(finalOrderField)){
                    compareTo = o1.getCorpCode().compareTo(o2.getCorpCode());
                } else if ("bankAcc".equals(finalOrderField)) {
                    if(StringUtils.isEmpty(o1.getBankAcc())&&StringUtils.isEmpty(o2.getBankAcc())){
                        compareTo=0;
                    }else if(StringUtils.isEmpty(o1.getBankAcc())){
                        compareTo=1;
                    }else if(StringUtils.isEmpty(o2.getBankAcc())){
                        compareTo=-1;
                    }else{
                        compareTo = o1.getBankAcc().compareTo(o2.getBankAcc());
                    }
                } else if ("balDate".equals(finalOrderField)) {
                    compareTo = o1.getBalDate().compareTo(o2.getBalDate());
                } else if ("curName".equals(finalOrderField)) {
                    compareTo = o1.getCurName().compareTo(o2.getCurName());
                } else if ("spBal".equals(finalOrderField)) {
                    compareTo = o1.getSpBal().compareTo(o2.getSpBal());
                } else if ("spAvailBal".equals(finalOrderField)) {
                    compareTo = o1.getSpAvailBal().compareTo(o2.getSpAvailBal());
                } else if ("spTiedupBal".equals(finalOrderField)) {
                    compareTo = o1.getSpTiedupBal().compareTo(o2.getSpTiedupBal());
                } else if ("ypBal".equals(finalOrderField)) {
                    compareTo = o1.getYpBal().compareTo(o2.getYpBal());
                } else if ("ypAvailBal".equals(finalOrderField)) {
                    compareTo = o1.getYpAvailBal().compareTo(o2.getYpAvailBal());
                } else if ("ypTiedupBal".equals(finalOrderField)) {
                    compareTo = o1.getYpTiedupBal().compareTo(o2.getYpTiedupBal());
                } else if ("bal".equals(finalOrderField)) {
                    compareTo = o1.getBal().compareTo(o2.getBal());
                } else if ("availBal".equals(finalOrderField)) {
                    compareTo = o1.getAvailBal().compareTo(o2.getAvailBal());
                } else if ("tiedupBal".equals(finalOrderField)) {
                    compareTo = o1.getTiedupBal().compareTo(o2.getTiedupBal());
                }

                int status=0;
                if(compareTo<0){
                    status = -1;
                }else if(compareTo>0) {
                    status = 1;
                }
                return finalOrderDir *status;
            }
        });

    }


    /**
     * 不通过余额表查询余额数据
     * @param corpIds
     * @param user
     * @param dataTablesInput
     * @param resource
     * @return
     */
    public DataTablesOutput searchBalance(String corpIds,ICurrentUser user, DataTablesInput dataTablesInput, String resource){
        Map<String,Object> params=new HashMap<>();
        DataTablesOutput output = new DataTablesOutput();
        output.setDraw(dataTablesInput.getDraw());
        List<String> corpAuthList = abCorpService.getUserCorpIds(resource, user.getId());
        List<String> corpList = null;
        if(StringUtils.isNotEmpty(corpIds)){
            corpList=new ArrayList<>();
            //设置查询单位和有权限单位取并集
            String[] corpIdArray=corpIds.split(",");
            for(String corpId:corpIdArray){
                if(StringUtils.isNotEmpty(corpId)
                        &&corpAuthList.contains(corpId)){
                    corpList.add(corpId);
                }
            }
        }else{
            corpList=corpAuthList;
        }
        //设置是否过滤票据账号
        accountGrantService.setElectricBillAccountIsDetail(dataTablesInput,resource,corpIds);
        //没有授权单位不需要做余额初始化功能
        if(ListUtils.isNullOrEmpty(corpList)){
            return output;
        }
        Date beginDate=null;
        Date endDate=null;
        if(StringUtils.isNotEmpty(dataTablesInput.getFormdata().get("ged_balDate"))){
            beginDate=DateUtils.str2Date(dataTablesInput.getFormdata().get("ged_balDate").toString(), DateUtils.date_sdf());
            params.put("beginDate", beginDate);
        }
        if(StringUtils.isNotEmpty(dataTablesInput.getFormdata().get("led_balDate"))){
            endDate=DateUtils.str2Date(dataTablesInput.getFormdata().get("led_balDate").toString(), DateUtils.date_sdf());
            params.put("endDate", endDate);
        }
        if(dataTablesInput.getFormdata().containsKey("accountGrantList")) {
            params.put("accountGrantList",dataTablesInput.getFormdata().get("accountGrantList"));
        }
        List<AbBillAccDtlBalVO> accBalDtlVoList = achieveDataByCacheOrDb(dataTablesInput, params, corpList);
        if(accBalDtlVoList == null || accBalDtlVoList.size()<=0) {
            return output;
        }
        if(beginDate==null){
            beginDate = DateUtils.getDateYMD(accBalDtlVoList.get(0).getBeginDate());
        }
        if(endDate==null){
            endDate = DateUtils.getDateYMD();
        }
        Map<String, List<AbBillAccDtlBalVO>> corpMapAccBalDtlVoList = getCorpMapAccBalDtlVoList(accBalDtlVoList);
        if (ListUtils.isNotNullOrEmpty(accBalDtlVoList)) {
            if (beginDate.getTime() < DateUtils.getDateYMD(accBalDtlVoList.get(0).getBeginDate()).getTime()) {
                beginDate = DateUtils.getDateYMD(accBalDtlVoList.get(0).getBeginDate());
            }
            List<AbBillAccBalVO> resultList = opAbBillAccBalVO(beginDate, endDate, corpList, corpMapAccBalDtlVoList, user,resource);
            List<AbBillAccBalVO> outList = new ArrayList<>();
            Map<String,Object> inMap = dataTablesInput.getFormdata();
            if(CollectionUtils.isEmpty(resultList)){
                return output;
            }
            Map<String, CorpDTO> corpDTOMap=new HashMap<>();
            int idSize=0;
            //按照条件过滤数据
            for(AbBillAccBalVO abBillAccBal : resultList){
                if(!ObjectUtils.isEmpty(inMap.get("likes_bankAcc")) && !"".equals(inMap.get("likes_bankAcc").toString())
                        && (abBillAccBal.getBankAcc() == null || !abBillAccBal.getBankAcc().contains(inMap.get("likes_bankAcc").toString()))){
                    continue;
                }
                if(!ObjectUtils.isEmpty(inMap.get("ged_balDate")) && !"".equals(inMap.get("ged_balDate").toString()) &&
                        (abBillAccBal.getBalDate() == null || abBillAccBal.getBalDate().getTime() < DateUtils.str2Date(inMap.get("ged_balDate").toString(), DateUtils.date_sdf()).getTime())){
                    continue;
                }
                if(!ObjectUtils.isEmpty(inMap.get("led_balDate")) && !"".equals(inMap.get("led_balDate").toString()) &&
                        (abBillAccBal.getBalDate() == null || abBillAccBal.getBalDate().getTime() > DateUtils.str2Date(inMap.get("led_balDate").toString(), DateUtils.date_sdf()).getTime())){
                    continue;
                }
                if(!ObjectUtils.isEmpty(inMap.get("geg_availBal")) && !"".equals(inMap.get("geg_availBal").toString()) &&
                        (abBillAccBal.getAvailBal() == null || abBillAccBal.getAvailBal().compareTo(new BigDecimal(inMap.get("geg_availBal").toString())) < 0)){
                    continue;
                }
                if(!ObjectUtils.isEmpty(inMap.get("leg_bal")) && !"".equals(inMap.get("leg_bal").toString()) &&
                        (abBillAccBal.getBal() == null || abBillAccBal.getBal().compareTo(new BigDecimal(inMap.get("leg_bal").toString())) > 0)){
                    continue;
                }
                if(!ObjectUtils.isEmpty(inMap.get("geg_bal")) && !"".equals(inMap.get("geg_bal").toString()) &&
                        (abBillAccBal.getBal() == null || abBillAccBal.getBal().compareTo(new BigDecimal(inMap.get("geg_bal").toString())) < 0)){
                    continue;
                }
                if(!ObjectUtils.isEmpty(inMap.get("leg_availBal")) && !"".equals(inMap.get("leg_availBal").toString()) &&
                        (abBillAccBal.getAvailBal() == null || abBillAccBal.getAvailBal().compareTo(new BigDecimal(inMap.get("leg_availBal").toString())) > 0)){
                    continue;
                }
                if(!ObjectUtils.isEmpty(inMap.get("geg_tiedupBal")) && !"".equals(inMap.get("geg_tiedupBal").toString()) &&
                        (abBillAccBal.getTiedupBal() == null || abBillAccBal.getTiedupBal().compareTo(new BigDecimal(inMap.get("geg_tiedupBal").toString())) < 0)){
                    continue;
                }
                if(!ObjectUtils.isEmpty(inMap.get("leg_tiedupBal")) && !"".equals(inMap.get("leg_tiedupBal").toString()) &&
                        (abBillAccBal.getTiedupBal() == null || abBillAccBal.getTiedupBal().compareTo(new BigDecimal(inMap.get("leg_tiedupBal").toString())) > 0)){
                    continue;
                }
                CorpDTO corpDTO = corpDTOMap.get(abBillAccBal.getCorpId());
                if(corpDTO==null){
                    corpDTO=iCorpService.get(abBillAccBal.getCorpId());
                    corpDTOMap.put(abBillAccBal.getCorpId(),corpDTO);
                }
                abBillAccBal.setCorpCode(corpDTO.getCode());
                abBillAccBal.setCorpName(corpDTO.getName());
                abBillAccBal.setId(String.valueOf(++idSize));
                outList.add(abBillAccBal);
            }
            sortAbBillAccBalVO(outList,dataTablesInput);
            output.setRecordsFiltered(outList.size());
            output.setRecordsTotal(outList.size());
            Integer len = dataTablesInput.getLength();
            Integer start = dataTablesInput.getStart();
            Integer bfNum = start;
            List limitList = new ArrayList<>();
            List<DictDTO> billTypes = abDictService.list(AccebillConstant.AB_BILL_TYPE_DICT);
            for(int i = bfNum; i < outList.size() && i < (bfNum + len); i++){
                AbBillAccBalVO billAccBalVO=outList.get(i);
                Map outMap = JsonUtils.json2Map(JsonUtils.toJSONString(billAccBalVO));
                //根据票据类型设置票据余额
                for(DictDTO dictDTO:billTypes){
                    if(billAccBalVO.getBillTypeBalVO().containsKey(dictDTO.getCode())){
                        outMap.put("bal"+dictDTO.getCode(),billAccBalVO.getBillTypeBalVO().get(dictDTO.getCode()).getBal());
                        outMap.put("availBal"+dictDTO.getCode(),billAccBalVO.getBillTypeBalVO().get(dictDTO.getCode()).getAvailBal());
                        outMap.put("tiedupBal"+dictDTO.getCode(),billAccBalVO.getBillTypeBalVO().get(dictDTO.getCode()).getTiedupBal());
                    }else{
                        outMap.put("bal"+dictDTO.getCode(),0);
                        outMap.put("availBal"+dictDTO.getCode(),0);
                        outMap.put("tiedupBal"+dictDTO.getCode(),0);
                    }
                }
                if(StringUtils.isNotBlank(outList.get(i).getCorpId())){
                    outMap.put("corpName",outList.get(i).getCorpName());
                    outMap.put("curName","人民币");
                    outMap.put("bankAcc",outList.get(i).getBankAcc() == null ? null : outList.get(i).getBankAcc());
                    outMap.put("corpCode",outList.get(i).getCorpCode());
                }
                limitList.add(outMap);
            }
            output.setData(limitList);
        }
        return output;
    }

    /**
     * 不通过余额表查询余额数据
     *
     * @param corpIds
     * @param beginDateStr
     * @param user
     * @param isBalDate
     * @param dataTablesInput
     * @return
     */
    public DataTablesOutput searchBalance(String corpIds, String beginDateStr, ICurrentUser user, boolean isBalDate, DataTablesInput dataTablesInput/*,String filteType*/) {
        Map<String, Object> params = new HashMap<>();
        DataTablesOutput output = new DataTablesOutput();
        output.setDraw(dataTablesInput.getDraw());
        List<String> corpAuthList = abCorpService.getUserCorpIds(AccebillConstant.AB_BILL_ACCBAL_RESET_CORPOP, user.getId());
        List<String> corpList = null;
        if (StringUtils.isNotEmpty(corpIds)) {
            corpList = new ArrayList<>();
            //设置查询单位和有权限单位取并集
            String[] corpIdArray = corpIds.split(",");
            for (String corpId : corpIdArray) {
                if (StringUtils.isNotEmpty(corpId)
                        && corpAuthList.contains(corpId)) {
                    corpList.add(corpId);
                }
            }
        } else {
            corpList = corpAuthList;
        }
        //设置是否过滤票据账号
        accountGrantService.setElectricBillAccount(dataTablesInput, AccebillConstant.AB_BILL_ACCBAL_QUERY_CORPOP, corpIds);
        //没有授权单位不需要做余额初始化功能
        if (ListUtils.isNullOrEmpty(corpList)) {
            return output;
        }
        Date beginDate = null;
        if (StringUtils.isNotEmpty(beginDateStr)) {
            beginDate = DateUtils.str2Date(beginDateStr, DateUtils.date_sdf());
            params.put("beginDate", beginDate);
        }
        List<AbBillAccDtlBalVO> accBalDtlVoList = abBillAccBalDao.listAbBillAccDtlBal(params, corpList);
        //List<AbBillAccDtlBalVO> accBalDtlVoList = abBillAccBalDao.getAbBillAccDtlBalList(params, corpList,dataTablesInput.getFormdata().get("accountGrantList"));
        if (accBalDtlVoList.size() <= 0) {
            return output;
        }
        Map<String, List<AbBillAccDtlBalVO>> corpMapAccBalDtlVoList = getCorpMapAccBalDtlVoList(accBalDtlVoList);
        if (ListUtils.isNotNullOrEmpty(accBalDtlVoList)) {
            if (beginDate.getTime() < DateUtils.getDateYMD(accBalDtlVoList.get(0).getBeginDate()).getTime()) {
                beginDate = DateUtils.getDateYMD(accBalDtlVoList.get(0).getBeginDate());
            }
            //beginDate = DateUtils.getDateYMD(accBalDtlVoList.get(0).getBeginDate());
            List<AbBillAccBal> resultList = opAbBillAccBal2(beginDate, corpList, corpMapAccBalDtlVoList, user, isBalDate);
            List<AbBillAccBal> outList = new ArrayList<>();
            Map<String, Object> inMap = dataTablesInput.getFormdata();
            if (CollectionUtils.isEmpty(resultList)) {
                return output;
            }
            //Map<String,AbBillAccBal> corpSearchMap = new HashMap<>();
            //按照条件过滤数据
            for (AbBillAccBal abBillAccBal : resultList) {
                if (dataTablesInput.getFormdata().containsKey("accountGrantList")) {
                    //添加账户授权控制
                    List<String> accountGrantList = dataTablesInput.getFormdata().get("accountGrantList") == null ?
                            null : (List<String>) dataTablesInput.getFormdata().get("accountGrantList");
                    if (abBillAccBal.getBankAcc() != null && !accountGrantList.contains(abBillAccBal.getBankAcc())) {
                        continue;
                    }
                }
                if (!ObjectUtils.isEmpty(inMap.get("likes_bankAcc")) && !"".equals(inMap.get("likes_bankAcc").toString())
                        && (abBillAccBal.getBankAcc() == null || !abBillAccBal.getBankAcc().contains(inMap.get("likes_bankAcc").toString()))) {
                    continue;
                }
                if (!ObjectUtils.isEmpty(inMap.get("ged_balDate")) && !"".equals(inMap.get("ged_balDate").toString()) &&
                        (abBillAccBal.getBalDate() == null || abBillAccBal.getBalDate().getTime() < DateUtils.str2Date(inMap.get("ged_balDate").toString(), DateUtils.date_sdf()).getTime())) {
                    continue;
                }
                if (!ObjectUtils.isEmpty(inMap.get("led_balDate")) && !"".equals(inMap.get("led_balDate").toString()) &&
                        (abBillAccBal.getBalDate() == null || abBillAccBal.getBalDate().getTime() > DateUtils.str2Date(inMap.get("led_balDate").toString(), DateUtils.date_sdf()).getTime())) {
                    continue;
                }
                if (!ObjectUtils.isEmpty(inMap.get("geg_availBal")) && !"".equals(inMap.get("geg_availBal").toString()) &&
                        (abBillAccBal.getAvailBal() == null || abBillAccBal.getAvailBal().compareTo(new BigDecimal(inMap.get("geg_availBal").toString())) < 0)) {
                    continue;
                }
                if (!ObjectUtils.isEmpty(inMap.get("leg_bal")) && !"".equals(inMap.get("leg_bal").toString()) &&
                        (abBillAccBal.getBal() == null || abBillAccBal.getBal().compareTo(new BigDecimal(inMap.get("leg_bal").toString())) > 0)) {
                    continue;
                }
                if (!ObjectUtils.isEmpty(inMap.get("geg_bal")) && !"".equals(inMap.get("geg_bal").toString()) &&
                        (abBillAccBal.getBal() == null || abBillAccBal.getBal().compareTo(new BigDecimal(inMap.get("geg_bal").toString())) < 0)) {
                    continue;
                }
                if (!ObjectUtils.isEmpty(inMap.get("leg_availBal")) && !"".equals(inMap.get("leg_availBal").toString()) &&
                        (abBillAccBal.getAvailBal() == null || abBillAccBal.getAvailBal().compareTo(new BigDecimal(inMap.get("leg_availBal").toString())) > 0)) {
                    continue;
                }
                if (!ObjectUtils.isEmpty(inMap.get("geg_tiedupBal")) && !"".equals(inMap.get("geg_tiedupBal").toString()) &&
                        (abBillAccBal.getTiedupBal() == null || abBillAccBal.getTiedupBal().compareTo(new BigDecimal(inMap.get("geg_tiedupBal").toString())) < 0)) {
                    continue;
                }
                if (!ObjectUtils.isEmpty(inMap.get("leg_tiedupBal")) && !"".equals(inMap.get("leg_tiedupBal").toString()) &&
                        (abBillAccBal.getTiedupBal() == null || abBillAccBal.getTiedupBal().compareTo(new BigDecimal(inMap.get("leg_tiedupBal").toString())) > 0)) {
                    continue;
                }
                outList.add(abBillAccBal);
                //如果是单位票据余额查询进行以下处理
                /*if(AccebillConstant.AB_BILL_CORPBAL_QUERY_CORPOP.equals(filteType)){
                    String key = abBillAccBal.getCorpId() + (abBillAccBal.getBalDate() == null ? "null" : abBillAccBal.getBalDate().getTime()) + abBillAccBal.getCurId();
                    if(corpSearchMap.get(key) == null){
                        corpSearchMap.put(key,abBillAccBal);
                    }else {
                        AbBillAccBal aBA = corpSearchMap.get(key);
                        aBA.setBal(aBA.getBal().add(abBillAccBal.getBal()));
                        aBA.setAvailBal(aBA.getAvailBal().add(abBillAccBal.getAvailBal()));
                        aBA.setTiedupBal(aBA.getTiedupBal().add(abBillAccBal.getTiedupBal()));
                        aBA.setSpBal(aBA.getSpBal().add(abBillAccBal.getSpBal()));
                        aBA.setSpAvailBal(aBA.getSpAvailBal().add(abBillAccBal.getSpAvailBal()));
                        aBA.setSpTiedupBal(aBA.getSpTiedupBal().add(abBillAccBal.getSpTiedupBal()));
                        aBA.setYpBal(aBA.getYpBal().add(abBillAccBal.getYpBal()));
                        aBA.setYpAvailBal(aBA.getYpAvailBal().add(abBillAccBal.getYpAvailBal()));
                        aBA.setYpTiedupBal(aBA.getYpTiedupBal().add(abBillAccBal.getYpTiedupBal()));
                    }
                }*/
            }
           /* if(!CollectionUtils.isEmpty(corpSearchMap)){
                outList = JsonUtils.json2List(JsonUtils.toJSONString(corpSearchMap.values()),AbBillAccBal.class);
            }*/
            output.setRecordsFiltered(outList.size());
            output.setRecordsTotal(outList.size());
            Integer len = dataTablesInput.getLength();
            Integer start = dataTablesInput.getStart();
            Integer bfNum = start;
            List limitList = new ArrayList<>();
            for (int i = bfNum; i < outList.size() && i < (bfNum + len); i++) {
                Map outMap = JsonUtils.json2Map(JsonUtils.toJSONString(outList.get(i)));
                if (StringUtils.isNotBlank(outList.get(i).getCorpId())) {
                    CorpDTO corpDTO = iCorpService.get(outList.get(i).getCorpId());
                    outMap.put("corpName", corpDTO.getName());
                    outMap.put("curName", "人民币");
                    outMap.put("bankAcc", outList.get(i).getBankAcc() == null ? null : outList.get(i).getBankAcc());
                    outMap.put("corpCode", corpDTO.getCode());
                }
                limitList.add(outMap);
            }
            output.setData(limitList);
        }
        return output;
    }
    /**
     * 进行区分每个单位的数据操作
     *
     * @param accBalDtlVoList
     * @return
     */
    private Map<String, List<AbBillAccDtlBalVO>> getCorpMapAccBalDtlVoList(List<AbBillAccDtlBalVO> accBalDtlVoList) {
        Map<String, List<AbBillAccDtlBalVO>> corpMapDtlBalList = new HashMap<>();
        for (AbBillAccDtlBalVO info : accBalDtlVoList) {
            if (!corpMapDtlBalList.containsKey(info.getCorpId())) {
                corpMapDtlBalList.put(info.getCorpId(), new ArrayList<AbBillAccDtlBalVO>());
            }
            corpMapDtlBalList.get(info.getCorpId()).add(info);
        }
        return corpMapDtlBalList;
    }
    /**
     * 根据票据余额明细生成生成新的余额数据
     *
     * @param beginDate 余额开始日期
     * @param corpList  需要设置的单位
     * @param corpList  需要设置的单位
     */
    private List<AbBillAccBal> opAbBillAccBal2(Date beginDate, List<String> corpList, Map<String, List<AbBillAccDtlBalVO>> corpMapAccBalDtlVoList, ICurrentUser user, boolean isBalDate) {
        Date balDate = beginDate;
        if (balDate == null) {
            balDate = DateUtils.getDateYMD();
        }
        Date enDate;
        if (isBalDate) {
            enDate = balDate;
        } else {
            enDate = DateUtils.getDateYMD();
        }
        List<AbBillAccBal> balList = new ArrayList<>();
        Map<String, AbBillAccBal> balMap = new HashMap<>();
        String curId = abVoucherinfoViceService.getCurIdByCNY();
        //票据流水标记已设置过余额id
        List<String> accIdList = new ArrayList<String>();
        //初始化日期和和单位的余额信息
        while (balDate.getTime() <= enDate.getTime()) {
            for (String corpId : corpList) {
                List<AbBillAccDtlBalVO> accBalDtlVoList = corpMapAccBalDtlVoList.get(corpId);
                if (ListUtils.isNotNullOrEmpty(accBalDtlVoList)) {
                    for (int i = 0; i < accBalDtlVoList.size(); i++) {
                        AbBillAccDtlBalVO info = accBalDtlVoList.get(i);
                        AbBillAccBal accBalInfo = initAbBillAccBal(corpId, info.getBankAcc(), info.getBankAccId(), balDate, balList, balMap, curId, user);
                        //对余额进行计算
                        boolean isRemove = setAccBalInfo(info, accBalInfo);
                        if (isRemove) {
                            accBalDtlVoList.remove(i);
                            i--;
                        }
                        //设置在线电票余额重置票据流水id(标记已设置过余额)
                        if (AccebillConstant.AB_BILL_FLAG_ELECT.equals(info.getBillType())) {
                            if (AccebillConstant.AB_BILL_SEND_SIGN_Y.equals(info.getSendSign())
                                    && AccebillConstant.AB_BILL_OPT_STATUS19.equals(info.getOptStatus())) {
                                accIdList.add(info.getAccId());
                            } else if (AccebillConstant.AB_BILL_SEND_SIGN_Y.equals(info.getSendSignOut())
                                    && AccebillConstant.AB_BILL_OPT_STATUS19.equals(info.getOutOptStatus())) {
                                accIdList.add(info.getAccIdOut());
                            } else if (AccebillConstant.AB_BILL_SEND_SIGN_Y.equals(info.getSendSignDiscount())
                                    && AccebillConstant.AB_BILL_OPT_STATUS19.equals(info.getOutDiscountOptStatus())) {
                                accIdList.add(info.getAccIdOut());
                            }
                        }
                    }
                } else {
                    initAbBillAccBal(corpId, null, null, balDate, balList, balMap, curId, user);
                }
            }
            balDate = DateUtils.addDay(balDate, 1);
        }
        return balList;
//    	logger.debug(balList.toString());
        /*abBillAccBalDao.batchJdbcSave(balList);
        //设置在线电票余额重置票据流水id(标记已设置过余额)
        if (accIdList != null && !accIdList.isEmpty()) {
            String[] accIds = accIdList.toArray(new String[accIdList.size()]);
            AbAccrecordsVO accRecordsVO = new AbAccrecordsVO();
            accRecordsVO.setIdArray(accIds);
            accRecordsVO.setCdtRatgs("1");//借用此字段用来标记已设置过余额
            abAccrecordsService.batchUpdate(accRecordsVO, user);
        }*/
    }
    /**
     * 根据票据余额明细生成生成新的余额数据
     *
     * @param beginDate 余额开始日期
     * @param corpList  需要设置的单位
     * @param corpList  需要设置的单位
     */
    private void opAbBillAccBal(Date beginDate, List<String> corpList, Map<String, List<AbBillAccDtlBalVO>> corpMapAccBalDtlVoList, ICurrentUser user, boolean isBalDate) {
        Date balDate = beginDate;
        if (balDate == null) {
            balDate = DateUtils.getDateYMD();
        }
        Date enDate;
        if (isBalDate) {
            enDate = balDate;
        } else {
            enDate = DateUtils.getDateYMD();
        }
        List<AbBillAccBal> balList = new ArrayList<>();
        Map<String, AbBillAccBal> balMap = new HashMap<>();
        String curId = abVoucherinfoViceService.getCurIdByCNY();
        //票据流水标记已设置过余额id
        List<String> accIdList = new ArrayList<String>();
        //初始化日期和和单位的余额信息
        while (balDate.getTime() <= enDate.getTime()) {
            for (String corpId : corpList) {
                List<AbBillAccDtlBalVO> accBalDtlVoList = corpMapAccBalDtlVoList.get(corpId);
                if (ListUtils.isNotNullOrEmpty(accBalDtlVoList)) {
                    for (int i = 0; i < accBalDtlVoList.size(); i++) {
                        AbBillAccDtlBalVO info = accBalDtlVoList.get(i);
                        AbBillAccBal accBalInfo = initAbBillAccBal(corpId, info.getBankAcc(), info.getBankAccId(), balDate, balList, balMap, curId, user);
                        //对余额进行计算
                        boolean isRemove = setAccBalInfo(info, accBalInfo);
                        if (isRemove) {
                            accBalDtlVoList.remove(i);
                            i--;
                        }
                        //设置在线电票余额重置票据流水id(标记已设置过余额)
                        if (AccebillConstant.AB_BILL_FLAG_ELECT.equals(info.getBillType())) {
                            if (AccebillConstant.AB_BILL_SEND_SIGN_Y.equals(info.getSendSign())
                                    && AccebillConstant.AB_BILL_OPT_STATUS19.equals(info.getOptStatus())) {
                                accIdList.add(info.getAccId());
                            } else if (AccebillConstant.AB_BILL_SEND_SIGN_Y.equals(info.getSendSignOut())
                                    && AccebillConstant.AB_BILL_OPT_STATUS19.equals(info.getOutOptStatus())) {
                                accIdList.add(info.getAccIdOut());
                            } else if (AccebillConstant.AB_BILL_SEND_SIGN_Y.equals(info.getSendSignDiscount())
                                    && AccebillConstant.AB_BILL_OPT_STATUS19.equals(info.getOutDiscountOptStatus())) {
                                accIdList.add(info.getAccIdOut());
                            }
                        }
                    }
                } else {
                    initAbBillAccBal(corpId, null, null, balDate, balList, balMap, curId, user);
                }
            }
            balDate = DateUtils.addDay(balDate, 1);
        }
//    	logger.debug(balList.toString());
        abBillAccBalDao.batchJdbcSave(balList);
        //设置在线电票余额重置票据流水id(标记已设置过余额)
        if (accIdList != null && !accIdList.isEmpty()) {
            String[] accIds = accIdList.toArray(new String[accIdList.size()]);
            AbAccrecordsVO accRecordsVO = new AbAccrecordsVO();
            accRecordsVO.setIdArray(accIds);
            accRecordsVO.setCdtRatgs("1");//借用此字段用来标记已设置过余额
            abAccrecordsService.batchUpdate(accRecordsVO, user);
        }
    }
    /**
     * 对余额进行计算操作
     *
     * @param dtlBillInfo
     * @param accBalInfo
     * @return 返回是否移除流水信息：true移除false不移除
     */
    public boolean setAccBalInfo(AbBillAccDtlBalVO dtlBillInfo, AbBillAccBal accBalInfo) {
        Date beginDate = DateUtils.getDateYMD(dtlBillInfo.getBeginDate());
        Date endDate = DateUtils.getDateYMD(dtlBillInfo.getEndDate());
        boolean reBoo = false;
        if (accBalInfo.getBalDate().getTime() >= beginDate.getTime()
                && accBalInfo.getBalDate().getTime() <= endDate.getTime()) {
            if (AccebillConstant.BRIEF_STATUS_6011.equals(dtlBillInfo.getBriefIdOut())) {
                //持有未出去和持有出去未完成的都算余额（以及贴现申请完成贴现办理完成之间的日期都算余额）
//	    		if(dtlBillInfo.getOutDate()==null
//	    				||(accBalInfo.getBalDate().getTime()>=dtlBillInfo.getOutDate().getTime()
//	    					&&(!AccebillConstant.AB_BILL_BUS_STATUS95.equals(dtlBillInfo.getOutStatus())
//			    				||!AccebillConstant.AB_BILL_OPT_STATUS19.equals(dtlBillInfo.getOutOptStatus())
//			    				||!AccebillConstant.AB_BILL_BUS_STATUS95.equals(dtlBillInfo.getOutDiscountStatus())
//			    				||!AccebillConstant.AB_BILL_OPT_STATUS19.equals(dtlBillInfo.getOutDiscountOptStatus())))
//	    				||(dtlBillInfo.getOutDiscountDate()!=null
//	    					&&accBalInfo.getBalDate().getTime()>=dtlBillInfo.getOutDate().getTime()
//	    					&&accBalInfo.getBalDate().getTime()<dtlBillInfo.getOutDiscountDate().getTime()
//	    	    			&&AccebillConstant.AB_BILL_BUS_STATUS95.equals(dtlBillInfo.getOutDiscountStatus())
//			    			&&AccebillConstant.AB_BILL_OPT_STATUS19.equals(dtlBillInfo.getOutDiscountOptStatus()))){
//	    			accBalInfo.setBal(accBalInfo.getBal().add(dtlBillInfo.getBillMoney()));
//	    			if(AccebillConstant.AB_BILL_TYPE_BANK.equals(String.valueOf(dtlBillInfo.getBillType()))){
//	    				accBalInfo.setYpBal(accBalInfo.getYpBal().add(dtlBillInfo.getBillMoney()));
//	    			}else{
//	    				accBalInfo.setSpBal(accBalInfo.getSpBal().add(dtlBillInfo.getBillMoney()));
//	    			}
//	    		}
                //持有未出去的算可用余额
                if (dtlBillInfo.getOutDate() == null
                        || accBalInfo.getBalDate().getTime() < dtlBillInfo.getOutDate().getTime()) {
                    accBalInfo.setAvailBal(accBalInfo.getAvailBal().add(dtlBillInfo.getBillMoney()));
                    if (AccebillConstant.AB_BILL_TYPE_BANK.equals(String.valueOf(dtlBillInfo.getBillType()))) {
                        accBalInfo.setYpAvailBal(accBalInfo.getYpAvailBal().add(dtlBillInfo.getBillMoney()));
                    } else {
                        accBalInfo.setSpAvailBal(accBalInfo.getSpAvailBal().add(dtlBillInfo.getBillMoney()));
                    }
                    accBalInfo.setBal(accBalInfo.getBal().add(dtlBillInfo.getBillMoney()));
                    if (AccebillConstant.AB_BILL_TYPE_BANK.equals(String.valueOf(dtlBillInfo.getBillType()))) {
                        accBalInfo.setYpBal(accBalInfo.getYpBal().add(dtlBillInfo.getBillMoney()));
                    } else {
                        accBalInfo.setSpBal(accBalInfo.getSpBal().add(dtlBillInfo.getBillMoney()));
                    }
                } else
                    //持有出去未完成的都算锁定占用余额（以及贴现申请完成贴现办理完成之间的日期都算锁定占用余额）
                    if (dtlBillInfo.getOutDate() != null
                            && ((accBalInfo.getBalDate().getTime() >= dtlBillInfo.getOutDate().getTime()
                            && (!AccebillConstant.AB_BILL_BUS_STATUS95.equals(dtlBillInfo.getOutStatus())
                            || !AccebillConstant.AB_BILL_OPT_STATUS19.equals(dtlBillInfo.getOutOptStatus())
                            || !AccebillConstant.AB_BILL_BUS_STATUS95.equals(dtlBillInfo.getOutDiscountStatus())
                            || !AccebillConstant.AB_BILL_OPT_STATUS19.equals(dtlBillInfo.getOutDiscountOptStatus())))
                            || (dtlBillInfo.getOutDiscountDate() != null
                            && accBalInfo.getBalDate().getTime() >= dtlBillInfo.getOutDate().getTime()
                            && accBalInfo.getBalDate().getTime() < dtlBillInfo.getOutDiscountDate().getTime()
                            && AccebillConstant.AB_BILL_BUS_STATUS95.equals(dtlBillInfo.getOutDiscountStatus())
                            && AccebillConstant.AB_BILL_OPT_STATUS19.equals(dtlBillInfo.getOutDiscountOptStatus()))
                    )) {
                        accBalInfo.setTiedupBal(accBalInfo.getTiedupBal().add(dtlBillInfo.getBillMoney()));
                        if (AccebillConstant.AB_BILL_TYPE_BANK.equals(String.valueOf(dtlBillInfo.getBillType()))) {
                            accBalInfo.setYpTiedupBal(accBalInfo.getYpTiedupBal().add(dtlBillInfo.getBillMoney()));
                        } else {
                            accBalInfo.setSpTiedupBal(accBalInfo.getSpTiedupBal().add(dtlBillInfo.getBillMoney()));
                        }
                        accBalInfo.setBal(accBalInfo.getBal().add(dtlBillInfo.getBillMoney()));
                        if (AccebillConstant.AB_BILL_TYPE_BANK.equals(String.valueOf(dtlBillInfo.getBillType()))) {
                            accBalInfo.setYpBal(accBalInfo.getYpBal().add(dtlBillInfo.getBillMoney()));
                        } else {
                            accBalInfo.setSpBal(accBalInfo.getSpBal().add(dtlBillInfo.getBillMoney()));
                        }
                    }
            } else {
                //持有未出去和持有出去未完成的都算余额
//	    		if(dtlBillInfo.getOutDate()==null
//	    				||(accBalInfo.getBalDate().getTime()>=dtlBillInfo.getOutDate().getTime()
//	    				&&(!AccebillConstant.AB_BILL_BUS_STATUS95.equals(dtlBillInfo.getOutStatus())
//	    				||!AccebillConstant.AB_BILL_OPT_STATUS19.equals(dtlBillInfo.getOutOptStatus())))){
//	    			accBalInfo.setBal(accBalInfo.getBal().add(dtlBillInfo.getBillMoney()));
//	    			if(AccebillConstant.AB_BILL_TYPE_BANK.equals(String.valueOf(dtlBillInfo.getBillType()))){
//	    				accBalInfo.setYpBal(accBalInfo.getYpBal().add(dtlBillInfo.getBillMoney()));
//	    			}else{
//	    				accBalInfo.setSpBal(accBalInfo.getSpBal().add(dtlBillInfo.getBillMoney()));
//	    			}
//	    		}
                //持有未出去的算可用余额
                if (dtlBillInfo.getOutDate() == null
                        || accBalInfo.getBalDate().getTime() < dtlBillInfo.getOutDate().getTime()) {
                    accBalInfo.setAvailBal(accBalInfo.getAvailBal().add(dtlBillInfo.getBillMoney()));
                    if (AccebillConstant.AB_BILL_TYPE_BANK.equals(String.valueOf(dtlBillInfo.getBillType()))) {
                        accBalInfo.setYpAvailBal(accBalInfo.getYpAvailBal().add(dtlBillInfo.getBillMoney()));
                    } else {
                        accBalInfo.setSpAvailBal(accBalInfo.getSpAvailBal().add(dtlBillInfo.getBillMoney()));
                    }
                    accBalInfo.setBal(accBalInfo.getBal().add(dtlBillInfo.getBillMoney()));
                    if (AccebillConstant.AB_BILL_TYPE_BANK.equals(String.valueOf(dtlBillInfo.getBillType()))) {
                        accBalInfo.setYpBal(accBalInfo.getYpBal().add(dtlBillInfo.getBillMoney()));
                    } else {
                        accBalInfo.setSpBal(accBalInfo.getSpBal().add(dtlBillInfo.getBillMoney()));
                    }
                } else
                    //持有出去未完成的都算锁定占用余额
                    if (dtlBillInfo.getOutDate() != null
                            && accBalInfo.getBalDate().getTime() >= dtlBillInfo.getOutDate().getTime()
                            && (!AccebillConstant.AB_BILL_BUS_STATUS95.equals(dtlBillInfo.getOutStatus())
                            || !AccebillConstant.AB_BILL_OPT_STATUS19.equals(dtlBillInfo.getOutOptStatus()))) {
                        accBalInfo.setTiedupBal(accBalInfo.getTiedupBal().add(dtlBillInfo.getBillMoney()));
                        if (AccebillConstant.AB_BILL_TYPE_BANK.equals(String.valueOf(dtlBillInfo.getBillType()))) {
                            accBalInfo.setYpTiedupBal(accBalInfo.getYpTiedupBal().add(dtlBillInfo.getBillMoney()));
                        } else {
                            accBalInfo.setSpTiedupBal(accBalInfo.getSpTiedupBal().add(dtlBillInfo.getBillMoney()));
                        }
                        accBalInfo.setBal(accBalInfo.getBal().add(dtlBillInfo.getBillMoney()));
                        if (AccebillConstant.AB_BILL_TYPE_BANK.equals(String.valueOf(dtlBillInfo.getBillType()))) {
                            accBalInfo.setYpBal(accBalInfo.getYpBal().add(dtlBillInfo.getBillMoney()));
                        } else {
                            accBalInfo.setSpBal(accBalInfo.getSpBal().add(dtlBillInfo.getBillMoney()));
                        }
                    }
            }
            if (accBalInfo.getBalDate().getTime() == endDate.getTime()) {
                return true;
            }
        }
        return reBoo;
    }
    /**
     * 对每个单位的余额进行初始化操作
     *
     * @param corpId    单位ID
     * @param bankAcc   持票账户
     * @param bankAccId 持票账户ID
     * @param balDate   余额日期
     * @param balList   需要设置的余额集合
     * @param balMap    需要设置的余额map
     * @param curId     币别ID
     * @param user      用户信息
     * @return 返回操作余额对象
     */
    public AbBillAccBal initAbBillAccBal(String corpId, String bankAcc, String bankAccId, Date balDate, List<AbBillAccBal> balList, Map<String, AbBillAccBal> balMap, String curId, ICurrentUser user) {
        String key = corpId + "_" + bankAcc + "_" + balDate.getTime();
        AbBillAccBal accBal = null;
        if (balMap.containsKey(key)) {
            accBal = balMap.get(key);
        } else {
            //冗余字段新增
            CorpDTO dto = iCorpService.get(corpId);
            accBal = new AbBillAccBal(corpId, dto.getCode(), dto.getName(), bankAccId, bankAcc, balDate, curId);
            if (user != null) {
                accBal.setCreateBy(user.getLoginName());
                accBal.setCreateDate(new Date());
            }
            balMap.put(key, accBal);
            balList.add(accBal);
        }
        return accBal;
    }
}
