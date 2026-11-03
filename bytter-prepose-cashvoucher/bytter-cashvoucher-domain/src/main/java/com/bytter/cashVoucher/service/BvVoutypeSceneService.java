package com.bytter.cashVoucher.service;

import com.bytter.bill.api.bill.constant.BlankvouConstant;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.core.utils.BeanUtils;
import com.bytter.core.utils.ListUtils;
import com.bytter.cashVoucher.dao.BvVoutypeSceneDao;
import com.bytter.cashVoucher.entity.BvVoutypeScene;
import com.bytter.cashVoucher.vo.BvVoucherTypeVO;
import com.bytter.cashVoucher.vo.BvVoutypeSceneVO;
import com.bytter.system.api.IMultiLangService;
import com.bytter.system.api.Ii18nService;
import com.bytter.system.api.dto.I18nDTO;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
/**
 * @Title: Service
* @Description: 凭证类型打印场景
* @author zhengg
* @date 2020-04-03 20:50:38
 * @version 1.0
 *
 */
@Service("bvVoutypeSceneService")
@Transactional
public class BvVoutypeSceneService extends BaseService<BvVoutypeScene, String> {
	
	@Autowired
	private BvVoutypeSceneDao bvVoutypeSceneDao;
	@Resource
    private IMultiLangService multiLangServiceI;
	@DubboReference(check = false)
	private Ii18nService ii18nService;
	/**
	 * 删除对象
	 * @param entity
	 */
	public void deleteEntity(BvVoutypeScene entity) {
		super.deleteEntity(entity);
	}
	/**
	 * 根据id删除
	 * @param id
	 */
	public void delete(String id) {
		super.delete(id);
	}
	/**
	 * 保存
	 * @param entity
	 */
	public void save(BvVoutypeScene entity) {
		super.saveEntity(entity);
	}
	/**
	 * 更新
	 * @param entity
	 */
	public void update(BvVoutypeScene entity) {
		super.updateEntity(entity);
	}
	/**
	 * 保存or更新
	 * @param entity
	 */
	public void saveOrUpdate(BvVoutypeScene entity) {
		super.saveOrUpdate(entity);
	}
	public BaseDao<BvVoutypeScene, String> getEntityDao() {
		return bvVoutypeSceneDao;
	}
	
	/**
	 * 根据vouTid获取有效的取值字段明细信息  
	 * @return
	 */
	@Transactional(readOnly = true)
	public List<BvVoutypeScene> listVoutypeSceneValidByVouTid(String vouTid) {
		List<BvVoutypeScene> listInfo = bvVoutypeSceneDao.listVoutypeSceneValidByVouTid(vouTid);
		if(ListUtils.isNullOrEmpty(listInfo)){
			listInfo=new ArrayList<>();
			BvVoutypeScene otherEntity = new BvVoutypeScene();
			otherEntity.setId(BlankvouConstant.BV_OTHERSCENE_UUID);
			otherEntity.setName(multiLangServiceI.getLang(BlankvouConstant.BV_OTHERSCENE_KEY));
			otherEntity.setOrdernum(1);
			otherEntity.setValidSign(BlankvouConstant.VALID_SIGN_1);
			otherEntity.setVouTid(vouTid);
			listInfo.add(otherEntity);
		}
		return listInfo;
	}
  /**
   * 根据vouTid获取有效的取值字段明细信息  
   * @return
   */
	@Transactional(readOnly = true)
	public List<BvVoutypeSceneVO> listVoutypeSceneVOValidByVouTid(String vouTid) {
		return entityToVO(listVoutypeSceneValidByVouTid(vouTid));
	}
	public List<BvVoutypeSceneVO> entityToVO(List<BvVoutypeScene> list){
		List<BvVoutypeSceneVO> voList=null;
		if(ListUtils.isNotNullOrEmpty(list)){
			voList=BeanUtils.copyListProperties(list, BvVoutypeSceneVO.class);
		}
		if (voList != null){
			voList.forEach(a -> {
				a.setSceneName(a.getName());
			});
		}
		return voList;
	}
	/**
	 * 根据条件查询凭证种类信息
	 * @param
	 * @return
	 */
	@Transactional(readOnly = true)
	public List<BvVoutypeSceneVO> listBvVoutypeScene(String vouTid) {
		List<BvVoutypeSceneVO> listInfo = bvVoutypeSceneDao.listBvVoutypeSceneByVouTid(vouTid);
		if(ListUtils.isNullOrEmpty(listInfo)){
			listInfo=new ArrayList<>();
			BvVoutypeSceneVO otherEntity = new BvVoutypeSceneVO();
			otherEntity.setId(UUID.randomUUID().toString().replace("-", ""));
			otherEntity.setName(multiLangServiceI.getLang(BlankvouConstant.BV_OTHERSCENE_KEY));
			otherEntity.setOrdernum(1);
			otherEntity.setValidSign(BlankvouConstant.VALID_SIGN_1);
			otherEntity.setVouTid(vouTid);
			listInfo.add(otherEntity);
		}
		return listInfo;
	}
  
	/**
	 * 批量删除凭证种类取值字段明细
	 */
	public void batchDeleteByVouTid(String vouTid) {
		bvVoutypeSceneDao.deleteByVouTid(vouTid);
		
	}
	/**
	 * 批量新增凭证种类取值字段明细
	 */
	public void batchBvVoutypeScene(List<BvVoutypeScene> entityList,BvVoucherTypeVO  bvVoucherTypeVO) {
		for (int i=0;i<entityList.size();i++) { 
			BvVoutypeScene entity = entityList.get(i);
			entity.setVouTid(bvVoucherTypeVO.getId());
			entity.setOrdernum(i+1);
			ii18nService.saveOrUpdateBusinessBaseContext(new I18nDTO(entity.getId(), bvVoucherTypeVO.getLocale(), entity.getName()));
		}
		bvVoutypeSceneDao.batchJdbcSave(entityList);
	}
}
