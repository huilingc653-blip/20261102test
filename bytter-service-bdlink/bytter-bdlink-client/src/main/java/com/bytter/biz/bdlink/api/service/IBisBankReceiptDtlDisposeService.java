package com.bytter.biz.bdlink.api.service;
import com.bytter.biz.bdlink.entity.BisBankReceiptDtlDispose;
import com.bytter.biz.bdlink.vo.BisBankReceiptDtlDisposeVO;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
/**
 * 银行电子回单
 * @author jiangw
 * @since 2023/5/4 14:35
 */
public interface IBisBankReceiptDtlDisposeService {
    
    /**
     * 数据的获取
     * @param dataTablesInput DataTablesInput
     * @return DataTablesOutput
     * @author jiangw
     * @since 2023/5/4 14:39
     */
    public DataTablesOutput<BisBankReceiptDtlDisposeVO> getDataTablesUnion(DataTablesInput dataTablesInput);
    /**
     * 根据id查询
     * @param id String
     * @return BisBankReceiptDtlDisposeVO
     * @author jiangw
     * @since 2023/5/4 14:59
     */
    public BisBankReceiptDtlDisposeVO EntityById(String id);
    
    /**
     * 根据id查询
     * @param id String
     * @return BisBankReceiptDtlDispose
     * @author jiangw
     * @since 2023/5/4 15:19
     */
    public BisBankReceiptDtlDispose get(String id);
    
    /**
     * 查询是否配置过此银行字段 true:配置过  false:没有配置过
     * @param bankTypeId String
     * @return boolean
     * @author jiangw
     * @since 2023/5/4 15:20
     */
    public boolean bankTypeDate(String bankTypeId);
    
    /**
     * 保存
     * @param bisBankReceiptDtlDispose BisBankReceiptDtlDispose
     * @param user ICurrentUser
     * @return void
     * @author jiangw
     * @since 2023/5/4 15:23
     */
    public void save(BisBankReceiptDtlDispose bisBankReceiptDtlDispose, ICurrentUser user);
    /**
     * 删除对象
     * @param bisBankReceiptDtlDispose BisBankReceiptDtlDispose
     * @return void
     * @author jiangw
     * @since 2023/5/4 15:25
     */
    public void deleteEntity(BisBankReceiptDtlDispose bisBankReceiptDtlDispose);
    /**
     * 修改更新
     * @param bisBankReceiptDtlDispose BisBankReceiptDtlDispose
     * @param user ICurrentUser
     * @return void
     * @author jiangw
     * @since 2023/5/4 15:27
     */
    public void update(BisBankReceiptDtlDispose bisBankReceiptDtlDispose,ICurrentUser user);
    /**
     * 修改更新
     * @param bisBankReceiptDtlDispose BisBankReceiptDtlDispose
     * @return void
     * @author jiangw
     * @since 2023/5/4 16:13
     */
    public void updateEntity(BisBankReceiptDtlDispose bisBankReceiptDtlDispose);

    /**
     * 修改新增查询
     * @param id
     * @return
     */
    public String getById(String origvouchId);
}
