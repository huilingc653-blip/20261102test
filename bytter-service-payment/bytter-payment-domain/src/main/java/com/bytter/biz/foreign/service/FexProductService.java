package com.bytter.biz.foreign.service;

import com.bytter.biz.foreign.dao.FexProductDao;
import com.bytter.biz.foreign.entity.FexProduct;
import com.bytter.biz.foreign.vo.FexProductVO;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.exception.ParameterException;
import com.bytter.core.utils.BeanUtils;
import com.bytter.core.utils.DateUtils;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.core.utils.SqlUtil;
import com.bytter.core.utils.StringUtils;
import com.bytter.core.constant.CommonConstant;
import com.bytter.biz.payment.api.constant.ForeignConstant;
import com.bytter.biz.payment.api.enums.ForeignMessageEnum;
import com.bytter.system.api.IBtCurrencyService;
import com.bytter.system.api.dto.BtCurrencyDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * @author huangx
 * @version 1.0
 * @Title: Service
 * @Description: 外汇产品表Service
 * @date 2020-02-16 15:20:33
 */
@Service("fexProductService")
@Transactional
public class FexProductService extends BaseService<FexProduct, String> {
    @Autowired
    private FexProductDao fexProductDao;

    @Autowired(required = false)
    private IBtCurrencyService btCurrencyService;

    @Override
    public BaseDao<FexProduct, String> getEntityDao() {
        return fexProductDao;
    }

    /**
     * 查询外汇产品
     *
     * @param dataTablesInput
     * @return
     */
    @SuppressWarnings("rawtypes")
    public DataTablesOutput getDataTablesUnion(DataTablesInput dataTablesInput, ICurrentUser user) {
        String tranType = (String) dataTablesInput.getFormdata().get("tranType");
        StringBuilder sql = new StringBuilder();
        if(tranType.equals(ForeignConstant.TRAN_TYPE_1)){
            ArrayList<String> tranTypes = new ArrayList<>(Arrays.asList(ForeignConstant.TRAN_TYPE_1, ForeignConstant.TRAN_TYPE_7, ForeignConstant.TRAN_TYPE_8, ForeignConstant.TRAN_TYPE_9));
            String tranTypesSql = SqlUtil.getInSql(tranTypes, "p.tran_type", true);
            sql.append(" and ").append(tranTypesSql);
        }else{
            sql.append(" and p.tran_type='" + tranType + "'");
        }


        //选择产品过滤条件
        //过滤掉已有所属组的（历史数据里未归属产品组的 pro_id 可能是 NULL，也可能是空串）
        String filterProIdNull = (String) dataTablesInput.getFormdata().get("filterProIdNull");
        if (CommonConstant.YES.equals(filterProIdNull)) {
            sql.append(" and (p.pro_Id is null or p.pro_Id = '')");
        }
        //编辑时过滤掉本身
        String filterId = (String) dataTablesInput.getFormdata().get("filterId");
        if (StringUtils.isNotBlank(filterId)) {
            sql.append(" and p.id<>'" + filterId + "'");
        }
        //过滤掉本身所属组， 以免交叉
        String filterProId = (String) dataTablesInput.getFormdata().get("filterProId");
        if (StringUtils.isNotBlank(filterProId)) {
            sql.append(" and p.id <>'" + filterProId + "'");
        }
        return fexProductDao.getDataTablesUnion(dataTablesInput, sql.toString());
    }

    /**
     * 根据ID获取外汇产品信息
     *
     * @param id
     * @return
     */
    public FexProductVO getByIdUnion(String id) {
        FexProductVO fexProduct = fexProductDao.getByIdUnion(id);
        injectCurrency(fexProduct);
        return fexProduct;
    }

    /**
     * 回填币种名称/英文代码，供查看页展示默认买入、卖出、交割币种。
     */
    private void injectCurrency(FexProductVO fexProduct) {
        if (fexProduct == null || btCurrencyService == null) {
            return;
        }
        Map<String, BtCurrencyDTO> currencyMap = btCurrencyService.getCurrencyMap();
        if (EmptyUtils.isEmpty(currencyMap)) {
            return;
        }
        if (StringUtils.isNotBlank(fexProduct.getBuyCurId()) && currencyMap.containsKey(fexProduct.getBuyCurId())) {
            BtCurrencyDTO buyCur = currencyMap.get(fexProduct.getBuyCurId());
            fexProduct.setBuyCurName(buyCur.getCurName());
            fexProduct.setBuyEnglishCode(buyCur.getEnglishCode());
        }
        if (StringUtils.isNotBlank(fexProduct.getSellCurId()) && currencyMap.containsKey(fexProduct.getSellCurId())) {
            BtCurrencyDTO sellCur = currencyMap.get(fexProduct.getSellCurId());
            fexProduct.setSellCurName(sellCur.getCurName());
            fexProduct.setSellEnglishCode(sellCur.getEnglishCode());
        }
        if (StringUtils.isNotBlank(fexProduct.getDeliveryCurId()) && currencyMap.containsKey(fexProduct.getDeliveryCurId())) {
            BtCurrencyDTO deliveryCur = currencyMap.get(fexProduct.getDeliveryCurId());
            fexProduct.setDeliveryCurName(deliveryCur.getCurName());
            fexProduct.setDeliveryEnglishCode(deliveryCur.getEnglishCode());
        }
    }

    /**
     * 根据产品层级和交易类型获取产品信息
     *
     * @param proLevel
     * @param tranType
     * @return
     */
    public List<FexProductVO> listByProLevelAndTranType(String proLevel, String tranType) {
        return fexProductDao.listByProLevelAndTranType(proLevel, tranType);
    }

    /**
     * 根据产品层级和外汇交易类型获取产品信息
     *
     * @param proLevel
     * @param fexTranType
     * @return
     */
    public List<FexProductVO> listByProLevelAndFexTranType(String proLevel, String fexTranType) {
        return fexProductDao.listByProLevelAndFexTranType(proLevel, fexTranType);
    }

    /**
     * 保存外汇产品信息
     *
     * @param fexProduct
     */
    public void saveFexProduct(FexProductVO fexProduct, ICurrentUser user) {
        FexProduct entity = new FexProduct();
        BeanUtils.copyNotNullProperties(fexProduct, entity);
        entity.setCreateBy(user.getLoginName());
        entity.setCreateDate(DateUtils.getDate());

        if(StringUtils.isEmpty(entity.getIsAutoDelivery())){
            entity.setIsAutoDelivery(CommonConstant.NO);
        }
        this.saveEntity(entity);

        //添加组成员
        if (StringUtils.isNotBlank(fexProduct.getBillId())) {
            String[] billIds = fexProduct.getBillId().split(CommonConstant.COMMA_SPLIT);
            for (String id : billIds) {
                FexProduct product = this.get(id, true);
                product.setProId(entity.getId());
                entity.setUpdateBy(user.getLoginName());
                entity.setUpdateDate(DateUtils.getDate());
                this.saveOrUpdate(product);
            }
        }
    }

    /**
     * 修改外汇产品信息
     *
     * @param fexProduct
     */
    public void updateFexProduct(FexProductVO fexProduct, ICurrentUser user) {
        FexProduct entity = this.get(fexProduct.getId());
        BeanUtils.copyNotNullProperties(fexProduct, entity);
        entity.setUpdateBy(user.getLoginName());
        entity.setUpdateDate(DateUtils.getDate());
        if(StringUtils.isBlank(fexProduct.getRefundTime())){
            entity.setRefundTime(null);
        }
        this.saveOrUpdate(entity);

        //添加组成员
        if (StringUtils.isNotBlank(fexProduct.getBillId())) {
            String[] billIds = fexProduct.getBillId().split(CommonConstant.COMMA_SPLIT);
            for (String id : billIds) {
                FexProduct product = this.get(id, true);
                product.setProId(entity.getId());
                entity.setUpdateBy(user.getLoginName());
                entity.setUpdateDate(DateUtils.getDate());
                this.saveOrUpdate(product);
            }
        }
    }

    /**
     * 删除外汇产品信息
     *
     * @param id
     */
    public void delFexProduct(String id, ICurrentUser user) {
        this.delete(id);
    }

    /**
     * 判断是否存在产品名称
     *
     * @param proName
     * @param id
     * @return
     */
    public boolean isExistProName(String proName, String proLevel, String id) {
        List<FexProductVO> list = fexProductDao.listByProName(proName, proLevel, id);
        if (EmptyUtils.isEmptyList(list)) {
            return false;
        }
        return true;
    }

    /**
     * 根据所属产品组获取产品信息
     *
     * @param proId
     * @return
     */
    public List<FexProduct> listByProId(String proId) {
        return fexProductDao.listBy("proId", proId);
    }


    /**
     * 根据产品Id获取产品
     *
     * @param fexProduct 单位持票信息VO
     * @return
     */
    @Transactional(readOnly = true)
    public List<FexProductVO> selectFexProductView(FexProductVO fexProduct) {
        if (fexProduct == null || StringUtils.isBlank(fexProduct.getId())) {
            return null;
        }
        String[] ids = StringUtils.split(fexProduct.getId(), CommonConstant.COMMA_SPLIT);
        return fexProductDao.listByIds(ids);
    }

    /**
     * <p>
     * 查询产品是否引用了规则组，列出所有引用规则组的产品
     * </p>
     *
     * @param tranGroupRuleId 规则组ID
     * @return number
     */
    public long countByTranGroupRuleId(String tranGroupRuleId) {
        List<FexProductVO> fexProductVOS = fexProductDao.countByTranGroupRuleId(tranGroupRuleId);
        if (fexProductVOS == null) {
            return 0;
        }
        return fexProductVOS.size();
    }

    /**
     * 数据校验
     *
     * @param fexProduct
     * @return
     */
    public boolean dataCheck(FexProductVO fexProduct) {
        if (this.isExistProName(fexProduct.getProName(), fexProduct.getProLevel(), fexProduct.getId())) {
            if (ForeignConstant.PRO_LEVEL_0.equals(fexProduct.getProLevel())) {
                throw new ParameterException(ForeignMessageEnum.FEX_PRODUCT_MESSAGE_NAME_UNIQUE);
            } else {
                throw new ParameterException(ForeignMessageEnum.FEX_PRODUCT_MESSAGE_PRONAME_UNIQUE);
            }
        }
        return true;
    }

    /**
     * 校验产品组是否可以改有效无效
     */
    public void checkGroupChangeValid(FexProductVO fexProduct) {
        FexProduct entity = this.get(fexProduct.getId());
        if (entity.getProLevel().equals(ForeignConstant.PRO_LEVEL_0) && fexProduct.getValidSign().equals(CommonConstant.VALID_SIGN_NO)) {
            List<FexProductVO> fexProductVOS = fexProductDao.listByGroupId(fexProduct.getId());
            if (fexProductVOS.size() > 0) {
                throw new ParameterException(ForeignMessageEnum.FEX_PRODUCT_CANNOT_CHANGE);
            }
        }
    }

    /**
     * 校验外汇产品跟其他业务的关联
     *
     * @param proId
     */
    public void checkAssociated(String proId) {
        if (fexProductDao.checkAssociated(proId) > 0){
            throw new ParameterException(ForeignMessageEnum.FEX_PRODUCT_MESSAGE_PRODUCT_DEL);
        }
    }

    /**
     * 资金收付交易初始化产品
     *
     * @return
     */
    public List<FexProductVO> getInitProductDate() {
        return fexProductDao.getInitProductDate();
    }

    /**
     * 查询产品列表
     * @param proLevel
     * @param tranType
     * @param fexTranType
     * @return
     */
    public List<FexProductVO> listByProAndFexTranType(String proLevel, String tranType, String fexTranType) {
        return fexProductDao.listByProAndFexTranType(proLevel, tranType,fexTranType);
    }
}
