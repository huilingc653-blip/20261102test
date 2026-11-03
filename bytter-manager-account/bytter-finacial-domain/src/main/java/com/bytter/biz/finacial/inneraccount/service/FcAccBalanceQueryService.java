package com.bytter.biz.finacial.inneraccount.service;

import com.bytter.biz.finacial.inneraccount.dao.FcAccBalanceQueryDao;
import com.bytter.biz.finacial.inneraccount.vo.FcAccBalanceQueryVO;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.system.api.IAccountGrantService;
import com.bytter.system.api.ICorpService;
import com.bytter.system.api.IInnerAccountService;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
/**
 * @author onlineGenerator
 * @version 1.0
 * @Title: Service
 * @Description: 内部账户余额
 * @date 2017-06-28 10:36:12
 */
@Service("fcAccBalanceQueryService")
@Transactional
public class FcAccBalanceQueryService extends BaseService<FcAccBalanceQueryVO, String> {
    @Autowired
    private FcAccBalanceQueryDao fcAccBalanceQueryDao;
    @DubboReference(check = false)
	private ICorpService corpService;
    @DubboReference(check = false)
	private IAccountGrantService accountGrantService;
    @DubboReference(check = false)
	private IInnerAccountService innerAccountService;
    @Override
    public BaseDao<FcAccBalanceQueryVO, String> getEntityDao() {
        return fcAccBalanceQueryDao;
    }
    /**
     * 查询内部账号余额列表
     *
     * @param dataTablesInput
     * @return
     * 上层没有被调用，注释
     */
    /*@SuppressWarnings("rawtypes")
    public DataTablesOutput getDataTablesUnion(DataTablesInput dataTablesInput, String userId) {
        StringBuilder joinSql = new StringBuilder();
        //单位权限
        String corpAuthSql = SqlUtil.getInSql(
                corpService.getUserCorpIds(ResourceConstant.NETBANK_INNERACCOUNT_FCACCBALANCE_QUERY, userId), "fa.corp_Id",
                true);
        joinSql.append(" and ").append(corpAuthSql);
        List<String> accountGrantList = accountGrantService.listByMultiValAccIds(userId, ResourceConstant.NETBANK_INNERACCOUNT_FCACCBALANCE_QUERY, null,
                AccountGrantConstant.INNER_BANK_TYPE, AccountGrantConstant.QUERY_BALANCE);
        List<FcAccDTO> listFcAcc = innerAccountService.listFcAccInfo(accountGrantList, true);
        //增加记录的显示,包括申请注销,冻结等
        List<FcAccDTO> accounts = new ArrayList<>();
        if (!EmptyUtils.isEmptyList(listFcAcc)){
            for(FcAccDTO fcAccDTO:listFcAcc){
                if(InnerAccountConstant.ACC_STATE_NORMAL.equals(fcAccDTO.getAccState())
                        || InnerAccountConstant.ACC_STATE_REQUISITION.equals(fcAccDTO.getAccState())
                        || InnerAccountConstant.ACC_STATE_ALL_FREEZE.equals(fcAccDTO.getAccState())
                        || InnerAccountConstant.ACC_STATE_PART_FREEZE.equals(fcAccDTO.getAccState())
                        || InnerAccountConstant.ACC_STATE_F.equals(fcAccDTO.getAccState())
                        || InnerAccountConstant.ACC_STATE_EDIT.equals(fcAccDTO.getAccState())
                        || InnerAccountConstant.ACC_STATE_G.equals(fcAccDTO.getAccState())
                        || InnerAccountConstant.ACC_STATE_Z.equals(fcAccDTO.getAccState())
                        || InnerAccountConstant.ACC_STATE_S.equals(fcAccDTO.getAccState())
                        || InnerAccountConstant.ACC_STATE_U.equals(fcAccDTO.getAccState())
                        || InnerAccountConstant.ACC_STATE_V.equals(fcAccDTO.getAccState())
                        || InnerAccountConstant.ACC_STATE_DESTORY.equals(fcAccDTO.getAccState())){
                    accounts.add(fcAccDTO);
                }
            }
        }
        List<String> list = new ArrayList<>();
        if (accounts == null) {
            List<FcAccBalanceQueryVO> listVO = new ArrayList<>();
            return new DataTablesOutput(dataTablesInput.getDraw(), listVO, 0L, 0L);
        }
        for (FcAccDTO acc : accounts) {
            list.add(acc.getBlnctrAcc());
        }
        Map<String, Object> params = new HashMap();
        String accountsTemp = SqlUtil.getInSqlByParams(list, "fc.account", params);
        joinSql.append(" and ").append(accountsTemp);
        //获取余额表数据
        DataTablesOutput dataTablesUnion = fcAccBalanceQueryDao.getDataTablesUnion(dataTablesInput, joinSql.toString(), params);
        //修改余额表超链接逻辑
        this.balanceLink(dataTablesUnion,joinSql,params);
        return dataTablesUnion;
    }*/

    /**
     * 查询资金池内部账号余额列表
     *
     * @param dataTablesInput
     * @return
     * 没有被调用，注释
     */
   /*@SuppressWarnings("rawtypes")
    public DataTablesOutput getDataTablesUnionForPool(DataTablesInput dataTablesInput, String userId) {
        StringBuilder joinSql = new StringBuilder();
        //单位权限
        String corpAuthSql = SqlUtil.getInSql(
                corpService.getUserCorpIds(ResourceConstant.NETBANK_INNERACCOUNT_FCACCBALANCE_POOL_QUERY, userId), "fa.corp_Id",
                true);
        joinSql.append(" and ").append(corpAuthSql);
        //内部账号查询余额权限
        List<String> corps = corpService.getUserCorpIds(ResourceConstant.NETBANK_INNERACCOUNT_FCACCBALANCE_POOL_QUERY, userId);
        List<String> accountIds = new ArrayList<>();
        for (String corp : corps) {
            accountIds.addAll(accountGrantService.listByMultiVal(userId, corp, ResourceConstant.NETBANK_INNERACCOUNT_FCACCBALANCE_POOL_QUERY,
                    null, AccountGrantConstant.POOLINNER_BANK_TYPE, AccountGrantConstant.QUERY_BALANCE));
        }
        //增加记录的显示,包括申请注销,冻结等
        List<String> accTypes = new ArrayList<>();
        accTypes.add(InnerAccountConstant.ACC_STATE_NORMAL);
        accTypes.add(InnerAccountConstant.ACC_STATE_REQUISITION);
        accTypes.add(InnerAccountConstant.ACC_STATE_ALL_FREEZE);
        accTypes.add(InnerAccountConstant.ACC_STATE_PART_FREEZE);
        accTypes.add(InnerAccountConstant.ACC_STATE_F);
        accTypes.add(InnerAccountConstant.ACC_STATE_EDIT);
        accTypes.add(InnerAccountConstant.ACC_STATE_G);
        accTypes.add(InnerAccountConstant.ACC_STATE_Z);
        accTypes.add(InnerAccountConstant.ACC_STATE_S);
        accTypes.add(InnerAccountConstant.ACC_STATE_U);
        accTypes.add(InnerAccountConstant.ACC_STATE_V);
        accTypes.add(InnerAccountConstant.ACC_STATE_DESTORY);
        List<FcAccDTO> accounts = innerAccountService.listFcAccInfo(accountIds, accTypes);
        List<String> list = new ArrayList<>();
        if (accounts == null) {
            List<FcAccBalanceQueryVO> listVO = new ArrayList<>();
            return new DataTablesOutput(dataTablesInput.getDraw(), listVO, 0L, 0L);
        }
        for (FcAccDTO acc : accounts) {
            list.add(acc.getBlnctrAcc());
        }
        Map<String, Object> params = new HashMap();
        String accountsTemp = SqlUtil.getInSqlByParams(list, "fc.account", params);
        joinSql.append(" and ").append(accountsTemp);
        return fcAccBalanceQueryDao.getDataTablesUnionForPool(dataTablesInput, joinSql.toString(), params);
    }*/
    /**
     * 查询已关联活期账号下的定期或通知账号
     *
     * @param dataTablesInput 数据源
     * @param account         已关联活期账号
     * @param userId          登录用户ID
     * @return 数据列表
     * 没有被调用，注释
     */
    /*public DataTablesOutput getAccountDtl(DataTablesInput dataTablesInput, String account, String userId) {
        Map<String, Object> formdata = dataTablesInput.getFormdata();
        formdata.put("likes_relationAcc", account);
        dataTablesInput.setFormdata(formdata);
        return getDataTablesUnion(dataTablesInput, userId);
    }*/
    /**
     * 为账号余额表添加超链接
     * @param output 待修改的数据源
     * @param joinSql 单位权限sql
     * @param params 参数
     * @return
     * 没有被调用，注释
     */
    /*private void balanceLink(DataTablesOutput output ,StringBuilder joinSql,Map<String,Object> params) {
        List<FcAccBalanceQueryVO> data = output.getData();
        //遍历已经关联的活期账号
        Set<String> set = new HashSet<>();
        for(FcAccBalanceQueryVO fcAccBalanceQueryVO : fcAccBalanceQueryDao.selectList(joinSql, params,false)){
            //过滤企业协定账号
            if(!InnerAccountConstant.FCACC_AGREEMENT.equals(fcAccBalanceQueryVO.getAccType())){
                set.add(fcAccBalanceQueryVO.getRelationAcc());
            }
        }
        //修改需要显示链接的数据
        for(FcAccBalanceQueryVO fcAccBalanceQueryVO : data){
            if(set.contains(fcAccBalanceQueryVO.getAccount())){
                fcAccBalanceQueryVO.setShowLink(true);
            }
        }
        output.setData(data);
    }*/
    /**
     * 获取单个账号的余额信息
     * @param account
     * @return
     * 没有被调用，注释
     */
    /*public FcAccBalanceQueryVO getFcAccBalByAccount(String account) {
        return fcAccBalanceQueryDao.getFcAccBalByAccount(account);
    }*/
}
