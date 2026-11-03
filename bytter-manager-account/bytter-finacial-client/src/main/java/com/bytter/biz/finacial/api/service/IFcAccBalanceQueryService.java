package com.bytter.biz.finacial.api.service;
import java.util.Map;
import com.bytter.biz.finacial.api.vo.FcAccBalanceQueryVO;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
/**
 * @author onlineGenerator
 * @version 1.0
 * @Title: Service
 * @Description: 内部账户余额
 * @date 2023-05-08 10:36:12
 */
public interface IFcAccBalanceQueryService {
    /**
     * 查询内部账号余额列表
     *
     * @param dataTablesInput
     * @return
     */
    public DataTablesOutput getDataTablesUnion(DataTablesInput dataTablesInput, String userId);
    /**
     * 查询资金池内部账号余额列表
     *
     * @param dataTablesInput
     * @return
     */
    public DataTablesOutput getDataTablesUnionForPool(DataTablesInput dataTablesInput, String userId);
    /**
     * 查询已关联活期账号下的定期或通知账号
     *
     * @param dataTablesInput 数据源
     * @param account         已关联活期账号
     * @param userId          登录用户ID
     * @return 数据列表
     */
    public DataTablesOutput getAccountDtl(DataTablesInput dataTablesInput, String account, String userId);
    /**
     * 为账号余额表添加超链接
     * @param output 待修改的数据源
     * @param joinSql 单位权限sql
     * @param params 参数
     * @return
     */
    public void balanceLink(DataTablesOutput output ,StringBuilder joinSql,Map<String,Object> params);
    /**
     * 获取单个账号的余额信息
     * @param account
     * @return
     */
    public FcAccBalanceQueryVO getFcAccBalByAccount(String account);
}
