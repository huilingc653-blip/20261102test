package com.bytter.check.reconcile.dao;

import com.bytter.biz.bdlink.api.constant.BdlinkConstant;
import com.bytter.biz.bdlink.api.constant.ReceiptConstant;
import com.bytter.check.reconcile.vo.BisCheckReasonVO;
import com.bytter.check.reconcile.vo.BisTrackingVO;
import com.bytter.core.base.BaseDao;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.constant.CommonConstant;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.core.utils.ListUtils;
import com.bytter.core.utils.SqlUtil;
import com.bytter.core.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class BisTrackingDao extends BaseDao<BisTrackingVO, String> {
    @Autowired
    private BisCheckReasonDao bisCheckReasonDao;

    private static StringBuilder initSql() {
        return new StringBuilder().append(" select   ").
                append(" bcd.id as id,bcd.CORP_ID as corpId,").
                append(" bcd.BILL_NO as billNo,bcd.BANK_ACC as bankAcc,bcd.ACC_NAME as accName,bcd.ACC_NATURE as accNature,").
                append(" bcd.CUR_ID as curId,").
                append(" bcd.CHECK_DATE as checkDate,bcd.BAL as bal,bcd.TOBILL_DATE as toBillDate,").
                append(" bcd.RETURN_TIME as returnTime,bcd.REASON_ID as reasonId,").
                append(" bcd.CHECK_RESULT as checkResult,bcd.CHECK_STATUS as checkStatus,bcd.CHECK_STAT as checkStat,").
                append(" case when bcd.expand_Fields is null or LENGTH(TRIM(bcd.expand_Fields)) = 0 then bcd.bank_acc else bcd.expand_Fields end as expandFields, ").
                append(" bisExc.VOUCHER_STAT as voucherStat,bisExc.id as bisExcId,bcd.mobile_no mobileNo ,  bcd.rmk rmk ").
                append(" from bis_check_dtl bcd").
                append(" inner join BIS_CHECK_EXC bisExc on bisExc.DTL_ID = bcd.id").
                append(" where 1=1");

    }

    @SuppressWarnings("rawtypes")
    public DataTablesOutput getDataTablesUnion(DataTablesInput input, List<String> corpAuthList, List<String> accList,String insBankTypeId) {
        Map<String, Object> params = new HashMap<String, Object>();
        StringBuilder sql = new StringBuilder(initSql());
        sql.append(" and bisExc.bus_type in (:busType) ");
        params.put("busType", new String[]{ReceiptConstant.BIS_BANK_BUS_TYPE_2});
        sql.append(" and bisExc.voucher_stat in (:voucherStat)");
        params.put("voucherStat", new String[]{
                BdlinkConstant.WAIT_RESULT,
                BdlinkConstant.PRE_SEND,
                BdlinkConstant.SUCCESS,
                BdlinkConstant.INTERFACE_FAILED,
                BdlinkConstant.BANK_FAILED,
        });
        // 对账账户
        String checkAccount = (String) input.getFormdata().get("checkAccount");
        if (StringUtils.isNotBlank(checkAccount)) {
            sql.append(" and case when bcd.expand_Fields is null or LENGTH(TRIM(bcd.expand_Fields)) = 0 then bcd.bank_acc like :checkAccount else bcd.expand_Fields like :checkAccount end ");
            params.put("checkAccount", "%" + checkAccount + "%");
        }
        // 是否保证金
        String isBail = (String) input.getFormdata().get("isBail");
        if (StringUtils.isNotBlank(isBail)) {
            if (CommonConstant.YES.equals(isBail)) {
                sql.append(" and UPPER(bcd.expand_Fields) like :isBail ");
                params.put("isBail", "%|Y%");
            }
            if (CommonConstant.NO.equals(isBail)) {
                sql.append(" and (bcd.expand_Fields is null or LENGTH(TRIM(bcd.expand_Fields)) = 0 or UPPER(bcd.expand_Fields) not like :isBail) ");
                params.put("isBail", "%|Y%");
            }
        }
        String corpAuthSql = SqlUtil.getInSqlByParams(corpAuthList, "bcd.corp_id","corpId", params);
        sql.append(" and ").append(corpAuthSql).append(" ");
        if (accList.size() > 0) {
            String accSql = SqlUtil.getInSqlByParams(accList, "bcd.BANK_ACC","bankAcc", params);
            sql.append(" and ").append(accSql).append(" ");
        }else {
            if(EmptyUtils.isNotEmpty(insBankTypeId)){
                sql.append(" and bcd.bank_acc = '#' ");
            }
        }
        return getDynamicBySQL(input, sql.toString(), BisTrackingVO.class, "", false, params);
    }

    public BisTrackingVO getCheckVo(Map<String, Object> params) {
        StringBuilder sql = new StringBuilder(initSql());
        if (params.containsKey("ids")) {
            sql.append(" and bcd.id in (:ids)");
        }
        List<BisTrackingVO> listInfo = this.listBySQLAliasToBean(sql.toString(), params, BisTrackingVO.class);
        if (ListUtils.isNotNullOrEmpty(listInfo)) {
            BisTrackingVO beanVo = listInfo.get(0);
            if (!StringUtils.isBlank(listInfo.get(0).getReasonId())) {
                Map<String, Object> queryParams = new HashMap<String, Object>();
                queryParams.put("validSign", ReceiptConstant.BIS_BANK_VALID_SIGN_1);
                queryParams.put("rides", Arrays.asList(beanVo.getReasonId().split(",")));
                List<BisCheckReasonVO> reasonList = bisCheckReasonDao.getListByValidSign(queryParams);
                if (ListUtils.isNotNullOrEmpty(reasonList)) {
                    String reasonName = "";
                    for (int i = 0; i < reasonList.size(); i++) {
                        if (i % 2 != 0) {
                            reasonName += ";";
                        }
                        reasonName += reasonList.get(i).getReasonName();
                    }
                    beanVo.setReasonName(reasonName);
                }
            }
            return beanVo;
        }
        return null;
    }


    public List<BisTrackingVO> queryList(Map<String, Object> params) {
        StringBuilder sql = new StringBuilder(initSql());
        if (params.containsKey("ids")) {
            sql.append(" and bcd.id in (:ids)");
        }
        List<BisTrackingVO> listInfo = this.listBySQLAliasToBean(sql.toString(), params, BisTrackingVO.class);
        return listInfo;
    }

}