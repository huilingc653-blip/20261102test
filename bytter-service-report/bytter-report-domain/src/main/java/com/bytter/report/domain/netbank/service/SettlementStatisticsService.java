package com.bytter.report.domain.netbank.service;
import com.bytter.biz.payment.api.dto.FcVoucherTypeDTO;
import com.bytter.biz.payment.api.service.IFcVoucherTypeService;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.constant.CommonConstant;
import com.bytter.core.constant.ResourceConstant;
import com.bytter.core.utils.DateUtils;
import com.bytter.core.utils.SqlUtil;
import com.bytter.report.domain.netbank.dao.SettlementStatisticsDao;
import com.bytter.report.domain.netbank.vo.SettlementStatisticsVO;
import com.bytter.system.api.IBtCurrencyService;
import com.bytter.system.api.ICorpService;
import com.bytter.system.api.IFcExcrateAdjustService;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 结算业务量统计表Service
 *
 * @author huangx 2022-06-21
 */
@Service("settlementStatisticsService")
@Transactional
public class SettlementStatisticsService extends BaseService<SettlementStatisticsVO, String> {
    @Autowired
    private SettlementStatisticsDao settlementStatisticsDao;
    @DubboReference(check = false)
	private ICorpService corpService;
    @DubboReference(check = false)
	private IFcExcrateAdjustService fcExcrateAdjustService;
    @DubboReference(check = false)
    private IFcVoucherTypeService voucherTypeService;
    @DubboReference(check = false)
    private IBtCurrencyService currencyService;
    @Override
    public BaseDao<SettlementStatisticsVO, String> getEntityDao() {
        return settlementStatisticsDao;
    }
    /**
     * 查询付款单
     *
     * @param dataTablesInput
     * @return
     */
    @SuppressWarnings("rawtypes")
    public DataTablesOutput getDataTablesUnion(DataTablesInput dataTablesInput, ICurrentUser user) {
        StringBuilder sql = new StringBuilder();
        String resource = ResourceConstant.NETBANK_REPORT_SETTLEMENT_STATISTICE_QUERY;
        String corpAuthSql = SqlUtil.getInSql(corpService.getUserCorpIds(resource, user.getId()), "n.corp_Id", true);
        sql.append(" and ").append(corpAuthSql);
        DataTablesOutput datas = settlementStatisticsDao.getDataTablesUnion(dataTablesInput, sql.toString());
        String changeCur=(String)dataTablesInput.getFormdata().get("changeCur");
        String reducedCurId=(String)dataTablesInput.getFormdata().get("reducedCurId");
        String rateType=(String)dataTablesInput.getFormdata().get("rateType");
        Date rateDate= DateUtils.getIntegralEndTime(DateUtils.parse((String)dataTablesInput.getFormdata().get("rateDate"), "yyyy-MM-dd"));
        //获取汇率
        if (datas != null && datas.getData() != null) {
            List<SettlementStatisticsVO> list = datas.getData();
            Map<String, FcVoucherTypeDTO> fcVoucherTypeMap = voucherTypeService.getFcVoucherTypeMap();
            Map<String, String> currencyNameMap= currencyService.getCurrencyNameByIds(list.stream().map(SettlementStatisticsVO::getCurId).collect(Collectors.toList()));
            HashMap<String, BigDecimal> curExrateMap = new HashMap<>();
            for(SettlementStatisticsVO vo:list){
                vo.setVoucherName(fcVoucherTypeMap.get(vo.getVoucherTypeId()).getVoucherName());
                vo.setCurName(currencyNameMap.get(vo.getCurId()));
                BigDecimal curExrate = BigDecimal.ONE;
                if(CommonConstant.YES.equals(changeCur)) {
                    if (curExrateMap.containsKey(vo.getCurId())) {
                        curExrate = curExrateMap.get(vo.getCurId());
                    } else {
                        curExrate = fcExcrateAdjustService.getExchangeRateBy(vo.getCurId(), reducedCurId, DateUtils.setSpecifiedDateOfTime(rateDate), rateType);
                        curExrateMap.put(vo.getCurId(), curExrate);
                    }
                }
                vo.setCurExrate(curExrate);
            }
        }
        return datas;
    }
}
