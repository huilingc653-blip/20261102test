package com.bytter.finance.domain.inl.dao;
import com.bytter.core.base.BaseDao;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.datatable.OrderString;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.finance.constant.InlConstant;
import com.bytter.finance.domain.inl.entity.CmsInlSend;
import com.bytter.finance.domain.inl.vo.CmsInlInfoVO;
import com.bytter.finance.domain.inl.vo.CmsInlSendVO;
import com.bytter.workflow.api.IWorkFlowService;
import org.springframework.stereotype.Repository;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
@Repository
public class InlSendDao extends BaseDao<CmsInlSend, String> {
    /**
     * 查询暂存或者打回数据
     *
     * @param input
     * @param corpIds
     * @return
     */
    public DataTablesOutput listSendUpdateDataTable(DataTablesInput input, List<String> corpIds) {
        String sql = " select send.*, info.bill_no infoBillNo,info.id inlInfoId,info.borrow_in_corp_name as borrowInCorpName, " +
                " info.borrow_out_corp_name as borrowOutCorpName,info.cur_name as curName,rt.repay_name as repayTypeName " +
                "   from CMS_INL_SEND send " +
                "   left join CMS_INL_INFO info " +
                "     on send.inl_info_id = info.id " +
                "   left join cms_repay_type rt on send.repay_type=rt.id "+
                " where 1 =1 " +
                " and (" + getSQLIn(corpIds, 900, "send.borrow_in_corp") + ")" +
                " and  send.status in (" + InlConstant.BACK + "," + InlConstant.TMP + ")";
        return getDynamicBySQL(input, sql, CmsInlSendVO.class, "send", false);
    }
    /**
     * 查询待审批数据
     *
     * @param input
     * @param ids
     * @return
     */
    public DataTablesOutput listInlDataTableApprove(DataTablesInput input, List<String> ids) {
        StringBuilder sql = new StringBuilder();
        sql.append(" select send.*,rt.repay_name as repayTypeName, info.bill_no infoBillNo, ");
        //TODO 补充名称信息
        sql.append(" info.borrow_in_corp_name as borrowInCorpName, info.borrow_out_corp_name as borrowOutCorpName,info.cur_name as curName ");
        sql.append("   from CMS_INL_SEND send ");
        sql.append("   left join CMS_INL_INFO info ");
        sql.append("     on send.inl_info_id = info.id ");
        sql.append("  left join cms_repay_type rt on send.repay_type=rt.id ");
        sql.append(" where 1 =1 ");
        if (!EmptyUtils.isEmpty(ids) && ids.size() > 0) {
            sql.append(" and (" + getSQLIn(ids, 900, "send.id") + ")");
        }
        String flowStatus = input.getParameter().get("flowStatus");
        // 已审批不过滤状态
        if (!IWorkFlowService.PASS.equals(flowStatus)) {
            sql.append(" and  send.status = " + InlConstant.COMMIT_APPROVE);
        }
        return getDynamicBySQL(input, sql.toString(), CmsInlSendVO.class, "send", false);
    }
    /**
     * 查询待审批数据
     *
     * @param input
     * @param corpIds
     * @return
     */
    public DataTablesOutput listSendConfirmDataTable(DataTablesInput input, List<String> corpIds) {
        String sql = " select send.*,rt.repay_name as repayTypeName from cms_inl_send send left join CMS_INL_INFO info on send.inl_info_id = info.id " +
                "   left join cms_repay_type rt on send.repay_type=rt.id "+
                " where send.status in ( " + InlConstant.APPROVED + "," + InlConstant.CONFIRM + ")";
        sql+=" and ("+getSQLIn(corpIds,900,"send.borrow_in_corp")+")";
        OrderString orderString = new OrderString("info.bill_No", "desc");
        input.setOrderString(orderString);
        return getDynamicBySQL(input, sql, CmsInlSendVO.class, "send", false);
    }
    /**
     * 查询待发放数据
     *
     * @param input
     * @return
     */
    public DataTablesOutput listInlInfoApproved(DataTablesInput input, List<String> corpIds) {
        String sql = " select * from ( " +
                " select c.*, " +
                "        bt_nvl_num(send.send_money,0) sendMoney, " +
                "        (c.money - bt_nvl_num(send.send_Money,0) - bt_nvl_num(sending.sending_money, 0)) waitSendMoney, " +
                "        bt_nvl_num(sending.sending_money,0) sendingMoney, " +
                "        case " +
                "          when c.money - bt_nvl_num(send.send_Money,0) = 0 then " +
                "           1 " +
                "          when c.money - bt_nvl_num(send.send_Money,0) = c.money then " +
                "           2 " +
                "          else " +
                "           3 " +
                "        end sendStatus  " +
                "   from CMS_INL_INFO c " +
                "   left join (select inl_info_id info_id, sum(send_money) sending_money " +
                "                from CMS_INL_SEND " +
                "               where status in (-1,0,11,95) " +
                "               group by inl_info_id) sending " +
                "     on c.id = sending.info_id " +
                "   left join (select inl_info_id info_id, sum(send_money) send_Money " +
                "                from CMS_INL_SEND " +
                "               where status = 96 " +
                "               group by inl_info_id) send " +
                "     on c.id = send.info_id " +
                " ) info " +
                "     where 1 = 1 " +
                " and info.status = " + InlConstant.APPROVED +
                " and (" + getSQLIn(corpIds, 900, "info.borrow_in_corp") + ")";
        OrderString orderString = new OrderString("info.bill_No", "desc");
        input.setOrderString(orderString);
        return getDynamicBySQL(input, sql, CmsInlInfoVO.class, "info", false);
    }
    /**
     * 获取申请审批通过数据详情
     *
     * @return
     */
    public List<CmsInlInfoVO> getByInfoId(Map<String, Object> params) {
        String sql = " select info.*, " +
                "        bt_nvl_num(send.send_money,0) sendMoney, " +
                "        (info.money - bt_nvl_num(send.send_Money,0) - bt_nvl_num(sending.sending_money, 0)) waitSendMoney, " +
                "        bt_nvl_num(sending.sending_money,0) sendingMoney " +
                "   from CMS_INL_INFO info " +
                "   left join (select inl_info_id info_id, sum(send_money) sending_money " +
                "                from CMS_INL_SEND " +
                "               where status in (11, 95) " +
                "               group by inl_info_id) sending " +
                "     on info.id = sending.info_id " +
                "   left join (select inl_info_id info_id, sum(send_money) send_Money " +
                "                from CMS_INL_SEND " +
                "               where status = 96 " +
                "               group by inl_info_id) send " +
                "     on info.id = send.info_id " +
                "     where 1 = 1 " +
                "     and info.id = :id";
        return listBySQLAliasToBean(sql, params, CmsInlInfoVO.class);
    }
    /**
     * 获取申请审批通过数据详情
     *
     * @return
     */
    public List<CmsInlInfoVO> getSendMoneyByInfoId(Map<String, Object> params) {
        String sql = " select info.*, " +
                "        bt_nvl_num(send.send_money,0) sendMoney " +
                "   from CMS_INL_INFO info " +
                "   left join (select inl_info_id info_id, sum(send_money) send_Money " +
                "                from CMS_INL_SEND " +
                "               where status in (-1,0,11,95,96) " +
                "               group by inl_info_id) send " +
                "     on info.id = send.info_id " +
                "     where 1 = 1 " +
                "     and info.id = :id";
        return listBySQLAliasToBean(sql, params, CmsInlInfoVO.class);
    }
    /**
     * 获取所有发放数据
     *
     * @param params
     * @return
     */
    public List<CmsInlSendVO> getSendsByInfoId(Map<String, Object> params) {
        String sql = "select send.*,info.borrow_in_corp_name as borrowInCorpName, info.borrow_out_corp_name as borrowOutCorpName,info.cur_name as curName " +
                "   from CMS_INL_SEND send" +
                //TODO 补充名称信息
                " left join CMS_INL_INFO info on send.inl_info_id = info.id " +
                "   where send.inl_info_id = :id  and  send.status != -2 ";
        return listBySQLAliasToBean(sql, params, CmsInlSendVO.class);
    }
    /**
     * 根据id修改还款方式
     *
     * @param id
     */
    public void updateRepayType(String id, String repayType) {
        Map<String, String> params = new HashMap<String, String>();
        params.put("repayType", repayType);
        params.put("id", id);
        createSQLQuery("update CMS_INL_SEND set repay_type =:repayType where id=:id", params).executeUpdate();
    }
    /**
     * 打回和暂存列表
     *
     * @return
     */
    /*
     * public List<CmsInlSendVO> getBySendId(Map<String,Object> params) { StringBuilder sql = new StringBuilder();
     * sql.append(" select send.*, "); // sql.append("        bt_nvl_num(send.send_money,0) sendMoney, "); sql.
     * append("        (info.money - bt_nvl_num(send.send_Money,0) - bt_nvl_num(sending.sending_money, 0)) waitSendMoney, "
     * ); // sql.append("        bt_nvl_num(sending.sending_money,0) sendingMoney ");
     * sql.append("   from CMS_INL_SEND sendinfo "); sql.append("   left join CMS_INL_INFO info");
     * sql.append("   on sendinfo.inl_info_id = info.id");
     * sql.append("   left join (select inl_info_id info_id, sum(send_money) sending_money ");
     * sql.append("                from CMS_INL_SEND "); sql.append("               where status in (11, 95, 0) ");
     * sql.append("               group by inl_info_id) sending "); sql.append("     on info.id = sending.info_id ");
     * sql.append("   left join (select inl_info_id info_id, sum(send_money) send_Money ");
     * sql.append("                from CMS_INL_SEND "); sql.append("               where status = 96 ");
     * sql.append("               group by inl_info_id) send "); sql.append("     on info.id = send.info_id ");
     * sql.append("     where 1 = 1 "); sql.append("     and sendinfo.id = :id"); return
     * listBySQLAliasToBean(sql.toString(), params, CmsInlSendVO.class); }
     */
    /**
     * 删除未还的测算利息
     *
     * @param sendBillCode
     */
    public void deleteInterestByBillNo(String sendBillCode) {
        Map<String, Object> params = new HashMap<String, Object>();
        String sql = "delete from cms_provide_interest  where 1 = 1 " +
                " and bill_code = :billCode " +
                " and status = 0 ";
        params.put("billCode", sendBillCode);
        createSQLQuery(sql, params).executeUpdate();
    }
    /**
     * 删除未还的测算利息
     *
     * @param sendBillNo
     */
    public boolean existsBillNo(String billNo) {
        if (EmptyUtils.isEmpty(billNo)) {
            return false;
        }
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("billNo", billNo);
        Object count = createSQLQuery("select count(1) from CMS_INL_SEND where bill_no = :billNo", params).uniqueResult();
        return count != null && ((Number) count).intValue() > 0;
    }

    public CmsInlSendVO getInterestByBillNo(String sendBillNo) {
        Map<String, Object> params = new HashMap<String, Object>();
        String sql = "select s.*,i.borrow_in_corp_name borrowInCorpName, i.borrow_out_corp_name borrowOutCorpName,i.cur_name curName  from CMS_INL_SEND s " +
                //TODO 补充名称信息
                " left join CMS_INL_INFO i on s.inl_info_id = i.id "+
                " where 1 = 1 "+
                " and s.bill_No = :billNo " +
                " and s.status in (95,96) ";
        params.put("billNo", sendBillNo);
        return getBySQLAliasToBean(sql, params, CmsInlSendVO.class);
    }
    public List<CmsInlSendVO> getListId(List<String> corpIds) {
        String sql = " select id as id from cms_inl_send send " + " where send.status in ( " + InlConstant.APPROVED + "," + InlConstant.CONFIRM + ") and (" + getSQLIn(corpIds, 900, "send.borrow_in_corp") + ")";
        return listBySQLAliasToBean(sql, null, CmsInlSendVO.class);
    }
}
