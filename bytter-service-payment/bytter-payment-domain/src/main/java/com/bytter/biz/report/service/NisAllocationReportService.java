package com.bytter.biz.report.service;
import com.alibaba.excel.util.ObjectUtils;
import com.bytter.biz.bdlink.api.constant.BdlinkConstant;
import com.bytter.biz.bdlink.api.constant.ResourceConstant;
import com.bytter.biz.payment.api.dto.BisExcDTO;
import com.bytter.biz.report.dao.NisAllocationReportDao;
import com.bytter.biz.report.vo.NisAllocationReportVO;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.enums.MatchTypeEnum;
import com.bytter.core.utils.DateUtils;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.core.utils.SqlUtil;
import com.bytter.core.utils.StringUtils;
import com.bytter.system.api.IBtCurrencyService;
import com.bytter.system.api.ICorpService;
import com.bytter.system.api.constant.CorpConstant;
import com.bytter.system.api.dto.BtCurrencyDTO;
import com.bytter.system.api.dto.CorpDTO;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.abel533.echarts.code.SeriesType;
import com.github.abel533.echarts.series.Bar;
import com.github.abel533.echarts.series.Series;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author onlineGenerator
 * @version 1.0
 * @Title: Service
 * @Description: BIS_EXC
 * @date 2017-10-09 16:31:56
 */
@Service("nisAllocationReportService")
@Transactional
public class NisAllocationReportService extends BaseService<BisExcDTO, String> {
	@Autowired
	private NisAllocationReportDao nisAllocationReportDao;
	@DubboReference(check = false)
	private ICorpService corpService;
	@DubboReference(check = false)
	private IBtCurrencyService btCurrencyService;
	@Override
	public BaseDao<BisExcDTO, String> getEntityDao() {
		return nisAllocationReportDao;
	}
	/**
	 * 资金调拨列表查询
	 *
	 * @param dataTablesInput
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public DataTablesOutput getDataTablesUnion(DataTablesInput dataTablesInput, ICurrentUser user) {
		StringBuilder sql = new StringBuilder();
		sql.append(" and t.voucherFrom in (" + getVoucherFrom(user) + ")");
		String lereqDate=(String)dataTablesInput.getFormdata().get("led_reqDate");
		if(StringUtils.isNotEmpty(lereqDate)){
			dataTablesInput.getFormdata().remove("led_reqDate");
			dataTablesInput.addFilter(MatchTypeEnum.LT,"reqDate", DateUtils.format(DateUtils.rollDay(DateUtils.parse(lereqDate, "yyyy-MM-dd"), 1)), Date.class);
		}
		StringBuilder commonSql = new StringBuilder();
		String corpAuthSql = SqlUtil.getInSql(corpService.getUserCorpIds(ResourceConstant.NETBANK_REPORT_ALLOCATION_QUERY, user.getId()), "t.corpId", true);
		commonSql.append(" and ").append(corpAuthSql);
		sql.append(commonSql);
		DataTablesOutput dataTablesOutput = nisAllocationReportDao.getDataTablesUnion(dataTablesInput, sql.toString());
		List<NisAllocationReportVO> list = dataTablesOutput.getData();
		if(null != list && list.size() > 0){
			Map<String, BtCurrencyDTO> currencyMap = btCurrencyService.getCurrency();
			for(NisAllocationReportVO vo : list){
				if(null != currencyMap){
					if(currencyMap.containsKey(vo.getCurId())){
						BtCurrencyDTO btCurrencyDTO = currencyMap.get(vo.getCurId());
						if(null != btCurrencyDTO){
							vo.setCurName(btCurrencyDTO.getCurName());
						}
					}
				}
			}
		}
		return dataTablesOutput;
	}
	/**
	 * 资金调拨汇总表
	 *
	 * @return
	 */
	public DataTablesOutput dataTableSum(DataTablesInput dataTablesInput, ICurrentUser user) {
		StringBuilder sql = new StringBuilder();
		sql.append(" and t.voucherFrom in (" + getVoucherFrom(user) + ")");
		StringBuilder commonSql = new StringBuilder();
		String corpAuthSql = SqlUtil.getInSql(corpService.getUserCorpIds(ResourceConstant.NETBANK_REPORT_ALLOCATIONSUM_QUERY, user.getId()), "t.corpId", true);
		commonSql.append(" and ").append(corpAuthSql);
		sql.append(commonSql);
		DataTablesOutput<NisAllocationReportVO> dataTablesOutput = nisAllocationReportDao.dataTableSum(dataTablesInput, sql.toString());
		List<String> curIds = dataTablesOutput.getData().stream().map(NisAllocationReportVO::getCurId).collect(Collectors.toList());
		Map<String, String> currencyNames = btCurrencyService.getCurrencyNameByIds(curIds);
		for (NisAllocationReportVO reportVO : dataTablesOutput.getData()) {
			reportVO.setCurName(currencyNames.get(reportVO.getCurId()));
		}
		return dataTablesOutput;
	}
	/**
	 * 根据当前用户判断展示业务类型
	 *
	 * @return
	 */
	private String getVoucherFrom(ICurrentUser user) {
		// 判断当前用户是否为单位用户
		CorpDTO corp = corpService.get(user.getCorpId());
		// 单位用户查询时只能选择到请款，上缴，头寸调拨，全部（的内容只能是前面三种），网点用户查询时可以选择到所有
		StringBuilder voucherFrom = new StringBuilder();
		voucherFrom.append("'" + BdlinkConstant.FROM_ALLOCATION_TYPE_ASK + "','" + BdlinkConstant.FROM_ALLOCATION_TYPE_TURN + "','" + BdlinkConstant.FROM_ALLOCATION_TYPE_POS + "'");
		if (!EmptyUtils.isEmpty(corp)) {
			if (CorpConstant.CORP_TYPE_CODE_NET.equals(corp.getType())) {
				voucherFrom.append(",'" + BdlinkConstant.FROM_ALLOCATION_TYPE_DOWN + "','" + BdlinkConstant.FROM_ALLOCATION_TYPE_UP + "'");
				voucherFrom.append(",'" + BdlinkConstant.FROM_ALLOCATION_TYPE_AUTO_DOWN + "','" + BdlinkConstant.FROM_ALLOCATION_TYPE_AUTO_UP + "'");
			}
		}
		return voucherFrom.toString();
	}
	/**
	 * 创建柱状图报表
	 * @param vo
	 * @return
	 */
	public Object createNisAllocationSumBar(NisAllocationReportVO vo, ICurrentUser user) {
		List dataList = null;
		if (!EmptyUtils.isEmpty(vo)) {
			StringBuilder sql = new StringBuilder();
			sql.append(" and t.voucherFrom in (" + getVoucherFrom(user) + ")");
			String corpAuthSql = SqlUtil.getInSql(corpService.getUserCorpIds(ResourceConstant.NETBANK_REPORT_ALLOCATIONSUM_QUERY, user.getId()), "t.corpId", true);
			sql.append(" and ").append(corpAuthSql);
			dataList = this.nisAllocationReportDao.listBy(vo.getNetId(), vo.getCorpId(), vo.getCurId(), vo.getBeginReqDate(), vo.getEndReqDate(), sql.toString());
		}
		return this.getBarData(dataList);
	}
	/**
	 * 组装报表数据
	 * @param list
	 * @return
	 */
	private Object getBarData(List<NisAllocationReportVO> list) {
		Map<String, Object> map = new HashMap<>();
		Set<String> legend = new HashSet();
		Set<String> corpSet = new HashSet();
		Map<String, CorpDTO> mapCorp = corpService.getCorpMap();
   	    Map<String, BtCurrencyDTO> mapCur = btCurrencyService.getCurrency();
		for (NisAllocationReportVO vo : list) {
			corpSet.add(!ObjectUtils.isEmpty(mapCorp.get(vo.getCorpId())) ? (String)mapCorp.get(vo.getCorpId()).getName() : "");
			vo.setCorpName(!ObjectUtils.isEmpty(mapCorp.get(vo.getCorpId())) ? (String)mapCorp.get(vo.getCorpId()).getName() : "");
			legend.add(!ObjectUtils.isEmpty(mapCur.get(vo.getCurId())) ? (String)mapCur.get(vo.getCurId()).getCurName() : "");
			vo.setCurName(!ObjectUtils.isEmpty(mapCur.get(vo.getCurId())) ? (String)mapCur.get(vo.getCurId()).getCurName() : "");
		}
		map.put("legend", legend);
		map.put("xAxisData2", corpSet);
		List<String> xAxisList = new ArrayList<>();
		for (int i = 0; i < corpSet.size(); i++) {
			xAxisList.add("归集");
			xAxisList.add("下拨");
			xAxisList.add("请款");
			xAxisList.add("上缴");
		}
		map.put("xAxisData1", xAxisList);
		List<Series> series = new ArrayList<>();
		Map<String, String> openParams = new HashMap<>();
		for (String curName : legend) {
			List<Object> datas = new ArrayList<>();
			for (String corpName : corpSet) {
				boolean flag = true;
				String curId = "";
				String corpId = "";
				for (NisAllocationReportVO vo : list) {
					if (curName.equalsIgnoreCase(vo.getCurName()) && corpName.equalsIgnoreCase(vo.getCorpName())) {
						flag = false;
						curId = vo.getCurId();
						corpId = vo.getCorpId();
						datas.add(vo.getUpPayMoney());
						datas.add(vo.getDownPayMoney());
						datas.add(vo.getAskPayMoney());
						datas.add(vo.getTurnPayMoney());
						break;
					}
				}
				if (flag) {
					datas.add(BigDecimal.ZERO);
					datas.add(BigDecimal.ZERO);
					datas.add(BigDecimal.ZERO);
					datas.add(BigDecimal.ZERO);
				} else {
					openParams.put(corpName + "归集" + curName, corpId + "!-!5,8!-!" + curId);
					openParams.put(corpName + "下拨" + curName, corpId + "!-!2,7!-!" + curId);
					openParams.put(corpName + "请款" + curName, corpId + "!-!3!-!" + curId);
					openParams.put(corpName + "上缴" + curName, corpId + "!-!6!-!" + curId);
				}
			}
			// 设置数据
			Bar bar = new Bar();
			bar.setName(curName);
			bar.setType(SeriesType.bar);
			bar.setStack("stack");
			bar.setData(datas);
			series.add(bar);
		}
		map.put("openParams", openParams);
		try {
			ObjectMapper mapper = new ObjectMapper();
			mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
			map.put("series", mapper.writeValueAsString(series));
			return map;
		} catch (JsonProcessingException e) {
			e.printStackTrace();
			return "[]";
		}
	}
	/**
	 * 手机报表数据列表
	 * @param vo
	 * @return
	 */
	public List<NisAllocationReportVO> getReportMoblieList(NisAllocationReportVO vo,ICurrentUser user) {
		StringBuilder sql = new StringBuilder();
		// 单位用户查询时只能选择到请款，上缴，头寸调拨，全部（的内容只能是前面三种），网点用户查询时可以选择到所有
		StringBuilder voucherFrom = new StringBuilder();
		voucherFrom.append("'" + BdlinkConstant.FROM_ALLOCATION_TYPE_ASK + "','" + BdlinkConstant.FROM_ALLOCATION_TYPE_TURN + "','" + BdlinkConstant.FROM_ALLOCATION_TYPE_POS + "'");
		if (!EmptyUtils.isEmpty(vo.getCorpType())) {
			if (CorpConstant.CORP_TYPE_CODE_NET.equals(Integer.valueOf(vo.getCorpType()))) {
				voucherFrom.append(",'" + BdlinkConstant.FROM_ALLOCATION_TYPE_DOWN + "','" + BdlinkConstant.FROM_ALLOCATION_TYPE_UP + "'");
				voucherFrom.append(",'" + BdlinkConstant.FROM_ALLOCATION_TYPE_AUTO_DOWN + "','" + BdlinkConstant.FROM_ALLOCATION_TYPE_AUTO_UP + "'");
			}
		}
		sql.append(" and t.voucherFrom in (" + voucherFrom + ")");
		StringBuilder commonSql = new StringBuilder();
		String corpAuthSql = SqlUtil.getInSql(corpService.getUserCorpIds(ResourceConstant.NETBANK_REPORT_ALLOCATION_QUERY, user.getId()), "t.corpId", true);
		commonSql.append(" and ").append(corpAuthSql);
		sql.append(commonSql);
		return nisAllocationReportDao.getReportMoblieList(vo, sql.toString());
	}
}
