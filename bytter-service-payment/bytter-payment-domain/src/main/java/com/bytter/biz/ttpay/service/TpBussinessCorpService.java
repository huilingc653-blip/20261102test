package com.bytter.biz.ttpay.service;
import com.bytter.biz.ttpay.dao.TpBussinessCorpDao;
import com.bytter.biz.ttpay.entity.TpBussinessCorp;
import com.bytter.biz.ttpay.vo.TpBussinessCorpVO;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.system.api.ICorpService;
import com.bytter.system.api.dto.CorpDTO;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Map;
@Service("tpBussinessCorpService")
@Transactional
public class TpBussinessCorpService  extends BaseService<TpBussinessCorp, String> {
    @Autowired
    private TpBussinessCorpDao tpBussinessCorpDao;
    @DubboReference(check = false)
    private ICorpService corpService;
    public List<TpBussinessCorpVO> getByBussnissId(String purposeId) {
        List<TpBussinessCorpVO> list = this.tpBussinessCorpDao.getByBussnissId(purposeId);
        Map<String, CorpDTO> corpMap = corpService.getCorpMap();
        for (TpBussinessCorpVO vo:list) {
            if (corpMap.containsKey(vo.getCorpId())) {
                vo.setCorpName(corpMap.get(vo.getCorpId()).getName());
            }
        }
        return list;
    }
    public void deleteTpBussnissId(String purposeId) {
        this.tpBussinessCorpDao.deleteTpBussnissId(purposeId);
    }
    @Override
    public BaseDao<TpBussinessCorp, String> getEntityDao() {
        return tpBussinessCorpDao;
    }
}
