package com.bytter.tally.service;
import com.bytter.biz.finacial.api.constant.FinaBookConstant;
import com.bytter.biz.finacial.api.constant.FinaBookConstant.SysRaceCode;
import com.bytter.biz.finacial.api.constant.FinacialConstant;
import com.bytter.biz.finacial.api.constant.NbconsoleConstant;
import com.bytter.biz.finacial.api.constant.ResourceConstant;
import com.bytter.biz.finacial.api.dto.*;
import com.bytter.biz.finacial.api.enums.accbook.ProcessType;
import com.bytter.biz.finacial.api.enums.bankCollect.CollectMessageEnum;
import com.bytter.biz.finacial.api.service.*;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.core.common.context.UserContextHelper;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.exception.ParameterException;
import com.bytter.core.constant.ConstantBean;
import com.bytter.core.utils.*;
import com.bytter.finance.constant.CmsConstant;
import com.bytter.system.api.*;
import com.bytter.system.api.constant.CorpVerifyMessageConstant;
import com.bytter.system.api.constant.NotificationConstant;
import com.bytter.system.api.constant.SysConfigConstant;
import com.bytter.system.api.dto.CorpDTO;
import com.bytter.system.api.dto.CorpVerifyErrorDTO;
import com.bytter.system.api.dto.FcAccDTO;
import com.bytter.tally.dto.FcAccbookPageDTO;
import com.bytter.tally.entity.TallyFcUnionAccbook;
import com.bytter.tally.vo.*;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.*;
/**
 * @author lixi
 * @version 1.0
 * @Title: Service
 * @Description: 网点往来账务信息表
 * @date 2017-07-13 10:02:21
 */
@Service("fcUnionAccbookService")
@Transactional
public class FcUnionAccbookService implements ICorpDisableVerifyService {
    @DubboReference(check = false)
    private ITallyService iTallyService;
    @DubboReference(check = false)
    private IFcAccService fcAccService;
    @DubboReference
    private IFcAccbookService iFcAccbookService;
    @DubboReference
    private IFcBusRaceService iFcBusRaceService;
    @DubboReference(check = false)
    private IFcBusinessRuleDefineService fcBusinessRuleDefineService;
    @DubboReference(check = false)
    private INotificationService notificationService;
    @DubboReference(check = false)
    private IConfigService configService;
    @Resource
    private IMultiLangService multiLangServiceI;
    @DubboReference(check = false)
    private ICorpService iCorpService;
    @DubboReference
    private IFcCashItemService iFcCashItemService;
    @DubboReference
    private IFcAccbookDtlService fcAccBookDtlService;
    /**
     * 保存
     *
     * @param entity 新增参数
     */
    public void save(TallyFcUnionAccbook entity) {
        FcUnionAccbookDTO unionAccBookDto = new FcUnionAccbookDTO();
        BeanUtils.copyNotNullProperties(entity, unionAccBookDto);
        iTallyService.saveUnionAccBook(unionAccBookDto);
    }
    /**
     * 更新
     *
     * @param entity 更新参数
     */
    public void update(TallyFcUnionAccbook entity) {
        FcUnionAccbookDTO unionAccBookDto = new FcUnionAccbookDTO();
        BeanUtils.copyNotNullProperties(entity, unionAccBookDto);
        iTallyService.updateUnionAccEntity(unionAccBookDto);
    }
    /**
     * 查询
     *
     * @param id 凭证id
     * @return 网点往来账务信息集合数据
     */
    @Transactional(readOnly = true)
    public List<TallyFcUnionAccbook> queryFcUnionAccbook(String id) {
        List<FcUnionAccbookDTO> unionAccBookList = iTallyService.queryFcUnionAccBook(id);
        return BeanUtils.copyListProperties(unionAccBookList, TallyFcUnionAccbook.class);
    }
    //查询数据
    @SuppressWarnings("rawtypes")
    @Transactional(readOnly = true)
    public DataTablesOutput queryFcUnionAccbook(DataTablesInput dataTablesInput, String type, String userId) {
        return iTallyService.queryFcUnionAccBook(dataTablesInput, type, userId);
    }
    /**
     * 构建凭证
     */
    public FcAccbookPageDTO createAccbook(String id, String opType, ICurrentUser user) {
        return processAccbook(id, opType, user);
    }
    /**
     * 创建凭证信息
     */
    public FcAccbookPageDTO processAccbook(String id, String opType, ICurrentUser user) {
        FcAccbookPageDTO entity = new FcAccbookPageDTO();
        List<TallyFcUnionAccbook> fcUnionAccBooks = queryFcUnionAccbook(id);
        FcUnionAccbookDTO fcUnionAccBookVo = new FcUnionAccbookDTO();
        BeanUtils.copyNotNullProperties(fcUnionAccBooks.get(0), fcUnionAccBookVo);
        if (EmptyUtils.isEmpty(fcUnionAccBookVo)) {
            throw new ParameterException(CollectMessageEnum.FIN_COLLECT_QUERY_BTNETCHALK_NULL);
        }
        VoucherRenderControlDTO controlBean = new VoucherRenderControlDTO();
        controlBean.setExternalAccEditable(false);
        controlBean.setExternalCorpEditable(false);
        controlBean.setBankEditable(false);
        controlBean.setCityEditable(false);
        controlBean.setProvinceEditable(false);
        controlBean.setSummaryEditable(false);
        List<VoucherRenderControlDTO> controlBeans = getControlBeanEdit(SysRaceCode.NB_COLLECTION_NET.get(), 2);
        if (NbconsoleConstant.OpType.INIT.get().equals(opType)) {
            FcBusinessRuleDefineDTO ruleDefine = getBusinessRuleExp(SysRaceCode.NB_COLLECTION_NET.get());
            entity = processTallyAccbook(fcUnionAccBookVo, controlBean, controlBeans, ruleDefine, user);
        } else {
            if (StringUtils.isEmpty(fcUnionAccBookVo.getReceiveBusGlobalId())) {
                throw new ParameterException(CollectMessageEnum.FIN_COLLECT_QUERY_BTNETCHALK_NULL);
            }
            FcAccbookDTO accbook = iFcAccbookService.getBookVOByOrderNo(fcUnionAccBookVo.getReceiveOrder(), FinacialConstant.BOOLEAN_FALSE);
            accbook.setEmergencySign(FinacialConstant.EMERGENCYSIGN_G);//紧急标志
            accbook.setBusGlobalId(fcUnionAccBookVo.getReceiveBusGlobalId());
            List<FcAccbookDtlDTO> bookDtls = fcAccBookDtlService.getBookDtlsByBusGlobalId(fcUnionAccBookVo.getReceiveBusGlobalId());
            if (null == bookDtls || bookDtls.size() == 0) {
                throw new ParameterException(CollectMessageEnum.FIN_COLLECT_QUERY_ACCBOOKDTL_NULL);
            }
            VoucherRenderControlDTO bean = new VoucherRenderControlDTO();
            if (NbconsoleConstant.OpType.EDIT.get().equals(opType)) {
                controlBean.setSummaryEditable(true);
                bean.setSummaryEditable(true);
            }
            bookDtls.get(0).setControlVo(controlBean);
            bookDtls.get(1).setControlVo(bean);
            if (StringUtils.isNotEmpty(bookDtls.get(0).getCashFlux())) {
                FcCashItemDTO fcCashItemDTO = iFcCashItemService.get(bookDtls.get(0).getCashFlux());
                if (null != fcCashItemDTO) {
                    bookDtls.get(0).setCashFluxName(fcCashItemDTO.getItemName());
                }
            }
            if (StringUtils.isNotEmpty(bookDtls.get(1).getCashFlux())) {
                FcCashItemDTO fcCashItemDTO = iFcCashItemService.get(bookDtls.get(1).getCashFlux());
                if (null != fcCashItemDTO) {
                    bookDtls.get(1).setCashFluxName(fcCashItemDTO.getItemName());
                }
            }
            FcAccbookExtVO bookExt = new FcAccbookExtVO();
            bookExt.setExternalCorp(fcUnionAccBookVo.getExternalCorp());//外单位户名
            bookExt.setExternalAcc(fcUnionAccBookVo.getExternalAcc());//外单位户名
            bookExt.setExternalBank(fcUnionAccBookVo.getBank());//外单位开户行
            bookExt.setCity(fcUnionAccBookVo.getExternalCity());//城市
            bookExt.setProv(fcUnionAccBookVo.getExternalProv());//省份
            entity.setBookDtls(bookDtls);
            entity.setBookExt(bookExt);
            accbook.setRaceName(accbook.getBusRaceName());
            entity.setAccbook(accbook);
        }
        return entity;
    }
    /**
     * 元素编辑控制
     */
    public List<VoucherRenderControlDTO> getControlBeanEdit(String sysCode, int size) {
        List<VoucherRenderControlDTO> controBeans = new ArrayList<VoucherRenderControlDTO>();
        VoucherRenderControlDTO controBean = null;
        for (int i = 0; i < size; i++) {
            controBean = new VoucherRenderControlDTO();
            controBean.setSummaryEditable(true);
            controBean.setVoucherTypeEditable(true);
            controBean.setCashFluxEditable(true);
            controBeans.add(controBean);
        }
        return controBeans;
    }
    /**
     * 根据系统凭证种类查询业务规则
     */
    public FcBusinessRuleDefineDTO getBusinessRuleExp(String sysRaceCode) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("sysRaceCode", sysRaceCode);
        FcBusinessRuleDefineDTO entity = fcBusinessRuleDefineService.getBusinessRuleExp(map);
        return entity;
    }
    /**
     * 设置凭证信息
     */
    public FcAccbookPageDTO processTallyAccbook(FcUnionAccbookDTO fcu, VoucherRenderControlDTO controlBean, List<VoucherRenderControlDTO> controlBeans, FcBusinessRuleDefineDTO ruleDefine, ICurrentUser user) {
        FcAccbookPageDTO entity = new FcAccbookPageDTO();
        FcAccbookDTO accBook = new FcAccbookDTO();
        accBook.setEmergencySign(FinacialConstant.EMERGENCYSIGN_G);//加急标志
        FcAccbookExtVO bookExt = new FcAccbookExtVO();
        entity.setAccbook(accBook);
        List<FcAccbookDtlDTO> accbookDtls = getAccbookDtls(entity, ruleDefine, controlBeans, fcu);
        bookExt.setExternalCorp(fcu.getExternalCorp());//外单位户名
        bookExt.setExternalAcc(fcu.getExternalAcc());//外单位户名
        bookExt.setExternalBank(fcu.getBank());//外单位开户行
        bookExt.setProv(fcu.getExternalProv());//所在省份
        bookExt.setCity(fcu.getExternalCity());//所在城市
        accBook.setRmk(fcu.getSummaryContent());//备注
//        CorpDTO userNet = iFcAccbookService.getUserNet(user);  //直接调用 system 模块
        CorpDTO userNet = iCorpService.getUserNet(user);
        FcBusRaceVo fcbusRace = getFcBusRaceId(userNet.getId(), FinaBookConstant.SysRaceCode.NB_COLLECTION_NET, user.getId());
        accBook.setBusRaceId(fcbusRace.getId());
        accBook.setRaceName(fcbusRace.getRaceName());
        accBook.setVoucherType(ruleDefine.getVoucherType());
        accBook.setInterestStartDate(fcu.getSendVoucherDate());
        accBook.setTallyDate(fcu.getSendVoucherDate());
        entity.setAccbook(accBook);
        entity.setBookExt(bookExt);
        entity.setBookDtls(accbookDtls);
        return entity;
    }
    /**
     * 记账时构造会计分录
     */
    public List<FcAccbookDtlDTO> getAccbookDtls(FcAccbookPageDTO entity, FcBusinessRuleDefineDTO ruleDefine, List<VoucherRenderControlDTO> controlBeans, FcUnionAccbookDTO unionAccBook) {
        List<FcVoucherEntryDefineDTO> entrys = ruleDefine.getVouchers();
        FcAccbookDtlDTO accbookDtl = null;
        List<FcAccbookDtlDTO> accbookDtls = new ArrayList<FcAccbookDtlDTO>();
        VoucherRenderControlDTO creditBean = controlBeans.get(0);
        creditBean.setAccountEditable(false);
        VoucherRenderControlDTO debitBean = controlBeans.get(1);
        debitBean.setAccountEditable(true);
        FcAccDTO fc = null;
        if (unionAccBook.getExternalAcc() != null) {
            fc = fcAccService.getAccDTOByAccount(unionAccBook.getExternalAcc());
        }
        List<Integer> groupNums = new ArrayList<Integer>();
        boolean flag = isSameNet(unionAccBook, fc);
        int wayInt = unionAccBook.getWay();
        String receiveAcc = unionAccBook.getReceiveAcc();
        FcAccDTO fcAcc = new FcAccDTO();
        for (FcVoucherEntryDefineDTO entry : entrys) {
            accbookDtl = new FcAccbookDtlDTO();
            fcAcc.setBlnctrAcc(entry.getAccount());
            accbookDtl.setCreditMoney(BigDecimal.ZERO);
            accbookDtl.setDebitMoney(BigDecimal.ZERO);
            if (!StringUtils.isEmpty(unionAccBook.getVoucherType())) {
                accbookDtl.setVoucherType(unionAccBook.getVoucherType());
            }
            if (entry.getBalanceWay().intValue() == 1 && entry.getEntryType().intValue() == 10) {//借方，联行往来
                if (-1 == wayInt) {
                    fcAcc.setBlnctrAcc(receiveAcc);
                    debitBean.setAccountEditable(false);
                    accbookDtl.setControlVo(debitBean);
                } else {
                    if (flag && fc != null) {
                        fcAcc = fc;
                        debitBean.setAccountEditable(false);
                        accbookDtl.setControlVo(debitBean);
                    } else {
                        debitBean.setAccountEditable(true);
                        accbookDtl.setControlVo(debitBean);
                    }
                }
                if (fcAcc != null && fcAcc.getBlnctrAcc() != null && fcAcc.getBlnctrAcc().length() != 0) {
                    if (fcAcc.getAccName() == null || fcAcc.getAccName().length() == 0) {
                        fcAcc = fcAccService.getAccDTOByAccount(fcAcc.getBlnctrAcc());
                    }
                }
                if (fcAcc.getBlnctrAcc() != null) {
                    //设置账号信息
                    accbookDtl.setAccount(fcAcc.getBlnctrAcc());
                    accbookDtl.setAccName(fcAcc.getAccName());
                }
                accbookDtl.setDebitMoney(unionAccBook.getSendMoney());
            } else if (entry.getBalanceWay().intValue() == -1 && entry.getEntryType().intValue() == 10) {//贷方，企业活期存款
                if (1 == wayInt) {
                    fcAcc.setBlnctrAcc(receiveAcc);
                    creditBean.setAccountEditable(false);
                    accbookDtl.setControlVo(creditBean);
                } else {
                    if (flag && fc != null) {
                        fcAcc = fc;
                        creditBean.setAccountEditable(false);
                        accbookDtl.setControlVo(creditBean);
                    } else {
                        creditBean.setAccountEditable(true);
                        accbookDtl.setControlVo(creditBean);
                    }
                }
                if (fcAcc != null && fcAcc.getBlnctrAcc() != null && fcAcc.getBlnctrAcc().length() != 0) {
                    //if (fcAcc.getAccName() == null || fcAcc.getAccName().length() == 0) {
                    fcAcc = fcAccService.getAccDTOByAccount(fcAcc.getBlnctrAcc());
                    //}
                }
                if (fcAcc.getBlnctrAcc() != null) {
                    //设置账号信息
                    accbookDtl.setAccount(fcAcc.getBlnctrAcc());
                    accbookDtl.setAccName(fcAcc.getAccName());
                }
                accbookDtl.setCreditMoney(unionAccBook.getSendMoney());
            }
            accbookDtl.setSummaryContent(unionAccBook.getSummaryContent());
            accbookDtl.setOrigVoucherNo(unionAccBook.getSendVoucher());
            accbookDtl.setOrigVoucherId(unionAccBook.getId());
            if (entryDefineIsExist(entry, groupNums)) {
                accbookDtls.add(accbookDtl);
            }
        }
        return accbookDtls;
    }
    /**
     * 判断第三方账号对应的网点与接收方的网点是否相同
     */
    private boolean isSameNet(FcUnionAccbookDTO unionAccBook, FcAccDTO fc) {
        if (fc != null) {
            return fc.getNetId().equals(unionAccBook.getReceiveNet());
        }
        return false;
    }
    /**
     * 获取业务总类id
     */
    public FcBusRaceVo getFcBusRaceId(String netCode, ConstantBean<String> sysRaceBean, String userId) {
        CorpDTO corp = iCorpService.getNetByCorpId(netCode);
        netCode = corp.getNetId();
//		List<FcBusRaceVo> raceList = busRaceService.findBySysRaceCode(sysRaceBean.get());
        List<FcBusRaceDTO> fcBusRaceDTOS = iFcBusRaceService.findBySysRaceCode(sysRaceBean.get());
        List<FcBusRaceVo> raceList = BeanUtils.copyListProperties(fcBusRaceDTOS, FcBusRaceVo.class);
        FcBusRaceVo tempRace = null;
        for (FcBusRaceVo busRace : raceList) {
            String netId = busRace.getNetId();
            if (!EmptyUtils.isEmpty(netId) && netCode.equals(netId)) {
                tempRace = busRace;
                break;
            } else if (EmptyUtils.isEmpty(netId)) {
                tempRace = busRace;
            }
        }
        if (null == tempRace) {
            tempRace = new FcBusRaceVo();
            tempRace.setNetId(netCode);
            tempRace.setRaceCode(sysRaceBean.get());
            tempRace.setRaceName(sysRaceBean.getName());
            tempRace.setSysRaceCode(sysRaceBean.get());
            FcBusRaceDTO fcBusRaceDTO = new FcBusRaceDTO();
            BeanUtils.copyProperties(tempRace, fcBusRaceDTO);
            iFcBusRaceService.save(fcBusRaceDTO, new String[]{netCode}, userId);
            List<FcBusRaceDTO> fcBusRaceDTOS1 = iFcBusRaceService.findBySysRaceCode(sysRaceBean.get());
            BeanUtils.copyProperties(fcBusRaceDTOS1.get(0), tempRace);
        }
        return tempRace;
    }
    /**
     * 判断相同组号的会计分录是否存在,存在时添加到accbookDtls
     */
    public boolean entryDefineIsExist(FcVoucherEntryDefineDTO entry, List<Integer> groupNums) {
        int groupNum = entry.getGroupNum();
        //初始化
        if (groupNums.isEmpty()) {
            groupNums.add(groupNum);
            return true;
        }
        if (groupNums.size() >= 1 && groupNums.contains(groupNum)) {
            return false;
        } else {
            groupNums.add(groupNum);
            return true;
        }
    }
    /**
     * 返回记账的业务种类
     */
    public Integer getOriType() {
        return FinaBookConstant.BusOriType.NB_COLLECTION_NET.get();
    }
    /**
     * 提交
     */
    public void submit(FcAccbookVoucherDTO fcAccbook, TallyFcUnionAccbook entity, String userId) {
        setAccBookVoucher(fcAccbook);
        VoucherWrapperDTO voucher = setVoucherWrapper(fcAccbook, userId);
        boolean isAutoCheck = this.isAutoCheck();
        if (isAutoCheck) { //获取自动复核员用户 setCheckUser(voucher);
            voucher.setAutoCheck(true);
        }
//		fcAccbookService.submitFcAccbook(voucher);
        VoucherWrapperDTO voucherWrapperDTO = new VoucherWrapperDTO();
        BeanUtils.copyProperties(voucher, voucherWrapperDTO);
        FcAccbookDTO accbook = iFcAccbookService.submitFcAccbook(voucherWrapperDTO);
        entity.setReceiveOrder(accbook.getOrderNo());
        entity.setOperatorCode(userId);
        entity.setReceiveBusGlobalId(accbook.getBusGlobalId());
        update(entity);
        notificationService.complete(NotificationConstant.FINACIAL.FCUNIONACCBOOK_TALLY, entity.getId(), userId);
    }
    /**
     * 功能介绍 : 初始化手工记账凭证
     */
    private void setAccBookVoucher(FcAccbookVoucherDTO voucher) {
        createVoucher(voucher);
        List<FcAccbookDtlDTO> bookDtls = new ArrayList<>();
        // 初始化数据
        for (FcAccbookDtlDTO bookDtl : voucher.getBookDtls()) {
            if (null == bookDtl.getDebitMoney()) {
                bookDtl.setDebitMoney(BigDecimal.ZERO);
            }
            if (null == bookDtl.getCreditMoney()) {
                bookDtl.setCreditMoney(BigDecimal.ZERO);
            }
            // 如果明细数据为空，则不保存非空明细
            if (StringUtils.isEmpty(bookDtl.getSummaryContent()) && StringUtils.isEmpty(bookDtl.getAccount()) && null == bookDtl.getDebitMoney() && null == bookDtl.getCreditMoney()) {
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
    private void createVoucher(FcAccbookVoucherDTO voucher) {
        voucher.getAccbook().setBusOriType(getOriType());
    }
    //凭证封装对象
    private VoucherWrapperDTO setVoucherWrapper(FcAccbookVoucherDTO fcAccBook, String userId) {
        VoucherWrapperDTO voucher = new VoucherWrapperDTO();
        voucher.setBusGlobalId(fcAccBook.getAccbook().getBusGlobalId());
        voucher.addTarget(fcAccBook);
        voucher.setProcessType(ProcessType.PROCESS_COMMON);
        voucher.setUserCode(userId);//设置用户
        voucher.setAutoAccounter(false);
        return voucher;
    }
    /**
     * 暂存FC_ACCBOOK
     */
    public void save(FcAccbookVoucherDTO fcAccBook, TallyFcUnionAccbook entity, String userId) {
        setAccBookVoucher(fcAccBook);
        VoucherWrapperDTO voucher = setVoucherWrapper(fcAccBook, userId);
        FcAccbookDTO accbook = iFcAccbookService.saveFcAccbook(voucher);
        entity.setReceiveOrder(accbook.getOrderNo());
        entity.setReceiveBusGlobalId(accbook.getBusGlobalId());
        entity.setOperatorCode(UserContextHelper.helper().getCurrentUser().getId());
        update(entity);
        notificationService.complete(NotificationConstant.FINACIAL.FCUNIONACCBOOK_TALLY, entity.getId(), userId);
    }
    /***
     * 打回
     */
    public void backFcAccbook(FcAccbookVoucherDTO fcAccbook, TallyFcUnionAccbook entity, String opType, String userId) {
        createVoucher(fcAccbook);
        VoucherWrapperDTO Voucher = setVoucherWrapper(fcAccbook, userId);
        //Voucher.getTarget().get(0).getAccbook().setId(entity.getBookId());
        if ("MODIFY".equals(opType)) {
//			fcAccbookService.deleteFcAccbook(Voucher);
            VoucherWrapperDTO voucherWrapperDTO = new VoucherWrapperDTO();
            BeanUtils.copyProperties(Voucher, voucherWrapperDTO);
            iFcAccbookService.deleteFcAccbook(voucherWrapperDTO);
        } else if ("CHECK".equals(opType)) {
//			fcAccbookService.backFcAccbook(Voucher);
            VoucherWrapperDTO voucherWrapperDTO = new VoucherWrapperDTO();
            BeanUtils.copyProperties(Voucher, voucherWrapperDTO);
            iFcAccbookService.backFcAccbook(voucherWrapperDTO);
        }
    }
    /***
     * 复核
     */
    public void checkFcAccbook(FcAccbookVoucherDTO fcAccbook, TallyFcUnionAccbook entity, String userId) {
        createVoucher(fcAccbook);
        VoucherWrapperDTO Voucher = setVoucherWrapper(fcAccbook, userId);
        VoucherWrapperDTO voucherWrapperDTO = new VoucherWrapperDTO();
        BeanUtils.copyProperties(Voucher, voucherWrapperDTO);
        iFcAccbookService.checkFcAccbook(voucherWrapperDTO);
        FcAccbookDTO accbook = Voucher.getTarget().get(0).getAccbook();
        update(entity);
    }
    /**
     * 修改状态
     */
    public void changeStatus(TallyFcUnionAccbook entity, String value, FcAccbookVoucherDTO fcAccbook, ICurrentUser user, String sign) {
        entity.setBillState(value);
        update(entity);
        if (fcAccbook != null) {
            VoucherWrapperDTO Voucher = setVoucherWrapper(fcAccbook, user.getId());
            if ("6".equals(sign)) {
                completeEditNotificationInfo(Voucher, user);
                completeCheckNotificationInfo(Voucher, user);
                Map<String, Object> map = new HashMap<String, Object>();
                map.put("money", NumberUtil.formatNumeric(entity.getSendMoney(), "#,##0.00"));
                notificationService.sendToUserByResPermission(entity.getId(), ResourceConstant.FCUNIONACCBOOK_FCUNIONACCBOOK_APPLY, NotificationConstant.FINACIAL.FCUNIONACCBOOK_TALLY, map, entity.getSendNet());
            } else if ("4".equals(sign)) {
                completeCheckNotificationInfo(Voucher, user);
                sendBackNotificationInfo(Voucher);
            } else if ("3".equals(sign)) {
                completeEditNotificationInfo(Voucher, user);
                completeCheckNotificationInfo(Voucher, user);
                sendCheckNotificationInfo(Voucher, user);
            } else if ("1".equals(sign)) {
                completeCheckNotificationInfo(Voucher, user);
                completeEditNotificationInfo(Voucher, user);
            }
        }
    }
    /**
     * 发送消息提醒
     */
    private void sendCheckNotificationInfo(VoucherWrapperDTO voucher, ICurrentUser user) {
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
            notificationService.sendIgnoreMySelf(accbook.getId(), accbook.getNetId(), NotificationConstant.FINACIAL.FCUNIONACCBOOK_CHECK, notifyVO, user.getId());
        }
    }
    /**
     * 发送消息提醒
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
            notificationService.sendToUser(accbook.getId(), accbook.getOperatorCode(), NotificationConstant.FINACIAL.FCUNIONACCBOOK_EDIT, notifyVO);
        }
    }
    /**
     * 消息完成
     */
    private void completeEditNotificationInfo(VoucherWrapperDTO voucher, ICurrentUser user) {
        for (FcAccbookVoucherDTO voucherVO : voucher.getTarget()) {
            FcAccbookDTO accbook = voucherVO.getAccbook();
            notificationService.complete(NotificationConstant.FINACIAL.FCUNIONACCBOOK_EDIT, accbook.getId(), user.getId());
        }
    }
    /**
     * 消息完成
     */
    private void completeCheckNotificationInfo(VoucherWrapperDTO voucher, ICurrentUser user) {
        for (FcAccbookVoucherDTO voucherVO : voucher.getTarget()) {
            FcAccbookDTO accbook = voucherVO.getAccbook();
            notificationService.complete(NotificationConstant.FINACIAL.FCUNIONACCBOOK_CHECK, accbook.getId(), user.getId());
        }
    }
    /**
     * 保存联行来账信息
     *
     * @param accbookDTO
     */
    public void saveFcUnionAccbook(FcUnionAccbookDTO accbookDTO) {
        TallyFcUnionAccbook unionAccbook = new TallyFcUnionAccbook();
        BeanUtils.copyProperties(accbookDTO, unionAccbook);
        save(unionAccbook);
    }
    /**
     * 根据发送凭证号获取往来信息
     *
     * @param sendVoucherNo 发送凭证号
     * @return 凭证信息
     */
    public List<FcUnionAccbookDTO> getUnionAccbookBySendNo(String sendVoucherNo) {
        return iTallyService.getUnionAccBookBySendNo(sendVoucherNo);
    }
    /**
     * 更新往来信息状态
     *
     * @param id           主键
     * @param billState    单据状态
     * @param oldBillState 旧单据状态
     */
    public void updateUnionAccbookBillState(String id, String billState, String oldBillState) {
        iTallyService.updateUnionAccBookBillState(id, billState, oldBillState);
    }
    /**
     * 判断是否自动复核
     */
    public boolean isAutoCheck() {
        boolean isAutoCheck = CmsConstant.IS_TRUE.equals(configService.getConfig("fcl", "tally_check").getValue());
        return isAutoCheck;
    }
    /**
     * 单位禁用校验-待记账
     *
     * @return
     * @Param corpId
     */
    @Override
    public List<CorpVerifyErrorDTO> verifyCorpDisable(String corpId) {
        List<CorpVerifyErrorDTO> corpVerifyErrorDTOS = new ArrayList<>();
        if (iTallyService.verifyCorpDisableByCorpId(corpId) > 0) {
            CorpVerifyErrorDTO corpVerifyErrorDTO = new CorpVerifyErrorDTO();
            corpVerifyErrorDTO.setCorpId(corpId);
            corpVerifyErrorDTO.setSysCode(SysConfigConstant.Module.FCL);
            corpVerifyErrorDTO.setBusCode(SysConfigConstant.BusType.FCL_TALLY);
            corpVerifyErrorDTO.setErrorMsg(multiLangServiceI.getLang(CorpVerifyMessageConstant.FIN_TALLY_VALID_DISABLE_ERROR));
            corpVerifyErrorDTOS.add(corpVerifyErrorDTO);
        }
        return corpVerifyErrorDTOS;
    }
}
