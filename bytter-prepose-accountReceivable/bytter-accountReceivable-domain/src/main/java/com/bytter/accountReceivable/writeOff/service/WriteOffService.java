package com.bytter.accountReceivable.writeOff.service;

import com.bytter.accountReceivable.constant.ResourceConstant;
import com.bytter.accountReceivable.constant.AccountReceivableConstant;
import com.bytter.accountReceivable.docManage.entity.DocManage;
import com.bytter.accountReceivable.docManage.service.DocManageService;
import com.bytter.accountReceivable.docManage.vo.DocManageVo;
import com.bytter.accountReceivable.docManage.vo.DocManageWriteOffVo;
import com.bytter.accountReceivable.query.vo.ForceWriteOffVo;
import com.bytter.accountReceivable.writeOff.constant.WriteoffConstant;
import com.bytter.accountReceivable.writeOff.dao.DocWriteOffRelationDao;
import com.bytter.accountReceivable.writeOff.dao.WriteOffDao;
import com.bytter.accountReceivable.writeOff.entity.DocWriteOffRelation;
import com.bytter.accountReceivable.writeOff.enums.WriteOffEnums;
import com.bytter.accountReceivable.writeOff.vo.*;
import com.bytter.biz.bdlink.api.service.IBisAccDtlService;
import com.bytter.biz.payment.api.constant.NisMessageConstant;
import com.bytter.biz.payment.api.dto.NisReceivableDTO;
import com.bytter.biz.payment.api.service.INisMessageService;
import com.bytter.biz.payment.api.service.INisReceivableService;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.workflow.bpm.IWorkFlowListener;
import com.bytter.workflow.bpm.annotation.WorkFlow;
import com.bytter.workflow.dto.IWorkFlowResult;
import com.bytter.workflow.dto.WorkFlowBusiness;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.exception.BusinessException;
import com.bytter.core.constant.AccebillConstant;
import com.bytter.core.constant.CommonConstant;
import com.bytter.core.constant.NetbankConstant;
import com.bytter.core.dict.DictInDTO;
import com.bytter.core.dict.DictInjectProvider;
import com.bytter.core.sequence.SequenceSingleton;
import com.bytter.core.sequence.enums.SequenceEnum;
import com.bytter.core.utils.*;
import com.bytter.system.api.*;
import com.bytter.system.api.constant.AccountGrantConstant;
import com.bytter.system.api.constant.SysConfigConstant;
import com.bytter.system.api.dto.BtBankAccDTO;
import com.bytter.system.api.dto.CorpDTO;
import com.bytter.workflow.api.IWorkFlowService;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.stream.Collectors;

@Service("writeOffService")
@Transactional
@WorkFlow(AccountReceivableConstant.DOC_WRITE_OFF_APPROVE)
public class WriteOffService extends BaseService<DocManage, String> implements IWorkFlowListener , DictInjectProvider{

    @DubboReference(check = false)
    private ICorpService corpService;
    @DubboReference(check = false)
    private IDictService iDictService;
    @DubboReference(check = false)
    private IBisAccDtlService bisAccDtlService;
    @DubboReference(check = false)
    private INisReceivableService nisReceivableService;
    @Autowired
    private IWorkFlowService workFlowService;
    @DubboReference(check = false)
    private INisMessageService nisMessageService;
    @DubboReference(check = false)
    private IAccountGrantService accountGrantService;
    @DubboReference(check = false)
    private IConfigService configService;
    @DubboReference(check = false)
    private IBtCurrencyService currencyService;
    @DubboReference(check = false)
    private IBtBankAccService bankAccService;

    @Autowired
    private WriteOffDao writeOffDao;
    @Autowired
    private DocManageService docManageService;
    @Autowired
    private DocWriteOffRelationDao docWriteOffRelationDao;
    @DubboReference(check = false)
    private IExternalCorpService externalCorpService;
    @DubboReference(check = false)
    private IDictService dictService;
    @Override
    public BaseDao<DocManage, String> getEntityDao() {
        return null;
    }

    public  String getWriteOffBatchNo(){
         //  String batchNo=SequenceSingleton.getSequence(SequenceEnum.ACCOUNT_RECEIVABLE_WRITE_OFF);
        return SequenceSingleton.getSequenceBySpecifyLockNoCache(SequenceEnum.ACCOUNT_RECEIVABLE_WRITE_OFF, null);
    }
    /*public  String getWriteOffBatchNo() {
        String batchNo = SequenceSingleton.getSequence(SequenceEnum.ACCOUNT_RECEIVABLE_WRITE_OFF);
        synchronized (this) {
            List<DocWriteOffRelation> writeOffListByBatchNo = docWriteOffRelationDao.getWriteOffListByBatchNo1(batchNo);
            // 证明被用过
            if (writeOffListByBatchNo.size() > 0) {
                batchNo = SequenceSingleton.getWriteOffBatchNo(SequenceEnum.ACCOUNT_RECEIVABLE_WRITE_OFF, true);
            }
            return batchNo;
        }
    }*/
    /**
     * 查询核销处理 应收单列表数据
     * @param dataTablesInput
     * @param user
     * @return
     */
    public DataTablesOutput getDocDataTablesOfWriteOff(DataTablesInput dataTablesInput, ICurrentUser user) {
//        Integer start = dataTablesInput.getStart();
//        Integer length = dataTablesInput.getLength();
//        dataTablesInput.setLength(999); //设置需要查询的数量，后面再截取
//        dataTablesInput.setStart(0); //设置需要查询的数量，后面再截取

        List<String> corpIds = corpService.getUserCorpIds(ResourceConstant.ACCOUNT_RECEIVABLE_WRITEOFF_OP, user.getId());
        String corpAuthSql = " and " + SqlUtil.getInSql(corpIds, "dm1.corp_Id", true);
        DataTablesOutput out = writeOffDao.getDocDataTablesOfWriteOff(dataTablesInput, corpAuthSql);

        //为了防止查出多条相同的应收单（因为关联的DOC_WIRTE_OFF_RELATION表的状态有-3的，95又能查回95和-3的）
//        List<DocManageWriteOffVo> dataVoList1 = out.getData();
//        List<DocManageWriteOffVo> dataVoList2 = new ArrayList<>();
//        Set<String> ids = new HashSet<>();
//        if (EmptyUtils.isNotEmpty(dataVoList1)) {
//            //先遍历一次，优先处理status为11的vo
//            for (DocManageWriteOffVo vo : dataVoList1) {
//                if ("11".equals(vo.getStatus())) {
//                    if (ids.add(vo.getId())) { //添加成功，说明没见过
//                        dataVoList2.add(vo);
//                    }
//                }
//            }
//            //再遍历一次，处理其他status不为11的vo
//            for (DocManageWriteOffVo vo : dataVoList1) {
//                if (!"11".equals(vo.getStatus())) {
//                    if (ids.add(vo.getId())) { //添加成功，说明没见过
//                        dataVoList2.add(vo);
//                    }
//                }
//            }
//            List<DocManageWriteOffVo> list = dataVoList2.subList(start, dataVoList2.size()).stream().limit(length).collect(Collectors.toList());
//            inject(list);
//            out.setData(list);
//            out.setRecordsTotal(dataVoList2.size());
//            out.setRecordsFiltered(dataVoList2.size());
//        }

        //为提高查询效率，如果查询的已核销数据才查询关联的id
        String ins_writeOffStatus = (String) dataTablesInput.getFormdata().get("ins_writeOffStatus");
        if(EmptyUtils.isNotEmpty(ins_writeOffStatus) && ins_writeOffStatus.contains(WriteoffConstant.WRITE_OFF_STATUS_2)) {
            List<DocManageWriteOffVo> dataList = out.getData();
            if(EmptyUtils.isNotEmpty(dataList)){
                StringBuffer writeOffIds = null;
                for (DocManageWriteOffVo docManageVo : dataList) {
                    if(WriteoffConstant.WRITE_OFF_STATUS_2.equals(docManageVo.getWriteOffStatus())){
                        List<DocWriteOffRelation> relationList = docWriteOffRelationDao.getDocWriteOffRelation(docManageVo.getId(), "", null);
                        if(EmptyUtils.isNotEmpty(relationList)){
                            writeOffIds = new StringBuffer();
                            for (int i = 0; i < relationList.size(); i++) {
                                if(i==0){
                                    writeOffIds.append(relationList.get(i).getRelationId());
                                }else {
                                    writeOffIds.append(",").append(relationList.get(i).getRelationId());
                                }
                            }
                            docManageVo.setWriteOffId(writeOffIds.toString());
                        }
                    }
                }
            }
        }
        return out;
    }

    /**
     * 查询核销处理 应收单列表数据
     * @return
     */
    public List<DocManageWriteOffVo> getDocsOfWriteOffByIds(String ids) {
        List<DocManageWriteOffVo> result = new ArrayList<>();
        if(EmptyUtils.isNotEmpty(ids)){
            result = writeOffDao.getDocsOfWriteOffByIds(Arrays.asList(ids.split(",")));
        }
        return result;
    }
    //  获取审批详情 应收单详情
    public List<DocManageWriteOffVo> getDocRelation(String ids,String batchNo){
        List<DocManageWriteOffVo> result = new ArrayList<>();
        if(EmptyUtils.isNotEmpty(ids)){
            result = writeOffDao.getDocRelation(Arrays.asList(ids.split(",")),batchNo);
        }
        return result;
    }
    //合成详情
    public List<DocManageWriteOffVo> getDocsOfWriteOffDocDetail(DocWriteOffRelationVo relationVo){
        List<DocManageWriteOffVo> docList = getDocsOfWriteOffByIds(relationVo.getDocId());
        if(docList.size() == 1){
            DocManageWriteOffVo vo = docList.get(0);
            vo.setExecuteMoney(relationVo.getMoney0().setScale(2, RoundingMode.HALF_UP));
            vo.setNewWriteOffBal(vo.getWriteOffBal().subtract(relationVo.getMoney0()));
            vo.setNewWriteOffStatus(vo.getNewWriteOffBal().compareTo(BigDecimal.ZERO) <= 0? "2":"1");
        }else {
            for (DocManageWriteOffVo vo : docList) {
                vo.setExecuteMoney(relationVo.getMoney0().compareTo(vo.getWriteOffBal()) > 0 ? vo.getWriteOffBal().setScale(2, RoundingMode.HALF_UP) : relationVo.getMoney0().setScale(2, RoundingMode.HALF_UP));
                BigDecimal temp = vo.getWriteOffBal().subtract(vo.getExecuteMoney());
                temp = temp.compareTo(BigDecimal.ZERO) < 1 ? BigDecimal.ZERO : temp;
                vo.setNewWriteOffBal(temp);
                vo.setNewWriteOffStatus(temp.compareTo(BigDecimal.ZERO) == 0 ? "2" : "1");
            }
        }
        return docList;
    }
    public List<BisAccDtlVO> getDocsOfWriteOffDtlDetail(DocWriteOffRelationVo relationVo){
        List<BisAccDtlVO> dtlList = getDtlsOfWriteOffByList(relationVo.getBusinessId(),relationVo.getBatchNo());
        for (BisAccDtlVO vo : dtlList) {
            vo.setReturnTime(vo.getTransTime());
        }
        if(dtlList.size() == 1){
            BisAccDtlVO vo = dtlList.get(0);
            vo.setExecuteMoney(relationVo.getMoney0().setScale(2, RoundingMode.HALF_UP));
            vo.setNewWriteOffBal(vo.getWriteOffBal().multiply(relationVo.getRate()).subtract(relationVo.getMoney0()));
            vo.setNewWriteOffStatusName(vo.getNewWriteOffBal().compareTo(BigDecimal.ZERO) <= 0? "已核销":"部分核销");
        }else{
            for (BisAccDtlVO vo : dtlList) {
                vo.setExecuteMoney(vo.getWriteOffBal().multiply(relationVo.getRate()).setScale(2, RoundingMode.HALF_UP));
                vo.setNewWriteOffStatusName(relationVo.getMoney0().compareTo(vo.getExecuteMoney()) >= 0? "已核销":"部分核销");
                vo.setExecuteMoney(relationVo.getMoney0().compareTo(vo.getExecuteMoney())>0?vo.getExecuteMoney():relationVo.getMoney0());
                BigDecimal temp = vo.getWriteOffBal().subtract(vo.getExecuteMoney());
                temp = temp.compareTo(BigDecimal.ZERO) < 1?BigDecimal.ZERO:temp;
                vo.setNewWriteOffBal(temp);
//                        vo.setNewWriteOffStatusName(temp.compareTo(BigDecimal.ZERO) == 0? "已核销":"部分核销");
            }
        }
        return dtlList;
    }

    public List<AbBillPayAndReceVO> getDocsOfWriteOffBillDetail(DocWriteOffRelationVo relationVo){
        List<AbBillPayAndReceVO> billList = billRecesByIds(relationVo.getBusinessId());
        if(billList.size() == 1){
            AbBillPayAndReceVO vo = billList.get(0);
            vo.setExecuteMoney(relationVo.getMoney0().setScale(2, RoundingMode.HALF_UP));
            vo.setNewWriteOffBal(vo.getWriteOffBal().multiply(relationVo.getRate()).subtract(relationVo.getMoney0()));
            vo.setNewWriteOffStatusName(vo.getNewWriteOffBal().compareTo(BigDecimal.ZERO) <= 0? "已核销":"部分核销");
        }else{
            for (AbBillPayAndReceVO vo : billList) {
                vo.setExecuteMoney(vo.getWriteOffBal().multiply(relationVo.getRate()).setScale(2, RoundingMode.HALF_UP));
                vo.setNewWriteOffStatusName(relationVo.getMoney0().compareTo(vo.getExecuteMoney())>=0? "已核销":"部分核销");
                vo.setExecuteMoney(relationVo.getMoney0().compareTo(vo.getExecuteMoney())>0?vo.getExecuteMoney():relationVo.getMoney0());
                BigDecimal temp = vo.getWriteOffBal().subtract(vo.getExecuteMoney());
                temp = temp.compareTo(BigDecimal.ZERO) < 1?BigDecimal.ZERO:temp;
                vo.setNewWriteOffBal(temp);
//                        vo.setNewWriteOffStatusName(temp.compareTo(BigDecimal.ZERO) == 0? "已核销":"部分核销");
            }
        }
        return billList;
    }

    public List<LcBillArrivalVO> getDocsOfWriteOffLcBillDetail(DocWriteOffRelationVo relationVo){
        List<LcBillArrivalVO> lcList = getLcsOfWriteOffByIds(relationVo.getBusinessId());
        if(lcList.size() == 1){
            LcBillArrivalVO vo = lcList.get(0);
            vo.setExecuteMoney(relationVo.getMoney0().setScale(2, RoundingMode.HALF_UP));
            vo.setNewWriteOffBal(vo.getWriteOffBal().multiply(relationVo.getRate()).subtract(relationVo.getMoney0()));
            vo.setNewWriteOffStatusName(vo.getNewWriteOffBal().compareTo(BigDecimal.ZERO) <= 0? "已核销":"部分核销");
        }else{
            for (LcBillArrivalVO vo : lcList) {
                vo.setExecuteMoney(vo.getWriteOffBal().multiply(relationVo.getRate()).setScale(2, RoundingMode.HALF_UP));
                vo.setNewWriteOffStatusName(relationVo.getMoney0().compareTo(vo.getExecuteMoney())>=0? "已核销":"部分核销");
                vo.setExecuteMoney(relationVo.getMoney0().compareTo(vo.getExecuteMoney())>0?vo.getExecuteMoney():relationVo.getMoney0());
                BigDecimal temp = vo.getWriteOffBal().subtract(vo.getExecuteMoney());
                temp = temp.compareTo(BigDecimal.ZERO) < 1?BigDecimal.ZERO:temp;
                vo.setNewWriteOffBal(temp);
//                        vo.setNewWriteOffStatusName(temp.compareTo(BigDecimal.ZERO) == 0? "已核销":"部分核销");
            }
        }
        return lcList;
    }
    /**
     * 查询核销处理 流水列表数据
     * @param dataTablesInput
     * @param user
     * @return
     */
    public DataTablesOutput getDtlDataTablesOfWriteOff(DataTablesInput dataTablesInput, ICurrentUser user) {
        //dataTablesInput.getFormdata().put("eqs_tenantId",getCurrentUser().getTenantId());
        List<String> corpIds = corpService.getUserCorpIds(ResourceConstant.ACCOUNT_RECEIVABLE_WRITEOFF_OP, user.getId());
        String corpAuthSql = " and " + SqlUtil.getInSql(corpIds, "ba.corp_Id", true);
        List<String> accList = accountGrantService.listByMultiValAccs(user.getId(), com.bytter.core.constant.ResourceConstant.NETBANK_BANKACC_BTBANKACCDTL_QUERY, null, AccountGrantConstant.ONLINE_BANK_TYPE + CommonConstant.COMMA_SPLIT + AccountGrantConstant.OFFLINE_BANK_TYPE, AccountGrantConstant.QUERY_DETAIL);
        corpAuthSql += " and " + SqlUtil.getInSql(accList, "t.bank_acc", true);
        DataTablesOutput dtlDataOutput = writeOffDao.getDtlDataTablesOfWriteOff(dataTablesInput, corpAuthSql);
        for (BisAccDtlVO dtl : (List<BisAccDtlVO>)dtlDataOutput.getData()) {
            Date transTime = dtl.getTransTime();
            String strTransTime = transTime.toString();
            Date dateYMD = DateUtils.getDateYMD(transTime);
            String dateTime = strTransTime.substring(11,strTransTime.length()-2);
            dtl.setTransTime(dateYMD);
            dtl.setTransDateTime(dateTime);
        }
        return dtlDataOutput;
    }

    /**
     * 查询核销处理 流水列表数据
     * @return
     */
    public List<BisAccDtlVO> getDtlsOfWriteOffByList(String ids,String... batchNo) {
        List<BisAccDtlVO> result = new ArrayList<>();
        if(EmptyUtils.isNotEmpty(ids)){
            result = writeOffDao.getDtlsOfWriteOffByIds(Arrays.asList(ids.split(",")), batchNo);
        }

        return result;
    }

    /**
     * 应收票据台账列表
     * @param dataTablesInput 查询入参
     * @return 返回列表数据信息
     */
    @SuppressWarnings("rawtypes")
    @Transactional(readOnly = true)
    public DataTablesOutput billReceDataTable(DataTablesInput dataTablesInput,ICurrentUser user) {
        List<String> corpAuthList=corpService.getUserCorpIds(ResourceConstant.ACCOUNT_RECEIVABLE_WRITEOFF_OP,dataTablesInput.getCurrentUserId());
        Map<String, Object> formdata = dataTablesInput.getFormdata();
        Object way = formdata.get("ins_transferWay");
        String corpIds = dataTablesInput.getFormdata().get("ins_corpId") == null ? null : String.valueOf(dataTablesInput.getFormdata().get("ins_corpId"));
        //设置是否过滤票据账号
        this.setElectricBillAccountIsDetail(dataTablesInput,ResourceConstant.ACCOUNT_RECEIVABLE_WRITEOFF_OP,corpIds);
        DataTablesOutput dataTablesOutput = writeOffDao.billReceDataTable(dataTablesInput, corpAuthList, user);
        formdata.put("ins_transferWay",way);
        dataTablesInput.setFormdata(formdata);
        dataTablesInput.setMoneySum(true);
        /*DataTablesOutput dataSumTablesOutput = abBillQuery.billReceDataTable(dataTablesInput,corpAuthList);
//		BigDecimal receiveMoneySum = abBillQuery.getBillReceiveMoneySum(dataTablesInput, corpAuthList);
        BigDecimal receiveMoneySum = ((AbBillPayAndReceVO)dataSumTablesOutput.getData().get(0)).getBillMoney();
        if(StringUtils.isNotEmpty(receiveMoneySum)){
            dataTablesOutput.setInsertInformation(receiveMoneySum.toString());
        }*/
        return dataTablesOutput;
    }

    /**
     * 应收票据台账列表
     * @return 返回列表数据信息
     */
    @SuppressWarnings("rawtypes")
    public List<AbBillPayAndReceVO> billRecesByIds(String ids,String...batchNo) {
        List<AbBillPayAndReceVO> result = new ArrayList<>();
        if(EmptyUtils.isNotEmpty(ids)){
            result = writeOffDao.getBillRecesByIds(Arrays.asList(ids.split(",")),batchNo);
        }
        return result;
    }

    /**
     * 权限控制类型为账号级  及 票据参数是否按票据账户控制为是
     * 按当前登录用户账户权限过滤是票据账号，账号权限为查询明细
     * @param input 查询输入
     * @param resource 资源权限
     * @param corpIds 查询单位
     */
    public void setElectricBillAccountIsDetail(DataTablesInput input,String resource,String corpIds) {
        String controlType = configService.getConfig(SysConfigConstant.Module.SYS, SysConfigConstant.CONTROLTYPE).getValue();
        boolean isElectricBill = configService.getConfig(AccebillConstant.AB_CONFIG_TYPE, AccebillConstant.AB_CONFIG_ISELECTRICBILL).toBoolean(true);
        //权限控制类型为账号级  及 票据参数是否按票据账户控制为是
        if (CommonConstant.NO.equals(controlType) && isElectricBill) {
            //设置账号授权控制
            List<String> accountGrantList = this.listByMultiVal(input.getCurrentUserId(), corpIds, resource,
                    AccountGrantConstant.ONLINE_BANK_TYPE + "," + AccountGrantConstant.OFFLINE_BANK_TYPE +"," + AccountGrantConstant.ELECTRIC_BILL_BANK_TYPE,
                    AccountGrantConstant.QUERY_DETAIL);
            //如果有多个资源，判断账号是否有值，有值则追加
            setAccountGrantList(input, accountGrantList);
        }
    }
    /**
     * 获取有权限的账号信息
     * @param userId 用户ID
     * @param corpId 单位ID
     * @param resource 需要过滤的资源单位
     * @param accountType 账号类型
     * @param permission 对应的授权权限
     * @return
     */
    public List<String> listByMultiVal(String userId, String corpId, String resource, String accountType, String permission){
        List<String> bankAccGrantList=null;
        List accountGrantList= accountGrantService.listDTOByMultiVal(userId,corpId,resource,null,accountType,permission, true);
        if(ListUtils.isNotNullOrEmpty(accountGrantList)){
            bankAccGrantList=new ArrayList<>();
            for(int i=0;i<accountGrantList.size();i++){
                BtBankAccDTO accDTO=(BtBankAccDTO)accountGrantList.get(i);
                bankAccGrantList.add(accDTO.getBankAcc());
            }
        }
        return bankAccGrantList;
//		return accountGrantService.listByMultiVal(userId,corpId,resource,null,accountType,permission);
    }

    /**
     * 如果有多个资源，判断账号是否有值，有值则追加 没有账号授权为空
     * @param input
     * @param accountGrantList
     */
    private void setAccountGrantList(DataTablesInput input, List<String> accountGrantList) {
        if(input.getFormdata().get("accountGrantList") == null) {
            input.getFormdata().put("accountGrantList", new ArrayList<String>());
        }
        if(!EmptyUtils.isEmpty(accountGrantList)){
            accountGrantList.addAll((List<String>) input.getFormdata().get("accountGrantList"));
            //去重
            HashSet h = new HashSet(accountGrantList);
            accountGrantList.clear();
            accountGrantList.addAll(h);
            input.getFormdata().put("accountGrantList", accountGrantList);
        }
    }

    /**
     * 查询核销处理 信用证交单列表数据
     * @param dataTablesInput
     * @param user
     * @return
     */
    public DataTablesOutput getLcDataTablesOfWriteOff(DataTablesInput dataTablesInput, ICurrentUser user) {
        List<String> corpIds = corpService.getUserCorpIds(ResourceConstant.ACCOUNT_RECEIVABLE_WRITEOFF_OP, user.getId());
        return writeOffDao.getLcDataTablesOfWriteOff(dataTablesInput, corpIds);
    }

    /**
     * 查询核销处理 信用证交单列表数据
     * @return
     */
    public List<LcBillArrivalVO> getLcsOfWriteOffByIds(String ids,String...batchNO) {
        List<LcBillArrivalVO> result = new ArrayList<>();
        if(EmptyUtils.isNotEmpty(ids)){
            result = writeOffDao.getLcsOfWriteOffByIds(Arrays.asList(ids.split(",")),batchNO);
        }
        return result;
    }

    /**
     * 确认核销
     * @param relationVo
     * @param user
     */
    public void sureWriteOff(DocWriteOffRelationVo relationVo, ICurrentUser user){
        if(EmptyUtils.isEmpty(relationVo.getExchangeRate())){
            relationVo.setExchangeRate(relationVo.getRate());
        }
        DocSureWriteOffVo sureData = this.getSureData(relationVo);
        if(sureData.isCheckFlag()){
            //删除原有的关联数据
//            if(StringUtils.isNotEmpty(relationVo.getDocId())){
//                String[] ids = relationVo.getDocId().split(",");
//                writeOffDao.delDocWriteOffRelationByDocIds(Arrays.asList(ids));
//            }
            List<DocWriteOffRelationVo> relationDataList = sureData.getRelationDataList();

            //自动核销时每一组单独走一个审批
            if (WriteoffConstant.WRITE_OFF_TYPE_1.equals(relationVo.getWriteOffType())) {
                for (DocWriteOffRelationVo relationDataVo : relationDataList) {
                    String batchNo = getWriteOffBatchNo();
                    DocWriteOffRelationVo workFlowRelationVo = null;
                    DocWriteOffRelation docWriteOffRelation = new DocWriteOffRelation();
                    BeanUtils.copyNotNullProperties(relationDataVo, docWriteOffRelation);
                    docWriteOffRelation.setRulesName(this.convertRuleByDict(relationDataVo.getBankAccTypeStr()));
                    docWriteOffRelation.setExchangeRate(relationVo.getExchangeRate());
                    docWriteOffRelation.setWriteOffStatus(relationDataVo.getAfterDocWriteOffStatus());
                    docWriteOffRelation.setCreateBy(user.getId());
                    docWriteOffRelation.setCreateDate(DateUtils.getDate());
                    docWriteOffRelation.setTenantId(user.getTenantId());
                    docWriteOffRelation.setBatchNo(batchNo);
                    docWriteOffRelation.setStatus(NetbankConstant.STATUE_WAIT_CHECK);
                    // 执行金额
                    docWriteOffRelation.setExecuteMoney(relationDataVo.getDocManageVo().getExecuteMoney());
                    if(workFlowRelationVo == null){
                        workFlowRelationVo = new DocWriteOffRelationVo();
                        workFlowRelationVo.setBatchNo(batchNo);
                        //需求未明确应收单多选时单位需要一致
                        workFlowRelationVo.setCorpId(relationDataVo.getCorpId());
                        workFlowRelationVo.setStatus(NetbankConstant.STATUE_WAIT_CHECK);
                    }
                    docWriteOffRelationDao.save(docWriteOffRelation);
                    docWriteOffRelationDao.flush();
                    this.startWorkFlow(workFlowRelationVo, user);
                }
            } else {

                String batchNo = "";
                if (!EmptyUtils.isEmpty(relationVo.getBatchNo())) {
                    batchNo = relationVo.getBatchNo();
                } else {
                    batchNo = getWriteOffBatchNo();
                }
                List<DocWriteOffRelation> writeOffListByBatchNo = docWriteOffRelationDao.getWriteOffListByBatchNo(batchNo);
                // 证明被用过
                if(EmptyUtils.isNotEmpty(writeOffListByBatchNo)){
                    throw new BusinessException(WriteOffEnums.WRITE_OFF_SURE_CHECK_MSG, "批次号重复，请重新制单！");
                }
                DocWriteOffRelationVo workFlowRelationVo = null;
                for (DocWriteOffRelationVo relationDataVo : relationDataList) {
                    DocWriteOffRelation docWriteOffRelation = new DocWriteOffRelation();
                    BeanUtils.copyNotNullProperties(relationDataVo, docWriteOffRelation);
                    docWriteOffRelation.setRulesName(this.convertRuleByDict(relationDataVo.getBankAccTypeStr()));
                    docWriteOffRelation.setExchangeRate(relationVo.getExchangeRate());
                    docWriteOffRelation.setWriteOffStatus(relationDataVo.getAfterDocWriteOffStatus());
                    docWriteOffRelation.setCreateBy(user.getId());
                    docWriteOffRelation.setCreateDate(DateUtils.getDate());
                    docWriteOffRelation.setTenantId(user.getTenantId());
                    docWriteOffRelation.setBatchNo(batchNo);
                    // 执行金额
                    Map<String,Object> executeMoneyMap = relationVo.getExecuteMoneyMap();
                    if(relationVo.getExecuteMoneyMap()==null){
                        throw new BusinessException(WriteOffEnums.WRITE_OFF_SURE_CHECK_MSG, sureData.getCheckMsg());
                    }
                    String id =relationDataVo.getDocId();
                    if("dtl".equals(executeMoneyMap.get("idType")) ){
                         //银行流水
                         id = relationDataVo.getRelationId();
                    }
                    BigDecimal executeMoney=null;
                    try {
                        executeMoney = new BigDecimal(executeMoneyMap.get(id).toString().replaceAll(",",""));
                    }catch (Exception e){
                        throw new BusinessException(WriteOffEnums.WRITE_OFF_SURE_CHECK_MSG, "执行金额输入错误");
                    }

                    docWriteOffRelation.setExecuteMoney(executeMoney);
                    docWriteOffRelation.setStatus(NetbankConstant.STATUE_WAIT_CHECK);
                    if(workFlowRelationVo == null){
                        workFlowRelationVo = new DocWriteOffRelationVo();
                        workFlowRelationVo.setBatchNo(batchNo);
                        //需求未明确应收单多选时单位需要一致
                        workFlowRelationVo.setCorpId(relationDataVo.getCorpId());
                        workFlowRelationVo.setStatus(NetbankConstant.STATUE_WAIT_CHECK);
                    }
                    docWriteOffRelationDao.save(docWriteOffRelation);
                }
                assert workFlowRelationVo != null;
                this.startWorkFlow(workFlowRelationVo, user);
            }
        }else {
            throw new BusinessException(WriteOffEnums.WRITE_OFF_SURE_CHECK_MSG, sureData.getCheckMsg());
        }
    }

    /**
     * 确认核销 审批完成处理
     *
     * @param businessKey
     * @param result
     */
    public void approvePassEnd(String businessKey, boolean result, ICurrentUser user) {
        String batchNo = businessKey;
        if(result){
            DocWriteOffRelationVo writeOffApproveVoByBatchNo = this.getWriteOffApproveVoByBatchNo(batchNo);
            if(WriteoffConstant.WRITE_OFF_TYPE_4.equals(writeOffApproveVoByBatchNo.getWriteOffType())){
                //取消核销
                cancelWriteOffPassed(batchNo, user);
            }else if(WriteoffConstant.WRITE_OFF_TYPE_3.equals(writeOffApproveVoByBatchNo.getWriteOffType())) {
                //强制核销
                List<DocManageVo> docManageVoByIds = writeOffDao.getDocManageVoByIds(Arrays.asList(writeOffApproveVoByBatchNo.getDocId().split(",")), null);
                if (EmptyUtils.isNotEmpty(docManageVoByIds)) {
                    DocManageVo docManageVo = docManageVoByIds.get(0);
                    BigDecimal writeOffBal = docManageVo.getWriteOffBal().subtract(writeOffApproveVoByBatchNo.getRelationConvertMoney());
                    String writeOffStatus = writeOffBal.compareTo(BigDecimal.ZERO)>0?WriteoffConstant.WRITE_OFF_STATUS_1:WriteoffConstant.WRITE_OFF_STATUS_2;
                    writeOffDao.toUpdateDocManageWriteOff(docManageVo.getId(), writeOffBal, writeOffStatus, writeOffApproveVoByBatchNo.getWriteOffType());
                }
                docWriteOffRelationDao.updateRelationByBatchNo(batchNo, NetbankConstant.STATUE_CHECK);
            }else {
                //自动核销或手工核销
                //重新查一遍用以计算余额等信息
                DocSureWriteOffVo sureData = this.getSureData(writeOffApproveVoByBatchNo);
                if(sureData.isCheckFlag()){
                    List<NisReceivableDTO> nisReceivableDTOList = sureData.getNisReceivableDTOList();
                    if(EmptyUtils.isNotEmpty(nisReceivableDTOList)){
                        for (NisReceivableDTO nisReceivableDTO : nisReceivableDTOList) {
                            nisReceivableDTO.setBusBillNo(batchNo);
                            nisReceivableService.saveNisReceivable(nisReceivableDTO, user);
                        }
                    }

                    List<DocWriteOffRelationVo> relationDataList = sureData.getRelationDataList();
                    String fbsStatus = CommonConstant.YES;
                    for (DocWriteOffRelationVo relationDataVo : relationDataList) {
                        writeOffDao.toUpdateDocManageWriteOff(relationDataVo.getDocId(), relationDataVo.getAfterDocWriteOffBal(), relationDataVo.getAfterDocWriteOffStatus(), relationDataVo.getWriteOffType());
                        if(WriteoffConstant.WRITE_OFF_TYPE_1.equals(relationDataVo.getWriteOffType())){
                            writeOffDao.toUpdateDtlWriteOff(relationDataVo.getRelationId(), relationDataVo.getAfterRelationWriteOffBal(), relationDataVo.getAfterRelationWriteOffStatus(), relationDataVo.getWriteOffType(), fbsStatus);
                        }else if(WriteoffConstant.WRITE_OFF_TYPE_2.equals(relationDataVo.getWriteOffType())){
                            if(WriteoffConstant.WRITE_OFF_PAYMENT_MEANS_1.equals(relationDataVo.getPaymentMeans())){
                                writeOffDao.toUpdateDtlWriteOff(relationDataVo.getRelationId(), relationDataVo.getAfterRelationWriteOffBal(), relationDataVo.getAfterRelationWriteOffStatus(), relationDataVo.getWriteOffType(), fbsStatus);
                            }else if(WriteoffConstant.WRITE_OFF_PAYMENT_MEANS_2.equals(relationDataVo.getPaymentMeans())){
                                writeOffDao.toUpdateBillWriteOff(relationDataVo.getRelationId(), relationDataVo.getAfterRelationWriteOffBal(), relationDataVo.getAfterRelationWriteOffStatus(), relationDataVo.getWriteOffType());
                            }else if(WriteoffConstant.WRITE_OFF_PAYMENT_MEANS_3.equals(relationDataVo.getPaymentMeans())){
                                writeOffDao.toUpdateLcWriteOff(relationDataVo.getRelationId(), relationDataVo.getAfterRelationWriteOffBal(), relationDataVo.getAfterRelationWriteOffStatus(), relationDataVo.getWriteOffType());
                            }
                        }
                    }
                    docWriteOffRelationDao.updateRelationByBatchNo(batchNo, NetbankConstant.STATUE_CHECK);
                }else {
                    throw new BusinessException(WriteOffEnums.WRITE_OFF_SURE_CHECK_MSG, sureData.getCheckMsg());
                }
            }
        }else {
            //审核不通过直接删除关系表
            docWriteOffRelationDao.delRelationByBatchNo1(batchNo);
        }
    }

    /**
     * 根据批次号获取核销数据
     * @param batchNo
     * @return
     */
    public DocWriteOffRelationVo getWriteOffApproveVoByBatchNo(String batchNo) {
        DocWriteOffRelationVo relationVo = null;
        List<DocWriteOffRelation> writeOffListByBatchNo = docWriteOffRelationDao.getWriteOffListByBatchNo(batchNo);
        if(EmptyUtils.isNotEmpty(writeOffListByBatchNo)){
            relationVo = new DocWriteOffRelationVo();
            relationVo.setRelationConvertMoney(BigDecimal.ZERO);
            relationVo.setRelationOriginMoney(BigDecimal.ZERO);
            for (DocWriteOffRelation relation : writeOffListByBatchNo) {
                if(StringUtils.isEmpty(relationVo.getDocId())){
                    relationVo.setDocId(relation.getDocId());
                }else{
                    relationVo.setDocId(relationVo.getDocId().concat(",").concat(relation.getDocId()));
                }
                if(StringUtils.isEmpty(relationVo.getRelationId())){
                    relationVo.setRelationId(relation.getRelationId());
                }else{
                    relationVo.setRelationId(relationVo.getRelationId().concat(",").concat(relation.getRelationId()));
                }
                relationVo.setExchangeRate(relation.getExchangeRate());
                relationVo.setWriteOffType(relation.getWriteOffType());
                relationVo.setPaymentMeans(relation.getPaymentMeans());
                relationVo.setRelationConvertMoney(relationVo.getRelationConvertMoney().add(relation.getRelationConvertMoney()));
                relationVo.setRelationOriginMoney(relationVo.getRelationOriginMoney().add(relation.getRelationOriginMoney()));
            }
        }
        return relationVo;
    }

    /**
     * 取消核销
     * @param relationVo
     * @param user
     */
    public void cancelWriteOff(DocWriteOffRelationVo relationVo, ICurrentUser user){
        //获取一批次所有核销数据
        List<String> writeOffStatusOfCancel = Arrays.asList(new String[]{WriteoffConstant.WRITE_OFF_STATUS_1,WriteoffConstant.WRITE_OFF_STATUS_2});
        List<DocWriteOffRelation> docWriteOffRelation = docWriteOffRelationDao.getDocWriteOffRelation("", relationVo.getBatchNo(), writeOffStatusOfCancel);
        if(EmptyUtils.isNotEmpty(docWriteOffRelation)) {
            //生成新的取消核销关系数据
            String batchNo = this.getWriteOffBatchNo();
            List<DocWriteOffRelation> cancelRelation = BeanUtils.copyListProperties(docWriteOffRelation, DocWriteOffRelation.class);
            for (DocWriteOffRelation relation : cancelRelation) {
                relation.setId(null);
                relation.setCancelBatchNo(relation.getBatchNo());
                relation.setBatchNo(batchNo);
                relation.setWriteOffType(WriteoffConstant.WRITE_OFF_TYPE_4);
                relation.setCreateBy(user.getId());
                relation.setCreateDate(DateUtils.getDate());
                relation.setStatus(NetbankConstant.STATUE_WAIT_CHECK);
            }
            docWriteOffRelationDao.batchSave(cancelRelation);

            List<DocManageVo> docManageVoByIds = writeOffDao.getDocManageVoByIds(Arrays.asList(cancelRelation.get(0).getDocId()), writeOffStatusOfCancel);
            DocWriteOffRelationVo workFlowRelationVo = new DocWriteOffRelationVo();
            workFlowRelationVo.setBatchNo(batchNo);
            //需求未明确应收单多选时单位需要一致
            workFlowRelationVo.setCorpId(docManageVoByIds.get(0).getCorpId());
            workFlowRelationVo.setStatus(NetbankConstant.STATUE_WAIT_CHECK);
            this.startWorkFlow(workFlowRelationVo, user);
        }else {
            throw new BusinessException(WriteOffEnums.WRITE_OFF_SURE_CHECK_MSG, "数据状态已发生变化，请刷新页面后重试！");
        }
    }

    /**
     * 审批通过后 取消核销
     * @param batchNo
     */
    public void cancelWriteOffPassed(String batchNo, ICurrentUser user){
        Map<String,List<String>> relationIdsMap = new HashMap<>();
        List<String> relationIds = null;
        BigDecimal exchangeRate = BigDecimal.ONE;
        String paymentMeansKey="";
        Map<String,BigDecimal> relationCurMoneyMap = new HashMap<>();
        List<String> docIds = new ArrayList<>();
        Map<String,BigDecimal> docMoneyMap = new HashMap<>();
        //获取一批次所有核销数据
        List<String> writeOffStatusOfCancel = Arrays.asList(new String[]{WriteoffConstant.WRITE_OFF_STATUS_1,WriteoffConstant.WRITE_OFF_STATUS_2});

        List<DocWriteOffRelation> cancelRelation = docWriteOffRelationDao.getDocWriteOffRelation("", batchNo, writeOffStatusOfCancel);
        docWriteOffRelationDao.delRelationByBatchNo(batchNo);
        // 取消核销审批通过状态
//        docWriteOffRelationDao.updateRelationByBatchNo(batchNo, NetbankConstant.STATUE_CHECK);

        String cancelBatchNo = cancelRelation.get(0).getCancelBatchNo();
        List<DocWriteOffRelation> docWriteOffRelation = docWriteOffRelationDao.getDocWriteOffRelation("", cancelBatchNo, writeOffStatusOfCancel);
        if(EmptyUtils.isNotEmpty(docWriteOffRelation)){
            for (DocWriteOffRelation relation : docWriteOffRelation) {
                //记录对应表id
                paymentMeansKey = relation.getPaymentMeans();
                if(relationIdsMap.containsKey(paymentMeansKey)){
                    relationIds = relationIdsMap.get(paymentMeansKey);
                }else {
                    relationIds = new ArrayList<>();
                }
                relationIds.add(relation.getRelationId());
                relationIdsMap.put(paymentMeansKey, relationIds);
                if(relationCurMoneyMap.containsKey(relation.getRelationId())){
                    relationCurMoneyMap.put(relation.getRelationId(), relationCurMoneyMap.get(relation.getRelationId()).add(relation.getRelationOriginMoney()));
                }else {
                    relationCurMoneyMap.put(relation.getRelationId(), relation.getRelationOriginMoney());
                }
                if(docMoneyMap.containsKey(relation.getDocId())){
                    docMoneyMap.put(relation.getDocId(), docMoneyMap.get(relation.getDocId()).add(relation.getRelationConvertMoney()));
                }else {
                    docMoneyMap.put(relation.getDocId(), relation.getRelationConvertMoney());
                }
                docIds.add(relation.getDocId());
                //清除关联记录
                //docWriteOffRelationDao.delete(relation);
                //作废收款单
                if("11".equals(relation.getWriteOffNum()) || "21".equals(relation.getWriteOffNum())){
                    this.abolishNisReceivable(relation.getDocId(), user);
                }else {
                    this.abolishNisReceivable(relation.getRelationId(), user);
                }
            }
            docWriteOffRelationDao.delRelationByBatchNo(cancelBatchNo);

            if(EmptyUtils.isNotEmpty(docIds)){
                List<DocManageVo> docManageVoByIds = writeOffDao.getDocManageVoByIds(docIds, writeOffStatusOfCancel);
                if(EmptyUtils.isNotEmpty(docManageVoByIds)){
                    BigDecimal docWriteOffBal = null;
                    for (DocManageVo docManageVoById : docManageVoByIds) {
                        //还原应收单核销信息
                        String writeOffStatus = WriteoffConstant.WRITE_OFF_STATUS_1;
                        String writeOffType = WriteoffConstant.WRITE_OFF_TYPE_4;
                        docWriteOffBal = docManageVoById.getWriteOffBal().add(docMoneyMap.get(docManageVoById.getId()));
                        if(docManageVoById.getReceivableMoney().compareTo(docWriteOffBal)<=0){
                            docWriteOffBal = null;
                            writeOffStatus = WriteoffConstant.WRITE_OFF_STATUS_0;
                        }
                        writeOffDao.toUpdateDocManageWriteOff(docManageVoById.getId(), docWriteOffBal, writeOffStatus, writeOffType);
                    }
                }
            }

            if(relationIdsMap.containsKey(WriteoffConstant.WRITE_OFF_PAYMENT_MEANS_1)){
                //还原对应流水表数据
                this.cancelDtlWriteOff(relationIdsMap.get(WriteoffConstant.WRITE_OFF_PAYMENT_MEANS_1), relationCurMoneyMap, user);
            }else if(relationIdsMap.containsKey(WriteoffConstant.WRITE_OFF_PAYMENT_MEANS_2)){
                //还原对应票据表数据
                this.cancelBillWriteOff(relationIdsMap.get(WriteoffConstant.WRITE_OFF_PAYMENT_MEANS_2), relationCurMoneyMap, user);
            }else if(relationIdsMap.containsKey(WriteoffConstant.WRITE_OFF_PAYMENT_MEANS_3)){
                //还原对应信用证表数据
                this.cancelLcWriteOff(relationIdsMap.get(WriteoffConstant.WRITE_OFF_PAYMENT_MEANS_3), relationCurMoneyMap, user);
            }
        }else {
            throw new BusinessException(WriteOffEnums.WRITE_OFF_SURE_CHECK_MSG, "数据状态已发生变化，请刷新页面后重试！");
        }
    }

    /**
     * 还原流水表核销信息
     * @param dtlIdList
     * @param relationCurMoneyMap
     */
    public void cancelDtlWriteOff(List<String> dtlIdList, Map<String,BigDecimal> relationCurMoneyMap, ICurrentUser user){
        //获取当前数据库流水数据
        List<String> writeOffStatusOfCancel = Arrays.asList(new String[]{WriteoffConstant.WRITE_OFF_STATUS_1,WriteoffConstant.WRITE_OFF_STATUS_2});
        List<BisAccDtlVO> dtlDataByIds = writeOffDao.getDtlDataByIds(dtlIdList, writeOffStatusOfCancel);
        if(EmptyUtils.isNotEmpty(dtlDataByIds)){
            String dtlId;
            String fbsStatus = CommonConstant.NO;
            for (BisAccDtlVO dtlDataById : dtlDataByIds) {
                dtlId = dtlDataById.getId();
                if(dtlDataById.getAmt().compareTo(dtlDataById.getWriteOffBal().add(relationCurMoneyMap.get(dtlId))) > 0){
                    //还关联其他应收单，不修改核销类型和状态
                    writeOffDao.toUpdateDtlWriteOff(dtlId, dtlDataById.getWriteOffBal().add(relationCurMoneyMap.get(dtlId)), WriteoffConstant.WRITE_OFF_STATUS_1, WriteoffConstant.WRITE_OFF_TYPE_4, fbsStatus);
                }else {
                    writeOffDao.toUpdateDtlWriteOff(dtlId, null, WriteoffConstant.WRITE_OFF_STATUS_0, WriteoffConstant.WRITE_OFF_TYPE_4, dtlDataById.getFbsStatus());
                }

            }
        }
    }

    /**
     * 还原票据表核销信息
     * @param billIdList
     * @param relationCurMoneyMap
     */
    public void cancelBillWriteOff(List<String> billIdList, Map<String,BigDecimal> relationCurMoneyMap, ICurrentUser user){
        //获取当前数据库流水数据
        List<String> writeOffStatusOfCancel = Arrays.asList(new String[]{WriteoffConstant.WRITE_OFF_STATUS_1,WriteoffConstant.WRITE_OFF_STATUS_2});
        List<AbBillPayAndReceVO> billDataByIds = writeOffDao.getBillDataByIds(billIdList, writeOffStatusOfCancel);
        if(EmptyUtils.isNotEmpty(billDataByIds)){
            String billId;
            for (AbBillPayAndReceVO billDataById : billDataByIds) {
                billId = billDataById.getId();
                if(billDataById.getBillMoney().compareTo(billDataById.getWriteOffBal().add(relationCurMoneyMap.get(billId))) > 0){
                    //还关联其他应收单，不修改核销类型和状态
                    writeOffDao.toUpdateBillWriteOff(billId, billDataById.getWriteOffBal().add(relationCurMoneyMap.get(billId)), WriteoffConstant.WRITE_OFF_STATUS_1, WriteoffConstant.WRITE_OFF_TYPE_4);
                }else {
                    writeOffDao.toUpdateBillWriteOff(billId, null, WriteoffConstant.WRITE_OFF_STATUS_0, WriteoffConstant.WRITE_OFF_TYPE_4);
                }
            }
        }
    }

    /**
     * 还原信用证表核销信息
     * @param lcIdList
     * @param relationCurMoneyMap
     */
    public void cancelLcWriteOff(List<String> lcIdList, Map<String,BigDecimal> relationCurMoneyMap, ICurrentUser user){
        //获取当前数据库流水数据
        List<String> writeOffStatusOfCancel = Arrays.asList(new String[]{WriteoffConstant.WRITE_OFF_STATUS_1,WriteoffConstant.WRITE_OFF_STATUS_2});
        List<LcBillArrivalVO> lcDataByIds = writeOffDao.getLcDataByIds(lcIdList, writeOffStatusOfCancel);
        if(EmptyUtils.isNotEmpty(lcDataByIds)){
            String lcId;
            String fbsStatus = CommonConstant.NO;
            for (LcBillArrivalVO lcDataById : lcDataByIds) {
                lcId = lcDataById.getId();
                if(lcDataById.getIncomingMoney().compareTo(lcDataById.getWriteOffBal().add(relationCurMoneyMap.get(lcId))) > 0){
                    //还关联其他应收单，不修改核销类型和状态
                    writeOffDao.toUpdateLcWriteOff(lcId, lcDataById.getWriteOffBal().add(relationCurMoneyMap.get(lcId)), WriteoffConstant.WRITE_OFF_STATUS_1, WriteoffConstant.WRITE_OFF_TYPE_4);
                }else {
                    writeOffDao.toUpdateLcWriteOff(lcId, null, WriteoffConstant.WRITE_OFF_STATUS_0, WriteoffConstant.WRITE_OFF_TYPE_4);
                }
            }
        }
    }

    /**
     * 获取规则中文
     * @param bankAccTypeStr
     * @return
     */
    public String convertRuleByDict(String bankAccTypeStr) {
        StringBuffer rules = new StringBuffer();
        if(StringUtils.isNotEmpty(bankAccTypeStr)){
            String[] rulesCodeArr = bankAccTypeStr.split(",");
            for (int i = 0; i < rulesCodeArr.length; i++) {
                if(i==0){
                    rules.append(iDictService.get("writeOff.autoMatchRule", rulesCodeArr[i]));
                }else {
                    rules.append("+").append(iDictService.get("writeOff.autoMatchRule", rulesCodeArr[i]));
                }
            }
        }
        return rules.toString();
    }

    /**
     * 获取核销确认页面数据
     * @param dataTablesInput
     * @return
     */
    public DataTablesOutput getSureDataByInput(DataTablesInput dataTablesInput){
        DocWriteOffRelationVo relationVo = new DocWriteOffRelationVo();
        String batchNo = (String) dataTablesInput.getFormdata().get("batchNo");
        if(StringUtils.isEmpty(batchNo)){
            relationVo.setDocId(String.valueOf(dataTablesInput.getFormdata().get("docId")));
            relationVo.setRelationId(String.valueOf(dataTablesInput.getFormdata().get("relationId")));
            relationVo.setWriteOffType(String.valueOf(dataTablesInput.getFormdata().get("writeOffType")));
            relationVo.setPaymentMeans(String.valueOf(dataTablesInput.getFormdata().get("paymentMeans")));
            relationVo.setBankAccTypeStr(String.valueOf(dataTablesInput.getFormdata().get("bankAccTypeStr")));
            if(StringUtils.isNotEmpty(dataTablesInput.getFormdata().get("exchangeRate"))){
                relationVo.setExchangeRate(new BigDecimal(String.valueOf(dataTablesInput.getFormdata().get("exchangeRate"))));
            }
        }else {
            List<DocWriteOffRelation> docWriteOffRelation = docWriteOffRelationDao.getDocWriteOffRelation(null, batchNo, null);
            DocWriteOffRelation relation = null;
            for (int i = 0; i < docWriteOffRelation.size(); i++) {
                relation =docWriteOffRelation.get(i);
                if(i==0){
                    relationVo.setDocId(relation.getDocId());
                    relationVo.setRelationId(relation.getRelationId());
                    relationVo.setWriteOffType(relation.getWriteOffType());
                    relationVo.setPaymentMeans(relation.getPaymentMeans());
                    relationVo.setExchangeRate(relation.getExchangeRate());
                }else {
                    relationVo.setDocId(relationVo.getDocId().concat(",").concat(relation.getDocId()));
                    relationVo.setRelationId(relationVo.getRelationId().concat(",").concat(relation.getRelationId()));
                }
            }
        }

        DocSureWriteOffVo sureData = this.getSureData(relationVo);
        DataTablesOutput out = new DataTablesOutput();
        if(sureData.isCheckFlag()){
            List<DocWriteOffRelationVo> relationDataList = sureData.getRelationDataList();
            //以应收单为展示主体
            if(EmptyUtils.isNotEmpty(relationDataList)){
                Set<String> docIdSet = new HashSet<>();
                List<DocWriteOffRelationVo> outDataList = new ArrayList<>();
                for (DocWriteOffRelationVo docWriteOffRelationVo : relationDataList) {
                    if(!docIdSet.contains(docWriteOffRelationVo.getDocId())){
                        docIdSet.add(docWriteOffRelationVo.getDocId());
                        docWriteOffRelationVo.setCheckMsg(sureData.getCheckMsg());
                        outDataList.add(docWriteOffRelationVo);
                    }
                }
                out.setData(outDataList);
                out.setRecordsTotal(outDataList.size());
                out.setRecordsFiltered(outDataList.size());
                out.setDraw(dataTablesInput.getDraw());
            }
        }else {
            throw new BusinessException(WriteOffEnums.WRITE_OFF_SURE_CHECK_MSG, sureData.getCheckMsg());
        }

        return out;
    }

    /**
     * 获取自动核销处理确认页面数据
     * @param dataTablesInput
     * @return
     */
    public DataTablesOutput getSureAutoDataByInput(DataTablesInput dataTablesInput){
        DataTablesOutput out = this.getSureDataByInput(dataTablesInput);
        List<DocWriteOffRelationVo> data = out.getData();
        Integer start = dataTablesInput.getStart();
        Integer length = dataTablesInput.getLength();

        List<DocWriteOffRelationVo> list = data.subList(start, data.size()).stream().limit(length).collect(Collectors.toList());
//        inject(list);
        out.setData(list);
        DataTablesOutput result = new DataTablesOutput();
        result.setData(list);
        result.setRecordsTotal(data.size());
        result.setRecordsFiltered(data.size());
        result.setDraw(dataTablesInput.getDraw());
        return result;
    }

    /**
     * 获取核销确认页面数据
     * @param relationVo
     * @return
     */
    public DocSureWriteOffVo getSureData(DocWriteOffRelationVo relationVo){
        DocSureWriteOffVo sureDataVo = null;

        List<String> writeOffStatus = Arrays.asList(new String[]{WriteoffConstant.WRITE_OFF_STATUS_0,WriteoffConstant.WRITE_OFF_STATUS_1});
        List<DocManageVo> docManageVoByIds = new ArrayList<>();
        if(StringUtils.isNotEmpty(relationVo.getDocId())){
            String[] ids = relationVo.getDocId().split(",");
            for (String id : ids) {
                List<DocManageVo> docManageVoById = writeOffDao.getDocManageVoByIds(Arrays.asList(id), writeOffStatus);
                docManageVoByIds.addAll(docManageVoById);
            }
        }

        List<BisAccDtlVO> dtlVoByIds = new ArrayList<>();
        if(WriteoffConstant.WRITE_OFF_PAYMENT_MEANS_1.equals(relationVo.getPaymentMeans()) && StringUtils.isNotEmpty(relationVo.getRelationId())){
            String[] ids = relationVo.getRelationId().split(",");
            for (String id : ids) {
                List<BisAccDtlVO> dtlDataById = writeOffDao.getDtlDataByIds(Arrays.asList(id), writeOffStatus);
                dtlVoByIds.addAll(dtlDataById);
            }
        }

        List<AbBillPayAndReceVO> billVoByIds = null;
        if(WriteoffConstant.WRITE_OFF_PAYMENT_MEANS_2.equals(relationVo.getPaymentMeans()) && StringUtils.isNotEmpty(relationVo.getRelationId())){
            billVoByIds = writeOffDao.getBillDataByIds(Arrays.asList(relationVo.getRelationId().split(",")), writeOffStatus);
        }

        List<LcBillArrivalVO> lcVoByIds = null;
        if(WriteoffConstant.WRITE_OFF_PAYMENT_MEANS_3.equals(relationVo.getPaymentMeans()) && StringUtils.isNotEmpty(relationVo.getRelationId())){
            lcVoByIds = writeOffDao.getLcDataByIds(Arrays.asList(relationVo.getRelationId().split(",")), writeOffStatus);
        }

        if(WriteoffConstant.WRITE_OFF_TYPE_1.equals(relationVo.getWriteOffType())){
            sureDataVo = this.getAutoSureData(relationVo, docManageVoByIds, dtlVoByIds);
        }else if(WriteoffConstant.WRITE_OFF_TYPE_2.equals(relationVo.getWriteOffType())){
            if(WriteoffConstant.WRITE_OFF_PAYMENT_MEANS_1.equals(relationVo.getPaymentMeans())){
                sureDataVo = this.getManualDtlSureData(relationVo, docManageVoByIds, dtlVoByIds);
            }else if(WriteoffConstant.WRITE_OFF_PAYMENT_MEANS_2.equals(relationVo.getPaymentMeans())){
                sureDataVo = this.getManualBillSureData(relationVo, docManageVoByIds, billVoByIds);
            }else if(WriteoffConstant.WRITE_OFF_PAYMENT_MEANS_3.equals(relationVo.getPaymentMeans())){
                sureDataVo = this.getManualLcSureData(relationVo, docManageVoByIds, lcVoByIds);
            }
        }
        return sureDataVo;
    }

    /**
     * 获取确认页面 自动核销数据
     *      -自动核销仅支持 银行流水、同币种、一对一
     * @param relationVo
     * @param docManageVoByIds
     * @param dtlVoByIds
     */
    private DocSureWriteOffVo getAutoSureData(DocWriteOffRelationVo relationVo, List<DocManageVo> docManageVoByIds, List<BisAccDtlVO> dtlVoByIds){
        DocSureWriteOffVo sureDataVo = new DocSureWriteOffVo(true);

        List<DocWriteOffRelationVo> sureDataList = new ArrayList<>();

        List<NisReceivableDTO> nisReceivableDTOList = new ArrayList<>();
        NisReceivableDTO nisReceivableByDoc = null;

        if(EmptyUtils.isEmpty(docManageVoByIds) || EmptyUtils.isEmpty(dtlVoByIds)){
            sureDataVo.setCheckFlag(false);
            sureDataVo.setCheckMsg("数据状态已发生变化，请刷新页面后重试！");
        }else if(docManageVoByIds.size()!= dtlVoByIds.size()){
            sureDataVo.setCheckFlag(false);
            sureDataVo.setCheckMsg("自动核销仅支持一对一核销！");
        }else {
            for (int i=0;i<docManageVoByIds.size();i++) {
                for (int j=0;j<dtlVoByIds.size();j++) {
                    if (i==j) {
                        //自动核销仅支持 银行流水、同币种、一对一
                        DocManageVo docManageVo = docManageVoByIds.get(i);
                        BisAccDtlVO bisAccDtlVo = dtlVoByIds.get(j);

                        if(!docManageVo.getCurId().equals(bisAccDtlVo.getCurId())){
                            sureDataVo.setCheckFlag(false);
                            sureDataVo.setCheckMsg("自动核销仅支持同币种核销！");
                        }else {
                            DocWriteOffRelationVo relationDataVo = new DocWriteOffRelationVo();
                            relationDataVo.setPaymentMeans(relationVo.getPaymentMeans());
                            relationDataVo.setWriteOffType(relationVo.getWriteOffType());
                            relationDataVo.setBankAccTypeStr(relationVo.getBankAccTypeStr());

                            relationDataVo.setWriteOffNum(WriteoffConstant.WRITE_OFF_NUM_11);
                            relationDataVo.setDocId(docManageVo.getId());
                            relationDataVo.setDocMoney(docManageVo.getWriteOffBal());
                            relationDataVo.setAfterDocWriteOffStatus(WriteoffConstant.WRITE_OFF_STATUS_2);
                            relationDataVo.setAfterDocWriteOffBal(docManageVo.getWriteOffBal().subtract(bisAccDtlVo.getAmt()));

                            relationDataVo.setRelationId(bisAccDtlVo.getId());
                            relationDataVo.setRelationOriginMoney(bisAccDtlVo.getAmt());
                            relationDataVo.setRelationConvertMoney(bisAccDtlVo.getAmt());
                            relationDataVo.setExchangeRate(BigDecimal.ONE);
                            relationDataVo.setAfterRelationWriteOffStatus(WriteoffConstant.WRITE_OFF_STATUS_2);
                            relationDataVo.setAfterRelationWriteOffBal(bisAccDtlVo.getAmt().subtract(docManageVo.getWriteOffBal()));

                            BigDecimal writeOffBal1 = docManageVo.getWriteOffBal();
                            BigDecimal writeOffBal2 = bisAccDtlVo.getWriteOffBal();
                            //选择二者更小的值做为执行金额
                            BigDecimal writeOffBal = BigDecimal.ZERO;
                            if (writeOffBal1.compareTo(writeOffBal2) == -1 || writeOffBal1.compareTo(writeOffBal2) == 0) {
                                writeOffBal = writeOffBal1;
                            } else {
                                writeOffBal = writeOffBal2;
                            }
                            docManageVo.setExecuteMoney(writeOffBal);
                            docManageVo.setNewWriteOffBal(writeOffBal1.subtract(writeOffBal));
                            relationDataVo.setAfterDocWriteOffBal(writeOffBal1.subtract(writeOffBal));

                            bisAccDtlVo.setNewWriteOffBal(writeOffBal2.subtract(writeOffBal));
                            relationDataVo.setAfterRelationWriteOffBal(writeOffBal2.subtract(writeOffBal));
                            bisAccDtlVo.setExecuteMoney(writeOffBal);

                            relationDataVo.setBeforeWriteOffBal(docManageVo.getWriteOffBal());

                            relationDataVo.setRelationOriginMoney(writeOffBal);
                            relationDataVo.setRelationConvertMoney(writeOffBal);

                            Date transTime = bisAccDtlVo.getTransTime();
                            String strTransTime = transTime.toString();
                            Date dateYMD = DateUtils.getDateYMD(transTime);
                            String dateTime = strTransTime.substring(11,strTransTime.length()-2);
                            bisAccDtlVo.setTransTime(dateYMD);
                            bisAccDtlVo.setTransDateTime(dateTime);
                            //之前已在getSureDataByInput()方法里面将二者的核销状态都定为“已核销“，这里只需要判断是否为部分核销
                            if (writeOffBal.compareTo(writeOffBal1) == -1) {
                                relationDataVo.setAfterDocWriteOffStatus(WriteoffConstant.WRITE_OFF_STATUS_1);
                            }
                            if (writeOffBal.compareTo(writeOffBal2) == -1) {
                                relationDataVo.setAfterRelationWriteOffStatus(WriteoffConstant.WRITE_OFF_STATUS_1);
                            }

                            relationDataVo.setDocManageVo(docManageVo);
                            relationDataVo.setBisAccDtlVO(bisAccDtlVo);
                            relationDataVo.setCorpId(docManageVo.getCorpId());

                            sureDataList.add(relationDataVo);
//                            inject(relationDataVo);

                            //生成收款单
                            nisReceivableDTOList.add(this.createNisReceivableByDoc(docManageVo, docManageVo.getWriteOffBal()));
                        }
                    }
                }
            }
        }

        sureDataVo.setRelationDataList(sureDataList);
        sureDataVo.setNisReceivableDTOList(nisReceivableDTOList);

        return sureDataVo;
    }

    /**
     * 获取确认页面 手工核销-结算方式银行流水数据
     * @param relationVo
     * @param docManageVoByIds
     * @param dtlVoByIds
     */
    private DocSureWriteOffVo getManualDtlSureData(DocWriteOffRelationVo relationVo, List<DocManageVo> docManageVoByIds, List<BisAccDtlVO> dtlVoByIds){
        DocSureWriteOffVo sureDataVo = new DocSureWriteOffVo(true);

        if(EmptyUtils.isNotEmpty(docManageVoByIds)){
            Set<DocManageVo> temp = new HashSet<>(docManageVoByIds);
            docManageVoByIds = new ArrayList<>(temp);
        }

        if(EmptyUtils.isNotEmpty(dtlVoByIds)){
            Set<BisAccDtlVO> temp = new HashSet<>(dtlVoByIds);
            dtlVoByIds = new ArrayList<>(temp);
        }


        if(EmptyUtils.isEmpty(docManageVoByIds) || EmptyUtils.isEmpty(dtlVoByIds)){
            sureDataVo.setCheckFlag(false);
            sureDataVo.setCheckMsg("数据状态已发生变化，请刷新页面后重试！");
        }else if(docManageVoByIds.size()>1 && dtlVoByIds.size()>1){
            sureDataVo.setCheckFlag(false);
            sureDataVo.setCheckMsg("自动核销不支持多对多核销！");
        }else {
            //计算总额，并判断多条时币种是否一致
            BigDecimal docMoney = BigDecimal.ZERO;
            String docCurId = docManageVoByIds.get(0).getCurId();
            for (DocManageVo docManageVoById : docManageVoByIds) {
                docMoney = docMoney.add(docManageVoById.getWriteOffBal());
                if(!docCurId.equals(docManageVoById.getCurId())){
                    sureDataVo.setCheckFlag(false);
                    sureDataVo.setCheckMsg("多条应收单必须同一币种！");
                    break;
                }
            }
            BigDecimal dtlMoney = BigDecimal.ZERO;
            String dtlCurId = dtlVoByIds.get(0).getCurId();
            if(sureDataVo.isCheckFlag()){
                for (BisAccDtlVO dtlVoById : dtlVoByIds) {
                    dtlMoney = dtlMoney.add(dtlVoById.getWriteOffBal());
                    if(!dtlCurId.equals(dtlVoById.getCurId())){
                        sureDataVo.setCheckFlag(false);
                        sureDataVo.setCheckMsg("多条流水必须同一币种！");
                        break;
                    }
                }
            }

            if(sureDataVo.isCheckFlag()){
                //根据输入汇率计算应收币种金额
                BigDecimal relationConvertMoney = BigDecimal.ZERO;
                BigDecimal exchangeRate = BigDecimal.ONE;
                if(!docCurId.equals(dtlCurId) && relationVo.getExchangeRate()!=null){
                    exchangeRate = relationVo.getExchangeRate();
                    relationConvertMoney = dtlMoney.multiply(relationVo.getExchangeRate()).setScale(2, BigDecimal.ROUND_HALF_UP);
                }else {
                    relationConvertMoney = dtlMoney;
                }

                DocWriteOffRelationVo relationCheckVo = new DocWriteOffRelationVo();
                relationCheckVo.setDocNum(docManageVoByIds.size());
                relationCheckVo.setDocMoney(docMoney);
                relationCheckVo.setRelationNum(dtlVoByIds.size());
                relationCheckVo.setRelationConvertMoney(relationConvertMoney);
                relationCheckVo.setExchangeRate(exchangeRate);
                this.checkManualDocAndRelation(sureDataVo, relationCheckVo);

                if(sureDataVo.isCheckFlag()){
                    List<DocWriteOffRelationVo> relationDataList = new ArrayList<>();
                    DocWriteOffRelationVo sureRelationVo = null;
                    List<NisReceivableDTO> nisReceivableDTOList = new ArrayList<>();
                    if(relationCheckVo.getDocNum() < relationCheckVo.getRelationNum()){
                        //一对多
                        DocManageVo docManageVo = docManageVoByIds.get(0);
                        for (BisAccDtlVO dtlVoById : dtlVoByIds) {
                            sureRelationVo = new DocWriteOffRelationVo();
                            sureRelationVo.setWriteOffNum(relationCheckVo.getWriteOffNum());
                            sureRelationVo.setDocId(docManageVo.getId());
                            sureRelationVo.setDocMoney(relationCheckVo.getDocMoney());
                            sureRelationVo.setAfterDocWriteOffBal(relationCheckVo.getAfterDocWriteOffBal());
                            sureRelationVo.setAfterDocWriteOffStatus(relationCheckVo.getAfterDocWriteOffStatus());

                            sureRelationVo.setBeforeWriteOffBal(docManageVo.getWriteOffBal());

                            sureRelationVo.setRelationId(dtlVoById.getId());
                            sureRelationVo.setRelationOriginMoney(dtlVoById.getWriteOffBal());
                            sureRelationVo.setRelationConvertMoney(dtlVoById.getWriteOffBal().multiply(exchangeRate).setScale(2, BigDecimal.ROUND_HALF_UP));
                            sureRelationVo.setAfterRelationWriteOffBal(relationCheckVo.getAfterRelationWriteOffBal());
                            sureRelationVo.setAfterRelationWriteOffStatus(relationCheckVo.getAfterRelationWriteOffStatus());

                            sureRelationVo.setWriteOffType(relationVo.getWriteOffType());
                            sureRelationVo.setPaymentMeans(relationVo.getPaymentMeans());
                            sureRelationVo.setExchangeRate(relationCheckVo.getExchangeRate());
                            sureRelationVo.setDocManageVo(docManageVo);
                            sureRelationVo.setCorpId(docManageVo.getCorpId());
//                            inject(sureRelationVo);
                            relationDataList.add(sureRelationVo);

                            //生成收款单
                            nisReceivableDTOList.add(this.createNisReceivableByDtl(dtlVoById));
                        }
                    }else {
                        //一对一或多对一
                        BisAccDtlVO dtlVO = dtlVoByIds.get(0);
                        for (DocManageVo docManageVoById : docManageVoByIds) {
                            sureRelationVo = new DocWriteOffRelationVo();
                            sureRelationVo.setWriteOffNum(relationCheckVo.getWriteOffNum());
                            sureRelationVo.setDocId(docManageVoById.getId());
                            if(relationCheckVo.getDocMoney().compareTo(docManageVoById.getWriteOffBal()) > 0){
                                sureRelationVo.setDocMoney(docManageVoById.getWriteOffBal());
                            }else {
                                sureRelationVo.setDocMoney(relationCheckVo.getDocMoney());
                            }
                            sureRelationVo.setAfterDocWriteOffBal(relationCheckVo.getAfterDocWriteOffBal());
                            sureRelationVo.setAfterDocWriteOffStatus(relationCheckVo.getAfterDocWriteOffStatus());

                            sureRelationVo.setBeforeWriteOffBal(docManageVoById.getWriteOffBal());

                            sureRelationVo.setRelationId(dtlVO.getId());
                            sureRelationVo.setRelationOriginMoney(sureRelationVo.getDocMoney().divide(exchangeRate, 2, BigDecimal.ROUND_HALF_UP));
                            sureRelationVo.setRelationConvertMoney(sureRelationVo.getDocMoney());
                            sureRelationVo.setAfterRelationWriteOffBal(relationCheckVo.getAfterRelationWriteOffBal());
                            sureRelationVo.setAfterRelationWriteOffStatus(relationCheckVo.getAfterRelationWriteOffStatus());

                            sureRelationVo.setWriteOffType(relationVo.getWriteOffType());
                            sureRelationVo.setPaymentMeans(relationVo.getPaymentMeans());
                            sureRelationVo.setExchangeRate(relationCheckVo.getExchangeRate());
                            sureRelationVo.setDocManageVo(docManageVoById);
                            sureRelationVo.setCorpId(docManageVoById.getCorpId());
//                            inject(sureRelationVo);
                            relationDataList.add(sureRelationVo);

                            //生成收款单
                            nisReceivableDTOList.add(this.createNisReceivableByDoc(docManageVoById,sureRelationVo.getDocMoney()));
                        }
                    }
                    sureDataVo.setRelationDataList(relationDataList);
                    sureDataVo.setNisReceivableDTOList(nisReceivableDTOList);
                }
            }
        }
        return sureDataVo;
    }

    /**
     * 获取确认页面 手工核销-结算方式票据数据
     * @param relationVo
     * @param docManageVoByIds
     * @param billVoByIds
     */
    private DocSureWriteOffVo getManualBillSureData(DocWriteOffRelationVo relationVo, List<DocManageVo> docManageVoByIds, List<AbBillPayAndReceVO> billVoByIds){
        DocSureWriteOffVo sureDataVo = new DocSureWriteOffVo(true);

        if(EmptyUtils.isNotEmpty(docManageVoByIds)){
            Set<DocManageVo> temp = new HashSet<>(docManageVoByIds);
            docManageVoByIds = new ArrayList<>(temp);
        }

        if(EmptyUtils.isNotEmpty(billVoByIds)){
            Set<AbBillPayAndReceVO> temp = new HashSet<>(billVoByIds);
            billVoByIds = new ArrayList<>(temp);
        }

        if(EmptyUtils.isEmpty(docManageVoByIds) || EmptyUtils.isEmpty(billVoByIds)){
            sureDataVo.setCheckFlag(false);
            sureDataVo.setCheckMsg("数据状态已发生变化，请刷新页面后重试！");
        }else if(docManageVoByIds.size()>1 && billVoByIds.size()>1){
            sureDataVo.setCheckFlag(false);
            sureDataVo.setCheckMsg("自动核销不支持多对多核销！");
        }else {
            //计算总额，并判断多条时币种是否一致
            BigDecimal docMoney = BigDecimal.ZERO;
            String docCurId = docManageVoByIds.get(0).getCurId();
            for (DocManageVo docManageVoById : docManageVoByIds) {
                docMoney = docMoney.add(docManageVoById.getWriteOffBal());
                if(!docCurId.equals(docManageVoById.getCurId())){
                    sureDataVo.setCheckFlag(false);
                    sureDataVo.setCheckMsg("多条应收单必须同一币种！");
                    break;
                }
            }
            BigDecimal dtlMoney = BigDecimal.ZERO;
            //获取人民币 票据币别默认都是人民币
            String dtlCurId = billVoByIds.get(0).getId();
            if(sureDataVo.isCheckFlag()){
                for (AbBillPayAndReceVO billVoById : billVoByIds) {
                    dtlMoney = dtlMoney.add(billVoById.getWriteOffBal());
                    /*if(!dtlCurId.equals(billVoById.getCurId())){
                        sureDataVo.setCheckFlag(false);
                        sureDataVo.setCheckMsg("多条流水必须同一币种！");
                        break;
                    }*/
                }
            }

            if(sureDataVo.isCheckFlag()){
                //根据输入汇率计算应收币种金额
                BigDecimal relationConvertMoney = BigDecimal.ZERO;
                BigDecimal exchangeRate = BigDecimal.ONE;
                if(!docCurId.equals(dtlCurId) && relationVo.getExchangeRate()!=null){
                    exchangeRate = relationVo.getExchangeRate();
                    relationConvertMoney = dtlMoney.multiply(relationVo.getExchangeRate()).setScale(2, BigDecimal.ROUND_HALF_UP);
                }else {
                    relationConvertMoney = dtlMoney;
                }

                DocWriteOffRelationVo relationCheckVo = new DocWriteOffRelationVo();
                relationCheckVo.setDocNum(docManageVoByIds.size());
                relationCheckVo.setDocMoney(docMoney);
                relationCheckVo.setRelationNum(billVoByIds.size());
                relationCheckVo.setRelationConvertMoney(relationConvertMoney);
                relationCheckVo.setExchangeRate(exchangeRate);
                this.checkManualDocAndRelation(sureDataVo, relationCheckVo);

                if(sureDataVo.isCheckFlag()){
                    List<DocWriteOffRelationVo> relationDataList = new ArrayList<>();
                    DocWriteOffRelationVo sureRelationVo = null;
                    if(relationCheckVo.getDocNum() < relationCheckVo.getRelationNum()){
                        //一对多
                        DocManageVo docManageVo = docManageVoByIds.get(0);
                        for (AbBillPayAndReceVO billVoById : billVoByIds) {
                            sureRelationVo = new DocWriteOffRelationVo();
                            sureRelationVo.setWriteOffNum(relationCheckVo.getWriteOffNum());
                            sureRelationVo.setDocId(docManageVo.getId());
                            sureRelationVo.setDocMoney(relationCheckVo.getDocMoney());
                            sureRelationVo.setAfterDocWriteOffBal(relationCheckVo.getAfterDocWriteOffBal());
                            sureRelationVo.setAfterDocWriteOffStatus(relationCheckVo.getAfterDocWriteOffStatus());

                            sureRelationVo.setRelationId(billVoById.getId());
                            sureRelationVo.setRelationOriginMoney(billVoById.getBillMoney());
                            sureRelationVo.setRelationConvertMoney(billVoById.getBillMoney().multiply(exchangeRate).setScale(2, BigDecimal.ROUND_HALF_UP));
                            sureRelationVo.setAfterRelationWriteOffBal(relationCheckVo.getAfterRelationWriteOffBal());
                            sureRelationVo.setAfterRelationWriteOffStatus(relationCheckVo.getAfterRelationWriteOffStatus());

                            sureRelationVo.setWriteOffType(relationVo.getWriteOffType());
                            sureRelationVo.setPaymentMeans(relationVo.getPaymentMeans());
                            sureRelationVo.setExchangeRate(relationCheckVo.getExchangeRate());
                            sureRelationVo.setDocManageVo(docManageVo);
                            sureRelationVo.setCorpId(docManageVo.getCorpId());
//                            inject(sureRelationVo);
                            relationDataList.add(sureRelationVo);
                        }
                    }else {
                        //一对一或多对一
                        AbBillPayAndReceVO billVO = billVoByIds.get(0);
                        for (DocManageVo docManageVoById : docManageVoByIds) {
                            sureRelationVo = new DocWriteOffRelationVo();
                            sureRelationVo.setWriteOffNum(relationCheckVo.getWriteOffNum());
                            sureRelationVo.setDocId(docManageVoById.getId());
                            if(relationCheckVo.getDocMoney().compareTo(docManageVoById.getWriteOffBal()) > 0){
                                sureRelationVo.setDocMoney(docManageVoById.getWriteOffBal());
                            }else {
                                sureRelationVo.setDocMoney(relationCheckVo.getDocMoney());
                            }
                            sureRelationVo.setAfterDocWriteOffBal(relationCheckVo.getAfterDocWriteOffBal());
                            sureRelationVo.setAfterDocWriteOffStatus(relationCheckVo.getAfterDocWriteOffStatus());

                            sureRelationVo.setRelationId(billVO.getId());
                            sureRelationVo.setRelationOriginMoney(sureRelationVo.getDocMoney().divide(exchangeRate, 2, BigDecimal.ROUND_HALF_UP));
                            sureRelationVo.setRelationConvertMoney(sureRelationVo.getDocMoney());
                            sureRelationVo.setAfterRelationWriteOffBal(relationCheckVo.getAfterRelationWriteOffBal());
                            sureRelationVo.setAfterRelationWriteOffStatus(relationCheckVo.getAfterRelationWriteOffStatus());

                            sureRelationVo.setWriteOffType(relationVo.getWriteOffType());
                            sureRelationVo.setPaymentMeans(relationVo.getPaymentMeans());
                            sureRelationVo.setExchangeRate(relationCheckVo.getExchangeRate());
                            sureRelationVo.setDocManageVo(docManageVoById);
                            sureRelationVo.setCorpId(docManageVoById.getCorpId());
//                            inject(sureRelationVo);
                            relationDataList.add(sureRelationVo);
                        }
                    }
                    sureDataVo.setRelationDataList(relationDataList);
                }
            }
        }
        return sureDataVo;
    }

    /**
     * 获取确认页面 手工核销-结算方式信用证数据
     * @param relationVo
     * @param docManageVoByIds
     * @param lcVoByIds
     */
    private DocSureWriteOffVo getManualLcSureData(DocWriteOffRelationVo relationVo, List<DocManageVo> docManageVoByIds, List<LcBillArrivalVO> lcVoByIds){
        DocSureWriteOffVo sureDataVo = new DocSureWriteOffVo(true);

        if(EmptyUtils.isNotEmpty(docManageVoByIds)){
            Set<DocManageVo> temp = new HashSet<>(docManageVoByIds);
            docManageVoByIds = new ArrayList<>(temp);
        }

        if(EmptyUtils.isNotEmpty(lcVoByIds)){
            Set<LcBillArrivalVO> temp = new HashSet<>(lcVoByIds);
            lcVoByIds = new ArrayList<>(temp);
        }

        if(EmptyUtils.isEmpty(docManageVoByIds) || EmptyUtils.isEmpty(lcVoByIds)){
            sureDataVo.setCheckFlag(false);
            sureDataVo.setCheckMsg("数据状态已发生变化，请刷新页面后重试！");
        }else if(docManageVoByIds.size()>1 && lcVoByIds.size()>1){
            sureDataVo.setCheckFlag(false);
            sureDataVo.setCheckMsg("自动核销不支持多对多核销！");
        }else {
            //计算总额，并判断多条时币种是否一致
            BigDecimal docMoney = BigDecimal.ZERO;
            String docCurId = docManageVoByIds.get(0).getCurId();
            for (DocManageVo docManageVoById : docManageVoByIds) {
                docMoney = docMoney.add(docManageVoById.getWriteOffBal());
                if(!docCurId.equals(docManageVoById.getCurId())){
                    sureDataVo.setCheckFlag(false);
                    sureDataVo.setCheckMsg("多条应收单必须同一币种！");
                    break;
                }
            }
            BigDecimal dtlMoney = BigDecimal.ZERO;
            String dtlCurId = lcVoByIds.get(0).getCurId();
            if(sureDataVo.isCheckFlag()){
                for (LcBillArrivalVO dtlVoById : lcVoByIds) {
                    dtlMoney = dtlMoney.add(dtlVoById.getWriteOffBal());
                    if(!dtlCurId.equals(dtlVoById.getCurId())){
                        sureDataVo.setCheckFlag(false);
                        sureDataVo.setCheckMsg("多条流水必须同一币种！");
                        break;
                    }
                }
            }

            if(sureDataVo.isCheckFlag()){
                //根据输入汇率计算应收币种金额
                BigDecimal relationConvertMoney = BigDecimal.ZERO;
                BigDecimal exchangeRate = BigDecimal.ONE;
                if(!docCurId.equals(dtlCurId) && relationVo.getExchangeRate()!=null){
                    exchangeRate = relationVo.getExchangeRate();
                    relationConvertMoney = dtlMoney.multiply(relationVo.getExchangeRate()).setScale(2, BigDecimal.ROUND_HALF_UP);
                }else {
                    relationConvertMoney = dtlMoney;
                }

                DocWriteOffRelationVo relationCheckVo = new DocWriteOffRelationVo();
                relationCheckVo.setDocNum(docManageVoByIds.size());
                relationCheckVo.setDocMoney(docMoney);
                relationCheckVo.setRelationNum(lcVoByIds.size());
                relationCheckVo.setRelationConvertMoney(relationConvertMoney);
                relationCheckVo.setExchangeRate(exchangeRate);
                this.checkManualDocAndRelation(sureDataVo, relationCheckVo);

                if(sureDataVo.isCheckFlag()){
                    List<DocWriteOffRelationVo> relationDataList = new ArrayList<>();
                    DocWriteOffRelationVo sureRelationVo = null;
                    if(relationCheckVo.getDocNum() < relationCheckVo.getRelationNum()){
                        //一对多
                        DocManageVo docManageVo = docManageVoByIds.get(0);
                        for (LcBillArrivalVO dtlVoById : lcVoByIds) {
                            sureRelationVo = new DocWriteOffRelationVo();
                            sureRelationVo.setWriteOffNum(relationCheckVo.getWriteOffNum());
                            sureRelationVo.setDocId(docManageVo.getId());
                            sureRelationVo.setDocMoney(relationCheckVo.getDocMoney());
                            sureRelationVo.setAfterDocWriteOffBal(relationCheckVo.getAfterDocWriteOffBal());
                            sureRelationVo.setAfterDocWriteOffStatus(relationCheckVo.getAfterDocWriteOffStatus());

                            sureRelationVo.setRelationId(dtlVoById.getId());
                            sureRelationVo.setRelationOriginMoney(dtlVoById.getIncomingMoney());
                            sureRelationVo.setRelationConvertMoney(dtlVoById.getIncomingMoney().multiply(exchangeRate).setScale(2, BigDecimal.ROUND_HALF_UP));
                            sureRelationVo.setAfterRelationWriteOffBal(relationCheckVo.getAfterRelationWriteOffBal());
                            sureRelationVo.setAfterRelationWriteOffStatus(relationCheckVo.getAfterRelationWriteOffStatus());

                            sureRelationVo.setWriteOffType(relationVo.getWriteOffType());
                            sureRelationVo.setPaymentMeans(relationVo.getPaymentMeans());
                            sureRelationVo.setExchangeRate(relationCheckVo.getExchangeRate());
                            sureRelationVo.setDocManageVo(docManageVo);
                            sureRelationVo.setCorpId(docManageVo.getCorpId());
//                            inject(sureRelationVo);
                            relationDataList.add(sureRelationVo);
                        }
                    }else {
                        //一对一或多对一
                        LcBillArrivalVO dtlVO = lcVoByIds.get(0);
                        for (DocManageVo docManageVoById : docManageVoByIds) {
                            sureRelationVo = new DocWriteOffRelationVo();
                            sureRelationVo.setWriteOffNum(relationCheckVo.getWriteOffNum());
                            sureRelationVo.setDocId(docManageVoById.getId());
                            if(relationCheckVo.getDocMoney().compareTo(docManageVoById.getWriteOffBal()) > 0){
                                sureRelationVo.setDocMoney(docManageVoById.getWriteOffBal());
                            }else {
                                sureRelationVo.setDocMoney(relationCheckVo.getDocMoney());
                            }
                            sureRelationVo.setAfterDocWriteOffBal(relationCheckVo.getAfterDocWriteOffBal());
                            sureRelationVo.setAfterDocWriteOffStatus(relationCheckVo.getAfterDocWriteOffStatus());

                            sureRelationVo.setRelationId(dtlVO.getId());
                            sureRelationVo.setRelationOriginMoney(sureRelationVo.getDocMoney().divide(exchangeRate, 2, BigDecimal.ROUND_HALF_UP));
                            sureRelationVo.setRelationConvertMoney(sureRelationVo.getDocMoney());
                            sureRelationVo.setAfterRelationWriteOffBal(relationCheckVo.getAfterRelationWriteOffBal());
                            sureRelationVo.setAfterRelationWriteOffStatus(relationCheckVo.getAfterRelationWriteOffStatus());

                            sureRelationVo.setWriteOffType(relationVo.getWriteOffType());
                            sureRelationVo.setPaymentMeans(relationVo.getPaymentMeans());
                            sureRelationVo.setExchangeRate(relationCheckVo.getExchangeRate());
                            sureRelationVo.setDocManageVo(docManageVoById);
                            sureRelationVo.setCorpId(docManageVoById.getCorpId());
//                            inject(sureRelationVo);
                            relationDataList.add(sureRelationVo);
                        }
                    }
                    sureDataVo.setRelationDataList(relationDataList);
                }
            }
        }
        return sureDataVo;
    }

    /**
     * 根据规则 进行校验和赋值
     * @param sureDataVo
     * @param relationCheckVo
     */
    private void checkManualDocAndRelation(DocSureWriteOffVo sureDataVo, DocWriteOffRelationVo relationCheckVo){
        BigDecimal docMoney = relationCheckVo.getDocMoney();
        int docNum = relationCheckVo.getDocNum();
        BigDecimal relationConvertMoney = relationCheckVo.getRelationConvertMoney();
        int relationNum = relationCheckVo.getRelationNum();
        BigDecimal exchangeRate = relationCheckVo.getExchangeRate();

        BigDecimal afterDocWriteOffBal = BigDecimal.ZERO;
        String afterDocWriteOffStatus = WriteoffConstant.WRITE_OFF_STATUS_2;
        BigDecimal afterRelationWriteOffBal = BigDecimal.ZERO;
        String afterRelationWriteOffStatus = WriteoffConstant.WRITE_OFF_STATUS_2;
        if(docNum==relationNum){
            relationCheckVo.setWriteOffNum(WriteoffConstant.WRITE_OFF_NUM_11);
        }else if(docNum<relationNum){
            relationCheckVo.setWriteOffNum(WriteoffConstant.WRITE_OFF_NUM_12);
        }else if(docNum>relationNum){
            relationCheckVo.setWriteOffNum(WriteoffConstant.WRITE_OFF_NUM_21);
        }
        if(docMoney.compareTo(relationConvertMoney)!=0){
            if(WriteoffConstant.WRITE_OFF_NUM_11.equals(relationCheckVo.getWriteOffNum())){
                //一对一，根据金额大小提示，但不阻断交易
                if(docMoney.compareTo(relationConvertMoney)>0){
                    sureDataVo.setCheckMsg("金额不一致，是否部分核销！");
                    afterDocWriteOffStatus = WriteoffConstant.WRITE_OFF_STATUS_1;
                    afterDocWriteOffBal = docMoney.subtract(relationConvertMoney);
                    relationCheckVo.setDocMoney(relationConvertMoney);
                }else if(docMoney.compareTo(relationConvertMoney)<0){
                    sureDataVo.setCheckMsg("金额不一致，是否继续！");
                    afterRelationWriteOffStatus = WriteoffConstant.WRITE_OFF_STATUS_1;
                    afterRelationWriteOffBal = relationConvertMoney.subtract(docMoney).divide(exchangeRate,2, BigDecimal.ROUND_HALF_UP);
                    relationCheckVo.setDocMoney(docMoney);
                }
            }else if(WriteoffConstant.WRITE_OFF_NUM_12.equals(relationCheckVo.getWriteOffNum())){
                //一对多，多的一方金额小于一条的金额，需提示并阻断交易
                if(docMoney.compareTo(relationConvertMoney)>0){
                    sureDataVo.setCheckMsg("金额不一致，是否部分核销！");
                    afterDocWriteOffStatus = WriteoffConstant.WRITE_OFF_STATUS_1;
                    afterDocWriteOffBal = docMoney.subtract(relationConvertMoney);
                    relationCheckVo.setDocMoney(relationConvertMoney);
                }else if(docMoney.compareTo(relationConvertMoney)<0){
                    sureDataVo.setCheckFlag(false);
                    sureDataVo.setCheckMsg("系统无法判断与哪一笔进行优先核销或是否存在汇率尾差，请一对一勾选后手工核销！");
                }
            }else if(WriteoffConstant.WRITE_OFF_NUM_21.equals(relationCheckVo.getWriteOffNum())){
                //多对一
                if(docMoney.compareTo(relationConvertMoney)>0){
                    sureDataVo.setCheckFlag(false);
                    sureDataVo.setCheckMsg("系统无法判断与哪一笔进行优先核销或是否存在汇率尾差，请一对一勾选后手工核销！");
                }else if(docMoney.compareTo(relationConvertMoney)<0){
                    sureDataVo.setCheckMsg("金额不一致，是否继续！");
                    afterRelationWriteOffStatus = WriteoffConstant.WRITE_OFF_STATUS_1;
                    afterRelationWriteOffBal = relationConvertMoney.subtract(docMoney).divide(exchangeRate,2, BigDecimal.ROUND_HALF_UP);
                    relationCheckVo.setDocMoney(docMoney);
                }
            }
        }

        if(sureDataVo.isCheckFlag()){
            relationCheckVo.setAfterDocWriteOffStatus(afterDocWriteOffStatus);
            relationCheckVo.setAfterDocWriteOffBal(afterDocWriteOffBal);
            relationCheckVo.setAfterRelationWriteOffStatus(afterRelationWriteOffStatus);
            relationCheckVo.setAfterRelationWriteOffBal(afterRelationWriteOffBal);
        }
    }

    /**
     * 获取核销历史数据
     * @param dataTablesInput
     * @return
     */
    public DataTablesOutput getWriteOffHisDataTablesByDocId(DataTablesInput dataTablesInput) {
        DataTablesOutput out = new DataTablesOutput();
        String docId = (String) dataTablesInput.getFormdata().get("docId");
        String batchNo = (String)dataTablesInput.getFormdata().get("batchNo");
        if(StringUtils.isNotEmpty(docId) || StringUtils.isNotEmpty(batchNo)){
            List<DocWriteOffRelation> writeOffHisByDocIdList = docWriteOffRelationDao.getWriteOffHisByDocId(docId, batchNo);
            if(EmptyUtils.isNotEmpty(writeOffHisByDocIdList)){
                out.setDraw(writeOffHisByDocIdList.size());
                out.setRecordsTotal(writeOffHisByDocIdList.size());
                out.setData(writeOffHisByDocIdList);
            }
        }
        return out;
    }

    /**
     * 获取核销历史数据
     * @param docId
     * @return
     */
    public List<DocWriteOffRelation> getWriteOffHisByDocId(String docId, String batchNo){
        return docWriteOffRelationDao.getWriteOffHisByDocId(docId, batchNo);
    }

    /**
     * 获取核销历史数据
     * @param docId
     * @return
     */
    public List<DocWriteOffRelation> getWriteOffHisByDocId1(String docId, String batchNo){
        List<DocWriteOffRelation> writeOffHisByDocId1 = docWriteOffRelationDao.getWriteOffHisByDocId1(docId, batchNo);
        return writeOffHisByDocId1;
    }

    /**
     * 获取核销批次数据数据
     * @param input
     * @return
     */
    public DataTablesOutput getWriteOffBatchDataApprove(DataTablesInput input){
        WorkFlowBusiness workFlowBusiness = workFlowService.getWorkFlowBusiness(AccountReceivableConstant.DOC_WRITE_OFF_APPROVE, input);
        String businessSql = SqlUtil.getInSql(workFlowBusiness.getBusinessIdList(), "rel.batch_no", true);
        DataTablesOutput dp = this.docWriteOffRelationDao.getWriteOffBatchData(input, businessSql);
        return workFlowBusiness.merge(dp);
    }

    public DataTablesOutput getWriteOffBatchData(DataTablesInput input){
//        List<DocManageVo> docManageVoByIds = writeOffDao.getDocManageVoByIds(Arrays.asList(docId), writeOffStatus);
        return this.docWriteOffRelationDao.getWriteOffBatchData(input, null);
    }


    /**
     * 获取强制核销 页面数据
     * @param docId
     * @return
     */
    public DocManageVo getForceViewDataByDocId(String docId){
        if(StringUtils.isNotEmpty(docId)){
            List<String> writeOffStatus = Arrays.asList(new String[]{WriteoffConstant.WRITE_OFF_STATUS_0,WriteoffConstant.WRITE_OFF_STATUS_1});
            List<DocManageVo> docManageVoByIds = writeOffDao.getDocManageVoByIds(Arrays.asList(docId), writeOffStatus);
            if(EmptyUtils.isNotEmpty(docManageVoByIds)){
                return docManageVoByIds.get(0);
            }
        }
        return new DocManageVo();
    }

    /**
     * 强制核销
     * @param relationVo
     * @param user
     */
    public void forceWriteOff(DocWriteOffRelationVo relationVo, ICurrentUser user) {
        //获取应收单关联的数据
        String docId = relationVo.getDocId();
        if(EmptyUtils.isNotEmpty(docId)){
            List<String> writeOffStatusOfForce = Arrays.asList(new String[]{WriteoffConstant.WRITE_OFF_STATUS_0,WriteoffConstant.WRITE_OFF_STATUS_1});
            List<DocManageVo> docManageVoByIds = writeOffDao.getDocManageVoByIds(Arrays.asList(docId), writeOffStatusOfForce);
            if(EmptyUtils.isNotEmpty(docManageVoByIds)){
                //删除原有的关联数据
//                writeOffDao.delDocWriteOffRelationByDocIds(Arrays.asList(docId));

                DocManageVo docManageVo = docManageVoByIds.get(0);

                String batchNo = getWriteOffBatchNo();

                DocWriteOffRelation docWriteOffRelation = new DocWriteOffRelation();
                BeanUtils.copyNotNullProperties(relationVo, docWriteOffRelation);
                docWriteOffRelation.setDocId(docManageVo.getId());
                docWriteOffRelation.setRelationOriginMoney(relationVo.getRelationConvertMoney());//核销原币金额
                docWriteOffRelation.setWriteOffStatus(relationVo.getAfterDocWriteOffStatus());
                docWriteOffRelation.setCreateBy(user.getId());
                docWriteOffRelation.setCreateDate(DateUtils.getDate());
                docWriteOffRelation.setTenantId(user.getTenantId());
                docWriteOffRelation.setBatchNo(batchNo);
                docWriteOffRelation.setStatus(NetbankConstant.STATUE_WAIT_CHECK);
                docWriteOffRelation.setWriteOffNum(WriteoffConstant.WRITE_OFF_NUM_10);
                // 执行金额
                docWriteOffRelation.setExecuteMoney(relationVo.getRelationConvertMoney());
                //BEFORE_WRITE_OFF_BAL
//                docWriteOffRelation.setRelationConvertMoney(relationVo.getAfterDocWriteOffBal());// 可核销应收币种金额
                docWriteOffRelation.setBeforeWriteOffBal(relationVo.getBeforeWriteOffBal());// 核销前金额
                docWriteOffRelationDao.save(docWriteOffRelation);

                DocWriteOffRelationVo workFlowRelationVo = new DocWriteOffRelationVo();
                workFlowRelationVo.setBatchNo(batchNo);
                //需求未明确应收单多选时单位需要一致
                workFlowRelationVo.setCorpId(docManageVo.getCorpId());
                workFlowRelationVo.setStatus(NetbankConstant.STATUE_WAIT_CHECK);
                this.startWorkFlow(workFlowRelationVo, user);
            }else {
                throw new BusinessException(WriteOffEnums.WRITE_OFF_SURE_CHECK_MSG, "数据状态已发生变化，请刷新页面后重试！");
            }
        }
    }



    /**
     * 自动生成收款单 - 流水维度
     * @param dtlVO
     */
    public NisReceivableDTO createNisReceivableByDtl(BisAccDtlVO dtlVO){
        BtBankAccDTO oppAccDto = bankAccService.getBankAccInfo(dtlVO.getOppAccNo());

        NisReceivableDTO dto = new NisReceivableDTO();
        dto.setCorpId(dtlVO.getCorpId());
        dto.setBusType(dtlVO.getBusType());
        dto.setBankAcc(dtlVO.getBankAcc());
        dto.setAccName(dtlVO.getAccName());
        dto.setBankName(dtlVO.getBankName());
        dto.setAmt(dtlVO.getAmt());
        dto.setCurId(dtlVO.getCurId());
        dto.setOppAccNo(dtlVO.getOppAccNo());
        dto.setOppAccName(dtlVO.getOppAccName());
        if(oppAccDto != null){
            dto.setOppBankCode(oppAccDto.getBankCode());
        }
        dto.setOppAccBank(dtlVO.getOppAccBank());
        dto.setRmk(dtlVO.getRemark());
        dto.setBillFrom(NetbankConstant.RECEIVABLE_BILLFROM_4);
        dto.setStatus(NetbankConstant.STATUE_TALLY_CHECKPASS);
        dto.setBankSerialNo(dtlVO.getBankSerialNo());
        dto.setTransTime(dtlVO.getTransTime());
        dto.setBillId(dtlVO.getId());
        //dto.setBusBillNo(docManage.getCode());

        //nisReceivableService.saveNisReceivable(dto, user);
        return dto;
    }

    /**
     * 自动生成收款单 - 应收单维度
     * @param docManageVo
     * @param writeOffMoney
     */
    public NisReceivableDTO createNisReceivableByDoc(DocManageVo docManageVo, BigDecimal writeOffMoney){
        NisReceivableDTO dto = new NisReceivableDTO();
        dto.setCorpId(docManageVo.getCorpId());
        dto.setBusType("");
        dto.setBankAcc(docManageVo.getPayeeAcc());
        dto.setAccName(docManageVo.getPayeeAccName());
        dto.setBankName(docManageVo.getPayeeBankName());
        //因为一对一可能金额不一致，需取核销金额
        dto.setAmt(writeOffMoney);
        dto.setCurId(docManageVo.getCurId());
        dto.setOppAccNo(docManageVo.getPayerAccNo());
        dto.setOppAccName(docManageVo.getPayerAccName());
        dto.setOppBankCode(docManageVo.getPayeeBankCode());
        dto.setOppAccBank(docManageVo.getPayerBankName());
        dto.setRmk("");
        dto.setBillFrom(NetbankConstant.RECEIVABLE_BILLFROM_4);
        dto.setStatus(NetbankConstant.STATUE_TALLY_CHECKPASS);
        dto.setBankSerialNo("");
        dto.setTransTime(docManageVo.getReceiptDate());
        dto.setBillId(docManageVo.getId());
        return dto;
    }

    /**
     * 作废收款单
     * @param billId
     * @param user
     */
    public String abolishNisReceivable(String billId, ICurrentUser user){
        writeOffDao.abolishNisReceivable(billId, user);
        return CommonConstant.NO;
    }

    /**
     * 根据应收单id获取核销历史记录
     * @param docId
     * @return
     */
    public List<DocWriteOffRelationVo> getWriteOffHisDetail(String docId){
        return writeOffDao.getWriteOffHisDetail3(docId);
    }

    /**
     * 根据billid获取收款单id
     * @param billId
     * @return
     */
    public NisReceivableDTO getNisReceivableByBillId(String billId){
        return writeOffDao.getNisReceivableByBillId(billId);
    }

    public NisReceivableDTO getNisReceivableByBillIdAndBatchNo(String billId, String batchNo){
        return writeOffDao.getNisReceivableByBillIdAndBatchNo(billId, batchNo);
    }

    /**
     * 提交审批流
     * @param relationVo
     * @param user
     */
    public void startWorkFlow(DocWriteOffRelationVo relationVo, ICurrentUser user){
        Integer messageType = NisMessageConstant.MESSAGE_TYPE_TEMP;
        String message = NisMessageConstant.NIS_LOG_ADD_TEMPSAVE_SUCC;
        //启动审批
        if (NetbankConstant.STATUE_WAIT_CHECK.equals(relationVo.getStatus())) {
            messageType = NisMessageConstant.MESSAGE_TYPE_SUBMIT;
            message = NisMessageConstant.NIS_LOG_ADD_SUBMIT_SUCC;

            //调用审批后方法
            getStartHandler().handler(AccountReceivableConstant.DOC_WRITE_OFF_APPROVE, relationVo.getBatchNo(), relationVo, relationVo.getCorpId(), user.getId());
        }
        if (user != null) {
            //生成日志
            nisMessageService.saveMessage(relationVo.getBatchNo(), messageType, message, "docWriteOff", user.getLoginName(), user.getUsername());
        }
    }

    @Override
    public void flowCompleted(IWorkFlowResult workFlowResult) {
        String bussinessKey = workFlowResult.getBusinessKey();
        ICurrentUser user = workFlowResult.getUser();
        boolean result = workFlowResult.isPass();// 是否通过
        approvePassEnd(bussinessKey, result, user);
    }

    @Override
    public void flowStarted(IWorkFlowResult workFlowResult) {

    }


    public ForceWriteOffVo queryForceWriteOff(String docId,String batchNo){
        DataTablesOutput output = this.writeOffDao.queryForceWriteOff(docId,batchNo);
        ForceWriteOffVo vo = (ForceWriteOffVo) output.getData().get(0);
        CorpDTO corpDTO = corpService.get(vo.getCorpId());
        //ExternalCorp externalCorp = externalCorpService.getByCorpIdAndCode(vo.getExternalCode());
        vo.setCorpName(corpDTO.getName());
        //vo.setExternalName(externalCorp.getName());
        return vo;
    }

    @Override
    public Map<String, Map<String, DictInDTO>> listDictMap(List<String> dictGroups) {
        return dictService.listDictMap(dictGroups);
    }
}
