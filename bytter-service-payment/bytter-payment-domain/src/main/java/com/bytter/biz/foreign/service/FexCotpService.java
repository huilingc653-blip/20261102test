package com.bytter.biz.foreign.service;

import com.bytter.biz.foreign.dao.FexCotpCustInfoDao;
import com.bytter.biz.foreign.dao.FexCotpDao;
import com.bytter.biz.foreign.entity.FexCotp;
import com.bytter.biz.foreign.entity.FexCotpCustInfo;
import com.bytter.biz.foreign.vo.FexCotpCustInfoVO;
import com.bytter.biz.foreign.vo.FexCotpVO;
import com.bytter.biz.foreign.vo.FexTranReqVO;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.workflow.bpm.annotation.WorkFlow;
import com.bytter.workflow.dto.IWorkFlowResult;
import com.bytter.workflow.dto.WorkFlowBusiness;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.exception.BusinessException;
import com.bytter.core.common.exception.ParameterException;
import com.bytter.core.utils.BeanUtils;
import com.bytter.core.utils.DateUtils;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.core.utils.StringUtils;
import com.bytter.core.enums.AccebillMessageEnum;
import com.bytter.workflow.bpm.IWorkFlowListener;
import com.bytter.workflow.api.IWorkFlowService;
import com.bytter.core.constant.CommonConstant;
import com.bytter.biz.payment.api.constant.BdlinkConstant;
import com.bytter.biz.payment.api.constant.FexMessageConstant;
import com.bytter.biz.payment.api.constant.FexWorkFlowConstant;
import com.bytter.biz.payment.api.constant.ForeignConstant;
import com.bytter.core.constant.ResourceConstant;
import com.bytter.biz.payment.api.enums.ForeignMessageEnum;
import com.bytter.system.api.constant.NotificationConstant;
import com.bytter.system.api.dto.ExternalCorpBankAccDTO;
import com.bytter.system.api.dto.ExternalCorpDTO;
import com.bytter.system.api.dto.InputBankInfoDTO;
import com.bytter.system.api.dto.RegionDTO;
import com.bytter.system.api.dto.SysCountryCustDTO;
import com.bytter.system.api.*;
import com.bytter.poi.excel.entity.result.ExcelVerifyHanlderResult;
import com.bytter.poi.handler.inter.IExcelVerifyHandler;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service("fexCotpService")
@WorkFlow(value = FexWorkFlowConstant.FEX_COTP)
@Transactional
public class FexCotpService  extends BaseService<FexCotp, String> implements IWorkFlowListener, IExcelVerifyHandler<FexCotpVO> {

    @Autowired
    private FexCotpDao fexCotpDao;

    @Autowired
    private FexMessageService fexMessageService;

    @Autowired
    private IWorkFlowService workFlowService;

    @DubboReference(check = false)
    private ICorpService corpService;

    @DubboReference(check = false)
    private INotificationService notificationService;

    @Autowired(required=false)
    private FexCustFieldRuleService fexCustFieldRuleService;

    @Autowired
    private FexCotpCustInfoDao fexCotpCustInfoDao;

    @Resource
    private IMultiLangService multiLangServiceI;

    @DubboReference(check = false)
    ISysCountryCustService sysCountryCustService;

    @DubboReference(check = false)
    IInputBankInfoService inputBankInfoService;

    @DubboReference(check = false)
    private IRegionService regionService;

    @DubboReference(check = false)
    private IExternalCorpService externalCorpService;

    public String saveCotpRecord(FexTranReqVO fexTranReq, ICurrentUser user, Boolean editFlag) {
        String cotpName = StringUtils.isNotBlank(fexTranReq.getCotpName()) ? fexTranReq.getCotpName() : fexTranReq.getCotpAccName();
        ExternalCorpDTO externalCorpDTO = buildExternalCorpDTO(fexTranReq, cotpName);
        externalCorpService.saveIfNotExist(externalCorpDTO);
        ExternalCorpDTO savedExternalCorp = externalCorpService.findByCorpIdAndName(externalCorpDTO.getCorpId(), externalCorpDTO.getName());
        if (savedExternalCorp != null) {
            return savedExternalCorp.getId();
        }
        // 名称未命中客商（客商主数据不会被单据改名）：按交易对手银行账号回查其所属客商，
        // 避免直接回落到 oppId（银行/联行号ID）导致单据的交易对手展示为空。
        ExternalCorpBankAccDTO bankAccDTO = externalCorpService.queryByBankAcc(fexTranReq.getCotpBankAcc());
        if (bankAccDTO != null && StringUtils.isNotBlank(bankAccDTO.getExternalCorpId())) {
            return bankAccDTO.getExternalCorpId();
        }
        return fexTranReq.getOppId();
    }

    public void updateExternalCorpInfo(FexTranReqVO fexTranReq, ICurrentUser user) {
        //客商同步的落点是 cotpId（单据 COTP_ID 存的就是客商ID）。
        //oppId 不落库、仅由前端在选择交易对手账号时赋值，不能拿它当前置条件，
        //否则未走弹窗选择、接口字段缺失或历史数据等场景下整段客商同步会被静默跳过，
        //表现为「单据上填的交易对手地址保存不到客商、再次编辑时该字段为空」。
        String externalCorpId = StringUtils.isNotBlank(fexTranReq.getCotpId())
                ? fexTranReq.getCotpId() : fexTranReq.getOppId();
        if (StringUtils.isBlank(externalCorpId) || externalCorpService == null) {
            return;
        }
        ExternalCorpDTO dto = new ExternalCorpDTO();
        dto.setId(externalCorpId);
        fillExternalCorpArea(dto, fexTranReq);
        //境内客商维护页的“地址”与资金收付交易的“交易对手银行地址”使用同一口径；
        //境外客商仍使用交易对手地址，避免改变既有的境外地址语义。
        String externalCorpAddress = Integer.valueOf(1).equals(dto.getIsNative())
                && StringUtils.isNotBlank(fexTranReq.getCotpBankAdrr())
                ? fexTranReq.getCotpBankAdrr() : fexTranReq.getCotpAdrr();
        dto.setAddress(externalCorpAddress);
        ExternalCorpBankAccDTO bankAccDTO = new ExternalCorpBankAccDTO();
        bankAccDTO.setExternalAcc(fexTranReq.getCotpBankAcc());
        bankAccDTO.setBankCountryId(fexTranReq.getCountryId());
        bankAccDTO.setSwiftCode(fexTranReq.getCotpSwiftCode());
        bankAccDTO.setBankAddr(fexTranReq.getCotpBankAdrr());
        externalCorpService.updateExternalCorpInfo(dto, bankAccDTO, user);
    }

    ExternalCorpDTO buildExternalCorpDTO(FexTranReqVO fexTranReq, String cotpName) {
        ExternalCorpDTO externalCorpDTO = new ExternalCorpDTO();
        externalCorpDTO.setCorpId(fexTranReq.getCorpId());
        externalCorpDTO.setName(cotpName);
        externalCorpDTO.setNameEn(fexTranReq.getCotpEnName());
        externalCorpDTO.setSourceSystem("BFS");
        externalCorpDTO.setStatus(1);
        externalCorpDTO.setExternalType("0");
        externalCorpDTO.setAddress(fexTranReq.getCotpAdrr());
        externalCorpDTO.setAddressEn(fexTranReq.getCotpAddressNameEn());
        fillExternalCorpArea(externalCorpDTO, fexTranReq);
        List<ExternalCorpBankAccDTO> externalCorpBankAccList = new ArrayList<ExternalCorpBankAccDTO>();
        externalCorpBankAccList.add(buildExternalCorpBankAccDTO(fexTranReq));
        externalCorpDTO.setExternalCorpBankAccList(externalCorpBankAccList);
        return externalCorpDTO;
    }

    ExternalCorpBankAccDTO buildExternalCorpBankAccDTO(FexTranReqVO fexTranReq) {
        ExternalCorpBankAccDTO externalCorpBankAccDTO = new ExternalCorpBankAccDTO();
        externalCorpBankAccDTO.setExternalAcc(fexTranReq.getCotpBankAcc());
        externalCorpBankAccDTO.setExternalAccName(fexTranReq.getCotpAccName());
        externalCorpBankAccDTO.setBank(fexTranReq.getCotpBankName());
        externalCorpBankAccDTO.setBankSourceCode(fexTranReq.getCotpSwiftCode());
        externalCorpBankAccDTO.setSwiftCode(fexTranReq.getCotpSwiftCode());
        externalCorpBankAccDTO.setAccProvince(fexTranReq.getCotpCountry());
        externalCorpBankAccDTO.setAccCity(fexTranReq.getCotpAddress());
        externalCorpBankAccDTO.setBankAddr(fexTranReq.getCotpBankAdrr());
        externalCorpBankAccDTO.setCurId(fexTranReq.getCotpAccCurId());
        externalCorpBankAccDTO.setLocalClearNo(fexTranReq.getLocalClearNo());
        externalCorpBankAccDTO.setBankCountryId(fexTranReq.getCountryId());
        externalCorpBankAccDTO.setIntermediaryBank(fexTranReq.getCotpAgentBank());
        externalCorpBankAccDTO.setIntermediaryBankSwiftCode(fexTranReq.getCotpAgentSwiftCode());
        externalCorpBankAccDTO.setIntermediaryBankAcc(fexTranReq.getCotpAgentBankAcc());
        externalCorpBankAccDTO.setIntermediaryBankAddress(fexTranReq.getCotpAgentBankAddr());
        externalCorpBankAccDTO.setIsDefaultAcc("0");
        return externalCorpBankAccDTO;
    }

    private void fillExternalCorpArea(ExternalCorpDTO externalCorpDTO, FexTranReqVO fexTranReq) {
        SysCountryCustDTO china = sysCountryCustService.getByEnCode(BdlinkConstant.COUNTRY_CODE_CHINA);
        if (china != null && StringUtils.isNotBlank(china.getId()) && StringUtils.isNotBlank(fexTranReq.getCotpCountry())
                && !china.getId().equals(fexTranReq.getCotpCountry())) {
            externalCorpDTO.setProvince(fexTranReq.getCotpCountry());
            externalCorpDTO.setCity(fexTranReq.getCotpAddress());
            externalCorpDTO.setIsNative(0);
        } else {
            externalCorpDTO.setProvince(fexTranReq.getCotpProv());
            externalCorpDTO.setCity(fexTranReq.getCotpCity());
            externalCorpDTO.setIsNative(1);
        }
    }

    /**
     * 查询暂存数据
     * @param input
     * @return
     */
    public DataTablesOutput getDataTable(DataTablesInput input,ICurrentUser user){
        return buildCotpOutput(input, listExternalCorpAsCotp());
    }

    /**
     * 查询待审批数据
     *
     * @param input
     * @return
     */
    public DataTablesOutput getDataTableApprove(DataTablesInput input, ICurrentUser user) {
        WorkFlowBusiness workFlowBusiness = workFlowService.getWorkFlowBusiness(FexWorkFlowConstant.FEX_COTP, input);
        List<String> corps = corpService.getUserCorpIds(ResourceConstant.FOREIGN_FEX_COTP_QUERY, user.getId());
        return workFlowBusiness.merge(fexCotpDao.getDataTableApprove(input, corps));
    }

    /**
     *查询交易对手组内数据
     * @param input
     * @return
     */
    public DataTablesOutput getChildDataTable(DataTablesInput input) {
        return fexCotpDao.getChildDataTable(input);
    }

    /**
     *查询交易对手组内数据
     * @param id
     * @return
     */
    public List<FexCotpVO> listChildren(String id) {
        Map<String, Object> params = new HashMap<>();
        params.put("id", id);
        return fexCotpDao.listChildren(params);
    }

    /**
     *
     * @param input
     * @return
     */
    public DataTablesOutput getChildDataTableByIds(DataTablesInput input) {
        List<String> idArr = new ArrayList<>();
        Map<String, Object> params = input.getFormdata();
        if (params != null && null != params.get("ids")) {
            String ids = params.get("ids").toString();
            idArr.addAll(Arrays.asList(ids.split(",")));
        }
        return fexCotpDao.getChildDataTableByIds(input, idArr);
    }

    /**
     * 查询所有交易对手组
     *
     * @param input
     * @return
     */
    public DataTablesOutput getCotpGroupDataTable(DataTablesInput input, ICurrentUser user) {
        return buildCotpOutput(input, Collections.<FexCotpVO>emptyList());
    }

    /**
     * 查询所有无组数据
     *
     * @param input
     * @return
     */
    public DataTablesOutput getNullParentCotpDataTable(DataTablesInput input, ICurrentUser user) {
        return buildCotpOutput(input, listExternalCorpAsCotp());
    }

    /**
     * 增加子节点数据
     * @param params
     */
    public void appendChild(Map<String,Object> params){
        fexCotpDao.updateCotpbyIds(params);

    }

    /**
     * 新增时向组内增加数据
     * @param parentId
     * @param ids
     */
    public void saveAndAppendChild(String parentId, String ids) {
        Map<String, Object> params = new HashMap<>();
        params.put("parentId", parentId);
        params.put("ids", Arrays.asList(ids.split(",")));
        appendChild(params);
    }

    /**
     * 获取权限内交易对手组数据
     * @param user
     * @return
     */
    public List<FexCotpVO> listCotpGroup(ICurrentUser user){
        List<String> corps = corpService.getUserCorpIds(ResourceConstant.FOREIGN_FEX_COTP_QUERY, user.getId());
        return fexCotpDao.listCotpGroup(corps);
    }

    /**
     * 获取权限内交易对手(类别为外部银行)
     * @param cotpType
     * @return
     */
    public List<FexCotpVO> listBankCotp(String cotpType) {
        return listExternalCorpAsCotp();
    }

    /**
     * 保存数据
     * @param fexCotpVO
     */
    public void save(FexCotpVO fexCotpVO, ICurrentUser user) {
        FexCotp fexCotp;
        if (StringUtils.isNotEmpty(fexCotpVO.getId())) {
            fexCotp = fexCotpDao.get(fexCotpVO.getId());
            fexCotpVO.setUpdateBy(user.getId());
            fexCotpVO.setUpdateDate(new Date());
        } else {
            fexCotp = new FexCotp();
            fexCotpVO.setCreateBy(user.getId());
            fexCotpVO.setCreateDate(new Date());
        }
        fexCotpVO.setCorpId(user.getCorpId());
        if (StringUtils.isNotEmpty(fexCotpVO.getCotpType())) {
            if (fexCotpVO.getCotpType().contains(",")) {
                for (String cotpType : fexCotpVO.getCotpType().split(",")) {
                    if (StringUtils.isNotEmpty(fexCotpVO.getCotpTypeName())) {
                        fexCotpVO.setCotpTypeName(fexCotpVO.getCotpTypeName() + " " + getCotpTypeName(cotpType));
                    } else {
                        fexCotpVO.setCotpTypeName(getCotpTypeName(cotpType));
                    }
                }
            } else {
                fexCotpVO.setCotpTypeName(getCotpTypeName(fexCotpVO.getCotpType()));
            }
        }
        BeanUtils.copyNotNullProperties(fexCotpVO, fexCotp);
        fexCotpDao.saveOrUpdate(fexCotp);
        fexCotpCustInfoDao.deleteCustFieldInfoByBusId(fexCotp.getId());
//        新增时增加组内成员
        if (StringUtils.isEmpty(fexCotpVO.getId()) && StringUtils.isNotEmpty(fexCotpVO.getIds())) {
            saveAndAppendChild(fexCotp.getId(),fexCotpVO.getIds());
        }
        List<FexCotpCustInfo> fexCotpCustInfos = new ArrayList<>();
        if (null != fexCotpVO.getFexCotpCustInfoVOS() && fexCotpVO.getFexCotpCustInfoVOS().size() > 0) {
            for (FexCotpCustInfoVO fexCotpCustInfoVO : fexCotpVO.getFexCotpCustInfoVOS()) {
                if (StringUtils.isNotEmpty(fexCotpCustInfoVO.getFieldId())) {
                    FexCotpCustInfo fexCotpCustInfo = new FexCotpCustInfo();
                    BeanUtils.copyProperties(fexCotpCustInfoVO, fexCotpCustInfo);
                    fexCotpCustInfo.setBusId(fexCotp.getId());
                    fexCotpCustInfos.add(fexCotpCustInfo);
                }
            }
        }
        fexCotpCustInfoDao.batchSave(fexCotpCustInfos);
        Integer messageType = FexMessageConstant.MESSAGE_TYPE_TEMP;
        String message = FexMessageConstant.FEX_LOG_ADD_TEMPSAVE_SUCC;
        //启动审批
        if (ForeignConstant.STATUE_WAIT_CHECK.equals(fexCotp.getStatus())) {
            messageType = FexMessageConstant.MESSAGE_TYPE_SUBMIT;
            message = FexMessageConstant.FEX_LOG_ADD_SUBMIT_SUCC;
            //调用审批后方法
            getStartHandler().handler(FexWorkFlowConstant.FEX_COTP, fexCotp.getId(), fexCotpVO, fexCotp.getCorpId(), user.getId());
//            notificationService.send(fexCotp.getId(), user.getCorpId(), NotificationConstant.FOREIGN.FEX_COTP, fexCotp);
        }
        notificationService.complete(NotificationConstant.FOREIGN.FEX_COTP, fexCotp.getId(), user.getId());
        if(user !=null) {
            //生成日志
            fexMessageService.saveMessage(fexCotp.getId(), messageType, message, FexMessageConstant.BUS_TYPE_FEX_COTP, user);
        }
    }

    /**
     * 保存交易对手数据
     *
     * @param cotpDO
     * @return
     */
    public String saveCotpInfo(FexCotp cotpDO, Boolean editFlag) {
        if (!editFlag) {
            cotpDO.setId(null);
        }
        if (StringUtils.isNotBlank(cotpDO.getId())) {
            fexCotpDao.delete(cotpDO.getId());
        }
        FexCotp saveCotpDO = cotpDO;
        fexCotpDao.save(saveCotpDO);
        return saveCotpDO.getId();
    }

    /**
     * 通过id获取VO信息
     * @param id
     * @return
     */
    public FexCotpVO getFexcotpById(String id) {
        return externalCorpToFexCotp(externalCorpService.getById(id));
    }

    @Override
    public FexCotp get(String id) {
        FexCotpVO vo = getFexcotpById(id);
        if (vo == null) {
            return null;
        }
        FexCotp cotp = new FexCotp();
        BeanUtils.copyNotNullProperties(vo, cotp);
        return cotp;
    }


    @Override
    public BaseDao<FexCotp, String> getEntityDao() {
        return fexCotpDao;
    }

    @Override
    public void flowCompleted(IWorkFlowResult workFlowResult) {
        /*判断业务主键是否为空*/
        if (StringUtils.isEmpty(workFlowResult.getBusinessKey())) {
            throw new BusinessException(AccebillMessageEnum.ACCEBILL_MESSAGE_ERROR_VALISNOTNULL);
        }
        String id = workFlowResult.getBusinessKey();
        boolean isPass = workFlowResult.isPass();
        FexCotp fexCotp = fexCotpDao.get(id);
        ICurrentUser user = workFlowResult.getUser();
        Integer messageType = 0;
        String message = "";
        if (isPass) {
            fexCotp.setStatus(ForeignConstant.STATUE_CHECK);
            messageType = FexMessageConstant.MESSAGE_TYPE_CHECK_PASS;
            message = FexMessageConstant.FEX_LOG_APP_SUCC;
        } else {
            fexCotp.setStatus(ForeignConstant.STATUE_BACK);
            messageType = FexMessageConstant.MESSAGE_TYPE_CHECK_BACK;
            message = FexMessageConstant.FEX_LOG_APP_BACK;
        }
        //生成日志
        fexMessageService.saveMessage(fexCotp.getId(), messageType, message, FexMessageConstant.BUS_TYPE_FEX_COTP, user);
        fexCotpDao.update(fexCotp);
    }

    /**
     * 获取自定义字段
     * @return
     */
/*    public List<FexCustFieldRuleVO> getCustfield(){
        return fexCustFieldRuleService.listByCotp();
    }*/

    /**
     * 获取保存得自定义数据
     * @param id
     * @return
     */
    public List<FexCotpCustInfoVO> listCustInfoByBusId(String id){
        Map<String, Object> params = new HashMap<>();
        params.put("id", id);
        return fexCotpCustInfoDao.listCustInfoByBusId(params);
    }

    /**
     * 检查是否名称重复
     * @param name
     * @param id
     * @return
     */
    public boolean checkName(String name, String id) {
        ExternalCorpDTO externalCorp = externalCorpService.findByCorpIdAndName(null, name);
        return externalCorp == null || StringUtils.equals(id, externalCorp.getId());
    }

    /**
     * 查询所有交易对手及交易对手组数据
     * @return
     */
    public List<FexCotpVO> listCotp(){
        return listExternalCorpAsCotp();
    }

    public String getCotpTypeName(String cotpType){
        String cotpTypeName = "";
        switch (cotpType) {
            case "01":
                cotpTypeName = multiLangServiceI.getLang("fex.cotp.internalBank");
                break;
            case "02":
                cotpTypeName = multiLangServiceI.getLang("fex.cotp.externalCounterparties");
                break;
            case "03":
                cotpTypeName = multiLangServiceI.getLang("fex.cotp.custodian");
                break;
            case "04":
                cotpTypeName = multiLangServiceI.getLang("fex.cotp.settlementBank");
                break;
            case "05":
                cotpTypeName = multiLangServiceI.getLang("fex.cotp.externalBank");
                break;
            case "06":
                cotpTypeName = multiLangServiceI.getLang("fex.cotp.exchange");
                break;
            case "07":
                cotpTypeName = multiLangServiceI.getLang("fex.cotp.agent");
                break;
            case "08":
                cotpTypeName = multiLangServiceI.getLang("fex.cotp.fundManager");
                break;
            case "09":
                cotpTypeName = multiLangServiceI.getLang("fex.cotp.issuingOrPayingAgent");
                break;
            case "10":
                cotpTypeName = multiLangServiceI.getLang("fex.cotp.debitCreditParty");
                break;
            case "11":
                cotpTypeName = multiLangServiceI.getLang("fex.cotp.clearingAgent");
                break;
            case "12":
                cotpTypeName = multiLangServiceI.getLang("fex.cotp.clearingHouse");
                break;
            case "13":
                cotpTypeName = multiLangServiceI.getLang("fex.cotp.insider");
                break;
            case "14":
                cotpTypeName = multiLangServiceI.getLang("fex.cotp.issuerAcceptor");
                break;
            case "15":
                cotpTypeName = multiLangServiceI.getLang("fex.cotp.taxAuthority");
                break;
        }
        return cotpTypeName;
    }

    /**
     * 根据交易对手类别名称查找交易对手类别
     * @param cotpTypeName
     * @return
     */
    public String getCotpTypeNameByName(String cotpTypeName){
        Map<String,String> cotpCode = new HashMap<>();
        for(int i = 1; i < 16; i++){
            String cotpTemp = "";
            String name = "";
            if(i < 10){
                cotpTemp = "0" + i;
            }else{
                cotpTemp = String.valueOf(i);
            }
            name = this.getCotpTypeName(cotpTemp);
            if(StringUtils.isNotEmpty(name)){
                cotpCode.put(name,cotpTemp);
            }
        }
        List<String> nameList = Arrays.asList(cotpTypeName.split(CommonConstant.SEMI_SPLIT));
        String cotpType = "";
        if(!EmptyUtils.isEmptyList(nameList)){
            for(int i = 0; i < nameList.size(); i++){
                String temp = cotpCode.get(nameList.get(i));
                if(!EmptyUtils.isEmpty(temp)){
                    if( i == 0){
                        cotpType = temp;
                    }else{
                        cotpType += "," + temp;
                    }
                }
            }
        }
        return cotpType;
    }

    /**
     * 返回当前数据及组内数据
     * @param params
     * @return
     */
    public List<FexCotpVO> listCotpAndChild(Map<String,Object> params){
        Object id = params == null ? null : params.get("id");
        if (id == null || StringUtils.isBlank(id.toString())) {
            return listExternalCorpAsCotp();
        }
        FexCotpVO vo = getFexcotpById(id.toString());
        return vo == null ? Collections.<FexCotpVO>emptyList() : Collections.singletonList(vo);
    }

    @Override
    public void flowStarted(IWorkFlowResult workFlowResult) {

    }

    @Override
    public ExcelVerifyHanlderResult verifyHandler(FexCotpVO cotpVO) {
        List<String> msgList = new ArrayList<>();
        //交易对手名称不能为空
        if (EmptyUtils.isEmpty(cotpVO.getCotpName())) {
            msgList.add("fex.cotp.cotpname.null");//交易对手名称不能为空
        }
        //正式名称不能为空
        if (EmptyUtils.isEmpty(cotpVO.getOfficialName())) {
            msgList.add("fex.cotp.officialname.null");//正式名称不能为空
        }
        //所属组
        if(!EmptyUtils.isEmpty(cotpVO.getParentName())){
            Map<String, Object> params = new HashMap<>();
            params.put("name", cotpVO.getParentName());
            List<FexCotpVO> cotpList = fexCotpDao.getCotpByName(params);
            if(EmptyUtils.isEmptyList(cotpList)){
                msgList.add("fex.cotp.parentname.noexists");//所属组在系统中不存在
            }else{
                cotpVO.setParentId(cotpList.get(0).getId());
            }
        }

        //交易对手类别
        if(!EmptyUtils.isEmpty(cotpVO.getCotpTypeName())){
            String cotpType = this.getCotpTypeNameByName(cotpVO.getCotpTypeName());
            if(!EmptyUtils.isEmpty(cotpType)){
                List<String> nameList = Arrays.asList(cotpVO.getCotpTypeName().split(CommonConstant.SEMI_SPLIT));
                List<String> cotpTypeList = Arrays.asList(cotpType.split(CommonConstant.COMMA_SPLIT));
                if(!EmptyUtils.isEmptyList(nameList) && !EmptyUtils.isEmptyList(cotpTypeList) && nameList.size() != cotpTypeList.size()){
                    msgList.add("fex.cotp.cotptypename.noexists");//交易对手类别系统中不存在
                }else{
                    cotpVO.setCotpType(this.getCotpTypeNameByName(cotpVO.getCotpTypeName()));
                }
            }else{
                msgList.add("fex.cotp.cotptypename.noexists");//交易对手类别系统中不存在
            }
        }else{
            msgList.add("fex.cotp.cotptypename.null");//交易对手类别不能为空
        }
        //国家/地址
        try {
            if (EmptyUtils.isEmpty(cotpVO.getCountryName()) || EmptyUtils.isEmpty(cotpVO.getAddressName())) {
                throw new ParameterException(ForeignMessageEnum.FEX_COTP_COUNTRY_ADDRESS_NULL);//国家地区和地点不能为空
            }
            SysCountryCustDTO countryDTO = sysCountryCustService.getByNameZh(cotpVO.getCountryName(), CommonConstant.NO);
            if (EmptyUtils.isEmpty(countryDTO) || EmptyUtils.isEmpty(countryDTO.getId())) {
                throw new ParameterException(ForeignMessageEnum.FEX_COTP_COUNTRY_ADDRESS_NOEXISTS);//国家地区和地点系统中不存在
            }
            if ("中国".equals(cotpVO.getCountryName())) {
                RegionDTO cityDTO = regionService.getByName(cotpVO.getAddressName(), Boolean.FALSE);
                if (EmptyUtils.isEmpty(cityDTO) || EmptyUtils.isEmpty(cityDTO.getId())) {
                    throw new ParameterException(ForeignMessageEnum.FEX_COTP_COUNTRY_ADDRESS_NOEXISTS);//国家地区和地点系统中不存在
                }

                cotpVO.setCity(cityDTO.getId());
                cotpVO.setProv(cityDTO.getParentId());
            } else {
                // 地区
                SysCountryCustDTO countryByName = sysCountryCustService.getByNameZh(cotpVO.getCountryName(), CommonConstant.YES);
                if (EmptyUtils.isEmpty(countryByName) || EmptyUtils.isEmpty(countryByName.getId())) {
                    throw new ParameterException(ForeignMessageEnum.FEX_COTP_COUNTRY_ADDRESS_NOEXISTS);//国家地区和地点系统中不存在
                }
                cotpVO.setAddress(countryByName.getId());
            }
            cotpVO.setCountryId(countryDTO.getId());
        } catch (ParameterException e) {
            msgList.add(e.getIMessageEnum().getCode());
        }

        //行号
        if(!EmptyUtils.isEmpty(cotpVO.getBankCode())){
            InputBankInfoDTO bankDTO = inputBankInfoService.getByBankCode(cotpVO.getBankCode());
            if(EmptyUtils.isEmpty(bankDTO)){
                msgList.add("fex.cotp.bankcode.noexists");//行号系统中不存在
            }
        }

        if (EmptyUtils.isEmptyList(msgList)) {
            return new ExcelVerifyHanlderResult(true, msgList);
        } else {
            return new ExcelVerifyHanlderResult(false, msgList);
        }
    }

    @Override
    public void verifyHandler(List<FexCotpVO> list) {

    }

    /**
     * 导入
     *
     * @param list
     */
    public void saveImport(List<FexCotpVO> list, ICurrentUser user) {
        for (FexCotpVO cotpVO : list) {
            FexCotp fc = new FexCotp();
            BeanUtils.copyNotNullProperties(cotpVO, fc);
            fc.setCorpId(user.getCorpId());
            fc.setCotpLevel(ForeignConstant.FEX_COTP_LEVEL_1);
            fc.setStatus(ForeignConstant.STATUE_TEMP);
            fc.setCreateBy(user.getId());
            fc.setCreateDate(DateUtils.getDate());
            fexCotpDao.saveOrUpdate(fc);
            Integer messageType = FexMessageConstant.MESSAGE_TYPE_TEMP;
            String message = FexMessageConstant.FEX_LOG_ADD_TEMPSAVE_SUCC;
            if(user !=null) {
                //生成日志
                fexMessageService.saveMessage(fc.getId(), messageType, message, FexMessageConstant.BUS_TYPE_FEX_COTP, user);
            }
        }
    }

    public List<FexCotpVO> listByOppId(String oppId) {
        FexCotpVO vo = getFexcotpById(oppId);
        return vo == null ? Collections.<FexCotpVO>emptyList() : Collections.singletonList(vo);
    }


    /**
     * 获取交易对手数据（层级是交易对手）
     * @return
     */
    public DataTablesOutput getCotpDataTable(DataTablesInput input, ICurrentUser currentUser) {
        return buildCotpOutput(input, listExternalCorpAsCotp());
    }


    /**
     * 查询所有交易对手数据
     * @return
     */
    public List<FexCotpVO> listCotpNoGroup() {
        List<FexCotpVO> fexCotpVOS = listCotp();
        if (EmptyUtils.isNotEmpty(fexCotpVOS)) {
            fexCotpVOS = fexCotpVOS.stream().filter(vo -> CommonConstant.YES.equals(vo.getCotpLevel()))
                    .collect(Collectors.toList());
        }
        return fexCotpVOS;
    }

    private List<FexCotpVO> listExternalCorpAsCotp() {
        List<ExternalCorpDTO> list = externalCorpService.queryAll();
        if (EmptyUtils.isEmpty(list)) {
            return Collections.emptyList();
        }
        List<FexCotpVO> result = new ArrayList<>();
        for (ExternalCorpDTO dto : list) {
            FexCotpVO vo = externalCorpToFexCotp(dto);
            if (vo != null) {
                result.add(vo);
            }
        }
        return result;
    }

    private DataTablesOutput<FexCotpVO> buildCotpOutput(DataTablesInput input, List<FexCotpVO> list) {
        List<FexCotpVO> data = list == null ? Collections.<FexCotpVO>emptyList() : list;
        return new DataTablesOutput<FexCotpVO>(input == null ? 0 : input.getDraw(), data, data.size(), data.size());
    }

    /**
     *
     * 客商转交易对手
     *
     * @param dto
     * @return
     */
    private FexCotpVO externalCorpToFexCotp(ExternalCorpDTO dto) {
        if (dto == null) {
            return null;
        }
        FexCotpVO vo = new FexCotpVO();
        vo.setId(dto.getId());
        vo.setOppId(dto.getId());
        vo.setCorpId(dto.getCorpId());
        vo.setCotpName(dto.getName());
        vo.setOfficialName(dto.getName());
        vo.setShortName(dto.getAbbreviate());
        vo.setCotpNameEn(dto.getNameEn());
        vo.setAddressEn(dto.getAddressEn());
        vo.setStreetAddr(dto.getAddress1());
        vo.setBuildingNumber(dto.getAddress2());
        vo.setState(dto.getProvinceState());
        vo.setTel(dto.getTel());
        vo.setCotpLevel(ForeignConstant.FEX_COTP_LEVEL_1);
        vo.setCotpType(StringUtils.isNotBlank(dto.getExternalType()) ? dto.getExternalType() : "02");
        vo.setStatus(dto.getStatus());
        fillFexCotpArea(vo, dto);
        return vo;
    }

    /**
     *
     * 补充国家区域信息
     *
     * @param vo
     * @param dto
     */
    private void fillFexCotpArea(FexCotpVO vo, ExternalCorpDTO dto) {
        if (Integer.valueOf(1).equals(dto.getIsNative())) {
            fillNativeFexCotpArea(vo, dto);
            return;
        }
        vo.setCountryId(dto.getProvince());
        vo.setAddress(dto.getCity());
        SysCountryCustDTO country = getCountry(dto.getProvince());
        if (country != null) {
            vo.setCountryName(country.getNameZh());
            vo.setCountryNameEn(country.getNameEn());
            vo.setEnCode(country.getEnCode());
        }
        SysCountryCustDTO address = getCountry(dto.getCity());
        if (address != null) {
            vo.setAddressName(address.getNameZh());
            vo.setAddressNameEn(address.getNameEn());
        }
    }

    private void fillNativeFexCotpArea(FexCotpVO vo, ExternalCorpDTO dto) {
        SysCountryCustDTO china = sysCountryCustService.getByEnCode(BdlinkConstant.COUNTRY_CODE_CHINA);
        if (china != null) {
            vo.setCountryId(china.getId());
            vo.setCountryName(china.getNameZh());
            vo.setCountryNameEn(china.getNameEn());
            vo.setEnCode(china.getEnCode());
        }
        vo.setProv(dto.getProvince());
        vo.setCity(dto.getCity());
        vo.setAddress(dto.getCity());
        RegionDTO prov = getRegion(dto.getProvince());
        if (prov != null) {
            vo.setProvName(prov.getName());
        }
        RegionDTO city = getRegion(dto.getCity());
        if (city != null) {
            vo.setCityName(city.getName());
            vo.setAddressName(city.getName());
        }
    }

    private SysCountryCustDTO getCountry(String id) {
        if (StringUtils.isBlank(id) || sysCountryCustService == null) {
            return null;
        }
        return sysCountryCustService.getById(id);
    }

    private RegionDTO getRegion(String id) {
        if (StringUtils.isBlank(id) || regionService == null) {
            return null;
        }
        return regionService.getById(id);
    }
}
