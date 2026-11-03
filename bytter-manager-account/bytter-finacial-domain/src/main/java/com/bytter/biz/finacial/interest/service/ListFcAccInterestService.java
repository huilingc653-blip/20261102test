package com.bytter.biz.finacial.interest.service;
import cn.hutool.core.collection.CollectionUtil;
import com.bytter.biz.finacial.api.constant.InterestConstant;
import com.bytter.biz.finacial.api.constant.SubjectConstant;
import com.bytter.biz.finacial.interest.dao.ListFcAccInterestDao;
import com.bytter.biz.finacial.interest.entity.FcInterestDtl;
import com.bytter.biz.finacial.interest.vo.ListFcAccInterestVO;
import com.bytter.biz.payment.api.constant.InnerAccountConstant;
import com.bytter.cloud.common.core.constant.CommonConstant;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.utils.*;
import com.bytter.finance.api.ICmsLoanService;
import com.bytter.finance.dto.CmsLoanRepayMoneyDTO;
import com.bytter.system.api.*;
import com.bytter.system.api.dto.FcAccDTO;
import com.bytter.system.api.dto.FcAccSubjectDTO;
import com.google.common.collect.Lists;
import org.apache.commons.collections.map.HashedMap;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
@Service("listFcAccInterestService")
@Transactional
public class ListFcAccInterestService extends BaseService<FcInterestDtl,String> {
    @Autowired
    ListFcAccInterestDao listFcAccInterestDao;
    @Autowired(required = false)
    IFcAccService fcAccService;
    @Autowired(required = false)
    IFcSubjectService subjectService;
    @DubboReference(check = false)
    private ICmsLoanService cmsLoanService;
    @DubboReference(check = false)
    private IUserService iUserService;
    @DubboReference(check = false)
    private ISystemForeignService iSystemForeignService;
    @DubboReference(check = false)
    private IFcAccService iFcAccService;
    @Override
    public BaseDao<FcInterestDtl, String> getEntityDao() {
        return listFcAccInterestDao;
    }
    /**
     * 表单数据显示
     * @param dataTablesInput
     * @return
     */
    public DataTablesOutput findlistBySql(DataTablesInput dataTablesInput) {
        Map<String, Object> inputParams = dataTablesInput.getFormdata();
        String netId = String.valueOf(inputParams.get("eqs_netId"));
        String accType = String.valueOf(inputParams.get("ins_accType"));
        String[] corpIds = null;
        if (StringUtils.isNotEmpty(inputParams.get("ins_corpId"))) {
            corpIds = inputParams.get("ins_corpId").toString().split(CommonConstant.COMMA_SPLIT);
        }
        String userId = dataTablesInput.getCurrentUserId();
        String blnctrAccParam= String.valueOf(inputParams.get("likes_blnctrAcc"));

        String gedInterestTime = String.valueOf(inputParams.get("ged_interestTime"));
        String ledInterestTime = String.valueOf(inputParams.get("led_interestTime"));
        String gedInterestStartDate = String.valueOf(inputParams.get("ged_interestStartDate"));
        String ledInterestStartDate = String.valueOf(inputParams.get("led_interestStartDate"));

        List<String> blnctrAccLists = Lists.newArrayList();
        /*
         * 如果前端查询某个账号，则不需要重新查询其他账号；只查这个账号的数据，否则dao层拼sql为   d.account like '%45645645%' and d.account in (:blnctrAccLists)
         */
        /*if (StringUtils.isBlank(blnctrAccParam) && StringUtils.isNotBlank(accType)){
            blnctrAccLists = iFcAccService.getBlnctrAccByNetIdAndAccType(netId, accType);
            if (ListUtils.isNullOrEmpty(blnctrAccLists)) {
                //查询当前用户下的所有内部账号
                List<FcAccDTO> fcAccDTOS = iFcAccService.getBlnctrAccByUserId(userId);
                blnctrAccLists = fcAccDTOS.stream().distinct().map(FcAccDTO::getBlnctrAcc).collect(Collectors.toList());
            }
        }
        if (StringUtils.isNotBlank(blnctrAccParam) && StringUtils.isNotBlank(accType)){
            blnctrAccLists = iFcAccService.getBlnctrAccByNetIdAndAccType(netId, accType);
            if (!blnctrAccLists.contains(blnctrAccParam)){
                blnctrAccLists = Lists.newArrayList();
            }
        }
        if (StringUtils.isBlank(blnctrAccParam) && StringUtils.isBlank(accType)){
            //查询当前用户下的所有内部账号
            List<FcAccDTO> fcAccDTOS = iFcAccService.getBlnctrAccByUserId(userId);
            blnctrAccLists = fcAccDTOS.stream().distinct().map(FcAccDTO::getBlnctrAcc).collect(Collectors.toList());
        }*/

        List<ListFcAccInterestVO> total = null;
        if (StringUtils.isBlank(netId) && StringUtils.isBlank(accType) && StringUtils.isBlank(blnctrAccParam) && (corpIds == null )
                && StringUtils.isBlank(gedInterestTime) && StringUtils.isBlank(ledInterestTime)
                && StringUtils.isBlank(gedInterestStartDate) && StringUtils.isBlank(ledInterestStartDate)){
            //没有任何条件查询时，blnctrAccLists 不生效
            total = listFcAccInterestDao.getTotal(dataTablesInput, blnctrAccLists);
        }else {
            if (StringUtils.isNotBlank(gedInterestTime) || StringUtils.isNotBlank(ledInterestTime) ||
                StringUtils.isNotBlank(gedInterestStartDate) || StringUtils.isNotBlank(ledInterestStartDate)){
                //任何一个日期范围不为空时，blnctrAccLists 不生效
                total = listFcAccInterestDao.getTotal(dataTablesInput, blnctrAccLists);
            }else {
                //netId,corpIds,accType,blnctrAccParam 其中至少有一个查询条件时，此时 和下面的 getTotal里面的日期是and的关系
                blnctrAccLists = iFcAccService.getFcAccByLikeBlnctrAccAndNetIdAndCorpIdAndAccType(blnctrAccParam,netId,corpIds,accType);
                if (ListUtils.isNullOrEmpty(blnctrAccLists)) {
                    return new DataTablesOutput();
                }
                total = listFcAccInterestDao.getTotal(dataTablesInput, blnctrAccLists);
            }

        }
        if (ListUtils.isNullOrEmpty(total)) {
            return new DataTablesOutput();
        }

        Integer page = (dataTablesInput.getStart() + dataTablesInput.getLength()) / dataTablesInput.getLength();
        ListFcAccInterestVO listFcAccInterestVO = total.get(page-1);
        dataTablesInput.setStart(0);
        //TODO 这里的SQL逻辑还是有问题 和上面的 total没有关联起来
        DataTablesOutput output = listFcAccInterestDao.findlistBySql(dataTablesInput,listFcAccInterestVO.getBlnctrAcc(),listFcAccInterestVO.getOrderNo());
        List<ListFcAccInterestVO> data = output.getData();
        if(CollectionUtil.isEmpty(data)){
            return output;
        }

        List<ListFcAccInterestVO> voList = new ArrayList<>();
        List<String> accounts = new ArrayList<>();
        for (Object datum : data) {
            ListFcAccInterestVO vo = (ListFcAccInterestVO)datum;
            voList.add(vo);
            accounts.add(vo.getBlnctrAcc());
        }
        //同一个 account，取其一即可
        List<FcAccSubjectDTO> accAndSubjectAndCurData = iFcAccService.getAccAndSubjectAndCurData(null, Lists.newArrayList(accounts.get(0)));
        FcAccSubjectDTO fcAccSubjectDTO = accAndSubjectAndCurData.get(0);
        voList.forEach(a -> {
            a.setSubjectId(fcAccSubjectDTO.getSubjectId());
            a.setSubjectName(fcAccSubjectDTO.getSubjectName());
            a.setAccName(fcAccSubjectDTO.getAccName());
            a.setCurName(fcAccSubjectDTO.getCurName());
            a.setNetId(fcAccSubjectDTO.getNetId());
        });
        output.setData(voList);
//        手动设置页数
        output.setRecordsTotal(total.size() * dataTablesInput.getLength());
        output.setRecordsFiltered(total.size() * dataTablesInput.getLength());
        return output;
    };
    /**
     * 通过数据获取FcAcccVO
     * @param id
     * @return
     */
    public List<ListFcAccInterestVO> getFcInterestDtl(String id){
        Map<String, Object> params = new HashedMap();
        List<String> ids = new ArrayList<>();
        if (null != id && id.contains(",")) {
            ids.addAll(Arrays.asList(id.split(",")));
            params.put("id", ids);
        } else {
            ids.add(id);
            params.put("id",ids);
        }
        /*List<ListFcAccInterestVO> listFcAccInterestVOS = listFcAccInterestDao.getFcAccInterestDtl(params);
        setFcAccInterestRepayMoney(listFcAccInterestVOS);
        return listFcAccInterestVOS;*/
        return this.getFcAccVO(params);
    }
    /**
    * <br>
    * <b>功能：</b> 设置贷款户金额和还款金额 <br>
    * <b>作者：</b>liyh<br>
    * <b>日期：</b> 2021/3/13 0013 <br>
    */
    public void setFcAccInterestRepayMoney(List<ListFcAccInterestVO> listFcAccInterestVOS) {
        for (ListFcAccInterestVO listFcAccInterestVO : listFcAccInterestVOS) {
            //计算放款户的放款金额及扣息活期户
            String accType = listFcAccInterestVO.getAccType();//开户种类
            String account = listFcAccInterestVO.getBlnctrAcc();//账户
            if(InnerAccountConstant.FCACC_LOAN.equals(accType)){//放款户
                //查询放款合同金额、已还金额、所有已还金额、剩余金额
                Date blnEndDate = listFcAccInterestVO.getBlnInstDate();//止息日期
                List<CmsLoanRepayMoneyDTO> repayMoneyDTOs = cmsLoanService.queryLoanRepayMoneyByLoanAcc(account, DateUtils.addDay(blnEndDate, 1));
                CmsLoanRepayMoneyDTO repayMoneyDTO;
                if(EmptyUtils.isNotEmpty(repayMoneyDTOs)){
                    repayMoneyDTO = repayMoneyDTOs.get(0);//还款数据
                    listFcAccInterestVO.setRepayMoneyDTO(repayMoneyDTO);
                } else {
                    repayMoneyDTO = new CmsLoanRepayMoneyDTO();
                    repayMoneyDTO.setLoanAcc(account);
                    repayMoneyDTO.setLoanAccName(listFcAccInterestVO.getAccName());
                }
                //查找该单位的活期账号
                Set<String> corpSet = new HashSet<>();
                corpSet.add(listFcAccInterestVO.getCorpId());
                //获取放款账户信息
                FcAccDTO fcAccDTO = fcAccService.getAccDTOByAccount(account);
                //扣息账号默认取放款账号对应的扣息账号，如果未配置则取单位开设的企业活期账号
                FcAccDTO interestAcc = null;
                if(StringUtils.isNotBlank(fcAccDTO.getAccForDeduct())){
                    //放款账号对应的扣息账号
                    interestAcc = fcAccService.getAccDTOByAccount(fcAccDTO.getAccForDeduct());
                } else {
                    List<FcAccDTO> interestAccs = fcAccService.getFcAccBySysSubjectId(SubjectConstant.SysSubject.INTEREST_EXPENSES_CURRENT_DEPOSIT.get(),
                            listFcAccInterestVO.getNetId(), corpSet, listFcAccInterestVO.getCurId());
                    if (EmptyUtils.isNotEmpty(interestAccs)) {
                        interestAcc = interestAccs.get(0);
                    }
                }
                if(interestAcc != null) {
                    repayMoneyDTO.setInterestAcc(interestAcc.getBlnctrAcc());
                    repayMoneyDTO.setInterestAccName(interestAcc.getAccName());
                }
                repayMoneyDTO.setLeftMoney(repayMoneyDTO.getLoanMoney().subtract(repayMoneyDTO.getSumRepayMoney()));//剩余金额
                listFcAccInterestVO.setRepayMoneyDTO(repayMoneyDTO);
            }
            String borrow = "", loan = "";
            if (InterestConstant.INTEREST_SIGN_IST.equals(listFcAccInterestVO.getInterestSign())) {
                //结息
                borrow = InterestConstant.INTEREST_ACCTYPE_VOUCHER_MAPPING.get(listFcAccInterestVO.getAccType())[0];
                loan = InterestConstant.INTEREST_ACCTYPE_VOUCHER_MAPPING.get(listFcAccInterestVO.getAccType())[1];
            } else if (InterestConstant.INTEREST_SIGN_PRE.equals(listFcAccInterestVO.getInterestSign())) {
                //预提
                borrow = InterestConstant.PREDRAW_ACCTYPE_VOUCHER_MAPPING.get(listFcAccInterestVO.getAccType())[0];
                loan = InterestConstant.PREDRAW_ACCTYPE_VOUCHER_MAPPING.get(listFcAccInterestVO.getAccType())[1];
            } else {
                borrow = InterestConstant.INTEREST_ACCTYPE_VOUCHER_MAPPING.get(listFcAccInterestVO.getAccType())[0];
                loan = InterestConstant.INTEREST_ACCTYPE_VOUCHER_MAPPING.get(listFcAccInterestVO.getAccType())[1];
            }
            if(-1 != borrow.indexOf(" ") ) {
                borrow = borrow.substring(borrow.indexOf(" "));
            }
            if(-1 != loan.indexOf(" ") ) {
                loan = loan.substring(loan.indexOf(" "));
            }
            listFcAccInterestVO.setBorrow(borrow);
            listFcAccInterestVO.setLoan(loan);
        }
    }
    /**
     * 通过accName返回借贷对方科目的类型
     * @param params
     * @return
     */
    public List<ListFcAccInterestVO> getFcAccVO(Map<String, Object> params){
        //params.put("accName", accName);
        List<ListFcAccInterestVO> list = listFcAccInterestDao.getFcAccInterestDtl(params);
        ArrayList<String> userIds01 = new ArrayList<>();
        ArrayList<String> userIds02 = new ArrayList<>();
        ArrayList<String> blnctrAccLists = new ArrayList<>();
        list.forEach( a -> {
            userIds01.add(a.getCheckerCode());
            userIds02.add(a.getOperatorCode());
            blnctrAccLists.add(a.getBlnctrAcc());
        });
        Map<String, String> username01Map = iUserService.getUserNameByIds(userIds01);
        Map<String, String> username02Map = iUserService.getUserNameByIds(userIds02);
        List<FcAccSubjectDTO> fcAccSubjectDTOS = iFcAccService.getAccAndSubjectAndCurData(params, blnctrAccLists);
        Map<String, FcAccSubjectDTO> accountMap = fcAccSubjectDTOS.stream().collect(Collectors.toMap(FcAccSubjectDTO::getBlnctrAcc, Function.identity()));
        List<ListFcAccInterestVO> listFcAccInterestVOS=list.stream().filter(a->accountMap.containsKey(a.getBlnctrAcc())).collect(Collectors.toList());
        listFcAccInterestVOS.forEach(a -> {
            a.setCheckerName(username01Map.get(a.getCheckerCode()));
            a.setOperatorName(username02Map.get(a.getOperatorCode()));
            FcAccSubjectDTO fcAccSubjectDTO = accountMap.get(a.getBlnctrAcc());
            a.setAccName(fcAccSubjectDTO.getAccName());
            a.setAccType(fcAccSubjectDTO.getAccType());
            a.setSubjectId(fcAccSubjectDTO.getSubjectId());
            a.setSubjectName(fcAccSubjectDTO.getSubjectName());
            a.setCurName(fcAccSubjectDTO.getCurName());
            a.setNetId(fcAccSubjectDTO.getNetId());
        });
        setFcAccInterestRepayMoney(listFcAccInterestVOS);
        return listFcAccInterestVOS;
    }
}
