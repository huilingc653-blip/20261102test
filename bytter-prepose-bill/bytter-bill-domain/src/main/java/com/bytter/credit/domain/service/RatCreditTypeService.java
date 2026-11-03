package com.bytter.credit.domain.service;

import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.exception.ParameterException;
import com.bytter.core.constant.CommonConstant;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.core.utils.StringUtils;
import com.bytter.credit.domain.dao.RatCreditTypeDao;
import com.bytter.credit.domain.entity.RatCreditType;
import com.bytter.credit.domain.vo.RatCreditTypeVO;
import com.bytter.credit.enums.RatMessageEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author onlineGenerator
 * @version 1.0
 * @Title: Service
 * @Description: 授信额度类型
 * @date 2017-07-20 14:54:51
 */
@Service("ratCreditTypeService")
@Transactional
public class RatCreditTypeService extends BaseService<RatCreditType, String> {
    @Autowired
    private RatCreditTypeDao ratCreditTypeDao;
    @Autowired
    private RatBkrationDisService disService;

    /**
     * 删除对象
     *
     * @param entity
     */
    public void deleteEntity(RatCreditType entity) {
        super.deleteEntity(entity);
    }

    /**
     * 根据id删除
     *
     * @param id
     */
    public void delete(String id) {
        super.delete(id);
    }

    /**
     * 保存
     *
     * @param entity
     */
    public void save(RatCreditType entity) {
        super.saveEntity(entity);
    }

    /**
     * 更新
     *
     * @param entity
     */
    public void update(RatCreditType entity) {
        super.updateEntity(entity);
    }

    /**
     * 保存or更新
     *
     * @param entity
     */
    public void saveOrUpdate(RatCreditType entity) {
        super.saveOrUpdate(entity);
    }

    /**
     * 功能介绍 : 删除授信额度类型前判断。<br>
     * <b>日期：</b>2017年9月18日<br>
     *
     * @version 1.0
     * @author liyh
     */
    public void deleteCreditType(String id) {
        RatCreditType entity = get(id, true);
        // 是否被分配引用
        // 检查授信品种是否被引用
        if (disService.checkCreditTypeUse(id)) {
            throw new ParameterException(RatMessageEnum.CMS_RAT_CREDITTYPE_USE);
        }
        entity.setStatus(CommonConstant.STATUE_DELETE);
        // 未被引用删除
        merge(entity);
    }

    /**
     * 功能介绍 : 保存或更新授信额度类型。<br>
     * <b>日期：</b>2017年7月20日<br>
     *
     * @version 1.0
     * @author liyh
     */
    public void saveOrUpdateCreditType(RatCreditType entity) {
        if (StringUtils.isEmpty(entity.getId())) {
            // 查询编号是否存在相应的授信品种类型
            String code = entity.getCode();
            if (StringUtils.isEmpty(code)) {
                throw new ParameterException(RatMessageEnum.CMS_RAT_CODE_NULL);
            }
            List<RatCreditType> itemTypes = ratCreditTypeDao.getCreditTypeByCode(code);
            if (EmptyUtils.isNotEmpty(itemTypes)) {
                throw new ParameterException(RatMessageEnum.CMS_RAT_CODE_EXIST);
            }
            this.save(entity);
            this.getEntityDao().flush();
        } else {
            this.update(entity);
        }
    }

    public BaseDao<RatCreditType, String> getEntityDao() {
        return ratCreditTypeDao;
    }

    /**
     * 功能介绍 : 获取授信额度类型列表。<br>
     * <b>日期：</b>2017年8月1日<br>
     *
     * @version 1.0
     * @author liyh
     */
    @Transactional(readOnly = true)
    public List<RatCreditTypeVO> getAllCreditType() {
        List<RatCreditTypeVO> list = ratCreditTypeDao.getAllCreditType();
        return list;
    }

    /**
     * 查询数据列表
     */
    @Transactional(readOnly = true)
    public DataTablesOutput dataTables(DataTablesInput input) {
        return ratCreditTypeDao.dataTables(input);
    }
}
