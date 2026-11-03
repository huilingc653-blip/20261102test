package com.bytter.biz.bdlink.service;

import cn.hutool.core.date.StopWatch;
import cn.hutool.core.util.ObjectUtil;
import com.bytter.biz.bdlink.api.dto.BisAccDtlDTO;
import com.bytter.biz.bdlink.dao.BisAccDtlDao;
import com.bytter.biz.bdlink.entity.BisAccDtl;
import com.bytter.biz.bdlink.entity.BtBankAccDtl;
import com.bytter.biz.bdlink.vo.BisAccDetailVO;
import com.bytter.biz.bdlink.vo.BisAccDtlExportVO;
import com.bytter.biz.bdlink.vo.BisAccDtlRulesVO;
import com.bytter.biz.bdlink.vo.BisAccDtlVO;
import com.bytter.biz.payment.api.dto.FcBankAccGroupDTO;
import com.bytter.biz.payment.api.service.IFcBankAccGroupService;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.page.IPageResult;
import com.bytter.core.common.page.IParameterInput;
import com.bytter.core.common.rest.RestResponse;
import com.bytter.core.constant.BisAccBalConstant;
import com.bytter.core.constant.CommonConstant;
import com.bytter.core.constant.ResourceConstant;
import com.bytter.core.utils.DateUtils;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.core.utils.SqlUtil;
import com.bytter.core.utils.StringUtils;
import com.bytter.system.api.*;
import com.bytter.system.api.constant.AccountGrantConstant;
import com.bytter.system.api.constant.SysConfigConstant;
import com.bytter.system.api.dto.BtBankAccDTO;
import com.bytter.system.api.dto.DictDTO;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * @author onlineGenerator
 * @version 1.0
 * @Title: Service
 * @Description: 银行账号明细表
 * @date 2017-07-05 11:20:29
 */
@Service("bisAccDtlService")
@Transactional
public class BisAccDtlService extends BaseService<BisAccDtl, String> {
    @Autowired
    private BisAccDtlDao bisAccDtlDao;
    @Autowired(required = false)
    private IAccountGrantService accountGrantService;
    @Autowired(required = false)
    private IConfigService configService;
    @Autowired(required = false)
    private ICorpService corpService;
    @Autowired(required=false)
    private IBtBankAccService btBankAccService;
    @Autowired(required=false)
    private IDictService dictService;
    @DubboReference(check = false)
    private IBtCurrencyService currencyService;
    @DubboReference(check = false)
    private IFcBankAccGroupService fcBankAccGroupService;

    public List<BisAccDtlVO> getByTransTime(String gedTransTime,String ledTransTime) {
        return this.bisAccDtlDao.getByTransTime(gedTransTime,ledTransTime);
    }
    /**
     * 删除对象
     *
     * @param entity
     */
    @Override
    public void deleteEntity(BisAccDtl entity) {
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
    public void save(BisAccDtl entity) {
        super.saveEntity(entity);
    }
    /**
     * 更新
     *
     * @param entity
     */
    public void update(BisAccDtl entity) {
        super.updateEntity(entity);
    }
    /**
     * 保存or更新
     *
     * @param entity
     */
    @Override
    public void saveOrUpdate(BisAccDtl entity) {
        super.saveOrUpdate(entity);
    }
    @Override
    public BaseDao<BisAccDtl, String> getEntityDao() {
        return bisAccDtlDao;
    }
    @SuppressWarnings("rawtypes")
    public DataTablesOutput getDataTablesUnion(DataTablesInput dataTablesInput, ICurrentUser user) {
        StopWatch stopWatch = new StopWatch("银行流水查询效率");
        stopWatch.start("getCorpIdsByUserResource");
        StringBuilder sql = new StringBuilder();

        Map<String, Object> formdata = dataTablesInput.getFormdata();
        if (ObjectUtil.isNotEmpty(formdata.get("ins_corpId"))) {
            List<String> corpIdList = Arrays.stream(formdata.get("ins_corpId").toString().split(",")).collect(Collectors.toList());
            sql.append(" and ").append(SqlUtil.getInSql(corpIdList, "acc.corp_id", true));
        } else {
            List<String> corpList = corpService.getCorpIdsByUserResource(ResourceConstant.NETBANK_BANKACC_BTBANKACCDTL_QUERY, user.getId());
            String accAuthSql = SqlUtil.getInSql(corpList, "acc.corp_id", true);
            sql.append(" and ").append(accAuthSql);
        }
        stopWatch.stop();
        /*stopWatch.start("listByMultiValAccs");
        // 权限控制类型  1-单位级 2-账号级
        List<String> accList = new ArrayList<String>();
        String controlType = configService.getConfig(SysConfigConstant.Module.SYS, SysConfigConstant.CONTROLTYPE).getValue();
        if (!CommonConstant.YES.equals(controlType)) {
            accList = accountGrantService.listByMultiValAccs(user.getId(), ResourceConstant.NETBANK_BANKACC_BTBANKACCDTL_QUERY, null, AccountGrantConstant.ONLINE_BANK_TYPE + CommonConstant.COMMA_SPLIT + AccountGrantConstant.OFFLINE_BANK_TYPE, AccountGrantConstant.QUERY_DETAIL);
        }
        stopWatch.stop();
        //根据查询条件及账户数据进行一次过滤
        stopWatch.start("getAcclistByCondition");
        List<String> accList2 = bisAccDtlDao.getAcclistByCondition(dataTablesInput, corpList, accList);
        stopWatch.stop();
        String accAuthSql = SqlUtil.getInSql(accList2, "t.bank_acc", true);
        sql.append(" and ").append(accAuthSql);*/
        stopWatch.start("getDataTablesUnionV2");
        //账户属性 账户性质 账户类别 过滤
        if(ObjectUtil.isNotEmpty(formdata.get("ins_attributeId"))){
            sql.append(" and ").append(SqlUtil.getInSql(Arrays.asList(formdata.get("ins_attributeId").toString().split(",")), "acc.attribute_id", true));
        }
        if(ObjectUtil.isNotEmpty(formdata.get("ins_natureId"))){
            sql.append(" and ").append(SqlUtil.getInSql(Arrays.asList(formdata.get("ins_natureId").toString().split(",")), "acc.nature_id", true));
        }
        if(ObjectUtil.isNotEmpty(formdata.get("ins_accType"))){
            sql.append(" and ").append(SqlUtil.getInSql(Arrays.asList(formdata.get("ins_accType").toString().split(",")), "acc.acc_type", true));
        }
        DataTablesOutput<BisAccDetailVO> result = this.bisAccDtlDao.getDataTablesUnionV2(dataTablesInput, sql.toString());
        stopWatch.stop();
        List<BisAccDetailVO> data = result.getData();
        stopWatch.start("getNameByIds");
        Map<String, String> corpNameMap = corpService.getNameByIds(data.stream().map(BisAccDetailVO::getCorpId).collect(Collectors.toList()));
        stopWatch.stop();
        stopWatch.start("getCurrencyNameByIds");
        Map<String, String> currencyNameMap = currencyService.getCurrencyNameByIds(data.stream().map(BisAccDetailVO::getCurId).collect(Collectors.toList()));
        stopWatch.stop();
        stopWatch.start("listDictMap");
        Map<String, Map<String, DictDTO>> dictMap = dictService.listDictMap("btBankAccDtl.cdSign", "btBankAcc.accType", "sf_yn");
        stopWatch.stop();
        stopWatch.start("组装数据");
        //查询所有账户组
        List<FcBankAccGroupDTO> validPayType = fcBankAccGroupService.getValidPayType();
        HashMap<String, String> map = new HashMap<>();
        for (FcBankAccGroupDTO fcBankAccGroup : validPayType) {
            map.put(fcBankAccGroup.getAccGroupCode(),fcBankAccGroup.getAccGroupName());
        }
        for (BisAccDetailVO vo : data) {
            vo.setCorpName(corpNameMap.get(vo.getCorpId()));
            vo.setCurName(currencyNameMap.get(vo.getCurId()));
            if(StringUtils.isNotEmpty(vo.getCdSign())){
                vo.setCdSignName(dictMap.get("btBankAccDtl.cdSign").getOrDefault(vo.getCdSign(),DictDTO.EMPTY).getName());
            }
            if(StringUtils.isNotEmpty(vo.getAccType())){
                vo.setAccTypeName(dictMap.get("btBankAcc.accType").getOrDefault(vo.getAccType(),DictDTO.EMPTY).getName());
            }
            if(StringUtils.isNotEmpty(vo.getIsOnline())){
                vo.setIsOnlineStr(dictMap.get("sf_yn").getOrDefault(vo.getIsOnline(),DictDTO.EMPTY).getName());
            }
            //查询账户组名称
            String fcAccGroupCode = vo.getFcAccGroupCode();
            if (EmptyUtils.isNotEmpty(fcAccGroupCode)){
                List<String> codes = Arrays.asList(fcAccGroupCode.split(","));
                StringBuilder accGroupName = new StringBuilder();
                for(int i = 0;i<=codes.size()-1;i++){
                    if (i==codes.size()-1){
                        if (map.containsKey(codes.get(i))){
                            accGroupName.append(map.get(codes.get(i)));
                        }
                    }else{
                        if (map.containsKey(codes.get(i))){
                            accGroupName.append(map.get(codes.get(i))+",");
                        }
                    }
                }
                vo.setFcAccGroupName(accGroupName.toString());
            }

            // 推送状态
            String tempStatus = "否";
            if (!EmptyUtils.isEmpty(vo.getSyncStatus()) && "S".equals(vo.getSyncStatus())){
                tempStatus = "是";
            }
            vo.setSyncStatus(tempStatus);
        }
        stopWatch.stop();
        logger.error(stopWatch.prettyPrint(TimeUnit.MILLISECONDS));
        return result;
    }
    /**
     * 根据时间范围查询流水记录总金额
     *
     * @param bankAcc        账户
     * @param curId          币别ID
     * @param beginTransTime 开始时间
     * @param endTransTime   结束时间
     * @param cdSign         借贷标志 0-收入 1-支出
     * @param rbSign         红蓝标志 1-红 2-蓝
     * @return
     */
    public BigDecimal getSumAmtBy(String bankAcc, String curId, String beginTransTime, String endTransTime, String cdSign, String rbSign) {
        return this.bisAccDtlDao.getSumAmtBy(bankAcc, curId, beginTransTime, endTransTime, cdSign, rbSign);
    }
    /**
     * 计算当天的明细金额
     *
     * @param bankAcc   账户
     * @param curId     币别ID
     * @param transTime 交易日期
     * @return
     */
    public BigDecimal getBalFromDetail(String bankAcc, String curId, Date transTime) {
        if (EmptyUtils.isEmpty(transTime)) {
            return BigDecimal.ZERO;
        }
        String beginTransTimeStr = DateUtils.date_sdf().format(transTime);
        String endTransTimeStr = DateUtils.date_sdf().format(DateUtils.addDay(transTime, 1));
        //当日收入明细红之和
        BigDecimal todaySumROfIncome = this.getSumAmtBy(bankAcc, curId, beginTransTimeStr, endTransTimeStr, BisAccBalConstant.CDSIGN_IN, BisAccBalConstant.RBSIGN_RED);
        //当日收入明细蓝之和
        BigDecimal todaySumBOfIncome = this.getSumAmtBy(bankAcc, curId, beginTransTimeStr, endTransTimeStr, BisAccBalConstant.CDSIGN_IN, BisAccBalConstant.RBSIGN_BLUE);
        //当日支出明细红之和
        BigDecimal todaySumROfOutcome = this.getSumAmtBy(bankAcc, curId, beginTransTimeStr, endTransTimeStr, BisAccBalConstant.CDSIGN_OUT, BisAccBalConstant.RBSIGN_RED);
        //当日支出明细蓝之和
        BigDecimal todaySumBOfOutcome = this.getSumAmtBy(bankAcc, curId, beginTransTimeStr, endTransTimeStr, BisAccBalConstant.CDSIGN_OUT, BisAccBalConstant.RBSIGN_BLUE);
        return todaySumBOfIncome.add(todaySumROfOutcome).subtract(todaySumROfIncome).subtract(todaySumBOfOutcome);
    }
    /**
     * 计算指定时间范围的明细金额
     *
     * @param bankAcc        账户
     * @param curId          币别ID
     * @param beginTransTime 开始时间
     * @param endTransTime   结束时间
     * @return
     */
    public BigDecimal getBalFromDetail(String bankAcc, String curId, Date beginTransTime, Date endTransTime) {
        if (EmptyUtils.isEmpty(beginTransTime) || EmptyUtils.isEmpty(endTransTime)) {
            return BigDecimal.ZERO;
        }
        String beginTransTimeStr = DateUtils.date_sdf().format(beginTransTime);
        String endTransTimeStr = DateUtils.date_sdf().format(DateUtils.addDay(endTransTime, 1));
        //当日收入明细红之和
        BigDecimal todaySumROfIncome = this.getSumAmtBy(bankAcc, curId, beginTransTimeStr, endTransTimeStr, BisAccBalConstant.CDSIGN_IN, BisAccBalConstant.RBSIGN_RED);
        //当日收入明细蓝之和
        BigDecimal todaySumBOfIncome = this.getSumAmtBy(bankAcc, curId, beginTransTimeStr, endTransTimeStr, BisAccBalConstant.CDSIGN_IN, BisAccBalConstant.RBSIGN_BLUE);
        //当日支出明细红之和
        BigDecimal todaySumROfOutcome = this.getSumAmtBy(bankAcc, curId, beginTransTimeStr, endTransTimeStr, BisAccBalConstant.CDSIGN_OUT, BisAccBalConstant.RBSIGN_RED);
        //当日支出明细蓝之和
        BigDecimal todaySumBOfOutcome = this.getSumAmtBy(bankAcc, curId, beginTransTimeStr, endTransTimeStr, BisAccBalConstant.CDSIGN_OUT, BisAccBalConstant.RBSIGN_BLUE);
        return todaySumBOfIncome.add(todaySumROfOutcome).subtract(todaySumROfIncome).subtract(todaySumBOfOutcome);
    }
    /**
     * 根据id查找银行账户流水信息
     * zuoy 2018-03-12
     *
     * @param id
     * @return BtBankAccDtlVO
     */
    public BisAccDtlVO getByUnion(String id) {
        return this.bisAccDtlDao.getByUnion(id);
    }
    /**
     * 查询银行流水可补录数据
     *
     * @param dataTablesInput
     * @param user
     * @return
     */
    public DataTablesOutput getDataTablesMakeup(DataTablesInput dataTablesInput, ICurrentUser user) {
        StringBuilder sql = new StringBuilder();
        List<String> accList = accountGrantService.listByMultiValAccs(user.getId(), ResourceConstant.NETBANK_BANKACC_BISACCDTL_MAKEUPQUERY, null, AccountGrantConstant.ONLINE_BANK_TYPE + CommonConstant.COMMA_SPLIT + AccountGrantConstant.OFFLINE_BANK_TYPE, AccountGrantConstant.QUERY_DETAIL);
        String accAuthSql = SqlUtil.getInSql(accList, "t.bank_acc", true);
        sql.append(" and ").append(accAuthSql);
        return this.bisAccDtlDao.getDataTablesMakeup(dataTablesInput, sql.toString());
    }
    /**
     * 更新当前流水交易时间之后的流水记录
     *
     * @param entity
     */
    public void updateAfterBtBankAccDtl(BisAccDtl entity, Integer orderNo) {
        List<BisAccDtl> afterList = this.bisAccDtlDao.listAfterBtBankAccDtl(entity.getBankAcc(), entity.getCurId(), entity.getTransTime(), entity.getId(), orderNo);
        // 更新余额
        if (!EmptyUtils.isEmptyList(afterList)) {
            BigDecimal lastBal = entity.getBal();
            List<BisAccDtl> newList = new ArrayList<>();
            for (BisAccDtl dtl : afterList) {
                boolean outBlue = BisAccBalConstant.CDSIGN_OUT.equalsIgnoreCase(dtl.getCdSign()) && BisAccBalConstant.RBSIGN_BLUE.equalsIgnoreCase(dtl.getRbSign());
                boolean inRed = BisAccBalConstant.CDSIGN_IN.equalsIgnoreCase(dtl.getCdSign()) && BisAccBalConstant.RBSIGN_RED.equalsIgnoreCase(dtl.getRbSign());
                if (outBlue || inRed) {
                    //支出(蓝) 收入(红)
                    dtl.setBal(lastBal.subtract(dtl.getAmt()));
                } else {
                    //收入(蓝) 支出(红)
                    dtl.setBal(lastBal.add(dtl.getAmt()));
                }
                lastBal = dtl.getBal();
                newList.add(dtl);
            }
//			this.batchSaveOrUpdate(newList);
            //只更新余额
            for (BisAccDtl dtl : newList) {
                bisAccDtlDao.updateBalById(dtl.getId(), dtl.getBal());
            }
        }
    }
    /**
     * 余额刷新
     */
    public void flushBalance() {
        List<BisAccDtl> list = this.getListForFlush();
        for (BisAccDtl data : list) {
            this.updateAfterBtBankAccDtl(data);
        }
    }
    public void flushBalance(String bankAcc) {
        List<BisAccDtl> list = this.getListForFlush();
        for (BisAccDtl data : list) {
            if (bankAcc.equals(data.getBankAcc())) {
                this.updateAfterBtBankAccDtl(data);
            }
        }
    }
    /**
     * 重新计算余额
     *
     * @param entity
     */
    private void updateAfterBtBankAccDtl(BisAccDtl entity) {
        getSession().flush();
        getSession().clear();
        List<BisAccDtl> afterList = this.bisAccDtlDao.listAfterBtBankAccDtl(entity.getBankAcc(), entity.getCurId());
        // 更新余额
        if (!EmptyUtils.isEmptyList(afterList)) {
            BtBankAccDTO btBankAccDTO = btBankAccService.getBankAccByBankAccAndCurId(entity.getBankAcc(),entity.getCurId());
            BisAccDtl bisAccDtl = bisAccDtlDao.get(afterList.get(0).getId());
            BigDecimal lastBal = bisAccDtl.getBal();
            //第一次取初始化余额
            if(btBankAccDTO !=null && btBankAccDTO.getInitBalance()!=null && DateUtils.dayDiffs(bisAccDtl.getTransTime(),btBankAccDTO.getInitBalanceDate())>0){
                lastBal = btBankAccDTO.getInitBalance();
            }else{
                lastBal = BigDecimal.ZERO;
            }
            List<BisAccDtl> newList = new ArrayList<>();
            for (BisAccDtl dtl : afterList) {
                boolean outBlue = BisAccBalConstant.CDSIGN_OUT.equalsIgnoreCase(dtl.getCdSign()) && BisAccBalConstant.RBSIGN_BLUE.equalsIgnoreCase(dtl.getRbSign());
                boolean inRed = BisAccBalConstant.CDSIGN_IN.equalsIgnoreCase(dtl.getCdSign()) && BisAccBalConstant.RBSIGN_RED.equalsIgnoreCase(dtl.getRbSign());
                if (outBlue || inRed) {
                    //支出(蓝) 收入(红)
                    dtl.setBal(lastBal.subtract(dtl.getAmt()));
                } else {
                    //收入(蓝) 支出(红)
                    dtl.setBal(lastBal.add(dtl.getAmt()));
                }
                lastBal = dtl.getBal();
                newList.add(dtl);
            }
            getSession().clear();
//			this.batchSaveOrUpdate(newList);
            //只更新余额
            for (BisAccDtl dtl : newList) {
                bisAccDtlDao.updateBalById(dtl.getId(), dtl.getBal());
            }
        }
    }
    public List<BisAccDtl> getListForFlush() {
        return this.bisAccDtlDao.getListForFlush();
    }
    /**
     * 根据账号币别获取流水
     *
     * @param bankAcc
     * @param curId
     * @return
     */
    public List<BisAccDtlVO> getListByBankAcc(String bankAcc, String curId, String remark) {
        return this.bisAccDtlDao.getListByBankAcc(bankAcc, curId, remark);
    }
    /**
     * 根据账号币别获取流水 - 分页
     *
     * @param parameterInput
     * @param bankAcc
     * @param curId
     * @return
     */
    public RestResponse<BisAccDtlVO> getPageListByBankAcc(IParameterInput parameterInput, String bankAcc, String curId, String remark) {
        IPageResult<BisAccDtlVO> pageResult = this.bisAccDtlDao.getPageListByBankAcc(parameterInput, bankAcc, curId, remark);
        return pageResult.convert(RestResponse.class);
    }
    /**
     * 根据账号id和币别id查找账号明细最小时间
     *
     * @param bankAcc
     * @param curId
     * @return
     */
    public Date getMinTransTime(String bankAcc, String curId) {
        return this.bisAccDtlDao.getMinTransTime(bankAcc, curId);
    }
    /**
     * 根据账号id币别交易日期查找明细数据
     *
     * @param bankAcc
     * @param curId
     * @param transTime
     * @return
     */
    public BisAccDtlVO getBisAccDtlVOByAccCurTime(String bankAcc, String curId, Date transTime) {
        return bisAccDtlDao.getBisAccDtlVOByAccCurTime(bankAcc, curId, transTime);
    }
    /**
     * 获取指定账号某天的流水集合
     * @param bankAcc
     * @param curId
     * @param transTime
     * @return
     */
    public List<BisAccDtlVO> listBisAccDtlVOByAccCurTime(String bankAcc, String curId, Date transTime){
        return bisAccDtlDao.listBisAccDtlVOByAccCurTime(bankAcc, curId, transTime);
    }
    /**
     * 获取当天流水的结余
     * @param bankAcc
     * @param curId
     * @param transTime
     * @return
     */
    public BigDecimal getLastBal(String bankAcc, String curId, Date transTime){
        BigDecimal lastBal = BigDecimal.ZERO;
        List<BisAccDtlVO> list = bisAccDtlDao.listBisAccDtlVOByAccCurTime(bankAcc, curId, transTime);
        for (BisAccDtlVO dtl : list) {
            boolean outBlue = BisAccBalConstant.CDSIGN_OUT.equalsIgnoreCase(dtl.getCdSign()) && BisAccBalConstant.RBSIGN_BLUE.equalsIgnoreCase(dtl.getRbSign());
            boolean inRed = BisAccBalConstant.CDSIGN_IN.equalsIgnoreCase(dtl.getCdSign()) && BisAccBalConstant.RBSIGN_RED.equalsIgnoreCase(dtl.getRbSign());
            if (outBlue || inRed) {
                //支出(蓝) 收入(红)
                lastBal = lastBal.subtract(dtl.getAmt());
            } else {
                //收入(蓝) 支出(红)
                lastBal = lastBal.add(dtl.getAmt());
            }
        }
        return lastBal;
    }
    public List listExport(DataTablesInput dataTablesInput, ICurrentUser user) {
        StringBuilder sql = new StringBuilder();
        List<String> corpList = corpService.getCorpIdsByUserResource(ResourceConstant.NETBANK_BANKACC_BTBANKACCDTL_QUERY, user.getId());
        List<String> accList = accountGrantService.listByMultiValAccs(user.getId(), ResourceConstant.NETBANK_BANKACC_BTBANKACCDTL_QUERY, null, AccountGrantConstant.ONLINE_BANK_TYPE + CommonConstant.COMMA_SPLIT + AccountGrantConstant.OFFLINE_BANK_TYPE, AccountGrantConstant.QUERY_DETAIL);
        List<String> accList2 = bisAccDtlDao.getAcclistByCondition(dataTablesInput, corpList, accList);
        String accAuthSql = SqlUtil.getInSql(accList2, "t.bank_acc", true);
        sql.append(" and ").append(accAuthSql);
        List<BisAccDtlExportVO> dtlList = this.bisAccDtlDao.listExport(dataTablesInput, sql.toString());
        Map<String, DictDTO> accTypeMap = dictService.listMap("btBankAcc.accType");
        Map<String, DictDTO> cdMap = dictService.listMap("btBankAccDtl.cdSign");
        for(BisAccDtlExportVO dtlVo : dtlList) {
            String cdSign = dtlVo.getCdSign();
            String accType = dtlVo.getAccType();
            DictDTO accTypeDto = accTypeMap.get(accType);
            DictDTO cdSignDto = cdMap.get(cdSign);
            if (!EmptyUtils.isEmpty(accTypeDto)) {
                dtlVo.setAccTypeName(accTypeDto.getName());
            }
            if (!EmptyUtils.isEmpty(cdSignDto)) {
                dtlVo.setCdSignName(cdSignDto.getName());
            }
        }
        return dtlList;
    }
    /**
     * 判断是否是合并单据
     *
     * @param id
     * @return
     */
    public Boolean isMergeBill(String id) {
        List<BisAccDtl> list = this.bisAccDtlDao.listBy("mergeId", id);
        return !EmptyUtils.isEmptyList(list);
    }
    /**
     * 获取一个账号期间发生额
     *
     * @param curId     币种
     * @param bankAcc   账号
     * @param beginDate 开始日期
     * @param endDate   结束日期
     * @return 期间发生额
     */
    public BigDecimal getDuringMoney(String curId, String bankAcc, Date beginDate, Date endDate) {
        return bisAccDtlDao.getDuringMoney(curId, bankAcc, beginDate, endDate);
    }
    /**
     * 电子回单勾兑查询银行流水
     *
     * @param dataTablesInput
     * @return
     */
    public DataTablesOutput getDataTablesUnionForReceiptBlending(DataTablesInput dataTablesInput) {
        return this.bisAccDtlDao.getDataTablesUnionForReceiptBlending(dataTablesInput);
    }
    /*
    被关联的流水的勾兑规则
     */
    public BisAccDtlRulesVO getRules(String id) {
        List<BisAccDtlRulesVO> list = this.bisAccDtlDao.getRules(id);
        if (list.size() == 1) {
            return list.get(0);
        } else {
            return null;
        }
    }
    /**
     * 根据票据认领中间表id数组修改
     *
     * @param claimIdArr 票据认领id数组
     */
    public void updateByClaimArr(String[] claimIdArr) {
        this.bisAccDtlDao.updateByClaimArr(claimIdArr);
    }
    /**
     * 根据id数组修改票据认领中间表id
     *
     * @param ids     流水明细id数组
     * @param claimId 票据认领中间表id
     */
    public void updateClaimIdByIds(String[] ids, String claimId) {
        bisAccDtlDao.updateClaimIdByIds(ids ,claimId);
    }
    /**
     * 根据流水余额
     *
     * @param dtl     流水
     */
    public void updateBal(BtBankAccDtl dtl) {
        bisAccDtlDao.updateBal(dtl);
    }
    /**
     * 根据流水号获取流水信息
     * @param accNo
     * @return
     */
    public BisAccDtl getBisAccDtlByAccNo(String accNo){
        List<BisAccDtl> list = this.bisAccDtlDao.listBy("accNo", accNo);
        if(!EmptyUtils.isEmptyList(list)){
            return list.get(0);
        }
        return null;
    }
    /**
     * 根据ids查询流水
     * @param ids
     * @return BtBankAccDtlVO
     */
    public List<BisAccDtlVO> getByUnionIds(List<String> ids) {
        return bisAccDtlDao.getByUnionIds(ids);
    }
    public List<BisAccDtl> listBy(String accNo){
    	return this.bisAccDtlDao.listBy("accNo", accNo);
    }
    public List<BisAccDtl> listAfterBtBankAccDtl(String bankAcc, String curId, Date transTime, String id, Integer orderNo) {
   	 	return bisAccDtlDao.listAfterBtBankAccDtl(bankAcc, curId, transTime, id, orderNo);
    }
    public int updateBalById(String id,BigDecimal bal){
    	return bisAccDtlDao.updateBalById(id, bal);
    }

    public List<BisAccDtl> getByIds(List<String> ids) {
    	return bisAccDtlDao.getByIds(ids);
    }

    public BisAccDtlDTO getLastDtlBal(String bankAcc, String curId, Date transTime) {
        return bisAccDtlDao.getLastDtlBal(bankAcc,curId,transTime);
    }
}
