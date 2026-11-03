package com.bytter.system.inneraccount.service;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.core.utils.SqlUtil;
import com.bytter.core.utils.StringUtils;
import com.bytter.system.api.ICorpService;
import com.bytter.system.api.IFcCorpPoolService;
import com.bytter.system.api.IFcSubjectService;
import com.bytter.system.api.dto.FcCorpPoolDTO;
import com.bytter.system.api.vo.FcAccHistoryVO;
import com.bytter.system.constants.ResourceConstant;
import com.bytter.system.inneraccount.dao.FcAccHistoryDao;
import com.bytter.system.inneraccount.entity.FcAccHistory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
 * @Title: Service
 * @Description: FC_ACC_HISTORY
 * @author onlineGenerator
 * @date 2017-06-07 14:30:19
 * @version 1.0
 *
 */
@Service("fcAccHistoryService")
@Transactional
public class FcAccHistoryService extends BaseService<FcAccHistory,String> {
	@Autowired
	private FcAccHistoryDao fcAccHistoryDao;
	@Autowired
	private ICorpService corpServiceI;
	@Autowired
	private IFcCorpPoolService fcCorpPoolServiceI;
	@Autowired
	private IFcSubjectService fcSubjectServiceI;
	/**
	 * 删除对象
	 * @param entity
	 */
	@Override
	public void deleteEntity(FcAccHistory entity){
		super.deleteEntity(entity);
	}
	/**
	 * 根据id删除
	 * @param id
	 */
	@Override
	public void delete(String id){
		super.delete(id);
	}
	/**
	 * 保存
	 * @param entity
	 */
	public void save(FcAccHistory entity){
		super.saveEntity(entity);
	}
	/**
	 * 更新
	 * @param entity
	 */
	public void update(FcAccHistory entity){
		super.updateEntity(entity);
	}
	/**
	 * 保存or更新
	 * @param entity
	 */
	@Override
	public void saveOrUpdate(FcAccHistory entity){
		super.saveOrUpdate(entity);
	}
	@Override
	public BaseDao<FcAccHistory, String> getEntityDao() {
		return fcAccHistoryDao;
	}
	/**
	 * 待提交的可改户申请的数据列表
	 * @param dataTablesInput
	 * @return
	 */
	public DataTablesOutput getDataTablesUnion(DataTablesInput dataTablesInput,ICurrentUser userVO) {
		Map<String,Object> params=new HashMap();
		String corpAuthSql=SqlUtil.getInSqlByParams(corpServiceI.getUserCorpIds(ResourceConstant.NETBANK_INNERACCOUNT_FCACC_CHANGEQUERY,userVO.getId()),"t.corp_Id",params);
		StringBuilder joinSql=new StringBuilder();
		String loginName = userVO.getLoginName();
		joinSql.append(" and ").append(corpAuthSql);
		return fcAccHistoryDao.getDataTablesUnion(dataTablesInput,joinSql.toString(),loginName,params);
	}
	/**
	 * 待提交的可改户申请的数据列表
	 * @param dataTablesInput
	 * @return
	 */
	public DataTablesOutput getDataTablesUnionForPool(DataTablesInput dataTablesInput,ICurrentUser userVO) {
		StringBuilder sql = new StringBuilder();
		Map<String,Object> params=new HashMap();
		List<String> corpList = new ArrayList<>();
		String resource = ResourceConstant.NETBANK_INNERACCOUNT_FCACC_POOL_CHANGEQUERY;
		List<FcCorpPoolDTO> pools = new ArrayList<>();
		//资金池不为空
		String poolId = (String) dataTablesInput.getFormdata().get("eqs_netId");
		if(!StringUtils.isBlank(poolId)){
			pools.add(fcCorpPoolServiceI.getById(poolId));
		}else{
			pools = fcCorpPoolServiceI.getUserCorpPools(userVO, resource);
		}
		for(FcCorpPoolDTO pool:pools) {
			List<FcCorpPoolDTO> poolChilds = fcCorpPoolServiceI.getAllUserCorpPoolChilds(userVO, resource, pool.getId());
			if (!EmptyUtils.isEmptyList(poolChilds)) {
				for (FcCorpPoolDTO dto : poolChilds) {
					corpList.add(dto.getCorpId());
				}
			}
		}
		String corpSql = SqlUtil.getInSql(corpList, "t.corp_id", true);
		sql.append(" and ").append(corpSql);
		return fcAccHistoryDao.getDataTablesUnionForPool(dataTablesInput,sql.toString(),userVO.getLoginName(),params);
	}
	/**
	 * 复核数据列表
	 * @param dataTablesInput
	 * @return
	 */
	public DataTablesOutput getCheckDataTable(DataTablesInput dataTablesInput,ICurrentUser user) {
		Map<String,Object> params=new HashMap();
		String corpAuthSql=SqlUtil.getInSqlByParams(corpServiceI.getUserCorpIds(ResourceConstant.NETBANK_INNERACCOUNT_FCACC_CHANGEQUERY,user.getId()),"t.corp_Id",params);
		StringBuilder joinSql=new StringBuilder();
		joinSql.append(" and ").append(corpAuthSql);
		return fcAccHistoryDao.getCheckDataTable(dataTablesInput,joinSql.toString(),user.getLoginName(),params);
	}
	/**
	 * 复核数据列表
	 * @param dataTablesInput
	 * @return
	 */
	public DataTablesOutput getCheckDataTableForPool(DataTablesInput dataTablesInput,ICurrentUser user) {
		StringBuilder sql = new StringBuilder();
		Map<String,Object> params=new HashMap();
		List<String> corpList = new ArrayList<>();
		String resource = ResourceConstant.NETBANK_INNERACCOUNT_FCACC_POOL_CHANGEQUERY;
		List<FcCorpPoolDTO> pools = new ArrayList<>();
		//资金池不为空
		String poolId = (String) dataTablesInput.getFormdata().get("eqs_netId");
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
		String corpSql = SqlUtil.getInSql(corpList, "t.corp_id", true);
		sql.append(" and ").append(corpSql);
		return fcAccHistoryDao.getCheckDataTableForPool(dataTablesInput,sql.toString(),user.getLoginName(),params);
	}
	/**
	 * 根据id查对象
	 *
	 * @param id
	 * @return
	 */
	public FcAccHistoryVO findFcAccHistoryVOById(String id) {
		FcAccHistoryVO fcAcc = fcAccHistoryDao.findFcAccHistoryVOById(id);
		return fcAcc;
	}
	/**
	 * 根据id查对象
	 *
	 * @param id
	 * @return
	 */
	public FcAccHistoryVO findFcAccHistoryVOByIdForPool(String id) {
		FcAccHistoryVO fcAcc = fcAccHistoryDao.findFcAccHistoryVOByIdForPool(id);
		return fcAcc;
	}
	/**
	 * 根据账号查询历史账户对象
	 *
	 * @param blnctrAcc
	 * @return
	 */
	public FcAccHistoryVO findFcAccHistoryVOByBlnctrAcc(String blnctrAcc) {
		FcAccHistoryVO fcAcc = fcAccHistoryDao.findFcAccHistoryVOByBlnctrAcc(blnctrAcc);
		return fcAcc;
	}
	/**
	 * 根据账号信息查找计息账号列表
	 * @param vo
	 * @return
	 */
	public List<FcAccHistoryVO> getAccListByExpPay(FcAccHistoryVO vo) {
		List<FcAccHistoryVO> accList = fcAccHistoryDao.getAccListByExpPay(vo);
		return accList;
	}
	/**
	 * 根据账号信息查找计息账号
	 * @param vo
	 * @return
	 */
	public FcAccHistoryVO getFcAccHistoryVO(FcAccHistoryVO vo) {
		FcAccHistoryVO accList = fcAccHistoryDao.getFcAccHistoryVO(vo);
		return accList;
	}
	
}
