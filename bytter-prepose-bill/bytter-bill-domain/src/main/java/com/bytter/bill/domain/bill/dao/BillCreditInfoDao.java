package com.bytter.bill.domain.bill.dao;

import com.bytter.bill.domain.bill.entity.BillAddCreditInfo;
import com.bytter.core.base.BaseDao;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;

@Repository
public class BillCreditInfoDao extends BaseDao<BillAddCreditInfo,String> {
    public List<BillAddCreditInfo> getBillAddCreditInfo(String vid){
        String sql="select * from AB_ADD_CREDIT_INFO where vid=:vid order by id asc";
        HashMap<String, Object> params = new HashMap<>();
        params.put("vid",vid);
        List<BillAddCreditInfo> list=this.listBySQLAliasToBean(sql.toString(), params, BillAddCreditInfo.class);
        return list;
    }
}
