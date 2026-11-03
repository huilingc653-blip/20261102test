package com.bytter.biz.transfer.swap.service;
import com.bytter.biz.bdlink.api.service.IBisAccDtlService;
import com.bytter.biz.bdlink.vo.BisAccDtlVO;
import com.bytter.biz.transfer.swap.dao.ErpTemplateStoreDao;
import com.bytter.biz.transfer.swap.entity.ErpHisDtlTemplate;
import com.bytter.biz.transfer.swap.entity.ErpInterConfigFill;
import com.bytter.biz.transfer.swap.entity.ErpTemplateStore;
import com.bytter.biz.transfer.swap.enums.ErpInterConfigEnum;
import com.bytter.biz.transfer.swap.enums.HisDtlTemplateStatusEnum;
import com.bytter.biz.transfer.swap.util.ErpHisDtlUtils;
import com.bytter.biz.transfer.swap.vo.ErpTemplateBillVO;
import com.bytter.biz.transfer.swap.vo.ErpTemplateStoreVO;
import com.bytter.check.constant.TransfterHisDtlConstant;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.core.utils.StringUtils;
import com.bytter.system.api.IBtBankAccService;
import com.bytter.system.api.ICorpService;
import com.bytter.system.api.IUserService;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.*;
/**
 * Created by zhouj on 2018/5/29.
 */
@Service("erpTemplateStoreService")
@Transactional
public class ErpTemplateStoreService extends BaseService<ErpTemplateStore,String> {
    @Autowired
    private ErpHisDtlComFieldService erpHisDtlComFieldService;
    @Autowired
    private ErpInterConfigFillService erpInterConfigFillService;
    @Autowired
    private ErpInterConfigService erpInterConfigService;
    @Autowired
    private ErpTemplateStoreDao templateStoreDao;
    @DubboReference(check = false)
    private ICorpService corpService;
    @DubboReference(check = false)
    private IBtBankAccService btBankAccService;
    @DubboReference(check = false)
    private IBisAccDtlService bisAccDtlService;
    @Autowired(required = false)
    private IUserService userService;
    @Override
    public BaseDao<ErpTemplateStore, String> getEntityDao() {
        return templateStoreDao;
    }
    public DataTablesOutput getDataTable(DataTablesInput dataTablesInput) {
        List<String> corpAuthList = new ArrayList<String>();
        String dealType = (String) dataTablesInput.getFormdata().get("dealType");
        if(ErpInterConfigEnum.PAYER.getCode().equals(dealType)){
            corpAuthList = corpService.getUserCorpIds(TransfterHisDtlConstant.TRANSFTER_PAYER_LIST_CORPOP, dataTablesInput.getCurrentUserId());
        }else{
            corpAuthList = corpService.getUserCorpIds(TransfterHisDtlConstant.TRANSFTER_PAYEE_LIST_CORPOP, dataTablesInput.getCurrentUserId());
        }
        //单据补录
        int newSet = erpInterConfigService.getValueByTypeAndCode(dealType,"newSet").intValue();
        if(newSet == 1){
            return erpHisDtlComFieldService.getDataTable(dataTablesInput,corpAuthList);
        }
        return getDataTable(dataTablesInput,corpAuthList);
    }
    private DataTablesOutput getDataTable(DataTablesInput dataTablesInput,List<String> corpAuthList){
        String bankTypeId = (String) dataTablesInput.getFormdata().get("bank_type_id");
        String accAndSql="";
        Map<String,Object> params0 = new HashMap<>();
        if(StringUtils.isNotEmpty(bankTypeId)){
            accAndSql = " AND acc.bank_type_id =:bankTypeId ";
            params0.put("bankTypeId",bankTypeId);
        }
        //用单位查询账号
        List<String> accStrList = btBankAccService.getBankAccsByCorpIdList(corpAuthList, accAndSql, params0);
//        查询bal
        Map<String, Object> params = new HashMap<>();
        String sql= templateStoreDao.selectBisAccDtlSql( dataTablesInput, params,  accStrList);
        List<BisAccDtlVO> bisAccDtlVOS = bisAccDtlService.getBisAccDtlListBySql(params, sql);

        dataTablesInput.getFormdata().remove("ged_transTime");
        dataTablesInput.getFormdata().remove("led_transTime");
        dataTablesInput.getFormdata().remove("led_transTime");
        List<ErpTemplateStoreVO> templateStoreVOList=new ArrayList<>();
        for(BisAccDtlVO dtlVO:bisAccDtlVOS){
            ErpTemplateStoreVO vo= templateStoreDao.getErpTemplateStoreVO(dataTablesInput,dtlVO.getId());
            String status = (String) dataTablesInput.getFormdata().get("status");
            if(StringUtils.isNotBlank(status)){
                if(String.valueOf(HisDtlTemplateStatusEnum.ISALL.getStatus()).equals(status)){//全部
                    if(null == vo) {
                        vo=new ErpTemplateStoreVO();
                    }
                    setErpTemplateStoreVO( vo, dtlVO);
                    templateStoreVOList.add(vo);
                }else if(String.valueOf(HisDtlTemplateStatusEnum.ISNULL.getStatus()).equals(status)){
                    if(null == vo) {
                        vo=new ErpTemplateStoreVO();
                    }
                    setErpTemplateStoreVO( vo, dtlVO);
                    templateStoreVOList.add(vo);
                }else{
                    if(null != vo) {
                        setErpTemplateStoreVO(vo, dtlVO);
                        templateStoreVOList.add(vo);
                    }
                }
            }
        }
        DataTablesOutput output = new DataTablesOutput();
        output.setData(templateStoreVOList);
        return output;
    }
    private ErpTemplateStoreVO setErpTemplateStoreVO(ErpTemplateStoreVO vo,BisAccDtlVO dtlVO){
        vo.setDtlId(dtlVO.getId());
        vo.setBankAcc(dtlVO.getBankAcc());
        vo.setAccName(dtlVO.getAccName());
        vo.setBankName(dtlVO.getBankName());
        vo.setOppAccNo(dtlVO.getOppAccNo());
        vo.setOppAccName(dtlVO.getOppAccName());
        vo.setOppAccBank(dtlVO.getOppAccBank());
        vo.setCdSign(dtlVO.getCdSign());
        vo.setRbSign(dtlVO.getRbSign());
        vo.setAmt(dtlVO.getAmt());
        vo.setCurId(dtlVO.getCurId());
        vo.setTransTime(dtlVO.getTransTime());
        if (StringUtils.isNotEmpty(vo.getOpName())) {
            vo.setOpName(userService.getByUserId(vo.getOpName()).getUsername());
        }
        return vo;
    }
    public ErpTemplateStoreVO selectBankccById(String id) {
//        return templateStoreDao.selectBankccById(id);
        String sql= templateStoreDao.selectBisAccDtlByIdSql( id);
        List<BisAccDtlVO> bisAccDtlVOS=new ArrayList<>();
        try {
            Map<String, Object> params = new HashMap<>();
            params.put("id",id);
            bisAccDtlVOS = bisAccDtlService.getBisAccDtlListBySql(params, sql);
        }catch (Exception e){
        }
        ErpTemplateStoreVO vo=new  ErpTemplateStoreVO();
        if(null != bisAccDtlVOS && bisAccDtlVOS.size()>0) {
            List<ErpTemplateStoreVO> templateStoreVOList = new ArrayList<>();
            BisAccDtlVO dtlVO = bisAccDtlVOS.get(0);
            vo = templateStoreDao.getErpTemplateStoreVOById(dtlVO.getId());
            vo.setDtlId(dtlVO.getId());
            vo.setBankAcc(dtlVO.getBankAcc());
            vo.setAccName(dtlVO.getAccName());
            vo.setBankName(dtlVO.getBankName());
            vo.setOppAccNo(dtlVO.getOppAccNo());
            vo.setOppAccName(dtlVO.getOppAccName());
            vo.setOppAccBank(dtlVO.getOppAccBank());
            vo.setCdSign(dtlVO.getCdSign());
            vo.setRbSign(dtlVO.getRbSign());
            vo.setAmt(dtlVO.getAmt());
            vo.setCurId(dtlVO.getCurId());
            vo.setTransTime(dtlVO.getTransTime());
            if (StringUtils.isNotEmpty(vo.getOpName())) {
                vo.setOpName(userService.getByUserId(vo.getOpName()).getUsername());
            }
            templateStoreVOList.add(vo);
        }
        return vo;
    }
    public DataTablesOutput getDataTablebill(DataTablesInput dataTablesInput) {
        List<String> corpAuthList = corpService.getUserCorpIds(TransfterHisDtlConstant.TRANSFTER_ACCEBILL_LIST_CORPOP, dataTablesInput.getCurrentUserId());
        return templateStoreDao.getDataTablebill(dataTablesInput,corpAuthList);
    }
    public ErpTemplateBillVO selectBillById(String id) {
        return templateStoreDao.selectBillById(id);
    }
    /**
     * 获取补录中间映射信息
     * @param serialId
     * @return
     */
    public ErpHisDtlTemplate getErpHisDtlTemplate(String serialId) {
        return templateStoreDao.getErpHisDtlTemplate(serialId);
    }
    public DataTablesOutput getDataTableQuery(DataTablesInput dataTablesInput) {
        List<String> corpAuthList = new ArrayList<String>();
        String dealType = (String) dataTablesInput.getFormdata().get("dealType");
        if(ErpInterConfigEnum.PAYER.getCode().equals(dealType)){
            corpAuthList = corpService.getUserCorpIds(TransfterHisDtlConstant.TRANSFTER_PAYER_QUERY_CORPOP, dataTablesInput.getCurrentUserId());
        }else{
            corpAuthList = corpService.getUserCorpIds(TransfterHisDtlConstant.TRANSFTER_PAYEE_QUERY_CORPOP, dataTablesInput.getCurrentUserId());
        }
        //单据补录
        int newSet = erpInterConfigService.getValueByTypeAndCode(dealType,"newSet").intValue();
        if(newSet == 1){
            return erpHisDtlComFieldService.getDataTable(dataTablesInput,corpAuthList);
        }
        return getDataTable( dataTablesInput, corpAuthList);
    }
    public DataTablesOutput getDataTablebillQuery(DataTablesInput dataTablesInput) {
        List<String> corpAuthList = corpService.getUserCorpIds(TransfterHisDtlConstant.TRANSFTER_ACCEBILL_QUERY_CORPOP, dataTablesInput.getCurrentUserId());
        if(dataTablesInput.getFormdata().containsKey("led_transTime")){
            String dateValue=dataTablesInput.getFormdata().get("led_transTime")+" 23:59:59";
            dataTablesInput.getFormdata().put("led_transTime", dateValue);
        }
        if(dataTablesInput.getFormdata().containsKey("led_businessDate")){
            String dateValue=dataTablesInput.getFormdata().get("led_businessDate")+" 23:59:59";
            dataTablesInput.getFormdata().put("led_businessDate", dateValue);
        }
        return templateStoreDao.getDataTablebill(dataTablesInput,corpAuthList);
    }
    
    public ErpTemplateStore selectDtlMapById(String id) {
        String hql = "from ErpTemplateStore where id=:id";
        Map<String,Object> params = new HashMap<String, Object>();
        params.put("id",id);
        List list = templateStoreDao.listByHQL(hql,params);
        if(EmptyUtils.isEmptyList(list)){
            return new ErpTemplateStore();
        }else{
            return (ErpTemplateStore) list.get(0);
        }
    }
    /**
     *
     * 根据模板补录和单据补录取 数据主信息列表头信息
     * @param newSetValue
     * @return
     */
    public List<Map<String,String>> erpDataHeader(int newSetValue,String type){
        if( newSetValue == 1) {
            /*** 单据补录*/
            List<ErpInterConfigFill>  configFills= erpInterConfigFillService.queryErpFieldValid(type);
            List<Map<String,String>> lms = ErpHisDtlUtils.getDataHeader(ErpHisDtlUtils.TYPECODE_NEWSET);
            if(StringUtils.isNotEmpty(configFills)){
                for (ErpInterConfigFill field : configFills){
                    lms.add(addEntity(field));
                }
            }
            return lms;
        }else{
            return ErpHisDtlUtils.getDataHeader(ErpHisDtlUtils.TYPECODE_TEMSET);
        }
    }
    /**
     * 动态添加列表头
     * @param field
     * @return
     */
    private Map<String,String> addEntity(ErpInterConfigFill field){
        Map<String,String>  enity=new HashMap<>();
        enity.put("field", field.getFieldCode());
        enity.put("text", field.getShowName());
        if(StringUtils.isNotEmpty(field.getDataSource()) && StringUtils.isNotBlank(field.getDataSource().trim())) {
            enity.put("type", "dynaSource");
            enity.put("groupCode", field.getDataSource());
        }
        return enity;
    }
}
