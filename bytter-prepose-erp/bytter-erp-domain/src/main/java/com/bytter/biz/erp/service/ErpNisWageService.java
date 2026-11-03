package com.bytter.biz.erp.service;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import com.bytter.biz.erp.dao.ErpNisWageDao;
import com.bytter.biz.erp.entity.*;
import com.bytter.biz.erp.vo.ErpNisWageDetailVo;
import com.bytter.biz.erp.vo.ErpNisWageVo;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.core.utils.StringUtils;
import com.bytter.erp.common.constant.ErpConstant;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Base64Utils;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.util.*;

/**
 * 代发工资总表实现类
 *
 * @author fangcf
 * @date 2026-05-13
 */
@Slf4j
@Service
public class ErpNisWageService extends BaseService<NisWage, String> {

    @Autowired
    private ErpNisWageDao erpNisWageDao;
    @Autowired
    private ErpNisWageDetailService erpNisWageDetailService;
    @Autowired
    private ErpPayInfoService erpPayInfoService;
    @Autowired
    private ErpBankAccInfoService erpBankAccInfoService;
    @Autowired
    private ErpSysCorpService erpSysCorpService;
    @Autowired
    private ErpBtCurrencyService erpBtCurrencyService;
    @Autowired
    private ErpInputBankInfoService erpInputBankInfoService;
    @Autowired
    private ErpSysRegionService erpSysRegionService;

    @Override
    public BaseDao getEntityDao() {
        return erpNisWageDao;
    }

    @Transactional
    public Map<String, Object> handleNisWage(ErpNisWageVo vo) {
        // 返回实体map
        Map<String, Object> dataMap = new HashMap<>();
        dataMap.put("wageNo", vo.getWageNo());
        dataMap.put("status", ErpConstant.SERVER_SUCCESS_STATUS);

        // 全部验证通过才保存
        boolean isChecked = true;
        //返回明细校验结果
        List<Map<String, Object>> checkDtlList = new ArrayList<>();
        Map<String, Object> checkMap = new HashMap<>();
        for (ErpNisWageDetailVo erpNisWageDetail : vo.getNisWageDetailList()) {
            // 明细返回对象
            Map<String, Object> dtlMap = new HashMap<>();
            // 验证代发工资明细信息
            String checkResult = checkDataDetails(vo, erpNisWageDetail, checkMap);
            if (ObjectUtil.isNotEmpty(checkResult)) {// 验证不通过
                isChecked = false;// 验证不通过
                dtlMap.put("wageDtlNo", erpNisWageDetail.getWageDtlNo());
                dtlMap.put("status", ErpConstant.SERVER_FAIL_STATUS);
                dtlMap.put("message", checkResult);
            } else {
                dtlMap.put("wageDtlNo", erpNisWageDetail.getWageDtlNo());
                dtlMap.put("status", ErpConstant.SERVER_SUCCESS_STATUS);
            }
            // 返回明细校验结果
            checkDtlList.add(dtlMap);
        }

        dataMap.put("nisWageDetailList", checkDtlList);
        if (isChecked) {
            // 保存代发工资
            saveErpNisWage(vo);
            dataMap.put("status", ErpConstant.SERVER_SUCCESS_STATUS);
            dataMap.put("message", "成功");
        } else {
            dataMap.put("status", ErpConstant.SERVER_FAIL_STATUS);
            dataMap.put("message", "失败原因：明细没有全部验证通过，详情见明细信息。");
        }

        return dataMap;
    }

    /**
     * 代发工资交易结果查询
     *
     * @param tenantId  租户id
     * @param wageNo    工资单据号码
     * @param wageDtlNo 工资明细单号
     * @return 代发工资交易结果
     */
    public List<ErpNisWageResultView> listErpNisWageResult(String tenantId, String wageNo, String wageDtlNo) {
        return erpNisWageDao.listErpNisWageResult(tenantId, wageNo, wageDtlNo);
    }

    public void saveErpNisWage(ErpNisWageVo erpNisWage) {
        // 保存erpPayInfo表信息
        ErpPayInfo erpPayInfo = createErpPayInfo(erpNisWage);
        erpPayInfoService.saveEntity(erpPayInfo);

        // 保存代发工资总表
        NisWage nisWage = createNisWage(erpNisWage);
        nisWage.setErpId(erpPayInfo.getId());
        super.saveEntity(nisWage);

        // 保存代发工资明细
        List<NisWageDetail> nisWageDetailList = new ArrayList<>();
        for (ErpNisWageDetailVo erpNisWageDetail : erpNisWage.getNisWageDetailList()) {
            NisWageDetail nisWageDetail = createNisWageDetail(nisWage, erpNisWageDetail);
            nisWageDetailList.add(nisWageDetail);
        }
        erpNisWageDetailService.batchSave(nisWageDetailList);
    }

    public NisWageDetail createNisWageDetail(NisWage wage, ErpNisWageDetailVo erpNisWageDetail) {
        NisWageDetail wageDetail = new NisWageDetail();
        wageDetail.setTenantId(wage.getTenantId());
        wageDetail.setWageId(wage.getId());// 工资单号

        wageDetail.setWageDtlNo(erpNisWageDetail.getWageDtlNo());// 单据号码
        wageDetail.setWorkNo(erpNisWageDetail.getWorkNo());// 工号
        wageDetail.setUserName(erpNisWageDetail.getAccName());// 姓名
        wageDetail.setBankAcc(erpNisWageDetail.getBankAcc()); // 银行账号
        wageDetail.setAccName(erpNisWageDetail.getAccName());// 户名
        wageDetail.setBankCode(erpNisWageDetail.getBankCode());// 开户行行号
        wageDetail.setBankName(erpNisWageDetail.getBankName());// 开户行名称
        wageDetail.setCreateDate(new Date());

        String wages = erpNisWageDetail.getWages().toString();
        try {
            wages = Base64Utils.encodeToString((erpNisWageDetail.getWages().toString()).getBytes("UTF-8"));
        } catch (UnsupportedEncodingException e) {
            log.error("金额加密异常！");
        }
        wageDetail.setWagesVerify(wages);
        wageDetail.setPurpose(erpNisWageDetail.getPurpose());// 用途
        wageDetail.setDifBank(erpNisWageDetail.getDifBank());// 跨行标识 0-同行 1-他行
        wageDetail.setAreaSign(erpNisWageDetail.getAreaSign());// 地域标识 0-同城
        wageDetail.setProv(erpNisWageDetail.getProv());
        wageDetail.setCity(erpNisWageDetail.getCity());
        return wageDetail;
    }

    public NisWage createNisWage(ErpNisWageVo erpNisWage) {
        NisWage nisWage = new NisWage();
        nisWage.setTenantId(erpNisWage.getTenantId());
        nisWage.setWageNo(erpNisWage.getWageNo());// 单据号码
        nisWage.setWageTitle(erpNisWage.getWageTitle());

        nisWage.setCorpId(erpNisWage.getCorpId());// 单位id
        nisWage.setCurId(erpNisWage.getCurId());// 币别id
        nisWage.setPayerAcc(erpNisWage.getPayerAccNo());// 付款账号
        nisWage.setPayerAccName(erpNisWage.getPayerAccName());// 付款账号名称
        nisWage.setBifCode(erpNisWage.getBifCodeId());

        nisWage.setWages(erpNisWage.getWages());// 金额
        nisWage.setWageDay(DateUtil.parseDate(erpNisWage.getWageDay()));// 工资发放日期
        nisWage.setWishPayday(nisWage.getWageDay());// 期望支付日

        nisWage.setAddWord("erp代发工资");// 附言
        nisWage.setStatus(0);// 状态
        nisWage.setCreateDate(new Date());
        return nisWage;
    }

    private ErpPayInfo createErpPayInfo(ErpNisWageVo erpNisWage) {
        ErpPayInfo payInfo = new ErpPayInfo();
        payInfo.setTenantId(erpNisWage.getTenantId());
        payInfo.setCorpCode(erpNisWage.getCorpCode());
        payInfo.setWishPayday(DateUtil.parseDate(erpNisWage.getWageDay()));
        payInfo.setSerialNoErp(erpNisWage.getWageNo());
        payInfo.setVoucherNoErp(erpNisWage.getWageNo());
        payInfo.setPayerAccNo(erpNisWage.getPayerAccNo());
        payInfo.setPayerAccName(erpNisWage.getPayerAccName());
        payInfo.setPayerBankName(erpNisWage.getPayerBankName());
        payInfo.setDataSource(erpNisWage.getDataSource());
        payInfo.setAmt(erpNisWage.getWages());
        payInfo.setVoucherType("48");
        payInfo.setStatus("1");
        payInfo.setIsforindividual("1");
        payInfo.setCreateDate(new Date());
        payInfo.setOppBankAcc("");
        payInfo.setOppAccName("");
        payInfo.setOppBankName("");
        payInfo.setIsPayed("0");
        payInfo.setAbs("代发工资");

        return payInfo;
    }

    /**
     * 校验主表字段
     *
     * @param vo
     * @return TODO
     */
    public String checkData(ErpNisWageVo vo, String tenantId) {
        StringBuilder result = new StringBuilder(2000);

        // 先判断报文是否正确
        if (ObjectUtil.isEmpty(vo)) {
            appendMsg(result, "请求报文格式不正确，解析失败！");
            return result.toString();
        }

        vo.setTenantId(tenantId);

        // wageNo 不能为空，长度30 不能重复
        if (ObjectUtil.isEmpty(vo.getWageNo()) || ObjectUtil.isEmpty(vo.getWageNo().trim())) {
            appendMsg(result, "批次号不能为空");
        } else {
            vo.setWageNo(vo.getWageNo().trim());
        }
        if (ObjectUtil.isNotEmpty(vo.getWageNo()) && vo.getWageNo().length() > 25) {
            appendMsg(result, "批次号长度不能超过25位");
        }

        // dataSource 不能为空 长度20
        if (ObjectUtil.isEmpty(vo.getDataSource()) || ObjectUtil.isEmpty(vo.getDataSource().trim())) {
            appendMsg(result, "来源系统不能为空");
        } else {
            vo.setDataSource(vo.getDataSource().trim());
        }
        if (ObjectUtil.isNotEmpty(vo.getDataSource()) && vo.getDataSource().length() > 20) {
            appendMsg(result, "来源系统长度不能超过20位");
        }

        // wishPayday 不能为空，格式YYYY-MM-DD
        if (ObjectUtil.isEmpty(vo.getWageDay())) {
            appendMsg(result, "工资发放日期不能为空");
        }
        if (ObjectUtil.isNotEmpty(vo.getWageDay()) && !vo.getWageDay().matches("\\d{4}-\\d{2}-\\d{2}")) {
            appendMsg(result, "工资发放日期格式不正确");
        }

        // payerAccNo 不能为空
        if (ObjectUtil.isEmpty(vo.getPayerAccNo()) || ObjectUtil.isEmpty(vo.getPayerAccNo().trim())) {
            appendMsg(result, "付款方帐号不能为空");
        } else {
            vo.setPayerAccNo(vo.getPayerAccNo().trim());
        }

        if (ObjectUtil.isNotEmpty(vo.getCorpCode()) && vo.getCorpCode().length() > 20) {
            appendMsg(result, "付款方单位代码长度不能超过20位");
        }

        // payerAccNo 不能为空的时候，长度不能超过60位
        if (ObjectUtil.isNotEmpty(vo.getPayerAccNo()) && vo.getPayerAccNo().length() > 50) {
            appendMsg(result, "付款方帐号长度不能超过50位");
        }

        // wages 不能为空，并且大于0
        if (ObjectUtil.isEmpty(vo.getWages())) {
            appendMsg(result, "工资总额不能为空");
        }
        if (ObjectUtil.isNotEmpty(vo.getWages()) && vo.getWages().compareTo(BigDecimal.ZERO) <= 0) {
            appendMsg(result, "工资总额必须大于0");
        }

        // 金额必须满足NUMBER(15,2)格式 - 整数部分最多13位，小数部分最多2位
        if (ObjectUtil.isNotEmpty(vo.getWages()) &&
                (vo.getWages().toString().length() > 16 || // 总长度检查（包括小数点）
                        !vo.getWages().toString().matches("^\\d{1,13}(\\.\\d{1,2})?$"))) {
            appendMsg(result, "实发薪资格式不正确");
        }

        // cur 不能为空
        if (ObjectUtil.isEmpty(vo.getCurCode()) || ObjectUtil.isEmpty(vo.getCurCode().trim())) {
            appendMsg(result, "币种不能为空");
        } else {
            vo.setCurCode(vo.getCurCode().trim());
        }

        // wageTitle 不能为空 长度100
        if (ObjectUtil.isEmpty(vo.getWageTitle()) || ObjectUtil.isEmpty(vo.getWageTitle().trim())) {
            result.append("工资册标题不能为空;");
        } else {
            vo.setWageTitle(vo.getWageTitle().trim());
        }
        if (ObjectUtil.isNotEmpty(vo.getWageTitle()) && vo.getWageTitle().length() > 100) {
            appendMsg(result, "工资册标题长度不能超过100位");
        }

        if (ObjectUtil.isEmpty(vo.getNisWageDetailList())) {
            appendMsg(result, "工资明细不能为空");
        }

        // 如果 result 不为空，则直接返回校验结果 否则继续校验数据正确性
        if (ObjectUtil.isNotEmpty(result.toString())) {
            return result.toString();
        }

        // 校验金额是否一致
        BigDecimal total = vo.getNisWageDetailList().stream().map(ErpNisWageDetailVo::getWages).filter(ObjectUtil::isNotEmpty).reduce(BigDecimal.ZERO, BigDecimal::add);
        if (total.compareTo(vo.getWages()) != 0) {
            appendMsg(result, "总金额与明细金额相加不一致");
            return result.toString();
        }

        // cur 是否有效
        BtCurrency currency = erpBtCurrencyService.getByEnglishCode(vo.getTenantId(), vo.getCurCode());
        if (ObjectUtil.isEmpty(currency)) {
            appendMsg(result, "币种无效");
            return result.toString();
        } else {
            vo.setCurCode(currency.getCurCode());
            vo.setCurId(currency.getId());
        }

        // 校验批次是否重复传递
        NisWage wage = erpNisWageDao.getOne(vo.getTenantId(), vo.getWageNo());
        if (ObjectUtil.isNotEmpty(wage)) {
            appendMsg(result, "批次号已存在");
            return result.toString();
        }

        // 校验单位
        if (ObjectUtil.isNotEmpty(vo.getCorpCode())) {
            SysCorp corpInfo = erpSysCorpService.getByCode(vo.getTenantId(), vo.getCorpCode());
            if (ObjectUtil.isEmpty(corpInfo)) {
                appendMsg(result, "单位无效");
                return result.toString();
            } else {
                vo.setCorpId(corpInfo.getId());
            }
        }

        // 校验付款账号
        if (ObjectUtil.isNotEmpty(vo.getPayerAccNo())) {
            BankAccInfo bankAccInfo = erpBankAccInfoService.getByAccNo(vo.getTenantId(), vo.getPayerAccNo(), vo.getCurId());
            if (ObjectUtil.isNotEmpty(bankAccInfo)) {
                vo.setPayerAccNo(bankAccInfo.getBankAcc());
                vo.setPayerBankName(bankAccInfo.getBankName());
                vo.setPayerAccName(bankAccInfo.getAccName());
                vo.setPayerBankCode(bankAccInfo.getBankCode());

                vo.setBifCode(bankAccInfo.getBifCode());
                vo.setBifCodeId(bankAccInfo.getBifCodeId());
                vo.setPayerProv(bankAccInfo.getProvId());
                vo.setPayerCity(bankAccInfo.getCityId());
                vo.setCorpId(bankAccInfo.getCorpId());
                SysCorp sysCorp = erpSysCorpService.getEntityById(vo.getTenantId(), bankAccInfo.getCorpId());
                if (ObjectUtil.isNotEmpty(sysCorp)) {
                    vo.setCorpCode(sysCorp.getCode());
                }
            } else {
                appendMsg(result, "对应币种的付款账号未维护");
            }
        }

        return result.toString();
    }

    /**
     * 校验明细字段
     *
     * @param vo               ERP工资单vo
     * @param erpNisWageDetail ERP工资明细vo
     * @param checkMap         校验结果map
     * @return 校验结果
     */
    private String checkDataDetails(ErpNisWageVo vo, ErpNisWageDetailVo erpNisWageDetail, Map<String, Object> checkMap) {
        StringBuilder result = new StringBuilder(2000);

        // wageDtlNo 不为空，32位，不能重复
        if (ObjectUtil.isEmpty(erpNisWageDetail.getWageDtlNo()) || ObjectUtil.isEmpty(erpNisWageDetail.getWageDtlNo().trim())) {
            appendMsg(result, "明细流水号不能为空");
        } else {
            erpNisWageDetail.setWageDtlNo(erpNisWageDetail.getWageDtlNo().trim());
        }
        if (ObjectUtil.isNotEmpty(erpNisWageDetail.getWageDtlNo()) && erpNisWageDetail.getWageDtlNo().length() > 32) {
            appendMsg(result, "明细流水号长度不能超过32位");
        }

        // wages 不能为空，并且大于0‘
        if (ObjectUtil.isEmpty(erpNisWageDetail.getWages())) {
            appendMsg(result, "实发薪资不能为空");
        }
        if (ObjectUtil.isNotEmpty(erpNisWageDetail.getWages()) && erpNisWageDetail.getWages().compareTo(BigDecimal.ZERO) <= 0) {
            appendMsg(result, "实发薪资必须大于0");
        }

        // 金额必须满足NUMBER(15,2)格式 - 整数部分最多13位，小数部分最多2位
        if (ObjectUtil.isNotEmpty(vo.getWages()) &&
                (vo.getWages().toString().length() > 16 || // 总长度检查（包括小数点）
                        !vo.getWages().toString().matches("^\\d{1,13}(\\.\\d{1,2})?$"))) {
            appendMsg(result, "实发薪资格式不正确");
        }

        // bankAcc 不能为空 60位
        if (ObjectUtil.isEmpty(erpNisWageDetail.getBankAcc()) || ObjectUtil.isEmpty(erpNisWageDetail.getBankAcc().trim())) {
            appendMsg(result, "收款方账号不能为空");
        } else {
            erpNisWageDetail.setBankAcc(erpNisWageDetail.getBankAcc().trim());
        }
        if (ObjectUtil.isNotEmpty(erpNisWageDetail.getBankAcc()) && erpNisWageDetail.getBankAcc().length() > 50) {
            appendMsg(result, "收款方账号长度不能超过50位");
        }
        if (ObjectUtil.isNotEmpty(erpNisWageDetail.getBankAcc()) && vo.getPayerAccNo().equals(erpNisWageDetail.getBankAcc())) {
            appendMsg(result, "收款方账号不能与付款方账号一致");
        }

        // accName 不能为空 150位
        if (ObjectUtil.isEmpty(erpNisWageDetail.getAccName()) || ObjectUtil.isEmpty(erpNisWageDetail.getAccName().trim())) {
            appendMsg(result, "收款方名称不能为空");
        } else {
            erpNisWageDetail.setAccName(erpNisWageDetail.getAccName().trim());
        }
        if (ObjectUtil.isNotEmpty(erpNisWageDetail.getAccName()) && erpNisWageDetail.getAccName().length() > 150) {
            appendMsg(result, "收款方名称长度不能超过150位");
        }

        // bankCode 不能为空 12 位
        if (ObjectUtil.isEmpty(erpNisWageDetail.getBankCode()) || ObjectUtil.isEmpty(erpNisWageDetail.getBankCode().trim())) {
            appendMsg(result, "收款方联行号不能为空");
        } else {
            erpNisWageDetail.setBankCode(erpNisWageDetail.getBankCode().trim());
        }
        if (ObjectUtil.isNotEmpty(erpNisWageDetail.getBankCode()) && erpNisWageDetail.getBankCode().length() > 12) {
            appendMsg(result, "收款方联行号长度不能超过12位");
        }

        // purpose 不能为空，60 位
        if (ObjectUtil.isEmpty(erpNisWageDetail.getPurpose()) || ObjectUtil.isEmpty(erpNisWageDetail.getPurpose().trim())) {
            appendMsg(result, "附言不能为空");
        } else {
            erpNisWageDetail.setPurpose(erpNisWageDetail.getPurpose().trim());
        }
        if (ObjectUtil.isNotEmpty(erpNisWageDetail.getPurpose()) && erpNisWageDetail.getPurpose().length() > 60) {
            appendMsg(result, "附言长度不能超过60位");
        }

        // 如果 result 不为空，则直接返回校验结果 否则继续校验数据正确性
        if (ObjectUtil.isNotEmpty(result.toString())) {
            return result.toString();
        }
        // 收付款账号不能一致
        if (vo.getPayerAccNo().equals(erpNisWageDetail.getBankAcc())) {
            appendMsg(result, "收款方账号不能与付款方账号一致");
            return result.toString();
        }

        // 收方联行号校验
        InputBankInfo inputBankInfo = erpInputBankInfoService.getByCode(erpNisWageDetail.getBankCode());
        if (ObjectUtil.isEmpty(inputBankInfo)) {
            appendMsg(result, "联行号不存在,请先联系银行维护。");
            return result.toString();
        }

        erpNisWageDetail.setBankName(inputBankInfo.getBankName());

        SysRegion sysRegion = erpSysRegionService.queryRegionByCity(erpNisWageDetail.getBankCode().substring(3, 7));
        if (null != sysRegion) {
            erpNisWageDetail.setCity(sysRegion.getId());
            erpNisWageDetail.setProv(sysRegion.getParentId());
        }
        // 跨行标识 0-同行 1-他行
        if (StringUtils.isEmpty(vo.getPayerBankCode()) || StringUtils.isEmpty(erpNisWageDetail.getBankCode())) {
            erpNisWageDetail.setDifBank("0");
        } else {
            if (vo.getPayerBankCode().substring(0, 3).equals(erpNisWageDetail.getBankCode().substring(0, 3))) {
                erpNisWageDetail.setDifBank("0");
            } else {
                erpNisWageDetail.setDifBank("1");
            }
        }
        // 是否同城 0:同城；1:异地
        if (StringUtils.isEmpty(vo.getPayerCity()) || StringUtils.isEmpty(erpNisWageDetail.getCity())) {
            erpNisWageDetail.setAreaSign("0");
        } else {
            if (vo.getPayerCity().equals(erpNisWageDetail.getCity())) {
                erpNisWageDetail.setAreaSign("0");
            } else {
                erpNisWageDetail.setAreaSign("1");
            }
        }

        NisWageDetail nisWageDetail = erpNisWageDetailService.getByDtlNo(vo.getTenantId(), erpNisWageDetail.getWageDtlNo());
        if (ObjectUtil.isNotEmpty(nisWageDetail)) {
            appendMsg(result, "流水号重复");
            return result.toString();
        }

        if (checkMap.containsKey(erpNisWageDetail.getWageDtlNo())) {
            appendMsg(result, "流水号重复");
            return result.toString();
        }

        // 放入map 校验
        checkMap.put(erpNisWageDetail.getWageDtlNo(), erpNisWageDetail.getWageDtlNo());

        return result.toString();
    }

    private void appendMsg(StringBuilder result, String msg) {
        if (result.length() > 0) {
            result.append(";");
        }
        result.append(msg);
    }
}
