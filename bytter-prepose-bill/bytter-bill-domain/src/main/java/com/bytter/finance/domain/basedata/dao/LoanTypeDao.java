package com.bytter.finance.domain.basedata.dao;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.bytter.finance.domain.basedata.vo.LoanTypeVo;
import com.bytter.finance.constant.BaseDataConstant;
import com.bytter.finance.dto.LoanTypeDTO;
import org.springframework.stereotype.Repository;
import com.bytter.finance.domain.basedata.entity.LoanType;
import com.bytter.core.base.BaseDao;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.utils.EmptyUtils;
/**
 * @Title: 贷款类型维护dao
 * @Description: CMS_LOAN_TYPE
 * @author zhengxn
 * @date 2017-07-19 14:05:55
 * @version 1.0
 *
 */
@Repository
public class LoanTypeDao extends BaseDao<LoanType, String> {
    /**
     * 查询贷款类型
     *
     * @param type
     *            借款：BaseDataConstant.TYPE_BORROW 贷款：BaseDataConstant.TYPE_CREDIT
     ***/
    public DataTablesOutput queryLoanType(DataTablesInput input) {
        StringBuilder sql = new StringBuilder();
        sql.append(
                "select t.code,t.name,t.id,t.is_default as isDefault,t.status,t.type from cms_loan_type t where t.type="
                        + BaseDataConstant.TYPE_CREDIT + " and t.status<> " + BaseDataConstant.STATUS_DELETE);
        return getDynamicBySQL(input, sql.toString(), LoanTypeVo.class, "", false);
    }
    /** 修改贷款类型 ***/
    public int updateLoan(String id, String code, String name, String loginname, String username, String date,
            String type) {
        boolean isOk = judgeTypeByCode(code, name, type, id);
        StringBuilder hql = new StringBuilder();
        Map<String, Object> param = new HashMap<String, Object>();
        if (isOk) {
            hql.append("update LoanType ");
            hql.append(" set code =:code,name =:name,updateName =:username,");
            hql.append("updateBy =:loginname,updateDate=BT_TO_DATE(:date,'YYYY-MM-DD HH24:MI:SS')");
            hql.append("where id =:id");
            param.put("code", code);
            param.put("name", name);
            param.put("username", username);
            param.put("loginname", loginname);
            param.put("date", date);
            param.put("id", id);
        }
        return batchExecute(hql.toString(), param);
    }
    /**
     * 删除贷款类型
     *
     * @param status
     *            删除：BaseDataConstant.STATUS_DELETE
     */
    public int deleteLoan(String id) {
        String hql = "update LoanType set status =:status where id=:id";
        Map<String, Object> param = new HashMap<String, Object>();
        param.put("id", id);
        param.put("status", BaseDataConstant.STATUS_DELETE);
        return batchExecute(hql, param);
    }
    /** 查询删除类型是否被引用 **/
    public boolean judgeLoanTypeByCode(String code, String type) {
        String sql = null;
        if (BaseDataConstant.TYPE_BORROW.equals(type)) {
            sql = "select * from cms_provide_loan_info where pl_class=:code";
        } else {
            sql = "select * from cms_loan_info where loan_genre=:code";
        }
        Map<String, Object> param = new HashMap<String, Object>();
        param.put("code", code);
        List<LoanType> cmsLoanInfoList = listBySQL(sql, param);
        if (cmsLoanInfoList != null && cmsLoanInfoList.size() > 0) {
            return false;
        }
        return true;
    }
    /** 查询类型编号是否存在 **/
    public boolean judgeTypeByCode(String code, String name, String type, String id) {
        String sql = "select id,code,name from cms_loan_type where code=:code  and type=:type and status <> -2";
        Map<String, Object> param = new HashMap<String, Object>();
        param.put("code", code);
        param.put("type", type);
        List<LoanType> infoList = listBySQLAliasToBean(sql, param, LoanType.class);
        if (infoList != null && infoList.size() > 0) {
            if (EmptyUtils.isNotEmpty(id)) {// 修改
                for (LoanType loanType : infoList) {
                    if (loanType.getId().equals(id)) {// 未修改值 提交
                        return true;
                    }
                }
            }
            return false;
        }
        return true;
    }
    /** 查询借款类型 ***/
    public DataTablesOutput queryBorrowType(DataTablesInput input) {
        StringBuilder sql = new StringBuilder();
        sql.append(" select t.code,t.name,t.id,t.is_default as isDefault,t.status,t.type,c.NAME as cmsLoanTypeManagerName from cms_loan_type t ");
        sql.append(" left join CMS_LOAN_TYPE_MANAGER c on c.id = t.CMS_LOAN_TYPE_MANAGER_ID ");
        sql.append(" where t.type="+ BaseDataConstant.TYPE_BORROW + " and t.status<>" + BaseDataConstant.STATUS_DELETE);
        return getDynamicBySQL(input, sql.toString(), LoanTypeVo.class, "t", false);
    }
    /**
     * 功能说明：根据类型code查询借款/贷款类型
     *
     * @param code
     * @param type
     *            借款：BaseDataConstant.TYPE_BORROW 贷款：BaseDataConstant.TYPE_CREDIT
     */
    public LoanTypeDTO queryLoanTypeByCode(String code, String type) {
        StringBuilder sql = new StringBuilder();
        sql.append(
                "select code,name,id,is_default,status,type from cms_loan_type  where type=:type and code=:code and status <>:status ");
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("type", type);
        map.put("code", code);
        map.put("status", BaseDataConstant.STATUS_DELETE);
        return getBySQLAliasToBean(sql.toString(), map, LoanTypeDTO.class);
    }
    /**
     * 功能说明：根据类型id查询借款/贷款类型
     *
     * @param code
     * @param type
     *            借款：BaseDataConstant.TYPE_BORROW 贷款：BaseDataConstant.TYPE_CREDIT
     */
    public LoanTypeDTO queryLoanTypeById(String id, String type) {
        StringBuilder sql = new StringBuilder();
        sql.append("select code,name,id,is_default,status,type from cms_loan_type  where id=:id and type=:type");
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("id", id);
        map.put("type", type);
        return getBySQLAliasToBean(sql.toString(), map, LoanTypeDTO.class);
    }
    /**
     * 功能说明：根绝type查询借款/贷款类型
     *
     * @param type
     *            借款：BaseDataConstant.TYPE_BORROW 贷款：BaseDataConstant.TYPE_CREDIT
     */
    public List<LoanTypeDTO> queryLoanTypeList(String type) {
        StringBuilder sql = new StringBuilder();
        sql.append("select code,name,id,is_default,status,type from cms_loan_type  where status <> "
                + BaseDataConstant.STATUS_DELETE + " and type=:type order by code ");
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("type", type);
        return listBySQLAliasToBean(sql.toString(), params, LoanTypeDTO.class);
    }

    /**
     * 借款类型维护选择系统借款类型数据查询
     *
     * @param type
     * @return
     */
    public List<LoanTypeDTO> queryCmsLoanTypeManagerList(String type) {
        StringBuilder sql = new StringBuilder();
        sql.append("select code,name,id,is_default,status,type from CMS_LOAN_TYPE_MANAGER where status <> "
                + BaseDataConstant.STATUS_DELETE + " and type=:type order by code ");
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("type", type);
        return listBySQLAliasToBean(sql.toString(), params, LoanTypeDTO.class);
    }
}
