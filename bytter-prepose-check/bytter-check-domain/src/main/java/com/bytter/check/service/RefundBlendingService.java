package com.bytter.check.service;//package com.bytter.system.service;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import cn.hutool.core.date.StopWatch;
import com.bytter.biz.payment.api.dto.FcVoucherTypeDTO;
import com.bytter.biz.payment.api.service.IFcVoucherTypeService;
import com.bytter.check.dao.PaymentBlendingDao;
import com.bytter.system.api.*;
import com.bytter.system.api.constant.SysConfigConstant;
import com.bytter.system.api.dto.DictDTO;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bytter.biz.bdlink.api.dto.BisAccDtlDTO;
import com.bytter.biz.bdlink.api.service.IBisAccDtlService;
import com.bytter.biz.bdlink.vo.BisAccDtlVO;
import com.bytter.biz.finacial.api.service.IFcDayCloseService;
import com.bytter.biz.payment.api.dto.BisExcDTO;
import com.bytter.biz.payment.api.dto.NisBillBankSerialNoDTO;
import com.bytter.biz.payment.api.dto.NisBillheadDTO;
import com.bytter.biz.payment.api.service.IBisExcService;
import com.bytter.biz.payment.api.service.INisBillheadService;
import com.bytter.check.dao.RefundBlendingDao;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.exception.ParameterException;
import com.bytter.core.constant.BdlinkConstant;
import com.bytter.core.constant.BtBankAccConstant;
import com.bytter.core.constant.CommonConstant;
import com.bytter.core.constant.NetbankConstant;
import com.bytter.core.constant.ResourceConstant;
import com.bytter.core.utils.BeanUtils;
import com.bytter.core.utils.DateUtils;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.core.utils.SqlUtil;
import com.bytter.core.utils.StringUtils;
import com.bytter.erp.dto.DSLIntefaceFlagDTO;
import com.bytter.erp.service.IDSLIntefaceFlagService;
import com.bytter.system.api.constant.AccountGrantConstant;
import com.bytter.system.api.constant.NotificationConstant;
import com.bytter.system.api.dto.BtBankAccDTO;
import com.bytter.system.api.dto.CorpDTO;
import com.bytter.system.api.enums.NetbankMessageEnum;
import com.xxl.job.core.handler.annotation.XxlJob;

//TODO 放到 资金核对
@Service("refundBlendingService")
@Transactional
public class RefundBlendingService extends BaseService<BisAccDtlVO, String> implements ITaskService {
    @Autowired
    private RefundBlendingDao refundBlendingDao;
    @DubboReference(check = false)
    private IAccountGrantService accountGrantService;
    @DubboReference(check = false)
    private INisBillheadService nisBillheadService;
    @DubboReference(check = false)
    private INotificationService notificationService;
    @DubboReference(check = false)
    private IBisExcService bisExcService;
    @DubboReference(check = false)
    private IBisAccDtlService bisAccDtlService;
    @DubboReference(check = false)
    private IFcDayCloseService iFcDayCloseService;
    @DubboReference(check = false)
    private ICorpService iCorpService;
    @DubboReference(check = false)
    private IDSLIntefaceFlagService idslIntefaceFlagService;
    @DubboReference(check = false)
    private IConfigService configService;
    @DubboReference(check = false)
    private IBtBankAccService iBtBankAccService;

    @DubboReference(check = false)
    private IFcVoucherTypeService fcVoucherTypeService;

    @Autowired
    private IDictService dictServiceI;
    @Autowired
    private PaymentBlendingDao paymentBlendingDao;
    @Override
    public BaseDao<BisAccDtlVO, String> getEntityDao() {
        return refundBlendingDao;
    }
    /**
     * 获取银行明细列表
     * @param dataTablesInput
     * @param user
     * @return
     */
    public DataTablesOutput getDataTablesUnion(DataTablesInput dataTablesInput, ICurrentUser user) {
        StopWatch stopWatch = new StopWatch("退款勾兑查询");
        stopWatch.start("获取单位列表");
        List<String> corpList = iCorpService.getCorpIdsByUserResource(ResourceConstant.NETBANK_BANKACC_REFUNDBLENDING_QUERY, user.getId());
        stopWatch.stop();
        stopWatch.start("获取账户列表");
        StringBuilder sql = new StringBuilder();
        List<String> accList =  accountGrantService.listByMultiValAccs(user.getId(), ResourceConstant.NETBANK_BANKACC_REFUNDBLENDING_QUERY, null, AccountGrantConstant.ONLINE_BANK_TYPE + CommonConstant.COMMA_SPLIT + AccountGrantConstant.OFFLINE_BANK_TYPE, AccountGrantConstant.QUERY_DETAIL);
        stopWatch.stop();
        //根据查询条件及账户数据进行一次过滤
        stopWatch.start("账户数据过滤");
        List<String> accList2 = bisAccDtlService.getAcclistByCondition(dataTablesInput, corpList, accList);
        stopWatch.stop();
        String corpAuthSql = SqlUtil.getInSql(accList2, "t.bank_acc", true);
        sql.append(" and ").append(corpAuthSql);
        stopWatch.start("退款勾兑查询");
//        return this.refundBlendingDao.getDataTablesUnion(dataTablesInput, sql.toString());
        DataTablesOutput output = this.refundBlendingDao.getDataTablesUnion(dataTablesInput, sql.toString());
        stopWatch.stop();
        List<BisAccDtlVO> bisAccDtlVOList =  output.getData();
        if(bisAccDtlVOList.size()>0) {
            stopWatch.start("获取账号数据");
            Map<String, BtBankAccDTO> accMap = iBtBankAccService.getBankAccMapByAccsAndCurId(accList);
            stopWatch.stop();
            stopWatch.start("获取数据字典数据");
            Map<String, DictDTO> dictMap = dictServiceI.listMap("paymentblending_status");
            stopWatch.stop();
            stopWatch.start("获取勾对时间数据");
            List<String> dtlIdList = bisAccDtlVOList.stream().map(BisAccDtlVO::getId).collect(Collectors.toList());
            //勾对时间 nisBillheadService
            Map<String, NisBillBankSerialNoDTO> blentMap = nisBillheadService.getBlendingMap(dtlIdList);
            stopWatch.stop();
            stopWatch.start("组装数据");
            for(BisAccDtlVO vo:bisAccDtlVOList){
                BtBankAccDTO acc=accMap.get(vo.getBankAcc()+vo.getCurId());
                if(!EmptyUtils.isEmpty(acc)) {
                    vo.setCorpId(acc.getCorpId());
                    vo.setCorpName(acc.getCorpName());
                    vo.setCurId(acc.getCurId());
                    vo.setCurName(acc.getCurName());
                    vo.setAttributeId(acc.getAttributeId());
                    vo.setAttributeName(acc.getAttributeName());
                }
                // 勾兑标志
                Optional.ofNullable(vo.getBlendSign())
                        .flatMap(blendSign -> Optional.ofNullable(dictMap.get(blendSign)))
                        .ifPresent(dict -> vo.setBlendSignName(dict.getName()));
                // 勾对时间
                Optional.ofNullable(blentMap.get(vo.getId()))
                        .ifPresent(nisBillBankSerialNoDTO -> vo.setPaymentBlendingDate(nisBillBankSerialNoDTO.getPaymentblendingDate()));
            }
            stopWatch.stop();
        }
        logger.info(stopWatch.prettyPrint(TimeUnit.MILLISECONDS));
        return output;
    }
    /**
     * 获取付款单列表
     * @param dataTablesInput
     * @param user
     * @return
     */
    public DataTablesOutput getBillDataTablesUnion(DataTablesInput dataTablesInput, ICurrentUser user) {
        return this.nisBillheadService.listNisBillheadByRefundblending(dataTablesInput);
    }
    /**
     * 保存付款单业务类型
     * @param id
     * @param busType
     */
    public int toSaveBillBusType(String id,String busType) {
        return refundBlendingDao.toSaveBillBusType(id,busType);
    }
    /**
     * 更新流水勾兑状态
     * @param id
     * @param blendSign
     * @return
     */
    public int toUpdateBlendSign(String id,String blendSign,String refundBlendBy){
        return refundBlendingDao.toUpdateBlendSign(id,blendSign,refundBlendBy);
    }
    /**
     * 更新流水勾兑状态-定时任务使用
     * @param id
     * @param blendSign
     * @return
     */
    public int toUpdateBlendSignForJob(String id,String blendSign,String refundBlendBy){
        return refundBlendingDao.toUpdateBlendSignForJob(id,blendSign,refundBlendBy);
    }
    /**
     * 更新流水的流水号
     * @param id
     * @param bankSerialNo
     * @return
     */
    public int toUpdateBankSerialNo(String id,String bankSerialNo){
        return refundBlendingDao.toUpdateBankSerialNo(id,bankSerialNo);
    }
    /**
     * 更新流水共享单号
     * @param id
     * @param sourceNum
     * @return
     */
    public int toUpdateSourceNum(String id,String sourceNum){
        return refundBlendingDao.toUpdateSourceNum(id,sourceNum);
    }
    /**
     * 更新指令与付款单的流水号
     * @param id
     * @param bankSerialNo
     * @return
     */
    public void toUpdateBisExc(String id,String bankSerialNo){
        bisExcService.updateBisExcForBankSerialNo(id,bankSerialNo);
        nisBillheadService.updateNisBillheadForBankSerialNo(id,bankSerialNo);
    }
    /**
     * 自动勾兑
     * @param ids
     * @param bankAccTypeStr
     */
    public String aoto(String ids, String bankAccTypeStr,String handle,ICurrentUser user){
        String autoTrue = configService.getConfig(NetbankConstant.RECEIVABLE_CONFIG_TYPE, NetbankConstant.RECEIVABLE_CONFIG_AUTOTRUE).getValue();
        String msg = "";
        String[] idArr = ids.split(",");
        String id = "";
        for(int i = 0;i<idArr.length;i++){
            if(StringUtils.isNotEmpty(idArr[i])){
                if(i == 0) {
                    id = "'"+idArr[i]+"'";
                }else {
                    id+= ",'"+idArr[i]+"'";
                }
            }
        }
        List<BisAccDtlVO> dtlList = refundBlendingDao.listDtlByids(id);
        DataTablesInput input = new DataTablesInput();
        Map<String,BisAccDtlVO> tempMap = new HashMap<>();
        Map<String,BisAccDtlVO> repeatMap = new HashMap<>();
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
            input.getFormdata().put("eqs_bankSerialNo",dtlList.get(i).getBankSerialNo());
            input.getFormdata().put("blendSign","0");
            input.getFormdata().put("type","auto");
            if(eqg_payMoney){
                input.getFormdata().put("eqg_payMoney",dtlList.get(i).getAmt());
            }
            if(eqs_payerAcc){
                input.getFormdata().put("eqs_payerAcc",dtlList.get(i).getBankAcc());
            }
            if(eqs_payeeAcc){
                input.getFormdata().put("eqs_payeeAcc",dtlList.get(i).getOppAccNo());
            }
            if(wishPayday){
                Date date = dtlList.get(i).getTransTime();
                String dateStr = DateUtils.formatDate(date,"yyyy-MM-dd");
                input.getFormdata().put("wishPayday",dateStr);
            }
            if(eqs_curId){
                input.getFormdata().put("eqs_curId",dtlList.get(i).getCurId());
            }
            //得到付款单
            DataTablesOutput output = this.nisBillheadService.listNisBillheadByRefundblending(input);
            if(output.getData().size() == 1){
                List<NisBillheadDTO> list = output.getData();
                //银行流水编号为空的才勾兑
                if(StringUtils.isEmpty(list.get(0).getBankSerialNo())) {
                      if(!tempMap.containsKey(list.get(0).getId())) {
                          tempMap.put(list.get(0).getId(), dtlList.get(i));
                      }else{
                          repeatMap.put(list.get(0).getId(), dtlList.get(i));
                      }
                }
            }
        }
        for(String key:tempMap.keySet()){
            BisAccDtlVO dtl = tempMap.get(key);
            if(repeatMap.containsKey(key)){
                continue;
            }else{
                isHasData = true;
                this.toUpdateBlendSign(dtl.getId(), BtBankAccConstant.BLENDSIGN_3, user.getLoginName());
                if (StringUtils.isNotEmpty(dtl.getBankSerialNo())) {
                    this.toUpdateBisExc(key, dtl.getBankSerialNo());
                } else {
                    this.toUpdateBankSerialNo(dtl.getId(), dtl.getId());
                    this.toUpdateBisExc(key, dtl.getId());
                }
                //如果参数自动勾对是否直接确认  为是则自动确认
                if("1".equals(autoTrue)){
                    msg = this.aotoTrueForAoto(dtl.getId(),key,handle,user);
                }
            }
        }
        if(!isHasData){
            msg ="bankacc.message.notDate";
        }
        return msg;
    }
    /**
     * 自动勾兑确认
     * @param dtlId
     * @param billId
     * @param handle
     * @return
     */
    public String aotoTrueForAoto(String dtlId, String billId,String handle,ICurrentUser user){
        String msg = "";
        //获取付款单
        NisBillheadDTO nisBillheadDTO = nisBillheadService.getNisBillheadById(billId);
        if(handle.equals(BtBankAccConstant.BLENDTYPE_3)){
            //付款方式
            FcVoucherTypeDTO fcVoucherTypeDTO = fcVoucherTypeService.getFcVoucherTypeById(nisBillheadDTO.getVoucherTypeId());
            //如果记账才判断记账凭证问题
            if("1".equals(fcVoucherTypeDTO.getIsAccount())) {
                String corpId = nisBillheadDTO.getCorpId();
                //得到付款单直属网点
                CorpDTO corpDTO = iCorpService.getNetByCorpId(corpId);
                //得到日结日期
                Date dayCloseDate = iFcDayCloseService.getDayCloseFinalDate(corpDTO.getNetId());
                //如果期望支付日期大于日结日期 表示未日结 需要提示用户作废账务处理“请及时作废原单的记账凭证！”
                if (DateUtils.dayDiffs(nisBillheadDTO.getWishPayday(), dayCloseDate) > 0) {
                    msg = "1";
                    //是否已付款的付款单无付款指令
                    if (CommonConstant.NO.equals(nisBillheadDTO.getIsPayed())) {
                        //作废原指令
                        BisExcDTO bisExcDTO = bisExcService.getBisExcByCbsBillNo(nisBillheadDTO.getBillNo(), BdlinkConstant.FROM_NIS_PAY);
                        bisExcService.updateBisExc(bisExcDTO.getId(), user);
                    }
                } else {
                    msg = "2";
                    return msg;
                }
            }
            nisBillheadService.backNisBillhead(billId,user,NetbankConstant.BACK_TYPE_5);
        }
        //原单处理方式 为 生成新单 生成新的付款单
        if(handle.equals(BtBankAccConstant.BLENDTYPE_2)){
            NisBillheadDTO newNisBillhead = new NisBillheadDTO();
            BeanUtils.copyNotNullProperties(nisBillheadDTO,newNisBillhead);
            newNisBillhead.setId(null);
            newNisBillhead.setBillNo(null);
            newNisBillhead.setStatus(NetbankConstant.STATUE_TEMP);
            newNisBillhead.setBillFrom(NetbankConstant.BILLFROM_7);
            nisBillheadService.saveNisBillhead(newNisBillhead,user);
        }
        //更新流水表勾兑标志为1已勾兑
        this.toUpdateBlendSign(dtlId,BtBankAccConstant.BLENDSIGN_1, user.getUsername());
        //更新流水表原单处理方式
        refundBlendingDao.toUpdateBlend(dtlId,handle);
        return msg;
    }


    public String aotoTrue(String dtlId, String billId,String handle,ICurrentUser user){
        String msg = "";
        //获取付款单
        NisBillheadDTO nisBillheadDTO = nisBillheadService.getNisBillheadById(billId);
        if(handle.equals(BtBankAccConstant.BLENDTYPE_3)){
            String corpId = nisBillheadDTO.getCorpId();
            //得到付款单直属网点
            CorpDTO corpDTO = iCorpService.getNetByCorpId(corpId);
            //得到日结日期
            Date dayCloseDate = iFcDayCloseService.getDayCloseFinalDate(corpDTO.getNetId());
            //如果期望支付日期大于日结日期 表示未日结 需要提示用户作废账务处理“请及时作废原单的记账凭证！”
            if(DateUtils.dayDiffs(nisBillheadDTO.getWishPayday(),dayCloseDate) > 0){
                msg = "1";
            }else{
                msg = "2";
                return msg;
            }
            nisBillheadService.backNisBillhead(billId,user, NetbankConstant.BACK_TYPE_5);
        }
        //原单处理方式 为 生成新单 生成新的付款单
        if(handle.equals(BtBankAccConstant.BLENDTYPE_2)){
            NisBillheadDTO newNisBillhead = new NisBillheadDTO();
            BeanUtils.copyNotNullProperties(nisBillheadDTO,newNisBillhead);
            newNisBillhead.setId(null);
            newNisBillhead.setBillNo(null);
            newNisBillhead.setStatus(NetbankConstant.STATUE_TEMP);
            newNisBillhead.setBillFrom(NetbankConstant.BILLFROM_7);
            nisBillheadService.saveNisBillhead(newNisBillhead,user);
        }
        //更新流水表勾兑标志为1已勾兑
        this.toUpdateBlendSign(dtlId,BtBankAccConstant.BLENDSIGN_1, user.getLoginName());
        //更新流水表原单处理方式
        refundBlendingDao.toUpdateBlend(dtlId,handle);
        return msg;
    }
    /**
     * 打回付款单
     * @param billId
     * @param user
     */
    public void backNisBillhead(String billId,ICurrentUser user){
//        nisBillheadService.backNisBillhead(billId,user,NetbankConstant.BACK_TYPE_5);
        NisBillheadDTO nisBillheadDTO = nisBillheadService.getNisBillheadById(billId);
        //是否已付款的付款单无付款指令
        if (CommonConstant.NO.equals(nisBillheadDTO.getIsPayed())) {
            //作废原指令
            BisExcDTO bisExcDTO = bisExcService.getBisExcByCbsBillNo(nisBillheadDTO.getBillNo(), BdlinkConstant.FROM_NIS_PAY);
            if (bisExcDTO != null) {
                //指令无效
                bisExcDTO.setVoucherStat(BdlinkConstant.INVALIDATE);
                bisExcDTO.setStatus(BdlinkConstant.STATUE_DEL);
                bisExcService.updateBisExc(bisExcDTO, user);
            }
        }
    }
    /**
     * 手工勾兑
     * @param dtlId
     * @param billId
     * @param bankSerialNo
     * @param handle
     * @param user
     */
    public String  manualPaymentBlending(String dtlId, String billId,String bankSerialNo,String handle,ICurrentUser user){
        String msg = "";
        //获取付款单
        NisBillheadDTO nisBillheadDTO = nisBillheadService.getNisBillheadById(billId);
        DSLIntefaceFlagDTO dslIntefaceFlag = idslIntefaceFlagService.getFlagBy(nisBillheadDTO.getBillNo() + ";S", "dsl_payment_result");
        if(handle.equals(BtBankAccConstant.BLENDTYPE_3)){
            String corpId = nisBillheadDTO.getCorpId();
            //得到付款单直属网点
            CorpDTO corpDTO = iCorpService.getNetByCorpId(corpId);
            //得到日结日期
            Date dayCloseDate = iFcDayCloseService.getDayCloseFinalDate(corpDTO.getNetId());
            //如果期望支付日期大于日结日期 表示未日结 需要提示用户作废账务处理“请及时作废原单的记账凭证！”
            if(DateUtils.dayDiffs(nisBillheadDTO.getWishPayday(),dayCloseDate) > 0){
                msg = "1";
            }else{
                msg = "2";
                return msg;
            }
            nisBillheadService.backNisBillhead(billId,user,NetbankConstant.BACK_TYPE_5);
        }
        //原单处理方式 为 生成新单 生成新的付款单
        if(handle.equals(BtBankAccConstant.BLENDTYPE_2)){
            NisBillheadDTO newNisBillhead = new NisBillheadDTO();
            BeanUtils.copyNotNullProperties(nisBillheadDTO,newNisBillhead);
            newNisBillhead.setStatus(NetbankConstant.STATUE_TEMP);
            newNisBillhead.setBillFrom(NetbankConstant.BILLFROM_7);
            newNisBillhead.setRefundOrgId(nisBillheadDTO.getId());
            newNisBillhead.setId(null);
            newNisBillhead.setBillNo(null);
            nisBillheadService.saveNisBillhead(newNisBillhead,user);
        }
        this.toUpdateBlendSign(dtlId,BtBankAccConstant.BLENDSIGN_1,user.getLoginName());
        if(StringUtils.isNotEmpty(bankSerialNo)) {
            this.toUpdateBisExc(billId, bankSerialNo);
        }else{
            this.toUpdateBisExc(billId, dtlId);
            this.toUpdateBankSerialNo(dtlId,dtlId);
        }
        //更新流水的共享单号
        String sourceNum;
        if (StringUtils.isNotEmpty(nisBillheadDTO.getBursarBillNo())) {
            sourceNum = nisBillheadDTO.getBursarBillNo();
        }else{
            sourceNum = nisBillheadDTO.getBillNo();
        }
        this.toUpdateSourceNum(dtlId, sourceNum);
        //插入退款勾兑中间表
        paymentBlendingDao.insetBillBankSerialNoRefund(billId, "nis_billhead", dtlId, dtlId);
        //更新流水表原单处理方式
        refundBlendingDao.toUpdateBlend(dtlId,handle);
        return msg;
    }
    /**
     * 取消勾兑
     * @param dtlId
     * @param billId
     * @return
     */
    public String cancelPaymentBlending(String dtlId, String billId,String handle,ICurrentUser user){
        String msg = "";
        //获取付款单
        NisBillheadDTO nisBillheadDTO = nisBillheadService.getNisBillheadById(billId);
        BisAccDtlDTO bisAccDtlDTO =  bisAccDtlService.getById(dtlId);
        handle = bisAccDtlDTO.getRefundBlendType();//获取勾兑时的原单处理方式
        //勾兑后原单处理方式为打回原单，不允许取消勾兑
        if(BtBankAccConstant.BLENDTYPE_3.equals(handle)){
            msg = "1";
            return msg;
        }
        //原单处理方式 为 生成新单 生成新的付款单
        if(BtBankAccConstant.BLENDTYPE_2.equals(handle)){
            // 根据原单获取新单
            List<NisBillheadDTO> nisBillheadDTOList =  nisBillheadService.listByRefundOrgId(billId);
            for (NisBillheadDTO dto : nisBillheadDTOList){
                if(dto.getBillFrom().equalsIgnoreCase(NetbankConstant.BILLFROM_7)){
                    if(NetbankConstant.STATUE_DEL.equals(dto.getStatus()) || NetbankConstant.STATUE_BACK.equals(dto.getStatus()) || NetbankConstant.STATUE_TEMP.equals(dto.getStatus())) {
                        nisBillheadService.updateNisBillheadForRefundBlend(dto.getId(), NetbankConstant.STATUE_DEL, "退款勾兑取消勾兑", "", user);
                    }else{
                        //付款单[付款单号]已提交付款，不允许取消勾兑
                        msg = dto.getBillNo();
                        return msg;
                    }
                }
            }
        }
        this.toUpdateBlendSign(dtlId,BtBankAccConstant.BLENDSIGN_0, null);
        this.toUpdateBisExc(billId,"");
        //更新流水的共享单号
        this.toUpdateSourceNum(dtlId, "");
        //删除退款勾兑中间表
        nisBillheadService.deleteBillBankSerialNoRefundByBillId(billId);
        return msg;
    }
    /**
     * 自动提示付款勾兑
     */
    @Override
    @XxlJob("RefundBlendingService")
    public void run(String arguments) {
//		银行账号为${bankAcc}在${endDate}过期，请及时办理续期或者注销手续！
        try {
            Date curDate = DateUtils.getDate();
            this.logger.info(curDate + "---启用付款勾兑提醒，开始执行定时任务---");
            Map<String,Object> map = this.refundBlendingDao.getDtlCountNoblendSign();
            map.put("count",map.get("COUNT"));
            if (EmptyUtils.isEmpty(map)) {
                this.logger.info(curDate + "---付款未勾兑的数量为0---");
            } else {
                notificationService.completeListByTag(NotificationConstant.NETBANK.BANKACC_PAYMENTBLENDING);
                notificationService.sendToUserByResPermission(ResourceConstant.NETBANK_BANKACC_PAYMENTBLENDING_QUERY,NotificationConstant.NETBANK.BANKACC_PAYMENTBLENDING,map);
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
    public void scrap(String dtlId){
        this.toUpdateBlendSign(dtlId,BtBankAccConstant.BLENDSIGN_2,null);
    }
    /**
     * 取消作废
     * @param dtlId
     */
    public void cancelScrap(String dtlId){
        this.toUpdateBlendSign(dtlId,BtBankAccConstant.BLENDSIGN_0,null);
    }
    /**
     * 银行流水号为空。更新为id
     * @param id
     */
    public void updateDtlBankSerialNo(String id){
        bisAccDtlService.updateDtlBankSerialNo(id);
    }
}
