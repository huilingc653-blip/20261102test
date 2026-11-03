package com.bytter.bill.domain.billbank.tool;
import com.bytter.bill.domain.billbank.dao.BillAccrecordsDao;
import com.bytter.bill.domain.billbank.dao.BillBankDao;
import com.bytter.bill.domain.billbank.dao.BillSignDao;
import com.bytter.bill.domain.billbank.entity.BillAbAccrecords;
import com.bytter.bill.domain.billbank.entity.BillAbVoucherinfo;
import com.bytter.bill.domain.billbank.entity.BillAbVoucherinfoVice;
import com.bytter.bill.domain.billbank.entity.BillSign;
import com.bytter.bill.domain.billbank.vo.SplitBaseImpVO;
import com.bytter.bill.domain.billbank.vo.VoucherInfoVo;
import com.bytter.core.utils.BeanUtils;
import com.bytter.core.utils.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

public class NewDraftDealMethod {
    private static final Logger logger = LoggerFactory.getLogger(NewDraftDealMethod.class);
    /**
     * 新票据子票区间退回合并操作
     *
     *
     * @param isMerge
     * @param
     */
    public static void draftMerge(String isMerge, String vid, BillBankDao billBankDao, BillAccrecordsDao billAccrecordsDao) {
        if (isMerge == null || vid == null) {
            return;
        }
        BillAbVoucherinfo abVoucherinfo = billBankDao.getVoucherInfoByVoucherId(vid);
        //旧票据不需要此处理和无拆分票据
        if (!"1".equals(abVoucherinfo.getNewStmIssuofpprInd()) || abVoucherinfo.getOldDraftId() == null || "0".equals(abVoucherinfo.getBlgrdbagcrclPrmtInd())) {
            return;
        }
        List<BillAbVoucherinfo> abVoucherinfos = billBankDao.getVoucherInfoListByOldDraftId(abVoucherinfo.getOldDraftId());
        if (CollectionUtils.isEmpty(abVoucherinfos)) {
            return;
        }
        //不需要合并
        if ("0".equals(isMerge)) {
            //循环查找已拆分票据从表数据，并将其设置为1有效
            for (BillAbVoucherinfo i : abVoucherinfos) {
                //找出锁定的另一部分子票
                if(i.getId().equals(vid)){
                    continue;
                }
                List<BillAbVoucherinfoVice> vices = billBankDao.getVoucherinfoViceListByVid(i.getId());
                if (!CollectionUtils.isEmpty(vices)) {
                    for (BillAbVoucherinfoVice v : vices) {
                        v.setValidSign("1");
                    }
                    billBankDao.batchSaveOrUpdate(vices);
                    //billBankDao.updateBatch(vices);
                }
            }
            return;
        }
        String oldId = abVoucherinfo.getOldDraftId();
        List<BillAbVoucherinfo> updVh = new ArrayList<BillAbVoucherinfo>();
        //合并拆分出来的票据，主表oldDraftId字段在原字段上加上del标识已删除
        for(BillAbVoucherinfo b : abVoucherinfos){
            if(StringUtil.isEmpty(b.getOldDraftId())){
                continue;
            }
            b.setOldDraftId(b.getOldDraftId() + "-del");
            b.setUpdateDate(new Date());
            updVh.add(b);
        }
        if(!CollectionUtils.isEmpty(updVh)){
            billBankDao.batchSaveOrUpdate(updVh);
            //billBankDao.updateBatch(updVh);
        }
        //需要进行合并操作的数据
        //设置从表信息为已删除
        String[] vids = new String[abVoucherinfos.size()];
        for (int i = 0; i < abVoucherinfos.size(); i++) {
            BillAbVoucherinfo ba = abVoucherinfos.get(i);
            vids[i] = ba.getId();
        }
        List<BillAbVoucherinfoVice> vices = billBankDao.getVoucherinfoViceListByVids(vids);
        logger.info("vices+++++=====" + vices.size());
        if (!CollectionUtils.isEmpty(vices)) {
            List<BillAbAccrecords> cAbaccrecords = null;
            for (BillAbVoucherinfoVice i : vices) {
                i.setValidSign("0");
                i.setUpdateDate(new Date());
                cAbaccrecords = billAccrecordsDao.getAccrecordsInfoByVoucherIdAndStatus(i.getVid(),null,"95");
                //cAbaccrecords = billBankDao.getAbAccrecordsListByVidVvid(i.getVid(), i.getId());
                //cAbaccrecords = abAccrecordsDao.queryAbAccrecordsByVidAndVVid(i.getVid(), i.getId(), false);
                if (CollectionUtils.isEmpty(cAbaccrecords)) {
                    logger.info("未能找到已拆包流水数据");
                    continue;
                }
                //将合并回退的票据流水设为已删除
                for (BillAbAccrecords aa : cAbaccrecords) {
                    aa.setStatus(-2);
                    aa.setUpdateDate(new Date());
                }
                billAccrecordsDao.batchSaveOrUpdate(cAbaccrecords);
                //billBankDao.updateBatch(cAbaccrecords);
            }
            billBankDao.batchSaveOrUpdate(vices);
        }
        //billBankDao.updateBatch(vices);
        BillAbVoucherinfo oldMainInfo = billBankDao.getVoucherInfoByVoucherId(oldId);
        if (oldMainInfo == null) {
           return;
        }
        List<BillAbVoucherinfoVice> oldCongInfo = billBankDao.getVoucherinfoViceListByVid(oldId);
        if (CollectionUtils.isEmpty(oldCongInfo)) {
            return;
        }
        //将已拆分原票据设为有效
        for (BillAbVoucherinfoVice vice : oldCongInfo) {
            vice.setValidSign("1");
        }
        billBankDao.batchSaveOrUpdate(oldCongInfo);
        //billBankDao.updateBatch(oldCongInfo);
        List<BillAbAccrecords> oldAccrecordList = billAccrecordsDao.getAccrecordsInfoByVoucherIdAndStatus(oldMainInfo.getId(), "96");
        if (!CollectionUtils.isEmpty(oldAccrecordList)) {
            for (BillAbAccrecords a : oldAccrecordList) {
                a.setStatus(95);
                a.setUpdateDate(new Date());
            }
            billAccrecordsDao.batchSaveOrUpdate(oldAccrecordList);
        }
        //billBankDao.updateBatch(oldAccrecordList);
    }
    /**
     * 同意清偿打回处理
     */
    public static String agreeSettleRepulse(BillSignDao billSignDao, String signId, Integer status, String accId, BillAccrecordsDao billAccrecordsDao){
        BillSign billSign = billSignDao.get(signId);
        if("0".equals(billSign.getNewStmIssuofpprInd()) || "0".equals(billSign.getBlgrdbagcrclPrmtInd())){
            return signId;
        }
        //查询新票拆票丢弃数据置为指定数据
        List<BillSign> billSigns = billSignDao.queryBillSignListByOrigNoAndStatus(billSign.getOrigNo(),-100);
        if(CollectionUtils.isEmpty(billSigns)){
            return signId;
        }
        //-200删除
        billSign.setStatus(-200);
        billSign.setUpdateDate(new Date());
        List<BillSign> oldList = new ArrayList<BillSign>();
        oldList.add(billSign);
        billSignDao.batchUpdate(oldList);
        BillSign sign = billSigns.get(0);
        sign.setStatus(status);
        billSign.setUpdateDate(new Date());
        billSignDao.batchUpdate(billSigns);
        BillAbAccrecords billAbAccrecords = billAccrecordsDao.get(accId);
        billAbAccrecords.setRecid(sign.getId());
        billAbAccrecords.setUpdateDate(new Date());
        List<BillAbAccrecords> billAbAccrecords1 = new ArrayList<BillAbAccrecords>();
        billAbAccrecords1.add(billAbAccrecords);
        billAccrecordsDao.batchUpdate(billAbAccrecords1);
        return sign.getId();
    }
    /**
     * 新票据拆分请求确认处理
     * @param billBankDao
     * @param recordId
     * @param
     */
    public static void newDraftSplitAck(BillBankDao billBankDao,String recordId){
        BillAbAccrecords businessInfo=billBankDao.getAccrecordsByRecordId(recordId);
        if(businessInfo == null){
            logger.info("无法找到票据信息，请确认");
            return;
        }
        //设置批量拆分数据
        if("2".equals(businessInfo.getCopyFlag())){
            String oldAccId=businessInfo.getOldAccId();
            //原被拆分单据
            BillAbAccrecords accrecordsSplit=billBankDao.getAccrecordsByRecordId(oldAccId);
            if(accrecordsSplit == null){
                return;
            }
            if(recordId.equals(accrecordsSplit.getCopyAccId())) {
                Map<String,Object> params=new HashMap<String,Object>();
                oldAccIdSynchOp(oldAccId,billBankDao);
//            1、查找下一个拆分单据
                List<BillAbAccrecords> awaitSendList = billBankDao.listByAwaitSendSplit(oldAccId);
//            2、修改原被拆分的可执行ID
                BillAbAccrecords accrecordsAwaitSend = null;
                if(awaitSendList==null||awaitSendList.isEmpty()){
                    logger.info("oldAccId="+oldAccId+"无待发送数据");
                    params.put("copyAccIdIsNull",true);
                } else {
                    accrecordsAwaitSend = awaitSendList.get(0);
                    params.put("copyAccIdNew",accrecordsAwaitSend.getId());
                }
                params.put("ids",new String[]{oldAccId});
                params.put("copyAccIdOld",accrecordsSplit.getCopyAccId());
                int size = billBankDao.batchUpdateBillAbAccrecords(params);
                BillAbVoucherinfo busVoucherInfo = billBankDao.getVoucherInfoByVoucherId(businessInfo.getVid());
                if(size>0 && accrecordsAwaitSend!=null) {
//                  3、设置下一个拆分票据的子票区间
                    BillAbVoucherinfo voucherInfoAwaitSend = billBankDao.getVoucherInfoByVoucherId(accrecordsAwaitSend.getVid());
                    params=new HashMap<String,Object>();
                    params.put("ids",new String[]{voucherInfoAwaitSend.getId()});
                    //根据原业务结束子票区间设置下一个发送票开始和结束子票区间
                    String sbbllRngStrtSnNew=String.format("%12s",new BigDecimal(busVoucherInfo.getSbbllRngEndSn())
                            .add(new BigDecimal("1"))
                            .setScale(0,BigDecimal.ROUND_HALF_UP).toString())
                            .replace(" ","0");
                    String sbbllRngEndSnNew=String.format("%12s",new BigDecimal(sbbllRngStrtSnNew)
                            .add(voucherInfoAwaitSend.getBillMoney().multiply(new BigDecimal("100")))
                            .subtract(new BigDecimal("1"))
                            .setScale(0,BigDecimal.ROUND_HALF_UP).toString())
                            .replace(" ","0");
                    params.put("sbbllRngStrtSnNew", sbbllRngStrtSnNew);
                    params.put("sbbllRngEndSnNew", sbbllRngEndSnNew);
                    params.put("orgSbbllRngStrtSnNew", sbbllRngStrtSnNew);
                    params.put("orgSbbllRngEndSnNew", busVoucherInfo.getOrgSbbllRngEndSn());
                    billBankDao.batchUpdateVoucherinfo(params);
                }

                //修改原拆分单据的OrgSbbllRngStrtSn
                params = new HashMap<String,Object>();
                params.put("ids", new String[]{accrecordsSplit.getVid()});
                String orgSbbllRngStrtSnNew = String.format("%12s",new BigDecimal(busVoucherInfo.getSbbllRngEndSn())
                        .add(new BigDecimal("1"))
                        .setScale(0, BigDecimal.ROUND_HALF_UP).toString())
                        .replace(" ","0");
                params.put("orgSbbllRngStrtSnNew", orgSbbllRngStrtSnNew);
                params.put("orgSbbllRngStrtSnOld", busVoucherInfo.getOrgSbbllRngStrtSn());
                billBankDao.batchUpdateVoucherinfo(params);

                //如果只无业务中的数据设置剩余可以用子票区间
                if(size>0 && accrecordsAwaitSend==null){
                    List<BillAbAccrecords> accrecordsUnderwaySplitList=billBankDao.listByUnderwaySplit(oldAccId);
                    if(accrecordsUnderwaySplitList==null||accrecordsUnderwaySplitList.isEmpty()){
                        //通过拆分出来的主信息ID获取可操作的持有流水信息
                        List<BillAbAccrecords> accrecordsSplitList=billBankDao.listByAvaidSplit(oldAccId);
                        if(accrecordsSplitList!=null&&!accrecordsSplitList.isEmpty()){
                            BillAbAccrecords avaidAccrecords=accrecordsSplitList.get(0);
                            BillAbVoucherinfo avaidVoucherinfo = billBankDao.getVoucherInfoByVoucherId(avaidAccrecords.getVid());
                            params=new HashMap<String,Object>();
                            params.put("ids",new String[]{avaidVoucherinfo.getId()});
                            //根据原结束子票区间设置开始子票区间
                            String sbbllRngStrtSnNew=String.format("%12s",new BigDecimal(avaidVoucherinfo.getOrgSbbllRngEndSn())
                                            .subtract(avaidVoucherinfo.getBillMoney().multiply(new BigDecimal("100")))
                                            .add(new BigDecimal("1"))
                                            .setScale(0,BigDecimal.ROUND_HALF_UP).toString())
                                    .replace(" ","0");
                            params.put("sbbllRngStrtSnNew", sbbllRngStrtSnNew);
                            params.put("sbbllRngEndSnNew", avaidVoucherinfo.getOrgSbbllRngEndSn());
                            params.put("orgSbbllRngStrtSnNew", sbbllRngStrtSnNew);
                            params.put("orgSbbllRngEndSnNew", avaidVoucherinfo.getOrgSbbllRngEndSn());
                            params.put("sbbllRngStrtSnOld", " ");
                            billBankDao.batchUpdateVoucherinfo(params);
                        }
                    }
                }
            }
            return;
        }

        BillAbVoucherinfo mainInfo = billBankDao.getVoucherInfoByVoucherId(businessInfo.getVid());
        if(mainInfo == null){
            logger.info("无法找到票据信息，请确认");
            return;
        }
        if(!"1".equals(mainInfo.getNewStmIssuofpprInd()) || StringUtil.isBlank_new(mainInfo.getOldDraftId()) || !"1".equals(mainInfo.getBlgrdbagcrclPrmtInd())){
            logger.info("票据未做拆包操作");
            return;
        }
        //使用oldDraftId查询出已拆分的票据信息
        List<BillAbVoucherinfo> mainInfoList = billBankDao.getVoucherInfoListByOldDraftId(mainInfo.getOldDraftId());
        //为空或者长度不为2返回。一般一张票一次交易会一分为二(同意清偿会出现多张的情况)
        if(CollectionUtils.isEmpty(mainInfoList)){
            logger.info("无法找到已拆分的票据信息");
            return;
        }
        for(BillAbVoucherinfo bv : mainInfoList){
            //找出拆分后的另一张子票的主表id
            if(bv.getId().equals(mainInfo.getId())){
                continue;
            }
            //String nonSplitDraftId = mainInfoList.get(0).getId().equals(mainInfo.getId()) ? mainInfoList.get(1).getId() : mainInfoList.get(0).getId();
            List<BillAbVoucherinfoVice> viceList = billBankDao.getVoucherinfoViceListByVid(bv.getId());
            if(CollectionUtils.isEmpty(viceList)){
                logger.info("查询从表信息为空或者已做解锁操作");
                continue;
            }
            for(BillAbVoucherinfoVice v : viceList){
                v.setValidSign("1");
                v.setUpdateDate(new Date());
            }
            //解锁拆分后另一部分子票
            billBankDao.batchSaveOrUpdate(viceList);
        }
    }

    private static Map<String,Object> newDraftArgPubDeal(BigDecimal billMoney,String sbbllRngStrtSn,String sbbllRngEndSn,String newStmIssuofpprInd
            ,String blgrdbagcrclPrmtInd, BillBankDao billBankDao,String recordId,String oldDraftId){
        Map<String,Object> resultMap = new HashMap<String,Object>();
        //不拆票的交易默认赋值
        BigDecimal orgMoney = billMoney;
        BigDecimal splitMoney = billMoney;
        String orgSbbllRngStrtSn = sbbllRngStrtSn;
        String orgSbbllRngEndSn = sbbllRngEndSn;
        resultMap.put("orgMoney",orgMoney);
        resultMap.put("splitMoney",splitMoney);
        resultMap.put("orgSbbllRngStrtSn",orgSbbllRngStrtSn);
        resultMap.put("orgSbbllRngEndSn",orgSbbllRngEndSn);
        if("0".equals(newStmIssuofpprInd) || "0".equals(blgrdbagcrclPrmtInd)){
            orgSbbllRngStrtSn = "0";
            orgSbbllRngEndSn = "0";
            resultMap.put("orgSbbllRngStrtSn",orgSbbllRngStrtSn);
            resultMap.put("orgSbbllRngEndSn",orgSbbllRngEndSn);
            return resultMap;
        }
        BillAbAccrecords acc = billBankDao.getAccrecordsByRecordId(recordId);
        //是否拆分标识为1，说明此笔交易是拆票交易
        if (StringUtil.isNotEmpty(oldDraftId) && "1".equals(acc.getSplitFlg())){
            if(oldDraftId.contains("-del")){
                //若OldDraftId包含-del,说明此笔数据为无效数据，将子票区间设置为-，使银行不会成功办理此笔业务
                logger.info("无效的票据信息=====oldDraftId："+oldDraftId+"recordId:"+recordId);
                orgSbbllRngStrtSn = "-";
                orgSbbllRngEndSn = "-";
                resultMap.put("orgSbbllRngStrtSn",orgSbbllRngStrtSn);
                resultMap.put("orgSbbllRngEndSn",orgSbbllRngEndSn);
                return resultMap;
            }
            BillAbVoucherinfo oldInfo = billBankDao.getVoucherInfoByVoucherId(oldDraftId);
            if(oldInfo == null){
                //若查出无数据，将子票区间设置为-，使银行不会成功办理此笔业务
                logger.info("无法查到相应的票据信息=====oldDraftId："+oldDraftId+"recordId:"+recordId);
                orgSbbllRngStrtSn = "-";
                orgSbbllRngEndSn = "-";
                resultMap.put("orgSbbllRngStrtSn",orgSbbllRngStrtSn);
                resultMap.put("orgSbbllRngEndSn",orgSbbllRngEndSn);
                return resultMap;
            }
            orgMoney = oldInfo.getBillMoney();
            orgSbbllRngStrtSn = oldInfo.getSbbllRngStrtSn();
            orgSbbllRngEndSn = oldInfo.getSbbllRngEndSn();
            resultMap.put("orgMoney",orgMoney);
            resultMap.put("orgSbbllRngStrtSn",orgSbbllRngStrtSn);
            resultMap.put("orgSbbllRngEndSn",orgSbbllRngEndSn);
        }
        return resultMap;
    }

    /**  *
     * 处理新票银行接口传参
     */
    public static<T> List<T> dealNewDraftArg(List<T> voucherInfoList, BillBankDao billBankDao){

        List voucherInfoListNew=new ArrayList<T>();
        for(T t : voucherInfoList){
            if(t instanceof SplitBaseImpVO){
                SplitBaseImpVO vo=(SplitBaseImpVO)t;
                //新批量拆分功能判断
                if("2".equals(vo.getCopyFlag()) && isNotEmpty(vo.getOrgSbbllRngStrtSn())){
                    //批量拆分的子票区间不允许为空
                    if(isEmpty(vo.getSbbllRngStrtSn())){
                        continue;
                    }
                    String orgSbbllRngStrtSn = vo.getOrgSbbllRngStrtSn();
                    String orgSbbllRngEndSn = vo.getOrgSbbllRngEndSn();
                    BigDecimal orgMoney=new BigDecimal(orgSbbllRngEndSn)
                            .subtract(new BigDecimal(orgSbbllRngStrtSn))
                            .add(new BigDecimal("1"))
                            .divide(new BigDecimal("100"),2, RoundingMode.HALF_UP);
                    vo.setSplitMoney(vo.getBillMoney());
                    vo.setOrgMoney(orgMoney);
                    voucherInfoListNew.add(vo);
                    continue;
                }

                Map<String,Object> beanToMap = BeanUtils.beanToMap(t);
                Map<String,Object> retMap = newDraftArgPubDeal((beanToMap.get("billMoney") == null ? null : new BigDecimal(String.valueOf(beanToMap.get("billMoney"))))
                        , String.valueOf(beanToMap.get("sbbllRngStrtSn")), String.valueOf(beanToMap.get("sbbllRngEndSn"))
                        , String.valueOf(beanToMap.get("newStmIssuofpprInd")), String.valueOf(beanToMap.get("blgrdbagcrclPrmtInd")),billBankDao
                        , String.valueOf(beanToMap.get("recordId")), String.valueOf(beanToMap.get("oldDraftId")));
                beanToMap.putAll(retMap);

                vo.setOrgMoney(new BigDecimal((retMap.get("orgMoney") == null ? "0" : retMap.get("orgMoney").toString())));
                vo.setSplitMoney(new BigDecimal((retMap.get("splitMoney") == null ? "0" : retMap.get("splitMoney").toString())));
                vo.setOrgSbbllRngStrtSn(String.valueOf(retMap.get("orgSbbllRngStrtSn")));
                vo.setOrgSbbllRngEndSn(String.valueOf(retMap.get("orgSbbllRngEndSn")));
                voucherInfoListNew.add(vo);
            }
//            if(t instanceof VoucherInfoVo){
//                    VoucherInfoVo vo = (VoucherInfoVo)t;
//                vo.setOrgMoney(new BigDecimal((retMap.get("orgMoney") == null ? "0" : retMap.get("orgMoney").toString())));
//                vo.setSplitMoney(new BigDecimal((retMap.get("splitMoney") == null ? "0" : retMap.get("splitMoney").toString())));
//                vo.setOrgSbbllRngStrtSn(String.valueOf(retMap.get("orgSbbllRngStrtSn")));
//                vo.setOrgSbbllRngEndSn(String.valueOf(retMap.get("orgSbbllRngEndSn")));
//            }else {
//                PoolBillVoucherVO vo = (PoolBillVoucherVO)t;
//                vo.setOrgMoney(new BigDecimal((retMap.get("orgMoney") == null ? "0" : retMap.get("orgMoney").toString())));
//                vo.setSplitMoney(new BigDecimal((retMap.get("splitMoney") == null ? "0" : retMap.get("splitMoney").toString())));
//                vo.setOrgSbbllRngStrtSn(String.valueOf(retMap.get("orgSbbllRngStrtSn")));
//                vo.setOrgSbbllRngEndSn(String.valueOf(retMap.get("orgSbbllRngEndSn")));
//            }
        }
        return voucherInfoListNew;
//        return voucherInfoList;
    }
    /**
     * 处理子票区间不足12位
     * @param sbbllRngStrtSn
     * @param sbbllRngEndSn
     * @return
     */
    public static List<String> subTicketIntervalLenLack(String sbbllRngStrtSn, String sbbllRngEndSn){
        List<String> result = new ArrayList<String>();
        if(StringUtil.isBlank(sbbllRngStrtSn) || StringUtil.isBlank(sbbllRngEndSn) || "0".equals(sbbllRngStrtSn) || "0".equals(sbbllRngEndSn)){
            sbbllRngStrtSn = "0";
            sbbllRngEndSn = "0";
            result.add(sbbllRngStrtSn);
            result.add(sbbllRngEndSn);
            return result;
        }
//        String zeroStr = "000000000000";
//        if(sbbllRngStrtSn.length() < 12){
//            String tempStr = zeroStr.substring(0,(12 - sbbllRngStrtSn.length()));
//            sbbllRngStrtSn = tempStr + sbbllRngStrtSn;
//        }
//        if(sbbllRngEndSn.length() < 12){
//            String tempStr = zeroStr.substring(0,(12 - sbbllRngEndSn.length()));
//            sbbllRngEndSn = tempStr + sbbllRngEndSn;
//        }
        result.add(String.format("%12s",sbbllRngStrtSn).replace(" ","0"));
        result.add(String.format("%12s",sbbllRngEndSn).replace(" ","0"));
        return result;
    }



    /**
     * 通过数据库同一拆分单据做同步处理
     * @param oldAccId
     */
    public static void oldAccIdSynchOp(String oldAccId, BillBankDao billBankDao){
        if(StringUtil.isNotEmpty(oldAccId)){
//                AbAccrecords accrecordsOld = abAccrecordsDao.get(,true);
            Map<String,Object> params = new HashMap<String,Object>();
            //合并拆分金额
            params.put("ids",new String[]{oldAccId});
//                params.put("updateDateOld",accrecordsOld.getUpdateDate());
            int size = billBankDao.batchUpdateBillAbAccrecords(params);
//                if (size != 1) {
//                    throw new ParameterException(AccebillMessageEnum.ACCEBILL_MESSAGE_ERROR_DATAISUPDATEERROR);
//                }
        }
    }

    private static boolean isEmpty(String str){
        return StringUtil.isEmpty(str)||StringUtil.isEmpty(str.trim());
    }

    private static boolean isNotEmpty(String str){
        return !isEmpty(str);
    }
}
