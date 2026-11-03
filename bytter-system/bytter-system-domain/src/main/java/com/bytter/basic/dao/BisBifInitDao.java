package com.bytter.basic.dao;
import com.bytter.basic.entity.BisBifInit;
import com.bytter.basic.vo.BisBifInitVO;
import com.bytter.core.base.BaseDao;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.utils.EmptyUtils;
import org.springframework.stereotype.Repository;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
 * @author onlineGenerator
 * @version 1.0
 * @Title: Dao
 * @Description: 接口代码维护
 * @date 2017-05-19 17:07:12
 */
@Repository
public class BisBifInitDao extends BaseDao<BisBifInit, String> {
    /**
     * 根据接口id查找
     *
     * @param id
     * @return
     */
    public BisBifInit getById(String id) {
        StringBuilder hql = new StringBuilder(" from BisBifInit where validSign = 1 and id = :id ");
        Map<String, Object> params = new HashMap();
        params.put("id", id);
        List<BisBifInit> list = this.listByHQL(hql.toString(), params);
        if (EmptyUtils.isEmptyList(list)) {
            return null;
        }
        return list.get(0);
    }

    /**
     * 根据ID获取有效接口信息。
     * @param ids ID集合。
     * @return 接口信息。
     */
    public List<BisBifInit> listByIds(List<String> ids){
        String hql = "from BisBifInit where validSign = 1 and id in (:id)";
        Map<String, Object> params = new HashMap();
        params.put("id", ids);
        return this.listByHQL(hql,params);
    }

    /**
     * 根据code获取有效接口信息。
     *
     * @param codeList Code集合。
     * @return 接口信息。
     */
    public List<BisBifInit> listByCode(List<String> codeList) {
        String hql = "from BisBifInit where validSign = 1 and bifCode in (:bifCode)";
        Map<String, Object> params = new HashMap<>();
        params.put("bifCode", codeList);
        return this.listByHQL(hql, params);
    }

    /**
     * 根据银行类型ID查询有效的接口代码
     *
     * @param validSign
     * @param bankTypeId
     * @return
     */
    public List<BisBifInit> listBySignOrBankType(String validSign, String bankTypeId) {
        String hql = " from BisBifInit where validSign=:validSign and bankTypeId=:bankTypeId";
        Map<String, Object> params = new HashMap();
        params.put("validSign", validSign);
        params.put("bankTypeId", bankTypeId);
        return listByHQL(hql, params);
    }
    /**
     * 根据code查询接口代码
     *
     * @param bifCode
     * @return
     */
    public List<BisBifInit> listByBifCode(String bifCode) {
        List<BisBifInit> list = listBy("bifCode", bifCode);
        return list;
    }
    /**
     * 查询列表
     *
     * @param dataTablesInput
     * @return
     */
    public DataTablesOutput getDataTablesUnion(DataTablesInput dataTablesInput) {
        StringBuilder sql = new StringBuilder();
        sql.append("select bbi.id as id,bbi.bif_Code as bifCode,bbi.name as name,bbi.bank_Type_Id as bankTypeId,");
        sql.append(" bbi.valid_Sign as validSign,bbi.update_By as updateBy,bbi.update_Date as updateDate,");
        sql.append(" bbi.create_By as createBy,bbi.create_Date as createDate,bbi.is_system as isSystem");
        sql.append(" ,bbi.is_domestic_bank as isDomesticBank,bbi.IS_SUPPORT_BANK_FOREIGN as isSupportBankForeign, ");
        sql.append(" bbi.dis_Connection_Alert as disConnectionAlert");
        sql.append(" from Bis_Bif_Init bbi where 1=1 ");
        return getDynamicBySQL(dataTablesInput, sql.toString(), BisBifInitVO.class, "bbi", false);
    }
    /**
     * 根据接口代码查找
     *
     * @param bifCode 接口代码
     * @return
     */
    public BisBifInit getByBifCode(String bifCode) {
        StringBuilder hql = new StringBuilder(" from BisBifInit where validSign = 1 and bifCode = :bifCode ");
        Map<String, Object> params = new HashMap();
        params.put("bifCode", bifCode);
        List<BisBifInit> list = this.listByHQL(hql.toString(), params);
        if (EmptyUtils.isEmptyList(list)) {
            return null;
        }
        return list.get(0);
    }
    /**
     * 根据接口代码名称查找
     *
     * @param name 接口代码名称
     * @return
     */
    public BisBifInit getByName(String name) {
        StringBuilder hql = new StringBuilder(" from BisBifInit where validSign = 1 and name = :name ");
        Map<String, Object> params = new HashMap();
        params.put("name", name);
        List<BisBifInit> list = this.listByHQL(hql.toString(), params);
        if (EmptyUtils.isEmptyList(list)) {
            return null;
        }
        return list.get(0);
    }
    /**
     * 根据账号过滤银行类别
     *
     * @return
     */
    public List<BisBifInit> listBisBifInitFilterByBankAcc() {
        StringBuilder sql = new StringBuilder();
        sql.append(" select distinct t.id,t.bif_code,t.name,t.bank_type_id,t.valid_sign ");
        sql.append(" from bis_bif_init t where t.valid_sign = '1'");
        sql.append(" and t.id in (select bif_code from bt_bank_acc)");
        sql.append(" order by t.bif_code ");
        return listBySQLAliasToBean(sql.toString(), null, BisBifInit.class);
    }

    /**
     * 获取去除bankTypeId重复的银行信息
     *
     * @return
     */
    public List<BisBifInit> listBisBifInit() {
        StringBuilder sql = new StringBuilder();
        sql.append(" select max(t.id) as id,max(t.bif_code) as bifCode,max(t.name) as name,t.bank_type_id,max(t.valid_sign) as validSign ");
        sql.append(" from bis_bif_init t where t.valid_sign = '1' group by t.bank_type_id ");

        return listBySQLAliasToBean(sql.toString(), null, BisBifInit.class);
    }

    /**
    * @Description 获取银行类型的直连银行为'是'的银行接口信息
    * @author zhous
    * @date 2024/8/22 17:18
    */
    public List<BisBifInit> getDirectConnectBankType() {
        StringBuilder sql = new StringBuilder();
        sql.append(" select bbi.id,bbi.name ");
        sql.append(" from bis_bif_init bbi ");
        sql.append(" left join bt_bank_type bbt on bbi.BANK_TYPE_ID  = bbt.id  ");
        sql.append(" where bbt.WHETHER_CONNECT_BANK = '1' and bbt.VALID_SIGN ='1' ");
        return listBySQLAliasToBean(sql.toString(), null, BisBifInit.class);
    }
}
