package com.bytter.biz.bdlinks.service;

import com.bytter.biz.allocation.entity.NisAllocationReq;
import com.bytter.biz.allocation.service.NisAllocationReqService;
import com.bytter.biz.bdlink.api.constant.BdlinkConstant;
import com.bytter.biz.bdlink.api.dto.BisExcChangeMsg;
import com.bytter.biz.bdlinks.dao.BisGatherCmdDao;
import com.bytter.biz.bdlinks.entity.BisExc;
import com.bytter.biz.bdlinks.entity.BisGatherCmd;
import com.bytter.biz.bdlinks.vo.BisExcVO;
import com.bytter.biz.bdlinks.vo.BisGatherCmdVO;
import com.bytter.biz.payment.api.constant.NisMessageConstant;
import com.bytter.biz.bdlink.api.constant.ResourceConstant;
import com.bytter.biz.bdlink.api.enums.NetbankMessageEnum;
import com.bytter.biz.payment.api.dto.BisGatherCmdDTO;
import com.bytter.biz.payment.api.service.IBisExcBackService;
import com.bytter.biz.payment.api.service.IBisGatherCmdBackService;
import com.bytter.biz.payment.api.service.INisMessageService;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.exception.BusinessException;
import com.bytter.core.common.exception.ParameterException;
import com.bytter.core.constant.CommonConstant;
import com.bytter.core.utils.*;
import com.bytter.system.api.*;
import com.bytter.system.api.constant.CorpVerifyMessageConstant;
import com.bytter.system.api.constant.NotificationConstant;
import com.bytter.system.api.constant.SysConfigConstant;
import com.bytter.system.api.dto.*;
import com.xxl.job.core.context.XxlJobHelper;
import com.xxl.job.core.handler.annotation.XxlJob;
import com.bytter.core.sequence.SequenceSingleton;
import com.bytter.core.sequence.enums.SequenceEnum;

import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
/**
 * @author onlineGenerator
 * @version 1.0
 * @Title: Service
 * @Description: 收款指令表
 * @date 2017-07-04 15:57:19
 */
@Service("bisGatherCmdService")
@Transactional
public class BisGatherCmdService extends BaseService<BisGatherCmd, String> implements ITaskService, ICorpDisableVerifyService, ICorpParentChangVerifyService {
    @Autowired
    private BisGatherCmdDao bisGatherCmdDao;
    @Autowired
    private NisAllocationReqService nisAllocationReqService;
    @DubboReference(check = false)
	private IBisGatherCmdBackService bisGatherCmdBackService;
    @DubboReference(check = false)
	private INisMessageService nisMessageService;
    @DubboReference(check = false)
	private INotificationService notificationService;
    @DubboReference(check = false)
	private IBtBankAccService btBankAccService;
    @DubboReference(check = false)
	private ICorpService corpService;
    @Resource
    private IMultiLangService multiLangServiceI;
    @DubboReference(check = false)
    private IBtCurrencyService btCurrencyService;
    @DubboReference(check = false)
    private IRegionService regionService;
    @DubboReference(check = false)
    private IBisBifInitService bisBifInitService;
    @DubboReference(check = false)
    private IConfigService configService;
    @DubboReference(check = false)
    private IDictService dictService;

//    @Autowired
//    private BisMQService bisMQService;


    /**
     * 删除对象
     *
     * @param entity
     */
    @Override
    public void deleteEntity(BisGatherCmd entity) {
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
    public void save(BisGatherCmd entity) {
        super.saveEntity(entity);
    }
    /**
     * 更新
     *
     * @param entity
     */
    public void update(BisGatherCmd entity) {
        super.updateEntity(entity);
    }
    /**
     * 保存or更新
     *
     * @param entity
     */
    @Override
    public void saveOrUpdate(BisGatherCmd entity) {
        super.saveOrUpdate(entity);
    }
    @Override
    public BaseDao<BisGatherCmd, String> getEntityDao() {
        return bisGatherCmdDao;
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
            sql.append(" and b.cmd_Stat in ('" + BdlinkConstant.PASS + "','" + BdlinkConstant.WAIT_FILL + "')");
            resource = ResourceConstant.NETBANK_BDLINK_BISGATHERCMD_SENDQUERY;
        } else if ("listTrace".equals(opType)) {
            //指令追踪 2-银行处理失败  9-接口处理失败
            resource = ResourceConstant.NETBANK_BDLINK_BISGATHERCMD_TRACEQUERY;
            sql.append(" and b.cmd_Stat in ('" + BdlinkConstant.BANK_FAILED + "','" + BdlinkConstant.INTERFACE_FAILED + "','" + BdlinkConstant.WAIT_RESULT + "')");
        } else {
            resource = ResourceConstant.NETBANK_BDLINK_BISGATHERCMD_QUERY;
        }
        if (!StringUtils.isBlank(resource)) {
            String corpAuthSql = SqlUtil.getInSql(
                    corpService.getUserCorpIds(resource, user.getId()), "p.corp_Id",
                    true);
            sql.append(" and ").append(corpAuthSql);
        }
        DataTablesOutput<BisGatherCmdVO> outputdata = bisGatherCmdDao.getDataTablesUnion(dataTablesInput, sql.toString());
        return this.setJoinField(outputdata, opType);
    }
    /**
     * 设置付款办理的关联字段
     * @param output
     * @param opType
     * @return
     */
    public DataTablesOutput setJoinField(DataTablesOutput output, String opType) {
        List<BisGatherCmdVO> listBisGatherCmd = output.getData();
        if (EmptyUtils.isNotEmpty(listBisGatherCmd)) {
            Map<String, BtCurrencyDTO> curMap = btCurrencyService.getCurrency();
            Map<String, BisBifInitDTO> bisBifInitMap = bisBifInitService.getBisBifInitMap();
            Map<String, Map<String, DictDTO>> listDictMap = dictService.listDictMap(BdlinkConstant.EXC_VOUCHERSTAT_DICT, BdlinkConstant.CMD_BUSORITYPE_DICT,
                    BdlinkConstant.EXC_CMDTYPE_DICT, BdlinkConstant.EXC_VOUCHERFROM_DICT);
            List<String> ids = listBisGatherCmd.stream().map(bisGatherCmd -> bisGatherCmd.getChildAccId()).collect(Collectors.toList());
            List<String> ids2 = listBisGatherCmd.stream().map(bisGatherCmd -> bisGatherCmd.getParentAccId()).collect(Collectors.toList());
            Set<String> set = new HashSet<String>();
            set.addAll(ids);
            set.addAll(ids2);
            List<String> list = set.stream().collect(Collectors.toList());
            List<BtBankAccDTO> listDTO = btBankAccService.getBankAccByIdList(list);
            Map<String, BtBankAccDTO> accMap = listDTO.stream().collect(Collectors.toMap(BtBankAccDTO::getId, Function.identity(),(a,b)->a));
            Map<String, RegionDTO> mapRegion = regionService.getRegionMap();
            for (BisGatherCmdVO bisGatherCmd : listBisGatherCmd) {
                BtBankAccDTO childAcc = (BtBankAccDTO)accMap.get(bisGatherCmd.getChildAccId());
                if(!EmptyUtils.isEmpty(childAcc)) {
                    bisGatherCmd.setChildAcc(childAcc.getBankAcc());
                    bisGatherCmd.setChildAccName(childAcc.getAccName());
                    bisGatherCmd.setChildBank(childAcc.getBankName());
                    bisGatherCmd.setChildProv(childAcc.getProv());
                    bisGatherCmd.setChildProvName(mapRegion.get(childAcc.getCity()) != null ? mapRegion.get(childAcc.getCity()).getName() : "");
                    bisGatherCmd.setChildCity(childAcc.getProv());
                    bisGatherCmd.setChildCityName(mapRegion.get(childAcc.getCity()) != null ? mapRegion.get(childAcc.getCity()).getName() : "");
                }
                BtBankAccDTO parentAcc = (BtBankAccDTO)accMap.get(bisGatherCmd.getParentAccId());
                if(!EmptyUtils.isEmpty(parentAcc)) {
                    bisGatherCmd.setParentAcc(parentAcc.getBankAcc());
                    bisGatherCmd.setParentAccName(parentAcc.getAccName());
                    bisGatherCmd.setParentBank(parentAcc.getBankName());
                    bisGatherCmd.setParentProv(parentAcc.getProv());
                    bisGatherCmd.setParentProvName(mapRegion.get(parentAcc.getProv()) != null ? mapRegion.get(parentAcc.getProv()).getName() : "");
                    bisGatherCmd.setChildCity(parentAcc.getCity());
                    bisGatherCmd.setChildCityName(mapRegion.get(parentAcc.getCity()) != null ? mapRegion.get(parentAcc.getCity()).getName() : "");
                }
                if (curMap.containsKey(bisGatherCmd.getCurId())) {
                    bisGatherCmd.setCurName(curMap.get(bisGatherCmd.getCurId()).getCurName());
                }
                if (bisBifInitMap.containsKey(bisGatherCmd.getBifCode())) {
                    bisGatherCmd.setBifName(bisBifInitMap.get(bisGatherCmd.getBifCode()).getName());
                }
                //字典注入
                this.injectValues(bisGatherCmd, listDictMap);
            }
        }
        return output;
    }
    /**
     * 付款指令信息字典注入
     * @param vo
     * @param listDictMap
     */
    private void injectValues(BisGatherCmdVO vo, Map<String, Map<String, DictDTO>> listDictMap) {
        // 交易状态
        Optional.ofNullable(vo.getCmdStat()).ifPresent(cmdStat -> {
            Map<String, DictDTO> dictDtoMap = listDictMap.get(BdlinkConstant.EXC_VOUCHERSTAT_DICT);
            Optional.ofNullable(dictDtoMap).flatMap(dictMap -> Optional.ofNullable(dictMap.get(cmdStat)))
                    .ifPresent(dict -> vo.setCmdStatName(dict.getName()));
        });
        // 业务类型
        Optional.ofNullable(vo.getBusOriType()).ifPresent(busOriType -> {
            Map<String, DictDTO> dictDtoMap = listDictMap.get(BdlinkConstant.CMD_BUSORITYPE_DICT);
            Optional.ofNullable(dictDtoMap).flatMap(dictMap -> Optional.ofNullable(dictMap.get(String.valueOf(busOriType)))).ifPresent(dict -> vo.setBusOriTypeName(dict.getName()));
        });
        // 指令类型
        Optional.ofNullable(vo.getCmdType()).ifPresent(cmdType -> {
            Map<String, DictDTO> dictDtoMap = listDictMap.get(BdlinkConstant.EXC_CMDTYPE_DICT);
            Optional.ofNullable(dictDtoMap).flatMap(dictMap -> Optional.ofNullable(dictMap.get(cmdType)))
                    .ifPresent(dict -> vo.setCmdTypeName(dict.getName()));
        });
        // 指令来源
        Optional.ofNullable(vo.getVoucherFrom()).ifPresent(voucherFrom -> {
            Map<String, DictDTO> dictDtoMap = listDictMap.get(BdlinkConstant.EXC_VOUCHERFROM_DICT);
            Optional.ofNullable(dictDtoMap).flatMap(dictMap -> Optional.ofNullable(dictMap.get(voucherFrom)))
                    .ifPresent(dict -> vo.setVoucherFromName(dict.getName()));
        });
    }
    /**
     * 根据ID获取指令
     *
     * @param id
     * @return
     */
    public BisGatherCmdVO getByIdUnion(String id) {
        BisGatherCmdVO bisGatherCmdVO =  bisGatherCmdDao.getByIdUnion(id);
        if (bisGatherCmdVO != null) {
            BtBankAccDTO childAcc = btBankAccService.getBankAccById(bisGatherCmdVO.getChildAccId());
            bisGatherCmdVO.setChildAcc(childAcc.getBankAcc());
            bisGatherCmdVO.setChildAccName(childAcc.getAccName());
            bisGatherCmdVO.setChildBank(childAcc.getBankName());
            BtBankAccDTO parentAcc = btBankAccService.getBankAccById(bisGatherCmdVO.getParentAccId());
            bisGatherCmdVO.setParentAcc(parentAcc.getBankAcc());
            bisGatherCmdVO.setParentAccName(parentAcc.getAccName());
            bisGatherCmdVO.setParentBank(parentAcc.getBankName());
            // 币种
            BtCurrencyDTO currencyDTO = btCurrencyService.getObjectById(bisGatherCmdVO.getCurId());
            bisGatherCmdVO.setCurName(currencyDTO == null ? "" : currencyDTO.getCurName());
            // 银企接口
            BisBifInitDTO bisBifInitDTO = bisBifInitService.getObjectById(bisGatherCmdVO.getBifCode());
            bisGatherCmdVO.setBifName(bisBifInitDTO == null ? "" : bisBifInitDTO.getName());
            // 子账号省市
            if (!StringUtil.isBlank(bisGatherCmdVO.getChildProv())) {
                RegionDTO prov = regionService.getById(bisGatherCmdVO.getChildProv());
                bisGatherCmdVO.setChildProvName(prov == null ? "" : prov.getName());
            }
            if (!StringUtil.isBlank(bisGatherCmdVO.getChildCity())) {
                RegionDTO city = regionService.getById(bisGatherCmdVO.getChildCity());
                bisGatherCmdVO.setChildCityName(city == null ? "" : city.getName());
            }
            // 父账号省市
            if (!StringUtil.isBlank(bisGatherCmdVO.getParentProv())) {
                RegionDTO prov = regionService.getById(bisGatherCmdVO.getParentProv());
                bisGatherCmdVO.setParentProvName(prov == null ? "" : prov.getName());
            }
            if (!StringUtil.isBlank(bisGatherCmdVO.getParentCity())) {
                RegionDTO city = regionService.getById(bisGatherCmdVO.getParentCity());
                bisGatherCmdVO.setParentCityName(city == null ? "" : city.getName());
            }
        }
        return bisGatherCmdVO;
    }
    /**
     * 根据业务单号查询指令
     *
     * @param cbsBillId
     * @return
     */
    public BisGatherCmd getByCbsBillId(String cbsBillId) {
        return bisGatherCmdDao.getUniqueBy("cbsBillId", cbsBillId);
    }
    /**
     * 根据业务单号查询指令
     *
     * @param cbsBillId
     * @return
     */
    public BisGatherCmdVO getByCbsBillIdUnion(String cbsBillId) {
        return bisGatherCmdDao.getByCbsBillIdUnion(cbsBillId);
    }
    /**
     * 根据指令单号查询付款指令
     *
     * @param voucherNo
     * @return
     */
    public BisGatherCmdVO getByVoucherNo(String voucherNo) {
        return bisGatherCmdDao.getByVoucherNo(voucherNo);
    }
    /**
     * 指令发送
     *
     * @param bisGatherCmd
     */
    public void sendBisGatherCmd(BisGatherCmd bisGatherCmd, ICurrentUser user) {
        //数据防篡改校验
        if(!bisGatherCmd.getVerifyCode().equals(this.setVerifyCode(bisGatherCmd))){
            throw new ParameterException(NetbankMessageEnum.BIS_EXC_MESSAGE_PAYINFO_CHANGED, bisGatherCmd.getVoucherNo()); //不应校验前置？
        }
        int result = 0;
        if(StringUtils.isBlank(bisGatherCmd.getId())){
            //指令自动发送，生成状态为3-准备发送
            bisGatherCmd.setCmdStat(BdlinkConstant.PRE_SEND);
            bisGatherCmd.setUpdateBy(user.getLoginName());
            bisGatherCmd.setUpdateDate(DateUtils.getDate());
            this.saveOrUpdate(bisGatherCmd); //BIS_GATHER_CMD 收款指令表
            result = 1;
        }else {
            //指令手工发送，更新原指令6-待发送状态为3-准备发送
            result = this.bisGatherCmdDao.updateBisGatherCmdVoucherStat(bisGatherCmd, BdlinkConstant.PRE_SEND);
        }
        if (result > 0) {
//            bisMQService.sendBisGatherExcChangeMsg(bisGatherCmd, BdlinkConstant.PRE_SEND); //发送收款指令变更消息 //原本发送了，也没有处理逻辑
            //生成日志
            nisMessageService.saveMessage(bisGatherCmd.getId(), NisMessageConstant.MESSAGE_TYPE_EXC_SEND, "", NisMessageConstant.BUS_TYPE_BDLINKGATHER, user.getLoginName(), user.getUsername());
            //完成消息通知
            notificationService.complete(NotificationConstant.NETBANK.BISGATHERCMD_SEND, bisGatherCmd.getId(), user.getId());
        } else {
            throw new BusinessException(NetbankMessageEnum.NIS_PAY_MESSAGE_STATUS_CHANGE, bisGatherCmd.getVoucherNo());  //TODO 无法提前校验？
        }
    }
    /**
     * 指令退回
     *
     * @param bisGatherCmd
     */
    public void backBisGatherCmd(BisGatherCmd bisGatherCmd, String backMsg, ICurrentUser user) {
        //指令退回，更新原指令状态为B-指令退回
        int result = this.bisGatherCmdDao.updateBisGatherCmdVoucherStat(bisGatherCmd, BdlinkConstant.BACK);
        if(result>0) {
            //更新业务单
            BisGatherCmdDTO dto = new BisGatherCmdDTO();
            BeanUtils.copyNotNullProperties(bisGatherCmd, dto);
            dto.setBackMsg(backMsg);
            // todo:数据转换
            com.bytter.biz.payment.api.dto.BisGatherCmdDTO bisGatherCmdDTO = BeanUtils.copyNewInstanceProperties(dto, com.bytter.biz.payment.api.dto.BisGatherCmdDTO.class);
            bisGatherCmdBackService.backBisGatherCmd(bisGatherCmdDTO, user);
//            bisMQService.sendBisGatherExcChangeMsg(bisGatherCmd, BdlinkConstant.BACK);
            //生成日志
            nisMessageService.saveMessage(bisGatherCmd.getId(), NisMessageConstant.MESSAGE_TYPE_EXC_BACK, backMsg, NisMessageConstant.BUS_TYPE_BDLINKGATHER, user.getLoginName(), user.getUsername());
            //完成消息通知
            notificationService.complete(NotificationConstant.NETBANK.BISGATHERCMD_SEND, bisGatherCmd.getId(), user.getId());
            notificationService.complete(NotificationConstant.NETBANK.BISGATHERCMD_FAIL, bisGatherCmd.getId(), user.getId());

        } else {
            throw new BusinessException(NetbankMessageEnum.NIS_PAY_MESSAGE_STATUS_CHANGE, bisGatherCmd.getVoucherNo());
        }
    }
    /**
     * 指令重发
     *
     * @param bisGatherCmd
     */
    public void resendBisGatherCmd(BisGatherCmd bisGatherCmd, ICurrentUser user) {
        //自动归集的指令，银行接口未写入加密信息，无法验证
        if(!BdlinkConstant.FROM_ALLOCATION_TYPE_AUTO_UP.equals(bisGatherCmd.getVoucherFrom())) {
            //数据防篡改校验
            if (!bisGatherCmd.getVerifyCode().equals(this.setVerifyCode(bisGatherCmd))) {
                throw new ParameterException(NetbankMessageEnum.BIS_EXC_MESSAGE_PAYINFO_CHANGED, bisGatherCmd.getVoucherNo());
            }
        }
        //指令重发，更新原指令状态为D-指令删除
        int result = this.bisGatherCmdDao.updateBisGatherCmdVoucherStat(bisGatherCmd, BdlinkConstant.DELETE);
        if(result>0) {
            //生成新的付款指令
            BisGatherCmd entity = new BisGatherCmd();
            BeanUtils.copyNotNullProperties(bisGatherCmd, entity);
            entity.setId(null);
            entity.setRunTime(DateUtils.getDate());
            entity.setVoucherNo(SequenceSingleton.getSequence(SequenceEnum.BIS_GATHER_CMD));
            entity.setCmdStat(BdlinkConstant.PRE_SEND);
            entity.setCreateBy(user.getLoginName());
            entity.setCreateDate(DateUtils.getDate());
            entity.setUpdateBy(user.getLoginName());
            entity.setUpdateDate(DateUtils.getDate());
            entity.setReturnTime(null);
            entity.setFirstReturnMsg(null);
            entity.setNextReturnMsg(null);
            entity.setNoticeSign(CommonConstant.SEND_SIGN_NO);
            //指令重发标志：1-重发指令
            entity.setResendSign(CommonConstant.SEND_SIGN_YES);
            this.saveOrUpdate(entity);
//            bisMQService.sendBisGatherExcChangeMsg(bisGatherCmd, BdlinkConstant.PRE_SEND);
            //生成日志
            nisMessageService.saveMessage(bisGatherCmd.getId(), NisMessageConstant.MESSAGE_TYPE_EXC_RESEND, "", NisMessageConstant.BUS_TYPE_BDLINKGATHER, user.getLoginName(), user.getUsername());
            //完成消息通知
            notificationService.complete(NotificationConstant.NETBANK.BISGATHERCMD_FAIL, bisGatherCmd.getId(), user.getId());
        } else {
            throw new BusinessException(NetbankMessageEnum.NIS_PAY_MESSAGE_STATUS_CHANGE, bisGatherCmd.getVoucherNo());
        }
    }
    /**
     * 指令作废
     *
     * @param bisGatherCmd
     */
    public void defeatBisGatherCmd(BisGatherCmd bisGatherCmd, ICurrentUser user) {
        //指令作废，更新原指令状态为7-指令无效
        int result = this.bisGatherCmdDao.updateBisGatherCmdVoucherStat(bisGatherCmd, BdlinkConstant.INVALIDATE);
        if(result>0) {
            //更新业务单
            BisGatherCmdDTO dto = new BisGatherCmdDTO();
            BeanUtils.copyNotNullProperties(bisGatherCmd, dto);
            // todo:数据转换
            com.bytter.biz.payment.api.dto.BisGatherCmdDTO bisGatherCmdDTO = BeanUtils.copyNewInstanceProperties(dto, com.bytter.biz.payment.api.dto.BisGatherCmdDTO.class);
            bisGatherCmdBackService.defeatBisGatherCmd(bisGatherCmdDTO, user);
//            bisMQService.sendBisGatherExcChangeMsg(bisGatherCmd, BdlinkConstant.INVALIDATE);
            //生成日志
            nisMessageService.saveMessage(bisGatherCmd.getId(), NisMessageConstant.MESSAGE_TYPE_EXC_REPEAL, "", NisMessageConstant.BUS_TYPE_BDLINKGATHER, user.getLoginName(), user.getUsername());
            //完成消息通知
            notificationService.complete(NotificationConstant.NETBANK.BISGATHERCMD_FAIL, bisGatherCmd.getId(), user.getId());
        } else {
            throw new BusinessException(NetbankMessageEnum.NIS_PAY_MESSAGE_STATUS_CHANGE, bisGatherCmd.getVoucherNo());
        }
    }
    /**
     * 指令交易成功/失败消息提醒
     */
    @Override
    @XxlJob("BisGatherCmdService")
    public void run(String arguments) {
        arguments =  XxlJobHelper.getJobParam();
        List<BisGatherCmdVO> list = null;
        if (NotificationConstant.NETBANK.BISGATHERCMD_FAIL.equals(arguments)) {
            //交易失败提醒
            list = this.bisGatherCmdDao.listByVoucherStat(false);
        } else if (NotificationConstant.NETBANK.BISGATHERCMD_SUCCESS.equals(arguments)) {
            //交易成功提醒
            list = this.bisGatherCmdDao.listByVoucherStat(true);
        }
        if (!EmptyUtils.isEmptyList(list)) {
            List<String> ids = new ArrayList<>();
            for (BisGatherCmdVO exc : list) {
                //消息提醒
                BtBankAccDTO bankAccDto = btBankAccService.getBankAccByIdAndCurId(exc.getChildAccId(), exc.getCurId());
                if (bankAccDto != null) {
                    ids.add(exc.getId());
                    notificationService.send(exc.getId(), bankAccDto.getCorpId(), arguments, exc);
                }
                //更新消息提醒标志
                this.bisGatherCmdDao.updateNoticeSign(ids);
            }
        }
    }
    /**
     * 生成加密信息
     * @param entity
     * @return
     */
    public String setVerifyCode(BisGatherCmd entity){
        //自动归集的指令，银行接口未写入加密信息，无法验证
        if(BdlinkConstant.FROM_ALLOCATION_TYPE_AUTO_UP.equals(entity.getVoucherFrom())){
            return entity.getVerifyCode();
        }
        //加密规则：子款账号+金额+父账户名+收款账号
        BtBankAccDTO childAcc = btBankAccService.getBankAccById(entity.getChildAccId());
        BtBankAccDTO parentAcc = btBankAccService.getBankAccById(entity.getParentAccId());
        if(childAcc == null){
            throw new ParameterException(NetbankMessageEnum.BIS_EXC_MESSAGE_PAYERACC_ISNOTEXIST, entity.getVoucherNo());
        }
        if(parentAcc == null){
            throw new ParameterException(NetbankMessageEnum.BIS_EXC_MESSAGE_PAYEEACC_ISNOTEXIST, entity.getVoucherNo());
        }
        //金额格式转换，不足两位小数，自动补零(制单跳过审批时金额未自动补零，导致生成指令和发送时加密串不一致)
        String gatherAmt = NumberUtil.formatDoubles(entity.getGatherAmt(), 2);
        String sourceData = childAcc.getBankAcc()+gatherAmt+parentAcc.getAccName()+parentAcc.getBankAcc();
        return BytterCypherUtil.encodeToString(sourceData);
    }
    @Override
    public List<CorpVerifyErrorDTO> verifyCorpDisable(String corpId) {
        if(StringUtils.isNotEmpty(corpId)){
            List<CorpVerifyErrorDTO> verifyErrorDTOS = new ArrayList<CorpVerifyErrorDTO>();
            //单位有在途的收款指令，不允许禁用！(自动：归集)
            int num = this.bisGatherCmdDao.getBisGatherCmdCountByCorpId(corpId, "auto");
            if(num>0){
                verifyErrorDTOS.add(new CorpVerifyErrorDTO(corpId,
                        SysConfigConstant.Module.NIS,
                        SysConfigConstant.BusType.NIS_GATHER_CMD,
                        multiLangServiceI.getLang(CorpVerifyMessageConstant.NIS_VALID_DISABLE_GATHERCMD_ERROR)));
            }
            //单位有在途的收款指令，不允许禁用！(手动：上划+上缴)
            num = this.bisGatherCmdDao.getBisGatherCmdCountByCorpId(corpId, "hand");
            if(num>0){
                verifyErrorDTOS.add(new CorpVerifyErrorDTO(corpId,
                        SysConfigConstant.Module.NIS,
                        SysConfigConstant.BusType.NIS_GATHER_CMD,
                        multiLangServiceI.getLang(CorpVerifyMessageConstant.NIS_VALID_DISABLE_GATHERCMD_ERROR)));
            }
            return verifyErrorDTOS;
        }
        return null;
    }
    @Override
    public List<CorpVerifyErrorDTO> verifyCorpParentChang(String corpId) {
        if(StringUtils.isNotEmpty(corpId)){
            List<CorpVerifyErrorDTO> verifyErrorDTOS = new ArrayList<CorpVerifyErrorDTO>();
            //单位有在途的资金调拨收款指令，不允许变更！(自动：归集)
            int num = this.bisGatherCmdDao.getBisGatherCmdCountByCorpId(corpId, "auto");
            if(num>0){
                verifyErrorDTOS.add(new CorpVerifyErrorDTO(corpId,
                        SysConfigConstant.Module.NIS,
                        SysConfigConstant.BusType.NIS_GATHER_CMD,
                        multiLangServiceI.getLang(CorpVerifyMessageConstant.NIS_VALID_CHANGE_GATHERCMD_ERROR)));
            }
            //单位有在途的资金调拨收款指令，不允许变更！(手动：上划+上缴)
            num = this.bisGatherCmdDao.getBisGatherCmdCountByCorpId(corpId, "hand");
            if(num>0){
                verifyErrorDTOS.add(new CorpVerifyErrorDTO(corpId,
                        SysConfigConstant.Module.NIS,
                        SysConfigConstant.BusType.NIS_GATHER_CMD,
                        multiLangServiceI.getLang(CorpVerifyMessageConstant.NIS_VALID_CHANGE_GATHERCMD_ERROR)));
            }
            return verifyErrorDTOS;
        }
        return null;
    }
    /**
     * 指令对应业务单号更新（指令生成时判断原指令状态）
     * @param bisGatherCmd
     * @return
     */
    public int updateBisGatherCmdCbsBillNo(BisGatherCmd bisGatherCmd){
        return bisGatherCmdDao.updateBisGatherCmdCbsBillNo(bisGatherCmd);
    }
    /**
     * 根据业务单号ID集合和指令来源，获取指令Map
     * @param billIds
     * @param voucherFrom
     * @return
     */
    public Map<String, BisGatherCmdDTO> getBisGatherCmdMapByCbsBillIds(List<String> billIds, String voucherFrom) {
        List<BisGatherCmdDTO> listBisGatherCmd = bisGatherCmdDao.getBisGatherCmdByCbsBillIds(billIds, voucherFrom);
        Map<String, BisGatherCmdDTO> BisGatherCmdMap = new HashMap<>();
        if (EmptyUtils.isNotEmpty(listBisGatherCmd)) {
            for (BisGatherCmdDTO dto:listBisGatherCmd) {
                BisGatherCmdMap.put(dto.getCbsBillId(), dto);
            }
        }
        return BisGatherCmdMap;
    }
    /**
     * 根据业务单号集合和指令来源，获取指令Map
     * @param billNos
     * @param voucherFrom
     * @return
     */
    public Map<String, BisGatherCmdDTO> getBisGatherCmdMapByCbsBillNos(List<String> billNos, String voucherFrom) {
        List<BisGatherCmdDTO> listBisGatherCmd = bisGatherCmdDao.getBisGatherCmdByCbsBillNos(billNos, voucherFrom);
        Map<String, BisGatherCmdDTO> bisGatherCmdMap = new HashMap<>();
        if (EmptyUtils.isNotEmpty(listBisGatherCmd)) {
            for (BisGatherCmdDTO dto:listBisGatherCmd) {
                bisGatherCmdMap.put(dto.getCbsBillNo(), dto);
            }
        }
        return bisGatherCmdMap;
    }

    public void setPayResultBisExc(BisGatherCmdVO bisGatherCmdVO, ICurrentUser user) {
        BisGatherCmd bisGatherCmd = this.get(bisGatherCmdVO.getId(), true);
        //失败状态才可以提交
        if (bisGatherCmd != null && !BdlinkConstant.WAIT_RESULT.equals(bisGatherCmd.getCmdStat())) {
            throw new ParameterException(com.bytter.biz.payment.api.enums.NetbankMessageEnum.NIS_PAY_MESSAGE_STATUS_CHANGE, bisGatherCmd.getVoucherNo());
        }
        //指令状态重设，更新原指令状态为0-交易成功/2-银行处理失败
        int result  =  this.bisGatherCmdDao.updateBisGatherCmdVoucherStat(bisGatherCmd,bisGatherCmdVO.getCmdStat());
        if (result > 0) {
            BisExcChangeMsg bisExcChangeMsg = new BisExcChangeMsg();
            bisExcChangeMsg.setCbsBillId(bisGatherCmd.getCbsBillId());
            bisExcChangeMsg.setVoucherStat(bisGatherCmdVO.getCmdStat());
            nisAllocationReqService.updateFromBisExcChange(bisExcChangeMsg);
            //生成业务日志
            Integer messageType = BdlinkConstant.SUCCESS.equals(bisGatherCmdVO.getCmdStat()) ? NisMessageConstant.MESSAGE_TYPE_PAY_SUCCESS : NisMessageConstant.MESSAGE_TYPE_PAY_FAILURE;
            NisAllocationReq entity = null;
            if(StringUtils.isNotBlank(bisGatherCmdVO.getCbsBillId())){
                entity = this.nisAllocationReqService.get(bisGatherCmdVO.getCbsBillId());
            }
            if (!EmptyUtils.isEmpty(entity)) {
                //生成日志
                nisMessageService.saveMessage(entity.getId(), messageType, bisGatherCmdVO.getHandleOpinion(), NisMessageConstant.BUS_TYPE_ALLOCATION, user.getLoginName(), user.getUsername());
            }
            //生成指令日志
            nisMessageService.saveMessage(bisGatherCmd.getId(), messageType, bisGatherCmdVO.getHandleOpinion(), NisMessageConstant.BUS_TYPE_BDLINKGATHER, user.getLoginName(), user.getUsername());
        } else {
            throw new BusinessException(com.bytter.biz.payment.api.enums.NetbankMessageEnum.NIS_PAY_MESSAGE_STATUS_CHANGE, bisGatherCmdVO.getVoucherNo());
        }
    }
}
