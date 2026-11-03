package com.bytter.report.domain.cms.service;
import com.bytter.report.domain.cms.dao.BiRemainTermDtlDao;
import com.bytter.report.domain.cms.entity.BiRemainTermDetail;
import com.bytter.report.domain.cms.vo.BiRemainTermDetailVO;
import com.bytter.report.domain.cms.vo.BiRemainTermVO;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.core.utils.BeanUtils;
import com.bytter.core.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.ArrayList;
import java.util.List;
/**
 * @ClassName BiRemainTermDtlService
 * @Description TODO
 * @Author fusb
 * @Date 2020/1/10 17:40
 * @Version 1.0
 **/
@Service("biRemainTermDtlService")
@Transactional
public class BiRemainTermDtlService extends BaseService<BiRemainTermDetail,String> {
    @Autowired
    BiRemainTermDtlDao biRemainTermDtlDao;
    /**
     *@Description 保存剩余期限维护子表信息
     *@Param vo
     *@return void
     *@Date 2020/1/10 17:00
     *@Author fusb
     */
    public void saveBiRemainTermDtl(BiRemainTermVO vo, String remainTermId) {
        List<BiRemainTermDetailVO> termDetails = vo.getRemainTermDtl();
        List<BiRemainTermDetail> entitys = new ArrayList<BiRemainTermDetail>(2);
        for (int i = 0;i < termDetails.size();i++) {
            if (StringUtils.isNotEmpty(termDetails.get(i).getConditions())) {
                BiRemainTermDetail entity = new BiRemainTermDetail();
                BeanUtils.copyNotNullProperties(termDetails.get(i),entity);
                entity.setRemainTermId(remainTermId);
                entitys.add(entity);
            }
        }
        biRemainTermDtlDao.deleteRemainTermDtl(remainTermId);
        this.batchSave(entitys);
    }
    /**
    *@Description 根据剩余期限维护主表id查询子表信息
    *@Param remainTermId
    *@return java.util.List<com.bytter.biz.cms.basedata.entity.BiRemainTermDetail>
    *@Date 2020/1/11 15:09
    *@Author fusb
    */
    @Transactional(readOnly = true)
    public List<BiRemainTermDetail> getBiRemainTermDtlByTermId(String remainTermId){
        return biRemainTermDtlDao.getBiRemainTermDtlByTermId(remainTermId);
    }
    /**
     *@Description 根据剩余期限维护主表id查询子表信息VO
     *@Param remainTermId
     *@return java.util.List<com.bytter.biz.cms.basedata.vo.BiRemainTermDetailVO>
     *@Date 2020/1/11 15:09
     *@Author fusb
     */
    @Transactional(readOnly = true)
    public List<BiRemainTermDetailVO> getBiRemainTermDtlVOByTermId(String remainTermId){
        return biRemainTermDtlDao.getBiRemainTermDtlVOByTermId(remainTermId);
    }
    /**
    *@Description 删除剩余期限维护子表信息
    *@Param remainTermId
    *@return void
    *@Date 2020/1/11 15:11
    *@Author fusb
    */
    public void deleteBiRemainTermDetail(String remainTermId) {
        biRemainTermDtlDao.deleteRemainTermDtl(remainTermId);
    }
    @Override
    public BaseDao<BiRemainTermDetail, String> getEntityDao() {
        return biRemainTermDtlDao;
    }
}
