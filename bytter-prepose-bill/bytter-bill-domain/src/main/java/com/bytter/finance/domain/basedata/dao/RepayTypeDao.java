package com.bytter.finance.domain.basedata.dao;
import com.bytter.core.base.BaseDao;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.constant.CommonConstant;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.finance.constant.BaseDataConstant;
import com.bytter.finance.domain.basedata.entity.RepayType;
import com.bytter.finance.domain.basedata.vo.RepayTypeVO;
import org.springframework.stereotype.Repository;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
 * @author onlineGenerator
 * @version 1.0
 * @Title: Dao
 * @Description: CMS_REPAY_TYPE
 * @date 2022-08-10 14:32:39
 */
@Repository
public class RepayTypeDao extends BaseDao<RepayType, String> {
    /**
     * 查询还款方式
     ***/
    public DataTablesOutput getRepayType(DataTablesInput input) {
        StringBuilder sql = new StringBuilder();
        sql.append(
                " select t.id, t.repay_code as repayCode, t.repay_name as repayName, t.repay_class as repayClass, t.capital_frequency_type as capitalFrequencyType, ");
        sql.append(
                " t.capital_frequency_next as capitalFrequencyNext, t.capital_frequency_num as capitalFrequencyNum, t.capital_frequency_detail as capitalFrequencyDetail, ");
        sql.append(
                " t.interest_frequency_type as interestFrequencyType, t.interest_frequency_next as interestFrequencyNext, t.interest_frequency_num as interestFrequencyNum, ");
        sql.append(" t.is_frequency_same as isFrequencySame, t.capital_and_interest as capitalAndInterest, ");
        sql.append(" t.rmk as rmk, t.valid_sign as validSign, t.status as status ");
        sql.append(" from cms_repay_type t where t.status <> " + BaseDataConstant.STATUS_DELETE);
        return getDynamicBySQL(input, sql.toString(), RepayTypeVO.class, "t", false, null);
    }

    /**
     * 功能说明：根据类型id查询还款方式1
     *
     * @param id
     */
    public RepayTypeVO getRepayTypeById(String id) {
        StringBuilder sql = new StringBuilder();
        sql.append(" select t.id, t.repay_code as repayCode, t.repay_name as repayName, t.repay_class as repayClass, t.capital_frequency_type as capitalFrequencyType, ");
        sql.append(" t.capital_frequency_next as capitalFrequencyNext, t.capital_frequency_num as capitalFrequencyNum, t.capital_frequency_detail as capitalFrequencyDetail, ");
        sql.append(" t.interest_frequency_type as interestFrequencyType, t.interest_frequency_next as interestFrequencyNext, t.interest_frequency_num as interestFrequencyNum, ");
        sql.append(" t.is_frequency_same as isFrequencySame, t.capital_and_interest as capitalAndInterest, ");
        sql.append(" t.rmk as rmk, t.valid_sign as validSign, t.status as status ");
        sql.append(" from cms_repay_type t where t.id=:id ");
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("id", id);
        return getBySQLAliasToBean(sql.toString(), map, RepayTypeVO.class);
    }

    /**
     * 删除还款方式
     */
    public int deleteRepayType(String id) {
        String sql = "update cms_repay_type set status =:status where id=:id";
        Map<String, Object> param = new HashMap<String, Object>();
        param.put("id", id);
        param.put("status", BaseDataConstant.STATUS_DELETE);
        return createSQLQuery(sql, param).executeUpdate();
    }
    /**
     * 查询删除类型是否被引用
     **/
    public BigDecimal judgeRepayTypeByCode(String id) {
        StringBuilder sql = new StringBuilder();
        sql.append("select count(1)  ");
        sql.append("from (  ");
        sql.append("         select info.REPAY_TYPE repayType  ");
        sql.append("         from CMS_PROVIDE_LOAN_INFO info  ");
        sql.append("         where info.REPAY_TYPE = :id  ");
        sql.append("               and info.status <> :status");
        sql.append("         union  ");
        sql.append("         select send.REPAY_TYPE repayType  ");
        sql.append("         from CMS_PROVIDE_LOAN_SEND send  ");
        sql.append("         where send.REPAY_TYPE = :id  ");
        sql.append("               and send.status <> :status");
        sql.append("     ) t ");
        Map<String, Object> param = new HashMap<String, Object>();
        param.put("id", id);
        param.put("status", BaseDataConstant.STATUS_DELETE);
        return (BigDecimal) createSQLQuery(sql.toString(), param).uniqueResult();
    }
    /**
     * 查询类型编号是否存在
     **/
    public boolean judgeTypeByCode(String repayCode, String repayName, String id) {
        String sql = "select id,repay_code as repayCode,repay_name as repayName from cms_repay_type where (repay_code = :code or repay_name = :name) and status <> -2";
        Map<String, Object> param = new HashMap<String, Object>();
        param.put("code", repayCode);
        param.put("name", repayName);
        List<RepayType> infoList = listBySQLAliasToBean(sql, param, RepayType.class);
        if (infoList != null && infoList.size() > 0) {
            if (EmptyUtils.isNotEmpty(id)) {// 修改
                for (RepayType repayType : infoList) {
                    if (repayType.getId().equals(id)) {// 未修改值 提交
                        return true;
                    }
                }
            }
            return false;
        }
        return true;
    }
    /**
     * 功能说明：根据类型code查询还款方式
     *
     * @param repayCode
     */
    public RepayTypeVO getRepayTypeByCode(String repayCode) {
        StringBuilder sql = new StringBuilder();
        sql.append(
                " select t.id, t.repay_code as repayCode, t.repay_name as repayName, t.repay_class as repayClass, t.capital_frequency_type as capitalFrequencyType, ");
        sql.append(
                " t.capital_frequency_next as capitalFrequencyNext, t.capital_frequency_num as capitalFrequencyNum, t.capital_frequency_detail as capitalFrequencyDetail, ");
        sql.append(
                " t.interest_frequency_type as interestFrequencyType, t.interest_frequency_next as interestFrequencyNext, t.interest_frequency_num as interestFrequencyNum, ");
        sql.append(" t.is_frequency_same as isFrequencySame, t.capital_and_interest as capitalAndInterest, ");
        sql.append(" t.rmk as rmk, t.valid_sign as validSign, t.status as status ");
        sql.append(" from cms_repay_type t where t.repay_code=:repayCode ");
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("repayCode", repayCode);
        return getBySQLAliasToBean(sql.toString(), map, RepayTypeVO.class);
    }
    /**
     * 功能说明：根据类型名称查询还款方式
     *
     * @param repayName
     */
    public RepayTypeVO getRepayTypeByName(String repayName) {
        StringBuilder sql = new StringBuilder();
        sql.append(
                " select t.id, t.repay_code as repayCode, t.repay_name as repayName, t.repay_class as repayClass, t.capital_frequency_type as capitalFrequencyType, ");
        sql.append(
                " t.capital_frequency_next as capitalFrequencyNext, t.capital_frequency_num as capitalFrequencyNum, t.capital_frequency_detail as capitalFrequencyDetail, ");
        sql.append(
                " t.interest_frequency_type as interestFrequencyType, t.interest_frequency_next as interestFrequencyNext, t.interest_frequency_num as interestFrequencyNum, ");
        sql.append(" t.is_frequency_same as isFrequencySame, t.capital_and_interest as capitalAndInterest, ");
        sql.append(" t.rmk as rmk, t.valid_sign as validSign, t.status as status ");
        sql.append(" from cms_repay_type t where t.repay_name=:repayName ");
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("repayName", repayName);
        return getBySQLAliasToBean(sql.toString(), map, RepayTypeVO.class);
    }

    /**
     * 根据ID批量获取还款方式数据。
     *
     * @param ids 还款方式ID集合。
     * @return 还款方式集合。
     */
    public List<RepayTypeVO> listRepayTypeByIds(String... ids) {
        StringBuilder sql = new StringBuilder();
        sql.append(" select t.id, t.repay_code as repayCode, t.repay_name as repayName, t.repay_class as repayClass, t.capital_frequency_type as capitalFrequencyType, ");
        sql.append(" t.capital_frequency_next as capitalFrequencyNext, t.capital_frequency_num as capitalFrequencyNum, t.capital_frequency_detail as capitalFrequencyDetail, ");
        sql.append(" t.interest_frequency_type as interestFrequencyType, t.interest_frequency_next as interestFrequencyNext, t.interest_frequency_num as interestFrequencyNum, ");
        sql.append(" t.is_frequency_same as isFrequencySame, t.capital_and_interest as capitalAndInterest, ");
        sql.append(" t.rmk as rmk, t.valid_sign as validSign, t.status as status ");
        sql.append(" from cms_repay_type t where t.id in(:repayIds) ");
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("repayIds", ids);
        return listBySQLAliasToBean(sql.toString(), map, RepayTypeVO.class);
    }

    /**
     * 查询所有非删除状态有效还款方式
     *
     * @return
     */
    public List<RepayTypeVO> listRepayTypeVO() {
        StringBuilder sql = new StringBuilder();
        sql.append(" select t.*  ");
        sql.append(" from cms_repay_type t ");
        sql.append(" where 1=1 ");
        sql.append(" and t.status <> :status and t.valid_sign = :validSign ");
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("status", BaseDataConstant.STATUS_DELETE);
        params.put("validSign", CommonConstant.YES);
        return this.listBySQLAliasToBean(sql.toString(), params, RepayTypeVO.class);
    }

    /**
     * 查询所有非删除状态有效还款方式(无随借随还)
     * @return
     */
    public List<RepayTypeVO> listNoNRepayTypeVO() {
        StringBuilder sql = new StringBuilder();
        sql.append(" select t.*  ");
        sql.append(" from cms_repay_type t ");
        sql.append(" where 1=1 ");
        sql.append(" and t.status <> :status and t.valid_sign = :validSign ");
        sql.append(" and t.CAPITAL_FREQUENCY_TYPE<>'N'");
        sql.append(" and (t.INTEREST_FREQUENCY_TYPE<>'N' or t.INTEREST_FREQUENCY_TYPE is null)");
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("status", BaseDataConstant.STATUS_DELETE);
        params.put("validSign", CommonConstant.YES);
        return this.listBySQLAliasToBean(sql.toString(), params, RepayTypeVO.class);
    }

    /**
     * 查询所有非删除状态有效还款方式(无本金与利息无关随借随还)
     * @return
     */
    public List<RepayTypeVO> listNoTRepayTypeVO() {
        StringBuilder sql = new StringBuilder();
        sql.append(" select t.*  ");
        sql.append(" from cms_repay_type t ");
        sql.append(" where 1=1 ");
        sql.append(" and t.status <> :status and t.valid_sign = :validSign ");
        sql.append(" and not exists (select 1 from cms_repay_type ty where ty.CAPITAL_AND_INTEREST = '3' ");
        sql.append(" and (ty.CAPITAL_FREQUENCY_TYPE = 'N' or ty.INTEREST_FREQUENCY_TYPE<>'N') and ty.id=t.id)");
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("status", BaseDataConstant.STATUS_DELETE);
        params.put("validSign", CommonConstant.YES);
        return this.listBySQLAliasToBean(sql.toString(), params, RepayTypeVO.class);
    }
}
