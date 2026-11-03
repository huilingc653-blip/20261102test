package com.bytter.cashVoucher.dao;

import com.bytter.cashVoucher.entity.BvVouEmployInfo;
import com.bytter.cashVoucher.vo.BvVouEmployInfoVo;
import com.bytter.core.base.BaseDao;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
/**
* @Title: Dao
* @Description: 凭证使用
* @author onlineGenerator
* @date 2017-06-09 09:06:03
* @version 1.0
*
*/
@Repository
public class BvVouEmployInfoDao extends BaseDao<BvVouEmployInfo,String>{
    /**
     * 根据凭证明细id获取凭证使用信息
     */
    public BvVouEmployInfoVo getDataByVoudtlId(Map<String,Object> params){
        StringBuilder sql = new StringBuilder("select ");
        sql.append(" bv.voud_no as voudNo,bv.voudtl_id as voudtlId,bv.employ_date as employDate," );
        sql.append(" bv.employ_person as employPerson,bv.vou_money as vouMoney,bv.data_source as dataSource,bv.rmk as rmk ");
        sql.append(" from BV_VOU_EMPLOY_INFO bv where voudtl_id = :voudtlId ");
        List<BvVouEmployInfoVo> listInfo=this.listBySQLAliasToBean(sql.toString(), params, BvVouEmployInfoVo.class);
        if(listInfo != null && !listInfo.isEmpty ()){
            return listInfo.get (0);
        }
        return null;
    }
}
