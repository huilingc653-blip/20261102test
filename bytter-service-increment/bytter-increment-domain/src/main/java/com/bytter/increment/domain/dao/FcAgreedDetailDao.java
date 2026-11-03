package com.bytter.increment.domain.dao;
import com.bytter.core.base.BaseDao;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.increment.api.constant.AgreedConstant;
import com.bytter.increment.api.dto.FcAgreeDepositDetailDTO;
import com.bytter.increment.domain.entity.FcAgreedDetail;
import org.springframework.stereotype.Repository;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
 * @author wangzx
 * @version 1.0
 * @Title: Dao
 * @Description: 协定存款登记明细表
 * @date 2019-05-10 16:44:38
 */
@Repository
public class FcAgreedDetailDao extends BaseDao<FcAgreedDetail, String> {
    /**
     * 根据协定主表id和行号查询明细
     * @param agreedId
     * @return
     */
    public List<FcAgreedDetail> getDetailByAgreedIdLineNo(String agreedId, Integer lineNo){
        StringBuilder hql = new StringBuilder();
        Map<String,Object> params = new HashMap<>();
        hql.append(" from FcAgreedDetail where agreedDepositId =:agreedId ");
        params.put("agreedId",agreedId);
        if(!EmptyUtils.isEmpty(lineNo)){
            hql.append(" and lineNo =:lineNo ");
            params.put("lineNo",lineNo);
        }
        hql.append(" order by minMoney ");
        return listByHQL(hql.toString(),params);
    }
    /**
     * <br>
     * <b>功能：</b> 获取账户协定明细 <br>
     * <b>作者：</b>liyh<br>
     * @param account 账号
     * @param balanceDate 余额时间
     * @param isAdjust 是否调整 true 是 false 否
     * <b>日期：</b> 2019/5/14 <br>
     */
    public List<FcAgreeDepositDetailDTO> getFcAgreeDepositDetail(String account, Date balanceDate, boolean isAdjust) {
        StringBuffer sql = new StringBuffer();
        sql.append(" select fd.max_money as maxMoney, fd.min_money as minMoney, fd.line_no as lineNo,")
            .append(" fd.agreed_deposit_id as agreeDepositId ")
            .append(" from Fc_Agreed_Deposit fa, Fc_Agreed_Detail fd where fa.id = fd.agreed_deposit_id")
            .append(" And fa.begin_Date <= :balanceDate ")
            .append(" And ( (fa.stop_date Is Null Or fa.stop_date > :balanceDate) And fa.End_Date >= :balanceDate) ")
            .append(" And fa.Blnctr_Acc = :account ");
        if(!isAdjust){
            /*新增去除过滤协定终止的数据*/
            sql.append(" and fa.status = " + AgreedConstant.STATUE_CHECK )
                    .append(" and fa.id not in (select id from fc_agreed_deposit where status = " + AgreedConstant.STATUE_CHECK)
                    .append(" and bus_type = '" + AgreedConstant.BUS_TYPE_2 + "'")
                    .append(" union ")
                    .append(" select id from fc_agreed_deposit ff where id in (select agreed_deposit_ori_id from fc_agreed_deposit")
                    .append(" where status = " + AgreedConstant.STATUE_CHECK + " and bus_type = '" + AgreedConstant.BUS_TYPE_2 + "'))");
        }
        //只取最新登记状态
        sql.append(" and fa.bill_status = " + AgreedConstant.BILL_STATUS_10 );
        sql.append(" order by fd.agreed_deposit_id, fd.line_no");
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("account", account);
        params.put("balanceDate", balanceDate);
        return listBySQLAliasToBean(sql.toString(), params, FcAgreeDepositDetailDTO.class);
    }
}
