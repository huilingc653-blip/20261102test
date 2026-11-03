package com.bytter.bill.domain.billPool.service;
import com.bytter.bill.domain.bill.service.AbCorpService;
import com.bytter.bill.domain.billPool.dao.AbPoolFinaApplyBookDao;
import com.bytter.bill.api.billPool.constant.AbPoolConstant;
import com.bytter.bill.domain.billPool.vo.AbPoolDtlBookVO;
import com.bytter.bill.domain.billPool.vo.AbPoolFinaApplyBookVO;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.utils.ListUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
/**
 * @author zhenghj
 * @version 1.0
 * @Title: Service
 * @Description: 票据池出票融资申请明细账簿信息DO
 * @date 2020-04-14
 */
@Service("abPoolFinaApplyBookService")
@Transactional
public class AbPoolFinaApplyBookService extends BaseService<AbPoolFinaApplyBookVO, String> {
    @Autowired
    private AbPoolFinaApplyBookDao abPoolFinaApplyBookDao;
    @Autowired
    private AbCorpService abCorpService;
    public BaseDao<AbPoolFinaApplyBookVO, String> getEntityDao() {
        return abPoolFinaApplyBookDao;
    }
    /**
     * "查询票据池出票融资账簿信息列表数据
     *
     * @param dataTablesInput
     * @return
     */
    @SuppressWarnings("rawtypes")
    @Transactional(readOnly = true)
    public DataTablesOutput datatableQueryBook(DataTablesInput dataTablesInput) {
        List<String> corpAuthList = abCorpService.getUserCorpIds(AbPoolConstant.AB_POLL_QUERY_FINANCING_OP, dataTablesInput.getCurrentUserId());
        return abPoolFinaApplyBookDao.datatableQueryBook(dataTablesInput, corpAuthList);
    }
    /**
     * "查询票据池出票融资账簿明细账簿列表数据
     *
     * @param dataTablesInput
     * @return
     */
    @SuppressWarnings("rawtypes")
    @Transactional(readOnly = true)
    public DataTablesOutput datatableQueryDtl(DataTablesInput dataTablesInput) {
        return abPoolFinaApplyBookDao.datatableQueryDtl(dataTablesInput);
    }
    /**
     * 查询票据池明细账簿信息列表数据
     *
     * @param dataTablesInput
     * @return
     */
    @SuppressWarnings("rawtypes")
    @Transactional(readOnly = true)
    public DataTablesOutput datatableQuery(DataTablesInput dataTablesInput) {
        List<String> corpAuthList = abCorpService.getUserCorpIds(AbPoolConstant.AB_POLL_QUERY_FINANCING_OP, dataTablesInput.getCurrentUserId());
        return abPoolFinaApplyBookDao.datatableQuery(dataTablesInput, corpAuthList);
    }
    /**
     * 根据ID获取明细账簿信息
     *
     * @param id
     * @return
     */
    @Transactional(readOnly = true)
    public AbPoolDtlBookVO getAbPoolDtlBookVOById(String id) {
        List<AbPoolDtlBookVO> list = abPoolFinaApplyBookDao.getAbPoolDtlBookVOById(id);
        if (ListUtils.isNotNullOrEmpty(list)) {
            return list.get(0);
        }
        return null;
    }
}
