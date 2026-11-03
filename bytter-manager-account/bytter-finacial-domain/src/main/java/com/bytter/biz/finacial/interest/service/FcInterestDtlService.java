package com.bytter.biz.finacial.interest.service;
import com.bytter.biz.finacial.api.dto.FcInterestDtlDTO;
import com.bytter.biz.finacial.interest.dao.FcInterestDtlDao;
import com.bytter.biz.finacial.interest.entity.FcInterestDtl;
import com.bytter.biz.finacial.interest.vo.FcInterestDtlVo;
import com.bytter.system.api.*;
import com.bytter.system.api.dto.DictDTO;
import com.bytter.system.api.dto.FcAccSubjectDTO;
import com.bytter.system.api.dto.FcCorpPoolDTO;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.utils.BeanUtils;
import com.bytter.core.utils.EmptyUtils;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
/**
 * @author onlineGenerator
 * @version 1.0
 * @Title: Service
 * @Description: FC_INTEREST_DTL
 * @date 2017-06-23 14:34:54
 */
@Service("fcInterestDtlService")
@Transactional
public class FcInterestDtlService extends BaseService<FcInterestDtl, String> {
    @Autowired
    private FcInterestDtlDao fcInterestDtlDao;
    @DubboReference(check = false)
    private ICorpService corpService;
    @DubboReference(check = false)
    private IFcCorpPoolService fcCorpPoolService;
    @DubboReference(check = false)
    private ISystemForeignService iSystemForeignService;
    @DubboReference(check = false)
    private IFcAccService iFcAccService;
    @Autowired
    private IDictService dictServiceI;
    @Transactional(readOnly = true)
    public DataTablesOutput listAccount(DataTablesInput input, String resource, ICurrentUser user, String isPool) {
        List<String> corpAuth;
        if ("1".equals(isPool)) {
            List<FcCorpPoolDTO> pool = fcCorpPoolService.getAllCorpPool(user, resource);
            corpAuth = new ArrayList<>();
            for (FcCorpPoolDTO p : pool) {
                corpAuth.add(p.getCorpId());
            }
        } else {
            corpAuth = corpService.getUserCorpIds(resource, user.getId());
        }
//        String eqs_accType = (String) input.getFormdata().get("eqs_accType");
//        if (EmptyUtils.isEmpty(eqs_accType)) {
//            input.getFormdata().put("ins_accType", "01,05,03,08");
//        }
        List<FcAccSubjectDTO> fcAccSubjectDTOS = iFcAccService.getAccCurDateByCorpIdsAndPool(corpAuth, isPool);
//        Map<String, FcAccSubjectDTO> accountMap = fcAccSubjectDTOS.stream().collect(Collectors.toMap(FcAccSubjectDTO::getBlnctrAcc, Function.identity()));
        Map<String, FcAccSubjectDTO> accountMap = fcAccSubjectDTOS.stream().collect(Collectors.toMap(FcAccSubjectDTO::getBlnctrAcc, Function.identity(), (key1, key2) -> key2));

        List<String> accounts = fcAccSubjectDTOS.stream().map(FcAccSubjectDTO::getBlnctrAcc).collect(Collectors.toList());
        DataTablesOutput dataTablesOutput = fcInterestDtlDao.listAccount(input, accounts, isPool);
        List data = dataTablesOutput.getData();
        ArrayList<FcInterestDtlVo> fcInterestDtlVos = new ArrayList<>();
        for (Object datum : data) {
            FcInterestDtlVo vo = (FcInterestDtlVo)datum;
            FcAccSubjectDTO fcAccSubjectDTO = accountMap.get(vo.getAccount());
            vo.setAccName(fcAccSubjectDTO.getAccName());
            vo.setCurName(fcAccSubjectDTO.getCurName());
            vo.setInterestSignName(dictServiceI.get("interest.sign",vo.getInterestSign()));
            fcInterestDtlVos.add(vo);
        }
        dataTablesOutput.setData(fcInterestDtlVos);
        return dataTablesOutput;
    }
    /**
     * <b>功能介绍：</b>根据流水号或者账号查询利息明细<br>
     * <b>日期：</b>2017年8月31日<br>
     * <b>作者：</b>caozx<br>
     *
     * @param account
     * @param vourcherNo
     * @return
     * @version 1.0
     */
    @Transactional(readOnly = true)
    public List<FcInterestDtlDTO> getFcInterestDtlDTOByVourcherNo(String account, String vourcherNo) {
        List<FcInterestDtlVo> dlts = fcInterestDtlDao.getFcInterestDtlDTOByVourcherNo(account, vourcherNo);
        List<FcInterestDtlDTO> dtos = BeanUtils.copyListProperties(dlts, FcInterestDtlDTO.class);
        return dtos;
    }
    /**
     * <b>功能介绍：</b>根据流水号账号回单号查询利息明细<br>
     * <b>日期：</b>2017年11月17日<br>
     * <b>作者：</b>caozx<br>
     *
     * @param account
     * @param orderNo
     * @param eReceiptNo
     * @return
     * @version 1.0
     */
    @Transactional(readOnly = true)
    public List<FcInterestDtlDTO> getFcInterestDtlDTOByOrderNo(String account, String orderNo, String eReceiptNo) {
        List<FcInterestDtlVo> dlts = fcInterestDtlDao.getFcInterestDtlDTOByOrderNo(account, orderNo, eReceiptNo);
        List<FcInterestDtlDTO> dtos = BeanUtils.copyListProperties(dlts, FcInterestDtlDTO.class);
        return dtos;
    }
    public BaseDao<FcInterestDtl, String> getEntityDao() {
        return fcInterestDtlDao;
    }
    @Transactional(readOnly = true)
    public List<FcInterestDtlDTO> getFcInterestDtlByAcc(String account, String isPool) {
        if (!EmptyUtils.isNotEmpty(account)) {
            return new ArrayList<>();
        }
        List<FcAccSubjectDTO> fcAccSubjectDTOS = iFcAccService.getAccCurDateByAccountAndPool(account, isPool);
        List<FcInterestDtlDTO> fcInterestDtlByAcc = fcInterestDtlDao.getFcInterestDtlByAcc(account, isPool);
        //计息标志
        Map<String, DictDTO> dictDTOMap = dictServiceI.listMap("interest.sign");
        fcInterestDtlByAcc.forEach( a -> {
            a.setAccName(fcAccSubjectDTOS.get(0).getAccName());
            a.setCurName(fcAccSubjectDTOS.get(0).getCurName());
            if (dictDTOMap.containsKey(a.getInterestSign())){
                a.setInterestSignName(dictDTOMap.get(a.getInterestSign()).getName());
            }
        });
        return fcInterestDtlByAcc;
    }
}
