package com.bytter.biz.inneraccount.service;

import com.alibaba.nacos.common.utils.CollectionUtils;
import com.bytter.biz.finacial.api.service.IFcOnlineLedgerService;
import com.bytter.biz.finacial.api.vo.FcOnlineLedgerVO;
import com.bytter.biz.inneraccount.vo.FcAccBalanceQueryVO;
import com.bytter.biz.payment.api.constant.InnerAccountConstant;
import com.bytter.biz.bdlink.api.constant.ResourceConstant;
import com.bytter.core.common.datatable.Column;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.datatable.Order;
import com.bytter.core.constant.CommonConstant;
import com.bytter.core.utils.BeanUtils;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.core.utils.SqlUtil;
import com.bytter.core.utils.StringUtils;
import com.bytter.system.api.IAccountGrantService;
import com.bytter.system.api.ICorpService;
import com.bytter.system.api.IInnerAccountService;
import com.bytter.system.api.constant.AccountGrantConstant;
import com.bytter.system.api.dto.FcAccDTO;
import com.bytter.system.api.vo.FcAccVO;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.util.*;
/**
 * @author onlineGenerator
 * @version 1.0
 * @Title: Service
 * @Description: 内部账户余额
 * @date 2017-06-28 10:36:12
 */
@Service("fcAccBalanceQueryService")
@Transactional
public class FcAccBalanceQueryService {
    @DubboReference(check = false)
	private ICorpService corpService;
    @DubboReference(check = false)
	private IAccountGrantService accountGrantService;
    @DubboReference(check = false)
	private IInnerAccountService innerAccountService;
    @Autowired
    private FcAccManageService fcAccService;
    @DubboReference(check = false)
    IFcOnlineLedgerService fcOnlineLedgerService;
    private List<FcAccDTO>  listFcAccInfo(String userId){
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
        return accounts;
    }
    /**
     * 查询内部账号余额列表
     *
     * @param dataTablesInput
     * @return
     * 上层没有被调用，注释
     */
    @SuppressWarnings("rawtypes")
    public DataTablesOutput getDataTablesUnion(DataTablesInput dataTablesInput, String userId,String version) {
        List<FcAccDTO> accounts = listFcAccInfo(userId);
        List<String> list = new ArrayList<>();
        if (accounts == null) {
            List<FcAccBalanceQueryVO> listVO = new ArrayList<>();
            return new DataTablesOutput(dataTablesInput.getDraw(), listVO, 0L, 0L);
        }
        for (FcAccDTO acc : accounts) {
            list.add(acc.getBlnctrAcc());
        }
        Map<String, Object> params = new HashMap();
        StringBuilder joinSql = new StringBuilder();
        //单位权限
        String corpAuthSql = SqlUtil.getInSql(
                corpService.getUserCorpIds(ResourceConstant.NETBANK_INNERACCOUNT_FCACCBALANCE_QUERY, userId), "t.corp_Id",
                true);
        joinSql.append(" and ").append(corpAuthSql);
        String accountsTemp = SqlUtil.getInSqlByParams(list, "t.blnctr_Acc", params);
        joinSql.append(" and ").append(accountsTemp);
        //获取余额表数据
        DataTablesOutput dataTablesUnion = null;
        //原始版本
        if(StringUtils.isEmpty(version) || "0".equals(version)){
            dataTablesUnion = getDataTablesUnion(dataTablesInput, joinSql.toString(), params);
        }
        //前后端分离版本
        else{
            dataTablesUnion = getDataTablesUnionNew(dataTablesInput, joinSql.toString(), params);
        }
        //修改余额表超链接逻辑
        this.balanceLink(dataTablesUnion,joinSql.toString(),params);
        return dataTablesUnion;
    }
    public DataTablesOutput getDataTablesUnion(DataTablesInput dataTablesInput, String joinSql, Map<String, Object> params) {
        // dataTablesInput条件处理
        String poolSign = (String) dataTablesInput.getFormdata().get("poolSign");
        String ins_corpId = (String) dataTablesInput.getFormdata().get("ins_corpId");
        String eqs_subjectId = (String) dataTablesInput.getFormdata().get("eqs_subjectId");
        String likes_account = (String) dataTablesInput.getFormdata().get("likes_account");
        String eqs_curId = (String) dataTablesInput.getFormdata().get("eqs_curId");
        String eqsCorpId= (String) dataTablesInput.getFormdata().get("eqs_corpId");

        FcAccVO fcAcc = new FcAccVO();
        fcAcc.setPoolSign(poolSign);
        if (StringUtils.isNotEmpty(ins_corpId)) {
            String[] corpIds = ins_corpId.split(CommonConstant.COMMA_SPLIT);
            joinSql+=" and "+SqlUtil.getInSql(corpIds, "t.corp_id", true);
        }
        if (StringUtils.isNotEmpty(likes_account)) {
            joinSql+=" and t.blnctr_Acc like '%" + likes_account + "%'";
        }
        if (StringUtils.isNotEmpty(eqs_subjectId)) {
            fcAcc.setSubjectId(eqs_subjectId);
        }
        if (StringUtils.isNotEmpty(eqs_curId)) {
            fcAcc.setCurId(eqs_curId);
        }
        if (StringUtils.isNotEmpty(eqsCorpId)) {
            fcAcc.setCorpId(eqsCorpId);
        }
        int pageSize = dataTablesInput.getPageSize();
        dataTablesInput.setPageSize(1000);
        List<FcAccVO> fcAccVOList = fcAccService.getFcAccList(fcAcc, joinSql, params);
        dataTablesInput.setPageSize(pageSize);
        List<FcAccBalanceQueryVO> voAll=new ArrayList<>();
        for (FcAccVO accVO:fcAccVOList) {
            FcAccBalanceQueryVO vo = new FcAccBalanceQueryVO();
            BeanUtils.copyNotNullProperties(accVO, vo);
            vo.setAccount(accVO.getBlnctrAcc());
            FcOnlineLedgerVO fcOnlineLedger = fcOnlineLedgerService.getEntityByAccount(vo.getAccount());
            if (null != fcOnlineLedger) {
                BeanUtils.copyNotNullProperties(fcOnlineLedger, vo);
                vo.setCheckBalance(fcOnlineLedger.getCheckBalance());
                if(!EmptyUtils.isEmpty(fcOnlineLedger.getCheckBalance())) {
                    vo.setAvailableBalance(fcOnlineLedger.getCheckBalance().subtract(fcOnlineLedger.getFreezeMoney()));
                }else{
                    vo.setAvailableBalance(BigDecimal.ZERO);
                }
            }
            if(StringUtils.isEmpty(vo.getSubjectName())){
                vo.setSubjectName("");
            }
            if(null ==vo.getOverdraftMoney()){
                vo.setOverdraftMoney(BigDecimal.ZERO);
            }
            voAll.add(vo);
        }

        //列表排序
        int orderSize =dataTablesInput.getOrder().size();
        for(int i=0;i<orderSize;i++) {
            Order order = dataTablesInput.getOrder().get(i);
            if (0 == order.getColumn() && "asc".equals(order.getDir())) {
                Collections.sort(voAll, Comparator.comparing(FcAccBalanceQueryVO::getAccount));
            } else if (0 == order.getColumn() && "desc".equals(order.getDir())) {//倒序
                Collections.sort(voAll, Comparator.comparing(FcAccBalanceQueryVO::getAccount).reversed());
            }
            if (1 == order.getColumn() && "asc".equals(order.getDir())) {
                Collections.sort(voAll, Comparator.comparing(FcAccBalanceQueryVO::getAccName));
            } else if (1 == order.getColumn() && "desc".equals(order.getDir())) {//倒序
                Collections.sort(voAll, Comparator.comparing(FcAccBalanceQueryVO::getAccName).reversed());
            }else if (2 == order.getColumn() && "asc".equals(order.getDir())) {
                Collections.sort(voAll, Comparator.comparing(FcAccBalanceQueryVO::getSubjectName));
            } else if (2 == order.getColumn() && "desc".equals(order.getDir())) {//倒序
                Collections.sort(voAll, Comparator.comparing(FcAccBalanceQueryVO::getSubjectName).reversed());
            }else if (3 == order.getColumn() && "asc".equals(order.getDir())) {
                Collections.sort(voAll, Comparator.comparing(FcAccBalanceQueryVO::getCurName));
            } else if (3 == order.getColumn() && "desc".equals(order.getDir())) {//倒序
                Collections.sort(voAll, Comparator.comparing(FcAccBalanceQueryVO::getCurName).reversed());
            }else if (4 == order.getColumn() && "asc".equals(order.getDir())) {
                Collections.sort(voAll, Comparator.comparing(FcAccBalanceQueryVO::getCheckBalance));
            } else if (4 == order.getColumn() && "desc".equals(order.getDir())) {//倒序
                Collections.sort(voAll, Comparator.comparing(FcAccBalanceQueryVO::getCheckBalance).reversed());
            }else if (5 == order.getColumn() && "asc".equals(order.getDir())) {
                Collections.sort(voAll, Comparator.comparing(FcAccBalanceQueryVO::getAvailableBalance));
            } else if (5 == order.getColumn() && "desc".equals(order.getDir())) {//倒序
                Collections.sort(voAll, Comparator.comparing(FcAccBalanceQueryVO::getAvailableBalance).reversed());
            }else if (6 == order.getColumn() && "asc".equals(order.getDir())) {
                Collections.sort(voAll, Comparator.comparing(FcAccBalanceQueryVO::getOverdraftMoney));
            } else if (6 == order.getColumn() && "desc".equals(order.getDir())) {//倒序
                Collections.sort(voAll, Comparator.comparing(FcAccBalanceQueryVO::getOverdraftMoney).reversed());
            }else if (7 == order.getColumn() && "asc".equals(order.getDir())) {
                Collections.sort(voAll, Comparator.comparing(FcAccBalanceQueryVO::getAccOpenTime));
            } else if (7 == order.getColumn() && "desc".equals(order.getDir())) {//倒序
                Collections.sort(voAll, Comparator.comparing(FcAccBalanceQueryVO::getAccOpenTime).reversed());
            }else if (8 == order.getColumn() && "asc".equals(order.getDir())) {
                Collections.sort(voAll, Comparator.comparing(FcAccBalanceQueryVO::getDeadlineDate, Comparator.nullsLast(Date::compareTo)));
            } else if (8 == order.getColumn() && "desc".equals(order.getDir())) {//倒序
                Collections.sort(voAll, Comparator.comparing(FcAccBalanceQueryVO::getDeadlineDate, Comparator.nullsLast(Date::compareTo)).reversed());
            }else if (9 == order.getColumn() && "asc".equals(order.getDir())) {
                Collections.sort(voAll, Comparator.comparing(FcAccBalanceQueryVO::getInterestRate, Comparator.nullsLast(BigDecimal::compareTo)));
            } else if (9 == order.getColumn() && "desc".equals(order.getDir())) {//倒序
                Collections.sort(voAll, Comparator.comparing(FcAccBalanceQueryVO::getInterestRate, Comparator.nullsLast(BigDecimal::compareTo)).reversed());
            }
        }
        // 组装数据
        Integer count = dataTablesInput.getStart();
        Integer first = dataTablesInput.getStart();
        Integer maxResult = dataTablesInput.getStart() + dataTablesInput.getLength();
        if(maxResult>voAll.size()){
            maxResult=voAll.size();
        }
        DataTablesOutput dataTablesOutput=new  DataTablesOutput ();
        List<FcAccBalanceQueryVO> voList=new ArrayList<>();
        for (;count >= first && count < maxResult ;count++ ) {
            FcAccBalanceQueryVO vo = voAll.get(count);
            voList.add(vo);
        }
        return new DataTablesOutput(dataTablesInput.getDraw(), voList, fcAccVOList.size(), fcAccVOList.size());
    }

    /**
     * 前后端分离查询
     * @param dataTablesInput
     * @param joinSql
     * @param params
     * @return
     */
    public DataTablesOutput getDataTablesUnionNew(DataTablesInput dataTablesInput, String joinSql, Map<String, Object> params) {
        // dataTablesInput条件处理
        String poolSign = (String) dataTablesInput.getFormdata().get("poolSign");
        String ins_corpId = (String) dataTablesInput.getFormdata().get("ins_corpId");
        String eqs_subjectId = (String) dataTablesInput.getFormdata().get("eqs_subjectId");
        String likes_account = (String) dataTablesInput.getFormdata().get("likes_account");
        String eqs_curId = (String) dataTablesInput.getFormdata().get("eqs_curId");
        String eqsCorpId= (String) dataTablesInput.getFormdata().get("eqs_corpId");

        FcAccVO fcAcc = new FcAccVO();
        fcAcc.setPoolSign(poolSign);
        if (StringUtils.isNotEmpty(ins_corpId)) {
            String[] corpIds = ins_corpId.split(CommonConstant.COMMA_SPLIT);
            joinSql+=" and "+SqlUtil.getInSql(corpIds, "t.corp_id", true);
        }
        if (StringUtils.isNotEmpty(likes_account)) {
            joinSql+=" and t.blnctr_Acc like '%" + likes_account + "%'";
        }
        if (StringUtils.isNotEmpty(eqs_subjectId)) {
            fcAcc.setSubjectId(eqs_subjectId);
        }
        if (StringUtils.isNotEmpty(eqs_curId)) {
            fcAcc.setCurId(eqs_curId);
        }
        if (StringUtils.isNotEmpty(eqsCorpId)) {
            fcAcc.setCorpId(eqsCorpId);
        }
        int pageSize = dataTablesInput.getPageSize();
        dataTablesInput.setPageSize(1000);
        List<FcAccVO> fcAccVOList = fcAccService.getFcAccList(fcAcc, joinSql, params);
        dataTablesInput.setPageSize(pageSize);
        List<FcAccBalanceQueryVO> voAll=new ArrayList<>();
        for (FcAccVO accVO:fcAccVOList) {
            FcAccBalanceQueryVO vo = new FcAccBalanceQueryVO();
            BeanUtils.copyNotNullProperties(accVO, vo);
            vo.setAccount(accVO.getBlnctrAcc());
            FcOnlineLedgerVO fcOnlineLedger = fcOnlineLedgerService.getEntityByAccount(vo.getAccount());
            if (null != fcOnlineLedger) {
                BeanUtils.copyNotNullProperties(fcOnlineLedger, vo);
                vo.setCheckBalance(fcOnlineLedger.getCheckBalance());
                if(!EmptyUtils.isEmpty(fcOnlineLedger.getCheckBalance())) {
                    vo.setAvailableBalance(fcOnlineLedger.getCheckBalance().subtract(fcOnlineLedger.getFreezeMoney()));
                }else{
                    vo.setAvailableBalance(BigDecimal.ZERO);
                }
            }
            if(StringUtils.isEmpty(vo.getSubjectName())){
                vo.setSubjectName("");
            }
            if(null ==vo.getOverdraftMoney()){
                vo.setOverdraftMoney(BigDecimal.ZERO);
            }
            voAll.add(vo);
        }

        //这里前后端分离的column只有当 排序时才会加入 排序
        List<Column> columns = dataTablesInput.getColumns();
        List<Order> orderList = dataTablesInput.getOrder();
        for (int i = 0; i < columns.size(); i++) {
            Column  column = columns.get(i);
            String orderSort = orderList.get(i).getDir();
            if (column != null && column.getName() != null && !column.getName().isEmpty()) {
                sortListByFieldName(voAll, column.getName(), orderSort);
            }
        }
        // 组装数据
        Integer count = dataTablesInput.getStart();
        Integer first = dataTablesInput.getStart();
        Integer maxResult = dataTablesInput.getStart() + dataTablesInput.getLength();
        if(maxResult>voAll.size()){
            maxResult=voAll.size();
        }
        DataTablesOutput dataTablesOutput=new  DataTablesOutput ();
        List<FcAccBalanceQueryVO> voList=new ArrayList<>();
        for (;count >= first && count < maxResult ;count++ ) {
            FcAccBalanceQueryVO vo = voAll.get(count);
            voList.add(vo);
        }
        return new DataTablesOutput(dataTablesInput.getDraw(), voList, fcAccVOList.size(), fcAccVOList.size());
    }

    /**
     * 查询资金池内部账号余额列表
     *
     * @param dataTablesInput
     * @return
     * 没有被调用，注释
     */
   @SuppressWarnings("rawtypes")
    public DataTablesOutput getDataTablesUnionForPool(DataTablesInput dataTablesInput, String userId) {
        StringBuilder joinSql = new StringBuilder();
        //单位权限
        String corpAuthSql = SqlUtil.getInSql(
                corpService.getUserCorpIds(ResourceConstant.NETBANK_INNERACCOUNT_FCACCBALANCE_POOL_QUERY, userId), "t.corp_Id",
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
        List<FcAccDTO> accounts = listFcAccInfo(accountIds, accTypes);
        List<String> list = new ArrayList<>();
        if (accounts == null) {
            List<FcAccBalanceQueryVO> listVO = new ArrayList<>();
            return new DataTablesOutput(dataTablesInput.getDraw(), listVO, 0L, 0L);
        }
        for (FcAccDTO acc : accounts) {
            list.add(acc.getBlnctrAcc());
        }
        Map<String, Object> params = new HashMap();
        String accountsTemp = SqlUtil.getInSqlByParams(list, "t.blnctr_acc", params);
        joinSql.append(" and ").append(accountsTemp);
        return getDataTablesUnion(dataTablesInput, joinSql.toString(), params);
    }
    /**
     * 根据账号ID列表,查询账号列表
     * @param ids
     * @param accState
     * @return
     */
    public List<FcAccDTO> listFcAccInfo(List<String> ids, List<String> accState){
        List<FcAccDTO> list=fcAccService.listFcAccInfo(ids,accState);
        if (CollectionUtils.isNotEmpty(list)){
            for(FcAccDTO fcAcc:list){
                FcOnlineLedgerVO fcOnlineLedgerVO = fcOnlineLedgerService.getEntityByAccount(fcAcc.getBlnctrAcc());
                fcAcc.setFcBalance(fcOnlineLedgerVO.getCheckBalance().subtract(fcOnlineLedgerVO.getFreezeMoney()));
            }
        }

        return list;
    }
    /**
     * 查询已关联活期账号下的定期或通知账号
     *
     * @param dataTablesInput 数据源
     * @param account         已关联活期账号
     * @param userId          登录用户ID
     * @return 数据列表
     * 没有被调用，注释
     */
    public DataTablesOutput getAccountDtl(DataTablesInput dataTablesInput, String account, String userId) {
        Map<String, Object> formdata = dataTablesInput.getFormdata();
        formdata.put("likes_relationAcc", account);
        dataTablesInput.setFormdata(formdata);
        return getDataTablesUnion(dataTablesInput, userId,"0");
    }
    /**
     * 为账号余额表添加超链接
     * @param output 待修改的数据源
     * @param joinSql 单位权限sql
     * @param params 参数
     * @return
     * 没有被调用，注释
     */
    private void balanceLink(DataTablesOutput output ,String joinSql,Map<String,Object> params) {
        List<FcAccBalanceQueryVO> data = output.getData();
        //遍历已经关联的活期账号
        Set<String> set = new HashSet<>();
        for(FcAccBalanceQueryVO fcAccBalanceQueryVO : data){
            // 非注销账号
            if(!"ACC_STATE_DESTORY".equals(fcAccBalanceQueryVO.getAccState()) ){
                //过滤企业协定账号
                if (!InnerAccountConstant.FCACC_AGREEMENT.equals(fcAccBalanceQueryVO.getAccType())) {
                    set.add(fcAccBalanceQueryVO.getRelationAcc());
                }
            }
        }
        //修改需要显示链接的数据
        for(FcAccBalanceQueryVO fcAccBalanceQueryVO : data){
            if(set.contains(fcAccBalanceQueryVO.getAccount())){
                fcAccBalanceQueryVO.setShowLink(true);
            }
        }
        output.setData(data);
    }
    public List<FcAccBalanceQueryVO> selectList(String joinSql, Map<String, Object> params,Boolean poolFlag) {
        DataTablesOutput dataTablesOutput=new  DataTablesOutput ();
        List<FcAccBalanceQueryVO> voList=new ArrayList<>();
        FcAccVO fcAcc = new FcAccVO();
        fcAcc.setPoolSign("0");
        List<FcAccVO> fcAccVOList = fcAccService.getFcAccList(fcAcc, joinSql, params);
        for (FcAccVO accVO : fcAccVOList) {
            FcAccBalanceQueryVO vo = new FcAccBalanceQueryVO();
            BeanUtils.copyNotNullProperties(accVO, vo);
            vo.setAccount(accVO.getBlnctrAcc());
            FcOnlineLedgerVO fcOnlineLedger = fcOnlineLedgerService.getEntityByAccount(vo.getAccount());
            if (null != fcOnlineLedger) {
                BeanUtils.copyNotNullProperties(fcOnlineLedger, vo);
                vo.setAvailableBalance(fcOnlineLedger.getCheckBalance().subtract(fcOnlineLedger.getFreezeMoney()));
            }
            voList.add(vo);
        }
        return voList;
    }
    /**
     * 获取单个账号的余额信息
     * @param account
     * @return
     * 没有被调用，注释
     */
    public FcAccBalanceQueryVO getFcAccBalByAccount(String account) {
        FcAccBalanceQueryVO vo=new FcAccBalanceQueryVO();
        FcOnlineLedgerVO fcOnlineLedger = fcOnlineLedgerService.getEntityByAccount(account);
        if (null != fcOnlineLedger) {
            BeanUtils.copyNotNullProperties(fcOnlineLedger, vo);
            vo.setAvailableBalance(fcOnlineLedger.getCheckBalance().subtract(fcOnlineLedger.getFreezeMoney()) );
            //账号，科目
            Map<String, Object> param = new HashMap();
            FcAccVO fcAcc = new FcAccVO();
            fcAcc.setBlnctrAcc(vo.getAccount());
            List<FcAccVO> fcAccVOList = fcAccService.getFcAccList(fcAcc, null, param);
            for (FcAccVO accVO : fcAccVOList) {
                BeanUtils.copyNotNullProperties(accVO, vo);
            }
        }
        return vo;
    }

    private void sortListByFieldName(List<FcAccBalanceQueryVO> voList, String fieldName, String direction) {
        if (voList == null || voList.isEmpty() || fieldName == null || fieldName.isEmpty()) {
            return;
        }

        try {
            String getterName = "get" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
            Method getterMethod = FcAccBalanceQueryVO.class.getMethod(getterName);

            boolean isAsc = "asc".equalsIgnoreCase(direction);

            Comparator<FcAccBalanceQueryVO> comparator = (o1, o2) -> {
                try {
                    Comparable value1 = (Comparable) getterMethod.invoke(o1);
                    Comparable value2 = (Comparable) getterMethod.invoke(o2);

                    if (value1 == null && value2 == null) {
                        return 0;
                    }
                    if (value1 == null) {
                        return 1;
                    }
                    if (value2 == null) {
                        return -1;
                    }

                    return value1.compareTo(value2);
                } catch (Exception e) {
                    throw new RuntimeException("排序失败,字段: " + fieldName, e);
                }
            };

            if (isAsc) {
                Collections.sort(voList, comparator);
            } else {
                Collections.sort(voList, comparator.reversed());
            }

        } catch (NoSuchMethodException e) {
            throw new RuntimeException("找不到字段对应的getter方法: " + fieldName, e);
        } catch (Exception e) {
            throw new RuntimeException("排序执行失败", e);
        }
    }
}
