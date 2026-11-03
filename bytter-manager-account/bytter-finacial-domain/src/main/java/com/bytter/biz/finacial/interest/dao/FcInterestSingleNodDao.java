package com.bytter.biz.finacial.interest.dao;
import com.bytter.biz.finacial.interest.entity.FcInterestSingle;
import com.bytter.biz.finacial.interest.vo.FcInterestSingleVO;
import com.bytter.core.base.BaseDao;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.core.utils.SqlUtil;
import com.bytter.biz.finacial.api.constant.InterestConstant;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import java.util.List;
/**
 * 企业通知存款个别计息Dao
 *
 * @author zuoy 2020-12-29
 */
@Component
public class FcInterestSingleNodDao extends BaseDao<FcInterestSingle, String> {
    public DataTablesOutput queryFcInterestSingleDda(DataTablesInput input, String userId, List<String> corpIds) {
        StringBuilder sql = new StringBuilder();
        /*sql.append("select * from (");
        sql.append("SELECT FIS.*,fa.CUR_ID,fa.NET_ID,fa.POOL_SIGN from FC_INTEREST_SINGLE fis ");
        sql.append(" LEFT JOIN FC_ACC fa on FIS.CORP_ID=fa.CORP_ID and FIS.ACCOUNT=Fa.BLNCTR_ACC");
        sql.append(") t where 1=1 ");*/
        sql.append("select * from (");
        sql.append("SELECT FIS.* from FC_INTEREST_SINGLE fis ");
        sql.append(") t where 1=1 ");
        //默认查询当前登录人所有管理单位
        if (!CollectionUtils.isEmpty(corpIds)) {
            sql.append(" and ").append(SqlUtil.getInSql(corpIds, "t.corp_Id", true));
        }
        //业务状态
        String eqs_status = (String) input.getFormdata().get("eqs_status");
        if (EmptyUtils.isEmpty(eqs_status)) {
            String opType = (String) input.getFormdata().get("opType");
            if (EmptyUtils.isNotEmpty(opType) && opType.equals("checkList")) {
                //复核界面数据只能复核他人经办数据
                sql.append(" and t.status = ").append(InterestConstant.INTEREST_WAIT_CHECK);
                input.getFormdata().put("nes_createBy", userId);
            } else {
                //经办-界面加载暂存、打回数据
                sql.append(" and t.status in(").append(InterestConstant.INTEREST_STATUE_TEMP).append(",").append(InterestConstant.INTEREST_BACK).append(")");
                //经办界面数据只能自己新增的数据
                input.getFormdata().put("eqs_createBy", userId);
            }
        }
        input.getFormdata().remove("opType");
        return getDynamicBySQL(input, sql.toString(), FcInterestSingleVO.class, "t", false);
    }
}
