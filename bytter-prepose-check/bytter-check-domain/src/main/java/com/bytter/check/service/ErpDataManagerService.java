package com.bytter.check.service;

import com.bytter.biz.dataexchange.basic.entity.ErpBasicData;
import com.bytter.biz.finacial.api.constant.FinacialConstant;
import com.bytter.biz.finacial.api.constant.FinacialResourceConstant;
import com.bytter.check.dao.ErpDataManagerDao;
import com.bytter.check.entity.FcBankCheckDtl;
import com.bytter.check.entity.FcCompareAcc;
import com.bytter.check.enums.CheckMessageEnum;
import com.bytter.check.vo.ErpDataVO;
import com.bytter.check.vo.FcCompareAccVO;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.exception.ParameterException;
import com.bytter.core.constant.CommonConstant;
import com.bytter.core.utils.BeanUtils;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.core.utils.StringUtils;
import com.bytter.poi.excel.entity.result.ExcelVerifyHanlderResult;
import com.bytter.poi.handler.inter.IExcelVerifyHandler;
import com.bytter.system.api.IBtBankAccService;
import com.bytter.system.api.IBtCurrencyService;
import com.bytter.system.api.ICorpService;
import com.bytter.system.api.dto.BtBankAccDTO;
import com.bytter.system.api.dto.BtCurrencyDTO;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;

import java.math.BigDecimal;
import java.util.*;
/**
 * @author onlineGenerator
 * @version 1.0
 * @Title: Service
 * @Description: FC_COMPARE_ACC
 * @date 2017-05-31 15:28:08
 */
@Service("erpDataManagerService")
@Transactional
public class ErpDataManagerService extends BaseService<FcCompareAcc, String> implements IExcelVerifyHandler<FcCompareAccVO> {
    @Autowired
    private ErpDataManagerDao erpDataManageDao;
    @DubboReference(check = false)
	private IBtBankAccService btBankAccService;
    @DubboReference(check = false)
	private ICorpService corpService;
    @Autowired
    private FcBankCheckDtlService fcBankCheckDtlService;
    @DubboReference(check = false)
    private IBtCurrencyService btCurrencyService;
    public void saveList(ErpDataVO vo, String importType) {
        for (int i = 0; i < vo.getAccList().size(); i++) {
            FcCompareAcc acc = vo.getAccList().get(i);
            //验证
            saveCheck(acc, i);
            if (FinacialConstant.IMPORT_TYPE_INTERFACE.equals(importType)) {
                acc.setImportType(FinacialConstant.IMPORT_TYPE_INTERFACE);//接口导入
                acc.setCompareSign(FinacialConstant.COMPARE_SIGN_NO);//未对账
            } else {
                acc.setImportType(FinacialConstant.IMPORT_TYPE_HAND);//手工导入
            }
            acc.setCheckType(FinacialConstant.CHECK_TYPE_ERP);
            //TODO 起息日期
            acc.setInterestStartDate(new Date());
            acc.setLineNo(new BigDecimal(i + 1));
            if (acc.getCreditMoney() == null) {
                acc.setCreditMoney(BigDecimal.ZERO);
            }
            if (acc.getDebitMoney() == null) {
                acc.setDebitMoney(BigDecimal.ZERO);
            }
			/*//流水号
			acc.setOrderNo(" ");
			//序号
			acc.setBankId(" ");
			//币别
			acc.setCurCode(" ");*/
            acc.setBankId(acc.getId());
            acc.setId("");
            save(acc);
        }
    }
    /**
     * erp维护列表数据
     */
    public DataTablesOutput getErpDataTables(DataTablesInput dataTablesInput, String userId) {
        dataTablesInput.getFormdata().remove("eqs_curId");
        List<String> corpAuth = null;
        String eqsCorpId = (String)dataTablesInput.getFormdata().get("eqs_corpId");
        if (null != eqsCorpId && StringUtils.isNotEmpty(eqsCorpId)) {
            corpAuth = new ArrayList<>();
            corpAuth.add(eqsCorpId);
            dataTablesInput.getFormdata().remove("eqs_corpId");
        }else{
            corpAuth = corpService.getUserCorpIds(FinacialResourceConstant.FINACIAL_CHECK_ERP_DATA_INTERFACE_QUERY, userId);
        }
        //根据单位获取账号
        List<BtBankAccDTO> bankaccList = btBankAccService.getBankAccByCorpIdList(corpAuth,"" ,new HashMap<>());
        List<String> accList = new ArrayList<>();
        Map<String,BtBankAccDTO> bankMap = new HashMap<>();
        if(EmptyUtils.isNotEmpty(bankaccList)){
            for(BtBankAccDTO dto : bankaccList){
                accList.add(dto.getBankAcc());
                bankMap.put(dto.getBankAcc(),dto);
            }
        }

        DataTablesOutput output = erpDataManageDao.getErpDataTables(dataTablesInput, accList);
        List<FcCompareAccVO> list = output.getData();
        if(EmptyUtils.isNotEmpty(list)){
            for(FcCompareAccVO vo : list){
                BtBankAccDTO bankAccDTO = bankMap.get(vo.getCompAcc());
                vo.setCorpId(bankAccDTO.getCorpId());
                vo.setCorpName(bankAccDTO.getCorpName());
                vo.setBankName(bankAccDTO.getBankName());
                vo.setCurName(bankAccDTO.getCurName());
            }
        }
        return output;
    }
    /**
     * erp导入银行帐号
     *
     * @param user
     * @return
     */
    public List<BtBankAccDTO> getBankAccsErpData(ICurrentUser user) {
        List<BtBankAccDTO> btBankAccDTOList = btBankAccService.getBankAccByUserId(user.getId(),"1");
        List<String> bankList = new ArrayList<>();
        Map<String,BtBankAccDTO> map = new HashMap<>();
        if(EmptyUtils.isNotEmpty(btBankAccDTOList)){
            for(BtBankAccDTO dto : btBankAccDTOList){
                bankList.add(dto.getBankAcc());
                map.put(dto.getBankAcc(),dto);
            }
        }
        List<BtBankAccDTO> list = erpDataManageDao.getBankAccsErpData(bankList);
        if(EmptyUtils.isNotEmpty(list)){
            List<BtCurrencyDTO> currencyDTOList = btCurrencyService.getBtCurrency();
            Map<String, BtCurrencyDTO> currencyDTOMap = new HashMap<>();
            for(BtCurrencyDTO currencyDTO : currencyDTOList) {
                currencyDTOMap.put(currencyDTO.getId(), currencyDTO);
            }
            for(BtBankAccDTO dto : list){
               String bankAcc = dto.getBankAcc();
               String curId = dto.getCurId();
                BtCurrencyDTO btCurrencyDTO = currencyDTOMap.get(curId);
                BtBankAccDTO temp = map.get(bankAcc);
                dto.setAccName(temp.getAccName());
                dto.setBankName(temp.getBankName());
                if(!EmptyUtils.isEmpty(btCurrencyDTO)) {
                    dto.setCurName(btCurrencyDTO.getCurName());
                }
            }
        }
        return list;
    }
    /**
     * erp导入银行帐号
     *
     * @param input
     * @return
     */
    public DataTablesOutput getBankAccsErpData(DataTablesInput input, ICurrentUser user) {
        List<ErpBasicData> erpBasicDataList = erpDataManageDao.getAllErpBasicData();
        List<String> bankList = new ArrayList<>();
        for(ErpBasicData erpBasicData : erpBasicDataList){
            BtBankAccDTO btBankAccDTO = btBankAccService.getBankAccByBankAccAndCurId(erpBasicData.getBytterCode(),erpBasicData.getErpReverse());
            if(!EmptyUtils.isEmpty(btBankAccDTO)){
                bankList.add(btBankAccDTO.getId());
            }
        }
        return btBankAccService.getBankAccsErpData(input, user.getId(),bankList);
    }
    /**
     * 功能介绍 : 获取第三方ERP接口数据<br>
     *
     * @param dataTablesInput
     * @return <b>日期：</b>2017-6-9<br> <br>
     * @version 1.0
     * @author lvwl
     */
    @Transactional(readOnly = true)
    public DataTablesOutput listInter(DataTablesInput dataTablesInput, String userId) {
        List<String> corpAuth = null;
        if (null != dataTablesInput.getFormdata().get("corpId") && StringUtils.isNotEmpty(dataTablesInput.getFormdata().get("corpId").toString())) {
            corpAuth = new ArrayList<>();
            corpAuth.add(dataTablesInput.getFormdata().get("corpId").toString());
        }else{
            corpAuth = corpService.getUserCorpIds(FinacialResourceConstant.FINACIAL_CHECK_ERP_DATA_INTERFACE_QUERY, userId);
        }
        //根据单位获取账号
        List<BtBankAccDTO> bankaccList = btBankAccService.getBankAccByCorpIdList(corpAuth,"", new HashMap<>());
        List<String> accList = new ArrayList<>();
        Map<String,BtBankAccDTO> bankMap = new HashMap<>();
        if(EmptyUtils.isNotEmpty(bankaccList)){
            for(BtBankAccDTO dto : bankaccList){
                accList.add(dto.getBankAcc());
                bankMap.put(dto.getBankAcc(),dto);
            }
        }
        DataTablesOutput output = erpDataManageDao.listInter(dataTablesInput, accList);
        List<FcCompareAccVO> list = output.getData();
        if(EmptyUtils.isNotEmpty(list)){
            for(FcCompareAccVO vo : list){
                BtBankAccDTO bankAccDTO = bankMap.get(vo.getCompAcc());
                vo.setCorpId(bankAccDTO.getCorpId());
                vo.setCorpName(bankAccDTO.getCorpName());
                vo.setBankName(bankAccDTO.getBankName());
                vo.setCurName(bankAccDTO.getCurName());
            }
        }
        return output;
    }
    /**
     * 功能介绍 : 提取ERP接口数据<br>
     *
     * @param
     * @return <b>日期：</b>2017-6-9<br> <br>
     * @version 1.0
     * @author lvwl
     */
//    @Transactional(readOnly = true)
    public void distillInter(List<String> ids) {
        List<FcCompareAcc> compAccExt = erpDataManageDao.getcompAccExt(ids);
        ErpDataVO vo = new ErpDataVO();
        vo.setAccList(compAccExt);
        this.saveList(vo, "3");//保存到compareAcc表中
    }
    /**
     * 功能介绍 :ERP接口导入数据生成（保存） <br>
     *
     * @param ids <b>日期：</b>2017-6-9<br> <br>
     * @version 1.0
     * @author lvwl
     */
    public void doAccInter(List<String> ids, String bankAcc, String curId) {
        List<FcCompareAcc> compAccExt = erpDataManageDao.getcompAccExt(ids);
        List compAccIds = erpDataManageDao.queryDeleteCompAcc(bankAcc, curId);//查出要删除的接口导入未结转的数据，在中间表中将状态改为未导入
        erpDataManageDao.deleteCompAcc(bankAcc, curId);//删除compareAcc表中为接口导入未结转的数据
        erpDataManageDao.updateFcBankBillYesToNo(bankAcc, curId);//把银行明细对账表已对账的数据变成未对账
        erpDataManageDao.updateFcCompareBillYesToNo(bankAcc, curId);//把企业明细对账表已对账的数据变成未对账
        if (!EmptyUtils.isEmptyList(compAccIds)) {
            erpDataManageDao.updateCompAccExt1(compAccIds);
        }
        ErpDataVO vo = new ErpDataVO();
        vo.setAccList(compAccExt);
        this.saveList(vo, "3");
    }
    /**
     * 改为未对账
     */
    public void updateCompAccExt1(String id) {
        List compAccIds = new ArrayList();
        compAccIds.add(id);
        erpDataManageDao.updateCompAccExt1(compAccIds);
    }
    /**
     * 更新导入标志
     **/
    public void updateCompAccExt(List<String> ids, String beginDate, String endDate, String corpId, String bankAcc, String userId) {
        erpDataManageDao.updateCompAccExt(ids, beginDate, endDate, corpId, bankAcc, userId);//把中间表id对应的状态改为1
    }
    /**
     * ERP数据导入
     */
    public DataTablesOutput erpImpDatatable(@RequestBody DataTablesInput dataTablesInput, String userId) {
        List<String> corpAuth = corpService.getUserCorpIds(FinacialResourceConstant.FINACIAL_CHECK_ERP_DATA_IMPROT, userId);
        //根据单位获取账号
        List<String> bankaccList = btBankAccService.getBankAccsByCorpIdList(corpAuth,"", new HashMap<>());
        DataTablesOutput output = erpDataManageDao.erpImpDatatable(dataTablesInput,bankaccList);
        List<FcCompareAccVO> list = output.getData();
        if(EmptyUtils.isNotEmpty(list)){
            List<BtCurrencyDTO> currencyDTOList = btCurrencyService.getBtCurrency();
            Map<String, BtCurrencyDTO> currencyDTOMap = new HashMap<>();
            for(BtCurrencyDTO currencyDTO : currencyDTOList) {
                currencyDTOMap.put(currencyDTO.getId(), currencyDTO);
            }
            for (FcCompareAccVO fcCompareAccVO : list){
                String curId = fcCompareAccVO.getCurId();
                BtCurrencyDTO currencyDTO = currencyDTOMap.get(curId);
                if (!EmptyUtils.isEmpty(currencyDTO)) {
                    fcCompareAccVO.setCurName(currencyDTO.getCurName());
                }
            }
        }
        return output;
    }
    /**
     * 功能介绍 : <br>
     *
     * @return <b>日期：</b>2017-6-13<br> <br>
     * @version 1.0
     * @author lvwl
     */
    public void impDataSave(List<FcCompareAccVO> list, String bankAcc, String curId) {
        if (EmptyUtils.isEmpty(bankAcc)) {
            throw new ParameterException(CheckMessageEnum.FIN_CHECK_CENTRALIYDATAMANAGER_BANKACC_EMPTY);
        } else {
            //TODO 检查 银行账号是否维护ERP科目对应关系
            if (false) {
                throw new ParameterException(CheckMessageEnum.FIN_CHECK_CENTRALIYDATAMANAGER_BANKACC_NOTERPR);
            }
        }
        List<FcCompareAccVO> importlist = new ArrayList<FcCompareAccVO>();
        BtBankAccDTO bankAccDTO = btBankAccService.getBankAccInfo(bankAcc);
        if (bankAccDTO == null) {
            //银行账号不存在
            throw new ParameterException(CheckMessageEnum.FIN_CHECK_CENTRALIYDATAMANAGER_BANKACC_ISNULL);
        }
        //验证
        for (FcCompareAccVO vo : list) {
            //验证
            //impDataSaveCheck(vo,0);
            //TODO 红蓝标识转化
            if ("红".equals(vo.getRedblueSign())) {
                vo.setRedblueSign("R");
            } else {
                vo.setRedblueSign("B");
            }
            vo.setCompAcc(bankAcc);
            if (vo.getCreditMoney() == null) {
                vo.setCreditMoney(BigDecimal.ZERO);
            }
            if (vo.getDebitMoney() == null) {
                vo.setDebitMoney(BigDecimal.ZERO);
            }
            vo.setImportType(FinacialConstant.IMPORT_TYPE_FILE);
            vo.setCheckType(FinacialConstant.CHECK_TYPE_ERP);
            vo.setCompareSign(FinacialConstant.COMPARE_SIGN_NO);
            vo.setBankId(bankAccDTO.getId());
            vo.setCurId(curId);
            //TODO 起息日期
            vo.setInterestStartDate(new Date());
			/*//流水号
	    	vo.setOrderNo(" ");
			//序号
	    	vo.setBankId(" ");
			//币别
	    	vo.setCurCode(" ");*/
            vo.setLineNo(new BigDecimal(3));
            importlist.add(vo);
        }
        List<FcCompareAcc> accList = BeanUtils.copyListProperties(importlist, FcCompareAcc.class);
        // 保存
        batchSave(accList);
    }
    /**
     * 删除对象
     *
     * @param id
     */
    public void deleteEntity(String id) {
        FcCompareAcc entity = get(id);
		if(!entity.getCompareSign().equals(FinacialConstant.COMPARE_SIGN_NO)){
			throw new ParameterException(CheckMessageEnum.FIN_CHECK_CENTRALIYDATAMANAGER_COMPARE_YES_DEL);
		}
        //已对账或未对账的数据才能删除
        FcBankCheckDtl bankCheckDtl = new FcBankCheckDtl();
        bankCheckDtl.setErpId(id);
        bankCheckDtl.setBankAcc(entity.getCompAcc());
        bankCheckDtl.setCurCode(entity.getCurId());
        bankCheckDtl.setStatus(CommonConstant.STATUE_DELETE);
        fcBankCheckDtlService.save(bankCheckDtl);
        String bankId = entity.getBankId();
        erpDataManageDao.deleteCompareAcc(entity);
        /**erp接口提取保存，还原导入标识*/
        if(FinacialConstant.IMPORT_TYPE_INTERFACE.equals(entity.getImportType())){
            if (EmptyUtils.isNotEmpty(bankId)) {
                updateCompAccExt1(bankId);
            }
        }
    }
    /**
     * 批量删除中心对账数据
     */
    public void batchDelete(List<String> ids) {
        for (String id : ids) {
            deleteEntity(id);
        }
    }
    /**
     * 根据id删除
     *
     * @param id
     */
    public void delete(String id) {
        super.delete(id);
    }
    /**
     * 保存
     *
     * @param entity
     */
    public void save(FcCompareAcc entity) {
        super.saveEntity(entity);
    }
    /**
     * 更新
     *
     * @param entity
     */
    public void update(FcCompareAcc entity) {
        super.updateEntity(entity);
    }
    /**
     * 保存or更新
     *
     * @param entity
     */
    public void saveOrUpdate(FcCompareAcc entity) {
        saveCheck(entity, 1);
        super.saveOrUpdate(entity);
    }
    /****************       验证begin         *****************/
    @Override
    public ExcelVerifyHanlderResult verifyHandler(FcCompareAccVO obj) {
        return null;
    }
    @Override
    public void verifyHandler(List<FcCompareAccVO> list) {
        List<String> bankAccList = new ArrayList<>();
        for (FcCompareAccVO vo : list) {
            List<String> msgList = new ArrayList<>();
            if (EmptyUtils.isEmpty(vo.getVoucherNo())) {
                msgList.add("fin.check.centraliydatamanager.voucherno.empty");
            }
            if (vo.getTallyDate() == null) {
                msgList.add("fin.check.centraliydatamanager.tallydate.empty");
            }
            if (!bigDecimalIsZero(vo.getCreditMoney()) && !bigDecimalIsZero(vo.getDebitMoney())) {
                msgList.add("fin.check.centraliydatamanager.de.cr.notzero");
            }
            if (bigDecimalIsZero(vo.getCreditMoney()) && bigDecimalIsZero(vo.getDebitMoney())) {
                msgList.add("fin.check.centraliydatamanager.de.cr.zero");
            }
//            if (vo.getBalance() == null) {
//                msgList.add("fin_check_centraliydatamanager_balance_empty");
//            }
            if (EmptyUtils.isBlank(vo.getDataSource())) {
                msgList.add("fin.check.centraliydatamanager.datasource.empty");
            }
            vo.addErrorMsg(msgList);
        }
    }
    /**
     * 功能介绍 : 导入保存验证<br>
     *
     * @param vo
     * @param index <b>日期：</b>2017-6-13<br> <br>
     * @version 1.0
     * @author lvwl
     */
    public void impDataSaveCheck(ErpDataVO vo, int index) {
        if (EmptyUtils.isEmpty(vo.getVoucherNo())) {
            throw new ParameterException(CheckMessageEnum.FIN_CHECK_CENTRALIYDATAMANAGER_VOUCHERNO_EMPTY);
        }
        if (vo.getTallyDate() == null) {
            throw new ParameterException(CheckMessageEnum.FIN_CHECK_CENTRALIYDATAMANAGER_TALLYDATE_EMPTY);
        }
        if (!bigDecimalIsZero(vo.getCreditMoney()) && !bigDecimalIsZero(vo.getDebitMoney())) {
            throw new ParameterException(CheckMessageEnum.FIN_CHECK_CENTRALIYDATAMANAGER_DE_CR_NOTZERO);
        }
        if (bigDecimalIsZero(vo.getCreditMoney()) && bigDecimalIsZero(vo.getDebitMoney())) {
            throw new ParameterException(CheckMessageEnum.FIN_CHECK_CENTRALIYDATAMANAGER_DE_CR_ZERO);
        }
        if (vo.getBalance() == null) {
            throw new ParameterException(CheckMessageEnum.FIN_CHECK_CENTRALIYDATAMANAGER_BALANCE_EMPTY);
        }
        if (EmptyUtils.isBlank(vo.getDataSource())) {
            throw new ParameterException(CheckMessageEnum.FIN_CHECK_CENTRALIYDATAMANAGER_DATASOURCE_EMPTY);
        }
    }
    /**
     * 功能介绍 : 保存验证<br>
     * <b>日期：</b>2017-6-5<br> <br>
     *
     * @version 1.0
     * @author lvwl
     */
    public void saveCheck(FcCompareAcc acc, int index) {
        if (EmptyUtils.isEmpty(acc.getVoucherNo())) {
            throw new ParameterException(CheckMessageEnum.FIN_CHECK_CENTRALIYDATAMANAGER_VOUCHERNO_EMPTY);
        }
        if (EmptyUtils.isEmpty(acc.getOrderNo())) {
            //throw new ParameterException(CheckMessageEnum.FIN_CHECK_CENTRALIYDATAMANAGER_ORDERNO_EMPTY);
        }
        if (EmptyUtils.isEmpty(acc.getCompAcc())) {
            throw new ParameterException(CheckMessageEnum.FIN_CHECK_CENTRALIYDATAMANAGER_COMPACC_EMPTY);
        }
        if (!bigDecimalIsZero(acc.getCreditMoney()) && !bigDecimalIsZero(acc.getDebitMoney())) {
            throw new ParameterException(CheckMessageEnum.FIN_CHECK_CENTRALIYDATAMANAGER_DE_CR_NOTZERO);
        }
        if (bigDecimalIsZero(acc.getCreditMoney()) && bigDecimalIsZero(acc.getDebitMoney())) {
            throw new ParameterException(CheckMessageEnum.FIN_CHECK_CENTRALIYDATAMANAGER_DE_CR_ZERO);
        }
        if (acc.getTallyDate() == null) {
            throw new ParameterException(CheckMessageEnum.FIN_CHECK_FCBANKBILLTEMP_TALLYDATE_NOT_NULL);
        }
    }
    /**
     * 功能介绍 : 判断BigDecimal是否为null或者为0<br>
     *
     * @return 为null或者0 ：true
     * <b>日期：</b>2017-6-9<br> <br>
     * @version 1.0
     * @author lvwl
     */
    @Transactional(readOnly = true)
    public boolean bigDecimalIsZero(BigDecimal data) {
        if (data == null || data.compareTo(BigDecimal.ZERO) == 0) {
            return true;
        }
        return false;
    }
    /****************       验证end         *****************/
    public BaseDao<FcCompareAcc, String> getEntityDao() {
        return erpDataManageDao;
    }
}
