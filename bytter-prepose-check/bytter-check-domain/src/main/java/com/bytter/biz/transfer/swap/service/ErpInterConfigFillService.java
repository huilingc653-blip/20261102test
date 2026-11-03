package com.bytter.biz.transfer.swap.service;
import com.bytter.biz.dataexchange.basic.service.ErpDataTypeService;
import com.bytter.biz.transfer.swap.dao.ErpInterConfigFillDao;
import com.bytter.biz.transfer.swap.entity.ErpDataTypeField;
import com.bytter.biz.transfer.swap.entity.ErpInterConfigFill;
import com.bytter.biz.transfer.swap.vo.ErpInterConfigFillFormatVO;
import com.bytter.biz.transfer.swap.vo.ErpInterConfigFillVO;
import com.bytter.check.dto.ErpDataTypeDTO;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.core.constant.CommonConstant;
import com.bytter.core.utils.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.bytter.core.utils.StringUtils;
import java.util.ArrayList;
import java.util.List;
/**
 * Created by zhouj on 2018/5/28.
 */
@Transactional
@Service("erpInterConfigFillService")
public class ErpInterConfigFillService extends BaseService<ErpInterConfigFill,String> {
    @Autowired
    private ErpInterConfigFillDao erpInterConfigFillDao;
    @Autowired
    private ErpDataTypeFieldService erpDataTypeFieldService;
    @Autowired
    private ErpDataTypeService erpDataTypeService;
    @Override
    public BaseDao<ErpInterConfigFill, String> getEntityDao() {
        return erpInterConfigFillDao;
    }
    /**
     * 配置明细信息
     *
     * @return list集合
     */
    @Transactional(readOnly = true)
    public List<ErpInterConfigFillVO> listErpInterConfigFillInfoByType(String type) {
        if(StringUtils.isBlank(type)||"undefined".equals(type)){
            return new ArrayList<>();
        }
        return erpInterConfigFillDao.listErpInterConfigFillInfoByType(type);
    }
    /**
     * 基础数据类型
     * @return
     */
    @Transactional(readOnly = true)
    public List<ErpDataTypeDTO> listErpDataTypeByValidSign() {
        return erpDataTypeService.listErpDataTypeByValidSign(CommonConstant.VALID_SIGN_YES);
    }
    /**
     * 获取可配置的补录字段
     * @param type
     * @return
     */
    public List<ErpDataTypeField> queryErpDataTypeFieldValid(String type) {
        return erpDataTypeFieldService.queryErpDataTypeFieldValid(type);
    }
    /**
     * 获取配置好的补录字段
     * @param type
     * @return
     */
    public List<ErpInterConfigFill> queryErpFieldValid(String type) {
        return erpInterConfigFillDao.queryErpFieldValid(type);
    }
    /**
     * 添加补录配置
     */
    public void saveInterConfigFill(ErpInterConfigFillFormatVO erpInterConfigFillFormatVO, ICurrentUser userVo){
        ErpInterConfigFill entity;
        String type=erpInterConfigFillFormatVO.getType();
        erpInterConfigFillDao.deleteEntity(type);
        List<ErpInterConfigFill> configFills=erpInterConfigFillFormatVO.getErpInterConfigFillList();
        for (ErpInterConfigFill configFill : configFills) {
            entity=new ErpInterConfigFill();
            configFill.setType(type);
            BeanUtils.copyProperties(configFill, entity);
            erpInterConfigFillDao.saveOrUpdate(entity);
        }
    }
}
