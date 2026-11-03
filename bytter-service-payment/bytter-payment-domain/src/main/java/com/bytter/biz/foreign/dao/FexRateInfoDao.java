package com.bytter.biz.foreign.dao;

import com.bytter.biz.foreign.entity.FexRateInfo;
import com.bytter.biz.foreign.vo.FexRateInfoTableVO;
import com.bytter.biz.foreign.vo.FexRateInfoVO;
import com.bytter.core.base.BaseDao;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.core.utils.SqlUtil;
import com.bytter.core.utils.StringUtils;
import com.bytter.core.constant.CommonConstant;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * @Title: Dao
 * @Description: 外汇牌价表
 * @author huangx
 * @date 2020-07-16
 *
 */
@Repository
public class FexRateInfoDao extends BaseDao<FexRateInfo, String> {

    /**
     * 外汇牌价表通用查@param sqlWhere
     *
     * @return
     */
    private String getCommonSql() {
        StringBuilder sql = new StringBuilder();
        sql.append("select r.id as id,r.rate_Exc_Id as rateExcId,r.rate_Type_Id as rateTypeId,r.cur as cur,r.refer_Cur as referCur,");
        sql.append(" r.purchase_Rate as purchaseRate,r.sell_Rate as sellRate,r.middle_Rate as middleRate,r.rmk as rmk,t.bif_Code as bifCode,");
        sql.append(" e.return_Time as returnTime,e.return_Msg as returnMsg,t.type_Name as rateTypeName,c.cur_Name as curName,rc.cur_Name as referCurName,");
        sql.append(" c.english_code as curEnCode,rc.english_code as referCurEnCode,r.term as term,r.EXCHANGE_RATE_DATE as exchangeRateDate,");
        sql.append(" r.cur_Unit as curUnit,(case when r.effect_date is not null then r.effect_date else e.return_Time end) as effectDate,");
        sql.append(" r.create_By as createBy, r.create_Date as createDate, r.update_By as updateBy, r.update_Date as updateDate");
        sql.append(" from fex_rate_info r left join fex_rate_exc e on r.rate_Exc_Id = e.id ");
        sql.append(" left join fex_rate_type t on t.id = r.rate_Type_Id");
        sql.append(" left join bt_currency c on r.cur=c.id");
        sql.append(" left join bt_currency rc on r.refer_Cur=rc.id");
        sql.append(" where 1=1");
        return sql.toString();
    }

    /**
     * 外汇牌价表通用查@param sqlWhere
     *
     * @return
     */
    private String getCommonSql2() {
        StringBuilder sql = new StringBuilder();
        sql.append("select r.id as id,r.rate_Exc_Id as rateExcId,r.rate_Type_Id as rateTypeId,c.id as cur,rc.id as referCur,");
        sql.append(" r.purchase_Rate as purchaseRate,r.sell_Rate as sellRate,r.middle_Rate as middleRate,r.rmk as rmk,t.bif_Code as bifCode,");
        sql.append(" e.return_Time as returnTime,e.return_Msg as returnMsg,t.type_Name as rateTypeName,c.cur_Name as curName,rc.cur_Name as referCurName,");
        sql.append(" c.english_code as curEnCode,rc.english_code as referCurEnCode,r.term as term,r.EXCHANGE_RATE_DATE as exchangeRateDate,");
        sql.append(" r.cur_Unit as curUnit,(case when r.effect_date is not null then r.effect_date else e.return_Time end) as effectDate,");
        sql.append(" r.create_By as createBy, r.create_Date as createDate, r.update_By as updateBy, r.update_Date as updateDate");
        sql.append(" from fex_rate_info r left join fex_rate_exc e on r.rate_Exc_Id = e.id ");
        sql.append(" left join fex_rate_type t on t.id = r.rate_Type_Id");
        sql.append(" left join bt_currency c on r.cur=c.id and c.valid_sign = '1'");
        sql.append(" left join bt_currency rc on r.refer_Cur=rc.id and rc.valid_sign = '1'");
        sql.append(" where 1=1");
        return sql.toString();
    }
    /**
     * 外汇牌价列表查询
     *
     * @param dataTablesInput
     * @return
     */
    @SuppressWarnings("rawtypes")
    public DataTablesOutput getDataTablesUnion(DataTablesInput dataTablesInput, String joinSql) {
        StringBuilder sqlL = new StringBuilder();
        sqlL.append("select r.id as id,r.rate_Exc_Id as rateExcId,r.rate_Type_Id as rateTypeId ,r.cur as cur,r.refer_Cur ,");
        sqlL.append(" r.purchase_Rate as purchaseRate,r.sell_Rate as sellRate,r.middle_Rate as middleRate,r.rmk as rmk,t.bif_Code as bifCode,");
        sqlL.append(" e.return_Time as returnTime,e.return_Msg as returnMsg,t.type_Name as rateTypeName,c.cur_Name as curName,rc.cur_Name as referCurName,");
        sqlL.append(" c.english_code as curEnCode,rc.english_code as referCurEnCode,r.term as term,r.EXCHANGE_RATE_DATE as exchangeRateDate,");
        sqlL.append(" r.cur_Unit as curUnit,(case when r.effect_date is not null then r.effect_date else e.return_Time end) as effectDate,");
        sqlL.append(" r.create_By as createBy, r.create_Date as createDate, r.update_By as updateBy, r.update_Date as updateDate");
        sqlL.append(" from fex_rate_info r left join fex_rate_exc e on r.rate_Exc_Id = e.id ");
        sqlL.append(" left join fex_rate_type t on t.id = r.rate_Type_Id");
        sqlL.append(" left join bt_currency c on r.cur=c.id");
        sqlL.append(" left join bt_currency rc on r.refer_Cur=rc.id");
        sqlL.append(" where 1=1");
        StringBuilder sql =new StringBuilder(sqlL);
      //  sql.append(sqlL).append(") r where 1=1");

        if (StringUtils.isNotEmpty(joinSql)) {
            sql.append(joinSql);
        }
        //银行返回时间/发布时间
        String gedReturnTime = (String) dataTablesInput.getFormdata().get("ged_returnTime");
        String ledReturnTime = (String) dataTablesInput.getFormdata().get("led_returnTime");
        if(StringUtils.isNotEmpty(gedReturnTime)){
            gedReturnTime = gedReturnTime + " 00:00:00";
            dataTablesInput.getFormdata().put("ged_effectDate", gedReturnTime);
            dataTablesInput.getFormdata().remove("ged_returnTime");

        }
        if(StringUtils.isNotEmpty(ledReturnTime)){
            ledReturnTime = ledReturnTime + " 23:59:59";
            dataTablesInput.getFormdata().put("led_effectDate", ledReturnTime);
            dataTablesInput.getFormdata().remove("led_returnTime");

        }
        return getDynamicBySQL(dataTablesInput, sql.toString(), FexRateInfoTableVO.class, "r", false);
    }

    /**
     * 根据ID获取外汇牌价信息
     *
     * @param id
     * @return
     */
    public FexRateInfoVO getByIdUnion(String id) {
        StringBuilder sql = new StringBuilder(this.getCommonSql());
        sql.append(" and r.id=:id");
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("id", id);
        return getBySQLAliasToBean(sql.toString(), params, FexRateInfoVO.class);
    }

    /**
     * 根据汇率日期获取牌价数据
     * @param minDate
     * @param maxDate
     * @return
     */
    public List<FexRateInfoVO> getByExchangeRateDate(Date minDate, Date maxDate){
        HashMap<String, Object> params = new HashMap<String, Object>();
        StringBuilder sql = new StringBuilder(this.getCommonSql());
        if(!EmptyUtils.isEmpty(minDate)){
            sql.append(" and r.EXCHANGE_RATE_DATE >= :minDate");
            params.put("minDate",minDate);
        }
        if(!EmptyUtils.isEmpty(maxDate)){
            sql.append(" and r.EXCHANGE_RATE_DATE <= :maxDate");
            params.put("maxDate",maxDate);
        }
        return listBySQLAliasToBean(sql.toString(), params, FexRateInfoVO.class);
    }

    /**
     *
     * @param rateTypeId
     * @param curId
     * @param referCurId
     * @param isSP   是否即期的查询 1是  0否
     * @return
     */
    public List<FexRateInfoVO> getFexRateInfoVO(String rateTypeId,String curId,String referCurId,String isSP){
        HashMap<String, Object> params = new HashMap<String, Object>();
        StringBuilder sql = new StringBuilder(this.getCommonSql2());
        if("1".equals(isSP)) {
            sql.append(" AND UPPER(r.term) = UPPER('sp') ");
        }
        if(!EmptyUtils.isEmpty(rateTypeId)){
            sql.append(" and r.rate_Type_Id = :rateTypeId");
            params.put("rateTypeId",rateTypeId);
        }
        if(!EmptyUtils.isEmpty(curId) && !EmptyUtils.isEmpty(referCurId)){
            sql.append(" and ((r.cur = :curId and r.refer_Cur=:referCurId) or (r.cur = :referCurId and r.refer_Cur=:curId) )");
            params.put("curId",curId);
            params.put("referCurId",referCurId);
        }
        sql.append(" order by e.return_Time desc,r.EXCHANGE_RATE_DATE desc");
        return listBySQLAliasToBean(sql.toString(), params, FexRateInfoVO.class);
    }

    /**
     * 根据id删除牌价信息
     * @param ids
     */
    public void deleteByIds(List<String> ids){
        StringBuilder sql = new StringBuilder();
        sql.append("delete from fex_rate_info where ").append(SqlUtil.getInSql(ids,"id",true));
        createSQLQuery(sql.toString()).executeUpdate();
    }

    /**
     * 根据id删除牌价指令信息
     * @param ids
     */
    public void deleteExcByIds(List<String> ids){
        StringBuilder sql = new StringBuilder();
        sql.append("delete from fex_rate_exc  where id in ");
        sql.append(" (select r.rate_Exc_Id from fex_rate_info r where ").append(SqlUtil.getInSql(ids,"r.id",true));
        sql.append(")");
        createSQLQuery(sql.toString()).executeUpdate();
    }


    /**
     *
     * 获取报价方式
     *
     * @param curId
     * @param referCurId
     * @param rateTypeId
     * @param exchangeDate
     * @param term
     * @return
     */
    public FexRateInfoVO getRateInfoByRateType(String curId,String referCurId,String rateTypeId,String exchangeDate,String term,String currentFlag){
        if(StringUtils.isBlank(curId)||StringUtils.isBlank(referCurId)){
            return null;
        }
        StringBuilder sql = new StringBuilder(this.getCommonSql2());
        sql.append(" and ((r.cur = :curId and r.refer_cur = :referCurId) or (r.refer_cur = :curId and r.cur = :referCurId)) and r.rate_type_id =:rateTypeId");
        HashMap<String, String> params = new HashMap<String, String>();
        if(CommonConstant.YES.equals(currentFlag)){
            if(!StringUtils.isBlank(exchangeDate)){
                sql.append(" and r.exchange_rate_date = BT_TO_DATE(:exchangeDate,'yyyy-MM-dd')");
                params.put("exchangeDate", exchangeDate);
            }
        }else{
            if(!StringUtils.isBlank(exchangeDate)){
                sql.append(" and r.exchange_rate_date <= BT_TO_DATE(:exchangeDate,'yyyy-MM-dd')");
                params.put("exchangeDate", exchangeDate);
            }
        }
        if(!StringUtils.isBlank(term)){
            sql.append(" and r.term = :term ");
            params.put("term", term);
        }
        sql.append(" order by e.return_Time desc,r.exchange_rate_date desc ");
        params.put("curId", curId);
        params.put("referCurId", referCurId);
        params.put("rateTypeId", rateTypeId);
        List<FexRateInfoVO> list = listBySQLAliasToBean(sql.toString(), params, FexRateInfoVO.class);
        if(!EmptyUtils.isEmpty(list)){
            for (FexRateInfoVO vo : list) {
                if(curId.equals(vo.getCur()) && referCurId.equals(vo.getReferCur())){
                    return vo;
                }
            }
            return list.get(0);
        }
        return null;
    }


    /**
     *
     * 获取报价方式
     *
     * @param curId
     * @param referCurId
     * @param rateTypeId
     * @param exchangeDate
     * @param term
     * @return
     */
    public FexRateInfoVO getRateInfo(String curId,String referCurId,String rateTypeId,String exchangeDate,String term,String currentFlag){
        if(StringUtils.isBlank(curId)||StringUtils.isBlank(referCurId)){
            return null;
        }
        StringBuilder sql = new StringBuilder(this.getCommonSql());
        sql.append(" and ((r.cur = :curId and r.refer_Cur = :referCurId) or (r.refer_Cur = :curId and r.cur = :referCurId)) and r.rate_type_id =:rateTypeId");
        HashMap<String, String> params = new HashMap<String, String>();
        if(CommonConstant.YES.equals(currentFlag)){
            if(!StringUtils.isBlank(exchangeDate)){
                sql.append(" and r.exchange_rate_date = BT_TO_DATE(:exchangeDate,'yyyy-MM-dd')");
                params.put("exchangeDate", exchangeDate);
            }
        }else{
            if(!StringUtils.isBlank(exchangeDate)){
                sql.append(" and r.exchange_rate_date <= BT_TO_DATE(:exchangeDate,'yyyy-MM-dd')");
                params.put("exchangeDate", exchangeDate);
            }
        }
        if(!StringUtils.isBlank(term)){
            sql.append(" and r.term = :term ");
            params.put("term", term);
        }
        sql.append(" order by e.return_Time desc,r.exchange_rate_date desc ");
        params.put("curId", curId);
        params.put("referCurId", referCurId);
        params.put("rateTypeId", rateTypeId);
        List<FexRateInfoVO> list = listBySQLAliasToBean(sql.toString(), params, FexRateInfoVO.class);
        if(!EmptyUtils.isEmpty(list)){
            for (FexRateInfoVO vo : list) {
                if(curId.equals(vo.getCur()) && referCurId.equals(vo.getReferCur())){
                    return vo;
                }
            }
            return list.get(0);
        }
        return null;
    }
}
