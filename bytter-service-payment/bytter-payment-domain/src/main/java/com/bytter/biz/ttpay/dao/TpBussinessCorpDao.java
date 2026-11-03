package com.bytter.biz.ttpay.dao;
import com.bytter.biz.ttpay.entity.TpBussinessCorp;
import com.bytter.biz.ttpay.vo.TpBussinessCorpVO;
import com.bytter.core.base.BaseDao;
import org.springframework.stereotype.Repository;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
@Repository
public class TpBussinessCorpDao extends BaseDao<TpBussinessCorp, String> {
    public List<TpBussinessCorpVO> getByBussnissId(String bussinessId) {
        StringBuilder sql = new StringBuilder();
        sql.append(" select pc.BUSINESS_ID, pc.corp_id from TP_BUSINESS_CORP pc where pc.BUSINESS_ID = :bussinessId ");
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("bussinessId", bussinessId);
        return this.listBySQLAliasToBean(sql.toString(), params, TpBussinessCorpVO.class);
    }
    public void deleteTpBussnissId(String bussinessId) {
        Map<String, Object> params = new HashMap<String, Object>();
        String sql = " delete from TP_BUSINESS_CORP where BUSINESS_ID = :bussinessId ";
        params.put("bussinessId", bussinessId);
        createSQLQuery(sql,params).executeUpdate();
    }
}
