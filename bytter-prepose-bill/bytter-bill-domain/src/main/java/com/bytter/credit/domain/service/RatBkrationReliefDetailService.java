package com.bytter.credit.domain.service;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.credit.domain.dao.RatBkrationReliefDetailDao;
import com.bytter.credit.domain.entity.RatBkrationReliefDetail;
import com.bytter.credit.domain.vo.RatUrationCurVO;
import com.bytter.credit.domain.vo.RatUrationQueryVO;
import com.bytter.credit.domain.vo.RatUrationUseBeanVO;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.system.api.IBtCurrencyService;
import com.bytter.system.api.dto.BtCurrencyDTO;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
/**
 * @Title: Service
 * @Description: 授信调剂明细表
 * @author onlineGenerator
 * @date 2017-08-13 16:33:55
 * @version 1.0
 *
 */
@Service("ratBkrationReliefDetailService")
@Transactional
public class RatBkrationReliefDetailService extends BaseService<RatBkrationReliefDetail, String> {
    @Autowired
    private RatBkrationReliefDetailDao ratBkrationReliefDetailDao;
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
        return ratBkrationReliefDetailDao;
    }
    /**
     * 功能介绍 : 保存授信分配品种明细金额。<br>
     * <b>日期：</b>2017年8月3日<br>
     * 
     * @version 1.0
     * @author liyh
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
        ratBkrationReliefDetailDao.flush();
    }
    /**
     * 功能介绍 : 获取调剂明细。<br>
     * <b>日期：</b>2017年8月16日<br>
     * 
     * @version 1.0
     * @author liyh
     */
    @Transactional(readOnly = true)
    public List<RatBkrationReliefDetail> getRationReliefDtlByRefCode(String refCode) {
        return ratBkrationReliefDetailDao.getRationReliefDtlByRefCode(refCode);
    }
    /**
     * 功能介绍 : 删除原授信调剂明细。<br>
     * <b>日期：</b>2017年8月18日<br>
     * 
     * @version 1.0
     * @author liyh
     */
    public void deleteBkrationRefDtlByCode(String refCode) {
        ratBkrationReliefDetailDao.deleteBkrationRefDtlByCode(refCode);
    }
    /**
     * 功能介绍 : 查询授信调剂在审占用金额。<br>
     * <b>日期：</b>2017年9月23日<br>
     * 
     * @version 1.0
     * @author liyh
     */
    @Transactional(readOnly = true)
    public List<RatUrationUseBeanVO> queryRationReliefUseDtl(RatUrationQueryVO queryVO) {
        //TODO 补充币种汇率字段
        List<RatUrationUseBeanVO> ratUrationUseList = ratBkrationReliefDetailDao.queryRationReliefUseDtl(queryVO);
        if (EmptyUtils.isEmpty(ratUrationUseList)) {
            return ratUrationUseList;
        }
        //都有值的
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
    /**
     * <br>
     * <b>功能：</b> 查询授信调剂在审占用明细 <br>
     * <b>作者：</b>liyh<br>
     * <b>日期：</b> 2020/11/18 0018 <br>
     */
    @Transactional(readOnly = true)
    public List<RatUrationCurVO> queryRationReliefFreeze(List<String> ratCodes) {
        return ratBkrationReliefDetailDao.queryRationReliefFreeze(ratCodes);
    }
}
