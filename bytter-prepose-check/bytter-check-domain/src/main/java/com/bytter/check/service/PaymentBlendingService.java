package com.bytter.check.service;

import cn.hutool.core.util.ObjectUtil;
import com.bytter.biz.bdlink.api.service.IBisAccDtlService;
import com.bytter.biz.payment.api.service.IBisExcService;
import com.bytter.biz.bdlink.vo.BisAccDtlVO;
import com.bytter.biz.payment.api.dto.VNisPayBillDTO;
import com.bytter.biz.payment.api.service.INisBillheadService;
import com.bytter.biz.payment.api.vo.NisBillBankSerialNoVO;
import com.bytter.check.dao.PaymentBlendingDao;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.constant.AllocationConstant;
import com.bytter.core.constant.BtBankAccConstant;
import com.bytter.core.constant.CommonConstant;
import com.bytter.core.constant.ResourceConstant;
import com.bytter.core.utils.DateUtils;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.core.utils.SqlUtil;
import com.bytter.core.utils.StringUtils;
import com.bytter.system.api.*;
import com.bytter.system.api.constant.AccountGrantConstant;
import com.bytter.system.api.constant.NotificationConstant;
import com.bytter.system.api.dto.BtBankAccDTO;
import com.bytter.system.api.dto.DictDTO;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.xxl.job.core.handler.annotation.XxlJob;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service("paymentBlendingService")
//@Scope(proxyMode = ScopedProxyMode.TARGET_CLASS)
@Transactional
public class PaymentBlendingService extends BaseService<BisAccDtlVO, String> implements ITaskService {
    @Autowired
    private PaymentBlendingDao paymentBlendingDao;
    @Autowired
    private IAccountGrantService accountGrantService;
    //TODO 放到资金核对
    @DubboReference(check = false)
    private INisBillheadService nisBillheadService;
    @DubboReference(check = false)
    private INotificationService notificationService;
    @DubboReference(check = false)
    private IBisAccDtlService bisAccDtlService;
    @DubboReference(check = false)
    private IUserService userService;
    @DubboReference(check = false)
    private IConfigService configService;
    @DubboReference(check = false)
    private IBtCurrencyService currencyService;
    @DubboReference(check = false)
    private IBtBankAccService iBtBankAccService;
    @DubboReference(check = false)
    private IDictService dictService;
    @DubboReference(check = false)
    private ICorpService corpService;
    @DubboReference(check = false)
    private IBisExcService iBisExcService;

    @Override
    public BaseDao<BisAccDtlVO, String> getEntityDao() {
        return paymentBlendingDao;
    }
    /**
     * 获取银行明细列表
     * @param dataTablesInput
     * @param user
     * @return
     */
    public DataTablesOutput getDataTablesUnion(DataTablesInput dataTablesInput, ICurrentUser user) {
        StringBuilder sql = new StringBuilder();
        String corpId = (String) dataTablesInput.getFormdata().get("ins_corpId");
        List<String> accList;
        if(StringUtils.isNotEmpty(corpId)){
            String[] corpIds = corpId.split(CommonConstant.COMMA_SPLIT);
            List<BtBankAccDTO> bankAccDTOS = iBtBankAccService.getBankAccByCorpIdList(Lists.newArrayList(corpIds), CommonConstant.EMPTY_STR, new HashMap<>());
            accList = bankAccDTOS.stream().map(BtBankAccDTO::getBankAcc).collect(Collectors.toList());
        }else {
            accList =  accountGrantService.listByMultiValAccs(user.getId(), ResourceConstant.NETBANK_BANKACC_PAYMENTBLENDING_QUERY, null, AccountGrantConstant.ONLINE_BANK_TYPE + CommonConstant.COMMA_SPLIT + AccountGrantConstant.OFFLINE_BANK_TYPE, AccountGrantConstant.QUERY_DETAIL);
        }
        String corpAuthSql = SqlUtil.getInSql(accList, "t.bank_acc", true);
        sql.append(" and ").append(corpAuthSql);
        //手动调拨单是否要与付款明细勾对
        String allocationAllowBlending = configService.getConfig(AllocationConstant.ALLOT_CONFIG_TYPE, AllocationConstant.ALLOT_CONFIG_ALLOCATIONALLOWBLENDING).getValue();
        DataTablesOutput output = this.paymentBlendingDao.getDataTablesUnion(dataTablesInput, sql.toString(),allocationAllowBlending,accList);
        List<BisAccDtlVO> list = output.getData();
        appendDictField(list);
        Map<String, String> currencyNameMap=currencyService.getCurrencyNameMap();
        Map<String, BtBankAccDTO> accDTOMap=iBtBankAccService.getBankAccByInfoAccs(accList);
        for(BisAccDtlVO bisAccDtlVO : list){
            if (StringUtils.isEmpty(bisAccDtlVO.getCurName())) {
                bisAccDtlVO.setCurName(currencyNameMap.get(bisAccDtlVO.getCurId()));
            }
            BtBankAccDTO btBankAccDTO = accDTOMap.get(bisAccDtlVO.getBankAcc());
            if (StringUtils.isEmpty(bisAccDtlVO.getCorpId())) {
                bisAccDtlVO.setCorpId(btBankAccDTO.getCorpId());
            }
            if (StringUtils.isEmpty(bisAccDtlVO.getCorpName()) && btBankAccDTO != null) {
                bisAccDtlVO.setCorpName(btBankAccDTO.getCorpName());
            }
        }
        return output;
    }

    //todo
    private void appendDictField(List<BisAccDtlVO> bisAccDtlVOS) {
        Map<String, Map<String, DictDTO>> dictMap = dictService.listDictMap("btBankAccDtl.cdSign", "btBankAccDtl.rbSign", "paymentblending_status", "refundblending_handle", "btBankAcc.accType");
        Map<String, DictDTO> emptyMap = Maps.newHashMap();
        List<String> dtlIdList=new ArrayList<>();
        for (BisAccDtlVO bisAccDtlVO : bisAccDtlVOS) {
            if (StringUtils.isNotEmpty(bisAccDtlVO.getCdSign())) {
                Map<String, DictDTO> cdSignMap = dictMap.getOrDefault("btBankAccDtl.cdSign", emptyMap);
                bisAccDtlVO.setCdSignName(cdSignMap.getOrDefault(bisAccDtlVO.getCdSign(), DictDTO.EMPTY).getName());
            }
            if (StringUtils.isNotEmpty(bisAccDtlVO.getRbSign())) {
                Map<String, DictDTO> rbSignMap = dictMap.getOrDefault("btBankAccDtl.rbSign", emptyMap);
                bisAccDtlVO.setRbSignName(rbSignMap.getOrDefault(bisAccDtlVO.getRbSign(), DictDTO.EMPTY).getName());
            }
            if (StringUtils.isNotEmpty(bisAccDtlVO.getBlendSign())) {
                Map<String, DictDTO> statusSignMap = dictMap.getOrDefault("paymentblending_status", emptyMap);
                bisAccDtlVO.setBlendSignName(statusSignMap.getOrDefault(bisAccDtlVO.getBlendSign(), DictDTO.EMPTY).getName());
            }
            if (StringUtils.isNotEmpty(bisAccDtlVO.getRefundBlendType())) {
                Map<String, DictDTO> statusSignMap = dictMap.getOrDefault("refundblending_handle", emptyMap);
                bisAccDtlVO.setRefundBlendTypeName(statusSignMap.getOrDefault(bisAccDtlVO.getRefundBlendType(), DictDTO.EMPTY).getName());
            }
            if (StringUtils.isNotEmpty(bisAccDtlVO.getAccType())) {
                Map<String, DictDTO> statusSignMap = dictMap.getOrDefault("btBankAcc.accType", emptyMap);
                bisAccDtlVO.setAccTypeName(statusSignMap.getOrDefault(bisAccDtlVO.getBlendSign(), DictDTO.EMPTY).getName());
            }
            //勾对时间
            dtlIdList.add(bisAccDtlVO.getId());
        }
        /*
            页面自动勾兑、手动勾兑时都已经处理了 bis_acc_dtl表中的 paymentblending_date ，无需做特殊处理
            //勾对时间 nisBillheadService
            Map<String, NisBillBankSerialNoDTO>  blentMap =nisBillheadService.getBlendingMap(dtlIdList);
            if (blentMap.size()>0) {
                bisAccDtlVOS.forEach(item->{
                    if (blentMap.containsKey(item.getId())){
                        item.setPaymentBlendingDate(blentMap.get(item.getId()).getPaymentblendingDate());
                    }
                });
            }
         */
    }


    /**
     * 获取付款单列表
     * @param dataTablesInput
     * @param user
     * @return
     */
    public DataTablesOutput getBillDataTablesUnion(DataTablesInput dataTablesInput, ICurrentUser user) {
        List<String> corpList = corpService.getCorpIdsByUserResource(ResourceConstant.NETBANK_BANKACC_PAYMENTBLENDING_QUERY, user.getId());
        String joinSql = " and "+SqlUtil.getInSql(corpList,"n.corpId",true);
        return this.nisBillheadService.listNisBillheadByDtl(dataTablesInput,joinSql);
    }
    /**
     * 保存付款单业务类型
     * @param id
     * @param busType
     */
    public int toSaveBillBusType(String id,String busType,String isFrom) {
        int result = 0;
        if("nis_billhead".equals(isFrom)){
            result = paymentBlendingDao.toSaveBillBusType(id,busType);
        }else if("nis_wage".equals(isFrom)){
            result = paymentBlendingDao.toSaveWageBusType(id,busType);
        }
        return result;
    }
    /**
     * 更新流水勾兑状态
     * @param id
     * @param blendSign
     * @return
     */
    public int toUpdateBlendSign(String id,String blendSign, String userName){
        return bisAccDtlService.toUpdateBlendSign(id,blendSign, userName);
    }
    /**
     * 更新流水的流水号
     * @param id
     * @param bankSerialNo
     * @return
     */
    public int toUpdateBankSerialNo(String id,String bankSerialNo){
        return bisAccDtlService.toUpdateBankSerialNo(id,bankSerialNo);
    }
    /**
     * 更新指令与付款单的流水号
     * @param id
     * @param bankSerialNo
     * @return
     */
    public void toUpdateBisExc(String id,String bankSerialNo,String isFrom,String dtlId){
//        bisExcService.updateBisExcForBankSerialNo(id,bankSerialNo);
//        nisBillheadService.updateNisBillheadForBankSerialNo(id,bankSerialNo);
        //多笔明细可以勾兑一笔付款单，插入中间表
        nisBillheadService.insetBillBankSerialNo(id,isFrom,bankSerialNo,dtlId);
    }
    /**
     * todo
     * 自动勾兑
     * @param ids
     * @param bankAccTypeStr
     */
    public String autoPaymentBlending(String ids, String bankAccTypeStr, String userName){
        String msg = "";
        String[] idArr = ids.split(",");
        List<BisAccDtlVO> dtlList = paymentBlendingDao.listDtlByids(idArr);
        DataTablesInput input = new DataTablesInput();
        Map<String, BisAccDtlVO> tempMap = new HashMap<>();
        Map<String, BisAccDtlVO> repeatMap = new HashMap<>();
        Boolean eqg_payMoney = false;
        Boolean eqs_payerAcc = false;
        Boolean eqs_payeeAcc = false;
        Boolean wishPayday = false;
        Boolean eqs_curId = false;
        Boolean isHasData = false;
        if(bankAccTypeStr.contains(BtBankAccConstant.PAYMENTBLENDING_1)){
            eqg_payMoney = true;
        }
        if(bankAccTypeStr.contains(BtBankAccConstant.PAYMENTBLENDING_2)){
            eqs_payerAcc = true;
        }
        if(bankAccTypeStr.contains(BtBankAccConstant.PAYMENTBLENDING_3)){
            eqs_payeeAcc = true;
        }
        if(bankAccTypeStr.contains(BtBankAccConstant.PAYMENTBLENDING_4)){
            wishPayday = true;
        }
        if(bankAccTypeStr.contains(BtBankAccConstant.PAYMENTBLENDING_5)){
            eqs_curId = true;
        }
        for(int i=0;i<dtlList.size();i++){
            BisAccDtlVO bisAccDtlVO = dtlList.get(i);
            input.getFormdata().put("eqs_bankSerialNo", bisAccDtlVO.getBankSerialNo());
            if(eqg_payMoney){
                if(EmptyUtils.isEmpty(bisAccDtlVO.getAmt())){
                    continue;
                }
                input.getFormdata().put("eqg_payMoney",bisAccDtlVO.getAmt());
            }
            if(eqs_payerAcc){
                if(EmptyUtils.isEmpty(bisAccDtlVO.getBankAcc())){
                    continue;
                }
                input.getFormdata().put("eqs_payerAcc",bisAccDtlVO.getBankAcc());
            }
            if(eqs_payeeAcc){
                if(EmptyUtils.isEmpty(bisAccDtlVO.getOppAccNo())){
                    continue;
                }
                input.getFormdata().put("eqs_payeeAcc",bisAccDtlVO.getOppAccNo());
            }
            if(wishPayday){
                if(EmptyUtils.isEmpty(bisAccDtlVO.getTransTime())){
                    continue;
                }
                Date date = bisAccDtlVO.getTransTime();
                String dateStr = DateUtils.formatDate(date,"yyyy-MM-dd");
                input.getFormdata().put("wishPayday",dateStr);
            }
            if(eqs_curId){
                if(EmptyUtils.isEmpty(bisAccDtlVO.getCurId())){
                    continue;
                }
                input.getFormdata().put("eqs_curId",bisAccDtlVO.getCurId());
            }
            //TODO
            DataTablesOutput output = this.nisBillheadService.listNisBillheadByDtl(input,null);
            if(output.getData().size() == 1){
                List<VNisPayBillDTO> list = output.getData();
                //银行流水编号为空的才勾兑
                if(StringUtils.isEmpty(list.get(0).getBankSerialNo())) {
                      if(!tempMap.containsKey(list.get(0).getId()+","+list.get(0).getIsFrom())) {
                          tempMap.put(list.get(0).getId()+","+list.get(0).getIsFrom(), dtlList.get(i));
                      }else{
                          repeatMap.put(list.get(0).getId()+","+list.get(0).getIsFrom(), dtlList.get(i));
                      }
                }
            }
        }
        for(String key:tempMap.keySet()){
            BisAccDtlVO dtl = tempMap.get(key);
            if(!repeatMap.containsKey(key)){
                isHasData = true;
                this.toUpdateBlendSign(dtl.getId(), BtBankAccConstant.BLENDSIGN_1, userName);
                this.toUpdateBisExc(key.substring(0,key.indexOf(",")), dtl.getId(),key.substring(key.indexOf(",")+1),dtl.getId());
            }
        }
        if(!isHasData){
            msg ="bankacc.message.notDate";
        }
//        this.run(NotificationConstant.NETBANK.BANKACC_PAYMENTBLENDING);
        return msg;
    }
    /**
     * 手工勾兑 : 只能一笔明细勾兑一笔单据
     * @param dtlId
     * @param billId
     * @return
     */
    public void manualPaymentBlending(String dtlId, String billId,String isFrom, String userName){
        // 只能一笔明细勾兑一笔单据
        if (ObjectUtil.isNotEmpty(dtlId) && ObjectUtil.isNotEmpty(billId)){
            //校验明细是否已勾对，否则不执行
            int result = this.toUpdateBlendSign(dtlId, BtBankAccConstant.BLENDSIGN_1, userName);
            if(result>0) {
                this.toUpdateBisExc(billId, dtlId, isFrom, dtlId);
            }
        }
        this.run(NotificationConstant.NETBANK.BANKACC_PAYMENTBLENDING);
    }
    /**
     * 取消勾兑
     * @param dtlId
     * @return
     */
    public void cancelPaymentBlending(String dtlId, String userName){
        String[] dtlIds =  dtlId.split(",");
        //所有的流水id
        List<String> dtlIdList = new ArrayList<>();
        //所有的业务id
        List<String> billIdList = new ArrayList<>();
        for(int i=0;i<dtlIds.length;i++) {
            if(StringUtils.isEmpty(dtlIds[i])){
                continue;
            }
            List<String> billTempList = nisBillheadService.getBillId(dtlIds[i]);
            billIdList.addAll(billTempList);
            dtlIdList.add(dtlIds[i]);
        }
        for(int j=0;j<billIdList.size();j++) {
            if(StringUtils.isEmpty(billIdList.get(j))){
                continue;
            }
            List<String> dtlTempList = nisBillheadService.getDtlId(billIdList.get(j));
            dtlIdList.addAll(dtlTempList);
        }
        //更新状态
        for(String id : dtlIdList){
            this.toUpdateBlendSign(id, BtBankAccConstant.BLENDSIGN_0, userName);
            this.nisBillheadService.deleteBillBankSerialNoByDtlId(id);
        }
        //删除中间表
        for(String id : billIdList){
            this.nisBillheadService.deleteBillBankSerialNoByBillId(id);
        }
        this.run(NotificationConstant.NETBANK.BANKACC_PAYMENTBLENDING);
    }
    /**
     * 自动提示付款勾兑
     */
    @Override
    @XxlJob("PaymentBlendingService")
    public void run(String arguments) {
//		银行账号为${bankAcc}在${endDate}过期，请及时办理续期或者注销手续！
        try {
            //手动调拨单是否要与付款明细勾对
            String allocationAllowBlending = configService.getConfig(AllocationConstant.ALLOT_CONFIG_TYPE, AllocationConstant.ALLOT_CONFIG_ALLOCATIONALLOWBLENDING).getValue();
            Date curDate = DateUtils.getDate();
            this.logger.info(curDate + "---启用付款勾兑提醒，开始执行定时任务---");
            List<String> userIds = userService.listUserIdsByPermission(ResourceConstant.NETBANK_BANKACC_PAYMENTBLENDING_QUERY);
            List<Map<String, Object>> list = this.paymentBlendingDao.getDtlCountNoblendSign(userIds,allocationAllowBlending);
            if (EmptyUtils.isEmptyList(list)) {
                this.logger.info(curDate + "---付款未勾兑的数量为0---");
            } else {
                notificationService.completeListByTag(NotificationConstant.NETBANK.BANKACC_PAYMENTBLENDING);
                for (Map<String, Object> map : list){
                    Map<String,Object> count = new HashMap<>();
                    count.put("count", map.get("COUNT"));
                    notificationService.sendToUser(null, map.get("USERID").toString(), NotificationConstant.NETBANK.BANKACC_PAYMENTBLENDING, count);
//                    notificationService.sendToUserByResPermission(ResourceConstant.NETBANK_BANKACC_PAYMENTBLENDING_APPLY, NotificationConstant.NETBANK.BANKACC_PAYMENTBLENDING, map);
                }
//                notificationService.sendToUserByResPermission(ResourceConstant.NETBANK_BANKACC_PAYMENTBLENDING_QUERY,NotificationConstant.NETBANK.BANKACC_PAYMENTBLENDING,map);
            }
            this.logger.info("---启用付款勾兑提醒，结束执行定时任务---");
        } catch (Exception e) {
            this.logger.error(e.getMessage(), e);
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
    /**
     * 作废
     * @param dtlId
     */
    public void scrap(String dtlId, String userName){
        this.toUpdateBlendSign(dtlId, BtBankAccConstant.BLENDSIGN_2, userName);
    }
    /**
     * 取消作废
     * @param dtlId
     */
    public void cancelScrap(String dtlId, String userName){
        this.toUpdateBlendSign(dtlId, BtBankAccConstant.BLENDSIGN_0, userName);
    }
    /**
     * 银行流水号为空。更新为id
     * @param id
     */
    public void updateDtlBankSerialNo(String id){
        bisAccDtlService.updateDtlBankSerialNo(id);
    }
    /**
     * 获取银行明细列表
     * @param dataTablesInput
     * @return
     */
    public DataTablesOutput getPaymentDtlDataTable(DataTablesInput dataTablesInput) {
        DataTablesOutput dataTablesOutput= this.paymentBlendingDao.getPaymentDtlDataTable(dataTablesInput);
        List<BisAccDtlVO> list=dataTablesOutput.getData();
        for(BisAccDtlVO vo:list){
            if(StringUtils.isNotEmpty(vo.getCorpId())){
                String corpName=corpService.getCorpName(vo.getCorpId());
                vo.setCorpName(corpName);
            }
            if(StringUtils.isNotEmpty(vo.getCorpId())){
                String blendSignName=dictService.getByName("paymentblending_status",vo.getBlendSign());
                vo.setBlendSignName(blendSignName);
            }
        }
        return dataTablesOutput;
    }
    /**
     * 获取银行明细列表
     * @param dataTablesInput
     * @param user
     * @return
     */
    public DataTablesOutput exportAll(DataTablesInput dataTablesInput, ICurrentUser user) {
        StringBuilder sql = new StringBuilder();
        //手动调拨单是否要与付款明细勾对
        String allocationAllowBlending = configService.getConfig(AllocationConstant.ALLOT_CONFIG_TYPE, AllocationConstant.ALLOT_CONFIG_ALLOCATIONALLOWBLENDING).getValue();
        List<String> accList =  accountGrantService.listByMultiValAccIds(user.getId(), ResourceConstant.NETBANK_BANKACC_PAYMENTBLENDING_QUERY, null, AccountGrantConstant.ONLINE_BANK_TYPE + CommonConstant.COMMA_SPLIT + AccountGrantConstant.OFFLINE_BANK_TYPE, AccountGrantConstant.QUERY_DETAIL);
        String corpAuthSql = SqlUtil.getInSql(accList, "acc.id", true);
        sql.append(" and ").append(corpAuthSql);
        DataTablesOutput dataTablesOutput = this.paymentBlendingDao.exportAll(dataTablesInput, sql.toString(), allocationAllowBlending);
        List<BisAccDtlVO> list=dataTablesOutput.getData();
        for (BisAccDtlVO bisAccDtlVO : list) {
            bisAccDtlVO.setCorpName(corpService.getCorpName(bisAccDtlVO.getCorpId()));
            bisAccDtlVO.setBlendSignName(dictService.getByName("paymentblending_status",bisAccDtlVO.getBlendSign()));
            String curId = bisAccDtlVO.getCurId();
            if (StringUtils.isNotEmpty(curId)){
                bisAccDtlVO.setCurName(currencyService.getObjectById(bisAccDtlVO.getCurId()).getCurName());
            }
        }
        //开始反射dict和corp值
        return dataTablesOutput;
    }
}
