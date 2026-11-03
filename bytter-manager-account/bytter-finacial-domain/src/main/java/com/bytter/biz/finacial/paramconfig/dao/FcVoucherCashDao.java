package com.bytter.biz.finacial.paramconfig.dao;
import com.bytter.biz.finacial.paramconfig.entity.FcVoucherCash;
import com.bytter.biz.finacial.paramconfig.vo.FcVoucherCashVo;
import com.bytter.core.base.BaseDao;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.core.utils.StringUtils;
import org.springframework.stereotype.Repository;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
 * @author
 * @version V1.0
 * @Title: dao
 * @Description: 现金流量项目科目关系数据访问
 * @date 2020-09-10 09:30:39
 */
@Repository
public class FcVoucherCashDao extends BaseDao<FcVoucherCash, String> {
    /**
     * 列表
     * @param input
     * @return
     */
    public DataTablesOutput listVoucherCash(DataTablesInput input) {
        StringBuilder sql = new StringBuilder();
        sql.append(" select fa.id as id ,fc.subject_code as debitSubCode,fc.subject_name as debitSubName, ");
        sql.append(" fs.subject_code as creditSubCode,fs.subject_name as creditSubName, ");
        sql.append(" fi.item_name as debitItemName,ft.item_name as creditItemName, ");
        sql.append(" fa.VALID_SIGN validSign,");
        sql.append(" fa.rmk as rmk ");
        sql.append(" from fc_voucher_cash fa ");
        sql.append(" left join fc_subject fc on fa.debit_subject = fc.id ");
        sql.append(" left join fc_subject fs on fa.credit_subject = fs.id ");
        sql.append(" left join fc_cash_item fi on fa.debit_item = fi.id ");
        sql.append(" left join fc_cash_item ft on fa.credit_item = ft.id ");
        sql.append(" where 1 = 1  ");
        /*sql.append(" select fa.id as id ,fa.debit_subject as debitSubCode, ");
        sql.append(" fa.credit_subject as creditSubCode, ");
        sql.append(" fi.item_name as debitItemName,ft.item_name as creditItemName, ");
        sql.append(" fa.VALID_SIGN validSign,");
        sql.append(" fa.rmk as rmk ");
        sql.append(" from fc_voucher_cash fa ");
        sql.append(" left join fc_cash_item fi on fa.debit_item = fi.id ");
        sql.append(" left join fc_cash_item ft on fa.credit_item = ft.id ");
        sql.append(" where 1 = 1  ");*/
        //fc.subject_code as debitSubCode,fc.subject_name as debitSubName --> debitSubCode -- id
        //fs.subject_code as creditSubCode,fs.subject_name as creditSubName --> creditSubCode -- id
        return getDynamicBySQL(input, sql.toString(), FcVoucherCashVo.class, null, false);
    }
    /**
    * <br>
    * <b>功能：</b> 查询是否存在录入的数据 <br>
    * <b>作者：</b>liyh<br>
    * <b>日期：</b> 2020/9/11 0011 <br>
    */
    public List<FcVoucherCashVo> queryVoucherCash(FcVoucherCash voucherCash) {
        Map<String, Object> params = new HashMap<String, Object>();
        StringBuilder sql = new StringBuilder();
        sql.append(" select a.id as id ,a.debit_subject as debitSubject ");
        sql.append(" from fc_voucher_cash a ");
        sql.append(" where 1=1 ");
        if (StringUtils.isNotEmpty(voucherCash.getDebitSubject()) || StringUtils.isNotEmpty(voucherCash.getCreditSubject())) {
            sql.append(" and (");
            if (EmptyUtils.isNotEmpty(voucherCash.getDebitSubject())) {
                sql.append(" a.debit_subject = :debitSubject ");
                params.put("debitSubject", voucherCash.getDebitSubject());
            }
            if (StringUtils.isNotEmpty(voucherCash.getDebitSubject()) && EmptyUtils.isNotEmpty(voucherCash.getCreditSubject())) {
                sql.append(" or a.credit_subject = :creditSubject ");
                params.put("creditSubject", voucherCash.getCreditSubject());
            } else if (EmptyUtils.isNotEmpty(voucherCash.getCreditSubject())){
                sql.append(" a.credit_subject = :creditSubject ");
                params.put("creditSubject", voucherCash.getCreditSubject());
            }
            sql.append(" )");
        }
        if (EmptyUtils.isNotEmpty(voucherCash.getId())) {
            sql.append(" and a.id <> :id ");
            params.put("id", voucherCash.getId());
        }
        return listBySQLAliasToBean(sql.toString(), params, FcVoucherCashVo.class);
    }
    /**
     * <br>
     * <b>功能：</b> 查询现金流量项目科目关系维护中已使用的现金流量科目 <br>
     * <b>作者：</b>liyh<br>
     * <b>日期：</b> 2020/9/14 0011 <br>
     */
    public List<FcVoucherCashVo> queryUsedItem(String itemId) {
        Map<String, Object> params = new HashMap<String, Object>();
        StringBuilder sql = new StringBuilder();
        sql.append(" select a.id as id ,a.debit_subject as debitSubject ");
        sql.append(" from fc_voucher_cash a ");
        sql.append(" where 1=1 ");
        sql.append(" and a.debit_item = :itemId or a.credit_item = :itemId ");
        params.put("itemId", itemId);
        return listBySQLAliasToBean(sql.toString(), params, FcVoucherCashVo.class);
    }
    /**
     * 根据id获取
     * @param params
     * @return
     */
    public FcVoucherCashVo getById(Map<String,Object> params) {
        /*StringBuilder sql = new StringBuilder();
        sql.append(" select fa.id as id ,fc.subject_code as debitSubCode,fc.subject_name as debitSubName, ");
        sql.append(" fs.subject_code as creditSubCode,fs.subject_name as creditSubName, ");
        sql.append(" fi.item_name as debitItemName,ft.item_name as creditItemName, ");
        sql.append(" fa.VALID_SIGN validSign,");
        sql.append(" fa.rmk as rmk ");
        sql.append(" from fc_voucher_cash fa ");
        sql.append(" left join fc_subject fc on fa.debit_subject = fc.id ");
        sql.append(" left join fc_subject fs on fa.credit_subject = fs.id ");
        sql.append(" left join fc_cash_item fi on fa.debit_item = fi.id ");
        sql.append(" left join fc_cash_item ft on fa.credit_item = ft.id ");
        sql.append(" where 1 = 1  ");
        sql.append(" and fa.id = :id ");*/
        StringBuilder sql = new StringBuilder();
        sql.append(" select fa.id as id ,fa.debit_subject as debitSubCode, ");
        sql.append(" fa.credit_subject as creditSubCode, ");
        sql.append(" fi.item_name as debitItemName,ft.item_name as creditItemName, ");
        sql.append(" fa.VALID_SIGN validSign,");
        sql.append(" fa.rmk as rmk ");
        sql.append(" from fc_voucher_cash fa ");
        sql.append(" left join fc_cash_item fi on fa.debit_item = fi.id ");
        sql.append(" left join fc_cash_item ft on fa.credit_item = ft.id ");
        sql.append(" where 1 = 1  ");
        sql.append(" and fa.id = :id ");
        return getBySQLAliasToBean(sql.toString(), params, FcVoucherCashVo.class);
    }
    /**
     * 根据科目选择现金流项目
     * @param debitItem
     * @param creditItem
     * @return
     */
    public List<FcVoucherCashVo> getItemBySubject(String debitItem,String creditItem) {
        Map<String, Object> params = new HashMap<String, Object>();
        StringBuilder sql = new StringBuilder();
        sql.append("select a.*, item1.ITEM_NAME debitItemName, item2.ITEM_NAME creditItemName ");
        sql.append("from fc_voucher_cash a ");
        sql.append("         left join FC_CASH_ITEM item1 on a.DEBIT_ITEM = item1.ID ");
        sql.append("         left join FC_CASH_ITEM item2 on a.CREDIT_ITEM = item2.ID ");
        sql.append("where 1 = 1 ");
        sql.append(" and a.VALID_SIGN = '1' ");
        if (StringUtils.isNotEmpty(debitItem)) {
            sql.append(" and a.debit_subject = :debitItem ");
            params.put("debitItem", debitItem);
        }
        if (StringUtils.isNotEmpty(creditItem)) {
            sql.append(" and a.credit_subject = :creditItem ");
            params.put("creditItem", creditItem);
        }
        return listBySQLAliasToBean(sql.toString(), params, FcVoucherCashVo.class);
    }
}
