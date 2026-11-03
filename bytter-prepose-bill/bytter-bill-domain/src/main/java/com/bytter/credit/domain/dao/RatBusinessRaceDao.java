package com.bytter.credit.domain.dao;
import com.bytter.credit.domain.entity.RatBusinessRace;
import com.bytter.credit.domain.vo.RatBusinessRaceVO;
import com.bytter.core.base.BaseDao;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.system.api.dto.CorpDTO;
import org.springframework.stereotype.Repository;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
@Repository
public class RatBusinessRaceDao extends BaseDao<RatBusinessRace, String> {
    public DataTablesOutput<RatBusinessRaceVO> dataTable(DataTablesInput input) {
        StringBuilder sql = new StringBuilder();
        sql.append(
                " select b.id,c.type,t.id as ritId,t.rit_name as ritName,c.id as customId,c.name,b.race_name as raceName,b.race_name as busName ");
        sql.append(" from rat_business_race b ");
        sql.append(" inner join rat_item_custom c  on b.custom_id = c.id ");
        sql.append(" inner join rat_item_type t  on c.rit_code = t.id ");
        sql.append(" where 1 = 1");
        return getDynamicBySQL(input, sql.toString(), RatBusinessRaceVO.class, null, false);
    }
    /***
     * 根据授信业务种类名称查询是否有相同 -----
     */
    public List<RatBusinessRace> findAllBy(String id, String raceName) {
        Map<String, Object> params = new HashMap<String, Object>();
        StringBuilder sb = new StringBuilder();
        sb.append(" from RatBusinessRace where 1 = 1 and raceName=:raceName ");
        if (EmptyUtils.isNotEmpty(id)) {
            sb.append(" and id <> :id ");
            params.put("id", id);
        }
        params.put("raceName", raceName);
        return createQuery(sb.toString(), params).list();
    }
    /***
     * 根据授信品种id查询业务品种 --------
     */
    public List<RatBusinessRaceVO> findAllByCustomId(String customId) {
        Map<String, Object> params = new HashMap<String, Object>();
        StringBuilder sb = new StringBuilder();
        sb.append(" select race.id as id,race.custom_id as customId,race.race_name as raceName  ")
                .append(" from rat_business_race race where 1 = 1 and custom_id =:customId ");
        params.put("customId", customId);
        return listBySQLAliasToBean(sb.toString(), params, RatBusinessRaceVO.class);
    }
    public boolean isUsed(String customId, String raceId) {
        String sql = "select 1 from RAT_URATION where item_code = :customId and bill_name =:raceId ";
        Map<String, Object> param = new HashMap<String, Object>();
        param.put("customId", customId);
        param.put("raceId", raceId);
        List<String> list = listBySQL(sql, param);
        if (list.size() > 0) {
            return true;
        } else {
            return false;
        }
    }
}
