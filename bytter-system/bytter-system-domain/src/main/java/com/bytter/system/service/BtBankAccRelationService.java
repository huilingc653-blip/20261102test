package com.bytter.system.service;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.core.utils.SqlUtil;
import com.bytter.system.api.ICorpService;
import com.bytter.system.api.IFcCorpPoolService;
import com.bytter.system.api.constant.CorpConstant;
import com.bytter.system.api.dto.CorpDTO;
import com.bytter.system.api.dto.FcCorpPoolDTO;
import com.bytter.system.api.vo.BtBankAccVO;
import com.bytter.system.constants.BtBankAccConstant;
import com.bytter.system.constants.ResourceConstant;
import com.bytter.system.dao.BtBankAccDao;
import com.bytter.system.dao.BtBankAccRelationDao;
import com.bytter.system.entity.BtBankAcc;
import com.bytter.system.entity.BtBankAccApp;
import com.bytter.system.entity.BtBankAccCur;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.ArrayList;
import java.util.List;
/**
 * @author onlineGenerator
 * @version 1.0
 * @Title: Service
 * @Description: BT_BANK_ACC
 * @date 2017-06-02 17:50:16
 */
@Service("btBankAccRelationService")
@Transactional
public class BtBankAccRelationService extends BaseService<BtBankAcc, String> {
	@Autowired
	private BtBankAccRelationDao btBankAccRelationDao;
	@Autowired(required=false)
	private ICorpService corpServiceI;
	@Autowired
	private BtBankAccDao btBankAccDao;
//	@Autowired(required=false)
//	private IBisAccGatherPolicyService bisAccGatherPolicyService;
	@Autowired
	private BtBankAccCommonService btBankAccCommonService;
	@Autowired
	private BtBankAccCurService btBankAccCurService;
	@Autowired(required=false)
	private IFcCorpPoolService fcCorpPoolServiceI;
	/**
	 * 删除对象
	 *
	 * @param entity
	 */
	@Override
	public void deleteEntity(BtBankAcc entity) {
		super.deleteEntity(entity);
	}
	/**
	 * 根据id删除
	 *
	 * @param id
	 */
	@Override
	public void delete(String id) {
		super.delete(id);
	}
	/**
	 * 保存
	 *
	 * @param entity
	 */
	public void save(BtBankAcc entity) {
		super.saveEntity(entity);
	}
	/**
	 * 更新
	 *
	 * @param entity
	 */
	public void update(BtBankAcc entity) {
		super.updateEntity(entity);
	}
	/**
	 * 保存or更新
	 *
	 * @param entity
	 */
	@Override
	public void saveOrUpdate(BtBankAcc entity) {
		super.saveOrUpdate(entity);
	}
	@Override
	public BaseDao<BtBankAcc, String> getEntityDao() {
		return btBankAccRelationDao;
	}
	@SuppressWarnings("rawtypes")
	public DataTablesOutput getDataTablesUnion(DataTablesInput dataTablesInput, ICurrentUser user) {
		StringBuilder sql = new StringBuilder();
		//用户的单位权限
		String corpAuthSql = SqlUtil.getInSql(
				corpServiceI.getUserCorpIds(ResourceConstant.NETBANK_BANKACC_BTBANKACCRELATION_QUERY, user.getId()), "acc.corp_id",
				true);
		sql.append(" and ").append(corpAuthSql);
		String id = dataTablesInput.getPara("id");
		BtBankAcc btBankAcc = btBankAccDao.get(id, true);
//		Boolean isHas = false;
//		//账号为总账号
//		if("4028802f5c0fe799015c1019d37a001c".equals(btBankAcc.getAttributeId())){
//			isHas = true;
//		}
		CorpDTO corpDTO = corpServiceI.get(btBankAcc.getCorpId());
//		if(!isHas) {
		List<String> corpList = new ArrayList<>();
		//如果是单位取单位本身和下属单位
		if (corpDTO.getType().equals(CorpConstant.CORP_TYPE_CODE_CORP)) {
			List<CorpDTO> corpDTOList = new ArrayList<>();
			//避免客户上线时单位信息初始化缺失parentIds字段，导致获取不全跨级单位集合
			List<CorpDTO> corpDTOUderSkipLevelList = corpServiceI.listUnderCorpByParentCorpIdSkipLevel(corpDTO.getId());
			List<CorpDTO> corpDTOUderList = corpServiceI.listUnderCorpByParentCorpId(corpDTO.getId());
			//非空校验，避免list 添加null对象
			if (corpDTOUderList != null && corpDTOUderList.size() > 0) {
				corpDTOList.addAll(corpDTOUderList);
			}
			corpDTOList.addAll(corpDTOUderSkipLevelList);
			if (!EmptyUtils.isEmptyList(corpDTOList)) {
				for (CorpDTO dto : corpDTOList) {
					corpList.add(dto.getId());
				}
			}
			corpList.add(corpDTO.getId());
		} else {//如果是网点取网点本身和下属单位。。不跨网点
			List<CorpDTO> corpDTOList = corpServiceI.listUnderCorpByNetId(corpDTO.getId());
			if (!EmptyUtils.isEmptyList(corpDTOList)) {
				for (CorpDTO dto : corpDTOList) {
					corpList.add(dto.getId());
				}
			}
			corpList.add(corpDTO.getId());
		}
		String corpSql = SqlUtil.getInSql(corpList, "acc.corp_id", true);
		sql.append(" and ").append(corpSql);
//		}else{
//			sql.append(" and sc.net_id = '"+corpDTO.getNetId()+"' ");
//		}
		return btBankAccRelationDao.getDataTablesUnion(dataTablesInput, sql.toString());
	}
	/**
	 * 获取资金池子单位账号
	 * @param dataTablesInput
	 * @param user
	 * @return
	 */
	public DataTablesOutput getDataTablesUnionForPool(DataTablesInput dataTablesInput, ICurrentUser user) {
		StringBuilder sql = new StringBuilder();
		String poolId = (String) dataTablesInput.getFormdata().get("poolId");
		String resource = ResourceConstant.NETBANK_BANKACC_BTBANKACCRELATION_POOL_QUERY;
		List<FcCorpPoolDTO> fcCorpPoolDTOList = fcCorpPoolServiceI.getUserCorpPoolChilds(user, resource, poolId);
		List<String> corpList = new ArrayList<>();
		if (!EmptyUtils.isEmptyList(fcCorpPoolDTOList)) {
			for (FcCorpPoolDTO dto : fcCorpPoolDTOList) {
				corpList.add(dto.getCorpId());
			}
		}
		String corpSql = SqlUtil.getInSql(corpList, "acc.corp_id", true);
		sql.append(" and ").append(corpSql);
		return btBankAccRelationDao.getDataTablesUnion(dataTablesInput, sql.toString());
	}
	/**
	 * 保存or更新
	 *
	 * @param entity
	 */
	public void saveOrUpdate(BtBankAccApp entity) {
		btBankAccRelationDao.saveOrUpdate(entity);
	}
	/**
	 * 根据单位id得到银行账号列表
	 *
	 * @param corpId
	 * @return
	 */
	public List<BtBankAccVO> listBankAccByCorpId(String corpId, String curId, String isOnline,String isCapitalPool) {
		return btBankAccRelationDao.listBankAccByCorpId(corpId, curId,isOnline,isCapitalPool);
	}
	/**
	 * 根据单位id得到银行账号列表
	 *
	 * @param corpId
	 * @return
	 */
	public List<BtBankAccVO> listBankAccInfosByCorpId(String corpId, String curId, String isOnline) {
		return btBankAccRelationDao.listBankAccInfosByCorpId(corpId, curId,isOnline);
	}
	/**
	 * 根据父账户ID和币别查询子账户
	 *
	 * @param prentAccId 父账户ID
	 * @param curId      币别ID
	 * @return
	 */
	public List<BtBankAccVO> getChildBankAccList(String prentAccId, String curId) {
		return btBankAccRelationDao.getChildBankAccList(prentAccId, curId);
	}
	/**
	 * 增加父子关系
	 *
	 * @param ids
	 * @param prentAccId
	 */
	public void toAddRelation(String ids, String prentAccId, String curId) {
		String[] id = ids.split(",");
		/**
		 * 更新子账号的父账号的id
		 */
		for (int i = 0; i < id.length; i++) {
			BtBankAccCur bankAccCur = this.btBankAccCommonService.getBtBankAccCur(id[i], curId);
			bankAccCur.setPrentAccId(prentAccId);
			this.btBankAccCurService.saveOrUpdate(bankAccCur);
		}
		BtBankAcc prentAcc = btBankAccRelationDao.get(prentAccId);
		prentAcc.setParentSign(BtBankAccConstant.PARENTSIGN_1 + "");
		this.save(prentAcc);
	}
	/**
	 * 删除父子关系
	 *
	 * @param id
	 */
	public void toDeleteRelation(String id, String curId) {
		BtBankAccCur bankAccCur = this.btBankAccCommonService.getBtBankAccCur(id, curId);
		String prentAccId = bankAccCur.getPrentAccId();
		bankAccCur.setPrentAccId(null);
		/**
		 * 如果子账号删除完了。改变父账号标志
		 */
		if (btBankAccCommonService.isChildBankAccByPrentAccId(prentAccId)) {
			BtBankAcc prentAcc = btBankAccRelationDao.get(prentAccId);
			prentAcc.setParentSign(BtBankAccConstant.PARENTSIGN_0 + "");
			this.saveOrUpdate(prentAcc);
		}
		this.btBankAccCurService.saveOrUpdate(bankAccCur);
		//根据父子账号取调拨策略如果取消父子关系。则需要取消调拨策略的父子关系
//		bisAccGatherPolicyService.deleteBisGatherPolicyByCurId(id, prentAccId, curId);//TODO放到结算
	}
}
