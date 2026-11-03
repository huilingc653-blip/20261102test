package com.bytter.report.domain.cms.service;
import com.bytter.report.domain.cms.dao.BiRemainTermDao;
import com.bytter.report.domain.cms.entity.BiRemainTerm;
import com.bytter.report.domain.cms.vo.BiRemainTermDetailVO;
import com.bytter.report.domain.cms.vo.BiRemainTermVO;
import com.bytter.report.api.enums.BiRemainTermMessageEnum;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.exception.BusinessException;
import com.bytter.core.utils.BeanUtils;
import com.bytter.core.utils.DateUtils;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.core.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.ArrayList;
import java.util.List;
/**
 * @ClassName CmsRemainTermService
 * @Description 剩余期限维护service
 * @Author fusb
 * @Date 2020/1/9 10:41
 * @Version 1.0
 **/
@Service("biRemainTermService")
@Transactional
public class BiRemainTermService extends BaseService<BiRemainTerm,String> {
    @Autowired
    BiRemainTermDao biRemainTermDao;
    @Autowired
    BiRemainTermDtlService biRemainTermDtlService;
    /**
    *@Description 剩余期限维护列表数据
    *@Param input 查询条件
    *@return com.bytter.core.common.datatable.DataTablesOutput
    *@Date 2020/1/9 10:44
    *@Author fusb
    */
    @Transactional(readOnly = true)
    public DataTablesOutput remainTermTables(DataTablesInput input) {
        DataTablesOutput output = biRemainTermDao.remainTermTables(input);
        output.setData(this.setFormulas(output.getData()));
        return output;
    }
    /**
    *@Description 查询有效的剩余期限信息
    *@Param
    *@return java.util.List<com.bytter.biz.cms.basedata.vo.BiRemainTermVO>
    *@Date 2020/1/15 14:51
    *@Author fusb
    */
    public List<BiRemainTermVO> getRemainTermList(){
        return  this.setFormulas(biRemainTermDao.getRemainTermList());
    }
    /**
    *@Description 保存剩余期限维护数据信息
    *@Param vo
    *@return void
    *@Date 2020/1/10 17:08
    *@Author fusb
    */
    public void remainTermSubmit(BiRemainTermVO vo, ICurrentUser user){
        this.checkData(vo);
        BiRemainTerm entity = new BiRemainTerm();
        BeanUtils.copyNotNullProperties(vo,entity);
        if (StringUtils.isEmpty(vo.getId())) { // 新增
            // 获取最大排序值
            entity.setSort(biRemainTermDao.getRemainTermSort());
            entity.setCreateBy(user.getId());
            entity.setCreateDate(DateUtils.getDate());
        } else { // 修改
            entity.setUpdateBy(user.getId());
            entity.setCreateDate(DateUtils.getDate());
        }
        // 保存主表信息
        this.saveOrUpdate(entity);
        // 保存子表信息
        biRemainTermDtlService.saveBiRemainTermDtl(vo,entity.getId());
    }
    /**
    *@Description 数据校验
    *@Param vo
    *@return void
    *@Date 2020/1/10 17:07
    *@Author fusb
    */
    public void checkData(BiRemainTermVO vo){
        BiRemainTermVO termVO = getBiRemainTermVOByTermName(vo.getTermName());
        if (null != termVO) {
            if (!termVO.getId().equals(vo.getId()) && termVO.getTermName().equals(vo.getTermName())) {
                throw new BusinessException(BiRemainTermMessageEnum.TERM_NAME_REPEAT);
            }
        }
    }
    /**
    *@Description 根据期限名称查询剩余期限维护信息
    *@Param termName
    *@return com.bytter.biz.cms.basedata.vo.BiRemainTermVO
    *@Date 2020/1/10 16:42
    *@Author fusb
    */
    @Transactional(readOnly = true)
    public BiRemainTermVO getBiRemainTermVOByTermName(String termName){
        BiRemainTermVO termVO = biRemainTermDao.getBiRemainTermVOByTermName(termName);
        if (null != termVO) {
            List<BiRemainTermVO> termVOList = new ArrayList<BiRemainTermVO>(1);
            termVOList.add(termVO);
            return this.setFormulas(termVOList).get(0);
        } else {
            return termVO;
        }
    }
    /**
    *@Description 根据id查询剩余期限维护信息
    *@Param id
    *@return com.bytter.biz.cms.basedata.vo.BiRemainTermVO
    *@Date 2020/1/11 12:12
    *@Author fusb
    */
    @Transactional(readOnly = true)
    public BiRemainTermVO getBiRemainTermVOById(String id) {
        BiRemainTermVO termVO = biRemainTermDao.getBiRemainTermVOById(id);
        List<BiRemainTermVO> termVOList = new ArrayList<BiRemainTermVO>(1);
        termVOList.add(termVO);
        return this.setFormulas(termVOList).get(0);
    }
    /**
    *@Description 重新设置BiRemainTermVO的公式
    *@Param termVOList
    *@return java.util.List<com.bytter.biz.cms.basedata.vo.BiRemainTermVO>
    *@Date 2020/1/11 12:43
    *@Author fusb
    */
    public List<BiRemainTermVO> setFormulas(List<BiRemainTermVO> termVOList) {
        if (EmptyUtils.isNotEmpty(termVOList)) {
           for (int i = 0;i < termVOList.size();i++) {
               List<BiRemainTermDetailVO> termDetails = biRemainTermDtlService.getBiRemainTermDtlVOByTermId(termVOList.get(i).getId());
               String formulas = "";
               for (BiRemainTermDetailVO dtl:termDetails) {
                    formulas += dtl.getField1Name()+dtl.getSymbolName()+dtl.getField2Name()+dtl.getConditionName()+dtl.getDays()+dtl.getUnitName();
                    if (termDetails.size()>1) {
                        if (StringUtils.isNotEmpty(dtl.getLogicalRelationship())) {
                            formulas += " " + dtl.getLogicalRelationshipName() + " ";
                        }
                    }
               }
               termVOList.get(i).setFormulas(formulas);
               termVOList.get(i).setRemainTermDtl(termDetails);
           }
        }
        return termVOList;
    }
    /**
    *@Description 删除剩余期限维护信息
    *@Param id
    *@return void
    *@Date 2020/1/11 15:06
    *@Author fusb
    */
    public void deleteBiRemainTerm(String id){
        biRemainTermDao.deleteRemainTerm(id);
        biRemainTermDtlService.deleteBiRemainTermDetail(id);
    }
    @Override
    public BaseDao getEntityDao() {
        return biRemainTermDao;
    }
}
