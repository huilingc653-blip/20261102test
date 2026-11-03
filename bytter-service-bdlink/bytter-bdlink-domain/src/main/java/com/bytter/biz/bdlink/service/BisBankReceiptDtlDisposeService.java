package com.bytter.biz.bdlink.service;
import com.bytter.biz.bdlink.dao.BisBankReceiptDtlDisposeDao;
import com.bytter.biz.bdlink.entity.BisBankReceiptDtlDispose;

import com.bytter.biz.bdlink.vo.BisBankReceiptDtlDisposeVO;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.utils.DateUtils;
import com.bytter.core.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service("bisBankReceiptDtlDisposeService")
@Transactional
public class BisBankReceiptDtlDisposeService extends BaseService<BisBankReceiptDtlDispose, String> {
    @Autowired
    private BisBankReceiptDtlDisposeDao bisBankReceiptDtlDisposeDao;
    @Override
    public BaseDao<BisBankReceiptDtlDispose, String> getEntityDao() {
        return bisBankReceiptDtlDisposeDao;
    }
    public DataTablesOutput<BisBankReceiptDtlDisposeVO> getDataTablesUnion(DataTablesInput dataTablesInput) {
        return bisBankReceiptDtlDisposeDao.getDataTablesUnion(dataTablesInput);
    }
    /*
    保存
    */
    public void save(BisBankReceiptDtlDispose bisBankReceiptDtlDispose, ICurrentUser user) {
        bisBankReceiptDtlDispose.setIsEnable("1");
        bisBankReceiptDtlDispose.setCreateDate(DateUtils.getDate());
        bisBankReceiptDtlDispose.setCreateBy(user.getLoginName());
        bisBankReceiptDtlDispose.setUpdateDate(DateUtils.getDate());
        bisBankReceiptDtlDispose.setUpdateBy(user.getLoginName());
        super.saveEntity(bisBankReceiptDtlDispose);
    }
    /*
    根据id删除
     */
    public void delete(String id) {
        super.delete(id);
    }
    /*
    删除对象
     */
    public void deleteEntity(BisBankReceiptDtlDispose bisBankReceiptDtlDispose) {
        super.deleteEntity(bisBankReceiptDtlDispose);
    }
    /*
    修改更新
     */
    public void update(BisBankReceiptDtlDispose bisBankReceiptDtlDispose,ICurrentUser user) {
        bisBankReceiptDtlDispose.setUpdateDate(DateUtils.getDate());
        bisBankReceiptDtlDispose.setUpdateBy(user.getLoginName());
        super.updateEntity(bisBankReceiptDtlDispose);
    }
    /*
    根据id查询
     */
    public BisBankReceiptDtlDisposeVO EntityById(String id) {
        return bisBankReceiptDtlDisposeDao.getByIdUnion(id);
    }
    //查询是否配置过此银行字段 true:配置过  false:没有配置过
    public boolean bankTypeDate(String bankTypeId) {
        if (StringUtils.isNotEmpty(bisBankReceiptDtlDisposeDao.getByBankTypeId(bankTypeId))){
            return true;
        }else{
            return false;
        }
    }
}
