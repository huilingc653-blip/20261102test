package com.bytter.cashVoucher.service;

import cn.hutool.core.util.ObjectUtil;
import com.bytter.bill.api.bill.constant.BlankvouConstant;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.utils.BeanUtils;
import com.bytter.core.utils.ListUtils;
import com.bytter.cashVoucher.dao.BvStorageBoxDao;
import com.bytter.cashVoucher.entity.BvStorageBox;
import com.bytter.cashVoucher.vo.BvStorageBoxVo;
import com.bytter.system.api.ICorpService;
import com.bytter.system.api.IDictService;
import com.bytter.system.api.IUserService;
import com.bytter.system.api.Ii18nService;
import com.bytter.system.api.dto.DictDTO;
import com.bytter.system.api.dto.I18nDTO;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.*;
/**
* @Title: Service
* @Description: 库箱维护
* @author onlineGenerator
* @date 2017-05-25 18:17:27
* @version 1.0
*
*/
@Service("bvStorageBoxService")
@Transactional
public class BvStorageBoxService extends BaseService<BvStorageBox,String> {
	@Autowired
	private BvStorageBoxDao bvStorageBoxDao;
	@Autowired
	private BvLogService bvLogService;
	@Autowired
	private BvVouInputService bvVouInputService;
	@DubboReference(check = false)
	private ICorpService corpService;
	@DubboReference(check = false)
	private Ii18nService ii18nService;
	@DubboReference(check = false)
	private IUserService userService;
	@DubboReference(check = false)
	private IDictService dictService;
	/**
	* 删除对象
	* @param entity
	*/
	public void deleteEntity(BvStorageBox entity,ICurrentUser userVo){
		super.deleteEntity(entity);
		bvLogService.saveBvLog(entity.getId(),BlankvouConstant.BVLOG_LOGTYPE_DELETE,"删除库箱",userVo.getId(),userVo.getUsername());
	}
	/**
	* 根据id删除
	* @param id
	*/
 	public void delete(String id){
 		super.delete(id);
 	}
	/**
	* 保存
	*/
 	public void save(BvStorageBox box,ICurrentUser userVo){
 		box.setValidSign("1");
    	box.setCreateBy(userVo.getLoginName());
    	box.setCreateName(userVo.getUsername());
		box.setCreateDate(new Date());
		// 冗余数据
		Optional.ofNullable(corpService.get(box.getCorpId())).ifPresent(corp -> box.setCorpName(corp.getName()));
		Optional.ofNullable(userService.getByUserId(box.getKeeperUid())).ifPresent(user -> box.setKeeperName(user.getUsername()));
 		super.saveEntity(box);
		ii18nService.saveOrUpdateBusinessBaseContext(new I18nDTO(box.getId(), LocaleContextHolder.getLocale().toString(), box.getName()));
 		bvLogService.saveBvLog(box.getId(),BlankvouConstant.BVLOG_LOGTYPE_ADD,"新增库箱",userVo.getLoginName(),userVo.getUsername());
 	}
	/**
	* 更新
	* @param entity
	*/
	public void update(BvStorageBox entity){
		super.updateEntity(entity);
	}
	
	/**
	* 更新ValidSign属性
	* @param entity
	*/
	public void updateValidSign(BvStorageBox entity,Integer type,ICurrentUser userVo){
		entity.setValidSign(type.toString());
		super.updateEntity(entity);
		switch(type)
		{
		case 0:bvLogService.saveBvLog(entity.getId(),BlankvouConstant.BVLOG_LOGTYPE_STOP,"停用库箱",userVo.getLoginName(),userVo.getUsername());break;
		case 1:bvLogService.saveBvLog(entity.getId(),BlankvouConstant.BVLOG_LOGTYPE_START,"启用库箱",userVo.getLoginName(),userVo.getUsername());break;
		case 2:bvLogService.saveBvLog(entity.getId(),BlankvouConstant.BVLOG_LOGTYPE_DELETE,"删除库箱",userVo.getLoginName(),userVo.getUsername());break;
		default:break;
		}	
	}
	
	/**
	* 保存or更新
	* @param entity
	*/
 	public void saveOrUpdate(BvStorageBox entity,ICurrentUser userVo){
 		entity.setUpdateBy(userVo.getLoginName());
 		entity.setUpdateName(userVo.getUsername());
 		entity.setUpdateDate(new Date());
		// 冗余数据
		Optional.ofNullable(corpService.get(entity.getCorpId())).ifPresent(corp -> entity.setCorpName(corp.getName()));
		Optional.ofNullable(userService.getByUserId(entity.getKeeperUid())).ifPresent(user -> entity.setKeeperName(user.getUsername()));
 		super.saveOrUpdate(entity);
 		bvLogService.saveBvLog(entity.getId(),BlankvouConstant.BVLOG_LOGTYPE_UPDATE,"修改库箱",userVo.getLoginName(),userVo.getUsername());
		ii18nService.saveOrUpdateBusinessBaseContext(new I18nDTO(entity.getId(), LocaleContextHolder.getLocale().toString(), entity.getName()));
 	}
	/**
	* BV_STORAGE_BOX table列表
	*/
	@SuppressWarnings({ "rawtypes" })
	@Transactional(readOnly = true)
	public DataTablesOutput getBvStorageBoxs(@RequestBody DataTablesInput input) {
		List<String> corpAuthList = corpService.getUserCorpIds(BlankvouConstant.AB_BILL_RESOURCE_BVSTORAGEBOX_QUERY, input.getCurrentUserId());
		DataTablesOutput<BvStorageBoxVo> boxList = bvStorageBoxDao.getBvStorageBoxs(input, corpAuthList);
		generateStorageBoxVo(boxList.getData());
		return boxList;
	}
	/**
	 * 远程赋值。
	 *
	 * @param boxList 库箱信息。
	 */
	private void generateStorageBoxVo(List<BvStorageBoxVo> boxList) {
		if (ObjectUtil.isNotEmpty(boxList)) {
			Map<String, DictDTO> listMap = dictService.listMap("bvstorageBox_status");
			if (ObjectUtil.isNotEmpty(listMap)) {
				boxList.forEach(box -> {
					if (ObjectUtil.isNotEmpty(box.getValidSign())) {
						Optional.ofNullable(listMap.get(box.getValidSign())).ifPresent(dict -> box.setValidSignName(dict.getName()));
					}
				});
			}
		}
	}
	/**
	 * 查询库箱维护表的详情页（查看）
	 * @param id
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	@Transactional(readOnly = true)
	public DataTablesOutput getBvStorageBoxLook(String id){
		DataTablesOutput<BvStorageBoxVo> storageBoxLook = bvStorageBoxDao.getBvStorageBoxLook(id);
		generateStorageBoxVo(storageBoxLook.getData());
		return storageBoxLook;
	}
	public BaseDao<BvStorageBox, String> getEntityDao() {
		return bvStorageBoxDao;
	}
	
	/**
	 * 根据条件查询库箱信息结果
	 * @return
	 */
	@Transactional(readOnly = true)
	public List<BvStorageBox> listStorageBoxList(Map <String,String> bvbMap){
		return bvStorageBoxDao.listStorageBoxList(bvbMap);
	}
	/**
	 * 根据条件查询库箱信息结果
	 * @return
	 */
	@Transactional(readOnly = true)
	public List<BvStorageBoxVo> listStorageBoxVOList(Map <String,String> bvbMap){
		return entityToVO(listStorageBoxList(bvbMap));
	}
	
	/**
	 * 获取有效的库箱明细（标记为【启用】）
	 * @return
	 */
	@Transactional(readOnly = true)
	public List <BvStorageBox> listStorageBoxByValid(ICurrentUser user){
		Map<String, String> params = new HashMap<> ();
		params.put("validSign","1");
		params.put("keeperUid",user.getId ());
		return bvStorageBoxDao.listStorageBoxByValid(params);
	}
	/**
	 * 获取有效的库箱明细（标记为【启用】）
	 * @return
	 */
	@Transactional(readOnly = true)
	public List <BvStorageBoxVo> listStorageBoxVOByValid(ICurrentUser user){
		return entityToVO(listStorageBoxByValid(user));
	}
	
	/**
	 * 根据单位id来查找库箱
	 * @return
	 */
	@Transactional(readOnly = true)
	public List <BvStorageBoxVo> listStorageBoxByCord(String corpId,ICurrentUser user){
		return bvStorageBoxDao.listStorageBoxByCord(corpId,user.getId ());
	}
	
	/**
	 * 根据单位id来查找库箱
	 * @return
	 */
	@Transactional(readOnly = true)
	public List <BvStorageBox> listStorageBoxByCords(String corpIds,ICurrentUser user){
		return bvStorageBoxDao.listStorageBoxByCords(corpIds,user.getId ());
	}
	
	/**
	 * 根据单位id来查找库箱
	 * @return
	 */
	@Transactional(readOnly = true)
	public List <BvStorageBoxVo> listStorageBoxVOByCords(String corpIds,ICurrentUser user){
		return entityToVO(listStorageBoxByCords(corpIds,user));
	}
	
	public List<BvStorageBoxVo> entityToVO(List<BvStorageBox> list){
		List<BvStorageBoxVo> voList=null;
		if(ListUtils.isNotNullOrEmpty(list)){
			voList=BeanUtils.copyListProperties(list, BvStorageBoxVo.class);
			// inject(voList);
		}
		return voList;
	}
    /**
     * 同一单位下不允许使用相同代码
     */
	public boolean storageBoxVerification(String code,String corpId,String id)
	{
		return bvStorageBoxDao.storageBoxVerification(code,corpId,id);
	}
	/**
	 * 判断库箱能否修改
	 * @return
	 */
	@Transactional(readOnly = true)
	public boolean isUpdate(String id)
	{
		return bvVouInputService.checkBvStorageBox(id);
	}
	/**
	 * 判断库箱能否停止
	 * @return
	 */
	public AjaxJson stop(AjaxJson j,String id,ICurrentUser userVo)
	{
		BvStorageBox bvStorageBox = get(id);
		boolean flag=bvVouInputService.checkBvStorageBox(id);
		if(flag){
			updateValidSign(bvStorageBox,0,userVo);	
		}
		else{			
			j.setSuccess(false);
		}
		return j;
	}
	
	/**
	 * 获取用户对应单位有权限的库箱信息
	 * @param corpId 单位ID
	 * @param user 用户信息
	 * @return
	 */
	@Transactional(readOnly = true)
	public Map<String,BvStorageBox> getStorageBoxMap(String corpId,ICurrentUser user){
		Map<String,BvStorageBox> boxMap=new HashMap<>();
		List<BvStorageBox> list= listStorageBoxByCords(corpId, user);
		if(!ListUtils.isNullOrEmpty(list)){
			for(BvStorageBox box:list){
				boxMap.put(box.getName(), box);
			}
		}
		return boxMap;
	}
}
