package com.bytter.biz.finacial.interest.dao;
import com.bytter.biz.finacial.api.constant.InterestConstant;
import com.bytter.biz.finacial.interest.entity.FcLoanRepay;
import com.bytter.biz.finacial.interest.vo.FcLessPayBillVO;
import com.bytter.biz.finacial.interest.vo.FcLoanRepayVO;
import com.bytter.core.base.BaseDao;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.constant.CommonConstant;
import com.bytter.core.utils.BeanUtils;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.core.utils.StringUtils;
import com.bytter.system.api.IBtCurrencyService;
import com.bytter.system.api.IFcAccService;
import com.bytter.system.api.constant.CorpConstant;
import com.bytter.system.api.dto.BtCurrencyDTO;
import com.bytter.system.api.dto.CorpDTO;
import com.bytter.system.api.dto.FcAccDTO;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Repository;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
/**
 * @author onlineGenerator
 * @version 1.0
 * @Title: Dao
 * @Description: FC_LOAN_REPAY
 * @date 2017-08-31 10:08:43
 */
@Repository
public class FcLoanRepayDao extends BaseDao<FcLoanRepay, String> {
    @DubboReference(check = false)
    IFcAccService iFcAccService;
    @DubboReference(check = false)
    private IBtCurrencyService currencyService;
    /**
     * <b>功能介绍：</b>放款利息还款列表暂存数据<br>
     * <b>日期：</b>2017年9月6日<br>
     * <b>作者：</b>caozx<br>
     *
     * @param dataTablesInput
     * @return
     * @version 1.0
     */
    @SuppressWarnings("rawtypes")
    public DataTablesOutput listFcLoanRepay(DataTablesInput dataTablesInput, List<String> corpAuth, String userId) {
        StringBuilder sql = new StringBuilder();
        sql.append("select r.id,r.net_id as netId,r.bus_type as busType,r.loan_acc as loanAcc,r.need_interest_acc as needInterestAcc,");
        sql.append("r.corp_id as corpId,r.cur_code as curCode,r.repay_acc as repayAcc,r.repay_type as repayType,");
        sql.append("r.interest,r.enter_date as enterDate,r.status ");
        sql.append("from FC_LOAN_REPAY r where r.status = " + CommonConstant.STATUE_TEMP + " or r.status = " + CommonConstant.STATUE_BACK);
        sql.append(" and r.create_by = '" + userId + "' ");
        sql.append(" and ").append(getSQLIn(corpAuth, 900, "r.corp_id"));
        return getDynamicBySQL(dataTablesInput, sql.toString(), FcLoanRepayVO.class, "r", false);
    }
    /**
     * <b>功能介绍：</b>放款利息还款列表待复核数据<br>
     * <b>日期：</b>2017年9月6日<br>
     * <b>作者：</b>caozx<br>
     *
     * @param dataTablesInput
     * @return
     * @version 1.0
     */
    @SuppressWarnings("rawtypes")
    public DataTablesOutput listCheckFcLoanRepay(DataTablesInput dataTablesInput, String loginName, List<String> corpAuth) {
        StringBuilder sql = new StringBuilder();
        sql.append("select r.id,r.net_id as netId,r.bus_type as busType,r.loan_acc as loanAcc,r.need_interest_acc as needInterestAcc,");
        sql.append("r.corp_id as corpId,r.cur_code as curCode,r.repay_acc as repayAcc,r.repay_type as repayType,");
        sql.append("r.interest,r.enter_date as enterDate,r.status ");
        sql.append("from FC_LOAN_REPAY r where r.operator_code != '" + loginName + "' and r.status = " + CommonConstant.STATUE_WAIT_CHECK);
        sql.append(" and ").append(getSQLIn(corpAuth, 900, "r.corp_id"));
        return getDynamicBySQL(dataTablesInput, sql.toString(), FcLoanRepayVO.class, "r", false);
    }
    /**
     * <b>功能介绍：</b>根据ID获取放款利息还款数据<br>
     * <b>日期：</b>2017年9月7日<br>
     * <b>作者：</b>caozx<br>
     *
     * @param id
     * @return
     * @version 1.0
     */
    public FcLoanRepayVO getFcLoanRepayById(String id) {
        StringBuilder sql = new StringBuilder();
		/*sql.append("select r.id,r.net_id as netId,r.bus_type as busType,r.loan_acc as loanAcc,a.acc_name as accName,r.need_interest_acc as needInterestAcc,");
		sql.append("r.corp_id as corpId,r.cur_code as curCode,r.repay_acc as repayAcc,r.repay_type as repayType,");
		sql.append("r.interest,r.enter_date as enterDate,r.status ");
		sql.append("from FC_LOAN_REPAY r,fc_acc a where r.loan_acc = a.blnctr_acc and r.id = '"+id+"'");*/
        sql.append("select r.id,r.net_id as netId,r.bus_type as busType,r.loan_acc as loanAcc,r.need_interest_acc as needInterestAcc,");
        sql.append("r.corp_id as corpId,r.cur_code as curCode,r.repay_acc as repayAcc,r.repay_type as repayType,");
        sql.append("r.interest,r.enter_date as enterDate,r.status ");
        sql.append("from FC_LOAN_REPAY r where r.id = '" + id + "'");
        List<FcLoanRepayVO> list = listBySQLAliasToBean(sql.toString(), null, FcLoanRepayVO.class);
        if (!EmptyUtils.isEmptyList(list)) {
            return list.get(0);
        }
        return null;
    }
    /**
     * <b>功能介绍：</b>根据单位查询放款账号<br>
     * <b>日期：</b>2017年8月31日<br>
     * <b>作者：</b>caozx<br>
     *
     * @param corpId
     * @return 由 基础模块 提供
     * @version 1.0
     */
    @SuppressWarnings("unchecked")
    public List<Map<String, Object>> getLoanAccByCorpCode(String corpId) {
		/*StringBuilder sql = new StringBuilder();
		Map<String,Object> params = new HashMap<String,Object>();
		if(StringUtils.isEmpty(corpId)){
			return null;
		}
		sql.append("select acc.blnctr_acc as acc from fc_acc acc where acc.acc_type=:accType ");
		sql.append("and acc.corp_id = :corpId ");
		sql.append("and acc.acc_state= :accState ");
		params.put("accType", InnerAccountConstant.FCACC_LOAN);
		params.put("corpId", corpId);
		params.put("accState", InnerAccountConstant.ACC_STATE_NORMAL);
		return createSQLQuery(sql.toString(), params).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();*/
        return null;
    }
    /**
     * <b>功能介绍：</b>根据贷款账号查询单位活期存款账户<br>
     * <b>日期：</b>2017年9月4日<br>
     * <b>作者：</b>caozx<br>
     *
     * @param loanAcc
     * @return 改为由 基础模块提供
     * @version 1.0
     */
    @SuppressWarnings("unchecked")
    public List<Map<String, Object>> getCorpCurAccInfo(String loanAcc) {
		/*if(StringUtils.isEmpty(loanAcc)){
			return null;
		}
		StringBuilder sql = new StringBuilder();
		Map<String,Object> params = new HashMap<String,Object>();
		sql.append("select a.blnctr_acc as account from fc_acc a, fc_acc b where a.corp_id = b.corp_id ");
		sql.append("and a.cur_id = b.cur_id ");
		sql.append("and a.acc_State in ('"+InnerAccountConstant.ACC_STATE_NORMAL+"', '"+InnerAccountConstant.ACC_STATE_PART_FREEZE+"') ");
		sql.append("and a.acc_type = :accType ");
		sql.append("and b.blnctr_acc = :loanAcc ");
		params.put("accType", InnerAccountConstant.FCACC_BUSINESS_ACCOUNT);
		params.put("loanAcc", loanAcc);
		return createSQLQuery(sql.toString(), params).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();*/
        return null;
    }
    /**
     * <b>功能介绍：</b>获取所有网点<br>
     * <b>日期：</b>2017年8月31日<br>
     * <b>作者：</b>caozx<br>
     *
     * @return
     * @version 1.0
     */
    public List<CorpDTO> listNet() {
        StringBuilder sql = new StringBuilder("select c.* from SYS_CORP c where 1 = 1 and c.TYPE = " + CorpConstant.CORP_TYPE_CODE_NET + " and c.STATUS = " + CorpConstant.CORP_STATUS_ENABLE + " order by c.CODE");
        return listBySQLAliasToBean(sql.toString(), null, CorpDTO.class);
    }
    /**
     * <b>功能介绍：</b>查询待归还账户明细<br>
     * <b>日期：</b>2017年9月1日<br>
     * <b>作者：</b>caozx<br>
     *
     * @param acc
     * @return
     * @version 1.0
     */
    public List<FcLessPayBillVO> queryRepayDetailByAcc(String acc) {
        if (StringUtils.isEmpty(acc)) {
            return null;
        }
        StringBuilder sql = new StringBuilder();
        Map<String, Object> params = new HashMap<>();
        sql.append("select b.id,b.account,b.cur_code as curCode,b.interest,b.enter_date as enterDate,b.needinterest_acc needinterestAcc ");
        sql.append("from fc_less_pay_bill b ");
        sql.append("where (b.is_repay is null or b.is_repay = 0) ");
        sql.append("and b.status = :status ");
        sql.append("and b.account= :account ");
        params.put("status", InterestConstant.INTEREST_STATUE_CHECK);
        params.put("account", acc);
        List<FcLessPayBillVO> lessPayBillVOS = listBySQLAliasToBean(sql.toString(), params, FcLessPayBillVO.class);
        List<String> accounts = lessPayBillVOS.stream().map(FcLessPayBillVO::getAccount).collect(Collectors.toList());
        List<String> needinterestAcc = lessPayBillVOS.stream().map(FcLessPayBillVO::getNeedinterestAcc).collect(Collectors.toList());
        List<FcAccDTO> fcAccDTOList = iFcAccService.getFcAccByBlnctrAccs(accounts);
        Map<String, FcAccDTO> fcBalanceMap = fcAccDTOList.stream().collect(Collectors.toMap(FcAccDTO::getBlnctrAcc, dto -> dto));
        List<FcAccDTO> needinterestAccDtoS = iFcAccService.getFcAccByBlnctrAccs(needinterestAcc);
        Map<String, FcAccDTO> needinterestAccDtoMap = needinterestAccDtoS.stream().collect(Collectors.toMap(FcAccDTO::getBlnctrAcc, dto -> dto));
        List<FcLessPayBillVO> vos = new ArrayList<>();
        for (FcLessPayBillVO billVO : lessPayBillVOS) {
            FcLessPayBillVO payBillVO = BeanUtils.copyNewInstanceProperties(billVO, FcLessPayBillVO.class);
            payBillVO.setAccName(fcBalanceMap.get(billVO.getAccount()).getAccName());
            BtCurrencyDTO currencyDTO = currencyService.getObjectById(billVO.getCurCode());
            payBillVO.setCurName(currencyDTO.getCurName());
            if (billVO.getNeedinterestAcc() != null) {
                payBillVO.setNeedinterestAccName(needinterestAccDtoMap.get(payBillVO.getNeedinterestAcc()).getAccName());
            } else {
                payBillVO.setNeedinterestAccName("");
            }
            vos.add(payBillVO);
        }
        return vos;
    }
    /**
     * <b>功能介绍：</b>更新不足款清单为已(未)还<br>
     * <b>日期：</b>2017年9月5日<br>
     * <b>作者：</b>caozx<br>
     *
     * @param id
     * @param isRepay
     * @version 1.0
     */
    public void updateFcLessPallBillRepay(String id, String isRepay) {
        Map<String, Object> params = new HashMap<>();
        StringBuilder sql = new StringBuilder("update fc_less_pay_bill set is_repay = :isRepay where id = :repayID ");
        params.put("isRepay", isRepay);
        params.put("repayID", id);
        createSQLQuery(sql.toString(), params).executeUpdate();
    }
    /**
     * <b>功能介绍：</b>根据还款信息获取不足款清单<br>
     * <b>日期：</b>2017年9月6日<br>
     * <b>作者：</b>caozx<br>
     *
     * @param id
     * @return
     * @version 1.0
     */
    public FcLessPayBillVO getFcLessPayBillByFcLoanRepay(String id) {
        StringBuilder sql = new StringBuilder();
        Map<String, Object> params = new HashMap<>();
        sql.append("select b.id,b.account,b.cur_code as curCode,b.interest,b.enter_date as enterDate,");
        sql.append("b.needinterest_acc as needinterestAcc ");
        sql.append("from fc_less_pay_bill b ");
        sql.append("where b.is_repay = 1  ");
        sql.append("and b.id= :id ");
        params.put("id", id);
        List<FcLessPayBillVO> list = listBySQLAliasToBean(sql.toString(), params, FcLessPayBillVO.class);
        if (EmptyUtils.isEmptyList(list)) {
            return null;
        }
        FcLessPayBillVO vo = list.get(0);
        List<String> accounts = list.stream().map(FcLessPayBillVO::getAccount).collect(Collectors.toList());
        List<String> needinterestAcc = list.stream().map(FcLessPayBillVO::getNeedinterestAcc).collect(Collectors.toList());
        List<FcAccDTO> fcAccDTOList = iFcAccService.getFcAccByBlnctrAccs(accounts);
        Map<String, FcAccDTO> fcBalanceMap = fcAccDTOList.stream().collect(Collectors.toMap(FcAccDTO::getBlnctrAcc, dto -> dto));
        List<FcAccDTO> needinterestAccDtoS = iFcAccService.getFcAccByBlnctrAccs(needinterestAcc);
        Map<String, FcAccDTO> needinterestAccDtoMap = needinterestAccDtoS.stream().collect(Collectors.toMap(FcAccDTO::getBlnctrAcc, dto -> dto));
        FcLessPayBillVO payBillVO = BeanUtils.copyNewInstanceProperties(vo, FcLessPayBillVO.class);
        payBillVO.setAccName(fcBalanceMap.get(vo.getAccount()).getAccName());
        if (vo.getNeedinterestAcc() != null) {
            payBillVO.setNeedinterestAccName(needinterestAccDtoMap.get(payBillVO.getNeedinterestAcc()).getAccName());
        } else {
            payBillVO.setNeedinterestAccName("");
        }
        return payBillVO;
    }
}
