package com.bytter.check.dao;
import com.bytter.check.entity.FcAutoCheckResult;
import com.bytter.check.vo.FcAutoCheckResultVO;
import com.bytter.core.base.BaseDao;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.datatable.OrderString;
import org.springframework.stereotype.Repository;
@Repository
public class FcAutoCheckResultDao extends BaseDao<FcAutoCheckResult, String> {
    /**
     * 功能介绍 :查询自动对账结果信息
     */
    public DataTablesOutput getDataTable(DataTablesInput dataTablesInput) {
        StringBuilder sql = new StringBuilder();
        sql.append("select fr.id,fr.CORP_ID as corpId,fr.CORP_NAME as corpName,fr.BANK_ACC as bankAcc,fr.BANK_ACC_NAME as bankAccName,");
        sql.append(" fr.CHECK_TYPE as checkType,fr.RUN_TIME as runTime,fr.LAUNCH_TIME as launchTime,fr.CHECK_CYCLE as checkCycle,fr.AUTO_CHECK_CODE as autoCheckCode,");
        sql.append(" fr.AUTO_CHECK_NAME as autoCheckName,fr.IS_SUCCESS as isSuccess");
        sql.append(" from FC_AUTO_CHECK_RESULT fr ");
        sql.append(" where 1=1 ");
        OrderString orderString = new OrderString("fr.create_date", "desc");
        dataTablesInput.setOrderString(orderString);
        return getDynamicBySQL(dataTablesInput, sql.toString(), FcAutoCheckResultVO.class, null, false);
    }
}
