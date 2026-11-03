package com.bytter.basic.service;
import com.bytter.basic.dao.BtPurposeDao;
import com.bytter.basic.entity.*;
import com.bytter.basic.vo.BtPurposeCorpVO;
import com.bytter.basic.vo.BtPurposeVO;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.exception.ParameterException;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.constant.CommonConstant;
import com.bytter.core.utils.*;
import com.bytter.poi.excel.entity.result.ExcelVerifyHanlderResult;
import com.bytter.poi.handler.inter.IExcelVerifyHandler;
import com.bytter.system.api.*;
import com.bytter.system.api.dto.*;
import com.bytter.system.api.enums.PurposeMessageEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.*;
/**
 * @author onlineGenerator
 * @version 1.0
 * @Title: Service
 * @Description: 用途维护表
 * @date 2017-05-24 09:57:15
 */
@Service("btPurposeService")
@Transactional
public class BtPurposeService extends BaseService<BtPurpose, String> implements IExcelVerifyHandler<BtPurposeVO> {
    @Autowired
    private BtPurposeDao btPurposeDao;
    @Autowired
    //@Qualifier("btPurposeCorpServiceBase")
    private BtPurposeCorpService btPurposeCorpService;
    @Autowired
    //@Qualifier("btPurposeCurServiceBase")
    private BtPurposeCurService btPurposeCurService;
    @Autowired
    //@Qualifier("btPurposeBankTypeServiceBase")
    private BtPurposeBankTypeService btPurposeBankTypeService;
    @Autowired
    //@Qualifier("btPurposeCountryServiceBase")
    private BtPurposeCountryService btPurposeCountryService;
    @Autowired
    private ICorpService corpServiceI;
    @Autowired
    private IBtCurrencyService btCurrencyServiceI;
    @Autowired
    private IBankTypeService bankTypeService;
    @Autowired
    private ISysCountryCustService sysCountryCustService;
    @Autowired
    private IDictService dictServiceI;
    /**
     * 删除对象
     *
     * @param entity
     */
    @Override
    public void deleteEntity(BtPurpose entity) {
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
    public void save(BtPurpose entity) {
        super.saveEntity(entity);
    }
    /**
     * 更新
     *
     * @param entity
     */
    public void update(BtPurpose entity) {
        super.updateEntity(entity);
    }
    /**
     * 保存or更新
     *
     * @param entity
     */
    @Override
    public void saveOrUpdate(BtPurpose entity) {
        super.saveOrUpdate(entity);
    }
    @Override
    public BaseDao<BtPurpose, String> getEntityDao() {
        return btPurposeDao;
    }
    /**
     * 新增用途
     *
     * @param vo
     */
    public void saveBtPurpose(BtPurposeVO vo, String userLoginName) {
        if (EmptyUtils.isBlank(vo.getSysBusType())) {
            throw new ParameterException(PurposeMessageEnum.BT_PURPOSE_MESSAGE_SYSBUSTYPE_NULL);
        }
        List<BtPurposeVO> list = this.listBy(null, vo.getPurpose(), null);
        if (!EmptyUtils.isEmptyList(list)) {
            throw new ParameterException(PurposeMessageEnum.BT_PURPOSE_MESSAGE_PURPOSE_EXIST);
        }
        vo.setUpdateBy(userLoginName);
        vo.setUpdateDate(DateUtils.getDate());
        BtPurpose btPurpose = new BtPurpose();
        BeanUtils.copyNotNullProperties(vo, btPurpose);
        this.save(btPurpose);
        this.saveBtPurposeCorp(btPurpose.getId(), vo.getCorpId());
        this.saveBtPurposeCur(btPurpose.getId(), vo.getCurId());
        this.saveBtPurposeBankType(btPurpose.getId(), vo.getBankTypeId());
        this.saveBtPurposeCountry(btPurpose.getId(), vo.getCountryId());
    }
    /**
     * 修改用途
     *
     * @param vo
     */
    public void updateBtPurpose(BtPurposeVO vo, String userLoginName) {
        if (EmptyUtils.isBlank(vo.getSysBusType())) {
            throw new ParameterException(PurposeMessageEnum.BT_PURPOSE_MESSAGE_SYSBUSTYPE_NULL);
        }
        List<BtPurposeVO> list = this.listBy(null, vo.getPurpose(), null);
        if (!EmptyUtils.isEmptyList(list)) {
            for (BtPurposeVO purpose : list) {
                if (!purpose.getId().equals(vo.getId())) {
                    throw new ParameterException(PurposeMessageEnum.BT_PURPOSE_MESSAGE_PURPOSE_EXIST);
                }
            }
        }
        vo.setUpdateBy(userLoginName);
        vo.setUpdateDate(DateUtils.getDate());
        BtPurpose btPurpose = new BtPurpose();
        BeanUtils.copyNotNullProperties(vo, btPurpose);
        this.updateEntity(btPurpose);
        this.saveBtPurposeCorp(btPurpose.getId(), vo.getCorpId());
        this.saveBtPurposeCur(btPurpose.getId(), vo.getCurId());
        this.saveBtPurposeBankType(btPurpose.getId(), vo.getBankTypeId());
        this.saveBtPurposeCountry(btPurpose.getId(), vo.getCountryId());
    }
    /**
     * 根据单位ID，用途类型过滤
     *
     * @param corpId
     * @param sysBusType 可为空
     * @return
     */
    public List<BtPurposeVO> listBy(String corpId, String sysBusType) {
        return this.listBy(corpId, null, sysBusType);
    }
    /**
     * 根据单位ID，用途类型过滤
     *
     * @param corpId
     * @param sysBusType 可为空
     * @return
     */
    public List<BtPurposeVO> listBySort(String corpId, String sysBusType, String busDtl) {
        return btPurposeDao.listBySort(corpId, sysBusType, busDtl);
    }
    /**
     * 根据条件查询
     *
     * @param corpId  单位ID
     * @param purpose 用途
     * @param purpose 用途类别
     * @return
     */
    public List<BtPurposeVO> listBy(String corpId, String purpose, String sysBusType) {
        List<BtPurposeVO> list = this.btPurposeDao.listBy(corpId, purpose, sysBusType);
        return this.initCorp(list);
    }
    /**
     * 根据条件查询
     *
     * @param corpId     单位ID
     * @param sysBusType 用途类别
     * @param curId      币别
     * @return
     */
    public List<BtPurposeVO> listBy(String corpId, String sysBusType, String curId, String countryId, String bankTypeId) {
        List<BtPurposeVO> list = this.btPurposeDao.listBy(corpId, sysBusType, curId, countryId, bankTypeId);
        return this.initCorp(list);
    }
    /**
     * 查询用途信息列表
     *
     * @param input
     * @return
     */
    @SuppressWarnings("rawtypes")
    public DataTablesOutput<BtPurposeVO> getDataTablesUnion(DataTablesInput input) {
        DataTablesOutput<BtPurposeVO> output = btPurposeDao.getDataTablesUnion(input);
        initCorp(output.getData());
        return output;
    }
    /**
     * 根据id查找用途信息
     *
     * @param id
     * @return
     */
    public BtPurposeVO getByUnion(String id) {
        BtPurposeVO vo = btPurposeDao.getByUnion(id);
        if (EmptyUtils.isEmpty(vo)) {
            return vo;
        }
        return initCorp(vo);
    }
    public void saveBtPurpose(String corpId, String purpose, String sysBusType, String userLoginName) {
        List<BtPurposeVO> list = this.listBy(corpId, purpose, sysBusType);
        if (EmptyUtils.isEmptyList(list)) {
            Date date = DateUtils.getDate();
            BtPurpose btPurpose = new BtPurpose();
            btPurpose.setPurpose(purpose);
            btPurpose.setSysBusType(sysBusType);
            btPurpose.setCreateBy(userLoginName);
            btPurpose.setCreateDate(date);
            btPurpose.setUpdateBy(userLoginName);
            btPurpose.setUpdateDate(date);
            this.save(btPurpose);
            this.saveBtPurposeCorp(btPurpose.getId(), corpId);
        }
    }
    /**
     * 保存用途单位
     *
     * @param purposeId
     * @param corpIds
     */
    public void saveBtPurposeCorp(String purposeId, String corpIds) {
        List<BtPurposeCorp> oldCorpList = btPurposeCorpService.getByPurposeId(purposeId);
        if (!EmptyUtils.isEmptyList(oldCorpList)) {
            this.btPurposeCorpService.deleteBtPurposeCorp(purposeId);
        }
        for (String corpId : corpIds.split(CommonConstant.COMMA_SPLIT)) {
            BtPurposeCorp btPurposeCorp = new BtPurposeCorp();
            btPurposeCorp.setPurposeId(purposeId);
            btPurposeCorp.setCorpId(corpId);
            this.btPurposeCorpService.save(btPurposeCorp);
        }
    }
    /**
     * 保存用途币种
     *
     * @param purposeId
     * @param curIds
     */
    public void saveBtPurposeCur(String purposeId, String curIds) {
        List<BtPurposeCur> oldList = btPurposeCurService.getByPurposeId(purposeId);
        if (!EmptyUtils.isEmptyList(oldList)) {
            this.btPurposeCurService.deleteBtPurposeCur(purposeId);
        }
        if (StringUtils.isNotBlank(curIds) && !"null".equals(curIds)) {
            for (String curId : curIds.split(CommonConstant.COMMA_SPLIT)) {
                BtPurposeCur btPurposeCur = new BtPurposeCur();
                btPurposeCur.setPurposeId(purposeId);
                btPurposeCur.setCurId(curId);
                this.btPurposeCurService.saveOrUpdate(btPurposeCur);
            }
        }
    }
    /**
     * 保存用途银行类别
     *
     * @param purposeId
     * @param bankTypeIds
     */
    public void saveBtPurposeBankType(String purposeId, String bankTypeIds) {
        List<BtPurposeBankType> oldList = btPurposeBankTypeService.getByPurposeId(purposeId);
        if (!EmptyUtils.isEmptyList(oldList)) {
            this.btPurposeBankTypeService.deleteBtPurposeBankType(purposeId);
        }
        if (StringUtils.isNotBlank(bankTypeIds) && !"null".equals(bankTypeIds)) {
            for (String corpId : bankTypeIds.split(CommonConstant.COMMA_SPLIT)) {
                BtPurposeBankType btPurposeBankType = new BtPurposeBankType();
                btPurposeBankType.setPurposeId(purposeId);
                btPurposeBankType.setBankTypeId(corpId);
                this.btPurposeBankTypeService.saveOrUpdate(btPurposeBankType);
            }
        }
    }
    /**
     * 保存用途地区
     *
     * @param purposeId
     * @param countryIds
     */
    public void saveBtPurposeCountry(String purposeId, String countryIds) {
        List<BtPurposeCountry> oldList = btPurposeCountryService.getByPurposeId(purposeId);
        if (!EmptyUtils.isEmptyList(oldList)) {
            this.btPurposeCountryService.deleteBtPurposeCountry(purposeId);
        }
        if (StringUtils.isNotBlank(countryIds) && !"null".equals(countryIds)) {
            for (String countryId : countryIds.split(CommonConstant.COMMA_SPLIT)) {
                BtPurposeCountry btPurposeCountry = new BtPurposeCountry();
                btPurposeCountry.setPurposeId(purposeId);
                btPurposeCountry.setCountryId(countryId);
                this.btPurposeCountryService.saveOrUpdate(btPurposeCountry);
            }
        }
    }
    /**
     * 加载单位信息
     *
     * @param list
     * @return
     */
    public List<BtPurposeVO> initCorp(List<BtPurposeVO> list) {
        if (!EmptyUtils.isEmptyList(list)) {
            for (BtPurposeVO vo : list) {
                initCorp(vo);
            }
        }
        return list;
    }
    /**
     * 加载单位信息
     *
     * @param vo
     * @return
     */
    public BtPurposeVO initCorp(BtPurposeVO vo) {
        if (!EmptyUtils.isEmpty(vo)) {
            List<BtPurposeCorp> corpList = this.btPurposeCorpService.getByPurposeId(vo.getId());
            if (!EmptyUtils.isEmptyList(corpList)) {
                StringBuilder corpIds = new StringBuilder();
                StringBuilder corpNames = new StringBuilder();
                for (BtPurposeCorp corp : corpList) {
                    if (EmptyUtils.isBlank(corpIds.toString())) {
                        corpIds.append(corp.getCorpId());
                    } else {
                        corpIds.append(CommonConstant.COMMA_SPLIT).append(corp.getCorpId());
                    }
                    CorpDTO corpDTO = corpServiceI.get(corp.getCorpId());
                    if (corpDTO != null) {
                        if (EmptyUtils.isBlank(corpNames.toString())) {
                            corpNames.append(corpDTO.getName());
                        } else {
                            corpNames.append(CommonConstant.COMMA_SPLIT).append(corpDTO.getName());
                        }
                    }
                }
                vo.setCorpId(corpIds.toString());
                vo.setCorpName(corpNames.toString());
            } else {
                vo.setCorpId("");
            }
            //币种
            List<BtPurposeCur> curList = this.btPurposeCurService.getByPurposeId(vo.getId());
            if (!EmptyUtils.isEmptyList(curList)) {
                StringBuilder curIds = new StringBuilder();
                StringBuilder curNames = new StringBuilder();
                for (BtPurposeCur cur : curList) {
                    if (EmptyUtils.isBlank(curIds.toString())) {
                        curIds.append(cur.getCurId());
                    } else {
                        curIds.append(CommonConstant.COMMA_SPLIT).append(cur.getCurId());
                    }
                    BtCurrencyDTO btCurrencyDTO =this.btCurrencyServiceI.getObjectById(cur.getCurId());
                    if (btCurrencyDTO != null) {
                        if (EmptyUtils.isBlank(curNames.toString())) {
                            curNames.append(btCurrencyDTO.getCurName());
                        } else {
                            curNames.append(CommonConstant.COMMA_SPLIT).append(btCurrencyDTO.getCurName());
                        }
                    }
                }
                vo.setCurId(curIds.toString());
                vo.setCurName(curNames.toString());
            } else {
                vo.setCurId("");
            }
            //银行类别
            List<BtPurposeBankType> bankTypeList = this.btPurposeBankTypeService.getByPurposeId(vo.getId());
            if (!EmptyUtils.isEmptyList(bankTypeList)) {
                StringBuilder bankTypeIds = new StringBuilder();
                StringBuilder bankTypeNames = new StringBuilder();
                for (BtPurposeBankType type : bankTypeList) {
                    if (EmptyUtils.isBlank(bankTypeIds.toString())) {
                        bankTypeIds.append(type.getBankTypeId());
                    } else {
                        bankTypeIds.append(CommonConstant.COMMA_SPLIT).append(type.getBankTypeId());
                    }
                    BankTypeDTO bankTypeDTO= bankTypeService.getObjectById(type.getBankTypeId());
                    if (bankTypeDTO != null) {
                        if (EmptyUtils.isBlank(bankTypeNames.toString())) {
                            bankTypeNames.append(bankTypeDTO.getTypeName());
                        } else {
                            bankTypeNames.append(CommonConstant.COMMA_SPLIT).append(bankTypeDTO.getTypeName());
                        }
                    }
                }
                vo.setBankTypeId(bankTypeIds.toString());
                vo.setBankTypeName(bankTypeNames.toString());
            } else {
                vo.setBankTypeId("");
            }
            //地区
            List<BtPurposeCountry> countryList = this.btPurposeCountryService.getByPurposeId(vo.getId());
            if (!EmptyUtils.isEmptyList(countryList)) {
                StringBuilder countryIds = new StringBuilder();
                StringBuilder countryNames = new StringBuilder();
                for (BtPurposeCountry country : countryList) {
                    SysCountryCustDTO countryDTO = sysCountryCustService.getById(country.getCountryId());
                    if (EmptyUtils.isBlank(countryIds.toString())) {
                        countryIds.append(country.getCountryId());
                    } else {
                        countryIds.append(CommonConstant.COMMA_SPLIT).append(country.getCountryId());
                    }
                    if (countryDTO != null) {
                        if (EmptyUtils.isBlank(countryNames.toString())) {
                            countryNames.append(countryDTO.getNameZh());
                        } else {
                            countryNames.append(CommonConstant.COMMA_SPLIT).append(countryDTO.getNameZh());
                        }
                    }
                }
                vo.setCountryId(countryIds.toString());
                vo.setCountryName(countryNames.toString());
            } else {
                vo.setCountryId("");
                vo.setCountryName("");
            }
        }
        return vo;
    }
    /**
     * 根据用途ID加载所属单位的信息,并进行排序
     *
     * @param purposeId
     * @return
     */
    public AjaxJson< List<BtPurposeCorpVO>> getCorpIdByPurposeId(String purposeId) {
        List<BtPurposeCorpVO> purposes = btPurposeCorpService.getCorpByPurposeId(purposeId);
        // 当所选节点没有父节点时,而从sys_corp表中取出的parentId不为空,进行排序时会过滤掉,这里将子节点的parentID重置,以第一级父节点显示
        Map<String, BtPurposeCorpVO> purposeMap = new HashMap<>();
        for (BtPurposeCorpVO vo : purposes) {
            purposeMap.put(vo.getId(), vo);
        }
        for (BtPurposeCorpVO vo : purposes) {
            if (null != vo.getParentCorpId() && null == purposeMap.get(vo.getParentCorpId())) {
                vo.setParentCorpId("");
            }
        }
        List<BtPurposeCorpVO> btPurposeCorpVO = TreeUtil.coverTreeTable(purposes);
        return AjaxJson.success(btPurposeCorpVO);
    }
    @Override
    public ExcelVerifyHanlderResult verifyHandler(BtPurposeVO btPurposeVO) {
        return null;
    }
    @Override
    public void verifyHandler(List<BtPurposeVO> list) {
        if (!EmptyUtils.isEmptyList(list)) {
            //单位集合
            HashMap<String, CorpDTO> corpMap = new HashMap<>();
            //币种集合
            HashMap<String, BtCurrencyDTO> curMap = new HashMap<>();
            //银行类别集合
            HashMap<String, BankTypeDTO> bankTypeMap = new HashMap<>();
            //国家/地区集合
            HashMap<String, SysCountryCustDTO> countryMap = new HashMap<>();
            //所属类别集合
            Map<String, DictDTO> sysBusTypeMap = dictServiceI.listMap("btPurpose.sysBusType");
            for (BtPurposeVO vo : list) {
                List<String> msgList = new ArrayList<>();
                List<BtPurposeVO> listPurpose = this.listBy(null, vo.getPurpose(), null);
                if (!EmptyUtils.isEmptyList(listPurpose)) {
                    msgList.add("bt.purpose.message.purpose.exist");
                }
                // 判断所属类别在系统中是否存在
                if (StringUtils.isNotBlank(vo.getSysBusTypeName())) {
                    String[] sysBusTypeNames = vo.getSysBusTypeName().split(CommonConstant.COMMA_SPLIT);
                    String sysBusType = "";
                    for (String sysBusTypeName : sysBusTypeNames) {
                        DictDTO dictDTO = sysBusTypeMap.get(sysBusTypeName);
                        if (dictDTO == null) {
                            msgList.add("bt.purpose.message.sysbustype.exist");
                            break;
                        } else {
                            if (StringUtils.isBlank(sysBusType)) {
                                sysBusType = dictDTO.getCode();
                            } else {
                                sysBusType = sysBusType + CommonConstant.COMMA_SPLIT + dictDTO.getCode();
                            }
                        }
                    }
                    vo.setSysBusType(sysBusType);
                }
                // 判断币别在系统中是否存在
                if (StringUtils.isNotBlank(vo.getCurName())) {
                    String[] curNames = vo.getCurName().split(CommonConstant.COMMA_SPLIT);
                    String curId = "";
                    for (String curName : curNames) {
                        BtCurrencyDTO cur = curMap.get(curName);
                        if (cur == null) {
                            cur = this.btCurrencyServiceI.getByCurNameOrEnglishCode(curName);
                            curMap.put(curName, cur);
                        }
                        if (EmptyUtils.isEmpty(cur) || EmptyUtils.isBlank(cur.getId())) {
                            msgList.add("nis.netbank.message.cur.null");
                            break;
                        } else {
                            if (StringUtils.isBlank(curId)) {
                                curId = cur.getId();
                            } else {
                                curId = curId + CommonConstant.COMMA_SPLIT + cur.getId();
                            }
                        }
                    }
                    vo.setCurId(curId);
                }
                // 判断银行类别在系统中是否存在
                if (StringUtils.isNotBlank(vo.getBankTypeName())) {
                    String[] bankTypeNames = vo.getBankTypeName().split(CommonConstant.COMMA_SPLIT);
                    String bankTypeId = "";
                    for (String bankTypeName : bankTypeNames) {
                        BankTypeDTO bankType = bankTypeMap.get(bankTypeName);
                        if (bankType == null) {
                            bankType = this.bankTypeService.getByTypeName(bankTypeName);
                            bankTypeMap.put(bankTypeName, bankType);
                        }
                        if (EmptyUtils.isEmpty(bankType) || EmptyUtils.isBlank(bankType.getId())) {
                            msgList.add("nis.netbank.message.btbanktype.null");
                            break;
                        } else {
                            if (StringUtils.isBlank(bankTypeId)) {
                                bankTypeId = bankType.getId();
                            } else {
                                bankTypeId = bankTypeId + CommonConstant.COMMA_SPLIT + bankType.getId();
                            }
                        }
                    }
                    vo.setBankTypeId(bankTypeId);
                }
                // 判断国家地区在系统中是否存在
                if (StringUtils.isNotBlank(vo.getCountryName())) {
                    String[] countryNames = vo.getCountryName().split(CommonConstant.COMMA_SPLIT);
                    String countryId = "";
                    for (String countryName : countryNames) {
                        SysCountryCustDTO countryCust = countryMap.get(countryName);
                        if (countryCust == null) {
                            countryCust = this.sysCountryCustService.getByNameZh(countryName, CommonConstant.NO);
                            countryMap.put(countryName, countryCust);
                        }
                        if (EmptyUtils.isEmpty(countryCust) || EmptyUtils.isBlank(countryCust.getId())) {
                            msgList.add("sys.black.white.import.country.nonentity.tips");
                            break;
                        } else {
                            if (StringUtils.isBlank(countryId)) {
                                countryId = countryCust.getId();
                            } else {
                                countryId = countryId + CommonConstant.COMMA_SPLIT + countryCust.getId();
                            }
                        }
                    }
                    vo.setCountryId(countryId);
                }
                // 判断单位在系统中是否存在
                if (StringUtils.isNotBlank(vo.getCorpName())) {
                    String[] corpNames = vo.getCorpName().split(CommonConstant.COMMA_SPLIT);
                    String corpId = "";
                    for (String corpName : corpNames) {
                        CorpDTO corp = corpMap.get(corpName);
                        if (corp == null) {
                            corp = this.corpServiceI.getCorpIdByName(corpName);
                            corpMap.put(corpName, corp);
                        }
                        if (EmptyUtils.isEmpty(corp) || EmptyUtils.isBlank(corp.getId())) {
                            msgList.add("nis.netbank.message.corp.null");
                            break;
                        } else {
                            if (StringUtils.isBlank(corpId)) {
                                corpId = corp.getId();
                            } else {
                                corpId = corpId + CommonConstant.COMMA_SPLIT + corp.getId();
                            }
                        }
                    }
                    vo.setCorpId(corpId);
                }
                vo.addErrorMsg(msgList);
            }
        }
    }
    /**
     * 导入
     *
     * @param list
     */
    public void saveImport(List<BtPurposeVO> list, ICurrentUser user) {
        for (BtPurposeVO vo : list) {
            saveBtPurpose(vo, user.getLoginName());
        }
    }
}
