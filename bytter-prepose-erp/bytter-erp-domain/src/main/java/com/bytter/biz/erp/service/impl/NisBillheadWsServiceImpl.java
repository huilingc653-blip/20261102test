package com.bytter.biz.erp.service.impl;

import com.bytter.biz.erp.base.AbstractCxfService;
import com.bytter.biz.erp.dao.ErpNisBillheadDao;
import com.bytter.biz.erp.dto.ErpNisBillheadDTO;
import com.bytter.biz.payment.api.dto.ErpPayInfoDTO;
import com.bytter.biz.payment.api.dto.NisBillheadDTO;
import com.bytter.biz.payment.api.service.IErpPayInfoService;
import com.bytter.biz.payment.api.service.INisBillheadService;
import com.bytter.core.utils.BeanUtils;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.core.utils.JsonUtils;
import com.bytter.core.utils.StringUtils;
import com.bytter.system.api.IBisBifInitService;
import com.bytter.system.api.IBtCurrencyService;
import com.bytter.system.api.ICorpService;
import com.bytter.system.api.IDictService;
import com.bytter.system.api.IInputBankInfoService;
import com.bytter.system.api.dto.BisBifInitDTO;
import com.bytter.system.api.dto.BtCurrencyDTO;
import com.bytter.system.api.dto.CorpDTO;
import com.bytter.system.api.dto.InputBankInfoDTO;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;

import javax.jws.WebService;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author yuwj
 */

@WebService(serviceName = "NisBillheadWs")
public class NisBillheadWsServiceImpl extends AbstractCxfService implements com.bytter.erp.service.NisBillheadWsService {
    @Autowired(required=false)
    private ErpNisBillheadDao erpNisBillheadDao;

    @DubboReference(check = false)
    private ICorpService corpService;
    @DubboReference(check = false)
    private INisBillheadService nisBillheadService;
    @DubboReference(check = false)
    private IInputBankInfoService inputBankInfoService;
    @DubboReference(check = false)
    private IBisBifInitService bisBifInitService;

    @DubboReference(check = false)
    private IBtCurrencyService btCurrencyServiceImpl;
    @DubboReference(check = false)
    private IErpPayInfoService erpPayInfoServiceImpl;
    @DubboReference(check = false)
    private IDictService dictService;

    @Override
    public String save(String params) {
        List<ErpNisBillheadDTO> list = JsonUtils.json2List(params, ErpNisBillheadDTO.class);
        if (EmptyUtils.isEmpty(list)) {
            throw new RuntimeException("报文体非json格式或格式不正确!");
        }
        //检验参数
        init(list);
        for (ErpNisBillheadDTO dto : list) {
            NisBillheadDTO nis = new NisBillheadDTO();
            ErpPayInfoDTO info = new ErpPayInfoDTO();

            this.setData(dto,info,nis);
            //保存中间表（erp_payInfo）
            String erpId = erpPayInfoServiceImpl.saveErpPayInfo(info);
            //保存付款表（nis_billhead）
            nis.setErpId(erpId);
            nisBillheadService.saveErpNisBillhead(nis);

        }
//        nisBillheadService.syncBatchSave(arrList);
        return success();
    }

    private void setData(ErpNisBillheadDTO dto, ErpPayInfoDTO info, NisBillheadDTO nis) {
        BeanUtils.copyNotNullProperties(dto,nis);
        //单据来源
        info.setDataSource(dto.getBillFrom());
        //外部系统id
        info.setSerialNoErp(dto.getSerialNoErp());
        //外部系统凭证号
        info.setVoucherNoErp(dto.getVoucherNoErp());
        nis.setBursarBillNo(dto.getVoucherNoErp());
        //单位编码
        info.setCorpCode(dto.getCorpCode());
        //金额
        info.setAmt(dto.getPayMoney());
        //币别
        info.setCurCode(dto.getCurCode());
        //付款方式
        info.setVoucherType(dto.getVoucherTypeCode());
        //摘要
        info.setAbs(dto.getPurpose());
        nis.setAddWord(dto.getPurpose());
        //对公对私标志
        info.setIsforindividual(dto.getIsForindividual());
        //是否加急
        info.setUrgencyFlag(dto.getIsQuick());
        //是否已支付
        info.setIsPayed("0");//默认未支付
        nis.setIsPayed("0");
        //状态
        info.setStatus("1");//默认为已转移（0未转移、1已转移、2删除打回）
        nis.setStatus(0);//默认录入
        //期望支付日期
        info.setWishPayday(dto.getWishPayday());
        info.setCreateDate(new Date());
        nis.setCreateDate(info.getCreateDate());
        info.setCreateBy("admin");
        nis.setCreateBy("admin");

        //付款方信息
        info.setPayerAccNo(dto.getPayerAcc());//付款帐号
        Map<Object, Object> map = erpNisBillheadDao.getVAccList(dto.getPayerAcc());
        String prov="",city="";
        if (null != map) {
            info.setPayerBankName(map.get("BANK_NAME").toString()); // 付款方开户行
            nis.setPayerBank(info.getPayerBankName());
            info.setPayerAccName(map.get("ACC_NAME").toString()); // 付款方户名
            nis.setPayerAccName(info.getPayerAccName());
            prov = map.get("PROV").toString();//付款省
            city = map.get("CITY").toString();//付方市
        }

        //收款方信息
        info.setOppBankAcc(dto.getPayeeAcc());//收款方账号
        info.setOppAccName(dto.getPayeeAccName());//收款方户名
        InputBankInfoDTO btBank = inputBankInfoService.getByBankCode(dto.getPayeeBankCode());
        //收款银联号
        info.setPayeeCode(btBank.getBankCode());
        // 带出收款方省
        info.setOppProv(btBank.getProv());
        nis.setProv(btBank.getProv());
        // 带出收款方市
        info.setOppCity(btBank.getCity());
        nis.setCity(btBank.getCity());
        // 带出收款方开户行
        info.setOppBankName(btBank.getBankName());
        nis.setPayeeBank(btBank.getBankName());
        if (!EmptyUtils.isBlank(btBank.getBankTypeId())) {
            // 根据银行类型带出任一接口代码
            List<BisBifInitDTO> bifList = bisBifInitService.listBySignOrBankType("1", btBank.getBankTypeId());
            if (!EmptyUtils.isEmptyList(bifList)) {
                nis.setBifCode(bifList.get(0).getId());
            }
        }

        // 是否同城 0,同城 1,异地
        if (!EmptyUtils.isEmpty(nis.getProv())
                && !EmptyUtils.isEmpty(nis.getCity())
                && !EmptyUtils.isEmpty(prov)
                && !EmptyUtils.isEmpty(city)) {
            String provCity = prov + city;// 付款方省市
            String sprovCity = nis.getProv() + nis.getCity();// 收款方省市
            if (provCity.equals(sprovCity)) {
                nis.setAreaSign("0");
                info.setAreaSign("0");
            } else {
                nis.setAreaSign("1");
                info.setAreaSign("1");
            }
        } else {
            nis.setAreaSign("0");
            info.setAreaSign("0");
        }

        // 行号的前三位判断是否跨行 0,同行 1,他行
        if (!EmptyUtils.isEmpty(dto.getPayerBankCode())
                && !EmptyUtils.isEmpty(nis.getPayeeBankCode())) {
            if (dto.getPayerBankCode().substring(0, 3)
                    .equals(nis.getPayeeBankCode().substring(0, 3))) {
                nis.setDifBank("0");
                info.setDifBank("0");
            } else {
                nis.setDifBank("1");
                info.setDifBank("1");
            }
        } else {
            nis.setDifBank("0");
            info.setDifBank("0");
        }

    }

    @Override
    public String delete(String params) {
        List<NisBillheadDTO> accList = JsonUtils.json2List(params, NisBillheadDTO.class);
        if (EmptyUtils.isEmpty(accList)) {
            throw new RuntimeException("报文体非json格式或格式不正确!");
        }
        for (NisBillheadDTO nisBillheadDTO : accList) {
            if(StringUtils.isEmpty(nisBillheadDTO.getErpId())||
                    StringUtils.isEmpty(nisBillheadDTO.getBillFrom()))
                throw new RuntimeException("ErpId或单据来源不能为空!");
        }
        nisBillheadService.syncBatchDelete(accList);
        return success();
    }

    @Override
    public String getVoucherStat(String params) {
        ErpNisBillheadDTO dto = JsonUtils.parseObject(params, ErpNisBillheadDTO.class);
        if (EmptyUtils.isEmpty(dto)) {
            throw new RuntimeException("报文体非json格式或格式不正确!");
        }
        if(StringUtils.isEmpty(dto.getSerialNoErp()))
            throw new RuntimeException("外部系统Id不能为空!");
        List<Map<Object,Object>> map = nisBillheadService.getVoucherStatByWs(dto.getSerialNoErp());
        List<Map<Object,Object>> list = new ArrayList<>();
        if(map!=null){
            for (Map<Object, Object> o : map) {
                Map<Object,Object> m = new HashMap<>();
                m.put("billNo",o.get("BILL_NO"));
                m.put("voucherStatus",o.get("STATUS"));
                m.put("statusDesc",o.get("STATE_DESC"));
                list.add(m);
            }
            return success(list);
        }else
            return failure(String.format("不存在外部系统Id为[%s]的数据！",dto.getSerialNoErp()));
    }

    private List<NisBillheadDTO> init(List<ErpNisBillheadDTO> list) {
        List<NisBillheadDTO> accList = new ArrayList<>();
        for (ErpNisBillheadDTO dto : list) {

            if (EmptyUtils.isEmpty(dto.getSerialNoErp())) {
                throw new RuntimeException("外部系统Id不能为空!");
            }else{
                //检验是否存在
                String id = erpNisBillheadDao.queryOnlyFile("erp_payinfo","id", "serial_no_erp", dto.getSerialNoErp());
                if(StringUtils.isNotEmpty(id))
                    throw new RuntimeException(String.format("外部系统id[%s]已存在",dto.getSerialNoErp()));
            }
            if (EmptyUtils.isEmpty(dto.getVoucherNoErp())) {
                throw new RuntimeException("外部系统凭证号不能为空!");
            }
            if (EmptyUtils.isEmpty(dto.getPayerAcc())) {
                throw new RuntimeException("付款账号不能为空!");
            } else {
                // 根据ERP系统付款账号查询资金系统银行帐号是否存在
                String bankacc = erpNisBillheadDao.queryOnlyFile("v_acc_list","bank_acc_id", "bank_acc", dto.getPayerAcc());
                if (EmptyUtils.isEmpty(bankacc)) {
                    throw new RuntimeException(String.format("付款账号是[%s]在资金系统不存在!",dto.getPayerAcc()));
                }else{
                    dto.setAccId(bankacc);//设置帐号id
                }

            }
            if (EmptyUtils.isEmpty(dto.getCorpCode())) {
                throw new RuntimeException("单位代码不能为空!");
            } else {
                // 根据单位代码获取单位ID
                CorpDTO corp = corpService.getCorpByCode(dto.getCorpCode());
                if (EmptyUtils.isEmpty(corp)) {
                    throw new RuntimeException(String.format("单位代码是[%s]在资金系统不存在!",dto.getCorpCode()));
                }
                dto.setCorpId(corp.getId());
            }
            if (EmptyUtils.isEmpty(dto.getVoucherTypeCode())) {
                throw new RuntimeException("交易类型不能为空!");
            } else {
                // 根据ERP系统交易类型查询资金系统是否存在
                String voucherType = erpNisBillheadDao.queryOnlyFile("fc_voucher_type", "ID", "voucher_type", dto.getVoucherTypeCode());
                if (EmptyUtils.isEmpty(voucherType)) {
                    throw new RuntimeException(String.format("交易类型是[%s]在资金系统不存在!",dto.getVoucherTypeCode()));
                }
                dto.setVoucherTypeId(voucherType);
            }
            if (EmptyUtils.isEmpty(dto.getPayMoney())) {
                throw new RuntimeException("金额不能为空!");
            } else {
                if (dto.getPayMoney().compareTo(new BigDecimal(0)) <= 0) {
                    throw new RuntimeException("付款金额必须大于0!");
                }
            }
            if (EmptyUtils.isEmpty(dto.getCurCode())) {
                BtCurrencyDTO rmbCurrency = btCurrencyServiceImpl.getRMBCurrency();
                dto.setCurId(rmbCurrency.getId());// ERP系统未传递币别代码默认 人民币
            } else {
                // 根据ERP系统币别查询资金系统是否存在
                BtCurrencyDTO currencyByCode = btCurrencyServiceImpl.getCurrencyByCode(dto.getCurCode());
                if (EmptyUtils.isEmpty(currencyByCode)) {
                    throw new RuntimeException(String.format("币别编码是[%s]在资金系统不存在!",dto.getCurCode()));
                }
                dto.setCurId(currencyByCode.getId());
            }

            if (!EmptyUtils.isEmpty(dto.getPayerBankCode())) {
                // 根据ERP系统收款联行号查询资金系统是否存在
                InputBankInfoDTO btBank = inputBankInfoService.getByBankCode(dto.getPayerBankCode());
                if (EmptyUtils.isEmpty(btBank)) {
                    throw new RuntimeException(String.format("付款联行号是[%s]在资金系统不存在!",dto.getPayerBankCode()));
                }
            }

            if (EmptyUtils.isEmpty(dto.getPayeeBankCode())) {
                throw new RuntimeException("收款联行号不能为空!");
            } else {
                if (dto.getPayeeBankCode().length() < 12) {
                    throw new RuntimeException("收款联行号长度不正确!");
                } else {
                    // 根据ERP系统收款联行号查询资金系统是否存在
                    InputBankInfoDTO btBank = inputBankInfoService.getByBankCode(dto.getPayeeBankCode());
                    if (EmptyUtils.isEmpty(btBank)) {
                        throw new RuntimeException(String.format("收款联行号是[%s]在资金系统不存在!",dto.getPayeeBankCode()));
                    }
                }
            }
            //收款人
            if (EmptyUtils.isEmpty(dto.getPayeeAccName())) {
                throw new RuntimeException("收款人不能为空!");
            }
            //收款账号
            if (EmptyUtils.isEmpty(dto.getPayeeAcc())) {
                throw new RuntimeException("收款账号不能为空!");
            }

            if (!EmptyUtils.isEmpty(dto.getPurpose())) {
                if (dto.getPurpose().length() > 30) {
                    throw new RuntimeException("用途长度不能超过30个字!");
                }
            }
            if (!EmptyUtils.isEmpty(dto.getIsForindividual())) {
                if (!"0".equals(dto.getIsForindividual())
                        && !"1".equals(dto.getIsForindividual())) {
                    throw new RuntimeException("对公,对私标识不正确(0对公付款  1 对私付款)!");
                }
            } else {
                dto.setIsForindividual("0");// ERP系统未传递对公对私标识 默认对公
            }
            if (EmptyUtils.isEmpty(dto.getBillFrom())) {
                throw new RuntimeException("单据来源代码不能为空!");
            }else{
                String s = dictService.get("nisBillhead.billFrom", dto.getBillFrom());
                if(EmptyUtils.isEmpty(s))
                    throw new RuntimeException(String.format("单据来源代码[%s]不存在!",dto.getBillFrom()));
            }
            //期望付款日期，不传则默认当天
            if(EmptyUtils.isEmpty(dto.getWishPayday()))
                dto.setWishPayday(new Date());
            //是否加急，不传则默认否
            if(EmptyUtils.isEmpty(dto.getIsQuick()))
                dto.setIsQuick("0");



//            NisBillheadDTO entity = new NisBillheadDTO();
//            BeanUtils.copyNotNullProperties(dto, entity);
//
//            // 校验开户行
//            if (!EmptyUtils.isBlank(dto.getPayeeBankCode())) {
//                InputBankInfoDTO btBank = inputBankInfoService.getByBankCode(dto.getPayeeBankCode());
//                if (EmptyUtils.isEmpty(btBank)) {
//                    throw new RuntimeException(String.format("收款账户的开户行号为[%s],不存在!", dto.getPayeeBankCode()));
//                }
//                entity.setPayeeBankCode(btBank.getBankCode());
//                entity.setPayeeAccName(btBank.getBankName());
//                // 带出省
//                entity.setProv(btBank.getProv());
//                // 带出市
//                entity.setCity(btBank.getCity());
//                // 带出银行类别
//                entity.setBankTypeId(btBank.getBankTypeId());
//                if (!EmptyUtils.isBlank(btBank.getBankTypeId())) {
//                    // 根据银行类型带出任一接口代码
//                    List<BisBifInitDTO> bifList = bisBifInitService.listBySignOrBankType("1", btBank.getBankTypeId());
//                    if (!EmptyUtils.isEmptyList(bifList)) {
//                        entity.setBifCode(bifList.get(0).getId());
//                    }
//                }
//            }
//
//            // -------------默认值-------------------
//            entity.setStatus(CommonConstant.STATUE_TEMP);
//            //是否加急
//            entity.setIsQuick(CommonConstant.NO);
//            accList.add(entity);
        }
        return accList;
    }
}
