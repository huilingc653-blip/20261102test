package com.bytter.biz.foreign.service;

import com.bytter.biz.foreign.dao.FexCorpBankAccDtlDao;
import com.bytter.biz.foreign.entity.FexCorpBankAccDtl;
import com.bytter.biz.foreign.vo.FexCorpBankAccDtlVO;
import com.bytter.biz.foreign.vo.FexCorpBankAccVO;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.core.utils.BeanUtils;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.core.utils.StringUtils;
import com.bytter.core.constant.CommonConstant;
import com.bytter.biz.payment.api.constant.ForeignConstant;
import com.bytter.biz.payment.api.constant.NetbankConstant;
import com.bytter.system.api.dto.BtBankAccDTO;
import com.bytter.biz.payment.api.dto.FcVoucherTypeDTO;
import com.bytter.system.api.dto.BankTypeDTO;
import com.bytter.system.api.dto.BtCurrencyDTO;
import com.bytter.system.api.IBankTypeService;
import com.bytter.system.api.IBtBankAccService;
import com.bytter.system.api.IBtCurrencyService;
import com.bytter.biz.payment.api.service.IFcVoucherTypeService;
import com.bytter.system.api.dto.BtSwiftInfoDTO;
import com.bytter.system.api.dto.ExternalCorpBankAccDTO;
import com.bytter.system.api.dto.SysCountryCustDTO;
import com.bytter.system.api.IBisBifInitService;
import com.bytter.system.api.IBtSwiftInfoService;
import com.bytter.system.api.IExternalCorpService;
import com.bytter.system.api.ISysCountryCustService;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * @Title: FexCorpBankAccDtlService
 * @Description: 主体单位默认银行账户明细service
 * @author fusb
 * @date 2020-02-17 10:31:56
 * @version 1.0
 */
@Service("fexCorpBankAccDtlService")
@Transactional
public class FexCorpBankAccDtlService extends BaseService<FexCorpBankAccDtl,String> {

    @Autowired
    private FexCorpBankAccDtlDao fexCorpBankAccDtlDao;

    @DubboReference(check = false)
    private IFcVoucherTypeService fcVoucherTypeService;

    @DubboReference(check = false)
    private IBtBankAccService btBankAccService;

    @DubboReference(check = false)
    private IExternalCorpService externalCorpService;

    @DubboReference(check = false)
    private IBisBifInitService bisBifInitService;

    @DubboReference(check = false)
    private ISysCountryCustService sysCountryCustService;

    @DubboReference(check = false)
    private IBtSwiftInfoService btSwiftInfoService;

    @DubboReference(check = false)
    private IBankTypeService bankTypeService;

    @DubboReference(check = false)
    private IBtCurrencyService btCurrencyService;

    /**
     * 通过主表id查询主体单位默认银行账户明细VO
     * @param mainId
     * @return
     */
    @Transactional(readOnly = true)
    public List<FexCorpBankAccDtlVO> getFecFexCorpBankAccDtlVOById(String mainId){
        List<FexCorpBankAccDtlVO> list = fexCorpBankAccDtlDao.getFecFexCorpBankAccDtlVOById(mainId);
        fillBankTypeAndCurrencyName(list);
        return list;
    }

    /**
     * 根据币种ID补充明细币种名称。
     */
    private void fillCurrencyName(List<FexCorpBankAccDtlVO> list) {
        if (EmptyUtils.isEmpty(list)) {
            return;
        }
        List<String> curIds = new ArrayList<String>();
        for (FexCorpBankAccDtlVO vo : list) {
            if (vo != null && StringUtils.isNotBlank(vo.getCurId()) && !curIds.contains(vo.getCurId())) {
                curIds.add(vo.getCurId());
            }
        }
        Map<String, BtCurrencyDTO> currencyMap = EmptyUtils.isEmpty(curIds)
                ? Collections.<String, BtCurrencyDTO>emptyMap()
                : btCurrencyService.getCurrencyByIds(curIds);
        for (FexCorpBankAccDtlVO vo : list) {
            if (vo == null) {
                continue;
            }
            BtCurrencyDTO currency = EmptyUtils.isEmpty(currencyMap) ? null : currencyMap.get(vo.getCurId());
            if (currency != null) {
                vo.setCurName(StringUtils.isNotBlank(currency.getEnglishCode()) ? currency.getEnglishCode() : currency.getCurName());
            }
        }
    }

    /**
     * 根据银行类别ID和币种ID补充明细展示名称。
     */
    private void fillBankTypeAndCurrencyName(List<FexCorpBankAccDtlVO> list) {
        if (EmptyUtils.isEmpty(list)) {
            return;
        }
        List<String> bankTypeIds = new ArrayList<String>();
        List<String> curIds = new ArrayList<String>();
        for (FexCorpBankAccDtlVO vo : list) {
            if (vo == null) {
                continue;
            }
            if (StringUtils.isNotBlank(vo.getBankType()) && !bankTypeIds.contains(vo.getBankType())) {
                bankTypeIds.add(vo.getBankType());
            }
            if (StringUtils.isNotBlank(vo.getCurId()) && !curIds.contains(vo.getCurId())) {
                curIds.add(vo.getCurId());
            }
        }
        Map<String, BankTypeDTO> bankTypeMap = EmptyUtils.isEmpty(bankTypeIds)
                ? Collections.<String, BankTypeDTO>emptyMap()
                : bankTypeService.listBankTypeMapByIds(bankTypeIds);
        Map<String, BtCurrencyDTO> currencyMap = EmptyUtils.isEmpty(curIds)
                ? Collections.<String, BtCurrencyDTO>emptyMap()
                : btCurrencyService.getCurrencyByIds(curIds);
        for (FexCorpBankAccDtlVO vo : list) {
            if (vo == null) {
                continue;
            }
            BankTypeDTO bankType = EmptyUtils.isEmpty(bankTypeMap) ? null : bankTypeMap.get(vo.getBankType());
            if (bankType != null) {
                vo.setBankTypeName(bankType.getTypeName());
            }
            BtCurrencyDTO currency = EmptyUtils.isEmpty(currencyMap) ? null : currencyMap.get(vo.getCurId());
            if (currency != null) {
                vo.setCurName(StringUtils.isNotBlank(currency.getEnglishCode()) ? currency.getEnglishCode() : currency.getCurName());
            }
        }
    }

    /**
     * 通过主表id查询主体单位默认银行账户明细实体
     * @param mainId
     * @return
     */
    @Transactional(readOnly = true)
    public List<FexCorpBankAccDtl> getFecFexCorpBankAccDtlById(String mainId){
        return fexCorpBankAccDtlDao.getFexCorpBankAccDtlById(mainId);
    }

    /**
     * 保存主体单位默认银行账户明细信息
     * @param vo
     * @param mainId
     */
    public void saveFexBankAccDtl(FexCorpBankAccVO vo, String mainId) {
        List<FexCorpBankAccDtlVO> termDetails = vo.getFexCorpBankAccDtl();
        List<FexCorpBankAccDtl> entitys = new ArrayList<FexCorpBankAccDtl>();
        for (int i = 0;i < termDetails.size();i++) {
            if (StringUtils.isNotEmpty(termDetails.get(i).getBankAcc()) || StringUtils.isNotEmpty(termDetails.get(i).getPayValue())) {
                FexCorpBankAccDtl entity = new FexCorpBankAccDtl();
                BeanUtils.copyNotNullProperties(termDetails.get(i),entity);
                entity.setProId(vo.getProId());
                entity.setMainId(mainId);
                entitys.add(entity);
            }
        }
        fexCorpBankAccDtlDao.deleteFexCorpBankAccDtl(mainId);
        this.batchSave(entitys);
    }

    /**
     *@Description 根据主表id和银行类型查询主体单位默认银行账户明细
     *@Param mainId
     *@Param bankType
     *@return
     */
    @Transactional(readOnly = true)
    public List<FexCorpBankAccDtlVO> getFecFexCorpBankAccDtlList(String mainId,String bankType){
        return fexCorpBankAccDtlDao.getFecFexCorpBankAccDtlList(mainId,bankType);
    }

    /**
     * 通过主表id删除主体单位默认银行账户明细信息
     * @param mainId
     */
    public void deleteFexCorpBankAccDtl(String mainId){
        fexCorpBankAccDtlDao.deleteFexCorpBankAccDtl(mainId);
    }

    @Override
    public BaseDao<FexCorpBankAccDtl, String> getEntityDao() {
        return fexCorpBankAccDtlDao;
    }

    private void setExternalCorpBankAccInfo(FexCorpBankAccDtlVO vo) {
        ExternalCorpBankAccDTO bankAcc = getExternalCorpBankAccById(vo.getAccId());
        if (bankAcc != null) {
            vo.setBankType(bankAcc.getBankCountryId());
            vo.setBankTypeName(bankAcc.getBank());
        }
    }

    private ExternalCorpBankAccDTO getExternalCorpBankAccById(String id) {
        if (StringUtils.isBlank(id) || externalCorpService == null) {
            return null;
        }
        List<String> ids = new ArrayList<>();
        ids.add(id);
        Map<String, ExternalCorpBankAccDTO> map = externalCorpService.queryFexExternalCorpBankAccMapByIds(ids);
        return EmptyUtils.isEmpty(map) ? null : map.get(id);
    }

    /**
     * 根据查询条件获取账户信息
     * @param searchVo
     * @return
     */
    public List<FexCorpBankAccDtlVO> getBankAccToTran(FexCorpBankAccVO searchVo){
        List<FexCorpBankAccDtlVO> fexCorpBankAccDtlVOS = fexCorpBankAccDtlDao.getBankAccToTran(searchVo);
        for (FexCorpBankAccDtlVO fexCorpBankAccDtlVO : fexCorpBankAccDtlVOS) {
            if (ForeignConstant.BUS_TYPE_1.equals(fexCorpBankAccDtlVO.getBusType())) {
                setExternalCorpBankAccInfo(fexCorpBankAccDtlVO);
            }else{
                BtBankAccDTO bankAcc = btBankAccService.getBankAccById(fexCorpBankAccDtlVO.getAccId());
                if(bankAcc!=null) {
                    fexCorpBankAccDtlVO.setBankType(bankAcc.getBankTypeId());
                    fexCorpBankAccDtlVO.setBankTypeName(bankAcc.getBankTypeName());
                }
            }
        }
        fillCurrencyName(fexCorpBankAccDtlVOS);
        return fexCorpBankAccDtlVOS;
    }

    /**
     * 根据查询条件获取账户信息
     *
     * @param searchVo
     * @return
     */
    public List<FexCorpBankAccDtlVO> getNewBankAccToTran(FexCorpBankAccVO searchVo) {
        //交易对手不为空时，获取交易对手组
        if ("1".equals(searchVo.getBusType())) {
            if (StringUtils.isBlank(searchVo.getCorpId())) {
                return Collections.emptyList();
            }
        } else {
            //TODO 主体单位不一定需要
//            if (StringUtils.isBlank(searchVo.getOppCorpId())) {
//                return Collections.emptyList();
//            }
        }
        List<FexCorpBankAccDtlVO> fexCorpBankAccDtlVOS = fexCorpBankAccDtlDao.getNewBankAccToTran(searchVo);
        for (FexCorpBankAccDtlVO fexCorpBankAccDtlVO : fexCorpBankAccDtlVOS) {
            if (ForeignConstant.BUS_TYPE_1.equals(fexCorpBankAccDtlVO.getBusType())) {
                setExternalCorpBankAccInfo(fexCorpBankAccDtlVO);
            } else {
                BtBankAccDTO bankAcc = btBankAccService.getBankAccById(fexCorpBankAccDtlVO.getAccId());
                if (bankAcc != null) {
                    fexCorpBankAccDtlVO.setBankType(bankAcc.getBankTypeId());
                    fexCorpBankAccDtlVO.setBankTypeName(bankAcc.getBankTypeName());
                }
            }
        }
        fillCurrencyName(fexCorpBankAccDtlVOS);
        return fexCorpBankAccDtlVOS;
    }


    /**
     *
     * 根据主体银行账户id查询数据
     *
     * @param corpAcc
     * @return
     */
    public FexCorpBankAccDtlVO getCorpAccInfo(String corpAcc, String corpAccCurId){
        FexCorpBankAccDtlVO fexCorpBankAccDtlVO = new FexCorpBankAccDtlVO();
        BtBankAccDTO bankAccInfo = btBankAccService.getBankAccInfo(corpAcc);
        fexCorpBankAccDtlVO.setAccId(bankAccInfo.getId());
        fexCorpBankAccDtlVO.setBankAcc(bankAccInfo.getBankAcc());
        fexCorpBankAccDtlVO.setAccName(bankAccInfo.getAccName());
        fexCorpBankAccDtlVO.setCurId(corpAccCurId);
        fexCorpBankAccDtlVO.setAccType(bankAccInfo.getAccType());
        return fexCorpBankAccDtlVO;
    }

    /**
     * 根据查询条件获取账户信息
     * @param searchVo
     * @return
     */
    public FexCorpBankAccDtlVO getBankAccToTranUnique(FexCorpBankAccVO searchVo){
        List<FexCorpBankAccDtlVO> bankAccList = null;
        //资金调拨无需取规则里面的支付方式
        if(!ForeignConstant.TRAN_TYPE_3.equals(searchVo.getTranType())){
            bankAccList = this.getBankAccToTran(searchVo);
        }
        FexCorpBankAccDtlVO dtl = new FexCorpBankAccDtlVO();;
        if (!EmptyUtils.isEmpty(bankAccList)) {
            BeanUtils.copyNotNullProperties(bankAccList.get(0), dtl);
        }else{
            //则默认取系统结算方式为34-外付网银的
            FcVoucherTypeDTO voucherType = fcVoucherTypeService.getSysFcVoucherType(NetbankConstant.PAY_TYPE_34);
            if (voucherType != null) {
                dtl.setVoucherTypeId(voucherType.getId());
                dtl.setVoucherName(voucherType.getVoucherName());
            }
        }
        return dtl;
    }

    /**
     * 根据查询条件获取账户信息
     * @param searchVo
     * @return
     */
    public FexCorpBankAccDtlVO getNewBankAccToTranUnique(FexCorpBankAccVO searchVo){
        List<FexCorpBankAccDtlVO> bankAccList = null;
        //资金调拨无需取规则里面的支付方式
        if(!ForeignConstant.TRAN_TYPE_3.equals(searchVo.getTranType())){
            bankAccList = this.getNewBankAccToTran(searchVo);
        }
        FexCorpBankAccDtlVO dtl = new FexCorpBankAccDtlVO();;
        if (!EmptyUtils.isEmpty(bankAccList)) {
            BeanUtils.copyNotNullProperties(bankAccList.get(0), dtl);
        }else{
            //则默认取系统结算方式为34-外付网银的
            FcVoucherTypeDTO voucherType = fcVoucherTypeService.getSysFcVoucherType(NetbankConstant.PAY_TYPE_34);
            if (voucherType != null) {
                dtl.setVoucherTypeId(voucherType.getId());
                dtl.setVoucherName(voucherType.getVoucherName());
            }
        }
        if (StringUtils.isNotBlank(dtl.getBankAcc())) {
            setParamInfo(dtl);
        }
        return dtl;
    }

    /**
     * 参数设置
     *
     * @param dtl
     */
    public void setParamInfo(FexCorpBankAccDtlVO dtl) {
        FexCorpBankAccDtlVO bankAccInfo = fexCorpBankAccDtlDao.getBankAccInfo(dtl.getBankAcc());
        //根据银行类别过滤
        if(StringUtils.isNotBlank(bankAccInfo.getBifCode())) {
            dtl.setBifCode(bankAccInfo.getBifCode());
        }

        if (StringUtils.isNotBlank(bankAccInfo.getAccType())) {
            dtl.setAccType(bankAccInfo.getAccType());
        }

        if (StringUtils.isNotBlank(bankAccInfo.getBankTypeId())) {
            dtl.setBankTypeId(bankAccInfo.getBankTypeId());
        }

        if (StringUtils.isNotBlank(bankAccInfo.getBankCode())) {
            dtl.setBankCode(bankAccInfo.getBankCode());
        }

        if (StringUtils.isNotBlank(bankAccInfo.getLocalClearNo())) {
            dtl.setLocalClearNo(bankAccInfo.getLocalClearNo());
        }

        if (StringUtils.isNotBlank(bankAccInfo.getIsDomesticBank())) {
            dtl.setIsDomesticBank(bankAccInfo.getIsDomesticBank());
        }

        if (StringUtils.isNotBlank(bankAccInfo.getProv())) {
            dtl.setProv(bankAccInfo.getProv());
        }

        //境外银行获取国家地区名称
        if (CommonConstant.NO.equals(bankAccInfo.getIsDomesticBank()) && StringUtils.isNotBlank(bankAccInfo.getProv())) {
            SysCountryCustDTO sysCountryCustDTO = sysCountryCustService.getById(bankAccInfo.getProv());
            if (sysCountryCustDTO != null) {
                dtl.setProvName(sysCountryCustDTO.getNameZh());
                //简称
                dtl.setCountryEnCode(sysCountryCustDTO.getEnCode());
            }
        }
        //境外银行地址
        if (CommonConstant.NO.equals(bankAccInfo.getIsDomesticBank())&& StringUtils.isNotBlank(bankAccInfo.getBankCode())) {
            BtSwiftInfoDTO btSwiftInfoDTO = btSwiftInfoService.getByCode(bankAccInfo.getBankCode());
            if (btSwiftInfoDTO != null) {
                dtl.setBankAdd(btSwiftInfoDTO.getBankAdd());
            }
        }
    }
}
