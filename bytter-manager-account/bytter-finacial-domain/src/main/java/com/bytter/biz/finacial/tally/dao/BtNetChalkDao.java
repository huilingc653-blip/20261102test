package com.bytter.biz.finacial.tally.dao;

import com.alibaba.excel.util.CollectionUtils;
import com.bytter.biz.finacial.api.constant.FinacialConstant;
import com.bytter.biz.finacial.api.constant.NbconsoleConstant;
import com.bytter.biz.finacial.api.dto.BtNetChalkDTO;
import com.bytter.biz.finacial.api.dto.FcAccbookDtlDTO;
import com.bytter.biz.finacial.tally.entity.BtNetChalk;
import com.bytter.core.base.BaseDao;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.datatable.OrderString;
import com.bytter.core.utils.SqlUtil;
import com.bytter.core.utils.StringUtils;
import com.bytter.system.api.IBtBankAccService;
import com.google.common.collect.Lists;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author
 * @version 1.0
 * @Title: Dao
 * @Description: BT_NET_CHALK
 * @date 2017-07-04 14:58:22
 */
@Repository
public class BtNetChalkDao extends BaseDao<BtNetChalk, String> {
    @DubboReference(check = false)
    private IBtBankAccService bankAccService;

    /**
     * 查询页面数据
     */
    public DataTablesOutput<BtNetChalkDTO> queryBtNetChalk(DataTablesInput dataTablesInput, String type, String userId, List<String> corpAuth, List<String> notCorpIds) {
        StringBuilder sb = new StringBuilder();
        sb.append(" select vbn.* from bt_net_chalk vbn  ");
        //对方账户为总账户的子账户(即银行账户父账号标志标识为0)
        sb.append(" left JOIN bt_bank_acc acc ON acc.BANK_ACC = vbn.OPP_ACC_NO ");
        sb.append(" left JOIN bt_bank_Acc_cur cur ON cur.BANK_ACC_ID = acc.id ");
        sb.append(" left join bt_bank_acc b on vbn.bank_acc_id = b.id ");
        sb.append(" left join sys_corp s on s.id = vbn.corp_id ");
        sb.append(" where 1 = 1  ");
        sb.append(" and  vbn.cd_sign = 0  and b.parent_sign = '1' and s.type = 0 ");
        if ("Init".equals(type)) {
            sb.append(" and (vbn.estate = '0' or vbn.estate = '6' or  vbn.estate is null or vbn.estate='') ");
        } else if ("Edit".equals(type)) {
            sb.append(" and (vbn.estate in( '5' , '4' , '3' )) and vbn.operator_code = '").append(userId).append("'");
        } else if ("Check".equals(type)) {
            sb.append(" and (vbn.estate = '3' ) and vbn.operator_code != '").append(userId).append("'");
        }
        sb.append(" and vbn.bank_acc is not null ");

        //下拨（本方账户是对方账户的父账号）
        sb.append(" and exists (select bb.prent_acc_id from bt_bank_acc_cur bb, bt_bank_acc bc ");
        sb.append(" where bb.bank_acc_id = bc.id and bc.bank_acc = vbn.opp_Acc_No and b.id = bb.prent_acc_id) ");
        //已关联内部账户
        //对方账户已关联内部账户
        sb.append(" AND EXISTS(SELECT 1 FROM bt_bank_acc_cur acur LEFT JOIN fc_acc fa ON fa.BLNCTR_ACC = acur.COUNTER_ACC WHERE acur.BANK_ACC_ID = acc.ID) ");
        //--去掉调拨的
        sb.append("and vbn.bank_acc <> vbn.opp_Acc_No and (vbn.purpose <> '转付下拨' or vbn.purpose is null) ");
        if (!CollectionUtils.isEmpty(notCorpIds)) {
            sb.append(" and ").append(SqlUtil.getNotinSql(notCorpIds, "vbn.corp_id", true));
        }
        sb.append(" and ").append(getSQLIn(corpAuth, 900, "vbn.corp_id"));

        if ("initCancel".equals(type)){
            //已取消（包括bt_net_chalk表与bis_acc_dtl表所有状态为2-已取消状态的数据）
            StringBuilder sql = new StringBuilder();
            sql.append(" select vbn.* from (");
            //bt_net_chalk
            sql.append(" select t.TRANS_TIME as trans_Time,t.BANK_NAME as bank_Name,t.CORP_ID as corp_Id,t.bank_Acc as bank_Acc, ");
            sql.append(" t.ESTATE AS estate,");
            sql.append(" t.CNT_ACCOUNT as cnt_Account,t.ACC_NAME as acc_Name,t.CUR_ID as cur_Id,");
            sql.append(" t.MONEY as money,t.PURPOSE as purpose,t.OPP_ACC_NO as opp_Acc_No,");
            sql.append(" t.OPP_BANK_NAME as opp_Bank_Name,t.ORDER_NO as order_No,");
            sql.append(" t.id as id,'0' as dataFrom ");
            sql.append(" from bt_net_chalk t where t.estate = '2' ");//记账无效
            sql.append(" union all ");
            //bis_Acc_dtl
            sql.append(" SELECT dtl.TRANS_TIME AS trans_Time,dtl.BANK_NAME AS bank_Name,sc.net_id AS corp_Id,");
            sql.append(" dtl.BANK_ACC AS bank_Acc,dtl.ESTATE AS estate,fa.BLNCTR_ACC AS cnt_Account,dtl.ACC_NAME AS acc_Name,");
            sql.append(" dtl.CUR_ID AS cur_ID,dtl.amt AS money,dtl.USES AS purpose,dtl.OPP_ACC_NO AS opp_Acc_No,");
            sql.append(" dtl.OPP_ACC_NAME AS opp_Bank_Name,");
            sql.append(" book.ORDER_NO AS order_No,");
            sql.append(" dtl.id AS id,'1' as dataFrom ");
            sql.append(" from bis_Acc_dtl dtl ");
            sql.append(" left JOIN bt_bank_acc acc ON acc.BANK_ACC = dtl.BANK_ACC");
            sql.append(" LEFT JOIN sys_corp sc ON sc.id = acc.corp_id");
            sql.append(" left JOIN bt_bank_acc_cur acccur ON acccur.BANK_ACC_ID = acc.ID");
            sql.append(" left JOIN fc_acc fa ON fa.BLNCTR_ACC = acccur.COUNTER_ACC");
            sql.append(" LEFT JOIN fc_accbook book ON book.BUS_GLOBAL_ID = dtl.BUS_GLOBAL_ID");
            sql.append(" where dtl.estate = '2' ");//记账无效
            sql.append(" ) vbn where 1=1 ");
            return getDynamicBySQL(dataTablesInput, sql.toString(), BtNetChalkDTO.class, null, false);
        }
        dataTablesInput.setOrderString(new OrderString("vbn.trans_time", "asc"));
        return getDynamicBySQL(dataTablesInput, sb.toString(), BtNetChalkDTO.class, null, false);
    }

    public BtNetChalkDTO queryBtNetChalkDTO(String id) {
        Map<String, Object> params = new HashMap<String, Object>();
        String sql = "select * from bt_net_chalk where id=:id";
        params.put("id", id);
        return this.getBySQLAliasToBean(sql, params, BtNetChalkDTO.class);
    }

    /**
     * 通过流水账主档表id查询明细信息
     */
    public List<FcAccbookDtlDTO> getBookDtlVo(String bookId) {
        Map<String, Object> params = new HashMap<String, Object>();
        String sb = " select a.book_id as bookId , a.summary_content as summaryContent , a.account as account , a.cur_code as curCode , a.acc_name as accName ," +
                "  a.debit_money as debitMoney , a.credit_money as creditMoney , a.voucher_type as voucherType , a.orig_voucher_no as origVoucherNo ," +
                "  a.cash_flux as cashFlux , a.line_no as lineNo, " +
                "  item.item_name cashFluxName" +
                " from FC_ACCBOOK_DTL a  " +
                "      left join FC_CASH_ITEM item " +
                "           on a.CASH_FLUX = item.id" +
                " where 1 = 1   " +
                " and book_id =:bookId " +
                " order by a.line_no asc";
        params.put("bookId", bookId);
        return listBySQLAliasToBean(sb, params, FcAccbookDtlDTO.class);
    }

    /**
     * 查询待记账数据
     *
     * @return
     */
    public List<BtNetChalkDTO> queryTallyData(String isAuto, List<String> corpIds) {
//        StringBuilder sb = new StringBuilder();
//        sb.append(" select vbn.* from bt_net_chalk vbn ").append(" where 1 = 1  ").append(" and vbn.cd_sign = 0 and vbn.corp_type = 0 ");
//        sb.append(" and (vbn.estate = '0' or vbn.estate = '6' or  vbn.estate is null) ");
//        sb.append(" and vbn.bank_acc is not null ");
//        if (FinacialConstant.BOOLEAN_FALSE.equals(isAuto)) {
//            sb.append(" and (vbn.remind_sign = '0' or vbn.remind_sign is null) ");
//        } else {
//            sb.append(" and vbn.PURPOSE is not null  ");
//            sb.append(" and (vbn.auto_tally_sign <> 0 or vbn.auto_tally_sign is null)");
//        }
//        //--去掉调拨的
//        if (!CollectionUtils.isEmpty(corpIds)) {
//            sb.append(SqlUtil.getNotinSql(corpIds, "vbn.corp_id", true));
//        }
//        return listBySQLAliasToBean(sb.toString(), null, BtNetChalkDTO.class);
        StringBuilder sb = new StringBuilder();
        sb.append(" select vbn.* from bt_net_chalk vbn  ")
                .append(" left join bt_bank_acc b on vbn.bank_acc_id = b.id ")
                .append(" left join sys_corp s on s.id = vbn.corp_id ")
                .append(" where 1 = 1  ")
                .append(" and  vbn.cd_sign = 0  and b.parent_sign = '1' and s.type = 0 ");
        sb.append(" and (vbn.estate = '0' or vbn.estate = '6' or  vbn.estate is null) ");
        sb.append(" and vbn.bank_acc is not null ");
        if (FinacialConstant.BOOLEAN_FALSE.equals(isAuto)) {
            sb.append(" and (vbn.remind_sign = '0' or vbn.remind_sign is null or vbn.remind_sign = '') ");
        } else {
            sb.append(" and vbn.PURPOSE is not null  ");
            sb.append(" and (vbn.auto_tally_sign <> 0 or vbn.auto_tally_sign is null)");
        }
        //--去掉调拨的
        sb.append("  and vbn.corp_id not in (select bc.corp_id from bt_bank_acc bc where bc.bank_acc = vbn.opp_Acc_No) ");
        //账号属于同一网点
        sb.append(" and s.NET_ID in (select s1.NET_ID from sys_corp s1 left join bt_bank_acc acc on s1.id=acc.corp_id where acc.bank_acc = vbn.opp_Acc_No)");
        return listBySQLAliasToBean(sb.toString(), null, BtNetChalkDTO.class);
    }


    /**
     * 查询待记账数据
     *
     * @return
     */
    public List<BtNetChalkDTO> queryTallyData(String isAuto,String configSql,String num) {

        StringBuilder sb = new StringBuilder();
        sb.append(" select vbn.* from bt_net_chalk vbn  ")
                .append(" left join bt_bank_acc b on vbn.bank_acc_id = b.id ")
                .append(" left join sys_corp s on s.id = vbn.corp_id ")
                .append(" where 1 = 1  ")
                .append(" and  vbn.cd_sign = 0  and b.parent_sign = '1' and s.type = 0 ");
        sb.append(" and (vbn.estate = '0' or vbn.estate = '6' or  vbn.estate is null) ");
        sb.append(" and vbn.bank_acc is not null ");
        if (FinacialConstant.BOOLEAN_FALSE.equals(isAuto)) {
            sb.append(" and (vbn.remind_sign = '0' or vbn.remind_sign is null or vbn.remind_sign = '') ");
        } else {
            sb.append(" and vbn.PURPOSE is not null  ");
            sb.append(" and (vbn.auto_tally_sign <> 0 or vbn.auto_tally_sign is null or vbn.auto_tally_sign = '')");
        }
        //--去掉调拨的
        sb.append("  and vbn.corp_id not in (select bc.corp_id from bt_bank_acc bc where bc.bank_acc = vbn.opp_Acc_No) ");
        //账号属于同一网点
        sb.append(" and s.NET_ID in (select s1.NET_ID from sys_corp s1 left join bt_bank_acc acc on s1.id=acc.corp_id where acc.bank_acc = vbn.opp_Acc_No)");
        sb.append(" and ").append(configSql);
        if(StringUtils.isNotBlank(num)){
            sb.append(" limit 0,").append(num); //一次记账多少条
        }
        return listBySQLAliasToBean(sb.toString(), null, BtNetChalkDTO.class);
    }

    /**
     * 单位校验使用
     *
     * @return
     * @Param corpId
     */
    public Integer verifyCorpDisableByCorpId(String corpId) {
        String sql = "select count(*) from bt_net_chalk vbn " + " left join bt_bank_acc b on vbn.bank_acc_id = b.id left join sys_corp s on s.id = vbn.corp_id " + " where 1 = 1 and (vbn.estate in( '0','6') or  vbn.estate is null) " +
                " and  vbn.cd_sign = 0  and b.parent_sign = '1' and s.type = 0 " +
                " and vbn.bank_acc is not null and vbn.corp_id <> (select bc.corp_id from bt_bank_acc bc where bc.bank_acc = vbn.opp_Acc_No)  " +
                " and vbn.corp_id = :corpId ";
        Map<String, Object> param = new HashMap<>();
        param.put("corpId", corpId);
        return Integer.valueOf(createSQLQuery(sql, param).uniqueResult().toString());
    }

    /**
     * <br>
     * <b>功能：</b> 提交收款流水记账信息 <br>
     * <b>作者：</b>liyh<br>
     * <b>日期：</b> 2022/3/5 0005 <br>
     */
    public int updateTallySign(BtNetChalkDTO entity, List<String> idList, String eState) {
        StringBuilder sb = new StringBuilder();
        sb.append(" update bt_net_chalk set order_no = :orderNo, voucher_no = :voucherNo, ");
        sb.append(" book_id = :bookId, tally_date = :tallyDate, ");
        sb.append(" operator_code = :operatorCode, Bus_Global_Id = :busGlobalId, ");
        sb.append(" eState = :eState ");
        sb.append(" where 1 = 1  ");
        sb.append(" and (eState = '0' or eState = '6' or eState = '' or eState is null ) ");
        sb.append(" and id in (:id)  ");

        Map<String, Object> param = new HashMap<>();
        param.put("orderNo", entity.getOrderNo());
        param.put("voucherNo", entity.getVoucherNo());
        param.put("bookId", entity.getBookId());
        param.put("tallyDate", entity.getTallyDate());
        param.put("operatorCode", entity.getOperatorCode());
        param.put("busGlobalId", entity.getBusGlobalId());
        param.put("eState", eState);
        param.put("id", idList);
        return createSQLQuery(sb.toString(), param).executeUpdate();
    }

    /**
     * <br>
     * <b>功能：</b> 收款流水记账自动复核信息 <br>
     * <b>作者：</b>liyh<br>
     * <b>日期：</b> 2022/3/5 0005 <br>
     */
    public int updateAutoCheckSign(BtNetChalkDTO entity, List<String> idList) {
        StringBuilder sb = new StringBuilder();
        sb.append(" update bt_net_chalk set order_no = :orderNo, voucher_no = :voucherNo, ");
        sb.append(" book_id = :bookId, tally_date = :tallyDate, ");
        sb.append(" operator_code = :operatorCode, Bus_Global_Id = :busGlobalId, ");
        sb.append(" checker_code = :checkerCode, check_time = :checkTime, ");
        sb.append(" eState = :eState ");
        sb.append(" where 1 = 1  ");
        sb.append(" and (eState = '0' or eState = '6' or eState = '' or eState is null ) ");
        sb.append(" and id in (:id)  ");

        Map<String, Object> param = new HashMap<>();
        param.put("orderNo", entity.getOrderNo());
        param.put("voucherNo", entity.getVoucherNo());
        param.put("bookId", entity.getBookId());
        param.put("tallyDate", entity.getTallyDate());
        param.put("operatorCode", entity.getOperatorCode());
        param.put("busGlobalId", entity.getBusGlobalId());
        param.put("checkerCode", entity.getCheckerCode());
        param.put("checkTime", entity.getCheckTime());
        param.put("eState", NbconsoleConstant.NbcEstate.E_STATE_CHECK_PASS.getValue());
        param.put("id", idList);
        return createSQLQuery(sb.toString(), param).executeUpdate();
    }

    /**
     * <br>
     * <b>功能：</b> 收款流水改账自动复核信息 <br>
     * <b>作者：</b>liyh<br>
     * <b>日期：</b> 2022/3/5 0005 <br>
     */
    public int updateEditAutoCheckSign(BtNetChalkDTO entity, List<String> idList) {
        StringBuilder sb = new StringBuilder();
        sb.append(" update bt_net_chalk set order_no = :orderNo, voucher_no = :voucherNo, ");
        sb.append(" book_id = :bookId, tally_date = :tallyDate, ");
        sb.append(" operator_code = :operatorCode, Bus_Global_Id = :busGlobalId, ");
        sb.append(" checker_code = :checkerCode, check_time = :checkTime, ");
        sb.append(" eState = :eState ");
        sb.append(" where 1 = 1  ");
        sb.append(" and (eState = '3' or eState = '4' or eState = '5') ");
        sb.append(" and id in (:id) ");

        Map<String, Object> param = new HashMap<>();
        param.put("orderNo", entity.getOrderNo());
        param.put("voucherNo", entity.getVoucherNo());
        param.put("bookId", entity.getBookId());
        param.put("tallyDate", entity.getTallyDate());
        param.put("operatorCode", entity.getOperatorCode());
        param.put("busGlobalId", entity.getBusGlobalId());
        param.put("checkerCode", entity.getCheckerCode());
        param.put("checkTime", entity.getCheckTime());
        param.put("eState", NbconsoleConstant.NbcEstate.E_STATE_CHECK_PASS.getValue());
        param.put("id", idList);
        return createSQLQuery(sb.toString(), param).executeUpdate();
    }

    /**
     * <br>
     * <b>功能：</b> 更新收款流水复核信息 <br>
     * <b>作者：</b>liyh<br>
     * <b>日期：</b> 2022/3/5 0005 <br>
     */
    public int updateCheckSign(BtNetChalkDTO entity, List<String> idList) {
        StringBuilder sb = new StringBuilder();
        sb.append(" update bt_net_chalk set voucher_no = :voucherNo, ");
        sb.append(" checker_code = :checkerCode, check_time = :checkTime, ");
        sb.append(" eState = :eState ");
        sb.append(" where 1 = 1  ");
        sb.append(" and eState = '3' ");
        sb.append(" and id in (:id) ");

        Map<String, Object> param = new HashMap<>();
        param.put("voucherNo", entity.getVoucherNo());
        param.put("checkerCode", entity.getCheckerCode());
        param.put("checkTime", entity.getCheckTime());
        param.put("eState", NbconsoleConstant.NbcEstate.E_STATE_CHECK_PASS.getValue());
        param.put("id", idList);
        return createSQLQuery(sb.toString(), param).executeUpdate();
    }

    /**
     * <br>
     * <b>功能：</b> 更新收款流水复核打回信息 <br>
     * <b>作者：</b>liyh<br>
     * <b>日期：</b> 2022/3/5 0005 <br>
     */
    public int updateCheckBackSign(String id) {
        String sb = " update bt_net_chalk set eState = :eState " +
                " where 1 = 1  " +
                " and eState = '3' " +
                " and id = :id  ";
        Map<String, Object> param = new HashMap<>();
        param.put("id", id);
        param.put("eState", NbconsoleConstant.NbcEstate.E_STATE_PASS_BACK.getValue());
        return createSQLQuery(sb, param).executeUpdate();
    }

    /**
     * <br>
     * <b>功能：</b> 更新收款流水改账提交信息 <br>
     * <b>作者：</b>liyh<br>
     * <b>日期：</b> 2022/3/5 0005 <br>
     */
    public int updateEditPassSign(List<String> idList, String eState) {
        StringBuilder sb = new StringBuilder();
        sb.append(" update bt_net_chalk set eState = :eState ");
        sb.append(" where 1 = 1  ");
        sb.append(" and (eState = '3' or eState = '4' or eState = '5') ");
        sb.append(" and id in (:id)  ");

        Map<String, Object> param = new HashMap<>();
        param.put("eState", eState);
        param.put("id", idList);
        return createSQLQuery(sb.toString(), param).executeUpdate();
    }

    /**
     * <br>
     * <b>功能：</b> 更新收款流水改账打回信息 <br>
     * <b>作者：</b>liyh<br>
     * <b>日期：</b> 2022/3/5 0005 <br>
     */
    public int updateEditBackSign(String id, String oriEState) {
        String sb = " update bt_net_chalk set order_no = null, voucher_no = null, " +
                " book_id = null, tally_date = null, " +
                " operator_code = null, Bus_Global_Id = null, " +
                " eState = :eState " +
                " where 1 = 1  " +
                " and (eState = :oriEState ) " +
                " and id = :id  ";
        Map<String, Object> param = new HashMap<>();
        param.put("eState", NbconsoleConstant.NbcEstate.E_STATE_BACK.getValue());
        param.put("oriEState", oriEState);
        param.put("id", id);
        return createSQLQuery(sb, param).executeUpdate();
    }

    /**
     * <br>
     * <b>功能：</b> 更新收款流水复核信息 <br>
     * <b>作者：</b>liyh<br>
     * <b>日期：</b> 2022/3/5 0005 <br>
     */
    public int updateAutoTallySign(String id, String tallySign) {
        String sb = " update bt_net_chalk set AUTO_TALLY_SIGN = :tallySign " +
                " where 1 = 1  " +
                " and (AUTO_TALLY_SIGN is null or AUTO_TALLY_SIGN <> '0')  " +
                " and id = :id  ";
        Map<String, Object> param = new HashMap<>();
        param.put("tallySign", tallySign);
        param.put("id", id);
        return createSQLQuery(sb, param).executeUpdate();
    }

    public List<BtNetChalkDTO> listQueryChalk(DataTablesInput dataTablesInput, String type, String userId, List<String> corpAuth) {
        StringBuilder sb = new StringBuilder(" select vbn.* from bt_net_chalk vbn where vbn.cd_sign = 0 and vbn.corp_type = 0 ");
        if ("Init".equals(type)) {
            sb.append(" and (vbn.estate = '0' or vbn.estate = '6' or vbn.estate is null) ");
            sb.append(" and vbn.bank_acc is not null ");
        } else if ("initCancel".equals(type)) {
            sb.append(" and (vbn.estate = '2' ) ");
        } else if ("Edit".equals(type)) {
            sb.append(" and (vbn.estate in( '5' , '4' , '3' )) and vbn.operator_code = '").append(userId).append("'");
        } else if ("Check".equals(type)) {
            sb.append(" and (vbn.estate = '3' ) and vbn.operator_code != '").append(userId).append("'");
        }
        //--去掉调拨的
        sb.append(" and (vbn.purpose <> '转付下拨' or vbn.purpose is null) ");
        sb.append(" AND vbn.bank_acc <> vbn.opp_Acc_No ");
        sb.append(" and ").append(getSQLIn(corpAuth, 900, "vbn.corp_id"));
        dataTablesInput.setOrderString(new OrderString("vbn.order_no", "asc"));
        return listBySQLAliasToBean(sb.toString(), null, BtNetChalkDTO.class);
    }

    public List<BtNetChalkDTO> listBtNetChalkVo(List<String> idList) {
        Map<String, Object> params = new HashMap<String, Object>();
        String sql = "select * from bt_net_chalk where id in (:id)";
        params.put("id", idList);
        return this.listBySQLAliasToBean(sql, params, BtNetChalkDTO.class);
    }

    public List<String> listByBookId(String bookId) {
        Map<String, Object> params = new HashMap<String, Object>();
        String sql = "select id from bt_net_chalk where book_Id = :bookId";
        params.put("bookId", bookId);
        List<BtNetChalk> list = this.listBySQLAliasToBean(sql, params, BtNetChalk.class);
        List<String> idList = Lists.newArrayList();
        list.forEach(btNetChalk -> idList.add(btNetChalk.getId()));
        return idList;
    }

    public int mergeUpdateEditBackSign(List<String> idList) {
        StringBuilder sb = new StringBuilder();
        sb.append(" update bt_net_chalk set order_no = null, voucher_no = null, ");
        sb.append(" book_id = null, tally_date = null, ");
        sb.append(" operator_code = null, Bus_Global_Id = null, ");
        sb.append(" eState = :eState ");
        sb.append(" where 1 = 1  ");
        sb.append(" and (eState = '3' or eState = '4' or eState = '5') ");
        sb.append(" and id in (:idList)  ");

        Map<String,Object> param = new HashMap<>();
        param.put("eState", NbconsoleConstant.NbcEstate.E_STATE_BACK.getValue());
        param.put("idList", idList);
        return createSQLQuery(sb.toString(), param).executeUpdate();
    }

    public int mergeUpdateCheckBackSign(List<String> idList) {
        StringBuilder sb = new StringBuilder();
        sb.append(" update bt_net_chalk set eState = :eState ");
        sb.append(" where 1 = 1  ");
        sb.append(" and eState = '3' ");
        sb.append(" and id in (:id)  ");

        Map<String,Object> param = new HashMap<>();
        param.put("id", idList);
        param.put("eState", NbconsoleConstant.NbcEstate.E_STATE_PASS_BACK.getValue());
        return createSQLQuery(sb.toString(), param).executeUpdate();
    }

    /**
     * 更新银行流水记账恢复状态
     *
     * @param id
     * @return
     */
    public int updateBisAccDtlRecovery(String id) {
        StringBuilder sb = new StringBuilder();
        sb.append(" update BIS_ACC_DTL set ");
        sb.append(" eState = :eState ");
        sb.append(" where 1 = 1  ");
        sb.append(" and id = :id ");

        Map<String, Object> param = new HashMap<>();
        param.put("eState", NbconsoleConstant.NbcEstate.E_STATE_CHECK.get());
        param.put("id", id);
        return createSQLQuery(sb.toString(), param).executeUpdate();
    }
}
