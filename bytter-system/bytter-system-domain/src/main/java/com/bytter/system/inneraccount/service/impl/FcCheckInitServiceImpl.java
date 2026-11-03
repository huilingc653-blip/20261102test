package com.bytter.system.inneraccount.service.impl;
import com.bytter.core.base.BaseDao;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.system.api.IFcCheckInitService;
import com.bytter.system.api.dto.FcCheckInitDTO;
import com.bytter.system.dao.BtBankAccCurDao;
import com.bytter.system.entity.BtBankAccCur;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service("fcCheckInitServiceI")
@DubboService
@Transactional
public class FcCheckInitServiceImpl extends BaseDao<BtBankAccCur,String> implements IFcCheckInitService {
    @Autowired
    private BtBankAccCurDao btBankAccCurDao;
    /**
     * 查询中心对账余额初始化
     * @param dataTablesInput
     * @param corpAuth
     * @param bankAcc
     * @return
     */
    @Override
    public DataTablesOutput getFcCheckInit(DataTablesInput dataTablesInput, List<String> corpAuth,List<String> bankAcc) {
        return btBankAccCurDao.getFcCheckInit(dataTablesInput,corpAuth,bankAcc);
    }
    @Override
    public FcCheckInitDTO FcCheckInitList(String bankAcc,String account) {
        Map<String,Object> val = new HashMap<String,Object>();
        StringBuffer sb = new StringBuffer();
        sb.append(" select c.bank_acc as bankacc,c.acc_name as bankaccname, c.bank_name as bankname, a.cur_id as curid, a.counter_acc as counterAcc");
        sb.append(" from bt_bank_acc_cur a ");
        sb.append(" inner join bt_bank_acc c on c.id = a.bank_acc_id ");
//        sb.append(" left join fc_check_init b on b.bank_acc = c.bank_acc ");
//        sb.append(" inner join fc_acc d on a.counter_acc = d.blnctr_acc ");
        sb.append(" where 1=1 and c.bank_acc = :bankacc ");
        val.put("bankacc", bankAcc);
//        sb.append(" and d.blnctr_acc = :account ");
//        val.put("account", account);
        return getBySQLAliasToBean(sb.toString(), val, FcCheckInitDTO.class);
    }
    /**
     * ERP余额初始化列表查询
     * @param dataTablesInput
     * @param corpAuth
     * @param bankAcc
     * @return
     */
    @Override
    public DataTablesOutput getFcCheckInitForErp(DataTablesInput dataTablesInput, List<String> corpAuth, List<String> bankAcc) {
        return btBankAccCurDao.getFcCheckInitErp(dataTablesInput,corpAuth,bankAcc);
    }
}
