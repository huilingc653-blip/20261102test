package com.bytter.biz.bdlinks.dao;
import com.bytter.biz.bdlinks.entity.BpDubiousTrade;
import com.bytter.core.base.BaseDao;
import com.bytter.core.utils.DateUtils;
import com.bytter.core.constant.CommonConstant;
import com.bytter.biz.bdlink.api.constant.BdlinkConstant;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
/**
 * @Title: Dao
 * @Description: BP_DUBIOUS_TRADE
 * @author huangx
 * @date 2021-3-5 11:18:25
 * @version 1.0
 *
 */
@Repository
public class BpDubiousTradeDao extends BaseDao<BpDubiousTrade,String> {
    /**
     * 修改指令表信息状态
     * @param voucherNo 指令编号
     * @param voucherStat 指令状态 T-可疑状态 3-准备发送（发送时由T-3） 7-指令无效（作废时由T-7）
     * @param foreignFlag 外汇标志 0-非外汇 1-外汇
     * @return
     */
    public int updateBisExcVoucherStat(String voucherNo, String voucherStat, String foreignFlag, Date reqDateNew, String verifyCodeNew){
        String tableName = "bis_exc";//默认付款指令表
        if(CommonConstant.YES.equals(foreignFlag)){
            tableName = "bis_foreign_exc";
        }
        //status:0-可发送 1-待发送 2-指令无效
        StringBuilder sql=new StringBuilder("update " + tableName + " set voucher_Stat=:voucherStat");
        if (BdlinkConstant.PRE_SEND.equals(voucherStat)) {
            //发送时更新申请日期，加密串信息
            sql.append(",req_Date=:reqDate,verify_Code=:verifyCode,notice_sign=:noticeSign");
        }
        sql.append(" where voucher_No=:voucherNo and voucher_Stat = :oldVoucherStat");
        Map<String,Object> params = new HashMap<>();
        params.put("reqDate", reqDateNew);
        params.put("verifyCode", verifyCodeNew);
        // 重置记账标识
        params.put("noticeSign", CommonConstant.NO);
        params.put("voucherStat", voucherStat);
        params.put("voucherNo", voucherNo);
        params.put("oldVoucherStat", BdlinkConstant.SUSPECTED);
        return this.batchSQLExecute(sql.toString(), params);
    }
    /**
     * 修改可疑指令表信息状态
     * @param voucherNo 指令编号
     * @param status 更新状态 0-可发送 1-不可发送 2-指令无效
     * @param foreignFlag 外汇标志 0-非外汇 1-外汇
     * @return
     */
    public int updateBpDubiousTrade(String voucherNo, String status, String foreignFlag){
        //status:0-可发送 1-不可发送 2-指令无效
        StringBuilder sql=new StringBuilder("update BpDubiousTrade set status=:status");
        sql.append(" where businessId=:voucherNo and status = :oldStatus and foreignFlag=:foreignFlag");
        Map<String,Object> params = new HashMap<>();
        params.put("status", status);
        params.put("voucherNo", voucherNo);
        params.put("oldStatus", "1");
        params.put("foreignFlag", foreignFlag);
        return this.batchExecute(sql.toString(), params);
    }
    /**
     * 更新对私明细的状态
     * @param billId 付款单号
     * @param voucherStat 修改的指令
     * @return
     */
    public int updateExpenseVoucherStatByBillId(String billId, String voucherStat){
        StringBuilder hql=new StringBuilder("update NisExpenseDetail set voucherStat=:voucherStat");
        hql.append(" where billId=:billId and voucherStat = :oldVoucherStat");
        Map<String,Object> params = new HashMap<>();
        params.put("billId", billId);
        params.put("voucherStat", voucherStat);
        params.put("oldVoucherStat", BdlinkConstant.SUSPECTED);
        return this.batchExecute(hql.toString(), params);
    }
}
