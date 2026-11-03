package com.bytter.credit.domain.service;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.system.api.IBtCurrencyService;
import com.bytter.system.api.dto.BtCurrencyDTO;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.bytter.credit.domain.dao.RatBkrationReliefDetailDao;
import com.bytter.credit.domain.entity.RatBkrationDisDetail;
import com.bytter.credit.domain.entity.RatBkrationReliefDetail;
import com.bytter.credit.domain.vo.RatUrationQueryVO;
import com.bytter.credit.domain.vo.RatUrationUseBeanVO;
import com.bytter.credit.domain.dao.RatCprationReliefDetailDao;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
/**
 * @author fusb
 * @version 1.0
 * @Title RatCprationReliefDetailService
 * @Description
 * @date 2018年11月24日
 */
@Service("ratCprationReliefDetailService")
@Transactional
public class RatCprationReliefDetailService extends BaseService<RatBkrationReliefDetail, String> {
    @Autowired
    private RatCprationReliefDetailDao ratCprationReliefDetailDao;
    @DubboReference
    private IBtCurrencyService btCurrencyServiceImpl;
    /**
     * 删除对象
     * 
     * @param entity
     */
    public void deleteEntity(RatBkrationReliefDetail entity) {
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
    public void save(RatBkrationReliefDetail entity) {
        super.saveEntity(entity);
    }
    /**
     * 更新
     * 
     * @param entity
     */
    public void update(RatBkrationReliefDetail entity) {
        super.updateEntity(entity);
    }
    /**
     * 保存or更新
     * 
     * @param entity
     */
    public void saveOrUpdate(RatBkrationReliefDetail entity) {
        super.saveOrUpdate(entity);
    }
    public BaseDao<RatBkrationReliefDetail, String> getEntityDao() {
        return ratCprationReliefDetailDao;
    }
    /**
     * 功能介绍 : 保存授信分配品种明细金额。
     * 
     * @param detail
     */
    public void saveRatBkrationRefDetail(RatBkrationReliefDetail detail) {
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
        ratCprationReliefDetailDao.flush();
    }
    /**
     * 功能介绍 : 获取调剂明细。
     * 
     * @param refCode
     * @return
     */
    @Transactional(readOnly = true)
    public List<RatBkrationReliefDetail> getRationReliefDtlByRefCode(String refCode) {
        return ratCprationReliefDetailDao.getRationReliefDtlByRefCode(refCode);
    }
    /**
     * 功能介绍 : 删除原授信调剂明细。
     * 
     * @param refCode
     */
    public void deleteBkrationRefDtlByCode(String refCode) {
        ratCprationReliefDetailDao.deleteCprationRefDtlByCode(refCode);
    }
    /**
     * 功能介绍 : 查询授信调剂在审占用金额。
     * 
     * @param queryVO
     * @return
     */
    @Transactional(readOnly = true)
    public List<RatUrationUseBeanVO> queryRationReliefUseDtl(RatUrationQueryVO queryVO) {
        //TODO 补充币种汇率字段
        List<RatUrationUseBeanVO> ratUrationUseList = ratCprationReliefDetailDao.queryRationReliefUseDtl(queryVO);
        if (EmptyUtils.isEmpty(ratUrationUseList)) {
            return ratUrationUseList;
        }
        List<String> curIds = ratUrationUseList.stream().map(ratUration -> ratUration.getCurCode()).collect(Collectors.toList());
        Map<String, BtCurrencyDTO> currencyByIds = btCurrencyServiceImpl.getCurrencyByIds(curIds);
        if (EmptyUtils.isEmpty(currencyByIds)) {
            return ratUrationUseList;
        }
        for (RatUrationUseBeanVO vo : ratUrationUseList) {
            //TODO 补充汇率信息
            vo.setExchangeRate(currencyByIds.get(vo.getCurCode()).getCurExrate());
        }
        return ratUrationUseList;
    }
}
