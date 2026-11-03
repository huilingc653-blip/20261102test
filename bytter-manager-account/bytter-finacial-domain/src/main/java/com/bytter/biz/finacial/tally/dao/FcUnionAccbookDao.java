package com.bytter.biz.finacial.tally.dao;
import com.bytter.biz.finacial.api.dto.FcUnionAccbookDTO;
import com.bytter.biz.finacial.tally.entity.FcUnionAccbook;
import com.bytter.core.base.BaseDao;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.utils.BeanUtils;
import com.bytter.core.utils.StringUtils;
import org.springframework.stereotype.Repository;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
 * @author onlineGenerator
 * @version 1.0
 * @Title: Dao
 * @Description: 网点往来账务信息表
 * @date 2017-07-13 10:02:21
 */
@Repository
public class FcUnionAccbookDao extends BaseDao<FcUnionAccbook, String> {
    public DataTablesOutput<FcUnionAccbookDTO> queryFcUnionAccBook(DataTablesInput dataTablesInput, String type, String user, List<String> corpAuth) {
        Map<String, Object> params = new HashMap<String, Object>();
        StringBuilder sb = new StringBuilder();
        sb.append(" select a.id as id, a.bank as bank,a.bill_state as billstate, a.bus_ori_type  as busoritype,a.external_acc as  externalAcc,a.external_city as externalcity,  ");
        sb.append(" a.external_corp as externalcorp,a.external_prov as externalprov, a.send_money as sendmoney, a.voucher_type as vouchertype, a.receive_acc as receiveacc,a.receive_bus_global_id as receivebusglobalid, ");
        sb.append(" a.receive_net as receivenet, a.receive_order as receiveorder, a.return_msg as returnmsg, a.send_acc as sendacc,  a.send_bus_global_id as sendbusglobalid, ");
        sb.append(" a.send_net as sendnet, a.send_voucher as sendvoucher, a.send_voucher_date as sendVoucherDate, a.start_bank as startbank, a.start_corp as startcorp, a.start_corp_acc as startcorpacc, ");
        sb.append(" a.summary_content as summarycontent, a.way as way ");
        sb.append(" from fc_union_accbook a where 1 = 1  ");
        if ("Init".equals(type)) {
            sb.append(" and ( a.bill_state in ('0' , '8','6','W') or  a.bill_state is null ) ");
        } else if ("Edit".equals(type)) {
            sb.append(" and a.bill_state in( '5' , '4' , '3' ) and a.operator_code = '").append(user).append("'");
        } else if ("Check".equals(type)) {
            sb.append(" and a.bill_state in ('3') and a.operator_code <> '").append(user).append("'");
        }
        String way=String.valueOf(dataTablesInput.getFormdata().get("eq_way"));
        if(StringUtils.isNotBlank(way)){
            sb.append("and a.way="+way);
        }
        sb.append(" and ").append(getSQLIn(corpAuth, 900, "a.send_Net"));
        return getDynamicBySQL(dataTablesInput, sb.toString(), FcUnionAccbookDTO.class, "", false);
    }
    public List<FcUnionAccbookDTO> queryFcUnionAccBook(String id) {
        List<FcUnionAccbook> unionAccbookList = listBy("id", id);
        return BeanUtils.copyListProperties(unionAccbookList, FcUnionAccbookDTO.class);
    }
    /**
     * 根据发送凭证号获取往来信息
     *
     * @param sendVoucherNo
     * @return
     */
    public List<FcUnionAccbookDTO> getUnionAccBookBySendNo(String sendVoucherNo) {
        String hql = " from FcUnionAccbook where sendVoucher = :sendVoucherNo ";
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("sendVoucherNo", sendVoucherNo);
        return listByHQLAliasToBean(hql, params, FcUnionAccbookDTO.class);
    }
    /**
     * 更新往来信息状态
     *
     * @param id
     * @param billState
     * @param oldBillState
     */
    public void updateUnionAccBookBillState(String id, String billState, String oldBillState) {
        String sql = " update fc_union_accbook set bill_state = :billState where id = :id and bill_state = :oldBillState ";
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("billState", billState);
        params.put("oldBillState", oldBillState);
        params.put("id", id);
        createSQLQuery(sql, params).executeUpdate();
    }
    /**
     * 单位校验使用
     *
     * @return
     * @Param corpId
     */
    public Integer verifyCorpDisableByCorpId(String corpId) {
        String sql = " select count(*) from fc_union_accbook a  " +
                " inner join fc_acc c  on c.blnctr_acc = a.receive_acc " +
                " where 1 = 1 and (a.bill_state in ('0' , '8','6','W') or  a.bill_state is null ) " +
                " and c.corp_id = :corpId ";
        Map<String, Object> param = new HashMap<>();
        param.put("corpId", corpId);
        return Integer.valueOf(createSQLQuery(sql, param).uniqueResult().toString());
    }
    /**
     * 查询待记账数据
     */
    //TODO 整体改造 查出结果,通过external_city  获得 sys服务 sys_region相应的记录数，返回，剔除不符合条件的.
    //TODO同理 fc_voucher_type也是一样的
    public List<FcUnionAccbookDTO> queryTallyFcUnionAccBook() {
        Map<String, Object> params = new HashMap<String, Object>();
        String sb = " select a.id as id,a.send_money as sendMoney ,a.send_net as sendNet " +
                " from fc_union_accbook a " +
                " left join fc_voucher_type b on a.voucher_type = b.id " +
                " where 1 = 1 " +
                " and ( a.bill_state in ('0' , '8','6','W') or  a.bill_state is null ) " +
                " and not exists( select 1 from fc_tally_remind_dtl d where d.bill_id = a.id ) ";
        return listBySQLAliasToBean(sb, params, FcUnionAccbookDTO.class);
    }
}
