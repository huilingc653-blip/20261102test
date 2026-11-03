package com.bytter.biz.foreign.service;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import com.bytter.biz.foreign.dao.FexTranReqFieldDao;
import com.bytter.biz.foreign.entity.FexTranReqField;
import com.bytter.biz.foreign.vo.FexTranReqFieldVo;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.core.constant.CommonConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * @Title: Entity
 * @Description: 交易请求字段配置表
 * @author wuxc
 * @date 2026-05-11
 */
@Service("fexTranReqFieldService")
public class FexTranReqFieldService extends BaseService<FexTranReqField, String> {

    @Autowired
    private FexTranReqFieldDao fexTranReqFieldDao;


    @Override
    public BaseDao<FexTranReqField, String> getEntityDao() {
        return fexTranReqFieldDao;
    }


    public List<FexTranReqFieldVo> getAllValidFields() {
        List<FexTranReqFieldVo> fexTranReqFieldVos = new ArrayList<>();
        List<FexTranReqField> fexTranReqFields = fexTranReqFieldDao.listBy("validSign", CommonConstant.VALID_SIGN_YES);

        if (!CollectionUtil.isEmpty(fexTranReqFields)) {
            fexTranReqFields.sort(Comparator.comparingInt(FexTranReqField::getFieldSort));
            fexTranReqFieldVos = BeanUtil.copyToList(fexTranReqFields, FexTranReqFieldVo.class);
        }
        return fexTranReqFieldVos;
    }
}
