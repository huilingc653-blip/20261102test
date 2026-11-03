package com.bytter.system.service.impl;
import cn.hutool.core.util.ObjectUtil;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.constant.CommonConstant;
import com.bytter.core.sequence.SequenceSingleton;
import com.bytter.core.sequence.enums.SequenceEnum;
import com.bytter.core.utils.BeanUtils;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.core.utils.SqlUtil;
import com.bytter.core.utils.StringUtil;
import com.bytter.system.api.*;
import com.bytter.system.api.dto.*;
import com.bytter.system.api.vo.BtBankAccAppVO;
import com.bytter.system.constants.BtBankAccConstant;
import com.bytter.system.constants.ResourceConstant;
import com.bytter.system.dao.BtBankAccAppDao;
import com.bytter.system.entity.BtBankAccApp;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service("btBankAccAppServiceI")
@DubboService
@Transactional
public class BtBankAccAppServiceImpl extends BaseService<BtBankAccApp, String> implements IBtBankAccAppService {
    @Autowired
    private BtBankAccAppDao btBankAccAppDao;
    @Autowired
    private ICorpService corpServiceI;
    @Autowired
    private IBtBankAccCommonService btBankAccCommonServiceI;
    @Autowired
    private IRegionService regionService;
    @Autowired
    private IDictService dictServiceI;
    @Autowired
    private IBtBankAccAppCurService btBankAccAppCurService;
    @Autowired
    private IBtCurrencyService btCurrencyServiceI;
    @Autowired
    private IBankTypeService bankTypeService;


    @Override
    public BaseDao<BtBankAccApp, String> getEntityDao() {
        return btBankAccAppDao;
    }
    @Override
    public DataTablesOutput getDataTablesUnion(DataTablesInput dataTablesInput, String joinSql, String busType) {
        DataTablesOutput output =  btBankAccAppDao.getDataTablesUnion(dataTablesInput, joinSql, busType);
        output = this.setJoinField(output);
        btBankAccCommonServiceI.setCurName(output);
        return output;
    }
    /**
     * 设置付款办理的关联字段
     * @param output
     * @return
     */
    @Override
    public DataTablesOutput setJoinField(DataTablesOutput output) {
        if(!EmptyUtils.isEmpty(output.getData())) {
            List<BtBankAccAppDTO> voList = output.getData();
            Map<String, RegionDTO> regionMap = regionService.getRegionMap();
            Map<String, Map<String, DictDTO>> listDictMap = dictServiceI.listDictMap(BtBankAccConstant.BANKACC_ACCTYPE_DICT,
                    BtBankAccConstant.BANKACC_ISDOMESTICBANK_DICT,CommonConstant.COMMON_SF_YN_DICT);
            Map<String, CorpDTO> corpMap = corpServiceI.getCorpMap();
            for(BtBankAccAppDTO dto:voList){
                RegionDTO region = regionMap.get(dto.getCity());
                if (region != null) {
                    dto.setProvName(region.getProvName());
                    dto.setCityName(region.getName());
                }
                //字典注入
                this.injectValues(dto, listDictMap);
                BankTypeDTO bankTypeDTO = bankTypeService.getObjectById(dto.getBankTypeId());
                if(!EmptyUtils.isEmpty(bankTypeDTO)){
                    dto.setBankTypeName(bankTypeDTO.getTypeName());
                }
                if(!EmptyUtils.isEmpty(corpMap)){
                    if(corpMap.containsKey(dto.getCorpId())){
                        CorpDTO corpDTO = corpMap.get(dto.getCorpId());
                        dto.setCorpName(corpDTO !=null ? corpDTO.getName(): "");
                    }
                }
            }
        }
        return output;
    }
    @Override
    public String getSequence(SequenceEnum sequenceEnum) {
        return SequenceSingleton.getSequence(SequenceEnum.ACC_NO);
    }
    /**
     * 付款信息字典注入
     * @param dto
     * @param listDictMap
     */
    private void injectValues(BtBankAccAppDTO dto, Map<String, Map<String, DictDTO>> listDictMap) {
        // 账户类别
        Optional.ofNullable(dto.getAccType()).ifPresent(accType -> {
            Map<String, DictDTO> dictDtoMap = listDictMap.get(BtBankAccConstant.BANKACC_ACCTYPE_DICT);
            Optional.ofNullable(dictDtoMap).flatMap(dictMap -> Optional.ofNullable(dictMap.get(accType)))
                    .ifPresent(dict -> dto.setAccTypeName(dict.getName()));
        });
        // 境内/境外账号
        Optional.ofNullable(dto.getIsDomesticBank()).ifPresent(isDomesticBank -> {
            Map<String, DictDTO> dictDtoMap = listDictMap.get(BtBankAccConstant.BANKACC_ISDOMESTICBANK_DICT);
            Optional.ofNullable(dictDtoMap).flatMap(dictMap -> Optional.ofNullable(dictMap.get(isDomesticBank)))
                    .ifPresent(dict -> dto.setIsDomesticBankName(dict.getName()));
        });
        // 电票专户
        Optional.ofNullable(dto.getElectricBill()).ifPresent(electricBill -> {
            Map<String, DictDTO> dictDtoMap = listDictMap.get(CommonConstant.COMMON_SF_YN_DICT);
            Optional.ofNullable(dictDtoMap).flatMap(dictMap -> Optional.ofNullable(dictMap.get(electricBill)))
                    .ifPresent(dict -> dto.setElectricBillName(dict.getName()));
        });
        // 是否离岸账号
        Optional.ofNullable(dto.getIsOffshoreAccount()).ifPresent(isOffshoreAccount -> {
            Map<String, DictDTO> dictDtoMap = listDictMap.get(CommonConstant.COMMON_SF_YN_DICT);
            Optional.ofNullable(dictDtoMap).flatMap(dictMap -> Optional.ofNullable(dictMap.get(isOffshoreAccount)))
                    .ifPresent(dict -> dto.setIsOffshoreAccount(dict.getName()));
        });
    }
    @Override
    public DataTablesOutput getDataTablesUnion(DataTablesInput dataTablesInput, ICurrentUser user) {
        StringBuilder sql = new StringBuilder();
        String corpAuthSql = SqlUtil.getInSql(
                corpServiceI.getUserCorpIds(ResourceConstant.NETBANK_BANKACC_BTBANKACCAPP_QUERY, user.getId()), "acc.corp_id",
                true);
        sql.append(" and ").append(corpAuthSql);
        sql.append(" and (acc.status = " + BtBankAccConstant.STATUE_TEMP + " or acc.status = " + BtBankAccConstant.STATUE_BACK + ") ");
        DataTablesOutput output = this.getDataTablesUnion(dataTablesInput, sql.toString(), BtBankAccConstant.BUSTYPE_1);
        return output;
    }
    @Override
    public DataTablesOutput dataTableApprove(DataTablesInput dataTablesInput, String businessSql) {
        DataTablesOutput output =  btBankAccAppDao.dataTableApprove(dataTablesInput, businessSql);
        output = this.setJoinField(output);
        this.btBankAccCommonServiceI.setCurName(output);
        return output;
    }
    @Override
    public int getBankAccAppCountByCorpId(String corpId) {
        return btBankAccAppDao.getBankAccAppCountByCorpId(corpId);
    }
    @Override
    public BtBankAccAppDTO getBankAccApp(String id) {
        BtBankAccAppVO bankAccAppVO = btBankAccAppDao.getBankAccApp(id);
        this.setJoinField(bankAccAppVO);
        BtBankAccAppDTO btBankAccAppDTO = new BtBankAccAppDTO();
        BeanUtils.copyNotNullProperties(bankAccAppVO, btBankAccAppDTO);
        return btBankAccAppDTO;
    }
    public void setJoinField(BtBankAccAppVO vo) {
        if (!StringUtil.isBlank(vo.getProv())) {
            RegionDTO prov = regionService.getById(vo.getProv());
            vo.setProvName(prov == null ? "" : prov.getName());
        }
        if (!StringUtil.isBlank(vo.getCity())) {
            RegionDTO city = regionService.getById(vo.getCity());
            vo.setCityName(city == null ? "" : city.getName());
        }
        Map<String, DictDTO> accTypeMap = dictServiceI.listMap(BtBankAccConstant.BANKACC_ACCTYPE_DICT);
        Map<String, DictDTO> isDomesticMap = dictServiceI.listMap(BtBankAccConstant.BANKACC_ISDOMESTICBANK_DICT);
        Map<String, DictDTO> electricBillMap = dictServiceI.listMap("sf_yn");
        vo.setAccTypeName(accTypeMap.containsKey(vo.getAccType()) ? accTypeMap.get(vo.getAccType()).getName():"");
        vo.setIsDomesticBankName(isDomesticMap.containsKey(vo.getIsDomesticBank()) ? isDomesticMap.get(vo.getIsDomesticBank()).getName():"");
        vo.setElectricBillName(electricBillMap.containsKey(vo.getElectricBill()) ? electricBillMap.get(vo.getElectricBill()).getName():"");
        // 币种
        List<BtBankAccAppCurDTO> list = btBankAccAppCurService.listByAppId(vo.getId());
        StringBuilder curName = new StringBuilder();
        StringBuilder curId = new StringBuilder();
        Map<String, BtCurrencyDTO> curMap = btCurrencyServiceI.getCurrency();
        for (BtBankAccAppCurDTO cur:list) {
            if (curMap.containsKey(cur.getCurId())) {
                if (StringUtil.isBlank(curName.toString())) {
                    curId.append(cur.getCurId());
                    curName.append(curMap.get(cur.getCurId()).getCurName());
                } else {
                    curId.append(CommonConstant.COMMA_SPLIT).append(cur.getCurId());
                    curName.append(CommonConstant.COMMA_SPLIT).append(curMap.get(cur.getCurId()).getCurName());
                }
            }
        }
        vo.setCurId(curId.toString());
        vo.setCurName(curName.toString());
        BankTypeDTO bankTypeDTO = bankTypeService.getObjectById(vo.getBankTypeId());
        if(!EmptyUtils.isEmpty(bankTypeDTO)){
            vo.setBankTypeName(bankTypeDTO.getTypeName());
        }
        // 单据类型
        if (ObjectUtil.isNotEmpty(vo.getReceiptCode())){
            List<String> codes = Arrays.asList(vo.getReceiptCode().split(","));
            List<Map<String,String>> mapList = btBankAccAppDao.getReceiptList(codes);
            String names = "";
            for (String code : codes){
                for (Map<String,String> map : mapList){
                    if (code.equals(map.get("receiptCode"))){
                        if (ObjectUtil.isEmpty(names)){
                            names += map.get("receiptName");
                        }else{
                            names += "," + map.get("receiptName");
                        }
                    }
                }
            }
            vo.setReceiptName(names);
        }
        // 账户组
        if (ObjectUtil.isNotEmpty(vo.getFcAccGroupCode())){
            List<String> codes = Arrays.asList(vo.getFcAccGroupCode().split(","));
            List<Map<String,String>> mapList = btBankAccAppDao.getAccGroupList(codes);
            String names = "";
            for (String code : codes){
                for (Map<String,String> map : mapList){
                    if (code.equals(map.get("fcAccGroupCode"))){
                        if (ObjectUtil.isEmpty(names)){
                            names += map.get("fcAccGroupName");
                        }else{
                            names += "," + map.get("fcAccGroupName");
                        }
                    }
                }
            }
            vo.setFcAccGroupName(names);
        }
    }
    @Override
    public BtBankAccAppDTO getById(String id) {
        BtBankAccApp entity = btBankAccAppDao.get(id, true);
        if (EmptyUtils.isEmpty(entity)) {
            return null;
        }
        BtBankAccAppDTO btBankAccAppDTO = new BtBankAccAppDTO();
        BeanUtils.copyNotNullProperties(entity, btBankAccAppDTO);
        return btBankAccAppDTO;
    }
    @Override
    public void saveOrUpdate(BtBankAccAppDTO btBankAccAppDTO) {
        BtBankAccApp bankAccApp = new BtBankAccApp();
        BeanUtils.copyNotNullProperties(btBankAccAppDTO, bankAccApp);
        this.btBankAccAppDao.saveOrUpdate(bankAccApp);
    }
    @Override
    public BtBankAccAppDTO save(BtBankAccAppDTO btBankAccAppDTO) {
        BtBankAccApp bankAccApp = new BtBankAccApp();
        BeanUtils.copyNotNullProperties(btBankAccAppDTO, bankAccApp);
        this.btBankAccAppDao.save(bankAccApp);
        btBankAccAppDTO.setId(bankAccApp.getId());
        return btBankAccAppDTO;
    }
    @Override
    public void deleteEntity(BtBankAccAppDTO btBankAccAppDTO) {
        this.btBankAccAppDao.delete(btBankAccAppDTO.getId());
    }
}
