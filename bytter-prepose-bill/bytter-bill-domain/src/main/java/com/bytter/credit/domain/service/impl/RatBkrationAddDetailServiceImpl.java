package com.bytter.credit.domain.service.impl;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.core.utils.BeanUtils;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.core.utils.StringUtils;
import com.bytter.credit.api.IRatBkrationAddDetailService;
import com.bytter.credit.domain.dao.RatBkrationAddDetailDao;
import com.bytter.credit.domain.entity.RatBkrationAddDetail;
import com.bytter.credit.domain.vm.RatBkRationAddDtlVm;
import com.bytter.credit.domain.vm.RatBkRationAddVm;
import com.bytter.credit.domain.vo.RatUrationCurVO;
import com.bytter.credit.domain.vo.RatUrationQueryVO;
import com.bytter.credit.domain.vo.RatUrationUseBeanVO;
import com.bytter.system.api.IBtCurrencyService;
import com.bytter.system.api.dto.BtCurrencyDTO;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.dubbo.config.annotation.DubboService;
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
 * @Description: 授信追加明细表
 * @author onlineGenerator
 * @date 2017-07-31 16:00:34
 * @version 1.0
 *
 */
@Service("ratBkrationAddDetailServiceImpl")
@Transactional
@DubboService
public class RatBkrationAddDetailServiceImpl extends BaseService<RatBkrationAddDetail, String> implements IRatBkrationAddDetailService{
    @Autowired
    private RatBkrationAddDetailDao ratBkrationAddDetailDao;
    @DubboReference
    private IBtCurrencyService btCurrencyServiceImpl;
    /**
     * 删除对象
     * 
     * @param entity
     */
    public void deleteEntity(RatBkrationAddDetail entity) {
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
    public void save(RatBkrationAddDetail entity) {
        super.saveEntity(entity);
    }
    /**
     * 更新
     * 
     * @param entity
     */
    public void update(RatBkrationAddDetail entity) {
        super.updateEntity(entity);
    }
    /**
     * 保存or更新
     * 
     * @param entity
     */
    public void saveOrUpdate(RatBkrationAddDetail entity) {
        super.saveOrUpdate(entity);
    }
    public BaseDao<RatBkrationAddDetail, String> getEntityDao() {
        return ratBkrationAddDetailDao;
    }
    /**
     * 功能介绍 : 根据授信追加编号获取单位已录入的授信明细集合。<br>
     * <b>日期：</b>2017年7月28日<br>
     * 
     * @version 1.0
     * @author liyh
     */
    @Transactional(readOnly = true)
    public List<RatBkRationAddDtlVm> getBkrationDtlByRatCodes(List<String> addCodes) {
        return ratBkrationAddDetailDao.getBkrationDtlByRatCodes(addCodes);
    }
    /**
     * 功能介绍 : 查询集团授信追加的授信品种明细。<br>
     * <b>日期：</b>2017年9月28日<br>
     * 
     * @version 1.0
     * @author liyh
     */
    @Transactional(readOnly = true)
    public List<RatBkRationAddDtlVm> getBkrationAddDtlByRatCode(String ratCode, String corpId, String itemFlag,
            String itemCode, String flag) {
        return ratBkrationAddDetailDao.getBkrationAddDtlByRatCode(ratCode, corpId, itemFlag, itemCode, flag);
    }
    /**
     * 功能介绍 : 保存授信追加品种明细金额。<br>
     * <b>日期：</b>2017年8月3日<br>
     * 
     * @version 1.0
     * @author liyh
     */
    public void saveRatBkrationAddDetail(RatBkrationAddDetail detail) {
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
     * 功能介绍 : 删除原授信追加明细。<br>
     * <b>日期：</b>2017年8月3日<br>
     * 
     * @version 1.0
     * @author liyh
     */
    public void deleteBkrationDisDtlByDisCode(RatBkRationAddVm disDTO) {
        ratBkrationAddDetailDao.deleteBkrationDisDtlByDisCode(disDTO);
    }
    /**
     * 功能介绍 : 查询集团授信追减在途额度明细。<br>
     * <b>日期：</b>2017年9月30日<br>
     * 
     * @version 1.0
     * @author liyh
     */
    @Transactional(readOnly = true)
    public List<RatUrationUseBeanVO> queryRationSubAddDtl(RatUrationQueryVO queryVO) {
        List<RatUrationUseBeanVO> ratUrationUseList = ratBkrationAddDetailDao.queryRationSubAddDtl(queryVO);
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
     * 功能介绍 : 查询集团授信追减在途额度明细 <br>
     * <b>日期：</b> 2020/11/18 0018 <br>
     * 
     * @version 1.0
     * @author liyh
     */
    @Transactional(readOnly = true)
    @Override
    public List<com.bytter.credit.vo.RatUrationCurVO> queryRationSubAddFreeze(List<String> ratCodes) {
    	List<RatUrationCurVO> list = ratBkrationAddDetailDao.queryRationSubAddFreeze(ratCodes);
    	List<com.bytter.credit.vo.RatUrationCurVO> listvo = BeanUtils.copyListProperties(list, com.bytter.credit.vo.RatUrationCurVO.class);
        return listvo;
    }
}
