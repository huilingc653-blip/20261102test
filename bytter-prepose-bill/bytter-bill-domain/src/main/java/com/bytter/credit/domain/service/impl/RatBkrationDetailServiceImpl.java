package com.bytter.credit.domain.service.impl;
import com.alibaba.nacos.client.naming.utils.CollectionUtils;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.core.enums.MatchTypeEnum;
import com.bytter.core.utils.BeanUtils;
import com.bytter.credit.api.IRatBkrationDetailService;
import com.bytter.credit.domain.dao.RatBkrationDetailDao;
import com.bytter.credit.domain.entity.RatBkrationDetail;
import com.bytter.credit.domain.vm.RatBkRationDtlVm;
import com.bytter.credit.domain.vm.RatBkRationVm;
import com.bytter.credit.domain.vo.RatItemDisVO;

import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
/**
 * @Title: Service
 * @Description: 授信录入明细表
 * @author onlineGenerator
 * @date 2017-07-21 14:35:53
 * @version 1.0
 *
 */
@Service("ratBkrationDetailServiceImpl")
@Transactional
@DubboService
public class RatBkrationDetailServiceImpl extends BaseService<RatBkrationDetail, String> implements IRatBkrationDetailService{
    @Autowired
    private RatBkrationDetailDao ratBkrationDetailDao;
    /**
     * 删除对象
     * 
     * @param entity
     */
    public void deleteEntity(RatBkrationDetail entity) {
        super.deleteEntity(entity);
    }
    /**
     * 根据id删除
     * 
     * @param id
     */
    public void delete(String id) {
        super.delete(id);
    }
    /**
     * 保存
     * 
     * @param entity
     */
    public void save(RatBkrationDetail entity) {
        super.saveEntity(entity);
    }
    /**
     * 更新
     * 
     * @param entity
     */
    public void update(RatBkrationDetail entity) {
        super.updateEntity(entity);
    }
    /**
     * 保存or更新
     * 
     * @param entity
     */
    public void saveOrUpdate(RatBkrationDetail entity) {
        super.saveOrUpdate(entity);
    }
    public BaseDao<RatBkrationDetail, String> getEntityDao() {
        return ratBkrationDetailDao;
    }
    /**
     * 功能介绍 : 检查授信品种是否被使用。<br>
     * <b>日期：</b>2017年7月21日<br>
     * 
     * @version 1.0
     * @author liyh
     */
    public boolean checkItemCodeUse(String itemCode) {
        List<RatBkrationDetail> list = ratBkrationDetailDao.listBy("itemCode", itemCode, MatchTypeEnum.EQ);
        if (null != list && !list.isEmpty()) {
            return true;
        }
        return false;
    }
    /**
     * 功能介绍 : 获取授信明细信息。<br>
     * <b>日期：</b>2017年7月24日<br>
     * 
     * @version 1.0
     * @author liyh
     */
    @Transactional(readOnly = true)
    public List<RatBkrationDetail> getBkrationDetailByRatCode(String ratCode) {
        return ratBkrationDetailDao.listBy("ratCode", ratCode, MatchTypeEnum.EQ);
    }
    /**
     * 功能介绍 : 删除原授信明细。<br>
     * <b>日期：</b>2017年7月26日<br>
     * 
     * @version 1.0
     * @author liyh
     */
    public void deleteBkrationDtlByRatCode(RatBkRationVm bkrationDTO) {
        ratBkrationDetailDao.deleteBkrationDtlByRatCode(bkrationDTO);
    }
    /**
     * 功能介绍 : 根据授信编号获取单位已录入的授信明细。<br>
     * <b>日期：</b>2017年7月27日<br>
     * 
     * @version 1.0
     * @author liyh
     */
    @Transactional(readOnly = true)
    public List<RatBkRationDtlVm> getBkrationDtlDTOByRatCode(String ratCode, String itemFlag) {
        return ratBkrationDetailDao.getBkrationDtlDTOByRatCode(ratCode, itemFlag);
    }
    /**
     * 功能介绍 : 根据授信编号获取单位已录入的授信明细集合。<br>
     * <b>日期：</b>2017年7月28日<br>
     * 
     * @version 1.0
     * @author liyh
     */
    @Transactional(readOnly = true)
    public List<RatBkRationDtlVm> getBkrationDtlByRatCodes(List<String> ratCodes) {
        return ratBkrationDetailDao.getBkrationDtlByRatCodes(ratCodes);
    }
    /**
     * 授信详情
     * 
     * @param ratCode
     * @param iteamCode
     * @return
     */
    @Transactional(readOnly = true)
    public List<RatBkRationDtlVm> getBkrationDtlByRatAndIteamCode(String ratCode, String iteamCode) {
        return ratBkrationDetailDao.getBkrationDtlByRatAndIteamCode(ratCode, iteamCode);
    }
    /**
     * 功能介绍 : 保存授信录入品种明细金额。<br>
     * <b>日期：</b>2017年8月3日<br>
     * 
     * @version 1.0
     * @author liyh
     */
    public void saveRatBkrationDetail(RatBkrationDetail detail) {
        Class userCla = (Class) detail.getClass();
        /*
         * 得到类中的所有属性集合
         */
        try {
            Field[] fs = userCla.getDeclaredFields();
            for (int i = 0; i < fs.length; i++) {
                Field f = fs[i];
                f.setAccessible(true); // 设置些属性是可以访问的
                Object val = f.get(detail);// 得到此属性的值
                String type = f.getType().toString();// 得到此属性的类型
                if (type.endsWith("BigDecimal")) {
                    if (null == val) {
                        f.set(detail, BigDecimal.ZERO);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        this.save(detail);
    }
    /**
     * 功能介绍 : 更新原授信中追加额度。<br>
     * <b>日期：</b>2017年8月7日<br>
     * 
     * @version 1.0
     * @author liyh
     */
    public void updateRationDtlAddMoney(RatBkRationDtlVm dto) {
        ratBkrationDetailDao.updateRationDtlAddMoney(dto);
    }
    /**
     * 功能介绍 : 查询授信品种的保证金比例或费率。<br>
     * <b>日期：</b>2017年8月16日<br>
     * 
     * @version 1.0
     * @author liyh
     */
    public RatItemDisVO getItemAssure(String ratCode, String itemCode) {
        List<RatItemDisVO> disVOs = ratBkrationDetailDao.getItemAssure(ratCode, itemCode);
        if (null != disVOs && !disVOs.isEmpty()) {
            return disVOs.get(0);
        }
        RatItemDisVO disVO = new RatItemDisVO();
        disVO.setAssurepercent(BigDecimal.ZERO);
        disVO.setInterestrate(BigDecimal.ZERO);
        disVO.setProcedureFeeRate(BigDecimal.ZERO);
        return disVO;
    }
    /**
     * 查询授信品种的保证金比例或费率。
     * 
     * @return
     */
    @Override
    public List<com.bytter.credit.vo.RatItemDisVO> getAllItemAssure() {
        List<RatItemDisVO> list = ratBkrationDetailDao.getItemAssure(null, null);
        if(CollectionUtils.isEmpty(list)) {
        	return null;
        }
        List<com.bytter.credit.vo.RatItemDisVO> listvo = BeanUtils.copyListProperties(list, com.bytter.credit.vo.RatItemDisVO.class);
        return listvo;
    }
    /**
     * 功能介绍 : 更新原授信中调剂额度。<br>
     * <b>日期：</b>2017年8月16日<br>
     * 
     * @version 1.0
     * @author liyh
     */
    public void updateRationDtlReliefMoney(RatBkRationDtlVm dto) {
        ratBkrationDetailDao.updateRationDtlReliefMoney(dto);
    }
    /**
     * 功能介绍 : 更新原授信中最高额度。<br>
     * <b>日期：</b>2017年8月16日<br>
     * 
     * @version 1.0
     * @author liyh
     */
    public void updateRationDtlMaxMoney(RatBkRationDtlVm dto) {
        ratBkrationDetailDao.updateRationDtlMaxMoney(dto);
    }
}
