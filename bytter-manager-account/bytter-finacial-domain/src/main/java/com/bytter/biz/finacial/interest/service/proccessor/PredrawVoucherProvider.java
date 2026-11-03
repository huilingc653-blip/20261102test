package com.bytter.biz.finacial.interest.service.proccessor;
import com.bytter.biz.finacial.api.constant.SubjectConstant;
import com.bytter.biz.finacial.api.dto.FcFinacialAccbookDTO;
import com.bytter.biz.finacial.api.dto.FcFinacialAccbookDtlDTO;
import com.bytter.biz.finacial.api.dto.FcInterestDtlDTO;
import com.bytter.biz.finacial.interest.bean.AccInterestInfo;
import com.bytter.biz.finacial.interest.bean.InterestParamBean;
import com.bytter.biz.finacial.interest.entity.FcInterestDtlTemp;
import com.bytter.biz.finacial.interest.vo.FcFinacialAccbookDtlVO;
import com.bytter.biz.finacial.interest.vo.FcFinacialAccbookVO;
import com.bytter.core.constant.ConstantBean;
import com.bytter.core.utils.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
@Transactional
@Service("interest_PredrawVoucherProvider")
public class PredrawVoucherProvider extends BaseInterestProvider{
    Integer certificateWay = null;
    
    /**创建预提凭证*/
    public List<FcFinacialAccbookDTO> createPredrawVoucher(FcInterestDtlDTO fcInterestDtlDTO, Date tallyDate, String userId,Integer ficateWay) {
        certificateWay = ficateWay;
        List<FcFinacialAccbookDTO> accbookDTOS = new ArrayList<>();
        FcInterestDtlTemp dtlTemp = new FcInterestDtlTemp();
        BeanUtils.copyProperties(fcInterestDtlDTO,dtlTemp);
        List<FcInterestDtlTemp> temps = new ArrayList<>();
        temps.add(dtlTemp);
        InterestParamBean paramBean = new InterestParamBean(true, false,false);
        Map<String, FcFinacialAccbookVO> accbooksMap = createCommonVoucher(temps,tallyDate,userId, paramBean);
        if (accbooksMap != null) {
            List<FcFinacialAccbookVO> vo = new ArrayList<>(accbooksMap.values());
            for(FcFinacialAccbookVO v :vo){
                FcFinacialAccbookDTO bookDTO = new FcFinacialAccbookDTO();
                BeanUtils.copyProperties(v,bookDTO);
                accbookDTOS.add(bookDTO);
                //分录
                List<FcFinacialAccbookDtlVO> dtlVOS = v.getFcAccbookDtls();
                List<FcFinacialAccbookDtlDTO> listDtl = new ArrayList<>();
                for (FcFinacialAccbookDtlVO dtlVO : dtlVOS ) {
                    FcFinacialAccbookDtlDTO bookDtl = new FcFinacialAccbookDtlDTO();
                    BeanUtils.copyProperties(dtlVO,bookDtl);
                    listDtl.add(bookDtl);
                }
                accbookDTOS.get(0).setFcAccbookDtls(listDtl);
            }
        }
        certificateWay = null;
        return accbookDTOS;
    }
    /**
     *贷方系统科目/企业活期存款
     */
    @Override
    public ConstantBean<String> getCreditSysAcc(){
        ConstantBean<String> sysSubject = null;
        if(certificateWay == 1) {
            sysSubject = SubjectConstant.SysSubject.INTEREST_EXPENSES_CURRENT_DEPOSIT;
        }else if(certificateWay == 2){
            sysSubject = SubjectConstant.SysSubject.FIXED_DEPOSIT;
        }
        return sysSubject;
    }
    /**
     * 功能：借方系统科目/应付利息－企业定期存款
     */
    @Override
    public ConstantBean<String> getDebitSysAcc(){
        return  SubjectConstant.SysSubject.INTEREST_PAYABLE_FIXED_DEPOSIT;
    }
    @Override
    public AccInterestInfo calcInterest(AccInterestInfo accInterestInfo) {
        return null;
    }
    @Override
    public void processInterestVoucher(AccInterestInfo accInterestInfo) {
    }
    @Override
    public void createInterestVoucher(AccInterestInfo accInterestInfo) {
    }
}
