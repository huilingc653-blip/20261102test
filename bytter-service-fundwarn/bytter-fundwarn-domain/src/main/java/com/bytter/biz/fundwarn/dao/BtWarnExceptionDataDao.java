package com.bytter.biz.fundwarn.dao;

import com.bytter.biz.fundwarn.entity.BtWarnData;
import com.bytter.biz.fundwarn.entity.BtWarnExceptionData;
import com.bytter.biz.fundwarn.vo.BtWarnDataInfoVO;
import com.bytter.biz.fundwarn.vo.BtWarnExceptionDataVO;
import com.bytter.core.base.BaseDao;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.utils.SqlUtil;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
* @Description 异常告警DAO
* @author zhous
* @date 2024/8/21 16:52
*/
@Repository
public class BtWarnExceptionDataDao extends BaseDao<BtWarnExceptionData,String> {

    public DataTablesOutput getDataTables(DataTablesInput dataTablesInput) {
        Map<String, Object> params=new HashMap<>();
        StringBuilder sql=new StringBuilder();
        sql.append("select a.id as id,a.send_date as sendDate,a.channel as channel,a.warn_type as warnType,");
        sql.append("a.warn_content as warnContent,a.req_msg as reqMsg,a.resp_msg as respMsg,a.send_status as sendStatus ");
        sql.append("from bt_warn_exception_data a ");
        sql.append(" where 1=1 ");
        return getDynamicBySQL(dataTablesInput, sql.toString(), BtWarnExceptionDataVO.class, null, false, params);
    }
}
