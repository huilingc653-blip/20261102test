package com.bytter.biz.foreign.service;

import com.bytter.biz.foreign.dao.FexCorpPoolBankaccQuotaDao;
import com.bytter.biz.foreign.vo.FexCorpPoolBankAccVO;
import com.bytter.biz.foreign.vo.FexCorpPoolBankaccQuotaVO;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.utils.DateUtils;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.core.utils.SqlUtil;
import com.bytter.core.utils.StringUtils;
import com.bytter.core.constant.ResourceConstant;
import com.bytter.system.api.dto.FcCorpPoolDTO;
import com.bytter.system.api.IFcCorpPoolService;
import com.bytter.system.api.IBtCurrencyService;
import com.bytter.system.api.IFcExcrateAdjustService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author wangzx
 * @version 1.0
 * @Title: Service
 * @Description: 资金池账户额度查询
 * @date 2020-03-11 10:31:56
 */
@Service("fexCorpPoolBankaccQuotaService")
@Transactional
public class FexCorpPoolBankaccQuotaService extends BaseService<FexCorpPoolBankaccQuotaVO, String> {

    @Autowired
    private FexCorpPoolBankaccQuotaDao fexCorpPoolBankaccQuotaDao;
    @Autowired
    private FexCorpPoolService fexCorpPoolService;
    @Autowired(required = false)
    private IBtCurrencyService btCurrencyService;
    @Autowired(required = false)
    private IFcExcrateAdjustService iFcExcrateAdjustService;
    @Autowired(required=false)
    private IFcCorpPoolService fcCorpPoolService;


    @Override
    public BaseDao<FexCorpPoolBankaccQuotaVO, String> getEntityDao() {
        return fexCorpPoolBankaccQuotaDao;
    }

    /**
     * 资金池账户额度查询
     *
     * @param dataTablesInput
     * @return
     */
    @SuppressWarnings("rawtypes")
    public DataTablesOutput getDataTablesUnion(DataTablesInput dataTablesInput, ICurrentUser user) {
        StringBuilder sql = new StringBuilder();
        List<String> corpList = new ArrayList<>();
        String resource = ResourceConstant.FOREIGN_FOREIGN_FEXCORPPOOL_QUERY;
        //资金池不为空
        String poolId = (String) dataTablesInput.getFormdata().get("eqs_poolId");
        if (!StringUtils.isBlank(poolId)) {
            List<FcCorpPoolDTO> poolChilds = fcCorpPoolService.getAllUserCorpPoolChilds(user, resource, poolId);
            if (!EmptyUtils.isEmptyList(poolChilds)) {
                for (FcCorpPoolDTO dto : poolChilds) {
                    corpList.add(dto.getCorpId());
                }
            }
        } else {
            List<FexCorpPoolBankAccVO> pools = fexCorpPoolService.getPoolAccUniq(user, resource);
            for (FexCorpPoolBankAccVO pool : pools) {
                List<FcCorpPoolDTO> poolChilds = fcCorpPoolService.getAllUserCorpPoolChilds(user, resource, pool.getPoolId());
                if (!EmptyUtils.isEmptyList(poolChilds)) {
                    for (FcCorpPoolDTO dto : poolChilds) {
                        corpList.add(dto.getCorpId());
                    }
                }
            }
        }
        String corpSql = SqlUtil.getInSql(corpList, "f.corp_id", true);
        sql.append(" and ").append(corpSql);
        DataTablesOutput data = fexCorpPoolBankaccQuotaDao.getDataTablesUnion(dataTablesInput, sql.toString());
        List<FexCorpPoolBankaccQuotaVO> list =  data.getData();//当前结果
        String balDate = (String) dataTablesInput.getFormdata().get("balDate");
        Date monthDate = DateUtils.parseDate(balDate);
        if (monthDate == null) {
            monthDate = DateUtils.parseDate(DateUtils.format(new Date(), "yyyy-MM-dd"));
        }
        List<FexCorpPoolBankaccQuotaVO> listData = this.toExchangeRate(list, monthDate);
        data.setData(listData);
        return data;
    }

    /**
     * 折算汇率
     */
    public List<FexCorpPoolBankaccQuotaVO> toExchangeRate(List<FexCorpPoolBankaccQuotaVO> list, Date monthDate) {
        if (EmptyUtils.isEmptyList(list) || monthDate == null) {
            return list;
        }
        for (FexCorpPoolBankaccQuotaVO vo : list) {
            if (StringUtils.isNotBlank(vo.getCurId()) && StringUtils.isNotBlank(vo.getAccCurId())) {
                //折算汇率
                BigDecimal curExrate;
                BigDecimal exchangeRateBy = iFcExcrateAdjustService.getExchangeRateByNoCheck(vo.getAccCurId(), vo.getCurId(), monthDate, vo.getRateTypeId());
                if (exchangeRateBy != null) {
                    curExrate = exchangeRateBy;
                    //外债余额
                    if(!EmptyUtils.isEmpty(vo.getExternalDebtBal())){
                        vo.setExternalDebtBal(vo.getExternalDebtBal().multiply(curExrate));
                    }
                    //外债限额
                /*if(!EmptyUtils.isEmpty(vo.getExternalDebtLimit())){
                    vo.setExternalDebtLimit(vo.getExternalDebtLimit().multiply(curExrate));
                }*/
                    //剩余外债额度
                    BigDecimal externalDebtLimit = EmptyUtils.isEmpty(vo.getExternalDebtLimit()) ? BigDecimal.ZERO : vo.getExternalDebtLimit();
                    BigDecimal externalDebtBal = EmptyUtils.isEmpty(vo.getExternalDebtBal()) ? BigDecimal.ZERO : vo.getExternalDebtBal();
                    vo.setRemainingDebtQuota(externalDebtLimit.subtract(externalDebtBal));
                    //对外放款余额
                    if(!EmptyUtils.isEmpty(vo.getExternalLoanBal())){
                        vo.setExternalLoanBal(vo.getExternalLoanBal().multiply(curExrate));
                    }
                    //对外放款限额
                /*if(!EmptyUtils.isEmpty(vo.getExternalLoanLimit())){
                    vo.setExternalLoanLimit(vo.getExternalLoanLimit().multiply(curExrate));
                }*/
                    //剩余对外放款额度
                    BigDecimal externalLoanLimit = EmptyUtils.isEmpty(vo.getExternalLoanLimit()) ? BigDecimal.ZERO : vo.getExternalLoanLimit();
                    BigDecimal externalLoanBal = EmptyUtils.isEmpty(vo.getExternalLoanBal()) ? BigDecimal.ZERO : vo.getExternalLoanBal();
                    vo.setRemainingLoanQuota(externalLoanLimit.subtract(externalLoanBal));
                    //流入余额
                    if (!EmptyUtils.isEmpty(vo.getInflowBal())) {
                        vo.setInflowBal(vo.getInflowBal().multiply(curExrate));
                    }
                    //流出余额
                    if (!EmptyUtils.isEmpty(vo.getOutflowBal())) {
                        vo.setOutflowBal(vo.getOutflowBal().multiply(curExrate));
                    }
                    //净流入额
                    if (!EmptyUtils.isEmpty(vo.getNetInflowBal())) {
                        vo.setNetInflowBal(vo.getNetInflowBal().multiply(curExrate));
                    }
                    //净流入限额
                /*if(!EmptyUtils.isEmpty(vo.getNetInflowLimit())){
                    vo.setNetInflowLimit(vo.getNetInflowLimit().multiply(curExrate));
                }*/
                    //剩余净流入额度
                    BigDecimal netInflowLimit = EmptyUtils.isEmpty(vo.getNetInflowLimit()) ? BigDecimal.ZERO : vo.getNetInflowLimit();
                    BigDecimal netInflowBal = EmptyUtils.isEmpty(vo.getNetInflowBal()) ? BigDecimal.ZERO : vo.getNetInflowBal();
                    vo.setRemainingNetInflowQuota(netInflowLimit.subtract(netInflowBal));
                }else{
                    vo.setExternalDebtBal(null);
                    vo.setRemainingDebtQuota(null);
                    vo.setExternalLoanBal(null);
                    vo.setRemainingLoanQuota(null);
                    vo.setInflowBal(null);
                    vo.setOutflowBal(null);
                    vo.setNetInflowBal(null);
                    vo.setRemainingNetInflowQuota(null);
                    vo.setId("flag");
                }
            }
        }
        return list;
    }
}
