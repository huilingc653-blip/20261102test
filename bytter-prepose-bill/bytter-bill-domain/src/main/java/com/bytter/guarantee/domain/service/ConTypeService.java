package com.bytter.guarantee.domain.service;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.utils.BeanUtils;
import com.bytter.core.utils.ListUtils;
import com.bytter.core.utils.StringUtils;
import com.bytter.guarantee.constant.ConConstant;
import com.bytter.guarantee.domain.dao.ConInfoDao;
import com.bytter.guarantee.domain.dao.ConTypeDao;
import com.bytter.guarantee.domain.dao.ConTypeFieldDao;
import com.bytter.guarantee.domain.entity.ConType;
import com.bytter.guarantee.domain.entity.ConTypeField;
import com.bytter.guarantee.domain.vo.ConTypeFieldVO;
import com.bytter.guarantee.domain.vo.ConTypeVO;
import com.bytter.system.api.ICorpService;
import com.bytter.system.api.Ii18nService;
import com.bytter.system.api.dto.I18nDTO;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
@Service("conTypeService")
@Transactional
public class ConTypeService extends BaseService<ConType, String> {
    @Autowired
    private ConInfoDao conInfoDao;
    @DubboReference(check = false)
    private ICorpService corpService;
    @Autowired
    private ConLogService log;
    @Autowired
    private ConTypeDao conTypeDao;
    @Autowired
    private ConCorpService conCorpService;
    @Autowired
    private ConTypeFieldDao conTypeFieldDao;
    @DubboReference(check = false)
    private Ii18nService ii18nService;
    @SuppressWarnings("rawtypes")
    @Transactional(readOnly = true)
    public DataTablesOutput getDataTablesUnion(DataTablesInput input) {
        // 获取该用户有权限的单位id集
        List<String> corpAuthList = corpService.getUserCorpIds(ConConstant.CONTRACT_TYPE_MANAGE_FST_CORP,
                input.getCurrentUserId());
        return conTypeDao.listContractTypeDate(input, corpAuthList);
    }
    @Override
    public BaseDao<ConType, String> getEntityDao() {
        return null;
    }
    /**
     * 
     * @Title: save @Description: TODO(保存方法) @param @param conType @param @param user 参数 @return void 返回类型 @throws
     */
    public void save(ConTypeVO conTypeVo, ICurrentUser user) {
        ConType conType = new ConType();
        BeanUtils.copyNotNullProperties(conTypeVo, conType);
        conType.setCreateId(user.getId());
        conType.setCreateTime(new Date());
        conTypeDao.save(conType);
        if (ii18nService != null) {
            ii18nService.saveOrUpdateBusinessBaseContext(
                    new I18nDTO(conType.getId(), LocaleContextHolder.getLocale().toString(), conType.getTypeName()));
        }
        if (null != conTypeVo.getConTypeFieldVO() && conTypeVo.getConTypeFieldVO().size() > 0) {
            for (ConTypeFieldVO fileVo : conTypeVo.getConTypeFieldVO()) {
                ConTypeField fieldObj = new ConTypeField();
                BeanUtils.copyNotNullProperties(fileVo, fieldObj);
                fieldObj.setTypeId(conType.getId());
                fieldObj.setValidSign(null != fieldObj.getValidSign()
                        && fieldObj.getValidSign().equals(ConConstant.CON_TYPE_SETING_CHEBOX) ? "1" : "0");
                fieldObj.setRequiredSign(null != fieldObj.getRequiredSign()
                        && fieldObj.getRequiredSign().equals(ConConstant.CON_TYPE_SETING_CHEBOX) ? "1" : "0");
                conTypeFieldDao.save(fieldObj);
            }
        }
    }
    @Override
    public void delete(List<String> ids) {
        for (String id : ids) {
            conTypeDao.delConTypeById(id);
            conTypeFieldDao.delFileIdTypeId(id);
        }
    }
    /**
     * 
     * @Title: queryConTractIsExist @Description: TODO(重复校验) @param @param contractCode @param @param id @param @return
     *         参数 @return boolean 返回类型 @throws
     */
    public boolean queryConTractIsExist(String contractCode, String id) {
        return conTypeDao.queryConTractIsExist(contractCode, id);
    }
    /**
     * 
     * @Title: updateContractType @Description: TODO(修改合同类型方法) @param @param conTypeVo @param @param user 参数 @return
     *         void 返回类型 @throws
     */
    public void updateContractType(ConTypeVO conTypeVo, ICurrentUser user) {
        ConType conType = new ConType();
        BeanUtils.copyNotNullProperties(conTypeVo, conType);
        conType.setUpdateId(user.getId());
        conType.setUpdateTime(new Date());
        conTypeDao.update(conType);
        if (ii18nService != null) {
            ii18nService.saveOrUpdateBusinessBaseContext(new I18nDTO(conTypeVo.getId(),
                    LocaleContextHolder.getLocale().toString(), conTypeVo.getTypeName()));
        }
        if (null != conTypeVo.getConTypeFieldVO() && conTypeVo.getConTypeFieldVO().size() > 0) {
            List<String> filedList = conTypeFieldDao.queryFileIdTypeIdList(conTypeVo.getId());
            conTypeFieldDao.delFileIdTypeId(conType.getId());
            for (ConTypeFieldVO fileVo : conTypeVo.getConTypeFieldVO()) {
                if (StringUtils.isNotEmpty(fileVo.getFieldName())) {
                    ConTypeField fieldObj = new ConTypeField();
                    BeanUtils.copyNotNullProperties(fileVo, fieldObj);
                    if (ListUtils.isNotNullOrEmpty(filedList) && !StringUtils.isBlank(fileVo.getId()))
                        fieldObj.setId(filedList.contains(fileVo.getId()) ? fileVo.getId() : null);
                    fieldObj.setTypeId(conType.getId());
                    fieldObj.setValidSign(!StringUtils.isBlank(fieldObj.getValidSign())
                            && fieldObj.getValidSign().equals(ConConstant.CON_TYPE_SETING_CHEBOX) ? "1" : "0");
                    fieldObj.setRequiredSign(!StringUtils.isBlank(fieldObj.getRequiredSign())
                            && fieldObj.getRequiredSign().equals(ConConstant.CON_TYPE_SETING_CHEBOX) ? "1" : "0");
                    conTypeFieldDao.save(fieldObj);
                }
            }
        } else {
            conTypeFieldDao.delFileIdTypeId(conType.getId());
        }
    }
    /**
     * 
     * @Title: getConTypeView @Description: TODO(查看详情) @param @param id @param @return 参数 @return ConTypeDao
     *         返回类型 @throws
     */
    public ConTypeVO getConTypeView(String id) {
        ConTypeVO conTypeVO = conTypeDao.getConTypeVOByID(id);
        if (StringUtils.isNotEmpty(conTypeVO)) {
            conTypeVO.setConTypeFieldVO(conTypeFieldDao.getConTypeFieldByTypeId(conTypeVO.getId()));
        }
        return conTypeVO;
    }
    public List<ConTypeFieldVO> getTypeFieldByTypeId(String typeId) {
        return conTypeFieldDao.getConTypeFieldByTypeId(typeId);
    }
    /**
     * 功能介绍 : 获取维护的有效合同类型<br>
     * <b>日期：</b>2019年11月06日<br>
     * 
     * @version 1.0
     * @author zhenghj
     */
    @Transactional(readOnly = true)
    public List<ConTypeVO> getAllConType(ConTypeVO conTypeVO, ICurrentUser user) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("validSign", ConConstant.CON_VALID_SIGN_1);
        if (StringUtils.isNotEmpty(conTypeVO.getTypeIdVal())) {
            params.put("typeIdVal", conTypeVO.getTypeIdVal());
        }
        if (StringUtils.isNotEmpty(conTypeVO.getCorpId())) {
            params.put("corpId", conTypeVO.getCorpId());
        }
        if (StringUtils.isNotEmpty(conTypeVO.getPdCode())) {
            params.put("pdCode", conTypeVO.getPdCode());
        }
        if (StringUtils.isNotEmpty(conTypeVO.getOpResource())) {
            List<String> corpAuthList = conCorpService.getUserCorpIds(conTypeVO.getOpResource(), user.getId());
            params.put("corpAuthList", corpAuthList);
        }
        return conTypeDao.getAllConType(params);
    }
    /**
     * 判断合同类型是否被引用
     * 
     * @param parmsList
     * @return
     */
    public boolean vnGtOccupy(List<String> parmsList) {
        return conTypeDao.vnGtOccupy(parmsList);
    }
}
