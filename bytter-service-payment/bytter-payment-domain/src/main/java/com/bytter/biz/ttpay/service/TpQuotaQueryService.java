package com.bytter.biz.ttpay.service;
import com.bytter.biz.payment.api.constant.TtPayConstant;
import com.bytter.biz.ttpay.dao.TpQuotaQueryDao;
import com.bytter.biz.ttpay.entity.TpQuota;
import com.bytter.biz.ttpay.vo.TpQuotaVO;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.system.api.ICorpService;
import com.bytter.system.api.dto.CorpDTO;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Map;
/**
 * @author mof
 * @version 1.0
 * @Title: Service
 * @Description: 限额查询Service
 * @date 2022-05-05 15:43:05
 */
@Service("tpQuotaQueryService")
@Transactional
public class TpQuotaQueryService extends BaseService<TpQuota, String> {
    @DubboReference(check = false)
	private ICorpService corpService;
    @Autowired
    private TpQuotaQueryDao tpQuotaQueryDao;
    @Override
    public BaseDao<TpQuota, String> getEntityDao() {
        return tpQuotaQueryDao;
    }
    public DataTablesOutput getDataTablesUnion(DataTablesInput dataTablesInput) {
        //获取该用户有权限的单位id集
        List<String> corpAuthList = corpService.getUserCorpIds(TtPayConstant.TTPAY_RESOURCE_QUOTAQUERY_QUERY, dataTablesInput.getCurrentUserId());
        DataTablesOutput dataTablesOutput = tpQuotaQueryDao.getDataTablesUnion(dataTablesInput, corpAuthList);
        Map<String, CorpDTO> corpMap = corpService.getCorpMap();
        List<TpQuotaVO> list = dataTablesOutput.getData();
        for (TpQuotaVO vo:list) {
            if (corpMap.containsKey(vo.getCorpId())) {
                vo.setCorpName(corpMap.get(vo.getCorpId()).getName());
            }
        }
        return dataTablesOutput;
    }
}
