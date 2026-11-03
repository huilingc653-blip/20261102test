package com.bytter.biz.finacial.interest.service;
import com.bytter.biz.finacial.api.constant.InterestConstant;
import com.bytter.biz.finacial.api.constant.ResourceConstant;
import com.bytter.biz.finacial.api.enums.interest.InterestMessageEnum;
import com.bytter.biz.finacial.api.enums.interest.OperateType;
import com.bytter.biz.finacial.api.service.IFcDayCloseService;
import com.bytter.biz.finacial.interest.dao.FcInterestSingleNodDao;
import com.bytter.biz.finacial.interest.entity.FcInterestDtlTemp;
import com.bytter.biz.finacial.interest.entity.FcInterestSingle;
import com.bytter.biz.finacial.interest.vo.FcInterestSingleVO;
import com.bytter.biz.finacial.interest.vo.InterestResult;
import com.bytter.biz.payment.api.constant.InnerAccountConstant;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.exception.ParameterException;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.system.api.ICorpService;
import com.bytter.system.api.IFcAccService;
import com.bytter.system.api.IMultiLangService;
import com.bytter.system.api.dto.FcAccDTO;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
/**
 * 企业通知存款个别计息Service
 *
 * @author zuoy 2020-12-29
 */
@Service
@Transactional
public class FcInterestSingleNodService extends BaseService<FcInterestSingle, String> {
    @Autowired
    private FcInterestSingleNodDao fcInterestSingleNodDao;
    @DubboReference(check = false)
    private ICorpService corpService;
    @Resource
    private IMultiLangService multiLangServiceI;
    @Autowired(required = false)
    private FcInterestService fcInterestService;
    @DubboReference(check = false)
    private IFcDayCloseService dayCloseService;
    @Autowired(required = false)
    private FcInterestSingleDdaService fcInterestSingleDdaService;
    @DubboReference(check = false)
    private IFcAccService iFcAccService;
    @Override
    public BaseDao<FcInterestSingle, String> getEntityDao() {
        return fcInterestSingleNodDao;
    }
    /**
     * 查询当前登录人管理单位下所有企业活期的个别计息数据
     *
     * @param input       请求对象
     * @param currentUser 当前登录人
     * @return
     */
    public DataTablesOutput dataTable(DataTablesInput input, ICurrentUser currentUser) {
        String opType = (String) input.getFormdata().get("opType");
        String resource = ResourceConstant.FINACIAL_INTEREST_SINGLE_NOD_HAND;
        if (EmptyUtils.isNotEmpty(opType) && opType.equals("checkList")) {
            resource = ResourceConstant.FINACIAL_INTEREST_SINGLE_NOD_CHECK;
        }
        List<String> corpIds = corpService.getUserCorpIds(resource, currentUser.getId());
        DataTablesOutput dataTablesOutput = fcInterestSingleNodDao.queryFcInterestSingleDda(input, currentUser.getId(), corpIds);
        List data = dataTablesOutput.getData();
        ArrayList<String> accounts = new ArrayList<>();
        ArrayList<FcInterestSingleVO> fcInterestSingleVOS = new ArrayList<>();
        for (Object datum : data) {
            FcInterestSingleVO vo = (FcInterestSingleVO) datum;
            accounts.add(vo.getAccount());
            fcInterestSingleVOS.add(vo);
        }
        List<FcAccDTO> fcAccDTOS = iFcAccService.getFcAccByBlnctrAccs(accounts);
        Map<String, FcAccDTO> accountMap = fcAccDTOS.stream().collect(Collectors.toMap(FcAccDTO::getBlnctrAcc, Function.identity()));
        fcInterestSingleVOS.forEach(a -> {
            FcAccDTO fcAccDTO = accountMap.get(a.getAccount());
            a.setCurId(fcAccDTO.getCurId());
            a.setNetId(fcAccDTO.getNetId());
            a.setPoolSign(fcAccDTO.getPoolSign());
        });
        dataTablesOutput.setData(fcInterestSingleVOS);
        return dataTablesOutput;
    }
    /**
     * 个别计息与提交
     *
     * @param entity      个别计息请求参数对象
     * @param currentUser 当前登录人
     */
    public AjaxJson doInterestaccrualBySingle(FcInterestSingleVO entity, ICurrentUser currentUser) {
        AjaxJson json = new AjaxJson();
        try {
            boolean poolFlag = InnerAccountConstant.POOL_SIGN_1.equals(entity.getPoolSign());
            OperateType opType = null;
            if ("interest".equals(entity.getOpType())) {
                opType = OperateType.INTEREST;
            } else if ("submit".equals(entity.getOpType())) {
                opType = OperateType.SUBMIT;
            }
            boolean excute = true;//执行标志 true-执行 false-停止
            FcInterestSingleVO fcInterestSingle = fcInterestSingleDdaService.queryFcInterestSingle(entity.getAccount());
            String itsSuccess = "";
            //界面标志pageFlag 0-新增界面 1-修改界面 默认空也是新增界面
            if (EmptyUtils.isEmpty(entity.getPageFlag())) entity.setPageFlag(0);
            if (entity.getPageFlag() == 0) {
                //判断是否存在个别计息数据，不存在则计息，存在则提示
                if (fcInterestSingle != null) {
                    excute = false;
                    itsSuccess = multiLangServiceI.getLang("finacial.interest.data.exist.account");
                    ;//该账户已存在个别计息数据！
                }
            }
            if (excute) {
                InterestResult interestResult = fcInterestService.interestaccrualByBatch(entity.getNetId(), entity.getAccType(), entity.getTallyDate(),
                        entity.getEndDate(), currentUser, opType, false, poolFlag,
                        entity.getAccount(), InterestConstant.GENERATEMODE_INTEREST_PART, entity.getMoney());
                if (interestResult == null || interestResult.getAccInterestInfo() == null || CollectionUtils.isEmpty(interestResult.getAccInterestInfo().getInterestDtls())) {
                    logger.debug("本计息周期内没有可以计息的数据，不能计息!");
                    throw new ParameterException(InterestMessageEnum.FIN_INTEREST_INTERESTNODATA);//不满足记账凭证条件
                }
                excute = saveOrUpdateFcInterestSingleEntity(entity, interestResult, currentUser);
                if (excute && opType == OperateType.INTEREST) {
                    itsSuccess = multiLangServiceI.getLang("finacial.interest.interest.success");
                } else if (excute && opType == OperateType.SUBMIT) {
                    itsSuccess = multiLangServiceI.getLang("common.message.submit.success");
                }
            }
            json.setSuccess(excute);
            json.setObj(itsSuccess);
            json.setMsg(itsSuccess);
        } catch (Exception e) {
            json.setSuccess(false);
            if (e instanceof ParameterException) {
                ParameterException new_name = (ParameterException) e;
                json.setMessageEnum(new_name.getIMessageEnum());
                json.setParams(new_name.getParams());
                json.setMsg(new_name.getMessage());
                if (InterestMessageEnum.FIN_INTEREST_NOTENOUGH_BALANCE.equals(new_name.getIMessageEnum())) {
                    json.setObj("notBalance");
                }else{
                    throw e;
                }
            }
            e.printStackTrace();
        }
        return json;
    }
    /**
     * 保存或修改个别计息
     *
     * @param entityVo        个别计息对象
     * @param interestResult 计息结果对象
     * @param currentUser    当前登录人
     * @return
     */
    public boolean saveOrUpdateFcInterestSingleEntity(FcInterestSingleVO entityVo, InterestResult interestResult, ICurrentUser currentUser) {
        List<FcInterestDtlTemp> list = interestResult.getAccInterestInfo().getInterestDtls();
        FcInterestDtlTemp fidt = list.get(0);
        BigDecimal interest = new BigDecimal(0.00);
        List<Date> startDateList = new ArrayList<>();
        List<Date> endDateList = new ArrayList<>();
        for (FcInterestDtlTemp entity : list) {
            interest = interest.add(entity.getInterest());
            startDateList.add(entity.getInterestStartDate());
            endDateList.add(entity.getBlnInstDate());
        }
        FcInterestSingle fis = new FcInterestSingle();
        if (EmptyUtils.isNotEmpty(entityVo.getId())) {
            fis = get(entityVo.getId());
        }
        fis.setCorpId(entityVo.getCorpId());
        fis.setInterestTime(fidt.getInterestTime());
        fis.setAccount(fidt.getAccount());
        fis.setAccType(entityVo.getAccType());
        fis.setStartDate(Collections.min(startDateList));
        fis.setEndDate(Collections.max(endDateList));
        fis.setMoney(null);
        fis.setInterest(interest);
        fis.setTallyDate(fidt.getTallyDate());
        fis.setOrderNo(fidt.getOrderNo());
//        fis.setPayWay("");
        fis.setStatus(fidt.getBusStatus());
        fis.setAffixAmount(entityVo.getAffixAmount());
        switch (entityVo.getPageFlag()) {
            case 0:
                fis.setCreateBy(currentUser.getId());
                fis.setCreateDate(new Date());
                break;
            case 1:
                fis.setUpdateBy(currentUser.getId());
                fis.setUpdateDate(new Date());
                break;
            case 2:
                fis.setCheckerCode(currentUser.getId());
                fis.setCheckDate(new Date());
                break;
            case 3:
                break;
            default:
                break;
        }
        FcAccDTO fcAccDTO = iFcAccService.getAccDTOByAccount(fis.getAccount());
        fis.setCurId(fcAccDTO.getCurId());
        fis.setNetId(fcAccDTO.getNetId());
        fis.setPoolSign(fcAccDTO.getPoolSign());
        fis.setAccName(fcAccDTO.getAccName());
        saveOrUpdate(fis);
        return true;
    }
}
