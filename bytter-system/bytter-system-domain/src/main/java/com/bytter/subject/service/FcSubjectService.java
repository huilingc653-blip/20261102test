package com.bytter.subject.service;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.core.common.exception.ParameterException;
import com.bytter.core.constant.SubjectConstant;
import com.bytter.core.utils.BeanUtils;
import com.bytter.core.utils.DateUtils;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.core.utils.StringUtils;
import com.bytter.subject.constant.FinacialConstant;
import com.bytter.subject.constant.OuterFinacialConstant;
import com.bytter.subject.dao.FcSubjectDao;
import com.bytter.subject.entity.FcSubject;
import com.bytter.subject.entity.FcSubjectInit;
import com.bytter.system.api.dto.FcInstrateDTO;
import com.bytter.subject.vo.FcSubjectInitVO;
import com.bytter.subject.vo.FcSubjectSortVo;
import com.bytter.subject.vo.FcSubjectVo;
import com.bytter.system.api.IDictService;
import com.bytter.system.api.IFcAccService;
import com.bytter.system.api.Ii18nService;
import com.bytter.system.api.dto.DictDTO;
import com.bytter.system.api.dto.FcSubjectDTO;
import com.bytter.system.api.enums.ParamConfigMessageEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
/**
 * @author 吕伟林
 * @version V1.0
 * @Title: Controller
 * @Description: 会计科目服务
 * @date 2017-05-12 14:18:39
 * TODO
 */
@Service("fcSubjectService")
@Transactional
public class FcSubjectService extends BaseService<FcSubject, String> {
    @Autowired
    private FcSubjectDao fcSubjectDao;
    @Autowired
    private IDictService dictServiceI;
    @Autowired
    private IFcAccService fcAccServiceI;
    @Autowired
    private Ii18nService i18nServiceI;
    /**
     * 功能介绍 : 获取树形结构的会计科目 ,会将 <b>科目类型<b> 加入树形<br>
     *
     * @param sub 查询条件
     * @return <b>日期：</b>2017-5-27<br>
     * @version 1.0
     * @author lvwl
     */
    public List<FcSubjectVo> listTreeData(FcSubject sub) {
        List<FcSubjectVo> paran = new ArrayList<FcSubjectVo>();
        List<FcSubjectVo> list = fcSubjectDao.listAllOrder(sub);
        //空查询条件 增加科目类别在列表
        if (StringUtils.isEmpty(sub.getSubjectCode()) && StringUtils.isEmpty(sub.getSubjectName()) && StringUtils.isBlank(sub.getSubjectSort())) {
            //增加科目类别显示在列表
            /*if(EmptyUtils.isEmpty(dictService)){
				dictServiceImpl = ApplicationContextUtil.getContext().getBean(IDictService.class);
			}*/
            //获取字典表 科目类型
            for (DictDTO bean : dictServiceI.list("Admin.SubjectSort")) {
                FcSubjectVo fs = new FcSubjectVo();
                fs.setId(bean.getCode());
                fs.setSubjectName(bean.getName());
                fs.setFatherId("");
                fs.setSubjectSort(bean.getCode());
                paran.add(fs);
            }
        }
        paran.addAll(list);
        return getTree(paran, null);
    }
    /**
     * 功能介绍 : 获取树形数据，父ID未查询到默认首级<br>
     *
     * @param list
     * @param fs
     * @return <b>日期：</b>2017-5-27<br>
     * @version 1.0
     * @author lvwl
     */
    public List<FcSubjectVo> getTree(List<FcSubjectVo> list, FcSubjectVo fs) {
        List<FcSubjectVo> treeList = new ArrayList<FcSubjectVo>();
        for (FcSubjectVo sub : list) {
            String parentId = sub.getParentId();
            List<FcSubjectVo> childrens = new ArrayList<FcSubjectVo>();
            //(EmptyUtils.isEmpty(fs) && EmptyUtils.isEmpty(parentId))正常 的从根目录开始  过滤了 有父Id 却没有父节点的数据
            if ((EmptyUtils.isEmpty(fs)) || (!EmptyUtils.isEmpty(fs) && EmptyUtils.isNotEmpty(parentId) && fs.getId().equals(parentId))) {
                //补充上有父ID去没有父节点的数据
                if (EmptyUtils.isEmpty(fs) && EmptyUtils.isNotEmpty(parentId)) {
                    if (checkParentNode(list, parentId)) {
                        continue;
                    }
                }
                childrens = getTree(list, sub);
                treeList.add(sub);
                if (!EmptyUtils.isEmptyList(childrens)) {
                    treeList.addAll(childrens);
                }
            }
        }
        return treeList;
    }
    /**
     * 功能介绍 : 判断集合list元素id<br>
     *
     * @param list
     * @param id
     * @return 存在 true <br>不存在 false
     * <b>日期：</b>2017-5-27<br>
     * @version 1.0
     * @author lvwl
     */
    public boolean checkParentNode(List<FcSubjectVo> list, String id) {
        for (FcSubjectVo fcSubject : list) {
            if (fcSubject.getId().equals(id)) {
                return true;
            }
        }
        return false;
    }
    /**
     * 删除对象
     *
     * @param entity
     */
    public void deleteEntity(FcSubject entity) {
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
    public void save(FcSubject entity, String userId) {
        entity.setCreateBy(userId);
        entity.setValidSign("1");
        entity.setIsCashItem(new Integer(0));
        entity.setCreateTime(new Date());
        super.saveEntity(entity);
    }
    /**
     * 更新
     *
     * @param entity
     */
    public void update(FcSubject entity, String userId) {
        entity.setUpdateBy(userId);
        entity.setUpdateTime(new Date());
        super.updateEntity(entity);
    }
    /**
     * 保存or更新
     *
     * @param entity
     */
    public void saveOrUpdate(FcSubject entity, String userId) {
        if (EmptyUtils.isEmpty(entity.getId())) {
            entity.setCreateBy(userId);
            entity.setCreateTime(new Date());
        } else {
            entity.setUpdateBy(userId);
            entity.setUpdateTime(new Date());
        }
        super.saveOrUpdate(entity);
    }
    /**
     * 更新,传入的是FcSubjectDTO
     *
     * @param fcSubjectDTO
     */
    public void update(FcSubjectDTO fcSubjectDTO, String loginName) {
        this.getSession().clear();
        FcSubject entity = new FcSubject();
        BeanUtils.copyNotNullProperties(fcSubjectDTO, entity);
        entity.setCreateBy(loginName);
        entity.setCreateTime(new Date());
        super.updateEntity(entity);
    }
    /**
     * 功能介绍 : 获取系统科目<br>
     *
     * @return <b>日期：</b>2017-5-27<br>
     * @version 1.0
     * @author lvwl
     */
    public List<FcSubjectSortVo> listSysSubject() {
        return fcSubjectDao.listSysSubject();
    }
    /**
     * 功能介绍 : 停用科目<br>
     *
     * @param id <b>日期：</b>2017-5-27<br>
     * @version 1.0
     * @author lvwl
     */
    public void validSubject(String id) {
        FcSubject fcSubject = get(id);
        boolean falg = fcAccServiceI.isExsits(fcSubject.getId());
        if (falg) {
            throw new ParameterException(ParamConfigMessageEnum.FIN_PARAMCONFIG_MESSAGE_INSTRATE_ISEMPLOY);
        } else {
            fcSubject.setValidSign(FinacialConstant.VALID_FALSE);
            saveOrUpdate(fcSubject);
            i18nServiceI.deleteBusinessBaseContext(fcSubject.getId());
        }
    }
    /**
     * 功能介绍 : 根据条件获取有效的会计科目<br>
     *
     * @param fs
     * @return <b>日期：</b>2017-5-27<br>
     * @version 1.0
     * @author lvwl
     */
    public List<FcSubjectVo> listValidSubject(FcSubject fs) {
        return fcSubjectDao.listValidSubject(fs);
    }
    public List<FcSubjectDTO> findSubjectByAccType(String accType, String valid) {
        Date date = DateUtils.getDate();//开户日期
        List<FcSubjectDTO> list = fcSubjectDao.findSubjectByAccType(accType, valid);
        if (StringUtils.isNotEmpty(list)) {
            for (FcSubjectDTO fc : list) {
                FcInstrateDTO fcInstrateDTO = fcSubjectDao.getExistInstrate(fc.getId(), date);
                if (StringUtils.isNotEmpty(fcInstrateDTO)) {
                    fc.setInterestRate(fcInstrateDTO.getInterestRate());
                }
            }
        }
        return list;
    }
    /**
     * 查询全部的有效会计科目 树形结构
     *
     * @param sort 是否添加科目类型  1添加 其他不添加
     * @return
     */
    public List<FcSubjectDTO> queryAllSubject(String sort) {
        List<FcSubjectDTO> paran = new ArrayList<FcSubjectDTO>();
        List<FcSubjectDTO> list = fcSubjectDao.queryAllSubject("1");
        if ("1".equals(sort)) {
            //获取字典表 科目类型
            for (DictDTO bean : dictServiceI.list("Admin.SubjectSort")) {
                FcSubjectDTO fs = new FcSubjectDTO();
                fs.setId(bean.getCode());
                fs.setSubjectName(bean.getName());
                fs.setFatherId("");
                fs.setSubjectSort(bean.getCode());
                paran.add(fs);
            }
        }
        paran.addAll(list);
        return paran;
    }
    /**
     * 功能介绍 : 获取系统准备的<b>初始化系统科目<b><br>
     *
     * @return <b>日期：</b>2017-5-27<br>
     * @version 1.0
     * @author lvwl
     */
    public List<FcSubjectInit> listFcSubjectInit() {
        return fcSubjectDao.listFcSubjectInit();
    }
    /**
     * 功能介绍 : 获取系统准备的<b>初始化系统科目<b>包括外键对象<br>
     *
     * @return <b>日期：</b>2017-5-27<br>
     * @version 1.0
     * @author lvwl
     */
    public List<FcSubjectInit> listFcSubjectInitFetch() {
        return fcSubjectDao.listFcSubjectInitFetch();
    }
    /**
     * 功能介绍 : 查看可初始化科目。<br>
     * <b>日期：</b>2017年11月20日<br>
     *
     * @version 1.0
     * @author liyh
     */
    public List<FcSubjectInitVO> listAllInitSubject() {
        return fcSubjectDao.listAllInitSubject();
    }
    /**
     * 功能介绍 : 导入系统提供的会计科目<br>
     * <b>日期：</b>2017-5-27<br>
     *
     * @version 1.0
     * @author lvwl
     */
    @SuppressWarnings("unused")
    public void importSubjectInit() {
        //会计科被帐号引用 不能删除数据
         /*List<FcSubjectInit> allInitData = fcSubjectDao.listFcSubjectInit();
 		List<FcSubject> fsList = BeanUtils.copyListProperties(allInitData, FcSubject.class);
 		saveInit(fsList,null);*/
        fcSubjectDao.importInitSubject();
    }
    /**
     * 功能介绍 : 初始化科目保存树形<br>
     *
     * @param list
     * @param fs   <b>日期：</b>2017-5-27<br>
     * @version 1.0
     * @author lvwl
     */
    public void saveInit(List<FcSubject> list, FcSubject fs, String userId) {
        for (FcSubject fcSubject : list) {
            String code = fcSubject.getSubjectCode();
            if ((EmptyUtils.isEmpty(fs) && code.length() == 4) || (!EmptyUtils.isEmpty(fs) && checkPaternity(fs.getSubjectCode(), code))) {
                if (EmptyUtils.isEmpty(fs)) {
                    //开始节点 设置科目类型为父ID
                    fcSubject.setFatherId(fcSubject.getSubjectSort());
                } else {
                    fcSubject.setFatherId(fs.getId());
                }
                if (SubjectConstant.SubjectSort.SUBJECT_SORT_G.get().equals(fcSubject.getSubjectSort())) {//表外才设置计量单位
                    fcSubject.setMeasureUnit(OuterFinacialConstant.OuterMeasureUnit.UNIT_MONEY.get());
                }
                FcSubject sub = fcSubjectDao.getSubjectByCode(code);
                if (EmptyUtils.isEmpty(sub)) {//code不存在
                    save(fcSubject, userId);
                    saveInit(list, fcSubject, userId);//保存子集
                } else {
                    saveInit(list, sub, userId);//保存子集
                }
            }
        }
    }
    /**
     * 功能介绍 : 判断father与child的父子关系成立<br>
     *
     * @param father 父节点ID
     * @param child  子节点ID
     * @return 成立：ture , 不成立：false
     * <b>日期：</b>2017-5-27<br>
     * @version 1.0
     * @author lvwl
     */
    public boolean checkPaternity(String father, String child) {
        //子节点ID中最前面为父ID              子ID在父ID基础上加两位
        if (child.indexOf(father) == 0 && child.substring(father.length()).length() == 2) {
            return true;
        }
        return false;
    }
    /**
     * 根据科目类型查询id的全部子集包含自己
     *
     * @param id
     */
    public List<String> getSubsetBySubjectSort(String id) {
        FcSubject sub = this.get(id);
        return getSubsetBySubjectSort(id, sub.getSubjectSort(), true);
    }
    /**
     * 根据科目类型查询id的全部子集包含自己
     *
     * @param id
     * @param subjectSort 科目类型
     */
    public List<String> getSubsetBySubjectSort(String id, String subjectSort) {
        return getSubsetBySubjectSort(id, subjectSort, true);
    }
    /**
     * 根据科目类型查询id的全部子集
     *
     * @param id
     * @param subjectSort 科目类型
     * @param sign        是否包含自己
     */
    public List<String> getSubsetBySubjectSort(String id, String subjectSort, boolean sign) {
        List<String> result = this.getFatherId(id, fcSubjectDao.listSubjectBySort(subjectSort));
        if (sign) {
            result.add(id);
        }
        return result;
    }
    public List<String> getFatherId(String id, List<FcSubject> list) {
        List<String> result = new ArrayList<String>();
        if (EmptyUtils.isEmpty(list) || EmptyUtils.isEmpty(id)) {
            return result;
        }
        for (FcSubject sub : list) {
            String fatherId = sub.getFatherId();
            if (EmptyUtils.isEmpty(fatherId)) {
                continue;
            }
            if (id.equals(fatherId)) {
                result.add(sub.getId());
                result.addAll(this.getFatherId(sub.getId(), list));
            }
        }
        return result;
    }
    public FcSubjectDTO findSubjectById(String id) {
        return fcSubjectDao.findSubjectById(id);
    }
    /******************    验证 begin  ******************/
    /**
     * 功能介绍 : 添加或修改科目验证<br>
     *
     * @param entity <b>日期：</b>2017-5-27<br>
     * @version 1.0
     * @author lvwl
     */
    public void checkAddSubject(FcSubject entity) {
        if (EmptyUtils.isNotEmpty(entity.getSysSubjectCode())) {
            entity.setSysSubjectCode(entity.getSysSubjectCode().trim());
        }
        if (EmptyUtils.isEmpty(entity.getId())) {
            if (EmptyUtils.isEmpty(entity.getSubjectCode())) {
                throw new ParameterException(ParamConfigMessageEnum.FIN_PARAMCONFIG_FCSUBJECT_SUBJECTCODE_NULL);
            } else if (!fcSubjectDao.checkSubjectCode(entity.getSubjectCode())) {
                throw new ParameterException(ParamConfigMessageEnum.FIN_PARAMCONFIG_FCSUBJECT_SUBJECTCODE_REPEAT);
            }
        } else {//修改
            if (!fcSubjectDao.checkSubjectCode(entity.getId(), entity.getSubjectCode())) {
                throw new ParameterException(ParamConfigMessageEnum.FIN_PARAMCONFIG_FCSUBJECT_SUBJECTCODE_REPEAT);
            }
        }
        if (EmptyUtils.isEmpty(entity.getSubjectName())) {
            throw new ParameterException(ParamConfigMessageEnum.FIN_PARAMCONFIG_FCSUBJECT_SUBJECTNAME_NULL);
        }
        if (EmptyUtils.isEmpty(entity.getBalanceWay())) {
            throw new ParameterException(ParamConfigMessageEnum.FIN_PARAMCONFIG_FCSUBJECT_BALANCEWAY_NULL);
        }
        if (EmptyUtils.isEmpty(entity.getInterestSign())) {
            throw new ParameterException(ParamConfigMessageEnum.FIN_PARAMCONFIG_FCSUBJECT_INTERESTSIGN_NULL);
        }
        if (EmptyUtils.isEmpty(entity.getAccType())) {
            throw new ParameterException(ParamConfigMessageEnum.FIN_PARAMCONFIG_FCSUBJECT_ACCTYPE_NULL);
        }
    }
    /**
     * 功能介绍 : 删除科目验证<br>
     *
     * @param entity <b>日期：</b>2017-5-27<br>
     * @version 1.0
     * @author lvwl
     */
    public void checkDelSubject(FcSubject entity) {
        //判断是否被 账户 引用
        //TODO
        if (false) {
            throw new ParameterException(ParamConfigMessageEnum.FIN_PARAMCONFIG_FCSUBJECT_SUBJECTCODE_USE);
        }
        //判断是否  子科目存在
        if (!fcSubjectDao.checkChildNode(entity.getId())) {
            throw new ParameterException(ParamConfigMessageEnum.FIN_PARAMCONFIG_FCSUBJECT_SUBJECTCODE_NODE);
        }
    }
    /**
     * 获取一级会计科目
     *
     * @return
     */
    public List<FcSubjectDTO> findSubjectByFatherId() {
        return fcSubjectDao.findSubjectByFatherId();
    }
    /******************    验证 end  ******************/
    @Override
    public BaseDao<FcSubject, String> getEntityDao() {
        return fcSubjectDao;
    }
    public List<FcSubjectDTO> getSubjectNameAndIdByIds(List<String> ids){
        return fcSubjectDao.getSubjectNameAndIdByIds(ids);
    }
    public List<FcSubjectDTO> getSubjectByIds(List<String> ids) {
        return fcSubjectDao.getSubjectByIds(ids);
    }
    /**
     * 根据 subjectsCodes , 查询 fc_subject 表，返回 subjectName; k = subjectCode, v = subjectName
     * @param subjectCodes
     * @return
     */
    public Map<String, String> getSubjectNameBySubjectCode(List<String> subjectCodes) {
        List<FcSubjectDTO> lists = fcSubjectDao.getSubjectNameBySubjectCode(subjectCodes);
        return lists.stream().collect(Collectors.toMap(FcSubjectDTO::getSubjectCode,FcSubjectDTO::getSubjectName));
    }
    public String getSubjectNameById(String id) {
        return fcSubjectDao.getSubjectNameById(id);
    }
}
