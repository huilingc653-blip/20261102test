package com.bytter.biz.foreign.dao;

import com.bytter.biz.foreign.vo.FexCorpPoolBankAccVO;
import com.bytter.core.base.BaseDao;
import com.bytter.core.utils.DateUtils;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.core.utils.StringUtils;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author wangzx
 * @version 1.0
 * @Title: dao
 * @Description: 资金池账户余额查询
 * @date 2020-02-25 09:31:56
 */
@Repository
public class FexCorpPoolBankaccBalDao extends BaseDao<FexCorpPoolBankAccVO, String> {


    /**
     * 根据条件查询资金池账户信息
     * @param fexCorpPoolBankAccVO
     * @return
     */
    public List<FexCorpPoolBankAccVO> getBalList(FexCorpPoolBankAccVO fexCorpPoolBankAccVO){
        StringBuilder sql = new StringBuilder();
        Map<String, Object> params = new HashMap<>();
        sql.append("select c.id as id, c.parent_id as parentId, c.parent_sign as parentSign, p.pool_code as poolCode, p.pool_name as poolName, c.corp_id as corpId, sc.name as corpName,");
        sql.append("c.acc_id as accId, acc.bank_acc as bankAcc, c.cur_id as curId, cur.english_code as englishCode, f.pool_id as poolId, f.pool_nature as poolNature,");
        sql.append(" c.acc_nature as accNature, bal.bal_date as balDate, bt_nvl_num(bal.bal,0) as bal ");
        sql.append(" from fex_corp_pool_bankacc c left join fex_corp_pool f on f.id = c.parent_id left join fc_corp_pool p on f.pool_id = p.id ");
        sql.append(" left join sys_corp sc on sc.id = c.corp_id ");
        sql.append(" left join bt_bank_acc acc on c.acc_id = acc.id left join bt_currency cur on c.cur_id = cur.id");
        sql.append(" left join (select bab.bank_acc,bab.bal_date,bab.cur_id,bab.bal from bis_acc_bal bab,");
        sql.append(" (select ba.bank_acc as bankAcc, ba.cur_id as curId, max(ba.bal_date) as balDate from bis_acc_bal ba");
        sql.append(" where bt_to_char(ba.bal_Date, 'yyyy-MM-dd') <= :balDate and ba.status = 95 group by ba.bank_acc, ba.cur_id) bb");
        sql.append(" where bab.bank_acc = bb.bankAcc and bab.cur_id = bb.curId and bab.bal_date = bb.balDate) bal");
        sql.append(" on bal.bank_acc = acc.bank_acc and bal.cur_Id = c.cur_id");
        sql.append(" where 1 = 1 and bt_to_char(bal.bal_Date, 'yyyy-MM-dd') <= :balDate ");

        if(!StringUtils.isBlank(fexCorpPoolBankAccVO.getPoolId())){
            sql.append(" and f.pool_id =:poolId");
            //sql.append(" and f.id =:poolId");
            params.put("poolId",fexCorpPoolBankAccVO.getPoolId());
        }
        if(!StringUtils.isBlank(fexCorpPoolBankAccVO.getParentId())){
            sql.append(" and c.parent_id =:parentId");
            params.put("parentId",fexCorpPoolBankAccVO.getParentId());
        }
        /*if(!StringUtils.isBlank(fexCorpPoolBankAccVO.getCurId())){
            sql.append(" and c.cur_id =:curId");
            params.put("curId",fexCorpPoolBankAccVO.getCurId());
        }*/
        if(!StringUtils.isBlank(fexCorpPoolBankAccVO.getParentSign())){
            sql.append(" and c.parent_sign =:parentSign");
            params.put("parentSign",fexCorpPoolBankAccVO.getParentSign());
        }
        if(!EmptyUtils.isEmpty(fexCorpPoolBankAccVO.getBalDate())){
            params.put("balDate", DateUtils.formatDate(fexCorpPoolBankAccVO.getBalDate()));
        }else{
            params.put("balDate", DateUtils.formatDate(DateUtils.getDate()));
        }
        return listBySQLAliasToBean(sql.toString(), params,FexCorpPoolBankAccVO.class);
    }
}
