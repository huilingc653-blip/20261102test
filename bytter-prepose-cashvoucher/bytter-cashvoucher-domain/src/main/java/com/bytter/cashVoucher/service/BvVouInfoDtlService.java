package com.bytter.cashVoucher.service;

import cn.hutool.core.util.ObjectUtil;
import com.bytter.bill.api.bill.constant.BlankvouConstant;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.utils.StringUtils;
import com.bytter.cashVoucher.dao.BvVouInfoDtlDao;
import com.bytter.cashVoucher.entity.BvVouEmployInfo;
import com.bytter.cashVoucher.entity.BvVouInfoDtl;
import com.bytter.cashVoucher.vo.BvVouInfoDtlVo;
import com.bytter.system.api.ICorpService;
import com.bytter.system.api.IDictService;
import com.bytter.system.api.IUserService;
import com.bytter.system.api.dto.DictDTO;
import com.bytter.system.api.dto.UserDTO;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
/**
* @Title: Service
* @Description: 凭证入库明细表
* @author onlineGenerator
* @date 2017-06-07 15:42:20
* @version 1.0
*
*/
@Service("bvVouInfoDtlService")
@Transactional
public class BvVouInfoDtlService extends BaseService<BvVouInfoDtl,String> {
	@Autowired
	private BvVouInfoDtlDao bvVouInfoDtlDao;
	@Autowired
	private BvLogService bvLogService;
	@Autowired
	private BvVouEmployInfoService bvVouEmployInfoService;
	@DubboReference(check = false)
	private ICorpService corpService;
	@DubboReference(check = false)
	private IUserService userService;
	@DubboReference(check = false)
	private IDictService dictService;
	/**
	 * 查询入库凭证明细
	 * @param input
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public DataTablesOutput getDataTablesUnion(DataTablesInput input,ICurrentUser userVo) {
		List<String> corpAuthList = corpService.getUserCorpIds(BlankvouConstant.AB_BILL_RESOURCE_BVVOUINFODTL_QUERY,input.getCurrentUserId());
		// 凭证冗余
		DataTablesOutput<BvVouInfoDtlVo> dataTablesUnion = bvVouInfoDtlDao.getDataTablesUnion(input, corpAuthList, userVo.getId());
		redactData(dataTablesUnion.getData());
		return dataTablesUnion;
	}
	/**
	 * 冗余凭证信息。
	 *
	 * @param dataTables 凭证列表。
	 */
	private void redactData(List<BvVouInfoDtlVo> dataTables) {
		if (ObjectUtil.isNotEmpty(dataTables)){
			Map<String, DictDTO> listMap = dictService.listMap("bvvouinfodtl_voustatus");
			if (ObjectUtil.isNotEmpty(listMap)){
				dataTables.forEach(vouInfo->{
					if (ObjectUtil.isNotEmpty(vouInfo.getVouStatus())){
						Optional.ofNullable(listMap.get(vouInfo.getVouStatus())).ifPresent(dict->vouInfo.setVouStatusName(dict.getName()));
					}
				});
			}
		}
	}
	/**
	 * 根据id查询单条入库凭证明细，用于使用界面数据填充
	 * @param id
	 * @return
	 */
	public BvVouInfoDtlVo getDataTablesRow(String id) {
		BvVouInfoDtlVo vo = new BvVouInfoDtlVo();
		if(StringUtils.isNotEmpty (id)){
			List<BvVouInfoDtlVo> list = bvVouInfoDtlDao.getDataTablesRow(id);
			redactData(list);
			if(list!=null && !list.isEmpty ()){
				vo = list.get (0);
			}
		}
		return vo;
	}
	
	/**
	* 删除对象
	* @param entity
	*//*
	public void deleteEntity(BvVouInfoDtl entity){
		super.deleteEntity(entity);
	}
	*//**
	* 根据id删除
	* @param id
	*//*
 	public void delete(String id){
 		super.delete(id);
 	}
	*//**
	* 保存
	* @param entity
	*//*
 	public void save(BvVouInfoDtl entity){
 		super.saveEntity(entity);
 	}
	*//**
	* 更新
	* @param entity
	*/
	public boolean update(BvVouInfoDtl entity,ICurrentUser userVo){
		entity.setUpdateBy(userVo.getLoginName());
		entity.setUpdateDate(new Date());
		entity.setUpdateName(userVo.getUsername());
		super.updateEntity(entity);
		return true;
	}
	
	/**
	* 保存or更新
	* @param
	*//*
 	public void saveOrUpdate(BvVouInfoDtl entity){
 		super.saveOrUpdate(entity);
 	}*/
	public BaseDao<BvVouInfoDtl, String> getEntityDao() {
		return bvVouInfoDtlDao;
	}
	
	/**
	 * 操作更新
	 * @param id
	 * @param operate 02出售；03使用；04退票；05丢失;06作废
	 * @param reason 退票或作废原因
	 */
	public boolean operateUpdate(String id,String reason,String operate,ICurrentUser userVo){
		BvVouInfoDtl bvVouInfoDtl = get(id);
		bvVouInfoDtl.setVouStatus(operate);
		bvVouInfoDtl.setReason(reason);
		boolean flag = update(bvVouInfoDtl,userVo);
		if(flag){
			String logType = "";
			if(BlankvouConstant.BVVOUINFODTL_STATUS_SELL.equals(operate)){
				logType = BlankvouConstant.BVLOG_LOGTYPE_SELL;
			}else if(BlankvouConstant.BVVOUINFODTL_STATUS_USE.equals(operate)){
				logType = BlankvouConstant.BVLOG_LOGTYPE_USE;
			}else if(BlankvouConstant.BVVOUINFODTL_STATUS_RETREAT.equals(operate)){
				logType = BlankvouConstant.BVLOG_LOGTYPE_BACKBILL;
			}else if(BlankvouConstant.BVVOUINFODTL_STATUS_LOSE.equals(operate)){
				logType = BlankvouConstant.BVLOG_LOGTYPE_LOSE;
			}else if(BlankvouConstant.BVVOUINFODTL_STATUS_TOVOID.equals(operate)){
				logType = BlankvouConstant.BVLOG_LOGTYPE_CANCELLATION;
			}
			flag = bvLogService.saveBvLog(id, logType, BlankvouConstant.LANG_LOG_UPDATE_SUCC,userVo);
		}
		return flag;
	}
	
	/**
	* 批量操作更新
	* @param ids
	* @param reason 退票或作废原因
	* @param operate 02出售；03使用；04退票；05丢失;06作废
	*/
	public boolean batchOperateUpdate(String ids,String reason,String operate,ICurrentUser userVo){
		Map<String, Object> params=new HashMap<>();
		params.put("ids", ids.split(","));
		params.put("reason",reason);
		params.put("vouStatus", operate);
		int num = bvVouInfoDtlDao.batchDeleteBvVouInfoDtl(params,userVo);
		if(num>0){
			String logType = "";
			if(BlankvouConstant.BVVOUINFODTL_STATUS_SELL.equals(operate)){
				logType = BlankvouConstant.BVLOG_LOGTYPE_SELL;
			}else if(BlankvouConstant.BVVOUINFODTL_STATUS_USE.equals(operate)){
				logType = BlankvouConstant.BVLOG_LOGTYPE_USE;
			}else if(BlankvouConstant.BVVOUINFODTL_STATUS_RETREAT.equals(operate)){
				logType = BlankvouConstant.BVLOG_LOGTYPE_BACKBILL;
			}else if(BlankvouConstant.BVVOUINFODTL_STATUS_LOSE.equals(operate)){
				logType = BlankvouConstant.BVLOG_LOGTYPE_LOSE;
			}else if(BlankvouConstant.BVVOUINFODTL_STATUS_TOVOID.equals(operate)){
				logType = BlankvouConstant.BVLOG_LOGTYPE_CANCELLATION;
			}
			String[] msgs = new String[ids.split(",").length];
			for(int i=0;i<ids.split(",").length;i++){
				msgs[i] = BlankvouConstant.LANG_LOG_UPDATE_SUCC;
			}
			bvLogService.batchSaveBvLog(ids.split(","), logType, msgs,userVo);
			return true;
		}
		return false;
	}
	
	/**
	* 批量移库
	* @param ids
	* @param sboxId 移库库箱
	*/
	public boolean batchTransf(String ids,String sboxId,ICurrentUser userVo){
		Map<String, Object> params=new HashMap<>();
		params.put("ids", ids.split(","));
		params.put("sboxId", sboxId);
		int num = bvVouInfoDtlDao.batchTransfBvVouInfoDtl(params,userVo);
		if(num>0){
			String[] msgs = new String[ids.split(",").length];
			for(int i=0;i<ids.split(",").length;i++){
				msgs[i] = BlankvouConstant.LANG_LOG_BATCHTRANSF_SUCC;
			}
			bvLogService.batchSaveBvLog(ids.split(","), BlankvouConstant.BVLOG_LOGTYPE_TRANSFER, msgs,userVo);
			return true;
		}
		return false;
	}
	
	/**
	* 判断用户有管理权限的库箱里是否有对应凭证号
	* @param loginName 登录用户名
	* @param vouTid    凭证类型ID
	* @param vouNo     凭证号
	* @return boolean 存在返回true,否则返回false
	*/
	@Transactional(readOnly = true)
	public boolean checkVouNo(String loginName, String vouTid, String vouNo){
		// 从Sys远程获取用户信息
		UserDTO userDTO = userService.getByLoginName(loginName);
		return bvVouInfoDtlDao.checkVouNo(userDTO.getId(), vouTid, vouNo);
	}
	
	/**
	* 获取这个用户有管理权限的库箱里面对应的凭证类型的凭证号,按照升序获取，如果传递了凭证号需要判断凭证号需要大于这个凭证号
	* @param loginName    登录用户名
	* @param vouTid       凭证类型ID
	* @param vouNo        凭证号
	* @param vouNum       凭证号数量
	* @param bankTypeName 所属银行
	* @param bankAcc      所属账号
	* @param corpId       单位id
	* @return boolean
	*/
	@Transactional(readOnly = true)
	public List<BvVouInfoDtl> getBvVouInfoDtlByLoginName(String loginName, String vouTid, String vouNo, int vouNum,String bankTypeName,String bankAcc,String corpId){
		// 从Sys远程获取用户信息
		UserDTO userDTO = userService.getByLoginName(loginName);
		return  bvVouInfoDtlDao.getBvVouInfoDtls(userDTO.getId(), vouTid, vouNo,vouNum,bankTypeName,bankAcc,corpId);
	}
	@Transactional(readOnly = true)
	public List<BvVouInfoDtl> getBvVouInfoDtlByLoginName(String loginName, String vouTid, int vouNum,String bankTypeName,String bankAcc,String corpId){
		// 从Sys远程获取用户信息
		UserDTO userDTO = userService.getByLoginName(loginName);
		return  bvVouInfoDtlDao.getBvVouInfoDtls(userDTO.getId(), vouTid, null,vouNum,bankTypeName,bankAcc,corpId);
	}
	
	/**
	 * 凭证号自动占用接口（根据凭证使用对象里面的内容自动占用凭证号，填写日志以及入库使用表）
	 * @param bvVouEmployInfo 凭证使用对象
	 */
	public void voucherOccupy(BvVouEmployInfo bvVouEmployInfo,ICurrentUser userVo){
		if(operateUpdate(bvVouEmployInfo.getVoudtlId(),null,BlankvouConstant.BVVOUINFODTL_STATUS_USE,userVo)){
			bvVouEmployInfoService.save(bvVouEmployInfo,BlankvouConstant.BVVOUEMPLOYINFO_DATASOURCE_PRINT,userVo);
		}
	}
	
	/**
	 * 凭证号批量自动占用接口（根据凭证使用对象里面的内容自动占用凭证号，填写日志以及入库使用表）
	 * @param bvVouEmployInfoList 凭证使用对象
	 */
	public void batchVoucherOccupy(List<BvVouEmployInfo> bvVouEmployInfoList,ICurrentUser userVo){
		StringBuilder ids = new StringBuilder();
		for(int i=0;i<bvVouEmployInfoList.size();i++){
			if(bvVouEmployInfoList.size()==1){
				ids = ids.append(bvVouEmployInfoList.get(i).getVoudtlId());
			}else{
				ids = ids.append(ids+","+bvVouEmployInfoList.get(i).getVoudtlId());
			}
		}
		if(batchOperateUpdate(ids.toString(),null,BlankvouConstant.BVVOUINFODTL_STATUS_USE,userVo)){
			bvVouEmployInfoService.batchSaveBvVouEmployInfo(bvVouEmployInfoList,userVo);
		}
	}
	
	/**
	 * 凭证查询--只能查询此用户有管理权限的单位以及自己管理库箱的数据
	 * @param input
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	@Transactional(readOnly = true)
	public DataTablesOutput queryList(DataTablesInput input,ICurrentUser userVo) {
		List<String> corpAuthList = corpService.getUserCorpIds(BlankvouConstant.AB_BILL_RESOURCE_BVVOUINFODTLQUERY_QUERY,input.getCurrentUserId());
		DataTablesOutput<BvVouInfoDtlVo> tablesOutput = bvVouInfoDtlDao.queryList(input, userVo.getId(), corpAuthList);
		redactData(tablesOutput.getData());
		return tablesOutput;
	}
	
	
	
}
