package com.bytter.increment.domain.service;
import cn.hutool.core.util.ObjectUtil;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.utils.DateUtils;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.core.utils.SqlUtil;
import com.bytter.increment.api.constant.ChoProductReqConstant;
import com.bytter.increment.api.constant.ResourceConstant;
import com.bytter.increment.domain.dao.ChoProductMaintainDao;
import com.bytter.increment.domain.entity.ChoProductReq;
import com.bytter.increment.domain.vo.ChoProductReqVo;
import com.bytter.system.api.IBankTypeService;
import com.bytter.system.api.IBtCurrencyService;
import com.bytter.system.api.ICorpService;
import com.bytter.system.api.IDictService;
import com.bytter.system.api.dto.BankTypeDTO;
import com.bytter.system.api.dto.BtCurrencyDTO;
import com.bytter.system.api.dto.DictDTO;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Optional;
/**
 * 理财维护service
 *
 * @author ouyj
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class ChoProductMaintainService extends BaseService<ChoProductReq, String> {
    @Autowired
    private ChoProductMaintainDao choProductMaintainDao;
    @DubboReference(check = false)
	private ICorpService corpService;
    @DubboReference(check = false)
    private IDictService dictService;
    @DubboReference(check = false)
    private IBtCurrencyService currencyService;
    @DubboReference(check = false)
    private IBankTypeService bankTypeService;
    @Override
    public BaseDao<ChoProductReq, String> getEntityDao() {
        return choProductMaintainDao;
    }
    /**
     * 理财维护查询页数据
     *
     * @param dataTablesInput 前端传参
     * @param user            用户
     * @return 前端数据
     */
    public DataTablesOutput getDataTablesUnion(DataTablesInput dataTablesInput, ICurrentUser user) {
        String moneyUint = (String) dataTablesInput.getFormdata().get("moneyUnit");
        if (EmptyUtils.isEmpty(moneyUint)) {
            moneyUint = ChoProductReqConstant.COMMON_MONEYUNIT_1;
        }
        StringBuilder sql = new StringBuilder();
        String corpAuthSql = SqlUtil.getInSql(
                corpService.getUserCorpIds(ResourceConstant.CHO_PRODUCTMAINTAIN_CHOPRODUCTMAINTAIN_QUERY, user.getId()), "pr.corp_id",
                true);
        sql.append(" and ").append(corpAuthSql);
        DataTablesOutput output = choProductMaintainDao.getDataTablesUnion(dataTablesInput, sql.toString());
        // 注入远程信息
        Map<String, DictDTO> listStatusMap = dictService.listMap("choBankFinance.status");
        Map<String, DictDTO> listTypeMap = dictService.listMap("financial.type");
        Map<String, DictDTO> listPayWayMap = dictService.listMap("financial.payWay");
        List<ChoProductReqVo> list = output.getData();
        for (ChoProductReqVo product : list) {
            setBeanMoneyType(product, moneyUint);
            if (ObjectUtil.isAllNotEmpty(product.getStatus(), listStatusMap)) {
                Optional.ofNullable(listStatusMap.get(product.getStatus() + "")).ifPresent(dict -> product.setStatusName(dict.getName()));
            }
            if (ObjectUtil.isAllNotEmpty(product.getFinancialType(), listTypeMap)) {
                Optional.ofNullable(listTypeMap.get(product.getFinancialType())).ifPresent(dict -> product.setFinancialTypeName(dict.getName()));
            }
            if (ObjectUtil.isAllNotEmpty(product.getPayWay(), listPayWayMap)) {
                Optional.ofNullable(listPayWayMap.get(product.getPayWay())).ifPresent(dict -> product.setPayWayName(dict.getName()));
            }
        }
        output.setData(list);
        return output;
    }
    /**
     * 金额单位转换
     *
     * @param reqVo     vo
     * @param moneyType 单位
     */
    private void setBeanMoneyType(ChoProductReqVo reqVo, String moneyType) {
        if (ChoProductReqConstant.COMMON_MONEYUNIT_1.equals(moneyType)) {
            return;
        }
        Class userCla = (Class) reqVo.getClass();
        /*
         * 得到类中的所有属性集合
         */
        try {
            Field[] fs = userCla.getDeclaredFields();
            for (int i = 0; i < fs.length; i++) {
                Field f = fs[i];
                f.setAccessible(true); //设置些属性是可以访问的
                Object val = f.get(reqVo);// 得到此属性的值
                String type = f.getType().toString();// 得到此属性的类型
                String name = f.getName();//得到属性的名称
                if (type.endsWith("BigDecimal") && name.endsWith("Money")) {
                    if (null == val) {
                        f.set(reqVo, BigDecimal.ZERO);
                    } else {
                        BigDecimal value = (BigDecimal) val;
                        f.set(reqVo, value.divide(BigDecimal.valueOf(Integer.valueOf(moneyType)), 6, BigDecimal.ROUND_HALF_UP));
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    /**
     * 更新对象
     *
     * @param entity 理财登记实体
     * @param user   当前用户
     */
    public void toModify(ChoProductReq entity, ICurrentUser user) {
        String corpName=corpService.getCorpName(entity.getCorpId());
        entity.setCorpName(corpName);
        BtCurrencyDTO btCurrencyDTO = currencyService.getObjectById(entity.getCurId());
        entity.setCurName(btCurrencyDTO.getCurName());
        BankTypeDTO bankTypeDTO = bankTypeService.getObjectById(entity.getBankTypeId());
        entity.setBankTypeName(bankTypeDTO.getTypeName());
        entity.setUpdateBy(user.getLoginName());
        entity.setUpdateDate(DateUtils.getDate());
        entity.setBizStatus(ChoProductReqConstant.BIZ_STATUS4);
        this.saveOrUpdate(entity);
    }
}
