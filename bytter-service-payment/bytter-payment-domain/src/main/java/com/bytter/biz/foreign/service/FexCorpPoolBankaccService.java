package com.bytter.biz.foreign.service;

import com.bytter.biz.foreign.dao.FexCorpPoolBankaccDao;
import com.bytter.biz.foreign.entity.FexCorpPoolBankacc;
import com.bytter.biz.foreign.entity.FexCorpPoolLimit;
import com.bytter.biz.foreign.vo.FexCorpPoolBankAccVO;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.core.common.exception.ParameterException;
import com.bytter.core.annotation.Corp;
import com.bytter.core.annotation.Currency;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.core.utils.StringUtils;
import com.bytter.biz.payment.api.constant.ForeignConstant;
import com.bytter.biz.payment.api.enums.ForeignMessageEnum;
import com.bytter.system.api.dto.BtBankAccDTO;
import com.bytter.system.api.IBtBankAccService;
import com.bytter.system.api.IBtCurrencyService;
import com.bytter.system.api.ICorpService;
import com.bytter.system.api.IDictService;
import com.bytter.system.api.dto.BtCurrencyDTO;
import com.bytter.system.api.dto.CorpDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author wangzx
 * @version 1.0
 * @Title: Service
 * @Description: 外汇资金池账户表
 * @date 2020-02-12 14:31:56
 */
@Service("fexCorpPoolBankaccService")
@Transactional
public class FexCorpPoolBankaccService extends BaseService<FexCorpPoolBankacc, String> {

    @Autowired
    private FexCorpPoolBankaccDao fexCorpPoolBankaccDao;
    @Autowired
    private FexCorpPoolLimitService fexCorpPoolLimitService;
    @Autowired(required = false)
    private IBtBankAccService btBankAccService;
    @Autowired(required = false)
    private IDictService dictService;
    @Autowired(required = false)
    private IBtCurrencyService btCurrencyService;
    @Autowired(required = false)
    private ICorpService corpService;

    @Override
    public BaseDao<FexCorpPoolBankacc, String> getEntityDao() {
        return fexCorpPoolBankaccDao;
    }

    /**
     * 删除对象
     * @param entity
     */
    @Override
    public void deleteEntity(FexCorpPoolBankacc entity){
        super.deleteEntity(entity);
    }
    /**
     * 根据id删除
     * @param id
     */
    @Override
    public void delete(String id){
        super.delete(id);
    }
    /**
     * 保存
     * @param entity
     */
    public void save(FexCorpPoolBankacc entity){
        super.saveEntity(entity);
    }
    /**
     * 更新
     * @param entity
     */
    public void update(FexCorpPoolBankacc entity){
        super.updateEntity(entity);
    }
    /**
     * 保存or更新
     * @param entity
     */
    @Override
    public void saveOrUpdate(FexCorpPoolBankacc entity){
        super.saveOrUpdate(entity);
    }

    /**
     * 根据外汇资金池表id(或主账号id)和币别id和是否主账号标识查找账号
     *
     * @param parentId
     * @param curId
     * @param parentSign
     * @return
     */
    @SuppressWarnings({"unchecked", "rawtypes"})
    public List<FexCorpPoolBankAccVO> listBankAccByParentId(String parentId, String curId, String parentSign) {
        List<FexCorpPoolBankAccVO> fexCorpPoolBankaccVOS = fexCorpPoolBankaccDao.listBankAccByParentId(parentId, curId, parentSign,"");
        injectValue(fexCorpPoolBankaccVOS);
        return fexCorpPoolBankaccVOS;
    }

    /**
     * 根据VO字段上的基础数据注解，为单条资金池账户数据注入展示值。
     *
     * @param vo 资金池账户数据
     */
    public void injectValue(FexCorpPoolBankAccVO vo) {
        if (vo == null) {
            return;
        }
        injectValue(Collections.singletonList(vo));
    }

    /**
     * 根据VO字段上的基础数据注解，批量注入单位、币种等展示值。
     *
     * @param list 资金池账户数据集合
     */
    public void injectValue(List<FexCorpPoolBankAccVO> list) {
        if (EmptyUtils.isEmptyList(list)) {
            return;
        }
        injectCorpValue(list);
        injectCurrencyValue(list);
    }

    /**
     * 根据外汇资金池表id(或主账号id)和币别id和是否主账号标识和账号id查找账号
     *
     * @param parentId
     * @param curId
     * @param parentSign
     * @return
     */
    @SuppressWarnings({"unchecked", "rawtypes"})
    public List<FexCorpPoolBankAccVO> listBankAccByParentAccId(String parentId, String curId, String parentSign, String accId) {
        List<FexCorpPoolBankAccVO> fexCorpPoolBankaccVOS = fexCorpPoolBankaccDao.listBankAccByParentId(parentId, curId, parentSign, accId);
        //this.inject(fexCorpPoolBankaccVOS);
        return fexCorpPoolBankaccVOS;
    }

    /**
     * 根据外汇资金池表id和币别id和是否主账号标识查找账号
     *
     * @param poolId 资金池id
     * @param curId 币别id
     * @param parentSign 是否主账户
     * @return
     */
    @SuppressWarnings({"unchecked", "rawtypes"})
    public List<FexCorpPoolBankAccVO> listBankAccByPoolId(String poolId, String curId, String parentSign, String accId) {
        List<FexCorpPoolBankAccVO> fexCorpPoolBankaccVOS = fexCorpPoolBankaccDao.listBankAccByPoolId(poolId, curId, parentSign, accId);
        //this.inject(fexCorpPoolBankaccVOS);
        return fexCorpPoolBankaccVOS;
    }

    /**
     * 根据资金池id和币别id和是否主账号标识查找账号
     *
     * @param poolId 资金池id
     * @param curId 币别id
     * @param parentSign 是否主账户
     * @return
     */
    @SuppressWarnings({"unchecked", "rawtypes"})
    public List<FexCorpPoolBankAccVO> listMainAccByPoolId(String poolId, String curId, String parentSign) {
        List<FexCorpPoolBankAccVO> fexCorpPoolBankaccVOS = fexCorpPoolBankaccDao.listMainAccByPoolId(poolId, curId, parentSign);
        //this.inject(fexCorpPoolBankaccVOS);
        return fexCorpPoolBankaccVOS;
    }
    /**
     *根据资金池账号id查找账号信息
     * @param id
     * @return
     */
    public FexCorpPoolBankAccVO getFexCorpPoolBankAccVOById(String id){
        FexCorpPoolBankAccVO accVO = fexCorpPoolBankaccDao.getFexCorpPoolBankAccVOById(id);
        List<FexCorpPoolLimit> fexCorpPoolLimits = fexCorpPoolLimitService.listFexCorpPoolLimitByrelatedId(accVO.getId());
        if (!EmptyUtils.isEmptyList(fexCorpPoolLimits)) {
            accVO.setFexCorpPoolLimits(fexCorpPoolLimits);
        }
        List<FexCorpPoolBankAccVO> accList = fexCorpPoolBankaccDao.listBankAccByParentId(id, "", "","");
        if(!EmptyUtils.isEmptyList(accList)){
            accVO.setChildList(accList);
        }
        String limit = "";
        for (int i = 0; i < fexCorpPoolLimits.size(); i++) {
            if (i == 0) {
                limit += fexCorpPoolLimits.get(i).getLimitType();
            } else {
                limit += "," + fexCorpPoolLimits.get(i).getLimitType();
            }
            if (ForeignConstant.FEX_LIMIT_TYPE_1.equals(fexCorpPoolLimits.get(i).getLimitType())) {
                accVO.setLimitMoney1(fexCorpPoolLimits.get(i).getLimitMoney());
            } else if (ForeignConstant.FEX_LIMIT_TYPE_2.equals(fexCorpPoolLimits.get(i).getLimitType())) {
                accVO.setLimitMoney2(fexCorpPoolLimits.get(i).getLimitMoney());
            } else {
                accVO.setLimitMoney3(fexCorpPoolLimits.get(i).getLimitMoney());
            }
        }
        accVO.setLimitType(limit);
        //this.inject(accVO);
        return accVO;
    }

    /**
     * 根据资金池id获取主账号
     *
     * @param poolId
     * @return
     */
    public List<BtBankAccDTO> getBankAcc(String poolId) {
        List<BtBankAccDTO> listAcc = new ArrayList<BtBankAccDTO>();
        List<FexCorpPoolBankAccVO> poolAcc = this.listMainAccByPoolId(poolId, "", ForeignConstant.FEX_PARENT_SIGN_1);
        if(!EmptyUtils.isEmptyList(poolAcc)){
            for(FexCorpPoolBankAccVO vo : poolAcc){
                BtBankAccDTO bankAccInfo = btBankAccService.getBankAccByIdAndCurId(vo.getAccId(),vo.getCurId());
                if(!EmptyUtils.isEmpty(bankAccInfo)){
                    bankAccInfo.setCurName(btCurrencyService.getObjectById(vo.getCurId()).getCurName());
                    bankAccInfo.setCurEnglishCode(btCurrencyService.getObjectById(vo.getCurId()).getEnglishCode());
                    bankAccInfo.setIsOnlineStr(dictService.get("sf_yn", bankAccInfo.getIsOnline()));
                    listAcc.add(bankAccInfo);
                }
            }
        }
        return listAcc;
    }

    /**
     * 数据校验
     *
     * @param accVO
     * @param isParent true主账户 false成员账户
     * @return
     */
    public boolean dataCheck(FexCorpPoolBankAccVO accVO, Boolean isParent) {
        String englishCode = "";
        if(isParent){
            List<FexCorpPoolBankAccVO> voList = this.listBankAccByParentAccId("",accVO.getCurId(),"",accVO.getAccId());
            if(!EmptyUtils.isEmptyList(voList)){
                englishCode = btCurrencyService.getObjectById(accVO.getCurId()).getEnglishCode();
                if(!EmptyUtils.isEmpty(accVO.getId())){//说明是修改
                    if(!voList.get(0).getId().equals(accVO.getId())){
                        throw new ParameterException(ForeignMessageEnum.FEX_CORP_POOL_BANKACC_ISEXIST,accVO.getBankAcc(),englishCode);
                    }
                }else{
                    throw new ParameterException(ForeignMessageEnum.FEX_CORP_POOL_BANKACC_ISEXIST,accVO.getBankAcc(),englishCode);
                }
            }
        }else{
            if(StringUtils.isNotBlank(accVO.getAccIds()) && StringUtils.isNotBlank(accVO.getCurIds()) ){
                String[] accTemp = accVO.getAccIds().split(",");
                String[] curTemp = accVO.getCurIds().split(",");
                ArrayList<String> accList = new ArrayList<String>(Arrays.asList(accTemp)) ;
                ArrayList<String> curList = new ArrayList<String>(Arrays.asList(curTemp)) ;
                Set accSet = new HashSet();
                Set curSet = new HashSet();
                accSet.addAll(accList);
                curSet.addAll(curList);
                if(accList.size() != accSet.size() && curList.size() != curSet.size()){
                    throw new ParameterException(ForeignMessageEnum.FEX_CORP_POOL_BANKACC_REPEAT);
                }
                if(!EmptyUtils.isEmpty(accVO.getId())){//说明是修改
                    //根据资金池id查找下面主账号(之前维护过的)
                    List<FexCorpPoolBankAccVO> oldList = this.listBankAccByParentAccId(accVO.getId(),"","","");
                    for(int i = 0; i < accList.size(); i++){
                        List<FexCorpPoolBankAccVO> newList = this.listBankAccByParentAccId("",curList.get(i),"",accList.get(i));
                        //新修改账号信息与之前维护过的id做比较
                        if(!EmptyUtils.isEmptyList(newList)){
                            for(FexCorpPoolBankAccVO oldInfo : oldList){
                                //新修改的账号id和币种id与之前维护的账号id与币种id相同，比较id是否一致，如果不致，则不是修改，抛异常
                                if(newList.get(0).getAccId().equals(oldInfo.getAccId()) && newList.get(0).getCurId().equals(oldInfo.getCurId())){
                                    if(!newList.get(0).getId().equals(oldInfo.getId())){
                                        throw new ParameterException(ForeignMessageEnum.FEX_CORP_POOL_BANKACC_ISEXIST,accVO.getBankAcc(),englishCode);
                                    }
                                }
                            }
                        }
                    }
                }else{
                    for(int i = 0; i < accList.size(); i++){
                        List<FexCorpPoolBankAccVO> voList = this.listBankAccByParentAccId("",curList.get(i),"",accList.get(i));
                        if(!EmptyUtils.isEmptyList(voList)){
                            BtBankAccDTO dto = btBankAccService.getBankAccById(accList.get(i));
                            String bankAcc = "";
                            if(!EmptyUtils.isEmpty(dto)){
                                bankAcc = dto.getBankAcc();
                            }
                            englishCode = btCurrencyService.getObjectById(curList.get(i)).getEnglishCode();
                            throw new ParameterException(ForeignMessageEnum.FEX_CORP_POOL_BANKACC_ISEXIST,bankAcc,englishCode);
                        }
                    }
                }
            }
        }
        return true;
    }

    /**
     * 处理VO中的{@link Corp}注解并批量注入单位属性。
     *
     * @param list 资金池账户数据集合
     */
    private void injectCorpValue(List<FexCorpPoolBankAccVO> list) {
        if (corpService == null) {
            return;
        }
        List<Field> fields = getAnnotationFields(FexCorpPoolBankAccVO.class, Corp.class);
        Set<String> corpIds = collectFieldValues(list, fields);
        if (corpIds.isEmpty()) {
            return;
        }
        Map<String, CorpDTO> corpMap = corpService.getCorpMapByCorpId(new ArrayList<>(corpIds));
        if (EmptyUtils.isEmpty(corpMap)) {
            return;
        }
        for (FexCorpPoolBankAccVO vo : list) {
            for (Field field : fields) {
                CorpDTO corp = corpMap.get(getStringFieldValue(vo, field));
                if (corp != null) {
                    Corp annotation = field.getAnnotation(Corp.class);
                    setFieldValue(vo, annotation.inject(), getSourceFieldValue(corp, annotation.field(), "name"));
                }
            }
        }
    }

    /**
     * 处理VO中的{@link Currency}注解并批量注入币种属性。
     *
     * @param list 资金池账户数据集合
     */
    private void injectCurrencyValue(List<FexCorpPoolBankAccVO> list) {
        if (btCurrencyService == null) {
            return;
        }
        List<Field> fields = getAnnotationFields(FexCorpPoolBankAccVO.class, Currency.class);
        Set<String> curIds = collectFieldValues(list, fields);
        if (curIds.isEmpty()) {
            return;
        }
        Map<String, BtCurrencyDTO> currencyMap = btCurrencyService.getCurrencyByIds(new ArrayList<>(curIds));
        if (EmptyUtils.isEmpty(currencyMap)) {
            return;
        }
        for (FexCorpPoolBankAccVO vo : list) {
            for (Field field : fields) {
                BtCurrencyDTO currency = currencyMap.get(getStringFieldValue(vo, field));
                if (currency != null) {
                    Currency annotation = field.getAnnotation(Currency.class);
                    setFieldValue(vo, annotation.inject(), getSourceFieldValue(currency, annotation.field(), "curName"));
                }
            }
        }
    }

    /**
     * 获取类及其父类中带指定注解的字段。
     *
     * @param type 数据类型
     * @param annotationType 注解类型
     * @return 带指定注解的字段集合
     */
    private List<Field> getAnnotationFields(Class<?> type, Class<? extends Annotation> annotationType) {
        List<Field> result = new ArrayList<>();
        Class<?> current = type;
        while (current != null && current != Object.class) {
            for (Field field : current.getDeclaredFields()) {
                if (field.getAnnotation(annotationType) != null) {
                    field.setAccessible(true);
                    result.add(field);
                }
            }
            current = current.getSuperclass();
        }
        return result;
    }

    /**
     * 收集指定注解字段中非空的基础数据ID。
     *
     * @param list 数据集合
     * @param fields 注解字段集合
     * @return 基础数据ID集合
     */
    private Set<String> collectFieldValues(List<FexCorpPoolBankAccVO> list, List<Field> fields) {
        Set<String> values = new HashSet<>();
        for (FexCorpPoolBankAccVO vo : list) {
            for (Field field : fields) {
                String value = getStringFieldValue(vo, field);
                if (StringUtils.isNotBlank(value)) {
                    values.add(value);
                }
            }
        }
        return values;
    }

    /**
     * 读取对象中指定字段的字符串值。
     *
     * @param target 数据对象
     * @param field 字段
     * @return 字段字符串值，读取失败时返回null
     */
    private String getStringFieldValue(Object target, Field field) {
        try {
            Object value = field.get(target);
            return value == null ? null : value.toString();
        } catch (IllegalAccessException e) {
            logger.warn("读取注入源字段失败: 类={}, 字段={}", target.getClass().getSimpleName(), field.getName(), e);
            return null;
        }
    }

    /**
     * 根据注解配置读取基础数据对象中的展示属性。
     *
     * @param source 基础数据对象
     * @param configuredField 注解配置的属性名
     * @param defaultField 未配置时使用的默认属性名
     * @return 展示属性值
     */
    private Object getSourceFieldValue(Object source, String configuredField, String defaultField) {
        String fieldName = StringUtils.isBlank(configuredField) ? defaultField : configuredField;
        Field field = findField(source.getClass(), fieldName);
        if (field == null) {
            logger.warn("注入数据源字段不存在: 类={}, 字段={}", source.getClass().getSimpleName(), fieldName);
            return null;
        }
        try {
            field.setAccessible(true);
            return field.get(source);
        } catch (IllegalAccessException e) {
            logger.warn("读取注入数据源字段失败: 类={}, 字段={}", source.getClass().getSimpleName(), fieldName, e);
            return null;
        }
    }

    /**
     * 将基础数据展示值写入注解指定的目标字段。
     *
     * @param target 目标对象
     * @param fieldName 目标字段名
     * @param value 展示值
     */
    private void setFieldValue(Object target, String fieldName, Object value) {
        if (StringUtils.isBlank(fieldName) || value == null) {
            return;
        }
        Field field = findField(target.getClass(), fieldName);
        if (field == null) {
            logger.warn("注入目标字段不存在: 类={}, 字段={}", target.getClass().getSimpleName(), fieldName);
            return;
        }
        try {
            field.setAccessible(true);
            field.set(target, value);
        } catch (IllegalAccessException e) {
            logger.warn("写入注入目标字段失败: 类={}, 字段={}", target.getClass().getSimpleName(), fieldName, e);
        }
    }

    /**
     * 在类及其父类中查找指定名称的字段。
     *
     * @param type 数据类型
     * @param fieldName 字段名
     * @return 对应字段，不存在时返回null
     */
    private Field findField(Class<?> type, String fieldName) {
        Class<?> current = type;
        while (current != null && current != Object.class) {
            try {
                return current.getDeclaredField(fieldName);
            } catch (NoSuchFieldException e) {
                current = current.getSuperclass();
            }
        }
        return null;
    }
}
