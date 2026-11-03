package com.bytter.biz.netbank.dao;
import com.bytter.biz.netbank.entity.BisFailReason;
import com.bytter.biz.netbank.vo.BisFailReasonVO;
import com.bytter.core.base.BaseDao;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.utils.StringUtils;
import com.bytter.core.constant.CommonConstant;
import org.springframework.stereotype.Repository;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Title: Dao
 * @Description: 交易失败原因表
 * @author huangx
 * @date 2019-05-17 14:26:13
 * @version 1.0
 *
 */
@Repository
public class BisFailReasonDao extends BaseDao<BisFailReason, String> {
    /**
     * 查询交易失败原因
     * @param input
     * @return
     */
    public DataTablesOutput<BisFailReasonVO> getDataTablesUnion(DataTablesInput input){
        StringBuilder sql = new StringBuilder();
        Map<String, Object> params = new HashMap<String, Object>();
        sql.append(" select b.id as id,b.FAIL_CODE as failCode,b.FAIL_NAME as failNameShow,b.FAIL_NAME as failName,b.KEY_WORDS as keyWords, ");
        sql.append(" b.ACTION_CODE as actionCode,b.bif_Code as bifCode ");
        sql.append(" from BIS_FAIL_REASON b where 1 = 1 ");
        //接口代码
        String bifCode = (String) input.getFormdata().get("bifCode");
        if(StringUtils.isNotEmpty(bifCode)){
            sql.append(" and (");
            String[] bifCodes = bifCode.split(CommonConstant.COMMA_SPLIT);
            for(int i=0; i<bifCodes.length;i++) {
                if(i==0){
                    sql.append(" b.bif_Code like :bifCode_"+i);
                }else {
                    sql.append(" or b.bif_Code like :bifCode_"+i);
                }
                params.put("bifCode_"+i,  "%"+bifCodes[i]+"%");
            }

            sql.append(")");
        }
        return getDynamicBySQL(input,sql.toString(), BisFailReasonVO.class,"b",false, params);
    }
    /**
     * 根据ID查询交易失败原因
     * @param id
     * @return
     */
    public BisFailReasonVO getByUnion(String id){
        StringBuilder sql = new StringBuilder();
        sql.append(" select b.id as id,b.FAIL_CODE as failCode,b.FAIL_NAME as failName,b.KEY_WORDS as keyWords, ");
        sql.append(" b.ACTION_CODE as actionCode,b.bif_Code as bifCode ");
        sql.append(" from BIS_FAIL_REASON b where b.id = :id ");
        HashMap<String,String> params = new HashMap();
        params.put("id",id);
        return getBySQLAliasToBean(sql.toString(),params, BisFailReasonVO.class);
    }
    /**
     * 根据银企接口获取交易失败原因
     * @param bifCode
     * @return
     */
    public List<BisFailReasonVO> listBisFailReasonByBifCode(String bifCode){
        StringBuilder sql = new StringBuilder();
        sql.append(" select b.id as id,b.FAIL_CODE as failCode,b.FAIL_NAME as failName,b.KEY_WORDS as keyWords, ");
        sql.append(" b.ACTION_CODE as actionCode,b.bif_Code as bifCode ");
        sql.append(" from BIS_FAIL_REASON b where 1 = 1 ");
        if(StringUtils.isNotBlank(bifCode)){
            sql.append(" and (");
            sql.append(" b.bif_Code is null or b.bif_Code like :bifCode");
            sql.append(")");
        }
        HashMap<String,String> params = new HashMap();
        params.put("bifCode", "%" + bifCode +"%");
        sql.append(" order by b.bif_Code");
        return listBySQLAliasToBean(sql.toString(),params, BisFailReasonVO.class);
    }
    /**
     * 根据id获取交易失败原因
     *
     * @param id
     * @return
     */
    public BisFailReason findById(String id) {
        StringBuilder sql = new StringBuilder();
        sql.append(" select b.id as id,b.FAIL_CODE as failCode,b.FAIL_NAME as failName,b.KEY_WORDS as keyWords, ");
        sql.append(" b.ACTION_CODE as actionCode,b.bif_Code as bifCode ");
        sql.append(" from BIS_FAIL_REASON b where b.id = :id ");
        HashMap<String,String> params = new HashMap();
        params.put("id",id);
        return getBySQLAliasToBean(sql.toString(),params, BisFailReason.class);
    }
    /**
     * 根据编码获取交易失败原因
     *
     * @param failCode
     * @return
     */
    public BisFailReason findByCode(String failCode) {
        return getUniqueBy("failCode", failCode);
    }
    /**
     * 重复校验
     *
     * @param propertyName
     * @param propertyValue
     * @return
     */
    public BisFailReason validByPropertyName(String propertyName, String propertyValue, String id) {
        BisFailReason reason = getUniqueBy(propertyName, propertyValue);
        if(reason != null && StringUtils.isNotEmpty(id) && reason.getId().equals(id)){
            return null;
        }
        return reason;
    }
    /**
     * 根据关键字获取失败原因
     * @param keyWord
     * @return
     */
    public List<BisFailReason> listByKeyWord(String keyWord, String id){
        String hql=" from BisFailReason b where b.keyWords like :keyWord";
        HashMap<String, String> params = new HashMap<>();
        params.put("keyWord", "%" + keyWord + "%");
        if(StringUtils.isNotEmpty(id)){
            hql += " and b.id<>:id";
            params.put("id", id);
        }
        return this.listByHQL(hql, params);
    }
}
