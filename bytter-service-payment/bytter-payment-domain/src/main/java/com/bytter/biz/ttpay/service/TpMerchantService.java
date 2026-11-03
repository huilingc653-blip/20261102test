package com.bytter.biz.ttpay.service;
import com.bytter.biz.payment.api.constant.TtPayConstant;
import com.bytter.biz.payment.api.enums.TtPayMessageEnum;
import com.bytter.biz.ttpay.dao.TpMerchantDao;
import com.bytter.biz.ttpay.entity.TpMerchant;
import com.bytter.biz.ttpay.vo.TpMerchantVO;
import com.bytter.biz.ttpay.vo.TpMerchantVarVO;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.exception.BusinessException;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.utils.BeanUtils;
import com.bytter.core.utils.StringUtils;
import com.bytter.poi.excel.entity.result.ExcelVerifyHanlderResult;
import com.bytter.poi.handler.inter.IUserExcelVerifyHandler;
import com.bytter.system.api.ICorpService;
import com.bytter.system.api.IDictService;
import com.bytter.system.api.dto.CorpDTO;
import com.bytter.system.api.dto.DictDTO;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.*;
import java.util.Map.Entry;
/**
 * @author 郑刚
 * @version 1.0
 * @Title: Service
 * @Description: 商户信息Service
 * @date 2019-09-26 15:43:05
 */
@Service("tpMerchantService")
@Transactional
public class TpMerchantService extends BaseService<TpMerchant, String> implements IUserExcelVerifyHandler<TpMerchantVO> {
    @DubboReference(check = false)
	private ICorpService corpService;
    @DubboReference(check = false)
	private IDictService dictService;
    @Autowired
    private TpMerchantDao tpMerchantDao;
    @Autowired
    private TpMerchantVarService tpMerchantVarService;
    @Autowired
    private TpLogService tpLogService;
    @Override
    public BaseDao<TpMerchant, String> getEntityDao() {
        return tpMerchantDao;
    }
//	/**
//	 *
//	 * @Title: 查询商户信息数据
//	 * @param input
//	 * @return 列表数据
//	 * @throws
//	 */
//	@SuppressWarnings("rawtypes")
//	@Transactional(readOnly = true)
//	public DataTablesOutput getDataTablesUnion(DataTablesInput input) {
//		DataTablesOutput data=tpMerchantDao.getDataTablesUnion(input);
//		return data;
//	}
    /**
     * @param queryVO
     * @return 列表数据
     * @throws
     * @Title: 查询商户信息数据
     */
    @Transactional(readOnly = true)
    public List<TpMerchantVO> getTpMerchantVOList(TpMerchantVO queryVO, ICurrentUser user) {
        Map<String, Object> params = new HashMap<>();
        List<String> corpAuthList = corpService.getUserCorpIds(TtPayConstant.TTPAY_RESOURCE_MERCHANT_OP, user.getId());
        if (StringUtils.isNotEmpty(queryVO.getCorpId())) {
            params.put("corpIds", queryVO.getCorpId().split(","));
        }
        if (StringUtils.isNotEmpty(queryVO.getPayType())) {
            params.put("payTypes", queryVO.getPayType().split(","));
        }
        if (StringUtils.isNotEmpty(queryVO.getMerchantNo())) {
            params.put("merchantNo", "%" + queryVO.getMerchantNo() + "%");
        }
        List<TpMerchantVO> list = tpMerchantDao.getTpMerchantVOList(params, corpAuthList);
        this.setJoinField(list);
        return list;
    }
    public void setJoinField(List<TpMerchantVO> list) {
        Map<String, CorpDTO> corpMap = corpService.getCorpMap();
        Map<String, DictDTO> payTypeMap = this.getPayTypeMap();
        for (TpMerchantVO vo:list) {
            if (corpMap.containsKey(vo.getCorpId())) {
                vo.setCorpCode(corpMap.get(vo.getCorpId()).getCode());
                vo.setCorpName(corpMap.get(vo.getCorpId()).getName());
            }
            if (payTypeMap.containsKey(vo.getPayType())) {
                vo.setPayTypeName(payTypeMap.get(vo.getPayType()).getName());
            }
        }
    }
    /**
     * @param queryVO
     * @return 列表数据
     * @throws
     * @Title: 查询商户信息数据
     */
    @Transactional(readOnly = true)
    public List<TpMerchantVO> getTpMerchantVOTreeList(TpMerchantVO queryVO, ICurrentUser user) {
        List<TpMerchantVO> list = getTpMerchantVOList(queryVO, user);
        List<TpMerchantVO> listTree = initTpMerchantVOTree(list);
        return listTree;
    }
    /**
     * @param queryVO
     * @return 列表数据
     * @throws
     * @Title: 查询商户信息数据
     */
    @Transactional(readOnly = true)
    public Object getTpMerchantVOTreeMapList(TpMerchantVO queryVO, ICurrentUser user) {
        List<TpMerchantVO> list = getTpMerchantVOList(queryVO, user);
        List<Map<String, Object>> listTree = initTpMerchantVOTreeMap(list);
        return listTree;
    }
    public void initRootMap(List<TpMerchantVO> list, Map<String, TpMerchantVO> rootMap) {
        for (TpMerchantVO info : list) {
            if (StringUtils.isEmpty(info.getParentId())) {
                rootMap.put(info.getId(), info);
            } else {
                if (rootMap.containsKey(info.getParentId())) {
                    List<TpMerchantVO> sonList = rootMap.get(info.getParentId()).getSonMerchantList();
                    if (sonList == null) {
                        sonList = new ArrayList<>();
                        rootMap.get(info.getParentId()).setSonMerchantList(sonList);
                    }
                    sonList.add(info);
                } else {
                    rootMap.put(info.getId(), info);
                }
            }
        }
    }
    /**
     * 对树结构进行转换
     *
     * @param list
     * @return
     */
    public List<Map<String, Object>> initTpMerchantVOTreeMap(List<TpMerchantVO> list) {
        List<Map<String, Object>> listTree = null;
        if (list != null && list.size() > 0) {
            listTree = new ArrayList<>();
            Map<String, TpMerchantVO> rootMap = new LinkedHashMap<>();
            initRootMap(list, rootMap);
            for (Entry<String, TpMerchantVO> info : rootMap.entrySet()) {
                Map<String, Object> mapInfo = new HashMap<>();
                mapInfo.put("name", info.getValue().getMerchantNo());
                mapInfo.put("merchantNo", info.getValue().getMerchantNo());
                mapInfo.put("id", info.getValue().getId());
                if (info.getValue().getSonMerchantList() != null) {
                    List<Map<String, Object>> children = new ArrayList<>();
                    for (TpMerchantVO sonInfo : info.getValue().getSonMerchantList()) {
                        Map<String, Object> sonMapInfo = new HashMap<>();
                        sonMapInfo.put("name", sonInfo.getMerchantNo());
                        sonMapInfo.put("merchantNo", sonInfo.getMerchantNo());
                        sonMapInfo.put("id", sonInfo.getId());
                        children.add(sonMapInfo);
                    }
                    mapInfo.put("children", children);
                }
                listTree.add(mapInfo);
            }
        }
        return listTree;
    }
    /**
     * 对树结构进行转换
     *
     * @param list
     * @return
     */
    public List<TpMerchantVO> initTpMerchantVOTree(List<TpMerchantVO> list) {
        List<TpMerchantVO> listTree = null;
        if (list != null && list.size() > 0) {
            listTree = new ArrayList<>();
            Map<String, TpMerchantVO> rootMap = new LinkedHashMap<>();
            initRootMap(list, rootMap);
            for (Entry<String, TpMerchantVO> info : rootMap.entrySet()) {
                listTree.add(info.getValue());
                if (info.getValue().getSonMerchantList() != null) {
                    listTree.addAll(info.getValue().getSonMerchantList());
                }
            }
        }
        return listTree;
    }
    /**
     * @param list 商户数据信息
     * @param user 用户
     * @return 处理结果
     * @throws
     * @Title: 商户信息数据新增或者修改操作
     */
    public AjaxJson saveImport(List<TpMerchantVO> list, ICurrentUser user) {
        AjaxJson reJson = new AjaxJson();
        TpMerchant merchant = null;
        String logType = TtPayConstant.TTPAY_LOGTYPE_IMP;
        String msg = TtPayConstant.COMMON_MESSAGE_IMPSUCCESS;
        List<TpMerchant> merchantList = new ArrayList<>();
        String[] quoteIds = new String[list.size()];
        int i = 0;
        int sort = 1000;
        for (TpMerchantVO infoVO : list) {
            merchant = new TpMerchant();
            BeanUtils.copyNotNullProperties(infoVO, merchant);
            merchant.setCreateName(user.getUsername());
            merchant.setCreateBy(user.getLoginName());
            merchant.setCreateDate(new Date());
            merchant.setValidSign(TtPayConstant.TTPAY_VALIDSIGN1);
            merchantList.add(merchant);
            quoteIds[i++] = merchant.getId();
            if (StringUtils.isNotEmpty(merchant.getParentId())) {
                merchant.setSort(Double.parseDouble(String.valueOf(sort + i)));
            }
        }
        tpMerchantDao.batchJdbcSave(merchantList);
        tpLogService.batchSaveTpLog(quoteIds, logType, msg, user);
        reJson.setSuccess(true);
        return reJson;
    }
    /**
     * @param infoVO 商户数据信息
     * @param user   用户
     * @return 处理结果
     * @throws
     * @Title: 商户信息数据新增或者修改操作
     */
    public AjaxJson saveOrUpdate(TpMerchantVO infoVO, ICurrentUser user) {
        initSonAndVarTpMerchantVO(infoVO);
        AjaxJson reJson = new AjaxJson();
        TpMerchant merchant = null;
        String logType = TtPayConstant.TTPAY_LOGTYPE_ADD;
        String msg = TtPayConstant.COMMON_MESSAGE_ADDSUCCESS;
        //id不为空时为修改数据
        if (!StringUtils.isEmpty(infoVO.getId())) {
            merchant = tpMerchantDao.get(infoVO.getId(), true);
            //对应修改的数据先把子商行和对应参数数据删除
            tpMerchantDao.deleteTpMerchantByMchId(infoVO.getId());
            tpMerchantVarService.deleteTpMerchantVarByMchId(infoVO.getId());
            merchant.setUpdateName(user.getUsername());
            merchant.setUpdateBy(user.getLoginName());
            merchant.setUpdateDate(new Date());
            logType = TtPayConstant.TTPAY_LOGTYPE_UPDATE;
            msg = TtPayConstant.COMMON_MESSAGE_UPDATESUCCESS;
        } else {
            merchant = new TpMerchant();
            merchant.setCreateName(user.getUsername());
            merchant.setCreateBy(user.getLoginName());
            merchant.setCreateDate(new Date());
            merchant.setValidSign(TtPayConstant.TTPAY_VALIDSIGN1);
        }
        if (!merchantNoCheck(infoVO.getMerchantNo(), infoVO.getPayType(), infoVO.getId())) {
            throw new BusinessException(TtPayMessageEnum.TTPAY_MESSAGE_ERROR_MERCHANTNO_FATHER_REP, infoVO.getMerchantNo());
        }
        BeanUtils.copyNotNullProperties(infoVO, merchant);
        tpMerchantDao.saveOrUpdate(merchant);
        infoVO.setId(merchant.getId());
        this.saveSonMerchant(infoVO);
        tpMerchantVarService.saveMerchantVar(infoVO);
        tpLogService.saveTpLog(infoVO.getId(), logType, msg, user);
        reJson.setSuccess(true);
        return reJson;
    }
    /**
     * 对子商户信息和参数信息进行转换初始化
     */
    public void initSonAndVarTpMerchantVO(TpMerchantVO infoVO) {
        if (infoVO == null) {
            return;
        }
        if (infoVO.getSonMerchantNo() != null
                && infoVO.getSonRmk() != null
                && infoVO.getSonMerchantNo().length > 0
                && infoVO.getSonMerchantNo().length == infoVO.getSonRmk().length) {
            List<TpMerchantVO> sonMerchantList = new ArrayList<>();
            for (int i = 0; i < infoVO.getSonMerchantNo().length; i++) {
                sonMerchantList.add(new TpMerchantVO(infoVO.getSonMerchantNo()[i], infoVO.getSonRmk()[i]));
            }
            infoVO.setSonMerchantList(sonMerchantList);
        }
        if (infoVO.getVarCode() != null
                && infoVO.getVarName() != null
                && infoVO.getVarValue() != null
                && infoVO.getVarCode().length > 0
                && infoVO.getVarValue().length == infoVO.getVarCode().length
                && infoVO.getVarValue().length == infoVO.getVarValue().length) {
            List<TpMerchantVarVO> varMerchantList = new ArrayList<>();

            for (int i = 0; i < infoVO.getVarCode().length; i++) {
                if(infoVO.getFileId().length==0){
                    varMerchantList.add(new TpMerchantVarVO(infoVO.getVarCode()[i], infoVO.getVarName()[i], infoVO.getVarValue()[i]));
                }else {
                    varMerchantList.add(new TpMerchantVarVO(infoVO.getVarCode()[i], infoVO.getVarName()[i], infoVO.getVarValue()[i], infoVO.getFileId()[i]));
                }
            }
            infoVO.setMerchantVarList(varMerchantList);
        }
    }
    /**
     * 对子商户信息进行新增
     */
    public void saveSonMerchant(TpMerchantVO infoVO) {
        if (infoVO != null
                && infoVO.getSonMerchantList() != null
                && infoVO.getSonMerchantList().size() > 0) {
            List<TpMerchant> sonInfoList = new ArrayList<>();
            double i = 0;
            Map<String, Boolean> merchantNoMap = new HashMap<>();
            for (TpMerchantVO sonInfoVO : infoVO.getSonMerchantList()) {
                TpMerchant sonInfo = new TpMerchant();
                BeanUtils.copyNotNullProperties(sonInfoVO, sonInfo);
                sonInfo.setParentId(infoVO.getId());
                sonInfo.setCorpId(infoVO.getCorpId());
                sonInfo.setPayType(infoVO.getPayType());
                sonInfo.setSort(++i);
                sonInfo.setValidSign(TtPayConstant.TTPAY_VALIDSIGN1);
                sonInfoList.add(sonInfo);
                if (merchantNoMap.containsKey(sonInfo.getMerchantNo()) || !merchantNoCheck(sonInfo.getMerchantNo(), sonInfo.getPayType(), sonInfo.getId())) {
                    throw new BusinessException(TtPayMessageEnum.TTPAY_MESSAGE_ERROR_MERCHANTNO_SON_REP, sonInfo.getMerchantNo());
                }
                merchantNoMap.put(sonInfoVO.getMerchantNo(), true);
            }
            tpMerchantDao.batchJdbcSave(sonInfoList);
        }
    }
    /**
     * 根据ID获取VO信息
     *
     * @param id
     * @return
     */
    @Transactional(readOnly = true)
    public TpMerchantVO getTpMerchantVO(String id) {
        TpMerchant info = this.get(id, true);
        TpMerchantVO infoVo = new TpMerchantVO();
        BeanUtils.copyProperties(info, infoVo);
        if (StringUtils.isNotEmpty(infoVo.getId())) {
            infoVo.setMerchantVarList(tpMerchantVarService.getTpMerchantVarVOByMchId(infoVo.getId()));
            infoVo.setSonMerchantList(this.getTpMerchantVOByMchId(infoVo.getId()));
        }
        return infoVo;
    }
    /**
     * 判断相同缴款方式下，是否merchantNo商户号是否有相同的维护
     *
     * @param merchantNo 商户号
     * @param payType    支付方式
     * @param mchId      商户ID
     * @return true不存在重复的信息 false存在重复的信息
     */
    @Transactional(readOnly = true)
    public boolean merchantNoCheck(String merchantNo, String payType, String mchId) {
        return tpMerchantDao.merchantNoCheck(merchantNo, payType, mchId);
    }
    /**
     * 根据客商信息id获取对应参数信息
     *
     * @param mchId
     * @return
     */
    @Transactional(readOnly = true)
    public List<TpMerchant> getTpMerchantByMchId(String mchId) {
        return tpMerchantDao.getTpMerchantByMchId(mchId);
    }
    /**
     * 根据客商信息id获取对应参数信息
     *
     * @param mchId
     * @return
     */
    @Transactional(readOnly = true)
    public List<TpMerchantVO> getTpMerchantVOByMchId(String mchId) {
        List<TpMerchant> list = this.getTpMerchantByMchId(mchId);
        if (list == null || list.size() < 1) {
            return null;
        }
        List<TpMerchantVO> voList = BeanUtils.copyListProperties(list, TpMerchantVO.class);
        return voList;
    }
    /**
     * 根据客商信息id删除商行信息
     *
     * @param mchId
     * @return
     */
    public void deleteMerchant(String mchId, ICurrentUser user) {
        TpMerchant tpMerchant = tpMerchantDao.get(mchId, true);
        tpMerchant.setValidSign(TtPayConstant.TTPAY_VALIDSIGN0);
        this.updateEntity(tpMerchant);
        // 删除商户参数信息表
        tpMerchantVarService.deleteTpMerchantVarByMchId(mchId);
        // 将其子关联商户号改为无效
        tpMerchantDao.deleteTpMerchantByMchId(mchId);
        tpLogService.saveTpLog(mchId, TtPayConstant.TTPAY_LOGTYPE_DELETE, TtPayConstant.COMMON_MESSAGE_DELETESUCCESS, user);
    }
    /**
     * 修改在线离线标记
     *
     * @param mchId    需要修改的ID
     * @param isOnline 在线离线标记 1在线0离线
     * @return
     */
    public boolean updateIsOnline(String mchId, String isOnline, ICurrentUser user) {
        if (StringUtils.isNotEmpty(mchId)) {
            TpMerchant info = this.get(mchId, true);
            info.setIsOnline(isOnline);
            tpMerchantDao.update(info);
            tpLogService.saveTpLog(mchId, TtPayConstant.TTPAY_LOGTYPE_UPDATE, TtPayConstant.COMMON_MESSAGE_ISONLINE_UPDATESUCCESS, user);
            return true;
        } else {
            return false;
        }
    }
    /**
     * 获取全部第三方支付方式
     *
     * @return
     */
    public Map<String, DictDTO> getPayTypeMap() {
        List<DictDTO> listDictDTO = dictService.list(TtPayConstant.TTPAY_PAYTYPE_DICT);
        Map<String, DictDTO> payTypeMap = new HashMap<>();
        for (DictDTO info : listDictDTO) {
            payTypeMap.put(info.getCode(), info);
            payTypeMap.put(info.getName(), info);
        }
        return payTypeMap;
    }
    @Override
    @Transactional(readOnly = true)
    public void verifyHandler(List<TpMerchantVO> list, Object... user) {
        if (list != null && !list.isEmpty()) {
            List<String> errorMsgList = null;
            Map<String, String> errorMap = new HashMap<>();
            Map<String, CorpDTO> corpMap = new HashMap<>();
            Map<String, TpMerchantVO> parentMerchantMap = new HashMap<>();
            Map<String, DictDTO> payTypeMap = this.getPayTypeMap();
            List<String> corpAuthList = corpService.getUserCorpIds(TtPayConstant.TTPAY_RESOURCE_MERCHANT_OP, ((ICurrentUser) user[0]).getId());
            Map<String, Boolean> corpAuthMap = new HashMap<>();
            for (String key : corpAuthList) {
                corpAuthMap.put(key, true);
            }
            CorpDTO corpDTO = null;
            for (TpMerchantVO info : list) {
                errorMsgList = new ArrayList<>();
                if (payTypeMap.containsKey(info.getPayTypeName())) {
                    info.setPayType(payTypeMap.get(info.getPayTypeName()).getCode());
                } else {
                    errorMsgList.add(TtPayConstant.TTPAY_MESSAGE_PAYTYPE_ISNOTEXIST);
                }
                if (!merchantNoCheck(info.getMerchantNo(), info.getPayType(), info.getId()) || errorMap.containsKey("merchantNo_" + info.getMerchantNo())) {
                    errorMsgList.add(TtPayConstant.TTPAY_MESSAGE_MERCHANTNO_REP);
                } else {
                    info.setId(UUID.randomUUID().toString().replace("-", ""));
                }
                errorMap.put("merchantNo_" + info.getMerchantNo(), TtPayConstant.TTPAY_MESSAGE_MERCHANTNO_REP);
                if (StringUtils.isEmpty(info.getMerchantNoParent())) {
                    /** 所属单位代码和所属单位名称校验 */
                    if (StringUtils.isEmpty(info.getCorpCode())
                            && StringUtils.isEmpty(info.getCorpName())) {
                        errorMsgList.add(TtPayConstant.TTPAY_MESSAGE_CORP_CODEANDNAME_NOTNULL);
                    } else {
                        if (errorMap.containsKey("corp_" + info.getCorpCode())) {
                            errorMsgList.add(errorMap.get("corp_" + info.getCorpCode()));
                        } else if (errorMap.containsKey("corp_" + info.getCorpName())) {
                            errorMsgList.add(errorMap.get("corp_" + info.getCorpCode()));
                        } else if (StringUtils.isNotEmpty(info.getCorpCode())) {
                            // 根据所属单位代码获取单位信息
                            if (corpMap.containsKey(info.getCorpCode())) {
                                corpDTO = corpMap.get(info.getCorpCode());
                            } else {
                                corpDTO = corpService.getCorpByCode(info.getCorpCode());
                            }
                            if (corpDTO != null) {
                                if (StringUtils.isNotEmpty(info.getCorpName())
                                        && !info.getCorpName().equals(corpDTO.getName())) {
                                    errorMsgList.add(TtPayConstant.TTPAY_EXCEL_ERRORMSG_CORPNAME_ISNOT);
                                } else {
                                    info.setCorpName(corpDTO.getName());
                                }
                            } else {
                                errorMap.put("corp_" + info.getCorpCode(), TtPayConstant.TTPAY_EXCEL_ERRORMSG_CORPCODE_ERROR);
                                errorMsgList.add(TtPayConstant.TTPAY_EXCEL_ERRORMSG_CORPCODE_ERROR);
                            }
                        } else {
                            // 只填单位名称
                            if (corpMap.containsKey(info.getCorpName())) {
                                corpDTO = corpMap.get(info.getCorpName());
                            } else {
                                corpDTO = corpService.getCorpIdByName(info.getCorpName());
                            }
                            if (corpDTO == null) {
                                errorMap.put("corp_" + info.getCorpName(), TtPayConstant.TTPAY_MESSAGE_CORPID_ISNOTEXIST);
                                errorMsgList.add(TtPayConstant.TTPAY_MESSAGE_CORPID_ISNOTEXIST);
                            } else {
                                info.setCorpCode(corpDTO.getCode());
                            }
                        }
                    }
                    /** 根据corpName获取corpId */
                    if (corpDTO != null) {
                        info.setCorpId(corpDTO.getId());
                        corpMap.put(corpDTO.getId(), corpDTO);
                        corpMap.put(corpDTO.getCode(), corpDTO);
                        corpMap.put(corpDTO.getName(), corpDTO);
                    }
                    if (StringUtils.isEmpty(info.getIsOnlineName())
                            || "否".equals(info.getIsOnlineName())
                            || "0".equals(info.getIsOnlineName())) {
                        info.setIsOnline(TtPayConstant.TTPAY_ISONLINE0);
                    } else {
                        info.setIsOnline(TtPayConstant.TTPAY_ISONLINE1);
                    }
                    parentMerchantMap.put("parent_" + info.getMerchantNo() + "_" + info.getPayType(), info);
                } else {
                    if (errorMap.containsKey("parent_" + info.getMerchantNoParent())) {
                        errorMsgList.add(errorMap.get("parent_" + info.getMerchantNoParent()));
                    } else {
                        TpMerchantVO parentInfo = parentMerchantMap.get("parent_" + info.getMerchantNoParent() + "_" + info.getPayType());
                        if (parentInfo == null) {
                            parentInfo = tpMerchantDao.getTpMerchantVOByMerchantNoAndPayType(info.getMerchantNoParent(), info.getPayType());
                            if (parentInfo != null) {
                                parentMerchantMap.put("parent_" + info.getMerchantNoParent() + "_" + info.getPayType(), parentInfo);
                            }
                        }
                        if (parentInfo == null) {
                            errorMsgList.add(TtPayConstant.TTPAY_MESSAGE_MERCHANTNO_ISNOTEXIST);
                            errorMap.put("parent_" + info.getMerchantNoParent(), TtPayConstant.TTPAY_MESSAGE_MERCHANTNO_ISNOTEXIST);
                        } else {
                            info.setParentId(parentInfo.getId());
                            info.setCorpId(parentInfo.getCorpId());
                            info.setCorpCode(parentInfo.getCorpCode());
                            info.setCorpName(parentInfo.getCorpName());
                            if (StringUtils.isEmpty(info.getCorpCode())
                                    && StringUtils.isNotEmpty(info.getCorpId())) {
                                if (corpMap.containsKey(parentInfo.getCorpId())) {
                                    corpDTO = corpMap.get(parentInfo.getCorpId());
                                }
                                if (corpDTO == null) {
                                    corpDTO = corpService.get(parentInfo.getCorpId());
                                    if (corpDTO != null) {
                                        corpMap.put(corpDTO.getId(), corpDTO);
                                        corpMap.put(corpDTO.getCode(), corpDTO);
                                        corpMap.put(corpDTO.getName(), corpDTO);
                                    }
                                }
                                if (corpDTO != null) {
                                    info.setCorpCode(corpDTO.getCode());
                                    info.setCorpName(corpDTO.getName());
                                }
                            }
                        }
                    }
                }
                if (!corpAuthMap.containsKey(info.getCorpId()) && StringUtils.isNotEmpty(info.getCorpId())) {
                    errorMsgList.add(TtPayConstant.TTPAY_EXCEL_ERRORMSG_CORPAUTH_ERROR);
                }
                info.addErrorMsg(errorMsgList);
            }
            // this.////inject(list);
        }
    }
    /**
     * 通过商户编号，查找对应商户
     *
     * @param merchantNo
     * @return
     */
    public TpMerchant getTpMerchant(String merchantNo) {
        return tpMerchantDao.getTpMerchant(merchantNo);
    }
    @Override
    @Transactional(readOnly = true)
    public ExcelVerifyHanlderResult verifyHandler(TpMerchantVO obj, Object... user) {
        // TODO Auto-generated method stub
        return null;
    }
    /**
     * 根据单位ID，支付方式过滤商户信息
     *
     * @param corpId  单位ID
     * @param payType 支付类型
     * @return 商户信息
     */
    public List<TpMerchantVO> getByList(String corpId, String payType) {
        List<TpMerchantVO> list = tpMerchantDao.getByList(corpId, payType);
        this.setJoinField(list);
        return list;
    }
    public List<TpMerchantVO> getNewMerchantNo(List<TpMerchantVO> tpMerchantVOList) {
        List<TpMerchantVO> list = new ArrayList<>();
        for (TpMerchantVO vo : tpMerchantVOList) {
            String merchantNo = vo.getMerchantNo() + "(" + vo.getPayTypeName() + ")";
            vo.setMerchantNo(merchantNo);
            list.add(vo);
        }
        return list;
    }
}
