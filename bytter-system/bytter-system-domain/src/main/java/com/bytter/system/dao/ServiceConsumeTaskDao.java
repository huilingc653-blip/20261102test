package com.bytter.system.dao;
import com.bytter.system.entity.SysTask;
import com.bytter.core.base.BaseDao;
import com.bytter.core.common.page.IPageResult;
import com.bytter.core.common.page.IParameterInput;
import org.springframework.stereotype.Repository;
import java.util.HashMap;
/**
 * @author onlineGenerator
 * @version 1.0
 * @Title: Dao
 * @Description: SYS_TASK
 * @date 2017-06-19 18:44:40
 */
@Repository
public class ServiceConsumeTaskDao extends BaseDao<SysTask, String> {
    public IPageResult<SysTask> listConsumeTask(IParameterInput input) {
        HashMap<String, Object> map = new HashMap<>();
        StringBuilder hql = new StringBuilder();
        hql.append("select st.id as id, ");
        hql.append(" st.taskname as taskname, ");
        hql.append(" st.active as active, ");
        hql.append(" st.deletedsw as deletedsw, ");
        hql.append(" st.type as type, ");
        hql.append(" st.command as command, ");
        hql.append(" st.arguments as arguments, ");
        hql.append(" st.period as period, ");
        hql.append(" st.second as second, ");
        hql.append(" st.minute as minute, ");
        hql.append(" st.hour as hour ");
        hql.append(" st.days as days ");
        hql.append(" st.month as month ");
        hql.append(" st.week as week ");
        hql.append(" st.startdtm as startdtm ");
        hql.append(" st.enddtm as enddtm ");
        hql.append(" st.taskowner as taskowner ");
        hql.append(" st.emailReminder as emailReminder ");
        hql.append(" st.createBy as createBy ");
        hql.append(" st.createDate as createDate ");
        hql.append(" st.updateBy as updateBy ");
        hql.append(" st.updateDate as updateDate ");
        hql.append("from SysTask st where 1=1 and type = 3 ");
        return getPageResultByHQL(input, hql.toString(), map, SysTask.class);
    }
}
