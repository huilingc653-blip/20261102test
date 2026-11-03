package com.bytter.biz.bdlinks.service;

import com.alibaba.excel.util.CollectionUtils;
import com.bytter.biz.bdlink.api.constant.BdlinkConstant;
import com.bytter.biz.bdlinks.dao.BisExcDao;
import com.bytter.biz.bdlinks.entity.BisExc;
import com.bytter.biz.bdlinks.vo.BisExcVO;
import com.bytter.biz.payment.api.constant.*;
import com.bytter.biz.payment.api.dto.*;
import com.bytter.biz.payment.api.enums.NetbankMessageEnum;
import com.bytter.biz.payment.api.service.*;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.workflow.bpm.IWorkFlowListener;
import com.bytter.workflow.bpm.IWorkFlowRevokeListener;
import com.bytter.workflow.bpm.annotation.WorkFlow;
import com.bytter.workflow.dto.IWorkFlowResult;
import com.bytter.workflow.dto.WorkFlowBusiness;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.exception.BusinessException;
import com.bytter.core.common.exception.ParameterException;
import com.bytter.core.constant.CommonConstant;
import com.bytter.core.sequence.SequenceSingleton;
import com.bytter.core.sequence.enums.SequenceEnum;
import com.bytter.core.utils.*;
import com.bytter.system.api.*;
import com.bytter.system.api.constant.AccountGrantConstant;
import com.bytter.system.api.constant.CorpVerifyMessageConstant;
import com.bytter.system.api.constant.NotificationConstant;
import com.bytter.system.api.constant.SysConfigConstant;
import com.bytter.system.api.dto.BtBankAccDTO;
import com.bytter.system.api.dto.*;
import com.bytter.workflow.api.IWorkFlowService;
import com.xxl.job.core.context.XxlJobHelper;
import com.xxl.job.core.handler.annotation.XxlJob;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.dubbo.config.annotation.DubboService;
import org.assertj.core.util.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;
/**
 * @author online Generator
 * @version 1.0
 * @Title: Service
 * @Description: BIS_EXC
 * @date 2017-06-28 16:31:56
 */
@Service("bisExcService")
@Transactional
@WorkFlow(NisWorkFlowConstant.NIS_BDLINK_PAY)
@DubboService(interfaceClass = IWorkFlowListener.class, group = NisWorkFlowConstant.NIS_BDLINK_PAY)
public class BisExcService extends BaseService<BisExc, String> implements IWorkFlowListener, ITaskService, IWorkFlowRevokeListener, ICorpDisableVerifyService, ICorpParentChangVerifyService {
    @Autowired
    private BisExcDao bisExcDao;
    @DubboReference(check = false)
    private IAccountGrantService accountGrantService;
    @DubboReference(check = false)
    private IBtBankAccService btBankAccService;
    @DubboReference(check = false)
    private ICorpService corpService;
    @DubboReference(check = false)
    private INisMessageService nisMessageService;
    @Autowired
    private IWorkFlowService workFlowService;
    @DubboReference(check = false)
    private IFcVoucherTypeService fcVoucherTypeService;
    @DubboReference(check = false)
    private INotificationService notificationService;
    @DubboReference(check = false)
    private IBisFailReasonService bisFailReasonService;
    @DubboReference(check = false)
    private IConfigService configService;
    @Resource
    private IMultiLangService multiLangServiceI;
    @DubboReference(check = false)
    private INisExpenseDetailService nisExpenseDetailService;
    @DubboReference(check = false)
    private IBisBifRuleService bisBifRuleService;
    @DubboReference(check = false)
    private INisWageService nisWageService;
    @DubboReference(check = false)
    private INisBillheadService iNisBillheadService;
    @DubboReference(check = false)
    private IDictService dictService;
    @DubboReference(check = false)
    private IBisBifInitService bisBifInitService;
    @DubboReference(check = false)
    private IBtCurrencyService btCurrencyService;
    @DubboReference(check = false)
    private IRegionService regionService;
    @DubboReference(check = false)
    private IBisExcBackService bisExcBackService;


    @Autowired
    private BisExcStatusChangeHandler bisExcStatusChangeHandler;

    @DubboReference(check = false)
    private ICorpService iCorpService;

    @DubboReference(check = false)
    private IBtCurrencyService iBtCurrencyService;

    @DubboReference(check = false)
    private IRegionService iRegionService;
    @DubboReference(check = false)
    private IBisBifInitService iBisBifInitService;

    /**
     * 删除对象
     *
     * @param entity
     */
    @Override
    public void deleteEntity(BisExc entity) {
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
    public void save(BisExc entity) {
        super.saveEntity(entity);
    }
    /**
     * 更新
     *
     * @param entity
     */
    public void update(BisExc entity) {
        super.updateEntity(entity);
    }
    /**
     * 保存or更新
     *
     * @param entity
     */
    @Override
    public void saveOrUpdate(BisExc entity) {
        super.saveOrUpdate(entity);
    }
    @Override
    public BaseDao<BisExc, String> getEntityDao() {
        return bisExcDao;
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
            //过滤掉代发工资的指令
            sql.append(" and b.voucher_From not in ('" + BdlinkConstant.FROM_NIS_WAGE + "')");
            resource = ResourceConstant.NETBANK_BDLINK_BISEXC_SENDQUERY;
        } else if ("listTrace".equals(opType)) {
            //指令追踪 2-银行处理失败  9-接口处理失败 1-等待结果 W-指令已消费 Y-异常交易 Z-数据被篡改
            sql.append(" and b.voucher_Stat in ('" + BdlinkConstant.BANK_FAILED + "','" + BdlinkConstant.INTERFACE_FAILED + "','" + BdlinkConstant.WAIT_RESULT + "','" + BdlinkConstant.CONSUMPTION
                    + "','" + BdlinkConstant.EXCEPTION + "','" + BdlinkConstant.DATA_TAMPER + "')");
            //开启代发工资指令支持重发、等待结果状态修改
            //sql.append(" and b.voucher_From not in ('" + BdlinkConstant.FROM_NIS_WAGE + "')");
            resource = ResourceConstant.NETBANK_BDLINK_BISEXC_TRACEQUERY;
        } else if ("listConfirm".equals(opType)) {
            //可疑指令发送确认 T-可疑状态
            sql.append(" and b.voucher_Stat in ('" + BdlinkConstant.SUSPECTED + "')");
            //过滤掉代发工资的指令
            sql.append(" and b.voucher_From not in ('" + BdlinkConstant.FROM_NIS_WAGE + "')");
            resource = ResourceConstant.NETBANK_BDLINK_BISEXC_CONFIRMQUERY;
        } else if ("listSalePayment".equals(opType)) {
            //直营付款
            sql.append(" and b.status in ('" + BdlinkConstant.STATUE_TEMP + "','" + BdlinkConstant.STATUE_BACK + "')  and b.voucher_From = '" + BdlinkConstant.FROM_BIS_EXC_PAY + "'");
            resource = ResourceConstant.NETBANK_BDLINK_BISEXC_SALEPAYMENTQUERY;
        } else if ("listTally".equals(opType)) {
            //记账 获取交易成功的自营付款和自动下拨的指令
            sql.append(" and ((b.voucher_From = '" + BdlinkConstant.FROM_BIS_EXC_PAY + "' and b.bus_ori_type= '" + BdlinkConstant.BUSORITYPE_48 + "') ");
            sql.append(" or (b.voucher_From = '" + BdlinkConstant.FROM_ALLOCATION_TYPE_AUTO_DOWN + "' and b.bus_ori_type= '" + BdlinkConstant.BUSORITYPE_33 + "')) ");
            sql.append("  and b.voucher_Stat ='" + BdlinkConstant.SUCCESS + "' ");
            sql.append(" and (b.tally_Sign ='' or b.tally_Sign ='0' or b.tally_Sign ='6' or  b.tally_sign is null)");
            resource = ResourceConstant.NETBANK_BDLINK_BISEXC_SALEPAYMENTQUERY;
        } else if ("listTallyChange".equals(opType)) {
            //改账 获取交易成功的自营付款和自动下拨的指令
            sql.append(" and ((b.voucher_From = '" + BdlinkConstant.FROM_BIS_EXC_PAY + "' and b.bus_ori_type= '" + BdlinkConstant.BUSORITYPE_48 + "') ");
            sql.append(" or (b.voucher_From = '" + BdlinkConstant.FROM_ALLOCATION_TYPE_AUTO_DOWN + "' and b.bus_ori_type= '" + BdlinkConstant.BUSORITYPE_33 + "')) ");
            sql.append("  and b.voucher_Stat ='" + BdlinkConstant.SUCCESS + "' ");
            sql.append(" and (b.tally_Sign in(5,4,3))");
            sql.append(" and b.tally_code = '").append(user.getLoginName()).append("'");
            resource = ResourceConstant.NETBANK_BDLINK_BISEXC_SALEPAYMENTQUERY;
        } else if ("listTallyCheck".equals(opType)) {
            //记账复核 获取交易成功的自营付款和自动下拨的指令
            sql.append(" and ((b.voucher_From = '" + BdlinkConstant.FROM_BIS_EXC_PAY + "' and b.bus_ori_type= '" + BdlinkConstant.BUSORITYPE_48 + "') ");
            sql.append(" or (b.voucher_From = '" + BdlinkConstant.FROM_ALLOCATION_TYPE_AUTO_DOWN + "' and b.bus_ori_type= '" + BdlinkConstant.BUSORITYPE_33 + "')) ");
            sql.append(" and b.voucher_Stat ='" + BdlinkConstant.SUCCESS + "'");
            sql.append(" and (b.tally_Sign='3')");
            sql.append(" and b.tally_code <> '").append(user.getLoginName()).append("'");
            resource = ResourceConstant.NETBANK_BDLINK_BISEXC_SALEPAYMENTQUERY;
        } else {
            resource = ResourceConstant.NETBANK_BDLINK_BISEXC_QUERY;
        }
        if (!StringUtils.isBlank(resource)) {
            String corpAuthSql = "";
            if ("listTally".equals(opType) || "listTallyChange".equals(opType) || "listTallyCheck".equals(opType)) {
                List<String> corpIds = corpService.getUserCorpIds(resource, user.getId());
                List<CorpDTO> netIdList = corpService.listCorpDTOByNetIds(corpIds);
                if (CollectionUtils.isEmpty(netIdList)) {
                    return new DataTablesOutput();
                }
                corpAuthSql = SqlUtil.getInSql(netIdList.stream().map(CorpDTO::getId).collect(Collectors.toList()), "b.corp_id", true);
            } else {
                corpAuthSql = SqlUtil.getInSql(corpService.getUserCorpIds(resource, user.getId()), "b.corp_Id", true);
            }
            //请款指令发送权限：1-付款方/0-收款方
            String askExcSendGrant = configService.getConfig(AllocationConstant.ALLOT_CONFIG_TYPE, AllocationConstant.ALLOT_CONFIG_ASKEXCSENDGRANT).getValue();
            if (CommonConstant.YES.equals(askExcSendGrant)) {
                String askCorpAuthSql = SqlUtil.getInSql(corpService.getUserCorpIds(resource, user.getId()), "b.corp_Id", true);
                sql.append(" and ((b.voucher_from <> '" + BdlinkConstant.FROM_ALLOCATION_TYPE_ASK + "' and ").append(corpAuthSql);
                sql.append(") or (b.voucher_from = '" + BdlinkConstant.FROM_ALLOCATION_TYPE_ASK + "' and ").append(askCorpAuthSql).append("))");
            } else {
                sql.append(" and ").append(corpAuthSql);
            }
        }
        DataTablesOutput<BisExcVO> outputdata = bisExcDao.getDataTablesUnion(dataTablesInput, sql.toString());
        return this.setJoinField(outputdata, opType);
    }
    /**
     * 设置付款办理的关联字段
     *
     * @param output
     * @param opType
     * @return
     */
    public DataTablesOutput setJoinField(DataTablesOutput output, String opType) {
        List<BisExcVO> listBisExc = output.getData();
        if (EmptyUtils.isNotEmpty(listBisExc)) {
            Map<String, CorpDTO> corpMap = corpService.getCorpMap();
            Map<String, BtCurrencyDTO> curMap = btCurrencyService.getCurrency();
            Map<String, FcVoucherTypeDTO> voucherTypeMap = fcVoucherTypeService.getFcVoucherTypeMap();
            Map<String, BisBifInitDTO> bisBifInitMap = bisBifInitService.getBisBifInitMap();
            //是否匹配交易失败原因
            String isMathcFailReason = configService.getConfig(NetbankConstant.NIS_CONFIG_TYPE, NetbankConstant.NIS_CONFIG_ISMATHCFAILREASON).getValue();
            Map<String, Map<String, DictDTO>> listDictMap = dictService.listDictMap(BdlinkConstant.EXC_VOUCHERSTAT_DICT, BdlinkConstant.EXC_BUSORITYPE_DICT, BdlinkConstant.EXC_CMDTYPE_DICT, BdlinkConstant.EXC_VOUCHERFROM_DICT);
            for (BisExcVO bisExc : listBisExc) {
                if (corpMap.containsKey(bisExc.getCorpId())) {
                    bisExc.setCorpName(corpMap.get(bisExc.getCorpId()).getName());
                }
                if (curMap.containsKey(bisExc.getCurId())) {
                    bisExc.setCurName(curMap.get(bisExc.getCurId()).getCurName());
                }
                if (voucherTypeMap.containsKey(bisExc.getVoucherTypeId())) {
                    bisExc.setVoucherTypeName(voucherTypeMap.get(bisExc.getVoucherTypeId()).getVoucherName());
                }
                if (bisBifInitMap.containsKey(bisExc.getBifCode())) {
                    bisExc.setBifName(bisBifInitMap.get(bisExc.getBifCode()).getName());
                }
                if ("listTrace".equals(opType) || "list".equals(opType)) {
                    if ("listTrace".equals(opType)) {
                        //等待结果的数据禁用复选框
                        if (BdlinkConstant.WAIT_RESULT.equals(bisExc.getVoucherStat()) ||
                                BdlinkConstant.CONSUMPTION.equals(bisExc.getVoucherStat())) {
                            bisExc.setCheckboxHide(CommonConstant.YES);
                        } else {
                            bisExc.setCheckboxHide(CommonConstant.NO);
                        }
                        if (CommonConstant.YES.equals(isMathcFailReason)) {
                            if (StringUtils.isNotEmpty(bisExc.getNextReturnMsg()) && !BdlinkConstant.SUCCESS.equals(bisExc.getVoucherStat())) {
                                //交易失败原因匹配
                                BisFailReasonDTO bisFailReasonDTO = bisFailReasonService.getFailReasonByReturnMsg(bisExc.getBifCode(), bisExc.getNextReturnMsg());
                                if (bisFailReasonDTO != null) {
                                    bisExc.setFailReason(bisFailReasonDTO.getFailName());
                                    //保存失败原因
                                    BisExc entity = this.get(bisExc.getId(), true);
                                    entity.setFailReason(bisFailReasonDTO.getFailName());
                                    this.saveOrUpdate(entity);
                                }
                            }
                        }
                    }
                    bisExc.setIsSpecFailForTranPay(CommonConstant.NO);
                    //转付
                    if (NetbankConstant.PAY_TYPE_02.equals(bisExc.getSysVoucherCode()) && (BdlinkConstant.INTERFACE_FAILED.equals(bisExc.getVoucherStat()) || BdlinkConstant.BANK_FAILED.equals(bisExc.getVoucherStat()))) {
                        //指令表bis_exc表的voucher_no 和 转付下拨指令表 bis_exc_proc的voucher_no 关系：第三位固定替换为3
                        String voucherVoProc = bisExc.getVoucherNo().substring(0, 2) + "3" + bisExc.getVoucherNo().substring(3);
                        if (this.isSpecFailForTranPay(voucherVoProc, bisExc.getCbsBillId())) {
                            bisExc.setIsSpecFailForTranPay(CommonConstant.YES);
                        }
                    }
                }
                //字典注入
                this.injectValues(bisExc, listDictMap);
            }
        }
        return output;
    }
    /**
     * 指令列表查询
     *
     * @param dataTablesInput
     * @return
     */
    @SuppressWarnings("rawtypes")
    public DataTablesOutput getDataTablesUnion(DataTablesInput dataTablesInput, String joinSql) {
        DataTablesOutput dataTablesOutput = bisExcDao.getDataTablesUnion(dataTablesInput, joinSql);
        String opType = (String) dataTablesInput.getFormdata().get("opType");
        List<BisExcVO> listBisExc = dataTablesOutput.getData();
        if (EmptyUtils.isNotEmpty(listBisExc)) {
            //是否匹配交易失败原因
            String isMathcFailReason = configService.getConfig(NetbankConstant.NIS_CONFIG_TYPE, NetbankConstant.NIS_CONFIG_ISMATHCFAILREASON).getValue();
            Map<String, Map<String, DictDTO>> listDictMap = dictService.listDictMap(BdlinkConstant.EXC_VOUCHERSTAT_DICT, BdlinkConstant.EXC_BUSORITYPE_DICT, BdlinkConstant.EXC_CMDTYPE_DICT, BdlinkConstant.EXC_VOUCHERFROM_DICT);
            for (BisExcVO bisExc : listBisExc) {
                if ("listTrace".equals(opType)) {
                    //等待结果的数据禁用复选框
                    if (BdlinkConstant.WAIT_RESULT.equals(bisExc.getVoucherStat())) {
                        bisExc.setCheckboxHide(CommonConstant.YES);
                    } else {
                        bisExc.setCheckboxHide(CommonConstant.NO);
                    }
                    if (CommonConstant.YES.equals(isMathcFailReason)) {
                        if (StringUtils.isNotEmpty(bisExc.getNextReturnMsg()) && !BdlinkConstant.SUCCESS.equals(bisExc.getVoucherStat())) {
                            //交易失败原因匹配
                            BisFailReasonDTO bisFailReasonDTO = bisFailReasonService.getFailReasonByReturnMsg(bisExc.getBifCode(), bisExc.getNextReturnMsg());
                            if (bisFailReasonDTO != null) {
                                bisExc.setFailReason(bisFailReasonDTO.getFailName());
                                //保存失败原因
                                BisExc entity = this.get(bisExc.getId(), true);
                                entity.setFailReason(bisFailReasonDTO.getFailName());
                                this.saveOrUpdate(entity);
                            }
                        }
                    }
                }
                bisExc.setIsSpecFailForTranPay(CommonConstant.NO);
                //转付
                if (NetbankConstant.PAY_TYPE_02.equals(bisExc.getSysVoucherCode()) && (BdlinkConstant.INTERFACE_FAILED.equals(bisExc.getVoucherStat()) || BdlinkConstant.BANK_FAILED.equals(bisExc.getVoucherStat()))) {
                    //指令表bis_exc表的voucher_no 和 转付下拨指令表 bis_exc_proc的voucher_no 关系：第三位固定替换为3
                    String voucherVoProc = bisExc.getVoucherNo().substring(0, 2) + "3" + bisExc.getVoucherNo().substring(3);
                    if (this.isSpecFailForTranPay(voucherVoProc, bisExc.getCbsBillId())) {
                        bisExc.setIsSpecFailForTranPay(CommonConstant.YES);
                    }
                }
                this.injectValues(bisExc, listDictMap);
            }
            dataTablesOutput.setData(listBisExc);
        }
        return dataTablesOutput;
    }
    /**
     * 根据ID获取指令
     *
     * @param id
     * @return
     */
    public BisExcVO getByIdUnion(String id) {
        BisExcVO bisExcVO = bisExcDao.getByIdUnion(id);
        if (bisExcVO != null) {
            // 单位
            CorpDTO corpDTO = corpService.get(bisExcVO.getCorpId());
            bisExcVO.setCorpName(corpDTO == null ? "" : corpDTO.getName());
            // 币种
            BtCurrencyDTO currencyDTO = btCurrencyService.getObjectById(bisExcVO.getCurId());
            bisExcVO.setCurName(currencyDTO == null ? "" : currencyDTO.getCurName());
            // 付款方式
            FcVoucherTypeDTO fcVoucherTypeDTO = fcVoucherTypeService.getFcVoucherTypeById(bisExcVO.getVoucherTypeId());
            bisExcVO.setVoucherTypeName(fcVoucherTypeDTO == null ? "" : fcVoucherTypeDTO.getVoucherName());
            bisExcVO.setSysVoucherCode(fcVoucherTypeDTO == null ? "" : fcVoucherTypeDTO.getSysVoucherCode());
            // 银企接口
            BisBifInitDTO bisBifInitDTO = bisBifInitService.getObjectById(bisExcVO.getBifCode());
            bisExcVO.setBifName(bisBifInitDTO == null ? "" : bisBifInitDTO.getName());
            // 付款方省市
            if (!StringUtil.isBlank(bisExcVO.getPayerProv())) {
                RegionDTO prov = regionService.getById(bisExcVO.getPayerProv());
                bisExcVO.setPayerProvName(prov == null ? "" : prov.getName());
            }
            if (!StringUtil.isBlank(bisExcVO.getPayerCity())) {
                RegionDTO city = regionService.getById(bisExcVO.getPayerCity());
                bisExcVO.setPayerCityName(city == null ? "" : city.getName());
            }
            // 收款方省市
            if (!StringUtil.isBlank(bisExcVO.getPayeeProv())) {
                RegionDTO prov = regionService.getById(bisExcVO.getPayeeProv());
                bisExcVO.setPayeeProvName(prov == null ? "" : prov.getName());
            }
            if (!StringUtil.isBlank(bisExcVO.getPayeeCity())) {
                RegionDTO city = regionService.getById(bisExcVO.getPayeeCity());
                bisExcVO.setPayeeCityName(city == null ? "" : city.getName());
            }
        }
        return bisExcVO;
    }
    /**
     * 根据ID获取指令
     *
     * @param id
     * @return
     */
    public BisExcVO getExcProcByIdUnion(String id) {
        BisExcVO bisExcVO = bisExcDao.getExcProcByIdUnion(id);
        if (bisExcVO != null) {
            NisBillheadDTO NisBillheadDTO = iNisBillheadService.getNisBillheadById(bisExcVO.getCbsBillId());
            if (NisBillheadDTO != null && NisBillheadDTO.getStatus() >= 95) {
                return bisExcVO;
            }
        }
        return new BisExcVO();
    }
    /**
     * 根据业务单号查询付款指令
     *
     * @param cbsBillId
     * @return
     */
    public BisExc getByCbsBillId(String cbsBillId) {
        return bisExcDao.getUniqueBy("cbsBillId", cbsBillId);
    }
    /**
     * 根据业务单号查询付款指令
     *
     * @param cbsBillId
     * @return
     */
    public BisExcVO getByCbsBillIdUnion(String cbsBillId) {
        return bisExcDao.getByCbsBillIdUnion(cbsBillId);
    }
    /**
     * 根据指令单号查询付款指令
     *
     * @param voucherNo
     * @return
     */
    public BisExcVO getByVoucherNo(String voucherNo) {
        BisExcVO bisExcVO = bisExcDao.getByVoucherNo(voucherNo);
        if(bisExcVO == null){
            return null;
        }
        //填充数据
        String corpName = iCorpService.getCorpName(bisExcVO.getCorpId());
        bisExcVO.setCorpName(corpName);
        BtCurrencyDTO btCurrencyDTO = iBtCurrencyService.getObjectById(bisExcVO.getCurId());
        bisExcVO.setCurName(btCurrencyDTO.getCurName());
        String payerCity = bisExcVO.getPayerCity();
        String payeeProv = bisExcVO.getPayeeProv();
        Map<String, RegionDTO> map = iRegionService.listByIds(Lists.newArrayList(payerCity,payeeProv));
        bisExcVO.setPayerCityName(map.get(payerCity).getName());
        bisExcVO.setPayeeProvName(map.get(payeeProv).getProvName());
        //这里的 bisExcVO.getBifCode() 实则存的是 id
        BisBifInitDTO bifInitDTO = iBisBifInitService.getObjectById(bisExcVO.getBifCode());
        bisExcVO.setBifName(bifInitDTO.getName());
        return bisExcVO;
    }
    /**
     * 根据业务单号获取指令信息
     *
     * @param cbsBillNo
     * @param voucherFrom
     * @return
     */
    public BisExcVO getByCbsBillNo(String cbsBillNo, String voucherFrom) {
        return bisExcDao.getByCbsBillNo(cbsBillNo, voucherFrom);
    }
    /**
     * 指令发送
     *
     * @param bisExc
     */
    public BisExc sendBisExc(BisExc bisExc, ICurrentUser user) {
        sendBisExcNoAccounting(bisExc, user); //疑惑？？？
        // 记账
        String serviceName = BdlinkConstant.getServiceNameByVoucherFrom(bisExc.getVoucherFrom());
        if (StringUtils.isNotEmpty(serviceName)) {
            IBisExcBackService bisExcBackService = (IBisExcBackService) this.getBean(serviceName);
            com.bytter.biz.payment.api.dto.BisExcDTO dto = new com.bytter.biz.payment.api.dto.BisExcDTO();
            BeanUtils.copyNotNullProperties(bisExc, dto);
            bisExcBackService.sendBisExcAccounting(dto, user);
        }
        return bisExc;
    }


    /**
     * 指令发送 无记账的业务逻辑 适用于自动指令 记账放在自己的业务里面
     * todo 优化指令发送？
     *
     */
    public BisExc sendBisExcNoAccounting(BisExc bisExc, ICurrentUser user) {
        //数据防篡改校验
        if (!bisExc.getVerifyCode().equals(this.setVerifyCode(bisExc))) {
            throw new ParameterException(NetbankMessageEnum.BIS_EXC_MESSAGE_PAYINFO_CHANGED, bisExc.getVoucherNo());
        }
        int result = 0;
        if (StringUtils.isBlank(bisExc.getId())) {
            //指令自动发送，生成状态为3-准备发送
            bisExc.setVoucherStat(BdlinkConstant.PRE_SEND);
            bisExc.setUpdateBy(user.getLoginName());
            bisExc.setUpdateDate(DateUtils.getDate());
            this.saveEntity(bisExc); //保存到指令表  -- 资金池 不记账
            result = 1;
        } else {
            //指令手工发送，更新原指令状态为3-准备发送
            bisExc.setReqDate(DateUtils.getDate());
            bisExc.setVerifyCode(this.setVerifyCode(bisExc));
            result = this.bisExcDao.updateBisExcVoucherStat(bisExc, BdlinkConstant.PRE_SEND);
        }
        if (result > 0) {
            //对私付款校验明细总金额与指令付款金额是否一致
            if (BdlinkConstant.FROM_NIS_PAY.equals(bisExc.getVoucherFrom())) {
                if (BdlinkConstant.TRANS_MODE_PRIVATE.equals(bisExc.getTransMode())) {
                    BigDecimal sumExpenseMoney = nisExpenseDetailService.getSumExpenseMoneyByBillId(bisExc.getCbsBillId());
                    if (sumExpenseMoney.compareTo(BigDecimal.ZERO) > 0 && sumExpenseMoney.compareTo(bisExc.getPayMoney()) != 0) {
                        throw new BusinessException(NetbankMessageEnum.NIS_PAY_MESSAGE_PAYMONEY_MISMATCH, bisExc.getCbsBillNo());
                    }
                }
            }
//            bisMQService.sendBisExcChangeMsg(bisExc, BdlinkConstant.PRE_SEND);
            bisExcStatusChangeHandler.processBisExcChangeMsg(bisExc.getVoucherFrom(),bisExc.getCbsBillId(),BdlinkConstant.PRE_SEND);
            //生成日志
            nisMessageService.saveMessage(bisExc.getId(), NisMessageConstant.MESSAGE_TYPE_EXC_SEND, "", NisMessageConstant.BUS_TYPE_BDLINKPAY, user.getLoginName(), user.getUsername());
            //完成消息通知
            notificationService.complete(NotificationConstant.NETBANK.BISEXC_SEND, bisExc.getId(), user.getId());
            notificationService.complete(NotificationConstant.NETBANK.BISEXC_OVERTIME, bisExc.getId(), user.getId());
            notificationService.complete(NotificationConstant.NETBANK.BISEXC_DOUBTFUL_OVERTIME, bisExc.getId(), user.getId());
        } else {
            throw new BusinessException(NetbankMessageEnum.NIS_PAY_MESSAGE_STATUS_CHANGE, bisExc.getVoucherNo());
        }
        return bisExc;
    }

    /**
     * 指令退回
     *
     * @param bisExc
     */
    public void backBisExc(BisExc bisExc, String backMsg, ICurrentUser user) {
        //指令退回，更新原指令状态为B-指令退回
        int result = this.bisExcDao.updateBisExcVoucherStat(bisExc, BdlinkConstant.BACK);
        if (result > 0) {
            //更新业务单
            String serviceName = BdlinkConstant.getServiceNameByVoucherFrom(bisExc.getVoucherFrom());
            if (StringUtils.isNotEmpty(serviceName)) {
                IBisExcBackService bisExcBackService = (IBisExcBackService) this.getBean(serviceName);
                com.bytter.biz.payment.api.dto.BisExcDTO dto = new com.bytter.biz.payment.api.dto.BisExcDTO();
                BeanUtils.copyNotNullProperties(bisExc, dto);
                dto.setBackMsg(backMsg);
                bisExcBackService.backBisExc(dto, user);
            }
//            bisMQService.sendBisExcChangeMsg(bisExc, BdlinkConstant.BACK);
            bisExcStatusChangeHandler.processBisExcChangeMsg(bisExc.getVoucherFrom(),bisExc.getCbsBillId(),BdlinkConstant.BACK);
            //生成日志
            nisMessageService.saveMessage(bisExc.getId(), NisMessageConstant.MESSAGE_TYPE_EXC_BACK, backMsg, NisMessageConstant.BUS_TYPE_BDLINKPAY, user.getLoginName(), user.getUsername());
            //完成消息通知
            notificationService.complete(NotificationConstant.NETBANK.BISEXC_SEND, bisExc.getId(), user.getId());
            notificationService.complete(NotificationConstant.NETBANK.BISEXC_FAIL, bisExc.getId(), user.getId());
            notificationService.complete(NotificationConstant.NETBANK.BISEXC_OVERTIME, bisExc.getId(), user.getId());
            notificationService.complete(NotificationConstant.NETBANK.BISEXC_DOUBTFUL_OVERTIME, bisExc.getId(), user.getId());
        } else {
            throw new BusinessException(NetbankMessageEnum.NIS_PAY_MESSAGE_STATUS_CHANGE, bisExc.getVoucherNo());
        }
    }
    /**
     * 指令退回（指定环节）
     *
     * @param bisExc
     */
    public void backBisExc(BisExc bisExc, String backMsg, String backNode, ICurrentUser user) {
        //指令退回，更新原指令状态为B-指令退回
        int result = this.bisExcDao.updateBisExcVoucherStat(bisExc, BdlinkConstant.BACK);
        if (result > 0) {
            //更新业务单
            String serviceName = BdlinkConstant.getServiceNameByVoucherFrom(bisExc.getVoucherFrom());
            if (StringUtils.isNotEmpty(serviceName)) {
                IBisExcBackService bisExcBackService = (IBisExcBackService) this.getBean(serviceName);
                com.bytter.biz.payment.api.dto.BisExcDTO dto = new com.bytter.biz.payment.api.dto.BisExcDTO();
                BeanUtils.copyNotNullProperties(bisExc, dto);
                dto.setBackMsg(backMsg);
                dto.setBackNode(backNode);
                bisExcBackService.backBisExc(dto, user);
            }
//            bisMQService.sendBisExcChangeMsg(bisExc, BdlinkConstant.BACK);
            bisExcStatusChangeHandler.processBisExcChangeMsg(bisExc.getVoucherFrom(),bisExc.getCbsBillId(),BdlinkConstant.BACK);
            //生成日志
            nisMessageService.saveMessage(bisExc.getId(), NisMessageConstant.MESSAGE_TYPE_EXC_BACK, backMsg, NisMessageConstant.BUS_TYPE_BDLINKPAY, user.getLoginName(), user.getUsername());
            //完成消息通知
            notificationService.complete(NotificationConstant.NETBANK.BISEXC_SEND, bisExc.getId(), user.getId());
            notificationService.complete(NotificationConstant.NETBANK.BISEXC_FAIL, bisExc.getId(), user.getId());
            notificationService.complete(NotificationConstant.NETBANK.BISEXC_OVERTIME, bisExc.getId(), user.getId());
            notificationService.complete(NotificationConstant.NETBANK.BISEXC_DOUBTFUL_OVERTIME, bisExc.getId(), user.getId());
        } else {
            throw new BusinessException(NetbankMessageEnum.NIS_PAY_MESSAGE_STATUS_CHANGE, bisExc.getVoucherNo());
        }
    }
    /**
     * 指令重发
     *
     * @param bisExc
     */
    public void resendBisExc(BisExc bisExc, ICurrentUser user) {
        //自动下拨的指令，银行接口未写入加密信息，无法验证
        if (!BdlinkConstant.FROM_ALLOCATION_TYPE_AUTO_DOWN.equals(bisExc.getVoucherFrom()) && !BdlinkConstant.FROM_ALLOCATION_TYPE_AUTO_UP.equals(bisExc.getVoucherFrom())) {
            //数据防篡改校验
            if (!bisExc.getVerifyCode().equals(this.setVerifyCode(bisExc))) {
                throw new BusinessException(NetbankMessageEnum.BIS_EXC_MESSAGE_PAYINFO_CHANGED, bisExc.getVoucherNo());
            }
        }
        //对私付款校验明细总金额与指令付款金额是否一致
        if (BdlinkConstant.FROM_NIS_PAY.equals(bisExc.getVoucherFrom())) {
            if (BdlinkConstant.TRANS_MODE_PRIVATE.equals(bisExc.getTransMode())) {
                //首先校验指令金额与明细金额总金额是否一致
                BigDecimal sumExpenseMoney = nisExpenseDetailService.getSumExpenseMoneyByBillId(bisExc.getCbsBillId());
                if (sumExpenseMoney.compareTo(BigDecimal.ZERO) > 0 && sumExpenseMoney.compareTo(bisExc.getPayMoney()) != 0) {
                    throw new BusinessException(NetbankMessageEnum.NIS_PAY_MESSAGE_PAYMONEY_MISMATCH, bisExc.getVoucherNo());
                }
                //再校验指令金额是否与失败金额一致，部分成功部分失败情况不允许重发
                sumExpenseMoney = nisExpenseDetailService.getSumFailExpenseMoneyByBillId(bisExc.getCbsBillId());
                if (sumExpenseMoney.compareTo(BigDecimal.ZERO) > 0 && sumExpenseMoney.compareTo(bisExc.getPayMoney()) != 0) {
                    throw new BusinessException(NetbankMessageEnum.NIS_PAY_MESSAGE_STATUS_FAIL_DEAL, bisExc.getCbsBillNo());
                }
            }
        }
        if (BdlinkConstant.FROM_NIS_WAGE.equals(bisExc.getVoucherFrom())) {
            //获取代发工资明细，存在部分明细失败，则不允许重发
            List<NisWageDetailDTO> listWageDtl = nisWageService.listByVoucherNo(bisExc.getVoucherNo(), bisExc.getCbsBillId());
            for (NisWageDetailDTO wageDetailDTO : listWageDtl) {
                if (!BdlinkConstant.INTERFACE_FAILED.equals(wageDetailDTO.getVoucherStat()) && !BdlinkConstant.BANK_FAILED.equals(wageDetailDTO.getVoucherStat())) {
                    throw new BusinessException(NetbankMessageEnum.NIS_WAGE_MESSAGE_STATUS_FAIL_DEAL, bisExc.getCbsBillNo());
                }
            }
        }
        //指令重发，更新原指令状态为D-指令删除
        int result = this.bisExcDao.updateBisExcVoucherStat(bisExc, BdlinkConstant.DELETE);
        if (result > 0) {
            //更新业务单
            String serviceName = BdlinkConstant.getServiceNameByVoucherFrom(bisExc.getVoucherFrom());
            if (StringUtils.isNotEmpty(serviceName)) {
                IBisExcBackService bisExcBackService = (IBisExcBackService) this.getBean(serviceName);
                com.bytter.biz.payment.api.dto.BisExcDTO dto = new com.bytter.biz.payment.api.dto.BisExcDTO();
                BeanUtils.copyNotNullProperties(bisExc, dto);
                bisExcBackService.resendBisExc(dto, user);
            }
            //生成新的付款指令
            BisExc entity = new BisExc();
            BeanUtils.copyNotNullProperties(bisExc, entity);
            entity.setId(null);
            entity.setReqDate(DateUtils.getDate());
            //调整了申请日期，需同步调整加密信息
            entity.setVerifyCode(this.setVerifyCode(entity));
            entity.setVoucherNo(SequenceSingleton.getSequence(SequenceEnum.BIS_EXC));
            //跨行转付在下拨指令成功后延迟支付时间
            if (BdlinkConstant.FROM_NIS_PAY.equals(entity.getVoucherFrom())) {
                String delayTime = configService.getConfig(NetbankConstant.NIS_CONFIG_TYPE, NetbankConstant.NIS_CONFIG_DELAYTIME).getValue();
                if (StringUtils.isNotBlank(delayTime)) {
                    entity.setDelayTime(new Integer(delayTime));
                }
            }
            //代发工资指令更新明细的交易状态为3-准备发送
            if (BdlinkConstant.FROM_NIS_WAGE.equals(bisExc.getVoucherFrom())) {
                result = nisWageService.updateVoucherStatByVoucherNo(bisExc.getVoucherNo(), bisExc.getCbsBillId(), entity.getVoucherNo());
                if (result < 1) {
                    throw new BusinessException(NetbankMessageEnum.NIS_WAGE_MESSAGE_STATUS_FAIL_DEAL, bisExc.getCbsBillNo());
                }
            }
            //接口代码业务规则
            BisBifRuleDTO bisBifRule = bisBifRuleService.findByBifId(bisExc.getBifCode());
            if(!ObjectUtils.isEmpty(bisBifRule)) {
	            if (CommonConstant.YES.equals(bisBifRule.getIsAddBillNo())) {
	                if (StringUtils.isNotBlank(entity.getAddWord())) {
	                    entity.setAddWord(entity.getAddWord() + BdlinkConstant.ADDWORD_SPLIT + entity.getVoucherNo());
	                } else {
	                    entity.setAddWord(BdlinkConstant.ADDWORD_SPLIT + entity.getVoucherNo());
	                }
	            }
            }
            entity.setVoucherStat(BdlinkConstant.PRE_SEND);
            entity.setCreateBy(user.getLoginName());
            entity.setCreateDate(DateUtils.getDate());
            entity.setUpdateBy(user.getLoginName());
            entity.setUpdateDate(DateUtils.getDate());
            entity.setReturnTime(null);
            entity.setFirstReturnMsg(null);
            entity.setNextReturnMsg(null);
            entity.setNoticeSign(CommonConstant.SEND_SIGN_NO);
            entity.setFailReason(null);
            //指令重发标志：1-重发指令
            entity.setResendSign(CommonConstant.SEND_SIGN_YES);
            this.saveOrUpdate(entity);
//            bisMQService.sendBisExcChangeMsg(entity, BdlinkConstant.PRE_SEND);
            bisExcStatusChangeHandler.processBisExcChangeMsg(entity.getVoucherFrom(),entity.getCbsBillId(),BdlinkConstant.PRE_SEND);
            //生成日志
            nisMessageService.saveMessage(bisExc.getId(), NisMessageConstant.MESSAGE_TYPE_EXC_RESEND, "", NisMessageConstant.BUS_TYPE_BDLINKPAY, user.getLoginName(), user.getUsername());
            //完成消息通知
            notificationService.complete(NotificationConstant.NETBANK.BISEXC_FAIL, bisExc.getId(), user.getId());
            notificationService.complete(NotificationConstant.NETBANK.BISEXC_OVERTIME, bisExc.getId(), user.getId());
            notificationService.complete(NotificationConstant.NETBANK.BISEXC_DOUBTFUL_OVERTIME, bisExc.getId(), user.getId());
        } else {
            throw new BusinessException(NetbankMessageEnum.NIS_PAY_MESSAGE_STATUS_CHANGE, bisExc.getVoucherNo());
        }
    }
    /**
     * 指令作废
     *
     * @param bisExc
     */
    public void defeatBisExc(BisExc bisExc, ICurrentUser user) {
        //指令作废，更新原指令状态为7-指令无效
        int result = this.bisExcDao.updateBisExcVoucherStat(bisExc, BdlinkConstant.INVALIDATE);
        if (result > 0) {
            //更新业务单
            String serviceName = BdlinkConstant.getServiceNameByVoucherFrom(bisExc.getVoucherFrom());
            if (StringUtils.isNotEmpty(serviceName)) {
                IBisExcBackService bisExcBackService = (IBisExcBackService) this.getBean(serviceName);
                com.bytter.biz.payment.api.dto.BisExcDTO dto = new com.bytter.biz.payment.api.dto.BisExcDTO();
                BeanUtils.copyNotNullProperties(bisExc, dto);
                bisExcBackService.defeatBisExc(dto, user);
            }
//            bisMQService.sendBisExcChangeMsg(bisExc, BdlinkConstant.INVALIDATE);
            bisExcStatusChangeHandler.processBisExcChangeMsg(bisExc.getVoucherFrom(),bisExc.getCbsBillId(),BdlinkConstant.PRE_SEND);
            //生成日志
            nisMessageService.saveMessage(bisExc.getId(), NisMessageConstant.MESSAGE_TYPE_EXC_REPEAL, "", NisMessageConstant.BUS_TYPE_BDLINKPAY, user.getLoginName(), user.getUsername());
            //完成消息通知
            notificationService.complete(NotificationConstant.NETBANK.BISEXC_FAIL, bisExc.getId(), user.getId());
            notificationService.complete(NotificationConstant.NETBANK.BISEXC_OVERTIME, bisExc.getId(), user.getId());
            notificationService.complete(NotificationConstant.NETBANK.BISEXC_DOUBTFUL_OVERTIME, bisExc.getId(), user.getId());
        } else {
            throw new BusinessException(NetbankMessageEnum.NIS_PAY_MESSAGE_STATUS_CHANGE, bisExc.getVoucherNo());
        }
    }
    /**
     * 获取账号（付款/收款）
     *
     * @param corpId        单位ID
     * @param curId         币别ID
     * @param bifCode       接口代码ID
     * @param voucherTypeId 结算方式ID
     * @param flag          0-收款  1-付款
     * @return
     */
    @SuppressWarnings("unchecked")
    public List<BtBankAccDTO> findBankAcc(String corpId, String curId, String bifCode, String voucherTypeId, String payerAcc, String flag) {
        List<BtBankAccDTO> bankAccList = new ArrayList<BtBankAccDTO>();
        if (!StringUtils.isEmpty(corpId)) {
            FcVoucherTypeDTO fcVoucherType = fcVoucherTypeService.getFcVoucherTypeById(voucherTypeId);
            //获取付款账号列表
            if ("1".equals(flag)) {
                //获取转账权限的在线账号
                List<BtBankAccDTO> listPayerAcc = accountGrantService.listOnlineBankAccount(corpId, AccountGrantConstant.TRANSFER_ACCOUNT);
                Set<String> bank = new HashSet<>();
                for (BtBankAccDTO dto : listPayerAcc) {
                    if (dto.getCurId().equals(curId)) {
                        if (NetbankConstant.PAY_TYPE_33.equalsIgnoreCase(fcVoucherType.getSysVoucherCode()) || NetbankConstant.PAY_TYPE_35.equalsIgnoreCase(fcVoucherType.getSysVoucherCode())) {
                            //代付&代理支付 银行账号对应的父账号
                            BtBankAccDTO prentBankAcc = this.btBankAccService.getParentAccByAccId(dto.getId(), dto.getCurId());
                            if (!EmptyUtils.isEmpty(prentBankAcc) && !bank.contains(prentBankAcc.getBankAcc()) && !corpId.equals(prentBankAcc.getCorpId()) && (EmptyUtils.isBlank(bifCode) || prentBankAcc.getBifCode().equalsIgnoreCase(bifCode))) {
                                bankAccList.add(prentBankAcc);
                                bank.add(prentBankAcc.getBankAcc());
                            }
                        } else if (EmptyUtils.isBlank(bifCode) || dto.getBifCode().equalsIgnoreCase(bifCode)) {
                            bankAccList.add(dto);
                        }
                    }
                }
            } else {
                //获取在线+离线账号
                if (NetbankConstant.PAY_TYPE_10.equals(fcVoucherType.getSysVoucherCode())) {
                    BtBankAccDTO dto = new BtBankAccDTO();
                    dto.setBankAcc(payerAcc);
                    List<BtBankAccDTO> listPayerAcc = btBankAccService.getBankAccList(dto);
                    //拨付，获取付款账号对应的子账号
                    for (BtBankAccDTO bankAcc : listPayerAcc) {
                        BtBankAccDTO dto1 = new BtBankAccDTO();
                        dto1.setPrentAccId(bankAcc.getId());
                        List<BtBankAccDTO> listPayeeAcc = btBankAccService.getBankAccList(dto1);
                        bankAccList.addAll(listPayeeAcc);
                    }
                }
            }
        }
        return bankAccList;
    }
    /**
     * 新增付款指令
     *
     * @param bisExc
     */
    public void addBisExc(BisExc bisExc, ICurrentUser user) {
        //校验时间戳，是否存在重复提交
        if (this.isExistTimeStamp(bisExc.getTimeStamp())) {
            throw new BusinessException(NetbankMessageEnum.NIS_MESSAGE_REPEAT_SUBMIT);
        }
        bisExc.setVoucherNo(SequenceSingleton.getSequence(SequenceEnum.BIS_EXC));
        //接口代码业务规则
        BisBifRuleDTO bisBifRule = bisBifRuleService.findByBifId(bisExc.getBifCode());
        if(!ObjectUtils.isEmpty(bisBifRule)) {
	        if (CommonConstant.YES.equals(bisBifRule.getIsAddBillNo())) {
	            if (StringUtils.isNotBlank(bisExc.getAddWord())) {
	                bisExc.setAddWord(bisExc.getAddWord() + BdlinkConstant.ADDWORD_SPLIT + bisExc.getVoucherNo());
	            } else {
	                bisExc.setAddWord(BdlinkConstant.ADDWORD_SPLIT + bisExc.getVoucherNo());
	            }
	        }
        }
        bisExc.setWishPayday(bisExc.getReqDate());
        bisExc.setVoucherFrom(BdlinkConstant.FROM_BIS_EXC_PAY);
        bisExc.setBusOriType(BdlinkConstant.BUSORITYPE_48);
        bisExc.setCmdType(BdlinkConstant.CMDTYPE_MANUAL);
        bisExc.setNoticeSign(CommonConstant.SEND_SIGN_NO);
        if (!NetbankConstant.STATUE_TEMP.equals(bisExc.getStatus())) {
            //提交审批默认待审批
            bisExc.setVoucherStat(BdlinkConstant.WAIT_APPROVE);
        } else {
            //新增默认为空
            bisExc.setVoucherStat("");
        }
        bisExc.setCreateBy(user.getLoginName());
        bisExc.setCreateDate(DateUtils.getDate());
        //加密信息
        bisExc.setVerifyCode(this.setVerifyCode(bisExc));
        this.save(bisExc);
        Integer messageType = NisMessageConstant.MESSAGE_TYPE_TEMP;
        //启动审批
        if (!NetbankConstant.STATUE_TEMP.equals(bisExc.getStatus())) {
            messageType = NisMessageConstant.MESSAGE_TYPE_SUBMIT;
             getStartHandler().handler(NisWorkFlowConstant.NIS_BDLINK_PAY, bisExc.getId(), bisExc, bisExc.getCorpId(), user.getId());
        }
        //生成日志
        nisMessageService.saveMessage(bisExc.getId(), messageType, "", NisMessageConstant.BUS_TYPE_BDLINKPAY, user.getLoginName(), user.getUsername());
    }
    /**
     * 编辑付款指令
     *
     * @param bisExc
     */
    public void updateBisExc(BisExcVO bisExc, ICurrentUser user) {
        bisExc.setUpdateBy(user.getLoginName());
        bisExc.setUpdateDate(DateUtils.getDate());
        if (!NetbankConstant.STATUE_TEMP.equals(bisExc.getStatus())) {
            //提交审批默认待审批
            bisExc.setVoucherStat(BdlinkConstant.WAIT_APPROVE);
        } else {
            //新增默认为空
            bisExc.setVoucherStat("");
        }
        //加密信息
        BisExc entity = new BisExc();
        BeanUtils.copyNotNullProperties(bisExc, entity);
        bisExc.setVerifyCode(this.setVerifyCode(entity));
        //接口代码业务规则
        BisBifRuleDTO bisBifRule = bisBifRuleService.findByBifId(bisExc.getBifCode());
        if(!ObjectUtils.isEmpty(bisBifRule)) {
	        if (CommonConstant.YES.equals(bisBifRule.getIsAddBillNo())) {
	            bisExc.setAddWord(bisExc.getAddWord() + BdlinkConstant.ADDWORD_SPLIT + bisExc.getVoucherNo());
	        }
        }
        //编辑付款指令，控制原状态为0暂存/-1打回
        Integer[] oldStatus = {BdlinkConstant.STATUE_TEMP, BdlinkConstant.STATUE_BACK};
        BeanUtils.copyNotNullProperties(bisExc, bisExc);
        this.updateBisExcStatus(bisExc, oldStatus);
        Integer messageType = NisMessageConstant.MESSAGE_TYPE_TEMP;
        //启动审批
        if (!NetbankConstant.STATUE_TEMP.equals(bisExc.getStatus())) {
            messageType = NisMessageConstant.MESSAGE_TYPE_SUBMIT;
             getStartHandler().handler(NisWorkFlowConstant.NIS_BDLINK_PAY, bisExc.getId(), bisExc, bisExc.getCorpId(), user.getId());
        }
        //生成日志
        nisMessageService.saveMessage(bisExc.getId(), messageType, "", NisMessageConstant.BUS_TYPE_BDLINKPAY, user.getLoginName(), user.getUsername());
        //完成消息通知
        notificationService.complete(NotificationConstant.NETBANK.BISEXC_SALEPAY, bisExc.getId(), user.getId());
    }
    /**
     * 删除付款指令
     *
     * @param id
     */
    public void deleteBisExc(String id, ICurrentUser user) {
        BisExcVO bisExc = this.getByIdUnion(id);
        bisExc.setStatus(BdlinkConstant.STATUE_DEL);
        bisExc.setUpdateBy(user.getLoginName());
        bisExc.setUpdateDate(DateUtils.getDate());
        //删除付款指令，控制原状态为0暂存/-1打回
        Integer[] oldStatus = {BdlinkConstant.STATUE_TEMP, BdlinkConstant.STATUE_BACK};
        this.updateBisExcStatus(bisExc, oldStatus);
        //生成日志
        nisMessageService.saveMessage(bisExc.getId(), NisMessageConstant.MESSAGE_TYPE_DELETE, "", NisMessageConstant.BUS_TYPE_BDLINKPAY, user.getLoginName(), user.getUsername());
        //完成消息通知
        notificationService.complete(NotificationConstant.NETBANK.BISEXC_SALEPAY, bisExc.getId(), user.getId());
    }
    /**
     * 审批流程列表查询
     *
     * @param dataTablesInput
     * @return
     */
    public DataTablesOutput dataTableApprove(DataTablesInput dataTablesInput) {
        if (IWorkFlowService.PASS.equals(dataTablesInput.getParameter().get("flowStatus"))) {
            //「已审批」只展示当前用户审批过的单据，与 /bpm/my/finishCount 角标同口径。
            //不能走 getWorkFlowBusiness(pass)：它按 ACT_RE_PROCDEF 解析流程定义，
            //流程定义重新部署后老单据会全部查不到（角标查的是 ACT_HI_PROCINST.PROC_DEF_KEY_）
            List<String> businessIdList = this.workFlowService.listFinishedBusinessKeys(
                    NisWorkFlowConstant.NIS_BDLINK_PAY, dataTablesInput.getParameter().get("cycleTime"),
                    dataTablesInput.getCurrentUserId());
            String businessSql = SqlUtil.getInSql(businessIdList, "b.id", true);
            DataTablesOutput dp = this.bisExcDao.dataTableApprove(dataTablesInput, businessSql);
            this.setJoinField(dp, "");
            return dp;
        }
        WorkFlowBusiness workFlowBusiness = this.workFlowService.getWorkFlowBusiness(NisWorkFlowConstant.NIS_BDLINK_PAY, dataTablesInput);
        String businessSql = SqlUtil.getInSql(workFlowBusiness.getBusinessIdList(), "b.id", true);
        DataTablesOutput dp = this.bisExcDao.dataTableApprove(dataTablesInput, businessSql);
        this.setJoinField(dp, "");
        return workFlowBusiness.merge(dp);
    }
    @Override
    public void flowStarted(IWorkFlowResult workFlowResult) {
    }
    @Override
    public void flowCompleted(IWorkFlowResult workFlowResult) {
        ICurrentUser user = workFlowResult.getUser();
        BisExc entity = this.get(workFlowResult.getBusinessKey(), true);
        BisExcVO bisExcVO = new BisExcVO();
        BeanUtils.copyNotNullProperties(entity, bisExcVO);
        if (workFlowResult.isPass()) {
            bisExcVO.setStatus(NetbankConstant.STATUE_CHECK);
            bisExcVO.setVoucherStat(BdlinkConstant.WAIT_FILL);
        } else {
            bisExcVO.setStatus(NetbankConstant.STATUE_BACK);
            bisExcVO.setVoucherStat(BdlinkConstant.PASS_BACK);
        }
        bisExcVO.setUpdateBy(user.getLoginName());
        bisExcVO.setUpdateDate(DateUtils.getDate());
        //加密信息
        bisExcVO.setVerifyCode(this.setVerifyCode(entity));
        Integer[] oldStatus = {BdlinkConstant.STATUE_WAIT_CHECK};
        this.updateBisExcStatus(bisExcVO, oldStatus);
        Integer messageType = workFlowResult.isPass() ? NisMessageConstant.MESSAGE_TYPE_CHECK_PASS : NisMessageConstant.MESSAGE_TYPE_CHECK_BACK;
        //生成日志
        nisMessageService.saveMessage(entity.getId(), messageType, "", NisMessageConstant.BUS_TYPE_BDLINKPAY, user.getLoginName(), user.getUsername());
    }
    /**
     * 指令交易成功/失败消息提醒
     */
    @Override
    @XxlJob("BisExcService")
    public void run(String arguments) {
        arguments = XxlJobHelper.getJobParam();
        logger.info("定时任务接收参数：【{}】", arguments);
        List<BisExcVO> list = null;
        //取未生成消息提醒的指令
        StringBuilder sql = new StringBuilder(" and (b.notice_sign <> '1' OR b.notice_sign is null or b.notice_sign='')");
        if (NotificationConstant.NETBANK.BISEXC_FAIL.equals(arguments)) {
            //交易失败提醒
            list = this.bisExcDao.listByVoucherStat(BdlinkConstant.BANK_FAILED, sql.toString());
        } else if (NotificationConstant.NETBANK.BISEXC_SUCCESS.equals(arguments)) {
            //交易成功提醒
            list = this.bisExcDao.listByVoucherStat(BdlinkConstant.SUCCESS, sql.toString());
        } else if (NotificationConstant.NETBANK.BISEXC_OVERTIME.equals(arguments)) {
            String payOverTime = configService.getConfig(NetbankConstant.NIS_CONFIG_TYPE, NetbankConstant.NIS_CONFIG_PAYOVERTIME).getValue();
            Integer payOverTimeDays = -1;
            if (StringUtils.isNotBlank(payOverTime)) {
                payOverTimeDays = Integer.parseInt(payOverTime) * payOverTimeDays;
            }
            Date reqDate = DateUtils.addDay(DateUtils.getDateYMD(), payOverTimeDays);
            sql.append(" and b.req_Date <= bt_to_date('").append(DateUtils.formatDate(reqDate)).append("','yyyy-MM-dd') ");
            //付款支付超时预警
            list = this.bisExcDao.listByVoucherStat(BdlinkConstant.PRE_SEND, sql.toString());
        } else if (NotificationConstant.NETBANK.BISEXC_DOUBTFUL_OVERTIME.equals(arguments)) {
            //付款可疑指令发送确认提醒
            list = this.bisExcDao.listByVoucherStat(BdlinkConstant.SUSPECTED, sql.toString());
        } else if (NotificationConstant.NETBANK.BISEXC_WAITRESULT_OVERTIME.equals(arguments)) {
            String waitResultOverTime = configService.getConfig(NetbankConstant.NIS_CONFIG_TYPE, NetbankConstant.NIS_CONFIG_WAITRESULTOVERTIME).getValue();
            Integer waitResultOverTimeDays = -1;
            if (StringUtils.isNotBlank(waitResultOverTime)) {
                waitResultOverTimeDays = Integer.parseInt(waitResultOverTime) * waitResultOverTimeDays;
            }
            Date transTime = DateUtils.addDay(DateUtils.getDateYMD(), waitResultOverTimeDays);
            sql.append(" and b.trans_Time <= bt_to_date('").append(DateUtils.formatDate(transTime)).append("','yyyy-MM-dd') ");
            //支付等待结果超时提醒
            list = this.bisExcDao.listByVoucherStat(BdlinkConstant.WAIT_RESULT, sql.toString());
        } else if (NotificationConstant.NETBANK.BISEXC_CONSUMPTION_OVERTIME.equals(arguments)) {
            //付款指令中间状态停留超时预警
            String consumptionOverTime = configService.getConfig(NetbankConstant.NIS_CONFIG_TYPE, NetbankConstant.NIS_CONFIG_CONSUMPTIONOVERTIME).getValue();
            Integer consumptionOverTimes = -1;
            if (StringUtils.isNotBlank(consumptionOverTime)) {
                //时间带小数位，转换为分钟
                Double consumptionOverTimeDouble = Double.parseDouble(consumptionOverTime)*60;
                consumptionOverTimes = consumptionOverTimeDouble.intValue()*consumptionOverTimes;
            }
            Date transTime = DateUtils.addMinute(DateUtils.getDate(), consumptionOverTimes);
            sql.append(" and b.trans_Time <= bt_to_date('").append(DateUtils.formatDate(transTime, DateUtils.PATTERN_CLASSICAL)).append("','yyyy-MM-dd hh24:mi:ss') ");
            list = this.bisExcDao.listByVoucherStat(BdlinkConstant.CONSUMPTION, sql.toString());
        }
        if (!EmptyUtils.isEmptyList(list)) {
            List<String> ids = new ArrayList<>();
            for (BisExcVO exc : list) {
                ids.add(exc.getId());
                //消息提醒
                notificationService.send(exc.getId(), exc.getCorpId(), arguments, exc);
            }
            //更新消息提醒标志
            this.bisExcDao.updateNoticeSign(ids);
        }
    }
    /**
     * 批量提交付款指令
     *
     * @param list
     */
    public void batchSubmitBisExc(List<BisExc> list, ICurrentUser user) {
        for (BisExc entity : list) {
            BisExcVO bisExc = new BisExcVO();
            BeanUtils.copyNotNullProperties(entity, bisExc);
            bisExc.setStatus(BdlinkConstant.STATUE_WAIT_CHECK);
            this.updateBisExc(bisExc, user);
        }
    }
    /**
     * 批量删除付款指令
     *
     * @param ids
     */
    public void batchDelBisExc(String ids, ICurrentUser user) {
        for (String id : ids.split(CommonConstant.COMMA_SPLIT)) {
            this.deleteBisExc(id, user);
        }
    }
    /**
     * 重复付款校验
     *
     * @param corpId     单位
     * @param payMoney   金额
     * @param payeeAcc   收款账号
     * @param id         单据号（编辑时校验需要）
     * @param wishPayday 交易日期
     * @return
     */
    public boolean isExistRepay(String corpId, BigDecimal payMoney, String payeeAcc, String curId, String id, Date wishPayday, String type) {
        List<BisExc> list = bisExcDao.listByRePay(corpId, payMoney, payeeAcc, curId, id, wishPayday, "", "", type);
        return !EmptyUtils.isEmptyList(list);
    }
    /**
     * 重复付款校验
     *
     * @param corpId     单位
     * @param payMoney   金额
     * @param payeeAcc   收款账号
     * @param id         单据号（编辑时校验需要）
     * @param wishPayday 交易日期
     * @return
     */
    public boolean isExistBatchRepay(String corpId, BigDecimal payMoney, String payeeAcc, String curId, String id, Date wishPayday, String voucherStat, String ids, String type) {
        List<BisExc> list = bisExcDao.listByRePay(corpId, payMoney, payeeAcc, curId, id, wishPayday, voucherStat, ids, type);
        return !EmptyUtils.isEmptyList(list);
    }
    /**
     * 根据业务单号获取指令信息（代发工资）
     *
     * @param cbsBillNo
     * @param voucherFrom
     * @return
     */
    public List<BisExcVO> getBisExcByCbsBillNoForNisWage(String cbsBillNo, String voucherFrom) {
        return this.bisExcDao.getBisExcByCbsBillNoForNisWage(cbsBillNo, voucherFrom);
    }
    /**
     * 撤回
     *
     * @param workFlowResult
     */
    @Override
    public void flowRevoke(IWorkFlowResult workFlowResult) {
        ICurrentUser user = workFlowResult.getUser();
        BisExc entity = this.get(workFlowResult.getBusinessKey(), true);
        entity.setStatus(BdlinkConstant.STATUE_TEMP);
        entity.setVoucherStat(null);
        entity.setUpdateBy(user.getLoginName());
        entity.setUpdateDate(DateUtils.getDate());
        this.saveOrUpdate(entity);
        //生成日志
        nisMessageService.saveMessage(entity.getId(), NisMessageConstant.MESSAGE_TYPE_CHECK_REVOKE, NisMessageConstant.NIS_LOG_WITHDRAW_SUCC, NisMessageConstant.BUS_TYPE_BDLINKPAY, user.getLoginName(), user.getUsername());
    }
    /**
     * 生成加密信息
     *
     * @param entity
     * @return
     */
    public String setVerifyCode(BisExc entity) {
        //自动下拨/跨行自动归集的指令，银行接口未写入加密信息，无法验证
        if (BdlinkConstant.FROM_ALLOCATION_TYPE_AUTO_DOWN.equals(entity.getVoucherFrom()) || BdlinkConstant.FROM_ALLOCATION_TYPE_AUTO_UP.equals(entity.getVoucherFrom())) {
            return entity.getVerifyCode();
        }
        //金额格式转换，不足两位小数，自动补零(制单跳过审批时金额未自动补零，导致生成指令和发送时加密串不一致)
        String payMoney = NumberUtil.formatDoubles(entity.getPayMoney(), 2);
        //加密规则：付款账号+付款金额+申请日期+收款账户名+收款账号
        String sourceData = entity.getPayerAcc() + payMoney + DateUtils.formatDate(entity.getReqDate(), DateUtils.PATTERN_CLASSICAL_SIMPLE)
        + (StringUtils.isBlank(entity.getPayeeAccName()) ? "" : entity.getPayeeAccName())
        + (StringUtils.isBlank(entity.getPayeeAcc()) ? "" : entity.getPayeeAcc());
        return BytterCypherUtil.encodeToString(sourceData);
    }

    public static void main(String[] args) {
    	String s = BytterCypherUtil.encodeToString("2222512024-01-05nullnull");
		System.out.println("s="+s);
	}
    @Override
    public List<CorpVerifyErrorDTO> verifyCorpDisable(String corpId) {
        if (StringUtils.isNotEmpty(corpId)) {
            List<CorpVerifyErrorDTO> verifyErrorDTOS = new ArrayList<CorpVerifyErrorDTO>();
            //单位有在途的自营付款单据，不允许禁用！
            int num = this.bisExcDao.getBisExcCountByCorpId(corpId, BdlinkConstant.FROM_BIS_EXC_PAY);
            if (num > 0) {
                verifyErrorDTOS.add(new CorpVerifyErrorDTO(corpId, SysConfigConstant.Module.NIS, SysConfigConstant.BusType.NIS_INPUT_EXC, multiLangServiceI.getLang(CorpVerifyMessageConstant.NIS_VALID_DISABLE_INPUTEXC_ERROR)));
            }
            //单位有在途的付款指令，不允许禁用！
            num = this.bisExcDao.getBisExcCountByCorpId(corpId, null);
            if (num > 0) {
                verifyErrorDTOS.add(new CorpVerifyErrorDTO(corpId, SysConfigConstant.Module.NIS, SysConfigConstant.BusType.NIS_EXC, multiLangServiceI.getLang(CorpVerifyMessageConstant.NIS_VALID_DISABLE_EXC_ERROR)));
            }
            return verifyErrorDTOS;
        }
        return null;
    }
    @Override
    public List<CorpVerifyErrorDTO> verifyCorpParentChang(String corpId) {
        if (StringUtils.isNotEmpty(corpId)) {
            List<CorpVerifyErrorDTO> verifyErrorDTOS = new ArrayList<CorpVerifyErrorDTO>();
            //单位有在途的资金调拨付款指令，不允许变更！
            int num = this.bisExcDao.getBisExcCountByCorpIdForAllocation(corpId);
            if (num > 0) {
                verifyErrorDTOS.add(new CorpVerifyErrorDTO(corpId, SysConfigConstant.Module.NIS, SysConfigConstant.BusType.NIS_EXC, multiLangServiceI.getLang(CorpVerifyMessageConstant.NIS_VALID_CHANGE_EXC_ERROR)));
            }
            return verifyErrorDTOS;
        }
        return null;
    }
    /**
     * 查询待记账数据
     */
    public List<BisExcDTO> queryTallyBisEx() {
        return bisExcDao.queryTallyBisEx();
    }
    /**
     * 指令对应业务单号更新（指令生成时判断原指令状态）
     *
     * @param bisExc
     * @return
     */
    public int updateBisExcCbsBillNo(BisExc bisExc) {
        return this.bisExcDao.updateBisExcCbsBillNo(bisExc);
    }
    /**
     * 指令结果查询
     *
     * @param bisExc
     */
    public void queryResultBisExc(BisExc bisExc, ICurrentUser user) {
        //指令结果查询，更新原指令状态为1-等待结果
        int result = this.bisExcDao.updateBisExcVoucherStat(bisExc, BdlinkConstant.WAIT_RESULT);
        if (result > 0) {
            //更新业务单
            String serviceName = BdlinkConstant.getServiceNameByVoucherFrom(bisExc.getVoucherFrom());
            if (StringUtils.isNotEmpty(serviceName)) {
                IBisExcBackService bisExcBackService = (IBisExcBackService) this.getBean(serviceName);
                com.bytter.biz.payment.api.dto.BisExcDTO dto = new com.bytter.biz.payment.api.dto.BisExcDTO();
                BeanUtils.copyNotNullProperties(bisExc, dto);
                bisExcBackService.queryResultBisExc(dto, user);
            }
//            bisMQService.sendBisExcChangeMsg(bisExc, BdlinkConstant.WAIT_RESULT);
            bisExcStatusChangeHandler.processBisExcChangeMsg(bisExc.getVoucherFrom(),bisExc.getCbsBillId(),BdlinkConstant.WAIT_RESULT);
            //生成日志
            nisMessageService.saveMessage(bisExc.getId(), NisMessageConstant.MESSAGE_TYPE_EXC_QUERY_RESULT, "", NisMessageConstant.BUS_TYPE_BDLINKPAY, user.getLoginName(), user.getUsername());
            //完成消息通知
            notificationService.complete(NotificationConstant.NETBANK.BISEXC_FAIL, bisExc.getId(), user.getId());
        }
    }
    /**
     * 判断是否存在相同的时间戳
     *
     * @param timeStamp
     * @return
     */
    public boolean isExistTimeStamp(String timeStamp) {
        if (StringUtils.isNotBlank(timeStamp)) {
            List<BisExc> list = this.bisExcDao.listBy("timeStamp", timeStamp);
            return EmptyUtils.isNotEmpty(list);
        }
        return false;
    }
    /**
     * 带原状态更新付款指令
     *
     * @param vo
     * @param oldStatus
     * @return
     */
    public void updateBisExcStatus(BisExcVO vo, Integer[] oldStatus) {
        int result = this.bisExcDao.updateBisExcStatus(vo, oldStatus);
        if (result < 1) {
            throw new ParameterException(NetbankMessageEnum.NIS_PAY_MESSAGE_STATUS_CHANGE, vo.getVoucherNo());
        }
    }
    /**
     * 设为支付成功/失败
     *
     * @param bisExcVo
     */
    public void setPayResultBisExc(BisExcVO bisExcVo, ICurrentUser user) {
        BisExc bisExc = this.get(bisExcVo.getId(), true);
        //失败状态才可以提交
        if (bisExc != null && !BdlinkConstant.WAIT_RESULT.equals(bisExc.getVoucherStat())) {
            throw new ParameterException(NetbankMessageEnum.NIS_PAY_MESSAGE_STATUS_CHANGE, bisExc.getVoucherNo());
        }
        //指令状态重设，更新原指令状态为0-交易成功/2-银行处理失败
        int result = this.bisExcDao.updateBisExcVoucherStat(bisExc, bisExcVo.getVoucherStat());
        if (result > 0) {
            //更新业务单
            String serviceName = BdlinkConstant.getServiceNameByVoucherFrom(bisExc.getVoucherFrom());
            if (StringUtils.isNotEmpty(serviceName)) {
                IBisExcBackService bisExcBackService = (IBisExcBackService) this.getBean(serviceName);
                com.bytter.biz.payment.api.dto.BisExcDTO dto = new com.bytter.biz.payment.api.dto.BisExcDTO();
                BeanUtils.copyNotNullProperties(bisExc, dto);
                dto.setVoucherStat(bisExcVo.getVoucherStat());
                dto.setHandleOpinion(bisExcVo.getHandleOpinion());
                dto.setExpenseDtlIds(bisExcVo.getExpenseDtlIds());
                bisExcBackService.setPayResultBisExc(dto, user);
            }
//            bisMQService.sendBisExcChangeMsg(bisExc,bisExcVo.getVoucherStat());
            bisExcStatusChangeHandler.processBisExcChangeMsg(bisExc.getVoucherFrom(),bisExc.getCbsBillId(),bisExcVo.getVoucherStat());
            //生成日志
            Integer messageType = BdlinkConstant.SUCCESS.equals(bisExcVo.getVoucherStat()) ? NisMessageConstant.MESSAGE_TYPE_PAY_SUCCESS : NisMessageConstant.MESSAGE_TYPE_PAY_FAILURE;
            nisMessageService.saveMessage(bisExc.getId(), messageType, bisExcVo.getHandleOpinion(), NisMessageConstant.BUS_TYPE_BDLINKPAY, user.getLoginName(), user.getUsername());
            //完成消息通知
            notificationService.complete(NotificationConstant.NETBANK.BISEXC_WAITRESULT_OVERTIME, bisExc.getId(), user.getId());
        } else {
            throw new BusinessException(NetbankMessageEnum.NIS_PAY_MESSAGE_STATUS_CHANGE, bisExc.getVoucherNo());
        }
    }
    /**
     * 设为支付成功/失败
     *
     * @param bisExcVo
     */
    public void setPayResultBisExcProc(BisExcVO bisExcVo, ICurrentUser user) {
        BisExcVO bisExcProc = this.getSpecFailById(bisExcVo.getId());
        //失败状态才可以提交
        if (bisExcProc != null && !BdlinkConstant.WAIT_RESULT.equals(bisExcProc.getVoucherStat())) {
            throw new ParameterException(NetbankMessageEnum.NIS_PAY_MESSAGE_STATUS_CHANGE, bisExcProc.getVoucherNo());
        }
        //指令状态重设，更新原指令状态为0-交易成功/2-银行处理失败
        int result = this.bisExcDao.updateBisExcProcVoucherStat(bisExcProc, bisExcVo.getVoucherStat());
        if (result > 0) {
            //更新转付的外付指令
            if (BdlinkConstant.BANK_FAILED.equals(bisExcVo.getVoucherStat())) {
                //获取转付指令
                BisExc bisExc = this.getByCbsBillId(bisExcProc.getCbsBillId());
                this.bisExcDao.updateBisExcVoucherStatForProc(bisExc, bisExcVo.getVoucherStat());
//                bisMQService.sendBisExcChangeMsg(bisExc, bisExcVo.getVoucherStat());
                bisExcStatusChangeHandler.processBisExcChangeMsg(bisExc.getVoucherFrom(),bisExc.getCbsBillId(),bisExcVo.getVoucherStat());
            }

            //生成日志
            Integer messageType = BdlinkConstant.SUCCESS.equals(bisExcVo.getVoucherStat()) ? NisMessageConstant.MESSAGE_TYPE_PAY_SUCCESS : NisMessageConstant.MESSAGE_TYPE_PAY_FAILURE;
            nisMessageService.saveMessage(bisExcProc.getId(), messageType, bisExcVo.getHandleOpinion(), NisMessageConstant.BUS_TYPE_BDLINKPAY, user.getLoginName(), user.getUsername());
        } else {
            throw new BusinessException(NetbankMessageEnum.NIS_PAY_MESSAGE_STATUS_CHANGE, bisExcVo.getVoucherNo());
        }
    }
    /**
     * 获取转付失败是否是特殊情况（下拨成功，外付失败）
     *
     * @param voucherNo
     * @return
     */
    public BisExcVO getSpecFailForTranPay(String voucherNo, String cbsBillId) {
        return bisExcDao.getSpecFailForTranPay(voucherNo, cbsBillId);
    }
    /**
     * 获取转付失败是否是特殊情况（下拨成功，外付失败）
     *
     * @param id
     * @return
     */
    public BisExcVO getSpecFailById(String id) {
        return bisExcDao.getSpecFailById(id);
    }
    /**
     * 判断转付失败是否是特殊情况（下拨成功，外付失败）
     *
     * @param voucherNo
     * @return
     */
    public boolean isSpecFailForTranPay(String voucherNo, String cbsBillId) {
        BisExcVO bisExcVO = this.getSpecFailForTranPay(voucherNo, cbsBillId);
        if (!EmptyUtils.isEmpty(bisExcVO)) {
            //转付下拨指令表交易成功，表示下拨成功，外付失败
            return BdlinkConstant.SUCCESS.equals(bisExcVO.getVoucherStat());
        }
        return false;
    }
    public List<BisExcDTO> getBisExcByCbsId(List<String> billIds) {
        return bisExcDao.getBisExcByCbsId(billIds);
    }

    public List<BisExcDTO> getAllBisExcBankSerialNo() {
        return bisExcDao.getAllBisExcBankSerialNo();
    }

    public List<BisExcDTO> getBisExcByCbsIdsWithCondition(List<String> billIds) {
        return bisExcDao.getBisExcByCbsIdsWithCondition(billIds);
    }



    /**
     * 根据业务单号ID集合和指令来源，获取指令Map
     *
     * @param billIds
     * @param voucherFrom
     * @return
     */
    public Map<String, BisExcDTO> getBisExcMapByCbsBillIds(List<String> billIds, String voucherFrom) {
        List<BisExcDTO> listBisExc = bisExcDao.getBisExcByCbsBillIds(billIds, voucherFrom);
        Map<String, BisExcDTO> bisExcMap = new HashMap<>();
        if (EmptyUtils.isNotEmpty(listBisExc)) {
            //交易状态
            Map<String, DictDTO> voucherStatDictMap = dictService.listMap(BdlinkConstant.EXC_VOUCHERSTAT_DICT);
            for (BisExcDTO dto : listBisExc) {
                if (voucherStatDictMap.containsKey(dto.getVoucherStat())) {
                    dto.setVoucherStatName(voucherStatDictMap.get(dto.getVoucherStat()).getName());
                }
                bisExcMap.put(dto.getCbsBillId(), dto);
            }
        }
        return bisExcMap;
    }
    public Map<String, BisExcDTO> getBisExcMapByCbsBillIds(List<String> billIds, String voucherFrom,List<String> voucherStat) {
        List<BisExcDTO> listBisExc = bisExcDao.getBisExcByCbsBillIds(billIds, voucherFrom,voucherStat);
        Map<String, BisExcDTO> bisExcMap = new HashMap<>();
        if (EmptyUtils.isNotEmpty(listBisExc)) {
            //交易状态
            Map<String, DictDTO> voucherStatDictMap = dictService.listMap(BdlinkConstant.EXC_VOUCHERSTAT_DICT);
            for (BisExcDTO dto : listBisExc) {
                if (voucherStatDictMap.containsKey(dto.getVoucherStat())) {
                    dto.setVoucherStatName(voucherStatDictMap.get(dto.getVoucherStat()).getName());
                }
                bisExcMap.put(dto.getCbsBillId(), dto);
            }
        }
        return bisExcMap;
    }
    /**
     * 根据业务单号集合和指令来源，获取指令Map
     *
     * @param billNos
     * @param voucherFrom
     * @return
     */
    public Map<String, BisExcDTO> getBisExcMapByCbsBillNos(List<String> billNos, String voucherFrom) {
        List<BisExcDTO> listBisExc = bisExcDao.getBisExcByCbsBillNos(billNos, voucherFrom);
        Map<String, BisExcDTO> bisExcMap = new HashMap<>();
        if (EmptyUtils.isNotEmpty(listBisExc)) {
            //交易状态
            Map<String, DictDTO> voucherStatDictMap = dictService.listMap(BdlinkConstant.EXC_VOUCHERSTAT_DICT);
            for (BisExcDTO dto : listBisExc) {
                Optional.ofNullable(voucherStatDictMap).flatMap(dictMap -> Optional.ofNullable(dictMap.get(dto.getVoucherStat()))).ifPresent(dict -> dto.setVoucherStatName(dict.getName()));
                bisExcMap.put(dto.getCbsBillNo(), dto);
            }
        }
        return bisExcMap;
    }
    /**
     * 付款指令信息字典注入
     *
     * @param vo
     * @param listDictMap
     */
    private void injectValues(BisExcVO vo, Map<String, Map<String, DictDTO>> listDictMap) {
        // 交易状态
        Optional.ofNullable(vo.getVoucherStat()).ifPresent(voucherStat -> {
            Map<String, DictDTO> dictDtoMap = listDictMap.get(BdlinkConstant.EXC_VOUCHERSTAT_DICT);
            Optional.ofNullable(dictDtoMap).flatMap(dictMap -> Optional.ofNullable(dictMap.get(voucherStat))).ifPresent(dict -> vo.setVoucherStatName(dict.getName()));
        });
        // 业务类型
        Optional.ofNullable(vo.getBusOriType()).ifPresent(busOriType -> {
            Map<String, DictDTO> dictDtoMap = listDictMap.get(BdlinkConstant.EXC_BUSORITYPE_DICT);
            Optional.ofNullable(dictDtoMap).flatMap(dictMap -> Optional.ofNullable(dictMap.get(String.valueOf(busOriType)))).ifPresent(dict -> vo.setBusOriTypeName(dict.getName()));
        });
        // 指令类型
        Optional.ofNullable(vo.getCmdType()).ifPresent(cmdType -> {
            Map<String, DictDTO> dictDtoMap = listDictMap.get(BdlinkConstant.EXC_CMDTYPE_DICT);
            Optional.ofNullable(dictDtoMap).flatMap(dictMap -> Optional.ofNullable(dictMap.get(cmdType))).ifPresent(dict -> vo.setCmdTypeName(dict.getName()));
        });
        // 指令来源
        Optional.ofNullable(vo.getVoucherFrom()).ifPresent(voucherFrom -> {
            Map<String, DictDTO> dictDtoMap = listDictMap.get(BdlinkConstant.EXC_VOUCHERFROM_DICT);
            Optional.ofNullable(dictDtoMap).flatMap(dictMap -> Optional.ofNullable(dictMap.get(voucherFrom))).ifPresent(dict -> vo.setVoucherFromName(dict.getName()));
        });
    }

    public List<String> getBisExcCbsBillIdsByVoucherstat(){
    	return bisExcDao.getBisExcCbsBillIdsByVoucherstat();
    }

    /**
     * 根据交易状态查询指令集合
     *
     * @param voucherStat 指令状态
     * @param joinSql     其他条件
     * @return
     */
    public List<BisExcVO> listByVoucherStat(String voucherStat, String joinSql) {
        return bisExcDao.listByVoucherStat(voucherStat,joinSql);
    }

    /**
     * 根据交易状态查询指令集合
     * @return
     */
    public List<BisExcVO> listByVoucherStatForSuccessJob() {
        return bisExcDao.listByVoucherStatForSuccessJob();
    }

    /**
     * 查询资金需求单下拨指令结果集合，未发送提醒的
     * @return
     */
    public List<BisExcVO> listNisDemandDialExcResult() {
        return bisExcDao.listNisDemandDialExcResult();
    }

    /**
     * 根据失败交易状态查询指令集合-外付网银
     * @return
     */
    public List<BisExcVO> listByVoucherStatForPaymentFailJob(String beginDate) {
        return bisExcDao.listByVoucherStatForPaymentFailJob(beginDate);
    }


    public void updateNoticeSign(List<String> ids) {
        bisExcDao.updateNoticeSign(ids);
    }

    /**
     * 查询资金需求单头寸指令结果集合，未发送提醒的
     * @return
     */
    public List<BisExcVO> listNisDemandPositionExcResult() {
        return bisExcDao.listNisDemandPositionExcResult();
    }

    public List<BisExcVO> listBisExc(BisExcDTO dto) {
        return bisExcDao.listBisExcByDto(dto);
    }
}
