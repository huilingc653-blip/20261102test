package com.bytter.cashVoucher.service;

import com.alibaba.nacos.common.utils.CollectionUtils;
import com.bytter.bill.api.bill.constant.BlankvouConstant;
import com.bytter.cashVoucher.dao.BvPrintFormatDao;
import com.bytter.cashVoucher.dao.BvVoucherTypeDao;
import com.bytter.cashVoucher.dao.BvVoutypeSceneDao;
import com.bytter.cashVoucher.entity.BvPrintFormat;
import com.bytter.cashVoucher.entity.BvPrintFormatDtl;
import com.bytter.cashVoucher.entity.BvPrintMatchRule;
import com.bytter.cashVoucher.entity.BvVoutypeScene;
import com.bytter.cashVoucher.vo.BvPrintFormatVo;
import com.bytter.cashVoucher.vo.BvPrintMatchRuleVo;
import com.bytter.cashVoucher.vo.BvVoucherTypeVO;
import com.bytter.cashVoucher.vo.BvVoutypeSceneVO;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.utils.BeanUtils;
import com.bytter.core.utils.StringUtils;
import com.bytter.system.api.IDictService;
import com.bytter.system.api.IMultiLangService;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;
import java.util.*;
import java.util.Map.Entry;
/**
* @Title: Service
* @Description: 凭证打印格式
* @author onlineGenerator
* @date 2017-05-31 15:25:10
* @version 1.0
*
*/
@Service("bvPrintFormatService")
@Transactional
public class BvPrintFormatService extends BaseService<BvPrintFormat,String> {
	
	@Autowired
	private BvPrintFormatDao bvPrintFormatDao;
	
	@Autowired
	private BvPrintMatchRuleService bvPrintMatchRuleService;
	
	@Autowired
	private BvPrintFormatDtlService bvPrintFormatDtlService;
	
	@Autowired
	private BvLogService bvLogService;
	@DubboReference(check = false)
	private IDictService dictService;
	@Autowired
	private BvVoucherTypeDao bvVoucherTypeDao;
	@Resource
    private IMultiLangService multiLangServiceI;
	@Autowired
	private BvVoutypeSceneDao bvVoutypeSceneDao;
	/**
	* 删除对象
	* @param entity
	*/
	@Override
	public void deleteEntity(BvPrintFormat entity){
		super.deleteEntity(entity);
	}
	/**
	* 根据id删除
	* @param id
	*/
 	public void delete(String id,ICurrentUser userVo){
		if(!StringUtils.isBlank(id)){
			String[] idsNew={id};
			boolean reBoo = batchDelete(idsNew,userVo);
			if(reBoo){
				bvLogService.saveBvLog(id, BlankvouConstant.BVLOG_LOGTYPE_DELETE, BlankvouConstant.LANG_LOG_DELETE_SUCC,userVo);
			}
		}
 	}
	/**
	* 保存
	* @param entity
	*/
 	public void save(BvPrintFormat entity){
 		super.saveEntity(entity);
 	}
	/**
	* 更新
	* @param entity
	*/
	public void update(BvPrintFormat entity){
		super.updateEntity(entity);
	}
	/**
	* 保存or更新
	* @param entity
	*/
 	@Override
	public void saveOrUpdate(BvPrintFormat entity){
 		super.saveOrUpdate(entity);
 	}
	@Override
	public BaseDao<BvPrintFormat, String> getEntityDao() {
		return bvPrintFormatDao;
	}
	
	/**
	 * 展示套打配置页面列表查询信息
	 * @param input
	 * @return
	 */
	@Transactional(readOnly = true)
	@SuppressWarnings({ "rawtypes"})
	public DataTablesOutput listBvPrintFormatInfo(DataTablesInput input){	
		return bvPrintFormatDao.listBvPrintFormatInfo(input);
	}
	
	/**
	 * 批量删除数据
	 * @param ids
	 * @return
	 */
	public boolean batchDelete(String ids,ICurrentUser userVo){
		if(StringUtils.isBlank(ids)){
			return false;
		}
		String[] idsNew=StringUtils.split(ids, ",");
		boolean reBoo = batchDelete(idsNew,userVo);
		if(reBoo){
			bvLogService.batchSaveBvLog(idsNew, BlankvouConstant.BVLOG_LOGTYPE_DELETE, BlankvouConstant.LANG_LOG_BATCHDELETE_SUCC,userVo);
		}
		return reBoo;
	}
	
	/**
	 * 批量删除数据
	 * @param ids
	 * @return
	 */
	public boolean batchDelete(String[] ids,ICurrentUser userVo){
		if(ids!=null&&ids.length>0){
			Map<String, Object> params=new HashMap<>();
			params.put("ids", ids);
			int num=bvPrintFormatDao.batchDeleteBvPrintFormat(params,userVo);
			bvPrintMatchRuleService.batchLogicDeleteByPrintIds(ids);
//			bvPrintFormatDtlService.batchDeleteByPrintIds(ids);
			if(num>0){
				return true;
			}
		}
		return false;
	}
	
	/**
	 * 根据ID获取凭证打印信息vo
	 * @param id
	 * @return
	 */
	@Transactional(readOnly = true)
	public BvPrintFormatVo getBvPrintFormatVo(String id){
		BvPrintFormatVo info=new BvPrintFormatVo();
		if(StringUtils.isNotBlank(id)){
			List<BvPrintFormatVo> listInfo=bvPrintFormatDao.listBvPrintFormatVoById(id);
			if(listInfo!=null&&!listInfo.isEmpty()){
				info=listInfo.get(0);
				List<BvVoutypeScene> list = bvVoutypeSceneDao.listBvPrintFormatVoById(info.getVouTid());
				if(CollectionUtils.isNotEmpty(list)) {
					info.setSceneName(!ObjectUtils.isEmpty(list.get(0)) && !StringUtils.isEmpty(list.get(0).getName()) ? list.get(0).getName(): "");
				}
				List<BvPrintFormatDtl> printFmtDtl=bvPrintFormatDtlService.listPrintFmtDtlInfoByPrintId(info.getId());
				info.setPrintFmtDtl(printFmtDtl);
				List<BvPrintMatchRule> printMR=bvPrintMatchRuleService.listPrintMatchRuleInfoByPrintId(info.getId());
				if(printMR!=null&&!printMR.isEmpty()){
					BvPrintMatchRule printMRinfo=printMR.get(0);
					printMRinfo.setVouTid(printMRinfo.getMatchVal());
					printMRinfo.setMatchVal(printMRinfo.getVouTidName());
					printMRinfo.setCodeName(multiLangServiceI.getLang(BlankvouConstant.LANG_VOUCHER_TYPE));
				}
				info.setPrintMR(printMR);
			}
		}
		return info;
	}
	/**
	 * 根据ID查询查询凭证打印格式主信息
	 * @param id 打印格式主信息ID
	 * @return
	 */
	@Transactional(readOnly = true)
	public List<BvPrintFormatVo> listBvPrintFormatVoById(String id) {
		return bvPrintFormatDao.listBvPrintFormatVoById(id);
	}
	/**
	 * 打印格式编号是否重复
	 * @param opType 操作类型
	 * @param fmtNo 套打编号
	 * @param id
	 * @return
	 */
	@Transactional(readOnly = true)
	public boolean checkFmtNo(String opType,String fmtNo,String id){
		int size=bvPrintFormatDao.checkFmtNo(opType,fmtNo,id);
		if(size>0){
			return false;
		}
		return true;
	}
	
	/**
	 * 添加凭证打印格式
	 */
	public void saveBvPrintFormat(BvPrintFormatVo bvPrintFormatVo,ICurrentUser userVo){
		BvPrintFormat entity=new BvPrintFormat();
		BeanUtils.copyProperties(bvPrintFormatVo, entity);
		entity.setCreateBy(userVo.getLoginName());
		entity.setCreateName(userVo.getUsername());
		entity.setCreateDate(new Date());
		if("null".equals(entity.getSceneId())||BlankvouConstant.BV_OTHERSCENE_UUID.equals(entity.getSceneId())){
			entity.setSceneId(null);
		}
		bvPrintFormatDao.save(entity);
		
		if(bvPrintFormatVo.getPrintMR()!=null){
			bvPrintMatchRuleService.batchBvPrintMatchRule(bvPrintFormatVo.getPrintMR(), entity.getId());
		}
		if(bvPrintFormatVo.getPrintFmtDtl()!=null){
			bvPrintFormatDtlService.batchBvPrintFormatDtl(bvPrintFormatVo.getPrintFmtDtl(), entity.getId());
		}
		bvLogService.saveBvLog(entity.getId(), BlankvouConstant.BVLOG_LOGTYPE_ADD, BlankvouConstant.LANG_LOG_SAVE_SUCC,userVo);
		
	}
	
	/**
	 * 修改凭证打印格式
	 */
	public void modifyBvPrintFormat(BvPrintFormatVo bvPrintFormatVo,ICurrentUser userVo){
		BvPrintFormat entity=bvPrintFormatDao.get(bvPrintFormatVo.getId());
		BeanUtils.copyProperties(bvPrintFormatVo, entity);
		entity.setUpdateBy(userVo.getLoginName());
		entity.setUpdateName(userVo.getUsername());
		entity.setUpdateDate(new Date());
		if("null".equals(entity.getSceneId())||BlankvouConstant.BV_OTHERSCENE_UUID.equals(entity.getSceneId())){
			entity.setSceneId(null);
		}
		bvPrintFormatDao.update(entity);
		String[] pinrtIds={entity.getId()};
		//套打匹配规则，先删除在新增
		bvPrintMatchRuleService.batchDeleteByPrintIds(pinrtIds);
		if(bvPrintFormatVo.getPrintMR()!=null){
			bvPrintMatchRuleService.batchBvPrintMatchRule(bvPrintFormatVo.getPrintMR(), entity.getId());
		}
		//套打明细信息，先删除在新增
		bvPrintFormatDtlService.batchDeleteByPrintIds(pinrtIds);
		if(bvPrintFormatVo.getPrintFmtDtl()!=null){
			bvPrintFormatDtlService.batchBvPrintFormatDtl(bvPrintFormatVo.getPrintFmtDtl(), entity.getId());
		}
		bvLogService.saveBvLog(entity.getId(), BlankvouConstant.BVLOG_LOGTYPE_UPDATE, BlankvouConstant.LANG_LOG_UPDATE_SUCC,userVo);
		
	}
	
	/**
	 * 复制凭证打印格式
	 */
	public void copyBvPrintFormat(BvPrintFormatVo bvPrintFormatVo,ICurrentUser userVo){
		BvPrintFormat entity=new BvPrintFormat();
		BeanUtils.copyProperties(bvPrintFormatVo, entity);
		entity.setId(null);
		entity.setCreateBy(userVo.getLoginName());
		entity.setCreateName(userVo.getUsername());
		entity.setCreateDate(new Date());
		if("null".equals(entity.getSceneId())||BlankvouConstant.BV_OTHERSCENE_UUID.equals(entity.getSceneId())){
			entity.setSceneId(null);
		}
		bvPrintFormatDao.save(entity);
		
		if(bvPrintFormatVo.getPrintMR()!=null){
			bvPrintMatchRuleService.batchBvPrintMatchRule(bvPrintFormatVo.getPrintMR(), entity.getId());
		}
		if(bvPrintFormatVo.getPrintFmtDtl()!=null){
			bvPrintFormatDtlService.batchBvPrintFormatDtl(bvPrintFormatVo.getPrintFmtDtl(), entity.getId());
		}
		bvLogService.saveBvLog(entity.getId(), BlankvouConstant.BVLOG_LOGTYPE_COPY, BlankvouConstant.LANG_LOG_COPY_SUCC,userVo);
		
	}
	
	/**
	 * 根据打印格式主信息ID
	 * 获取凭证打印格式匹配规则信息
	 *
	 * @return list集合
	 */
	@Transactional(readOnly = true)
	public List<BvPrintMatchRuleVo> listPrintMatchRuleInfoByPrintId(String printId, String opType, String vouTid) {
		List<BvPrintMatchRuleVo> printMRVoList=new ArrayList<BvPrintMatchRuleVo>();
		if("add".equals(opType)&&StringUtils.isNotBlank(vouTid)&&!"undefined".equals(vouTid)){
			BvPrintMatchRuleVo printMRinfo=new BvPrintMatchRuleVo();
			printMRinfo.setMatchVal(multiLangServiceI.getLang(BlankvouConstant.LANG_VOUCHER_TYPE));
			printMRinfo.setVouTid(vouTid);
			printMRinfo.setMatchRule("01");
			printMRinfo.setValidSign("1");
			printMRVoList.add(printMRinfo);
		}
		List<BvPrintMatchRule> pintMRList=bvPrintMatchRuleService.listPrintMatchRuleInfoByPrintId(printId);
		if(pintMRList!=null&&!pintMRList.isEmpty()){
			printMRVoList.addAll(BeanUtils.copyListProperties(pintMRList, BvPrintMatchRuleVo.class));
			BvPrintMatchRule printMR=pintMRList.get(0);
			if(vouTid.equals(printMR.getMatchVal())||"undefined".equals(vouTid)){
				BvPrintMatchRuleVo printMRinfo=printMRVoList.get(0);
				printMRinfo.setVouTid(printMRinfo.getMatchVal());
				printMRinfo.setMatchVal(multiLangServiceI.getLang(BlankvouConstant.LANG_VOUCHER_TYPE));
			}else{
				printMRVoList.remove(0);
				BvPrintMatchRuleVo printMRinfo=new BvPrintMatchRuleVo();
				printMRinfo.setMatchVal(multiLangServiceI.getLang(BlankvouConstant.LANG_VOUCHER_TYPE));
				printMRinfo.setVouTid(vouTid);
				printMRinfo.setMatchRule("01");
				printMRinfo.setValidSign("1");
				printMRVoList.add(0, printMRinfo);
			}
		}
		for (Object datum : printMRVoList) {
			BvPrintMatchRuleVo vo = (BvPrintMatchRuleVo) datum;
			String matchRuleName = dictService.getByName("bv_print_match_rule", vo.getMatchRule());
			vo.setMatchRuleName(matchRuleName);
			String validSignName = dictService.getByName("valid_sign", vo.getValidSign());
			vo.setValidSignName(validSignName);
			BvVoucherTypeVO bvVoucherTypeVO = bvVoucherTypeDao.getVoucherTypeById(vo.getVouTid());
			vo.setVouTidName(bvVoucherTypeVO.getName());

		}
		return printMRVoList;
	}
	
	/**
	 * 根据打印格式主信息ID
	 * 获取打印格式打印明细信息
	 *
	 * @return list集合
	 */
	@Transactional(readOnly = true)
	public List<BvPrintFormatDtl> listPrintFmtDtlInfoByPrintId(String printId) {
		return bvPrintFormatDtlService.listPrintFmtDtlInfoByPrintId(printId);
	}
	/**
	 * 根据ID查询查询凭证打印格式信息
	 * @param vouTid
	 * @return
	 */
	@Transactional(readOnly = true)
	public List<BvPrintFormatVo> getPrintFormatByVouTid(String vouTid) {
		return getPrintFormatByVouTid(vouTid, null);
	}
	/**
	 * 根据ID查询查询凭证打印格式信息
	 * @param vouTid
	 * @param sceneId
	 * @return
	 */
	@Transactional(readOnly = true)
	public List<BvPrintFormatVo> getPrintFormatByVouTid(String vouTid, String sceneId) {
		return bvPrintFormatDao.getPrintFormatByVouTid(vouTid, sceneId);
	}
}
