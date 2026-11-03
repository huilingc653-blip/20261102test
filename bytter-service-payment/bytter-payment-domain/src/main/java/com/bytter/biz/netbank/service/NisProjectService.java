package com.bytter.biz.netbank.service;
import com.bytter.biz.netbank.dao.NisProjectDao;
import com.bytter.biz.netbank.entity.NisProject;
import com.bytter.biz.netbank.vo.NisProjectVO;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.system.api.IDictService;
import com.bytter.system.api.Ii18nService;
import com.bytter.system.api.dto.I18nDTO;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
/**
 * @author shenc
 * @version 1.0
 * @Title
 * @Description:
 * @date 2018/5/14 16:05
 */
@Service("nisProjectService")
@Transactional
public class NisProjectService extends BaseService<NisProject,String> {
    @Autowired
    private NisProjectDao nisProjectDao;
    @DubboReference(check = false)
    private Ii18nService ii18nService;
    @DubboReference(check = false)
    private IDictService dictService;
    @Override
    public BaseDao<NisProject, String> getEntityDao() {
        return nisProjectDao;
    }
    public DataTablesOutput<NisProjectVO> getDataTablesUnion(DataTablesInput input){
        DataTablesOutput<NisProjectVO> output= this.nisProjectDao.getDataTablesUnion(input);
        List<NisProjectVO> list=output.getData();
        for(NisProjectVO vo:list) {
            vo.setProjectCdSignName(dictService.get("btBankAccDtl.cdSign", vo.getProjectCdSign()));
            vo.setMatchAttrName(dictService.get("nisProject.matchAttr", vo.getMatchAttr()));
            vo.setMatchRelationName(dictService.get("nisProject.matchRelation",vo.getMatchRelation()));
        }
        return output;
    }
    /**
     * 删除对象
     * @param entity
     */
    @Override
    public void deleteEntity(NisProject entity) {
        super.deleteEntity(entity);
        //删除多语言
        ii18nService.deleteBusinessBaseContext(entity.getId());
    }
    /**
     * 根据ID删除
     * @param id
     */
    @Override
    public void delete(String id) {
        super.delete(id);
    }
    /**
     * 保存对象
     * @param entity
     */
    public void save(NisProject entity,String locale){
        super.saveEntity(entity);
        //新增当前语种
        I18nDTO i18nDTO = new I18nDTO();
        i18nDTO.setKeyId(entity.getId());
        i18nDTO.setLocale(locale);
        i18nDTO.setContext(entity.getProjectName());
        ii18nService.saveOrUpdateBusinessBaseContext(i18nDTO);
    }
    /**
     * 更新对象
     * @param entity
     */
    public void update(NisProject entity){
        super.updateEntity(entity);
    }
    /**
     * 保存or更新
     * @param entity
     */
    public void saveOrUpdate(NisProject entity,String locale) {
        super.merge(entity);
        //新增当前语种
        I18nDTO i18nDTO = new I18nDTO();
        i18nDTO.setKeyId(entity.getId());
        i18nDTO.setLocale(locale);
        i18nDTO.setContext(entity.getProjectName());
        ii18nService.saveOrUpdateBusinessBaseContext(i18nDTO);
    }
    /**
     * 根据项目编码获取项目实体
     * @param code
     * @return
     */
    public NisProject findProjectCode(String code){
        return nisProjectDao.findProjectCode(code);
    }
    public NisProjectVO getByUnion(String id){
        return this.nisProjectDao.getByUnion(id);
    }
    public NisProject getById(String id){
        return nisProjectDao.findByid(id);
    }
    /**
     * 删除项目实体
     * @param entity
     */
    public void deleteNisProject(NisProject entity){
        nisProjectDao.deleteById(entity.getId());
        //删除多语言
        ii18nService.deleteBusinessBaseContext(entity.getId());
    }
    /**
     * 获取所有的项目
     */
    public List<NisProject> getListAll(){
        return listAll("projectCode",true);
    }
    /**
     * 获取所有的项目
     */
    public List<NisProject> listNisProjectByCdSign(String projectCdSign){
		return this.nisProjectDao.listNisProjectByCdSign(projectCdSign);
    }
}
