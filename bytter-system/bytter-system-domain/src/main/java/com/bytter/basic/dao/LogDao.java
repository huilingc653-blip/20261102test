package com.bytter.basic.dao;

import com.bytter.basic.entity.BtLog;
import com.bytter.basic.vo.BtLogVO;
import com.bytter.core.base.BaseDao;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.datatable.OrderString;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.core.utils.SqlUtil;
import com.bytter.core.utils.StringUtils;
import com.google.common.collect.Maps;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Map;

/**
 * Created by zhouj on 2017/5/17.
 */
@Repository
public class LogDao extends BaseDao<BtLog, String> {

    public DataTablesOutput getLogList(DataTablesInput input, List<String> userCorpIds, String userId) {
        StringBuffer sb = new StringBuffer();
        String stime = input.getPara("startTime");
        String etime = input.getPara("endTime");
        if (StringUtils.isNotBlank(stime)) {
            stime = stime + " 00:00:00";
            input.getFormdata().put("ged_startTime", stime);
        }
        if (StringUtils.isNotBlank(etime)) {
            etime = etime + " 23:59:59";
            input.getFormdata().put("led_endTime", etime);
        }
        if (EmptyUtils.isEmptyList(input.getOrder())) {
            input.setOrderString(new OrderString("s.operatetime", "desc"));
        }
        sb.append("select s.loglevel as loglevel,to_char(s.operatetime,'YYYY-MM-DD HH24:MI:SS') as operatetime, ");
        sb.append(" s.operatetype as operatetype,s.logcontent as logcontent,s.broswer as broswer,s.note as note,s.userId as userId,u.username as username ");
        sb.append(" from SYS_LOG s left join SYS_USER u on s.userId=u.id  where 1 = 1 ");
        Map<String, Object> map = Maps.newHashMap();
        sb.append(" and ( ");
        sb.append(" u.id = :userId ");
        map.put("userId", userId);
        if (!CollectionUtils.isEmpty(userCorpIds)) {
            sb.append(" or ").append(SqlUtil.getInSql(userCorpIds, "u.corp_Id", true));
        }
        sb.append(" ) ");
        return dealDynamicBySQL(input, sb.toString(), BtLogVO.class, "s", false, map);
    }
}
