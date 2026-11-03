package com.bytter.report.domain.cms.dao;

import com.bytter.core.base.BaseDao;
import com.bytter.core.utils.DateUtils;
import com.bytter.core.utils.SqlUtil;
import com.bytter.report.domain.cms.vo.BankCreditBalanceVO;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 银行授信余额表DAO层
 *
 * @author fangcf
 * @date 2025-07-01
 */
@Repository
public class BankCreditBalanceDao extends BaseDao<BankCreditBalanceVO, String> {

    /**
     * 查询银行授信余额明细表
     *
     * @param collectCorpList 单位集合
     * @return 出参
     */
    public List<BankCreditBalanceVO> listBankCreditBalanceDetail(List<String> collectCorpList, Date weekStartDay) {
        StringBuilder sql = new StringBuilder();
        sql.append(" select sc.id as corpId, sc.name as corpName, sc.code as corpCode, sc.PARENT_IDS as parendIds, rb.BANK_TYPE as bankTypeId, bbt.TYPE_NAME as bankTypeName, ");
        sql.append(" rbd.id as disCode, rbdd.ITEM_CODE as itemCode, ric.name as itemName, rbdd.ITEM_FLAG as itemFlag, rbdd.COMP_SIGN as compSign, ");
        sql.append(" case when ric.name = '银行承兑汇票（低风险）' then '2' when ric.name = '银行承兑汇票（敞口）' then '1' else '' end as creditType, ");
        // 银行授信额度, 当授信合同已过期且有占用的情况下, 银行授信额度设置为0
        sql.append(" case when ");
        sql.append("    rb.ENDDATE < bt_to_date(:weekStartDay, 'yyyy-MM-dd') and exists (select 1 from rat_uration ru where ru.rat_code = rb.id and ru.status in (1, 2)) ");
        sql.append(" then 0 else rbdd.MONEY end as money, ");
        sql.append(" rbdd.SUPER_BY_RELIEF_MONEY as superByReliefMoney, rbdd.SUPER_RELIEF_MONEY as superReliefMoney, rbdd.SUPER_PROCESS_MONEY as superProcessMoney, ");
        sql.append(" rbdd.FREEZE_MONEY as freezeMoney, rbdd.VIRTUAL_RELIEF_MONEY as virtualReliefMoney, rbdd.VIRTUAL_URATION as virtualUration, rbdd.RELIEF_AND_URATION_MONEY as reliefAndUrationMoney, ");
        sql.append(" rbdd.MAX_MONEY as maxMoney, rbdd.BY_RELIEF_MAX_MONEY as byReliefMaxMoney, rbdd.RELIEF_MAX_MONEY as reliefMaxMoney, rbdd.PROCESS_MAX_MONEY as processMaxMoney, ");
        sql.append(" rbdd.FREEZE_MAX_MONEY as freezeMaxMoney, rbdd.VIRTUAL_MAX_MONEY as virtualMaxMoney ");
        sql.append(" from rat_bkration_dis rbd ");
        sql.append(" left join rat_bkration_dis_detail rbdd on rbd.id = rbdd.dis_code ");
        sql.append(" left join rat_bkration rb on rb.id = rbd.rat_code ");
        sql.append(" left join rat_item_custom ric on rbdd.item_code = ric.id and ric.status = 0 ");
        sql.append(" left join sys_corp sc on sc.id = rbd.corp_Id ");
        sql.append(" left join bt_bank_type bbt on bbt.id = rb.BANK_TYPE ");
        sql.append(" where rb.status = 95 and rbd.status = 95 and rb.rat_type = '0' ");// 审批通过且是银行授信
        sql.append(" and ").append(SqlUtil.getInSql(collectCorpList, "rbd.corp_Id", true));
        // 固定查询两个授信品种为：银行承兑汇票（低风险）、银行承兑汇票（敞口）
        sql.append(" and ric.name in ('银行承兑汇票（低风险）', '银行承兑汇票（敞口）') ");
        // 过期且未占用，也需要剔除合同，此处sql写的意思是：授信合同到期日期 >= 查询日期 或者 该合同存在授信占用
        sql.append(" and (rb.ENDDATE >= bt_to_date(:weekStartDay, 'yyyy-MM-dd') or exists (select 1 from rat_uration ru where ru.rat_code = rb.id and ru.status in (1, 2)))");

        Map<String, Object> params = Maps.newHashMap();
        params.put("weekStartDay", DateUtils.formatDate(weekStartDay));
        return this.listBySQLAliasToBean(sql.toString(), params, BankCreditBalanceVO.class);
    }

    /**
     * 获取需要汇总主数据单位：单位代码为（100）下，二级以下单位
     *
     * @return 需要汇总的主数据单位集合
     */
    public List<String> listCorpId() {
        StringBuilder sql = new StringBuilder();
        sql.append(" select sc.id as corpId ");
        sql.append(" from sys_corp sc ");
        // 单位有效且只取值100网点下单位
        sql.append(" where sc.STATUS = 1 and sc.NET_ID = (SELECT id FROM sys_corp WHERE CODE = '100') ");
        // 排除网点自己
        sql.append(" and sc.code != '100' ");
        // 排除管理单位为“（6000）大区”的单位
        sql.append(" and (sc.UNIT_CM is null or sc.UNIT_CM != (SELECT id FROM sys_cell_manage WHERE cell_code = '6000')) ");
        List<BankCreditBalanceVO> resultList = this.listBySQLAliasToBean(sql.toString(), null, BankCreditBalanceVO.class);
        if (!CollectionUtils.isEmpty(resultList)) {
            return resultList.stream().map(BankCreditBalanceVO::getCorpId).collect(Collectors.toList());
        }
        return Lists.newArrayList();
    }

    /**
     * 获取区域（二级单位）：单位代码为（100）下级单位，且管理单元为（6000）大区的二级单位
     *
     * @return 二级单位
     */
    public List<BankCreditBalanceVO> listSecondLevCorp() {
        StringBuilder sql = new StringBuilder();
        sql.append(" select sc.id as secondLevCorpId, sc.name as secondLevCorpName, sc.sort as sort ");
        sql.append(" from sys_corp sc  ");
        sql.append(" inner join sys_cell_manage scm on scm.id = sc.UNIT_CM ");
        // 单位有效且只取值100网点下单位，管理单元有效且管理单元为“（6000）大区”
        sql.append(" where sc.STATUS = 1 and scm.valid_sign = '1' and scm.cell_code = '6000' and sc.NET_ID = (SELECT id FROM sys_corp WHERE CODE = '100') ");
        return this.listBySQLAliasToBean(sql.toString(), null, BankCreditBalanceVO.class);
    }

    /**
     * 承兑汇票：银行承兑汇票（低风险）和 银行承兑汇票（敞口）授信品种占用
     *
     * @param corpId       单位id
     * @param bankTypeId   银行类型
     * @param itemCode     授信品种id
     * @param weekStartDay 周开始日
     */
    public BigDecimal calculateOccupyBal(String corpId, String bankTypeId, String itemCode, Date weekStartDay) {
        StringBuilder sql = new StringBuilder();
        sql.append(" select bt_nvL_num(sum(bt_nvL_num(ru.MONEY, 0)), 0) as money ");
        sql.append(" from rat_uration ru ");
        sql.append(" left join rat_bkration rb on rb.ID = ru.RAT_CODE ");
        // 占用为银行授信，且授信占用状态为：1-冻结占用，2-占用
        sql.append(" where ru.RAT_TYPE = '0' and ru.STATUS in ('1', '2') ");
        // 票据业务占用授信或者手动占用授信
        sql.append(" and ( (ru.SYSCODE = 'accebill' and ru.BUSINESS_NAME = '04') or ((ru.SYSCODE = 'rat' and ru.BUSINESS_NAME = '05')) ) ");
        // 结束日期大于查询开始日期的额度
        sql.append(" and ru.CORP_ID = :corpId and rb.BANK_TYPE = :bankTypeId and ru.ITEM_CODE = :itemCode and ru.END_DATE > BT_TO_DATE(:weekStartDay, 'yyyy-MM-dd') ");
        Map<String, Object> params = Maps.newHashMap();
        params.put("corpId", corpId);
        params.put("bankTypeId", bankTypeId);
        params.put("itemCode", itemCode);
        params.put("weekStartDay", DateUtils.formatDate(weekStartDay));
        List<Map<String, Object>> list = this.listMapBySQL(sql.toString(), params);
        BigDecimal occupyBal = BigDecimal.ZERO;
        if (!CollectionUtils.isEmpty(list)) {
            Map<String, Object> map = list.get(0);
            return new BigDecimal(map.get("money").toString());
        }
        return occupyBal;
    }

    /**
     * 流动资金贷款 和 并购贷款 授信品种占用
     *
     * @param corpId       单位id
     * @param bankTypeId   银行类型
     * @param weekStartDay 周开始日
     * @param itemName     授信品种名称
     */
    public BigDecimal calculateOccupyBal(String corpId, String bankTypeId, Date weekStartDay, String itemName) {
        StringBuilder sql = new StringBuilder();
        sql.append(" select bt_nvL_num(sum(bt_nvL_num(ru.MONEY, 0)), 0) as money  ");
        sql.append(" from rat_uration ru ");
        sql.append(" left join rat_bkration rb on rb.ID = ru.RAT_CODE ");
        sql.append(" left join rat_item_custom ric on ru.item_code = ric.id and ric.status = 0 ");
        // 占用为银行授信，且授信占用状态为：1-冻结占用，2-占用
        sql.append(" where ru.RAT_TYPE = '0' and ru.STATUS in ('1', '2') ");
        // 结束日期大于查询开始日期的额度
        sql.append(" and ru.CORP_ID = :corpId and rb.BANK_TYPE = :bankTypeId and ric.NAME = :itemName and ru.END_DATE > BT_TO_DATE(:weekStartDay, 'yyyy-MM-dd') ");
        Map<String, Object> params = Maps.newHashMap();
        params.put("corpId", corpId);
        params.put("bankTypeId", bankTypeId);
        params.put("itemName", itemName);
        params.put("weekStartDay", DateUtils.formatDate(weekStartDay));
        List<Map<String, Object>> list = this.listMapBySQL(sql.toString(), params);
        BigDecimal occupyBal = BigDecimal.ZERO;
        if (!CollectionUtils.isEmpty(list)) {
            Map<String, Object> map = list.get(0);
            return new BigDecimal(map.get("money").toString());
        }
        return occupyBal;
    }

    /**
     * 根据分配id查询分配明细
     *
     * @param disCode 分配id
     * @return 分配明细集合
     */
    public List<BankCreditBalanceVO> listByDisCode(String disCode) {
        StringBuilder sql = new StringBuilder();
        sql.append(" select rbd.id as disCode, rbdd.ITEM_CODE as itemCode, rbdd.ITEM_FLAG as itemFlag, rbdd.MONEY as money, rbdd.COMP_SIGN as compSign ");
        sql.append(" from rat_bkration_dis rbd ");
        sql.append(" left join rat_bkration_dis_detail rbdd on rbd.id = rbdd.dis_code ");
        sql.append(" where rbd.id = :disCode ");

        Map<String, Object> params = Maps.newHashMap();
        params.put("disCode", disCode);
        return this.listBySQLAliasToBean(sql.toString(), params, BankCreditBalanceVO.class);
    }
}
