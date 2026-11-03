package com.bytter.increment.domain.service;

import com.bytter.biz.payment.api.constant.NisMessageConstant;
import com.bytter.core.constant.BdlinkConstant;
import com.bytter.core.constant.ResourceConstant;
import com.bytter.increment.api.dto.BisFinanceExcDTO;
import com.bytter.increment.api.service.IBisFinanceExcBackService;
import com.bytter.biz.payment.api.service.INisMessageService;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.constant.CommonConstant;
import com.bytter.core.sequence.SequenceSingleton;
import com.bytter.core.sequence.enums.SequenceEnum;
import com.bytter.core.utils.*;
import com.bytter.increment.domain.dao.BisFinanceExcDao;
import com.bytter.increment.domain.entity.BisFinanceExc;
import com.bytter.increment.domain.vo.BisFinanceExcVO;
import com.bytter.system.api.*;
import com.bytter.system.api.constant.CorpVerifyMessageConstant;
import com.bytter.system.api.constant.NotificationConstant;
import com.bytter.system.api.constant.SysConfigConstant;
import com.bytter.system.api.dto.*;
import com.xxl.job.core.context.XxlJobHelper;
import com.xxl.job.core.handler.annotation.XxlJob;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
/**
 * @author onlineGenerator
 * @version 1.0
 * @Title: Service
 * @Description: BIS_FINANCE_EXC
 * @date 2017-08-23 11:08:59
 */
@Service("bisFinanceExcService")
@Transactional
public class BisFinanceExcService extends BaseService<BisFinanceExc, String> implements ITaskService, ICorpDisableVerifyService {
    @Autowired
    private BisFinanceExcDao bisFinanceExcDao;
    @DubboReference(check = false)
	private IBisFinanceExcBackService bisFinanceExcBackService;
    @DubboReference(check = false)
	private ICorpService corpService;
    @DubboReference(check = false)
	private INotificationService notificationService;

    @Autowired
    private ChoMessageService choMessageService;
    
    @Resource
    private IMultiLangService multiLangServiceI;
    @DubboReference(check = false)
    private IDictService dictService;
    @DubboReference(check = false)
    private IBisBifInitService bisBifInitService;
    @DubboReference(check = false)
    private IBtCurrencyService btCurrencyService;
//    @Autowired
//    private BisMQService bisMQService;
    /**
     * 删除对象
     *
     * @param entity
     */
    @Override
    public void deleteEntity(BisFinanceExc entity) {
        super.deleteEntity(entity);
    }
    /**
     * 根据id删除
     *
     * @param id
     */
    @Override
    public void delete(String id) {
        super.delete(id);
    }
    /**
     * 保存
     *
     * @param entity
     */
    public void save(BisFinanceExc entity) {
        super.saveEntity(entity);
    }
    /**
     * 更新
     *
     * @param entity
     */
    public void update(BisFinanceExc entity) {
        super.updateEntity(entity);
    }
    /**
     * 保存or更新
     *
     * @param entity
     */
    @Override
    public void saveOrUpdate(BisFinanceExc entity) {
        super.saveOrUpdate(entity);
    }
    @Override
    public BaseDao<BisFinanceExc, String> getEntityDao() {
        return bisFinanceExcDao;
    }
    /**
     * 指令列表查询
     *
     * @param dataTablesInput
     * @return
     */
    @SuppressWarnings("rawtypes")
    public DataTablesOutput getDataTablesUnion(DataTablesInput dataTablesInput, ICurrentUser user) {
        StringBuilder sql = new StringBuilder();
        String resource = "";
        String opType = (String) dataTablesInput.getFormdata().get("opType");
        if ("listSend".equals(opType)) {
            //指令发送:p-审批通过 6-等待补充数据
            sql.append(" and b.voucher_Stat in ('" + BdlinkConstant.PASS + "','" + BdlinkConstant.WAIT_FILL + "')");
            resource = ResourceConstant.NETBANK_BDLINK_BISFINANCEEXC_SENDQUERY;
        } else if ("listTrace".equals(opType)) {
            //指令追踪 2-银行处理失败  9-接口处理失败
            sql.append(" and b.voucher_Stat in ('" + BdlinkConstant.BANK_FAILED + "','" + BdlinkConstant.INTERFACE_FAILED + "')");
            resource = ResourceConstant.NETBANK_BDLINK_BISFINANCEEXC_TRACEQUERY;
        } else if ("listSel".equals(opType)) {
            sql.append(" and b.cbs_bill_id not in (select f.pay_bill_id from cho_bank_finance f where  f.status <> -2 and f.pay_bill_id is not null)");
            resource = ResourceConstant.NETBANK_BDLINK_BISFINANCEEXC_QUERY;
        } else {
            resource = ResourceConstant.NETBANK_BDLINK_BISFINANCEEXC_QUERY;
        }
        String corpAuthSql = SqlUtil.getInSql(
                corpService.getUserCorpIds(resource, user.getId()), "b.corp_Id",
                true);
        sql.append(" and ").append(corpAuthSql);
        DataTablesOutput<BisFinanceExcVO> outputdata =  bisFinanceExcDao.getDataTablesUnion(dataTablesInput, sql.toString());
        return this.setJoinField(outputdata, opType);
    }
    /**
     * 设置付款办理的关联字段
     * @param output
     * @param opType
     * @return
     */
    public DataTablesOutput setJoinField(DataTablesOutput output, String opType) {
        List<BisFinanceExcVO> listBisFinaceExc = output.getData();
        if (EmptyUtils.isNotEmpty(listBisFinaceExc)) {
            Map<String, CorpDTO> corpMap = corpService.getCorpMap();
            Map<String, BtCurrencyDTO> curMap = btCurrencyService.getCurrency();
            Map<String, BisBifInitDTO> bisBifInitMap = bisBifInitService.getBisBifInitMap();
            Map<String, Map<String, DictDTO>> listDictMap = dictService.listDictMap(BdlinkConstant.EXC_VOUCHERSTAT_DICT, BdlinkConstant.EXC_DATEUNIT_DICT,
                    BdlinkConstant.EXC_FINANCE_BUSTYPE_DICT);
            for (BisFinanceExcVO bisFinanceExc : listBisFinaceExc) {
                if (corpMap.containsKey(bisFinanceExc.getCorpId())) {
                    bisFinanceExc.setCorpName(corpMap.get(bisFinanceExc.getCorpId()).getName());
                }
                if (curMap.containsKey(bisFinanceExc.getCurId())) {
                    bisFinanceExc.setCurName(curMap.get(bisFinanceExc.getCurId()).getCurName());
                }
                if (bisBifInitMap.containsKey(bisFinanceExc.getBifCode())) {
                    bisFinanceExc.setBifName(bisBifInitMap.get(bisFinanceExc.getBifCode()).getName());
                }
                //字典注入
                this.injectValues(bisFinanceExc, listDictMap);
            }
        }
        return output;
    }
    /**
     * 付款指令信息字典注入
     * @param vo
     * @param listDictMap
     */
    private void injectValues(BisFinanceExcVO vo, Map<String, Map<String, DictDTO>> listDictMap) {
        // 交易状态
        Optional.ofNullable(vo.getVoucherStat()).ifPresent(voucherStat -> {
            Map<String, DictDTO> dictDtoMap = listDictMap.get(BdlinkConstant.EXC_VOUCHERSTAT_DICT);
            Optional.ofNullable(dictDtoMap).flatMap(dictMap -> Optional.ofNullable(dictMap.get(voucherStat)))
                    .ifPresent(dict -> vo.setVoucherStatName(dict.getName()));
        });
        // 日期单位
        Optional.ofNullable(vo.getDateUnit()).ifPresent(dateUnit -> {
            Map<String, DictDTO> dictDtoMap = listDictMap.get(BdlinkConstant.EXC_DATEUNIT_DICT);
            Optional.ofNullable(dictDtoMap).flatMap(dictMap -> Optional.ofNullable(dictMap.get(dateUnit)))
                    .ifPresent(dict -> vo.setDateUnitName(dict.getName()));
        });
        // 业务类型
        Optional.ofNullable(vo.getBusType()).ifPresent(busType -> {
            Map<String, DictDTO> dictDtoMap = listDictMap.get(BdlinkConstant.EXC_FINANCE_BUSTYPE_DICT);
            Optional.ofNullable(dictDtoMap).flatMap(dictMap -> Optional.ofNullable(dictMap.get(busType+"")))
                    .ifPresent(dict -> vo.setBusTypeName(dict.getName()));
        });
    }
    /**
     * 根据ID获取指令
     *
     * @param id
     * @return
     */
    public BisFinanceExcVO getByIdUnion(String id) {
        return bisFinanceExcDao.getByIdUnion(id);
    }
    /**
     * 根据业务单号查询指令
     *
     * @param cbsBillId
     * @return
     */
    public BisFinanceExc getByCbsBillId(String cbsBillId) {
        return bisFinanceExcDao.getUniqueBy("cbsBillId", cbsBillId);
    }
    /**
     * 指令发送
     *
     * @param bisFinanceExc
     */
    public void sendBisFinanceExc(BisFinanceExc bisFinanceExc, ICurrentUser user) {
        int result = 0;
        if(StringUtils.isBlank(bisFinanceExc.getId())){
            //指令自动发送，生成状态为3-准备发送
            bisFinanceExc.setVoucherStat(BdlinkConstant.PRE_SEND);
            bisFinanceExc.setUpdateBy(user.getLoginName());
            bisFinanceExc.setUpdateDate(DateUtils.getDate());
            this.saveOrUpdate(bisFinanceExc);
            result = 1;
        }else {
            //指令手工发送，更新原指令6-待发送状态为3-准备发送
            result = this.bisFinanceExcDao.updateBisFinanceExcVoucherStat(bisFinanceExc, BdlinkConstant.PRE_SEND);
        }
        if(result>0) {
//            bisMQService.sendBisFinanceExcChangeMsg(bisFinanceExc, BdlinkConstant.PRE_SEND);
            //生成日志
            choMessageService.saveMessage(bisFinanceExc.getId(), NisMessageConstant.MESSAGE_TYPE_EXC_SEND, "", NisMessageConstant.BUS_TYPE_BDLINKFINANCE, user.getLoginName(), user.getUsername());
            //完成消息通知
            notificationService.complete(NotificationConstant.NETBANK.BISFINANCEEXC_SEND, bisFinanceExc.getId(), user.getId());
        }
    }
    /**
     * 指令退回
     *
     * @param bisFinanceExc
     */
    public void backBisFinanceExc(BisFinanceExc bisFinanceExc, String backMsg, ICurrentUser user) {
        //指令退回，更新原指令状态为B-指令退回
        int result = this.bisFinanceExcDao.updateBisFinanceExcVoucherStat(bisFinanceExc, BdlinkConstant.BACK);
        if(result>0) {
            //更新业务单
        	BisFinanceExcDTO dto = new BisFinanceExcDTO();
            BeanUtils.copyNotNullProperties(bisFinanceExc, dto);
            dto.setBackMsg(backMsg);
            // todo:数据转换
            bisFinanceExcBackService.backBisFinanceExc(dto, user);
//            bisMQService.sendBisFinanceExcChangeMsg(bisFinanceExc, BdlinkConstant.BACK);
            //生成日志
            choMessageService.saveMessage(bisFinanceExc.getId(), NisMessageConstant.MESSAGE_TYPE_EXC_BACK, backMsg, NisMessageConstant.BUS_TYPE_BDLINKFINANCE, user.getLoginName(), user.getUsername());
            //完成消息通知
            notificationService.complete(NotificationConstant.NETBANK.BISFINANCEEXC_SEND, bisFinanceExc.getId(), user.getId());
            notificationService.complete(NotificationConstant.NETBANK.BISFINANCEEXC_FAIL, bisFinanceExc.getId(), user.getId());
        }
    }
    /**
     * 指令重发
     *
     * @param bisFinanceExc
     */
    public void resendBisFinanceExc(BisFinanceExc bisFinanceExc, ICurrentUser user) {
        //指令重发，更新原指令状态为D-指令删除
        int result = this.bisFinanceExcDao.updateBisFinanceExcVoucherStat(bisFinanceExc, BdlinkConstant.DELETE);
        if(result>0) {
            //生成新的付款指令
            BisFinanceExc entity = new BisFinanceExc();
            BeanUtils.copyNotNullProperties(bisFinanceExc, entity);
            entity.setId(null);
            entity.setReqDate(DateUtils.getDate());
            entity.setVoucherNo(SequenceSingleton.getSequence(SequenceEnum.BIS_FINANCE_EXC));
            entity.setVoucherStat(BdlinkConstant.PRE_SEND);
            entity.setCreateBy(user.getLoginName());
            entity.setCreateDate(DateUtils.getDate());
            entity.setUpdateBy(user.getLoginName());
            entity.setUpdateDate(DateUtils.getDate());
            entity.setNoticeSign(CommonConstant.SEND_SIGN_NO);
            //指令重发标志：1-重发指令
            entity.setResendSign(CommonConstant.SEND_SIGN_YES);
            this.saveOrUpdate(entity);
//            bisMQService.sendBisFinanceExcChangeMsg(bisFinanceExc, BdlinkConstant.PRE_SEND);
            //生成日志
            choMessageService.saveMessage(bisFinanceExc.getId(), NisMessageConstant.MESSAGE_TYPE_EXC_RESEND, "", NisMessageConstant.BUS_TYPE_BDLINKFINANCE, user.getLoginName(), user.getUsername());
            //完成消息通知
            notificationService.complete(NotificationConstant.NETBANK.BISFINANCEEXC_FAIL, bisFinanceExc.getId(), user.getId());
        }
    }
    /**
     * 指令作废
     *
     * @param bisFinanceExc
     */
    public void defeatBisFinanceExc(BisFinanceExc bisFinanceExc, ICurrentUser user) {
        //指令作废，更新原指令状态为7-指令无效
        int result = this.bisFinanceExcDao.updateBisFinanceExcVoucherStat(bisFinanceExc, BdlinkConstant.INVALIDATE);
        if(result>0) {
//            bisMQService.sendBisFinanceExcChangeMsg(bisFinanceExc, BdlinkConstant.INVALIDATE);
            //生成日志
            choMessageService.saveMessage(bisFinanceExc.getId(), NisMessageConstant.MESSAGE_TYPE_EXC_REPEAL, "", NisMessageConstant.BUS_TYPE_BDLINKFINANCE, user.getLoginName(), user.getUsername());
            //完成消息通知
            notificationService.complete(NotificationConstant.NETBANK.BISFINANCEEXC_FAIL, bisFinanceExc.getId(), user.getId());
        }
    }
    /**
     * 指令交易成功/失败消息提醒
     */
    @Override
    @XxlJob("BisFinanceExcService")
    public void run(String arguments) {
        String jobParam = XxlJobHelper.getJobParam();
        arguments = jobParam;
        logger.info("定时任务接收参数：【{}】", jobParam);
        List<BisFinanceExcVO> list = null;
        if (NotificationConstant.NETBANK.BISFINANCEEXC_FAIL.equals(arguments)) {
            //交易失败提醒
            list = this.bisFinanceExcDao.listByVoucherStat(false);
        } else if (NotificationConstant.NETBANK.BISFINANCEEXC_SUCCESS.equals(arguments)) {
            //交易成功提醒
            list = this.bisFinanceExcDao.listByVoucherStat(true);
        }
        if (!EmptyUtils.isEmptyList(list)) {
            List<String> ids = new ArrayList<>();
            for (BisFinanceExcVO exc : list) {
                ids.add(exc.getId());
                //消息提醒
                notificationService.send(exc.getId(), exc.getCorpId(), arguments, exc);
            }
            //更新消息提醒标志
            this.bisFinanceExcDao.updateNoticeSign(ids);
        }
    }
    @Override
    public List<CorpVerifyErrorDTO> verifyCorpDisable(String corpId) {
        if(StringUtils.isNotEmpty(corpId)){
            List<CorpVerifyErrorDTO> verifyErrorDTOS = new ArrayList<CorpVerifyErrorDTO>();
            //单位有在途的理财指令，不允许禁用！
            int num = this.bisFinanceExcDao.getFinanceExcCountByCorpId(corpId);
            if(num>0){
                verifyErrorDTOS.add(new CorpVerifyErrorDTO(corpId,
                        SysConfigConstant.Module.NIS,
                        SysConfigConstant.BusType.NIS_FINANCE_EXC,
                        multiLangServiceI.getLang(CorpVerifyMessageConstant.NIS_VALID_DISABLE_FINANCEEXC_ERROR)));
            }
            return verifyErrorDTOS;
        }
        return null;
    }
    /**
     * 指令对应业务单号更新（指令生成时判断原指令状态）
     * @param bisFinanceExc
     * @return
     */
    public int updateBisFinanceExcCbsBillNo(BisFinanceExc bisFinanceExc){
        return this.bisFinanceExcDao.updateBisFinanceExcCbsBillNo(bisFinanceExc);
    }

    public BisFinanceExcDTO getByVoucherNo(String voucherNo) {
        BisFinanceExcVO vo = bisFinanceExcDao.getByVoucherNo(voucherNo);
        BisFinanceExcDTO bisFinanceExcDTO = new BisFinanceExcDTO();
        BeanUtils.copyProperties(vo,bisFinanceExcDTO);
        return bisFinanceExcDTO;
    }
}
