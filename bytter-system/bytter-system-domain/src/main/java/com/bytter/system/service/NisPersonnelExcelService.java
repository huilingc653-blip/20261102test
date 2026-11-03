package com.bytter.system.service;

import com.bytter.basic.entity.BisBifInit;
import com.bytter.basic.service.BisBifInitService;
import com.bytter.basic.service.BtInputBankInfoService;
import com.bytter.basic.vo.BtInputBankInfoVO;
import com.bytter.biz.payment.api.service.NisPersonnelService;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.core.utils.IdcardUtils;
import com.bytter.core.utils.StringUtils;
import com.bytter.poi.excel.entity.result.ExcelVerifyHanlderResult;
import com.bytter.poi.handler.inter.IExcelVerifyHandler;
import com.bytter.system.api.ICorpDeleteVerifyService;
import com.bytter.system.api.constant.CorpVerifyMessageConstant;
import com.bytter.system.api.constant.SysConfigConstant;
import com.bytter.system.api.dto.CorpVerifyErrorDTO;
import com.bytter.system.entity.Corp;
import com.bytter.system.entity.Region;
import com.bytter.system.vo.DictVO;
import com.bytter.system.vo.v1.NisPersonnelExcelVO;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class NisPersonnelExcelService implements IExcelVerifyHandler<NisPersonnelExcelVO>, ICorpDeleteVerifyService {
    @DubboReference
    private NisPersonnelService nisPersonnelService;
    @Autowired
    private CorpService corpService;
    @Autowired
    private MultiLangService multiLangService;
    @Autowired
    private DictService dictService;
    @Autowired
    private RegionService regionService;
    @Autowired
    private BtInputBankInfoService inputBankInfoService;
    @Autowired
    private BisBifInitService bisBifInitService;

    @Override
    public ExcelVerifyHanlderResult verifyHandler(NisPersonnelExcelVO vo) {
        List<String> msgList = new ArrayList<>();
        // 判断编码是否重复
        boolean result = nisPersonnelService.isRepeatWorkNo(vo.getId(), vo.getWorkNo());
        if (result) {
            msgList.add("nis.personneldetail.message.workno.unique");
        }
        // 判断身份证号是否重复
        boolean resultCard = nisPersonnelService.isRepeatIdcard(vo.getId(), vo.getIdcard());
        if (resultCard) {
            msgList.add("nis.personneldetail.message.idcard.unique");
        }
        // 检验身份证号是否正确
        if (!EmptyUtils.isEmpty(vo.getIdcard()) && !IdcardUtils.validateCard(vo.getIdcard())) {
            msgList.add("nis.netbank.message.idcard");
        }
        // 判断单位在系统中是否存在
        Corp corp = corpService.getCorpByName(vo.getCorpName());
        if (EmptyUtils.isEmpty(corp) || EmptyUtils.isBlank(corp.getId())) {
            msgList.add("nis.netbank.message.corp.null");
        } else {
            vo.setCorpId(corp.getId());
        }
        // 判断开户行在系统中是否存在
        if (!EmptyUtils.isBlank(vo.getBankCode())) {
            BtInputBankInfoVO btBankDTO = this.inputBankInfoService.getByBankCodeOrBankNameAndImport(vo.getBankCode(), null);
            if (EmptyUtils.isEmpty(btBankDTO)) {
                msgList.add("nis.netbank.message.bank.null");
            } else {
                vo.setBankCode(btBankDTO.getBankCode());
                vo.setBankName(btBankDTO.getBankName());
            }
        }
        // 判断接口代码在系统中是否存在
        if (!EmptyUtils.isBlank(vo.getBifName())) {
            BisBifInit bisBifInitDTO = bisBifInitService.getByName(vo.getBifName());
            if (EmptyUtils.isEmpty(bisBifInitDTO)) {
                msgList.add("nis.netbank.message.bisbifinit.null");
            } else {
                vo.setBifCode(bisBifInitDTO.getId());
            }
        }
        // 判断账户类型
        if (!EmptyUtils.isBlank(vo.getCardTypeName())) {
            DictVO dictVO = dictService.getDictByDictGroupAndName("nisPersonnelDetail.cardType", vo.getCardTypeName());
            if (dictVO == null || EmptyUtils.isBlank(dictVO.getCode())) {
                msgList.add("nis.netbank.message.cardtype.mismatch");
            } else {
                vo.setCardType(dictVO.getCode());
            }
        }
        // 判断省市在系统中是否存在
        Region provRegion = regionService.getByName(vo.getProvName(), true);
        Region cityRegion = regionService.getByName(vo.getCityName(), false);
        if (EmptyUtils.isEmpty(provRegion)) {
            msgList.add("nis.netbank.message.provname.null");
        } else if (EmptyUtils.isEmpty(cityRegion)) {
            msgList.add("nis.netbank.message.cityname.null");
        } else {
            //判断省市是否一致
            if (!cityRegion.getParentId().equals(provRegion.getId())) {
                msgList.add("nis.netbank.message.cityname.mismatch");
            } else {
                vo.setProv(provRegion.getId());
                vo.setCity(cityRegion.getId());
            }
        }
        if (EmptyUtils.isEmptyList(msgList)) {
            return new ExcelVerifyHanlderResult(true, msgList);
        } else {
            return new ExcelVerifyHanlderResult(false, msgList);
        }
    }

    @Override
    public void verifyHandler(List<NisPersonnelExcelVO> list) {

    }

    @Override
    public List<CorpVerifyErrorDTO> verifyCorpDelete(String corpId) {
        if (StringUtils.isNotEmpty(corpId)) {
            List<CorpVerifyErrorDTO> verifyErrorDTOS = new ArrayList<CorpVerifyErrorDTO>();
            //单位有维护职工信息，不允许删除！
            int num = nisPersonnelService.getPersonnelCountByCorpId(corpId);
            if (num > 0) {
                verifyErrorDTOS.add(new CorpVerifyErrorDTO(corpId,
                        SysConfigConstant.Module.NIS,
                        SysConfigConstant.BusType.NIS_PERSONNEL,
                        multiLangService.getLang(CorpVerifyMessageConstant.NIS_VALID_DEL_PERSONNEL_ERROR)));
            }
            return verifyErrorDTOS;
        }
        return null;
    }
}
