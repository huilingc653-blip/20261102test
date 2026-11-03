package com.bytter.cashVoucher.service;

import cn.hutool.core.util.ObjectUtil;
import com.bytter.bill.api.bill.constant.BlankvouConstant;
import com.bytter.bill.api.bill.enums.BlankvouMessageEnum;
import com.bytter.cashVoucher.dao.BvVouInfoDtlDao;
import com.bytter.cashVoucher.dao.BvVouInputDao;
import com.bytter.cashVoucher.entity.BvStorageBox;
import com.bytter.cashVoucher.entity.BvVouInfoDtl;
import com.bytter.cashVoucher.entity.BvVouInput;
import com.bytter.cashVoucher.entity.BvVoucherType;
import com.bytter.cashVoucher.vo.BvVouInputVo;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.exception.BusinessException;
import com.bytter.core.utils.BeanUtils;
import com.bytter.core.utils.ListUtils;
import com.bytter.core.utils.StringUtils;
import com.bytter.poi.excel.entity.result.ExcelVerifyHanlderResult;
import com.bytter.poi.handler.inter.IUserExcelVerifyHandler;
import com.bytter.system.api.*;
import com.bytter.system.api.dto.BtBankAccDTO;
import com.bytter.system.api.dto.CorpDTO;
import com.bytter.system.api.dto.DictDTO;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.*;
/**
 * @Title: Service
 * @Description: 凭证入库信息表
 * @author onlineGenerator
 * @date 2017-05-25 17:06:40
 * @version 1.0
 *
 */
@Service("bvVouInputService")
@Transactional
public class BvVouInputService extends BaseService<BvVouInput,String> implements IUserExcelVerifyHandler<BvVouInputVo> {
	@Autowired
	private BvVouInputDao bvVouInputDao;
	@Autowired
	private BvVouInfoDtlDao bvVouInfoDtlDao;
	@Autowired
	private BvStorageBoxService bvStorageBoxService;
	@Autowired
	private BvVoucherTypeService bvVoucherTypeService;
	@Autowired
	private BvLogService bvLogService;
	@DubboReference(check = false)
	private ICorpService corpService;
	@DubboReference(check = false)
	private IBtBankAccService bankAccService;
	@DubboReference(check = false)
	private IFcExcrateAdjustService fcExcrateAdjustService;
	@DubboReference(check = false)
	private IDictService dictService;
	@DubboReference(check = false)
	private IBtCurrencyService btCurrencyService;
	/**
	 * 查询入库凭证
	 * @param input
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	@Transactional(readOnly = true)
	public DataTablesOutput getDataTablesUnion(DataTablesInput input,ICurrentUser userVo) {
		List<String> corpAuthList = corpService.getUserCorpIds(BlankvouConstant.AB_BILL_RESOURCE_BVVOUINPUT_QUERY,input.getCurrentUserId());
		// bvvouinput_status 远程获取字典数据
		DataTablesOutput<BvVouInputVo> dataTablesUnion = bvVouInputDao.getDataTablesUnion(input, corpAuthList, userVo.getId());
		if (ObjectUtil.isNotEmpty(dataTablesUnion.getData())) {
			Map<String, DictDTO> listMap = dictService.listMap("bvvouinput_status");
			if (ObjectUtil.isNotEmpty(listMap)) {
				dataTablesUnion.getData().forEach(vouInput -> {
					if (ObjectUtil.isNotEmpty(vouInput.getStatus())) {
						Optional.ofNullable(listMap.get(vouInput.getStatus() + "")).ifPresent(dict -> vouInput.setStatusName(dict.getName()));
					}
				});
			}
		}
		return dataTablesUnion;
	}
	/**
	 * 根据条件查询单条入库凭证信息，用于查看或修改
	 * @param id
	 * @param operate 操作方式编辑或查看
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@Transactional(readOnly = true)
	public BvVouInputVo getDataTablesRow(String id,String operate,ICurrentUser userVo) {
		BvVouInputVo vo = null;
		if (StringUtils.isNotEmpty(id)) {
			DataTablesInput input = new DataTablesInput();
			Map <String,Object> formdata = new HashMap<>();
			formdata.put("eqs_id", id);
			input.setFormdata(formdata);
			vo = ((List<BvVouInputVo>)getDataTablesUnion(input,userVo).getData()).get(0);
			if(BlankvouConstant.BILL_OPERATE_EDIT.equals (operate)){
				vo.setBeginNo(getVoucherNum(vo.getBeginNo(),vo.getRuleNo()));
				vo.setEndNo(getVoucherNum(vo.getEndNo(),vo.getRuleNo()));
			}
		}
		return vo;
	}
	/**
	 * 删除对象
	 * @param id
	 */
	public boolean deleteById(String id,ICurrentUser userVo){
		BvVouInput bvVouInput = get(id);
		bvVouInput.setUpdateName(userVo.getUsername());
		bvVouInput.setUpdateBy(userVo.getLoginName());
		bvVouInput.setUpdateDate(new Date());
		bvVouInput.setStatus(BlankvouConstant.BVVOUINPUT_STATUS_DELETE);
		try{
			saveOrUpdate(bvVouInput);
			bvLogService.saveBvLog(bvVouInput.getId(), BlankvouConstant.BVLOG_LOGTYPE_DELETE, BlankvouConstant.LANG_LOG_DELETE_SUCC,userVo);
		}catch(Exception e){
			return false;
		}
		return true;
	}
	/**
	 * 批量删除
	 * @param ids
	 */
	public boolean batchDelete(String ids,ICurrentUser userVo){
		Map<String, Object> params=new HashMap<>();
		params.put("ids", ids.split (","));
		int num = bvVouInputDao.batchDeleteBvVouInput(params,userVo);
		if(num>0){
			String[] msgs = new String[ids.split(",").length];
			for(int i=0;i<ids.split(",").length;i++){
				msgs[i] = BlankvouConstant.LANG_LOG_DELETE_SUCC;
			}
			bvLogService.batchSaveBvLog(ids.split(","), BlankvouConstant.BVLOG_LOGTYPE_DELETE, msgs,userVo);
			return true;
		}
		return false;
	}
	/**
	 * 根据id删除
	 * @param id
	 */
 	/*public void delete(String id){
 		super.delete(id);
 	}*/
	/**
	 * 保存
	 * @param entity
	 */
	public void save(BvVouInput entity,ICurrentUser userVo){
		save(entity, BlankvouConstant.BVLOG_LOGTYPE_ADD, userVo);
	}
	/**
	 * 保存
	 * @param entity
	 */
	public void save(BvVouInput entity,String logType,ICurrentUser userVo){
		// 获取汇率
		BigDecimal curExrate = fcExcrateAdjustService.getExchangeRateBy(entity.getCurId(), new Date());
		entity.setCurExrate(curExrate);
		entity.setCreateName(userVo.getUsername());
		entity.setCreateBy(userVo.getLoginName());
		entity.setCreateDate(new Date());
		entity.setStatus(BlankvouConstant.BVVOUINPUT_STATUS_CREATE);
		// 冗余数据
		redoundData(entity);
		super.saveEntity(getVoucherNo(entity));
		bvVouInputDao.flush();
		bvLogService.saveBvLog(entity.getId(), logType, BlankvouConstant.LANG_LOG_SAVE_SUCC,userVo);
	}
	/**
	 * 冗余凭证数据。
	 *
	 * @param entity 空白凭证信息。
	 */
	private void redoundData(BvVouInput entity) {
		// 冗余所属单位数据
		CorpDTO corpDTO = corpService.get(entity.getCorpId());
		Optional.ofNullable(corpDTO).ifPresent(corp -> {
			entity.setCorpName(corp.getName());
			entity.setCorpCode(corp.getCode());
		});
		// 冗余银行数据
		BtBankAccDTO bankAcc = bankAccService.getBankAccById(entity.getBankId());
		//历史数据兼容 bankId可能为空
		if (ObjectUtil.isNotEmpty(bankAcc)) {
			entity.setBankName(bankAcc.getBankName());
			entity.setCurName(bankAcc.getCurName());
		}
	}
	private String getBankTypeName(String bankId) {
		BtBankAccDTO bankAcc = bankAccService.getBankAccById(bankId);
		return ObjectUtil.isNotEmpty(bankAcc) ? bankAcc.getBankTypeName() : "";
	}
	/**
	 * 更新
	 * @param id
	 */
	public void updateById(BvVouInput bvVouInput,String id,ICurrentUser userVo){
		BvVouInput entity = get(id,true);
		BeanUtils.copyNotNullProperties(bvVouInput, entity);
		entity.setUpdateName(userVo.getUsername());
		entity.setUpdateBy(userVo.getLoginName());
		entity.setUpdateDate(new Date());
		// 冗余数据
		redoundData(entity);
		super.saveOrUpdate(getVoucherNo(entity));
		bvLogService.saveBvLog(entity.getId(), BlankvouConstant.BVLOG_LOGTYPE_UPDATE, BlankvouConstant.LANG_LOG_UPDATE_SUCC,userVo);
	}
	/**
	 * 保存or更新
	 */
 	/*public void saveOrUpdate(BvVouInput entity){
 		super.saveOrUpdate(getVoucherNo(entity));
 	}*/
	@Override
	public BaseDao<BvVouInput, String> getEntityDao() {
		return bvVouInputDao;
	}
	public void batchBtorage(String ids,String operate,ICurrentUser userVo){
		if(StringUtils.isNotEmpty(ids)){
			String[] idArray=StringUtils.split(ids,",");
			for(int i=0;i<idArray.length;i++){
				String id=idArray[i];
				if(StringUtils.isNotEmpty(id)){
					BvVouInput bvVouInput = this.get(id);
					boolean flag = checkVoucherNoUnique(bvVouInput, BlankvouConstant.BILL_OPERATE_STORAGE);
					if(!flag){
						throw new BusinessException(BlankvouMessageEnum.BLANKVOU_MESSAGE_ERROR_CHECKRULENO,String.valueOf(i+1));
					}
					storage(bvVouInput, operate, userVo);
				}
			}
		}
	}
	/**
	 * 空白凭证入库
	 * @param entity
	 */
	public void storage(BvVouInput entity,String operate,ICurrentUser userVo){
		//凭证入库
		entity.setStatus(BlankvouConstant.BVVOUINPUT_STATUS_STORAGE);
		// 冗余数据
		redoundData(entity);
		if(BlankvouConstant.BILL_OPERATE_EDIT.equals(operate)){
			entity.setUpdateName(userVo.getUsername());
			entity.setUpdateBy(userVo.getLoginName());
			entity.setUpdateDate(new Date());
			super.saveOrUpdate(entity);//操作栏或修改页面点击入库
			bvLogService.saveBvLog(entity.getId(), BlankvouConstant.BVLOG_LOGTYPE_INPUT, BlankvouConstant.LANG_LOG_UPDATE_SUCC,userVo);
		}else{
			entity.setCreateName(userVo.getUsername());
			entity.setCreateBy(userVo.getLoginName());
			entity.setCreateDate(new Date());
			super.saveEntity(getVoucherNo(entity));//新增页面点击入库
			bvLogService.saveBvLog(entity.getId(), BlankvouConstant.BVLOG_LOGTYPE_INPUT, BlankvouConstant.LANG_LOG_SAVE_SUCC,userVo);
		}
		/*根据起始号码，截止号码及编码规则生成凭证明细*/
		//获取明细凭证编号
		String noRules = entity.getRuleNo();
		@SuppressWarnings("unused")
		String bf[] = getBefAfterNo(noRules);
		String vouchers = produceVoucherNo(entity.getBeginNo(), entity.getEndNo(), noRules);
		//明细入库
		BvVouInfoDtl bvVouInfoDT = null;
		int length = vouchers.split(",").length;
		String[] VouNos = vouchers.split(",");
		String msgs[] = new String[length];
		String bvVouInfoDtIds[] = new String[length];
		String bankTypeName = getBankTypeName(entity.getBankId());
		for (int i=0;i<length;i++) {
			bvVouInfoDT = new BvVouInfoDtl();
			//入库信息ID
			bvVouInfoDT.setInputId(entity.getId());
			//所属单位ID
			bvVouInfoDT.setCorpId(entity.getCorpId());
			bvVouInfoDT.setCorpCode(entity.getCorpCode());
			bvVouInfoDT.setCorpName(entity.getCorpName());
			//所属库箱ID
			bvVouInfoDT.setSboxId(entity.getSboxId());
			//所属账户
			bvVouInfoDT.setBankAcc(entity.getBankAcc());
			bvVouInfoDT.setBankName(entity.getBankName());
			//所属银行ID
			bvVouInfoDT.setBankId(entity.getBankId());
			bvVouInfoDT.setTypeName(bankTypeName);
			//凭证种类
			bvVouInfoDT.setVouTid(entity.getVouTid());
			//凭证编号
			bvVouInfoDT.setVouNo(VouNos[i]);
			//购入日期
			bvVouInfoDT.setInputDate(entity.getInputDate());
			//币别ID
			bvVouInfoDT.setCurId(entity.getCurId());
			bvVouInfoDT.setCurName(entity.getCurName());
			//汇率
			bvVouInfoDT.setCurExrate(entity.getCurExrate());
			//凭证状态
			bvVouInfoDT.setVouStatus(BlankvouConstant.BVVOUINFODTL_STATUS_STORAGE);
			//备注
			bvVouInfoDT.setRmk(entity.getRmk());
			bvVouInfoDT.setCreateName(userVo.getUsername());
			bvVouInfoDT.setCreateBy(userVo.getLoginName());
			bvVouInfoDT.setCreateDate(new Date());
			bvVouInfoDtlDao.saveOrUpdate(bvVouInfoDT);
			bvVouInfoDtIds[i] = bvVouInfoDT.getId();
			msgs[i] = BlankvouConstant.LANG_LOG_SAVE_SUCC;
		}
		bvLogService.batchSaveBvLog(bvVouInfoDtIds, BlankvouConstant.BVLOG_LOGTYPE_ADD, msgs,userVo);
	}
	/**
	 * 获取编码规则里的前缀和后缀部分
	 * @param noRules
	 */
	@Transactional(readOnly = true)
	public String[] getBefAfterNo(String noRules) {
		int staIndex = noRules.indexOf("*");
		int endIndex = noRules.lastIndexOf("*");
		String befNo = noRules.substring(0, staIndex);
		String aftNo = noRules.substring(endIndex + 1, noRules.length());
		return new String[] { befNo, aftNo };
	}
	/**
	 * 根据起始号码的截止号码批量产生凭证号
	 * @param startNum 起始号码
	 * @param endNum 截止号码
	 * @param noRules 编码规则
	 */
	@Transactional(readOnly = true)
	public String produceVoucherNo(String startNum, String endNum, String noRules) {
		int staIndex = noRules.indexOf("*");
		int endIndex = noRules.lastIndexOf("*");
		Long sNum = Long.valueOf(startNum.substring(staIndex, endIndex+1));//起始号码
		Long eNum = Long.valueOf(endNum.substring(staIndex, endIndex+1));//截止号码
		String[] bf = getBefAfterNo(noRules);
		StringBuffer sb = new StringBuffer();
		int length = endIndex - staIndex + 1;//*号个数
		for (long i = sNum; i <= eNum; i++) {
			String vouCherNo = bf[0] + repairNum(i, length) + bf[1];
			sb.append(vouCherNo+",");
		}
		return sb.toString().substring(0, sb.length() - 1);
	}
	/**
	 * 根据编码规则对起始号和截止号做编号截取
	 * @param voucherNum 起始号码或截止号码
	 * @param noRules 编码规则
	 */
	@Transactional(readOnly = true)
	public String getVoucherNum(String voucherNum,String noRules) {
		int staIndex = noRules.indexOf("*");
		int endIndex = noRules.lastIndexOf("*");
		voucherNum = voucherNum.substring(staIndex,endIndex+1);
		return voucherNum;
	}
	/**
	 * 功能：凭证代码不够长度的自动右边补0
	 * @param index
	 * @param length *的长度
	 */
	protected String repairNum(Long index, int length) {
		//得到一个NumberFormat的实例
		NumberFormat nf = NumberFormat.getInstance();
		//设置是否使用分组
		nf.setGroupingUsed(false);
		//设置最大整数位数
		nf.setMaximumIntegerDigits(length);
		//设置最小整数位数
		nf.setMinimumIntegerDigits(length);
		return nf.format(index);
	}
	/**
	 * 起始号和截止号添加前后缀及补位处理
	 */
	@Transactional(readOnly = true)
	public BvVouInput getVoucherNo(BvVouInput entity) {
		String noRules = entity.getRuleNo();
		String[] bf = getBefAfterNo(noRules);
		int staIndex = noRules.indexOf("*");
		int endIndex = noRules.lastIndexOf("*");
		int length = endIndex - staIndex + 1;//*号个数
		String beginNo = entity.getBeginNo();
		if(beginNo.length ()>length){
			beginNo = beginNo.substring (staIndex,endIndex+1);
		}
		String endNo = entity.getEndNo();
		if(endNo.length ()>length){
			endNo = endNo.substring (staIndex,endIndex+1);
		}
		String prefix = "";//前缀
		String suffix = "";//后缀
		if(beginNo.length()==noRules.length()){
			prefix = beginNo.substring(0, staIndex);
			suffix = beginNo.substring(endIndex+1, beginNo.length());
			if(bf[0].equals(prefix)&&bf[1].equals(suffix)){
				entity.setBeginNo(bf[0]+repairNum(Long.valueOf(beginNo), length)+bf[1]);
			}
		}else{
			entity.setBeginNo(bf[0]+repairNum(Long.valueOf(beginNo), length)+bf[1]);
		}
		if(endNo.length()==noRules.length()){
			prefix = endNo.substring(0, staIndex);
			suffix = endNo.substring(endIndex+1, beginNo.length());
			if(bf[0].equals(prefix)&&bf[1].equals(suffix)){
				entity.setBeginNo(bf[0]+repairNum(Long.valueOf(beginNo), length)+bf[1]);
			}
		}else{
			entity.setEndNo(bf[0]+repairNum(Long.valueOf(endNo), length)+bf[1]);
		}
		return entity;
	}
	/**
	 * 凭证所属账户+凭证种类+票据号码确定票据的唯一性，系统限制重复录入重要空白凭证；                                 
	 * @param entity
	 * @param operate 操作类型
	 * @return
	 */
	public boolean checkVoucherNoUnique(BvVouInput entity,String operate){
		return bvVouInputDao.checkVoucherNoUnique(getVoucherNo(entity),operate);
	}
	/**
	 * 校验凭证类型是否存在空白凭证使用                                 
	 * @return
	 */
	public boolean checkBvVoucherType(String vouTid){
		return  bvVouInputDao.checkBvVoucherType(vouTid);
	}
	/**
	 * 校验库箱是否存在空白凭证使用                                
	 * @return
	 */
	@Transactional(readOnly = true)
	public boolean checkBvStorageBox(String sboxId){
		return  bvVouInputDao.checkBvStorageBox(sboxId);
	}
	/**
	 * 根据银行账号获取银行账号信息--票据专户
	 * @param bankAcc  银行账号
	 * @return
	 */
	@Transactional(readOnly = true)
	public List<BtBankAccDTO> getBankDataByBankAcc(String bankAcc){
		BtBankAccDTO params=new BtBankAccDTO();
		params.setBankAcc(bankAcc);
		return bankAccService.getBankAccList(params);
	}
	/**
	 * 导入保证申请
	 *
	 * @param list
	 *            导入vo信息
	 * @return 处理结果
	 */
	public boolean saveImport(List<BvVouInputVo> list, ICurrentUser userVo) {
		for (int i=0;i<list.size();i++) {
			BvVouInputVo vouInputInfo = list.get(i);
			BvVouInput entity=new BvVouInput();
			BeanUtils.copyProperties(vouInputInfo, entity);
			boolean flag = checkVoucherNoUnique(entity, BlankvouConstant.BILL_OPERATE_SAVE);
			if(!flag){
				throw new BusinessException(BlankvouMessageEnum.BLANKVOU_MESSAGE_ERROR_CHECKRULENO,String.valueOf(i+1));
			}
			save(entity, BlankvouConstant.BVLOG_LOGTYPE_IMPORT, userVo);
		}
		return true;
	}
	@Override
	public ExcelVerifyHanlderResult verifyHandler(BvVouInputVo obj, Object... user) {
		return null;
	}
	@Override
	public void verifyHandler(List<BvVouInputVo> list, Object... user) {
		if(!ListUtils.isNullOrEmpty(list)){
			Map<String,Map<String,BvStorageBox>> boxMap=new HashMap<>();
			Map<String,BvVoucherType> vouTypeMap=bvVoucherTypeService.getVoucherTypeMapValid();
			ICurrentUser userInfo=(ICurrentUser)user[0];
			for(BvVouInputVo info:list){
				List<String> errorMsgList = new ArrayList<>();
				CorpDTO corpDTO = corpService.getCorpByCode(info.getCorpCode());
				if(corpDTO==null){
					errorMsgList.add(BlankvouConstant.BLANKVOU_BVVOUINPUT_CORPCODE_ISERROR);
				}else{
					info.setCorpName(corpDTO.getName());
					info.setCorpId(corpDTO.getId());
					if(!boxMap.containsKey(corpDTO.getId())){
						boxMap.put(corpDTO.getId(), bvStorageBoxService.getStorageBoxMap(corpDTO.getId(), userInfo));
					}
					if(boxMap.containsKey(corpDTO.getId())
							&&boxMap.get(corpDTO.getId()).containsKey(info.getStorageBoxName())){
						info.setSboxId(boxMap.get(corpDTO.getId()).get(info.getStorageBoxName()).getId());
					}else{
						errorMsgList.add(BlankvouConstant.BLANKVOU_BVVOUINPUT_SBOXNAME_ISERROR);
					}
				}
				if(vouTypeMap.containsKey(info.getVoucherTypeName())){
					info.setVouTid(vouTypeMap.get(info.getVoucherTypeName()).getId());
				}else{
					errorMsgList.add(BlankvouConstant.BLANKVOU_BVVOUINPUT_VOUTNAME_ISERROR);
				}
				if(info.getRuleNo() == null){
					errorMsgList.add(BlankvouConstant.BLANKVOU_BVVOUINPUT_RULENO_ISNOTNULL);
				}else {
					int indexBegin=info.getRuleNo().indexOf("*");
					int indexEnd=info.getRuleNo().lastIndexOf("*");
					if(indexBegin!=-1){
						String ruleNoNew=info.getRuleNo().substring(indexBegin, indexEnd+1);
						if(ruleNoNew.replace("*", "").length()!=0){
							errorMsgList.add(BlankvouConstant.BLANKVOU_BVVOUINPUT_RULENO_ISERROR);
						}
					}else{
						errorMsgList.add(BlankvouConstant.BLANKVOU_BVVOUINPUT_RULENO_ISERROR);
					}
					int length=indexEnd-indexBegin+1;
					if(info.getBeginNo() != null){
						if(info.getBeginNo().length()>length
								||info.getEndNo().length()>length){
							errorMsgList.add(BlankvouConstant.BLANKVOU_BVVOUINPUT_RULENO_ISLENGTHERROR);
						}
					}
				}
				if(info.getBeginNo() == null && info.getEndNo() == null){
					errorMsgList.add(BlankvouConstant.BLANKVOU_BVVOUINPUT_BEGINNO_ISNOTNULL);
					errorMsgList.add(BlankvouConstant.BLANKVOU_BVVOUINPUT_ENDNO_ISNOTNULL);
				} else if(info.getBeginNo() == null){
					errorMsgList.add(BlankvouConstant.BLANKVOU_BVVOUINPUT_BEGINNO_ISNOTNULL);
				}else if (info.getEndNo() == null){
					errorMsgList.add(BlankvouConstant.BLANKVOU_BVVOUINPUT_ENDNO_ISNOTNULL);
				}else {
					if(!StringUtils.isNumeric(info.getBeginNo())
							||!StringUtils.isNumeric(info.getEndNo())){
						errorMsgList.add(BlankvouConstant.BLANKVOU_BVVOUINPUT_RULENO_ISSTRINGERROR);
					}else if(Integer.parseInt(info.getBeginNo())>=Integer.parseInt(info.getEndNo())){
						errorMsgList.add(BlankvouConstant.BLANKVOU_BVVOUINPUT_RULENO_ISSIZEERROR);
					}
				}
				if(StringUtils.isNotEmpty(info.getBankAcc())){
					BtBankAccDTO bankAcc=bankAccService.getBankAccInfo(info.getBankAcc());
					if(bankAcc==null){
						errorMsgList.add(BlankvouConstant.BLANKVOU_BVVOUINPUT_BANKACC_ISNULL);
					}else if(!bankAcc.getCorpId().equals(info.getCorpId())){
						errorMsgList.add(BlankvouConstant.BLANKVOU_BVVOUINPUT_BANKACC_ISNOTCORP);
					}else{
						info.setBankName(bankAcc.getBankName());
					}
				}
				if(!ListUtils.isNullOrEmpty(errorMsgList)){
					info.addErrorMsg(errorMsgList);
				}
			}
		}
	}
}
