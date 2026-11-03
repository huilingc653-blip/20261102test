package com.bytter.bill.domain.billPool.dao;

import com.bytter.bill.domain.billPool.entity.AbPoolPledgee;
import com.bytter.bill.api.billPool.constant.AbPoolConstant;
import com.bytter.bill.domain.billPool.vo.AbPoolPledgeeVO;
import com.bytter.core.base.BaseDao;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.utils.ListUtils;
import com.bytter.core.utils.StringUtils;
import org.hibernate.SQLQuery;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @version V1.0
 * @Title: 票据池质权人Dao
 * @Package com.bytter.biz.billPool.entity
 * @Description: TODO
 * @author: Jinyong.cao
 * @date: 2019-12-30 下午5:30:29
 */
@Repository
public class AbPoolPledgeeDao extends BaseDao<AbPoolPledgee, String> {

    protected static StringBuilder publicSqlVoid() {
        return new StringBuilder().append("select ")
                .append(" ct.id as id,ap.id as pId,")
                .append(" ap.bp_code as bpCode,ap.bp_Name as bpName,")
                .append(" ct.BP_ID as bpId,ct.BANK_ACC as bankAcc,")
                .append(" ct.ACC_NAME as accName,")
                .append(" ct.BANK_NO as bankNo,")
                .append(" ct.BANK_NAME as bankName,")
                .append("   (case   when ct.VALID_SIGN is null  then  '0' else ct.VALID_SIGN end)  as validSign,")
                .append(" ct.rmk as rmk,ap.bp_Type as bpType")
                .append(" from AB_POOL ap left join AB_POOL_PLEDGEE ct on(ap.id = ct.bp_id) ")
                .append(" WHERE 1=1 ");
    }

    public DataTablesOutput<AbPoolPledgeeVO> listAbPoolPledgee(DataTablesInput input, List<String> corpAuthList) {
        Map<String, Object> params = new HashMap<String, Object>();
        StringBuilder sql = new StringBuilder(publicSqlVoid());
        sql.append(" and ap.VALID_SIGN=1");
        if (StringUtils.isNotEmpty(input.getFormdata().get("eqs_validSign"))) {
            String validSign = String.valueOf(input.getFormdata().get("eqs_validSign"));
            if (validSign.equals("1")) {
                sql.append(" and ct.VALID_SIGN=1");
            } else if (validSign.equals("0")) {
                sql.append(" and (ct.VALID_SIGN=0 or ct.VALID_SIGN is null)");
            }
            input.getFormdata().remove("eqs_validSign");
        }
        return this.getDynamicBySQL(input, sql.toString(), AbPoolPledgeeVO.class, "ct", false, params);
    }

    public AbPoolPledgeeVO getAbPoolPledgeeVOByBpId(String bpId) {
        StringBuilder hql = new StringBuilder(publicSqlVoid());
        hql.append(" and ap.id=:id");
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("id", bpId);
        List<AbPoolPledgeeVO> list = listBySQLAliasToBean(hql.toString(), params, AbPoolPledgeeVO.class);
        return null != list && list.size() > 0 ? list.get(0) : null;
    }

    public void updatePoolPledgeeVO(AbPoolPledgee abPoolPledgee) {
        if (null != abPoolPledgee) {
            if (StringUtils.isNotEmpty(abPoolPledgee.getId())) {
                this.update(abPoolPledgee);
            } else {
                abPoolPledgee.setValidSign(AbPoolConstant.AB_POLL_VALID_SIGN_0);
                this.save(abPoolPledgee);
            }
        }
    }

    public boolean checkRepeat(AbPoolPledgeeVO abPoolPledgeeVO) {
        StringBuilder sql = new StringBuilder("select ");
        sql.append(" ap.ID");
        sql.append(" from AB_POOL ap left join AB_POOL_PLEDGEE bta on(ap.id = bta.bp_id)");
        sql.append(" where 1=1");
        if (StringUtils.isNotBlank(abPoolPledgeeVO.getpId())) {
            sql.append(" and bta.bp_Id <> :id");
        }
        if (StringUtils.isNotBlank(abPoolPledgeeVO.getAccName())) {
            sql.append(" and bta.ACC_NAME=:accName");
        }
        SQLQuery query = createSQLQuery(sql.toString());
        if (StringUtils.isNotBlank(abPoolPledgeeVO.getpId())) {
            query.setString("id", abPoolPledgeeVO.getpId());
        }
        if (!StringUtils.isBlank(abPoolPledgeeVO.getAccName())) {
            query.setString("accName", abPoolPledgeeVO.getAccName());
        }
        if (!query.list().isEmpty()) {
            return true;
        }
        return false;
    }


    public int updateEntityByParams(Map<String, Object> params) {
        StringBuilder hql = new StringBuilder("update AbPoolPledgee "
                + " set "
                + "updateTime = :updateTime ");
        if (params.containsKey("updateId")) {
            hql.append(",updateId = :updateId ");
        }
        if (params.containsKey("validSign")) {
            hql.append(",validSign = :validSign ");
        }
        hql.append(" where 1=1 ");
        if (params.containsKey("id")) {
            hql.append(" and id =:id ");
        }
        return this.batchExecute(hql.toString(), params);
    }


    public AbPoolPledgee findAbPoolPledgeeByBpId(String bpId) {
        String hql = "from AbPoolPledgee where bp_id=:bpId and validSign=:validSign";
        Map<String, Object> params = new HashMap<>();
        params.put("bpId", bpId);
        params.put("validSign", AbPoolConstant.AB_POLL_VALID_SIGN_1);
        List<AbPoolPledgee> list = this.listByHQL(hql, params);
        if (ListUtils.isNotNullOrEmpty(list)) {
            return list.get(0);
        }
        return null;
    }


    public AbPoolPledgeeVO getAbPoolPledgeeByBpId(String bpid) {
        StringBuilder hql = new StringBuilder(publicSqlVoid());
        hql.append(" and ap.id=:id");
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("id", bpid);
        List<AbPoolPledgeeVO> list = listBySQLAliasToBean(hql.toString(), params, AbPoolPledgeeVO.class);
        return ListUtils.isNotNullOrEmpty(list) ? list.get(0) : null;
    }


}
