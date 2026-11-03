package com.bytter.bill.domain.bill.service;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.StrUtil;
import com.bytter.bill.api.bill.enums.AccebillMessageEnum;
import com.bytter.bill.domain.bill.dao.AbAccrecordsDao;
import com.bytter.bill.domain.bill.dao.AbDiscountDao;
import com.bytter.bill.domain.bill.dao.AbVoucherinfoDao;
import com.bytter.bill.domain.bill.dao.AbVoucherinfoViceDao;
import com.bytter.bill.domain.bill.entity.AbAccrecords;
import com.bytter.bill.domain.bill.entity.AbDiscount;
import com.bytter.bill.domain.bill.entity.AbVoucherinfo;
import com.bytter.bill.domain.bill.entity.AbVoucherinfoVice;
import com.bytter.biz.payment.api.service.INisBillheadService;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.core.common.exception.ParameterException;
import com.bytter.core.constant.AccebillConstant;
import com.bytter.core.utils.ApplicationContextUtil;
import com.bytter.core.utils.DateUtils;
import com.bytter.core.utils.StringUtil;
import com.bytter.core.utils.StringUtils;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import java.math.BigDecimal;
import java.util.*;

/**
 * 拆票工具类
 */
@Service("billTicketSplittingUtils")
@Transactional
public class BillTicketSplittingUtils {
    protected final Logger logger = LoggerFactory.getLogger(getClass());
    @Autowired
    private AbVoucherinfoDao abVoucherinfoDao;

    @Autowired
    private AbVoucherinfoViceDao abVoucherinfoViceDao;

    @Autowired
    private AbAccrecordsDao abAccrecordsDao;

    @Autowired
    private AbDiscountDao abDiscountDao;

    @Autowired(required = false)
    private INisBillheadService iNisBillheadService;

    @Autowired(required = false)
    private AbVoucherinfoViceService abVoucherinfoViceService;

    /**
     * 拆票功能
     * param: vIds主表id 必传
     * param: vvIds 从表id 必传
     * param: accId 流水id 必传
     * param: transMoneys 交易金额 必传
     * return: Map: key-> infoIds, viceIds ,accIds ,transMoneys为拆票后需要进行业务处理的数据key，infoIdsHold, viceIdsHold ,accIdsHold ,
     * transMoneysHold为拆票后为对应非业务处理的数据key ;valus 为对应的值。infoIdsHold, viceIdsHold ,accIdsHold ,transMoneysHold的中的null值说明此票不需要拆票或者为旧票
     */
    public Map<String, String> ticketSplittingOld(String vIds, String vvIds, String accIds, String transMoneys, ICurrentUser user) {
        if (StringUtils.isBlank(vIds) || StringUtils.isBlank(vvIds) || StringUtils.isBlank(accIds) || StringUtils.isBlank(transMoneys)) {
            throw new ParameterException(AccebillMessageEnum.ACCEBILL_MESSAGE_ERROR_VALISNOTNULL);
        }
        String[] vIdsArr = vIds.split(",");
        String[] vvIdsArr = vvIds.split(",");
        String[] accIdsArr = accIds.split(",");
        String[] transMoneysArr = transMoneys.split(",");
        Map<String, String> resultMap = Maps.newHashMap();

        List<String> infoIdsHold = Lists.newArrayList();
        List<String> viceIdsHold = Lists.newArrayList();
        List<String> accIdsHold = Lists.newArrayList();
        List<String> transMoneysHold = Lists.newArrayList();

        List<String> infoList = Lists.newArrayList();
        List<String> viceIdList = Lists.newArrayList();
        List<String> accIdList = Lists.newArrayList();
        List<String> transMoneyList = Lists.newArrayList();

        int num = vIdsArr.length;
        if ((num != vvIdsArr.length) && (num != transMoneysArr.length) && (num != accIdsArr.length)) {
            throw new ParameterException(AccebillMessageEnum.TOTAL_UNEQUAL);
        }

        for (int i = 0; i < num; i++) {
            String infoId = vIdsArr[i];
            BigDecimal money = new BigDecimal(transMoneysArr[i]);
            if (StringUtils.isBlank(infoId)) {
                throw new ParameterException(AccebillMessageEnum.VOUCHER_INFO_ID_DISC);
            }
            if (money.compareTo(new BigDecimal("0.01")) == -1) {
                throw new ParameterException(AccebillMessageEnum.BILL_SPLITTING_AMOUNT);
            }
            //查 ab_voucherInfo
            List<String> idList = Lists.newArrayList();
            idList.add(infoId);

            List<AbVoucherinfo> infoDBs = abVoucherinfoDao.list(idList);
            if (CollectionUtils.isEmpty(infoDBs)) {
                throw new ParameterException(AccebillMessageEnum.ACCEBILL_RATURATION_MESSAGE_VOUINFONOTNULL);
            }
            AbVoucherinfo abVoucherinfo = infoDBs.get(0);

            // AB_VOUCHERINFO.NEW_STM_ISSUOFPPR_IND IS '出票标志。0：老系统出票,   1：新系统出票。默认值为0，不能为空
            if (AccebillConstant.NEW_STM_ISSUOFPPR_IND_ZERO.equals(abVoucherinfo.getNewStmIssuofpprInd())) {
                //旧系统数据也要保留
                infoList.add(infoId);
                viceIdList.add(vvIdsArr[i]);
                accIdList.add(accIdsArr[i]);
                transMoneyList.add(transMoneysArr[i]);
                infoIdsHold.add(null);
                viceIdsHold.add(null);
                accIdsHold.add(null);
                transMoneysHold.add(null);
                continue;
            }
            //新票据可修改，并且前后端需判断交易金额大于0，小于票据金额，且为该票据标准金额的整数倍 , 不处理交易金额小于0和大于原金额的记录
            if (money.compareTo(BigDecimal.ZERO) == 0 || money.compareTo(abVoucherinfo.getBillMoney()) == 1) {
                continue;
            }
            //这里表示新系统出票，再判断是否进行拆票处理，通过判断前端传输的金额和原金额进行对比得出结果
            if (money.compareTo(abVoucherinfo.getBillMoney()) == 0) {
                //这里不是拆票
                infoList.add(infoId);
                viceIdList.add(vvIdsArr[i]);
                accIdList.add(accIdsArr[i]);
                transMoneyList.add(transMoneysArr[i]);
                infoIdsHold.add(null);
                viceIdsHold.add(null);
                accIdsHold.add(null);
                transMoneysHold.add(null);
                continue;
            }
            //查 ab_voucherInfo_vice
            // 是否用传进来的viceId进行查询？因为现在做解除质押的时候通过info.id 找到多条从表记录
            //List<AbVoucherinfoVice> viceDBs = abVoucherinfoViceDao.qeryVoucherinfoViceRecodesByVid(infoId);
            AbVoucherinfoVice vice = abVoucherinfoViceDao.get(vvIdsArr[i]);
            List<AbVoucherinfoVice> viceDBs = new ArrayList<>();
            // 处理从表记录
            if (vice == null) {
                throw new ParameterException(AccebillMessageEnum.ACCEBILL_RATURATION_MESSAGE_VOUVICEINFONOTNULL);
            }
            viceDBs.add(vice);

            //查流水表 ab_accrecords
            List<AbAccrecords> abAccrecords = abAccrecordsDao.queryAbAccrecordsByVidAndVVid(abVoucherinfo.getId(), vvIdsArr[i], false);
            if (CollectionUtils.isEmpty(abAccrecords)) {
                throw new ParameterException(AccebillMessageEnum.BILL_ACCRECORDS_MESSAGE_IS_NULL);
            }
            //处理主表、从表、流水表 三表记录
            Map<String, String> abAccRecordsId = saveRecords(money, abVoucherinfo, vvIdsArr[i], accIdsArr[i], viceDBs, abAccrecords, user);
            infoList.add(abAccRecordsId.get("infoId"));
            viceIdList.add(abAccRecordsId.get("viceId"));
            accIdList.add(abAccRecordsId.get("recordsId"));
            transMoneyList.add(transMoneysArr[i]);
            infoIdsHold.add(abAccRecordsId.get("infoIdsHold"));
            viceIdsHold.add(abAccRecordsId.get("viceIdsHold"));
            accIdsHold.add(abAccRecordsId.get("accIdsHold"));
            transMoneysHold.add(abAccRecordsId.get("transMoneysHold"));

        }

        resultMap.put("infoIds", list2Str(infoList));
        resultMap.put("viceIds", list2Str(viceIdList));
        resultMap.put("accIds", list2Str(accIdList));
        resultMap.put("transMoneys", list2Str(transMoneyList));
        resultMap.put("infoIdsHold", list2Str(infoIdsHold));
        resultMap.put("viceIdsHold", list2Str(viceIdsHold));
        resultMap.put("accIdsHold", list2Str(accIdsHold));
        resultMap.put("transMoneysHold", list2Str(transMoneysHold));
        return resultMap;
    }

    /**
     * ListToStr
     */
    private String list2Str(List<String> tranParam) {
        StringBuilder stringBuilder = new StringBuilder();
        for (String s : tranParam) {
            stringBuilder.append(s).append(",");
        }
        String result = new String(stringBuilder);

        return result.substring(0, result.length() - 1);
    }

    /**
     * 拆票功能
     * param: vIds主表id 必传
     * param: vvIds 从表id 必传
     * param: accId 流水id 必传
     * param: transMoneys 交易金额 必传
     * return: Map: key-> infoIds, viceIds ,accIds ,transMoneys为拆票后需要进行业务处理的数据key，infoIdsHold, viceIdsHold ,accIdsHold ,
     * transMoneysHold为拆票后为对应非业务处理的数据key ;valus 为对应的值。infoIdsHold, viceIdsHold ,accIdsHold ,transMoneysHold的中的null值说明此票不需要拆票或者为旧票
     */
    public Map<String, String> ticketSplitting(String vIds, String vvIds, String accIds, String transMoneys, ICurrentUser user) {
        return ticketSplitting(vIds, vvIds, accIds, transMoneys, null, null, user);
    }

    /**
     * 拆票功能
     * param: accId 流水id 必传
     * param: transMoneys 交易金额 必传
     * return: Map: key-> infoIds, viceIds ,accIds ,transMoneys为拆票后需要进行业务处理的数据key，infoIdsHold, viceIdsHold ,accIdsHold ,
     * transMoneysHold为拆票后为对应非业务处理的数据key ;valus 为对应的值。infoIdsHold, viceIdsHold ,accIdsHold ,transMoneysHold的中的null值说明此票不需要拆票或者为旧票
     */
    public Map<String, String> ticketSplitting(String accIds, String transMoneys,String payId, ICurrentUser user) {
        return ticketSplittingNew(accIds,transMoneys,null,null,payId,user);
    }

    /**
     * 拆票功能
     * param: vIds主表id 必传
     * param: vvIds 从表id 必传
     * param: accId 流水id 必传
     * param: transMoneys 交易金额 必传
     * param: sbbllRngStrtSnSplit 拆分子票区间开始
     * param: sbbllRngEndSnSplit 拆分子票区间结束
     * return: Map: key-> infoIds, viceIds ,accIds ,transMoneys为拆票后需要进行业务处理的数据key，infoIdsHold, viceIdsHold ,accIdsHold ,
     * transMoneysHold为拆票后为对应非业务处理的数据key ;valus 为对应的值。infoIdsHold, viceIdsHold ,accIdsHold ,transMoneysHold的中的null值说明此票不需要拆票或者为旧票
     */
    public Map<String, String> ticketSplitting(String vIds, String vvIds, String accIds, String transMoneys, String sbbllRngStrtSnSplit, String sbbllRngEndSnSplit, ICurrentUser user) {
        return ticketSplittingNew(accIds,transMoneys,sbbllRngStrtSnSplit,sbbllRngEndSnSplit,user);
    }

    /**
     * 拆票功能
     * param: vIds主表id 必传
     * param: vvIds 从表id 必传
     * param: accId 流水id 必传
     * param: transMoneys 交易金额 必传
     * param: sbbllRngStrtSnSplit 拆分子票区间开始
     * param: sbbllRngEndSnSplit 拆分子票区间结束
     * return: Map: key-> infoIds, viceIds ,accIds ,transMoneys为拆票后需要进行业务处理的数据key，infoIdsHold, viceIdsHold ,accIdsHold ,
     * transMoneysHold为拆票后为对应非业务处理的数据key ;valus 为对应的值。infoIdsHold, viceIdsHold ,accIdsHold ,transMoneysHold的中的null值说明此票不需要拆票或者为旧票
     */
    public Map<String, String> ticketSplittingNew(String accIds, String transMoneys, String sbbllRngStrtSnSplit, String sbbllRngEndSnSplit, ICurrentUser user) {
        return ticketSplittingNew(accIds,transMoneys,sbbllRngStrtSnSplit,sbbllRngEndSnSplit,null,user);
    }

    /**
     * 拆票功能
     * param: vIds主表id 必传
     * param: vvIds 从表id 必传
     * param: accId 流水id 必传
     * param: transMoneys 交易金额 必传
     * param: sbbllRngStrtSnSplit 拆分子票区间开始
     * param: sbbllRngEndSnSplit 拆分子票区间结束
     * param: payId 付款表ID
     * return: Map: key-> infoIds, viceIds ,accIds ,transMoneys为拆票后需要进行业务处理的数据key，infoIdsHold, viceIdsHold ,accIdsHold ,
     * transMoneysHold为拆票后为对应非业务处理的数据key ;valus 为对应的值。infoIdsHold, viceIdsHold ,accIdsHold ,transMoneysHold的中的null值说明此票不需要拆票或者为旧票
     */
    public Map<String, String> ticketSplittingNew(String accIds, String transMoneys, String sbbllRngStrtSnSplit, String sbbllRngEndSnSplit,String payId, ICurrentUser user) {
//        申请或者导入判断金额少于票面金额则进行拆分，基于签收流水进行拆分（A签收表票据拆分成：A1拆分金额票；A2剩余金额拆分票；A1和A2建立关联A签收表，再一次拆分就是A3拆分金额票，A2剩余金额票额度变化）
        if (StringUtils.isBlank(accIds) || StringUtils.isBlank(transMoneys)) {
            throw new ParameterException(AccebillMessageEnum.ACCEBILL_MESSAGE_ERROR_VALISNOTNULL);
        }
        String[] accIdsArr = accIds.split(",");
        String[] transMoneysArr = transMoneys.split(",");
        Map<String, String> resultMap = Maps.newHashMap();

        List<String> infoIdsHold = Lists.newArrayList();
        List<String> viceIdsHold = Lists.newArrayList();
        List<String> accIdsHold = Lists.newArrayList();
        List<String> transMoneysHold = Lists.newArrayList();

        List<String> infoList = Lists.newArrayList();
        List<String> viceIdList = Lists.newArrayList();
        List<String> accIdList = Lists.newArrayList();
        List<String> transMoneyList = Lists.newArrayList();

//        int num = vIdsArr.length;
        if (transMoneysArr.length != accIdsArr.length) {
            throw new ParameterException(AccebillMessageEnum.TOTAL_UNEQUAL);
        }

        for (int i = 0; i < accIdsArr.length; i++) {
            BigDecimal money = new BigDecimal(transMoneysArr[i]).setScale(2,BigDecimal.ROUND_HALF_UP);
            //查流水表 ab_accrecords
            AbAccrecords accrecords = abAccrecordsDao.get(accIdsArr[i], true);
            if (accrecords == null || accrecords.getId() == null) {
                throw new ParameterException(AccebillMessageEnum.BILL_ACCRECORDS_MESSAGE_IS_NULL);
            }
            if (money.compareTo(new BigDecimal("0.01")) < 0) {
                throw new ParameterException(AccebillMessageEnum.BILL_SPLITTING_AMOUNT);
            }
            AbVoucherinfo voucherinfo = abVoucherinfoDao.get(accrecords.getVid(), true);
            if (voucherinfo == null || voucherinfo.getId() == null) {
                throw new ParameterException(AccebillMessageEnum.ACCEBILL_RATURATION_MESSAGE_VOUINFONOTNULL);
            }
            //交易金额不能大于票面金额
            if (money.compareTo(voucherinfo.getBillMoney().setScale(2,BigDecimal.ROUND_HALF_UP)) > 0) {
                throw new ParameterException(AccebillMessageEnum.ACCEBILL_TICKET_MONEY_GRE_BILL_MONEY_ERROR);
            }
            // AB_VOUCHERINFO.NEW_STM_ISSUOFPPR_IND IS '出票标志。0：老系统出票,   1：新系统出票。默认值为0，不能为空
            //这里表示新系统出票，再判断是否进行拆票处理，通过判断前端传输的金额和原金额进行对比得出结果
            if (AccebillConstant.NEW_STM_ISSUOFPPR_IND_ZERO.equals(voucherinfo.getNewStmIssuofpprInd())) {
                //旧系统数据也要保留
                infoList.add(accrecords.getVid());
                viceIdList.add(accrecords.getVvid());
                accIdList.add(accIdsArr[i]);
                transMoneyList.add(transMoneysArr[i]);
                infoIdsHold.add(null);
                viceIdsHold.add(null);
                accIdsHold.add(null);
                transMoneysHold.add(null);
                continue;
            } else if (money.compareTo(voucherinfo.getBillMoney().setScale(2,BigDecimal.ROUND_HALF_UP)) == 0 && isNotEmpty(voucherinfo.getSbbllRngStrtSn())) {
                if("2".equals(accrecords.getCopyFlag())){
//                    清除批量拆分标记和拆分标志以及拆分流水标志
                    Map<String, Object> params = Maps.newHashMap();
                    params.put("ids", new String[]{accrecords.getId()});
                    params.put("copyFlag", "0");
                    params.put("splitFlg", "0");
                    params.put("oldAccIdIsNull", "0");
                    abAccrecordsDao.batchUpdate(params);
                    abAccrecordsDao.flush();
                    abAccrecordsDao.clear();
                }
                infoList.add(accrecords.getVid());
                viceIdList.add(accrecords.getVvid());
                accIdList.add(accIdsArr[i]);
                transMoneyList.add(transMoneysArr[i]);
                infoIdsHold.add(null);
                viceIdsHold.add(null);
                accIdsHold.add(null);
                transMoneysHold.add(null);
                continue;
            }

            AbVoucherinfoVice vice = abVoucherinfoViceDao.get(accrecords.getVvid(), true);
            // 处理从表记录
            if (vice == null || vice.getId() == null) {
                throw new ParameterException(AccebillMessageEnum.ACCEBILL_RATURATION_MESSAGE_VOUVICEINFONOTNULL);
            }
            //处理主表、从表、流水表 三表记录
            Map<String, String> abAccRecordsId = saveRecordsNew(money, voucherinfo, vice, accrecords, sbbllRngStrtSnSplit, sbbllRngEndSnSplit, payId, user);
            infoList.add(abAccRecordsId.get("infoId"));
            viceIdList.add(abAccRecordsId.get("viceId"));
            accIdList.add(abAccRecordsId.get("recordsId"));
            transMoneyList.add(transMoneysArr[i]);
            infoIdsHold.add(abAccRecordsId.get("infoIdsHold"));
            viceIdsHold.add(abAccRecordsId.get("viceIdsHold"));
            accIdsHold.add(abAccRecordsId.get("accIdsHold"));
            transMoneysHold.add(abAccRecordsId.get("transMoneysHold"));
        }
        resultMap.put("infoIds", StrUtil.join(",", infoList));
        resultMap.put("viceIds", StrUtil.join(",", viceIdList));
        resultMap.put("accIds", StrUtil.join(",", accIdList));
        resultMap.put("transMoneys", StrUtil.join(",", transMoneyList));
        resultMap.put("infoIdsHold", StrUtil.join(",", infoIdsHold));
        resultMap.put("viceIdsHold", StrUtil.join(",", viceIdsHold));
        resultMap.put("accIdsHold", StrUtil.join(",", accIdsHold));
        resultMap.put("transMoneysHold", StrUtil.join(",", transMoneysHold));
        return resultMap;
    }

    @Transactional
    public Map<String, String> saveRecordsNew(BigDecimal transMoney, AbVoucherinfo info, AbVoucherinfoVice vice, AbAccrecords accrecords
            , String sbbllRngStrtSnSplit, String sbbllRngEndSnSplit, String payId, ICurrentUser user) {
        oldAccIdSynchOp(accrecords.getId());
        AbVoucherinfo infoSplit = info;
        AbVoucherinfoVice viceSplit = vice;
        AbAccrecords accrecordsSplit = accrecords;
        Map<String, String> resultMap = Maps.newHashMap();
        //1、先备份未拆分数据
        if (isEmpty(vice.getOldDraftId())) {
            //批量导入需判断是否被拆分过，是则取到可用剩余拆分金额进行拆分操作
            List<AbVoucherinfo> voucherinfoSplitList = abVoucherinfoDao.listByOldDraftId(vice.getId());
            if (CollectionUtil.isNotEmpty(voucherinfoSplitList)) {
                //获取已拆分并且可操作的明细数据
                List<String> vidList = Lists.newArrayList();
                for (AbVoucherinfo voucherinfo : voucherinfoSplitList) {
                    if ((isEmpty(sbbllRngStrtSnSplit)
                            && voucherinfo.getBillMoney().setScale(2,BigDecimal.ROUND_HALF_UP).compareTo(transMoney) >= 0)
                            || (isNotEmpty(sbbllRngStrtSnSplit)
                            && isNotEmpty(sbbllRngEndSnSplit)
                            && isNotEmpty(voucherinfo.getSbbllRngStrtSn())
                            && Long.parseLong(sbbllRngStrtSnSplit) >= Long.parseLong(voucherinfo.getSbbllRngStrtSn())
                            && Long.parseLong(sbbllRngEndSnSplit) <= Long.parseLong(voucherinfo.getSbbllRngEndSn()))) {
                        vidList.add(voucherinfo.getId());
                    }
                }
                List<AbAccrecords> accrecordsSplitList = abAccrecordsDao.listByAvaidSplit(vidList, vice.getCorpId());
                if (CollectionUtil.isEmpty(accrecordsSplitList)) {
                    this.logger.error("vidList:" + vidList +"  ,CorpId:"+vice.getCorpId());
                    throw new ParameterException(AccebillMessageEnum.ACCEBILL_MESSAGE_ERROR_DATAISUPDATEERROR);
                }
                accrecordsSplit = accrecordsSplitList.get(0);
                infoSplit = abVoucherinfoDao.get(accrecordsSplit.getVid(), true);
                viceSplit = abVoucherinfoViceDao.get(accrecordsSplit.getVvid(), true);
            } else {
                //未拆分过则进行主信息备份产生拆分数据记录
//            1.1备份主信息
                infoSplit = copyVoucherinfo(info, info.getBillMoney(), user);
                infoSplit.setOrgSbbllRngStrtSn(info.getSbbllRngStrtSn());
                infoSplit.setOrgSbbllRngEndSn(info.getSbbllRngEndSn());
                infoSplit.setOldDraftId(info.getId());
//                infoSplit.setSbbllRngStrtSn(" ");
//                infoSplit.setSbbllRngEndSn(" ");
                abVoucherinfoDao.save(infoSplit);
                abVoucherinfoDao.flush();
                abVoucherinfoDao.clear();
//            1.2备份从表数据
                viceSplit = copyVoucherinfoVice(vice, infoSplit.getId(), vice.getId(), user);
                abVoucherinfoViceDao.save(viceSplit);
                //原数据备份变已拆分
                Map<String, Object> params = Maps.newHashMap();
                params.put("ids", new String[]{vice.getId()});
                params.put("validSign", AccebillConstant.VALID_SIGN_SPLIT_TICKET);
                params.put("validSignNot", AccebillConstant.VALID_SIGN_SPLIT_TICKET);
                int size = abVoucherinfoViceDao.batchUpdate(params);
                if (size != 1) {
                    this.logger.error("params:" + params +"  ,size:"+size);
                    throw new ParameterException(AccebillMessageEnum.ACCEBILL_MESSAGE_ERROR_DATAISUPDATEERROR);
                }
//            1.3备份流水表数据
                accrecordsSplit = copyAccrecords(accrecords, null, infoSplit.getId(), viceSplit.getId(), user);
                //如果贴现办理流水则需要复制上一个库存流水
                if (AccebillConstant.BRIEF_STATUS_6011.equals(accrecords.getBriefId())) {
                    AbAccrecords accrecordsLast = abAccrecordsDao.get(accrecords.getLastid(), true);
                    AbAccrecords accrecordsLastCopy = copyAccrecords(accrecordsLast, null, infoSplit.getId(), viceSplit.getId(), user);
                    abAccrecordsDao.save(accrecordsLastCopy);
                    accrecordsSplit.setLastid(accrecordsLastCopy.getId());
                }
                accrecordsSplit.setCopyAccId(null);
                abAccrecordsDao.save(accrecordsSplit);
                //如果贴现办理流水则需要复制贴现表的数据
                if (AccebillConstant.BRIEF_STATUS_6011.equals(accrecords.getBriefId())) {
                    AbDiscount abDiscountDb = abDiscountDao.getAbDiscountByRecordId(accrecords.getId());
                    AbDiscount newAbDis = new AbDiscount();
                    com.bytter.core.utils.BeanUtils.copyNotNullProperties(abDiscountDb,newAbDis);
                    newAbDis.setId(null);
                    newAbDis.setRecordId(accrecordsSplit.getId());
                    abDiscountDao.save(newAbDis);
                }
//                //如果占用金额大于0则需要设置付款挂接的持有流水ID为拆分出来的持有流水ID
//                if (viceSplit.getPayLockMoney() != null
//                        && viceSplit.getPayLockMoney().compareTo(BigDecimal.ZERO) > 0) {
//                    iNisBillheadService.updateNisBillByBillId(accrecords.getId(), accrecordsSplit.getId());
//                }
                //原数据备份变已拆分
                params = Maps.newHashMap();
                params.put("ids", new String[]{accrecords.getId()});
                params.put("splitFlg", "1");
                params.put("validSign", AccebillConstant.VALID_SIGN_SPLIT_TICKET);
                params.put("statusNew", AccebillConstant.AB_BILL_BUS_STATUS96);
                size = abAccrecordsDao.batchUpdate(params);
                if (size != 1) {
                    this.logger.error("params:" + params +"  ,size:"+size);
                    throw new ParameterException(AccebillMessageEnum.ACCEBILL_MESSAGE_ERROR_DATAISUPDATEERROR);
                }
                abAccrecordsDao.flush();
                abAccrecordsDao.clear();
            }
        }
        //2、对已拆分票据进行金额拆分
        if (StringUtils.isNotEmpty(sbbllRngStrtSnSplit)
                && StringUtils.isNotEmpty(infoSplit.getSbbllRngStrtSn())
                && Long.parseLong(sbbllRngStrtSnSplit) != Long.parseLong(infoSplit.getSbbllRngStrtSn())) {
            throw new ParameterException(AccebillMessageEnum.ACCEBILL_TICKET_SBBLLRNG_NOT_BIGIN_ERROR);
        }

        //拆分金额大于可拆分金额不允许拆分
        if (infoSplit.getBillMoney().setScale(2,BigDecimal.ROUND_HALF_UP).compareTo(transMoney) < 0) {
            throw new ParameterException(AccebillMessageEnum.ACCEBILL_TICKET_TRANSMONEY_GT_BILLMONEY_ERROR);
        } else if (infoSplit.getBillMoney().setScale(2,BigDecimal.ROUND_HALF_UP).compareTo(transMoney) == 0) {
            Map<String, Object> params = Maps.newHashMap();
            params.put("ids", new String[]{accrecordsSplit.getId()});
            params.put("splitFlg", "1");
            params.put("copyFlag", "2");
            String oldAccId = accrecordsSplit.getOldAccId();
            if (isNotEmpty(infoSplit.getSbbllRngStrtSn()) || StrUtil.isEmpty(oldAccId)) {
                oldAccId = accrecordsSplit.getId();
            }
            params.put("oldAccIdNew", oldAccId);
            abAccrecordsDao.batchUpdate(params);

            //拆分金额和可拆分金额一致的时候直接设置为可拆分票据
            if (isEmpty(infoSplit.getSbbllRngStrtSn())
                    && isNotEmpty(sbbllRngStrtSnSplit)) {
                params = Maps.newHashMap();
                infoSplit.setSbbllRngStrtSn(sbbllRngStrtSnSplit);
                infoSplit.setSbbllRngEndSn(sbbllRngEndSnSplit);
                params.put("ids", new String[]{infoSplit.getId()});
                params.put("sbbllRngStrtSnNew", infoSplit.getSbbllRngStrtSn());
                params.put("sbbllRngEndSnNew", infoSplit.getSbbllRngEndSn());
                abVoucherinfoDao.batchUpdate(params);
            }

            //付款选票占用额度不为空则进行从表可用
            if(viceSplit.getPayLockMoney()!=null
                    && viceSplit.getPayLockMoney().compareTo(BigDecimal.ZERO)>0){
                params=new HashMap<>();
                params.put("vid",viceSplit.getId());
                params.put("validSignNew",AccebillConstant.VALID_SIGN_TRUE);
                abVoucherinfoViceDao.updatePayLockMoney(params);
            }

            resultMap.put("infoId", infoSplit.getId());
            resultMap.put("infoIdsHold", infoSplit.getId());
            resultMap.put("transMoneysHold", NumberUtil.formatPercent(infoSplit.getBillMoney().doubleValue(), 2));
            resultMap.put("viceId", viceSplit.getId());
            resultMap.put("viceIdsHold", viceSplit.getId());
            resultMap.put("recordsId", accrecordsSplit.getId());
            resultMap.put("accIdsHold", accrecordsSplit.getId());
            abAccrecordsDao.flush();
            abAccrecordsDao.clear();
            return resultMap;
        }

//      2、复制可拆分数据并且设置拆分金额
//      复制主信息
        AbVoucherinfo infoTrans = copyVoucherinfo(infoSplit, transMoney, user);
        String sbbllRngStrtSnOther = null;
        if (isNotEmpty(sbbllRngStrtSnSplit)) {
            infoTrans.setSbbllRngStrtSn(sbbllRngStrtSnSplit);
            infoTrans.setSbbllRngEndSn(sbbllRngEndSnSplit);
            infoTrans.setOrgSbbllRngStrtSn(infoSplit.getSbbllRngStrtSn());
            infoTrans.setOrgSbbllRngEndSn(infoSplit.getSbbllRngEndSn());
            //剩余子票区间的开始为交易的子票区间结束+1
            sbbllRngStrtSnOther = StrUtil.fill(new BigDecimal(sbbllRngEndSnSplit)
                    .add(new BigDecimal("1"))
                    .setScale(0, BigDecimal.ROUND_HALF_UP).toString(), '0', 12, true);
        } else {
            infoTrans.setSbbllRngStrtSn(" ");
            infoTrans.setSbbllRngEndSn(" ");
            if (isEmpty(infoSplit.getSbbllRngStrtSn())) {
                infoTrans.setOrgSbbllRngStrtSn(infoSplit.getOrgSbbllRngStrtSn());
                infoTrans.setOrgSbbllRngEndSn(infoSplit.getOrgSbbllRngEndSn());
            } else {
                infoTrans.setOrgSbbllRngStrtSn(infoSplit.getSbbllRngStrtSn());
                infoTrans.setOrgSbbllRngEndSn(infoSplit.getSbbllRngEndSn());
            }
        }
        abVoucherinfoDao.save(infoTrans);
//      复制从表数据
        AbVoucherinfoVice viceTrans = copyVoucherinfoVice(viceSplit, infoTrans.getId(), viceSplit.getOldDraftId(), user);
        if(StrUtil.isNotEmpty(payId)){
            viceTrans.setPayLockMoney(transMoney);
        }
        viceTrans.setValidSign(AccebillConstant.VALID_SIGN_TRUE);
        //付款选票占用额度不为空则进行占用额度扣减
        if(viceTrans.getPayLockMoney()!=null && StrUtil.isNotEmpty(payId)){
            Map<String,Object> params=new HashMap<>();
            params.put("vid",viceSplit.getId());
            params.put("subtractMoney",transMoney);
            abVoucherinfoViceDao.updatePayLockMoney(params);
        }
        abVoucherinfoViceDao.save(viceTrans);
        String oldAccId = accrecordsSplit.getOldAccId();
        if (isNotEmpty(infoSplit.getSbbllRngStrtSn()) || StrUtil.isEmpty(oldAccId)) {
            oldAccId = accrecordsSplit.getId();
        }

        //如果持有拆分数据有子票区间且oldAccId有值则删除oldAccId值
        if (isNotEmpty(infoSplit.getSbbllRngStrtSn())
                && (isNotEmpty(accrecordsSplit.getOldAccId())||isNotEmpty(accrecordsSplit.getCopyAccId()))) {
            Map<String, Object> params = Maps.newHashMap();
            params.put("ids", new String[]{accrecordsSplit.getId()});
            params.put("oldAccIdIsNull", true);
            params.put("copyAccIdIsNull", true);
//            params.put("oldAccIdOld", accrecordsSplit.getOldAccId());
//            params.put("copyAccId", accrecordsSplit.getCopyAccId());
            abAccrecordsDao.batchUpdate(params);
        }

//      复制流水表数据
        AbAccrecords accrecordsTrans = copyAccrecords(accrecordsSplit, oldAccId, infoTrans.getId(), viceTrans.getId(), user);
        //如果贴现办理流水则需要复制上一个库存流水
        if (AccebillConstant.BRIEF_STATUS_6011.equals(accrecords.getBriefId())) {
            AbAccrecords accrecordsLast = abAccrecordsDao.get(accrecordsSplit.getLastid(), true);
            AbAccrecords accrecordsLastCopy = copyAccrecords(accrecordsLast, null, infoTrans.getId(), viceTrans.getId(), user);
            abAccrecordsDao.save(accrecordsLastCopy);
            accrecordsTrans.setLastid(accrecordsLastCopy.getId());
        }
        accrecordsTrans.setSplitFlg("1");
        accrecordsTrans.setCopyFlag("2");
        abAccrecordsDao.save(accrecordsTrans);

//      3、设置剩余金额和剩余子票区间
        Map<String, Object> params = new HashMap<>();
        params.put("ids", new String[]{infoSplit.getId()});
        params.put("billMoneySubtract", transMoney);
        params.put("billMoneyOld", infoSplit.getBillMoney());
        if (isNotEmpty(sbbllRngStrtSnOther)) {
            params.put("sbbllRngStrtSnNew", sbbllRngStrtSnOther);
            if (isEmpty(infoSplit.getSbbllRngEndSn())) {
                //根据开始子票区间设置结束子票区间
                String sbbllRngEndSnNew = StrUtil.fill(infoSplit.getBillMoney()
                        .multiply(new BigDecimal("100"))
                        .add(new BigDecimal(sbbllRngStrtSnOther))
                        .subtract(new BigDecimal("1"))
                        .setScale(0, BigDecimal.ROUND_HALF_UP).toString(), '0', 12, true);
                params.put("sbbllRngEndSnNew", sbbllRngEndSnNew);
            }
        }

        //如果有子票区间设置原子票区间
        if (isNotEmpty(infoSplit.getSbbllRngStrtSn())) {
            params.put("orgSbbllRngStrtSnNew", infoSplit.getSbbllRngStrtSn());
            params.put("orgSbbllRngEndSnNew", infoSplit.getSbbllRngEndSn());
        }

        //拆分以后设置可用子票区间为空
        if (isEmpty(sbbllRngStrtSnOther) && isNotEmpty(infoSplit.getSbbllRngStrtSn())) {
            params.put("sbbllRngStrtSnNew", " ");
            params.put("sbbllRngEndSnNew", " ");
        }
        if (isNotEmpty(infoSplit.getSbbllRngStrtSn())) {
            params.put("sbbllRngStrtSnOld", infoSplit.getSbbllRngStrtSn());
            params.put("sbbllRngEndSnOld", infoSplit.getSbbllRngEndSn());
        }
        int size = abVoucherinfoDao.batchUpdate(params);
        if (size != 1) {
            this.logger.error("params:" + params +"  ,size:"+size);
            throw new ParameterException(AccebillMessageEnum.ACCEBILL_MESSAGE_ERROR_DATAISUPDATEERROR);
        }
        resultMap.put("infoId", infoTrans.getId());
        resultMap.put("infoIdsHold", infoSplit.getId());
        BigDecimal otherBillMoney = infoSplit.getBillMoney().subtract(transMoney);
        resultMap.put("transMoneysHold", NumberUtil.formatPercent(otherBillMoney.doubleValue(), 2));
        resultMap.put("viceId", viceTrans.getId());
        resultMap.put("viceIdsHold", viceSplit.getId());
        resultMap.put("recordsId", accrecordsTrans.getId());
        resultMap.put("accIdsHold", accrecordsSplit.getId());
        abAccrecordsDao.flush();
        abAccrecordsDao.clear();
        return resultMap;
    }

    /**
     * 复制票据主信息表
     *
     * @param info
     * @param billMoney  票面金额
     * @param user       用户信息
     * @return
     */
    private AbVoucherinfo copyVoucherinfo(AbVoucherinfo info, BigDecimal billMoney, ICurrentUser user) {
        AbVoucherinfo infoSplit = new AbVoucherinfo();
        BeanUtil.copyProperties(info, infoSplit);
        infoSplit.setId(null);
        infoSplit.setBillMoney(billMoney);
//        infoSplit.setOldDraftId(oldDraftId);
        infoSplit.setUpdateBy(user == null ? null : user.getLoginName());
        infoSplit.setUpdateName(user == null ? null : user.getUsername());
        infoSplit.setUpdateDate(new Date());
        return infoSplit;
    }

    /**
     * 复制票据从表
     *
     * @param vice
     * @param vid  票据主信息ID
     * @param oldDraftId 原拆分从表ID
     * @param user 用户信息
     * @return
     */
    private AbVoucherinfoVice copyVoucherinfoVice(AbVoucherinfoVice vice, String vid,String oldDraftId, ICurrentUser user) {
        AbVoucherinfoVice viceSplit = new AbVoucherinfoVice();
        BeanUtil.copyProperties(vice, viceSplit);
        viceSplit.setId(null);
        viceSplit.setVid(vid);
        viceSplit.setOldDraftId(oldDraftId);
        viceSplit.setUpdateBy(user == null ? null : user.getLoginName());
        viceSplit.setUpdateName(user == null ? null : user.getUsername());
        viceSplit.setUpdateDate(new Date());
        return viceSplit;
    }

    /**
     * 复制票据流水表
     *
     * @param accrecords
     * @param oldAccId   原拆分表ID
     * @param vid        票据主信息ID
     * @param vvid       票据主从表ID
     * @param user       用户信息
     * @return
     */
    private AbAccrecords copyAccrecords(AbAccrecords accrecords, String oldAccId, String vid, String vvid, ICurrentUser user) {
        AbAccrecords accrecordsSplit = new AbAccrecords();
        BeanUtil.copyProperties(accrecords, accrecordsSplit);
        accrecordsSplit.setId(null);
        accrecordsSplit.setOldAccId(oldAccId);
        accrecordsSplit.setVid(vid);
        accrecordsSplit.setVvid(vvid);
        accrecordsSplit.setCreateDate(DateUtils.addSecond(new Date(),-1));
        accrecordsSplit.setApplyDate(new Date());
        accrecordsSplit.setUpdateBy(user == null ? null : user.getLoginName());
        accrecordsSplit.setUpdateName(user == null ? null : user.getUsername());
        accrecordsSplit.setUpdateDate(new Date());
        return accrecordsSplit;
    }

    @Transactional
    public Map<String, String> saveRecords(BigDecimal money, AbVoucherinfo info, String oldViceId, String oldAccId, List<AbVoucherinfoVice> viceDBs
            , List<AbAccrecords> records, ICurrentUser user) {

        Map<String, String> resultMap = Maps.newHashMap();
        //先处理 AbVoucherinfo 表
        AbVoucherinfo infoFirstNew = new AbVoucherinfo();
        AbVoucherinfo infoSecondNew = new AbVoucherinfo();

        BeanUtils.copyProperties(info, infoFirstNew, "id");
        BeanUtils.copyProperties(info, infoSecondNew, "id");

        // (一张票，优先交易前面的金额,eg:票面金额为100。第一次拆票金额为10，即0.01-10 先交易，10.01-100保留下来以后再交易)
        infoFirstNew.setBillMoney(money);

        //子票区间结束序号。旧票据默认0，新票据 12位长度，不足12位前面补0。开票阶段，旧票据默认值为0，新票后台赋值为票据金额*100不足12位前面补0。
        //判断是否是第一次拆票，
        if (info.getSbbllRngStrtSn().equals("000000000001")) {
            infoFirstNew.setSbbllRngEndSn(setSbbllRngSN(money, null, 1));
        } else {
            String sbbllRngStrtSn = info.getSbbllRngStrtSn();
            String s = setSbbllRngSN(money, null, 1);
            BigDecimal addResult = new BigDecimal(sbbllRngStrtSn).add(new BigDecimal(s)).subtract(new BigDecimal("1"));
            String s1 = String.valueOf(addResult);
            StringBuilder result = new StringBuilder();
            if (s1.length() < 12) {
                int forTime = 12 - s1.length();
                for (int i = 0; i < forTime; i++) {
                    result.append("0");
                }
            }
            result.append(s1);
            infoFirstNew.setSbbllRngEndSn(String.valueOf(result));
        }
        //拆分出来的两张票，票据主表AB_VOUCHERINFO中的字段old_draft_id设为原票据id，需要进行交易的子票，根据原有流程进行处理，
        // 不需要交易的子票将其从表AB_VOUCHERINFO_VICE中的字段VALID_SIGN设置为3拆票中（暂时锁定此部分票据）。原票据，从表AB_VOUCHERINFO_VICE中的字段VALID_SIGN设置为3已拆票（新增类型）。
        infoFirstNew.setOldDraftId(info.getId());
        infoFirstNew.setCreateBy(user == null ? null : user.getLoginName());
        infoFirstNew.setUpdateBy(user == null ? null : user.getLoginName());
        infoFirstNew.setCreateName(user == null ? null : user.getUsername());
        infoFirstNew.setUpdateName(user == null ? null : user.getUsername());
        infoFirstNew.setCreateDate(new Date());
        infoFirstNew.setUpdateDate(new Date());
        abVoucherinfoDao.saveOrUpdate(infoFirstNew);
        resultMap.put("infoId", infoFirstNew.getId());

        infoSecondNew.setBillMoney(info.getBillMoney().subtract(money));
        BigDecimal sbbllRngStrtSn = new BigDecimal(infoFirstNew.getSbbllRngEndSn()).add(new BigDecimal("1"));
        String s1 = String.valueOf(sbbllRngStrtSn);
        StringBuilder result = new StringBuilder();
        if (s1.length() < 12) {
            int forTime = 12 - s1.length();
            for (int i = 0; i < forTime; i++) {
                result.append("0");
            }
        }
        result.append(s1);
        infoSecondNew.setSbbllRngStrtSn(String.valueOf(result));
        infoSecondNew.setOldDraftId(info.getId());
        infoSecondNew.setCreateBy(user == null ? null : user.getLoginName());
        infoSecondNew.setUpdateBy(user == null ? null : user.getLoginName());
        infoSecondNew.setCreateName(user == null ? null : user.getUsername());
        infoSecondNew.setUpdateName(user == null ? null : user.getUsername());
        infoSecondNew.setCreateDate(new Date());
        infoSecondNew.setUpdateDate(new Date());
        abVoucherinfoDao.saveOrUpdate(infoSecondNew);
        resultMap.put("infoIdsHold", infoSecondNew.getId());
        resultMap.put("transMoneysHold", infoSecondNew.getBillMoney().toString());

        //先处理拆票后需要交易的部分
        List<AbVoucherinfoVice> handleVices = Lists.newArrayList();
        List<AbVoucherinfoVice> viceList = Lists.newArrayList();
        for (AbVoucherinfoVice viceDB : viceDBs) {
            handleVices.add(viceDB);
            viceList.add(viceDB);
        }
        List<AbAccrecords> handleAccs = Lists.newArrayList();
        List<AbAccrecords> accList = Lists.newArrayList();
        List<AbAccrecords> handleAccs2 = Lists.newArrayList();
        List<AbAccrecords> accList2 = Lists.newArrayList();
        Map<String, String> idMaphandle = new HashMap<>();
        Map<String, String> idMapacc = new HashMap<>();
        for (AbAccrecords record : records) {
            handleAccs.add(record);
            accList.add(record);
        }
        //拆票后，需要交易的处理
        for (AbVoucherinfoVice handleVice : handleVices) {
            AbVoucherinfoVice newObj = new AbVoucherinfoVice();
            BeanUtils.copyProperties(handleVice, newObj, "id");
            //处理拆票之后需要交易的从表记录
            newObj.setVid(infoFirstNew.getId());
            //newObj.setCreateBy(user == null ? null : user.getUsername());
            newObj.setUpdateDate(new Date());
            newObj.setCreateDate(new Date());
            //newObj.setUpdateBy(user == null ? null : user.getLoginName());

            newObj.setCreateBy(user == null ? null : user.getLoginName());
            newObj.setUpdateBy(user == null ? null : user.getLoginName());
            newObj.setCreateName(user == null ? null : user.getUsername());
            newObj.setUpdateName(user == null ? null : user.getUsername());

            List<AbVoucherinfoVice> saveList = Lists.newArrayList();
            saveList.add(newObj);
            abVoucherinfoViceDao.batchSave(saveList);
            if (oldViceId.equals(handleVice.getId())) {
                resultMap.put("viceId", newObj.getId());
            }

            //String firstLastId = null;
            for (AbAccrecords handleAcc : handleAccs) {
                if (!handleAcc.getVvid().equals(handleVice.getId())) {
                    continue;
                }
                AbAccrecords recordsFirstNews = new AbAccrecords();
                BeanUtils.copyProperties(handleAcc, recordsFirstNews, "id");
                recordsFirstNews.setVid(infoFirstNew.getId());
                recordsFirstNews.setVvid(newObj.getId());
               /* if (StringUtils.isNotBlank(firstLastId)) {
                    recordsFirstNews.setLastid(firstLastId);
                }*/
                recordsFirstNews.setCreateDate(new Date());
                recordsFirstNews.setUpdateDate(new Date());
                recordsFirstNews.setCreateBy(user == null ? null : user.getLoginName());
                recordsFirstNews.setUpdateBy(user == null ? null : user.getLoginName());
                recordsFirstNews.setCreateName(user == null ? null : user.getUsername());
                recordsFirstNews.setUpdateName(user == null ? null : user.getUsername());
                recordsFirstNews.setCopyFlag("1");
                recordsFirstNews.setCopyAccId(handleAcc.getId());

                List<AbAccrecords> recordsList = Lists.newArrayList();
                recordsList.add(recordsFirstNews);
                abAccrecordsDao.batchSave(recordsList);
                handleAccs2.add(recordsFirstNews);
                idMaphandle.put(handleAcc.getId(), recordsFirstNews.getId());
                if (oldAccId.equals(handleAcc.getId())) {
                    resultMap.put("recordsId", recordsFirstNews.getId());
                }
            }
        }
        //拆票后，不需要交易的处理
        for (AbVoucherinfoVice abVoucherinfoVice : viceList) {
            AbVoucherinfoVice newObj = new AbVoucherinfoVice();
            BeanUtils.copyProperties(abVoucherinfoVice, newObj, "id");
            newObj.setVid(infoSecondNew.getId());
            //validSign为1有效的才需要锁定，其他状态的数据保持原状
            if (AccebillConstant.AB_BILL_VALID_SIGN_1.equals(abVoucherinfoVice.getValidSign())) {
                //不需要交易的子票也修改为3，用于区分其他状态
                newObj.setValidSign(AccebillConstant.VALID_SIGN_SPLIT_TICKET);
            }
            //newObj.setCreateBy(user == null ? null : user.getUsername());
            newObj.setUpdateDate(new Date());
            newObj.setCreateDate(new Date());
            //newObj.setCreateBy(user == null ? null : user.getLoginName());

            newObj.setCreateBy(user == null ? null : user.getLoginName());
            newObj.setUpdateBy(user == null ? null : user.getLoginName());
            newObj.setCreateName(user == null ? null : user.getUsername());
            newObj.setUpdateName(user == null ? null : user.getUsername());

            List<AbVoucherinfoVice> saveList = Lists.newArrayList();
            saveList.add(newObj);
            abVoucherinfoViceDao.batchSave(saveList);

            if (oldViceId.equals(abVoucherinfoVice.getId())) {
                resultMap.put("viceIdsHold", newObj.getId());
            }

            //String firstLastId = null;
            for (AbAccrecords abAccrecords : accList) {
                if (!abAccrecords.getVvid().equals(abVoucherinfoVice.getId())) {
                    continue;
                }
                AbAccrecords recordsTwoNews = new AbAccrecords();
                BeanUtils.copyProperties(abAccrecords, recordsTwoNews, "id");
                recordsTwoNews.setVid(infoSecondNew.getId());
                recordsTwoNews.setVvid(newObj.getId());
                /*if (StringUtils.isNotBlank(firstLastId)) {
                    recordsTwoNews.setLastid(firstLastId);
                }*/
                recordsTwoNews.setCreateDate(new Date());
                recordsTwoNews.setUpdateDate(new Date());
                //recordsFirstNews.setCreateBy(user == null ? null : user.getUsername());

                recordsTwoNews.setCreateBy(user == null ? null : user.getLoginName());
                recordsTwoNews.setUpdateBy(user == null ? null : user.getLoginName());
                recordsTwoNews.setCreateName(user == null ? null : user.getUsername());
                recordsTwoNews.setUpdateName(user == null ? null : user.getUsername());
                recordsTwoNews.setCopyFlag("1");
                recordsTwoNews.setCopyAccId(abAccrecords.getId());

                List<AbAccrecords> recordsList = Lists.newArrayList();
                recordsList.add(recordsTwoNews);
                abAccrecordsDao.batchSave(recordsList);
                accList2.add(recordsTwoNews);
                idMapacc.put(abAccrecords.getId(), recordsTwoNews.getId());
                if (oldAccId.equals(abAccrecords.getId())) {
                    resultMap.put("accIdsHold", recordsTwoNews.getId());
                }
            }
        }

        List<AbVoucherinfoVice> updVvList = new ArrayList<>();
        //原数据的处理
        for (AbVoucherinfoVice viceDB : viceDBs) {
            //validSign为1有效的才需要锁定，其他状态的数据保持原状
            if (AccebillConstant.AB_BILL_VALID_SIGN_0.equals(viceDB.getValidSign())) {
                continue;
            }
            //viceDB.setUpdateBy(user == null ? viceDB.getUpdateBy() : user.getUsername());
            //viceDB.setCreateBy(user == null ? null : user.getLoginName());
            viceDB.setUpdateBy(user == null ? null : user.getLoginName());
            //viceDB.setCreateName(user == null ? null : user.getUsername());
            viceDB.setUpdateName(user == null ? null : user.getUsername());
            viceDB.setUpdateDate(new Date());
            viceDB.setValidSign(AccebillConstant.VALID_SIGN_SPLIT_TICKET);
            updVvList.add(viceDB);
        }
        abVoucherinfoViceDao.batchUpdate(updVvList);

        Map<String, Integer> indexMap = new HashMap<>();
        for (int i = 0; i < records.size(); i++) {
            AbAccrecords record = records.get(i);
            indexMap.put(record.getId(), i);
            record.setCreateBy(user == null ? record.getCreateBy() : user.getUsername());
            record.setUpdateDate(new Date());
            record.setStatus(AccebillConstant.ACCE_BILL_STATUS_96);
        }
        abAccrecordsDao.batchUpdate(records);

        String handId = "";
        for (int i = 0; i < handleAccs2.size(); i++) {
            AbAccrecords a = handleAccs2.get(i);
            if (StringUtils.isBlank(a.getLastid())) {
                continue;
            }
            handId = idMaphandle.get(a.getLastid());
            a.setLastid(handId);
        }
        abAccrecordsDao.batchUpdate(handleAccs2);

        String accId = "";
        for (int i = 0; i < accList2.size(); i++) {
            AbAccrecords a = accList2.get(i);
            if (StringUtils.isBlank(a.getLastid())) {
                continue;
            }
            accId = idMapacc.get(a.getLastid());
            a.setLastid(accId);
        }
        abAccrecordsDao.batchUpdate(accList2);

        return resultMap;
    }

    /**
     * 处理流水表
     */
    @Transactional
    public String saveOfAbAccRecords(List<AbAccrecords> accrecordsList, AbVoucherinfo info, AbVoucherinfoVice vice, String flag) {
        int maxSize = accrecordsList.size();
        List<AbAccrecords> save2DbResult = Lists.newArrayList();
        for (int i = 0; i < maxSize; i++) {
            AbAccrecords abAccrecords = new AbAccrecords();
            BeanUtils.copyProperties(accrecordsList.get(i), abAccrecords);
            List<AbAccrecords> saveOrUpdateObj = Lists.newArrayList();
            if (AccebillConstant.DATE_FLAG_NEW.equals(flag)) {
                abAccrecords.setId(null);
                abAccrecords.setVid(info.getId());
                abAccrecords.setVvid(vice.getId());

                saveOrUpdateObj.add(abAccrecords);
                abAccrecordsDao.batchSaveOrUpdate(saveOrUpdateObj);
            }
            if (AccebillConstant.DATE_FLAG_RES.equals(flag)) {
                abAccrecords.setStatus(Integer.valueOf(AccebillConstant.ABLOG_LOGTYPE_SPLIT_T));
                saveOrUpdateObj.add(abAccrecords);
                abAccrecordsDao.batchUpdate(saveOrUpdateObj);
            }
            //保存
            save2DbResult.add(abAccrecords);
            if (maxSize - i > 1) {
                accrecordsList.get(i + 1).setLastid(abAccrecords.getId());
            }
        }
        return save2DbResult.get(save2DbResult.size() - 1).getId();
    }

    /**
     * flag：1 开始，2结束
     *
     * @param beginMoney
     * @param endMoney
     * @param flag
     * @return
     */
    private String setSbbllRngSN(BigDecimal beginMoney, BigDecimal endMoney, int flag) {
        StringBuilder result = new StringBuilder();
        if (flag == 1) {
            if (ObjectUtils.isEmpty(beginMoney)) {
                throw new ParameterException(AccebillMessageEnum.BILL_SPLIT_TICKET_MONEY_IS_NULL);
            }
            String sbbllRngSN = String.valueOf((beginMoney.multiply(BigDecimal.TEN).multiply(BigDecimal.TEN)).longValue());
            if (sbbllRngSN.length() < 12) {
                int forTime = 12 - sbbllRngSN.length();
                for (int i = 0; i < forTime; i++) {
                    result.append("0");
                }
            }
            result.append(new StringBuilder(sbbllRngSN));
        }
        if (flag == 2) {
            if (ObjectUtils.isEmpty(endMoney)) {
                if (ObjectUtils.isEmpty(endMoney)) {
                    throw new ParameterException(AccebillMessageEnum.BILL_SPLIT_TICKET_MONEY_IS_NULL);
                }
            }
            String sbbllRngSN = String.valueOf((endMoney.multiply(BigDecimal.TEN).multiply(BigDecimal.TEN).add(BigDecimal.ONE)).longValue());
            if (sbbllRngSN.length() < 12) {
                int forTime = 12 - sbbllRngSN.length();
                for (int i = 0; i < forTime; i++) {
                    result.append("0");
                }
            }
            result.append(new StringBuilder(sbbllRngSN));

        }
        return String.valueOf(result);
    }

    /**
     * 打回退回拆分业务处理
     *
     * @param abAccrecords
     */
    public void splitRepulseOp(AbAccrecords abAccrecords) {
        splitRepulseOp(abAccrecords, null);
    }

    /**
     * 打回退回拆分业务处理
     *
     * @param abAccrecords
     * @param mergeOpt     回退是否合并，0不合并，1合并
     */
    public void splitRepulseOp(AbAccrecords abAccrecords, String mergeOpt) {
//        1、线下票据审批打回和线上票据审批打回规则一致；
//        2、有子票区间的票据打回不进行票据合并；
//        3、如果拆分以后单据都已经提交无可以用待拆分单据，打回单据不合并作为待拆分单据使用。
//        4、针对贴现特此判断（如果是启用贴现申请模式则指令退回不做合单单据处理，只清理子票区间）
//        同时把金额合并到可以操作待申请单据中，同时判断无其他业务中单据只有剩余额度时自动赋值剩余票据的子票区间
        if (abAccrecords != null
                && "1".equals(abAccrecords.getSplitFlg())
                && "2".equals(abAccrecords.getCopyFlag())) {
            abVoucherinfoDao.flush();
            abVoucherinfoDao.clear();
            Map<String, Object> params = null;
            oldAccIdSynchOp(abAccrecords.getOldAccId());
            AbVoucherinfo abVoucherinfo = abVoucherinfoDao.get(abAccrecords.getVid(), true);
            AbAccrecords accrecordsOld = abAccrecordsDao.get(abAccrecords.getOldAccId(), true);
            //如果是启用贴现申请模式则指令退回不做合单单据处理，只清理子票区间
            boolean isNotDiscount = abVoucherinfoViceService.isNotDiscountApply();
            if(!isNotDiscount && AccebillConstant.BRIEF_STATUS_011.equals(abAccrecords.getBriefId())){
                //子票区间不为空则清理
                if("1".equals(mergeOpt) && isNotEmpty(abVoucherinfo.getSbbllRngStrtSn())){
                    //合并打回置空子票区间
                    params = new HashMap<>();
                    params.put("ids", new String[]{abVoucherinfo.getId()});
                    params.put("sbbllRngStrtSnNew", " ");
                    params.put("sbbllRngEndSnNew", " ");
                    abVoucherinfoDao.batchUpdate(params);
                }
            } else {
                //贴现办理退回退回设置
                if (isNotEmpty(abVoucherinfo.getSbbllRngStrtSn()) && AccebillConstant.BRIEF_STATUS_6011.equals(abAccrecords.getBriefId())) {
                    List<AbAccrecords> accrecordsSplitList = abAccrecordsDao.listByAvaidSplit(abAccrecords.getOldAccId());
                    AbAccrecords avaidAccrecords = null;
                    for (AbAccrecords entity : accrecordsSplitList) {
                        if (!abAccrecords.getVid().equals(entity.getVid())) {
                            avaidAccrecords = entity;
                            break;
                        }
                    }
                    if (avaidAccrecords != null) {
                        AbVoucherinfo avaidVoucherinfo = abVoucherinfoDao.get(avaidAccrecords.getVid(), true);
                        params = new HashMap<>();
                        //合并拆分金额
                        params.put("ids", new String[]{avaidVoucherinfo.getId()});
                        //判断是否有业务中的同批拆分数据，有则不设置子票区间，无设置最终子票区间
                        List<AbAccrecords> accrecordsUnderwaySplitList = abAccrecordsDao.listByUnderwaySplit(abAccrecords.getOldAccId());
                        if (CollectionUtil.isEmpty(accrecordsUnderwaySplitList)) {
                            BigDecimal avaidBillMoney = avaidVoucherinfo.getBillMoney();
                            //根据原结束子票区间设置开始子票区间
                            String sbbllRngStrtSnNew = StrUtil.fill(new BigDecimal(avaidVoucherinfo.getOrgSbbllRngEndSn())
                                    .subtract(avaidBillMoney.multiply(new BigDecimal("100")))
                                    .add(new BigDecimal("1"))
                                    .setScale(0, BigDecimal.ROUND_HALF_UP).toString(), '0', 12, true);
                            params.put("sbbllRngStrtSnNew", sbbllRngStrtSnNew);
                            params.put("sbbllRngEndSnNew", avaidVoucherinfo.getOrgSbbllRngEndSn());
                            params.put("orgSbbllRngStrtSnNew", sbbllRngStrtSnNew);
                            params.put("orgSbbllRngEndSnNew", avaidVoucherinfo.getOrgSbbllRngEndSn());
                            abVoucherinfoDao.batchUpdate(params);
                        }
                    }
                } else {
                    //合并金额和设置持有子票区间动作
                    if (isEmpty(abVoucherinfo.getSbbllRngStrtSn())
                            || StrUtil.isNotEmpty(mergeOpt)) {
                        //通过拆分出来的主信息ID获取可操作的持有流水信息排除自身
                        List<AbAccrecords> accrecordsSplitList = abAccrecordsDao.listByAvaidSplit(abAccrecords.getOldAccId());
                        AbAccrecords avaidAccrecords = null;
                        for (AbAccrecords entity : accrecordsSplitList) {
                            if (!abAccrecords.getVid().equals(entity.getVid()) &&  !AccebillConstant.BRIEF_STATUS_6011.equals(entity.getBriefId())) {
                                avaidAccrecords = entity;
                                break;
                            }
                        }
                        if (avaidAccrecords != null) {
                            AbVoucherinfo avaidVoucherinfo = abVoucherinfoDao.get(avaidAccrecords.getVid(), true);
                            params = new HashMap<>();
                            //合并拆分金额
                            params.put("ids", new String[]{avaidVoucherinfo.getId()});

                            BigDecimal avaidBillMoney = avaidVoucherinfo.getBillMoney();
                            //退回合并和子票区间为空需要做金额合并
                            if ("1".equals(mergeOpt) || isEmpty(abVoucherinfo.getSbbllRngStrtSn())) {
                                params.put("billMoneyAdd", abVoucherinfo.getBillMoney());
                                avaidBillMoney = avaidBillMoney.add(abVoucherinfo.getBillMoney());
                            }
                            params.put("billMoneyOld", avaidVoucherinfo.getBillMoney());
                            //判断是否有业务中的同批拆分数据，有则不设置子票区间，无设置最终子票区间
                            List<AbAccrecords> accrecordsUnderwaySplitList = abAccrecordsDao.listByUnderwaySplit(abAccrecords.getOldAccId());
                            if (CollectionUtil.isEmpty(accrecordsUnderwaySplitList)) {
                                //根据原结束子票区间设置开始子票区间
                                String sbbllRngStrtSnNew = StrUtil.fill(new BigDecimal(avaidVoucherinfo.getOrgSbbllRngEndSn())
                                        .subtract(avaidBillMoney.multiply(new BigDecimal("100")))
                                        .add(new BigDecimal("1"))
                                        .setScale(0, BigDecimal.ROUND_HALF_UP).toString(), '0', 12, true);
                                params.put("sbbllRngStrtSnNew", sbbllRngStrtSnNew);
                                params.put("sbbllRngEndSnNew", avaidVoucherinfo.getOrgSbbllRngEndSn());
                                params.put("orgSbbllRngStrtSnNew", sbbllRngStrtSnNew);
                                params.put("orgSbbllRngEndSnNew", avaidVoucherinfo.getOrgSbbllRngEndSn());
                            }
                            if (isNotEmpty(avaidVoucherinfo.getSbbllRngStrtSn())) {
                                params.put("sbbllRngStrtSnOld", avaidVoucherinfo.getSbbllRngStrtSn());
                                params.put("sbbllRngEndSnOld", avaidVoucherinfo.getSbbllRngEndSn());
                            }
                            int size = abVoucherinfoDao.batchUpdate(params);
                            abVoucherinfoDao.flush();
                            if (size != 1) {
                                this.logger.error("params:" + params +"  ,size:"+size);
                                throw new ParameterException(AccebillMessageEnum.ACCEBILL_MESSAGE_ERROR_DATAISUPDATEERROR);
                            }
                            //退回合并和子票区间为空需要做设置本次打回拆分持有流水为作废
                            if ("1".equals(mergeOpt) || isEmpty(abVoucherinfo.getSbbllRngStrtSn())) {
                                //1、从表做为失效
                                params = new HashMap<>();
                                params.put("ids", new String[]{abAccrecords.getVvid()});
                                params.put("validSign", AccebillConstant.VALID_SIGN_FALE);
                                params.put("validSignNot", AccebillConstant.VALID_SIGN_FALE);
                                abVoucherinfoViceDao.batchUpdate(params);

                                //2、持有流水表做为失效
                                params = new HashMap<>();
                                params.put("ids", new String[]{abAccrecords.getLastid()});
                                //贴现可能存在贴现办理情况需要获取贴现申请前的lastID
                                if (AccebillConstant.BRIEF_STATUS_011.equals(abAccrecords.getBriefId())) {
                                    AbAccrecords accrecordsLast = abAccrecordsDao.get(abAccrecords.getLastid(), true);
                                    if (AccebillConstant.BRIEF_STATUS_6011.equals(abAccrecords.getBriefId())) {
                                        params.put("ids", new String[]{accrecordsLast.getLastid()});
                                    }
                                }
                                params.put("statusNew", AccebillConstant.AB_BILL_BUS_STATUS_2);
                                params.put("validSignNew", AccebillConstant.VALID_SIGN_DEL);
                                abAccrecordsDao.batchUpdate(params);
                            }
                        } else {
                            //无可以用流水 打回 本身变成可用流水
                            //判断是否有业务中的同批拆分数据，有则不设置子票区间，无设置最终子票区间
                            List<AbAccrecords> accrecordsUnderwaySplitList = abAccrecordsDao.listByUnderwaySplit(abAccrecords.getOldAccId());
                            if (CollectionUtil.isEmpty(accrecordsUnderwaySplitList)
                                    && isEmpty(abVoucherinfo.getSbbllRngStrtSn())) {
                                params = new HashMap<>();
                                params.put("ids", new String[]{abVoucherinfo.getId()});
                                //根据原结束子票区间设置开始子票区间
                                String sbbllRngStrtSnNew = StrUtil.fill(new BigDecimal(abVoucherinfo.getOrgSbbllRngEndSn())
                                        .subtract(abVoucherinfo.getBillMoney().multiply(new BigDecimal("100")))
                                        .add(new BigDecimal("1"))
                                        .setScale(0, BigDecimal.ROUND_HALF_UP).toString(), '0', 12, true);
                                params.put("sbbllRngStrtSnNew", sbbllRngStrtSnNew);
                                params.put("sbbllRngEndSnNew", abVoucherinfo.getOrgSbbllRngEndSn());
                                params.put("orgSbbllRngStrtSnNew", sbbllRngStrtSnNew);
                                params.put("orgbbllRngEndSnNew", abVoucherinfo.getOrgSbbllRngEndSn());
                                abVoucherinfoDao.batchUpdate(params);
                            } else if (isNotEmpty(abVoucherinfo.getSbbllRngStrtSn())
                                    && !"0".equals(mergeOpt)
                                    && (avaidAccrecords != null || abAccrecords.getId().equals(accrecordsOld.getCopyAccId()))) {
                                //合并打回置空子票区间 (子票区间不为空并且非拆分退回数据)
                                params = new HashMap<>();
                                params.put("ids", new String[]{abVoucherinfo.getId()});
                                params.put("sbbllRngStrtSnNew", " ");
                                params.put("sbbllRngEndSnNew", " ");
                                abVoucherinfoDao.batchUpdate(params);
                            }

                            //本身可用设置付款占用金额为0
                            params = new HashMap<>();
                            params.put("ids", new String[]{abAccrecords.getVvid()});
                            params.put("payLockMoneyNew", BigDecimal.ZERO);
                            abVoucherinfoViceDao.batchUpdate(params);
                        }
                    }
                }
            }

//            指令退回需要单独判断
            if (StrUtil.isNotEmpty(mergeOpt)) {
                String oldAccId = abAccrecords.getOldAccId();
                //指令打回判断进行下一个待发送处理
                params = new HashMap<>();
//            1、查找下一个拆分单据
                List<AbAccrecords> awaitSendList = abAccrecordsDao.listByAwaitSendSplit(oldAccId);
//            2、修改原被拆分的可执行ID
                AbAccrecords accrecordsAwaitSend = null;
                if (awaitSendList == null || awaitSendList.isEmpty()) {
                    logger.info("oldAccId=" + oldAccId + "无待发送数据");
                    params.put("copyAccIdIsNull", true);
                } else {
                    accrecordsAwaitSend = awaitSendList.get(0);
                    params.put("copyAccIdNew", accrecordsAwaitSend.getId());
                }
                params.put("ids", new String[]{oldAccId});
                params.put("copyAccIdOld", abAccrecords.getId());
                int size = abAccrecordsDao.batchUpdate(params);
                if (size > 0) {
//                    3、设置下一个拆分票据的子票区间
                    if (CollectionUtil.isNotEmpty(awaitSendList)) {
//                        AbVoucherinfo busVoucherInfo = abVoucherinfoDao.get(abAccrecords.getVid(), true);
                        AbVoucherinfo voucherInfoAwaitSend = abVoucherinfoDao.get(accrecordsAwaitSend.getVid(), true);
                        params = new HashMap<>();
                        params.put("ids", new String[]{voucherInfoAwaitSend.getId()});
                        //根据原业务结束子票区间设置下一个发送票开始和结束子票区间
                        String sbbllRngStrtSnNew = abVoucherinfo.getSbbllRngStrtSn();
                        if ("0".equals(mergeOpt)) {
                            sbbllRngStrtSnNew = String.format("%12s", new BigDecimal(abVoucherinfo.getSbbllRngEndSn())
                                            .add(new BigDecimal("1"))
                                            .setScale(0, BigDecimal.ROUND_HALF_UP).toString())
                                    .replace(" ", "0");
                        }
                        String sbbllRngEndSnNew = String.format("%12s", new BigDecimal(sbbllRngStrtSnNew)
                                        .add(voucherInfoAwaitSend.getBillMoney().multiply(new BigDecimal("100")))
                                        .subtract(new BigDecimal("1"))
                                        .setScale(0, BigDecimal.ROUND_HALF_UP).toString())
                                .replace(" ", "0");
                        params.put("sbbllRngStrtSnNew", sbbllRngStrtSnNew);
                        params.put("sbbllRngEndSnNew", sbbllRngEndSnNew);
                        params.put("orgSbbllRngStrtSnNew", sbbllRngStrtSnNew);
                        params.put("orgSbbllRngEndSnNew", abVoucherinfo.getOrgSbbllRngEndSn());
                        size = abVoucherinfoDao.batchUpdate(params);
                        logger.debug(StrUtil.builder(params.toString(), " ", String.valueOf(size)).toString());
                    }
                }
            }
        }
    }

    /**
     * 审批通过拆分业务处理
     *
     * @param abAccrecords
     */
    public void splitConsentOp(AbAccrecords abAccrecords) {
//        1、线上票据审批通过，有子票区间的作为离线处理。无子票区间设置同一批第一笔数据设置子票区间。
//        2、线下票据审批通过，如果有子票区间正常审批通过，无子票区间根据审批先后顺序从小到大赋值子票区间。
//        判断原单A，无其他待审批单据只有剩余额度时自动赋值剩余票据的子票区间
        if (abAccrecords != null
                && "1".equals(abAccrecords.getSplitFlg())
                && "2".equals(abAccrecords.getCopyFlag())) {
            abVoucherinfoDao.flush();
            abVoucherinfoDao.clear();
            Map<String, Object> params = null;
            oldAccIdSynchOp(abAccrecords.getOldAccId());
            AbVoucherinfo abVoucherinfo = abVoucherinfoDao.get(abAccrecords.getVid(), true);
            if (isNotEmpty(abVoucherinfo.getSbbllRngStrtSn())) {
                if (AccebillConstant.AB_BILL_SEND_SIGN_Y.equals(abAccrecords.getSendSign())
                        && !abVoucherinfo.getSbbllRngEndSn().equals(abVoucherinfo.getOrgSbbllRngEndSn())) {
                    //子票区间不为空且为线上指令数据设置离线处理和设置指令状态为19
                    params = new HashMap<>();
                    params.put("ids", new String[]{abAccrecords.getId()});
                    params.put("sendSignNew", AccebillConstant.AB_BILL_SEND_SIGN_N);
                    params.put("optStatusNew", AccebillConstant.AB_BILL_OPT_STATUS19);
                    abAccrecordsDao.batchUpdate(params);
                }
            } else {
                if (AccebillConstant.AB_BILL_SEND_SIGN_N.equals(abAccrecords.getSendSign())) {
                    AbAccrecords accrecordsSplit = abAccrecordsDao.get(abAccrecords.getOldAccId(), true);
                    if (accrecordsSplit == null) {
                        return;
                    }
                    AbVoucherinfo voucherinfoSplit = abVoucherinfoDao.get(accrecordsSplit.getVid(), true);
                    if (voucherinfoSplit == null) {
                        return;
                    }

                    //子票区间为空为线下票据设置子票区间
                    params = new HashMap<>();
                    params.put("ids", new String[]{abVoucherinfo.getId()});
                    params.put("sbbllRngStrtSnNew", voucherinfoSplit.getOrgSbbllRngStrtSn());
                    String sbbllRngEndSnNew = StrUtil.fill(abVoucherinfo.getBillMoney()
                            .multiply(new BigDecimal("100"))
                            .add(new BigDecimal(voucherinfoSplit.getOrgSbbllRngStrtSn()))
                            .subtract(new BigDecimal("1"))
                            .setScale(0, BigDecimal.ROUND_HALF_UP).toString(), '0', 12, true);
                    params.put("sbbllRngEndSnNew", sbbllRngEndSnNew);
                    params.put("sbbllRngStrtSnOld", abVoucherinfo.getSbbllRngStrtSn());
                    params.put("sbbllRngEndSnOld", abVoucherinfo.getSbbllRngEndSn());
                    int size = abVoucherinfoDao.batchUpdate(params);
                    if (size != 1) {
                        this.logger.error("params:" + params +"  ,size:"+size);
                        throw new ParameterException(AccebillMessageEnum.ACCEBILL_MESSAGE_ERROR_DATAISUPDATEERROR);
                    }

                    //修改原拆分单据的OrgSbbllRngStrtSn
                    params = new HashMap<>();
                    params.put("ids", new String[]{voucherinfoSplit.getId()});
                    String orgSbbllRngStrtSnNew = StrUtil.fill(new BigDecimal(sbbllRngEndSnNew)
                            .add(new BigDecimal("1"))
                            .setScale(0, BigDecimal.ROUND_HALF_UP).toString(), '0', 12, true);
                    params.put("orgSbbllRngStrtSnNew", orgSbbllRngStrtSnNew);
                    params.put("orgSbbllRngStrtSnOld", voucherinfoSplit.getOrgSbbllRngStrtSn());
                    size = abVoucherinfoDao.batchUpdate(params);
                    if (size != 1) {
                        this.logger.error("params:" + params +"  ,size:"+size);
                        throw new ParameterException(AccebillMessageEnum.ACCEBILL_MESSAGE_ERROR_DATAISUPDATEERROR);
                    }

                    //如果只无业务中的数据设置剩余可以用子票区间
                    List<AbAccrecords> accrecordsUnderwaySplitList = abAccrecordsDao.listByUnderwaySplit(abAccrecords.getOldAccId());
                    if (CollectionUtil.isEmpty(accrecordsUnderwaySplitList)) {
                        //通过拆分出来的主信息ID获取可操作的持有流水信息
                        List<AbAccrecords> accrecordsSplitList = abAccrecordsDao.listByAvaidSplit(abAccrecords.getOldAccId());
                        AbAccrecords avaidAccrecords = null;
                        for (AbAccrecords entity : accrecordsSplitList) {
                            if (!abAccrecords.getId().equals(entity.getId())) {
                                avaidAccrecords = entity;
                                break;
                            }
                        }
                        if (avaidAccrecords != null) {
                            AbVoucherinfo avaidVoucherinfo = abVoucherinfoDao.get(avaidAccrecords.getVid(), true);
                            params = new HashMap<>();
                            params.put("ids", new String[]{avaidVoucherinfo.getId()});
                            //根据原结束子票区间设置开始子票区间
                            String sbbllRngStrtSnNew = StrUtil.fill(new BigDecimal(avaidVoucherinfo.getOrgSbbllRngEndSn())
                                    .subtract(avaidVoucherinfo.getBillMoney().multiply(new BigDecimal("100")))
                                    .add(new BigDecimal("1"))
                                    .setScale(0, BigDecimal.ROUND_HALF_UP).toString(), '0', 12, true);
                            params.put("sbbllRngStrtSnNew", sbbllRngStrtSnNew);
                            params.put("sbbllRngEndSnNew", avaidVoucherinfo.getOrgSbbllRngEndSn());
                            params.put("sbbllRngStrtSnOld", " ");
                            abVoucherinfoDao.batchUpdate(params);
                        }
                    }
                } else {
                    //设置同一批第一笔数据子票区间
                    final String oldAccId = abAccrecords.getOldAccId();
//                    为不影响原业务性能做异步线程处理
//                    new Thread() {
//                        public void run() {
                            BillTicketSplittingUtils service = (BillTicketSplittingUtils) ApplicationContextUtil.getContext().getBean("billTicketSplittingUtils");
                            service.setOnLineSplitSbbllRng(oldAccId);
//                        }
//                    }.start();
                }
            }
        }
    }

    /**
     * 设置审批通过在线票据子票区间值（同一批拆分数据只给第一笔子票区间值）
     *
     * @param oldAccId
     */
    public void setOnLineSplitSbbllRng(String oldAccId) {
        oldAccIdSynchOp(oldAccId);
        AbAccrecords accrecordsSplit = abAccrecordsDao.get(oldAccId, true);
//        copyAccId 作为发送中单据处理
        String copyAccId = accrecordsSplit.getCopyAccId();
        if (isNotEmpty(copyAccId)) {
            logger.info("oldAccId={}已有拆分发送中子票区间不需要设置，发送中流水ID={} ", oldAccId, copyAccId);
            return;
        }
        List<AbAccrecords> awaitSendList = abAccrecordsDao.listByAwaitSendSplit(oldAccId);
        if (CollectionUtil.isEmpty(awaitSendList)) {
            logger.info("oldAccId={}无待发送数据", oldAccId);
            return;
        }
        AbAccrecords abAccrecordsAwaitSend = awaitSendList.get(0);

        //设置发送中拆分票据流水表ID
        Map<String, Object> params = new HashMap<>();
        params.put("ids", new String[]{oldAccId});
        params.put("copyAccIdNew", abAccrecordsAwaitSend.getId());
        params.put("copyAccIdWIsNull", true);
        int size = abAccrecordsDao.batchUpdate(params);
        if (size < 1) {
            logger.info("oldAccId={}已有拆分发送中子票区间,不进行设置待发送的子票区间", oldAccId);
            return;
        }

        //设置拆分票据发送中票据子票区间
        AbVoucherinfo awaitSendAvaidVoucherinfo = abVoucherinfoDao.get(abAccrecordsAwaitSend.getVid(), true);
        AbVoucherinfo voucherinfoSplit = abVoucherinfoDao.get(accrecordsSplit.getVid(), true);
        params = new HashMap<>();
        params.put("ids", new String[]{awaitSendAvaidVoucherinfo.getId()});
        //根据原结束子票区间设置开始子票区间
        String sbbllRngEndSnNew = StrUtil.fill(new BigDecimal(voucherinfoSplit.getOrgSbbllRngStrtSn())
                .add(awaitSendAvaidVoucherinfo.getBillMoney().multiply(new BigDecimal("100")))
                .subtract(new BigDecimal("1"))
                .setScale(0, BigDecimal.ROUND_HALF_UP).toString(), '0', 12, true);
        params.put("sbbllRngStrtSnNew", voucherinfoSplit.getOrgSbbllRngStrtSn());
        params.put("sbbllRngEndSnNew", sbbllRngEndSnNew);
        params.put("orgSbbllRngStrtSnNew", voucherinfoSplit.getOrgSbbllRngStrtSn());
        params.put("orgSbbllRngEndSnNew", voucherinfoSplit.getOrgSbbllRngEndSn());
        abVoucherinfoDao.batchUpdate(params);
    }

    /**
     * 通过数据库同一拆分单据做同步处理
     *
     * @param oldAccId
     */
    public void oldAccIdSynchOp(String oldAccId) {
        if (StrUtil.isNotEmpty(oldAccId)) {
//                AbAccrecords accrecordsOld = abAccrecordsDao.get(,true);
            Map<String, Object> params = new HashMap<>();
            //合并拆分金额
            params.put("ids", new String[]{oldAccId});
//                params.put("updateDateOld",accrecordsOld.getUpdateDate());
            int size = abAccrecordsDao.batchUpdate(params);
//                if (size != 1) {
//                    throw new ParameterException(AccebillMessageEnum.ACCEBILL_MESSAGE_ERROR_DATAISUPDATEERROR);
//                }
        }
    }

    /**
     * 忽略空字符串
     *
     * @param str
     * @return
     */
    private static boolean isEmpty(String str) {
        return StringUtil.isEmpty(str) || StringUtil.isEmpty(str.trim());
    }

    private static boolean isNotEmpty(String str) {
        return !isEmpty(str);
    }
}
