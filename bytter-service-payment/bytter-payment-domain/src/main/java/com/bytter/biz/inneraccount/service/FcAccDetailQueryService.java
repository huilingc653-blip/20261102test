package com.bytter.biz.inneraccount.service;

import com.bytter.biz.finacial.api.dto.FcAccDetailDTO;
import com.bytter.biz.finacial.api.dto.FcAccbookDtlDTO;
import com.bytter.biz.finacial.api.dto.FcAccbookVoucherDTO;
import com.bytter.biz.finacial.api.dto.VoucherWrapperDTO;
import com.bytter.biz.finacial.api.service.IFcAccDetailQueryService;
import com.bytter.biz.finacial.api.service.IFcAccbookService;
import com.bytter.biz.netbank.entity.FcVoucherType;
import com.bytter.biz.netbank.service.FcVoucherTypeService;
import com.bytter.biz.bdlink.api.constant.NetbankConstant;
import com.bytter.biz.bdlink.api.constant.ResourceConstant;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.core.utils.SqlUtil;
import com.bytter.system.api.IAccountGrantService;
import com.bytter.system.api.ICorpService;
import com.bytter.system.api.constant.AccountGrantConstant;
import com.bytter.system.api.vo.FcAccVO;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
 * @author onlineGenerator
 * @version 1.0
 * @Title: Service
 * @Description: 内部账户流水
 * @date 2017-06-23 19:36:12
 */
@Service("fcAccDetailQueryService")
public class FcAccDetailQueryService {
    @DubboReference(check = false)
    private IFcAccDetailQueryService fcAccDetailQueryServiceImpl;
    @DubboReference(check = false)
    private ICorpService corpService;
    @DubboReference(check = false)
    private IAccountGrantService accountGrantService;
    @DubboReference(check = false)
    private IFcAccbookService fcAccbookService;
    @Autowired
    private FcAccManageService fcAccService;
    @Autowired
    private FcVoucherTypeService fcVoucherTypeService;
    /**
     * 查询内部账号流水列表
     *
     * @param
     * @return
     */
    public DataTablesOutput getDataTablesUnion(DataTablesInput dataTablesInput, String userId) {
        StringBuilder joinSql = new StringBuilder();
        //设置账号授权控制
        List<String> accountGrantIdList = accountGrantService.listByMultiValAccs(userId, ResourceConstant.NETBANK_INNERACCOUNT_FCACCDETAIL_QUERY, null,
                AccountGrantConstant.INNER_BANK_TYPE, AccountGrantConstant.QUERY_DETAIL);
        Map<String, Object> params = new HashMap();
        String accountsTemp = SqlUtil.getInSqlByParams(accountGrantIdList, "detail.account", params);
        joinSql.append(" and ").append(accountsTemp);
        DataTablesOutput dataTablesOutput = fcAccDetailQueryServiceImpl.getDataTablesUnion(dataTablesInput, joinSql.toString(), params);
        List<FcAccDetailDTO> fcAccDetailVOList = dataTablesOutput.getData();
        for(FcAccDetailDTO vo:fcAccDetailVOList){
            //账号，科目
            Map<String,Object> param=new HashMap();
            FcAccVO fcAcc =new FcAccVO();
            fcAcc.setBlnctrAcc(vo.getAccount());
            List<FcAccVO> fcAccVOList=fcAccService.getFcAccList( fcAcc, null,param);
            for(FcAccVO accVO:fcAccVOList){
                vo.setAccName(accVO.getAccName());
                vo.setAccType(accVO.getAccType());
                vo.setCorpId(accVO.getCorpId());
                vo.setCurName(accVO.getCurName());
            }
            FcVoucherType fcVoucherType = fcVoucherTypeService.getById(vo.getVoucherType());
            if (null != fcVoucherType ) {
                vo.setVoucherName(fcVoucherType.getVoucherName());
                vo.setSysVoucherCode(fcVoucherType.getSysVoucherCode());
            }
        }
        return dataTablesOutput;
    }
    private DataTablesOutput setInnerExtInfo(DataTablesOutput dataTablesOutput) {
        List<FcAccDetailDTO> list = dataTablesOutput.getData();
        HashMap<String, VoucherWrapperDTO> voucherMap = new HashMap<>();
        for (FcAccDetailDTO vo : list) {
            if (NetbankConstant.PAY_TYPE_05.equals(vo.getSysVoucherCode())
                    || NetbankConstant.PAY_TYPE_37.equals(vo.getSysVoucherCode())) {
                VoucherWrapperDTO voucherWrapperDTO = voucherMap.get(vo.getVoucherNo());
                if (voucherWrapperDTO == null) {
                    voucherWrapperDTO = this.fcAccbookService.getVoucherByVoucherNo(vo.getVoucherNo());
                }
                if (voucherWrapperDTO != null) {
                    List<FcAccbookVoucherDTO> accbookDtls = voucherWrapperDTO.getTarget();
                    List<FcAccbookDtlDTO> bookDtls = accbookDtls.get(0).getBookDtls();
                    //借贷明细超过2条则不显示外单位
                    if (!EmptyUtils.isEmpty(bookDtls) && bookDtls.size() == 2) {
                        for (FcAccbookDtlDTO dtl : bookDtls) {
                            if (vo.getAccount().equals(dtl.getAccount())) {
                                continue;
                            }
                            vo.setExternalAcc(dtl.getAccount());
                            vo.setExternalName(dtl.getAccName());
                        }
                    }
                }
            }
        }
        return dataTablesOutput;
    }
}
