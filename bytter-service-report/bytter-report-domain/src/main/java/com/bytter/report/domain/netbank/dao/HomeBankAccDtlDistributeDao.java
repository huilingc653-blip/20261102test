package com.bytter.report.domain.netbank.dao;
import com.bytter.core.constant.BdlinkConstant;
import com.bytter.report.domain.netbank.vo.HomeBankAccDtlCurDateDistributeVO;
import com.bytter.report.domain.netbank.vo.HomeBankAccDtlDistributeSearchVO;
import com.bytter.report.domain.netbank.vo.HomeBankAccDtlDistributeVO;
// import com.bytter.biz.netbank.spi.constant.BdlinkConstant;
// import com.bytter.biz.netbank.spi.dto.BisAccDtlDTO;
import com.bytter.core.base.BaseDao;
import com.bytter.core.utils.DateUtils;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.core.utils.SqlUtil;
import com.bytter.system.api.dto.BisAccDtlDTO;
import org.springframework.stereotype.Repository;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
 * 结算收支分析Dao
 * @author huangx 2022-03-28
 */
@Repository
public class HomeBankAccDtlDistributeDao extends BaseDao<HomeBankAccDtlDistributeVO, String> {
    /**
     * 通过单位查询银行账户明细
     * 查询规则为查出明细表以及余额表里面存在的有效数据，并不以任何表为主体
     * 在取期初期末余额时先去掉已删除或无效账号
     * 期初余额为余额表里按照初始时间查询并以corpId以及CurId分组，且对应账号为有效账号以及账号没有删除的余额(bal)之和，如何初始查询时间查询余额为空，那么取离初始最近一边的余额，若无数据，则为空
     * 期末余额取当天的值
     */
    @SuppressWarnings("rawtypes")
    public List<HomeBankAccDtlDistributeVO> getDataByCorp(HomeBankAccDtlDistributeSearchVO searchVO) {
        StringBuilder sql = new StringBuilder();
        Map<String, Object> params = new HashMap<>();
        params.put("beginDate", DateUtils.formatDate(searchVO.getBeginDate(), DateUtils.PATTERN_CLASSICAL));
        params.put("beginDates", DateUtils.formatDate(DateUtils.addDay(searchVO.getBeginDate(), -1), DateUtils.PATTERN_CLASSICAL_SIMPLE));
        params.put("endDate", DateUtils.formatDate(searchVO.getEndDate(), DateUtils.PATTERN_CLASSICAL_SIMPLE) + " 23:59:59");
        params.put("endDates", DateUtils.formatDate(searchVO.getEndDate(), DateUtils.PATTERN_CLASSICAL_SIMPLE));
        //账号控制
        String accAuthSql = "";
        if(EmptyUtils.isNotEmpty(searchVO.getAccIds())) {
            accAuthSql = " and " + SqlUtil.getInSql(searchVO.getAccIds(), "acc.id", true);
        }
        sql.append("SELECT hcorpId as corpId,sc.name AS corpName,sc.code AS code,bc.CUR_NAME AS curName,hcurId as curId,sum( startMoney ) AS startMoney,sum( getamt ) AS getMoney,sum( getnumbers ) AS getNumbers,");
        sql.append("sum( borrowamt ) AS borrowMoney,sum( borrownumbers ) AS borrowNumbers,sum( endMoney ) AS endMoney FROM(");
        //收入
        sql.append("SELECT acc.corp_id AS hcorpId,d.cur_id AS hcurId,0 AS startMoney,sum( CASE WHEN d.rb_sign = '2' OR d.rb_sign IS NULL THEN d.amt ELSE - d.amt END ) AS getamt,");
        sql.append("count( * ) AS getnumbers,0 AS borrowamt,0 AS borrownumbers,0 AS endMoney FROM bis_acc_dtl d LEFT JOIN bt_bank_acc acc ON d.bank_acc = acc.bank_acc ");
        sql.append(" where acc.status <> -2 and acc.valid_sign = 1");
        sql.append(accAuthSql);
        sql.append(" and d.trans_time >= bt_to_date ( :beginDate, 'YYYY-MM-DD HH24:MI:SS' ) AND d.trans_time <= bt_to_date ( :endDate, 'YYYY-MM-DD HH24:MI:SS' ) ");
        sql.append(" and d.cd_sign='0' and acc.corp_id is not null group by acc.corp_id,d.cur_id");
        sql.append(" UNION ALL ");
        //支出
        sql.append("SELECT acc.corp_id AS hcorpId,d.cur_id AS hcurId,0 AS startMoney,0 AS getamt,0 AS getnumbers,");
        sql.append("sum( CASE WHEN d.rb_sign = '2' OR d.rb_sign IS NULL THEN d.amt ELSE - d.amt END ) AS borrowamt,count( * ) AS borrownumbers,0 AS endMoney  FROM bis_acc_dtl d LEFT JOIN bt_bank_acc acc ON d.bank_acc = acc.bank_acc ");
        sql.append(" where acc.status <> -2 and acc.valid_sign = 1");
        sql.append(accAuthSql);
        sql.append(" and d.trans_time >= bt_to_date ( :beginDate, 'YYYY-MM-DD HH24:MI:SS' ) AND d.trans_time <= bt_to_date ( :endDate, 'YYYY-MM-DD HH24:MI:SS' ) ");
        sql.append(" and d.cd_sign='1' and acc.corp_id is not null group by acc.corp_id,d.cur_id");
        sql.append(" UNION ALL ");
        //期初
        sql.append("SELECT b.corp_id AS hcorpId,b.cur_id AS hcurId,sum( b.bal ) AS startMoney,0 AS getamt,0 AS getnumbers,0 AS borrowamt,0 AS borrownumbers,0 AS endMoney");
        sql.append(" from bis_acc_bal b left join bt_bank_acc acc on b.bank_acc = acc.bank_acc where acc.status <> -2 and acc.valid_sign = 1 ");
        sql.append(accAuthSql);
        sql.append("and b.bal_date=bt_to_date(:beginDates,'YYYY-MM-DD') and b.bal is not null group by b.corp_id,b.cur_id");
        //sql.append("  and b.bal_date=(select  max(bal_date) from bis_acc_bal bal where bal_date < bt_to_date(:beginDates,'YYYY-MM-DD') and bal.bank_acc=b.bank_acc and bal.cur_id=b.cur_id) and b.bal is not null group by b.corp_id,b.cur_id");
        //sql.append(" UNION ALL ");
        //期末
        /*sql.append("SELECT b.corp_id AS hcorpId,b.cur_id AS hcurId,0 AS startMoney,0 AS getamt,0 AS getnumbers,0 AS borrowamt,0 AS borrownumbers,sum( b.bal ) AS endMoney");
        sql.append(" from bis_acc_bal b left join bt_bank_acc acc on b.bank_acc = acc.bank_acc where acc.status <> -2 and acc.valid_sign = 1 ");
        sql.append(accAuthSql);
        sql.append("and b.bal_date=bt_to_date(:endDates,'YYYY-MM-DD') and b.bal is not null group by b.corp_id,b.cur_id");*/
        //sql.append(" and b.bal_date=(select  max(bal_date) from bis_acc_bal bal where bal_date <= bt_to_date(:endDates,'YYYY-MM-DD') and bal.bank_acc=b.bank_acc and bal.cur_id=b.cur_id) and b.bal is not null group by b.corp_id,b.cur_id");
        sql.append(" ) hl left join sys_corp sc ON hl.hcorpId = sc.id left join bt_currency bc ON hl.hcurId = bc.id where 1=1");
        //单位控制
        if(EmptyUtils.isNotEmpty(searchVO.getCorpIds())) {
            String corpAuthSql = " and " + SqlUtil.getInSql(searchVO.getCorpIds(), "hl.hcorpId", true);
            sql.append(corpAuthSql);
        }
        sql.append(" GROUP BY hl.hcorpId, hl.hcurId,sc.code,sc.name,bc.CUR_NAME");
        return listBySQLAliasToBean(sql.toString(), params, HomeBankAccDtlDistributeVO.class);
    }
    /**
     * 通过单位查询银行账户明细
     * 查询规则为查出明细表以及余额表里面存在的有效数据，并不以任何表为主体
     * 在取期初期末余额时先去掉已删除或无效账号
     * 期初余额为余额表里按照初始时间查询并以corpId以及CurId分组，且对应账号为有效账号以及账号没有删除的余额(bal)之和，如何初始查询时间查询余额为空，那么取离初始最近一边的余额，若无数据，则为空
     * 期末余额取当天的值
     */
    @SuppressWarnings("rawtypes")
    public List<HomeBankAccDtlDistributeVO> getDataGroupByTransTime(HomeBankAccDtlDistributeSearchVO searchVO) {
        StringBuilder sql = new StringBuilder();
        Map<String, Object> params = new HashMap<>();
        params.put("beginDate", DateUtils.formatDate(searchVO.getBeginDate(), DateUtils.PATTERN_CLASSICAL));
        params.put("endDate", DateUtils.formatDate(searchVO.getEndDate(), DateUtils.PATTERN_CLASSICAL_SIMPLE)+" 23:59:59");
        //账号控制
        String accAuthSql = "";
        if(EmptyUtils.isNotEmpty(searchVO.getAccIds())) {
            accAuthSql = " and " + SqlUtil.getInSql(searchVO.getAccIds(), "acc.id", true);
        }
        sql.append("SELECT hcorpId as corpId,bc.CUR_NAME AS curName,hcurId as curId,transTime,sum( getamt ) AS getMoney,sum( getnumbers ) AS getNumbers,");
        sql.append("sum( borrowamt ) AS borrowMoney,sum( borrownumbers ) AS borrowNumbers FROM(");
        //收入
        sql.append("SELECT acc.corp_id AS hcorpId,d.cur_id AS hcurId,bt_to_char(d.trans_time, 'YYYY-MM-DD') as transTime,sum( CASE WHEN d.rb_sign = '2' OR d.rb_sign IS NULL THEN d.amt ELSE - d.amt END ) AS getamt,");
        sql.append("count( * ) AS getnumbers,0 AS borrowamt,0 AS borrownumbers FROM bis_acc_dtl d LEFT JOIN bt_bank_acc acc ON d.bank_acc = acc.bank_acc ");
        sql.append(" where acc.status <> -2 and acc.valid_sign = 1");
        sql.append(accAuthSql);
        sql.append(" and d.trans_time >= bt_to_date ( :beginDate, 'YYYY-MM-DD HH24:MI:SS' ) AND d.trans_time <= bt_to_date ( :endDate, 'YYYY-MM-DD HH24:MI:SS' ) ");
        sql.append(" and d.cd_sign='0' and acc.corp_id is not null group by acc.corp_id,d.cur_id,bt_to_char(d.trans_time, 'YYYY-MM-DD')");
        sql.append(" UNION ALL ");
        //支出
        sql.append("SELECT acc.corp_id AS hcorpId,d.cur_id AS hcurId,bt_to_char(d.trans_time, 'YYYY-MM-DD') as transTime,0 AS getamt,0 AS getnumbers,");
        sql.append("sum( CASE WHEN d.rb_sign = '2' OR d.rb_sign IS NULL THEN d.amt ELSE - d.amt END ) AS borrowamt,count( * ) AS borrownumbers  FROM bis_acc_dtl d LEFT JOIN bt_bank_acc acc ON d.bank_acc = acc.bank_acc ");
        sql.append(" where acc.status <> -2 and acc.valid_sign = 1");
        sql.append(accAuthSql);
        sql.append(" and d.trans_time >= bt_to_date ( :beginDate, 'YYYY-MM-DD HH24:MI:SS' ) AND d.trans_time <= bt_to_date ( :endDate, 'YYYY-MM-DD HH24:MI:SS' ) ");
        sql.append(" and d.cd_sign='1' and acc.corp_id is not null group by acc.corp_id,d.cur_id,bt_to_char(d.trans_time, 'YYYY-MM-DD')");
        sql.append(" ) hl left join bt_currency bc ON hl.hcurId = bc.id where 1=1");
        //单位控制
        if(EmptyUtils.isNotEmpty(searchVO.getCorpIds())) {
            String corpAuthSql = " and " + SqlUtil.getInSql(searchVO.getCorpIds(), "hl.hcorpId", true);
            sql.append(corpAuthSql);
        }
        sql.append(" GROUP BY hl.hcorpId, hl.hcurId,transTime,bc.CUR_NAME");
        return listBySQLAliasToBean(sql.toString(), params, HomeBankAccDtlDistributeVO.class);
    }
    /**
     * 获取当日交易情况
     * @param searchVO
     * @return
     */
    public List<HomeBankAccDtlCurDateDistributeVO> getExc(HomeBankAccDtlDistributeSearchVO searchVO) {
        StringBuilder sql = new StringBuilder();
        Map<String, Object> params = new HashMap<>();
        params.put("beginDate", DateUtils.parse(DateUtils.formatDate(searchVO.getBeginDate(), DateUtils.PATTERN_CLASSICAL), DateUtils.PATTERN_CLASSICAL));
        params.put("endDate", DateUtils.parse(DateUtils.formatDate(searchVO.getEndDate(), DateUtils.PATTERN_CLASSICAL_SIMPLE)+" 23:59:59", DateUtils.PATTERN_CLASSICAL));
        sql.append("select count(*) as totalNum,bt_nvl_num(sum(c.pay_money),0) as totalMoney,c.voucher_Stat as voucherStat from bis_exc c ");
        sql.append(" left join bt_bank_acc acc on c.payer_acc = acc.bank_acc ");
        sql.append(" where c.voucher_stat in ('" + BdlinkConstant.SUCCESS + "','" + BdlinkConstant.WAIT_FILL + "','" + BdlinkConstant.INTERFACE_FAILED + "','" + BdlinkConstant.BANK_FAILED + "')");
        //单位控制
        if(EmptyUtils.isNotEmpty(searchVO.getCorpIds())) {
            String corpAuthSql = " and " + SqlUtil.getInSql(searchVO.getCorpIds(), "c.corp_Id", true);
            sql.append(corpAuthSql);
        }
        //账号控制
        if(EmptyUtils.isNotEmpty(searchVO.getAccIds())) {
            String accAuthSql = " and " + SqlUtil.getInSql(searchVO.getAccIds(), "acc.id", true);
            sql.append(accAuthSql);
        }
        sql.append(" and c.req_date>=:beginDate and c.req_date<=:endDate ");
        sql.append(" group by c.voucher_Stat");
        return listBySQLAliasToBean(sql.toString(), params, HomeBankAccDtlCurDateDistributeVO.class);
    }
    /**
     * 根据流水
     * @param searchVO
     * @return
     */
    public List<BisAccDtlDTO> getBankAccDtl(HomeBankAccDtlDistributeSearchVO searchVO) {
        Map<String, Object> params = new HashMap<>();
        params.put("beginDate", DateUtils.parse(DateUtils.formatDate(searchVO.getBeginDate(), DateUtils.PATTERN_CLASSICAL_SIMPLE), DateUtils.PATTERN_CLASSICAL_SIMPLE));
        params.put("endDate", DateUtils.parse(DateUtils.formatDate(searchVO.getEndDate(), DateUtils.PATTERN_CLASSICAL_SIMPLE), DateUtils.PATTERN_CLASSICAL_SIMPLE));
        StringBuilder sql = new StringBuilder();
        sql.append(" select t.id,t.acc_no,acc.corp_id,t.bank_acc,t.acc_name,t.bank_name,t.bif_code, bis.name bifName,t.opp_acc_no,t.opp_acc_name,t.opp_acc_bank,t.cd_sign,t.rb_sign,");
        sql.append(" t.amt,t.bal,t.cur_id,t.uses,t.postscript,t.remark,t.trans_time,t.voucher_no,t.bank_serial_no,t.return_time, ");
        sql.append(" t.import_sign,t.import_date,t.create_by,t.create_date,t.update_by,t.update_date,t.fbs_status, t.project_remark,t.REFUND_BLEND_TYPE from bis_acc_dtl t ");
        sql.append(" left join bt_bank_acc acc on t.bank_acc = acc.bank_acc ");
        sql.append(" left join bis_bif_init bis on bis.id = t.bif_code  ");
        sql.append(" where 1=1 ");
        //单位控制
        if(EmptyUtils.isNotEmpty(searchVO.getCorpIds())) {
            String corpAuthSql = " and " + SqlUtil.getInSql(searchVO.getCorpIds(), "acc.corp_Id", true);
            sql.append(corpAuthSql);
        }
        //账号控制
        if(EmptyUtils.isNotEmpty(searchVO.getAccIds())) {
            String accAuthSql = " and " + SqlUtil.getInSql(searchVO.getAccIds(), "acc.id", true);
            sql.append(accAuthSql);
        }
        sql.append(" order by t.trans_time desc");
        return listBySQLAliasToBean(sql.toString(), 0, 10, params, BisAccDtlDTO.class);
    }
}
