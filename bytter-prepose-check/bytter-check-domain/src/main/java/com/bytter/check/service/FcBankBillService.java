package com.bytter.check.service;

import com.bytter.biz.bdlink.api.dto.BisAccDtlDTO;
import com.bytter.biz.bdlink.api.service.IBisAccDtlService;
import com.bytter.biz.finacial.api.constant.FinacialConstant;
import com.bytter.biz.finacial.api.constant.FinacialResourceConstant;
import com.bytter.biz.finacial.api.enums.bankCollect.CollectMessageEnum;
import com.bytter.check.dao.FcBankBillDao;
import com.bytter.check.dao.FcCheckInitDao;
import com.bytter.check.entity.FcBankBill;
import com.bytter.check.entity.FcBankCheckDtl;
import com.bytter.check.entity.FcCheckInit;
import com.bytter.check.enums.CheckMessageEnum;
import com.bytter.check.vo.FcBankBillSearchVo;
import com.bytter.check.vo.FcBankBillVo;
import com.bytter.check.vo.FcCompareErpSearchVO;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.exception.ParameterException;
import com.bytter.core.constant.CommonConstant;
import com.bytter.core.sequence.SequenceSingleton;
import com.bytter.core.sequence.enums.SequenceEnum;
import com.bytter.core.utils.BeanUtils;
import com.bytter.core.utils.DateUtils;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.core.utils.StringUtils;
import com.bytter.system.api.*;
import com.bytter.system.api.dto.BtBankAccCurDTO;
import com.bytter.system.api.dto.BtBankAccDTO;
import com.bytter.system.api.dto.CorpDTO;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @version 1.0
 * @Title: Service
 * @Description: FC_BANK_BILL
 * @date 2017-06-02 10:04:16
 */
@Service("fcBankBillService")
@Transactional
public class FcBankBillService extends BaseService<FcBankBill, String> {
    @Autowired
    private FcBankBillDao fcBankBillDao;
    @Autowired
    private FcCheckInitDao fcCheckInitDao;
    @DubboReference(check = false)
	private ICorpService corpService;
    @DubboReference(check = false)
    private IBtCurrencyService iBtCurrencyService;
    @Resource
	private IMultiLangService multiLangServiceI;
    @Autowired
    private FcBankCheckDtlService fcBankCheckDtlService;
    @DubboReference(check = false)
    private IBtBankAccService btBankAccService;
    @DubboReference(check = false)
    private IBisAccDtlService iBisAccDtlService;
    @Value("${jdbc.url:}")
    private String dbUrl;

    @DubboReference(check = false)
    private IBtBankAccService iBtBankAccService;

    @DubboReference(check = false)
    private IBtBankAccCurService iBtBankAccCurService;

    /**
     * 删除对象
     */
    public void deleteEntity(FcBankBill entity) {
        super.deleteEntity(entity);
    }
    /**
     * 根据id删除
     */
    public void delete(String id) {
        super.delete(id);
    }
    /**
     * 保存
     */
    public void save(FcBankBillVo fb, String type, String userId) {
        List<FcBankBill> fcBankBills = fb.getAccList();
        List<FcBankBill> tempList = new ArrayList<FcBankBill>();
        for (int i = 0; i < fcBankBills.size(); i++) {
            int index = i + 1;
            FcBankBill fbb = new FcBankBill();
            fbb = fcBankBills.get(i);
            String the = multiLangServiceI.getLang("common.table.the");
            String no = multiLangServiceI.getLang("table.fcReportFormat.orderNo");//行
            String lineMsg = the + index + no;//第几行
            if ("update".equals(type)) {
                lineMsg = "";
            }
            checkDate(fbb, lineMsg);
            BtBankAccDTO dto = iBtBankAccService.getByBankAcc(fbb.getBankAcc());
            BtBankAccCurDTO bankAccCurDTO = iBtBankAccCurService.getBankAccCurByBankAccId(dto.getId(),fbb.getCurId());
            fbb.setBankTypeId(dto.getBankTypeId());
            fbb.setAccount(bankAccCurDTO.getCounterAcc());
            //check(fbb , i+1 ,msg);
            fbb.setImportType(FinacialConstant.IMPORT_TYPE_HAND);
            FcCheckInit fcCheckInit = fcCheckInitDao.getListByBankAcc(fbb.getBankAcc(), null,null);
            if (fcCheckInit == null) {
                throw new ParameterException(CheckMessageEnum.FIN_PARAMCONFIG_FCBANKINIT_IS_NULL);
            }
            if (fbb.getTallyDate().compareTo(fcCheckInit.getCheckDate()) <= 0) {
                String initTime = multiLangServiceI.getLang("table.fcInitAcc.initTime");
                throw new ParameterException(CheckMessageEnum.FIN_PARAMCONFIG_TALLYDATE_PASS_CHECKDATE, lineMsg +
                        DateUtils.formatDate(fbb.getTallyDate()), DateUtils.formatDate(fcCheckInit.getCheckDate()));//",初始化日期为
            }
            if ("update".equals(type)){
                fbb.setUpdateTime(new Date());
                fbb.setUpdateBy(StringUtils.isNotBlank(userId) ? userId : "");
            }else {
                //这个字段 流水号 有长度设置，使用"11111"进行补齐
                fbb.setBankId(this.getBankId("11111"));
                fbb.setCreateTime(new Date());
                fbb.setUpdateTime(new Date());
                fbb.setCreateBy(StringUtils.isNotBlank(userId) ? userId : "");
                fbb.setUpdateBy(StringUtils.isNotBlank(userId) ? userId : "");
            }
            tempList.add(fbb);
        }
        batchSave(tempList);
    }
    private void checkDate(FcBankBill fbb, String lineMsg) {
        if (EmptyUtils.isEmpty(fbb.getBankAcc())) {
            //银行账号不能为空
            throw new ParameterException(CheckMessageEnum.FIN_CHECK_FCBANKBILLTEMP_NETBANK_NULL, lineMsg);
        }
		/*if(EmptyUtils.isEmpty(fbb.getAccount())){
			//内部账号/ERP企业科目不能为空
			throw new ParameterException(CheckMessageEnum.FIN_CHECK_CENTRALIYDATAMANAGER_ACCOUNT_EMPTY ,lineMsg);
		}*/
        if (EmptyUtils.isEmpty(fbb.getTallyDate())) {
            //记账日期不能为空
            throw new ParameterException(CheckMessageEnum.FIN_CHECK_FCBANKBILL_TALLYDATE_NULL, lineMsg);
        }
        if (EmptyUtils.isEmpty(fbb.getCurId())) {
            //币别不能为空
            throw new ParameterException(CheckMessageEnum.FIN_CHECK_FCBANKBILL_CURID_NULL, lineMsg);
        }
        if (EmptyUtils.isEmpty(fbb.getDebitMoney())) {
            //借方发生额不能为空
            //throw new ParameterException(CheckMessageEnum.FIN_CHECK_FCBANKBILLTEMP_DEBITMONEY_NULL  ,lineMsg);
            fbb.setDebitMoney(BigDecimal.ZERO);
        }
        if (EmptyUtils.isEmpty(fbb.getCreditMoney())) {
            //贷方发生额不能为空
            //throw new ParameterException(CheckMessageEnum.FIN_CHECK_FCBANKBILLTEMP_CREDITMONEY_NULL , lineMsg);
            fbb.setCreditMoney(BigDecimal.ZERO);
        }
        if (fbb.getDebitMoney().compareTo(BigDecimal.ZERO) == 0 && fbb.getCreditMoney().compareTo(BigDecimal.ZERO) == 0) {
            //借贷方不能都为0
            throw new ParameterException(CheckMessageEnum.FIN_CHECK_FCBANKBILL_DE_CR_ZERO, lineMsg);
        }
        if (fbb.getDebitMoney().compareTo(BigDecimal.ZERO) > 0 && fbb.getCreditMoney().compareTo(BigDecimal.ZERO) > 0) {
            //借贷方不能都不为0
            throw new ParameterException(CheckMessageEnum.FIN_CHECK_FCBANKBILL_DE_CR_NOT_ZERO, lineMsg);
        }
        if (EmptyUtils.isEmpty(fbb.getRedblueSign())) {
            throw new ParameterException(CheckMessageEnum.FIN_CHECK_FCBANKBILL_REDBULESIGN_NULL, lineMsg);
            //红蓝标志不能为空
        }
    }
    /**
     * 保存对账单
     */
    public void doBillSave(List<FcBankBill> fcBankBills) {
        for (FcBankBill fcBankBill : fcBankBills) {
            String oriBankId = fcBankBill.getBankId();
            String importType = fcBankBill.getImportType();
            if (EmptyUtils.isBlank(oriBankId) || EmptyUtils.isBlank(importType)) {
//                fcBankBill.setLineNo(1111111);//行号暂时为11111
//                fcBankBill.setBankId(this.getBankId("11111"));
            }
            fcBankBill.setImportDate(DateUtils.getTimestamp());
            fcBankBill.setVoucherNo(fcBankBill.getBillNo());
            super.saveEntity(fcBankBill);
        }
    }
    /**
     * 更新
     */
    public void update(FcBankBill entity) {
        super.updateEntity(entity);
    }
    /**
     * 保存or更新
     */
    public void saveOrUpdate(FcBankBill entity) {
        super.saveOrUpdate(entity);
    }
    @Transactional(readOnly = true)
    public DataTablesOutput getBankBill(DataTablesInput dataTablesInput, String userId) {
        List<String> corpAuth = corpService.getUserCorpIds(FinacialResourceConstant.FINACIAL_CHECK_BANK_QUERY, userId);
        List<BtBankAccDTO> btBankAccDTOList = btBankAccService.getBankAccByCorpIdList(corpAuth,"", new HashMap<>());
        // 银行类型
        Map<String, String> bankMap = btBankAccDTOList.stream().collect(Collectors.toMap(BtBankAccDTO::getBankAcc, BtBankAccDTO::getBankTypeName,(a,b)->a));
        // 银行账号
        Map<String, BtBankAccDTO> bankAccDTOMap = btBankAccDTOList.stream().collect(Collectors.toMap(BtBankAccDTO::getBankAcc, Function.identity(),(a,b)->a));
        // 币别
        Map<String, String> curMap = btBankAccDTOList.stream().filter(a -> a.getCurName() != null).collect(Collectors.toMap(BtBankAccDTO::getBankAcc, BtBankAccDTO::getCurName,(a,b)->a));
        // 单位
        Map<String, CorpDTO> corpMap = corpService.getCorpMap();

        List<String> bankAccList = new ArrayList<>();
        if(EmptyUtils.isNotEmpty(btBankAccDTOList)){
            for(BtBankAccDTO bankAccDTO : btBankAccDTOList){
                bankAccList.add(bankAccDTO.getBankAcc());
            }
        }
        DataTablesOutput output = fcBankBillDao.getFcBankBill(dataTablesInput, bankAccList);
        //币别、公司名称、银行类型
        List<FcBankBillVo> lists = output.getData();
        lists.forEach(a -> {
            a.setCurName(curMap.get(a.getBankAcc()));
            a.setBankTypeName(bankMap.get(a.getBankAcc()));
            if(null != bankAccDTOMap){
                BtBankAccDTO btBankAccDTO = bankAccDTOMap.get(a.getBankAcc());
                if(null != btBankAccDTO && null != corpMap){
                    CorpDTO corpDTO = corpMap.get(btBankAccDTO.getCorpId());
                    if(null != corpDTO){
                        a.setCorpId(corpDTO.getId());
                        a.setCodeName(corpDTO.getName());
                    }
                }
            }
        });
        if (null != dataTablesInput.getFormdata().get("eqs_corpId") && StringUtils.isNotEmpty(dataTablesInput.getFormdata().get("eqs_corpId").toString())) {
            corpAuth = new ArrayList<>();
            corpAuth.add(dataTablesInput.getFormdata().get("eqs_corpId").toString());
            for(String corpId:corpAuth){
                List<FcBankBillVo> list = lists.stream().filter(a->a.getCorpId().equals(corpId)).collect(Collectors.toList());
                output.setData(list);
            }
        }
        return output;
    }
    @Transactional(readOnly = true)
    public DataTablesOutput getInterfaceBankBill(DataTablesInput dataTablesInput, String userId) {
        //mysql查询优化
//		if(dbUrl.toLowerCase().contains("mysql")){
//            DataTablesOutput d = fcBankBillDao.getInterfaceFcBankBill(dataTablesInput, userId);
//            return d;
//        }
        List<String> corpAuth = corpService.getUserCorpIds(FinacialResourceConstant.FINACIAL_CHECK_BANK_INTERFACE_QUERY, userId);
        //查询账号列表
        String accSql=fcBankBillDao.findBankAccSql(  corpAuth,  userId);
        Map<String, Object> params=new HashMap<>();
        List<BtBankAccDTO> accList= btBankAccService.listBankAccBySql(accSql,params);
        List<String> bankAccList=new ArrayList<>();
        for(BtBankAccDTO acc:accList){
            bankAccList.add(acc.getBankAcc());
        }
        //过滤账号
        List<FcBankBillVo> billAccList= fcBankBillDao.getIsCheckBankAcc(bankAccList);
        List<String> accS=new ArrayList<>();
        for(FcBankBillVo billVo:billAccList){
            accS.add(billVo.getBankAcc());
        }
        String sql=fcBankBillDao.findBisAccDtlSql(corpAuth, bankAccList) ;
        return iBisAccDtlService.getInterfaceFcBankBill(dataTablesInput,sql);
    }
    public BaseDao<FcBankBill, String> getEntityDao() {
        return fcBankBillDao;
    }
    /**
     * 功能介绍:查询银行对账数据(未达、已对账、已勾兑、已对账等)<br>
     * <b>日期：</b>2017年6月8日<br>
     *
     * @version 1.0
     * @author caozx
     */
    @Transactional(readOnly = true)
    public List<Map<String, Object>> queryBankCheckDatas(String bankAcc, String account, Date transDate, String[] compareSigns,BigDecimal beginMoney,BigDecimal endMoney) {
        if (0 == compareSigns.length) {
            return new ArrayList<>();
        }
        return fcBankBillDao.queryBankCheckDatas(bankAcc, account, transDate, compareSigns,beginMoney,endMoney);
    }
    /**
     * 功能介绍:查询银行对帐数据<br>
     * <b>日期：</b>2017年6月8日<br>
     *
     * @version 1.0
     * @author caozx
     */
    @Transactional(readOnly = true)
    public List<FcBankBillVo> queryCheckFcBankBills(FcBankBillSearchVo bankSearchBean,
                                                    String orderBy, String userCode) {
        return fcBankBillDao.queryCheckFcBankBills(bankSearchBean, orderBy, userCode);
    }
    /**
     * <b>功能介绍:更新状态为已对账</b><br>
     * <b>日期：</b>2017年6月13日<br>
     * <b>作者：</b>caozx<br>
     *
     * @version 1.0
     */
    public void updateComparesignOverCheck(List<String> bankBillPKList, String compareSign) {
        fcBankBillDao.updateComparesignOverCheck(bankBillPKList, compareSign);
    }
    /**
     * 功能介绍:获取银行未达账金额合计<br>
     * <b>日期：</b>2017年6月8日<br>
     *
     * @version 1.0
     * @author caozx
     */
    @Transactional(readOnly = true)
    public Map<String, Object> queryStayOverBankSum(Date transDate, String bankAcc, String overType, String curId) {
        return fcBankBillDao.queryStayOverBankSum(transDate, bankAcc, overType, curId);
    }
    /**
     * 功能介绍:统计未对账总数<br>
     * <b>日期：</b>2017年6月8日<br>
     *
     * @version 1.0
     * @author caozx
     */
    @Transactional(readOnly = true)
    public Map<String, Object> queryUnOverCtrSum(Date transDate, String bankAcc, String overType) {
        return fcBankBillDao.queryUnOverCtrSum(transDate, bankAcc, overType);
    }
    /**
     * <b>功能介绍:查询上期留存的银行数据</b><br>
     * <b>日期：</b>2017年6月16日<br>
     * <b>作者：</b>caozx<br>
     *
     * @version 1.0
     */
    @Transactional(readOnly = true)
    public List<FcBankBillVo> queryUnOverCtrStayDatas(String bankAcc, String overType, Date transDate, String curId) {
        return fcBankBillDao.queryUnOverCtrStayDatas(bankAcc, overType, transDate, curId);
    }
    /**
     * 判断是否初始化余额
     */
    public void validateAccInit(FcBankBillVo fc) {
        String bankAcc = fc.getBankAcc();
        String curId = fc.getCurId();
        //Date tallyDate = fc.getTallyDate();
        FcCheckInit fcCheckInit = fcCheckInitDao.getListByBankAcc(bankAcc, curId,null);
        if (fcCheckInit == null) {
            throw new ParameterException(CheckMessageEnum.FIN_PARAMCONFIG_FCBANKINIT_IS_NULL);
        }
    }
    /**
     * 提取银行对账数据
     */
    public void batchSaveFcBankBill(FcBankBillVo fcBankBill, ICurrentUser user) {
        if(EmptyUtils.isNotEmpty(fcBankBill.getId())) {
            String[] ids = fcBankBill.getId().split(",");
            List<String> idList = new ArrayList<>();
            for(int i=0;i<ids.length;i++){
                idList.add(ids[i]);
            }
            List<BisAccDtlDTO> bisAccDtlDTOList =  iBisAccDtlService.getByIds(idList);
            List<FcBankBill> insertList = new ArrayList<>();
            for(BisAccDtlDTO dto : bisAccDtlDTOList){
                FcBankBill entity = new FcBankBill();
                entity.setBankAcc(dto.getBankAcc());
                entity.setTallyDate(dto.getTransTime());
                if(dto.getCdSign().equals("1")){
                    entity.setDebitMoney(dto.getAmt());
                    entity.setCreditMoney(BigDecimal.ZERO);
                }else{
                    entity.setDebitMoney(BigDecimal.ZERO);
                    entity.setCreditMoney(dto.getAmt());
                }
                entity.setOppAccNo(dto.getOppAccNo());
                entity.setOppAccName(dto.getOppAccName());
                entity.setVoucherNo(dto.getVoucherNo());
                entity.setSummaryContent(dto.getRemark());
                entity.setCurId(dto.getCurId());
                entity.setBankId(dto.getId());
                insertList.add(entity);
            }
//        List<FcBankBill> insertList = fcBankBillDao.insertUnCheckBankBills(fcBankBill);//查询未对账银行对账单
            List<FcBankBill> tempList = new ArrayList<>();
            String bankIds = "";
            for (FcBankBill temp : insertList) {
                temp.setCompareSign(FinacialConstant.COMPARE_SIGN_NO);
                temp.setImportType(FinacialConstant.IMPORT_TYPE_INTERFACE);
                temp.setRedblueSign(FinacialConstant.RB_SIGN_B);
                tempList.add(temp);
                if (EmptyUtils.isEmpty(bankIds)) {
                    bankIds = temp.getBankId();
                } else {
                    bankIds = "," + temp.getBankId();
                }
            }
            Boolean isHave = fcBankBillDao.isHaveOverBankBill1(bankIds);
            if (!isHave) {
                batchSave(tempList);
            } else {
                throw new ParameterException(CheckMessageEnum.FIN_CHECK_BANKACC_DATA_EXIST);//提取的数据已存在
            }
            //更新已导入标识
            if (tempList.size() > 0) {
                int count = iBisAccDtlService.updateBankBillImportSign(bankIds);
                if (count != tempList.size()) {
                    throw new ParameterException(CollectMessageEnum.FIN_COLLECT_MANUAL_STATUSCHANGE);//状态已改变，刷新页面重试
                }
            }
        }
    }
    /**
     * 保存银行对账数据
     */
    public void saveFcBankBills(FcBankBillVo fcBankBill) {
        List<FcBankBill> list = fcBankBill.getAccList();
        String bankIds = getBankIds(fcBankBill);
        boolean flag = fcBankBillDao.isHaveOverBankBill(bankIds);
        if (flag) {
            throw new ParameterException(CheckMessageEnum.FIN_CHECK_FCBANKBILL_CONTAIN_COMPARE_SIGN_OVER);//存在已结转的数据不能添加
        }
        for (FcBankBill temp : list) {
            fcBankBillDao.deleteFcBankBills(temp);//删除原有对账单记录
            Integer lineNo = fcBankBillDao.getMaxLineNo(temp.getBankAcc(), temp.getTallyDate());
            //设置行号
            temp.setLineNo(lineNo);
            temp.setCompareSign(FinacialConstant.COMPARE_SIGN_NO);
            temp.setImportType(FinacialConstant.IMPORT_TYPE_INTERFACE);
            temp.setRedblueSign(FinacialConstant.RB_SIGN_B);
            this.saveEntity(temp);
            fcBankBillDao.flush();
        }
        //batchSave(tempList);
        //String[] split = bankIds.split(",");
		/*if(!EmptyUtils.isEmpty(split)){
			for (String bankId : split) {
				//更新导出标志
				fcBankBillDao.updateBankBillImportSign(bankId);
			}
		}*/
        iBisAccDtlService.updateBankBillImportSign(bankIds);
    }
    /**
     * 功能介绍 ： 获取凭证号。<br>
     */
    protected String getBankId(String prefix) {
        return SequenceSingleton.getSequence(SequenceEnum.FC_BANK_BILL_BANK_ID, prefix);
    }
    /**
     * 获取bankids
     */
    public String getBankIds(FcBankBillVo fcBankBill) {
        List<FcBankBill> list = fcBankBill.getAccList();
        String bankIds = "";
        for (FcBankBill lis : list) {
            String temp = lis.getBankId();
            if (bankIds == "") {
                bankIds += temp;
            } else {
                bankIds += "," + temp;
            }
        }
        return bankIds;
    }
    /**
     * 查询维护了余额初始化过的账号
     */
    @Transactional(readOnly = true)
    public List<FcBankBillVo> getIsCheckBankAcc(String userId) {
        List<BtBankAccDTO> bankaccList = btBankAccService.getBankAccByUserId(userId,"1");
        List<String> accList = new ArrayList<>();
        Map<String,BtBankAccDTO> bankMap = new HashMap<>();
        if(EmptyUtils.isNotEmpty(bankaccList)){
            for(BtBankAccDTO dto : bankaccList){
                accList.add(dto.getBankAcc());
                bankMap.put(dto.getBankAcc(),dto);
            }
        }
        List<FcBankBillVo> list = fcBankBillDao.getIsCheckBankAcc(accList);
        if(EmptyUtils.isNotEmpty(list)){
            for(FcBankBillVo fcBankBillVo : list){
                String bankAcc = fcBankBillVo.getBankAcc();
                BtBankAccDTO dto = bankMap.get(bankAcc);
                if(!EmptyUtils.isEmpty(dto)) {
                    fcBankBillVo.setBankName(dto.getBankName());
                    fcBankBillVo.setCurName(dto.getCurName());
                    fcBankBillVo.setCurId(dto.getCurId());
                }
            }
        }
        return list;
    }
    //修改提交数据
    public void updateFcbankBill(FcBankBill entity, String userId) {
        FcBankBill temp = this.get(entity.getId());
        BeanUtils.copyNotNullProperties(entity, temp);
        FcBankBillVo vo = new FcBankBillVo();
        List<FcBankBill> accList = Arrays.asList(temp);
        vo.setAccList(accList);
        this.save(vo, "update", userId);
    }
    /**
     * <b>功能介绍：</b>结转银行对账数据<br>
     * <b>日期：</b>2017年10月17日<br>
     * <b>作者：</b>caozx<br>
     *
     * @param bankAcc
     * @param transDate
     * @version 1.0
     */
    public void updateOverBankInfos(String bankAcc, Date transDate, String curId) {
        fcBankBillDao.updateOverBankInfos(bankAcc, transDate, curId);
    }
    /**
     * <b>功能介绍：</b>结转银行未达数据<br>
     * <b>日期：</b>2017年10月17日<br>
     * <b>作者：</b>caozx<br>
     *
     * @param bankAcc
     * @param transDate
     * @version 1.0
     */
    public void updateOverStayBankInfos(String bankAcc, Date transDate, String curId) {
        fcBankBillDao.updateOverStayBankInfos(bankAcc, transDate, curId);
    }
    /**
     * <br>
     * <b>功能：</b> 根据结转ID获取银行对账单数据 <br>
     * <b>作者：</b>liyh<br>
     * <b>日期：</b> 2022/1/6 0006 <br>
     */
    public List<Map<String, String>> getOverBankBillId(String[] adId){
        return fcBankBillDao.getOverBankBillId(adId);
    }
    /**
     * <b>功能介绍：</b>反结转更新<br>
     * <b>日期：</b>2017年10月17日<br>
     * <b>作者：</b>caozx<br>
     *
     * @param bankIds
     * @version 1.0
     */
    public int updateRedoAdjustOver(List<String> bankIds) {
        return fcBankBillDao.updateRedoAdjustOver(bankIds);
    }
    /**
     * <b>功能介绍：</b>获取银行可结转对账单ID<br>
     * <b>日期：</b>2017年10月17日<br>
     * <b>作者：</b>caozx<br>
     *
     * @param transDate
     * @param bankacc
     * @return
     * @version 1.0
     */
    @Transactional(readOnly = true)
    public List<FcBankBillVo> getBankDataOverData(Date transDate, String bankacc, String curId) {
        return fcBankBillDao.getBankDataOverData(transDate, bankacc, curId);
    }
    /**
     * <b>功能介绍：</b>查询银行结转明细清单<br>
     * <b>日期：</b>2017年10月20日<br>
     * <b>作者：</b>caozx<br>
     *
     * @param
     * @param ajustId
     * @param stayRadios
     * @param handRadios
     * @param checkRadios
     * @return
     * @version 1.0
     */
    @Transactional(readOnly = true)
    public List<FcBankBillVo> queryBankOverDatas(String ajustId, String stayRadios, String handRadios, String checkRadios) {
        return fcBankBillDao.queryBankOverDatas(ajustId, stayRadios, handRadios, checkRadios);
    }
    /**
     * 删除银行对账单
     */
    public void deleteFcbankBill(String id) {
		/*boolean flag = fcBankBalanceAdjustService.checkIsAdjust(fcBankBill.getAccount(),fcBankBill.getBankAcc());
		if(flag){
			throw new ParameterException(CheckMessageEnum.FIN_CHECK_FCBANKBILL_IS_ALREADY_ADJUST);
		}
		this.deleteEntity(fcBankBill);*/
        FcBankBill entity = get(id);
        String compareSign = entity.getCompareSign();
        if(!FinacialConstant.COMPARE_SIGN_NO.equals(compareSign)){
            throw new ParameterException(CheckMessageEnum.FIN_CHECK_CENTRALIYDATAMANAGER_COMPARE_YES_DEL);
        }
        fcBankBillDao.deleteBankBill(entity);
        //还原明细导入标识
        iBisAccDtlService.recoverBankBillImportSign(entity.getBankId());
        //已对账或未对账的数据才能删除
        FcBankCheckDtl bankCheckDtl = new FcBankCheckDtl();
        bankCheckDtl.setBankId(id);
        bankCheckDtl.setBankAcc(entity.getBankAcc());
        bankCheckDtl.setCurCode(entity.getCurId());
        bankCheckDtl.setStatus(CommonConstant.STATUE_DELETE);
        fcBankCheckDtlService.save(bankCheckDtl);
    }
    /**
     * <br>
     * <b>功能：</b>更新上期未达的数据为未对账<br>
     */
    public void updateErpBillInitStatus(Date transDate, Date checkDate, String bankAcc, String curId) {
        fcBankBillDao.updateBankBillInitStatus(transDate, checkDate, bankAcc, curId);
    }
    /**
     * <br>
     * <b>功能：</b>查询银行对帐数据ERP<br>
     */
    public List<FcBankBillVo> queryFcBankBillsErp(FcBankBillVo searchBean, String userCode) {
        return fcBankBillDao.queryFcBankBillsErp(searchBean, userCode);
    }
    @Transactional(readOnly = true)
    public List<Map<String, Object>> queryErpBankCheckDatas(FcCompareErpSearchVO fcCompareErpSearchVO, Date transDate, String[] compareSigns) {
        return fcBankBillDao.queryErpBankCheckDatas(fcCompareErpSearchVO, transDate, compareSigns);
    }
}
