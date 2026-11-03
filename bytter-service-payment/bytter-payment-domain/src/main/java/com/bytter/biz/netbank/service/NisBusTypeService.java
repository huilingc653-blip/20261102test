package com.bytter.biz.netbank.service;

import com.bytter.biz.bdlink.api.enums.NetbankMessageEnum;
import com.bytter.biz.budget.spi.dto.FbsItemDTO;
import com.bytter.biz.budget.spi.service.IFbsItemService;
import com.bytter.biz.netbank.dao.NisBusTypeDao;
import com.bytter.biz.netbank.dao.NisBusTypeRuleDao;
import com.bytter.biz.netbank.entity.NisBusType;
import com.bytter.biz.netbank.entity.NisBusTypeRule;
import com.bytter.biz.netbank.vo.NisBusTypeExcleVO;
import com.bytter.biz.netbank.vo.NisBusTypeRuleVO;
import com.bytter.biz.netbank.vo.NisBusTypeVO;
import com.bytter.biz.netbank.vo.NisCorpBusVO;
import com.bytter.biz.payment.api.constant.NisBusTypeConstant;
import com.bytter.biz.payment.api.dto.NisBusTypeDTO;
import com.bytter.biz.payment.api.dto.NisBusTypeRuleDTO;
import com.bytter.biz.payment.api.service.INisBusTypeService;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.exception.BusinessException;
import com.bytter.core.common.exception.ParameterException;
import com.bytter.core.constant.CommonConstant;
import com.bytter.core.utils.BeanUtils;
import com.bytter.core.utils.DateUtils;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.core.utils.StringUtils;
import com.bytter.poi.excel.entity.result.ExcelVerifyHanlderResult;
import com.bytter.poi.handler.inter.IExcelVerifyHandler;
import com.bytter.system.api.IDictService;
import com.bytter.system.api.Ii18nService;
import com.bytter.system.api.dto.DictDTO;
import com.bytter.system.api.dto.I18nDTO;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * @author onlineGenerator
 * @version 1.0
 * @Title: Service
 * @Description: 业务类别表
 * @date 2017-05-17 19:23:52
 */
@Service("nisBusTypeService")
@Transactional
@DubboService
public class NisBusTypeService extends BaseService<NisBusType, String> implements INisBusTypeService, IExcelVerifyHandler<NisBusTypeExcleVO> {
    @Autowired
    private NisBusTypeDao nisBusTypeDao;
    @SuppressWarnings("unused")
    @DubboReference(check = false)
    private IDictService dictService;
    @Autowired
    private NisCorpBusService nisCorpBusService;
    @DubboReference(check = false)
    private IFbsItemService iFbsItemService;
    @DubboReference(check = false)
    private Ii18nService ii18nService;
    @Autowired(required = false)
    private NisBusTypeRuleDao nisBusTypeRuleDao;

    /**
     * 删除对象
     *
     * @param entity
     */
    @Override
    public void deleteEntity(NisBusType entity) {
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
    public void save(NisBusType entity) {
        super.saveEntity(entity);
    }

    /**
     * 保存
     *
     * @param entity
     */
    public void save(NisBusType entity, String locale, HttpServletRequest request) {
        super.saveEntity(entity);
        //新增当前语种
        I18nDTO i18nDTO = new I18nDTO();
        i18nDTO.setKeyId(entity.getId());
        i18nDTO.setLocale(locale);
        i18nDTO.setContext(entity.getBusTypeName());
        ii18nService.saveOrUpdateBusinessBaseContext(i18nDTO);
        //如果是收款则保存业务种类规则
        if ((!("1".equals(entity.getSysBusType()) || "2".equals(entity.getSysBusType()) || "3".equals(entity.getSysBusType())))) {
            String[] matchRule = request.getParameterValues("matchRule");
            String[] matchElements = request.getParameterValues("matchElements");
            String[] matchRelations = request.getParameterValues("matchRelations");
            String[] matchContent = request.getParameterValues("matchContent");
            String[] leftParenthesis = request.getParameterValues("leftParenthesis");
            String[] rightParenthesis = request.getParameterValues("rightParenthesis");
            List<NisBusTypeRule> nisBusTypeRuleList = new ArrayList<>();
            int leftCount = 0;
            int leftSort = 0;
            int rightCount = 0;
            int rightSort = 0;
            for (int i = 0; i < matchElements.length; i++) {
                if (StringUtils.isNotEmpty(matchElements[i].trim())) {
                    NisBusTypeRule nisBusTypeRule = new NisBusTypeRule();
                    nisBusTypeRule.setMatchRule(matchRule[i]);
                    nisBusTypeRule.setMatchElements(matchElements[i]);
                    if (!EmptyUtils.isEmpty(matchRelations) && !EmptyUtils.isEmpty(matchRelations[i])) {
                        nisBusTypeRule.setMatchRelations(matchRelations[i]);
                    }
                    nisBusTypeRule.setMatchContent(matchContent[i]);
                    nisBusTypeRule.setSort(i);
                    nisBusTypeRule.setBusId(entity.getId());
                    if (!EmptyUtils.isEmpty(leftParenthesis) && leftParenthesis.length > i) {
                        nisBusTypeRule.setLeftParenthesis(leftParenthesis[i]);
                        if ("(".equals(leftParenthesis[i])) {
                            leftCount++;
                            leftSort += i;
                        }
                    }
                    if (!EmptyUtils.isEmpty(rightParenthesis) && rightParenthesis.length > i) {
                        nisBusTypeRule.setRightParenthesis(rightParenthesis[i]);
                        if (")".equals(rightParenthesis[i])) {
                            rightCount++;
                            rightSort += i;
                        }
                    }
                    nisBusTypeRuleList.add(nisBusTypeRule);
                }
            }
            if (leftCount != rightCount || leftSort > rightSort) {
                throw new BusinessException(NetbankMessageEnum.NIS_ASSIGN_MESSAGE_BUSTYPE_MATCHRULE_PARENTHESIS);
            }
            nisBusTypeRuleDao.batchSave(nisBusTypeRuleList);
        }
    }

    /**
     * 保存or更新
     *
     * @param entity
     */
    public void saveOrUpdate(NisBusType entity, String locale, HttpServletRequest request) {
        if (entity.getValidSign().equals("0")) {
            boolean flag = this.nisCorpBusService.isUsedBusType(entity.getId());
            if (flag) {
                throw new ParameterException(NetbankMessageEnum.NIS_ASSIGN_MESSAGE_BUSTYPEID_USE_NO_VALIDSIGN, entity.getBusTypeName());
            }
        }
        super.saveOrUpdate(entity);
        //更新当前语种
        I18nDTO i18nDTO = new I18nDTO();
        i18nDTO.setKeyId(entity.getId());
        i18nDTO.setLocale(locale);
        i18nDTO.setContext(entity.getBusTypeName());
        ii18nService.saveOrUpdateBusinessBaseContext(i18nDTO);
        int leftCount = 0;
        int leftSort = 0;
        int rightCount = 0;
        int rightSort = 0;
        //如果是收款则保存业务种类规则
        if ((!("1".equals(entity.getSysBusType()) || "2".equals(entity.getSysBusType()) || "3".equals(entity.getSysBusType())))) {
            String[] matchRule = request.getParameterValues("matchRule");
            String[] matchElements = request.getParameterValues("matchElements");
            String[] matchRelations = request.getParameterValues("matchRelations");
            String[] matchContent = request.getParameterValues("matchContent");
            String[] leftParenthesis = request.getParameterValues("leftParenthesis");
            String[] rightParenthesis = request.getParameterValues("rightParenthesis");
            List<NisBusTypeRule> nisBusTypeRuleList = new ArrayList<>();
            for (int i = 0; i < matchElements.length; i++) {
                if (StringUtils.isNotEmpty(matchElements[i].trim())) {
                    NisBusTypeRule nisBusTypeRule = new NisBusTypeRule();
                    nisBusTypeRule.setMatchRule(matchRule[i]);
                    nisBusTypeRule.setMatchElements(matchElements[i]);
                    nisBusTypeRule.setMatchRelations(matchRelations[i]);
                    nisBusTypeRule.setMatchContent(matchContent[i]);
                    nisBusTypeRule.setSort(i);
                    nisBusTypeRule.setBusId(entity.getId());
                    if (!EmptyUtils.isEmpty(leftParenthesis) && leftParenthesis.length > i) {
                        nisBusTypeRule.setLeftParenthesis(leftParenthesis[i]);
                        if ("(".equals(leftParenthesis[i])) {
                            leftCount++;
                            leftSort += i;
                        }
                    }
                    if (!EmptyUtils.isEmpty(rightParenthesis) && rightParenthesis.length > i) {
                        nisBusTypeRule.setRightParenthesis(rightParenthesis[i]);
                        if (")".equals(rightParenthesis[i])) {
                            rightCount++;
                            rightSort += i;
                        }
                    }
                    nisBusTypeRuleList.add(nisBusTypeRule);
                }
            }
            if (leftCount != rightCount || leftSort > rightSort) {
                throw new BusinessException(NetbankMessageEnum.NIS_ASSIGN_MESSAGE_BUSTYPE_MATCHRULE_PARENTHESIS);
            }
            //先删除规则再插入
            nisBusTypeRuleDao.deleteByBusId(entity.getId());
            nisBusTypeRuleDao.batchSave(nisBusTypeRuleList);
        } else {
            if (leftCount != rightCount || leftSort > rightSort) {
                throw new BusinessException(NetbankMessageEnum.NIS_ASSIGN_MESSAGE_BUSTYPE_MATCHRULE_PARENTHESIS);
            }
            nisBusTypeRuleDao.deleteByBusId(entity.getId());
        }
    }

    /**
     * 更新
     *
     * @param entity
     */
    public void update(NisBusType entity) {
        super.updateEntity(entity);
    }

    /**
     * 保存or更新
     *
     * @param entity
     */
    @Override
    public void saveOrUpdate(NisBusType entity) {
        super.saveOrUpdate(entity);
    }

    /**
     * 删除对象
     *
     * @param entity
     */
    public void deleteNisBusType(NisBusType entity) {
        boolean flag = this.nisCorpBusService.isUsedBusType(entity.getId());
        if (flag) {
            throw new ParameterException(NetbankMessageEnum.NIS_ASSIGN_MESSAGE_BUSTYPEID_USE, entity.getBusTypeName());
        }
        this.deleteEntity(entity);
        //删除多语言
        ii18nService.deleteBusinessBaseContext(entity.getId());
        //删除匹配规则
        nisBusTypeRuleDao.deleteByBusId(entity.getId());
    }

    /**
     * 批量删除对象
     *
     * @param ids
     */
    public void batchDelete(String ids) {
        for (String id : ids.split(CommonConstant.COMMA_SPLIT)) {
            NisBusType entity = this.get(id);
            this.deleteNisBusType(entity);
            //删除多语言
            ii18nService.deleteBusinessBaseContext(entity.getId());
        }
    }

    @Override
    public BaseDao<NisBusType, String> getEntityDao() {
        return nisBusTypeDao;
    }

    /**
     * 查询业务类型列表
     *
     * @param input
     * @return
     */
    @SuppressWarnings("rawtypes")
    public DataTablesOutput findListBysql(DataTablesInput input) {
        // 初始化各租户默认的三个业务类型
        this.saveDefaultBusType(input.getCurrentUser().getTenantId());
        DataTablesOutput output = nisBusTypeDao.findListBysql(input);
        List<NisBusTypeVO> list = output.getData();
        for (NisBusTypeVO vo : list) {
            vo.setCdSignStr(dictService.get("nisBusType.cdSign", vo.getCdSign()));
            FbsItemDTO fbsItemDTO = iFbsItemService.getFbsItemByCode(vo.getItemCode());
            if (!EmptyUtils.isEmpty(fbsItemDTO)) {
                vo.setItemName(fbsItemDTO.getItemName());
            }
        }
        return output;
    }

    /**
     * 查询有效的业务种类列表
     */
    public List<NisBusTypeVO> getNisBusType(String validSign) {
        List<NisBusTypeVO> voList = nisBusTypeDao.listNisBusType(validSign, null);
        for (NisBusTypeVO vo : voList) {
            vo.setCdSignStr(dictService.get("nisBusType.cdSign", vo.getCdSign()));
            FbsItemDTO fbsItemDTO = iFbsItemService.getFbsItemByCode(vo.getItemCode());
            if (!EmptyUtils.isEmpty(fbsItemDTO)) {
                vo.setItemName(fbsItemDTO.getItemName());
            }
        }
        return voList;
    }

    /**
     * 根据编码获取业务类型
     *
     * @param busType
     * @return
     */
    public NisBusType findBusType(String busType) {
        return nisBusTypeDao.findBusType(busType);
    }

    /**
     * 获取有效的业务种类
     *
     * @return
     */
    public List<NisBusType> getValidBusType() {
        return nisBusTypeDao.getValidBusType();
    }

    /**
     * 根据ID查询业务类型信息
     *
     * @param id
     * @return
     */
    public NisBusTypeVO getByUnion(String id) {
        NisBusTypeVO vo = this.nisBusTypeDao.getByUnion(id);
        FbsItemDTO fbsItemDTO = iFbsItemService.getFbsItemByCode(vo.getItemCode());
        if (!EmptyUtils.isEmpty(fbsItemDTO)) {
            vo.setItemName(fbsItemDTO.getItemName());
        }
        vo.setCdSignStr(dictService.get("nisBusType.cdSign", vo.getCdSign()));
        vo.setValidSign(dictService.get("valid_sign", vo.getValidSign()));
        vo.setSysBusType(dictService.get("SYS_BUS_TYPE", vo.getSysBusType()));
        vo.setSettleType(dictService.get("settlType", vo.getSettleType()));
        return vo;
    }

    /**
     * 导入保存
     *
     * @param list
     */
    public void saveImport(List<NisBusTypeExcleVO> list, ICurrentUser user) {
        for (NisBusTypeExcleVO nbVO : list) {
            NisBusType nb = new NisBusType();
            BeanUtils.copyNotNullProperties(nbVO, nb);
            nb.setValidSign(NisBusTypeConstant.VALID_TRUE);
            nb.setCreateBy(user.getLoginName());
            nb.setCreateDate(DateUtils.getDate());
            this.save(nb);
//            //如果是收款保存匹配规则
//            if(NisBusTypeConstant.CD_SIGN_PAYEE.equals(nb.getCdSign())&&
//                    (nb.getSysBusType().compareTo("3") > 0 || "10".equals(nb.getSysBusType()))){
//                List<NisBusTypeRule> nisBusTypeRuleList = new ArrayList<>();
//                String matchRule = nbVO.getMatchRule();
//                String matchElements = nbVO.getMatchElements();
//                String matchRelations = nbVO.getMatchRelations();
//                String matchContent = nbVO.getMatchContent();
//                String[] matchRuleStr = null;
//                String[] matchElementsStr = null;
//                String[] matchRelationsStr = null;
//                String[] matchContentStr = null;
//                if(!StringUtils.isEmpty(matchRule)) {
//                    matchRule = matchRule.replace("；", ";");
//                    matchRuleStr = matchRule.split(";");
//                }
//                if(!StringUtils.isEmpty(matchElements)) {
//                    matchElements = matchElements.replace("；", ";");
//                    matchElementsStr = matchElements.split(";");
//                }
//                if(!StringUtils.isEmpty(matchRelations)) {
//                    matchRelations = matchRelations.replace("；", ";");
//                    matchRelationsStr = matchRelations.split(";");
//                }
//                if(!StringUtils.isEmpty(matchRelations)) {
//                    matchContent = matchContent.replace("；", ";");
//                    matchContentStr = matchContent.split(";");
//                }
//                if(!EmptyUtils.isEmpty(matchElementsStr)) {
//                    for (int i = 0; i < matchElementsStr.length; i++) {
//                        if (StringUtils.isNotEmpty(matchElementsStr[i].trim())) {
//                            NisBusTypeRule nisBusTypeRule = new NisBusTypeRule();
//                            String matchRuleTemp = "";
//                            String matchElementsTemp = "";
//                            String matchRelationsTemp = "";
//                            String matchContentTemp = "";
//                            matchElementsTemp = iDictService.getCodeByName("nisBusTypeRule.matchElements", matchElementsStr[i]);
//                            if(!EmptyUtils.isEmpty(matchRuleStr)) {
//                                if (matchRuleStr.length > i) {
//                                    matchRuleTemp = matchRuleStr[i];
//                                } else if (matchRuleStr.length > 0) {
//                                    matchRuleTemp = matchRuleStr[0];
//                                }
//                            }
//                            matchRuleTemp = iDictService.getCodeByName("FUNDWARN_NUM_OPERATOR", matchRuleTemp);
//                            if (matchRelationsStr.length > i) {
//                                matchRelationsTemp = matchRelationsStr[i];
//                                matchRelationsTemp = iDictService.getCodeByName("nisBusTypeRule.matchRelations", matchRelationsTemp);
//                            }
//                            if (matchContentStr.length > i) {
//                                matchContentTemp = matchContentStr[i];
//                            }
//                            if(EmptyUtils.isEmpty(matchRuleTemp)){
//                                matchRuleTemp = "02";
//                            }
//                            if(EmptyUtils.isEmpty(matchElementsTemp)){
//                                continue;
//                            }
//                            nisBusTypeRule.setMatchRule(matchRuleTemp);
//                            nisBusTypeRule.setMatchElements(matchElementsTemp);
//                            nisBusTypeRule.setMatchRelations(matchRelationsTemp);
//                            nisBusTypeRule.setMatchContent(matchContentTemp);
//                            nisBusTypeRule.setSort(i);
//                            nisBusTypeRule.setBusId(nb.getId());
//                            boolean isHasRule = nisBusTypeRuleDao.isHas(nisBusTypeRule.getMatchRule(), nisBusTypeRule.getMatchElements(), nisBusTypeRule.getMatchRelations(), nisBusTypeRule.getMatchContent());
//                            if (isHasRule) {
//                                String message = iDictService.get("FUNDWARN_NUM_OPERATOR", nisBusTypeRule.getMatchRule())
//                                        + iDictService.get("nisBusTypeRule.matchElements", nisBusTypeRule.getMatchElements())
//                                        + iDictService.get("nisBusTypeRule.matchRelations", nisBusTypeRule.getMatchRelations())
//                                        + nisBusTypeRule.getMatchContent();
//                                throw new BusinessException(NetbankMessageEnum.NIS_ASSIGN_MESSAGE_BUSTYPE_MATCHRULE_REPEAT, message);
//                            }
//                            nisBusTypeRuleList.add(nisBusTypeRule);
//                        }
//                    }
//                }
//                nisBusTypeRuleDao.batchSave(nisBusTypeRuleList);
//            }
        }
    }

    /**
     * 上传附件相关校验
     *
     * @param nbVO
     * @return
     */
    @Override
    public ExcelVerifyHanlderResult verifyHandler(NisBusTypeExcleVO nbVO) {
        List<String> errMessage = new ArrayList<String>();
        // 业务种类编码必须唯一
        if (nisBusTypeDao.findBusType(nbVO.getBusType()) != null) {
            errMessage.add("nis.assign.message.bustype.unique");
        }
        // 收付款标志
        if (!NisBusTypeConstant.CD_SIGN_PAYEE.equals(nbVO.getCdSign()) && !NisBusTypeConstant.CD_SIGN_PAYER.equals(nbVO.getCdSign())) {
            errMessage.add("收付款标志格式错误");
        }
        // 计划科目
        if (EmptyUtils.isNotEmpty(nbVO.getItemCode())) {
            FbsItemDTO fbsItemDTO = iFbsItemService.getFbsItemByCode(nbVO.getItemCode());
            if (EmptyUtils.isEmpty(fbsItemDTO)) {
                errMessage.add("nis.assign.message.bustype.itemcode.null");
            } else {
                if (NisBusTypeConstant.CD_SIGN_PAYEE.equals(nbVO.getCdSign())) {
                    if (fbsItemDTO.getSign() < 0) {
                        //收款和收款计划科目不匹配（不是收款科目）
                        errMessage.add("nis.assign.message.bustype.itemcode.mismatchings");
                    }
                } else if (NisBusTypeConstant.CD_SIGN_PAYER.equals(nbVO.getCdSign())) {
                    if (fbsItemDTO.getSign() > 0) {
                        //付款和付款计划科目不匹配（不是付款科目）
                        errMessage.add("nis.assign.message.bustype.itemcode.mismatchingf");
                    }
                }
            }
        }
        // 对应系统业务类别
        if (EmptyUtils.isNotEmpty(nbVO.getSysBusTypeName())) {
            String sysBusType = dictService.getCodeByName("SYS_BUS_TYPE", nbVO.getSysBusTypeName());
            if (StringUtils.isEmpty(sysBusType)) {
                errMessage.add("对应系统业务类别不存在！");
            } else {
                if (NisBusTypeConstant.SYS_BUS_TYPE_1.equals(sysBusType) || NisBusTypeConstant.SYS_BUS_TYPE_2.equals(sysBusType) || NisBusTypeConstant.SYS_BUS_TYPE_3.equals(sysBusType)) {
                    errMessage.add("该系统业务类型不能使用");
                } else {
                    nbVO.setSysBusType(sysBusType);
                }
            }
        }
        // 结算类型
        if (EmptyUtils.isNotEmpty(nbVO.getSettleTypeName())) {
            String settleType = dictService.getCodeByName("settlType", nbVO.getSettleTypeName());
            if (StringUtils.isEmpty(settleType)) {
                errMessage.add("结算类型不存在！");
            } else {
                nbVO.setSettleType(settleType);
            }
        }
        return new ExcelVerifyHanlderResult(false, errMessage);
    }

    @Override
    public void verifyHandler(List<NisBusTypeExcleVO> list) {

    }

    /**
     * 根据业务id获取所有业务规则
     *
     * @param busId
     * @return
     */
    public List<NisBusTypeRuleVO> getByByBusId(String busId) {
        return nisBusTypeRuleDao.getByByBusId(busId);
    }

    /**
     * 根据业务id获取所有业务规则
     *
     * @param busId
     * @return
     */
    public List<NisBusTypeRuleVO> getByByBusIdNoParenthesis(String busId) {
        return nisBusTypeRuleDao.getByByBusIdNoParenthesis(busId);
    }

    /**
     * 根据业务id获取所有业务规则
     *
     * @param busId
     * @return
     */
    public List<NisBusTypeRuleVO> getByByBusIdParenthesis(String busId) {
        return nisBusTypeRuleDao.getByByBusIdParenthesis(busId);
    }

    /**
     * 获取有效的业务类别
     *
     * @return
     */
    @Override
    public List<NisBusTypeDTO> getNisBusTypeAll(String corpId, String cdSign) {
        List<NisBusTypeDTO> nisBusTypeDTOList = new ArrayList<>();
        List<NisCorpBusVO> list = this.nisCorpBusService.getBySysBusType(corpId, cdSign, null);
        if (!EmptyUtils.isEmpty(list)) {
            for (NisCorpBusVO nisCorpBusVO : list) {
                NisBusType nisBusType = this.nisBusTypeDao.get(nisCorpBusVO.getBusId());
                if (!EmptyUtils.isEmpty(nisBusType)) {
                    NisBusTypeDTO nisBusTypeDTO = new NisBusTypeDTO();
                    BeanUtils.copyNotNullProperties(nisBusType, nisBusTypeDTO);
                    FbsItemDTO fbsItemDTO = iFbsItemService.getFbsItemByCode(nisBusTypeDTO.getItemCode());
                    if (!EmptyUtils.isEmpty(fbsItemDTO)) {
                        nisBusTypeDTO.setItemName(fbsItemDTO.getItemName());
                    }
                    nisBusTypeDTOList.add(nisBusTypeDTO);
                }
            }
        }
        return nisBusTypeDTOList;
    }

    /**
     * 根据业务类别获取匹配规则
     *
     * @param busId
     * @return
     */
    @Override
    public List<List<NisBusTypeRuleDTO>> getNisBusTypeRuleDTOByBusId(String busId) {
        List<List<NisBusTypeRuleDTO>> list = new ArrayList<>();
        List<NisBusTypeRuleVO> nisBusTypeRuleVOParenthesisList = this.getByByBusId(busId);
        List<NisBusTypeRuleVO> tempList = null;
        //是否开括号了
        Boolean isParenthesis = false;
        //没有括号的条件放一起
        List<NisBusTypeRuleVO> tempListNoParenthesis = new ArrayList<>();
        for (NisBusTypeRuleVO vo : nisBusTypeRuleVOParenthesisList) {
            if ("(".equals(vo.getLeftParenthesis())) {
                isParenthesis = true;
                tempList = new ArrayList<>();
                tempList.add(vo);
            } else if (")".equals(vo.getRightParenthesis())) {
                tempList.add(vo);
                List<NisBusTypeRuleDTO> tempDTOList = new ArrayList<>();
                if (!EmptyUtils.isEmpty(tempList)) {
                    tempDTOList = BeanUtils.copyListProperties(tempList, NisBusTypeRuleDTO.class);
                }
                isParenthesis = false;
                if (tempDTOList.size() > 0) {
                    list.add(tempDTOList);
                }
            } else if (isParenthesis) {
                tempList.add(vo);
            } else {
                tempListNoParenthesis.add(vo);
            }
        }
        List<NisBusTypeRuleDTO> tempDTONoParenthesisList = new ArrayList<>();
        if (!EmptyUtils.isEmpty(tempListNoParenthesis)) {
            tempDTONoParenthesisList = BeanUtils.copyListProperties(tempListNoParenthesis, NisBusTypeRuleDTO.class);
        }
        if (tempDTONoParenthesisList.size() > 0) {
            list.add(tempDTONoParenthesisList);
        }
        return list;
    }

    /**
     * 根据系统业务类别得到业务类别
     *
     * @return
     */
    @Override
    public List<NisBusTypeDTO> getBySysBusType(String corpId, String cdSign, String sysBusType) {
        List<NisBusTypeDTO> nisBusTypeDTOList = new ArrayList<>();
        List<NisCorpBusVO> list = this.nisCorpBusService.getBySysBusType(corpId, cdSign, sysBusType);
        if (!EmptyUtils.isEmpty(list)) {
            for (NisCorpBusVO nisCorpBusVO : list) {
                NisBusType nisBusType = this.nisBusTypeDao.get(nisCorpBusVO.getBusId());
                if (!EmptyUtils.isEmpty(nisBusType)) {
                    NisBusTypeDTO nisBusTypeDTO = new NisBusTypeDTO();
                    BeanUtils.copyNotNullProperties(nisBusType, nisBusTypeDTO);
                    FbsItemDTO fbsItemDTO = iFbsItemService.getFbsItemByCode(nisBusTypeDTO.getItemCode());
                    if (!EmptyUtils.isEmpty(fbsItemDTO)) {
                        nisBusTypeDTO.setItemName(fbsItemDTO.getItemName());
                    }
                    nisBusTypeDTOList.add(nisBusTypeDTO);
                }
            }
        }
        return nisBusTypeDTOList;
    }

    @Override
    public NisBusTypeDTO getById(String id) {
        NisBusTypeDTO nisBusTypeDTO = new NisBusTypeDTO();
        if (EmptyUtils.isEmpty(id)) {
            return null;
        }
        NisBusType nisBusType = nisBusTypeDao.findBusTypeByIdOrType(id);
        if (!EmptyUtils.isEmpty(nisBusType)) {
            BeanUtils.copyNotNullProperties(nisBusType, nisBusTypeDTO);
        } else {
            return null;
        }
        return nisBusTypeDTO;
    }

    @Override
    public NisBusTypeDTO getByBusType(String busType) {
        return BeanUtils.copyNewInstanceProperties(nisBusTypeDao.getByBusType(busType), NisBusTypeDTO.class);
    }

    /**
     * 每个租户都有三个默认的业务类型
     *
     * @param tenantId 租户id
     */
    private void saveDefaultBusType(String tenantId) {
        // 1、请款或下拨
        String busType = NisBusTypeConstant.SYS_BUS_TYPE_BS001;
        // 查询该业务类型是否已初始化
        NisBusType nisBusType = nisBusTypeDao.findBusType(busType);
        if (null == nisBusType) {
            // 未初始化，则新增
            nisBusType = new NisBusType();
            nisBusType.setBusType(busType);
            nisBusType.setBusTypeName("请款或下拨");
            // 收款
            nisBusType.setCdSign(NisBusTypeConstant.CD_SIGN_PAYEE);
            // 默认有效
            nisBusType.setValidSign(NisBusTypeConstant.VALID_TRUE);
            // 请款或下拨
            nisBusType.setSysBusType(NisBusTypeConstant.SYS_BUS_TYPE_1);
            nisBusType.setSettleType("0");
            nisBusType.setTenantId(tenantId);
            this.saveEntity(nisBusType);
        }
        // 2、上缴或上划
        busType = NisBusTypeConstant.SYS_BUS_TYPE_BS002;
        // 查询该业务类型是否已初始化
        nisBusType = nisBusTypeDao.findBusType(busType);
        if (null == nisBusType) {
            // 未初始化，则新增
            nisBusType = new NisBusType();
            nisBusType.setBusType(busType);
            nisBusType.setBusTypeName("上缴或上划");
            // 收款
            nisBusType.setCdSign(NisBusTypeConstant.CD_SIGN_PAYEE);
            // 默认有效
            nisBusType.setValidSign(NisBusTypeConstant.VALID_TRUE);
            // 上缴或上划
            nisBusType.setSysBusType(NisBusTypeConstant.SYS_BUS_TYPE_2);
            nisBusType.setSettleType("0");
            nisBusType.setTenantId(tenantId);
            this.saveEntity(nisBusType);
        }
        // 3、头寸调拨
        busType = NisBusTypeConstant.SYS_BUS_TYPE_BS003;
        // 查询该业务类型是否已初始化
        nisBusType = nisBusTypeDao.findBusType(busType);
        if (null == nisBusType) {
            // 未初始化，则新增
            nisBusType = new NisBusType();
            nisBusType.setBusType(busType);
            nisBusType.setBusTypeName("头寸调拨");
            // 收款
            nisBusType.setCdSign(NisBusTypeConstant.CD_SIGN_PAYEE);
            // 默认有效
            nisBusType.setValidSign(NisBusTypeConstant.VALID_TRUE);
            // 头寸调拨
            nisBusType.setSysBusType(NisBusTypeConstant.SYS_BUS_TYPE_3);
            nisBusType.setSettleType("0");
            nisBusType.setTenantId(tenantId);
            this.saveEntity(nisBusType);
        }
        this.getSession().flush();
        this.getSession().clear();
    }
}
