package com.bytter.system.service;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.core.constant.CommonConstant;
import com.bytter.core.utils.StringUtils;
import com.bytter.system.dao.ModuleDao;
import com.bytter.system.entity.Module;
/**
 * Created by zhoul on 2017/4/12.
 */
@Service("moduleService")
@Scope(proxyMode = ScopedProxyMode.TARGET_CLASS)
public class ModuleService extends BaseService<Module, String> {
    @Autowired
    private ModuleDao dao;
    @Override
    public BaseDao<Module, String> getEntityDao() {
        return dao;
    }
    public void save(Module module) {
        if (module.getSort() == null) {
            module.setSort(500);
        }
        saveEntity(module);
    }
    public void update(Module module) {
        if (module.getSort() == null) {
            module.setSort(500);
        }
        updateEntity(module);
    }
    public void delete(String id) {
        dao.delete(id);
    }
    /**
     * 获取所有的菜单，排序为树形顺序机构
     *
     * @return
     */
    public List<Module> getTreeTable() {
        return listToTreeTable(dao.listAll());
    }
    @Transactional
    public void assign(Module module, String menuIds) {
        //将游离态的role转换成持久化状态
        dao.removeModuleMenuRelation(module.getId());
        dao.flush();
        if (StringUtils.isNotEmpty(menuIds)) {
            Set<String> moduleMenuSet = new HashSet<>();
            //传入的值包含英文状态下的逗号，保存单个或者一个菜单
            List<String> strings = Arrays.asList(menuIds.split(CommonConstant.COMMA_SPLIT));
            moduleMenuSet.addAll(strings);
            dao.saveModuleMenu(module.getId(), moduleMenuSet);
        }
    }
    public List<String> listModuleMenuByModuleId(String moduleId) {
        return dao.listModuleMenuByModuleId(moduleId);
    }
}
