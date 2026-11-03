package com.bytter.tally.service;

import cn.hutool.core.collection.CollectionUtil;
import com.alibaba.excel.util.CollectionUtils;
import com.bytter.biz.bdlink.api.dto.BisAccDtlDTO;
import com.bytter.biz.bdlink.api.service.IBisAccDtlService;
import com.bytter.biz.finacial.api.constant.FinaBookConstant;
import com.bytter.biz.finacial.api.constant.FinaBookConstant.SysRaceCode;
import com.bytter.biz.finacial.api.constant.FinacialConstant;
import com.bytter.biz.finacial.api.constant.NbconsoleConstant;
import com.bytter.biz.finacial.api.constant.ResourceConstant;
import com.bytter.biz.finacial.api.dto.*;
import com.bytter.biz.finacial.api.enums.accbook.AccbookMessageEnum;
import com.bytter.biz.finacial.api.enums.accbook.ProcessType;
import com.bytter.biz.finacial.api.enums.bankCollect.CollectMessageEnum;
import com.bytter.biz.finacial.api.service.IFcAccbookService;
import com.bytter.biz.finacial.api.service.IFcBusRaceService;
import com.bytter.biz.finacial.api.service.IFcBusinessRuleDefineService;
import com.bytter.biz.finacial.api.service.ITallyService;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.exception.ParameterException;
import com.bytter.core.constant.ConstantBean;
import com.bytter.core.utils.BeanUtils;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.core.utils.NumberUtil;
import com.bytter.core.utils.StringUtils;
import com.bytter.finance.constant.CmsConstant;
import com.bytter.finance.enums.PrecativeMessageEnum;
import com.bytter.system.api.*;
import com.bytter.system.api.constant.ConfigConstant;
import com.bytter.system.api.constant.NotificationConstant;
import com.bytter.system.api.dto.FcAccDTO;
import com.bytter.system.api.dto.UserDTO;
import com.bytter.system.api.dto.*;
import com.bytter.tally.vo.FcAccbookExtVO;
import com.bytter.tally.vo.FcAccbookNotifyVO;
import com.bytter.tally.vo.FcAccbookPageVO;
import com.bytter.tally.vo.FcAccbookVO;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author
 * @version 1.0
 * @Title: Service
 * @Description: BIS_ACC_DTL
 * @date 2019-5-14
 */
@Service("FcBankAccDtlService")
@Transactional
public class FcBankAccDtlService {
    @DubboReference(check = false)
    private IFcCorpPoolService corpPoolService;
    @DubboReference(check = false)
    private IBisAccDtlService iBisAccDtlService;
    @DubboReference(check = false)
    private IFcBusinessRuleDefineService iFcBusinessRuleDefineService;
    @DubboReference(check = false)
    private IFcBusRaceService iFcBusRaceService;
    @DubboReference(check = false)
    private IBtBankAccService iBtBankAccService;
    @DubboReference(check = false)
    private IFcAccService iFcAccService;
    @DubboReference(check = false)
    private IFcAccbookService fcAccbookService;
    @DubboReference(check = false)
    private IConfigService configService;
    @DubboReference(check = false)
    private INotificationService notificationService;
    @DubboReference(check = false)
    private IUserService userService;
    @DubboReference(check = false)
    private ITallyService tallyService;

    //查询数据
    @Transactional(readOnly = true)
    public DataTablesOutput<BisAccDtlCopyDTO> queryBisAccDtl(DataTablesInput input, String type, String menuType, ICurrentUser user) {
        DataTablesOutput<BisAccDtlCopyDTO> output = new DataTablesOutput<BisAccDtlCopyDTO>();
        String resource = "fcl:pool:tally:apply";
        List<String> corpAuth = corpPoolService.getUserCorpPoolIds(resource, user);
        if(corpAuth.size()>0) {
            //查询账户
            String sql = "SELECT b.bank_acc as bankAcc,c.id AS poolId, c.pool_name AS corpName, c.corp_id AS corpId, cur.cur_name AS curName " +
                    " FROM bt_bank_acc  b, fc_corp_pool  c, fc_corp_pool d, bt_bank_acc e, bt_bank_acc_cur f, bt_currency cur " +
                    " WHERE b.corp_id in (:corpIds)" +
                    " AND b.corp_id = c.corp_id AND c.pool_sign = '1' AND c.status = '1'" +
                    "    AND e.corp_id = d.corp_id AND d.pool_sign = '0' AND d.status = '1'" +
                    "    AND e.id = f.bank_acc_id AND f.prent_acc_id = b.id" +
                    "    AND f.cur_id = cur.id AND c.id = d.pool_id";
            Map<String, Object> params = new HashMap<>();
            params.put("corpIds", corpAuth);
            List<BtBankAccDTO> btBankAccDTOS = iBtBankAccService.listBankAccBySql(sql, params);

            if (CollectionUtil.isEmpty(btBankAccDTOS)) {
                return output;
            }
            Map<String, BtBankAccDTO> bankAccDTOMap = btBankAccDTOS.stream().collect(Collectors.toMap(BtBankAccDTO::getBankAcc, Function.identity(), (pre, next) -> next));
            if ("Init".equals(type)) {
                //查询流水
                List<BisAccDtlDTO> dtlList = iBisAccDtlService.queryBisAccDtlDTO(input, menuType, new ArrayList<>(bankAccDTOMap.keySet()));
                List<String> collect = dtlList.stream().map(BisAccDtlDTO::getId).distinct().collect(Collectors.toList());
                //fc_bank_acc_dtl
                if (dtlList.isEmpty()) {
                    return output;
                }
                if (!collect.isEmpty()) {
                    List<FcBankAccDtlDTO> fcBankAccDtls = tallyService.getFcBankAccDtlsByIds(collect);
                    if (CollectionUtil.isNotEmpty(fcBankAccDtls)) {
                        Map<String, String> statusMap = fcBankAccDtls.stream().collect(Collectors.toMap(FcBankAccDtlDTO::getDtlId, FcBankAccDtlDTO::getTallyStatus));
                        dtlList.stream().forEach(vo -> {
                            if (StringUtils.isNotBlank(statusMap.get(vo.getId()))) {
                                vo.setStatus(Integer.valueOf(statusMap.get(vo.getId())));
                            }
                        });
                    }
                }
                List<BisAccDtlCopyDTO> listvo = new ArrayList<>();
                for (BisAccDtlDTO dto : dtlList) {
                    BtBankAccDTO acc = bankAccDTOMap.get(dto.getBankAcc());
                    dto.setPoolId(acc.getPoolId());
                    dto.setCorpName(acc.getCorpName());
                    dto.setCorpId(acc.getCorpId());
                    dto.setCurName(acc.getCurName());
                    BisAccDtlCopyDTO bisAccDtlCopyDTO = new BisAccDtlCopyDTO();
                    BeanUtils.copyProperties(dto, bisAccDtlCopyDTO);
                    listvo.add(bisAccDtlCopyDTO);
                }
                output.setData(listvo);
                return output;
            } else {
                //查询流水
                List<BisAccDtlDTO> dtlList = iBisAccDtlService.queryBisAccDtlDTO(input, menuType, new ArrayList<>(bankAccDTOMap.keySet()));
                List<BisAccDtlCopyDTO> listvo = new ArrayList<>();
                for (BisAccDtlDTO dto : dtlList) {
                    BtBankAccDTO acc = bankAccDTOMap.get(dto.getBankAcc());
                    dto.setPoolId(acc.getPoolId());
                    dto.setCorpName(acc.getCorpName());
                    dto.setCorpId(acc.getCorpId());
                    dto.setCurName(acc.getCurName());
                    //查询fc_bank_acc_dtl
                    List<String> dtlIds = new ArrayList<>();
                    dtlIds.add(dto.getId());//id相同
                    List<FcBankAccDtlDTO> fcBankAccDtlDTOList = tallyService.listFcBankAccDtlDTOByDtlIds(dtlIds, type, user.getId());
                    if (!CollectionUtils.isEmpty(fcBankAccDtlDTOList)) {
                        for (FcBankAccDtlDTO dtlDTO : fcBankAccDtlDTOList) {
                            dto.setId(dtlDTO.getId());
                            dto.setTallySign(dtlDTO.getTallyStatus());
                            dto.setAccDtlId(dtlDTO.getDtlId());
                        }

                        BisAccDtlCopyDTO bisAccDtlCopyDTO = new BisAccDtlCopyDTO();
                        BeanUtils.copyProperties(dto, bisAccDtlCopyDTO);
                        listvo.add(bisAccDtlCopyDTO);
                    }
                }
                output.setData(listvo);
                return output;
            }
        } else {
            return output;
        }
    }
    /***
     * 创建分录
     * @param key id
     * @param type 记账、改账、复核
     * @param menuType 归集、下拨
     * @return
     */
    public FcAccbookPageVO createAccbook(String key, String poolId, String curId, String type, String menuType, ICurrentUser user) {
        return processAccbook(key, poolId, curId, type, menuType, user);
    }
    /*
     *获取分录
     */
    public FcAccbookPageVO processAccbook(String key, String poolId, String curId, String type, String menuType, ICurrentUser user) {
        FcAccbookPageVO accbookPageVO = new FcAccbookPageVO();
        List<VoucherRenderControlDTO> controlBeans = new ArrayList<VoucherRenderControlDTO>();
        FcBusinessRuleDefinePoolDTO ruleDefine = null;
        if ("up".equals(menuType)) {
            ruleDefine = getBusinessRuleExp(SysRaceCode.NB_POOL_UP_PAY.get(), poolId, curId);
        } else {
            ruleDefine = getBusinessRuleExp(SysRaceCode.NB_POOL_DOWN_PAY.get(), poolId, curId);
        }
        int len = ruleDefine.getVouchers().size();
        if (len > 0) {
            controlBeans = getControlBeanEdit(len);
        }
        BisAccDtlDTO bisAccInfo = iBisAccDtlService.getById(key);//银行账号流水
        if (NbconsoleConstant.OpType.INIT.get().equals(type)) {
            accbookPageVO = processTallyAccbook(bisAccInfo, controlBeans, ruleDefine, menuType, user);
        } else {
            FcBankAccDtlDTO bankAccDtl = tallyService.getFcBankAccDtl(key);//银行账号流水记账存入表
            if (StringUtils.isEmpty(bankAccDtl.getBusGlobalId())) {
                throw new ParameterException(CollectMessageEnum.FIN_COLLECT_QUERY_BTNETCHALK_NULL);
            }
            FcAccbookDTO books = fcAccbookService.getBookVOByOrderNo(bankAccDtl.getOrderNo(), FinacialConstant.BOOLEAN_TRUE);
            FcAccbookVO accbook = new FcAccbookVO();
            if (!EmptyUtils.isEmpty(books)) {
                BeanUtils.copyNotNullProperties(books, accbook);
                accbook.setRaceName(books.getBusRaceName());
            }
            List<FcAccbookDtlDTO> bookDtls = fcAccbookService.getPoolPageDtlsByBookId(accbook.getId());
            FcAccbookExtDTO bookExt = fcAccbookService.getBookExtVOByBookId(accbook.getId());
            if (null == bookDtls || bookDtls.size() == 0) {
                throw new ParameterException(CollectMessageEnum.FIN_COLLECT_QUERY_ACCBOOKDTL_NULL);
            }
            for (int i = 0; i < bookDtls.size(); i++) {
                bookDtls.get(i).setControlVo(controlBeans.get(i));
            }
            accbookPageVO.setAccbook(accbook);
            accbookPageVO.setBookDtl(bookDtls);
            FcAccbookExtVO bookExtVO = new FcAccbookExtVO();
            BeanUtils.copyProperties(bookExt, bookExtVO);
            accbookPageVO.setBookExt(bookExtVO);
        }
        return accbookPageVO;
    }
    /**
     * 设置凭证信息
     */
    public FcAccbookPageVO processTallyAccbook(BisAccDtlDTO bisAccInfo, List<VoucherRenderControlDTO> controlBeans,
                                               FcBusinessRuleDefinePoolDTO ruleDefine, String menuType, ICurrentUser user) {
        //凭证传输对象，用于页面显示
        FcAccbookPageVO entity = new FcAccbookPageVO();
        //账务凭证保存对象
        FcAccbookVO accBook = new FcAccbookVO();
        FcAccbookExtVO bookExt = new FcAccbookExtVO();
        List<FcAccbookDtlDTO> accbookDtls = null;
        BtBankAccDTO btBankAccDTO = iBtBankAccService.getBankAccInfo(bisAccInfo.getBankAcc());
        FcBusRacePoolDTO fcbusRacePool = null;
        if ("up".equals(menuType)) {
            accbookDtls = getAccbookDtlsByUp(ruleDefine, controlBeans, bisAccInfo, user);
            fcbusRacePool = getFcBusRaceId(btBankAccDTO.getCorpId(), FinaBookConstant.SysRaceCode.NB_POOL_UP_PAY, user.getId());
        } else {
            accbookDtls = getAccbookDtlsByDown(ruleDefine, controlBeans, bisAccInfo, user);
            fcbusRacePool = getFcBusRaceId(btBankAccDTO.getCorpId(), FinaBookConstant.SysRaceCode.NB_POOL_DOWN_PAY, user.getId());
        }
        accBook.setRmk(bisAccInfo.getRemark());// 备注
        accBook.setEmergencySign(FinacialConstant.EMERGENCYSIGN_G);// 加急标志
        accBook.setInterestStartDate(bisAccInfo.getTransTime());
        accBook.setTallyDate(bisAccInfo.getTransTime());
        accBook.setVoucherNo(bisAccInfo.getVoucherNo());
        accBook.setBusRaceId(fcbusRacePool.getId());
        accBook.setRaceName(fcbusRacePool.getRaceName());

//        accBook.setOrderNo(bisAccInfo.getOrderNo());
//        accBook.setBusGlobalId(accbookDtls.get(1).getbus);

        accBook.setVoucherType(ruleDefine.getVoucherType());
        BtBankAccDTO oppBankAcc = iBtBankAccService.getBankAccInfo(bisAccInfo.getOppAccNo());
        bookExt.setExternalAcc(bisAccInfo.getOppAccNo());
        bookExt.setExternalCorp(oppBankAcc.getAccName());
        bookExt.setExternalBank(bisAccInfo.getOppAccBank());
        bookExt.setExternalProv(oppBankAcc.getProv());
        bookExt.setExternalCity(oppBankAcc.getCity());
        entity.setBookExt(bookExt);
        entity.setAccbook(accBook);
        entity.setBookDtl(accbookDtls);//ok
        return entity;
    }
    /**
     * 功能：记账时的会计分录构造
     */
    public List<FcAccbookDtlDTO> getAccbookDtlsByUp(FcBusinessRuleDefinePoolDTO ruleDefinePool, List<VoucherRenderControlDTO> controlBeans,
                                                    BisAccDtlDTO info, ICurrentUser user) {
        List<FcVoucherEntryDefinePoolDTO> entrys = ruleDefinePool.getVouchers();
        FcAccbookDtlDTO accbookDtl = null;
        this.collectionSort(entrys);
        List<FcAccbookDtlDTO> accbookDtls = new ArrayList<FcAccbookDtlDTO>();
        int balanceWay, groupNum;
        for (FcVoucherEntryDefinePoolDTO voucherEntry : entrys) {
            if (voucherEntry.getGroupNum() == null) {
                throw new ParameterException(PrecativeMessageEnum.CMS_PRECATIVE_GROUPNUM_IS_NULL);
            }
            accbookDtl = new FcAccbookDtlDTO();
            accbookDtl.setCreditMoney(BigDecimal.ZERO);
            accbookDtl.setDebitMoney(BigDecimal.ZERO);
            if (null == voucherEntry.getSubjectCode()) {
                throw new ParameterException(PrecativeMessageEnum.CMS_PRECATIVE_SUBJECT_IS_NULL);
            }
            balanceWay = voucherEntry.getBalanceWay().intValue();
            groupNum = voucherEntry.getGroupNum().intValue();
            VoucherRenderControlDTO bean = controlBeans.get(groupNum - 1);
            /**
             * 借：资金池活期存款-下级成员单位
             * 贷：其他应付款-本方单位
             */
            if (Integer.valueOf(FinacialConstant.BALANCE_WAY_DEBIT).intValue() == balanceWay) {
                String countertAcc = iBtBankAccService.getBlnctrAcc(info.getOppAccNo(), info.getCurId());
                if (null != countertAcc) {
                    FcAccDTO fcacc = iFcAccService.getAccDTOByAccount(countertAcc);
                    accbookDtl.setAccount(countertAcc);
                    accbookDtl.setAccName(fcacc.getAccName());
                } else {
                    throw new ParameterException(CollectMessageEnum.FIN_COLLECT_RELATION_BANKACC_FCACC, info.getOppAccNo());
                }
                bean.setAccountEditable(false);
                accbookDtl.setDebitMoney(info.getAmt());
            } else if (Integer.valueOf(FinacialConstant.BALANCE_WAY_CREDIT).intValue() == balanceWay) {
                bean.setAccountEditable(true);
                accbookDtl.setCreditMoney(info.getAmt());
            }
            accbookDtl.setControlVo(bean);
            accbookDtl.setSubjectCode(voucherEntry.getSubjectCode());
            accbookDtl.setCurCode(info.getCurId());
            accbookDtl.setOrigVoucherNo(info.getAccNo());
            accbookDtl.setOrigVoucherId(info.getId());
            if (EmptyUtils.isEmpty(info.getRemark())) {//明细摘要为空取记账的摘要
                accbookDtl.setSummaryContent(voucherEntry.getSummary());//记账摘要
            } else {
                accbookDtl.setSummaryContent(info.getRemark());//明细摘要
            }
            accbookDtls.add(accbookDtl);
        }
        return accbookDtls;
    }
    /**
     * 功能：记账时的会计分录构造
     */
    public List<FcAccbookDtlDTO> getAccbookDtlsByDown(FcBusinessRuleDefinePoolDTO ruleDefinePool, List<VoucherRenderControlDTO> controlBeans,
                                                      BisAccDtlDTO info, ICurrentUser user) {
        List<FcVoucherEntryDefinePoolDTO> entrys = ruleDefinePool.getVouchers();
        FcAccbookDtlDTO accbookDtl = null;
        this.collectionSort(entrys);
        List<FcAccbookDtlDTO> accbookDtls = new ArrayList<FcAccbookDtlDTO>();
        int balanceWay, groupNum;
        for (FcVoucherEntryDefinePoolDTO voucherEntry : entrys) {
            if (voucherEntry.getGroupNum() == null) {
                throw new ParameterException(PrecativeMessageEnum.CMS_PRECATIVE_GROUPNUM_IS_NULL);
            }
            accbookDtl = new FcAccbookDtlDTO();
            accbookDtl.setCreditMoney(BigDecimal.ZERO);
            accbookDtl.setDebitMoney(BigDecimal.ZERO);
            if (null == voucherEntry.getSubjectCode()) {
                throw new ParameterException(PrecativeMessageEnum.CMS_PRECATIVE_SUBJECT_IS_NULL);
            }
            //if (Integer.valueOf(FinacialConstant.ENTRY_TYPE_10).intValue() == voucherEntry.getEntryType().intValue()) {
            balanceWay = voucherEntry.getBalanceWay().intValue();
            groupNum = voucherEntry.getGroupNum().intValue();
            VoucherRenderControlDTO bean = controlBeans.get(groupNum - 1);
            /**
             * 借：其他应付-本方单位
             * 贷：资金池活期存款-下级成员单位
             */
            if (Integer.valueOf(FinacialConstant.BALANCE_WAY_DEBIT).intValue() == balanceWay) {
                bean.setAccountEditable(true);
                accbookDtl.setDebitMoney(info.getAmt());
            } else if (Integer.valueOf(FinacialConstant.BALANCE_WAY_CREDIT).intValue() == balanceWay) {
                bean.setAccountEditable(false);
                accbookDtl.setCreditMoney(info.getAmt());
                String countertAcc = iBtBankAccService.getBlnctrAcc(info.getOppAccNo(), info.getCurId());
                if (null != countertAcc) {
                    FcAccDTO fcacc = iFcAccService.getAccDTOByAccount(countertAcc);
                    accbookDtl.setAccount(countertAcc);
                    accbookDtl.setAccName(fcacc.getAccName());
                } else {
                    throw new ParameterException(CollectMessageEnum.FIN_COLLECT_RELATION_BANKACC_FCACC, info.getOppAccNo());
                }
            }
            accbookDtl.setControlVo(bean);
            accbookDtl.setSubjectCode(voucherEntry.getSubjectCode());
            accbookDtl.setCurCode(info.getCurId());
            accbookDtl.setOrigVoucherNo(info.getAccNo());
            accbookDtl.setOrigVoucherId(info.getId());
            if (EmptyUtils.isEmpty(info.getRemark())) {//明细摘要为空取记账的摘要
                accbookDtl.setSummaryContent(voucherEntry.getSummary());//记账摘要
            } else {
                accbookDtl.setSummaryContent(info.getRemark());//明细摘要
            }
            accbookDtls.add(accbookDtl);
            //}
        }
        return accbookDtls;
    }
    /**
     * 获取业务总类id
     */
    public FcBusRacePoolDTO getFcBusRaceId(String poolId, ConstantBean<String> sysBean, String userId) {
        List<FcBusRacePoolDTO> raceList = iFcBusRaceService.findByPoolSysRaceCode(sysBean.get());
        FcBusRacePoolDTO tempRace = null;
        for (FcBusRacePoolDTO busRace : raceList) {
            String poolId1 = busRace.getPoolId();
            if (!StringUtils.isEmpty(poolId1) && poolId.equals(poolId1)) {
                tempRace = busRace;
                break;
            }
        }
        if (null == tempRace) {
            tempRace = new FcBusRacePoolDTO();
            tempRace.setPoolId(poolId);
            tempRace.setRaceCode(sysBean.get());
            tempRace.setRaceName(sysBean.getName());
            tempRace.setSysRaceCode(sysBean.get());
            iFcBusRaceService.saveRacePool(tempRace, new String[]{poolId}, userId);
            raceList = iFcBusRaceService.findByPoolSysRaceCode(sysBean.get());
            tempRace = raceList.get(0);
        }
        return tempRace;
    }
    /**
     * 根据系统凭证种类查询业务规则
     */
    public FcBusinessRuleDefinePoolDTO getBusinessRuleExp(String sysRaceCode, String poolId, String curId) {
        FcBusinessRuleDefinePoolDTO entity = iFcBusinessRuleDefineService.getBusinessRulePoolExp(poolId, curId, sysRaceCode);
        return entity;
    }
    /**
     * 元素编辑控制
     */
    public List<VoucherRenderControlDTO> getControlBeanEdit(int size) {
        List<VoucherRenderControlDTO> controBeans = new ArrayList<VoucherRenderControlDTO>();
        VoucherRenderControlDTO controBean = null;
        for (int i = 0; i < size; i++) {
            controBean = new VoucherRenderControlDTO();
            controBean.setSummaryEditable(true);
            controBean.setVoucherTypeEditable(true);
            controBean.setAccountEditable(false);
            controBeans.add(controBean);
        }
        return controBeans;
    }
    /**
     * 功能：对业务规则排序
     */
    public void collectionSort(List<FcVoucherEntryDefinePoolDTO> entrys) {
        Collections.sort(entrys, new Comparator<FcVoucherEntryDefinePoolDTO>() {
            @Override
            public int compare(FcVoucherEntryDefinePoolDTO o1, FcVoucherEntryDefinePoolDTO o2) {
                return o1.getGroupNum().compareTo(o2.getGroupNum());
            }
        });
    }
    /**
     * 暂存FC_ACCBOOK
     */
    public void save(FcAccbookVoucherDTO fcAccbook, String id, String menuType, String accDtlId, String userId) {
        check(fcAccbook);
        FcBankAccDtlDTO dtl = tallyService.findByDtlId(id);
        if (dtl != null) {
            validata(dtl.getOrderNo());
        }
        setAccBookVoucher(fcAccbook, menuType);
        VoucherWrapperDTO voucher = setVoucherWrapper(fcAccbook, userId);
        fcAccbookService.saveFcAccbook(voucher);
        FcAccbookDTO accbook = voucher.getTarget().get(0).getAccbook();
        if (StringUtils.isEmpty(accbook.getOperatorCode())){
            accbook.setOperatorCode(userId);
        }
        BisAccDtlDTO bisAccDtl = iBisAccDtlService.getById(id);
        BtBankAccDTO bankAccDTO = iBtBankAccService.getBankAccInfo(bisAccDtl.getBankAcc());//本方账号
        BtBankAccDTO oppAccDTO = iBtBankAccService.getBankAccInfo(bisAccDtl.getOppAccNo());//对方账号
        BtBankAccDTO parentAcc = iBtBankAccService.getParentAccByAccId(oppAccDTO.getId(), bisAccDtl.getCurId());//查询对方账号的父账号
        FcCorpPoolDTO corpPool = null;
        if (bisAccDtl.getBankAcc().equals(parentAcc.getBankAcc())) {//对方账号父账号和本方账号对比
            //本方账号corpId,对方账号corpId
            corpPool = corpPoolService.getInfoByPoolIdCorpId(bankAccDTO.getCorpId(), oppAccDTO.getCorpId());
        } else {
            //对方账号corpId,本方账号corpId
            corpPool = corpPoolService.getInfoByPoolIdCorpId(oppAccDTO.getCorpId(), bankAccDTO.getCorpId());
        }
        String tallyStatus = NbconsoleConstant.NbcEstate.E_STATE_TEMP.getValue();

        FcBankAccDtlDTO bankAccDtl = null;
        if (StringUtils.isNotBlank(accDtlId)){
            bankAccDtl = tallyService.getFcBankAccDtl(accDtlId);
        }
        bankAccDtl = setAccDtlEntity(id, corpPool, accbook, tallyStatus, bankAccDtl);
        saveOrUpdate(bankAccDtl);
        notificationService.complete(NotificationConstant.FINACIAL.POOL_TALLY, bisAccDtl.getId(), userId);
    }
    /**
     * 功能介绍 : 保存FC_ACCBOOK
     */
    public void submit(FcAccbookVoucherDTO fcAccbook, String id, String menuType, String accDtlId, ICurrentUser user) {
        check(fcAccbook);
        setAccBookVoucher(fcAccbook, menuType);
        VoucherWrapperDTO voucher = setVoucherWrapper(fcAccbook, user.getId());
        validata(voucher.getTarget().get(0).getAccbook().getOrderNo());
        FcAccbookDTO accbook=fcAccbookService.submitFcAccbook(voucher);
        voucher.getTarget().get(0).setAccbook(accbook);
//        FcAccbookDTO accbook = voucher.getTarget().get(0).getAccbook();
        if (StringUtils.isEmpty(accbook.getOperatorCode())){
            accbook.setOperatorCode(user.getId());
        }
        FcBankAccDtlDTO bankAccDtl = null;
        if (EmptyUtils.isNotEmpty(accDtlId)) {
            bankAccDtl = tallyService.getFcBankAccDtl(accDtlId);
            if (EmptyUtils.isEmpty(bankAccDtl)) {
                bankAccDtl = tallyService.getFcBankAccDtl(id);
            }
        } else {
            bankAccDtl = tallyService.getFcBankAccDtl(id);
        }
        BisAccDtlDTO bisAccDtl = iBisAccDtlService.getById(bankAccDtl.getDtlId());
        FcCorpPoolDTO corpPool = null;
        if (bisAccDtl != null) {
            BtBankAccDTO bankAccDTO = iBtBankAccService.getBankAccInfo(bisAccDtl.getBankAcc());//本方账号
            BtBankAccDTO oppAccDTO = iBtBankAccService.getBankAccInfo(bisAccDtl.getOppAccNo());//对方账号
            BtBankAccDTO parentAcc = iBtBankAccService.getParentAccByAccId(oppAccDTO.getId(), bisAccDtl.getCurId());//查询对方账号的父账号
            if (bisAccDtl.getBankAcc().equals(parentAcc.getBankAcc())) {//对方账号父账号和本方账号对比
                //本方账号corpId,对方账号corpId
                corpPool = corpPoolService.getInfoByPoolIdCorpId(bankAccDTO.getCorpId(), oppAccDTO.getCorpId());
            } else {
                //对方账号corpId,本方账号corpId
                corpPool = corpPoolService.getInfoByPoolIdCorpId(oppAccDTO.getCorpId(), bankAccDTO.getCorpId());
            }
        }
        String tallyStatus = NbconsoleConstant.NbcEstate.E_STATE_WAIT_CHECK.getValue();
        bankAccDtl = setAccDtlEntity(id, corpPool, accbook, tallyStatus, bankAccDtl);
        boolean isAutoCheck = this.isAutoCheck();
        if (isAutoCheck) {
            //如果自动复核，则复核凭证通过
            completeEditNotificationInfo(voucher, user);
            completeCheckNotificationInfo(voucher, user);
            saveOrUpdate(bankAccDtl);
            //获取自动复核员用户
            setCheckUser(voucher);
            checkFcAccbook(voucher, bankAccDtl.getId(), user);
        } else {
            sendCheckNotificationInfo(voucher, user);
            saveOrUpdate(bankAccDtl);
        }
        if(bisAccDtl!=null){
            notificationService.complete(NotificationConstant.FINACIAL.POOL_TALLY, bisAccDtl.getId(), user.getId());
        }else {
            notificationService.complete(NotificationConstant.FINACIAL.POOL_TALLY, null, user.getId());
        }

    }
    /**
     * 构造复核传输对象
     */
    public void createCheckVoucher(FcAccbookVoucherDTO fcAccbook, String id, ICurrentUser user, String menuType) {
        createVoucher(fcAccbook, menuType);
        VoucherWrapperDTO Voucher = setVoucherWrapper(fcAccbook, user.getId());
        checkFcAccbook(Voucher, id, user);
    }
    /***
     * 复核
     */
    public void checkFcAccbook(VoucherWrapperDTO Voucher, String id, ICurrentUser user) {
        FcBankAccDtlDTO entity = tallyService.getFcBankAccDtl(id);
        validata(entity.getOrderNo());
        validataBack(entity.getOrderNo());
        fcAccbookService.checkFcAccbook(Voucher);
        FcAccbookDTO accbook = Voucher.getTarget().get(0).getAccbook();
        entity.setVoucherNo(accbook.getVoucherNo());
        entity.setCheckCode(accbook.getCheckerCode());
        entity.setCheckDate(accbook.getCheckTime());
        entity.setTallyStatus(NbconsoleConstant.NbcEstate.E_STATE_CHECK_PASS.getValue());
        completeEditNotificationInfo(Voucher, user);
        completeCheckNotificationInfo(Voucher, user);
        update(entity);
    }
    /***
     * 打回
     */
    public void backFcAccbook(FcAccbookVoucherDTO fcAccbook, String id, String menuType, ICurrentUser user) {
        createVoucher(fcAccbook, menuType);
        String opType = fcAccbook.getOpType().name();
        FcBankAccDtlDTO entity = tallyService.getFcBankAccDtl(id);
        VoucherWrapperDTO Voucher = setVoucherWrapper(fcAccbook, user.getCorpId());
        //Voucher.getTarget().get(0).getAccbook().setId(entity.getBookId());
        completeEditNotificationInfo(Voucher, user);
        completeCheckNotificationInfo(Voucher, user);
        if ("MODIFY".equals(opType)) {
            validata(entity.getOrderNo());
            entity.setOrderNo(null);
            entity.setBusGlobalId(null);
            entity.setTallyStatus(NbconsoleConstant.NbcEstate.E_STATE_CHECK.getValue());
            fcAccbookService.deleteFcAccbook(Voucher);
            Map<String, Object> map = new HashMap<String, Object>();
            BisAccDtlDTO bisAccInfo = iBisAccDtlService.getById(entity.getDtlId());//银行账号流水
            map.put("money", NumberUtil.formatNumeric(bisAccInfo.getBal(), "#,##0.00"));
            notificationService.sendToUserByResPermission(entity.getId(), ResourceConstant.FCL_POOL_TALLY_APPLY, NotificationConstant.FINACIAL.POOL_TALLY, map, entity.getPoolId());
        } else if ("CHECK".equals(opType)) {
            validataBack(entity.getOrderNo());
            entity.setTallyStatus(NbconsoleConstant.NbcEstate.E_STATE_PASS_BACK.getValue());
            fcAccbookService.backFcAccbook(Voucher);
            sendBackNotificationInfo(Voucher);
        }
        this.update(entity);
    }
    public FcBankAccDtlDTO setAccDtlEntity(String id, FcCorpPoolDTO poolDTO, FcAccbookDTO accbook, String status, FcBankAccDtlDTO entity) {
        if (entity == null) {
            entity = new FcBankAccDtlDTO();
            entity.setDtlId(id);
            if (poolDTO != null) {
                entity.setPoolId(poolDTO.getId());
                entity.setCorpId(poolDTO.getCorpId());
            }
        }
        entity.setOrderNo(accbook.getOrderNo());
        entity.setVoucherNo(accbook.getVoucherNo());
        entity.setBusGlobalId(accbook.getBusGlobalId());
        entity.setTallyCode(accbook.getOperatorCode());
        entity.setTallyDate(accbook.getTallyDate());
        entity.setTallyStatus(status);
        return entity;
    }
    /**
     * 功能介绍 : 初始化手工记账凭证
     */
    private void setAccBookVoucher(FcAccbookVoucherDTO voucher, String menuType) {
        createVoucher(voucher, menuType);
        List<FcAccbookDtlDTO> bookDtls = new ArrayList<FcAccbookDtlDTO>();
        // 初始化数据
        for (FcAccbookDtlDTO bookDtl : voucher.getBookDtls()) {
            if (null == bookDtl.getDebitMoney()) {
                bookDtl.setDebitMoney(BigDecimal.ZERO);
            }
            if (null == bookDtl.getCreditMoney()) {
                bookDtl.setCreditMoney(BigDecimal.ZERO);
            }
            // 如果明细数据为空，则不保存非空明细
            if (StringUtils.isEmpty(bookDtl.getSummaryContent()) && StringUtils.isEmpty(bookDtl.getAccount())
                    && null == bookDtl.getDebitMoney() && null == bookDtl.getCreditMoney()) {
            } else {
                bookDtls.add(bookDtl);
            }
        }
        // 明细进行先借后贷的排序
        Collections.sort(bookDtls, new Comparator<FcAccbookDtlDTO>() {
            public int compare(FcAccbookDtlDTO o1, FcAccbookDtlDTO o2) {
                return o1.getCreditMoney().abs().compareTo(BigDecimal.ZERO);
            }
        });
        voucher.setBookDtls(bookDtls);
    }
    //凭证封装对象
    private VoucherWrapperDTO setVoucherWrapper(FcAccbookVoucherDTO fcAccbook, String userId) {
        VoucherWrapperDTO voucher = new VoucherWrapperDTO();
        voucher.setBusGlobalId(fcAccbook.getAccbook().getBusGlobalId());
        voucher.addTarget(fcAccbook);
        voucher.setProcessType(ProcessType.PROCESS_COMMON);
        voucher.setUserCode(userId);//设置用户
        voucher.setAutoAccounter(false);
        return voucher;
    }
    private void createVoucher(FcAccbookVoucherDTO voucher, String menuType) {
        voucher.getAccbook().setBusOriType(getOriType(menuType));
    }
    /**
     * 返回记账的业务种类
     */
    public Integer getOriType(String menuType) {
        if ("up".equals(menuType)) {
            return FinaBookConstant.BusOriType.NB_POOL_UP_PAYMENT.get();
        } else {
            return FinaBookConstant.BusOriType.NB_POOL_DOWN_PAYMENT.get();
        }
    }
    /**
     * 判断是否自动复核
     */
    public boolean isAutoCheck() {
        boolean isAutoCheck = CmsConstant.IS_TRUE.equals(configService.getConfig("fcl", "tally_check").getValue());
        return isAutoCheck;
    }
    /**
     * 发送消息提醒
     */
    private void sendCheckNotificationInfo(VoucherWrapperDTO voucher, ICurrentUser user) {
        for (FcAccbookVoucherDTO voucherDTO : voucher.getTarget()) {
            FcAccbookDTO accbook = voucherDTO.getAccbook();
            // 发送消息复核提醒
            FcAccbookNotifyVO notifyVO = new FcAccbookNotifyVO();
            notifyVO.setOrderNo(accbook.getOrderNo());
            List<FcAccbookDtlDTO> bookDtls = voucherDTO.getBookDtls();
            BigDecimal money = BigDecimal.ZERO;
            if (!EmptyUtils.isEmptyList(bookDtls)) {
                for (FcAccbookDtlDTO accbookDtl : bookDtls) {
                    if (null != accbookDtl.getDebitMoney()) {
                        money = money.add(accbookDtl.getDebitMoney());
                    }
                }
            }
            notifyVO.setMoney(money);
            FcCorpPoolDTO poolDTO = corpPoolService.getById(accbook.getNetId());
            if(poolDTO==null){
                notificationService.sendIgnoreMySelf(accbook.getId(), null,//需要确定是传资金池的id还是t2的corpId
                        NotificationConstant.FINACIAL.POOL_CHECK, notifyVO, user.getId());
            }else {
                notificationService.sendIgnoreMySelf(accbook.getId(), poolDTO.getCorpId(),//需要确定是传资金池的id还是t2的corpId
                        NotificationConstant.FINACIAL.POOL_CHECK, notifyVO, user.getId());
            }
        }
    }
    /**
     * 发送消息提醒-打回
     */
    private void sendBackNotificationInfo(VoucherWrapperDTO voucher) {
        for (FcAccbookVoucherDTO voucherVO : voucher.getTarget()) {
            FcAccbookDTO accbook = voucherVO.getAccbook();
            // 打回改账处，通知提醒记账人
            FcAccbookNotifyVO notifyVO = new FcAccbookNotifyVO();
            notifyVO.setOrderNo(accbook.getOrderNo());
            List<FcAccbookDtlDTO> bookDtls = voucherVO.getBookDtls();
            BigDecimal money = BigDecimal.ZERO;
            if (!EmptyUtils.isEmptyList(bookDtls)) {
                for (FcAccbookDtlDTO accbookDtl : bookDtls) {
                    if (null != accbookDtl.getDebitMoney()) {
                        money = money.add(accbookDtl.getDebitMoney());
                    }
                }
            }
            notifyVO.setMoney(money);
            notificationService.sendToUser(accbook.getId(), accbook.getOperatorCode(),
                    NotificationConstant.FINACIAL.POOL_EDIT, notifyVO);
        }
    }
    /**
     * 发送编辑消息
     */
    private void completeEditNotificationInfo(VoucherWrapperDTO voucher, ICurrentUser user) {
        for (FcAccbookVoucherDTO voucherDTO : voucher.getTarget()) {
            FcAccbookDTO accbook = voucherDTO.getAccbook();
            notificationService.complete(NotificationConstant.FINACIAL.POOL_EDIT, accbook.getId(), user.getId());
        }
    }
    /**
     * 消息完成
     */
    private void completeCheckNotificationInfo(VoucherWrapperDTO voucher, ICurrentUser user) {
        for (FcAccbookVoucherDTO voucherDTO : voucher.getTarget()) {
            FcAccbookDTO accbook = voucherDTO.getAccbook();
            notificationService.complete(NotificationConstant.FINACIAL.POOL_CHECK, accbook.getId(), user.getId());
        }
    }
    /**
     * 设置自动复核人
     */
    public void setCheckUser(VoucherWrapperDTO voucher) {
        String netCode = voucher.getNetId();//记账网点
        if (!EmptyUtils.isEmpty(netCode)) {
            voucher.setUserCode(getAutoCheckUser(netCode));
            return;
        }
        FcAccbookVoucherDTO voucherVO = voucher.getBookVoucher();
        if (!EmptyUtils.isEmpty(voucherVO)) {
            netCode = voucherVO.getNetCode();//记账网点
            if (!EmptyUtils.isEmpty(netCode)) {
                voucher.setUserCode(getAutoCheckUser(netCode));
                return;
            }
            FcAccbookDTO accbook = voucherVO.getAccbook();
            netCode = accbook.getNetId();//记账网点
            if (!EmptyUtils.isEmpty(netCode)) {
                voucher.setUserCode(getAutoCheckUser(netCode));
                return;
            }
            List<FcAccbookDtlDTO> accbookDtls = voucherVO.getBookDtls();
            if (!EmptyUtils.isEmptyList(accbookDtls)) {
                FcAccbookDtlDTO accbookDtl = accbookDtls.get(0);
                String account = accbookDtl.getAccount();
                FcAccDTO accDTO = iFcAccService.getAccDTOByAccount(account);
                netCode = accDTO.getNetId();//账号网点
                if (!EmptyUtils.isEmpty(netCode)) {
                    voucher.setUserCode(getAutoCheckUser(netCode));
                }
            }
        }
    }
    /**
     * 获取网点自动记账人
     */
    public String getAutoCheckUser(String netId) {
        ConfigDTO configDTO = configService.getConfig(ConfigConstant.FCL, "autoChecker");
        String paramValue = configDTO.getValue();
        if (EmptyUtils.isEmpty(paramValue)) {//自动记账人为空
            throw new ParameterException(AccbookMessageEnum.FIN_BOOK_TALLY_AUTOCHECKER_NULL);
        }
        paramValue = paramValue.replaceAll("；", ";").replaceAll("，", ";").replaceAll(",", ";");
        String[] netUsers = paramValue.split(";");//分割
        Map<String, String> userInfo = new HashMap<String, String>();
        for (String netUser : netUsers) {
            String[] temps = netUser.split("=");//'='分割为网点和记账人
            if (temps.length == 2) {
                userInfo.put(temps[0].trim(), temps[1].trim());
            }
        }
        FcCorpPoolDTO corpDTO = corpPoolService.getById(netId);
        String userCode = userInfo.get(corpDTO.getCorpCode());//获取复核人
        String poolCode = userInfo.get(corpDTO.getPoolCode());
        if (EmptyUtils.isEmpty(poolCode)) {
            throw new ParameterException(AccbookMessageEnum.FIN_BOOK_POOL_TALLY_AUTOCHECKER_NULL);
        }
        UserDTO userDTO = userService.getByLoginName(poolCode);
        if (null != userDTO) {
            return userDTO.getId();
        }
        throw new ParameterException(AccbookMessageEnum.FIN_BOOK_TALLY_USER_NOTEXIST,
                poolCode);
    }
    /**
     * 提交前数据校验
     */
    private void check(FcAccbookVoucherDTO fcAccbook) {
        if (EmptyUtils.isEmpty(fcAccbook.getAccbook().getVoucherType())) {
            throw new ParameterException(AccbookMessageEnum.FIN_BOOK_TALLY_VOUCHERTYPE_NO);
        }
        if (EmptyUtils.isEmpty(fcAccbook.getAccbook().getInterestStartDate())) {
            throw new ParameterException(AccbookMessageEnum.FIN_BOOK_TALLY_INTERESTSTARTDATE_NO);
        }
        if (EmptyUtils.isEmpty(fcAccbook.getAccbook().getTallyDate())) {
            throw new ParameterException(AccbookMessageEnum.FIN_BOOK_TALLY_TALLYDATE_NO);
        }
        if (EmptyUtils.isEmpty(fcAccbook.getAccbook().getEmergencySign())) {
            throw new ParameterException(AccbookMessageEnum.FIN_BOOK_TALLY_EMERGENCYSIGN_NO);
        }
        if (EmptyUtils.isEmpty(fcAccbook.getAccbook().getBusRaceId())) {
            throw new ParameterException(AccbookMessageEnum.FIN_BOOK_TALLY_BUSRACE_NO);
        }
        for (FcAccbookDtlDTO accbookDtl : fcAccbook.getBookDtls()) {
            if (EmptyUtils.isEmpty(accbookDtl.getAccount())) {
                throw new ParameterException(AccbookMessageEnum.FIN_BOOK_TALLY_ACCOUNT_NO);
            }
            if (EmptyUtils.isEmpty(accbookDtl.getSummaryContent())) {
                throw new ParameterException(AccbookMessageEnum.FIN_BOOK_TALLY_SUMMARYCONTENT_NO);
            }
        }
    }
    /**
     * 更新
     *
     * @param entity
     */
    public void update(FcBankAccDtlDTO entity) {
        tallyService.updateFcBankAccDtlEntity(entity);
    }
    /**
     * 保存or更新
     *
     * @param entity
     */
    public void saveOrUpdate(FcBankAccDtlDTO entity) {
        tallyService.fcBankAccDtlSaveOrUpdate(entity);
    }
    private void validata(String orderNo) {
        if (EmptyUtils.isNotEmpty(orderNo)) {
            FcBankAccDtlDTO bankAccDtl = tallyService.findByOrderNo(orderNo);
            if (bankAccDtl != null) {
                if (NbconsoleConstant.NbcEstate.E_STATE_CHECK_PASS.equals(bankAccDtl.getTallyStatus())) {//复核通过
                    throw new ParameterException(CollectMessageEnum.FIN_COLLECT_MANUAL_ISPASS);
                }
            }
        }
    }
    private void validataBack(String orderNo) {
        if (EmptyUtils.isNotEmpty(orderNo)) {
            FcBankAccDtlDTO bankAccDtl = tallyService.findByOrderNo(orderNo);
            if (bankAccDtl != null) {
                if (EmptyUtils.isNotEmpty(bankAccDtl.getTallyStatus()) &&
                        (bankAccDtl.getTallyStatus().equals(NbconsoleConstant.NbcEstate.E_STATE_PASS_BACK) || //复核打回
                                bankAccDtl.getTallyStatus().equals(NbconsoleConstant.NbcEstate.E_STATE_CHECK))) {//待记账
                    throw new ParameterException(CollectMessageEnum.FIN_COLLECT_MANUAL_ISBACK);
                }
            }
        }
    }
    /**
     * 待记账数据
     */
    public List<BisAccDtlCopyDTO> queryTallyBisAccDtl(String menuType) {
        return tallyService.queryTallyBisAccDtl(menuType);
    }
}
