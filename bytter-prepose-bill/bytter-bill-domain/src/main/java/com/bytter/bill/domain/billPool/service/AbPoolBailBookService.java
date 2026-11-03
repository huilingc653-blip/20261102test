package com.bytter.bill.domain.billPool.service;
import com.bytter.bill.domain.bill.service.AbCorpService;
import com.bytter.bill.domain.billPool.dao.AbPoolBailBookDao;
import com.bytter.bill.domain.billPool.entity.AbPoolBailBook;
import com.bytter.bill.api.billPool.constant.AbPoolConstant;
import com.bytter.bill.domain.billPool.vo.AbPoolBailBookVO;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
/**
 * @Title: AbPoolBailBookService
 * @Description: 保证金登记簿Service
 * @author wangzy
 * @date 2020/3/17 17:10
 *
 */
@Service("abPoolBailBookService")
@Transactional
public class AbPoolBailBookService  extends BaseService<AbPoolBailBook, String> {
    @Autowired
    private AbPoolBailBookDao abPoolBailBookDao;
    @Autowired
    private AbCorpService abCorpService;
    @Override
    public BaseDao<AbPoolBailBook, String> getEntityDao() {
        return abPoolBailBookDao;
    }
    /**
     * 保证金登记簿列表查询
     * @param input
     * @return
     */
    @SuppressWarnings("rawtypes")
    @Transactional(readOnly = true)
    public DataTablesOutput getDataTablesUnion(DataTablesInput input) {
        List<String> corpAuthList = abCorpService.getUserCorpIds(AbPoolConstant.AB_POLL_QUERY_BAIL_OP,input.getCurrentUser().getId());
        return abPoolBailBookDao.getDataTablesUnion(input, corpAuthList);
    }
    /**
     * 查看保证金登记簿详情
     * @param id 登记簿id
     * @return
     */
    @Transactional(readOnly = true)
    public AbPoolBailBookVO getAbPoolBailBookVOByDtlId(String id) {
        if (StringUtils.isEmpty(id)){
            return null;
        }
        AbPoolBailBookVO abPoolBailBookVO = abPoolBailBookDao.getAbPoolBailBookVOInfo(id);
        return abPoolBailBookVO;
    }
}
