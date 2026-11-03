package com.bytter.biz.creditRating.service;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import com.bytter.biz.creditRating.api.constant.CreditRatingConstant;
import com.bytter.biz.creditRating.dao.CrScoringIndicatorDao;
import com.bytter.biz.creditRating.dao.CrmScoringIndicatorSolidificatiDao;
import com.bytter.biz.creditRating.entity.CrQuaMethodForScoringInd;
import com.bytter.biz.creditRating.entity.CrScoringIndicator;
import com.bytter.biz.creditRating.enums.CreditRatingEnum;
import com.bytter.biz.creditRating.vo.CrQuaMethodForScoringIndVo;
import com.bytter.biz.creditRating.vo.CrScoringIndicatorVo;
import com.bytter.biz.creditRating.vo.CrScoringStandardVo;
import com.bytter.biz.creditRating.vo.CrmScoringIndicatorSolidificatiVO;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.core.common.exception.ParameterException;
import com.bytter.core.utils.*;

import com.bytter.poi.excel.entity.result.ExcelVerifyHanlderResult;
import com.bytter.poi.handler.inter.IExcelVerifyHandler;
import com.bytter.system.api.IDictService;
import com.bytter.system.api.IMultiLangService;
import com.bytter.system.api.dto.DictDTO;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@Service("crScoringIndicatorService")
@Transactional
public class CrScoringIndicatorService extends BaseService<CrScoringIndicator, String> implements IExcelVerifyHandler<CrScoringIndicatorVo> {

    @Autowired
    private CrScoringIndicatorDao crScoringIndicatorDao;

    @Autowired
    private CrQuaMethodForScoringIndService crQuaMethodForScoringIndService;

    @DubboReference(check = false)
    private IDictService dictService;

    @Resource
    private IMultiLangService multiLangServiceI;

    @Autowired
    private CrmScoringIndicatorSolidificatiDao crmScoringIndicatorSolidificatiDao;

    @Override
    public BaseDao<CrScoringIndicator, String> getEntityDao() {
        return crScoringIndicatorDao;
    }

    public List<CrScoringIndicatorVo> getByIds(List<String> ids) {
        List<CrScoringIndicatorVo> list = crScoringIndicatorDao.getByIds(ids);
        if (CollectionUtils.isEmpty(list)) {
            return new ArrayList<>();
        }
        List<String> sidList = new ArrayList<>();
        for (CrScoringIndicatorVo crScoringIndicatorVo : list) {
            sidList.add(crScoringIndicatorVo.getId());
        }
        List<CrQuaMethodForScoringIndVo> crQuaMethodForScoringIndVoList = crQuaMethodForScoringIndService.batchSelectBySids(sidList);
        //根据评分指标id分组
        Map<String, List<CrQuaMethodForScoringIndVo>> quaListGroupBySid = crQuaMethodForScoringIndVoList.stream().collect(Collectors.groupingBy(CrQuaMethodForScoringIndVo::getScoringIndicatorId));
        //遍历指标表数据
        for (CrScoringIndicatorVo crScoringIndicatorVo : list) {

            if (quaListGroupBySid.containsKey(crScoringIndicatorVo.getId())) {
                //获取每个指标对应的定量List
                List<CrQuaMethodForScoringIndVo> crQuaMethodForScoringIndVoList1 = quaListGroupBySid.get(crScoringIndicatorVo.getId());                //存放评分细则
                List<CrScoringStandardVo> crScoringStandardVos = new ArrayList<>();
                for (CrQuaMethodForScoringIndVo crQuaMethodForScoringIndVo : crQuaMethodForScoringIndVoList1) {
                    //将定量数据设置到Vo中
                    setQuaMethodForScoringIndProperties(crQuaMethodForScoringIndVo, crScoringIndicatorVo);
                }
                for (CrQuaMethodForScoringIndVo crQuaMethodForScoringIndVo : crQuaMethodForScoringIndVoList1) {
                    //设置评分细则
                    if (crQuaMethodForScoringIndVo.getValueNameCode() != null && crScoringIndicatorVo.getScoringMethod() != null) {
                        if (crQuaMethodForScoringIndVo.getValueNameCode().equals(String.valueOf(crScoringIndicatorVo.getScoringMethod().intValue()))) {
                            crScoringStandardVos.add(setScoringStandardProperties(crQuaMethodForScoringIndVo));
                        }
                    }
                }
                //将评分细则设置到Vo中
                crScoringIndicatorVo.setCrScoringStandardVoList(crScoringStandardVos);
            }
        }
        return list;
    }

    //根据迭代得到所有父级id
    public void makeIdsSet(CrScoringIndicatorVo crScoringIndicatorVo,Set<String> sidSet){
        if (crScoringIndicatorVo != null) {
            sidSet.add(crScoringIndicatorVo.getId());
            if(EmptyUtils.isNotEmpty(crScoringIndicatorVo.getParentId())){
                makeIdsSet(this.getById(crScoringIndicatorVo.getParentId()),sidSet);
            }
        }
    }

    public List<CrScoringIndicatorVo> listEffectTree(CrScoringIndicatorVo vo) {
        List<CrScoringIndicatorVo> voList = crScoringIndicatorDao.getEffectData(vo);
        //得到所有数据以及这些数据的所有父级数据
        Set<String> sidSet = new HashSet<>();
        for (CrScoringIndicatorVo crScoringIndicatorVo : voList) {
            makeIdsSet(crScoringIndicatorVo,sidSet);
        }
        List<String> sidList = new ArrayList<>(sidSet);
        if (sidList.size() <= 0) {
            return new ArrayList<>();
        }
        List<CrScoringIndicatorVo> list = crScoringIndicatorDao.getByIds(sidList);
        //查询指标类别id对应的定量方法数据
        List<CrQuaMethodForScoringIndVo> crQuaMethodForScoringIndVoList = crQuaMethodForScoringIndService.batchSelectBySids(sidList);
        //根据指标类别id分组
        Map<String, List<CrQuaMethodForScoringIndVo>> quaListGroupBySid = crQuaMethodForScoringIndVoList.stream().collect(Collectors.groupingBy(CrQuaMethodForScoringIndVo::getScoringIndicatorId));
        //将定量方法数据封装到vo中
        for (CrScoringIndicatorVo crScoringIndicatorVo : list) {
            if (quaListGroupBySid.containsKey(crScoringIndicatorVo.getId())) {
                List<CrScoringStandardVo> crScoringStandardVos = new ArrayList<>();
                List<CrQuaMethodForScoringIndVo> crQuaMethodForScoringIndVoList1 = quaListGroupBySid.get(crScoringIndicatorVo.getId());
                for (CrQuaMethodForScoringIndVo crQuaMethodForScoringIndVo : crQuaMethodForScoringIndVoList1) {
                    //将定量方法数据封装到vo中
                    setQuaMethodForScoringIndProperties(crQuaMethodForScoringIndVo, crScoringIndicatorVo);
                    if (crQuaMethodForScoringIndVo.getValueNameCode().equals(crScoringIndicatorVo.getScoringMethod())) {
                        crScoringStandardVos.add(setScoringStandardProperties(crQuaMethodForScoringIndVo));
                    }
                }
                //设置评分标准
                crScoringIndicatorVo.setCrScoringStandardVoList(crScoringStandardVos);
            }
        }
        if (!CollectionUtils.isEmpty(list)) {
            //对list做筛选,筛选末级指标的评分性质不能为空的数据
            list = list.stream().filter(item -> {
                        if (item.getIsLast() == CreditRatingConstant.CREDIT_SCORING_YES) {
                            return !EmptyUtils.isEmpty(item.getIndexProperty());
                        }
                        return true;
                    })
                    .collect(Collectors.toList());
        }

        return getTree(list);
    }

    /**
     * 单位组件，根据类型组装单位树
     *
     * @return
     */
    public List<CrScoringIndicatorVo> listAllTree(CrScoringIndicatorVo vo) {
        List<String> sidList = new ArrayList<>();
        List<CrScoringIndicatorVo> list = crScoringIndicatorDao.getList(vo);
        for (CrScoringIndicatorVo crScoringIndicatorVo : list) {
            sidList.add(crScoringIndicatorVo.getId());
        }
        //查询指标类别id对应的定量方法数据
        List<CrQuaMethodForScoringIndVo> crQuaMethodForScoringIndVoList = crQuaMethodForScoringIndService.batchSelectBySids(sidList);
        //根据指标类别id分组
        Map<String, List<CrQuaMethodForScoringIndVo>> quaListGroupBySid = crQuaMethodForScoringIndVoList.stream().collect(Collectors.groupingBy(CrQuaMethodForScoringIndVo::getScoringIndicatorId));
        //将定量方法数据封装到vo中
        for (CrScoringIndicatorVo crScoringIndicatorVo : list) {
            if (quaListGroupBySid.containsKey(crScoringIndicatorVo.getId())) {
                List<CrScoringStandardVo> crScoringStandardVos = new ArrayList<>();
                List<CrQuaMethodForScoringIndVo> crQuaMethodForScoringIndVoList1 = quaListGroupBySid.get(crScoringIndicatorVo.getId());
                for (CrQuaMethodForScoringIndVo crQuaMethodForScoringIndVo : crQuaMethodForScoringIndVoList1) {
                    //将定量方法数据封装到vo中
                    setQuaMethodForScoringIndProperties(crQuaMethodForScoringIndVo, crScoringIndicatorVo);
                    if (crQuaMethodForScoringIndVo.getValueNameCode().equals(crScoringIndicatorVo.getScoringMethod())) {
                        crScoringStandardVos.add(setScoringStandardProperties(crQuaMethodForScoringIndVo));
                    }
                }
                //设置评分标准
                crScoringIndicatorVo.setCrScoringStandardVoList(crScoringStandardVos);
            }
        }
        return getTree(list);
    }

    /**
     * 将定量方法的数据转换称vo中对应的字段
     *
     * @param crQuaMethodForScoringIndVo
     * @param crScoringIndicatorVo
     */
    private void setQuaMethodForScoringIndProperties(CrQuaMethodForScoringIndVo crQuaMethodForScoringIndVo, CrScoringIndicatorVo crScoringIndicatorVo) {
        switch (crQuaMethodForScoringIndVo.getValueNameCode()) {
            case CreditRatingConstant.INDICATOR_DIRECTION_CODE://指标方向
                crScoringIndicatorVo.setIndicatorDirectionValue(crQuaMethodForScoringIndVo.getValue());
                break;
            case CreditRatingConstant.MAX_POSSIBLE_VALUE_CODE://最大可能值
                crScoringIndicatorVo.setMaxPossibleValue(crQuaMethodForScoringIndVo.getValue());
                break;
            case CreditRatingConstant.MIN_POSSIBLE_VALUE_CODE://最小可能值
                crScoringIndicatorVo.setMinPossibleValue(crQuaMethodForScoringIndVo.getValue());
                break;
            case CreditRatingConstant.IS_AUTO_SCORING_CODE://是否自动评分
                crScoringIndicatorVo.setIsAutoScoring(crQuaMethodForScoringIndVo.getValue());
                break;
            case CreditRatingConstant.SCORING_METHOD_CODE://评分方法
                crScoringIndicatorVo.setScoringMethod(crQuaMethodForScoringIndVo.getValue());
                break;
            case CreditRatingConstant.IS_ADD_OR_SUB_GRADE_CODE://是否额外加/减分
                crScoringIndicatorVo.setIsAddOrSubGrade(crQuaMethodForScoringIndVo.getValue());
                break;
            case CreditRatingConstant.INDEX_PROPERTY_CODE://指标性质
                crScoringIndicatorVo.setIndexProperty(crQuaMethodForScoringIndVo.getValue());
                break;
        }
    }

    /**
     * 功能介绍 : 获取树形结构，通过父指标id绑定层级<br>
     *
     * @param list
     * @param list   <b>日期：</b>2024-08-20<br>
     * @version 1.0
     * @author zhoup
     */
    public List<CrScoringIndicatorVo> getTree(List<CrScoringIndicatorVo> list) {
        List<CrScoringIndicatorVo> crScoringIndicatorVos = listTree(list);
        Collections.sort(crScoringIndicatorVos, Comparator.comparing(CrScoringIndicatorVo::getScoringIndicatorCode));
        List<CrScoringIndicatorVo> crScoringIndicatorVos1 = sortedList(crScoringIndicatorVos);
        return crScoringIndicatorVos1;
    }

    /**
     * 对list排序
     * @param list
     * @return
     */
    public List<CrScoringIndicatorVo> sortedList(List<CrScoringIndicatorVo> list) {
        List<CrScoringIndicatorVo> crScoringIndicatorVos = new ArrayList<>();
        for (CrScoringIndicatorVo crScoringIndicatorVo : list) {
            sortedTree(crScoringIndicatorVo, crScoringIndicatorVos);
        }
        return crScoringIndicatorVos;
    }

    /**
     * 对子类递归排序
     * @param vo
     * @param list
     */
    public void sortedTree(CrScoringIndicatorVo vo, List<CrScoringIndicatorVo> list) {
        list.add(vo);
        if (vo.getChildrens() != null && vo.getChildrens().size() > 0) {
            Collections.sort(vo.getChildrens(), Comparator.comparing(CrScoringIndicatorVo::getScoringIndicatorCode));
            for (CrScoringIndicatorVo crScoringIndicatorVo : vo.getChildrens()) {
                sortedTree(crScoringIndicatorVo, list);
            }
        }


    }

    /**
     * 功能介绍 : 判断集合list元素id<br>
     *
     * @param list
     * @param id
     * @return 存在 true <br>不存在 false
     * <b>日期：</b>2024-08-20<br>
     * @version 1.0
     * @author zhoup
     */
    public boolean checkParentNode(List<CrScoringIndicatorVo> list, String id) {
        for (CrScoringIndicatorVo crScoringIndicatorVo : list) {
            if (crScoringIndicatorVo.getId().equals(id)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 根据评分指标id查询评分指标
     *
     * @param id
     * @return
     * @author zhoup
     * @since 2024/09/09
     */
    public CrScoringIndicatorVo getById(String id) {
        CrScoringIndicatorVo vo = crScoringIndicatorDao.getById(id);
        List<CrScoringStandardVo> crScoringStandardVos = new ArrayList<>();
        List<CrQuaMethodForScoringIndVo> crQuaMethodForScoringIndVoList = crQuaMethodForScoringIndService.getBySid(vo.getId());
        for (CrQuaMethodForScoringIndVo crQuaMethodForScoringIndVo : crQuaMethodForScoringIndVoList) {
            setQuaMethodForScoringIndProperties(crQuaMethodForScoringIndVo, vo);
        }
        for (CrQuaMethodForScoringIndVo crQuaMethodForScoringIndVo : crQuaMethodForScoringIndVoList) {
            //设置评分细则
            if (crQuaMethodForScoringIndVo.getValueNameCode() != null && vo.getScoringMethod() != null) {
                if (crQuaMethodForScoringIndVo.getValueNameCode().equals(String.valueOf(vo.getScoringMethod().intValue()))) {
                    crScoringStandardVos.add(setScoringStandardProperties(crQuaMethodForScoringIndVo));
                }
            }
        }
        vo.setCrScoringStandardVoList(crScoringStandardVos);
        return vo;
    }

    /**
     * 将定量方法的Vo的数据封装到评分准则vo中
     *
     * @param crQuaMethodForScoringIndVo
     * @return
     * @author zhoup
     * @since 2024/09/09
     */
    private CrScoringStandardVo setScoringStandardProperties(CrQuaMethodForScoringIndVo crQuaMethodForScoringIndVo) {
        CrScoringStandardVo crScoringStandardVo = new CrScoringStandardVo();
        crScoringStandardVo.setCode(Integer.parseInt(crQuaMethodForScoringIndVo.getValueNameCode()));
        crScoringStandardVo.setMethodName(crQuaMethodForScoringIndVo.getValueName());
        //归一法没有tOtherValueDirection
        if (crQuaMethodForScoringIndVo.getValueDirection() != null) {
            crScoringStandardVo.setValueDirection(crQuaMethodForScoringIndVo.getValueDirection().intValue());
        }
        if (ObjectUtil.isNotNull(crQuaMethodForScoringIndVo.getOtherValueDirection())) {
            crScoringStandardVo.setOtherValueDirection(crQuaMethodForScoringIndVo.getOtherValueDirection().intValue());
        }
        crScoringStandardVo.setOthervalue(crQuaMethodForScoringIndVo.getOtherValue());
        crScoringStandardVo.setValue(crQuaMethodForScoringIndVo.getValue());
        crScoringStandardVo.setGrade(crQuaMethodForScoringIndVo.getGrade());
        return crScoringStandardVo;
    }

    /**
     * 将评分指标Vo中的评分细则的数据封装到定量实体中
     *
     * @param crScoringStandardVoList,crScoringIndicatorVo
     * @return
     * @author zhoup
     * @since 2024/09/09
     */
    private List<CrQuaMethodForScoringInd> setScoringStandardToEntityProperties(List<CrScoringStandardVo> crScoringStandardVoList, CrScoringIndicatorVo crScoringIndicatorVo, String userId) {
        //这里数据处理前端保证了如果第一行是大于或者大于等于，那么评分条件为降序，如果第一行是小于或小于等于，评分条件为升序
        System.out.println( JsonUtils.toJSONString(crScoringStandardVoList));
        List<CrQuaMethodForScoringInd> crQuaMethodForScoringInds = new ArrayList<>();
        for (int i = 0; i < crScoringStandardVoList.size(); i++) {
            //设置另外一个范围的符号
            if (crScoringStandardVoList.get(i).getValueDirection() == CreditRatingConstant.CREDIT_SCORING_GT) {//如果符号为大于，另外一个就是小于等于
                crScoringStandardVoList.get(i).setOtherValueDirection(CreditRatingConstant.CREDIT_SCORING_LTE);
            } else if (crScoringStandardVoList.get(i).getValueDirection() == CreditRatingConstant.CREDIT_SCORING_GTE) {//如果符号为大于等于，另外一个就是小于
                crScoringStandardVoList.get(i).setOtherValueDirection(CreditRatingConstant.CREDIT_SCORING_LT);
            } else if (crScoringStandardVoList.get(i).getValueDirection() == CreditRatingConstant.CREDIT_SCORING_EQ) {//如果符号为等于，另外一个就是等于
                crScoringStandardVoList.get(i).setOtherValueDirection(CreditRatingConstant.CREDIT_SCORING_EQ);
            } else if (crScoringStandardVoList.get(i).getValueDirection() == CreditRatingConstant.CREDIT_SCORING_LT) {//如果符号为小于，另外一个就是大于等于
                crScoringStandardVoList.get(i).setOtherValueDirection(CreditRatingConstant.CREDIT_SCORING_GTE);
            } else if (crScoringStandardVoList.get(i).getValueDirection() == CreditRatingConstant.CREDIT_SCORING_LTE) {//如果符号为小于等于，另外一个就是大于
                crScoringStandardVoList.get(i).setOtherValueDirection(CreditRatingConstant.CREDIT_SCORING_GT);
            }
            //设置另外一个范围的值
            if (crScoringStandardVoList.get(i).getValueDirection() == CreditRatingConstant.CREDIT_SCORING_EQ) {//如果为等于，另另外一个值=这个值
                crScoringStandardVoList.get(i).setOthervalue(crScoringStandardVoList.get(i).getValue());
            } else if (crScoringStandardVoList.get(i).getValueDirection() == CreditRatingConstant.CREDIT_SCORING_GT
                    || crScoringStandardVoList.get(i).getValueDirection() == CreditRatingConstant.CREDIT_SCORING_GTE) {//如果为大于，另另外一个值=上一行的值
                //如果是上一行没有值，那么他的最大值为null
                if (i == 0) {
                    crScoringStandardVoList.get(i).setOthervalue(null);
                } else {
                    //判断跟上一行是否反号
                    if (crScoringStandardVoList.get(i - 1).getValueDirection() == CreditRatingConstant.CREDIT_SCORING_LT
                            || crScoringStandardVoList.get(i - 1).getValueDirection() == CreditRatingConstant.CREDIT_SCORING_LTE
                                || crScoringStandardVoList.get(i - 1).getValueDirection() == CreditRatingConstant.CREDIT_SCORING_EQ) {
                        crScoringStandardVoList.get(i).setOthervalue(null);
                    } else {
                        crScoringStandardVoList.get(i).setOthervalue(crScoringStandardVoList.get(i - 1).getValue());
                    }
                }
            } else if (crScoringStandardVoList.get(i).getValueDirection() == CreditRatingConstant.CREDIT_SCORING_LT
                    || crScoringStandardVoList.get(i).getValueDirection() == CreditRatingConstant.CREDIT_SCORING_LTE) {//如果为小于，另另外一个值=下一行的值
                //如果下一行没有值，那么他的最小值为null
                if (i == 0) {
                    crScoringStandardVoList.get(i).setOthervalue(null);
                } else {
                    if (crScoringStandardVoList.get(i - 1).getValueDirection() == CreditRatingConstant.CREDIT_SCORING_GT
                            || crScoringStandardVoList.get(i - 1).getValueDirection() == CreditRatingConstant.CREDIT_SCORING_GTE
                                 || crScoringStandardVoList.get(i - 1).getValueDirection() == CreditRatingConstant.CREDIT_SCORING_EQ) {//判断跟上一行是否反号
                        crScoringStandardVoList.get(i).setOthervalue(null);
                    } else {
                        crScoringStandardVoList.get(i).setOthervalue(crScoringStandardVoList.get(i - 1).getValue());
                    }
                }
            }

            CrQuaMethodForScoringInd crQuaMethodForScoringInd = new CrQuaMethodForScoringInd();
            BeanUtils.copyNotNullProperties(crScoringIndicatorVo, crQuaMethodForScoringInd);//复制主表属性
            crQuaMethodForScoringInd.setId(null);//防止把主表id赋值给定量表对象
            crQuaMethodForScoringInd.setScoringIndicatorId(crScoringIndicatorVo.getId());
            crQuaMethodForScoringInd.setValueName(dictService.get(CreditRatingConstant.SCORING_METHOD_GROUPCODE, crScoringIndicatorVo.getScoringMethod()));//值名称取方法名称
            crQuaMethodForScoringInd.setValueNameCode(crScoringIndicatorVo.getScoringMethod().toString());//值名称代码取方法名称中在字典中对应的code
            crQuaMethodForScoringInd.setValue(crScoringStandardVoList.get(i).getValue());
            crQuaMethodForScoringInd.setValueDirection(crScoringStandardVoList.get(i).getValueDirection());
            crQuaMethodForScoringInd.setOtherValue(crScoringStandardVoList.get(i).getOthervalue());
            crQuaMethodForScoringInd.setOtherValueDirection(crScoringStandardVoList.get(i).getOtherValueDirection());
            crQuaMethodForScoringInd.setGrade(crScoringStandardVoList.get(i).getGrade());
            crQuaMethodForScoringInd.setVersionNumber(CreditRatingConstant.INIT_VERSION_NUMBER);
            crQuaMethodForScoringInd.setCreator(userId);
            crQuaMethodForScoringInd.setCreateDate(DateUtils.getDate());
            crQuaMethodForScoringInds.add(crQuaMethodForScoringInd);
        }
        System.out.println( JsonUtils.toJSONString(crQuaMethodForScoringInds));
        return crQuaMethodForScoringInds;
    }

    /**
     * 查询所有有校的评分指标
     *
     * @return
     * @author zhoup
     * @since 2024/09/09
     */
    public List<CrScoringIndicatorVo> getEffectData(CrScoringIndicatorVo vo) {
        List<String> sidList = new ArrayList<>();
        List<CrScoringIndicatorVo> effectData = crScoringIndicatorDao.getEffectData(vo);
        for (CrScoringIndicatorVo crScoringIndicatorVo : effectData) {
            sidList.add(crScoringIndicatorVo.getId());
        }
        List<CrQuaMethodForScoringIndVo> crQuaMethodForScoringIndVoList = crQuaMethodForScoringIndService.batchSelectBySids(sidList);
        //根据评分指标id分组
        Map<String, List<CrQuaMethodForScoringIndVo>> quaListGroupBySid = crQuaMethodForScoringIndVoList.stream().collect(Collectors.groupingBy(CrQuaMethodForScoringIndVo::getScoringIndicatorId));
        for (CrScoringIndicatorVo crScoringIndicatorVo : effectData) {
            if (quaListGroupBySid.containsKey(crScoringIndicatorVo.getId())) {
                List<CrQuaMethodForScoringIndVo> crQuaMethodForScoringIndVoList1 = quaListGroupBySid.get(crScoringIndicatorVo.getId());
                for (CrQuaMethodForScoringIndVo crQuaMethodForScoringIndVo : crQuaMethodForScoringIndVoList1) {
                    setQuaMethodForScoringIndProperties(crQuaMethodForScoringIndVo, crScoringIndicatorVo);
                }
            }
        }
        return effectData;
    }

    /**
     * 保存评分指标
     *
     * @param crScoringIndicatorVo
     * @author zhoup
     * @since 2024/09/09
     */
    public void save(CrScoringIndicatorVo crScoringIndicatorVo, String userId) {
        CrScoringIndicator crScoringIndicator = new CrScoringIndicator();
        BeanUtils.copyNotNullProperties(crScoringIndicatorVo, crScoringIndicator);
        crScoringIndicator.setScoringIndicatorCode(getOrgCode());
        crScoringIndicator.setDeleteFlag(CreditRatingConstant.INIT_DELETE_FLAG);
        crScoringIndicator.setVersionNumber(CreditRatingConstant.INIT_VERSION_NUMBER);
        crScoringIndicator.setIsLast(CreditRatingConstant.CREDIT_SCORING_YES);
        crScoringIndicator.setCreator(userId);
        crScoringIndicator.setCreateDate(DateUtils.getDate());
        checkData(crScoringIndicator);
        //如果parentId不为空，那么需要修改该父指标是否末级指标状态
        if (EmptyUtils.isNotEmpty(crScoringIndicatorVo.getParentId())) {
            CrScoringIndicatorVo parent = crScoringIndicatorDao.getById(crScoringIndicatorVo.getParentId());
            parent.setIsLast(CreditRatingConstant.CREDIT_SCORING_NO);//是否末级指标设置成否
            //清空数据源,父级指标一旦有了子级，就不在参与评分，所以为了不影响后续评分，在此处判断并且清除
            parent.setDataSourceId(null);
            parent.setDataSourceCode(null);
            parent.setDataSourceName(null);
            parent.setDataSourceOrigin(null);
            //更新父指标主信息
            CrScoringIndicator parentEntity = new CrScoringIndicator();
            BeanUtils.copyNotNullProperties(parent, parentEntity);
            crScoringIndicatorDao.update(parentEntity);

            //清空父级的定量数据,父级指标一旦有了子级，就不在参与评分，所以为了不影响后续评分，在此处判断并且清除
            //判断是否被清空
            List<String> ids = new ArrayList<>();
            List<CrQuaMethodForScoringIndVo> crQuaMethodForScoringIndVoList = crQuaMethodForScoringIndService.getBySid(parent.getId());
            crQuaMethodForScoringIndVoList.forEach(item -> ids.add(item.getId()));
            //删除原来的评分细则
            if (EmptyUtils.isNotEmpty(ids)) {
                crQuaMethodForScoringIndService.delete(ids);
            }
            if (parent.getLevel() != null) {
                crScoringIndicator.setLevel_(parent.getLevel() + 1);
            }
        } else {
            //如果parentId为空，说明该指标为顶级指标
            crScoringIndicator.setLevel_(1);
        }
        crScoringIndicatorDao.save(crScoringIndicator);
    }

    /**
     * 将vo中的数据转换成定量方法实体
     *
     * @param crScoringIndicatorVo
     * @param crScoringIndicator
     * @param userId
     * @param valueName
     * @param ValueNameCode
     * @param value
     * @return
     * @author zhoup
     * @since 2024/09/09
     */
    private CrQuaMethodForScoringInd setProperties(CrScoringIndicatorVo crScoringIndicatorVo, CrScoringIndicator crScoringIndicator, String userId, String valueName, String ValueNameCode, BigDecimal value) {
        CrQuaMethodForScoringInd crQuaMethodForScoringInd = new CrQuaMethodForScoringInd();
        BeanUtils.copyNotNullProperties(crScoringIndicatorVo, crQuaMethodForScoringInd);
        crQuaMethodForScoringInd.setId(null);//防止把主表id赋值给定量表对象
        crQuaMethodForScoringInd.setScoringIndicatorId(crScoringIndicator.getId());
        crQuaMethodForScoringInd.setScoringIndicatorCode(crScoringIndicatorVo.getScoringIndicatorCode());
        crQuaMethodForScoringInd.setVersionNumber(CreditRatingConstant.INIT_VERSION_NUMBER);
        crQuaMethodForScoringInd.setCreator(userId);
        crQuaMethodForScoringInd.setCreateDate(DateUtils.getDate());
        crQuaMethodForScoringInd.setValueName(valueName);
        crQuaMethodForScoringInd.setValue(value);
        crQuaMethodForScoringInd.setValueNameCode(ValueNameCode);
        return crQuaMethodForScoringInd;
    }

    /**
     * 校验评分指标的数据合规性
     *
     * @param crScoringIndicator
     */
    private void checkData(CrScoringIndicator crScoringIndicator) {
        if (crScoringIndicator.getScoringIndicatorDescription() != null &&
                crScoringIndicator.getScoringIndicatorDescription().length() > 200) {
            throw new ParameterException(CreditRatingEnum.CREDIT_RATING_DESCRIPTION_TOO_LONG);
        }
        if (crScoringIndicator.getScoringRubric() != null &&
                crScoringIndicator.getScoringRubric().length() > 800) {
            throw new ParameterException(CreditRatingEnum.CREDIT_SCORING_RUBRIC_TOO_LONG);
        }
        if (crScoringIndicator.getId() == null) {
            CrScoringIndicatorVo vo = crScoringIndicatorDao.getByScoringIndCode(crScoringIndicator.getScoringIndicatorCode());
            if (ObjectUtil.isNotNull(vo)) {
                throw new RuntimeException(String.format("评分指标编号已存在", vo.getScoringIndicatorCode()));
            }
        }else{
            //判断指标是否引用
            List<CrmScoringIndicatorSolidificatiVO> indicatorListByScoringId = crmScoringIndicatorSolidificatiDao.getIndicatorListByScoringId(crScoringIndicator.getId());
            if (CollectionUtil.isNotEmpty(indicatorListByScoringId)) {
                throw new ParameterException(CreditRatingEnum.CREDIT_SCORING_INDICATOR_IS_USED);
            }
        }
    }

    /**
     * 更新评分指标
     *
     * @param crScoringIndicatorVo
     * @author zhoup
     * @since 2024/09/09
     */
    public void update(CrScoringIndicatorVo crScoringIndicatorVo, String userId) {
        CrScoringIndicatorVo crScoringIndicatorVoDB = crScoringIndicatorDao.getById(crScoringIndicatorVo.getId());
        CrScoringIndicator crScoringIndicator = new CrScoringIndicator();
        if (ObjectUtil.isNotNull(crScoringIndicatorVoDB)) {
            BeanUtils.copyNotNullProperties(crScoringIndicatorVo, crScoringIndicatorVoDB);
            BeanUtils.copyNotNullProperties(crScoringIndicatorVoDB, crScoringIndicator);
        }
        checkData(crScoringIndicator);
        crScoringIndicatorDao.update(crScoringIndicator);
        //判断是否是最后一层指标
        if (crScoringIndicatorVoDB.getIsLast() == CreditRatingConstant.CREDIT_SCORING_YES) {//代表为末级指标
            List<CrQuaMethodForScoringIndVo> crQuaMethodForScoringIndVoList = crQuaMethodForScoringIndService.getBySid(crScoringIndicatorVo.getId());
            if (CollectionUtils.isEmpty(crQuaMethodForScoringIndVoList)) {
                //设置定量值
                CrQuaMethodForScoringInd crQuaMethodForScoringInd1 = setProperties(crScoringIndicatorVo, crScoringIndicator, userId, CreditRatingConstant.INDICATOR_DIRECTION_NAME, CreditRatingConstant.INDICATOR_DIRECTION_CODE, crScoringIndicatorVo.getIndicatorDirectionValue());
                CrQuaMethodForScoringInd crQuaMethodForScoringInd2 = setProperties(crScoringIndicatorVo, crScoringIndicator, userId, CreditRatingConstant.MAX_POSSIBLE_VALUE_NAME, CreditRatingConstant.MAX_POSSIBLE_VALUE_CODE, crScoringIndicatorVo.getMaxPossibleValue());
                CrQuaMethodForScoringInd crQuaMethodForScoringInd3 = setProperties(crScoringIndicatorVo, crScoringIndicator, userId, CreditRatingConstant.MIN_POSSIBLE_VALUE_NAME, CreditRatingConstant.MIN_POSSIBLE_VALUE_CODE, crScoringIndicatorVo.getMinPossibleValue());
                CrQuaMethodForScoringInd crQuaMethodForScoringInd4 = setProperties(crScoringIndicatorVo, crScoringIndicator, userId, CreditRatingConstant.IS_AUTO_SCORING, CreditRatingConstant.IS_AUTO_SCORING_CODE, crScoringIndicatorVo.getIsAutoScoring());
                CrQuaMethodForScoringInd crQuaMethodForScoringInd5 = setProperties(crScoringIndicatorVo, crScoringIndicator, userId, CreditRatingConstant.SCORING_METHOD, CreditRatingConstant.SCORING_METHOD_CODE, crScoringIndicatorVo.getScoringMethod());
                CrQuaMethodForScoringInd crQuaMethodForScoringInd6 = setProperties(crScoringIndicatorVo, crScoringIndicator, userId, CreditRatingConstant.IS_ADD_OR_SUB_GRADE, CreditRatingConstant.IS_ADD_OR_SUB_GRADE_CODE, crScoringIndicatorVo.getIsAddOrSubGrade());
                CrQuaMethodForScoringInd crQuaMethodForScoringInd7 = setProperties(crScoringIndicatorVo, crScoringIndicator, userId, CreditRatingConstant.INDEX_PROPERTY_NAME, CreditRatingConstant.INDEX_PROPERTY_CODE, crScoringIndicatorVo.getIndexProperty());
                List<CrQuaMethodForScoringInd> crQuaMethodForScoringIndList = new ArrayList<>();
                crQuaMethodForScoringIndList.add(crQuaMethodForScoringInd1);
                crQuaMethodForScoringIndList.add(crQuaMethodForScoringInd2);
                crQuaMethodForScoringIndList.add(crQuaMethodForScoringInd3);
                crQuaMethodForScoringIndList.add(crQuaMethodForScoringInd4);
                crQuaMethodForScoringIndList.add(crQuaMethodForScoringInd5);
                crQuaMethodForScoringIndList.add(crQuaMethodForScoringInd6);
                crQuaMethodForScoringIndList.add(crQuaMethodForScoringInd7);
                //评分细则
                List<CrScoringStandardVo> crScoringStandardVoList = crScoringIndicatorVo.getCrScoringStandardVoList();
                if (!CollectionUtils.isEmpty(crScoringStandardVoList)) {
                    //设置评分细则
                    List<CrQuaMethodForScoringInd> crQuaMethodForScoringInds = setScoringStandardToEntityProperties(crScoringStandardVoList, crScoringIndicatorVo, userId);
                    //将评分细则的定量数据加入
                    crQuaMethodForScoringIndList.addAll(crQuaMethodForScoringInds);
                }
                //保存
                crQuaMethodForScoringIndService.batchSave(crQuaMethodForScoringIndList);
            } else {
                //将Vo的值赋值给实体
                List<CrQuaMethodForScoringInd> crQuaMethodForScoringInds = BeanUtils.copyListProperties(crQuaMethodForScoringIndVoList, CrQuaMethodForScoringInd.class);
                for (CrQuaMethodForScoringInd crQuaMethodForScoringInd : crQuaMethodForScoringInds) {
                    switch (crQuaMethodForScoringInd.getValueNameCode()) {
                        case CreditRatingConstant.INDICATOR_DIRECTION_CODE://指标方向
                            crQuaMethodForScoringInd.setValue(crScoringIndicatorVo.getIndicatorDirectionValue());
                            break;
                        case CreditRatingConstant.MAX_POSSIBLE_VALUE_CODE://最小可能值
                            crQuaMethodForScoringInd.setValue(crScoringIndicatorVo.getMaxPossibleValue());
                            break;
                        case CreditRatingConstant.MIN_POSSIBLE_VALUE_CODE://最大可能值
                            crQuaMethodForScoringInd.setValue(crScoringIndicatorVo.getMinPossibleValue());
                            break;
                        case CreditRatingConstant.IS_AUTO_SCORING_CODE://是否自动评分
                            crQuaMethodForScoringInd.setValue(crScoringIndicatorVo.getIsAutoScoring());
                            break;
                        case CreditRatingConstant.SCORING_METHOD_CODE://评分方法
                            crQuaMethodForScoringInd.setValue(crScoringIndicatorVo.getScoringMethod());
                            break;
                        case CreditRatingConstant.IS_ADD_OR_SUB_GRADE_CODE://是否额外加减分
                            crQuaMethodForScoringInd.setValue(crScoringIndicatorVo.getIsAddOrSubGrade());
                            break;
                        case CreditRatingConstant.INDEX_PROPERTY_CODE://是否额外加减分
                            crQuaMethodForScoringInd.setValue(crScoringIndicatorVo.getIndexProperty());
                            break;
                    }
                }
                crQuaMethodForScoringIndService.batchUpdate(crQuaMethodForScoringInds);
                //评分细则更新
                ArrayList<String> ids = new ArrayList<>();
                //过滤出要删的数据
                if (crScoringIndicatorVo.getScoringMethod() != null) {
                    List<CrQuaMethodForScoringIndVo> rubicList = crQuaMethodForScoringIndVoList.stream().filter(item -> item.getValueNameCode().equals(String.valueOf(crScoringIndicatorVo.getScoringMethod().intValue()))).collect(Collectors.toList());
                    rubicList.forEach(item -> ids.add(item.getId()));
                    //删除原来的评分细则
                    crQuaMethodForScoringIndService.delete(ids);
                    //重新添加评分细则
                    List<CrQuaMethodForScoringInd> crQuaMethodForScoringInds1 = setScoringStandardToEntityProperties(crScoringIndicatorVo.getCrScoringStandardVoList(), crScoringIndicatorVo, userId);
                    crQuaMethodForScoringIndService.batchSave(crQuaMethodForScoringInds1);
                }
            }
        } else {
            //如果是父指标，那么父指标为无效，则该指标下的所有子指标也失效
            if (crScoringIndicatorVo.getStatus() == CreditRatingConstant.NOT_EFFECT_STATUS) {
                //通过byId的parentId查询所有子类指标
                List<CrScoringIndicatorVo> crScoringIndicatorVoList = allChildren(crScoringIndicatorVo);
                if (!CollectionUtils.isEmpty(crScoringIndicatorVoList)) {
                    List<CrScoringIndicator> crScoringIndicators = BeanUtils.copyListProperties(crScoringIndicatorVoList, CrScoringIndicator.class);
                    for (CrScoringIndicator scoringIndicator : crScoringIndicators) {
                        scoringIndicator.setStatus(CreditRatingConstant.NOT_EFFECT_STATUS);
                    }
                    crScoringIndicatorDao.batchUpdate(crScoringIndicators);
                }
            }
        }
    }

    /**
     * 删除评分指标
     *
     * @param id
     * @author zhoup
     * @since 2024/09/09
     */
    public void delete(String id) {
        //判断指标是否引用
        List<CrmScoringIndicatorSolidificatiVO> indicatorListByScoringId = crmScoringIndicatorSolidificatiDao.getIndicatorListByScoringId(id);
        if (CollectionUtil.isNotEmpty(indicatorListByScoringId)) {
            throw new ParameterException(CreditRatingEnum.CREDIT_SCORING_INDICATOR_IS_USED);
        }
        CrScoringIndicatorVo crScoringIndicatorVo = crScoringIndicatorDao.getById(id);
        //查询出该指标下的所有子类指标
        List<CrScoringIndicatorVo> crScoringIndicatorVos = allChildren(crScoringIndicatorVo);
        crScoringIndicatorVos.add(crScoringIndicatorVo);//把删除的该指标也加进去，做批量更新
        List<CrScoringIndicator> crScoringIndicators = BeanUtils.copyListProperties(crScoringIndicatorVos, CrScoringIndicator.class);
        for (CrScoringIndicator crScoringIndicator : crScoringIndicators) {
            crScoringIndicator.setDeleteFlag(CreditRatingConstant.DELETE_STATUS);
            crScoringIndicator.setStatus(CreditRatingConstant.DELETE_STATUS);
        }
        crScoringIndicatorDao.batchUpdate(crScoringIndicators);
        //判断是否是子类别
        if (crScoringIndicatorVo.getParentId() != null) {
            //如果是子类别，判断父类别下是否还有其它子类别
            List<CrScoringIndicatorVo> byParentId = crScoringIndicatorDao.getByParentId(crScoringIndicatorVo.getParentId());
            if (CollectionUtils.isEmpty(byParentId)) {
                //如果父类别下没有其它子类别，则将父类别改成末级状态
                CrScoringIndicatorVo cvo = crScoringIndicatorDao.getById(crScoringIndicatorVo.getParentId());
                CrScoringIndicator crScoringIndicator = new CrScoringIndicator();
                BeanUtils.copyNotNullProperties(cvo, crScoringIndicator);
                crScoringIndicator.setIsLast(CreditRatingConstant.CREDIT_SCORING_YES);
                crScoringIndicatorDao.update(crScoringIndicator);
            }
        }
    }

    /**
     * 通过父id查询评分指标
     *
     * @param parentId
     * @return
     */
    public List<CrScoringIndicatorVo> getByParentId(String parentId) {
        return crScoringIndicatorDao.getByParentId(parentId);
    }

    /**
     * 复制节点以及节点下的所有子节点数据
     *
     * @param id
     * @author zhoup
     * @since 2024/09/09
     */
    public void copy(String id) {
        //定义存放评分指标表返回的数据集
        List<CrScoringIndicatorVo> crScoringIndicatorVos = new ArrayList<>();
        //定义存放评分指标的实体
        List<CrScoringIndicator> crScoringIndicators = new ArrayList<>();
        //定义存放评分指标数据集的id
        List<String> sidList = new ArrayList<>();
        //定义一个Map来绑定父子指标的关系
        HashMap<String, CrScoringIndicatorVo> bindParentAndChildMap = new HashMap<>();
        //获取当前节点以及当前节点下的所有子节点评分指标数据
        CrScoringIndicatorVo byId = getById(id);
        //所有子节点数据
        List<CrScoringIndicatorVo> allChildIndicator = getAllChildIndicator(id, crScoringIndicatorVos);
        allChildIndicator.add(byId);//把当前节点加入集合
        //定义maxLevel为多少层级
        int maxLevel = 0;
        for (CrScoringIndicatorVo crScoringIndicatorVo : allChildIndicator) {
            if (crScoringIndicatorVo.getLevel() > maxLevel) {
                maxLevel = crScoringIndicatorVo.getLevel();
            }
            sidList.add(crScoringIndicatorVo.getId());
        }

        //通过sids查出所有的定量数据
        List<CrQuaMethodForScoringIndVo> crQuaMethodForScoringIndVoList = crQuaMethodForScoringIndService.batchSelectBySids(sidList);
        //根据sid分组
        Map<String, List<CrQuaMethodForScoringIndVo>> groupBySid = crQuaMethodForScoringIndVoList.stream().collect(Collectors.groupingBy(CrQuaMethodForScoringIndVo::getScoringIndicatorId));
        //定义map，通过编号来区分评分指标表和定量表的关联关系
        Map<String, List<CrQuaMethodForScoringIndVo>> byScoringCode = new HashMap<>();
        //遍历allChildIndicator
        for (int i = 1; i <= maxLevel; i++) {
            for (CrScoringIndicatorVo crScoringIndicatorVo : allChildIndicator) {
                if (crScoringIndicatorVo.getLevel() == i) {
                    //指标实体设值
                    CrScoringIndicator crScoringIndicator = new CrScoringIndicator();
                    BeanUtils.copyNotNullProperties(crScoringIndicatorVo, crScoringIndicator);
                    crScoringIndicator.setId(null);//生成新的id
                    crScoringIndicator.setScoringIndicatorCode(getOrgCode());//设置编号
                    checkData(crScoringIndicator);
                    crScoringIndicatorDao.save(crScoringIndicator);
                    getSession().flush();
                    //更新子指标的parentId
                    if (crScoringIndicatorVo.getLevel() != maxLevel) {
                        for (CrScoringIndicatorVo childScoringIndicatorVo : allChildIndicator) {
                            if (crScoringIndicatorVo.getId().equals(childScoringIndicatorVo.getParentId())) {
                                childScoringIndicatorVo.setParentId(crScoringIndicator.getId());
                            }
                        }
                    }
                    crScoringIndicators.add(crScoringIndicator);
                    //如果将新生成的评分指标数据和定量数据关联
                    if (groupBySid.containsKey(crScoringIndicatorVo.getId())) {
                        byScoringCode.put(crScoringIndicator.getScoringIndicatorCode(), groupBySid.get(crScoringIndicatorVo.getId()));
                    }
                }
            }
        }
        getSession().flush();

        //将新生成的评分指标id插入定量数据中
        for (CrScoringIndicator crScoringIndicator : crScoringIndicators) {
            if (byScoringCode.containsKey(crScoringIndicator.getScoringIndicatorCode())) {
                for (CrQuaMethodForScoringIndVo crQuaMethodForScoringIndVo : byScoringCode.get(crScoringIndicator.getScoringIndicatorCode())) {
                    crQuaMethodForScoringIndVo.setScoringIndicatorId(crScoringIndicator.getId());
                    crQuaMethodForScoringIndVo.setScoringIndicatorCode(crScoringIndicator.getScoringIndicatorCode());
                }
            }
        }
        List<CrQuaMethodForScoringInd> crQuaMethodForScoringInds = new ArrayList<>();
        //遍历crQuaMethodForScoringIndVoList
        for (CrQuaMethodForScoringIndVo crQuaMethodForScoringIndVo : crQuaMethodForScoringIndVoList) {
            CrQuaMethodForScoringInd crQuaMethodForScoringInd = new CrQuaMethodForScoringInd();
            BeanUtils.copyNotNullProperties(crQuaMethodForScoringIndVo, crQuaMethodForScoringInd);
            crQuaMethodForScoringInd.setId(null);
            crQuaMethodForScoringInds.add(crQuaMethodForScoringInd);
        }
        crQuaMethodForScoringIndService.batchSave(crQuaMethodForScoringInds);
    }

    /**
     * 递归查询当前节点下所有的节点数据
     *
     * @param parentId
     * @param crScoringIndicatorVoList
     * @return
     */
    private List<CrScoringIndicatorVo> getAllChildIndicator(String parentId, List<CrScoringIndicatorVo> crScoringIndicatorVoList) {
        if (EmptyUtils.isNotEmpty(parentId)) {
            List<CrScoringIndicatorVo> child = crScoringIndicatorDao.getByParentId(parentId);
            if (!CollectionUtils.isEmpty(child)) {
                for (CrScoringIndicatorVo crScoringIndicatorVo : child) {
                    crScoringIndicatorVoList.add(crScoringIndicatorVo);
                    getAllChildIndicator(crScoringIndicatorVo.getId(), crScoringIndicatorVoList);
                }
            }
        }
        return crScoringIndicatorVoList;
    }

    @Override
    public ExcelVerifyHanlderResult verifyHandler(CrScoringIndicatorVo crScoringIndicatorVo) {
        return null;
    }

    @Override
    public void verifyHandler(List<CrScoringIndicatorVo> list) {

    }

    /**
     * 导入保存
     *
     * @param list
     * @param currentUser
     * @author zhoup
     * @since 2024/09/09
     */
    public void saveImport(List<CrScoringIndicatorVo> list, ICurrentUser currentUser) {
//        checkData(crScoringIndicatorVo);
//        CrScoringIndicator crScoringIndicator = new CrScoringIndicator();
//        BeanUtils.copyNotNullProperties(crScoringIndicatorVo, crScoringIndicator);
//        //如果parentId不为空，那么需要修改该父指标是否末级指标状态
//        if (EmptyUtils.isNotEmpty(crScoringIndicatorVo.getParentId())){
//            CrScoringIndicatorVo parent = crScoringIndicatorDao.getById(crScoringIndicatorVo.getParentId());
//            parent.setIsLast(CreditRatingConstant.CREDIT_SCORING_NO);//是否末级指标设置成否
//            CrScoringIndicator parentEntity = new CrScoringIndicator();
//            BeanUtils.copyNotNullProperties(parent, parentEntity);
//            crScoringIndicatorDao.update(parentEntity);
//            if (parent.getLevel() != null){
//                crScoringIndicator.setLevel(parent.getLevel()+1);
//            }
//        }else{
//            //如果parentId为空，说明该指标为顶级指标
//            crScoringIndicator.setLevel(1);
//        }
//
//        crScoringIndicator.setDeleteFlag(CreditRatingConstant.INIT_DELETE_FLAG);
//        crScoringIndicator.setVersionNumber(CreditRatingConstant.INIT_VERSION_NUMBER);
//        crScoringIndicator.setIsLast(CreditRatingConstant.CREDIT_SCORING_YES);
//        crScoringIndicator.setCreator(userId);
//        crScoringIndicator.setCreateDate(DateUtils.getDate());
//        crScoringIndicatorDao.save(crScoringIndicator);
    }

    /**
     * 导出
     *
     * @param searchVo
     * @param currentUser
     * @return
     * @author zhoup
     * @since 2024/09/09
     */
    public HSSFWorkbook exportData(CrScoringIndicatorVo searchVo, ICurrentUser currentUser) {
        //创建文档对象HSSFWorkbook
        HSSFWorkbook workbook = new HSSFWorkbook();
        //创建sheet
        String tempScoringInd = multiLangServiceI.getLang("table.creditRating.scoringIndicators");
        HSSFSheet sheet = workbook.createSheet(!StringUtils.isBlank(tempScoringInd) ? tempScoringInd : "评分指标");
        //待插入的数据
        //String resource = ResourceConstant.NETBANK_REPORT_NISALLOCATIONREQ_QUERY;
        List<CrScoringIndicatorVo> listVO = crScoringIndicatorDao.getList(searchVo);

        //for循环方式进行构建"行和列"
        //给sheet构建2行
        for (int i = 0; i < listVO.size() + 2; i++) {
            sheet.createRow(i);  //sheet的行
        }

        //设置表头
        this.setExcelHeader(workbook, sheet);
        //设置表体
        this.setExcelBody(workbook, sheet, listVO);

        return workbook;
    }

    /**
     * 设置表头
     *
     * @param sheet 表对象
     * @author zhoup
     * @since 2024/09/09
     */
    private void setExcelHeader(HSSFWorkbook workbook, HSSFSheet sheet) {
        //第一行
        HSSFRow row1 = sheet.getRow(0);
        row1.createCell(0).setCellValue(multiLangServiceI.getLang("credit.indicator.type.code"));
        row1.createCell(1).setCellValue(multiLangServiceI.getLang("credit.indicator.type.name"));
        row1.createCell(2).setCellValue(multiLangServiceI.getLang("credit.indicator.is.effective"));
        row1.createCell(3).setCellValue(multiLangServiceI.getLang("credit.indicator.rmk"));

        //表头样式
        for (int i = 0; i < 4; i++) {
            sheet.setColumnWidth(i, 25 * 256);
            HSSFCell cell = row1.getCell(i);
            if (cell != null) {
                cell.setCellStyle(this.setExcelHeaderStyle(workbook));
            }
        }
    }

    /**
     * 插入表体数据
     *
     * @param sheet  表对象
     * @param listVO 数据源
     * @author zhoup
     * @since 2024/09/09
     */
    private void setExcelBody(HSSFWorkbook workbook, HSSFSheet sheet, List<CrScoringIndicatorVo> listVO) {
        List<CrScoringIndicatorVo> sortedList = listVO.stream().sorted(Comparator.comparing(CrScoringIndicatorVo::getScoringIndicatorCode)).collect(Collectors.toList());
        List<CrScoringIndicatorVo> crScoringIndicatorVos = this.listTree(sortedList);
//        recursiveSortedChildren(crScoringIndicatorVos);
        //对crScoringIndicatorVos集合中的code字段排序，并且childrens也对code字段尽心排序
        //行指针
        int index = 1;
        recursiveChildren(crScoringIndicatorVos, workbook, sheet, index);
    }

    /**
     * 将List封装成拥有层级关系的list
     */
    private List<CrScoringIndicatorVo> listTree(List<CrScoringIndicatorVo> list) {
        Map<String, CrScoringIndicatorVo> map = new HashMap<>();
        List<CrScoringIndicatorVo> rootList = new ArrayList<>();

        // 将所有对象放入 map 中，以 id 为键
        for (CrScoringIndicatorVo vo : list) {
            map.put(vo.getId(), vo);
        }
        // 遍历列表，将每个对象放入其父节点的 children 列表中
        for (CrScoringIndicatorVo vo : list) {
            String parentId = vo.getParentId();
            if (parentId == null || parentId.isEmpty()) {
                // 如果没有父节点，则为根节点
                rootList.add(vo);
            } else {

                // 否则，将其放入父节点的 children 列表中
                CrScoringIndicatorVo parent = map.get(parentId);
                if (parent != null) {
                    if (parent.getChildrens() == null) {
                        parent.setChildrens(new ArrayList<>());
                    }
                    parent.getChildrens().add(vo);
                }else {
                    rootList.add(vo);
                }
            }
        }
        return rootList;
    }

    /**
     * 递归导出
     *
     * @param list
     * @param workbook
     * @param sheet
     * @param index
     */

    private int recursiveChildren(List<CrScoringIndicatorVo> list, HSSFWorkbook workbook, HSSFSheet sheet, int index) {
        Map<String, DictDTO> validSignMap = dictService.listMap("valid_sign");
        for (CrScoringIndicatorVo vo : list) {
            HSSFRow row = sheet.getRow(index);
            row.createCell(0).setCellValue(vo.getScoringIndicatorCode());
            row.createCell(1).setCellValue(vo.getScoringIndicatorName());
            String status = vo.getStatus().toString();
            String statusName = "";
            DictDTO dictDTO = validSignMap.get(status);
            if(!EmptyUtils.isEmpty(dictDTO)){
                statusName = dictDTO.getName();
            }
            row.createCell(2).setCellValue(statusName);
            if (StringUtils.isNotEmpty(vo.getScoringIndicatorDescription())) {
                row.createCell(3).setCellValue(vo.getScoringIndicatorDescription());
            } else {
                row.createCell(3).setCellValue("");
            }
            index++;
            //表体样式
            for (int i = 0; i < 4; i++) {
                HSSFCell cell = row.getCell(i);
                if (cell != null) {
                    cell.setCellStyle(this.setExcelBodyStyle(workbook));
                }
            }
            if (vo.getChildrens() != null && vo.getChildrens().size() > 0) {
                index = this.recursiveChildren(vo.getChildrens(), workbook, sheet, index);
            }
        }
        return index;
    }

    /**
     * 给字符串前面补上level-1个空格
     */
    private String addSpace(String str, int level) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < level - 1; i++) {
            sb.append("  ");
        }
        sb.append(str);
        return sb.toString();
    }

    /**
     * 设置表头样式
     *
     * @param workbook 文档对象
     * @return 表头样式
     * @author zhoup
     * @since 2024/09/09
     */
    private HSSFCellStyle setExcelHeaderStyle(HSSFWorkbook workbook) {
        //构建列的样式
        HSSFCellStyle cs = workbook.createCellStyle();
        //创建字体样式
        HSSFFont font = workbook.createFont();
        font.setFontName("宋体"); //字体
        font.setBold(true);                     //加粗
//        font.setColor((short) 50);               //设置字体颜色
        font.setFontHeightInPoints((short) 12); //字体大小

        cs.setFont(font);      //列的字体
        cs.setVerticalAlignment(VerticalAlignment.CENTER);  //垂直居中对齐
        cs.setAlignment(HorizontalAlignment.CENTER);        //水平居中对齐
        cs.setLocked(true);
        cs.setWrapText(true);
        cs.setLeftBorderColor(HSSFColor.BLACK.index);// 左边框的颜色
        cs.setBorderLeft(BorderStyle.THIN);// 边框的大小
        cs.setRightBorderColor(HSSFColor.BLACK.index);// 右边框的颜色
        cs.setBorderRight(BorderStyle.THIN);// 边框的大小
        cs.setBottomBorderColor(HSSFColor.GREEN.index);// 下边框的颜色
        cs.setBorderBottom(BorderStyle.THIN); // 下边框的大小
        cs.setTopBorderColor(HSSFColor.BLACK.index);// 上边框的颜色
        cs.setBorderTop(BorderStyle.THIN);// 上边框的颜色
        cs.setBottomBorderColor(HSSFColor.BLACK.index); // 设置单元格的边框颜色
        cs.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        cs.setFillForegroundColor(HSSFColor.GREY_25_PERCENT.index); // 设置单元格的背景颜色（单元格的样式会覆盖列或行的样式）
        return cs;
    }

    /**
     * 设置表体样式
     *
     * @param workbook 文档对象
     * @return 表体样式
     * @author zhoup
     * @since 2024/09/09
     */
    private HSSFCellStyle setExcelBodyStyle(HSSFWorkbook workbook) {
        HSSFCellStyle cs = workbook.createCellStyle();
//        HSSFPalette palette = workbook.getCustomPalette();//调色版
//        palette.setColorAtIndex(HSSFColor.GREY_25_PERCENT.index,(byte)0,(byte)0,(byte)228);
        HSSFFont font = workbook.createFont();
        font.setFontName("宋体"); //字体
        font.setFontHeightInPoints((short) 12);
        cs.setFont(font);      //列的字体
        cs.setVerticalAlignment(VerticalAlignment.CENTER);  //垂直居中对齐
        cs.setAlignment(HorizontalAlignment.CENTER);        //水平居中对齐
        cs.setLeftBorderColor(HSSFColor.BLACK.index);// 左边框的颜色
        cs.setBorderLeft(BorderStyle.THIN);// 边框的大小
        cs.setRightBorderColor(HSSFColor.BLACK.index);// 右边框的颜色
        cs.setBorderRight(BorderStyle.THIN);// 边框的大小
        cs.setBottomBorderColor(HSSFColor.GREEN.index);// 下边框的颜色
        cs.setBorderBottom(BorderStyle.THIN); // 下边框的大小
        cs.setTopBorderColor(HSSFColor.BLACK.index);// 上边框的颜色
        cs.setBorderTop(BorderStyle.THIN);// 上边框的颜色
        cs.setBottomBorderColor(HSSFColor.BLACK.index); // 设置单元格的边框颜色
//        cs.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
//        cs.setFillBackgroundColor(HSSFColor.GREY_25_PERCENT.index);
        return cs;
    }

    public synchronized String createScoringCode() {
        //查询数据库中最大的值
        Object maxScoringCode1 = crScoringIndicatorDao.getMaxScoringCode();
        if (EmptyUtils.isEmpty(maxScoringCode1)) {
            return "0001";
        }
        String maxScoringCode = String.valueOf(maxScoringCode1);
        int max = Integer.parseInt(maxScoringCode);
        String newMaxScoringCode = String.valueOf(max + 1);
        //判断是几位数，如果小于4在前面补0
        StringBuilder preZero = new StringBuilder();
        if (newMaxScoringCode.length() < 4) {
            for (int i = 0; i < 4 - newMaxScoringCode.length(); i++) {
                preZero.append("0");
            }
            newMaxScoringCode = preZero + newMaxScoringCode;
        }
        return newMaxScoringCode;
    }

    /**
     * 使用递归查询某一个指标下的所有子类指标，不管多少层级，不包括自己
     *
     * @param crScoringIndicatorVo
     * @return
     */
    public List<CrScoringIndicatorVo> allChildren(CrScoringIndicatorVo crScoringIndicatorVo) {
        List<CrScoringIndicatorVo> result = new ArrayList<>();
        if (crScoringIndicatorVo != null) {
            List<CrScoringIndicatorVo> children = crScoringIndicatorDao.getByParentId(crScoringIndicatorVo.getId());
            for (CrScoringIndicatorVo child : children) {
                result.add(child);
                result.addAll(allChildren(child)); // 递归调用
            }
        }
        return result;
    }

    /**
     * 获取机构编号
     */
    public String getOrgCode() {
        //查询数据库中机构编号的最大值
        Object maxScoringCode = crScoringIndicatorDao.getMaxScoringCode();
        if (!EmptyUtils.isEmpty(maxScoringCode)) {
            String maxCode = maxScoringCode.toString();
            //对maxCode做加1操作
            int max = Integer.parseInt(maxCode);
            maxCode = String.valueOf(max + 1);
            //记录maxCode的长度
            int length = maxCode.length();
            //通过判断maxCode的长度往前补0，补齐四位数
            if (maxCode.length() < 4) {
                for (int i = 0; i < 4 - length; i++) {
                    maxCode = "0" + maxCode;
                }
            }
            return maxCode;
        } else {
            return "0001";
        }
    }
}
