package com.bytter.credit.domain.service;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.credit.domain.dao.RatDisDetailHisDao;
import com.bytter.credit.domain.entity.RatBkrationDisDetail;
import com.bytter.credit.domain.entity.RatDisDetailHis;
import com.bytter.credit.domain.vm.RatBkRationDtlVm;
import com.bytter.credit.domain.vo.RatDisDetailHisVO;
import com.bytter.credit.dto.RatBkrationDisDtlDTO;
import com.bytter.credit.dto.RatBkrationDisListDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.List;
/**
 * 
 * @author luxy
 * @version 1.0
 * @Title:RatDisDetailHisService
 * @Description:授信分配历史明细Service
 * @date 2018年11月26日 下午6:42:23
 */
@Service("RatDisDetailHisService")
@Transactional
public class RatDisDetailHisService extends BaseService<RatDisDetailHis, String> {
    @Autowired
    private RatDisDetailHisDao ratDisHisDao;
    /**
     * 保存历史分配明细
     */
    @SuppressWarnings("rawtypes")
    public void saveDisDetailHis(RatDisDetailHis detailHis) {
        Class userCla = (Class) detailHis.getClass();
        /*
         * 得到类中的所有属性集合
         */
        try {
            Field[] fs = userCla.getDeclaredFields();
            for (int i = 0; i < fs.length; i++) {
                Field f = fs[i];
                f.setAccessible(true); // 设置些属性是可以访问的
                Object val = f.get(detailHis);// 得到此属性的值
                String type = f.getType().toString();// 得到此属性的类型
                if (type.endsWith("BigDecimal")) {
                    if (null == val) {
                        f.set(detailHis, BigDecimal.ZERO);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        super.saveEntity(detailHis);
    }
    /**
     * 根据分配编号和分配次数查询分配历史明细
     */
    public List<RatDisDetailHis> queryDisDetailHisList(String disCode) {
        return ratDisHisDao.queryDisDetailHisByDisCode(disCode);
    }
    /**
     * 根据分配编号查询分配历史明细
     */
    public List<RatDisDetailHisVO> queryDisHisListByDisCode(String disCode) {
        return ratDisHisDao.queryDisHisListByDisCode(disCode);
    }
    /**
     * 根据授信分配编号获取授信分配明细
     */
    @Transactional(readOnly = true)
    public List<RatBkRationDtlVm> getCprationDtlByDisCodes(List<String> disCodes) {
        return ratDisHisDao.getCprationDtlByDisCodes(disCodes);
    }
    /**
     * 根据授信编号获取授信分配明细
     */
    @Transactional(readOnly = true)
    public List<RatBkrationDisDtlDTO> getCprationDtlByRatCodes(List<String> ratCodes) {
        return ratDisHisDao.getCprationDtlByRatCodes(ratCodes);
    }
    /**
     * 删除分配历史明细
     */
    public void deleteDisDtlHisByDisCode(RatBkrationDisListDTO disDTO) {
        ratDisHisDao.deleteDisDtlHisByDisCode(disDTO);
    }
    /**
     * 查询审批通过的分配明细
     */
    public List<RatBkrationDisDetail> queryDisDetailByStatus(String disCode, Integer status) {
        return ratDisHisDao.queryDisDetailByStatus(disCode, status);
    }
    /**
     * 查询同一单位审批通过的分配明细
     */
    public List<RatBkrationDisDtlDTO> queryDisDetailsPass(List<String> ratCode, String corpId) {
        return ratDisHisDao.queryDisDetailsPass(ratCode, corpId);
    }
    /**
     * 根据授信品种，分配编号查询分配待审批额度
     */
    public RatDisDetailHisVO queryDisDetailApprove(String ratCode, String itemCode) {
        return ratDisHisDao.queryDisDetailApprove(ratCode, itemCode);
    }
    @Override
    public BaseDao<RatDisDetailHis, String> getEntityDao() {
        // TODO Auto-generated method stub
        return ratDisHisDao;
    }
}
