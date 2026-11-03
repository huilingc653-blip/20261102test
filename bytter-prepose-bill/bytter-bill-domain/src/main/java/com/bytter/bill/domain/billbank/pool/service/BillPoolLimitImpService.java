package com.bytter.bill.domain.billbank.pool.service;
import com.bytter.bill.api.bill.constant.AccebillConstant;
import com.bytter.bill.domain.billbank.pool.dao.BillPoolLimitImpDao;
import com.bytter.bill.domain.billbank.pool.entity.AbPoolLimitAllotcheck;
import com.bytter.bill.domain.billbank.pool.entity.AbPoolLimitApplycheck;
import com.bytter.bill.domain.billbank.pool.utils.BillPoolConstant;
import com.bytter.bill.domain.billbank.pool.vo.PoolBaseVO;
import com.bytter.bill.domain.billbank.pool.vo.PoolLimitVO;
import com.bytter.bill.domain.billbank.util.StringUtil;
import com.bytter.core.utils.BeanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.*;
/**
 * <br>
 * <b>功能：</b>额度操作业务处理类<br>
 * <b>作者：</b>郑刚<br>
 * <b>日期：</b> 2020-02-24 <br>
 */
@Service("billPoolLimitImpService")
@Transactional
public class BillPoolLimitImpService {
	private Logger logger= LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private BillPoolImpService billPoolImpService;
	
	@Autowired
	private BillPoolBankExcImpService billPoolBankExcImpService;
	
	@Autowired
	private BillPoolManageImpService billPoolManageImpService;
	@Autowired
	private BillPoolLimitImpDao billPoolLimitImpDao;
	/**
	 * <b>功能：</b>获取待发送额度操作数据<br>
	 * <b>作者：</b>郑刚<br>
	 * <b>日期：</b> 2020-02-24 <br>
	 * @param type 业务类型
	 * @param bifCode 银行接口代码
	 * @param reCode 银行返回代码
	 * @return
	 */
	public List<PoolLimitVO> queryLimitByBifCode(String type,String bifCode,String reCode){
		if (logger.isDebugEnabled()) {
			logger.debug("type="+type+",bifCode="+bifCode+" ,reCode="+reCode);
		}
		return billPoolLimitImpDao.queryLimitInfo(type, BillPoolConstant.optStatus_16, bifCode, reCode);
	}
	
	/**
	 * <b>功能：</b>获取等待结果额度数据<br>
	 * <b>作者：</b>郑刚<br>
	 * <b>日期：</b> 2020-02-24 <br>
	 * @param type 业务类型
	 * @param bifCode 银行接口代码
	 * @param reCode 银行返回代码
	 * @return
	 */
	public List<PoolLimitVO> queryLimitResultByBifCode(String type,String bifCode,String reCode){
		if (logger.isDebugEnabled()) {
			logger.debug("type="+type+",bifCode="+bifCode+" ,reCode="+reCode);
		}
		return billPoolLimitImpDao.queryLimitInfo(type, BillPoolConstant.optStatus_18, bifCode, reCode);
	}
	/**
	 * <b>功能：修改额度的指令状态</b><br>
	 * <b>作者：</b>郑刚<br>
	 * <b>日期：</b> 2020-02-24 <br>
	 * @param type 业务类型
	 * @param paramsVo 修改数据的参数
	 * @return
	 */
	public void updatePoolLimit(String type,PoolLimitVO paramsVo){
		if (logger.isDebugEnabled()) {
			logger.debug("type="+type+",paramsVo="+paramsVo.toString());
		}
		if(StringUtil.isNotEmpty(paramsVo.getBusId())){
			billPoolBankExcImpService.updateBillPoolBankExc(paramsVo);
			PoolLimitVO info=queryLimitById(type, paramsVo.getBusId());
			if(StringUtil.isNotEmpty(paramsVo.getOptStatus())
					&&!paramsVo.getOptStatus().equals(info.getOptStatus())){
				billPoolLimitImpDao.updateLimitOptStatusByBusId(type, paramsVo.getBusId(), paramsVo.getOptStatus(),paramsVo.getBillId());
				if(AccebillConstant.AB_BILL_OPT_STATUS19.equals(paramsVo.getOptStatus())){
//					获取所属单位的集团单位
					PoolBaseVO corpInfo=billPoolManageImpService.getTopLevelCorp();
					if(corpInfo!=null){//对顶级集团单位生成额度查询指令
						billPoolManageImpService.saveBillPoolQueryExc(info.getBpId(), corpInfo.getCorpId(), BillPoolConstant.POOL_EXC_QUERY_TYPE_LIMIT);
					}
					billPoolManageImpService.saveBillPoolQueryExc(info.getBpId(), info.getCorpId(), BillPoolConstant.POOL_EXC_QUERY_TYPE_LIMIT);
				}
			}
		}
	}
	/**
	 * <b>功能：新增复核额度的信息</b><br>
	 * <b>作者：</b>郑刚<br>
	 * <b>日期：</b> 2020-02-24 <br>
	 * @param type 业务类型
	 * @param paramsVo 新增数据的参数
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public void savePoolLimitCheck(String type,List<PoolLimitVO> paramsVo) {
		if (logger.isDebugEnabled()) {
			logger.debug("type="+type+",paramsVo="+paramsVo.toString());
		}
		if(paramsVo!=null&&paramsVo.size()>0){
			PoolBaseVO poolInfo=billPoolImpService.queryPoolByPoolNo(paramsVo.get(0).getPoolNo(),paramsVo.get(0).getBifCode());
			Map<String,String> busTypeMap=new HashMap<String,String>();
			Map<String,String> corpMap=new HashMap<String,String>();
			if(poolInfo!=null){
				if(BillPoolConstant.LIMIT_TYPE_APPLYCHECK.equals(type)){
					List<AbPoolLimitApplycheck> addList=new ArrayList<AbPoolLimitApplycheck>();
					for(PoolLimitVO info:paramsVo){
						info.setPoolNo(poolInfo.getBusId());
						AbPoolLimitApplycheck checkInfo=new AbPoolLimitApplycheck();
						String corpId=billPoolImpService.getCorpId(info.getApplyCorpName(), info.getApplyBankOrgCode(), corpMap);
						if(StringUtil.isEmpty(corpId)){
							continue;
						}
						checkInfo.setCorpId(corpId);
						info.setCorpId(corpId);
						if(StringUtil.isNotEmpty(info.getBustypeDtlCode())){
							String busTypeId=getBusTypeId(info.getBustypeCode(), info.getBustypeDtlCode(), busTypeMap);
							if(StringUtil.isEmpty(busTypeId)){
								continue;
							}
							checkInfo.setBustypeId(busTypeId);
							info.setBusId(busTypeId);
						}
						//判断是否操作重复数据存在则不入库存（单位、金额、业务类型、风险类型、申请日期、结束日期 存在且业务未结束数据 ）
						if(billPoolLimitImpDao.isLimitCheckRepeated(type, info)){
							continue;
						}
						BeanUtils.copyNotNullProperties(info, checkInfo);
						checkInfo.setId(UUID.randomUUID().toString().replace("-", ""));
						checkInfo.setOptStatus(null);
						checkInfo.setStatus(null);
						checkInfo.setLimtType(info.getLimitType());
						checkInfo.setBpId(poolInfo.getBusId());
						checkInfo.setApplyMoney(info.getMoney());
						checkInfo.setCreateTime(info.getAppDate());
						checkInfo.setEndDate(info.getEndDate());
						addList.add(checkInfo);
						info.setBusId(checkInfo.getId());
						info.setReCode(null);
						info.setReMsg(null);
						billPoolBankExcImpService.updateBillPoolBankExc(info);
					}
					if(addList.size()>0){
						billPoolLimitImpDao.batchJdbcSave(addList);
					}
					
				}else if(BillPoolConstant.LIMIT_TYPE_ALLOTCHECK.equals(type)){
					List<AbPoolLimitAllotcheck> addList=new ArrayList<AbPoolLimitAllotcheck>();
					for(PoolLimitVO info:paramsVo){
						info.setPoolNo(poolInfo.getBusId());
						AbPoolLimitAllotcheck checkInfo=new AbPoolLimitAllotcheck();
						String corpId=billPoolImpService.getCorpId(info.getApplyCorpName(), info.getApplyBankOrgCode(), corpMap);
						if(StringUtil.isEmpty(corpId)){
							continue;
						}
						checkInfo.setCorpId(corpId);
						info.setCorpId(corpId);
						if(StringUtil.isNotEmpty(info.getBustypeDtlCode())){
							String busTypeId=getBusTypeId(info.getBustypeCode(), info.getBustypeDtlCode(), busTypeMap);
							if(StringUtil.isEmpty(busTypeId)){
								continue;
							}
							checkInfo.setBustypeId(busTypeId);
							info.setBusId(busTypeId);
						}
						//判断是否操作重复数据存在则不入库存（单位、金额、业务类型、风险类型、申请日期、结束日期 存在且业务未结束数据 ）
						if(billPoolLimitImpDao.isLimitCheckRepeated(type, info)){
							continue;
						}
						BeanUtils.copyNotNullProperties(info, checkInfo);
						checkInfo.setId(UUID.randomUUID().toString().replace("-", ""));
						checkInfo.setOptStatus(null);
						checkInfo.setStatus(null);
						checkInfo.setLimtType(info.getLimitType());
						checkInfo.setBpId(poolInfo.getBusId());
						checkInfo.setApplyMoney(info.getMoney());
						checkInfo.setCreateTime(info.getAppDate());
						checkInfo.setEndDate(info.getEndDate());
						addList.add(checkInfo);
						info.setBusId(checkInfo.getId());
						info.setReCode(null);
						info.setReMsg(null);
						billPoolBankExcImpService.updateBillPoolBankExc(info);
					}
					if(addList.size()>0){
						billPoolLimitImpDao.batchJdbcSave(addList);
					}
				}
			}
		}
	}
	
	/**
	 * <b>功能：</b>根据业务ID获取数据<br>
	 * <b>作者：</b>郑刚<br>
	 * <b>日期：</b> 2020-02-24 <br>
	 * @param type 业务类型
	 * @param busId 业务ID
	 * @return
	 */
	public PoolLimitVO queryLimitById(String type,String busId){
		if (logger.isDebugEnabled()) {
			logger.debug("type="+type+",busId="+busId);
		}
		if(StringUtil.isNotEmpty(busId)){
			List<PoolLimitVO> list=billPoolLimitImpDao.queryLimitInfo(type, busId);
			if(list!=null&&list.size()>0){
				return list.get(0);
			}
		}
		return null;
	}
	
	/**
	 * 根据业务类型代码获取业务类型ID
	 * @param busTypeCode 业务类型上级代码
	 * @param busTypeCodeDtl 业务类型明细代码
	 * @param busTypeMap 业务类型map
	 * @return
	 */
	public String getBusTypeId(String busTypeCode,String busTypeCodeDtl,Map<String,String> busTypeMap){
		if(StringUtil.isNotEmpty(busTypeCode)
				||StringUtil.isNotEmpty(busTypeCodeDtl)){
			String key=busTypeCode+"-"+busTypeCodeDtl;
			if(busTypeMap.containsKey(key)){
				return busTypeMap.get(key);
			}else{
				String busTypeId=billPoolImpService.getBusTypeIdByBusTypeCode(busTypeCode, busTypeCodeDtl);
				busTypeMap.put(key, busTypeId);
				return busTypeId;
			}
		}
		return null;
	}
}
