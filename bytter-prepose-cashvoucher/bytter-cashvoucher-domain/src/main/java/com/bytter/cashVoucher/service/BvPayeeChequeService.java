package com.bytter.cashVoucher.service;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.bytter.bill.api.bill.constant.BlankvouConstant;
import com.bytter.bill.api.bill.constant.PayeeChequeConstant;
import com.bytter.bill.api.bill.enums.BlankvouMessageEnum;
import com.bytter.cashVoucher.dao.BvPayeeChequeDao;
import com.bytter.cashVoucher.dao.BvVouInputDao;
import com.bytter.cashVoucher.entity.BvPayeeCheque;
import com.bytter.cashVoucher.entity.BvStorageBox;
import com.bytter.cashVoucher.entity.BvVoucherType;
import com.bytter.cashVoucher.vo.BvPayeeChequeEnum;
import com.bytter.cashVoucher.vo.BvPayeeChequeVO;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.exception.BusinessException;
import com.bytter.core.dict.DictInDTO;
import com.bytter.core.dict.DictInjectProvider;
import com.bytter.core.utils.BeanUtils;
import com.bytter.core.utils.ListUtils;
import com.bytter.core.utils.StringUtils;
import com.bytter.poi.excel.entity.result.ExcelVerifyHanlderResult;
import com.bytter.poi.handler.inter.IUserExcelVerifyHandler;
import com.bytter.system.api.*;
import com.bytter.system.api.constant.CorpVerifyMessageConstant;
import com.bytter.system.api.constant.NotificationConstant;
import com.bytter.system.api.constant.SysConfigConstant;
import com.bytter.system.api.dto.BtCurrencyDTO;
import com.bytter.system.api.dto.CorpDTO;
import com.bytter.system.api.dto.CorpVerifyErrorDTO;
import com.bytter.system.api.dto.DictDTO;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.*;
/**
 * @Title: BvPayeeChequeService
 * @Description: 支票收款登记业务处理类
 * @author Jinyong.Cao
 * @date 2018-06-13 13:06:40
 * @version 1.0
 *
 */
@Service("bvPayeeChequeService")
@Transactional
public class BvPayeeChequeService extends BaseService<BvPayeeCheque, String>
		implements IUserExcelVerifyHandler<BvPayeeChequeVO>, ICorpDeleteVerifyService  , DictInjectProvider {
	@DubboReference(check = false)
	private ICorpService corpService;
	@Autowired
	private BvPayeeChequeDao bvPayeeChequeDao;
	@Autowired
	private BvVoucherTypeService bvVoucherTypeService;
	@Autowired
	private BvLogService bvLogService;
    @Resource
    private IMultiLangService multiLangServiceI;
    
	@DubboReference(check = false)
	private IFcExcrateAdjustService fcExcrateAdjustService;
	
	@Autowired(required=false)
	private BvVouInputDao bvVouInputDao;
	@DubboReference(check = false)
	private INotificationService notificationService;
	@DubboReference(check = false)
	private IBtCurrencyService btCurrencyService;
	@DubboReference(check = false)
	private IDictService dictService;
	/**
	 *
	 * @Title: getDataTablesUnion
	 * @Description: TODO(查询凭证支票列表)
	 * @param @param input  (引用穿参对象)
	 * @param @param userVo (当前用户登录信息)
	 * @param @return    参数
	 * @return DataTablesOutput    返回类型
	 * @throws
	 */
	@SuppressWarnings("rawtypes")
	@Transactional(readOnly = true)
	public DataTablesOutput getDataTablesUnion(DataTablesInput input,ICurrentUser userVo,String opQueryType) {
		List<String> corpAuthList = corpService.getUserCorpIds(opQueryType,input.getCurrentUserId());
		DataTablesOutput<BvPayeeChequeVO> dataTablesUnion = bvPayeeChequeDao.getDataTablesUnion(input, corpAuthList, userVo.getId());
		Optional.ofNullable(dataTablesUnion.getData()).ifPresent(dataTables -> {
			Map<String, DictDTO> listMap = dictService.listMap("BV_PAYEECHEQUE_STATUS");
			Optional.ofNullable(listMap).ifPresent(list-> dataTables.forEach(data->{
				if (ObjectUtil.isNotEmpty(data.getStatus())){
					Optional.ofNullable(list.get(data.getStatus() + "")).ifPresent(dict -> data.setStatusName(dict.getName()));
				}
			}));
		});
		return dataTablesUnion;
	}
	/**
	 *
	 * @Title: baseSaveOrUpdate
	 * @Description: TODO(更新或者修改支票信息实体类)
	 * @param @param bvPayeeCheque  (支票信息实体类)
	 * @param @param operate    (操作类型)
	 * @param @param iCurrentUser    (当前用户信息)
	 * @return void    返回类型
	 * @throws
	 */
	public void baseSaveOrUpdate(BvPayeeCheque bvPayeeCheque,String operate,ICurrentUser userVo){
		// 冗余数据
		CorpDTO corpDTO = corpService.get(bvPayeeCheque.getCorpId());
		Optional.ofNullable(corpDTO).ifPresent(corp->{
			bvPayeeCheque.setCorpCode(corp.getCode());
			bvPayeeCheque.setCorpName(corp.getName());
		});
		BvPayeeChequeEnum bvPayeeChequeEnum = BvPayeeChequeEnum.getResource(operate);
		BtCurrencyDTO currencyDTO = btCurrencyService.getObjectById(bvPayeeCheque.getCurId());
		Optional.ofNullable(currencyDTO).ifPresent(curr->bvPayeeCheque.setCurName(curr.getCurName()));
		// 获取汇率
		BigDecimal curExrate = fcExcrateAdjustService.getExchangeRateBy(bvPayeeCheque.getCurId(), corpDTO.getCurId(),
				bvPayeeCheque.getInputDate(), null);
		bvPayeeCheque.setCurExrate(curExrate);
		bvPayeeCheque.setStatus(bvPayeeChequeEnum.getStatus());
		bvPayeeCheque.setCreateName(userVo.getUsername());
		bvPayeeCheque.setCreateDate(new Date());
		bvPayeeCheque.setCreateBy(userVo.getLoginName());
		bvPayeeCheque.setUpdateName(userVo.getUsername());
		bvPayeeCheque.setUpdateBy(userVo.getLoginName());
		bvPayeeCheque.setUpdateDate(new Date());
		bvPayeeChequeDao.saveOrUpdate(bvPayeeCheque);
		// 提交复核后添加消息处理
		if(BlankvouConstant.BV_CHEQUE_STATUS_CHECK.equals(bvPayeeChequeEnum.getStatus())){
			notificationService.sendIgnoreMySelf(bvPayeeCheque.getId(), bvPayeeCheque.getCorpId(),
					NotificationConstant.CASHVOUCHER.CASHVOUCHER_BVPAYEECHEQUE_CHECK, bvPayeeCheque, userVo.getId());
		}
		bvLogService.saveBvLog(bvPayeeCheque.getId(),bvPayeeChequeEnum.getLogtype(),bvPayeeChequeEnum.getMsg(), userVo);
	}
	/**
	 * 根据条件查询单条支票收支信息，用于查看或修改
	 *
	 * @param id
	 * @param operate
	 *            操作方式编辑或查看
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@Transactional(readOnly = true)
	public BvPayeeChequeVO getDataTablesRow(String id, String operate,
											ICurrentUser userVo) {
		BvPayeeChequeVO vo = null;
		if (StringUtils.isNotEmpty(id)) {
			DataTablesInput input = new DataTablesInput();
			Map<String, Object> formdata = new HashMap<>();
			formdata.put("eqs_id", id);
			input.setFormdata(formdata);
			vo = ((List<BvPayeeChequeVO>) getDataTablesUnion(input, userVo,null).getData()).get(0);
		}
		return vo;
	}
	/**
	 * 删除对象
	 *
	 * @param id
	 */
	public boolean deleteById(String id, ICurrentUser userVo) {
		BvPayeeCheque bvPayeeCheque = get(id);
		try {
			baseSaveOrUpdate(bvPayeeCheque,"delete",userVo);
		} catch (Exception e) {
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
		params.put("status", BlankvouConstant.BV_CHEQUE_STATUS_DELETE);
		int num = bvPayeeChequeDao.batchDeleteBvPayeeCheque(params,userVo);
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
	 批量提交
	 **/
	public void batchBtorage(String ids,String operate,ICurrentUser userVo){
		if(StringUtils.isNotEmpty(ids)){
			String[] idArray=StringUtils.split(ids,",");
			for(int i=0;i<idArray.length;i++){
				String id=idArray[i];
				if(StringUtils.isNotEmpty(id)){
					BvPayeeCheque bvPayeeCheque = bvPayeeChequeDao.get(id,true);
					boolean flag = checkPayeeChequeNoUnique(bvPayeeCheque, operate);
					if(!flag){
						throw new BusinessException(BlankvouMessageEnum.BLANKVOU_MESSAGE_ERROR_CHECKRULENO,String.valueOf(i+1));
					}
					baseSaveOrUpdate(bvPayeeCheque, BlankvouConstant.BILL_OPERATE_CHECK, userVo);
				}
			}
		}
	}
	/**
	 * 获取票据信息是否已存在
	 *
	 * @param bvPayeeCheque
	 * @param operate
	 */
	public boolean checkPayeeChequeNoUnique(BvPayeeCheque bvPayeeCheque, String operate) {
		return bvPayeeChequeDao.checkPayeeChequeNoUnique(bvPayeeCheque, operate);
	}
	/**
	 * 导入申请
	 * @param list
	 * @return 处理结果
	 */
	public boolean saveImport(List<BvPayeeChequeVO> list, ICurrentUser userVo) {
		for (int i=0;i<list.size();i++) {
			BvPayeeChequeVO bvPayeeChequeVO = list.get(i);
			BvPayeeCheque entity=new BvPayeeCheque();
			BeanUtils.copyProperties(bvPayeeChequeVO, entity);
			boolean flag = checkPayeeChequeNoUnique(entity, BlankvouConstant.BILL_OPERATE_SAVE);
			if(!flag){
				throw new BusinessException(BlankvouMessageEnum.BLANKVOU_MESSAGE_ERROR_CHECKRULENO,String.valueOf(i+1));
			}
			baseSaveOrUpdate(entity, BlankvouConstant.BILL_OPERATE_SAVE, userVo);
		}
		return true;
	}
	@Override
	public ExcelVerifyHanlderResult verifyHandler(BvPayeeChequeVO obj,
												  Object... user) {
		return null;
	}
	@SuppressWarnings("unused")
	@Override
	public void verifyHandler(List<BvPayeeChequeVO> list, Object... user) {
		if (!ListUtils.isNullOrEmpty(list)) {
			Map<String, Map<String, BvStorageBox>> boxMap = new HashMap<>();
			Map<String, BvVoucherType> vouTypeMap=bvVoucherTypeService.getVoucherTypeMapValid();
			ICurrentUser userInfo = (ICurrentUser) user[0];
			for (BvPayeeChequeVO info : list) {
				List<String> errorMsgList = new ArrayList<>();
				CorpDTO corpDTO = corpService.getCorpByCode(info.getCorpCode());
				if (corpDTO == null) {
					errorMsgList.add(PayeeChequeConstant.BLANKVOU_BVPAYEECHEQUE_CORPCODE_ISERROR);// 单位代码不能为空
				} else {
					info.setCorpName(corpDTO.getName());
					info.setCorpId(corpDTO.getId());// 所属单位
				}
				if(!StringUtils.isNotEmpty(info.getOrigNo())){
					errorMsgList.add(PayeeChequeConstant.BLANKVOU_BVPAYEECHEQUE_ORIGNO_ISERROR);// 票据号码不能为空
				}
				if(!StringUtils.isNotEmpty(info.getBillMoney()) && StrUtil.isBlank(info.getBillMoneyFormatError())){
					errorMsgList.add(PayeeChequeConstant.BLANKVOU_BVPAYEECHEQUE_BILLMONEY_ISERROR);// 票据金额为空
				}
				if(!StringUtils.isNotEmpty(info.getInputDate()) && StrUtil.isBlank(info.getInputDateFormatError())){
					errorMsgList.add(PayeeChequeConstant.BLANKVOU_BVPAYEECHEQUE_INPUTDATE_ISERROR);// 购入日期为空
				}
				if(!StringUtils.isNotEmpty(info.getPayee())){
					errorMsgList.add(PayeeChequeConstant.BLANKVOU_BVPAYEECHEQUE_PAYER_ISERROR);// 收款人
				}
				if(!StringUtils.isNotEmpty(info.getPayeeAcc())){
					errorMsgList.add(PayeeChequeConstant.BLANKVOU_BVPAYEECHEQUE_PAYERACC_ISERROR);// 收款人账户
				}
				if(!StringUtils.isNotEmpty(info.getPayeeBankName())){
					errorMsgList.add(PayeeChequeConstant.BLANKVOU_BVPAYEECHEQUE_PAYERBANKNAME_ISERROR);// 收款人开户行名称
				}
				if(!StringUtils.isNotEmpty(info.getPayer())){
					errorMsgList.add(PayeeChequeConstant.BLANKVOU_BVPAYEECHEQUE_PAYEE_ISERROR);// 付款人
				}
				if(!StringUtils.isNotEmpty(info.getPayerAcc())){
					errorMsgList.add(PayeeChequeConstant.BLANKVOU_BVPAYEECHEQUE_PAYEEACC_ISERROR);// 付款人账户
				}
				if(!StringUtils.isNotEmpty(info.getPayerBankName())){
					errorMsgList.add(PayeeChequeConstant.BLANKVOU_BVPAYEECHEQUE_PAYEEBANKNAME_ISERROR);// 付款人开户行名称
				}
				//info.setInputDate(new Date());
				if(!ListUtils.isNullOrEmpty(errorMsgList)){
					info.addErrorMsg(errorMsgList);
				}
			}
		}
	}
	@Override
	public List<CorpVerifyErrorDTO> verifyCorpDelete(String corpId) {
		if(StringUtils.isNotEmpty(corpId)){
			   List<CorpVerifyErrorDTO> verifyErrorDTOS=new ArrayList<>();
	           //单位有入库登记的空白凭证，不允许删除！
	           int num=bvVouInputDao.getValidBvInputNumByCorpId(corpId);
	           if(num>0){
	               verifyErrorDTOS.add(new CorpVerifyErrorDTO(corpId,
	                       SysConfigConstant.Module.CASHVOUCHER,
	                       SysConfigConstant.BusType.CASH_VOU_CHER,
	                       multiLangServiceI.getLang(CorpVerifyMessageConstant.CASHVOUCHER_VALID_BVINPUT_ERROR)));
	           }
	           //单位有登记的支票，不允许删除！
	           num=bvPayeeChequeDao.getValidBvPayNumByCorpId(corpId);
	           if(num>0){
	               verifyErrorDTOS.add(new CorpVerifyErrorDTO(corpId,
	                       SysConfigConstant.Module.CASHVOUCHER,
	                       SysConfigConstant.BusType.BV_PAYEE_CHEQUE,
	                       multiLangServiceI.getLang(CorpVerifyMessageConstant.CASHVOUCHER_VALID_BVPAYEECHEQUE_ERROR)));
	           }
	           if(ListUtils.isNotNullOrEmpty(verifyErrorDTOS)){
	               return verifyErrorDTOS;
	           }
		   }
		return null;
	}
	
	@Override
	public BaseDao<BvPayeeCheque, String> getEntityDao() {
		return bvPayeeChequeDao;
	}

    @Override
    public Map<String, Map<String, DictInDTO>> listDictMap(List<String> dictGroups) {
        return dictService.listDictMap(dictGroups);
    }
}
