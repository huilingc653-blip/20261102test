package com.bytter.system.dao;
import com.bytter.system.entity.LoginLog;
import com.bytter.system.vo.LoginLogVO;
import com.bytter.core.base.BaseDao;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.datatable.OrderString;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.core.utils.SqlUtil;
import com.google.common.collect.Maps;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import java.util.List;
import java.util.Map;
@Repository
public class LoginLogDao extends BaseDao<LoginLog, String> {
    public DataTablesOutput listLoginLog(DataTablesInput input, List<String> userCorpIds, String userId) {
        Map<String, Object> map = Maps.newHashMap();
        StringBuffer sql = new StringBuffer();
        String startDate = (String) input.getFormdata().get("ged_loginDate");
        String endDate = (String) input.getFormdata().get("led_loginDate");
        Object onlyUser = input.getFormdata().get("onlyUser");
        if (StringUtils.hasText(startDate)) {
            startDate = startDate + " 00:00:00";
            input.getFormdata().put("ged_loginDate", startDate);
        }
        if (StringUtils.hasText(endDate)) {
            endDate = endDate + " 23:59:59";
            input.getFormdata().put("led_loginDate", endDate);
        }
        if (EmptyUtils.isEmptyList(input.getOrder())) {
            input.setOrderString(new OrderString("s.loginDate", "desc"));
        }
        sql.append("select * from ( select u.ID as id, ");
        sql.append(" u.USERNAME as username, ");
        sql.append(" u.corp_id, ");
        sql.append(" ul.TYPE as type, ");
        sql.append(" ul.HOST as host, ");
        sql.append(" ul.USER_AGENT as userAgent, ");
        sql.append(" ul.LOGIN_DATE as loginDate, ");
        sql.append(" ul.LOGOUT_DATE as logoutDate, ");
        sql.append(" ul.REASON as REASON, ");
        sql.append(" ul.STATUS as status ");
        sql.append("from SYS_USER_LOGIN_LOG ul left join SYS_USER u on u.LOGIN_NAME=ul.LOGIN_NAME) s  where 1=1 ");
        if (onlyUser != null && StringUtils.hasText(userId)) {
            sql.append(" and s.id = :userId ");
            map.put("userId", userId);
        } else {
            sql.append(" and ( ");
            sql.append(" s.id = :userId ");
            map.put("userId", userId);
            if (!CollectionUtils.isEmpty(userCorpIds)) {
                sql.append(" or ").append(SqlUtil.getInSql(userCorpIds, "s.corp_Id", true));
            }
            sql.append(" ) ");
        }
        return dealDynamicBySQL(input, sql.toString(), LoginLogVO.class, "s", false, map);
    }
}
