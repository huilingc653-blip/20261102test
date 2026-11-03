package com.bytter.credit.domain.service;
import com.bytter.credit.domain.dao.RatBkrationAdjustDtlDao;
import com.bytter.credit.domain.entity.RatBkrationAdjustDetail;
import com.bytter.credit.domain.vo.RatBkrationAdjustDtlVO;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.List;
/**
 * 授信调整明细服务层
 * 
 * @author luxy Title: RatBkrationAdjustDtlService
 */
@Service("ratBkrationAdjustDtlService")
public class RatBkrationAdjustDtlService extends BaseService<RatBkrationAdjustDetail, String> {
    @Autowired
    private RatBkrationAdjustDtlDao bkrationAdjustDtlDao;
    /**
     * 保存授信调整明细
     * 
     * @param detail
     *            授信调整明细对象
     */
    public void saveRatBkAdjustDetail(RatBkrationAdjustDetail detail) {
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
        super.saveEntity(detail);
        this.getEntityDao().flush();
    }
    /**
     * 根据adjustCode删除授信调整明细
     * 
     * @param adjustCode
     *            授信调整主表id
     */
    public void deleteAdjustDtl(String adjustCode) {
        bkrationAdjustDtlDao.deleteBkAdjustDtl(adjustCode);
    }
    /**
     * 根据adjustCode查找授信调整明细
     * 
     * @param adjustCode
     *            授信调整主表id
     * @return 授信调整明细集合
     */
    public List<RatBkrationAdjustDtlVO> queryAdjustDtlBy(String adjustCode) {
        return bkrationAdjustDtlDao.getRatBkAdjustDtl(adjustCode);
    }

    /**
     * 根据adjustCode查询授信录入最高额度调整明细
     *
     * @param adjustCode 授信调整主表id
     * @return 授信录入最高额度调整明细集合
     */
    public List<RatBkrationAdjustDtlVO> queryInputAdjustDtl(String adjustCode){
        return bkrationAdjustDtlDao.queryInputAdjustDtl(adjustCode);
    }

    /**
     * 根据adjustCode查询授信分配最高额度调整明细
     *
     * @param adjustCode 授信调整主表id
     * @return 授信录入最高额度调整明细集合
     */
    public List<RatBkrationAdjustDtlVO> queryDisAdjustDtl(String adjustCode){
        return bkrationAdjustDtlDao.queryDisAdjustDtl(adjustCode);
    }

    @Override
    public BaseDao<RatBkrationAdjustDetail, String> getEntityDao() {
        return bkrationAdjustDtlDao;
    }
}
