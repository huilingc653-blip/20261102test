package com.bytter.tally.dao;
import com.bytter.biz.payment.api.dto.NisBillheadDTO;
import com.bytter.core.base.BaseDao;
import com.bytter.system.api.dto.FcAccDTO;
import org.springframework.stereotype.Repository;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
* @Title: Dao
* @Description: BT_NET_CHALK
* @author
* @date 2017-07-04 14:58:22
* @version 1.0
*
*/
@Repository
public class NisBillheadManualDao extends BaseDao<NisBillheadDTO,String>{
	public List<FcAccDTO> queryBySomeCondPgd(String sql){
		String sb = " select t.blnctr_acc as blnctrAcc,t.acc_name as accName, " +
				" bc.cur_name as curName, f.subject_name as subjectName, t.shortCut as shortCut, " +
				" nvl(fl.online_balance,0) - nvl(fl.freeze_money,0) as tallyBalance, " +
				" nvl(fl.check_balance,0) - nvl(fl.freeze_money,0) as checkBalance " +
				" from Fc_Acc t left join fc_Subject f on t.subject_Code = f.subject_code " +
				" left join bt_currency bc on t.cur_code = bc.cur_code " +
				" left join fc_online_ledger fl on t.blnctr_acc = fl.account " +
				" where 1 = 1 " +
				sql;
		return listBySQLAliasToBean(sb, null, FcAccDTO.class);
	}
    /**
     * <br>
     * <b>功能：</b> 更新付款单记账自动复核信息 <br>
     * <b>作者：</b>liyh<br>
     * <b>日期：</b> 2022/3/5 0005 <br>
     */
    /**
     * <br>
     * <b>功能：</b> 更新付款单记账信息 <br>
     * <b>作者：</b>liyh<br>
     * <b>日期：</b> 2022/3/5 0005 <br>
     */
    /**
     * <br>
     * <b>功能：</b> 更新付款单记账信息 <br>
     * <b>作者：</b>liyh<br>
     * <b>日期：</b> 2022/3/5 0005 <br>
     */
    /**
     * <br>
     * <b>功能：</b> 更新付款单记账信息 <br>
     * <b>作者：</b>liyh<br>
     * <b>日期：</b> 2022/3/5 0005 <br>
     */
}
