package com.bytter.cashVoucher.service;

import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.nacos.common.utils.CollectionUtils;
import com.bytter.bill.api.bill.constant.BlankvouConstant;
import com.bytter.bill.api.bill.enums.BlankvouMessageEnum;
import com.bytter.bill.api.bill.service.IVoucherPrintService;
import com.bytter.biz.payment.api.service.IVoucherPrintingService;
import com.bytter.cashVoucher.dao.BvPrintVouOpinfoDao;
import com.bytter.cashVoucher.entity.*;
import com.bytter.cashVoucher.enums.ViewModel;
import com.bytter.cashVoucher.enums.VoucherViewEnum;
import com.bytter.cashVoucher.vo.*;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.exception.BusinessException;
import com.bytter.core.utils.*;
import com.bytter.system.api.ICorpService;
import com.bytter.system.api.IDictService;
import com.bytter.system.api.IMultiLangService;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;
/**
* @Title: Service
* @Description: 凭证打印操作
* @author onlineGenerator
* @date 2017-06-13 11:18:30
* @version 1.0
*
*/
@Service("bvPrintVouOpinfoService")
@Transactional
public class BvPrintVouOpinfoService extends BaseService<BvPrintVouOpinfo,String> {
	@Autowired
	private BvPrintVouOpinfoDao bvPrintVouOpinfoDao;
	
	@Autowired
	private BvVoucherTypeService bvVoucherTypeService;
	
	@Autowired
	private BvPrintFormatService bvPrintFormatService;
	@Autowired
	private BvPrintMatchRuleService bvPrintMatchRuleService;
	
	@Autowired
	private BvPrintFormatDtlService bvPrintFormatDtlService;
	
	@Autowired
	private BvVoutypeDataFieldService bvVoutypeDataFieldService;
	
	@Autowired
	private BvVouInfoDtlService bvVouInfoDtlService;
	@Autowired
	private BvVoutypeSceneService bvVoutypeSceneService;
	
	@Autowired
	private BvLogService bvLogService;
	
	@DubboReference(check = false)
	private ICorpService corpService;
	@DubboReference(check = false)
	private IDictService dictService;
	@Resource
    private IMultiLangService multiLangServiceI;
	@DubboReference(check = false)
	private IVoucherPrintService voucherPrintService;

	@DubboReference
	private IVoucherPrintingService iVoucherPrintingService;
	
	/**
	* 删除对象
	* @param entity
	*/
	public void deleteEntity(BvPrintVouOpinfo entity){
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
 	public void save(BvPrintVouOpinfo entity){
 		super.saveEntity(entity);
 	}
	/**
	* 更新
	* @param entity
	*/
	public void update(BvPrintVouOpinfo entity){
		super.updateEntity(entity);
	}
	/**
	* 保存or更新
	* @param entity
	*/
 	public void saveOrUpdate(BvPrintVouOpinfo entity){
 		super.saveOrUpdate(entity);
 	}
	public BaseDao<BvPrintVouOpinfo, String> getEntityDao() {
		return bvPrintVouOpinfoDao;
	}
	
	/**
	 * 进去查询凭证打印操作页面初始信息
	 * @param model
	 * @param vouTid
	 */
	@Transactional(readOnly = true)
	public void toListInit(Model model,String vouTid){
		BvVoucherType vouTypeInfo=null;
		if(StringUtils.isAllBlank(vouTid)||"undefined".equals(vouTid)){
			List<BvVoucherType> vouTypelist=bvVoucherTypeService.queryVoucherTypeValid();
			if(vouTypelist!=null&&!vouTypelist.isEmpty()){
				vouTypeInfo=vouTypelist.get(0);
			}
		}else{
			vouTypeInfo=bvVoucherTypeService.get(vouTid);
		}
		if(vouTypeInfo!=null){
			model.addAttribute("vouTypeInfo", vouTypeInfo);
			List<BvVoutypeDataFieldVO> vtDaFieldList=bvVoutypeDataFieldService.listVoutypeDataFieldVOValidByVouTid(vouTypeInfo.getId());
			boolean storpMeony = true;//记在凭证金额字段位置值展示一次后面需要清除
			boolean storpBankAcc = true;//第一次出现银行账户字段进行获取设置查询条件
			boolean storpBankType = true;//第一次出现银行大类字段进行获取设置查询条件
			if(CollectionUtils.isNotEmpty(vtDaFieldList)){
				for(int i=0;i<vtDaFieldList.size();i++){
					BvVoutypeDataFieldVO field=vtDaFieldList.get(i);
					if(BlankvouConstant.BV_VOUTYPE_DATATYPE_MONEY.equals(field.getDataType())
							&&"1".equals(field.getIsShow())
							&&storpMeony){
						storpMeony=false;
						model.addAttribute("isShowMoney", "1");
						model.addAttribute("moneyCode", BlankvouConstant.BV_VOUTYPE_CODE_OPMONEY);
						model.addAttribute("moneyName", field.getFieldName());
						vtDaFieldList.remove(field);
						i--;
					}else if(BlankvouConstant.BV_VOUTYPE_DATATYPE_BANKACC.equals(field.getDataType())
							&&"1".equals(field.getIsShow())
							&&storpBankAcc){
						model.addAttribute(BlankvouConstant.BV_VOUTYPE_CODE_OPBANKACC+"Code", field.getFieldCode().replace("_", "").toLowerCase());
						model.addAttribute(BlankvouConstant.BV_VOUTYPE_CODE_OPBANKACC+"Name", field.getFieldName());
					}else if(BlankvouConstant.BV_VOUTYPE_DATATYPE_BANKTYPE.equals(field.getDataType())
							&&"1".equals(field.getIsShow())
							&&storpBankType){
						model.addAttribute(BlankvouConstant.BV_VOUTYPE_CODE_OPBANKTYPE+"Code", field.getFieldCode().replace("_", "").toLowerCase());
						model.addAttribute(BlankvouConstant.BV_VOUTYPE_CODE_OPBANKTYPE+"Name", field.getFieldName());
					}
				}
			}

			model.addAttribute("vtDaFieldList", vtDaFieldList);
			//对多个套打场景进行初始化
			List<BvVoutypeSceneVO> sceneList=bvVoutypeSceneService.listVoutypeSceneVOValidByVouTid(vouTid);
			if(ListUtils.isNotNullOrEmpty(sceneList)
					&&StringUtils.isNotEmpty(sceneList.get(0).getId())){
				if(sceneList.size()>1){
					model.addAttribute("sceneList", sceneList);
				}else{
					model.addAttribute("sceneId", sceneList.get(0).getId());
				}
			}
		}
	}
	/**
	 * 查询凭证打印操作列表信息，根据不同模块查询不同凭证打印数据。
	 *
	 * @param input 查询条件。
	 * @return 凭证打印数据。
	 */
	public DataTablesOutput listBvPrintVouOp(DataTablesInput input) {
		Map<String, Object> params = new HashMap<>();
		Map<String, Object> formMap = input.getFormdata();
		String vouTid = String.valueOf(formMap.get("voucherTypeId"));
		formMap.remove("voucherTypeId");
		formMap.remove("voucherType");
		BvVoucherType vouTypeInfo = null;
		if (StringUtils.isNotBlank(vouTid) && !"null".equals(vouTid)) {
			vouTypeInfo = bvVoucherTypeService.get(vouTid);
		}
		if (vouTypeInfo != null && StringUtils.isBlank(vouTypeInfo.getDataSource())) {
			throw new BusinessException(BlankvouMessageEnum.BLANKVOU_MESSAGE_NO_DATA_SOURE);
		}
		// 区分前端的排序字段是否属于已打印信息相关的【true:凭证端排序;false:数据端排序；】
		AtomicBoolean isOrder = new AtomicBoolean(true);
		Map<String, String> orderByMap = input.getOrderByMap();
		System.out.println("orderByMap = " + orderByMap);
		// 凭证端排序字段名
		List<String> columnList = ListUtil.toList("idpop", "vounopop", "isprintpop", "printsizepop", "opbypop", "opnamepop", "opdatepop");
		Optional.ofNullable(orderByMap).ifPresent(list -> {
			// 过滤出不在当前SQL中的字段
			List<String> collect = list.keySet().stream().filter(map -> !columnList.contains(map)).collect(Collectors.toList());
			// 前端排序只允许一个字段
			Optional.of(collect).ifPresent(order -> {
				if (ObjectUtil.isNotEmpty(order)){
					isOrder.set(false);
				}
			});
		});
		// 根据视图所属模块来获取对应数据
		AtomicReference<DataTablesOutput<Map<String, Object>>> listData = new AtomicReference<>();
		Optional.ofNullable(vouTypeInfo).filter(voucherType -> ObjectUtil.isNotEmpty(voucherType.getDataSource())).ifPresent(voucherType -> {
			// 根据视图的数据来源来获取视图所属的模块
			ViewModel viewModel = VoucherViewEnum.getModelByView(voucherType.getDataSource());
			// 组装视图中需要显示的字段，视图中的表别名统一定为【s】
			String selectField = getSqlFields(voucherType.getId(), viewModel);
			// 组装单位条件
			String corpAuthSql = SqlUtil.getInSqlByParams(
					corpService.getUserCorpIds(BlankvouConstant.AB_BILL_RESOURCE_BVPRINTVOUOPINFO_QUERY, input.getCurrentUserId()),
					"s.corp_id",
					params);
			Optional.ofNullable(viewModel).ifPresent(model -> {
				switch (model) {
					case VIEW_PAYEE:
						// 从【凭证模块】获取凭证打印数据
						listData.set(listBvPrintVouOpByVoucher(input, selectField, corpAuthSql, params, isOrder.get()));
						break;
					case VIEW_NIS:
						// 从【结算模块】获取凭证打印数据
						System.out.println("从【结算模块】获取凭证打印数据>>>>>>>>>>>>>>>>>");
						listData.set(new DataTablesOutput<>());
						break;
					case VIEW_BILL:
						// 从【票据模块】获取凭证打印数据
						System.out.println("从【票据模块】获取凭证打印数据>>>>>>>>>>>>>>>>>");
						listData.set(voucherPrintService.listBvPrintVouOp(input, selectField, corpAuthSql, params, isOrder.get()));
						break;
					default:
						System.out.println("当前模块业务尚未实现：model = " + model.getDesc());
						break;
				}
			});
		});
		// 组装数据后，和打印表的数据关联
		DataTablesOutput<Map<String, Object>> dataTablesOutput = listData.get();
		Optional.ofNullable(dataTablesOutput).ifPresent(data -> {
			DataTablesOutput<Map<String, Object>> printInfo = listBvPrintInfo(input, vouTid, params, isOrder.get());
			// 不为空则拼接数据，为空，则只返回一个是否打印
			if (ObjectUtil.isNotEmpty(printInfo) && ObjectUtil.isNotEmpty(printInfo.getData())) {
				Optional.ofNullable(data.getData()).ifPresent(list -> list.forEach(mapData -> {
					List<Map<String, Object>> collect = printInfo.getData().stream().
							filter(print -> StrUtil.equals(Convert.toStr(print.get("dataId")), Convert.toStr(mapData.get("id")))).
							collect(Collectors.toList());
					Optional.of(collect).ifPresent(listMap -> {
						if (ObjectUtil.isNotEmpty(listMap)) {
							mapData.putAll(listMap.get(0));
						}
					});
				}));
			} else {
				// 为空时，补一下为空数据
				Optional.ofNullable(data.getData()).ifPresent(list -> list.forEach(map -> {
					map.put("isprintpop", "0");
					columnList.remove("isprintpop");
					columnList.forEach(column -> map.put(column, null));
				}));
			}
			// 如果是以打印信息中的字段排序，需要重新排序
			if (isOrder.get()) {
				Optional.ofNullable(data.getData()).ifPresent(list -> {
					String key = orderByMap.keySet().iterator().next();
					String value = orderByMap.values().iterator().next();
					List<Map<String, Object>> collect = list.stream().sorted(Comparator.comparing(order -> Convert.toStr(order.get(key), ""))).collect(Collectors.toList());
					if (StrUtil.equalsIgnoreCase(value, "desc")) {
						ListUtil.reverse(collect);
					}
					data.setData(collect);
				});
			}
		});
		if(ObjectUtils.isEmpty(dataTablesOutput)) {
			dataTablesOutput = new DataTablesOutput<Map<String, Object>>();
		}
		return dataTablesOutput;
	}
	/**
	 * 获取需要查询的字段名。
	 *
	 * @param vouTid 凭证类型编号。
	 * @return 凭证类型中需要查询的字段。
	 */
	private String getSqlFields(String vouTid,ViewModel viewModel) {
		List<BvVoutypeDataField> fieldList = bvVoutypeDataFieldService.listVoutypeDataFieldValidByVouTid(vouTid);
		StringBuilder sqlField = new StringBuilder();
		AtomicBoolean isMoney= new AtomicBoolean(true);//控制金额字段只初始一次
		Optional.ofNullable(fieldList).ifPresent(listField -> {
			listField.forEach(info -> {
				// 过滤掉不需要打印的字段，以及固定显示的ID和corp_id
				if ("1".equals(info.getIsShow()) || "id".equalsIgnoreCase(info.getFieldCode())
						|| "corp_id".equalsIgnoreCase(info.getFieldCode())) {
					// 金额字段只显示一次
					if (BlankvouConstant.BV_VOUTYPE_DATATYPE_MONEY.equals(info.getDataType()) && isMoney.get()) {
						isMoney.set(false);
						sqlField.append(" ,s.").append(info.getFieldCode()).append(" as ").append(BlankvouConstant.BV_VOUTYPE_CODE_OPMONEY);
					} else {
						// 支票业务中，需要计算，做特殊处理
						if (ViewModel.VIEW_PAYEE.equals(viewModel) && StrUtil.equalsIgnoreCase(info.getFieldCode(), "curMoney")) {
							sqlField.append(" ,(( case s.CUR_EXRATE when null then 1 else s.CUR_EXRATE end ) * s.BILL_MONEY)").append(" as ").append(info.getFieldCode());
//							sqlField.append(" ,(1 * s.BILL_MONEY)").append(" as ").append(info.getFieldCode());
						} else {
							// 避免关键字，统一加
							sqlField.append(" ,s.").append(info.getFieldCode()).append(" as ").append(info.getFieldCode().replace("_", ""));
						}
					}
				}
			});
		});
		// 去掉第一个,前缀
		return StrUtil.removePrefix(sqlField, " ,");
	}
	/**
	 * 从凭证模块获取支票类型的打印数据。
	 *
	 * @param input      查询条件。
	 * @param listFields 查询字段。
	 * @param where      查询条件（单位编号）。
	 * @param isOrder    是否需要排序。
	 * @return 支票凭证类数据。
	 */
	public DataTablesOutput<Map<String, Object>> listBvPrintVouOpByVoucher(DataTablesInput input, String listFields, String where, Map<String,Object> params, boolean isOrder) {
		String sqlField = "select " + listFields +
				" from bv_payee_cheque s " +
				" where s.status = 5 " +
				" and " + where;
		if (isOrder){
			// 移除当前排序
			input.getOrder().remove(0);
		}
		return bvPrintVouOpinfoDao.listBvPrintVouOpInfo(input, sqlField, params);
	}
	/**
	 * 根据支票编号获取支票数据。
	 *
	 * @param listFields 查询字段。
	 * @param dataId 支票编号。
	 * @return 支票数据。
	 */
	public Map<String,Object> queryBvPrintVouOpByVoucher(String listFields, String dataId){
		String sqlField = "select " + listFields +
				" from bv_payee_cheque s " +
				" where s.status = 5 " +
				" and s.id=:dataId" ;
		Map<String,Object> params = new HashMap<>();
		params.put("dataId",dataId);
		return bvPrintVouOpinfoDao.getBySQL(sqlField,params);
	}
	/**
	 * 根据打印编号获取打印信息。
	 *
	 * @param popId 打印编号。
	 * @return 打印信息。
	 */
	public Map<String, Object> queryBvPrintInfo(String popId) {
		Map<String, Object> params = new HashMap<>();
		StringBuilder sqlField = new StringBuilder("select p.id as idpop,p.vou_no as vounopop ");
		sqlField.append(" ,(CASE WHEN p.print_size = :print_size1 THEN :print_size1 ELSE :print_size0 END) as isprintpop ");
		params.put("print_size1", "1");
		params.put("print_size0", "0");
		sqlField.append(" ,p.data_id as dataId ");
		sqlField.append(" ,p.print_size as printsizepop ");
		sqlField.append(" ,p.UPDATE_BY as opbypop ");
		sqlField.append(" ,p.UPDATE_NAME as opnamepop ");
		sqlField.append(" ,p.UPDATE_DATE as opdatepop ");
		sqlField.append(" from bv_print_vou_opinfo p ");
		sqlField.append(" where p.id =:popId ");
		params.put("popId", popId);
		return bvPrintVouOpinfoDao.getBySQL(sqlField.toString(), params);
	}
	/**
	 * 获取已打印信息。
	 *
	 * @param input   查询条件。
	 * @param vouTid  凭证类型编号。
	 * @param params  查询条件。
	 * @param isOrder 是否需要排序。
	 * @return 已打印信息。
	 */
	public DataTablesOutput<Map<String, Object>> listBvPrintInfo(DataTablesInput input,String vouTid, Map<String,Object> params, boolean isOrder){
		StringBuilder sqlField=new StringBuilder("select p.id as idpop,p.vou_no as vounopop ");
		sqlField.append(" ,(CASE WHEN p.print_size = :print_size1 THEN :print_size1 ELSE :print_size0 END) as isprintpop ");
		params.put("print_size1", "1");
		params.put("print_size0", "0");
		sqlField.append(" ,p.data_id as dataId ");
		sqlField.append(" ,p.print_size as printsizepop ");
		sqlField.append(" ,p.UPDATE_BY as opbypop ");
		sqlField.append(" ,p.UPDATE_NAME as opnamepop ");
		sqlField.append(" ,p.UPDATE_DATE as opdatepop ");
		sqlField.append(" from bv_print_vou_opinfo p ");
		sqlField.append(" where p.vou_tid=:vou_tid ");
		params.put("vou_tid", vouTid);
		if (!isOrder){
			// 移除当前排序
			input.getOrder().remove(0);
		}
		return bvPrintVouOpinfoDao.listBvPrintVouOpInfo(input, sqlField.toString(), params);
	}
//	public static void main(String[] args) {
//		Map<String, String> orderByMap = new LinkedHashMap<String, String>();
//		orderByMap.put("isprintpop","desc");
//		Optional.ofNullable(orderByMap).ifPresent(list->{
//			List<String> columnList = ListUtil.toList("idpop","vounopop","isprintpop","printsizepop","opbypop","opnamepop","opdatepop");
//			List<String> collect = list.keySet().stream().filter(map -> !columnList.contains(map)).collect(Collectors.toList());
//			System.out.println("collect = " + collect);
//		});
//		String data = "{\"draw\":2,\"recordsTotal\":2,\"recordsFiltered\":2,\"insertInformation\":null,\"data\":[{\"payerbankname\":\"兴业银行股份有限公司深圳分行\",\"isprintpop\":\"0\"},{\"payerbankname\":\"中国工商银行股份有限公司深圳软件园支行\",\"isprintpop\":\"1\"}],\"error\":null}";
//		DataTablesOutput<Map<String, Object>> dataTablesOutput = JSONUtil.toBean(data, DataTablesOutput.class);
//		System.out.println("dataTablesOutput = " + dataTablesOutput.getData());
//		dataTablesOutput.getData().forEach(outData->{
//			outData.put("insert","0");
//		});
//		System.out.println("dataTablesOutput = " + dataTablesOutput.getData());
//		String key = orderByMap.keySet().iterator().next();
//		String value = orderByMap.values().iterator().next();
//		System.out.println("key = " + key);
//		System.out.println("value = " + value);
//		List<Map<String, Object>> collect = dataTablesOutput.getData().stream().sorted(Comparator.comparing(order -> Convert.toStr(order.get(key),""))).collect(Collectors.toList());
//		System.out.println("collect = " + collect);
//		if (StrUtil.equalsIgnoreCase(value,"desc")){
//			ListUtil.reverse(collect);
//		}
//		System.out.println("collect = " + collect);
//	}
	/**
	 * 查询凭证打印操作列表信息
	 * @param input
	 * @return
	 */
	@SuppressWarnings({ "rawtypes"})
	@Transactional(readOnly = true)
	public DataTablesOutput listBvPrintVouOpInfo(DataTablesInput input) {
		Map<String, Object> params = new HashMap<>();
		Map<String, Object> formMap = input.getFormdata();
		String vouTid = String.valueOf(formMap.get("voucherTypeId"));
		input.getFormdata().remove("voucherTypeId");
		input.getFormdata().remove("voucherType");
		BvVoucherType vouTypeInfo = null;
		if (StringUtils.isNotBlank(vouTid) && !"null".equals(vouTid)) {
			vouTypeInfo = bvVoucherTypeService.get(vouTid);
		}
		if (vouTypeInfo != null && StringUtils.isBlank(vouTypeInfo.getDataSource())) {
			throw new BusinessException(BlankvouMessageEnum.BLANKVOU_MESSAGE_NO_DATA_SOURE);
		}
		String sql = getBvPrintOpInfoSql(vouTypeInfo, params);
		String corpAuthSql = SqlUtil.getInSqlByParams(
				corpService.getUserCorpIds(BlankvouConstant.AB_BILL_RESOURCE_BVPRINTVOUOPINFO_QUERY, input.getCurrentUserId()),
				"s.corp_id",
				params);
		if (vouTypeInfo != null) {
			sql += " and " + corpAuthSql;
		}
		return bvPrintVouOpinfoDao.listBvPrintVouOpInfo(input, sql, params);
	}
	
	/**
	 * 根据凭证类型实体获取套打操作数据信息
	 */
	@Transactional(readOnly = true)
	public String getBvPrintOpInfoSql(BvVoucherType vouTypeInfo, Map<String, Object> params) {
		StringBuilder sqlField = new StringBuilder("select p.id as idpop,p.vou_no as vounopop ");
		sqlField.append(" ,(CASE WHEN p.print_size = :print_size1 THEN :print_size1 ELSE :print_size0 END) as isprintpop ");
		params.put("print_size1", "1");
		params.put("print_size0", "0");
		sqlField.append(" ,p.print_size as printsizepop ");
		sqlField.append(" ,p.UPDATE_BY as opbypop ");
		sqlField.append(" ,p.UPDATE_NAME as opnamepop ");
		sqlField.append(" ,p.UPDATE_DATE as opdatepop ");
		StringBuilder sqlForm = new StringBuilder(" from "+vouTypeInfo.getDataSource()+" s ");
		boolean isMoney = true;// 控制金额字段只初始一次
		if (vouTypeInfo != null) {
			List<BvVoutypeDataField> vtDaFieldList = bvVoutypeDataFieldService.listVoutypeDataFieldValidByVouTid(vouTypeInfo.getId());
			sqlForm.append(" left join ").append("bv_print_vou_opinfo").append(" p on p.data_id=s.id and p.vou_tid=:vou_tid ");
			params.put("vou_tid", vouTypeInfo.getId());
			for (BvVoutypeDataField info : vtDaFieldList) {
				if ("1".equals(info.getIsShow())
						|| "id".equalsIgnoreCase(info.getFieldCode())
						|| "corp_id".equalsIgnoreCase(info.getFieldCode())) {
					if (BlankvouConstant.BV_VOUTYPE_DATATYPE_MONEY.equals(info.getDataType())
							&& isMoney) {
						isMoney = false;
						sqlField.append(" ,s.").append(info.getFieldCode()).append(" as ").append(BlankvouConstant.BV_VOUTYPE_CODE_OPMONEY);
					} else {
						sqlField.append(" ,s.").append(info.getFieldCode()).append(" as ").append(info.getFieldCode().replace("_", ""));
					}
				}
			}
		}
		sqlForm.append(" where 1=1 ");
		sqlField.append(sqlForm);
		return sqlField.toString();
	}
	
	/**
	 * 套打格式设置预览功能
	 * @param bvPrintFormatVo
	 * @param userVo
	 * @return
	 */
	@Transactional(readOnly = true)
	public List<BvPrintFormatVo> printView(BvPrintFormatVo bvPrintFormatVo, ICurrentUser userVo){
		List<BvPrintFormatVo> printFmtList=new ArrayList<BvPrintFormatVo>();
		bvPrintFormatVo.setIndex(1);
		bvPrintFormatVo.setIsMatch("1");
		setBvPrintFormatDtlData(bvPrintFormatVo.getPrintFmtDtl(),null,true,userVo);
		bvPrintFormatVo.setId("viewId");
		printFmtList.add(bvPrintFormatVo);
		return printFmtList;
	}
	
	/**
	 * 批量获取套打格式模版
	 * @param bvPrintVouOpinfoVo
	 * @return
	 */
	@Transactional(readOnly = true)
	public List<BvPrintFormatVo> loadPrintFormat(BvPrintVouOpinfoVo bvPrintVouOpinfoVo, ICurrentUser userVo){
		List<BvPrintFormatVo> printFmtList=new ArrayList<BvPrintFormatVo>();
		String[] ids=String.valueOf(bvPrintVouOpinfoVo.getDataIds()).split(",");
		String[] vouNos=null;
		boolean isInbox=false;
		//设置是否占用凭证号判断1、凭证类型启用入库；2、打印场景未维护或者为默认打印场景（第一个对应场景）
		if("1".equals(bvPrintVouOpinfoVo.getIsInbox())){
			BvVoutypeScene scene=null;
			if(StringUtils.isNotEmpty(bvPrintVouOpinfoVo.getSceneId())){
				scene = bvVoutypeSceneService.get(bvPrintVouOpinfoVo.getSceneId(), true);
			}
			if(scene==null||scene.getOrdernum()==1){
				isInbox=true;
			}
		}
		if(isInbox){
			vouNos=String.valueOf(bvPrintVouOpinfoVo.getPrintNo()).split(",");
		}
		boolean isStorp=false;
		Map<String, List<BvPrintFormatVo>> printFmtVoMap=new TreeMap<String, List<BvPrintFormatVo>>();
		Map<String,String> vouNoMap=new HashMap<String, String>();
		String vouNoNew="";//已取得最新凭证号
		for(int i=0;i<ids.length;i++){
			if(isStorp){
				break;
			}
			String dataId=ids[i];
			BvPrintFormatVo printFmt=getPrintFormat(bvPrintVouOpinfoVo.getVouTid(), bvPrintVouOpinfoVo.getSceneId(), dataId,userVo);
			if(printFmt==null){
				printFmt=new BvPrintFormatVo();
				printFmt.setIsMatch("0");
				printFmt.setId(bvPrintVouOpinfoVo.getVouTid());
				isStorp=true;
			}else{
				printFmt.setIsMatch("1");
				//凭证占用判断
				if(isInbox){
					BvPrintVouOpinfo opinfo=bvPrintVouOpinfoDao.getBvPrintVouOpinfoByDataIdVouTid(dataId,bvPrintVouOpinfoVo.getVouTid());
					//判断凭证号是否操作
					String vouNo=vouNos[i];
					if(vouNoMap.containsKey(String.valueOf(vouNo))){//前端填写的凭证已读取过提示凭证错误
						printFmt.setIsMatch("4");
						printFmt.setVouNo(vouNo);
						isStorp=true;
					}else if(StringUtils.isEmpty(vouNo)
							||"undefined".equals(vouNo)//前端未传递凭证号进此处
							||(opinfo!=null&&vouNo.equals(opinfo.getVouNo()))//或者前端传递凭证号和之前打印的凭证号一致的 重打凭证
							){
						if(StringUtils.isNotEmpty(vouNoNew)){//缓存最新的凭证号如果已经存在，获取大于等于最新凭证号的第二笔
							List<BvVouInfoDtl> vouDtlInfo=bvVouInfoDtlService.getBvVouInfoDtlByLoginName(userVo.getLoginName(), bvPrintVouOpinfoVo.getVouTid(), vouNoNew, 2,printFmt.getBankType(),printFmt.getBankAcc(),printFmt.getCorpId());
							if(vouDtlInfo!=null&&vouDtlInfo.size()>1){
								printFmt.setVouNo(vouDtlInfo.get(1).getVouNo());
								printFmt.setVouId(vouDtlInfo.get(1).getId());
							}else{//凭证号获取失败
								printFmt.setIsMatch("2");
								isStorp=true;
							}
						}else{//最新的凭证号如果不存在 获取最小的凭证号
							List<BvVouInfoDtl> vouDtlInfo=bvVouInfoDtlService.getBvVouInfoDtlByLoginName(userVo.getLoginName(), bvPrintVouOpinfoVo.getVouTid(), 1,printFmt.getBankType(),printFmt.getBankAcc(),printFmt.getCorpId());
							if(vouDtlInfo!=null&&!vouDtlInfo.isEmpty()){
								printFmt.setVouNo(vouDtlInfo.get(0).getVouNo());
								printFmt.setVouId(vouDtlInfo.get(0).getId());
							}else{//凭证号获取失败
								printFmt.setIsMatch("2");
								isStorp=true;
							}
						}
					}else if(opinfo==null||!vouNo.equals(opinfo.getVouNo())){
						List<BvVouInfoDtl> vouDtlInfo=bvVouInfoDtlService.getBvVouInfoDtlByLoginName(userVo.getLoginName(), bvPrintVouOpinfoVo.getVouTid(),vouNo, 2,printFmt.getBankType(),printFmt.getBankAcc(),printFmt.getCorpId());
						if(vouDtlInfo!=null&&!vouDtlInfo.isEmpty()){
							printFmt.setVouNo(vouDtlInfo.get(0).getVouNo());
							printFmt.setVouId(vouDtlInfo.get(0).getId());
							if(!vouNo.equals(printFmt.getVouNo())){
								printFmt.setIsMatch("3");
								printFmt.setVouNo(vouNo);
								isStorp=true;
							}
						}else{
							printFmt.setIsMatch("3");
							printFmt.setVouNo(vouNo);
							isStorp=true;
						}
					}
					if(vouNoMap.containsKey(printFmt.getVouNo())){
						printFmt.setIsMatch("4");
						isStorp=true;
					}
					vouNoNew=printFmt.getVouNo();
					vouNoMap.put(printFmt.getVouNo(), printFmt.getVouNo());
				}
			}
			printFmt.setOpInfoId(dataId);
			printFmt.setIndex(i+1);
			if(printFmtVoMap.containsKey(printFmt.getId())){
				printFmtVoMap.get(printFmt.getId()).add(printFmt);
			}else{
				List<BvPrintFormatVo> printFmtListNew=new ArrayList<BvPrintFormatVo>();
				printFmtListNew.add(printFmt);
				printFmtVoMap.put(printFmt.getId(), printFmtListNew);
			}
		}
		if(!printFmtVoMap.isEmpty()){
			Iterator<String> iter=printFmtVoMap.keySet().iterator();
			while(iter.hasNext()){
				printFmtList.addAll(printFmtVoMap.get(iter.next()));
			}
		}
		return printFmtList;
	}
	
	/**
	 * 根据数据ID和凭证类型 获取套打的格式信息
	 * @param vouTid 配置类型ID
	 * @param sceneId 打印场景ID
	 * @param dataId 数据ID
	 * @return
	 */
	@Transactional(readOnly = true)
	public BvPrintFormatVo getPrintFormat(String vouTid, String sceneId, String dataId, ICurrentUser userVo){
		List<BvPrintFormatVo> printFmtList=bvPrintFormatService.getPrintFormatByVouTid(vouTid, sceneId);
		if(printFmtList!=null&&!printFmtList.isEmpty()){
			Map<String,Object> dataInfo=getPrintDataInfo(vouTid, dataId);
			if(!dataInfo.isEmpty()){
				for(BvPrintFormatVo pintFmt:printFmtList ){
					boolean isMatch=printMatchOp(dataInfo, pintFmt.getId(),userVo);
					if(isMatch){
						setPrintDataTypeVal(pintFmt, dataInfo);
						List<BvPrintFormatDtl> printFmtDtlList=bvPrintFormatDtlService.getIsPrintFmtInfoByPrintId(pintFmt.getId());
						if(printFmtDtlList!=null&&!printFmtDtlList.isEmpty()){
							setBvPrintFormatDtlData(printFmtDtlList,dataInfo,false,userVo);
							pintFmt.setPrintFmtDtl(printFmtDtlList);
							return pintFmt;
						}
					}
				}
			}
		}
		return null;
	}
	
	/**
	 * 设置打印特殊处理
	 */
	@Transactional(readOnly = true)
	public void setPrintDataTypeVal(BvPrintFormatVo pintFmt,Map<String,Object> dataInfo){
		//设置打印金额特殊处理
		Object moneyObj=getValueByMapKey(dataInfo, BlankvouConstant.BV_VOUTYPE_CODE_OPMONEY);
		pintFmt.setMoney(objToString(moneyObj));
		//设置打印银行账户特殊处理
		Object bankAccObj=getValueByMapKey(dataInfo, BlankvouConstant.BV_VOUTYPE_CODE_OPBANKACC);
		pintFmt.setBankAcc(objToString(bankAccObj));
		//设置打印银行大类特殊处理
		Object bankTypeObj=getValueByMapKey(dataInfo, BlankvouConstant.BV_VOUTYPE_CODE_OPBANKTYPE);
		pintFmt.setBankType(objToString(bankTypeObj));
		//设置打印单位ID获取值
		Object corpIdObj=getValueByMapKey(dataInfo, BlankvouConstant.BV_VOUTYPE_CODE_OPCORPID);
		pintFmt.setCorpId(objToString(corpIdObj));
		//凭证套打完成回调servicebean
		Object servicebeanpop=getValueByMapKey(dataInfo, BlankvouConstant.BV_VOUTYPE_CODE_SERVICEBEAN);
		pintFmt.setServicebeanpop(objToString(servicebeanpop));
	}
	
	/**
	 * 根据数据ID和凭证类型获取需要打印处理的数据信息
	 * @param vouTid
	 * @param dataId
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@Transactional(readOnly = true)
	public Map<String,Object> getPrintDataInfo(String vouTid,String dataId){
		Map<String,Object> dataInfo=new HashMap<>();
		BvVoucherType vouTypeInfo=null;
		if(StringUtils.isNotBlank(vouTid)&&!"null".equals(vouTid)){
			vouTypeInfo=bvVoucherTypeService.get(vouTid);
		}
		Map<String,String> dictGroupInfo=new HashMap<>();
		if(vouTypeInfo!=null){
			StringBuilder sql=new StringBuilder("");
			List<BvVoutypeDataField> vtDaFieldList=bvVoutypeDataFieldService.listVoutypeDataFieldValidByVouTid(vouTypeInfo.getId());
			if(vtDaFieldList!=null&&!vtDaFieldList.isEmpty()){
				boolean isAppendMoney=true;//设置只取第一次
				boolean isAppendBankAcc=true;//设置只取第一次
				boolean isAppendBankType=true;//设置只取第一次
				boolean isServiceBean=true;//设置只取第一次
				for(BvVoutypeDataField info:vtDaFieldList){
					if(BlankvouConstant.BV_VOUTYPE_CODE_OPCODE.equals(info.getFieldCode())
							||BlankvouConstant.BV_VOUTYPE_CODE_OPNAME.equals(info.getFieldCode())
							||BlankvouConstant.BV_VOUTYPE_CODE_OPDATE.equals(info.getFieldCode())){
						continue;
					}
					String fieldStr=" s."+info.getFieldCode()+" ";
					if(sql.indexOf(fieldStr)<0){//sql中排除重复的字段
						if(sql.length()>0){
							sql.append(",");
						}
						sql.append(fieldStr);
						if(BlankvouConstant.BV_VOUTYPE_DATATYPE_MONEY.equals(info.getDataType())
								&&isAppendMoney){//对标识的金额特殊处理
							isAppendMoney=false;
							sql.append(",").append(" s.").append(info.getFieldCode()).append(" as "+BlankvouConstant.BV_VOUTYPE_CODE_OPMONEY+" ");
						}else if(BlankvouConstant.BV_VOUTYPE_DATATYPE_BANKACC.equals(info.getDataType())
								&&isAppendBankAcc){//对标识的银行账户特殊处理
							isAppendBankAcc=false;
							sql.append(",").append(" s.").append(info.getFieldCode()).append(" as "+BlankvouConstant.BV_VOUTYPE_CODE_OPBANKACC+" ");
						}else if(BlankvouConstant.BV_VOUTYPE_DATATYPE_BANKTYPE.equals(info.getDataType())
								&&isAppendBankType){//对标识的银行大类特殊处理
							isAppendBankType=false;
							sql.append(",").append(" s.").append(info.getFieldCode()).append(" as "+BlankvouConstant.BV_VOUTYPE_CODE_OPBANKTYPE+" ");
						}else if(BlankvouConstant.BV_VOUTYPE_DATATYPE_SERVICEBEAN.equals(info.getDataType())
								&&isServiceBean){//对标识的银行大类特殊处理
							isServiceBean=false;
							sql.append(",").append(" s.").append(info.getFieldCode()).append(" as "+BlankvouConstant.BV_VOUTYPE_CODE_SERVICEBEAN+" ");
						}
					}
					if(StringUtils.isNotEmpty(info.getDictGroup())&&StringUtils.isNotEmpty(info.getDictGroup().trim())){
						dictGroupInfo.put(info.getFieldCode(), info.getDictGroup());
					}
				}
				if(vouTypeInfo.getDataSource() != null  ){
					try {
						 bvVoucherTypeService.isExistTable(vouTypeInfo.getDataSource());
					} catch (Exception e) {
						if (e.getCause() != null) { //获取错误信息
							throw new BusinessException(BlankvouMessageEnum.BLANKVOU_MESSAGE_DATA_SOURCE_ILLEGAL);
						} else { //未知错误,请检查!
							throw new BusinessException(BlankvouMessageEnum.BLANKVOU_MESSAGE_ERROR_CAUSEISNULL, e.getCause().getMessage());

						}
					}

				}
				sql.append(" from ").append(vouTypeInfo.getDataSource()).append(" s where s.id=:DATAID ");
			}
			Map<String,Object> params=new HashMap<>();
			params.put("DATAID", dataId);
			dataInfo=bvPrintVouOpinfoDao.getBySQL(("select "+sql.toString()).toUpperCase(), params);
			//设置字典值
			if(dataInfo!=null&&dataInfo.size()>0
					&&dictGroupInfo.size()>0){
				Iterator<String> keys=dictGroupInfo.keySet().iterator();
				while(keys.hasNext()){
					String key=keys.next();
					String dataInfoKey=key.toUpperCase();
					if(dataInfo.containsKey(dataInfoKey)){
						String valNew=dictService.get(dictGroupInfo.get(key), dataInfo.get(dataInfoKey));
						dataInfo.put(dataInfoKey, valNew);
					}else if(dataInfo.containsKey(key.toLowerCase())){
						dataInfoKey=key.toLowerCase();
						String valNew=dictService.get(dictGroupInfo.get(key), dataInfo.get(dataInfoKey));
						dataInfo.put(dataInfoKey, valNew);
					}
				}
			}
		}
		return dataInfo;
	}
	
	/**
	 * 打印数据和套打格式匹配规则进行匹配匹配上则返回true
	 * @param dataInfo
	 * @param printId
	 * @param userVo
	 * @return
	 */
	@Transactional(readOnly = true)
	public boolean printMatchOp(Map<String,Object> dataInfo,String printId,ICurrentUser userVo){
		boolean reBoo=true;
		String mrValue;
		String dataValue;
		List<BvPrintMatchRuleVo> printMR=bvPrintMatchRuleService.getCanMatchInfoByPrintId(printId);
		if(printMR==null){
			return true;
		}
		for(BvPrintMatchRuleVo info:printMR){
			mrValue=String.valueOf(info.getMatchVal());
			if(BlankvouConstant.BV_VOUTYPE_CODE_OPCODE.equals(info.getCode()) && userVo!=null){
				dataValue=userVo.getLoginName();
			}else if(BlankvouConstant.BV_VOUTYPE_CODE_OPNAME.equals(info.getCode()) && userVo!=null){
				dataValue=userVo.getUsername();
			}else{
				dataValue=String.valueOf(getValueByMapKey(dataInfo, info.getCode()));
			}
			if("null".equals(dataValue)){
				return false;
			}
			if(BlankvouConstant.BV_PRINT_MATCH_RULE_ALL.equals(info.getMatchRule())){
				if(!dataValue.equals(mrValue)){
					return false;
				}
			}else if(BlankvouConstant.BV_PRINT_MATCH_RULE_LIKE.equals(info.getMatchRule())){
				if(dataValue.indexOf(mrValue)<0){
					return false;
				}
			}else if(BlankvouConstant.BV_PRINT_MATCH_RULE_LLIKE.equals(info.getMatchRule())){
				if(!dataValue.startsWith(mrValue)){
					return false;
				}
			}else if(BlankvouConstant.BV_PRINT_MATCH_RULE_RLIKE.equals(info.getMatchRule())){
				if(!dataValue.endsWith(mrValue)){
					return false;
				}
			}else if(BlankvouConstant.BV_PRINT_MATCH_RULE_INTERVAL.equals(info.getMatchRule())){
				try{
					Double beginNum=Double.valueOf(mrValue.split(",")[0]);
					Double endNum=Double.valueOf(mrValue.split(",")[1]);
					Double numVal;
					Object valObj=getValueByMapKey(dataInfo, info.getCode());
					numVal=objToDouble(valObj);
					if(valObj==null){
						return false;
					}
					if(numVal<beginNum||numVal>endNum){
						return false;
					}
				}catch(Exception e){
					this.logger.error(e.toString());
					ExceptionStackUtils.getStack(e);
					return false;
				}
			}else{
				return false;
			}
		}
		return reBoo;
	}
	
	
	/**
	 * 给打印明细数据信息赋值
	 * @param printFmtDtlList 套打明细信息
	 * @param dataInfo 数据信息
	 * @param isView 是否套打格式预览功能
	 * @param userVo 操作用户
	 * @return
	 */
	@Transactional(readOnly = true)
	public void setBvPrintFormatDtlData(List<BvPrintFormatDtl> printFmtDtlList,Map<String,Object> dataInfo,boolean isView,ICurrentUser userVo){
		for(BvPrintFormatDtl info:printFmtDtlList){
			if("1".equals(info.getIsPrint())){
				if(BlankvouConstant.BV_VOUTYPE_CODE_OPCODE.equals(info.getCode())
						&&StringUtils.isEmpty(info.getOtherVal())){
					info.setOtherVal(userVo.getLoginName());
				}else if(BlankvouConstant.BV_VOUTYPE_CODE_OPNAME.equals(info.getCode())
						&&StringUtils.isEmpty(info.getOtherVal())){
					info.setOtherVal(userVo.getUsername());
				}else if(BlankvouConstant.BV_VOUTYPE_CODE_OPDATE.equals(info.getCode())
						&&StringUtils.isEmpty(info.getOtherVal())){
					setBvPrintFormatDtlData(info, new Date(),false);
				}else{
					Object val=getValueByMapKey(dataInfo, info.getCode());
					if(val==null&&info.getOtherVal()!=null&&info.getOtherVal().length()>0){
						val=info.getOtherVal();
					}
					setBvPrintFormatDtlData(info, val, isView);
				}
			}
		}
	}
	
	/**
	 * 给打印明细数据信息赋值
	 * @param info
	 * @param val
	 * @param isView 是否套打格式预览功能
	 * @return
	 */
	@Transactional(readOnly = true)
	public void setBvPrintFormatDtlData(BvPrintFormatDtl info,Object val, boolean isView) {
		if(val!=null&&info!=null){
			if(val instanceof Date){
				info.setOtherVal(getDateFmtInfo(info.getValPrintType(),info.getValPrintFmt(),(Date)val));
			}else if(val instanceof Double){
				info.setOtherVal(getMoneyFmtInfo(info.getValPrintType(),info.getValPrintFmt(),(Double)val));
			}else if(val instanceof Integer){
				info.setOtherVal(getMoneyFmtInfo(info.getValPrintType(),info.getValPrintFmt(),Double.valueOf((Integer)val)));
			}else if(val instanceof Float){
				info.setOtherVal(getMoneyFmtInfo(info.getValPrintType(),info.getValPrintFmt(),Double.valueOf((Float)val)));
			}else if(val instanceof BigDecimal){
				info.setOtherVal(getMoneyFmtInfo(info.getValPrintType(),info.getValPrintFmt(),Double.valueOf(((BigDecimal)val).doubleValue())));
			}else if(val instanceof String&&!BlankvouConstant.BV_PRINT_VALTYPE_STR.equals(info.getValPrintType())){
				if(BlankvouConstant.BV_PRINT_VALTYPE_MONEYCNY.equals(info.getValPrintType())
						||BlankvouConstant.BV_PRINT_VALTYPE_MONEYNUM.equals(info.getValPrintType())
						||BlankvouConstant.BV_PRINT_VALTYPE_MONEYNUMCNY.equals(info.getValPrintType())){
					try{
						info.setOtherVal(getMoneyFmtInfo(info.getValPrintType(),info.getValPrintFmt(),Double.valueOf(String.valueOf(val))));
					}catch(Exception e){
						this.logger.error(e.toString());
						ExceptionStackUtils.getStack(e);
					}
				}else if(BlankvouConstant.BV_PRINT_VALTYPE_DATECNY.equals(info.getValPrintType())
						||BlankvouConstant.BV_PRINT_VALTYPE_DATENUM.equals(info.getValPrintType())){
					try{
						String dateStr=String.valueOf(val);
						dateStr=dateStr.replace(".", "");
						dateStr=dateStr.replace(",", "");
						info.setOtherVal(getDateFmtInfo(info.getValPrintType(),info.getValPrintFmt(),DateUtils.parseDate(dateStr, "yyyyMMdd")));
					}catch(Exception e){
						this.logger.error(e.toString());
						ExceptionStackUtils.getStack(e);
					}
				}else{
					info.setOtherVal(String.valueOf(val));
				}
			}else{
				info.setOtherVal(String.valueOf(val));
			}
		}else if(isView&&info!=null&&val==null){
			if(BlankvouConstant.BV_PRINT_VALTYPE_MONEYCNY.equals(info.getValPrintType())
					||BlankvouConstant.BV_PRINT_VALTYPE_MONEYNUM.equals(info.getValPrintType())
					||BlankvouConstant.BV_PRINT_VALTYPE_MONEYNUMCNY.equals(info.getValPrintType())){
				info.setOtherVal(getMoneyFmtInfo(info.getValPrintType(),info.getValPrintFmt(),0D));
			}else if(BlankvouConstant.BV_PRINT_VALTYPE_DATECNY.equals(info.getValPrintType())
					||BlankvouConstant.BV_PRINT_VALTYPE_DATENUM.equals(info.getValPrintType())){
				info.setOtherVal(getDateFmtInfo(info.getValPrintType(),info.getValPrintFmt(),new Date()));
			}else if(BlankvouConstant.BV_PRINT_VALTYPE_SIGNATURE.equals(info.getValPrintType())){
				//套打预览如果是签章则设置test值获取一个默认的打印签章图标
				info.setOtherVal("test");
			}else{
				info.setOtherVal(info.getName());
			}
		}
	}
	
	/**
	 * 金额格式的取值转换
	 * @param valType
	 * @param valFmt
	 * @param money
	 * @return
	 */
	@Transactional(readOnly = true)
	public String getMoneyFmtInfo(String valType,String valFmt,Double money){
		String fmt="####.##";
		if(BlankvouConstant.BV_PRINT_VALTYPE_STR.equals(valType)){
			return NumberUtil.formatNumeric(money, fmt);
		}else{
			if(BlankvouConstant.BV_PRINT_VALTYPE_MONEYCNY.equals(valType)){
				return StringUtils.chineseMoney(money);
			}else{
				if(BlankvouConstant.BV_PRINT_VALTYPEFMT_MONEY1.equals(valFmt)){
					fmt="####.00";
				}else if(BlankvouConstant.BV_PRINT_VALTYPEFMT_MONEY2.equals(valFmt)){
					fmt="#,###.00";
				}else{
					fmt="####.00";
					String newMoney=NumberUtil.formatNumeric(money, fmt).replace(".", "");
					if(BlankvouConstant.BV_PRINT_VALTYPE_MONEYNUMCNY.equals(valType)){
						return StringUtils.numToChinese(newMoney);
					}else{
						return newMoney;
					}
				}
				return NumberUtil.formatNumeric(money, fmt);
			}
		}
	}
	
	/**
	 * 日期格式的取值转换
	 * @param valType
	 * @param valFmt
	 * @param date
	 * @return
	 */
	@Transactional(readOnly = true)
	public String getDateFmtInfo(String valType,String valFmt,Date date){
		String fmt="yyyy-MM-dd";
		if(BlankvouConstant.BV_PRINT_VALTYPE_STR.equals(valType)){
			return DateUtils.formatDate(date, fmt);
		}else{
			if(BlankvouConstant.BV_PRINT_VALTYPEFMT_YYYYMMDD.equals(valFmt)){
				fmt="yyyyMMdd";
			}else if(BlankvouConstant.BV_PRINT_VALTYPEFMT_YYYY.equals(valFmt)){
				fmt="yyyy";
			}else if(BlankvouConstant.BV_PRINT_VALTYPEFMT_MM.equals(valFmt)){
				fmt="MM";
			}else if(BlankvouConstant.BV_PRINT_VALTYPEFMT_DD.equals(valFmt)){
				fmt="dd";
			}else if(!BlankvouConstant.BV_PRINT_VALTYPEFMT_YYYYMMDD_CNY.equals(valFmt)){
				valFmt=BlankvouConstant.BV_PRINT_VALTYPEFMT_YYYY_MM_DD;
			}
			if(BlankvouConstant.BV_PRINT_VALTYPE_DATECNY.equals(valType)){
				if(BlankvouConstant.BV_PRINT_VALTYPEFMT_YYYYMMDD_CNY.equals(valFmt)){
					return StringUtils.chineseYMD(DateUtils.formatDate(date, "yyyy"))+"年"+
							StringUtils.chineseYMD(DateUtils.formatDate(date, "MM"))+"月"+
							StringUtils.chineseYMD(DateUtils.formatDate(date, "dd"))+"日";
				}else if(BlankvouConstant.BV_PRINT_VALTYPEFMT_YYYY_MM_DD.equals(valFmt)){
					return StringUtils.chineseYMD(DateUtils.formatDate(date, "yyyy"))+"-"+
							StringUtils.chineseYMD(DateUtils.formatDate(date, "MM"))+"-"+
							StringUtils.chineseYMD(DateUtils.formatDate(date, "dd"));
				}if(BlankvouConstant.BV_PRINT_VALTYPEFMT_YYYYMMDD.equals(valFmt)){
					return StringUtils.chineseYMD(DateUtils.formatDate(date, "yyyy"))+
							StringUtils.chineseYMD(DateUtils.formatDate(date, "MM"))+
							StringUtils.chineseYMD(DateUtils.formatDate(date, "dd"));
				}else{
					return StringUtils.chineseYMD(DateUtils.formatDate(date, fmt));
				}
			}else{
				return DateUtils.formatDate(date, fmt);
			}
		}
	}
	/**
	 * 根据key去获取map值
	 * @param dataInfo
	 * @param mapKey
	 * @return
	 */
	@Transactional(readOnly = true)
	public Object getValueByMapKey(Map<String,Object> dataInfo,String mapKey) {
		Object value=null;
	    if(!StringUtils.isBlank(mapKey)&&dataInfo!=null){
	        try {
	            value=dataInfo.get(mapKey.toUpperCase());
	            if(value==null){
	            	value=dataInfo.get(mapKey.toLowerCase());
	            }
	        } catch (Exception e) {
				this.logger.error(e.toString());
				ExceptionStackUtils.getStack(e);
            }
	    }
	    return value;
	}
	
	/**
	 * 凭证打印进行占用凭证操作
	 * @param bvPrintVouOpinfoVo
	 */
	public void printOccupyVouNo(BvPrintVouOpinfoVo bvPrintVouOpinfoVo,ICurrentUser userVo){
		if(bvPrintVouOpinfoVo.getPrintData()!=null
				&&!bvPrintVouOpinfoVo.getPrintData().isEmpty()){
			for(BvPrintFormatVo printInfo:bvPrintVouOpinfoVo.getPrintData()){
				String msg=BlankvouConstant.LANG_LOG_PRINT_SUCC;
				if(StringUtils.isNotEmpty(printInfo.getVouNo())){
					BvVouEmployInfo info=new BvVouEmployInfo();
					info.setVoudNo(printInfo.getVouNo());
					info.setVoudtlId(printInfo.getVouId());
					try{
						info.setVouMoney(new BigDecimal(printInfo.getMoney()));
					}catch(Exception e){
						this.logger.error(e.toString());
						ExceptionStackUtils.getStack(e);
					}
					info.setEmployDate(new Date());
					info.setEmployPerson(userVo.getUsername());
					bvVouInfoDtlService.voucherOccupy(info,userVo);
					msg="占用凭证号为："+printInfo.getVouNo();
				}
				BvPrintVouOpinfo opInfo=bvPrintVouOpinfoDao.getBvPrintVouOpinfoByDataIdVouTid(printInfo.getOpInfoId(),printInfo.getVouTid());
				String logType=BlankvouConstant.BVLOG_LOGTYPE_PRINT;
				if(opInfo==null){
					opInfo=new BvPrintVouOpinfo();
					opInfo.setVouTid(printInfo.getVouTid());
					opInfo.setDataId(printInfo.getOpInfoId());
					opInfo.setPrintSize(1);
					opInfo.setCreateBy(userVo.getLoginName());
					opInfo.setCreateName(userVo.getUsername());
					opInfo.setCreateDate(new Date());
				}else{
					opInfo.setPrintSize(opInfo.getPrintSize()+1);
					if(msg.equals(BlankvouConstant.LANG_LOG_PRINT_SUCC)){
						msg=BlankvouConstant.LANG_LOG_REPPRINT_SUCC;
					}
					logType=BlankvouConstant.BVLOG_LOGTYPE_REPRINT;
				}
				opInfo.setUpdateBy(userVo.getLoginName());
				opInfo.setUpdateName(userVo.getUsername());
				opInfo.setUpdateDate(new Date());
				opInfo.setVouNo(printInfo.getVouNo());
				opInfo.setVoudtlId(printInfo.getVouId());
				try{
					opInfo.setVouMoney(new BigDecimal(printInfo.getMoney()));
				}catch(Exception e){
					this.logger.error(e.toString());
					ExceptionStackUtils.getStack(e);
				}
				bvPrintVouOpinfoDao.saveOrUpdate(opInfo);
				if(StringUtils.isNotEmpty(printInfo.getSceneName())){
					msg=printInfo.getSceneName()+","+multiLangServiceI.getLang(msg);
				}
				bvLogService.saveBvLog(opInfo.getId(), logType, msg,userVo);
				if(StringUtils.isNotEmpty(printInfo.getServicebeanpop())){
					// todo:回调Service待调整
					logger.info("凭证打印回调Serviec：【{}】", printInfo.getServicebeanpop());
					Object objService=this.getBean(printInfo.getServicebeanpop());
					if(objService==null){
						throw new BusinessException(BlankvouMessageEnum.BLANDVOU_MESSAGE_ERROR_PRINTSERVICEISNULL);
					}
					IVoucherPrintService printService = (IVoucherPrintService)objService;
					printService.printComplete(opInfo.getDataId(), opInfo.getVouNo(),userVo);
				}
			}
		}
	}
	/**
	 * 获取凭证打印信息详情。
	 *
	 * @param vouTypeInfo 凭证类型。
	 * @param popId       打印编号。
	 * @param dataId      凭证信息编号。
	 * @param vouTid      凭证类型编号。
	 * @return 凭证详情信息。
	 */
	public Map<String,Object> getBvPrintVouOpInfo(BvVoucherType vouTypeInfo, String popId, String dataId, String vouTid){
		// 根据视图的数据来源来获取视图所属的模块
		ViewModel viewModel = VoucherViewEnum.getModelByView(vouTypeInfo.getDataSource());
		// 组装视图中需要显示的字段，视图中的表别名统一定为【s】
		String selectField = getSqlFields(vouTypeInfo.getId(), viewModel);
		Map<String, Object> dataMap = new HashMap<>();
		switch (viewModel) {
			case VIEW_PAYEE:
				// 从【凭证模块】获取凭证打印数据
				dataMap = queryBvPrintVouOpByVoucher(selectField, dataId);
				break;
			case VIEW_NIS:
				// 从【结算模块】获取凭证打印数据
				//System.out.println("从【结算模块】获取凭证打印数据>>>>>>>>>>>>>>>>>");
				dataMap = iVoucherPrintingService.queryBvPrintVouOpById(selectField,dataId,vouTypeInfo.getDataSource());
				break;
			case VIEW_BILL:
				// 从【票据模块】获取凭证打印数据
				//System.out.println("从【票据模块】获取凭证打印数据>>>>>>>>>>>>>>>>>");
				// listData.set(new DataTablesOutput<>());
				dataMap = voucherPrintService.queryBvPrintVouOpById(selectField,dataId,vouTypeInfo.getDataSource());
				break;
			default:
				System.out.println("当前模块业务尚未实现：model = " + viewModel.getDesc());
				break;
		}
		if (ObjectUtil.isNotEmpty(dataMap)){
			// 如果不为空，则可以查询已打印信息，为空则没有打印信息
			Map<String, Object> printInfo = queryBvPrintInfo(popId);
			if (ObjectUtil.isNotEmpty(printInfo)){
				dataMap.putAll(printInfo);
			}
		}
		return dataMap;
	}
	
	/**
	 * 进去查询凭证打印操作页面初始信息
	 * @param model
	 * @param vouTid
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Transactional(readOnly = true)
	public void toViewInit(Model model, String popId, String dataId, String vouTid) {
		BvVoucherType vouTypeInfo = bvVoucherTypeService.get(vouTid);
		if (vouTypeInfo != null) {
			model.addAttribute("vouTypeInfo", vouTypeInfo);
			List<BvVoutypeDataField> vtDaFieldList = bvVoutypeDataFieldService.listVoutypeDataFieldValidByVouTid(vouTypeInfo.getId());
			Map<String, Object> params = new HashMap<>();
			// String sql=this.getBvPrintOpInfoSql(vouTypeInfo, params);
			Map dataInfo = getBvPrintVouOpInfo(vouTypeInfo, popId, dataId, vouTid);
			boolean storpMoney = true;
			for (int i = 0; i < vtDaFieldList.size(); i++) {
				BvVoutypeDataField field = vtDaFieldList.get(i);
				if ("1".equals(field.getIsShow())) {
					String mapKey = field.getFieldCode().replace("_", "");
					if (storpMoney && BlankvouConstant.BV_VOUTYPE_DATATYPE_MONEY.equals(field.getDataType())) {
						storpMoney = false;
						mapKey = BlankvouConstant.BV_VOUTYPE_CODE_OPMONEY;
					}
					Object fieldVal = getValueByMapKey(dataInfo, mapKey);
					String fieldValStr = objToString(fieldVal);
					if (StringUtils.isNotEmpty(field.getDictGroup()) && StringUtils.isNotEmpty(field.getDictGroup().trim())) {
						field.setFieldVal(dictService.get(field.getDictGroup(), fieldValStr));
					} else {
						field.setFieldVal(fieldValStr);
					}
				}
			}
			model.addAttribute("vtDaFieldList", vtDaFieldList);
			BvPrintVouOpinfo info = bvPrintVouOpinfoDao.get(popId, true);
			if (info != null) {
				model.addAttribute("vouOpInfo", info);
			}
			List<BvLog> bvlogList = bvLogService.listBvLog(popId);
			model.addAttribute("bvlogList", bvlogList);
		}
	}
	
	/**
	 * object转成Double
	 * @param val
	 * @return
	 */
	@Transactional(readOnly = true)
	public Double objToDouble(Object val){
		Double numVal=null;
		if(val instanceof Double){
			numVal=(Double) val;
		}else if(val instanceof Integer){
			numVal=new Double((Integer) val);
		}else if(val instanceof BigDecimal){
			numVal=((BigDecimal) val).doubleValue();
		}else if(val instanceof String){
			numVal=Double.valueOf(String.valueOf(val));
		}
		return numVal;
	}
	
	/**
	 * object转成字符串
	 * @param val
	 * @return
	 */
	@Transactional(readOnly = true)
	public String objToString(Object val){
		Double numVal=0D;
		if(val instanceof Double){
			numVal=(Double) val;
		}else if(val instanceof Integer){
			numVal=new Double((Integer) val);
		}else if(val instanceof BigDecimal){
			numVal=((BigDecimal) val).doubleValue();
		}else if(val instanceof String){
			return String.valueOf(val);
		}else if(val instanceof Date){
			return DateUtils.formatDate((Date)val, "yyyy-MM-dd");
		}
		if(numVal>0){
			return NumberUtil.formatNumeric(numVal, "####.##");
		}
		return "";
	}
}
