package com.bytter.check.service;
import com.bytter.biz.finacial.api.constant.FinacialConstant;
import com.bytter.biz.finacial.api.constant.FinacialResourceConstant;
import com.bytter.check.dao.FcCheckInitDao;
import com.bytter.check.entity.FcBankBalanceAdjust;
import com.bytter.check.entity.FcCheckInit;
import com.bytter.check.vo.FcBankBillVo;
import com.bytter.check.vo.FcCheckInitVo;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.utils.BeanUtils;
import com.bytter.core.utils.DateUtil;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.system.api.*;
import com.bytter.system.api.dto.BtBankAccDTO;
import com.bytter.system.api.dto.FcAccDTO;
import com.bytter.system.api.dto.FcCheckInitDTO;
import com.bytter.system.api.dto.UserDTO;
import com.bytter.system.api.vo.FcAccVO;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.*;
/**
* @Title: Service
* @Description: 银行中心余额初始表
* @date 2017-05-31 11:15:24
* @version 1.0
*
*/
@Service("fcCheckInitService")
@Transactional
public class FcCheckInitService extends BaseService<FcCheckInit,String> {
	
	@Autowired
	private FcCheckInitDao fcCheckInitDao;
	@DubboReference(check = false)
	private ICorpService corpService;
	@Autowired
	private FcBankBalanceAdjustService fcBankBalanceAdjustService;
	@DubboReference(check = false)
	private IFcAccService iFcAccService;
	@DubboReference(check = false)
	private IFcCheckInitService iFcCheckInitService;
	@DubboReference(check = false)
	private IBtBankAccService btBankAccService;

	@DubboReference(check = false)
	private IUserService iUserService;
	/**
	* 删除对象
	* @param entity
	*/
	public void deleteEntity(FcCheckInit entity){
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
 	public void save(FcCheckInit entity){
 		super.saveEntity(entity);
 	}
	/**
	* 更新
	* @param entity
	*/
	public void update(FcCheckInit entity){
		super.updateEntity(entity);
	}
	/**
	* 保存or更新
	* @param 
	*/
 	public void saveOrUpdate(FcCheckInitVo fci, String userId){
 		//TODO 进行了余额调节表编制以及结转的账号不能修改初始化日期和余额。
 		FcCheckInit entity = new FcCheckInit();
		UserDTO userDTO = iUserService.getByUserId(userId);
		BeanUtils.copyNotNullProperties(fci, entity);
 		entity.setCheckType(FinacialConstant.CHECK_TYPE_CEN);
 		if(EmptyUtils.isEmpty(entity.getId())){
			entity.setUpdateTime(new Date());
			entity.setCreateTime(new Date());
			 entity.setCreateBy(userDTO.getCreateBy());
			 entity.setUpdateBy(userDTO.getUpdateBy());
			 super.saveEntity(entity);
 		}else{
			 entity.setUpdateBy(userDTO.getUpdateBy());
			 entity.setUpdateTime(new Date());
 			super.updateEntity(entity);
 		}
 	}
 	/**
 	 * 中心对账余额初始化查询初始化信息
 	 */
 	@Transactional(readOnly = true)
 	public FcCheckInitVo fcCheckInit(FcCheckInitVo fci){
		FcCheckInitDTO dto = iFcCheckInitService.FcCheckInitList(fci.getBankAcc(),fci.getAccount());
		if(dto == null){
			FcAccDTO account = iFcAccService.getAccDTOByAccount(fci.getAccount());
			fci.setCurId(account.getCurId());
			return fci;
		}
		FcCheckInit fcCheckInit = fcCheckInitDao.getListByBankAcc(dto.getBankAcc(), dto.getCurId(), "1");
		FcAccVO fcAccVO = iFcAccService.getFcAccVOByBlnctrAcc(fci.getAccount());
		if (fcCheckInit != null ){
			dto.setCheckDate(fcCheckInit.getCheckDate());
			dto.setBlnBalance(fcCheckInit.getBlnBalance());
			dto.setBankBalance(fcCheckInit.getBankBalance());
			dto.setId(fcCheckInit.getId());
			dto.setRmk(fcCheckInit.getRmk());
		}
		if (fcAccVO != null && fcAccVO.getBlnctrAcc().equals(dto.getCounterAcc())){
			dto.setAccName(fcAccVO.getAccName());
			dto.setAccount(fcAccVO.getBlnctrAcc());
		}
		FcCheckInitVo fcCheckInitVo = new FcCheckInitVo();
		BeanUtils.copyNotNullProperties(dto,fcCheckInitVo);
 		return fcCheckInitVo;
 	}
	public BaseDao<FcCheckInit, String> getEntityDao() {
		return fcCheckInitDao;
	}
	
	@Transactional(readOnly = true)
	public DataTablesOutput getFcCheckInit(DataTablesInput dataTablesInput,String checkType,String userId){
		List<String> corpAuth = corpService.getUserCorpIds(FinacialResourceConstant.FINACIAL_CHECK_CENTRALITY_BALANCE_INIT_QUERY,userId);
		List<String> bankAcc = new ArrayList<>();
		List<FcBankBillVo> list = fcCheckInitDao.listFcBankBillVo();
		if(EmptyUtils.isNotEmpty(list)){
			for(FcBankBillVo vo : list){
				bankAcc.add(vo.getBankAcc());
			}
		}
		List<String> acc = new ArrayList<>();
		Map<String,FcCheckInit> mapTemp = new HashMap<>();
		DataTablesOutput output = iFcCheckInitService.getFcCheckInit(dataTablesInput,corpAuth,bankAcc);
		List<Map<String,Object>> fcCheckInitVoList = output.getData();
		for(Map map :fcCheckInitVoList){
			String bankAccStr = map.get("bankAcc").toString();
			acc.add(bankAccStr);
		}
		List<FcCheckInit> listTemp = fcCheckInitDao.getListByBankAcc(acc,checkType);
		for(FcCheckInit fcCheckInit : listTemp){
			mapTemp.put(fcCheckInit.getBankAcc(),fcCheckInit);
		}
		for(Map map :fcCheckInitVoList){
			String bankAccStr = map.get("bankAcc").toString();
			FcCheckInit fcCheckInit = mapTemp.get(bankAccStr);
			if(!EmptyUtils.isEmpty(fcCheckInit)){
				map.put("id",fcCheckInit.getId());
				map.put("checkDate", DateUtil.formatDate(fcCheckInit.getCheckDate(),"yyyy-MM-dd"));
				map.put("blnBalance",fcCheckInit.getBlnBalance());
				map.put("bankBalance",fcCheckInit.getBankBalance());
			}
		}
		return output;
	}
	
	/**
	 * <b>功能介绍：</b>通过账号获取账户余额初始化信息<br>
	 * <b>日期：</b>2017年9月27日<br>
	 * <b>作者：</b>caozx<br>
	 * @version 1.0
	 * @param bankAcc
	 * @param account
	 * @return
	 */
	@Transactional(readOnly = true)
	public FcCheckInit getCheckInitByAccount(String bankAcc, String account,String checkType, String curId) {
        return fcCheckInitDao.getCheckInitByAccount(bankAcc, account, checkType, curId);
    }
	
	/**
	 * <b>功能介绍：</b>保存账户结转日期<br>
	 * <b>日期：</b>2017年10月17日<br>
	 * <b>作者：</b>caozx<br>
	 * @version 1.0
	 * @param bankAcc
	 * @param account
	 * @param transDate
	 */
	public void updateCheckInitCarryDate(String bankAcc, String account, Date transDate,String checkType) {
        fcCheckInitDao.updateCheckInitCarryDate(bankAcc, account, transDate,checkType);
    }
	
	/**
	 * <b>功能介绍：</b>反结转更新<br>
	 * <b>日期：</b>2017年10月17日<br>
	 * <b>作者：</b>caozx<br>
	 * @version 1.0
	 * @param balAdjust
	 */
	public void updateRedoAdjustOver(FcBankBalanceAdjust balAdjust){
    	fcCheckInitDao.updateRedoAdjustOver(balAdjust);
    }
	/**
	 * 判断是否余额调节表编制
	 */
	public boolean checkAdjust(FcCheckInitVo fci) {
		return fcBankBalanceAdjustService.checkIsAdjust(fci.getAccount(), fci.getBankAcc());
	}
	
	/**ERP余额初始化列表查询**/
	public DataTablesOutput queryCheckInitList(DataTablesInput dataTablesInput,String bankAcc, String curId,String checkType,List<String> corpAuth){
		List<String> bankAccList = new ArrayList<>();
		List<FcCheckInitVo> list = fcCheckInitDao.listByErpBasicData();
		Map<String,FcCheckInitVo> fcCheckInitVoMap  = new HashMap<>();
		if(EmptyUtils.isNotEmpty(list)){
			for(FcCheckInitVo vo : list){
				bankAccList.add(vo.getBankAcc());
				fcCheckInitVoMap.put(vo.getBankAcc(),vo);
			}
		}
		Map<String, Object> param = dataTablesInput.getFormdata();
		if(EmptyUtils.isNotEmpty(bankAcc)){
			param.put("eqs_bankAcc",bankAcc);
		}
		if(EmptyUtils.isNotEmpty(curId)){
			param.put("eqs_curId",curId);
		}
		if(!EmptyUtils.isEmpty(param.get("likes_bankAccount"))){
			param.put("likes_bankAcc",param.get("likes_bankAccount"));
		}
		if(!EmptyUtils.isEmpty(param.get("likes_bankAccountName"))){
			param.put("likes_accName",param.get("likes_bankAccountName"));
		}
		DataTablesOutput output = iFcCheckInitService.getFcCheckInitForErp(dataTablesInput,corpAuth,bankAccList);
		List<Map<String,Object>> fcCheckInitVoList = output.getData();
		List<String> acc = new ArrayList<>();
		Map<String,FcCheckInit> mapTemp = new HashMap<>();
		for(Map map :fcCheckInitVoList){
			String bankAccStr = map.get("bankAcc").toString();
			acc.add(bankAccStr);
		}
		List<FcCheckInit> listTemp = fcCheckInitDao.getListByBankAcc(acc,checkType);
		for(FcCheckInit fcCheckInit : listTemp){
			mapTemp.put(fcCheckInit.getBankAcc(),fcCheckInit);
		}
		for(Map map :fcCheckInitVoList){
			String bankAccStr = map.get("bankAcc").toString();
			FcCheckInit fcCheckInit = mapTemp.get(bankAccStr);
			FcCheckInitVo vo = fcCheckInitVoMap.get(bankAccStr);
			if(!EmptyUtils.isEmpty(vo)){
				map.put("erpCode",vo.getErpCode());
				map.put("erpName",vo.getErpName());
			}
			if(!EmptyUtils.isEmpty(fcCheckInit)){
				map.put("id",fcCheckInit.getId());
				map.put("checkDate", DateUtil.formatDate(fcCheckInit.getCheckDate(),"yyyy-MM-dd"));
				map.put("blnBalance",fcCheckInit.getBlnBalance());
				map.put("bankBalance",fcCheckInit.getBankBalance());
			}
		}
		return output;
	}
	/**
	 * 保存or更新
	 * @param
	 */
	public void erpSaveOrUpdate(FcCheckInitVo fci){
		//TODO 进行了余额调节表编制以及结转的账号不能修改初始化日期和余额。
		FcCheckInit entity = new FcCheckInit();
		BeanUtils.copyNotNullProperties(fci, entity);
		entity.setCheckType(FinacialConstant.CHECK_TYPE_ERP);
		if(EmptyUtils.isEmpty(entity.getId())){
			super.saveEntity(entity);
		}else{
			super.updateEntity(entity);
		}
	}
    /**
     * <br>
     * <b>功能：</b> 查询对账账号 <br>
     * <b>作者：</b>liyh<br>
     * <b>日期：</b> 2022/1/7 0007 <br>
     */
    public List<BtBankAccDTO> getInitBankAccsData(String corpId, String userId, String checkType) {
		//获取初始化过的银行账号
		List<FcCheckInit> fcCheckInitList = fcCheckInitDao.getListByBankAcc(null,checkType);
		List<String> bankaccList = new ArrayList<>();
		for(FcCheckInit fcCheckInit : fcCheckInitList){
			BtBankAccDTO btBankAccDTO = btBankAccService.getBankAccByBankAccAndCurId(fcCheckInit.getBankAcc(),fcCheckInit.getCurId());
			if(!EmptyUtils.isEmpty(btBankAccDTO)){
				bankaccList.add(btBankAccDTO.getId());
			}
		}
		Map<String,Object>  map = fcCheckInitDao.getInitBankAccsDataSql(corpId, userId,bankaccList);
        return btBankAccService.listBankAccBySql((String)map.get("sql"),(Map<String, Object>)map.get("map"));
    }
	
}
