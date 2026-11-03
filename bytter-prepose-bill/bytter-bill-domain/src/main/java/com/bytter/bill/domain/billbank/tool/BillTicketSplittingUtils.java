package com.bytter.bill.domain.billbank.tool;

import com.bytter.bill.api.bill.constant.AccebillConstant;
import com.bytter.bill.domain.billbank.dao.BillAccrecordsDao;
import com.bytter.bill.domain.billbank.dao.BillBankDao;
import com.bytter.bill.domain.billbank.entity.BillAbAccrecords;
import com.bytter.bill.domain.billbank.entity.BillAbVoucherinfo;
import com.bytter.bill.domain.billbank.entity.BillAbVoucherinfoVice;
import com.bytter.bill.domain.billbank.util.BeanUtils;
import com.bytter.core.utils.StringUtil;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.util.*;

/**  *
 * 拆票工具类
 */
public class BillTicketSplittingUtils {

    /**  *
     * 拆票功能
     * param: vIds主表id 必传
     * param: vvIds 从表id 必传
     * param: accId 流水id 必传
     * param: transMoneys 交易金额 必传
     * return: Map: key-> infoIds, viceIds ,accIds ,transMoneys ;valus 为对应的 id值
     */
   /* public static Map<String, String> ticketSplitting(String vIds, String vvIds, String accIds, String transMoneys, BillBankDao billBankDao) {
        if (StringUtil.isEmpty(vIds) || StringUtil.isEmpty(vvIds) || StringUtil.isEmpty(accIds) || StringUtil.isEmpty(transMoneys)) {
            return null;
        }
        String[] vIdsArr = vIds.split(",");
        String[] vvIdsArr = vvIds.split(",");
        String[] accIdsArr = accIds.split(",");
        String[] transMoneysArr = transMoneys.split(",");
        Map<String, String> resultMap = Maps.newHashMap();

        List<String> infoList = new ArrayList<>();
        List<String> viceIdList = new ArrayList<>();
        List<String> accIdList = new ArrayList<>();
        List<String> transMoneyList = new ArrayList<>();

        int num = vIdsArr.length;
        if ((num != vvIdsArr.length) && (num != transMoneysArr.length) && (num != accIdsArr.length)) {
            return null;
        }

        for (int i = 0; i < num; i++) {
            String infoId = vIdsArr[i];
            BigDecimal money = new BigDecimal(transMoneysArr[i]);
            //查 ab_voucherInfo
            List<String> idList = new ArrayList<>();
            idList.add(infoId);

            //List<AbVoucherinfo> infoDBs = abVoucherinfoDao.list(idList);
            List<BillAbVoucherinfo> infoDBs = billBankDao.getVoucherInfoListByDraftId(idList.toArray(new String[idList.size()]));
            BillAbVoucherinfo abVoucherinfo = infoDBs.get(0);

            // AB_VOUCHERINFO.NEW_STM_ISSUOFPPR_IND IS '出票标志。0：老系统出票,   1：新系统出票。默认值为0，不能为空
            if (AccebillConstant.NEW_STM_ISSUOFPPR_IND_ZERO.equals(abVoucherinfo.getNewStmIssuofpprInd())) {
                //旧系统数据也要保留
                infoList.add(infoId);
                viceIdList.add(vvIdsArr[i]);
                accIdList.add(accIdsArr[i]);
                transMoneyList.add(transMoneysArr[i]);
                continue;
            }

            //这里表示新系统出票，再判断是否进行拆票处理，通过判断前端传输的金额和原金额进行对比得出结果
            if (money.compareTo(abVoucherinfo.getBillMoney()) == 0) {
                //这里不是拆票
                infoList.add(infoId);
                viceIdList.add(vvIdsArr[i]);
                accIdList.add(accIdsArr[i]);
                transMoneyList.add(transMoneysArr[i]);
                continue;
            }
            //查 ab_voucherInfo_vice
            // 是否用传进来的viceId进行查询？因为现在做解除质押的时候通过info.id 找到多条从表记录
            List<BillAbVoucherinfoVice> viceDBs = billBankDao.getVoucherinfoViceListByVid_1(infoId);

            //查流水表 ab_accrecords
            List<BillAbAccrecords> abAccrecords = billBankDao.getAbAccrecordsListByVidVvid_95(abVoucherinfo.getId(), null);
            //处理主表、从表、流水表 三表记录
            Map<String, String> abAccRecordsId = saveRecords(money, abVoucherinfo, vvIdsArr[i], accIdsArr[i], viceDBs, abAccrecords, billBankDao);
            infoList.add(abAccRecordsId.get("infoId"));
            viceIdList.add(abAccRecordsId.get("viceId"));
            accIdList.add(abAccRecordsId.get("recordsId"));
            transMoneyList.add(transMoneysArr[i]);
        }

        resultMap.put("infoIds", list2Str(infoList));
        resultMap.put("viceIds", list2Str(viceIdList));
        resultMap.put("accIds", list2Str(accIdList));
        resultMap.put("transMoneys", list2Str(transMoneyList));
        return resultMap;
    }*/

    /**  *
     * 拆票功能
     * 同意清偿签收用
     */
    public static BillAbVoucherinfo ticketSplitting(String vId, String transMoney, BillBankDao billBankDao, BillAccrecordsDao billAccrecordsDao) {
        if (StringUtil.isEmpty(vId) || StringUtil.isEmpty(transMoney) || billBankDao == null) {
            return null;
        }

        String infoId = vId;
        BigDecimal money = new BigDecimal(transMoney);
        //查 ab_voucherInfo
        //List<AbVoucherinfo> infoDBs = abVoucherinfoDao.list(idList);
        List<BillAbVoucherinfo> infoDBs = billBankDao.getVoucherInfoListByDraftId(new String[]{infoId});
        BillAbVoucherinfo abVoucherinfo = infoDBs.get(0);

        // AB_VOUCHERINFO.NEW_STM_ISSUOFPPR_IND IS '出票标志。0：老系统出票,   1：新系统出票。默认值为0，不能为空
        if ("0".equals(abVoucherinfo.getNewStmIssuofpprInd())) {
            return null;
        }

        //这里表示新系统出票，再判断是否进行拆票处理，通过判断前端传输的金额和原金额进行对比得出结果
        if (money.compareTo(abVoucherinfo.getBillMoney()) == 0) {
            //这里不是拆票
            return null;
        }
        //查 ab_voucherInfo_vice
        // 是否用传进来的viceId进行查询？因为现在做解除质押的时候通过info.id 找到多条从表记录
        List<BillAbVoucherinfoVice> viceDBs = billBankDao.getVoucherinfoViceListByVid_1(infoId);

        //查流水表 ab_accrecords
        List<BillAbAccrecords> abAccrecords = billAccrecordsDao.getAccrecordsInfoByVoucherIdAndStatus(abVoucherinfo.getId(),"95");
        //List<BillAbAccrecords> abAccrecords = billBankDao.getAbAccrecordsListByVidVvid_95(abVoucherinfo.getId(), null);
        //处理主表、从表、流水表 三表记录
        BillAbVoucherinfo result = saveRecords(money, abVoucherinfo, viceDBs, abAccrecords, billBankDao,billAccrecordsDao);
        return result;
    }

    public static BillAbVoucherinfo saveRecords(BigDecimal money, BillAbVoucherinfo info, List<BillAbVoucherinfoVice> viceDBs
            , List<BillAbAccrecords> records, BillBankDao billBankDao, BillAccrecordsDao billAccrecordsDao) {
        //先处理 AbVoucherinfo 表
        BillAbVoucherinfo infoFirstNew = new BillAbVoucherinfo();
        BillAbVoucherinfo infoSecondNew = new BillAbVoucherinfo();
        List<BillAbAccrecords> handleAccs2 = new ArrayList<BillAbAccrecords>();
        List<BillAbAccrecords> accList2 = new ArrayList<BillAbAccrecords>();
        Map<String,String> idMaphandle = new HashMap<String,String>();
        Map<String,String> idMapacc = new HashMap<String,String>();

        BeanUtils.copyNotNullProperties(info, infoFirstNew, "id");
        BeanUtils.copyNotNullProperties(info, infoSecondNew, "id");

        // (一张票，优先交易前面的金额,eg:票面金额为100。第一次拆票金额为10，即0.01-10 先交易，10.01-100保留下来以后再交易)
        infoFirstNew.setBillMoney(money);

        //子票区间结束序号。旧票据默认0，新票据 12位长度，不足12位前面补0。开票阶段，旧票据默认值为0，新票后台赋值为票据金额*100不足12位前面补0。
        String str = "000000000000";
        BigDecimal beginNO = new BigDecimal(info.getSbbllRngStrtSn());
        BigDecimal firstEndNo = beginNO.add(money.multiply(new BigDecimal("100"))).subtract(BigDecimal.ONE);
        infoFirstNew.setSbbllRngEndSn(String.format("%12s",firstEndNo.setScale(0,BigDecimal.ROUND_HALF_UP)).replace(" ","0"));
        //拆分出来的两张票，票据主表AB_VOUCHERINFO中的字段old_draft_id设为原票据id，需要进行交易的子票，根据原有流程进行处理，
        // 不需要交易的子票将其从表AB_VOUCHERINFO_VICE中的字段VALID_SIGN设置为3拆票中（暂时锁定此部分票据）。原票据，从表AB_VOUCHERINFO_VICE中的字段VALID_SIGN设置为3已拆票（新增类型）。
        infoFirstNew.setOldDraftId(info.getId());
       /* infoFirstNew.setCreateBy(user == null ? null : user.getUsername());
        infoFirstNew.setUpdateBy(user == null ? null : user.getUsername());*/
        infoFirstNew.setCreateDate(new Date());
        infoFirstNew.setUpdateDate(new Date());
        //查看区间是否已经存在
        List<BillAbVoucherinfo> splitUserList = billBankDao.getVoucherInfoListByOrigNoAndSbblNo(info.getOrigNo(),infoFirstNew.getSbbllRngStrtSn(),infoFirstNew.getSbbllRngEndSn());
        if(CollectionUtils.isEmpty(splitUserList)){
            billBankDao.saveOrUpdate(infoFirstNew);
        }else {
            BillAbVoucherinfo sel = splitUserList.get(0);
            BeanUtils.copyNotNullProperties(infoFirstNew,sel);
            //更新
            billBankDao.saveOrUpdate(sel);
            infoFirstNew = sel;
        }

        infoSecondNew.setBillMoney(info.getBillMoney().subtract(money));

        BigDecimal secondStartNo = firstEndNo.add(BigDecimal.ONE);
        infoSecondNew.setSbbllRngStrtSn(String.format("%12s",secondStartNo.setScale(0,BigDecimal.ROUND_HALF_UP)).replace(" ","0"));
        infoSecondNew.setOldDraftId(info.getId());
        /*infoSecondNew.setCreateBy(user == null ? null : user.getUsername());
        infoSecondNew.setUpdateBy(user == null ? null : user.getUsername());*/
        infoSecondNew.setCreateDate(new Date());
        infoSecondNew.setUpdateDate(new Date());
        billBankDao.saveOrUpdate(infoSecondNew);

        //先处理拆票后需要交易的部分
        List<BillAbVoucherinfoVice> handleVices = new ArrayList<BillAbVoucherinfoVice>();
        List<BillAbVoucherinfoVice> viceList = new ArrayList<BillAbVoucherinfoVice>();
        for (BillAbVoucherinfoVice viceDB : viceDBs) {
            handleVices.add(viceDB);
            viceList.add(viceDB);
        }
        List<BillAbAccrecords> handleAccs = new ArrayList<BillAbAccrecords>();
        List<BillAbAccrecords> accList = new ArrayList<BillAbAccrecords>();
        if(!CollectionUtils.isEmpty(records)) {
            for (BillAbAccrecords record : records) {
                handleAccs.add(record);
                accList.add(record);
            }
        }
        //拆票后，需要交易的处理
        for (BillAbVoucherinfoVice handleVice : handleVices) {
            BillAbVoucherinfoVice newObj = new BillAbVoucherinfoVice();
            BeanUtils.copyNotNullProperties(handleVice, newObj, "id");
            //处理拆票之后需要交易的从表记录
            newObj.setVid(infoFirstNew.getId());
            //newObj.setCreateBy(user == null ? null : user.getUsername());
            newObj.setUpdateDate(new Date());
            newObj.setCreateDate(new Date());
            //newObj.setUpdateBy(user == null ? null : user.getLoginName());
            List<BillAbVoucherinfoVice> saveList = new ArrayList<BillAbVoucherinfoVice>();
            saveList.add(newObj);
            billBankDao.batchSave(saveList);

            String firstLastId = null;
            for (BillAbAccrecords handleAcc : handleAccs) {
                if (!handleAcc.getVvid().equals(handleVice.getId())) {
                    continue;
                }
                BillAbAccrecords recordsFirstNews = new BillAbAccrecords();
                BeanUtils.copyNotNullProperties(handleAcc, recordsFirstNews, "id");
                recordsFirstNews.setVid(infoFirstNew.getId());
                recordsFirstNews.setVvid(newObj.getId());
                if (!StringUtil.isEmpty(firstLastId)) {
                    recordsFirstNews.setLastid(firstLastId);
                }
                recordsFirstNews.setCreateDate(new Date());
                recordsFirstNews.setUpdateDate(new Date());
                //recordsFirstNews.setCreateBy(user == null ? null : user.getUsername());
                List<BillAbAccrecords> recordsList = new ArrayList<BillAbAccrecords>();
                recordsList.add(recordsFirstNews);
                billAccrecordsDao.batchSave(recordsList);
                handleAccs2.add(recordsFirstNews);
                idMaphandle.put(handleAcc.getId(),recordsFirstNews.getId());
            }
        }
        //拆票后，不需要交易的处理
        for (BillAbVoucherinfoVice abVoucherinfoVice : viceList) {
            BillAbVoucherinfoVice newObj = new BillAbVoucherinfoVice();
            BeanUtils.copyNotNullProperties(abVoucherinfoVice, newObj, "id");
            newObj.setVid(infoSecondNew.getId());
            //validSign为1有效的才需要锁定，其他状态的数据保持原状
            if (AccebillConstant.AB_BILL_VALID_SIGN_1.equals(abVoucherinfoVice.getValidSign())) {
                //不需要交易的子票也修改为3，用于区分其他状态
                newObj.setValidSign("3");
            }
            //newObj.setCreateBy(user == null ? null : user.getUsername());
            newObj.setUpdateDate(new Date());
            newObj.setCreateDate(new Date());
            //newObj.setCreateBy(user == null ? null : user.getLoginName());
            List<BillAbVoucherinfoVice> saveList = new ArrayList<BillAbVoucherinfoVice>();
            saveList.add(newObj);
            billBankDao.batchSave(saveList);

            String firstLastId = null;
            if(CollectionUtils.isEmpty(accList)){
                continue;
            }
            for (BillAbAccrecords abAccrecords : accList) {
                if (!abAccrecords.getVvid().equals(abVoucherinfoVice.getId())) {
                    continue;
                }
                BillAbAccrecords recordsFirstNews = new BillAbAccrecords();
                BeanUtils.copyNotNullProperties(abAccrecords, recordsFirstNews, "id");
                recordsFirstNews.setVid(infoSecondNew.getId());
                recordsFirstNews.setVvid(newObj.getId());
                if (!StringUtil.isEmpty(firstLastId)) {
                    recordsFirstNews.setLastid(firstLastId);
                }
                recordsFirstNews.setCreateDate(new Date());
                recordsFirstNews.setUpdateDate(new Date());
                //recordsFirstNews.setCreateBy(user == null ? null : user.getUsername());
                List<BillAbAccrecords> recordsList = new ArrayList<BillAbAccrecords>();
                recordsList.add(recordsFirstNews);
                billAccrecordsDao.batchSave(recordsList);
                accList2.add(recordsFirstNews);
                idMapacc.put(abAccrecords.getId(),recordsFirstNews.getId());
            }
        }

        List<BillAbVoucherinfoVice> updVvList = new ArrayList<BillAbVoucherinfoVice>();
        //原数据的处理
        for (BillAbVoucherinfoVice viceDB : viceDBs) {
            //validSign为1有效的才需要锁定，其他状态的数据保持原状
            if (AccebillConstant.AB_BILL_VALID_SIGN_0.equals(viceDB.getValidSign())) {
                continue;
            }
            //viceDB.setUpdateBy(user == null ? viceDB.getUpdateBy() : user.getUsername());
            viceDB.setUpdateDate(new Date());
            viceDB.setValidSign("3");
            updVvList.add(viceDB);
        }
        billBankDao.batchSaveOrUpdate(updVvList);
        if(!CollectionUtils.isEmpty(records)) {
            for (BillAbAccrecords record : records) {
                //record.setCreateBy(user == null ? record.getCreateBy() : user.getUsername());
                record.setUpdateDate(new Date());
                record.setStatus(96);
            }
        }
        if(!CollectionUtils.isEmpty(records)){
            billAccrecordsDao.batchSaveOrUpdate(records);
        }

        String handId = "";
        if(!CollectionUtils.isEmpty(handleAccs2)) {
            for (int i = 0; i < handleAccs2.size(); i++) {
                BillAbAccrecords a = handleAccs2.get(i);
                if (StringUtil.isBlank(a.getLastid())) {
                    continue;
                }
                handId = idMaphandle.get(a.getLastid());
                a.setLastid(handId);
            }
        }
        billAccrecordsDao.batchSaveOrUpdate(handleAccs2);
        String accId = "";
        if(!CollectionUtils.isEmpty(accList2)) {
            for (int i = 0; i < accList2.size(); i++) {
                BillAbAccrecords a = accList2.get(i);
                if (StringUtil.isBlank(a.getLastid())) {
                    continue;
                }
                accId = idMapacc.get(a.getLastid());
                a.setLastid(accId);
            }
        }
        billAccrecordsDao.batchSaveOrUpdate(accList2);
        return infoFirstNew;
    }
}
