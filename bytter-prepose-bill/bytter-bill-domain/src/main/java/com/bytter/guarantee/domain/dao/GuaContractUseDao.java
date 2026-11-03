package com.bytter.guarantee.domain.dao;
import com.bytter.guarantee.domain.entity.GuaContractOccup;
import com.bytter.guarantee.domain.vo.GuaContractUseDtlVO;
import com.bytter.guarantee.constant.GuaranteeConstant;
import com.bytter.core.base.BaseDao;
import org.springframework.stereotype.Repository;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
 * @author luxy
 * @Title: GuaContractUseDao
 * @ProjectName T3(3.2)
 * @Description: 被担保合同使用登记数据访问层
 * @date 2020/4/29 10:03
 */
@Repository
public class GuaContractUseDao extends BaseDao<GuaContractOccup, String> {
    /**
     * 获取担保合同使用登记数据
     * 
     * @param contractId
     * @return
     */
    public List<GuaContractUseDtlVO> getUseList(String contractId) {
        StringBuilder sql = new StringBuilder();
        Map<String, Object> map = new HashMap<>();
        sql.append(" select op.id,op.syscode as sysCode, ").append(" op.item_no as itemNo, ")
                .append(" op.item_curid   as itemCurId, ").append(" op.item_money   as itemMoney, ")
                //TODO 补充名称相关信息
                .append(" info.cur_name as itemCurName, ")
                .append(" op.start_date   as startDate, ").append(" op.end_date     as endDate, ")
                .append(" op.item_concode as itemConCode, ").append(" op.status, ")
                .append(" op.contract_id as contractId ").append(" from gua_contract_occup op ")
                .append(" left join GUA_CONTRACT_INFO info on op.contract_id = info.id ")
                .append(" where op.dada_type =:dataType ").append(" and op.status in (0, 2) ")// 查询暂存和使用中的数据
                .append(" and op.contract_id=:contractId ");
        map.put("contractId", contractId);
        map.put("dataType", GuaranteeConstant.DATA_TYPE_1);// 数据类型为使用登记
        return listBySQLAliasToBean(sql.toString(), map, GuaContractUseDtlVO.class);
    }
    /**
     * 根据id查询担保合同使用详情
     * 
     * @param id
     * @return
     */
    public GuaContractUseDtlVO queryUseDtlById(String id) {
        StringBuilder sql = new StringBuilder();
        sql.append(" select op.id,op.syscode as sysCode, ").append(" op.item_no as itemNo, ")
                .append(" op.item_curid   as itemCurId, ").append(" op.item_money   as itemMoney, ")
                //TODO 补充名称相关信息
                .append(" info.cur_name as itemCurName, ")
                .append(" op.start_date   as startDate, ").append(" op.end_date     as endDate, ")
                .append(" op.item_concode as itemConCode, ").append(" op.status, ")
                .append(" op.contract_id as contractId ").append(" from gua_contract_occup op ")
                .append(" left join GUA_CONTRACT_INFO info on op.contract_id = info.id ")
                .append(" where op.dada_type =:dataType ").append(" and op.id=:id ");
        Map<String, String> params = new HashMap<>();
        params.put("dataType", GuaranteeConstant.DATA_TYPE_1);// 数据类型为使用登记
        params.put("id", id);
        return getBySQLAliasToBean(sql.toString(), params, GuaContractUseDtlVO.class);
    }
    /**
     *
     */
    public List<GuaContractUseDtlVO> queryUseDtlByConId(String contractId, String sysCode) {
        StringBuilder sql = new StringBuilder();
        sql.append(" select op.id,op.syscode as sysCode, ").append(" op.item_no as itemNo, ")
                .append(" op.business_id as businessId,").append(" op.item_curid   as itemCurId, ")
                //TODO 补充名称相关信息
                .append(" info.cur_name as itemCurName, ")
                .append(" op.item_money   as itemMoney, ").append(" op.start_date   as startDate, ")
                .append(" op.end_date     as endDate, ").append(" op.item_concode as itemConCode, ")
                .append(" op.status, ").append(" op.contract_id as contractId ").append(" from gua_contract_occup op ")
                .append(" left join GUA_CONTRACT_INFO info on op.contract_id = info.id ")
                .append(" where  ") // op.dada_type =:dataType and
                .append(" op.contract_id=:contractId ").append(" and op.status<>:status ")
                .append(" and op.syscode=:syscode ");
        Map<String, Object> params = new HashMap<>();
        // params.put("dataType", GuaranteeConstant.DATA_TYPE_1);//数据类型为使用登记
        params.put("contractId", contractId);
        params.put("syscode", sysCode);
        params.put("status", GuaranteeConstant.GUA_CONTRACT_RELEASE);// 查询除释放状态的业务
        return listBySQLAliasToBean(sql.toString(), params, GuaContractUseDtlVO.class);
    }
}
