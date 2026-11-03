package com.bytter.system.service;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.constant.SubjectConstant;
import com.bytter.core.enums.MatchTypeEnum;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.core.utils.SqlUtil;
import com.bytter.core.utils.StringUtils;
import com.bytter.system.api.ICorpService;
import com.bytter.system.api.IFcCorpPoolService;
import com.bytter.system.api.dto.CorpDTO;
import com.bytter.system.api.dto.FcCorpPoolDTO;
import com.bytter.system.api.vo.BtBankAccVO;
import com.bytter.system.constants.InnerAccountConstant;
import com.bytter.system.constants.ResourceConstant;
import com.bytter.system.dao.BtBankAccMaintainDao;
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
@Service("btBankAccMaintainService")
@Transactional
public class BtBankAccMaintainService extends BaseService<BtBankAcc, String> {
	@Autowired
	private BtBankAccMaintainDao btBankAccMaintainDao;
	@Autowired(required=false)
	private ICorpService corpServiceI;
	@Autowired
	private BtBankAccCommonService btBankAccCommonService;
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
		return btBankAccMaintainDao;
	}
	private String getAuthSql(ICurrentUser user) {
		StringBuilder sql = new StringBuilder();
		String corpAuthSql = SqlUtil.getInSql(
				corpServiceI.getUserCorpIds(ResourceConstant.NETBANK_BANKACC_BTBANKACCMAINTAIN_QUERY, user.getId()), "acc.corp_id",
				true);
		sql.append(" and ").append(corpAuthSql);
		return sql.toString();
	}
	@SuppressWarnings("rawtypes")
	public DataTablesOutput getDataTablesUnion(DataTablesInput dataTablesInput, ICurrentUser user) {
		return btBankAccMaintainDao.getDataTablesUnion(dataTablesInput, getAuthSql(user));
	}
	/**
	 * 资金池查询
	 * @param dataTablesInput
	 * @param user
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public DataTablesOutput getDataTablesUnionForPool(DataTablesInput dataTablesInput, ICurrentUser user) {
		StringBuilder sql = new StringBuilder();
		List<String> corpList = new ArrayList<>();
		String resource = ResourceConstant.NETBANK_BANKACC_BTBANKACCMAINTAIN_POOL_QUERY;
		List<FcCorpPoolDTO> pools = new ArrayList<>();
		//资金池不为空
		String poolId = (String) dataTablesInput.getFormdata().get("netId");
		if(!StringUtils.isBlank(poolId)){
			pools.add(fcCorpPoolServiceI.getById(poolId));
		}else{
			pools = fcCorpPoolServiceI.getUserCorpPools(user, resource);
		}
		for(FcCorpPoolDTO pool:pools) {
			List<FcCorpPoolDTO> poolChilds = fcCorpPoolServiceI.getAllUserCorpPoolChilds(user, resource, pool.getId());
			if (!EmptyUtils.isEmptyList(poolChilds)) {
				for (FcCorpPoolDTO dto : poolChilds) {
					corpList.add(dto.getCorpId());
				}
			}
		}
		String corpSql = SqlUtil.getInSql(corpList, "acc.corp_id", true);
		sql.append(" and ").append(corpSql);
		return btBankAccMaintainDao.getDataTablesUnion(dataTablesInput, sql.toString());
	}
	/**
	 * 保存or更新
	 *
	 * @param entity
	 */
	public void saveOrUpdate(BtBankAccApp entity) {
		btBankAccMaintainDao.saveOrUpdate(entity);
	}
	@SuppressWarnings("rawtypes")
	public DataTablesOutput dataTableForCounterAcc(DataTablesInput dataTablesInput, ICurrentUser user, String corpId,String id) {
		CorpDTO corpDto = null;
		if (StringUtils.isNotEmpty(corpId)) {
			corpDto = corpServiceI.get(corpId);
		}
		String joinSql = getAuthSql(user);
		String falg = (String)dataTablesInput.getFormdata().get("flag");
		if("1".equals(falg)){
			//上级内部账号取上级网点的企业活期存款
			joinSql += " and f.SYS_SUBJECT_CODE in ('"+SubjectConstant.SysSubject.INTEREST_EXPENSES_CURRENT_DEPOSIT.get()+"')";
			//判断是否有上级网点
			if(StringUtils.isEmpty(corpDto.getParentId())){
				dataTablesInput.addFilter(MatchTypeEnum.EQ, "netId", "-1");
			}else{
				CorpDTO corpDTO = corpServiceI.get(corpDto.getParentId());
				Integer type = corpDTO.getType();
				if(type == 0){
					dataTablesInput.addFilter(MatchTypeEnum.EQ, "netId", corpDto.getParentId());
					dataTablesInput.addFilter(MatchTypeEnum.EQ, "corpId", corpDTO.getId());
				}else{
					dataTablesInput.addFilter(MatchTypeEnum.EQ, "netId", "-1");
				}
			}
			//为单位银行账户，展示企业活期
			dataTablesInput.addFilter(MatchTypeEnum.EQ, "accType", InnerAccountConstant.FCACC_BUSINESS_ACCOUNT);
		}
		else {
			//内部账号取同单位的企业活期存款和银行活期存款的内部账号
			joinSql += " and f.SYS_SUBJECT_CODE in ('"+SubjectConstant.SysSubject.INTEREST_EXPENSES_CURRENT_DEPOSIT.get()+"','"+SubjectConstant.SysSubject.INTEREST_BANK_CURRENT_DEPOSIT.get()+"')";
			dataTablesInput.addFilter(MatchTypeEnum.EQ, "corpId", corpId);
		}
		return btBankAccMaintainDao.dataTableForCounterAcc(dataTablesInput, joinSql);
	}
	/**
	 * 资金池内部账号设置
	 * @param dataTablesInput
	 * @param user
	 * @param corpId
	 * @return
	 */
	public DataTablesOutput dataTableForCounterPoolAcc(DataTablesInput dataTablesInput, ICurrentUser user, String corpId) {
		StringBuilder sql = new StringBuilder();
		String falg = (String)dataTablesInput.getFormdata().get("flag");
		if("1".equals(falg)){
			List<FcCorpPoolDTO> corpPoolDTOList = null;
			String netIds = "";
			if (StringUtils.isNotEmpty(corpId)) {
				//获取单位所有的资金池
				corpPoolDTOList = fcCorpPoolServiceI.getFcCorpPoolByCorpId(corpId);
			}
			//判断是否本身是资金池
			if(EmptyUtils.isEmptyList(corpPoolDTOList)){
				dataTablesInput.addFilter(MatchTypeEnum.EQ, "netId", "-1");
			}else{
				for(FcCorpPoolDTO fcCorpPoolDTO:corpPoolDTOList){
					netIds += fcCorpPoolDTO.getPoolId()+",";
				}
				netIds = netIds.substring(0, netIds.length()-1);
				dataTablesInput.addFilter(MatchTypeEnum.IN, "netId", netIds);
				dataTablesInput.addFilter(MatchTypeEnum.EQ, "corpId", corpId);
			}
		}else{
			dataTablesInput.addFilter(MatchTypeEnum.EQ, "corpId", corpId);
		}
		//为单位银行账户，展示企业活期
		dataTablesInput.addFilter(MatchTypeEnum.EQ, "accType", InnerAccountConstant.FCACC_BUSINESS_ACCOUNT);
		return btBankAccMaintainDao.dataTableForPoolCounterAcc(dataTablesInput, sql.toString());
	}
	/**
	 * 关联内部账号
	 *
	 * @param id
	 * @param blnctrAcc
	 * @param flag
	 * @param curId
	 */
	public void toAdd(String id, String blnctrAcc, String flag, String curId) {
		BtBankAccCur cur = btBankAccCommonService.getBtBankAccCur(id, curId);
		if ("0".equals(flag)) {
			cur.setCounterAcc(blnctrAcc);
		} else {
			cur.setSuperCounterAcc(blnctrAcc);
		}
		btBankAccMaintainDao.saveOrUpdate(cur);
	}
	/**
	 * 取消关联内部账号
	 *
	 * @param id
	 * @param flag
	 * @param curId
	 */
	public void toDel(String id, String flag, String curId) {
		BtBankAccCur cur = btBankAccCommonService.getBtBankAccCur(id, curId);
		if ("0".equals(flag)) {
			cur.setCounterAcc("");
		} else {
			cur.setSuperCounterAcc("");
		}
		btBankAccMaintainDao.saveOrUpdate(cur);
	}
}
