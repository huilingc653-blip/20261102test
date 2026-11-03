package com.bytter.guarantee.domain.service;
import com.bytter.guarantee.domain.dao.ConPdMapDao;
import com.bytter.guarantee.domain.dao.ConTypeDao;
import com.bytter.guarantee.domain.entity.ConPdMap;
import com.bytter.guarantee.domain.entity.ConPdMapdtl;
import com.bytter.guarantee.domain.vo.ConPdMapVO;
import com.bytter.guarantee.domain.vo.ConTypeVO;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.utils.BeanUtils;
import com.bytter.core.utils.DateUtils;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.core.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
/**
 * @Title: ConPdMapService
 * @Description: 合同类型要素映射Service
 * @author wangzy
 * @date 2019/11/8 13:35
 *
 */
@Service("conPdMapService")
@Transactional
public class ConPdMapService extends BaseService<ConPdMap, String> {
    @Autowired
    private ConPdMapDao conPdMapDao;
    @Autowired
    private ConPdMapDtlService conPdMapDtlService;
    @Autowired
    private ConTypeDao conTypeDao;
    @Override
    public BaseDao<ConPdMap, String> getEntityDao() {
        return conPdMapDao;
    }
    /**
     * 合同类型要素映射列表查询
     * 
     * @param input
     * @return
     *
     */
    @SuppressWarnings("rawtypes")
    @Transactional(readOnly = true)
    public DataTablesOutput getDataTablesUnion(DataTablesInput input) {
        return conPdMapDao.getDataTablesUnion(input);
    }
    /**
     * @Title: 合同关系映射保存 @param infoVO 合同类型映射数据信息 @param user 用户 @return 处理结果 @throws
     */
    public boolean saveOrUpdateConPdMapDtl(ConPdMapVO infoVO, ICurrentUser user) {
        if (EmptyUtils.isEmpty(infoVO) || EmptyUtils.isEmpty(infoVO.getConTypeDtlIds())
                || EmptyUtils.isEmpty(infoVO.getPdDtlCodes())) {
            return false;
        }
        // 映射不为空时为修改，否则为新增
        ConPdMap conPdMap = new ConPdMap();
        ConPdMapVO conPdMapVO = conPdMapDao.getConPdMapVO(infoVO.getTypeId(), infoVO.getPdCode());
        if (StringUtils.isNotEmpty(conPdMapVO)) {
            BeanUtils.copyNotNullProperties(conPdMapVO, conPdMap);
            conPdMap.setUpdateTime(DateUtils.getDate());
            conPdMap.setUpdateId(user.getId());
            this.updateEntity(conPdMap);
            // 先将明细表中数据删除，然后进行新增
            conPdMapDao.deletePdDtlByMapId(conPdMapVO.getId());
        } else {
            // 先保存产品类型和合同类型
            BeanUtils.copyNotNullProperties(infoVO, conPdMap);
            conPdMap.setCreateId(user.getId());
            conPdMap.setCreateTime(DateUtils.getDate());
            this.saveEntity(conPdMap);
        }
        // 保存映射表明细数据
        ConPdMapdtl conPdMapdtl = null;
        String[] conTypeDtlIds = infoVO.getConTypeDtlIds();
        String[] pdDtlCodes = infoVO.getPdDtlCodes();
        for (int i = 0; i < conTypeDtlIds.length; i++) {
            conPdMapdtl = new ConPdMapdtl();
            conPdMapdtl.setPdDtlCode(pdDtlCodes[i]);
            conPdMapdtl.setTypeDtlId(conTypeDtlIds[i]);
            conPdMapdtl.setMapId(conPdMap.getId());
            conPdMapDtlService.saveConPdMapdtl(conPdMapdtl);
        }
        return true;
    }
    /**
     * @Title: 合同关系映射删除 @param id 合同类型映射表id @return 处理结果 @throws
     */
    public boolean deleteConPdMapAndDtl(String id) {
        if (StringUtils.isNotEmpty(id)) {
            // 先删除映射明细表中数据
            conPdMapDao.deletePdDtlByMapId(id);
            // 删除映射表中数据
            this.delete(id);
            return true;
        }
        return false;
    }
    /**
     * 查询合同类型信息
     * 
     * @param id
     *            合同类型ID
     * @return
     *
     */
    public ConTypeVO getConTypeVO(String id) {
        ConTypeVO conTypeVO = conTypeDao.getConTypeVOByID(id);
        return conTypeVO;
    }
}
