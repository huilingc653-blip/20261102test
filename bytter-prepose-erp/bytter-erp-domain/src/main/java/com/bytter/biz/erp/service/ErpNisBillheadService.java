package com.bytter.biz.erp.service;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import com.bytter.biz.erp.dao.ErpNisBillheadDao;
import com.bytter.biz.erp.entity.*;
import com.bytter.biz.erp.vo.request.PaymentReqVo;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.erp.common.constant.ErpConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;

/**
 * 付款单单笔接口实现类
 *
 * @author fangcf
 * @date 2026-04-29
 */
@Service("erpNisBillheadService")
public class ErpNisBillheadService extends BaseService<NisBillhead, String> {
    @Autowired
    private ErpPayInfoService erpPayInfoService;
    @Autowired
    private ErpNisBillheadDao erpNisBillheadDao;
    @Autowired
    private ErpSysCorpService erpSysCorpService;
    @Autowired
    private ErpBtCurrencyService erpBtCurrencyService;
    @Autowired
    private ErpBankAccInfoService erpBankAccInfoService;
    @Autowired
    private ErpInputBankInfoService erpInputBankInfoService;
    @Autowired
    private ErpSysRegionService erpSysRegionService;
    @Autowired
    private ErpFcVoucherTypeService erpFcVoucherTypeService;


    @Override
    public BaseDao getEntityDao() {
        return erpNisBillheadDao;
    }

    /**
     * 付款单单笔推送
     *
     * @param paymentVo
     * @return TODO
     */
    @Transactional
    public Map<String, String> handlePaymentSingle(PaymentReqVo paymentVo) {
        // 返回实体map
        Map<String, String> resMap = new HashMap<>();

        //保存付款中间表
        ErpPayInfo erpPayInfo = buildErpPayInfo(paymentVo);
        erpPayInfoService.saveEntity(erpPayInfo);

        //处理付款信息
        NisBillhead nisBillhead = buildNisBillhead(paymentVo);
        nisBillhead.setErpId(erpPayInfo.getId());
        super.saveEntity(nisBillhead);//保存付款单

        resMap.put("serialNoErp", paymentVo.getSerialNoErp());
        resMap.put("voucherNoErp", paymentVo.getVoucherNoErp());
        resMap.put("status", ErpConstant.SERVER_SUCCESS_STATUS);
        resMap.put("message", "处理成功");

        return resMap;
    }

    /**
     * 付款结果 单笔查询
     *
     * @param tenantId
     * @param serialNoErp
     * @param voucherNoErp
     * @return TODO
     */
    public List<ErpPaymentResultView> getPaymentSingleResult(String tenantId, String serialNoErp, String voucherNoErp) {
        return erpNisBillheadDao.listPaymentSingleResult(tenantId, serialNoErp, voucherNoErp);
    }

    /**
     * 组装付款单
     *
     * @param row
     * @return TODO
     */
    private NisBillhead buildNisBillhead(PaymentReqVo row) {
        NisBillhead form = new NisBillhead();

        form.setTenantId(row.getTenantId());
        form.setId(row.getSerialNoErp());
        form.setBillNo(row.getVoucherNoErp());

        form.setCreateDate(new Date());
        form.setCorpId(row.getCorpId());
        form.setVoucherTypeId(row.getVoucherTypeId());

        form.setAccId(row.getPayerAccId());
        form.setPayerAcc(row.getPayerAccNo());
        form.setPayerAccName(row.getPayerAccName());
        form.setPayerBank(row.getPayerBankCode());
        form.setBifCode(row.getBifCode());

        form.setCurId(row.getCurId());
        form.setPayMoney(row.getAmt());

        form.setPayeeAcc(row.getPayeeAccNo());
        form.setPayeeAccName(row.getPayeeAccName());
        form.setPayeeBankCode(row.getPayeeCode());
        form.setPayeeBank(row.getPayeeBank());

        form.setProv(row.getPayeeProv());
        form.setCity(row.getPayeeCity());
        String payDate = row.getWishPayday();
        if (ObjectUtil.isNotEmpty(payDate)) {
            form.setWishPayday(DateUtil.parseDate(payDate));
        } else {
            form.setWishPayday(new Date());
        }
        form.setIsQuick(row.getUrgencyFlag());
        form.setIsForindividual(row.getIsforindividual());


        form.setDifBank(row.getDifBank());
        form.setAreaSign(row.getAreaSign());

        form.setBursarBillNo(row.getVoucherNoErp());
        form.setPurpose(row.getAbs());
        form.setAddWord(row.getAbs());
        // 单据来源：6-ERP系统
        form.setBillFrom("6");
        // 付款业务处理状态：0-暂存
        form.setStatus(0);
        // 是否已付款默认0 0- 未付款，1-已付款
        form.setIsPayed("0");
        // 是否垫资，默认为0
        form.setIsAdvance("0");
        // 是否已选票据默认否
        form.setIsOpenBill("0");
        form.setBankTypeId(row.getBankTypeId());

        return form;
    }

    /**
     * 组装 ErpPayInfo 表信息
     *
     * @param bean
     * @return TODO
     */
    private ErpPayInfo buildErpPayInfo(PaymentReqVo bean) {
        ErpPayInfo payInfo = new ErpPayInfo();
        payInfo.setId(bean.getSerialNoErp());
        payInfo.setTenantId(bean.getTenantId());
        payInfo.setSerialNoErp(bean.getSerialNoErp());// 流水号
        payInfo.setVoucherNoErp(bean.getVoucherNoErp());// 凭证号
        payInfo.setCorpCode(ObjectUtil.isNotEmpty(bean.getCorpCode()) ? bean.getCorpCode() : null); // 单位代码
        payInfo.setOppCorpCode(bean.getPayeeCorpCode()); // 收方单位代码

        payInfo.setPayerAccNo(bean.getPayerAccNo());// 付款账号
        payInfo.setPayerAccName(bean.getPayerAccName()); // 付款户名
        payInfo.setPayerBankName(bean.getPayerBankName());// 付款方开户行

        payInfo.setOppBankAcc(bean.getPayeeAccNo()); // 收款人账号
        payInfo.setOppAccName(bean.getPayeeAccName()); // 收款人户名
        payInfo.setOppBankName(bean.getPayeeBank());// 收款人开户行
        payInfo.setPayeeCode(bean.getPayeeCode());// 收款人联行号
        payInfo.setOppProv(bean.getPayeeProv());// 收款方所在省
        payInfo.setOppCity(bean.getPayeeCity());// 收款方所在市


        // 是否同城 0,同城 1,异地
        payInfo.setAreaSign(bean.getAreaSign());
        // 行号的前三位判断是否跨行 0,同行 1,他行
        payInfo.setDifBank(bean.getDifBank());
        payInfo.setDataSource(bean.getDataSource());
        payInfo.setVoucherType(bean.getVoucherType());// 付款方式，默认外付网银
        payInfo.setCurCode(bean.getCur()); // 币种,默认人民币
        payInfo.setAmt(bean.getAmt()); // 金额
        payInfo.setIsforindividual(bean.getIsforindividual());// 对公标志
        // 1：加急；默认0不加急
        payInfo.setUrgencyFlag(bean.getUrgencyFlag());
        payInfo.setAbs(bean.getAbs());// 用途
        // payInfo.setRmk(bean.getAbs());// 摘要
        String payDate = bean.getWishPayday();
        if (ObjectUtil.isNotEmpty(payDate)) {
            payInfo.setWishPayday(DateUtil.parseDate(payDate));
        } else {
            payInfo.setWishPayday(new Date());
        }
        payInfo.setCreateDate(new Date()); // 创建时间
        payInfo.setStatus("1"); // 单据状态,已转移
        payInfo.setIsPayed("0");

        return payInfo;
    }

    // 校验必填字段
    public String checkData(PaymentReqVo paymentVo, String tenantId) {
        StringBuilder result = new StringBuilder(2000);

        // 先判断报文是否正确
        if (ObjectUtil.isEmpty(paymentVo)) {
            appendMsg(result, "请求报文格式不正确，解析失败！");
            return result.toString();
        }

        paymentVo.setTenantId(tenantId);

        // serialNoErp 不能为空，长度32，不能重复
        if (ObjectUtil.isEmpty(paymentVo.getSerialNoErp())) {
            appendMsg(result, "流水号不能为空");
        }
        if (ObjectUtil.isNotEmpty(paymentVo.getSerialNoErp()) && !paymentVo.getSerialNoErp().matches("[a-zA-Z0-9]+")) {
            appendMsg(result, "流水号只能是数字和字母");
        }
        if (ObjectUtil.isNotEmpty(paymentVo.getSerialNoErp()) && paymentVo.getSerialNoErp().length() > 32) {
            appendMsg(result, "流水号长度不能超过32位");
        }

        // voucherNoErp 不能为空，长度50
        if (ObjectUtil.isEmpty(paymentVo.getVoucherNoErp()) || ObjectUtil.isEmpty(paymentVo.getVoucherNoErp().trim())) {
            appendMsg(result, "付款单号不能为空");
        } else {
            paymentVo.setVoucherNoErp(paymentVo.getVoucherNoErp().trim());
        }
        if (ObjectUtil.isNotEmpty(paymentVo.getVoucherNoErp()) && paymentVo.getVoucherNoErp().length() > 25) {
            appendMsg(result, "付款单号长度不能超过25位");
        }

        // wishPayday 不能为空，格式YYYY-MM-DD
        if (ObjectUtil.isEmpty(paymentVo.getWishPayday())) {
            appendMsg(result, "期望支付日期不能为空");
        }
        if (ObjectUtil.isNotEmpty(paymentVo.getWishPayday()) && !paymentVo.getWishPayday().matches("\\d{4}-\\d{2}-\\d{2}")) {
            appendMsg(result, "期望支付日期格式不正确");
        }

        // payerAccNo 不能同时为空
        if (ObjectUtil.isEmpty(paymentVo.getPayerAccNo()) || ObjectUtil.isEmpty(paymentVo.getPayerAccNo().trim())) {
            appendMsg(result, "付款方银行账号不能为空");
        } else {
            paymentVo.setPayerAccNo(paymentVo.getPayerAccNo().trim());
        }

        // corpCode 不为空的时候，长度不能超过20位
        if (ObjectUtil.isNotEmpty(paymentVo.getCorpCode()) && paymentVo.getCorpCode().length() > 20) {
            appendMsg(result, "付款方单位代码长度不能超过20位");
        }

        // payerAccNo 不能为空的时候，长度不能超过32位
        if (ObjectUtil.isNotEmpty(paymentVo.getPayerAccNo()) && paymentVo.getPayerAccNo().length() > 50) {
            appendMsg(result, "付款方银行账号长度不能超过50位");
        }

        // amt 不能为空，并且大于0
        if (ObjectUtil.isEmpty(paymentVo.getAmt())) {
            appendMsg(result, "交易金额不能为空");
        }
        if (ObjectUtil.isNotEmpty(paymentVo.getAmt()) && paymentVo.getAmt().compareTo(BigDecimal.ZERO) <= 0) {
            appendMsg(result, "交易金额必须大于0");
        }

        // 金额必须满足NUMBER(15,2)格式 - 整数部分最多13位，小数部分最多2位
        if (ObjectUtil.isNotEmpty(paymentVo.getAmt()) &&
                (paymentVo.getAmt().toString().length() > 16 || // 总长度检查（包括小数点）
                        !paymentVo.getAmt().toString().matches("^\\d{1,13}(\\.\\d{1,2})?$"))) {
            appendMsg(result, "交易金额格式不正确");
        }

        // cur 不能为空
        if (ObjectUtil.isEmpty(paymentVo.getCur()) || ObjectUtil.isEmpty(paymentVo.getCur().trim())) {
            appendMsg(result, "币种不能为空");
        } else {
            paymentVo.setCur(paymentVo.getCur().trim());
        }

        // payeeAccNo 不能为空，长度30
        if (ObjectUtil.isEmpty(paymentVo.getPayeeAccNo()) || ObjectUtil.isEmpty(paymentVo.getPayeeAccNo().trim())) {
            appendMsg(result, "收款方账号不能为空");
        } else {
            paymentVo.setPayeeAccNo(paymentVo.getPayeeAccNo().trim());
        }
        if (ObjectUtil.isNotEmpty(paymentVo.getPayeeAccNo()) && paymentVo.getPayeeAccNo().length() > 50) {
            appendMsg(result, "收款方账号长度不能超过50位");
        }

        // payeeAccName 不能为空 长度150
        if (ObjectUtil.isEmpty(paymentVo.getPayeeAccName()) || ObjectUtil.isEmpty(paymentVo.getPayeeAccName().trim())) {
            appendMsg(result, "收款方账号名称不能为空");
        } else {
            paymentVo.setPayeeAccName(paymentVo.getPayeeAccName().trim());
        }
        if (ObjectUtil.isNotEmpty(paymentVo.getPayeeAccName()) && paymentVo.getPayeeAccName().length() > 150) {
            appendMsg(result, "收款方账号名称长度不能超过150位");
        }

        // payeeCode 不能为空 长度12 且存在系统
        if (ObjectUtil.isEmpty(paymentVo.getPayeeCode()) || ObjectUtil.isEmpty(paymentVo.getPayeeCode().trim())) {
            appendMsg(result, "收款方联行号不能为空");
        } else {
            paymentVo.setPayeeCode(paymentVo.getPayeeCode().trim());
        }

        // abs 不能为空 长度64
        if (ObjectUtil.isEmpty(paymentVo.getAbs()) || ObjectUtil.isEmpty(paymentVo.getAbs().trim())) {
            appendMsg(result, "摘要不能为空");
        } else {
            paymentVo.setAbs(paymentVo.getAbs().trim());
        }
        if (ObjectUtil.isNotEmpty(paymentVo.getAbs()) && paymentVo.getAbs().length() > 64) {
            appendMsg(result, "摘要长度不能超过64位");
        }

        // dataSource 不能为空，长度20
        if (ObjectUtil.isEmpty(paymentVo.getDataSource()) || ObjectUtil.isEmpty(paymentVo.getDataSource().trim())) {
            appendMsg(result, "数据来源不能为空");
        } else {
            paymentVo.setDataSource(paymentVo.getDataSource().trim());
        }
        if (ObjectUtil.isNotEmpty(paymentVo.getDataSource()) && paymentVo.getDataSource().length() > 20) {
            appendMsg(result, "数据来源长度不能超过20位");
        }

        // urgencyFlag 不为空的情况下校验
        if (ObjectUtil.isNotEmpty(paymentVo.getUrgencyFlag()) && ObjectUtil.isNotEmpty(paymentVo.getUrgencyFlag().trim())) {
            if (!"0".equals(paymentVo.getUrgencyFlag().trim()) && !"1".equals(paymentVo.getUrgencyFlag().trim())) {
                appendMsg(result, "紧急标识只能为0或1");
            }
        }

        // isforindividual 不为空的时候校验
        if (ObjectUtil.isNotEmpty(paymentVo.getIsforindividual()) && ObjectUtil.isNotEmpty(paymentVo.getIsforindividual().trim())) {
            if (!"0".equals(paymentVo.getIsforindividual().trim()) && !"1".equals(paymentVo.getIsforindividual().trim())) {
                appendMsg(result, "对公对私只能为0或1");
            }
        }

        // 如果 result 不为空，则直接返回校验结果 否则继续校验数据正确性
        if (ObjectUtil.isNotEmpty(result.toString())) {
            return result.toString();
        }

        // serialNoErp 不能重复
        ErpPayInfo erpPayInfo = erpPayInfoService.getBySerialNoErp(paymentVo.getSerialNoErp());
        if (ObjectUtil.isNotEmpty(erpPayInfo)) {
            appendMsg(result, "流水号已存在");
            return result.toString();
        }

        // corpCode 校验是否有效
        if (ObjectUtil.isNotEmpty(paymentVo.getCorpCode())) {
            SysCorp sysCorp = erpSysCorpService.getByCode(paymentVo.getTenantId(), paymentVo.getCorpCode());
            if (ObjectUtil.isEmpty(sysCorp)) {
                appendMsg(result, "单位代码无效");
                return result.toString();
            } else {
                paymentVo.setCorpId(sysCorp.getId());
            }
        }

        // cur 是否有效
        BtCurrency currency = erpBtCurrencyService.getByEnglishCode(paymentVo.getTenantId(), paymentVo.getCur());
        if (ObjectUtil.isEmpty(currency)) {
            appendMsg(result, "币种无效");
            return result.toString();
        } else {
            paymentVo.setCur(currency.getCurCode());
            paymentVo.setCurId(currency.getId());
        }

        // payerAccNo 校验付款账号是否有效
        if (ObjectUtil.isNotEmpty(paymentVo.getPayerAccNo())) {
            BankAccInfo bankAccInfo = erpBankAccInfoService.getByAccNo(paymentVo.getTenantId(), paymentVo.getPayerAccNo(), currency.getId());
            if (ObjectUtil.isEmpty(bankAccInfo)) {
                appendMsg(result, "对应币种的付款账号未维护");
                return result.toString();
            } else {
                paymentVo.setPayerAccId(bankAccInfo.getBankAccId());
                paymentVo.setPayerAccNo(bankAccInfo.getBankAcc());
                paymentVo.setPayerBankName(bankAccInfo.getBankName());
                paymentVo.setPayerAccName(bankAccInfo.getAccName());
                paymentVo.setPayerBankCode(bankAccInfo.getBankCode());
                paymentVo.setBankTypeId(bankAccInfo.getBankTypeId());

                paymentVo.setBifCode(bankAccInfo.getBifCode());
                paymentVo.setPayerProv(bankAccInfo.getProvId());
                paymentVo.setPayerCity(bankAccInfo.getCityId());
                paymentVo.setCorpId(bankAccInfo.getCorpId());
                SysCorp sysCorp = erpSysCorpService.getEntityById(paymentVo.getTenantId(), bankAccInfo.getCorpId());
                if (ObjectUtil.isNotEmpty(sysCorp)) {
                    paymentVo.setCorpCode(sysCorp.getCode());
                }
            }

            //  校验收付款账号是否一致，不能一致
            if (paymentVo.getPayerAccNo().equals(paymentVo.getPayeeAccNo())) {
                result.append("收款方账号不能与付款方账号一致");
                return result.toString();
            }
        }

        // 收方联行号校验
        InputBankInfo inputBankInfo = erpInputBankInfoService.getByCode(paymentVo.getPayeeCode());
        if (ObjectUtil.isEmpty(inputBankInfo)) {
            appendMsg(result, "联行号不存在,请先联系银行维护。");
            return result.toString();
        } else {
            paymentVo.setPayeeBank(inputBankInfo.getBankName());

            SysRegion sysRegion = erpSysRegionService.queryRegionByCity(paymentVo.getPayeeCode().substring(3, 7));
            if (ObjectUtil.isNotEmpty(sysRegion)) {
                paymentVo.setPayeeCity(sysRegion.getId());
                paymentVo.setPayeeProv(sysRegion.getParentId());
            }
        }

        // 付款方式
        if (ObjectUtil.isEmpty(paymentVo.getVoucherType()) || ObjectUtil.isEmpty(paymentVo.getVoucherType().trim())) {
            paymentVo.setVoucherType("34");
        }

        FcVoucherType fcVoucherType = erpFcVoucherTypeService.listVoucherTypeByType(paymentVo.getTenantId(), paymentVo.getVoucherType());
        if (ObjectUtil.isEmpty(fcVoucherType)) {
            appendMsg(result, "付款方式未分配");
            return result.toString();
        } else {
            paymentVo.setVoucherTypeId(fcVoucherType.getId());
        }
        // 是否紧急
        if (ObjectUtil.isEmpty(paymentVo.getUrgencyFlag()) || ObjectUtil.isEmpty(paymentVo.getUrgencyFlag().trim())) {
            paymentVo.setUrgencyFlag("0");
        }
        // 是否对私
        if (ObjectUtil.isEmpty(paymentVo.getIsforindividual()) || ObjectUtil.isEmpty(paymentVo.getIsforindividual().trim())) {
            paymentVo.setIsforindividual("0");
        }

        // 设置同城异地，同行跨行
        if (ObjectUtil.isEmpty(result.toString())) {//验证通过时
            // 商业银行根据开户行名称前缀匹配
            String payerBankprefix = paymentVo.getPayerBankCode().substring(0, 3);
            if (payerBankprefix.equals(paymentVo.getPayeeCode().substring(0, 3))) {
                // 城商行匹配
                List<String> bankPrefix = Arrays.asList(new String[]{"313", "314", "402"});
                if (bankPrefix.contains(payerBankprefix)) {
                    if (paymentVo.getPayerBankName().substring(0, 4).equals(paymentVo.getPayeeBank().substring(0, 4))) {
                        paymentVo.setDifBank("0");
                    } else {
                        paymentVo.setDifBank("1");
                    }
                } else {
                    paymentVo.setDifBank("0");
                }

            } else {
                paymentVo.setDifBank("1");
            }

            // 同城异地
            if (paymentVo.getPayerCity().equals(paymentVo.getPayeeCity())) {
                paymentVo.setAreaSign("0");
            } else {
                paymentVo.setAreaSign("1");
            }
        }

        return result.toString();
    }

    private void appendMsg(StringBuilder result, String msg) {
        if (result.length() > 0) {
            result.append(";");
        }
        result.append(msg);
    }
}
