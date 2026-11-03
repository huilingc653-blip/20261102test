package com.bytter.biz.foreign.dao;

import cn.hutool.core.collection.CollectionUtil;
import com.bytter.biz.foreign.entity.FexTranTemplateConfig;
import com.bytter.biz.foreign.vo.CountryIdVO;
import com.bytter.biz.foreign.vo.FexTranTemplateConfigDetailVO;
import com.bytter.biz.foreign.vo.FexTranTemplateConfigParamVO;
import com.bytter.biz.foreign.vo.FexTranTemplateConfigVO;
import com.bytter.core.base.BaseDao;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.core.utils.StringUtils;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author ZZF
 * @version 1.0
 * @Title: Dao
 * @Description: 交易模板配置Dao
 * @date 2025-11-17 15:20:33
 */
@Repository
public class FexTranConfigTemplateDao extends BaseDao<FexTranTemplateConfig, String> {




    /**
     * 配置模板列表
     *
     * @param vo
     * @return
     */
    public List<FexTranTemplateConfigVO> listOfParent(FexTranTemplateConfigParamVO vo) {
        StringBuilder sql = new StringBuilder();
        sql.append(" select p.id,p.template_no templateNo,p.template_name templateName, p.valid_sign validSign " +
                "FROM fex_tran_template_config p " +
                "left join fex_tran_template_config c on p.id=c.parent_id and c.is_delete=0  " +
                getCountryJoinSql("c") +
                "left join bt_bank_type bbt on bbt.id=c.bank_type_id " +
                "left join bis_bif_init bbi on bbi.id=c.bif_id " +
                "left join bt_bank_type_custom bbtc on bbtc.id=c.pay_type_id  " +
                "WHERE p.parent_id IS  null and p.is_delete=0    ");
        StringBuilder joinSql = new StringBuilder();
        Map<String, Object> param = new HashMap<>();
        if (EmptyUtils.isNotEmpty(vo.getTemplateNo())) {
            joinSql.append(" AND p.template_no like '%" + vo.getTemplateNo() + "%'");
        }
        if (EmptyUtils.isNotEmpty(vo.getTemplateName())) {
            joinSql.append(" AND p.template_name like '%" + vo.getTemplateName() + "%'");
        }
        if (EmptyUtils.isNotEmpty(vo.getValidSign())) {
            joinSql.append(" AND p.VALID_SIGN = :validSign");
            param.put("validSign", vo.getValidSign());
        }
        if (EmptyUtils.isNotEmpty(vo.getPayTypeName())) {
            joinSql.append(" AND bbtc.custom_name like '%" + vo.getPayTypeName() + "%'");
        }
        if (EmptyUtils.isNotEmpty(vo.getBifIds())) {
            joinSql.append(" and (" + getSQLIn(vo.getBifIds(), 900, "c.bif_id") + ")");
        }
        if (EmptyUtils.isNotEmpty(vo.getBankTypeIds())) {
            joinSql.append(" and (" + getSQLIn(vo.getBankTypeIds(), 900, "c.bank_type_id") + ")");
        }
        if (EmptyUtils.isNotEmpty(vo.getCountryIds())) {
            joinSql.append(" and (" + getSQLIn(vo.getCountryIds(), 900, "scc.id") + ")");
        }
        if (StringUtils.isNotEmpty(joinSql)) {
            sql.append(joinSql);
        }
        sql.append(" group by p.id,p.template_no ,p.template_name , p.valid_sign ,p.create_date order by p.create_date asc");
        return listBySQLAliasToBean(sql.toString(), param, FexTranTemplateConfigVO.class);
    }

    /**
     * 配置模板列表
     *
     * @param vo
     * @return
     */
    public List<FexTranTemplateConfigDetailVO> listOfChildren(FexTranTemplateConfigParamVO vo) {
        StringBuilder sql = new StringBuilder();
        sql.append(" select  distinct f.id," +
                "p.template_no templateNo," +
                "p.template_name templateNameParent," +
                "f.template_name templateName," +
                "f.parent_id parentId," +
                "f.bank_type_id bankTypeId, " +
                "f.country_id countryId," +
                "f.bif_id bifId, " +
                "f.sort sort, " +
                "f.valid_sign validSign, " +
                "f.pay_type_id payTypeId, " +
                "scc.NAME_ZH countryZhName, " +
                "bbt.type_name bankTypeName, " +
                "bbi.BIF_CODE bifName," +
                "bbtc.custom_name  payTypeName,  " +
                "bbtc.custom_name  customName , " +
                "bbtc.custom_code  customCode  " +
                "FROM fex_tran_template_config f " +
                "left join fex_tran_template_config p on p.id=f.parent_id  and p.is_delete=0  " +
                getCountryJoinSql("f") +
                "left join bt_bank_type bbt on bbt.id=f.bank_type_id " +
                "left join bis_bif_init bbi on bbi.id=f.bif_id " +
                "left join bt_bank_type_custom bbtc on bbtc.id=f.pay_type_id " +
                "WHERE f.parent_id IS not null  and f.is_delete=0 and f.valid_sign=1 ");
        HashMap<String, Object> param = new HashMap<>();
        if (EmptyUtils.isNotEmpty(vo.getPayTypeId())) {
            param.put("payTypeId", vo.getPayTypeId());
            sql.append(" and f.pay_type_id =:payTypeId ");
        }
        if (EmptyUtils.isNotEmpty(vo.getBankTypeIds())) {
            sql.append(" and (" + getSQLIn(vo.getBankTypeIds(), 900, "f.bank_type_id") + ")");
        }
        if (EmptyUtils.isNotEmpty(vo.getPayTypeIds())) {
            sql.append(" and (" + getSQLIn(vo.getPayTypeIds(), 900, "f.pay_type_id") + ")");
        }
        if (EmptyUtils.isNotEmpty(vo.getIds())) {
            sql.append(" and (" + getSQLIn(vo.getIds(), 900, "f.id") + ")");
        }
        sql.append("  order by f.sort asc");
        return listBySQLAliasToBean(sql.toString(), param, FexTranTemplateConfigDetailVO.class);
    }




    /**
     * 配置模板列表
     *
     * @param vo
     * @return
     */
    public  List<FexTranTemplateConfigDetailVO> list(FexTranTemplateConfigParamVO vo) {
        StringBuilder sql = new StringBuilder();
        sql.append(" select f.id," +
                "f.template_no templateNo," +
                "p.template_name templateName," +
                "f.parent_id parentId," +
                "f.bank_type_id bankTypeId, " +
                "f.country_id countryId, " +
                "f.bif_id bifId, " +
                "f.sort sort, " +
                "p.valid_sign validSignParent, " +
                "f.valid_sign validSign, " +
                "f.pay_type_id payTypeId, " +
                "scc.NAME_ZH countryZhName, " +
                "bbt.type_name bankTypeName, " +
                "bbi.bif_code bifName," +
                "bbtc.custom_code  customCode , " +
                "bbtc.custom_name  customName  " +
                "FROM fex_tran_template_config f " +
                "left join fex_tran_template_config p on p.id=f.parent_id  and p.is_delete=0 " +
                getCountryJoinSql("f") +
                "left join bt_bank_type bbt on bbt.id=f.bank_type_id " +
                "left join bis_bif_init bbi on bbi.id=f.bif_id " +
                "left join bt_bank_type_custom bbtc on bbtc.id=f.pay_type_id " +
                "WHERE f.parent_id IS not null and f.is_delete=0   ");
        StringBuilder joinSql = new StringBuilder();
        Map<String, Object> param = new HashMap<>();
        if (EmptyUtils.isNotEmpty(vo.getParentId())) {
            joinSql.append(" AND f.parent_id = :parentId");
            param.put("parentId", vo.getParentId());
        }
        if (EmptyUtils.isNotEmpty(vo.getValidSign())) {
            joinSql.append(" AND f.VALID_SIGN = :validSign");
            param.put("validSign", vo.getValidSign());
        }
        if (EmptyUtils.isNotEmpty(vo.getPayTypeName())) {
            joinSql.append(" AND bbtc.custom_name like '%" + vo.getPayTypeName() + "%'");
        }
        if (EmptyUtils.isNotEmpty(vo.getParentIds())) {
            joinSql.append(" and (" + getSQLIn(vo.getParentIds(), 900, "f.parent_id") + ")");
        }
        if (EmptyUtils.isNotEmpty(vo.getBifIds())) {
            joinSql.append(" and (" + getSQLIn(vo.getBifIds(), 900, "f.bif_id") + ")");
        }
        if (EmptyUtils.isNotEmpty(vo.getBankTypeIds())) {
            joinSql.append(" and (" + getSQLIn(vo.getBankTypeIds(), 900, "f.bank_type_id") + ")");
        }
        if (EmptyUtils.isNotEmpty(vo.getCountryIds())) {
            joinSql.append(" and (" + getSQLIn(vo.getCountryIds(), 900, "scc.id") + ")");
        }
        if (StringUtils.isNotEmpty(joinSql)) {
            sql.append(joinSql);
        }
        sql.append(" order by f.sort asc");
        List<FexTranTemplateConfigDetailVO>  list=   listBySQLAliasToBean(sql.toString(), param, FexTranTemplateConfigDetailVO.class);
        if(EmptyUtils.isNotEmpty(list)){
            list.forEach(fexTranTemplateConfigDetailVO->{
                fexTranTemplateConfigDetailVO.setPayTypeName(fexTranTemplateConfigDetailVO.getCustomCode()+"("+fexTranTemplateConfigDetailVO.getCustomName()+")");
            });
        }
        return list;
    }



    public DataTablesOutput<FexTranTemplateConfigDetailVO> page(DataTablesInput dataTablesInput) {
        StringBuilder sql = new StringBuilder();
        sql.append(" select  f.id,p.template_no templateNo,p.template_name templateName," +
                "f.parent_id parentId,f.bank_type_id bankTypeId, f.country_id countryId,cm.NAME_ZH countryZhName, f.bif_id bifId, " +
                " f.valid_sign validSign, f.pay_type_id payTypeId,   " +
                "bbt.type_name bankTypeName, bbi.BIF_CODE bifName,bbtc.custom_code as customCode,bbtc.custom_name as customName " +
                "FROM fex_tran_template_config f " +
                "left join fex_tran_template_config p on p.id=f.parent_id  and  p.is_delete=0 " +
                "inner join sys_country_cust_manager cm on cm.id=f.country_id  "+
                "left join bt_bank_type bbt on bbt.id=f.bank_type_id " +
                "left join bis_bif_init bbi on bbi.id=f.bif_id " +
                "left join bt_bank_type_custom bbtc on bbtc.id=f.pay_type_id " +
                "WHERE f.parent_id IS not null  and f.is_delete=0 " +
                "and f.valid_sign=1 " +
                "and f.country_id is not null " +
                "and f.bank_type_id is not null " +
                "and f.bif_id is not null " +
                "and f.pay_type_id  is not null ");
        StringBuilder joinSql = new StringBuilder();
        Map<String,Object> param= dataTablesInput.getFormdata();

        Object parentIdoB=  param.get("parentId");
        if (!EmptyUtils.isEmpty(parentIdoB)) {
            String parentId= (String) parentIdoB;
            joinSql.append(" AND f.parent_id = :parentId");
            param.put("parentId", parentId);
        }

        Object validSignOb=   param.get("validSign");
        if (!EmptyUtils.isEmpty(validSignOb)) {
            String validSign= (String) validSignOb;
            joinSql.append(" AND f.VALID_SIGN = :validSign");
            param.put("validSign", validSign);
        }

        Object payTypeNameOb= param.get("payTypeName");
        if (!EmptyUtils.isEmpty(payTypeNameOb)) {
            String payTypeName= (String) payTypeNameOb;
            if (!EmptyUtils.isEmpty(payTypeName)) {
                joinSql.append(" AND bbtc.custom_name like '%" +payTypeName + "%'");
            }
        }

        Object templateNameOb= param.get("templateName");
        if (!EmptyUtils.isEmpty(templateNameOb)) {
            String templateName= (String) templateNameOb;
            if (!EmptyUtils.isEmpty(templateName)) {
                joinSql.append(" AND (f.template_name like '%" +templateName + "%'  ");
                joinSql.append(" or bbt.TYPE_NAME like '%" +templateName + "%'  ");
                joinSql.append(" or cm.NAME_ZH like '%" +templateName + "%'  ");
                joinSql.append(" or bbi.NAME like '%" +templateName + "%'  ");
                joinSql.append(" or bbtc.CUSTOM_CODE like '%" +templateName + "%'  ");
                joinSql.append(" or bbtc.CUSTOM_NAME like '%" +templateName + "%' ) ");
            }
        }

        Object templateNoOb= param.get("templateNo");
        if (!EmptyUtils.isEmpty(templateNoOb)) {
            String templateNo= (String) templateNoOb;
            if (!EmptyUtils.isEmpty(templateNo)) {
                joinSql.append(" AND p.template_no like '%" +templateNo + "%'");
            }
        }


        Object bifIdsOb=   param.get("bifIds");
        if (!EmptyUtils.isEmpty(bifIdsOb)) {
            List<String> bifIds= (List<String>)bifIdsOb;
            joinSql.append(" and (" + getSQLIn(bifIds, 900, "f.bif_id") + ")");
        }

        Object bankTypeIdsOb=   param.get("bankTypeIds");
        if (!EmptyUtils.isEmpty(bankTypeIdsOb)) {
            List<String> bankTypeIds= (List<String>) bankTypeIdsOb;
            joinSql.append(" and (" + getSQLIn(bankTypeIds, 900, "f.bank_type_id") + ")");
        }
        Object  countryIdsOb=  param.get("countryIds");
        if (!EmptyUtils.isEmpty(countryIdsOb)) {
            List<String> countryIds= (List<String>) countryIdsOb;
            joinSql.append(" and (" + getSQLIn(converCCountryToMCountryId(countryIds), 900, "cm.id") + ")");
        }
        if (StringUtils.isNotEmpty(joinSql)) {
            sql.append(joinSql);
        }
        sql.append(" GROUP BY f.id, p.template_no, p.template_name, f.parent_id, f.bank_type_id,cm.NAME_ZH, f.bif_id, f.valid_sign, f.pay_type_id, bbt.type_name, bbi.BIF_CODE,  bbtc.custom_code,  bbtc.custom_name  ,f.country_id ");
        // 模板编号允许重复，分页排序必须追加唯一键，避免翻页时数据重复或丢失
        sql.append(" order by p.template_no asc, f.id asc");

        DataTablesOutput<FexTranTemplateConfigDetailVO>  result=  getDynamicBySQL(dataTablesInput, sql.toString(), FexTranTemplateConfigDetailVO.class, "f", false);
        converMCountryToCCountryId( result);
        return result;
    }
    public List<CountryIdVO> getCuntryIds(List<String> managerCountryIds, List<String> custCountryIds) {
        String sql = "select cm.sys_id sysCountryId,cm.id managerCountryId,scc.id custCountryId  " +
                "from   sys_country_cust_manager cm   " +
                "inner join sys_country sc on sc.id=cm.sys_id  " +
                "inner join sys_country_cust scc on scc.sys_id=sc.id ";
        StringBuilder joinSql = new StringBuilder(sql);
        if(EmptyUtils.isNotEmpty(managerCountryIds)){
            joinSql.append(" and (" + getSQLIn(managerCountryIds, 900, "cm.id") + ")");
        }
        if(EmptyUtils.isNotEmpty(custCountryIds)){
            joinSql.append(" and (" + getSQLIn(custCountryIds, 900, "scc.id") + ")");
        }
        List<CountryIdVO> result=  listBySQLAliasToBean(joinSql.toString(), null, CountryIdVO.class);
        return result;
    }

    private List<String> converCCountryToMCountryId(List<String> cCountryIds) {
        if(!EmptyUtils.isEmpty(cCountryIds)){
           return getCuntryIds(null,cCountryIds).stream().map(CountryIdVO::getManagerCountryId).distinct().collect(Collectors.toList());
        }
        return new ArrayList<>();
    }
    private void converMCountryToCCountryId(DataTablesOutput<FexTranTemplateConfigDetailVO> result) {
        List<FexTranTemplateConfigDetailVO> data = result.getData();
        if (!EmptyUtils.isEmpty(data)) {
            Map<String, List<CountryIdVO>> countryIdVOS = getCuntryIds(data.stream().map(FexTranTemplateConfigDetailVO::getCountryId).collect(Collectors.toList()), null).stream().collect(Collectors.groupingBy(CountryIdVO::getManagerCountryId));
            for (FexTranTemplateConfigDetailVO vo : data) {
                vo.setPayTypeName(vo.getCustomCode() + "（" + vo.getCustomName() + "）");
                List<CountryIdVO> countryIdVOS1 = countryIdVOS.get(vo.getCountryId());
                if (!EmptyUtils.isEmpty(countryIdVOS1)) {
                    vo.setCountryId(countryIdVOS1.get(0).getCustCountryId());
                }
            }
        }
        result.setData(data);
    }

    public List<FexTranTemplateConfig> listByCondition(FexTranTemplateConfig vo) {
        StringBuilder joinSql = new StringBuilder();
        Map<String, Object> param = new HashMap<>();
        if (EmptyUtils.isNotEmpty(vo.getTemplateNo())) {
            joinSql.append(" AND parent.template_no = :templateNo");
            param.put("templateNo", vo.getTemplateNo());
        }
        if (EmptyUtils.isNotEmpty(vo.getTemplateName())) {
            joinSql.append(" AND parent.template_name = :templateName");
            param.put("templateName", vo.getTemplateName());
        }
        if (EmptyUtils.isNotEmpty(vo.getPayTypeId())) {
            joinSql.append(" AND parent.pay_type_id = :payTypeId");
            param.put("payTypeId", vo.getPayTypeId());
        }
        if (EmptyUtils.isNotEmpty(vo.getParentId())) {
            joinSql.append(" AND parent.parent_id = :parentId");
            param.put("parentId", vo.getParentId());
        }
        if (EmptyUtils.isNotEmpty(vo.getIsDelete())) {
            joinSql.append(" AND parent.is_delete = :isDelete");
            param.put("isDelete", vo.getIsDelete());
        }
        StringBuilder sql = new StringBuilder("select  id, parent_id, template_no," +
                " template_name, template_desc, " +
                "remark, bank_type_id, bif_id, country_id, pay_type_id, valid_sign," +
                " is_delete, create_by, create_date, update_by, update_date," +
                " sort from fex_tran_template_config parent where   is_delete=0 ");
        if (StringUtils.isNotEmpty(joinSql)) {
            sql.append(joinSql);
        }
        sql.append(" order by parent.create_date desc");
        return listBySQLAliasToBean(sql.toString(), param, FexTranTemplateConfig.class);
    }


    public void deleteByIds(List<String> ids,String userId) {
        if(EmptyUtils.isEmpty(ids)){
            return;
        }
        StringBuilder joinSql = new StringBuilder("update fex_tran_template_config set is_delete=1 ,update_by=:updateBy where 1=1");
        Map<String, Object> param = new HashMap<>();
        param.put("updateBy", userId);
        if (EmptyUtils.isNotEmpty(ids)) {
            joinSql.append(" and " + getSQLIn(ids, 900, "id") );
        }
        this.batchSQLExecute(joinSql.toString(), param);
    }


    /**
     * 批量检查配置组合是否重复
     * 检查 bankTypeId、bifId、countryId、payTypeId 四个字段同时相同的数据
     *
     * @param voList 待检查的配置列表
     * @return 返回重复的组合信息，包含重复的字段组合和对应的ID列表
     */
    public List<FexTranTemplateConfigDetailVO> batchCheckDuplicate(List<FexTranTemplateConfigDetailVO> voList) {
        if (EmptyUtils.isEmpty(voList)) {
            return new ArrayList<>();
        }
        StringBuilder sql = new StringBuilder();
        Map<String, Object> param = new HashMap<>();
        sql.append(" SELECT t.id id,");
        sql.append("     p.template_no AS templateNo, ");
        sql.append("     p.template_name AS templateName, ");
        sql.append("     t.parent_id AS parentId, ");
        sql.append("     t.bank_type_id AS bankTypeId, ");
        sql.append("     t.bif_id AS bifId, ");
        sql.append("     t.country_id AS countryId, ");
        sql.append("     t.pay_type_id AS payTypeId ");
        sql.append(" FROM fex_tran_template_config t " +
                "left join fex_tran_template_config p ON t.parent_id = p.id  and p.is_delete=0 ");
        sql.append(" WHERE t.is_delete = '0' ");
        sql.append("   AND ( ");
        for (int i = 0; i < voList.size(); i++) {
            FexTranTemplateConfigDetailVO vo = voList.get(i);
            if (i > 0) {
                sql.append(" OR ");
            }
            sql.append(" (t.bank_type_id = :bankTypeId").append(i);
            sql.append(" AND t.bif_id = :bifId").append(i);
            sql.append(" AND t.country_id = :countryId").append(i);
            sql.append(" AND t.pay_type_id = :payTypeId").append(i);
            sql.append(") ");
            param.put("bankTypeId" + i, vo.getBankTypeId());
            param.put("bifId" + i, vo.getBifId());
            param.put("countryId" + i, vo.getCountryId());
            param.put("payTypeId" + i, vo.getPayTypeId());
        }
        sql.append(" ) ");
        return listBySQLAliasToBean(sql.toString(), param, FexTranTemplateConfigDetailVO.class);
    }

    /**
     *
     * 查询模板信息
     *
     * @param fexTranTemplateConfigParam
     * @return
     */
    public FexTranTemplateConfigVO getTemplateConfig(FexTranTemplateConfigVO fexTranTemplateConfigParam) {
        StringBuilder sql = new StringBuilder();
        sql.append(" select f.id,p.template_no templateNo,p.template_name templateName,f.parent_id parentId,f.bank_type_id bankTypeId,");
        sql.append(" f.country_id countryId,f.bif_id bifId,f.sort sort,f.valid_sign validSign,f.pay_type_id payTypeId,");
        sql.append(" scc.NAME_ZH countryZhName,bbt.type_name bankTypeName,bbi.BIF_CODE bifName, bbtc.custom_name  payTypeName ,bbtc.custom_code   bankPayType ");
        sql.append("FROM fex_tran_template_config f ");
        sql.append("left join fex_tran_template_config p on p.id=f.parent_id and p.is_delete=0  ");
        sql.append(getCountryJoinSql("f"));
        sql.append("left join bt_bank_type bbt on bbt.id=f.bank_type_id " );
        sql.append("left join bis_bif_init bbi on bbi.id=f.bif_id " );
        sql.append("left join bt_bank_type_custom bbtc on bbtc.id=f.pay_type_id " );
        sql.append("WHERE f.parent_id IS not null and f.is_delete=0 ");
        sql.append(" AND f.bank_type_id = :bankTypeId");
        sql.append(" AND f.bif_id = :bifId");
        sql.append(" AND scc.id = :countryId");
        sql.append(" AND f.pay_type_id = :payTypeId");
        Map<String, Object> param = new HashMap<>();
        param.put("bankTypeId", fexTranTemplateConfigParam.getBankTypeId());
        param.put("bifId", fexTranTemplateConfigParam.getBifId());
        param.put("countryId", fexTranTemplateConfigParam.getCountryId());
        param.put("payTypeId", fexTranTemplateConfigParam.getPayTypeId());
        return getBySQLAliasToBean(sql.toString(),param,FexTranTemplateConfigVO.class);
    }



    public FexTranTemplateConfigVO getTemplateConfigById(String id) {
        StringBuilder sql = new StringBuilder();
        sql.append(" select f.id,p.template_no templateNo,f.template_name templateName,p.template_name templateNameParent,f.parent_id parentId,f.bank_type_id bankTypeId,");
        sql.append(" f.country_id countryId,f.bif_id bifId,f.sort sort,f.valid_sign validSign,f.pay_type_id payTypeId,");
        sql.append(" scc.NAME_ZH countryZhName,bbt.type_name bankTypeName,bbi.BIF_CODE bifName, bbtc.custom_name  payTypeName, bbtc.custom_code as  bankPayType ");
        sql.append("FROM fex_tran_template_config f ");
        sql.append("left join fex_tran_template_config p on p.id=f.parent_id and p.is_delete=0  ");
        sql.append(getCountryJoinSql("f"));
        sql.append("left join bt_bank_type bbt on bbt.id=f.bank_type_id " );
        sql.append("left join bis_bif_init bbi on bbi.id=f.bif_id " );
        sql.append("left join bt_bank_type_custom bbtc on bbtc.id=f.pay_type_id " );
        sql.append("WHERE f.parent_id IS not null and f.is_delete=0 ");
        sql.append(" AND f.id = :id");
        Map<String, Object> param = new HashMap<>();
        param.put("id", id);
        List<FexTranTemplateConfigVO> fexTranTemplateConfigVOS = listBySQLAliasToBean(sql.toString(), param, FexTranTemplateConfigVO.class);
        if(CollectionUtil.isNotEmpty(fexTranTemplateConfigVOS)){
            return fexTranTemplateConfigVOS.get(0);
        }
        return null;
    }



    public DataTablesOutput<FexTranTemplateConfigVO> listOfParentPage(DataTablesInput dataTablesInput) {
        StringBuilder sql = new StringBuilder();
        sql.append(" select p.id,p.template_no templateNo,p.template_name templateName, p.valid_sign validSign " +
                "FROM fex_tran_template_config p " +
                "left join fex_tran_template_config c on p.id=c.parent_id   and c.is_delete=0 " +
                getCountryJoinSql("c") +
                "left join bt_bank_type bbt on bbt.id=c.bank_type_id " +
                "left join bis_bif_init bbi on bbi.id=c.bif_id " +
                "left join bt_bank_type_custom bbtc on bbtc.id=c.pay_type_id " +
                "WHERE p.parent_id IS  null and p.is_delete=0  and p.valid_sign=1 ");
        StringBuilder joinSql = new StringBuilder();
        Map<String,Object> param= dataTablesInput.getFormdata();
        Object parentIdoB=  param.get("parentId");
        if (!EmptyUtils.isEmpty(parentIdoB)) {
            String parentId= (String) parentIdoB;
            joinSql.append(" AND p.parent_id = :parentId");
            param.put("parentId", parentId);
        }

        Object payTypeNameOb= param.get("payTypeName");
        if (!EmptyUtils.isEmpty(payTypeNameOb)) {
            String payTypeName= (String) payTypeNameOb;
            if (!EmptyUtils.isEmpty(payTypeName)) {
                joinSql.append(" AND bbtc.custom_name like '%" +payTypeName + "%'");
                joinSql.append(" AND bbtc.custom_code like '%" +payTypeName + "%'");
            }
        }

        Object templateNameOb= param.get("templateName");
        if (!EmptyUtils.isEmpty(templateNameOb)) {
            String templateName= (String) templateNameOb;
            if (!EmptyUtils.isEmpty(templateName)) {
                joinSql.append(" AND p.template_name like '%" +templateName + "%'");
            }
        }
        Object templateNoOb= param.get("templateNo");
        if (!EmptyUtils.isEmpty(templateNoOb)) {
            String templateNo= (String) templateNoOb;
            if (!EmptyUtils.isEmpty(templateNo)) {
                joinSql.append(" AND p.template_no like '%" +templateNo + "%'");
            }
        }
        Object bifIdsOb= param.get("bifIds");
        if (!EmptyUtils.isEmpty(bifIdsOb)) {
            List<String>  bifIds= (List<String>)bifIdsOb ;
            joinSql.append(" and (" + getSQLIn(bifIds, 900, "c.bif_id") + ")");
        }

        Object bankTypeIdsOb=   param.get("bankTypeIds");
        if (!EmptyUtils.isEmpty(bankTypeIdsOb)) {
            List<String>  bankTypeIds= (List<String>)bankTypeIdsOb ;
                joinSql.append(" and (" + getSQLIn(bankTypeIds, 900, "c.bank_type_id") + ")");

        }
        Object  countryIdsOb=  param.get("countryIds");
        if (!EmptyUtils.isEmpty(countryIdsOb)) {
            List<String> countryIds= (List<String>)countryIdsOb ;
                joinSql.append(" and (" + getSQLIn(countryIds, 900, "scc.id") + ")");
        }
        if (StringUtils.isNotEmpty(joinSql)) {
            sql.append(joinSql);
        }
        sql.append(" group by p.id,p.template_no,p.template_name, p.valid_sign ");
        sql.append(" order by p.template_no asc");
        return getDynamicBySQL(dataTablesInput, sql.toString(), FexTranTemplateConfigVO.class, "p", false);
    }



    public List<FexTranTemplateConfigVO> selectByIds(List<String> ids) {
        if(EmptyUtils.isEmpty(ids)){
            return new ArrayList<>();
        }
        StringBuilder joinSql = new StringBuilder("select   id, parent_id, template_no, template_name, template_desc, remark, bank_type_id, bif_id, country_id, pay_type_id, valid_sign, is_delete, create_by, create_date, update_by, update_date, sort from  fex_tran_template_config where is_delete=0 ");
        if (EmptyUtils.isNotEmpty(ids)) {
            joinSql.append(" and " + getSQLIn(ids, 900, "id") );
        }
        return listBySQLAliasToBean(joinSql.toString(),null,FexTranTemplateConfigVO.class);
    }


    /**
     * 通过内管国家的系统国家 ID，关联到企业端国家数据。
     */
    private String getCountryJoinSql(String templateAlias) {
        return "inner join sys_country_cust_manager cm on cm.id=" + templateAlias + ".country_id "
                + "inner join sys_country sc on sc.id=cm.sys_id "
                + "inner join sys_country_cust scc on scc.sys_id=sc.id ";
    }


}
