package com.bytter.biz.inneraccount.service;

import com.bytter.biz.finacial.api.constant.CloseConstant;
import com.bytter.biz.finacial.api.service.IFcAccSummonsService;
import com.bytter.biz.finacial.api.service.IFcCarryService;
import com.bytter.biz.finacial.api.vo.FcCarryVO;
import com.bytter.biz.finacial.api.vo.VoucherInfoVO;
import com.bytter.biz.inneraccount.dao.FcAccSummonsDao;
import com.bytter.biz.inneraccount.vo.FcAccSummonsVO;
import com.bytter.biz.netbank.service.FcVoucherTypeService;
import com.bytter.biz.payment.api.dto.FcVoucherTypeDTO;
import com.bytter.core.base.AbstractService;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.constant.ResourceConstant;
import com.bytter.core.utils.BeanUtils;
import com.bytter.core.utils.SqlUtil;
import com.bytter.core.utils.StringUtils;
import com.bytter.system.api.IBtBankAccService;
import com.bytter.system.api.ICorpService;
import com.bytter.system.api.IDictService;
import com.bytter.system.api.IUserService;
import com.bytter.system.api.dto.BtBankAccDTO;
import com.bytter.system.api.dto.FcAccDTO;
import com.bytter.system.api.dto.UserDTO;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
@Service("fcAccSummonsService")
@Transactional
public class FcAccSummonsService extends AbstractService {
    @DubboReference(check = false)
    IBtBankAccService btBankAccService;
    @Autowired
    private FcAccManageService fcAccService;
    @Autowired
    private FcVoucherTypeService fcVoucherTypeService;
    @Autowired
    @DubboReference(check = false)
    private ICorpService corpService;
    @DubboReference(check = false)
    private IFcAccSummonsService fcAccSummonsServiceImpl;
    @DubboReference
    private IUserService userService;
    @DubboReference
    private IFcCarryService fcCarryService;

    @Autowired
    private FcAccSummonsDao fcAccSummonsDao;

    @DubboReference(check = false)
    private IDictService dictService;
    /**
     * 查询内传票凭证列表
     *
     * @param dataTablesInput
     * @return 没有被调用，注释
     */
    public DataTablesOutput getDataTablesUnion(DataTablesInput dataTablesInput, ICurrentUser userVO) {
        DataTablesOutput<FcAccSummonsVO> dataTablesUnion = fcAccSummonsDao.getDataTablesUnion(dataTablesInput);
        Map<String,String> map = new HashMap<>();
        dataTablesUnion.getData().forEach( s -> {
                if(!map.containsKey(s.getVoucherType())){
                    map.put(s.getVoucherType(),dictService.get("summons.voucherType",s.getVoucherType()));
                }
                if(StringUtils.isNotBlank(s.getVoucherType())){
                    s.setVoucherTypeName(map.get(s.getVoucherType()));
                }
        });
        return dataTablesUnion;
    }
    /**
     * 根据内部账号查找凭证的银行信息
     *
     * @param account
     * @return
     */
    public List getBank(String account, String curId) {
        List<BtBankAccDTO> list = btBankAccService.getBankAccByCountAcc(account, curId);
        List<Map<String, String>> bankInfo = new ArrayList<>();
        for (BtBankAccDTO bankAcc : list) {
            Map<String, String> bankMap = new HashMap<>();
            bankMap.put("bankAcc", bankAcc.getBankAcc());
            bankMap.put("bankName", bankAcc.getBankName());
            bankInfo.add(bankMap);
        }
        return bankInfo;
    }
    /**
     * 查询凭证详细信息
     *
     * @param vou
     * @return 没有被调用，注释
     */
    public List<VoucherInfoVO> getInfo(VoucherInfoVO vou) {
        //查询信贷
        List<VoucherInfoVO> infoList = fcAccSummonsServiceImpl.getVoucherInfo(vou);
        //查询内部账号
        List<String> accList = infoList.stream()
                .map(VoucherInfoVO::getAccount)
                .collect(Collectors.toList());
        //查询所有账号基本信息
        List<FcAccDTO> accDTOs = fcAccService.getAccSet(accList);
        Map<String, FcAccDTO> fcAccMap = accDTOs.stream()
                .collect(Collectors.toMap(FcAccDTO::getBlnctrAcc, Function.identity()));
        // 查询结算方式
        Map<String ,FcVoucherTypeDTO> voucherTypeMap=fcVoucherTypeService.getFcVoucherTypeIdMap();
        //查询用户
        List<UserDTO> userList = userService.listAllUserByUnDelete();
        Map<String ,String> userMap=userList.stream()
                .collect(Collectors.toMap(UserDTO::getId, UserDTO::getUsername));
        //重新组装vo
        for (VoucherInfoVO vo : infoList) {
            vo.setOpName(userMap.get(vo.getOperatorCode()));
            vo.setCheckName(userMap.get(vo.getCheckerCode()));
            FcAccDTO fcacc = fcAccMap.get(vo.getAccount());
            vo.setCurId(fcacc.getCurId());
            vo.setSubjectId(fcacc.getSubjectId());
            vo.setSubjectName(fcacc.getSubjectName());
            vo.setCorpId(fcacc.getCorpId());
            vo.setCorpName(fcacc.getCorpName());
            vo.setCode(fcacc.getCorpCode());
            vo.setNetId(fcacc.getNetId());
            vo.setNetName(fcacc.getNetName());
           // vo.setType(fcacc.getcorptype);
            vo.setBalanceWay(fcacc.getBalanceWay());
            vo.setVoucherName(voucherTypeMap.get(vo.getVoucherType()).getVoucherName());
            vo.setSysVoucherCode(voucherTypeMap.get(vo.getVoucherType()).getSysVoucherCode());
            if("1".equals(vo.getManageName())){
                FcCarryVO fcCarry=new FcCarryVO();
                fcCarry.setOperatorCode(vo.getOperatorCode());
                fcCarry.setCarryType(CloseConstant.CarryType.CARRY_TYPE_DAY_OVER.getValue() );
                fcCarry.setNetId(vo.getNetId());
                fcCarry.setVoucherStartDate(vo.getTallyDate());
                fcCarry.setVoucherEndDate(vo.getTallyDate());
                List<FcCarryVO> fcCarryVOList = fcCarryService.getFcCarryList(fcCarry);
                if(null != fcCarryVOList && fcCarryVOList.size()>0){
                    vo.setManageName(vo.getOpName());
                }
            }
        }
        return infoList;
    }
    /**
     * 查询凭证详细信息
     *
     * @param vou
     * @return 没有被调用，注释
     */
    public List<VoucherInfoVO> getVoucherInfo(VoucherInfoVO vou) {
        List<VoucherInfoVO> listInfo = getInfo(vou);
        List<Map<String, Object>> bankInfo = null;
        VoucherInfoVO info = null;
        List<VoucherInfoVO> voucherInfo = new ArrayList<VoucherInfoVO>();
        for (VoucherInfoVO vo : listInfo) {
            info = new VoucherInfoVO();
            BeanUtils.copyProperties(vo, info);
            bankInfo = getBank(vo.getAccount(), vo.getCurId());
            // 如果记录不止一条，则无效
            if (bankInfo.size() > 0 && bankInfo.size() < 2) {
                for (Map<String, Object> m : bankInfo) {
                    for (String k : m.keySet()) {
                        if ("bank_acc".equalsIgnoreCase(k)) {
                            info.setBankAcc(m.get(k).toString());
                        }
                        if ("bank_name".equalsIgnoreCase(k)) {
                            info.setBankName(m.get(k).toString());
                        }
                    }
                }
            } else {
                info.setBankAcc("");
                info.setBankName("");
            }
            voucherInfo.add(info);
        }
        return voucherInfo;
    }
}
