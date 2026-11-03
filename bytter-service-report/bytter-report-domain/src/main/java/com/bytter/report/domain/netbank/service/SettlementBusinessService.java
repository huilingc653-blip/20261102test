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
import com.bytter.report.domain.netbank.dao.SettlementBusinessDao;
import com.bytter.report.domain.netbank.vo.SettlementBusinessVO;
import com.bytter.system.api.IBtCurrencyService;
import com.bytter.system.api.ICorpService;
import com.bytter.system.api.IDictService;
import com.bytter.system.api.IFcExcrateAdjustService;
import com.bytter.system.api.dto.BtCurrencyDTO;
import com.bytter.system.api.dto.DictDTO;

import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
/**
 * 结算业务统计表Service
 *
 * @author qijh 2022-11-30
 */
@Service("settlementBusinessService")
@Transactional
public class SettlementBusinessService extends BaseService<SettlementBusinessVO, String> {
    @Autowired
    private SettlementBusinessDao settlementBusinessDao;
    @DubboReference(check = false)
	private ICorpService corpService;
    @DubboReference(check = false)
	private IFcExcrateAdjustService fcExcrateAdjustService;
    @DubboReference(check = false)
    private IBtCurrencyService currencyService;
    @DubboReference(check = false)
    private IFcVoucherTypeService fcVoucherTypeService;
    @DubboReference
    IBtCurrencyService btCurrencyService;
    @DubboReference
    IDictService dictService;
    
    @Override
    public BaseDao<SettlementBusinessVO, String> getEntityDao() {
        return settlementBusinessDao;
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
        String resource = ResourceConstant.NETBANK_REPORT_SETTLEMENT_BUSINESS_QUERY;
        String corpAuthSql = SqlUtil.getInSql(corpService.getUserCorpIds(resource, user.getId()), "c.corp_Id", true);
        sql.append(" and ").append(corpAuthSql);
        DataTablesOutput datas = settlementBusinessDao.getDataTablesUnion(dataTablesInput, sql.toString());
        String changeCur=(String)dataTablesInput.getFormdata().get("changeCur");
        String reducedCurId=(String)dataTablesInput.getFormdata().get("reducedCurId");
        String rateType=(String)dataTablesInput.getFormdata().get("rateType");
        Date rateDate= DateUtils.getIntegralEndTime(DateUtils.parse((String)dataTablesInput.getFormdata().get("rateDate"), "yyyy-MM-dd"));
        //获取汇率
        if (datas != null && datas.getData() != null) {
            List<SettlementBusinessVO> list = datas.getData();
            Map<String, BtCurrencyDTO> mapCur = btCurrencyService.getCurrency();
            List<FcVoucherTypeDTO> listVoucher = fcVoucherTypeService.getFcVoucherTypeAll();
            Map<String, String> voucherMap = listVoucher.stream().collect(Collectors.toMap(FcVoucherTypeDTO::getId, FcVoucherTypeDTO::getVoucherName));
            HashMap<String, BigDecimal> curExrateMap = new HashMap<>();
            for(SettlementBusinessVO vo:list){
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
                vo.setCurName(!ObjectUtils.isEmpty(mapCur.get(vo.getCurId())) ? mapCur.get(vo.getCurId()).getCurName() : "");
                vo.setVoucherName(!ObjectUtils.isEmpty(voucherMap.get(vo.getVoucherTypeId())) ? voucherMap.get(vo.getVoucherTypeId()) : "");
            }
        }
        return datas;
    }
}
