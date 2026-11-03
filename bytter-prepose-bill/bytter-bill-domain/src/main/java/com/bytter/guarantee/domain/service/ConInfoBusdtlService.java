package com.bytter.guarantee.domain.service;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.bytter.guarantee.domain.dao.ConInfoBusdtlDao;
import com.bytter.guarantee.domain.entity.ConInfoBusDtl;
import com.bytter.guarantee.domain.vo.ConInfoBusdtlVO;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.core.utils.StringUtils;
/**
 * @author zhenghj
 * @version 1.0
 * @Title: 合同字段明细业务Service
 * @Description: 合同字段明细业务处理类
 * @date 2019-11-04
 */
@Service("conInfoBusdtlService")
@Transactional
public class ConInfoBusdtlService extends BaseService<ConInfoBusDtl, String> {
    @Autowired
    private ConInfoBusdtlDao conInfoBusdtlDao;
    public BaseDao<ConInfoBusDtl, String> getEntityDao() {
        return conInfoBusdtlDao;
    }
    /**
     * 通过参数获取字段明细业务信息
     * 
     * @param conBusdtlVO
     * @return
     */
    @Transactional(readOnly = true)
    public List<ConInfoBusDtl> getConInfoBusDtl(ConInfoBusdtlVO conBusdtlVO) {
        Map<String, Object> params = new HashMap<>();
        if (StringUtils.isNotEmpty(conBusdtlVO.getMid())) {
            params.put("mid", conBusdtlVO.getMid());
        }
        return conInfoBusdtlDao.getConInfoBusDtlByParams(params);
    }
    /**
     * 根据主表id删除字段明细信息
     * 
     * @param mid
     *            主表id
     */
    public void deleteConInfoBusDtlByMid(String mid) {
        if (StringUtils.isEmpty(mid)) {
            return;
        }
        conInfoBusdtlDao.deleteConInfoBusDtlByMid(mid);
    }
}
