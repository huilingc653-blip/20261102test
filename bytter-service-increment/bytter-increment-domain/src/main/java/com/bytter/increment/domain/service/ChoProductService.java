package com.bytter.increment.domain.service;
import cn.hutool.core.util.ObjectUtil;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.increment.domain.dao.ChoProductDao;
import com.bytter.increment.domain.entity.ChoProduct;
import com.bytter.increment.domain.entity.ChoProductReq;
import com.bytter.increment.domain.vo.ChoProductVo;
import com.bytter.system.api.IBankTypeService;
import com.bytter.system.api.IBtCurrencyService;
import com.bytter.system.api.IDictService;
import com.bytter.system.api.dto.BankTypeDTO;
import com.bytter.system.api.dto.BtCurrencyDTO;
import com.bytter.system.api.dto.DictDTO;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Map;
import java.util.Optional;
/**
 * @author shenc
 * @version 1.0
 * @Title: Service
 * @Description: 理财产品
 * @date 2019-03-26 14:03:30
 */
@Service("choProductService")
@Transactional
public class ChoProductService extends BaseService<ChoProduct, String> {
    @Autowired
    private ChoProductDao choProductDao;
    @DubboReference(check = false)
    private IDictService dictService;
    @DubboReference(check = false)
    private IBtCurrencyService btCurrencyService;
    @DubboReference(check = false)
    private IBankTypeService bankTypeService;
    @Override
    public BaseDao<ChoProduct, String> getEntityDao() {
        return choProductDao;
    }
    /**
     * 保存对象
     *
     * @param entity
     */
    public void save(ChoProduct entity) {
        super.saveEntity(entity);
    }
    /**
     * 更新对象
     *
     * @param entity
     */
    public void update(ChoProduct entity) {
        super.updateEntity(entity);
    }
    /**
     * 保存或更新
     *
     * @param entity
     */
    @Override
    public void saveOrUpdate(ChoProduct entity) {
        super.saveOrUpdate(entity);
    }
    /**
     * 删除对象
     *
     * @param entity
     */
    @Override
    public void deleteEntity(ChoProduct entity) {
        delete(entity.getId());
    }
    /**
     * 根据Id删除
     *
     * @param id
     */
    @Override
    public void delete(String id) {
        ChoProduct entity = this.get(id);
        entity.setValidSign("0");
        this.update(entity);
    }
    /**
     * 新增理财产品。
     *
     * @param entity 理财产品数据。
     */
    public void saveChoProduct(ChoProduct entity) {
        redundant(entity);
        this.save(entity);
    }
    /**
     * 数据冗余。
     *
     * @param entity 理财产品。
     */
    private void redundant(ChoProduct entity) {
        // 银行类型冗余
        Optional.ofNullable(entity.getBankTypeId()).ifPresent(bankTypeId -> {
            BankTypeDTO bankTypeDTO = bankTypeService.getObjectById(bankTypeId);
            Optional.ofNullable(bankTypeDTO).ifPresent(bankType -> entity.setBankTypeName(bankType.getTypeName()));
        });
        // 币种冗余
        Optional.ofNullable(entity.getCurId()).ifPresent(curId -> {
            BtCurrencyDTO currencyDTO = btCurrencyService.getObjectById(curId);
            Optional.ofNullable(currencyDTO).ifPresent(currency -> entity.setCurName(currency.getCurName()));
        });
    }
    /**
     * 修改理财产品数据。
     *
     * @param entity 理财产品。
     */
    public void modifyChoProduct(ChoProduct entity) {
        redundant(entity);
        this.saveOrUpdate(entity);
    }
    public DataTablesOutput getDataTablesUnion(DataTablesInput input) {
        DataTablesOutput<ChoProductVo> dataTablesUnion = choProductDao.getDataTablesUnion(input);
        // 注入远程信息
        Optional.ofNullable(dataTablesUnion).ifPresent(dataTables -> injectValues(dataTables.getData()));
        return dataTablesUnion;
    }
    /**
     * 注入远程信息。
     *
     * @param listData 理财产品列表。
     */
    private void injectValues(List<ChoProductVo> listData) {
        Optional.ofNullable(listData).ifPresent(data -> {
            Map<String, DictDTO> listProductMap = dictService.listMap("cho.productType");
            Map<String, DictDTO> listTypeMap = dictService.listMap("financial.type");
            data.forEach(product -> {
                if (ObjectUtil.isAllNotEmpty(product.getProductType(), listProductMap)) {
                    Optional.ofNullable(listProductMap.get(product.getProductType())).ifPresent(dict -> product.setProductTypeName(dict.getName()));
                }
                if (ObjectUtil.isAllNotEmpty(product.getFinancialType(), listTypeMap)) {
                    Optional.ofNullable(listTypeMap.get(product.getFinancialType())).ifPresent(dict -> product.setFinancialTypeName(dict.getName()));
                }
            });
        });
    }
    /**
     * 根据产品代码查找产品对象
     *
     * @param code
     * @return
     */
    public ChoProduct findProductByCode(String code) {
        return choProductDao.findProductByCode(code);
    }
    /**
     * 根据产品类型和银行类型或者产品列表
     *
     * @param productType
     * @param bankType
     * @return
     */
    public List<ChoProduct> getProductsByType(String productType, String bankType) {
        return choProductDao.getProductsByType(productType, bankType);
    }
    /**
     * 查询所有状态为有效的产品
     *
     * @return
     */
    public List<ChoProductVo> listValidSignAll(String financialType) {
        List<ChoProductVo> listData = choProductDao.listValidSignAll(financialType);
        // 注入远程信息
        injectValues(listData);
        return listData;
    }

    public List<ChoProductVo> listValidSignAll(String financialType, String productType) {
        List<ChoProductVo> listData = choProductDao.listValidSignAll(financialType, productType);
        // 注入远程信息
        injectValues(listData);
        return listData;
    }
    /**
     * 根据产品代码查询产品类型
     *
     * @param productCode 产品代码
     * @return 产品类型
     */
    public ChoProductVo getByCode(String productCode) {
        return choProductDao.getByCode(productCode);
    }
    /**
     * 校验是否存在产品代码、产品类型、产品名称、银行类别、币种一致的产品类型
     *
     * @param choProductReq 理财登记vo
     * @return 产品类型
     */
    public ChoProductVo checkIsRepeat(ChoProductReq choProductReq) {
        return choProductDao.checkIsRepeat(choProductReq);
    }
    /**
     * 根据产品代码删除产品类型
     *
     * @param productCode 产品代码
     */
    public void delByCode(String productCode) {
        choProductDao.delByCode(productCode);
    }
}
