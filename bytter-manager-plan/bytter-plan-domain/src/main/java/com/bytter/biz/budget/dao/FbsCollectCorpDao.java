package com.bytter.biz.budget.dao;
import com.bytter.biz.budget.entity.FbsCollectCorp;
import com.bytter.biz.budget.vo.FbsCollectCorpVO;
import com.bytter.core.base.BaseDao;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.core.utils.SqlUtil;
import org.springframework.stereotype.Repository;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
@Repository
public class FbsCollectCorpDao extends BaseDao<FbsCollectCorp, String> {
    /**
     * 获取汇总单位虚拟本单位
     * @param corpId
     * @return
     */
    public FbsCollectCorpVO listCorpByCorpId(String corpId) {
        StringBuilder sql = new StringBuilder();
        Map<String, Object> params = new HashMap<>();
        sql.append(" select f.id as id,f.corp_id as corpId,f.corp_code as corpCode,f.corp_name as corpName from FBS_COLLECT_CORP f where   f.corp_id = :corpId ");
        params.put("corpId",corpId);
        List<FbsCollectCorpVO> list = this.listBySQLAliasToBean(sql.toString(), params, FbsCollectCorpVO.class);
        if(!EmptyUtils.isEmpty(list) && list.size()>0){
            return list.get(0);
        }else{
            return null;
        }
    }
    /**
     * 获取汇总单位虚拟本单位
     * @param id
     * @return
     */
    public FbsCollectCorpVO listCorpById(String id) {
        StringBuilder sql = new StringBuilder();
        Map<String, Object> params = new HashMap<>();
        sql.append(" select f.id as id,f.corp_id as corpId,f.corp_code as corpCode,f.corp_name as corpName from FBS_COLLECT_CORP f where  f.id = :id ");
        params.put("id",id);
        List<FbsCollectCorpVO> list = this.listBySQLAliasToBean(sql.toString(), params, FbsCollectCorpVO.class);
        if(!EmptyUtils.isEmpty(list) && list.size()>0){
            return list.get(0);
        }else{
            return null;
        }
    }
    /**
     * 根据单位id获取
     * @param corpId
     * @return
     */
    public String  getIdByCorpId(String[] corpId) {
        String id = "";
        StringBuilder sql = new StringBuilder();
        Map<String, Object> params = new HashMap<>();
        sql.append(" select f.id as id,f.corp_id as corpId,f.corp_code as corpCode,f.corp_name as corpName from FBS_COLLECT_CORP f where  ");
        sql.append(SqlUtil.getInSql(corpId,"f.corp_id",true));
        List<FbsCollectCorpVO> list = this.listBySQLAliasToBean(sql.toString(), params, FbsCollectCorpVO.class);
        if(!EmptyUtils.isEmpty(list)){
            for(FbsCollectCorpVO vo : list){
                if(EmptyUtils.isEmpty(id)){
                    id = vo.getId();
                }else{
                    id = id+","+vo.getId();
                }
            }
        }
        return id;
    }
    /**
     * 根据单位id获取
     * @param corpId
     * @return
     */
    public List<String>  getIdByCorpId(List<String> corpId) {
        List<String> idList = new ArrayList<>();
        StringBuilder sql = new StringBuilder();
        Map<String, Object> params = new HashMap<>();
        sql.append(" select f.id as id,f.corp_id as corpId,f.corp_code as corpCode,f.corp_name as corpName from FBS_COLLECT_CORP f where  ");
        sql.append(SqlUtil.getInSql(corpId,"f.corp_id",true));
        List<FbsCollectCorpVO> list = this.listBySQLAliasToBean(sql.toString(), params, FbsCollectCorpVO.class);
        if(!EmptyUtils.isEmpty(list)){
            for(FbsCollectCorpVO vo : list){
               idList.add(vo.getId());
            }
        }
        return idList;
    }
}
