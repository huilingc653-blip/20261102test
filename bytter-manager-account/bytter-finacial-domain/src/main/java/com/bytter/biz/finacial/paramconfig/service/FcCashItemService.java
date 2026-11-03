package com.bytter.biz.finacial.paramconfig.service;
import com.bytter.biz.finacial.paramconfig.dao.FcCashItemDao;
import com.bytter.biz.finacial.paramconfig.entity.FcCashItem;
import com.bytter.biz.finacial.paramconfig.vo.FcCashItemVo;
import com.bytter.biz.finacial.paramconfig.vo.FcVoucherCashVo;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.exception.ParameterException;
import com.bytter.core.utils.DateUtils;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.core.constant.CommonConstant;
import com.bytter.biz.finacial.api.enums.paramconfig.ParamConfigMessageEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
/**
 * @author
 * @version V1.0
 * @Title: dao
 * @Description: 现金流量项目服务
 * @date 2020-09-10 09:30:39
 */
@Service("fcCashItemService")
@Transactional
public class FcCashItemService extends BaseService<FcCashItem, String> {
    @Autowired
    private FcCashItemDao cashItemDao;
    @Autowired(required = false)
    private FcVoucherCashService voucherCashService;
    @Override
    public BaseDao<FcCashItem, String> getEntityDao() {
        return cashItemDao;
    }
    public DataTablesOutput listCashItem(DataTablesInput input) {
        return cashItemDao.listCashItem(input);
    }
    /**
    * <br>
    * <b>功能：</b> 现金流量科目保存更新 <br>
    * <b>作者：</b>liyh<br>
    * <b>日期：</b> 2020/9/10 0010 <br>
    */
    public void submitCashItem(FcCashItem cashItem, ICurrentUser user) {
        //校验是否重复
        List<FcCashItemVo> itemList = cashItemDao.queryCashItem(cashItem);
        if (!EmptyUtils.isEmptyList(itemList)) {
            throw new ParameterException(ParamConfigMessageEnum.FIN_PARAMCONFIG_MESSAGE_ITEM_EXISTS);
        }
        if (EmptyUtils.isEmpty(cashItem.getId())) {
            cashItem.setCreateBy(user.getId());
            cashItem.setCreateTime(DateUtils.getDate());
        } else {
            //            失效时检查是否被引用
            if (CommonConstant.NO.equals(cashItem.getValidSign())) {
                List<FcVoucherCashVo> fcVoucherCashVos = voucherCashService.queryUsedItem(cashItem.getId());
                if (EmptyUtils.isNotEmpty(fcVoucherCashVos)) {
                    throw new ParameterException(ParamConfigMessageEnum.FIN_PARAMCONFIG_MESSAGE_ITEM_ISEMPLOY);
                }
            }
            cashItem.setUpdateBy(user.getId());
            cashItem.setUpdateTime(DateUtils.getDate());
        }
        cashItemDao.saveOrUpdate(cashItem);
    }
    /**
    * <br>
    * <b>功能：</b> 现金流量科目数据删除 <br>
    * <b>作者：</b>liyh<br>
    * <b>日期：</b> 2020/9/10 0010 <br>
    */
    public void deleteCashItem(String cashItemId, ICurrentUser user) {
        //未被现金流量项目科目关系引用
        List<FcVoucherCashVo> itemList = voucherCashService.queryUsedItem(cashItemId);
        if (EmptyUtils.isNotEmpty(itemList)) {
            throw new ParameterException(ParamConfigMessageEnum.FIN_PARAMCONFIG_MESSAGE_ITEM_ISEMPLOY);
        }
        cashItemDao.delete(cashItemId);
    }
    /**
     *查询所有有效现金流项目
     * @return
     */
    public List<FcCashItemVo> getAllCashItem(){
        return cashItemDao.getAllCashItem(null);
    }
}
