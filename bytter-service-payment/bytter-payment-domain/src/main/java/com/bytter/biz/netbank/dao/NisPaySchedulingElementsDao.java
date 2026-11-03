package com.bytter.biz.netbank.dao;
import com.bytter.biz.netbank.entity.NisPaySchedulingElements;
import com.bytter.biz.netbank.vo.NisPaySchedulingElementsVO;
import com.bytter.core.base.BaseDao;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.biz.bdlink.api.constant.NetbankConstant;
import org.springframework.stereotype.Repository;
import java.util.List;
/**
 *付款排期要素
 */
@Repository
public class NisPaySchedulingElementsDao extends BaseDao<NisPaySchedulingElements,String> {
    /**
     * 查询业务种类维护列表
     *
     * @param input
     * @return
     */
    @SuppressWarnings("rawtypes")
    public DataTablesOutput<NisPaySchedulingElementsVO> dataTable(DataTablesInput input) {
        StringBuilder sb = new StringBuilder();
        sb.append(" select n.id as id,n.NAME as name,n.VALID_SIGN as validSign,n.STATUS as status, n.IS_SYS as isSys, ");
        sb.append(" n.CREATE_BY as createBy,n.CREATE_DATE as createDate,n.UPDATE_BY as updateBy,n.UPDATE_DATE as updateDate ");
        sb.append(" from NIS_PAY_SCHEDULING_ELEMENTS n where status =  "+ NetbankConstant.STATUE_CHECK);
        return getDynamicBySQL(input, sb.toString(), NisPaySchedulingElementsVO.class, "n", false);
    }
    /**
     * 查询排期要素
     * @return
     */
    public List<NisPaySchedulingElementsVO> list(){
        StringBuilder sb = new StringBuilder();
        sb.append(" select n.id as id,n.NAME as name,n.VALID_SIGN as validSign,n.STATUS as status, n.IS_SYS as isSys, ");
        sb.append(" n.CREATE_BY as createBy,n.CREATE_DATE as createDate,n.UPDATE_BY as updateBy,n.UPDATE_DATE as updateDate ");
        sb.append(" from NIS_PAY_SCHEDULING_ELEMENTS n where status =  "+ NetbankConstant.STATUE_CHECK+" and valid_sign = '1' ");
        return listBySQLAliasToBean(sb.toString(),null, NisPaySchedulingElementsVO.class);
    }
}
