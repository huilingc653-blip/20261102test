package com.bytter.biz.netbank.dao;

import cn.hutool.core.util.ObjectUtil;
import com.bytter.biz.netbank.entity.BtBankAccUse;
import com.bytter.biz.netbank.vo.BtBankAccUseVO;
import com.bytter.core.base.BaseDao;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.constant.CommonConstant;
import com.bytter.system.api.dto.BtBankAccDTO;
import com.bytter.system.api.vo.BtBankAccVO;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Description: 账号用途维护
 * Company: 深圳市拜特科技股份有限公司
 * Copyright: Copyright (c) 2024
 *
 * @author ZRS
 * @date 2024/10/23
 */
@Repository
public class BtBankAccUseDao extends BaseDao<BtBankAccUse,String> {

    /***
     *  列表数据
     * @param input
     * @return TODO
     */
    public DataTablesOutput<BtBankAccUseVO> listData(DataTablesInput input) {
        StringBuilder sql = new StringBuilder(128);
        sql.append("select t.id,t.use_code useCode,t.use_name useName,t.use_rmk useRmk, ");
        sql.append("t.create_date createDate,t.create_by createBy, ");
        sql.append("t.update_date updateDate,t.update_by updateBy ");
        sql.append("from bt_bank_acc_use t ");
        sql.append("where 1=1 ");
        return getDynamicBySQL(input,sql.toString(), BtBankAccUseVO.class,"",false,null);
    }

    public boolean checkCode(String useCode, String id) {
        Map<String,Object> params = new HashMap<>();
        StringBuilder sql = new StringBuilder(128);
        sql.append("select t.* from bt_bank_acc_use t where t.use_code = :useCode ");
        params.put("useCode",useCode);
        if (ObjectUtil.isNotEmpty(id)){
            sql.append("and t.id <> :id ");
            params.put("id",id);
        }
        List<BtBankAccUseVO> list = this.listBySQLAliasToBean(sql.toString(), params, BtBankAccUseVO.class);
        if (ObjectUtil.isNotEmpty(list)){
            return true;
        }else{
            return false;
        }
    }

    public boolean checkUse(String useCode) {
        Map<String,Object> params = new HashMap<>();
        StringBuilder sql = new StringBuilder(128);
        sql.append("select b.id from bt_bank_acc b where  1=1 ");
        sql.append("and b.ACC_USE_CODE LIKE BT_CONCAT3('%',:useCode,'%') ");
        params.put("useCode",useCode);
        List<BtBankAccDTO> list = this.listBySQLAliasToBean(sql.toString(), params, BtBankAccDTO.class);
        if (ObjectUtil.isNotEmpty(list)){
            return true;
        }else{
            return false;
        }
    }
}
