package com.bytter.biz.finacial.tally.dao;
import com.bytter.biz.finacial.api.dto.FcManualFinacialColumnDTO;
import com.bytter.biz.finacial.api.dto.FcManualFinacialTypeDTO;
import com.bytter.biz.finacial.tally.entity.FcManualFinacialColumn;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.system.api.dto.FcAccDTO;
import org.springframework.stereotype.Repository;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
@Repository
public class FcManualFinacialColumnDao extends BaseDao<FcManualFinacialColumn, String> {
    /**
     * 查询全部手工帐类型表
     *
     * @return 全部类型
     */
    public FcManualFinacialTypeDTO getType(String id) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("id", id);
        return this.getBySQLAliasToBean("select * from fc_manual_finacial_type where id = :id ", params, FcManualFinacialTypeDTO.class);
    }
    public FcManualFinacialTypeDTO getTypeBysysCodeAndFlow(String sysCode, String flowType) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("sysCode", sysCode);
        params.put("flowType", flowType);
        return this.getBySQLAliasToBean("select * from fc_manual_finacial_type where sys_race_code = :sysCode and flow_type = :flowType ", params, FcManualFinacialTypeDTO.class);
    }
    /**
     * 查询全部手工帐类型表
     *
     * @return 全部类型
     */
    public boolean checkTypeCode(String typeCode) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("typeCode", typeCode);
        List list = this.createSQLQuery("select * from fc_manual_finacial_type where sys_race_code = :typeCode ", params).list();
        return list != null && list.size() > 0;
    }
    /**
     * 分页查询
     *
     * @param dataTablesInput
     * @param user
     * @return
     */
    public DataTablesOutput<FcManualFinacialTypeDTO> getDataTablesAllType(DataTablesInput dataTablesInput, ICurrentUser user) {
        return this.getDynamicBySQL(dataTablesInput, "select * from fc_manual_finacial_type a ", FcManualFinacialTypeDTO.class, "a", true);
    }
    /**
     * 根据手工帐ID查询 回写列值对应 数据
     *
     * @param typeId 手工帐类型ID
     * @return 这个类型的 回写列值对应 数据
     */
    public List<FcManualFinacialColumnDTO> listColumnVoBySysRaceCode(String typeId) {
        Map<String, Object> params = new HashMap<String, Object>();
        String sql = "select col.* from fc_manual_finacial_column col" +
                " inner join fc_manual_finacial_type tp " +
                " on col.type_id = tp.id " +
                " where tp.id = :typeId" +
                " order by col.write_back_table ";
        params.put("typeId", typeId);
        return this.listBySQLAliasToBean(sql, params, FcManualFinacialColumnDTO.class);
    }
    /**
     * 根据类型Id删除 回写规则
     *
     * @param typeId
     */
    public void deleteColumnByTypeId(String typeId) {
        Map<String, Object> params = new HashMap<String, Object>();
        String sql = "delete from  fc_manual_finacial_column " +
                " where type_id = :typeId";
        params.put("typeId", typeId);
        this.createSQLQuery(sql, params).executeUpdate();
    }
    public void deleteTypeById(String id) {
        Map<String, Object> params = new HashMap<String, Object>();
        String sql = "delete from  fc_manual_finacial_type" +
                " where id = :id";
        params.put("id", id);
        this.createSQLQuery(sql, params).executeUpdate();
    }
    //TODO Fc_Acc\bt_currency\sys_corp 都是在基础模块
    public List<FcAccDTO> queryBySomeCondPgd(String sql) {
        String sb = " select t.blnctr_acc as blnctrAcc,t.acc_name as accName, " +
                " bc.cur_name as curName, f.subject_name as subjectName, t.shortCut as shortCut, " +
                " nvl(fl.online_balance,0) - nvl(fl.freeze_money,0) as tallyBalance, " +
                " nvl(fl.check_balance,0) - nvl(fl.freeze_money,0) as checkBalance " +
                " from Fc_Acc t left join fc_Subject f on t.subject_id = f.id " +
                " left join bt_currency bc on t.cur_id = bc.id " + //bt_currency 没有作用？
                " left join sys_corp s on s.id = t.corp_id " +  //sys_corp 没有什么作用？
                " left join fc_online_ledger fl on t.blnctr_acc = fl.account " +
                " left join bis_acc_attribute ba on t.attribute_id = ba.id " +
                " where 1 = 1 " +
                sql;
        //sb.append(" and (substr(ba.attribute_des, 2, 1) in ('2', '3') or substr(ba.attribute_des, 1, 1) in ('2', '3')) ");
        return listBySQLAliasToBean(sb, null, FcAccDTO.class);
    }
}
