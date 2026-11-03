package com.bytter.biz.netbank.service;
import com.bytter.biz.netbank.dao.NisPaySchedulingElementsDao;
import com.bytter.biz.netbank.entity.NisPaySchedulingElements;
import com.bytter.biz.netbank.vo.NisPaySchedulingElementsVO;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
/**
 *付款排期要素
 */
@Service("nisPaySchedulingElementsService")
@Transactional
public class NisPaySchedulingElementsService extends BaseService<NisPaySchedulingElements, String> {
    @Autowired
    private NisPaySchedulingElementsDao nisPaySchedulingElementsDao;
    @Override
    public BaseDao<NisPaySchedulingElements, String> getEntityDao() {
        return nisPaySchedulingElementsDao;
    }
    /**
     * 查询付款排期要素列表
     *
     * @param input
     * @return
     */
    @SuppressWarnings("rawtypes")
    public DataTablesOutput<NisPaySchedulingElementsVO> dataTable(DataTablesInput input) {
        return nisPaySchedulingElementsDao.dataTable(input);
    }
    /**
     * 查询排期要素
     * @return
     */
    public List<NisPaySchedulingElementsVO> list(){
        return nisPaySchedulingElementsDao.list();
    }
}
