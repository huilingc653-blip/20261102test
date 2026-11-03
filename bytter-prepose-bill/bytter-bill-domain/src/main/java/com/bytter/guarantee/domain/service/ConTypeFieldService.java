package com.bytter.guarantee.domain.service;
import com.bytter.guarantee.domain.dao.ConTypeFieldDao;
import com.bytter.guarantee.domain.entity.ConTypeField;
import com.bytter.guarantee.domain.vo.ConTypeFieldVO;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.core.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
/**
 * @author zhenghj
 * @version 1.0
 * @Title: 合同类型字段明细业务Service
 * @Description: 合同类型字段明细业务处理类
 * @date 2019-11-06
 */
@Service("conTypeFieldService")
@Transactional
public class ConTypeFieldService extends BaseService<ConTypeField, String> {
    @Autowired
    private ConLogService log;
    @Autowired
    private ConTypeFieldDao conTypeFieldDao;
    @Override
    public BaseDao<ConTypeField, String> getEntityDao() {
        return null;
    }
    /**
     * 功能介绍 : 根据合同类型id获取有效的合同类型字段明细表<br>
     * <b>日期：</b>2019年11月06日<br>
     * 
     * @version 1.0
     * @author zhenghj
     */
    @Transactional(readOnly = true)
    public List<ConTypeFieldVO> getConTypeFieldById(String id) {
        if (StringUtils.isNotEmpty(id)) {
            return conTypeFieldDao.getConTypeFieldById(id);
        }
        return null;
    }
}
