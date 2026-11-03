package com.bytter.biz.foreign.service;

import com.bytter.biz.foreign.dao.FexRateTypeDao;
import com.bytter.biz.foreign.entity.FexRateType;
import com.bytter.biz.foreign.vo.FexRateTypeVO;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.exception.ParameterException;
import com.bytter.core.utils.BeanUtils;
import com.bytter.core.utils.DateUtils;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.system.api.enums.BaseDataMessageEnum;
import com.bytter.biz.payment.api.enums.ForeignMessageEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * @author qinl
 * @version 1.0
 * @Title: Service
 * @Description: 外汇牌价类型表
 * @date 2020-07-09
 */
@Service("fexRateTypeService")
@Transactional
public class FexRateTypeService extends BaseService<FexRateType, String> {
    @Autowired
    private FexRateTypeDao fexRateTypeDao;

    @Override
    public BaseDao<FexRateType, String> getEntityDao() {
        return fexRateTypeDao;
    }

    public DataTablesOutput getDataTablesUnion(DataTablesInput dataTablesInput, ICurrentUser user) {
        return fexRateTypeDao.getDataTablesUnion(dataTablesInput, null);
    }

    public FexRateTypeVO getByVO(FexRateTypeVO vo){
        return fexRateTypeDao.getByVO(vo);
    }

    public List<FexRateTypeVO> getFexRateTypeList(FexRateTypeVO vo){
        return fexRateTypeDao.getFexRateTypeList(vo);
    }

    public FexRateTypeVO getById(String id){
        return fexRateTypeDao.getById(id);
    }

    public void save(FexRateTypeVO vo, ICurrentUser user) {
        FexRateType fexRateType = BeanUtils.copyNewInstanceProperties(vo, FexRateType.class);
        fexRateType.setCreateBy(user.getLoginName());
        fexRateType.setCreateDate(DateUtils.getDate());
        this.saveEntity(fexRateType);
    }

    /**
     * 新增数据校验
     */
    public void saveCheck(FexRateTypeVO vo,FexRateTypeVO vo2){
        //修改是否修改了名称
        if(!vo.getTypeName().equals(vo2.getTypeName())){
            //名称重复校验
            FexRateTypeVO query = new FexRateTypeVO();
            query.setTypeName(vo.getTypeName());
            FexRateTypeVO byTypeName = this.getByVO(query);
            if (byTypeName != null) {
                throw new ParameterException(ForeignMessageEnum.FEX_PRODUCT_MESSAGE_NAME_UNIQUE);
            }
        }

        //判断是否修改了类别和接口
        if(vo.getBifCode().equals(vo2.getBifCode()) && vo.getRateInterfaceType().equals(vo2.getRateInterfaceType())){
            //未修改
        }else{
            //同一接口同一类别唯一
            FexRateTypeVO query2 = new FexRateTypeVO();
            query2.setBifCode(vo.getBifCode());
            query2.setRateInterfaceType(vo.getRateInterfaceType());
            List<FexRateTypeVO> fexRateTypeList = fexRateTypeDao.getFexRateTypeList(query2);
            if (EmptyUtils.isNotEmpty(fexRateTypeList)) {
                throw new ParameterException(ForeignMessageEnum.FEX_RATE_TYPE_UNIQUE_CHECK);
            }
        }
    }

    public void deleteData(String id) {
//        deleteCheck(id);
        this.delete(id);
    }

    /**
     * 关联业务检查
     */
    private void deleteCheck(String id){
        Map<String,Map<String,String>> tableMap = new HashMap<>();
        Map<String,String> queryMap = new HashMap<>();

        tableMap.put("xxxxx",queryMap);
        queryMap.put("id","1241278461892412762732187");

        if (fexRateTypeDao.checkAssociated(id,tableMap) > 0) {
            throw new ParameterException(BaseDataMessageEnum.CMS_LOAN_DATA_REFERENCED_EXIST);
        }
    }

    public void update(FexRateTypeVO vo, ICurrentUser user) {


        FexRateType fexRateType = this.get(vo.getId());
        BeanUtils.copyNotNullProperties(vo, fexRateType);
        fexRateType.setUpdateBy(user.getLoginName());
        fexRateType.setUpdateDate(DateUtils.getDate());
        this.updateEntity(fexRateType);
    }

    /**
     * 根据外汇牌价接口类别获取外汇牌价类型
     * @param rateInterfaceType 外汇牌价接口类别 1-银企直联 2-网络机器人
     * @return
     */
    public List<FexRateTypeVO> listByRateInterfaceType(String rateInterfaceType){
        return fexRateTypeDao.listByRateInterfaceType(rateInterfaceType);
    }

    /**
     * 获取所有有效的外汇牌价类型
     * @return
     */
    public List<FexRateTypeVO> listFexRateTypeAll(){
        return fexRateTypeDao.listFexRateTypeAll();
    }
}
