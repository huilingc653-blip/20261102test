package com.bytter.bill.domain.billPool.service;
import com.bytter.bill.domain.bill.service.AbCorpService;
import com.bytter.bill.domain.billPool.dao.AbPoolLimitDao;
import com.bytter.bill.domain.billPool.entity.AbPoolLimit;
import com.bytter.bill.api.billPool.constant.AbPoolConstant;
import com.bytter.bill.domain.billPool.vo.AbPoolLimitVO;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.utils.DateUtils;
import com.bytter.core.utils.ListUtils;
import com.bytter.core.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
 * @Title: 额度信息Service
 * @Description: 额度信息业务处理类
 * @author zhenghj
 * @date 2020-02-20
 * @version 1.0
 *
 */
@Service("abPoolLimitService")
@Transactional
public class AbPoolLimitService extends BaseService<AbPoolLimit, String>{
	
	@Autowired
	private AbPoolLimitDao abPoolLimitDao;
	
	@Autowired
	private AbCorpService abCorpService;
	@Override
	public BaseDao<AbPoolLimit, String> getEntityDao() {
		return abPoolLimitDao;
	}
	/**
	 * 额度信息查询
	 * @param corpId 单位ID
	 * @param bpId 票据池ID
	 * @return 额度信息
	 */
	@Transactional(readOnly = true)
	public AbPoolLimitVO getLimitInfo(String corpId, String bpId){
		return getLimitInfo(corpId, bpId, null);
	}
	/**
	 * 额度信息查询
	 * @param corpId 单位ID
	 * @param bpId 票据池ID
	 * @param bankAcc 银行账号
	 * @return 额度信息
	 */
	@Transactional(readOnly = true)
	public AbPoolLimitVO getLimitInfo(String corpId, String bpId,String bankAcc){
		if (StringUtils.isEmpty(corpId) || (StringUtils.isEmpty(bpId)&&StringUtils.isEmpty(bankAcc))){
			return null;
		}
		AbPoolLimitVO poolLimitVO=abPoolLimitDao.getLimitInfo(corpId, bpId, bankAcc);
		if(poolLimitVO!=null){
			if(poolLimitVO.getPoolMoney()==null){
				poolLimitVO.setPoolMoney(new BigDecimal(0));
			}
			if(poolLimitVO.getValidMoney()==null){
				poolLimitVO.setValidMoney(new BigDecimal(0));
			}
		}
		return poolLimitVO;
	}
	
	/**
	 * 查询票据池票据池额度信息列表数据
	 * 
	 * @param dataTablesInput
	 * @return
	 */
	@Transactional(readOnly = true)
	public DataTablesOutput<AbPoolLimitVO> datatableQuery(DataTablesInput dataTablesInput) {
		List<String> corpAuthList=abCorpService.getUserCorpIds(AbPoolConstant.AB_POLL_QUERY_LIMIT_OP,dataTablesInput.getCurrentUserId());
		return abPoolLimitDao.datatableQuery(dataTablesInput,corpAuthList);
	}
	
	/**
	 * 根据ID获取票据池额度信息
	 * @param id 池Id
	 * @return
	 */
	public AbPoolLimitVO getAbPoolLimitVOById(String id) {
		List<AbPoolLimitVO> list=abPoolLimitDao.getAbPoolLimitVOById(id);
		if(ListUtils.isNotNullOrEmpty(list)){
			AbPoolLimitVO limitVO=list.get(0);
			Map<String,Object> poolDtl=new HashMap<>();
			Map<String,Object> params=new HashMap<>();
			Date queryDate= DateUtils.getDateYMD();
			limitVO.setQueryDate(queryDate);
			params.put("datem1",DateUtils.addMonth(queryDate,1));
			params.put("datem2",DateUtils.addMonth(queryDate,2));
			params.put("datem3",DateUtils.addMonth(queryDate,3));
			params.put("datem6",DateUtils.addMonth(queryDate,6));
			params.put("corpId",limitVO.getCorpId());
			params.put("bpId",limitVO.getBpId());
			poolDtl.putAll(abPoolLimitDao.getEmitMoneyInfo(params));
			poolDtl.putAll(abPoolLimitDao.getHoldMoneyInfo(params));
			//初始化余额空的信息
			String[] poolDtlKeys={"sumemitmoney","sumemitmoney1","sumemitmoney2","sumemitmoney3","sumemitmoney4","sumemitmoney5","sumemitmoney6","sumemitmoney7",
								"sumholdmoney","sumholdmoney1","sumholdmoney2","sumholdmoney3","sumholdmoney4","sumholdmoney5","sumholdmoney6","sumholdmoney7"};
			for(String mapKey:poolDtlKeys){
				if(poolDtl.get(mapKey)==null){
					poolDtl.put(mapKey,new BigDecimal(0));
				}
			}
			limitVO.setPoolDtl(poolDtl);
			return limitVO;
		}
		return null;
	}
}
