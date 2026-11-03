package com.bytter.basic.service;

import com.bytter.basic.dao.BisBifInitDao;
import com.bytter.basic.entity.BisBifInit;
import com.bytter.basic.vo.BisBifInitVO;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.constant.CommonConstant;
import com.bytter.core.utils.StringUtils;
import com.bytter.system.api.IBankTypeService;
import com.bytter.system.service.DictService;
import com.bytter.system.vo.DictVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author onlineGenerator
 * @version 1.0
 * @Title: Service
 * @Description: 接口代码维护
 * @date 2017-05-19 17:07:12
 */
@Service("bisBifInitServiceBase")
@Transactional
public class BisBifInitService extends BaseService<BisBifInit, String> {
    @Autowired
    private BisBifInitDao bisBifInitDao;
    @Autowired
    private IBankTypeService iBankTypeService;

    @Autowired
    private DictService dictService;

    /**
     * 删除对象
     *
     * @param entity
     */
    @Override
    public void deleteEntity(BisBifInit entity) {
        super.deleteEntity(entity);
    }
    /**
     * 根据id删除
     *
     * @param id
     */
    @Override
    public void delete(String id) {
        super.delete(id);
    }
    /**
     * 保存
     *
     * @param entity
     */
    public void save(BisBifInit entity) {
        super.saveEntity(entity);
    }
    /**
     * 更新
     *
     * @param entity
     */
    public void update(BisBifInit entity) {
        super.updateEntity(entity);
    }
    /**
     * 保存or更新
     *
     * @param entity
     */
    @Override
    public void saveOrUpdate(BisBifInit entity) {
        super.saveOrUpdate(entity);
    }
    @Override
    public BaseDao<BisBifInit, String> getEntityDao() {
        return bisBifInitDao;
    }
    /**
     * 获取有效的银行接口代码列表信息
     *
     * @param
     */
    public List<BisBifInit> getListByValidSign(String validSign) {
        if (StringUtils.isEmpty(validSign)) {
            return bisBifInitDao.listAll();
        } else {
            return bisBifInitDao.listBy("validSign", validSign);
        }
    }
    /**
     * 根据有效标志，银行类别过滤
     *
     * @param validSign
     * @param bankTypeId
     * @return
     */
    public List<BisBifInit> listBySignOrBankType(String validSign, String bankTypeId) {
        if (StringUtils.isEmpty(validSign)) {
            return bisBifInitDao.listBy("bankType", bankTypeId);
        } else {
            return bisBifInitDao.listBySignOrBankType(validSign, bankTypeId);
        }
    }
    /**
     * 判断是否存在多条
     *
     * @param bifCode
     * @param opType
     * @param id
     * @return
     */
    public boolean isExistMulByBifCode(String bifCode, String opType, String id) {
        boolean flag = false;
        List<BisBifInit> list = bisBifInitDao.listByBifCode(bifCode);
        if (list != null) {
            if (CommonConstant.SUBMIT_ADD.equals(opType) && list.size() > 0) {
                flag = true;
            } else if (CommonConstant.SUBMIT_MODIFY.equals(opType)) {
                if (list.size() > 0) {
                    if (!id.equals(list.get(0).getId())) {
                        flag = true;
                    }
                }
            }
        }
        return flag;
    }
    /**
     * 查询数据集
     *
     * @param dataTablesInput
     * @return
     */
    public DataTablesOutput getDataTablesUnion(DataTablesInput dataTablesInput) {
        DataTablesOutput dataTablesOutput= bisBifInitDao.getDataTablesUnion(dataTablesInput);
        if (dataTablesOutput != null && dataTablesOutput.getData() != null) {
            //有效标志
            Map<String, String> validSign = dictService.getDictGroupByCode("sf_yn").stream().collect(Collectors.toMap(DictVO::getCode, DictVO::getName));

            List<BisBifInitVO> bisBifInitVOList = dataTablesOutput.getData();
            for (BisBifInitVO bisBifInit : bisBifInitVOList) {
                if (!org.springframework.util.StringUtils.isEmpty(bisBifInit.getBankTypeId())) {
                    bisBifInit.setBankTypeName(iBankTypeService.getObjectById(bisBifInit.getBankTypeId()).getTypeName());
                }
                if (StringUtils.isNotBlank(bisBifInit.getValidSign())){
                    bisBifInit.setValidSignName(validSign.get(bisBifInit.getValidSign()));
                }
            }
        }
        return dataTablesOutput;
    }
    /**
     * 根据接口代码查找
     *
     * @param bifCode 接口代码
     * @return
     */
    public BisBifInit getByBifCode(String bifCode) {
        return this.bisBifInitDao.getByBifCode(bifCode);
    }
    /**
     * 根据接口代码名称查找
     *
     * @param name 接口代码名称
     * @return
     */
    public BisBifInit getByName(String name) {
        return this.bisBifInitDao.getByName(name);
    }
    /**
     * 根据接口id查找
     *
     * @param id
     * @return
     */
    public BisBifInit getById(String id) {
        return this.bisBifInitDao.getById(id);
    }

    /**
     * 根据ID集合获取有效接口信息。
     *
     * @param ids ID集合。
     * @return 接口信息。
     */
    public List<BisBifInit> listByIds(List<String> ids){
        return bisBifInitDao.listByIds(ids);
    }

    /**
     * 根据ID集合获取有效接口信息。
     *
     * @param codeList ID集合。
     * @return 接口信息。
     */
    public List<BisBifInit> listByCode(List<String> codeList){
        return bisBifInitDao.listByCode(codeList);
    }

    /**
     * 根据账号过滤银行类别
     *
     * @return
     */
    public List<BisBifInit> listBisBifInitFilterByBankAcc() {
        return this.bisBifInitDao.listBisBifInitFilterByBankAcc();
    }

    /**
     * 根据账号过滤银行类别
     *
     * @return
     */
    public List<BisBifInit> listBisBifInit() {
        return this.bisBifInitDao.listBisBifInit();
    }

    /**
    * @Description 获取银行类型的直连银行为'是'的银行接口信息
    * @author zhous
    * @date 2024/8/22 17:18
    */
    public List<BisBifInit> getDirectConnectBankType() {
        return bisBifInitDao.getDirectConnectBankType();
    }
}
