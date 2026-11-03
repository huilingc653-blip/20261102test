package com.bytter.bill.domain.billPool.service;
import com.bytter.bill.domain.billPool.dao.AbPoolBusTypeDao;
import com.bytter.bill.domain.billPool.entity.AbPoolBusType;
import com.bytter.bill.api.billPool.constant.AbPoolConstant;
import com.bytter.bill.api.billPool.enums.AbPoolMessageEnum;
import com.bytter.bill.domain.billPool.vo.AbPoolBusTypeVO;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.core.common.exception.BusinessException;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.utils.BeanUtils;
import com.bytter.core.utils.ListUtils;
import com.bytter.core.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.*;
/**
 * 
* @Title:  AbPoolBusTypeService.java   
* @Package com.bytter.biz.billPool.service   
* @Description:    TODO   
* @author: Jinyong.cao   
* @date:   2020-1-02 下午19:33:07 
* @version V1.0
 */
@Service("abPoolBusTypeService")
@Transactional
public class AbPoolBusTypeService extends BaseService<AbPoolBusType, String>{
	@Autowired
	private AbPoolBusTypeDao abPoolBusTypeDao;
	
	@Override
    public BaseDao<AbPoolBusType, String> getEntityDao() {
        return abPoolBusTypeDao;
    }
	
	@Transactional(readOnly = true)
    public List<AbPoolBusTypeVO> listAbPoolBusTypeVO(AbPoolBusTypeVO queryVo){
        // 查询数据
        List<AbPoolBusTypeVO> list = abPoolBusTypeDao.listBusType(queryVo);
        // 递归过滤拼接控件数据
//        List<AbPoolBusTypeVO> objList = ListUtils.isNotNullOrEmpty(list)?BusTypeUtil.splicingStructure(list):null;
        List<AbPoolBusTypeVO> treeList = this.getListTree(list);
        return treeList;
    }
	
	 /**
     * 对树表结构进行转换
     * @param list 票据池单位LIST
     * @return
     */
    public List<AbPoolBusTypeVO> getListTree(List<AbPoolBusTypeVO> list){
        List<AbPoolBusTypeVO> listTree = null;
        if (ListUtils.isNotNullOrEmpty(list)) {
            listTree = new ArrayList<>();
            // 所有含有子单位的rootMap
            Map<String, AbPoolBusTypeVO> rootMap = new LinkedHashMap<>();
            initRootMap(list, rootMap);
            for (AbPoolBusTypeVO info : list){
                if (StringUtils.isEmpty(info.getParentId()) || !rootMap.containsKey(info.getParentId())){
                    listTree.add(info);
                    if (rootMap.containsKey(info.getId())){
                        // 生成每个子单位下的单位
                        this.getChildrenData(info.getChildren(), listTree, rootMap);
                    }
                }
            }
        }
        return listTree;
    }
	
    public void getChildrenData(List<AbPoolBusTypeVO> list, List<AbPoolBusTypeVO> listTree, Map<String, AbPoolBusTypeVO> rootMap){
        if (ListUtils.isNotNullOrEmpty(list)){
            for (AbPoolBusTypeVO abPoolBusTypeVO:list){
                listTree.add(abPoolBusTypeVO);
                AbPoolBusTypeVO ab = rootMap.get(abPoolBusTypeVO.getId());
                if (ab != null){
                    // 含有子单位时进行回调
                    this.getChildrenData(ab.getChildren(), listTree, rootMap);
                }
            }
        }
    }
	
    /**
     * 
    * @Title: getAbPoolBustTypeVOByBpId  
    * @Description: TODO(修改查询返回详细信息)  
    * @param @param bpId
    * @param @return    参数  
    * @return AbPoolBusTypeVO    返回类型  
    * @throws
     */
	public AbPoolBusTypeVO getAbPoolBustTypeVOByBpId(String bpId){
		return StringUtils.isEmpty(bpId)?null:abPoolBusTypeDao.getAbPoolBustTypeVOByBpId(bpId);
	}
	
	/**
	 * 
	* @Title: batchBusTypeOptWith  
	* @Description: TODO(批量操作业务（启用/禁用/删除）方法)  
	* @param @param ids
	* @param @param opt
	* @param @param userVo
	* @param @return    参数  
	* @return AjaxJson    返回类型  
	* @throws
	 */
	public AjaxJson batchBusTypeOptWith(String ids,Integer opt,ICurrentUser userVo){
		AjaxJson ajaxJson = new AjaxJson();
		try {
			if(String.valueOf(opt).equals(AbPoolConstant.AB_POLL_VALID_SIGN_2)){
				ajaxJson = deleteRowData(ids, opt, userVo);
			}else{
				if (StringUtils.isNotEmpty(ids)) {
			        String[] ridArray = ids.split(",");
			        for(String rid:ridArray){
			        	if(!StringUtils.isBlank(rid)){
				        	this.updateDispose(rid, opt, userVo);
			        	}
			        }
			    } 
			}
	    }catch (RuntimeException e) {
			ajaxJson.setSuccess(false);
			ajaxJson.setMessageEnum(AbPoolMessageEnum.AB_POOL_MESSAGE_BUSTYPEALL_ERROR);
			return ajaxJson;
		}
		ajaxJson.setMessageEnum(AbPoolMessageEnum.AB_POOL_MESSAGE_SUCCESS);
		ajaxJson.setSuccess(true);
		return ajaxJson;   
	}
	
	public AjaxJson deleteRowData(String ids,Integer opt,ICurrentUser userVo){
		    AjaxJson reJson = new AjaxJson();
	        reJson.setSuccess(false);
	        List<AbPoolBusTypeVO> list = abPoolBusTypeDao.listBusType(new AbPoolBusTypeVO());
	        Map<String, AbPoolBusTypeVO> rootMap = new LinkedHashMap<String, AbPoolBusTypeVO>();
	        if (list != null && list.size() > 0){
	            initRootMap(list, rootMap);
	        }
	        AbPoolBusType abPoolBusType = null;
	        if (StringUtils.isNotEmpty(ids)){
	            String[] idArray = ids.split(",");
	            for (String id : idArray){
	            	abPoolBusType = abPoolBusTypeDao.get(id);
	                if (AbPoolConstant.AB_POLL_VALID_SIGN_1.equals(abPoolBusType.getValidSign())){
	                	reJson.setMsg(AbPoolMessageEnum.AB_POOL_MESSAGE_BUSTYPE_ERROR.getCode());
	                    throw new BusinessException(AbPoolMessageEnum.AB_POOL_MESSAGE_BUSTYPE_ERROR);
	                }
	                if (rootMap.containsKey(id)){
	                    this.deleteBusType(rootMap.get(id).getChildren(), userVo, rootMap);
	                }
	                abPoolBusType.setValidSign(AbPoolConstant.AB_POLL_VALID_SIGN_2);
	                abPoolBusType.setUpdateTime(new Date());
	                abPoolBusType.setUpdateId(userVo.getId());
	                this.updateEntity(abPoolBusType);
	            }
	            reJson.setSuccess(true);
	        }
	        return reJson;
	}
	
	 public void deleteBusType(List<AbPoolBusTypeVO> list,ICurrentUser user, Map<String, AbPoolBusTypeVO> rootMap){
	        if (list != null && list.size() > 0){
	        	AbPoolBusType abPoolBusType = null;
	            for (AbPoolBusTypeVO abPoolBusTypeVO : list){
	            	abPoolBusType = this.get(abPoolBusTypeVO.getId());
	                if (AbPoolConstant.AB_POLL_VALID_SIGN_1.equals(abPoolBusType.getValidSign())){
	                    throw new BusinessException(AbPoolMessageEnum.AB_POOL_MESSAGE_BUSTYPE_ERROR);
	                }
	                abPoolBusType.setValidSign(AbPoolConstant.AB_POLL_VALID_SIGN_2);
	                abPoolBusType.setUpdateTime(new Date());
	                abPoolBusType.setUpdateId(user.getId());
	                this.updateEntity(abPoolBusType);
	                if (rootMap.containsKey(abPoolBusType.getId())){
	                	deleteBusType(rootMap.get(abPoolBusType.getId()).getChildren(), user, rootMap);
	                }
	            }
	        }
	    }
	
	
	public void updateDispose(String id,Integer opt,ICurrentUser userVo){
		 Map<String,Object> params = new HashMap<>();
		 params.put("id", id);
		 params.put("updateTime", new Date());
		 params.put("updateId", userVo.getId());
		 params.put("validSign", String.valueOf(opt));
		 abPoolBusTypeDao.updateEntityByParams(params);
	}
	
	/**
	 * 
	* @Title: settingPoolBusType  
	* @Description: TODO(新增设置修改公共方法)  
	* @param @param abPoolBusTypeVO
	* @param @param user
	* @param @return    参数  
	* @return AjaxJson    返回类型  
	* @throws
	 */
	public AjaxJson settingPoolBusType(AbPoolBusTypeVO abPoolBusTypeVO,ICurrentUser user){
		AjaxJson reJson = new AjaxJson();
		//判断是否重复
//		if(abPoolBusTypeDao.checkRepeat(abPoolBusTypeVO)){
//			reJson.setMessageEnum(AbPoolMessageEnum.BILLPOOL_BUSTYPE_CHECK_REPEAT);
//			reJson.setSuccess(false);
//			return reJson;
//		}
		try{
			AbPoolBusType abPoolBusType = new AbPoolBusType();
			BeanUtils.copyNotNullProperties(abPoolBusTypeVO, abPoolBusType);
			if(StringUtils.isNotEmpty(abPoolBusType.getId())){
				Map<String,Object> params = new HashMap<>();
				params.put("id", abPoolBusType.getId());
				params.put("updateTime", new Date());
				params.put("updateId", user.getId());
				params.put("bustypeName", abPoolBusType.getBustypeName());
				params.put("bustypeCode", abPoolBusType.getBustypeCode());
				abPoolBusTypeDao.updateEntityByParams(params);
			}else{
				abPoolBusType.setCreateId(user.getId());
				abPoolBusType.setCreateTime(new Date());
				abPoolBusType.setValidSign(AbPoolConstant.AB_POLL_VALID_SIGN_0);
				abPoolBusTypeDao.save(abPoolBusType);
			}
		}catch(Exception e){
			  throw new BusinessException(AbPoolMessageEnum.AB_POOL_MESSAGE_ERROR);
		}
		reJson.setMessageEnum(AbPoolMessageEnum.AB_POOL_MESSAGE_SUCCESS);
		reJson.setSuccess(true);
		return reJson;
	}
	
	/**
	 * 
	 * 
	* @Title: getTreeDataList  
	* @Description: TODO(返回组件信息)  
	* @param @param resource
	* @param @param queryVO
	* @param @param user
	* @param @return    参数  
	* @return Object    返回类型  
	* @throws
	 */
   @Transactional(readOnly = true)
    public Object getTreeDataList(String resource,AbPoolBusTypeVO queryVO, ICurrentUser user){
	    List<AbPoolBusTypeVO> list = abPoolBusTypeDao.listBusType(queryVO);
	    return ListUtils.isNotNullOrEmpty(list)?this.initAbPoolBusTypeVOTreeMap(list):null;
    }
   
   public List<Map<String, Object>> initAbPoolBusTypeVOTreeMap(List<AbPoolBusTypeVO> list){
       List<Map<String, Object>> listTree = null;
       if(list != null && list.size() > 0){
           listTree = new ArrayList<>();
           Map<String, AbPoolBusTypeVO> rootMap = new LinkedHashMap<>();
           initRootMap(list, rootMap);
           for(AbPoolBusTypeVO info:list){
               if(StringUtils.isEmpty(info.getParentId()) || !rootMap.containsKey(info.getParentId())){
                   Map<String, Object> mapInfo = new HashMap<>();
                   if(AbPoolConstant.AB_POLL_VALID_SIGN_1.equals(info.getValidSign())){
                       mapInfo.put("busTypeId", info.getId());
                       mapInfo.put("name", "("+info.getBustypeCode()+")"+info.getBustypeName());
                   }
                   // 根节点被禁用，取子节点
                   AbPoolBusTypeVO abPoolBusTypeVO = rootMap.get(info.getId());
                   if(abPoolBusTypeVO != null) {
                       this.initChildrenTree(abPoolBusTypeVO.getChildren(), mapInfo, rootMap, listTree);
                   }
                   if(mapInfo.size() > 0){
                       listTree.add(mapInfo);
                   }
               }
           }
       }
       return listTree;
   }
   public void initRootMap(List<AbPoolBusTypeVO> list,Map<String, AbPoolBusTypeVO> rootMap) {
       List<String> ids = abPoolBusTypeDao.getAllPoolParentIds();
       for (AbPoolBusTypeVO info : list) {
           if (StringUtils.isEmpty(info.getParentId()) || ids.contains(info.getId())) {
               rootMap.put(info.getId(), info);
           }
           if (rootMap.containsKey(info.getParentId())) {
               List<AbPoolBusTypeVO> sonList = rootMap.get(info.getParentId()).getChildren();
               if (sonList == null) {
                   sonList = new ArrayList<>();
                   rootMap.get(info.getParentId()).setChildren(sonList);
               }
               sonList.add(info);
           }
       }
    }
    
    @SuppressWarnings("unchecked")
	public void initChildrenTree(List<AbPoolBusTypeVO> list, Map<String, Object> mapInfo, Map<String, AbPoolBusTypeVO> rootMap, List<Map<String, Object>> listTree){
        List<Map<String, Object>> children = new ArrayList<>();
        if(ListUtils.isNotNullOrEmpty(list)){
            for(AbPoolBusTypeVO vice : list){
            	AbPoolBusTypeVO ab = rootMap.get(vice.getId());
                if(AbPoolConstant.AB_POLL_VALID_SIGN_1.equals(vice.getValidSign())){
                    Map<String, Object> baseVice = new HashMap<>();
                    baseVice.put("busTypeId", vice.getId());
                    baseVice.put("name", "("+vice.getBustypeCode()+")"+vice.getBustypeName());
                    if(ab != null){
                        this.initChildrenTree(ab.getChildren(), baseVice, rootMap, listTree);
                    }
                    // mapInfo小于1表示根节点被禁用
                    if(mapInfo.size() < 1){
                        // 添加节点
                        listTree.add(baseVice);
                    }else {
                        // 添加子节点
                        children.add(baseVice);
                    }
                }else {
                    // 当前单位节点被禁用时，查询其下面的子单位节点
                    if(null!=ab){
                        this.initChildrenTree(ab.getChildren(), mapInfo, rootMap, listTree);
                    }
                }
            }
            if(mapInfo.containsKey("children")){ 
                children.addAll((List<Map<String, Object>>)mapInfo.get("children"));
            }
            if( ListUtils.isNotNullOrEmpty(children)){
                mapInfo.put("children", children);
            }
        }
    }
	
}
