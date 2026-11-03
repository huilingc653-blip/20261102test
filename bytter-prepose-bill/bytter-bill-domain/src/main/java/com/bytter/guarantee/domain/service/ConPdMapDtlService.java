package com.bytter.guarantee.domain.service;
import com.bytter.guarantee.domain.dao.ConPdMapDtlDao;
import com.bytter.guarantee.domain.entity.ConPdMapdtl;
import com.bytter.guarantee.domain.vo.ConPdMapDtlVO;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.core.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
/**
 * @Title: ConPdMapDtlService
 * @Description: 合同类型要素映射明细Service
 * @author wangzy
 * @date 2019/11/9 16:17
 *
 */
@Service("conPdMapDtlService")
@Transactional
public class ConPdMapDtlService extends BaseService<ConPdMapdtl, String> {
    @Autowired
    private ConPdMapDtlDao conPdMapDtlDao;
    @Override
    public BaseDao<ConPdMapdtl, String> getEntityDao() {
        return conPdMapDtlDao;
    }
    /**
     * @Title: 合同关系映射明细保存 @param info 合同类型映射明细数据信息 @return boolean @throws
     */
    public boolean saveConPdMapdtl(ConPdMapdtl info) {
        this.saveEntity(info);
        return true;
    }
    /**
     * 根据映射id查询所有映射明细信息
     * 
     * @param pdCode
     * @return
     */
    @Transactional(readOnly = true)
    public List<ConPdMapDtlVO> listConPdMapDtlVO(String pdCode, String typeId) {
        if (StringUtils.isNotEmpty(pdCode) && StringUtils.isNotEmpty(typeId)) {
            return conPdMapDtlDao.listConPdMapDtlVO(pdCode, typeId);
        }
        return null;
    }
    /**
     * 查询合同类型对应的所有关键要素
     * 
     * @param conTypeId
     * @return
     */
    @Transactional(readOnly = true)
    public List<ConPdMapDtlVO> listTypeField(String conTypeId) {
        return conPdMapDtlDao.listTypeField(conTypeId);
    }
}
