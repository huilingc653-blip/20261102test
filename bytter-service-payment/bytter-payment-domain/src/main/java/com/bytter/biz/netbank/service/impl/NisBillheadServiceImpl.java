package com.bytter.biz.netbank.service.impl;

import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.bytter.bill.api.bill.dto.AbAccrecordsDTO;
import com.bytter.bill.api.bill.service.IAccebillService;
import com.bytter.biz.bdlink.api.constant.NetbankConstant;
import com.bytter.biz.bdlink.api.enums.NetbankMessageEnum;
import com.bytter.biz.budget.spi.dto.FbsBudgetProcessDTO;
import com.bytter.biz.budget.spi.service.IFbsBudgetProcessService;
import com.bytter.biz.expense.entity.NisExpenseDetail;
import com.bytter.biz.expense.service.NisExpenseDetailService;
import com.bytter.biz.expense.vo.NisExpenseDetailVO;
import com.bytter.biz.netbank.dao.NisBillDao;
import com.bytter.biz.netbank.dao.NisBillheadDao;
import com.bytter.biz.netbank.entity.FcVoucherType;
import com.bytter.biz.netbank.entity.NisBill;
import com.bytter.biz.netbank.entity.NisBillhead;
import com.bytter.biz.netbank.service.FcVoucherTypeService;
import com.bytter.biz.netbank.service.NisBillService;
import com.bytter.biz.netbank.service.NisBillheadService;
import com.bytter.biz.netbank.service.NisMessageService;
import com.bytter.biz.netbank.vo.NisBillheadVO;
import com.bytter.biz.netbank.vo.VNisPayBillVO;
import com.bytter.biz.payment.api.constant.NisMessageConstant;
import com.bytter.biz.payment.api.dto.AbVoucherinfoDTO;
import com.bytter.biz.payment.api.dto.NisBillBankSerialNoDTO;
import com.bytter.biz.payment.api.dto.NisBillheadDTO;
import com.bytter.biz.payment.api.dto.VNisPayBillDTO;
import com.bytter.biz.payment.api.service.INisBillheadService;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.core.common.context.UserContextHelper;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.exception.ParameterException;
import com.bytter.core.constant.BudgetConstant;
import com.bytter.core.constant.CommonConstant;
import com.bytter.core.sequence.SequenceSingleton;
import com.bytter.core.sequence.enums.SequenceEnum;
import com.bytter.core.utils.*;
import com.bytter.system.api.*;
import com.bytter.system.api.constant.NotificationConstant;
import com.bytter.system.api.dto.BtBankAccDTO;
import com.bytter.system.api.dto.BtCurrencyDTO;
import com.google.common.collect.Maps;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author huangx
 * @Description 付款单接口实现类
 * @date 2018/2/6
 */
@Service("nisBillheadServiceImpl")
@Transactional
@DubboService
public class NisBillheadServiceImpl implements INisBillheadService {
    @Autowired
    private NisBillheadService nisBillheadService;
    @Autowired
    private NisExpenseDetailService nisExpenseDetailService;
    @DubboReference(check = false)
    private IBtBankAccService btBankAccService;
    @Autowired
    private NisMessageService nisMessageService;
    @DubboReference(check = false)
    private INotificationService notificationService;
    @Autowired
    private FcVoucherTypeService fcVoucherTypeService;
    @DubboReference(check = false)
    private IFbsBudgetProcessService fbsBudgetProcessService;
    @DubboReference(check = false)
    private IConfigService configService;
    @DubboReference(check = false)
    private IUserService userService;
    @Autowired
    private NisBillService nisBillService;

    @Autowired
    private NisBillheadDao nisBillheadDao;
    @Autowired
    private NisBillDao nisBillDao;
    @DubboReference(check = false)
    private IBtCurrencyService btCurrencyService;
    @DubboReference(check = false)
    private IAccebillService accebillService;
    /**
     * 更新付款单（票据反写接口）
     * 状态：-1-打回  100-付款完成
     *
     * @param id     付款单ID
     * @param status 状态
     * @param msg    信息
     */
    @Override
    public void updateNisBillhead(String id, Integer status, String msg, ICurrentUser user) {
        NisBillhead entity = nisBillheadService.get(id, true);
        entity.setStatus(status);
        entity.setUpdateBy(user.getLoginName());
        entity.setUpdateDate(DateUtils.getDate());
        entity.setWishPayday(DateUtils.getDateYMD());
        //外接模块撤回单据
        if (status.equals(NetbankConstant.STATUE_DEL)) {
            entity.setErpId("");
        }
        nisBillheadService.saveOrUpdate(entity);
        //生成日志
        if (status.equals(NetbankConstant.STATUE_BACK)) {
            //删除票据同时删除关联中间表
            nisBillService.delByPayId(id);
            nisMessageService.saveMessage(entity.getId(), NisMessageConstant.MESSAGE_TYPE_BACK, msg, NisMessageConstant.BUS_TYPE_PAY, user);
        } else if (status.equals(NetbankConstant.STATUE_DEL)) {
            nisMessageService.saveMessage(entity.getId(), NisMessageConstant.MESSAGE_TYPE_DELETE, msg, NisMessageConstant.BUS_TYPE_PAY, user);
        } else if (status.equals(NetbankConstant.STATUE_BILL_COMPLETE)) {
            nisMessageService.saveMessage(entity.getId(), NisMessageConstant.MESSAGE_TYPE_SUCCEED, msg, NisMessageConstant.BUS_TYPE_PAY, user);
        }
    }

    public void updateNisBillheadPyaerInfo(String id, String payerAcc,String payerAccName,String payerBank, ICurrentUser user) {
        NisBillhead entity = nisBillheadService.get(id, true);
        entity.setPayerAcc(payerAcc);
        entity.setPayerAccName(payerAccName);
        entity.setPayerBank(payerBank);
        entity.setUpdateBy(user.getLoginName());
        entity.setUpdateDate(DateUtils.getDate());
        nisBillheadService.saveOrUpdate(entity);
    }

    @Override
    public List<NisBillheadDTO> listByRefundOrgId(String refundOrgId) {
        List<NisBillheadVO> nisBillheadVOS = nisBillheadService.listByRefundOrgId(refundOrgId);
        return BeanUtils.copyListProperties(nisBillheadVOS, NisBillheadDTO.class);
    }

    /**
     * 更新付款单（退款勾兑）
     *
     * @param id     付款单ID
     * @param status 状态
     * @param msg    信息
     */
    @Override
    public void updateNisBillheadForRefundBlend(String id, Integer status, String msg, String bankSerialNo, ICurrentUser user) {
        NisBillhead entity = nisBillheadService.get(id, true);
        entity.setStatus(status);
        entity.setUpdateBy(user.getLoginName());
        entity.setUpdateDate(DateUtils.getDate());
        entity.setBankSerialNo(bankSerialNo);
        nisBillheadService.saveOrUpdate(entity);
        //生成日志
        if (status.equals(NetbankConstant.STATUE_BACK)) {
            nisMessageService.saveMessage(entity.getId(), NisMessageConstant.MESSAGE_TYPE_BACK, msg, NisMessageConstant.BUS_TYPE_PAY, user);
        } else if (status.equals(NetbankConstant.STATUE_DEL)) {
            nisMessageService.saveMessage(entity.getId(), NisMessageConstant.MESSAGE_TYPE_DELETE, msg, NisMessageConstant.BUS_TYPE_PAY, user);
        } else if (status.equals(NetbankConstant.STATUE_BILL_COMPLETE)) {
            nisMessageService.saveMessage(entity.getId(), NisMessageConstant.MESSAGE_TYPE_SUCCEED, msg, NisMessageConstant.BUS_TYPE_PAY, user);
        }
    }
    @Override
    public void updateNisBillheadForInvalidFin(String billNo, Integer status, String msg, String tallySign, ICurrentUser user) {
        NisBillheadVO nisBillheadVO = nisBillheadService.getByBillNo(billNo);
        if (null != nisBillheadVO) {
            NisBillhead entity = nisBillheadService.get(nisBillheadVO.getId(), true);
            entity.setStatus(status);
            entity.setTallySign(tallySign);
            entity.setUpdateBy(user.getLoginName());
            entity.setUpdateDate(DateUtils.getDate());
            if (NetbankConstant.STATUE_TALLY_INVALID_TEMP == status) {
                nisMessageService.saveMessage(entity.getId(), NisMessageConstant.MESSAGE_TYPE_TALLY_TEMP, msg, NisMessageConstant.BUS_TYPE_PAY, user);
            } else if (NetbankConstant.STATUE_TALLY_INVALID_WAIT_CHECK == status) {
                nisMessageService.saveMessage(entity.getId(), NisMessageConstant.MESSAGE_TYPE_TALLY_SUBMIT, msg, NisMessageConstant.BUS_TYPE_PAY, user);
            } else if (NetbankConstant.STATUE_TALLY_INVALID_BACK == status) {
                nisMessageService.saveMessage(entity.getId(), NisMessageConstant.MESSAGE_TYPE_TALLY_BACK, msg, NisMessageConstant.BUS_TYPE_PAY, user);
            } else if (NetbankConstant.STATUE_TALLY_INVALID_CHECKPASS == status) {
                //云南交投-内转特殊处理：记账作废后状态更新为95，支持重新记账
                if (NetbankConstant.PAY_TYPE_05.equals(nisBillheadVO.getSysVoucherCode())) {
                    entity.setStatus(NetbankConstant.STATUE_CHECK);
                    entity.setTallySign("");
                    entity.setVoucherNo("");
                    entity.setOrderNo("");
                }
                nisMessageService.saveMessage(entity.getId(), NisMessageConstant.MESSAGE_TYPE_TALLY_CHECK_PASS, msg, NisMessageConstant.BUS_TYPE_PAY, user);
            }
            nisBillheadService.saveOrUpdate(entity);
        }
    }
    /**
     * 更新下拨账号（记账反写接口）
     *
     * @param billNo    付款单号
     * @param downAccId 下拨账号
     */
    @Override
    public void updateNisBillheadDownAccId(String billNo, String downAccId) {
        NisBillheadVO vo = nisBillheadService.getByBillNo(billNo);
        if (EmptyUtils.isNotEmpty(vo.getDownAccId()) && vo.getDownAccId().equals(downAccId)) {
            return;
        }
        NisBillhead entity = nisBillheadService.get(vo.getId(), true);
        entity.setDownAccId(downAccId);
        nisBillheadService.saveOrUpdate(entity);
    }
    /**
     * 更新下拨账号（记账反写接口）
     *
     * @param billNo    付款单号
     * @param bankAccId 银行账号Id
     */
    @Override
    public void updateNisBillheadForindividual(String billNo, String bankAccId) {
        //获取付款账号
        BtBankAccDTO bankAccDTO = btBankAccService.getBankAccById(bankAccId);
        if (bankAccDTO != null) {
            NisBillheadVO vo = nisBillheadService.getByBillNo(billNo);
            NisBillhead entity = nisBillheadService.get(vo.getId(), true);
            entity.setBifCode(bankAccDTO.getBifCode());
            nisBillheadService.saveOrUpdate(entity);
            //更新明细表
            if (NetbankConstant.FORINDIVIDUAL_1.equals(entity.getIsForindividual())) {
                List<NisExpenseDetailVO> listDetail = nisExpenseDetailService.listByBillId(vo.getId());
                for (NisExpenseDetailVO detailVO : listDetail) {
                    NisExpenseDetail detail = nisExpenseDetailService.get(detailVO.getId());
                    //接口代码
                    detail.setBifCode(bankAccDTO.getBifCode());
                    //跨行标志
                    if (StringUtils.isEmpty(bankAccDTO.getBankCode()) || StringUtils.isEmpty(detailVO.getBankCode())) {
                        detail.setDifBank(NetbankConstant.DIFBANK_0);
                    } else if (bankAccDTO.getBankCode().substring(0, 3).equals(detailVO.getBankCode().substring(0, 3))) {
                        detail.setDifBank(NetbankConstant.DIFBANK_0);
                    } else {
                        detail.setDifBank(NetbankConstant.DIFBANK_1);
                    }
                    //地域标志
                    if (StringUtils.isEmpty(bankAccDTO.getProv()) || StringUtils.isEmpty(detailVO.getProv())) {
                        detail.setAreaSign(NetbankConstant.AREASIGN_0);
                    } else if (bankAccDTO.getProv().equals(detailVO.getProv()) && bankAccDTO.getCity().equals(detailVO.getCity())) {
                        detail.setAreaSign(NetbankConstant.AREASIGN_0);
                    } else {
                        detail.setAreaSign(NetbankConstant.AREASIGN_1);
                    }
                    nisExpenseDetailService.saveOrUpdate(detail);
                }
            }
        }
    }
    /**
     * 保存付款单
     *
     * @param nisBillheadDTO
     */
    @Override
    public void saveNisBillhead(NisBillheadDTO nisBillheadDTO, ICurrentUser user) {
        NisBillheadVO entity = new NisBillheadVO();
        //外部模块调用billNo应该payment统一设置
        if(StringUtils.isEmpty(nisBillheadDTO.getBillNo())){
            nisBillheadDTO.setBillNo(SequenceSingleton.getSequence(SequenceEnum.NIS_BILLHEAD));
        }
        BeanUtils.copyNotNullProperties(nisBillheadDTO, entity);
        //是否已付款默认否
        entity.setIsPayed(NetbankConstant.ISPAYED_0);
        //是否垫支默认否
        entity.setIsAdvance(NetbankConstant.ISADVANCE_0);
        //是否已选票据默认否
        entity.setIsOpenBill(NetbankConstant.ISOPENBILL_0);
        if (EmptyUtils.isEmpty(entity.getIsQuick())) {
            //是否加急默认否
            entity.setIsQuick(NetbankConstant.ISQUICK_0);
        }
        //是否自动支付
        entity.setIsAutoPay(CommonConstant.NO);
        if (EmptyUtils.isEmpty(entity.getAccId())) {
            BtBankAccDTO btBankAccDTO = btBankAccService.getBankAccInfo(entity.getPayerAcc());
            if (null != btBankAccDTO) {
                entity.setAccId(btBankAccDTO.getId());
                entity.setBankTypeId(btBankAccDTO.getBankTypeId());
                entity.setBifCode(btBankAccDTO.getBifCode());
            }
        }else {
            if(StrUtil.isBlank(entity.getBifCode())){
                BtBankAccDTO btBankAccDTO = btBankAccService.getBankAccById(entity.getAccId());
                if (null != btBankAccDTO) {
                    entity.setBankTypeId(btBankAccDTO.getBankTypeId());
                    entity.setBifCode(btBankAccDTO.getBifCode());
                }
            }
        }
        nisBillheadService.saveNisBillhead(entity, user);
        FcVoucherType voucherType = fcVoucherTypeService.get(entity.getVoucherTypeId(), true);
        if (voucherType != null) {
            entity.setVoucherName(voucherType.getVoucherName());
        }
        //消息提醒
        notificationService.sendToUserByLoginName(entity.getId(), entity.getCreateBy(), NotificationConstant.NETBANK.PAY_APPLY, entity);
    }
    /**
     * 根据外部单号获取付款信息
     *
     * @param erpId
     */
    @Override
    public List<NisBillheadDTO> listNisBillheadByErpId(String erpId) {
        List<NisBillheadVO> listVo = nisBillheadService.listByErpId(erpId);
        List<NisBillheadDTO> list = new ArrayList<NisBillheadDTO>();
        if (!EmptyUtils.isEmptyList(listVo)) {
            for (NisBillheadVO vo : listVo) {
                NisBillheadDTO dto = new NisBillheadDTO();
                BeanUtils.copyNotNullProperties(vo, dto);
                list.add(dto);
            }
        }
        return list;
    }

    /**
     * 根据共享结算单号list获取付款信息
     *
     * @param bursarBillNoList
     */
    public List<NisBillheadDTO> listNisBillheadByBursarBillNos(List<String> bursarBillNoList) {
        List<NisBillheadVO> listVo = nisBillheadService.listByBursarBillNos(bursarBillNoList);
        List<NisBillheadDTO> list = new ArrayList<>();
        if (!EmptyUtils.isEmptyList(listVo)) {
            for (NisBillheadVO vo : listVo) {
                NisBillheadDTO dto = new NisBillheadDTO();
                BeanUtils.copyNotNullProperties(vo, dto);
                list.add(dto);
            }
        }
        return list;
    }

    @Override
    public Map<String, NisBillheadDTO> listNisBillInfoByErpId(List<String> erpIdList) {
        List<NisBillheadVO> nisBillheadList = nisBillheadDao.listNisBillInfoByErpId(erpIdList);
        if (ObjectUtil.isNotEmpty(nisBillheadList)) {
            List<NisBillheadDTO> list = new ArrayList<NisBillheadDTO>();
            nisBillheadList.forEach(item -> {
                NisBillheadDTO dto = new NisBillheadDTO();
                BeanUtils.copyNotNullProperties(item, dto);
                list.add(dto);
            });
            return list.stream().collect(Collectors.toMap(NisBillheadDTO::getErpId, Function.identity()));
        }
        return Maps.newHashMap();
    }

    /**
     * 根据外部单号获取付款信息
     *
     * @param erpId
     */
    @Override
    public NisBillheadDTO getNisBillheadByErpId(String erpId) {
        NisBillheadVO vo = nisBillheadService.getByErpId(erpId);
        NisBillheadDTO dto = new NisBillheadDTO();
        if (!EmptyUtils.isEmpty(vo)) {
            BeanUtils.copyNotNullProperties(vo, dto);
        }
        return dto;
    }
    @Override
    public void deteleNisbillHeadById(String id, ICurrentUser user) {
        NisBillhead entity = nisBillheadService.get(id);
        nisBillheadService.deleteNisBillhead(entity, user, false, true);
        nisBillheadService.getEntityDao().flush();
    }
    /**
     * 根据单号获取付款信息
     *
     * @param billNo
     */
    @Override
    public NisBillheadDTO getNisBillheadByBillNo(String billNo) {
        NisBillheadVO vo = nisBillheadService.getByBillNo(billNo);
        NisBillheadDTO dto = new NisBillheadDTO();
        if (!EmptyUtils.isEmpty(vo)) {
            BeanUtils.copyNotNullProperties(vo, dto);
        }
        return dto;
    }
    /**
     * 根据单号获取付款信息
     *
     * @param id
     */
    @Override
    public NisBillheadDTO getNisBillheadById(String id) {
        NisBillhead entity = nisBillheadService.get(id, true);
        NisBillheadDTO dto = new NisBillheadDTO();
        if (!EmptyUtils.isEmpty(entity)) {
            BeanUtils.copyNotNullProperties(entity, dto);
        }
        return dto;
    }
    /**
     * 手工记账待记账的付款单
     *
     * @return
     */
    @Override
    public DataTablesOutput getDataTablesUnion(DataTablesInput dataTablesInput, ICurrentUser user) {
        return nisBillheadService.getDataTablesUnions(dataTablesInput, user);
    }
    @Override
    public void updateNisBillheadByDto(NisBillheadDTO nisBillheadDTO, ICurrentUser user) {
        nisBillheadService.backNisBillhead(nisBillheadDTO.getId(), user, NetbankConstant.BACK_TYPE_4);
    }
    /**
     * <br>
     * <b>功能：</b>查询付款计划（企e金服接口）<br>
     * <b>作者：</b>黄茜<br>
     * <b>日期：</b>2018-11-25<br>
     *
     * @param corpflag
     * @param corpCode
     * @return
     */
    @Override
    public BigDecimal queryNisBillhead(String corpflag, String corpCode, Date wishPayday) {
        return nisBillheadService.queryNisBillhead(corpflag, corpCode, wishPayday);
    }
    /**
     * 外系统根据时间获取付款单
     *
     * @param beginDate
     * @param endDate
     * @return
     */
    @Override
    public List<NisBillheadDTO> listNisBillheadByTime(String beginDate, String endDate) {
        List<NisBillheadVO> listVo = nisBillheadService.listByTime(beginDate, endDate);
        List<NisBillheadDTO> list = new ArrayList<>();
        if (!EmptyUtils.isEmptyList(listVo)) {
            for (NisBillheadVO vo : listVo) {
                NisBillheadDTO dto = new NisBillheadDTO();
                BeanUtils.copyNotNullProperties(vo, dto);
                list.add(dto);
            }
        }
        return list;
    }
    @Override
    public void syncBatchSave(List<NisBillheadDTO> list) {
        for (NisBillheadDTO dto : list) {
            NisBillheadVO erpVO = this.nisBillheadService.getNisBillheadByErpId(dto.getErpId(), dto.getBillFrom());
            if (!EmptyUtils.isEmpty(erpVO)) {
                throw new ParameterException(NetbankMessageEnum.ERP_PAYMENT_DOCUMENT_ALREADY_EXISTS, dto.getErpId());
            }
            ICurrentUser user = null;
            String voucherName = EmptyUtils.isBlank(dto.getVoucherName()) ? "外付网银" : dto.getVoucherName();
            //判断付款方式是否存在
            FcVoucherType voucherType = fcVoucherTypeService.getByVoucherName(voucherName);
            dto.setVoucherTypeId(voucherType.getId());
            this.saveNisBillhead(dto, user);
        }
    }
    @Override
    public void syncBatchDelete(List<NisBillheadDTO> list) {
        for (NisBillheadDTO dto : list) {
            NisBillheadVO vo = this.nisBillheadService.getNisBillheadByErpId(dto.getErpId(), dto.getBillFrom());
            if (EmptyUtils.isEmpty(vo)) {
                throw new ParameterException(NetbankMessageEnum.ERP_PAYMENT_DOCUMENT_NO_EXISTS, dto.getErpId());
            }
            //暂存和编辑状态才可以删除
            if (!NetbankConstant.STATUE_TEMP.equals(vo.getStatus()) && !NetbankConstant.STATUE_BACK.equals(vo.getStatus())) {
                throw new ParameterException(NetbankMessageEnum.ERP_PAYMENT_DOCUMENT_NO_DEL, dto.getErpId());
            }
            ICurrentUser user = this.userService.getByLoginName(vo.getCreateBy());
            this.nisBillheadService.delNisBillhead(vo.getId(), user, false, "");
        }
    }
    @Override
    public String getVoucherStatByErpId(String erpId, String billFrom) {
        NisBillheadVO vo = this.nisBillheadService.getNisBillheadByErpId(erpId, billFrom);
        if (EmptyUtils.isEmpty(vo)) {
            throw new ParameterException(NetbankMessageEnum.ERP_PAYMENT_DOCUMENT_NO_EXISTS, erpId);
        }
        return vo.getVoucherStat();
    }
    /**
     * 判断账号付款是否使用中
     *
     * @param blnctrAcc
     * @return
     */
    @Override
    public boolean isExistBlnctrAcc(String blnctrAcc) {
        return nisBillheadService.isExistBlnctrAcc(blnctrAcc);
    }
    /**
     * 根据银行流水获取付款单
     *
     * @param dataTablesInput
     * @return
     */
    @Override
    public DataTablesOutput listNisBillheadByDtl(DataTablesInput dataTablesInput,String joinSql) {
        DataTablesOutput dataTablesOutput = nisBillheadService.listNisBillheadByDtl(dataTablesInput,joinSql);
        List<VNisPayBillDTO> list = dataTablesOutput.getData();
        if(null != list && list.size() > 0){
            Map<String, BtCurrencyDTO> currency = btCurrencyService.getCurrency();
            if(null != currency){
                for(VNisPayBillDTO dto : list){
                    BtCurrencyDTO btCurrencyDTO = currency.get(dto.getCurId());
                    if(null != btCurrencyDTO){
                        dto.setCurName(btCurrencyDTO.getCurName());
                    }
                }
            }
        }
        return dataTablesOutput;
    }
    /**
     * 更新付款单银行流水号（付款明细勾兑返写银行流水号）
     *
     * @param id
     * @param bankSerialNo
     */
    @Override
    public void updateNisBillheadForBankSerialNo(String id, String bankSerialNo) {
        NisBillhead entity = nisBillheadService.get(id, true);
        if (StringUtils.isNotEmpty(entity)) {
            entity.setBankSerialNo(bankSerialNo);
            nisBillheadService.saveOrUpdate(entity);
        }
    }
    /**
     * 根据银行流水获取付款单(退款勾兑)
     *
     * @param dataTablesInput
     * @return
     */
    @Override
    public DataTablesOutput listNisBillheadByRefundblending(DataTablesInput dataTablesInput) {
        return nisBillheadService.listNisBillheadByRefundblending(dataTablesInput);
    }
    @Override
    public List<NisBillheadDTO> listNisBillheadByRefundblending(Date beginDate,Date endDate) {
        return nisBillheadService.getDataTablesUnionForRefundblending(beginDate,endDate);
    }
    @Override
    public List<NisBillheadDTO> listNisBillheadByRefundblendingNew(Date beginDate,Date endDate,int size) {
        return nisBillheadService.getDataTablesUnionForRefundblendingNew(beginDate,endDate,size);
    }
    @Override
    public int getCountDataTablesUnionForRefundblending(Date beginDate,Date endDate) {
    	return nisBillheadService.getCountDataTablesUnionForRefundblending(beginDate,endDate);
    }
    /**
     * 退款勾兑打回付款单
     *
     * @param id
     * @param user
     * @param backType
     */
    @Override
    public void backNisBillhead(String id, ICurrentUser user, String backType) {
        nisBillheadService.backNisBillhead(id, user, backType);
    }
    /**
     * 根据流水号查询付款单
     *
     * @param bankSerialNo
     * @return
     */
    @Override
    public List<NisBillheadDTO> listNisBillheadByBankSerialNo(String bankSerialNo) {
        return nisBillheadService.listNisBillheadByBankSerialNo(bankSerialNo);
    }
    /**
     * Erp接口（WebService）获取付款单交易状态信息
     *
     * @param serialNoErp 外部系统id
     * @return
     */
    @Override
    public List<Map<Object, Object>> getVoucherStatByWs(String serialNoErp) {
        return nisBillheadService.getVoucherStatByWs(serialNoErp);
    }
    /**
     * erp标准接口保存
     *
     * @param dto
     */
    @Override
    public void saveErpNisBillhead(NisBillheadDTO dto) {
        NisBillhead entity = new NisBillhead();
        BeanUtils.copyNotNullProperties(dto, entity);
        //设置付款表的单号
        entity.setBillNo(SequenceSingleton.getSequence(SequenceEnum.NIS_BILLHEAD));
        //是否垫支 默认否
        entity.setIsAdvance(NetbankConstant.ISADVANCE_0);
        //是否挂接资金计划
        entity.setInnerSign(BudgetConstant.INNER_SIGN_0);
        //是否直接付款 y  n
        entity.setDirectPayFlag("y");
        nisBillheadService.save(entity);
    }
    @Override
    public BigDecimal getSum(String bankAcc, String curId, String startTime, String endTime) {
        return nisBillheadService.getSum(bankAcc, curId, startTime, endTime);
    }
    @Override
    public BigDecimal getSumForCorp(String corpId, String curId, String startTime, String endTime) {
        return nisBillheadService.getSumForCorp(corpId, curId, startTime, endTime);
    }
    @Override
    public NisBillheadDTO getMoneyById(String id) {
        return nisBillheadService.getMoneyById(id);
    }
    @Override
    public List<NisBillheadDTO> queryTallyUnions() {
        return nisBillheadService.queryTallyUnions();
    }
    /**
     * 票据支付失败处理
     *
     * @param id          付款单ID
     * @param failureList
     * @param successList
     */
    @Override
    public void backNisBillheadForBill(String id, List<AbVoucherinfoDTO> failureList, List<AbVoucherinfoDTO> successList) {
        //获取原付款单
        NisBillhead entity = nisBillheadService.get(id, true);
        ICurrentUser user = this.userService.getByLoginName(entity.getCreateBy());
        if (EmptyUtils.isEmpty(successList)) {
            //整笔付款失败，直接在原付款单处理
            entity.setStatus(NetbankConstant.STATUE_BACK);
            nisBillheadService.saveOrUpdate(entity);
            //更新资金计划
            fbsBudgetProcessService.updateBudgetProcess(BudgetConstant.BUSINESS_NAME_PAY, BudgetConstant.PROCESS_SYSNAME_NIS, entity.getBillNo(), NetbankConstant.STATUE_BACK, user);
            nisMessageService.saveMessage(entity.getId(), NisMessageConstant.MESSAGE_TYPE_BACK, "", NisMessageConstant.BUS_TYPE_PAY, user);
        } else {
            //部分成功部分失败，自动拆分付款单
            //生成拆分付款单-成功
            NisBillhead successNisbillhead = new NisBillhead();
            BeanUtils.copyNotNullProperties(entity, successNisbillhead);
            successNisbillhead.setId(null);
            successNisbillhead.setBillNo(SequenceSingleton.getSequence(SequenceEnum.NIS_BILLHEAD));
            successNisbillhead.setPayMoney(BigDecimal.ZERO);
            successNisbillhead.setSplitId(entity.getId());
            successNisbillhead.setOrigSplitId(entity.getId());
            for (AbVoucherinfoDTO bill : successList) {
                successNisbillhead.setPayMoney(successNisbillhead.getPayMoney().add(bill.getBillMoney()));
            }
            nisBillheadService.saveOrUpdate(successNisbillhead);
            //生成日志
            nisMessageService.saveMessage(successNisbillhead.getId(), NisMessageConstant.MESSAGE_TYPE_TEMP, NetbankConstant.NETBANK_LOG_SPLIT_SUCC, NisMessageConstant.BUS_TYPE_PAY, user);
            String billIdSuccess = "";
            for (AbVoucherinfoDTO bill : successList) {
                billIdSuccess += bill.getId() + CommonConstant.COMMA_SPLIT;
            }
            //保存票据关联信息
            nisBillService.saveOrUpdate(successNisbillhead.getId(), billIdSuccess);
            //生成拆分付款单-失败
            NisBillhead failureNisbillhead = new NisBillhead();
            BeanUtils.copyNotNullProperties(entity, failureNisbillhead);
            failureNisbillhead.setId(null);
            failureNisbillhead.setBillNo(SequenceSingleton.getSequence(SequenceEnum.NIS_BILLHEAD));
            failureNisbillhead.setPayMoney(BigDecimal.ZERO);
            failureNisbillhead.setSplitId(entity.getId());
            failureNisbillhead.setOrigSplitId(entity.getId());
            failureNisbillhead.setStatus(NetbankConstant.STATUE_BACK);
            for (AbVoucherinfoDTO bill : failureList) {
                failureNisbillhead.setPayMoney(failureNisbillhead.getPayMoney().add(bill.getBillMoney()));
            }
            nisBillheadService.saveOrUpdate(failureNisbillhead);
            //生成日志
            nisMessageService.saveMessage(failureNisbillhead.getId(), NisMessageConstant.MESSAGE_TYPE_TEMP, NetbankConstant.NETBANK_LOG_SPLIT_SUCC, NisMessageConstant.BUS_TYPE_PAY, user);
            String billIdFailure = "";
            for (AbVoucherinfoDTO bill : failureList) {
                billIdFailure += bill.getId() + CommonConstant.COMMA_SPLIT;
            }
            //保存票据关联信息
            nisBillService.saveOrUpdate(failureNisbillhead.getId(), billIdFailure);
            //修改原付款单状态为删除
            entity.setStatus(NetbankConstant.STATUE_DEL);
            nisBillheadService.saveOrUpdate(entity);
            //资金计划处理
            List<FbsBudgetProcessDTO> processDTOList = this.fbsBudgetProcessService.getBudgetProcessByBillNo(BudgetConstant.BUSINESS_NAME_PAY, BudgetConstant.PROCESS_SYSNAME_NIS, entity.getBillNo());
            List<FbsBudgetProcessDTO> list = new ArrayList<>();
            if (!EmptyUtils.isEmpty(processDTOList) && processDTOList.size() == 1) {
                //挂接单个计划科目，直接释放原资金计划
                fbsBudgetProcessService.deleteBudgetProcess(BudgetConstant.BUSINESS_NAME_PAY, BudgetConstant.PROCESS_SYSNAME_NIS, entity.getBillNo());
                FbsBudgetProcessDTO successProcessDTO = new FbsBudgetProcessDTO();
                BeanUtils.copyNotNullProperties(processDTOList.get(0), successProcessDTO);
                successProcessDTO.setId(null);
                successProcessDTO.setBillId(successNisbillhead.getId());
                successProcessDTO.setBillNo(successNisbillhead.getBillNo());
                successProcessDTO.setProcessMoney(successNisbillhead.getPayMoney());
                if (successProcessDTO.getExchangeRate() != null) {
                    successProcessDTO.setProcessCurMoney(successProcessDTO.getProcessMoney().multiply(successProcessDTO.getExchangeRate()));
                } else {
                    successProcessDTO.setProcessCurMoney(successProcessDTO.getProcessMoney());
                }
                list.add(successProcessDTO);
                FbsBudgetProcessDTO failureProcessDTO = new FbsBudgetProcessDTO();
                BeanUtils.copyNotNullProperties(processDTOList.get(0), failureProcessDTO);
                failureProcessDTO.setId(null);
                failureProcessDTO.setBillId(failureNisbillhead.getId());
                failureProcessDTO.setBillNo(failureNisbillhead.getBillNo());
                failureProcessDTO.setProcessMoney(failureNisbillhead.getPayMoney());
                if (failureProcessDTO.getExchangeRate() != null) {
                    failureProcessDTO.setProcessCurMoney(failureProcessDTO.getProcessMoney().multiply(failureProcessDTO.getExchangeRate()));
                } else {
                    failureProcessDTO.setProcessCurMoney(failureProcessDTO.getProcessMoney());
                }
                failureProcessDTO.setStatus(BudgetConstant.STATUE_BACK);
                list.add(failureProcessDTO);
            }
            fbsBudgetProcessService.writeBudgetProcess(list, user);
        }
    }
    /**
     * 票据支付成功处理
     *
     * @param ids 付款单ID
     */
    @Override
    public void successNisBillheadForBill(String ids,Integer status) {
        if(StrUtil.isNotEmpty(ids)) {
            List<String> idsArray=StrUtil.split(ids,",");
            idsArray.forEach(id->{
                //获取原付款单
                NisBillhead entity = nisBillheadService.get(id, true);
                //判断付款为暂存和打回状态进行资金计划执行操作
                if (entity.getStatus() != null
                        && (entity.getStatus() == NetbankConstant.STATUE_BACK || entity.getStatus() == NetbankConstant.STATUE_TEMP)) {
                    ICurrentUser user = UserContextHelper.helper().getCurrentUser();
                    if (user == null) {
                        user = userService.getScheduler();
                    }
                    fbsBudgetProcessService.updateBudgetProcess(BudgetConstant.BUSINESS_NAME_PAY, BudgetConstant.PROCESS_SYSNAME_NIS,
                            entity.getBillNo(), NetbankConstant.STATUE_CHECK, user);
                }
                //付款单修改为票据完成
                entity.setStatus(status);
                if(entity.getBillMoney()==null){
                    entity.setBillMoney(entity.getPayMoney());
                }
                //期望付款日期改成当天
                entity.setWishPayday(DateUtils.getDateYMD());
                nisBillheadService.saveOrUpdate(entity);
                ICurrentUser user = UserContextHelper.helper().getCurrentUser();
                if(user==null){
                    user = userService.getScheduler();
                }
                if(NetbankConstant.STATUE_BILL_COMPLETE.equals(status)) {
                    nisMessageService.saveMessage(entity.getId(), NisMessageConstant.MESSAGE_TYPE_SUCCEED, "票据业务完成联动付款单业务完成！", NisMessageConstant.BUS_TYPE_PAY, user);
                } else if (NetbankConstant.STATUE_CHECK.equals(status)) {
                    nisMessageService.saveMessage(entity.getId(), NisMessageConstant.MESSAGE_TYPE_CHECK_PASS, "票据暂存联动付款单审批完成！", NisMessageConstant.BUS_TYPE_PAY, user);
                }
            });
        }
    }
    @Override
    public List<NisBillheadDTO> listErpTodoBill() {
        List<NisBillheadVO> listErp = nisBillheadService.listErpTodoBill();
        if (EmptyUtils.isEmptyList(listErp)) {
            return new ArrayList<>();
        }
        return BeanUtils.copyListProperties(listErp, NisBillheadDTO.class);
    }
    @Override
    public List<String> listBillId(List<String> listId) {
        if (ObjectUtil.isEmpty(listId)) {
            return ListUtil.empty();
        }
        return nisBillheadService.listBillId(listId);
    }
    @Override
    public List<String> listPayIdByBill(List<String> listId) {
        return nisBillheadService.listPayIdByBill(listId);
    }
    @Override
    public List<NisBillheadDTO> getCmsInlSendId(List<String> ids) {
        return nisBillheadService.getCmsInlSendId(ids);
    }

    @Override
    public List<NisBillheadDTO> listByErpIds(List<String> ids) {
    	List<NisBillheadVO> list = nisBillheadService.listByErpIds(ids);
    	List<NisBillheadDTO> listDTO = BeanUtils.copyListProperties(list, NisBillheadDTO.class);
        return listDTO;
    }


    @Override
    public int updateEditPassSign(String businessId, String tallySign) {
        return nisBillheadService.updateEditPassSign(businessId, tallySign);
    }
    @Override
    public int updateAutoCheckSign(String id, String orderNo, String userId, String voucherNo, Integer statueTallyCheckPass) {
        return nisBillheadService.updateAutoCheckSign(id, orderNo, userId, voucherNo, statueTallyCheckPass);
    }
    @Override
    public int updateTallySign(String id, String orderNo, String userId) {
        return nisBillheadService.updateTallySign(id, orderNo, userId);
    }
    @Override
    public int updateCheckSign(String id, String voucherNo, Integer statueTallyCheckPass) {
        return nisBillheadService.updateCheckSign(id, voucherNo, statueTallyCheckPass);
    }
    @Override
    public int updateEditBackSign(String businessId, String tallySign) {
        return nisBillheadService.updateEditBackSign(businessId, tallySign);
    }
    @Override
    public int updateCheckBackSign(String id, String voucherNo) {
        return nisBillheadService.updateCheckBackSign(id, voucherNo);
    }

    @Override
    public void createSQLQueryFinacialColumn(String toString, Map<String, Object> params) {
        nisBillheadDao.createSQLQuery(toString, params).executeUpdate();
    }


    public List<NisBillBankSerialNoDTO> getBlendingList(List<String> dtlIdS) {
        return nisBillheadDao.getBlendingList(dtlIdS);
    }

    public Map<String, NisBillBankSerialNoDTO> getBlendingMap(List<String> dtlIdS) {
        List<NisBillBankSerialNoDTO> voList = nisBillheadDao.getBlendingList(dtlIdS);
        return voList.stream().collect(Collectors.toMap(NisBillBankSerialNoDTO::getDtlId, Function.identity(), (existing, replacement) -> existing));
    }


    public List<NisBillBankSerialNoDTO> getBlendingDate(NisBillBankSerialNoDTO dto) {
        return nisBillheadDao.getBlendingDate(dto);
    }
    /**
     * 插入中间关联表
     *
     * @param billId
     * @param isFrom
     * @param bankSerialNo
     */
    public void insetBillBankSerialNo(String billId, String isFrom, String bankSerialNo, String dtlId) {
        nisBillheadDao.insetBillBankSerialNo(billId, isFrom, bankSerialNo, dtlId);
    }
    /**
     * 删除中间关联表
     *
     * @param dtlId
     */
    public void deleteBillBankSerialNoByDtlId(String dtlId) {
        nisBillheadDao.deleteBillBankSerialNoByDtlId( dtlId) ;
    }
    /**
     * 删除中间关联表
     *
     * @param billId
     */
    public void deleteBillBankSerialNoByBillId(String billId) {
        nisBillheadDao.deleteBillBankSerialNoByBillId( billId) ;
    }
    /**
     * 删除退款勾兑中间关联表
     *
     * @param billId
     */
    public void deleteBillBankSerialNoRefundByBillId(String billId) {
        nisBillheadDao.deleteBillBankSerialNoRefundByBillId( billId) ;
    }
    /**
     * 根据业务单据号得到流水id
     *
     * @param billId
     * @return
     */
    public List<String> getDtlId(String billId) {
       return nisBillheadDao.getDtlId( billId) ;
    }
    /**
     * 根据流水id获取业务单号
     *
     * @param dtlId
     * @return
     */
    public List<String> getBillId(String dtlId) {
        return nisBillheadDao.getBillId( dtlId) ;
    }


    /**
     * 根据票据流水id获取对应的挂接付款金额
     *
     * @param billIds
     * @return
     */
    public String getBillBalances(String billIds, String payId) {
        String[] billIdAr = billIds.split(CommonConstant.COMMA_SPLIT);
        StringBuilder billBalances = new StringBuilder();
        for (String billId : billIdAr) {
            NisBill nisBill = nisBillDao.getByBillId(billId,payId);
            if (!ObjectUtils.isEmpty(nisBill)) {
                if (EmptyUtils.isBlank(billBalances.toString())) {
                    billBalances.append(nisBill.getBalance().toString());
                } else {
                    billBalances.append(CommonConstant.COMMA_SPLIT).append(nisBill.getBalance().toString());
                }
            }
        }
        return billBalances.toString();
    }

    @Override
    public void updateNisBillByBillId(String billIdOld, String billIdNew) {
        nisBillDao.updateNisBillByBillId(billIdOld,billIdNew);
    }

    public List<NisBillheadDTO> listNisBillheadByPayIds(String payIds){
        if(StringUtils.isBlank(payIds)){
            return null;
        }
        List<NisBillheadVO> nisBillheadVOS = nisBillheadService.listByIdUnion(payIds);
        List<NisBillheadDTO> nisBillheadDTOS = BeanUtils.copyListProperties(nisBillheadVOS,NisBillheadDTO.class);
        return nisBillheadDTOS;
    }

    @Override
    public void syncScfPlatformProduct(String voucherId, String platformId, String productId) {
        nisBillheadService.syncScfPlatformProduct(voucherId, platformId, productId);
    }

}
