package com.bytter.biz.foreign.service;

import com.bytter.biz.budget.spi.enums.FbsBudgetMessageEnum;
import com.bytter.biz.foreign.dao.FexExposureItemDao;
import com.bytter.biz.foreign.entity.FexExposureDetail;
import com.bytter.biz.foreign.entity.FexExposureItem;
import com.bytter.biz.foreign.vo.FexExposureItemVO;
import com.bytter.biz.foreign.vo.FexExposureTemplateItemVO;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.core.common.exception.ParameterException;
import com.bytter.core.dict.DictInDTO;
import com.bytter.core.utils.DateUtils;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.core.utils.SortList;
import com.bytter.core.utils.StringUtils;
import com.bytter.system.api.IDictService;
import com.bytter.system.api.Ii18nService;
import com.bytter.system.api.dto.I18nDTO;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * @Title: Service
 * @Description: 敞口科目表
 * @author huangx
 * @date 2022-06-24 15:20:33
 * @version 1.0
 *
 */
@Service("fexExposureItemService")
@Transactional
public class FexExposureItemService extends BaseService<FexExposureItem, String> {

    @Autowired
    private FexExposureItemDao fexExposureItemDao;

    @Autowired
    private FexExposureTemplateItemService fexExposureTemplateItemService;

    @Autowired
    private FexExposureDetailService fexExposureDetailService;

    @Override
    public BaseDao<FexExposureItem, String> getEntityDao() {
        return fexExposureItemDao;
    }

    @Autowired(required = false)
    private Ii18nService ii18nService;

    @DubboReference(check = false)
    private IDictService dictService;

    /**
     * 获取树形表格
     *
     * @return
     * @throws Exception
     */
    public List<FexExposureItemVO> listTreeData(FexExposureItemVO fexExposureItem, String validSign) {
        List<FexExposureItemVO> list = fexExposureItemDao.listFexExposureItem(fexExposureItem, validSign);
        if (list != null && list.size() > 0 && fexExposureItem != null) {
            List<String> codes = getListNoRepeat(list);
            if (codes != null && codes.size() > 0) {
                list = fexExposureItemDao.listFexExposureItemByCodes(codes, fexExposureItem);
            }
        }
        return getListSort(list, fexExposureItem);
    }

    /**
     * 重复code过滤
     *
     * @param list
     * @return
     */
    private List<String> getListNoRepeat(List<FexExposureItemVO> list) {
        List<String> codes = new ArrayList<String>();
        for (FexExposureItemVO vo : list) {
            if (!codes.contains(vo.getItemCode())) {
                codes.add(vo.getItemCode());
            }
            if (StringUtils.isNotEmpty(vo.getSuperItemCodes())) {
                String[] superCodes = vo.getSuperItemCodes().split(",");
                for (String code : superCodes) {
                    if (!codes.contains(code)) {
                        codes.add(code);
                    }
                }
            }
        }
        return codes;
    }

    /**
     * 根据层级结构排序，组装 tree 结构
     *
     * @param list
     * @return
     */
    public List<FexExposureItemVO> getListSort(List<FexExposureItemVO> list, FexExposureItemVO fexExposureItem) {
        if (list == null || list.size() <= 0) {
            return list;
        }
        List<FexExposureItemVO> root = new ArrayList<FexExposureItemVO>();
        List<FexExposureItemVO> subList = new ArrayList<FexExposureItemVO>();
        Map<String, Map<String, DictInDTO>> dicMap=  dictService.listDictMap(Arrays.asList("fbsItem.sign","sf_yn","valid_sign"));

        Map<String, DictInDTO> signMap=   dicMap.get("fbsItem.sign");
        Map<String, DictInDTO> sfynMap=   dicMap.get("sf_yn");
        Map<String, DictInDTO> validMap=   dicMap.get("valid_sign");
        for (FexExposureItemVO fbs : list) {

            if (!sfynMap.isEmpty() && !EmptyUtils.isEmpty(fbs.getIsNaturalHedge())) {
                DictInDTO sign = sfynMap.get(fbs.getIsNaturalHedge());
                if (sign != null) {
                    fbs.setIsNaturalHedgeName(sign.getName());
                }
            }
            if (!signMap.isEmpty() && !EmptyUtils.isEmpty(fbs.getSign())) {
                DictInDTO sign = signMap.get(fbs.getSign() + "");
                if (sign != null) {
                    fbs.setSignName(sign.getName());
                }
            }
            if (!validMap.isEmpty() && !EmptyUtils.isEmpty(fbs.getValidSign())) {
                DictInDTO sign = validMap.get(fbs.getValidSign());
                if (sign != null) {
                    fbs.setValidSignName(sign.getName());
                }
            }


            if (StringUtils.isEmpty(fbs.getSuperItemCode())) {
                root.add(fbs);
            } else {
                subList.add(fbs);
            }
        }
        List<FexExposureItemVO> allList = new ArrayList<FexExposureItemVO>();
        if(EmptyUtils.isEmpty(fexExposureItem) || EmptyUtils.isEmpty(fexExposureItem.getShowItem())) {
            for (FexExposureItemVO fbs : root) {
                getListSort(fbs, subList, allList);
            }
        }else{
            if(fexExposureItem.getShowItem() > 1 || fexExposureItem.getShowItem() == -1){
                for (FexExposureItemVO fbs : subList) {
                    fbs.setSuperItemCode("");
                    getListSort(fbs, root, allList);
                }
            }else{
                for (FexExposureItemVO fbs : root) {
                    getListSort(fbs, subList, allList);
                }
            }
        }
        SortList<FexExposureItemVO> sortList = new SortList<FexExposureItemVO>();
        sortList.Sort(allList, "getItemCode", "getSuperItemCode", null);
        return allList;
    }

    public void getListSort(FexExposureItemVO FexExposureItemVO, List<FexExposureItemVO> subList, List<FexExposureItemVO> allList) {
        allList.add(FexExposureItemVO);
        for (FexExposureItemVO sub : subList) {
            if (FexExposureItemVO.getItemCode().equals(sub.getSuperItemCode())) {
                getListSort(sub, subList, allList);
            }
        }

    }
    
    /**
     * 获取科目最高层级
     * @return
     */
    public Integer getItemMaxLevel(){
        return fexExposureItemDao.getItemMaxLevel();
    }

    /**
     * 保存科目
     *
     * @param entity
     */
    public void saveItem(FexExposureItem entity, ICurrentUser user, String locale) {
        StringBuilder sb = new StringBuilder();
        if (StringUtils.isEmpty(entity.getId())) {
            String superItemCode = entity.getSuperItemCode();
            if (StringUtils.isNotEmpty(superItemCode)) {
                FexExposureItemVO itemVO = fexExposureItemDao.getFexExposureItemVO("", superItemCode);
                if (StringUtils.isNotEmpty(itemVO.getSuperItemCodes())) {
                    entity.setSuperItemCodes(sb.append(itemVO.getSuperItemCodes()).append(",").append(superItemCode).toString());
                } else {
                    entity.setSuperItemCodes(superItemCode);
                }
                String seqCode = fexExposureItemDao.getSequence(entity.getSuperItemCode(), 3);
                if(EmptyUtils.isBlank(seqCode)){
                    seqCode = entity.getSuperItemCode() + "001";
                }
                entity.setSeqCode(entity.getSuperItemCode());
                entity.setItemCode(seqCode);
            } else {
                String seqCode = fexExposureItemDao.getSequence(null, 2);
                if(EmptyUtils.isBlank(seqCode)){
                    seqCode = "100";
                } else if ("9".equals(seqCode)){
                    seqCode = fexExposureItemDao.getSequence("9", 2);
                    entity.setSeqCode("9");
                } else {
                    if("900".equals(seqCode)) {
                        entity.setSeqCode("9");
                    }
                }
                entity.setItemCode(seqCode);
            }
            entity.setCreateBy(user.getLoginName());
            entity.setCreateDate(DateUtils.getDate());
            entity.setUpdateBy(user.getLoginName());
            entity.setUpdateDate(DateUtils.getDate());
        }
        super.saveEntity(entity);
        //新增当前语种
        I18nDTO i18nDTO = new I18nDTO();
        i18nDTO.setKeyId(entity.getId());
        i18nDTO.setLocale(locale);
        i18nDTO.setContext(entity.getItemName());
        ii18nService.saveOrUpdateBusinessBaseContext(i18nDTO);
    }

    /**
     * 查询子节点是否存在 有效的 科目存在
     *
     * @param itemCode
     * @return
     */
    public boolean isEdit(String itemCode) {
        BigDecimal count = fexExposureItemDao.isEdit(itemCode);
        if (count.compareTo(BigDecimal.ZERO) > 0) {
            return true;
        }
        return false;
    }

    /**
     * 保存or更新
     *
     * @param entity
     */
    public void updateItem(FexExposureItem entity, ICurrentUser user, String locale) {
        entity.setUpdateBy(user.getLoginName());
        entity.setUpdateDate(DateUtils.getDate());
        super.saveOrUpdate(entity);
        //更新当前语种
        I18nDTO i18nDTO = new I18nDTO();
        i18nDTO.setKeyId(entity.getId());
        i18nDTO.setLocale(locale);
        i18nDTO.setContext(entity.getItemName());
        ii18nService.saveOrUpdateBusinessBaseContext(i18nDTO);
    }

    /**
     * 获取科目
     * @param id
     * @param itemCode
     * @return
     */
    public FexExposureItemVO getFexExposureItemVO(String id, String itemCode) {
        return fexExposureItemDao.getFexExposureItemVO(id, itemCode);
    }

    /**
     * 删除科目
     *
     * @param entity
     */
    public void deleteItem(FexExposureItem entity) {
        List<String> allList = new ArrayList<String>();
        List<String> itemCodes = new ArrayList<String>();
        itemCodes.add(entity.getItemCode());
        //通过当前科目代码，查找是否有子节点
        List<FexExposureItemVO> list = fexExposureItemDao.listBySuperItemCodes(itemCodes);
        //通过科目代码，递归查找所有子节点存储于allList
        getItemCodes(list, allList);
        //allList中添加当前节点代码
        allList.add(entity.getItemCode());
        //根据所有节点code判断是否存在使用记录
        List<FexExposureTemplateItemVO> exposureTemplateItemList = fexExposureTemplateItemService.listVOByItemCodes(allList);
        if (!EmptyUtils.isEmptyList(exposureTemplateItemList)) {
            throw new ParameterException(FbsBudgetMessageEnum.FBS_BUDGET_ITEM_CODE_MESSAGE_EXISTS, entity.getItemCode());
        } else {
            fexExposureItemDao.deleteByItemCodes(allList);
        }
        //删除多语言
        ii18nService.deleteBusinessBaseContext(entity.getId());
    }

    /**
     * 通过科目代码，递归查找所有子节点
     *
     * @param list
     * @param allList 存储所有子节点
     */
    private void getItemCodes(List<FexExposureItemVO> list, List<String> allList) {
        List<String> itemCodes = new ArrayList<String>();
        if (EmptyUtils.isNotEmpty(list) && list.size() > 0) {
            for (FexExposureItemVO vo : list) {
                itemCodes.add(vo.getItemCode());
                allList.add(vo.getItemCode());
            }
        }
        if (itemCodes.size() > 0) {
            list = fexExposureItemDao.listBySuperItemCodes(itemCodes);
            getItemCodes(list, allList);
        }
    }

    /**
     * 校验科目是否使用
     * @param itemCode
     * @return
     */
    public boolean validItemUsed(String itemCode) {
        boolean result = false;
        List<FexExposureDetail> exposureDetailList = fexExposureDetailService.listByItemCode(itemCode);
        if (EmptyUtils.isNotEmpty(exposureDetailList)) {
            result = true;
        }
        return result;
    }
}
