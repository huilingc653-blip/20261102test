package com.bytter.check.service;
import com.bytter.biz.finacial.api.constant.CheckConstant;
import com.bytter.biz.finacial.api.constant.FinacialResourceConstant;
import com.bytter.check.enums.CheckMessageEnum;
import com.bytter.check.dao.FcBankBalanceAdjustDao;
import com.bytter.check.entity.FcBankBalanceAdjust;
import com.bytter.check.entity.FcBankCheckLog;
import com.bytter.check.vo.AdjustDetailVO;
import com.bytter.check.vo.FcBankBalanceAdjustVO;
import com.bytter.check.vo.FcBankBillVo;
import com.bytter.check.vo.FcCompareAccVO;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.exception.ParameterException;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.core.utils.StringUtils;
import com.bytter.system.api.IBtBankAccService;
import com.bytter.system.api.IBtCurrencyService;
import com.bytter.system.api.ICorpService;
import com.bytter.system.api.IFcAccService;
import com.bytter.system.api.constant.CorpConstant;
import com.bytter.system.api.dto.*;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
* @Title: Service
* @Description: 对账余额调节表
* @author onlineGenerator
* @date 2017-06-02 15:29:08
* @version 1.0
*
*/
@Service("fcBankBalanceAdjustService")
@Transactional
public class FcBankBalanceAdjustService extends BaseService<FcBankBalanceAdjust,String> {
	@Autowired
	private FcBankBalanceAdjustDao fcBankBalanceAdjustDao;
	@Autowired
	private FcCheckInitService fcCheckInitService;
	@Autowired
	private FcBankBillService bankBillService;
	@Autowired
	private FcBankCheckDtlService bankCheckDtlService;
	@Autowired
	private FcCompareCenterService compareAccService;
	@Autowired
	private FcBankCheckLogService bankCheckLogService;
	@DubboReference(check = false)
	private ICorpService iCorpService;
	@DubboReference(check = false)
	private IBtBankAccService btBankAccService;
	@DubboReference(check = false)
	private IFcAccService fcAccService;
	@DubboReference(check = false)
	private IBtCurrencyService iBtCurrencyService;
	/**
	* 删除对象
	* @param entity
	*/
	public void deleteEntity(FcBankBalanceAdjust entity){
		super.deleteEntity(entity);
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
	* @param entity
	*/
 	public void save(FcBankBalanceAdjust entity){
 		super.saveEntity(entity);
 	}
	/**
	* 更新
	* @param entity
	*/
	public void update(FcBankBalanceAdjust entity){
		super.updateEntity(entity);
	}
	/**
	* 保存or更新
	* @param entity
	*/
 	public void saveOrUpdate(FcBankBalanceAdjust entity){
 		super.saveOrUpdate(entity);
 	}
	public BaseDao<FcBankBalanceAdjust, String> getEntityDao() {
		return fcBankBalanceAdjustDao;
	}
	
	/**
	 * <b>功能介绍：</b>余额调节表列表数据<br>
	 * <b>日期：</b>2017年10月17日<br>
	 * <b>作者：</b>caozx<br>
	 * @version 1.0
	 * @param input
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	@Transactional(readOnly = true)
	public DataTablesOutput listFcBankBalanceAdjust(DataTablesInput input,String type,String userId){
		List<String> corpAuth = new ArrayList<>();
		//查询账户
		List<String> accS = new ArrayList<>();
		String checkType = (String) input.getFormdata().get("eqs_checkType");
		String eqs_corpId = (String) input.getFormdata().get("eqs_corpId");
		if(EmptyUtils.isEmpty(checkType)){
			checkType = "0";
			input.getFormdata().put("eqs_checkType",checkType);
		}
		if("1".equals(checkType)){
			if (null != input.getFormdata() && StringUtils.isNotEmpty(input.getFormdata().get("eqs_netId"))) {
				corpAuth.add(input.getFormdata().get("eqs_netId").toString());
				input.getFormdata().remove("eqs_netId");
			}else{
				if("1".equals(type)){//反结转查询
					corpAuth = iCorpService.getUserCorpIds(FinacialResourceConstant.FINACIAL_CHECK_OPPOSITE_CARRYOVER_QUERY,userId);
				}else if("2".equals(type)){//对账查询
					corpAuth = iCorpService.getUserCorpIds(FinacialResourceConstant.FINACIAL_CHECK_BANK_ENTERPRISE_QUERY,userId);
				}else if("3".equals(type)){//余额调节表查询
					corpAuth = iCorpService.getUserCorpIds(FinacialResourceConstant.FINACIAL_CHECK_BALANCE_ADJUST_QUERY,userId);
				}
			}
			if(EmptyUtils.isNotEmpty(eqs_corpId)){
				corpAuth = new ArrayList<>();
				corpAuth.add(eqs_corpId);
			}
			List<BtBankAccDTO> accList = btBankAccService.getListByCorpIds(corpAuth);
			for (BtBankAccDTO acc : accList) {
				accS.add(acc.getBankAcc());
			}
		}else {
			if("1".equals(type)){//反结转查询
				corpAuth = iCorpService.getUserCorpIds(FinacialResourceConstant.FINACIAL_CHECK_OPPOSITE_CARRYOVER_QUERY,userId);
			}else if("2".equals(type)){//对账查询
				corpAuth = iCorpService.getUserCorpIds(FinacialResourceConstant.FINACIAL_CHECK_BANK_ENTERPRISE_QUERY,userId);
			}else if("3".equals(type)){//余额调节表查询
				corpAuth = iCorpService.getUserCorpIds(FinacialResourceConstant.FINACIAL_CHECK_BALANCE_ADJUST_QUERY,userId);
			}
			if(EmptyUtils.isNotEmpty(eqs_corpId)){
				corpAuth = new ArrayList<>();
				corpAuth.add(eqs_corpId);
			}
			List<BtBankAccDTO> accList = btBankAccService.getListByCorpIds(corpAuth);
			for (BtBankAccDTO acc : accList) {
				accS.add(acc.getBankAcc());
			}
		}
		DataTablesOutput output = fcBankBalanceAdjustDao.listFcBankBalanceAdjust(input,accS);
		List<FcBankBalanceAdjustVO> voList=output.getData();
		if (voList.size() > 0) {
			Map<String, BtCurrencyDTO> curMap = iBtCurrencyService.getCurrency();
			for (FcBankBalanceAdjustVO vo : voList) {
				vo.setCurName(curMap.get(vo.getCurCode()).getCurName());
				BtBankAccDTO btBankAccDTO = btBankAccService.getBankAccInfo(vo.getBankAcc());
				if(!EmptyUtils.isEmpty(btBankAccDTO)){
					vo.setCorpName(btBankAccDTO.getCorpName());
				}
			}
		}
		return output;
	}
	
	/**
	 * <b>功能介绍：</b>查询余额调节表<br>
	 * <b>日期：</b>2017年10月17日<br>
	 * <b>作者：</b>caozx<br>
	 * @version 1.0
	 * @param adIds
	 * @return
	 */
	@Transactional(readOnly = true)
	public List<FcBankBalanceAdjust> getFcBankBalanceAdjust(String[] adIds){
		return fcBankBalanceAdjustDao.getFcBankBalanceAdjust(adIds);
	}
	
	/**
	 * <b>功能介绍：</b>反结转操作<br>
	 * <b>日期：</b>2017年10月17日<br>
	 * <b>作者：</b>caozx<br>
	 * @version 1.0
	 * @param ids
	 */
	public void doRedoAdjustOver(String ids,ICurrentUser user){
        long time0 = System.currentTimeMillis(); // 开始时间
		String[] adId = ids.split(",");
		List<FcBankBalanceAdjust> balAdjs = this.getFcBankBalanceAdjust(adId);
		//检查是否是最新的结转记录
        List<FcBankBalanceAdjustVO> othreAdjusts = fcBankBalanceAdjustDao.queryBehindAdjusts(adId);
        if (EmptyUtils.isNotEmpty(othreAdjusts) && null != othreAdjusts && othreAdjusts.size() > 0) {
            throw new ParameterException(CheckMessageEnum.FIN_CHECK_BANKACC_ADJUST_BEHIND);
        }
        List<FcBankCheckLog> checkLogs = new ArrayList<>();
		for (FcBankBalanceAdjust balAdjust : balAdjs) {
			//更新对账银行结转日期
			fcCheckInitService.updateRedoAdjustOver(balAdjust);
			String paramType = balAdjust.getCheckType();
			List<FcBankCheckLog> checkLog = bankCheckLogService.getCheckLogByBillId(balAdjust.getId());
			FcBankCheckLog log = compareAccService.buildCheckLog(balAdjust.getBankAcc(), balAdjust.getAccount(), CheckConstant.OPTYPE_REDO_TYPE_OVER, paramType, EmptyUtils.isEmptyList(checkLog)?" ":checkLog.get(0).getCheckFlag(),user);
            checkLogs.add(log);
		}
        //更新此次对账明细为未对账
        List<Map<String, String>> bankIdList = bankBillService.getOverBankBillId(adId);
        List<String> bankIds = new ArrayList<>();
        for(Map<String, String> idMap : bankIdList){
            bankIds.add(idMap.get("ID"));
        }
        int bankCount = bankBillService.updateRedoAdjustOver(bankIds);
        //bankCheckDtlService.updateBankRedoAdjustOver(adId);//删除银行对账单对账日志明细
        List<Map<String, String>> accIdList = compareAccService.getOverAccBillId(adId);
        List<String> accIds = new ArrayList<>();
        for(Map<String, String> idMap : accIdList){
            accIds.add(idMap.get("ID"));
        }
        int accCount = compareAccService.updateRedoAdjustOver(accIds);
        //bankCheckDtlService.updateAccRedoAdjustOver(adId);//删除中心对账单对账日志明细
        //bankCheckLogService.updateRedoCheckLog(adId);//删除对账日志记录
        bankCheckLogService.batchSave(checkLogs);//批量插入反结转日志
        //判断执行成功的条数需等于执行反结转的条数
		int count = fcBankBalanceAdjustDao.doDeleteBalanceAdjust(adId,user.getLoginName());//删除余额调节表数据
        if (count != adId.length) {
            throw new ParameterException(CheckMessageEnum.FIN_CHECK_BANKACC_ADJUST_CHANGED);
        }
        fcBankBalanceAdjustDao.doDeleteBalanceAdjustDtls(adId, user.getLoginName());
	}
	
	/**
	 * <b>功能介绍：</b>查询余额调节表<br>
	 * <b>日期：</b>2017年10月18日<br>
	 * <b>作者：</b>caozx<br>
	 * @version 1.0
	 * @param ajId
	 * @return
	 */
	@Transactional(readOnly = true)
	public Map<String, Object> toLoadBalanceAdjustDetails(String ajId){
		Map<String, Object> info = new HashMap<String, Object>();
		FcBankBalanceAdjustVO balAdj = fcBankBalanceAdjustDao.getFcBankBalanceAdjustById(ajId);
		if(null != balAdj){
			BtBankAccDTO bankAcc=btBankAccService.getBankAccInfo(balAdj.getBankAcc());
			if(null != bankAcc){
				info.put("accName", bankAcc.getAccName());
				info.put("bankName", bankAcc.getBankName());
				info.put("bankAcc", bankAcc.getBankAcc());
			}
			Map<String, BtCurrencyDTO> curMap = iBtCurrencyService.getCurrency();
			info.put("curName", curMap.get(balAdj.getCurCode()).getCurName());
		}
		info.put("transDate", balAdj.getEndDate());
		return info;
	}
	
	/**
	 * <b>功能介绍：</b>查询余额调节表<br>
	 * <b>日期：</b>2017年10月18日<br>
	 * <b>作者：</b>caozx<br>
	 * @version 1.0
	 * @param ajId
	 * @return
	 */
	@Transactional(readOnly = true)
	public FcBankBalanceAdjustVO getFcBankBalanceAdjustById(String ajId){
		FcBankBalanceAdjustVO balAdj = fcBankBalanceAdjustDao.getFcBankBalanceAdjustById(ajId);
		if(null != balAdj){
			BtBankAccDTO bankAcc=btBankAccService.getBankAccInfo(balAdj.getBankAcc());
			if(null != bankAcc){
				balAdj.setAccName(bankAcc.getAccName());
				balAdj.setBankName(bankAcc.getBankName());
				balAdj.setBankAcc(bankAcc.getBankAcc());
			}
			Map<String, BtCurrencyDTO> curMap = iBtCurrencyService.getCurrency();
			balAdj.setCurName( curMap.get(balAdj.getCurCode()).getCurName());
		}
		return balAdj;
	}
	
	/**
	 * <b>功能介绍：</b>查询余额调节表明细<br>
	 * <b>日期：</b>2017年10月19日<br>
	 * <b>作者：</b>caozx<br>
	 * @version 1.0
	 * @param dataType
	 * @param queryType
	 * @param adjId
	 * @return
	 */
	@Transactional(readOnly = true)
	public List<AdjustDetailVO> queryAdjustDetaiData(String dataType, String queryType, String adjId){
		FcBankBalanceAdjust adjust = this.get(adjId,true);
		return fcBankBalanceAdjustDao.queryAdjustDetaiData(dataType, queryType,adjust.getCheckType(),adjId);
	}
	
	/**
	 * <b>功能介绍：</b>查询中心(ERP)结转明细清单<br>
	 * <b>日期：</b>2017年10月20日<br>
	 * <b>作者：</b>caozx<br>
	 * @version 1.0
	 * @return
	 */
	@Transactional(readOnly = true)
	public List<FcCompareAccVO> queryCntrOverDatas(String eqs_ajustId, String eqs_stayRadios, String eqs_handRadios, String eqs_checkRadios){
		String ajustId = eqs_ajustId;
    	String stayRadios = eqs_stayRadios;
    	String handRadios = eqs_handRadios;
    	String checkRadios = eqs_checkRadios;
    	FcBankBalanceAdjust adj = this.get(ajustId,true);
    	//if(FinacialConstant.BOOLEAN_FALSE.equals(adj.getCheckType())){//中心对账单
    		return compareAccService.queryCntrOverDatas(ajustId, stayRadios, handRadios, checkRadios);
//    	}else{//ERP对账单
//    		return null;
//    	}
	}
	
	/**
	 * <b>功能介绍：</b>查询银行结转对账数据<br>
	 * <b>日期：</b>2017年10月20日<br>
	 * <b>作者：</b>caozx<br>
	 * @version 1.0
	 * @return
	 */
	@Transactional(readOnly = true)
	public List<FcBankBillVo> queryBankOverDatas(String ajustId, String stayRadios, String handRadios, String checkRadios){
		return bankBillService.queryBankOverDatas( ajustId, stayRadios, handRadios, checkRadios);
	}
	
	/**
	 * 判断账号是否进行了余额调节表
	 */
	@Transactional(readOnly = true)
	public boolean checkIsAdjust(String account , String bankAcc){
		return fcBankBalanceAdjustDao.checkIsAdjust(account , bankAcc);
	}
	
	/**
	 * <b>功能介绍：</b>没有权限的网点<br>
	 * <b>日期：</b>2017年11月13日<br>
	 * <b>作者：</b>caozx<br>
	 * @version 1.0
	 * @return
	 */
	public String getNotAuthNets(String resouce,String userId){
		List<CorpDTO> authCorps = iCorpService.listUserCorp(resouce,userId);
		List<String> authNets = new ArrayList<String>();
		List<CorpDTO> allNet = iCorpService.listNet();
		List<String> allNets = new ArrayList<String>();
		String notAuthNet = "";
		for (CorpDTO corp : authCorps) {
			if(CorpConstant.CORP_TYPE_CODE_NET.equals(corp.getType())){
				authNets.add(corp.getId());
			}
		}
		for (CorpDTO net : allNet) {
			allNets.add(net.getId());
		}
		
		allNets.removeAll(authNets);
		for (String corpId : allNets) {
			notAuthNet += corpId +",";
		}
		return notAuthNet;
	}
	
}
