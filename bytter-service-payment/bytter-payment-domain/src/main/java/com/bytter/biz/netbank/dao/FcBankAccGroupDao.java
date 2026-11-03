package com.bytter.biz.netbank.dao;

import com.bytter.biz.finacial.api.dto.FcBankAccDtlDTO;
import com.bytter.biz.netbank.entity.FcBankAccGroup;
import com.bytter.biz.netbank.vo.FcBankAccGroupVO;
import com.bytter.core.base.BaseDao;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.constant.BtBankAccConstant;
import com.bytter.core.utils.StringUtils;
import com.bytter.system.api.dto.BtBankAccAppDTO;
import com.google.common.collect.Lists;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author linqp
 * @Description 银行账户组
 */
@Repository
public class FcBankAccGroupDao extends BaseDao<FcBankAccGroup, String> {

    public DataTablesOutput<FcBankAccGroupVO> getDataTablesUnion(DataTablesInput dataTablesInput) {
        StringBuilder sql = new StringBuilder();
        sql.append(" select * from (select bag.id, ");
        sql.append("         bag.ACC_GROUP_CODE                                as accGroupCode, ");
        sql.append("         bag.ACC_GROUP_NAME                                as accGroupName, ");
        sql.append("         ga.GROUP_ID                                       as groupId, ");
        sql.append("         group_concat(a.operation_area_name Separator ',') as areaNames, ");
        sql.append(" group_concat(a.id Separator ',')  as operationAreaIds, ");
        sql.append(" group_concat(ga.AREA_CODE Separator ',') as areaCodes ");
        sql.append(" from fc_bank_acc_group bag ");
        sql.append(" left join fc_bank_acc_group_area ga on ga.GROUP_ID = bag.ID ");
        sql.append(" left join SYS_OPERATION_AREA a on ga.AREA_CODE = a.operation_area_code and a.status=1 ");
        sql.append(" group by bag.id) g where 1=1 ");


        return this.dealDynamicBySQL(dataTablesInput, sql.toString(), FcBankAccGroupVO.class, "", false);
    }

    public FcBankAccGroupVO getById(String id) {
        if(StringUtils.isEmpty(id)){
            return new FcBankAccGroupVO();
        }
        StringBuilder sql = new StringBuilder();
        sql.append(" select * from (select bag.id, ");
        sql.append("         bag.ACC_GROUP_CODE                                as accGroupCode, ");
        sql.append("         bag.ACC_GROUP_NAME                                as accGroupName, ");
        sql.append("         ga.GROUP_ID                                       as groupId, ");
        sql.append("         group_concat(a.operation_area_name Separator ',') as areaNames, ");
        sql.append(" group_concat(a.id Separator ',')  as operationAreaIds, ");
        sql.append(" group_concat(ga.AREA_CODE Separator ',') as areaCodes ");
        sql.append(" from fc_bank_acc_group bag ");
        sql.append(" left join fc_bank_acc_group_area ga on ga.GROUP_ID = bag.ID ");
        sql.append(" left join SYS_OPERATION_AREA a on ga.AREA_CODE = a.operation_area_code ");
        sql.append(" group by ga.GROUP_ID) g where g.id='"+id+"'");

        List<FcBankAccGroupVO> fcBankAccGroupVOList = this.listBySQLAliasToBean(sql.toString(),null, FcBankAccGroupVO.class);
        if(CollectionUtils.isNotEmpty(fcBankAccGroupVOList)){
            return fcBankAccGroupVOList.get(0);
        }
        return new FcBankAccGroupVO();
    }

    public List<FcBankAccGroup> listByAccGroupCode(String accGroupCode) {
       return this.listBy("accGroupCode", accGroupCode);
    }

    public List<FcBankAccGroup> listByAccGroupName(String accGroupName) {
        return this.listBy("accGroupName", accGroupName);
    }
}
