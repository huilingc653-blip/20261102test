package com.bytter.tally.dao;

import com.bytter.biz.finacial.api.dto.BtNetChalkDTO;
import com.bytter.core.base.BaseDao;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.datatable.OrderString;
import com.bytter.core.utils.StringUtils;
import com.bytter.tally.vo.BisAccDtlTallyVO;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @ClassName BtNetChalkOtherReceiveDao
 * @Author zx
 * @Description 其他收款手工记账Dao
 * @Date 2024/11/8 9:17
 */
@Repository
public class BtNetChalkOtherReceiveDao extends BaseDao<BisAccDtlTallyVO, String> {

    /**
     * 查询页面数据
     */
    public DataTablesOutput<BtNetChalkDTO> queryBtNetChalk(DataTablesInput dataTablesInput, String type, String userId, List<String> corpAuth) {
        StringBuilder sb = new StringBuilder();
        sb.append(" select vbn.* from bt_net_chalk vbn  ");
        sb.append(" left join bt_bank_acc b on vbn.bank_acc_id = b.id ");
        sb.append(" left join sys_corp s on s.id = vbn.corp_id ");
        sb.append(" where 1 = 1  ");
        sb.append(" and vbn.cd_sign = 0 and s.type = 0 ");
        if ("Init".equals(type)) {
            sb.append(" and (vbn.estate = '0' or vbn.estate = '6' or  vbn.estate is null or vbn.estate='') ");
        } else if ("Edit".equals(type)) {
            sb.append(" and (vbn.estate in( '5' , '4' , '3' )) and vbn.operator_code = '").append(userId).append("'");
        } else if ("Check".equals(type)) {
            sb.append(" and (vbn.estate = '3' ) and vbn.operator_code != '").append(userId).append("'");
        }
        sb.append(" and vbn.bank_acc is not null ");
        //不符合归集（对方账户没有父子关系）
        sb.append(" and not exists (select bb.prent_acc_id from bt_bank_acc_cur bb, bt_bank_acc bc ");
        sb.append(" where bb.bank_acc_id = bc.id and bc.bank_acc = vbn.opp_Acc_No and b.id = bb.prent_acc_id) ");
        sb.append(" and ").append(getSQLIn(corpAuth, 900, "vbn.corp_id"));
        dataTablesInput.setOrderString(new OrderString("vbn.trans_time", "asc"));
        return getDynamicBySQL(dataTablesInput, sb.toString(), BtNetChalkDTO.class, null, false);
    }

}
